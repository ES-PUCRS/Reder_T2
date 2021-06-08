package redes.routing.ui.server.renders

import groovy.text.SimpleTemplateEngine 
import groovy.lang.Lazy

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
	def static console(def map) {
		Properties properties = importProperties()
		def root = new File(properties."ui.views.path")
		def file = new File(root, "console.html")
		
		new SimpleTemplateEngine()
			.createTemplate(file).make()
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