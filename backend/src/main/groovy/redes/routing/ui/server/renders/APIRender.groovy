package redes.routing.ui.server.renders

import groovy.text.SimpleTemplateEngine

import redes.routing.ui.server.renders.interfaces.Render
import redes.routing.core.Firmware
import redes.routing.Router

import library.JSON

class APIRender extends Render {
	
	def static API(def map) {
		super.build(['variable':
			JSON.parse(
				[

					"ROOT API ENDPOINT": "/API",

					"Health check":
						JSON.parse(
							[
								"endpoint": "/health",
								"expected": healthHandler(null)
							]
						),

					"Install module":
						JSON.parse(
							[
								"endpoint": "/install",
								"params":
									JSON.parse(
										[
											"object": "module",
											"port": "Integer | Null"
										]
									),
								"expected":
									JSON.parse(
										"port": "Generated port"
									),
								"whenever fails":
									JSON.parse(
										"error": "Exception message"
									)
							]
						),


					"List tables":
						JSON.parse(
							[
								"endpoint": "/list",
								"param":
									JSON.parse(
											"object": "modules | routes"
									),
								"expected":
									JSON.parse(
										"content": JSON.parse(["list": "[1, 2, 3]"])
									),
								"whenever fails":
									JSON.parse(
										"error": "Exception message"
									)
							]
						),	


					"Start module":
						JSON.parse(
							[
								"endpoint": "/start",
								"params":
									JSON.parse(
										[
											"object": "module",
											"target": "Integer"
										]
									),
								"expected" : "ok"
							]
						),

					"Kill module":
						JSON.parse(
							[
								"endpoint": "/kill",
								"params":
									JSON.parse(
										[
											"object": "module",
											"target": "Integer"
										]
									),
								"expected" : "ok"
							]
						),

					"Remove module":
						JSON.parse(
							[
								"endpoint": "/remove",
								"params":
									JSON.parse(
										[
											"object": "module",
											"target": "Integer"
										]
									),
								"expected" : "ok"
							]
						)

				]
			)
		])
	}

	/*
	 *	Shell is the JQuery Script, which not
	 */
	def static health(def map) {
		super.build(['variable': healthHandler(map)])
	}
	def static healthHandler(def map) {
		JSON.parse("status", "up")
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
		} catch (e) { if(new Boolean(Router.properties."api.debug")) println e.getLocalizedMessage() 
			response = JSON.parse("error", e.getLocalizedMessage()) }

		if(response == "{ \"error\": \"null\" }")
			response = "{}"

		super.build(['variable' : JSON.verify(response)])
	}


	/*
	 *	Call list firmware objects
	 */
	def static list(def map) {
		def response = ""

		try {
			if(map.get("object")[0] == "modules") {
				response += Firmware.getInstance()
									.listModules() as String

				if (response != "[:]")
					response = response
									.replaceAll("\\[", "\\[\"")
									.replaceAll(":\\{", "\":{")
									.replaceAll("\\}, ", "},\"")
									.replaceAll("\"", "\"")
									.replaceAll("\\[", "\\{")
									.replaceAll("\\]", "\\}")
	
			}

			else if(map.get("object")[0] == "routes") {
				response =	Firmware.getInstance()
									.listRoutingTable()

				// if (response != "[:]")
					// response = response
									// .replaceAll("\\[", "\\{")
									// .replaceAll("\\]", "\\}")
				// response = "{\"array\": [${response}]}"
			}

			else {
				response = JSON.parse("error", "No object to list")
			}

			// response = JSON.parse("contentx", response)
		} catch (e) { response = JSON.parse("error", e.getLocalizedMessage()) }

		super.build('variable' : JSON.verify(response))		
	}


	/*
	 *	Send message or file to antoher router
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
		super.build(binding)
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
		
		// TODO Remove this on API
		if(response == "{ \"error\": \"null\" }")
			response = "{}"

		super.build(['variable': JSON.verify(response)])
	}



 	/*
	 *	Module Start
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
		super.build(binding)
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
		super.build(binding)
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
		super.build(binding)
	}

}