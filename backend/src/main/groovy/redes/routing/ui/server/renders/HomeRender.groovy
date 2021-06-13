package redes.routing.ui.server.renders

import groovy.text.SimpleTemplateEngine 
import groovy.lang.Lazy

import redes.routing.core.Firmware
import redes.routing.Router

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
		def file = new File(root, "template.html")
		def response = "Generated module port: "
		
		try {
			if(map.get("object")[0] == "module")
				response += Firmware.getInstance().installModule()
			else
				response = "Install object was not defined"
		} catch (e) { response = "Install object was not defined ->\n[${e.getLocalizedMessage()}]" }

		
		def binding = [
			'variable' : response
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
		def file = new File(root, "template.html")
		def response = ""
		
		try {
			if(map.get("object")[0] == "module")
				response += Firmware
								.getInstance()
								.listModules() as String
		} catch (e) { response = "Object was not defined ->\n <${e.getLocalizedMessage()}>" }
		
		if(response == "[:]")
			response = "\tThere is no installed module."
		else
			response = response ?.replaceAll("\\[", "\\[\n\t ")
								?.replaceAll(","  , "\n\t")
								?.replaceAll(":",": ")
								?.replaceAll("\\]","\n\\]")
	
		def binding = [
			'variable' : response
		]

		new SimpleTemplateEngine()
			.createTemplate(file)
			.make(binding)
	}
	


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