package redes.routing.core

import groovy.transform.ThreadInterrupt
import groovy.lang.Lazy

import java.net.InetAddress

import redes.routing.ui.server.Server
import redes.routing.Router

@ThreadInterrupt
class Firmware
	extends Thread {

    @Lazy
	Properties properties

	// Instance variables -------------------------------------------------------------

		// #DEFINE
		private static final int distance = 0
		private static final int nextHope = 1

		// Local host domain address
		public static InetAddress domain


		// Wired configure connection port
		private static int port

		// Singleton instance holder
		private static Firmware instance

		// Module connection Map<port, socket connection>
		private static Map<Integer, SocketException> modules

		// Rounting list table Map<destination, int[2]> -> [0]distance & [1]next hop
		private static Map<Integer, Integer[]> routingTable
		private static Map<Integer, Integer> wiring

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
		routingTable = new HashMap()
		modules = new HashMap()
		wiring = new HashMap()
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
			modules.get(routingTable.get(destination)[nextHope])
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
		modules.get(module)
			   .kill()
		modules.remove(module)
	}


	def listRoutingTable() {
		String routes = routingTable.toString()
		//TODO -> Formatar o retorno da routing table
		routes
	}

	protected void reroute(int node) {
		routingTable.remove(node)
	}
	protected void reroute(int dst, int dist, int module) {
		if(routingTable.containsKey(dst)) {
			Integer[] info = routingTable.get(dst)
			int distance = dist
			if(info[this.distance] < dist)
				distance = info[this.distance]
			routingTable.put(dst, [distance, module])
		} else {
			routingTable.put(dst, [dist, module])
		}
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

}