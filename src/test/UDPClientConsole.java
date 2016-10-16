package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClientConsole {

	public static void main(String args[]) throws Exception {
		System.out.println("--CLIENT--");
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		boolean stop = false; // marquer stop pour stoper socket
		while (stop == false) {
			String sentence = inFromUser.readLine();
			sendData = sentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 3333);
			clientSocket.send(sendPacket);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			// String modifiedSentence = new String(receivePacket.getData());
			// On récupere que le contenu
			String modifiedSentence = new String(sendPacket.getData(), 0, sendPacket.getLength());
			System.out.println("DU SERVEUR:" + modifiedSentence);
			if (sentence.equals("quit")) {
				stop = true;
			}
		}
		clientSocket.close(); // On ferme la socket
		System.out.println("Socket Client close");
	}


}
