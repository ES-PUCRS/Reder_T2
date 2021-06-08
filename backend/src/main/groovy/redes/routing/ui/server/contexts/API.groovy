package redes.routing.ui.server.contexts

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import groovy.lang.Lazy

import redes.routing.ui.server.Render
import redes.routing.ui.server.Web
import redes.routing.ui.ANSI
import redes.routing.Router

import java.util.HashMap
import java.util.Map
import java.util.Set

class API extends Context {

	@Override
	public void handle(HttpExchange exchange) {
		Properties properties = super.importProperties()
		if (super.corsPolicy(exchange)) return

		def json = "{ \"key\": \"value\" }"
		
		def exchangeRequestURI = exchange.requestURI as String
		def params = super.mapParams exchangeRequestURI
		def path = exchange.requestURI.path
		
		if( !path.contains("console") && !path.contains("favicon") &&
			(super.findHeaderParamKey(exchange, "Origin") 		   ||
			 super.findHeaderParamValue(exchange, "WindowsPowerShell")) )
			println "${ANSI.GREEN}[API] ${exchange.getRequestMethod()} ${path.replace("/API","")?:"/"} ${ANSI.RESET}-> Params: $params"

		// listHeaders(exchange)

		super.reply(exchange, json)
	}

}