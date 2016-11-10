package serveur;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


/**
 * Pour le client Ajax
 * http://www.java2s.com/Code/Jar/h/Downloadhttp20070405jar.htm
 *
 */
public class Handler{
	
	public static class RootHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange argHttpExhange) throws IOException {
			String indexMessage = "Bienvenue sur le serveur localhost/127.0.01 " + " Port: " + ServeurUI.portHttp + "<br>";
			argHttpExhange.sendResponseHeaders(200, indexMessage.length());
			OutputStream os = argHttpExhange.getResponseBody();
			os.write(indexMessage.getBytes());
			os.close();
		}
	}
	

}
