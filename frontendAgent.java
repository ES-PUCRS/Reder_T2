
import java.net.InetSocketAddress;
import java.io.OutputStream;
import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.util.Arrays;

public class frontendAgent {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null);
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
		    httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    	    if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
	            httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
	            httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
	            httpExchange.sendResponseHeaders(204, -1);
	            return;
	        }
	        
	        String response = "{ \"data\": \"This is the response\" }";
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }


}