package redes.routing.ui.server.renders.interfaces

import groovy.text.SimpleTemplateEngine

import redes.routing.Router

abstract class Render {

	private static final Properties properties = importProperties()
	private static final root = new File(properties."ui.views.path")
	private static final templateJson = new File(root, "template.json")


	/*
	 *	Build the response to send to the context to be responded
	 */
	def static build(def binding, def file = templateJson) {
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
	    
	    properties
	}
}