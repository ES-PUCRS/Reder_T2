package redes.routing.ui.server.contexts

import com.sun.net.httpserver.HttpExchange

import redes.routing.ui.server.contexts.interfaces.Context
import redes.routing.ui.server.renders.APIRender
import redes.routing.ui.ANSI

class APIContext extends Context {
	
	private static Properties properties

	APIContext() {
		properties = super.importProperties()
	}

	@Override
	public void handle(HttpExchange exchange) {
		if (super.corsPolicy(exchange)) return

		try {
			def exchangeRequestURI = exchange.requestURI as String
			def params = super.mapParams exchangeRequestURI
			def path = exchange.requestURI.path
			def respond = ""

			// Log filter
			if( !path.contains("console") && !path.contains("favicon")	   &&
				(super.findHeaderParamKey(exchange, "Origin")			   ||
				(super.findHeaderParamValue(exchange, "WindowsPowerShell") || new Boolean(properties."api.debug"))) )
				println "${ANSI.GREEN}[API] ${exchange.getRequestMethod()} ${path.replace("/API","")?:"/"} ${ANSI.RESET}-> Params: $params"


			try { respond = APIRender."${path?.substring(1)?.replaceAll("API/","")}"(params) }
			catch(ignored) {
				ignored.printStackStrace()
				if(new Boolean(properties."api.debug")) println ignored.getLocalizedMessage()
			}
			
			// println respond

			super.reply(exchange, respond)
		} catch(e) {
			StackTraceUtils.sanitize(e)
			print e
		} finally {
			exchange.responseBody.close()
		}
	}

}