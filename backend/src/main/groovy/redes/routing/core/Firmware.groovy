package redes.routing.core

import groovy.transform.ThreadInterrupt
import groovy.lang.Lazy

import redes.routing.ui.server.Web
import redes.routing.Router

@ThreadInterrupt
class Firmware 
	extends Thread {

    @Lazy
	Properties properties

	// Instance variables -------------------------------------------------------------

		public static Integer port

		private static Firmware instance

		private static Map 

	// --------------------------------------------------------------------------------

	// Start the router firmware
	def static run(String[] args){
		def _instance = getInstance()
		if(args.size() > 0) {
			port = Integer.parseInt(args[0])
		}

		new Web().start()
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
	}

}