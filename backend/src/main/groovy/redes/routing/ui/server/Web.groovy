package redes.routing.ui.server

import com.sun.net.httpserver.*
import groovy.lang.Lazy

import redes.routing.ui.server.contexts.*
import redes.routing.core.Firmware
import redes.routing.Router

class Web
	extends Thread {

	@Lazy
	private static Properties properties

	def static importProperties() {
		new Object() {}
	    	.getClass()
	    	.getResource( Router.propertiesPath )
	    	.withInputStream {
	        	properties.load(it)
	    	}
	}

	private Web () { importProperties() }

	public void run() {
		def port = (Firmware.port as int)
		def server = HttpServer.create(new InetSocketAddress(port), 0)

		server.createContext("/", new Home())
		server.createContext("/API", new API())

		server.start()
		println "Web Server started on port ${port}"
	}

}