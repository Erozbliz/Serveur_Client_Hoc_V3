package serveur;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

/**
 * Pour le client Ajax
 * http://www.java2s.com/Code/Jar/h/Downloadhttp20070405jar.htm
 *
 */
public class HttpServeur implements Runnable{
	public int port;
	public HttpServer server;
	public int portThread = ServeurUI.portHttp;

	//Méthode sans thread
	public void Start(int port) {
		try {
			this.port = port;
			server = HttpServer.create(new InetSocketAddress(port), 0);
			System.out.println("SERVEUR HTTP OK" + port);
			server.createContext("/", new Handler.IndexHandler());
			server.createContext("/getListMatch", new Handler.GetListMatchHandler());
			server.createContext("/postParis", new Handler.PostParisHandler());
			server.createContext("/getEvent", new Handler.GetEventHandler());
			server.setExecutor(null);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Stop() {
		server.stop(0);
		System.out.println("serveur http arreté");
	}

	//Avec Thread
	@Override
	public void run() {
		try {
			server = HttpServer.create(new InetSocketAddress(portThread), 0);
			System.out.println("SERVEUR HTTP OK" + port);
			server.createContext("/", new Handler.IndexHandler());
			server.createContext("/getListMatch", new Handler.GetListMatchHandler());
			server.createContext("/postParis", new Handler.PostParisHandler());
			server.createContext("/getEvent", new Handler.GetEventHandler());
			server.setExecutor(null);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
