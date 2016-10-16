package serveur;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


/**
 * Pour la pool de Thread
 *
 */
public class ReponseClient implements Runnable {


	DatagramSocket serverSocket;
	DatagramPacket requestPacket;
	static byte sendBuffer[] = new byte[1024];
	byte[] sendData = new byte[1024];

	public ReponseClient(DatagramSocket socket, DatagramPacket packet) {
		this.serverSocket = socket;
		this.requestPacket = packet;
	}
	
	public void run(){
		String sentence = new String(requestPacket.getData(), 0, requestPacket.getLength());
		String capitalizedSentence = "";
		
		ServeurUI.appendToTextArea("UDP msg : "+sentence+"\n");
		System.out.println("RECEIVED: " + sentence);
		InetAddress IPAddress = requestPacket.getAddress();
		int port = requestPacket.getPort();
		
		if(sentence.equals("score")){
			capitalizedSentence = "le résultat est 3/3";
			sendData = capitalizedSentence.getBytes();
		}else{
			capitalizedSentence = sentence.toUpperCase();
			sendData = capitalizedSentence.getBytes();
		}
		
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		try {
			serverSocket.send(sendPacket);
		} catch (IOException e) {
			System.err.println("Error \n");
			e.printStackTrace();
		}
	}


}
