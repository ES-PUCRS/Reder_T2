package redes.routing.ui.server.contexts

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import groovy.lang.Lazy

import redes.routing.ui.server.Render
import redes.routing.ui.server.Web
import redes.routing.ui.ANSI
import redes.routing.Router


class Home extends Context {

	@Override
	public void handle(HttpExchange exchange) {
		Properties properties = super.importProperties()

		def render
		def root = new File(properties."ui.views.path")

		try {

			if (!"GET".equalsIgnoreCase(exchange.requestMethod)) {
		        exchange.sendResponseHeaders(405, 0)
				exchange.responseBody.close()
				return
			}

			def exchangeRequestURI = exchange.requestURI as String
			def params = super.mapParams exchangeRequestURI
			def path = exchange.requestURI.path


			if(!path.contains("console") && !path.contains("favicon"))
				println "${ANSI.GREEN}[CLI] GET $path ${ANSI.RESET}-> Params: $params"


			def file
			render = null
			if (path.contains(".ico")){
				file = new File((root.getPath() + "/assets"), path.substring(1))
			} else if (path.contains(".css")){
				file = new File((root.getPath() + "/styles"), path.substring(1))
			} else {
				file = new File(root, path.substring(1))
				if (file.isDirectory()) {
					file = new File(file, "shell.html")
					render = Render.index(params)
				} else {
					file = new File(root, path.substring(1) + ".html")
			   		render = Render."${file.name.split(/\./)[0]}"(params)
				}
			}

			super.reply(exchange, file?:render)
		} catch(e) {
			println e.getLineNumber()
			println e.getMessage()
		}
	}
}