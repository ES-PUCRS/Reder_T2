package redes.routing.ui.server.renders

import groovy.text.SimpleTemplateEngine 
import groovy.lang.Lazy

import redes.routing.core.Firmware
import redes.routing.Router

import library.JSON

class HomeRender {
	
	/*
	 *	Shell is the JQuery Script, which not
	 *	accepts render patterns.
	 */
	def static index(def map) {	null }


	/*
	 *	Build 'console' HTML view
	 */
	// def static console(def map) {
	// 	Properties properties = importProperties()
	// 	def root = new File(properties."ui.views.path")
	// 	def file = new File(root, "console.html")
		
	// 	new SimpleTemplateEngine()
	// 		.createTemplate(file)
	// 		.make()
	// }


	/*
	 *	Call Firmware installer
	 */
	def static install(def map) {
		Properties properties = importProperties()
		def root = new File(properties."ui.views.path")
		def file = new File(root, "template.json")
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

		new SimpleTemplateEngine()
			.createTemplate(file)
			.make(binding)
	}


	/*
	 *	Call list firmware objects
	 */
	def static list(def map) {
		Properties properties = importProperties()
		def root = new File(properties."ui.views.path")
		def file = new File(root, "template.json")
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
										?.replaceAll("\\]", "\\\\n\\]")
										?.replaceAll("\\\""	 , "\\\\\"")
			}

			else if(map.get("object")[0] == "routes")
				response += Firmware
								.getInstance()
								.listRoutingTable() as String

			response = JSON.parse("content", response)
		} catch (e) { response = JSON.parse("error", e.getLocalizedMessage()) }
		
		// println response

		def binding = [
			'variable' : JSON.verify(response)
		]

		new SimpleTemplateEngine()
			.createTemplate(file)
			.make(binding)
	}
	

	/*
	 *	Call list firmware objects
	 */
	def static send(def map) {
		Properties properties = importProperties()
		def root = new File(properties."ui.views.path")
		def file = new File(root, "template.json")
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
		new SimpleTemplateEngine()
			.createTemplate(file)
			.make(binding)
	}


	/*
	 *	Call list firmware objects
	 */
	def static wire(def map) {
		Properties properties = importProperties()
		def root = new File(properties."ui.views.path")
		def file = new File(root, "template.json")
		def response = ""

		try {
			if(map.get("object")?[0] == "module")
				response += Firmware
								.getInstance()
								.wireModule(
									Integer.parseInt(map.get("index") [0]),
									Integer.parseInt(map.get("target")[0])
								) as String
			else
				JSON.parse("error", "Object was not defined")
		} catch (e) { response = JSON.parse("error", e.getLocalizedMessage()) }
		
		if(response == "null")
			response = "{}"

		def binding = ['variable': JSON.verify(response)]
		new SimpleTemplateEngine()
			.createTemplate(file)
			.make(binding)
	}

	
	// Null responses ------------------------------------------------------


	/*
	 *	Module killer objects
	 */
	def static start(def map) {
		Properties properties = importProperties()
		def root = new File(properties."ui.views.path")
		def file = new File(root, "template.json")

		try {
			if(map.get("object")[0] == "module")
				Firmware
					.getInstance()
					.startModule(
						Integer.parseInt(map.get("target")[0])
					)
		} catch (e) { }

		def binding = ['variable':'{}']
		new SimpleTemplateEngine()
			.createTemplate(file)
			.make(binding)
	}

	
	/*
	 *	Module killer objects
	 */
	def static kill(def map) {
		Properties properties = importProperties()
		def root = new File(properties."ui.views.path")
		def file = new File(root, "template.json")

		try {
			if(map.get("object")[0] == "module")
				Firmware
					.getInstance()
					.killModule(
						Integer.parseInt(map.get("target")[0])
					)
		} catch (e) { }

		def binding = ['variable':'{}']
		new SimpleTemplateEngine()
			.createTemplate(file)
			.make(binding)
	}


	/*
	 *	Kill and remove object from firmware
	 */
	def static remove(def map) {
		Properties properties = importProperties()
		def root = new File(properties."ui.views.path")
		def file = new File(root, "template.json")

		try {
			if(map.get("object")[0] == "module")
				Firmware
					.getInstance()
					.removeModule(
						Integer.parseInt(map.get("target")[0])
					)
		} catch (e) { }

		def binding = ['variable':'{}']
		new SimpleTemplateEngine()
			.createTemplate(file)
			.make(binding)
	}


	/*
	 * 	Private method due to import project properties
	 *
	 *	return properties object
	 */
	def static importProperties(){
		Properties properties = new Properties();
		new Object() {}
	    	.getClass()
	    	.getResource( Router.propertiesPath )
	    	.withInputStream {
	        	properties.load(it)
	    	}
	    properties
	}

}