package redes.routing.core

import groovy.transform.ThreadInterrupt
import groovy.lang.Lazy



@ThreadInterrupt
class Firmware 
	extends Thread {

    @Lazy
	Properties properties

	// Start the router firmware
	def static run(String[] args){
		if(args.size() > 0) {
			println "00"
			// def _instance = getInstance()
			// args.each{ arg -> 
			// 	_instance.startSocket(arg as String)
			// }
		} else {
			println "throw invalid wired(port) error"
		}
	}

	// Singleton access
	def static getInstance() {
		if(!instance)
			instance = new Firmware()
		return instance
	}

	// Singleton constructor

	private Firmware(){
	    this
		    .getClass()
	    	.getResource( VM.propertiesPath )
	    	.withInputStream {
	        	properties.load(it)
	    	}
	}

}