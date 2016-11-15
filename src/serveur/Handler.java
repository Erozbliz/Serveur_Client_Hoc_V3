package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import objet.Match;


/**
 * Pour le client Ajax
 * http://www.java2s.com/Code/Jar/h/Downloadhttp20070405jar.htm
 *
 */
public class Handler{
	
	
	/**
	 * Route /
	 */
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
	
	/**
	 * Route  /getListMatch
	 */
	public static class GetListMatchHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange argHttpExhange) throws IOException {
			Map<String, Object> parameters = new HashMap<String, Object>();
			URI requestedUri = argHttpExhange.getRequestURI();
			String query = requestedUri.getRawQuery();
			
			ArrayList<Match> response = ServeurUI.listeDesMatch;
			
			/*JSONObject obj = new JSONObject();
			
			for(int i=0;i<response.size();i++){
				obj.put("list"+i, response.get(i)); //pb car
			}*/
			
			//obj.put("list", response);
			//String responseStr = obj.toJSONString();
			String responseStr = serialiseListMatchJson(response);
			
			/*for (String key : parameters.keySet())
				response += key + " = " + parameters.get(key) + "\n";*/
			argHttpExhange.sendResponseHeaders(200, responseStr.length());
			OutputStream os = argHttpExhange.getResponseBody();
			os.write(responseStr.toString().getBytes());
			os.close();
		}
	}
	
	/**
	 * 
	 *
	 */
	public static class GetListParisHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange argHttpExhange) throws IOException {
			Map<String, Object> parameters = new HashMap<String, Object>();
			URI requestedUri = argHttpExhange.getRequestURI();
			String query = requestedUri.getRawQuery();
			
			String response = "sssssssssss";
			for (String key : parameters.keySet())
				response += key + " = " + parameters.get(key) + "\n";
			argHttpExhange.sendResponseHeaders(200, response.length());
			OutputStream os = argHttpExhange.getResponseBody();
			os.write(response.toString().getBytes());
			os.close();
		}
	}
	
	/**
	 * Route : /postParis
	 *
	 */
	public static class PostParisHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange he) throws IOException {
			System.out.println("PostParisHandler...");
			Map<String, Object> parameters = new HashMap<String, Object>();
			InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String query = br.readLine();
			parseQuery(query, parameters);
			// send response
			String response = "";
			// somme = 44 match = 0 user = user21 equipe = 1
			String mySomme = (String) parameters.get("somme");
			String myMatch = (String) parameters.get("match");
			String myUser = (String) parameters.get("user");
			String myEquipe = (String) parameters.get("equipe");
			response = myUser+ " "+mySomme +"$ Match "+myMatch+" Equipe "+myEquipe;
			ServeurUI.addBet(myMatch, myUser,  Integer.parseInt(myEquipe), Integer.parseInt(mySomme));
			
			/*
			for (String key : parameters.keySet())
				response += key + " = " + parameters.get(key) + "\n";*/
			he.sendResponseHeaders(200, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.toString().getBytes());
			os.close();

		}
	}
	
	public static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {

		if (query != null) {
			String pairs[] = query.split("[&]");

			for (String pair : pairs) {
				String param[] = pair.split("[=]");

				String key = null;
				String value = null;
				if (param.length > 0) {
					key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
				}

				if (param.length > 1) {
					value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
				}

				if (parameters.containsKey(key)) {
					Object obj = parameters.get(key);
					if (obj instanceof List<?>) {
						List<String> values = (List<String>) obj;
						values.add(value);
					} else if (obj instanceof String) {
						List<String> values = new ArrayList<String>();
						values.add((String) obj);
						values.add(value);
						parameters.put(key, values);
					}
				} else {
					parameters.put(key, value);
				}
			}
		}
	}
	
	

	
	
	
	//pas dans l'ordre
	public static String serialiseListMatchJson(ArrayList<Match> listMatch){
		
		JSONObject obj = new JSONObject();
		// Map obj = new LinkedHashMap();

		JSONArray listEquipe1 = new JSONArray();
		JSONArray listEquipe2 = new JSONArray();
		 
		
		for(int i=0;i<listMatch.size();i++){
			//obj.put("listButEquipe1_"+i, listMatch.get(i).getNameEquipe1() +" / "+listMatch.get(i).getNameEquipe2() ); 
			JSONArray listArrayInObj = new JSONArray();

			listArrayInObj.add(listMatch.get(i).getNameEquipe1() +" VS "+listMatch.get(i).getNameEquipe2());
			listArrayInObj.add(listMatch.get(i).getButEquipe1());
			listArrayInObj.add(listMatch.get(i).getButEquipe2());
			listArrayInObj.add(listMatch.get(i).getPenaltyEquipe1());
			listArrayInObj.add(listMatch.get(i).getPenaltyEquipe2());
			listArrayInObj.add(listMatch.get(i).getDate());
			listArrayInObj.add(listMatch.get(i).getStatusMatch());
			obj.put(i,listArrayInObj);
			/*obj.put("listButEquipe1_"+i, listMatch.get(i).getButEquipe1()); 
			obj.put("listButEquipe2_"+i, listMatch.get(i).getButEquipe2());
			obj.put("listPenaltyEquipe1_"+i, listMatch.get(i).getPenaltyEquipe1());
			obj.put("listPenaltyEquipe2_"+i, listMatch.get(i).getPenaltyEquipe2());*/
			/*for(int j=0;j<listMatch.get(i).getListeButEquipe1().size();j++){
				listEquipe1.add(listMatch.get(i).getListeButEquipe1().get(i));
			}
			for(int j=0;j<listMatch.get(i).getListeButEquipe2().size();j++){
				listEquipe2.add(listMatch.get(i).getListeButEquipe2().get(i));
			}
			obj.put("listEquipe1_"+i, listEquipe1);
			obj.put("listEquipe2_"+i, listEquipe2);*/
		}
		
		//obj.put("list", response);
		String responseStr = obj.toJSONString();
		return responseStr;
	}

	

}
