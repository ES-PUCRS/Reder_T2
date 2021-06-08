package redes.routing.ui.server.contexts

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import groovy.lang.Lazy

import redes.routing.ui.server.Render
import redes.routing.ui.server.Web
import redes.routing.ui.ANSI
import redes.routing.Router


class Context implements HttpHandler {

	private static final TYPE = [
		"js"	: "application/javascript",
		"json"	: "application/json",
		"ico"	: "image/x-icon",
		"jpg"	: "image/jpeg",
		"png"	: "image/png",
		"html"	: "text/html",
		"css"	: "text/css"
	]


	def static mapParams (String exchangeRequestURI) {
		def params = [:]
		if ((exchangeRequestURI.charAt(exchangeRequestURI.length()-1)) != "?") {
			if (exchangeRequestURI.contains("?")){
				params = exchangeRequestURI
					.replaceAll(".*\\?","")
					.split('&')
					.inject([:]) { map, token ->
	    				token.split('=').with {
    						map[it[0]?.trim()] = (it[1]?.trim()?.replace("%20","")?.split(','))
						}
						map
					}
			}
		}
		params
	}


	def static reply(HttpExchange exchange, def response) {
		try {
			if(!response) {
				exchange.sendResponseHeaders(404, 0)
				return
			}

	        def type = "json"
	        if(response instanceof File && response?.exists())
				type = response.name.split(/\./)[-1]

			exchange.responseHeaders.set(
				"Content-Type",
				TYPE[type]
			)
	        exchange.sendResponseHeaders(200, 0)

	        if(type != "json")
		        response.withInputStream {
					exchange.responseBody << it
		        }
	        else
				exchange.responseBody << response

		} catch(e) {
			println e.getLineNumber()
			println e.getMessage()
		} finally {
			exchange.responseBody.close()
		}
	}


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
	
	
	@Override
	public void handle(HttpExchange exchange) { }

}