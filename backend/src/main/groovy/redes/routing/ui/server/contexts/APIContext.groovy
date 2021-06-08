package redes.routing.ui.server.contexts

import com.sun.net.httpserver.HttpExchange

import redes.routing.ui.server.contexts.interfaces.Context
import redes.routing.ui.ANSI

class APIContext extends Context {

	@Override
	public void handle(HttpExchange exchange) {
		Properties properties = super.importProperties()
		if (super.corsPolicy(exchange)) return

		def json = "{ \"key\": \"value\" }"
		
		def exchangeRequestURI = exchange.requestURI as String
		def params = super.mapParams exchangeRequestURI
		def path = exchange.requestURI.path
		
		if( !path.contains("console") && !path.contains("favicon") 		&&
			(super.findHeaderParamKey(exchange, "Origin") 		   		||
			(super.findHeaderParamValue(exchange, "WindowsPowerShell")  || new Boolean(properties."api.debug"))) )
			println "${ANSI.GREEN}[API] ${exchange.getRequestMethod()} ${path.replace("/API","")?:"/"} ${ANSI.RESET}-> Params: $params"

		// listHeaders(exchange)

		super.reply(exchange, json)
	}

}