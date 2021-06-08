package redes.routing.ui.server.contexts

import com.sun.net.httpserver.HttpExchange

import redes.routing.ui.server.contexts.interfaces.Context
import redes.routing.ui.server.renders.HomeRender
import redes.routing.ui.ANSI

class HomeContext extends Context {

	private static Properties properties
	private static root

	HomeContext(){
		properties = super.importProperties()
		root = new File(properties."ui.views.path")
	}

	@Override
	public void handle(HttpExchange exchange) {
		def respond
		

		try {

			if (!"GET".equalsIgnoreCase(exchange.requestMethod)) {
		        super.sendResponseHeaders(exchange, 405)
				return
			}

			def exchangeRequestURI = exchange.requestURI as String
			def params = super.mapParams exchangeRequestURI
			def path = exchange.requestURI.path


			if(!path.contains("console") && !path.contains("favicon"))
				println "${ANSI.GREEN}[CLI] GET $path ${ANSI.RESET}-> Params: $params"

			
			try {
				try { respond = "${path?.substring(1)?.replaceAll(".*\\.","")?:"index"}"(path, params) }
				catch(ignored){ respond = HomeRender."${path?.substring(1)?.replaceAll("\\..*","")}"(params) }
			} catch (ignore) { }
			

			super.reply(exchange, respond)
		} catch(e) {
			StackTraceUtils.sanitize(e)
			print e
		} finally {
			exchange.responseBody.close()
		}
	}

	def index (def path, def map){
		new File(root, "shell.html")
	}

	def ico (def path, def map){
		new File((root.getPath() + "/assets"), path.substring(1))
	}

	def css (def path, def map){
		new File((root.getPath() + "/styles"), path.substring(1))
	}
}