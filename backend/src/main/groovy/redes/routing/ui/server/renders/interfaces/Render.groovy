package redes.routing.ui.server.renders.interfaces

import groovy.text.SimpleTemplateEngine

import redes.routing.Router

abstract class Render {

	private static final root = new File(Router.properties."ui.views.path")
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

}