package redes.routing.ui.server.contexts.interfaces

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler

import redes.routing.Router


abstract class Context implements HttpHandler {

	/*
	 *	HTTP Header Content-Types list
	 */
	private static final TYPE = [
		"js"	: "application/javascript",
		"json"	: "application/json",
		"ico"	: "image/x-icon",
		"jpg"	: "image/jpeg",
		"text"	: "text/plain",
		"png"	: "image/png",
		"html"	: "text/html",
		"css"	: "text/css"
	]


	/*
	 * 	Private method due to import project properties to the heritage classes
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
	    return properties
	}


	/*
	 *	mapParams is used to split the uri string with params
	 *	into a Map [:]
	 *
	 *	return Map || null
	 */
	def static mapParams (String exchangeRequestURI) {
		def params = [:]
		if ((exchangeRequestURI.charAt(exchangeRequestURI.length()-1)) != "?") {
			if (exchangeRequestURI.contains("?")){
				params = exchangeRequestURI
					.replaceAll(".*\\?","")
					.split('&')
					.inject([:]) { map, token ->
	    				token.split('=').with {
    						map[it[0]?.trim()] = (it[1]?.trim()?.replace("%20"," ")?.split(','))
						}
						map
					}
			}
		}
		params
	}


	/*
	 *	reply is used to respond to the requester with the
	 *	implemented context.
	 *	
	 *	return void
	 */
	def static reply(HttpExchange exchange, def respond, def map = new HashMap()) {
		try {
			def type
			if( !(type = getResponseType(respond)) ) {
				sendResponseHeaders(exchange, 404)
				return
			}

			sendResponseHeaders(exchange, 200, map.put("Content-Type",type))
	        if(respond instanceof File)
		        respond.withInputStream {
					exchange.responseBody << it
		        }
	        else
				exchange.responseBody << respond

		} catch(e) {
			e.printStackTrace()
		} finally  {
			exchange.responseBody.close()
		}
	}


	/*
	 *	Send all map into the response header to the client	
	 */
	def static sendResponseHeaders(HttpExchange exchange, def httpStatus, def map = null){
		for(Map.Entry<String, String> entry : map) {
			exchange.responseHeaders.set(
				entry.getKey(),
				entry.getValue()
			)
		}

        exchange.sendResponseHeaders(httpStatus, 0)
	}


	/*
	 *	Define the used Content-Type on the respond to set on header
	 *	
	 *	return TYPE || null
	 */
	def static getResponseType(def respond){
		if(!respond)
			return null

		if(respond instanceof File && respond.exists())
			return TYPE[respond.name.split(/\./)[-1] as String]

		TYPE["json"]
	}


	/*
	 * 	Find a specific paramether on the header
	 *
	 *	return List<String> || null
	 */
	def static findHeaderParamKey 	(HttpExchange exchange, String key	) { findHeaderParam(exchange, key  , true ) }
	def static findHeaderParamValue (HttpExchange exchange, String value) { findHeaderParam(exchange, value, false) }
	def static findHeaderParam 		(HttpExchange exchange, String param, boolean filter ) {
		try {
			Set<Map.Entry<String, List<String>>> origin = exchange?.getRequestHeaders()?.entrySet()
			for(Map.Entry<String, List<String>> entry : origin) {
			    List<String> value = entry.getValue();
			    String key = entry.getKey();
			    
			    if (filter && param == key) 	return value
			    else {
		    		for(String str: value)
		    			if(str.contains(param)) return key
			    }
			}
		} catch (e) { e.printStackTrace() }
	}


	/*
	 *	This method is used due to configure cors policy
	 *
	 * 	return boolean
	 */
	def corsPolicy(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*")
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*")
            exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true")
            exchange.getResponseHeaders().add("Access-Control-Allow-Credentials-Header", "*")
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
            exchange.sendResponseHeaders(204, -1)
            true
        }
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*")
    }
    

	/*
	 * 	DEBUG ONLY
	 *	This method will print on terminal all the headers on the request
	 *
	 *	return void
	 */
	def static listHeaders (HttpExchange exchange) {
		try {
			Set<Map.Entry<String, List<String>>> origin = exchange?.getRequestHeaders()?.entrySet()
			for(Map.Entry<String, List<String>> entry : origin) {
			    List<String> value = entry.getValue();
			    String key = entry.getKey();
				println "${key}: ${value}"
			}
		} catch (e) { e.printStackTrace() }
	}

}