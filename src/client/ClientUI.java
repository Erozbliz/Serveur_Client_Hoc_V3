package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import serveur.ServeurUI;
import tools.Tools;

public class ClientUI extends JFrame implements ActionListener {

	// Serveur
	String addressTCP = "localhost";
	int portTCP = 2222;
	int portUDP = 3333;

	Boolean isConnected = false;

	String username = "Se connecter d'abord  ";
	Socket sock;
	BufferedReader reader;
	PrintWriter writer;


	// Pour l'interface
	static JTextArea textArea1 = new JTextArea("Client Console");
	JButton btClean = new JButton("Effacer");
	JButton btConnectTCP = new JButton("Connexion TCP");
	JButton btSentMsgUDPdefault = new JButton("Message UDP");
	JButton btScore = new JButton("Afficher le score");
	JLabel jUser = new JLabel(username);
	JTextField tfPari = new JTextField();
	JButton btPari = new JButton("Parier");
	JButton btAfficherPari = new JButton("Afficher Somme");

	// Pour modifier le textArea d'une autre classe (ServeurUI.appendToTextArea("texte");)
	public static void appendToTextArea(String text) {
		textArea1.append(text);
	}

	// Liste des utilisateurs
	private static Logger logger = Logger.getLogger(ServeurUI.class);

	/**
	 * Interface
	 */
	public ClientUI() {

		JFrame frame = new JFrame();
		frame.setTitle("Client Hoc");
		frame.setSize(600, 400);
		frame.setResizable(false);
		JPanel pannel = new JPanel();

		JTabbedPane onglets = new JTabbedPane(SwingConstants.TOP);

		// Premier Onglet
		JPanel onglet1 = new JPanel();

		//Panel pour les rangées ligne,colonne
		JPanel pan1 = new JPanel();
		pan1.setLayout(new GridLayout(1, 4));
		JPanel pan2 = new JPanel();
		pan2.setLayout(new GridLayout(1, 1));
		JPanel pan3 = new JPanel();
		pan3.setLayout(new GridLayout(1, 2));

		JLabel titreOnglet1 = new JLabel("");
		onglet1.add(titreOnglet1);
		onglet1.setPreferredSize(new Dimension(600, 400));
		onglets.addTab("Menu", onglet1);

		// Déclaration texte
		textArea1.setEditable(false); // disable editing
		// Taille de la console
		JScrollPane scrollPane = new JScrollPane(textArea1);
		scrollPane.setPreferredSize(new Dimension(500, 150));

		// Déclartion bouton en public

		// Ajout listener
		btClean.addActionListener(this);
		btConnectTCP.addActionListener(this);
		btSentMsgUDPdefault.addActionListener(this);
		btScore.addActionListener(this);
		btPari.addActionListener(this);
		btPari.setEnabled(false); // il faut que l'utilisateur se connecte d'abord
		btAfficherPari.addActionListener(this);
		btAfficherPari.setEnabled(false);

		// Ajout dans le JPanel
		pan1.add(btClean);
		pan1.add(btConnectTCP);
		pan1.add(btSentMsgUDPdefault);
		pan1.add(btScore);
		pan2.add(scrollPane);
		pan3.add(jUser);
		pan3.add(tfPari);
		pan3.add(btPari);
		pan3.add(btAfficherPari);

		onglet1.add(pan1, "North"); //en haut
		onglet1.add(pan2, "Center");//au milieu
		onglet1.add(pan3, "South"); //en bas

		frame.getContentPane().add(pannel);
		frame.setVisible(true);

		// Deuxieme Onglet
		JPanel onglet2 = new JPanel();
		JLabel titreOnglet2 = new JLabel("A propos");
		onglet2.add(titreOnglet2);
		onglets.addTab("A propos", onglet2);
		onglets.setOpaque(true);
		pannel.add(onglets);
		frame.getContentPane().add(pannel);
		frame.setVisible(true);

	}

