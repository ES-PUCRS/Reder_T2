package redes.routing.core

import redes.routing.core.datastructure.Table

import groovy.transform.ThreadInterrupt

import java.net.InetAddress

import redes.routing.ui.server.Server
import redes.routing.Router

import java.util.stream.Collectors
import java.util.stream.Stream
import java.util.Collections
import java.util.ArrayList
import java.util.List

@ThreadInterrupt
class Firmware
	extends Thread {

	// Instance variables -------------------------------------------------------------

		// #DEFINE
		private static final int destination = 0
		private static final int metric = 1
		private static final int nextHop = 2

		// Local host domain address
		public static InetAddress domain


		// Wired configure connection port
		private static int port

		// Singleton instance holder
		private static Firmware instance

		// Module connection Map<port, socket connection>
		private static Map<Integer, SocketException> modules

		// Rounting list table Map<destination, int[2]> -> [0]metricance & [1]next hop
		private static Table routingTable

		private static Thread share
		private static Thread flush
	// --------------------------------------------------------------------------------

	// Start the router firmware
	def static run(String[] args) {
		def _instance = getInstance()
		if(args.size() > 0) {
			port = Integer.parseInt(args[0])
		}

		new Server().start()
		share.start()
		flush.start()
	}

	// Singleton access
	def static getInstance() {
		if(!instance)
			instance = new Firmware()
		return instance
	}

	// Singleton constructor
	private Firmware() {
		domain = InetAddress.getByName(Router.properties."router.domain")
		routingTable = new Table()
		modules = new HashMap()

		share = new Thread(replayer)
		flush = new Thread(flusher)
	}


	def installModule(Integer port = null) {
		try {
			do { port = available(port) } while ( !port )
			modules.put(port, new SocketModule( port ))
		} catch (SocketException se) { return se.getLocalizedMessage() }

		port
	}


	def send(int src, Map header) {
		def dst
		try { dst = header.get("dst") as int }
		catch(Exception e) { println "Unreacheable. There is no destine on request." }

		if(!routingTable.contains(dst)) {
			println "Unreachable"
			return
		} else {
			def wrapper = routingTable.get(dst)

			if(wrapper[this.metric] == 0) {
				println header.get("content").replaceAll("%20", " ")
			} else {
				send(dst, header.get("content"), header.get("origin") as int)
			}
		}
	}


	def send(int destination, String message, Integer origin = port) {
		try {
			modules.get(routingTable.getNextHop(destination))
				   .send(message.replaceAll("\\s","%20"), destination, origin)
		} catch (e) { return "Unreacheable" }
	}


	def freeModule() {
		modules.find { port, module ->
			if(!module.getWire()) port
		}
		 ?.getValue()
		 ?.getPort()
	}


	def wireModule(int router) {
		def local = freeModule()
		if(local) {
			try {
				def result = RouterConnection.requestModule(router)
				def target = result?.get("port")
				if(target && target != "null") {
					modules.get(local)
					   	   .wire(target as int)
				} else {
					"The router ${router} does not have any free module to connect with."
				}
			} catch (Exception e) { e.printStackTrace() }
		} else {
			"There is no local free module to connect with ${router}"
		}
	}
	

	def wireModule(int module, int wire) {
		try {
			modules.get(module)
				   .wire(wire)
		} catch (Exception e) { return e.getLocalizedMessage() }
	}

	def unwireModule(int module) {
		try {
			modules.get(module)
				   .unwire()
		} catch(Exception e) { e.printStackTrace() }
	}

	def listModules() {
		modules.toString()
	}
	
	def startModule(int module) {
		modules.get(module)
		       .start()
	}

	def killModule(int module) {
		modules.get(module)
		       .stop()
	}


	def removeModule(int module) {
		try {
			modules.get(module).kill()
			reroute(module)
			modules.remove(module)
		} catch (Exception e) {e.printStackTrace()}
	}

	def getRoutingTable() { routingTable.getList() }
	def getReplyTable() { routingTable.getReplyList() }

	def listRoutingTableAPI() { routingTable }
	def listRoutingTable() {
		routingTable.toString().replaceAll("-1", "local")
							   .replaceAll("16, \\d+", "16, inalcancavel")
	}

	protected void routesUpdate(int nextHop, Map table) {
		try {
		routesUpdate(
			nextHop,
			table?.entrySet()
				.stream()
				.map( entry ->
					Stream.of(
        				entry, new int[] {
        					entry.getKey() as int,
        					entry.getValue() as int
        				}
    				).collect()
				)
				.map(x -> x.get(1))
				.collect() as ArrayList
		)
		} catch (Exception e) { e.printStackTrace() }
	}

	protected void routesUpdate(int nextHop, ArrayList table) {
		routingTable.sync(
			nextHop,
			table,
			modules
				.keySet()
			  	.stream()
			  	.collect(Collectors.toList()) as ArrayList
		)
	}

	protected void reroute(int node) { routingTable.remove(node) }
	protected void reroute(int dst, int metric, int nextHop, Integer local = null) {
		try {
			if(routingTable.contains(dst)) {
				Integer[] info = routingTable.get(dst)
				int distance = info[this.metric]
				if(info[this.metric] > metric || metric == 16)
					distance = metric
				routingTable.add(dst, distance, nextHop)
			} else {
				routingTable.add(dst, metric, nextHop)
			}

			if(metric > 0)
				routingTable.forwarding(nextHop, local)
			else if (metric == 16)
				routingTable.removeForwarding(local)

		} catch (Exception e) { e.printStackTrace() }
	}

	def flushTable(){
		ArrayList flushList = []
		modules.each { port, module ->
			if(!module.getAlive()) {
				flushList.add(port)
			}
			module.resetAlive()
		}
		routingTable.flush(flushList)
	}

    private int generatePort() {
    	int begin = Integer.parseInt( Router.properties."router.start.ip" )
    	return (new Random().nextInt(65353-begin) + begin)
    }

    private Integer available(Integer port = null) {
		if(!port) port = generatePort()

        ServerSocket ss = null
        DatagramSocket ds = null
        try {
            ss = new ServerSocket(port)
            ss.setReuseAddress(true)
            ds = new DatagramSocket(port)
            ds.setReuseAddress(true)
            return port;
        }
        catch (IOException e) { }
        finally {
            if (ds)
                ds.close()

            if (ss)
                try { ss.close() }
                catch (IOException ignored) { }
        }
        return available()
    }


	/* Watch the port waiting for request */
	private Runnable replayer = new Runnable() {
		public void run() {
			int time = Router.properties."router.timer.sharetable" as int
			while(true) {
				share.sleep(time)
				modules?.each { port, module ->
					module.rip(port)
				}
			}
		}
	}

	/* Flush the table removing routes that has not updated */
	private Runnable flusher = new Runnable() {
		public void run() {
			int time = Router.properties."router.timer.flushtable" as int
			while(true) {
				flush.sleep(time)
				flushTable()
			}
		}
	}

}