package redes.routing.core

import groovy.transform.ThreadInterrupt
import groovy.lang.Lazy

import redes.routing.ui.server.Server
import redes.routing.Router

@ThreadInterrupt
class Firmware
	extends Thread {

    @Lazy
	Properties properties

	// Instance variables -------------------------------------------------------------

		public static Integer port

		private static Firmware instance

		private static Map modules

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
		modules = new HashMap()

	    this.getClass()
	    	.getResource( Router.propertiesPath )
	    	.withInputStream {
	        	properties.load(it)
	    	}
	}

    private int generatePort () {
    	int begin = Integer.parseInt( properties."router.start.ip" )
    	return (new Random().nextInt(65353-begin) + begin)
    }

	def installModule(){
		Integer port
		do {
			port = generatePort()
			try { modules.put(port, new SocketModule( generatePort() )) }
			catch (SocketException se) { port = null; se.printStackTrace() }
		} while ( !port )

		port
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
			   .stop()
		modules.remove(module)
	}
}