	/**
	 * Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Lancement de l'interface
		new ClientUI();
	}

	/**
	 * Méthode appelée sur clic
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btClean) {
			textArea1.setText("Clean\n");
		} else if (e.getSource() == btConnectTCP) {
			//Bouton "Connexion TCP"
			if (isConnected == false) {
				
				//Génère un utilisateur aléatoire
				String user = Tools.randUser();
				username = user;

				jUser.setText(user);
				//tf_username.setEditable(false);

				try {
					textArea1.append("\n Adresse TCP = " + addressTCP + " / port TCP = " + portTCP + "\n");
					sock = new Socket(addressTCP, portTCP);
					InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
					reader = new BufferedReader(streamreader);
					writer = new PrintWriter(sock.getOutputStream());
					writer.println(user + ": est connecté pour le pari:Connect");
					writer.flush();
					isConnected = true;
					btPari.setEnabled(true); //maintenant l'utilisateur peut parier
					btAfficherPari.setEnabled(true);//maintenant l'utilisateur peut afficher les scores
				} catch (Exception ex) {
					textArea1.append("\n Erreur : Serveur non trouvé \n");
					//tf_username.setEditable(true);
				}

				ListenThread();

			} else if (isConnected == true) {
				textArea1.append("You are already connected. \n");
			}

		} else if (e.getSource() == btSentMsgUDPdefault) {
			//Bouton MessageUDP
			try {
				messageUDP("test");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == btScore) {
			//Bouton Afficher le score
			try {
				messageUDP("score");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == btPari) {
			//Bouton faire un pari
			sendToServer("pari", "pari");
		}
	}

	/**
	 * 
	 * @param msg = le message a envoyer au client
	 * @param code = si "pari" alors on envoie un message pour le pari
	 */
	public void sendToServer(String msg, String code) {

		String codePourTraitement = "Chat";
		if (code.equals("pari")) {
			codePourTraitement = "Pari";
			msg = "Pari";
		}
		if (code.equals("score")) {
			codePourTraitement = "Score";
			msg = "Pari";
		}
		try {
			
			//si c'est un nombre
			if(Tools.isInteger(tfPari.getText())){
				writer.println(username + ":" + tfPari.getText() + ":" + codePourTraitement);
				writer.flush(); 
				tfPari.setText("");
				tfPari.setBackground(Color.green);
				tfPari.requestFocus();
			}else{
				textArea1.append("Rentrer un chiffre\n");
				//tfPari.setText("");
				tfPari.setBackground(Color.red);
				tfPari.requestFocus();
			}
			
		} catch (Exception ex) {
			textArea1.append("Le message n'a pas été envoyé\n");
		}
		
	}

	public void ListenThread() {
		Thread reader = new Thread(new IncomingReader());
		reader.start();
	}

	public class IncomingReader implements Runnable {
		@Override
		public void run() {
			String[] data;
			String stream;
			String done = "Done";
			String connect = "Connect";
			String disconnect = "Disconnect";
			String chat = "Chat";

			try {
				while ((stream = reader.readLine()) != null) {
					data = stream.split(":");

					if (data[2].equals(chat)) {
						textArea1.append(data[0] + ": " + data[1] + "\n");
						textArea1.setCaretPosition(textArea1.getDocument().getLength());
					} else if (data[2].equals(connect)) {
						//textArea1.removeAll();
						//userAdd(data[0]);
					} else if (data[2].equals(disconnect)) {
						//userRemove(data[0]);
					} else if (data[2].equals(done)) {
						// users.setText("");
						//writeUsers();
						//users.clear();
					}
				}
			} catch (Exception ex) {
			}
		}
	}
	
	
	/**
	 * Envoie d'un message UDP
	 * @param sentence
	 * @throws Exception
	 */
	public void messageUDP(String sentence) throws Exception {
		System.out.println("--CLIENT--");
		//BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portUDP);
		clientSocket.send(sendPacket);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		// String modifiedSentence = new String(receivePacket.getData());
		// On récupere que le contenu
		/*String modifiedSentence = new String(sendPacket.getData(), 0, sendPacket.getLength());
		textArea1.append(modifiedSentence+"\n");
		System.out.println("DU SERVEUR:" + modifiedSentence);*/
		
		String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
		textArea1.append(modifiedSentence+"\n");
		System.out.println("DU SERVEUR:" + modifiedSentence);
		
		
		
		clientSocket.close(); // On ferme la socket
		System.out.println("Socket Client close");
	}

}
