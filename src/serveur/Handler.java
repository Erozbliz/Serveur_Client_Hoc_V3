package serveur;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


/**
 * Pour le client Ajax
 * http://www.java2s.com/Code/Jar/h/Downloadhttp20070405jar.htm
 *
 */
public class Handler{
	
	public static class IndexHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange argHttpExhange) throws IOException {
			String indexMessage = "Bienvenue sur le serveur localhost/127.0.01 " + " Port: " + ServeurUI.portHttp + "<br>";
			argHttpExhange.sendResponseHeaders(200, indexMessage.length());
			OutputStream os = argHttpExhange.getResponseBody();
			os.write(indexMessage.getBytes());
			os.close();
		}
	}
	
	public static class GetListMatchHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange argHttpExhange) throws IOException {
			Map<String, Object> parameters = new HashMap<String, Object>();
			URI requestedUri = argHttpExhange.getRequestURI();
			String query = requestedUri.getRawQuery();
			String response = "xxxxxxxxxxx";
			for (String key : parameters.keySet())
				response += key + " = " + parameters.get(key) + "\n";
			argHttpExhange.sendResponseHeaders(200, response.length());
			OutputStream os = argHttpExhange.getResponseBody();
			os.write(response.toString().getBytes());
			os.close();
		}
	}

	

}
