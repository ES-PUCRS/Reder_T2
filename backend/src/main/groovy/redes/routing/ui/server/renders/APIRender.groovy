package redes.routing.ui.server.renders

import groovy.text.SimpleTemplateEngine

import redes.routing.ui.server.renders.interfaces.Render
import redes.routing.core.Firmware
import redes.routing.Router

import library.JSON

class APIRender extends Render {
	

	private static final Properties properties = super.importProperties()

	def static health(def map) {
		super.build(['variable': JSON.parse("status", "up")])
	}


	/*
	 *	Shell is the JQuery Script, which not
	 *	accepts render patterns.
	 */
	def static connect(def map) {
		super.build(['variable': JSON.parse("content", "check")])
	}


	/*
	 *	Call Firmware installer
	 */
	// def static install(def map) {
	// 	def response = ""
	// 	def port

	// 	if (map?.get("port"))
	// 		port = Integer.parseInt(map?.get("port")?[0])
		
	// 	try {
	// 		if(map.get("object")[0] == "module")
	// 			response += JSON.parse(
	// 							"content",
	// 							Firmware.getInstance()
	// 									.installModule(port) as String
	// 						)
	// 		else
	// 			response = JSON.parse("error", "Install object was not defined")
	// 	} catch (e) { if(new Boolean(properties."api.debug")) println e.getLocalizedMessage() 
	// 		response = JSON.parse("error", e.getLocalizedMessage()) }

	// 	def binding = [
	// 		'variable' : JSON.verify(response)
	// 	]

	// 	build(binding)
	// }


	// def static list(def map) 	{}
	// def static wire(def map) 	{}
	// def static start(def map) 	{}
	// def static kill(def map) 	{}
	// def static remove(def map) 	{}
	



	/*
	 *	Build the response to send to the context to be responded
	 */
	// def static Writable build(def binding, def file = templateJson) {
	// 	println "caled"
	// 	try{
	// 		new SimpleTemplateEngine()
	// 			.createTemplate(file)
	// 			.make(binding)
	// 	} catch(Exception e) { e.printStackTrace() }
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