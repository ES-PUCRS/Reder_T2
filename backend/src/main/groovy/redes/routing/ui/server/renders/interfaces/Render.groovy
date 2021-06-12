package redes.routing.ui.server.renders.interfaces

import groovy.text.SimpleTemplateEngine

import redes.routing.Router

abstract class Render {
		
	private static final Properties properties = importProperties()
	private static templateJson
	private static root

	
	def static install(def map)	{}
	def static remove(def map)	{}
	def static start(def map)	{}
	def static wire(def map)	{}
	def static kill(def map)	{}
	def static list(def map)	{}

	

	/*
	 *	Build the response to send to the context to be responded
	 */
	def static build(def binding, def file = templateJson) {
		println "caled"
		try{
			new SimpleTemplateEngine()
				.createTemplate(file)
				.make(binding)
		} catch(Exception e) { e.printStackTrace() }
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
 		templateJson = new File(root, "template.json")
	    root = new File(properties."ui.views.path")
	    properties
	}
}