import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;

import java.nio.charset.StandardCharsets;

import java.net.InetSocketAddress;
import java.net.DatagramSocket;
import java.net.ServerSocket;

import java.util.Random;
import java.util.Arrays;

public class Agent {

    public static void main(String[] args) throws Exception {
        System.out.println("Agent running on 8080");
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null);
        server.start();
    }

    static class MyHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            if(corsPolicy(httpExchange)) return;
            String response = "";
            int status = 200;

            try { response = deploy(httpExchange); }
            catch (Exception e) { response = e.getMessage(); status = 444; e.printStackTrace(); }

            httpExchange.sendResponseHeaders(status, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private boolean corsPolicy(HttpExchange httpExchange) throws IOException {
            if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials-Header", "*");
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
                httpExchange.sendResponseHeaders(204, -1);
                return true;
            }
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            return false;
        }


        private String deploy(HttpExchange httpExchange) throws IOException {
            Integer controlPort = null;
            do {
                controlPort = generatePort();
            } while (!available(controlPort));

            if(System.getenv("OS") != null) {
                Runtime.getRuntime().exec(
                    new String[]{
                        "cmd", "/c", "start",
                        "cmd", "/k",
                        "deploy.bat " + controlPort
                    }
                );
            } else {
                Runtime.getRuntime().exec(
                    new String[] {
                        "exo-open", "--launch", "TerminalEmulator"
                        "./deploy.sh " + controlPort
                    }
                );
            }


            return controlPort + "";
        }

        private String readBody(HttpExchange httpExchange) {
            InputStream is = httpExchange.getRequestBody();
            byte[] bytes = new byte[0];
            try { bytes = readAllBytes(is); }
            catch(Exception ignored) {}
            if(bytes.length == 0) return null;
            return new String(bytes, StandardCharsets.UTF_8);
        }

        private static int generatePort ()
        { return (new Random().nextInt(65353-10000) + 10000); }

        public static boolean available(int port) {
            ServerSocket ss = null;
            DatagramSocket ds = null;
            try {
                ss = new ServerSocket(port);
                ss.setReuseAddress(true);
                ds = new DatagramSocket(port);
                ds.setReuseAddress(true);
                return true;
            }
            catch (IOException e) {}
            finally {
                if (ds != null)
                    ds.close();

                if (ss != null)
                    try { ss.close(); }
                    catch (IOException ignored) { /* should not be thrown */ }
            }
            return false;
        }

        private static byte[] readAllBytes(InputStream is) throws IOException {
            byte[] buffer = new byte[1024];
            int reads = -1;
            byte[] response = null;
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                while ((reads = is.read(buffer, 0, 1024)) != -1)
                    outputStream.write(buffer, 0, reads);

                response = outputStream.toByteArray();
            } catch (Exception ignored) {}
            finally {
                try { is.close(); }
                catch (Exception ignored) {}
            }
            return response;
        }

    }
}