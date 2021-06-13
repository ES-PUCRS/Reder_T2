package redes.routing.ui.server.renders

import groovy.text.SimpleTemplateEngine

import redes.routing.ui.server.renders.interfaces.Render
import redes.routing.core.Firmware
import redes.routing.Router

import library.JSON

class APIRender extends Render {
	
	private static final Properties properties = super.importProperties()


	/*
	 *	Shell is the JQuery Script, which not
	 */
	def static health(def map) {
		super.build(['variable': JSON.parse("status", "up")])
	}


	/*
	 *	Call Firmware installer
	 */
	def static install(def map) {
		def response = ""
		def port

		if (map?.get("port"))
			port = Integer.parseInt(map?.get("port")?[0])
		
		try {
			if(map.get("object")[0] == "module")
				response += JSON.parse(
								"port",
								Firmware.getInstance()
										.installModule(port) as String
							)
			else
				response = JSON.parse("error", "Install object was not defined")
		} catch (e) { if(new Boolean(properties."api.debug")) println e.getLocalizedMessage() 
			response = JSON.parse("error", e.getLocalizedMessage()) }

		def binding = [
			'variable' : JSON.verify(response)
		]

		build(binding)
	}


	/*
	 *	Call list firmware objects
	 */
	def static list(def map) {
		def response = ""
		
		try {
			if(map.get("object")[0] == "modules"){
				response += JSON.parse(
								"",
								Firmware
									.getInstance()
									.listModules()
								)
									
				if(response == "[:]")
					response = "\\tThere is no installed module."
				else
					response = response
										?.replaceAll("\\[" 	 , "\\[\\\\n\\\\t ")
										?.replaceAll("\\},"  , "\\}\\\\n\\\\t")
										?.replaceAll(":"   	 , ": ")
										?.replaceAll("\\]"	 , "\\\\n\\]")
										?.replaceAll("\\\""	 , "\\\\\"")
			}

			else if(map.get("object")[0] == "routes"){
				response += Firmware
								.getInstance()
								.listRoutingTable() as String

				if(response == "[:]"){
					response = "\\tThe ip table is empty."
				} else {
					response = response
										?.substring(1)
										?.substring(0, response.length() - 2)
										?.replaceAll("\\],","\\]\\\\n\\\\t")

					response = "[\\n\\t" + response + "\\n]"
				}
			}

			response = JSON.parse("content", response)
		} catch (e) { response = JSON.parse("error", e.getLocalizedMessage()) }
		
		def binding = [
			'variable' : JSON.verify(response)
		]

		super.build(binding)
	}


	/*
	 *	Send message or file to antoher router
	 */
	def static send(def map) {
		
	}


 	/*
	 *	Module Start
	 */
	def static start(def map) {

	}


	/*
	 *	Call wire firmware objects
	 */
	def static wire(def map) {
		def response = ""

		try {
			if(map.get("object")?[0] == "module") {
				response +=
						JSON.parse("error",
							Firmware
								.getInstance()
								.wireModule(
									Integer.parseInt(map.get("index") [0]),
									Integer.parseInt(map.get("target")[0])
								) as String
						)
			} else if (map.get("object")?[0] == "cut") {
				response += 
						JSON.parse("error",
							Firmware
								.getInstance()
								.unwireModule(
									Integer.parseInt(map.get("index")[0])
								) as String
						)
			} else
				JSON.parse("error", "Object was not defined")
		} catch (e) { response = JSON.parse("error", e.getLocalizedMessage()) }
		

		def binding = ['variable': JSON.verify(response)]
		super.build(binding)
	}


	/*
	 *	Module killer objects
	 */
	def static kill(def map) {

	}


	/*
	 *	Kill and remove object from firmware
	 */
	def static remove(def map) {
		
	}

}