package serveur;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import tools.Serialisation;

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

	public void run() {
		String sentence = new String(requestPacket.getData(), 0, requestPacket.getLength());
		String capitalizedSentence = "";

		ServeurUI.appendToTextArea("\nUDP msg : " + sentence + "\n");
		System.out.println("RECEIVED: " + sentence);
		InetAddress IPAddress = requestPacket.getAddress();
		int port = requestPacket.getPort();

		if (sentence.equals("score")) {
			//capitalizedSentence = "le résultat est 3/3";
			//sendData = capitalizedSentence.getBytes();
			ServeurUI.appendToTextArea("Envoie de la liste des Matchs\n");
			try {
				//sendData = Serialisation.serialisation(ServeurUI.listeDesMatch); //Sérialisation de la liste des matchs
				sendData = Serialisation.serialize2(ServeurUI.getListeMatch()); //Sérialisation de la liste des matchs

			} catch (IOException e) {
				System.err.println("Error serialisation\n");
				e.printStackTrace();
			}

		} else {
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
