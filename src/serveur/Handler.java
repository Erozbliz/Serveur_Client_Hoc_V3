package serveur;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
	
	
public static String serialiseListMatchJson(ArrayList<Match> listMatch){
		
		LinkedHashMap<String, JSONArray> jsonOrderedMap = new LinkedHashMap<String, JSONArray>();
		JSONObject obj = new JSONObject();
		// Map obj = new LinkedHashMap();

		JSONArray listEquipe1 = new JSONArray();
		JSONArray listEquipe2 = new JSONArray();
		 
		
		for(int i=0;i<listMatch.size();i++){
			//obj.put("listButEquipe1_"+i, listMatch.get(i).getNameEquipe1() +" / "+listMatch.get(i).getNameEquipe2() ); 
			JSONArray listArrayInObj = new JSONArray();

			listArrayInObj.add(listMatch.get(i).getButEquipe1());
			listArrayInObj.add(listMatch.get(i).getButEquipe2());
			listArrayInObj.add(listMatch.get(i).getPenaltyEquipe1());
			listArrayInObj.add(listMatch.get(i).getPenaltyEquipe2());
			listArrayInObj.add(listMatch.get(i).getDate());
			listArrayInObj.add(listMatch.get(i).getStatusMatch());
			jsonOrderedMap.put(listMatch.get(i).getNameEquipe1() +" VS "+listMatch.get(i).getNameEquipe2(),listArrayInObj);
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
		
		JSONObject orderedJson = new JSONObject(jsonOrderedMap);
		JSONArray jsonArray = new JSONArray(Arrays.asList(orderedJson));

		
		//obj.put("list", response);
		String responseStr = orderedJson.toJSONString();
		return responseStr;
		
	}
	
	
	
	//pas dans l'ordre
	public static String serialiseListMatchJson2(ArrayList<Match> listMatch){
		
		JSONObject obj = new JSONObject();
		// Map obj = new LinkedHashMap();

		JSONArray listEquipe1 = new JSONArray();
		JSONArray listEquipe2 = new JSONArray();
		 
		
		for(int i=0;i<listMatch.size();i++){
			//obj.put("listButEquipe1_"+i, listMatch.get(i).getNameEquipe1() +" / "+listMatch.get(i).getNameEquipe2() ); 
			JSONArray listArrayInObj = new JSONArray();

			listArrayInObj.add(listMatch.get(i).getButEquipe1());
			listArrayInObj.add(listMatch.get(i).getButEquipe2());
			listArrayInObj.add(listMatch.get(i).getPenaltyEquipe1());
			listArrayInObj.add(listMatch.get(i).getPenaltyEquipe2());
			listArrayInObj.add(listMatch.get(i).getDate());
			listArrayInObj.add(listMatch.get(i).getStatusMatch());
			obj.put(listMatch.get(i).getNameEquipe1() +" VS "+listMatch.get(i).getNameEquipe2(),listArrayInObj);
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
