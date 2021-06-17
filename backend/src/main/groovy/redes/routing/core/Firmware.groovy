package redes.routing.core

import redes.routing.core.datastructure.Table

import groovy.transform.ThreadInterrupt
import groovy.lang.Lazy

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

    @Lazy
	Properties properties

	// Instance variables -------------------------------------------------------------

		// #DEFINE
		private static final int distance = 0
		private static final int nextHop = 1

		// Local host domain address
		public static InetAddress domain


		// Wired configure connection port
		private static int port

		// Singleton instance holder
		private static Firmware instance

		// Module connection Map<port, socket connection>
		private static Map<Integer, SocketException> modules

		// Rounting list table Map<destination, int[2]> -> [0]distance & [1]next hop
		private static Map<Integer, Integer> localForwarding
		private static Table routingTable

		private static Thread thread
	// --------------------------------------------------------------------------------

	// Start the router firmware
	def static run(String[] args) {
		def _instance = getInstance()
		if(args.size() > 0) {
			port = Integer.parseInt(args[0])
		}

		new Server().start()
	}

	// Singleton access
	def static getInstance() {
		if(!instance)
			instance = new Firmware()
		return instance
	}

	// Singleton constructor
	private Firmware() {
	    this.getClass()
	    	.getResource( Router.propertiesPath )
	    	.withInputStream {
	        	properties.load(it)
	    	}

		domain = InetAddress.getByName(properties."router.domain")
		localForwarding = new HashMap()
		routingTable = new Table()
		modules = new HashMap()

		thread = new Thread(replayer)
		thread.start()
	}


	def installModule(Integer port = null) {
		try {
			do { port = available(port) } while ( !port )
			modules.put(port, new SocketModule( port )) }
		catch (SocketException se) { return se.getLocalizedMessage() }

		port
	}



	def send(int destination, String message) {
		try { 
			modules.get(localForwarding.get(routingTable.getNextHope(destination)))
				   .send(message)
		} catch (e) { return e.getLocalizedMessage() }
	}



	def wireModule(int module, int wire) {
		try {
			modules.get(module)
				   .wire(wire)
		} catch (Exception e) { return e.getLocalizedMessage() }
	}

	def unwireModule(int module) {
		modules.get(module)
			   .unwire()
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

	def listRoutingTable() {
		routingTable.toString().replaceAll("-1", "local")
	}

	protected void routesUpdate(int nextHop, Map table) {
		routesUpdate(
			nextHop,
			table.entrySet()
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
	protected void reroute(int dst, int dist, int nextHop, Integer local = null) {
		// println "${dst} ${dist} ${nextHop}"
		if(routingTable.contains(dst)) {
			Integer[] info = routingTable.get(dst)
			int distance = dist
			if(info[this.distance] < dist)
				distance = info[this.distance]
			routingTable.add(dst, distance, nextHop)
		} else {
			routingTable.add(dst, dist, nextHop)
		}

		if(dist > 0)
			localForwarding.put(nextHop, local)
	}

    private int generatePort() {
    	int begin = Integer.parseInt( properties."router.start.ip" )
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
			while(true) {
				thread.sleep(10000)
				modules.each { port, module ->
					module.rip(port)
				}
			}
		}
	}

}