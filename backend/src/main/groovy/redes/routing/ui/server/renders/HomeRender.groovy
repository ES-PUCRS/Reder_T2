package redes.routing.ui.server.renders

import groovy.text.SimpleTemplateEngine

import redes.routing.ui.server.renders.interfaces.Render

import redes.routing.core.RouterConnection
import redes.routing.core.Firmware
import redes.routing.Router

import library.JSON

class HomeRender extends Render {
	
	private static final Properties properties = super.importProperties()
	private static final root = new File(properties."ui.views.path")

	/*
	 *	Shell is the JQuery Script, which not
	 *	accepts render patterns.
	 */
	def static index(def map) {	null }

	def static test(def map) {
		RouterConnection.requestModule(1020)
		build(['variable': JSON.parse("transation", "ok")])
	}


	/*
	 *	Build 'console' HTML view
	 */
	// def static console(def map) {
	// 	Properties properties = importProperties()
	// 	def root = new File(properties."ui.views.path")
	// 	def templateJson = new File(root, "console.html")
		
	// 	new SimpleTemplateEngine()
	// 		.createTemplate(templateJson)
	// 		.make()
	// }


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
								"content",
								Firmware.getInstance()
										.installModule(port) as String
							)
			else
				response = JSON.parse("error", "Install object was not defined")
		} catch (e) { e.printStackTrace() 
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
				response += Firmware
								.getInstance()
								.listModules()
									
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

		build(binding)
	}
	

	/*
	 *	Call list firmware objects
	 */
	def static send(def map) {
		def response = ""

		try {
			if(map.get("object")?[0] == "message")
				response += 
						JSON.parse("error",
							Firmware
								.getInstance()
								.send(
									Integer.parseInt(map.get("destination") [0]),
									map.get("content")[0]
								) as String
						)
			else
				response = JSON.parse("error", "Action not defined")
		} catch (e) { response = JSON.parse("error", e.getLocalizedMessage()) }
		
		if(response == "{ \"error\": \"null\" }")
			response = "{}"

		def binding = ['variable': JSON.verify(response)]
		build(binding)
	}


	/*
	 *	Call wire firmware objects
	 */
	def static wire(def map) {
		def response = ""

		try {
			if(map.get("object")?[0] == "module"){
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
		
		if(response == "{ \"error\": \"null\" }")
			response = "{}"

		def binding = ['variable': JSON.verify(response)]
		build(binding)
	}


	// Null responses ------------------------------------------------------



	/*
	 *	Module killer objects
	 */
	def static start(def map) {
		try {
			if(map.get("object")[0] == "module")
				Firmware
					.getInstance()
					.startModule(
						Integer.parseInt(map.get("target")[0])
					)
		} catch (e) { }

		def binding = ['variable':'{}']
		build(binding)
	}

	
	/*
	 *	Module killer objects
	 */
	def static kill(def map) {
		try {
			if(map.get("object")[0] == "module")
				Firmware
					.getInstance()
					.killModule(
						Integer.parseInt(map.get("target")[0])
					)
		} catch (e) { }

		def binding = ['variable':'{}']
		build(binding)
	}


	/*
	 *	Kill and remove object from firmware
	 */
	def static remove(def map) {
		try {
			if(map.get("object")[0] == "module")
				Firmware
					.getInstance()
					.removeModule(
						Integer.parseInt(map.get("target")[0])
					)
		} catch (e) { }

		def binding = ['variable':'{}']
		build(binding)
	}


	/*
	 *	Confirm that the router is running
	 */
	def static health(def map) {
		build(['variable': JSON.parse("content", "check")])
	}



	/*
	 *	Build the response to send to the context to be responded
	 */
	// private static Writable build(def binding, def file = templateJson) {
	// 	new SimpleTemplateEngine()
	// 		.createTemplate(file)
	// 		.make(binding)
	// }

	/*
	 * 	Private method due to import project properties
	 *
	 *	return properties object
	 */
	// def static importProperties(){
	// 	Properties properties = new Properties();
	// 	new Object() {}
	//     	.getClass()
	//     	.getResource( Router.propertiesPath )
	//     	.withInputStream {
	//         	properties.load(it)
	//     	}
	//     properties
	// }

}