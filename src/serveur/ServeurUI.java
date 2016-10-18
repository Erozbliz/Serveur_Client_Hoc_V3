package serveur;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import objet.Match;
import test.RejectedExecutionHandlerImpl;
import tools.Tools;

/***
 * Si probleme de port faire :
 * netstat -ano | find ":2222" & netstat -ano | find ":3333"
 * TASKKILL /PID 111111 -f
 *
 */
public class ServeurUI extends JFrame implements ActionListener {

	// Serveur
	int portTCP = 2222;
	int portUDP = 3333;

	// Pour Serveur UDP
	private DatagramSocket serverSocket; // datagramme
	private ExecutorService poolThread; // pool de thread
	private byte buffer[] = new byte[1024]; // buffer

	// Pour la liste des matchs
	ArrayList<Match> listeDesMatch = new ArrayList<Match>();

	// Pour l'interface
	static JTextArea textArea1 = new JTextArea("Serveur Console");
	JButton btClean = new JButton("Effacer");
	JButton btLaunch = new JButton("Lancer Serveur");
	JButton btSent = new JButton("Envoyer message aux clients");
	JButton btDisconnectAllClients = new JButton("Deconnexion");

	//Pour les Matchs
	JButton btAjoutMatch = new JButton("Ajouter Match");
	//--liste
	DefaultListModel model = new DefaultListModel();
	JList list = new JList(model);
	//--Fin liste

	JButton btBut1 = new JButton("But Equipe 1");
	JButton btBut2 = new JButton("But Equipe 2");
	JButton btPenalty1 = new JButton("Pénalty Equipe 1");
	JButton btPenalty2 = new JButton("Pénalty Equipe 2");
	JButton btAfficheInfo = new JButton("Affiche les informations");

	// Pour modifier le textArea d'une autre classe (ServeurUI.appendToTextArea("texte");)
	public static void appendToTextArea(String text) {
		textArea1.append(text);
	}

	// Liste des utilisateurs
	ArrayList clientOutputStreams;
	ArrayList<String> users;
	private static Logger logger = Logger.getLogger(ServeurUI.class);

	/**
	 * Interface
	 */
	public ServeurUI() {

		JFrame frame = new JFrame();
		frame.setTitle("Serveur Hoc");
		frame.setSize(700, 400);
		frame.setResizable(false);
		JPanel pannel = new JPanel();

		JTabbedPane onglets = new JTabbedPane();

		// Premier Onglet
		JPanel onglet1 = new JPanel();

		//Panel pour les rangées ligne,colonne
		JPanel pan1 = new JPanel();
		pan1.setLayout(new GridLayout(1, 4));
		JPanel pan2 = new JPanel();
		pan2.setLayout(new GridLayout(1, 1));
		JPanel pan3 = new JPanel();
		pan3.setLayout(new GridLayout(4, 1));
		JPanel panListeBt = new JPanel(); //liste des boutons
		panListeBt.setLayout(new GridLayout(1, 4));

		onglet1.setPreferredSize(new Dimension(700, 400));
		onglets.addTab("Menu", onglet1);

		// Déclaration texte
		textArea1.setEditable(false); // disable editing
		// Taille de la console
		JScrollPane scrollPane = new JScrollPane(textArea1);
		scrollPane.setPreferredSize(new Dimension(600, 120));
		JScrollPane paneList = new JScrollPane(list);
		paneList.setPreferredSize(new Dimension(50, 15));

		// Déclartion bouton en public

		// Ajout listener
		// --- Lancement ---
		btClean.addActionListener(this);
		btSent.addActionListener(this);
		btLaunch.addActionListener(this);
		btDisconnectAllClients.addActionListener(this);
		// --- Matchs   ---
		btAjoutMatch.addActionListener(this);
		btBut1.addActionListener(this);
		btBut2.addActionListener(this);
		btPenalty1.addActionListener(this);
		btPenalty2.addActionListener(this);
		btAfficheInfo.addActionListener(this);

		// Ajout dans le JPanel (Lancement)
		pan1.add(btLaunch);
		pan1.add(btClean);
		pan1.add(btSent);
		pan1.add(btDisconnectAllClients);
		pan2.add(scrollPane);

		//Ajout dans le gridlayout (Matchs)
		panListeBt.add(btBut1);
		panListeBt.add(btBut2);
		panListeBt.add(btPenalty1);
		panListeBt.add(btPenalty2);

		pan3.add(btAjoutMatch);
		pan3.add(paneList);
		pan3.add(panListeBt); //gridlayout qui contient une liste de bouton
		pan3.add(btAfficheInfo);

		onglet1.add(pan1, "North"); //en haut
		onglet1.add(pan2, "Center");//au milieu
		onglet1.add(pan3, "South"); //en bas

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
		new ServeurUI();

		//creation liste pour les matchs
		//	listeDesMatch = new ArrayList<Match>();
	}

	/**
	 * Méthode appelée sur clic
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btClean) {
			textArea1.setText("Clean\n");
		} else if (e.getSource() == btDisconnectAllClients) {
			textArea1.append("\nDéconnexion des clients\n");
		} else if (e.getSource() == btLaunch) {
			textArea1.append("\nLancement du serveur TCP sur port : " + portTCP);
			// Serveur TCP
			Thread starter = new Thread(new ServerStartTCP());
			starter.start();
			// Serveur UDP
			textArea1.append("\nLancement du serveur UDP sur port : " + portUDP + "\n");
			Thread serverPoolUDP = new Thread(new ServerStartUDP());
			serverPoolUDP.start();
		} else if (e.getSource() == btSent) {
			textArea1.append("\nEnvoie manuel");
			sendToEveryone("Serveur:Envoie manuel:Chat");
		} else if (e.getSource() == btAjoutMatch) {
			int numeroMatch = 1;
			String equipe1 = "red";
			String equipe2 = "blue";
			String crDate = Tools.currentStrDate();
			//listeDesMatchs.ajouteMatch(new Match(numeroMatch, crDate, equipe1, equipe2));
			listeDesMatch.add(new Match(numeroMatch, crDate, equipe1, equipe2));
			//ajout dans la liste de selection
			model.addElement("n." + numeroMatch + " " + equipe1 + " vs " + equipe2);
			//dans la console
			textArea1.append("\nAjout d'un Match : " + "n." + numeroMatch + " " + equipe1 + " vs " + equipe2);
		} else if (e.getSource() == btBut1) {
			String crDate = Tools.currentStrDate();
			textArea1.append("\n:" + crDate + " But equipe 1 du match n." + 1);
		} else if (e.getSource() == btBut2) {
			String crDate = Tools.currentStrDate();
			textArea1.append("\n:" + crDate + " But equipe 2 du match n." + 1);
		} else if (e.getSource() == btPenalty1) {
			String crDate = Tools.currentStrDate();
			textArea1.append("\n:" + crDate + " Pénalty equipe 1 du match n." + 1);
		} else if (e.getSource() == btPenalty2) {
			String crDate = Tools.currentStrDate();
			textArea1.append("\n:" + crDate + " Pénalty equipe 2 du match n." + 1);
		}else if (e.getSource() == btAfficheInfo) {
			String crDate = Tools.currentStrDate();
			textArea1.append("\n:" + crDate + " Information sur le match n." + 1);
			String strName = (String) list.getSelectedValue();
			String strNameEquipe1 = listeDesMatch.get(0).getNameEquipe1();
			String strNameEquipe2 = listeDesMatch.get(0).getNameEquipe2();
			int strPenalty1 = listeDesMatch.get(0).getPenaltyEquipe1();
			int strPenalty2 = listeDesMatch.get(0).getPenaltyEquipe2();
			List<String> listeButEquipe1 = listeDesMatch.get(0).getListeButEquipe1();
			List<String> listeButEquipe2 = listeDesMatch.get(0).getListeButEquipe2();
			String strStatusMatch = listeDesMatch.get(0).getStatusMatch();
			

		}
		

	}

	/**
	 * Pour le serveur TCP
	 *
	 */
	public class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket sock;
		PrintWriter client;

		public ClientHandler(Socket clientSocket, PrintWriter user) {
			client = user;
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch (Exception ex) {
				textArea1.append("Unexpected error... \n");
			}

		}

		@Override
		public void run() {
			String message;
			String connect = "Connect";
			String disconnect = "Disconnect";
			String chat = "Chat";
			String pari = "Pari";
			String score = "Score";
			String[] data;

			try {
				// message = user558:has connected.:Connect
				// Structure name:message:Chat/Connect/Disconnect/Pari
				while ((message = reader.readLine()) != null) {

					// textArea1.append("Received: " + message + "\n");
					data = message.split(":");

					// data[1] contient le message

					//for (String token : data) { textArea1.append(token +"\n"); }

					textArea1.append(data[0] + " : " + data[1] + "\n");

					// shutDownWithKeyword(data[1]);

					if (data[2].equals(connect)) {
						// si name:message/Connect
						sendToEveryone((data[0] + ":" + data[1] + ":" + chat));
					} else if (data[2].equals(disconnect)) {
						// sinon si name:message/Disconnect
						sendToEveryone((data[0] + ":has disconnected." + ":" + chat));
						// userRemove(data[0]);
					} else if (data[2].equals(chat)) {
						// sinon si name:message/Chat
						sendToEveryone(message);
					} else if (data[2].equals(pari)) {
						// sinon si name:message/Pari
						sendToEveryone((data[0] + ": a parié " + data[1] + ":" + chat));
					} else if (data[2].equals(score)) {
						sendToEveryone((data[0] + ": Le score est de 3/3 :" + chat));
					} else {

					}
				}
			} catch (Exception ex) {
				textArea1.append("Connexion perdue. \n");
				ex.printStackTrace();
				clientOutputStreams.remove(client);
			}
		}
	}

	/**
	 * Protocol TCP (Socket)
	 * http://stackoverflow.com/questions/773121/how-can-i-implement-a-threaded-udp-based-server-in-java
	 * pour udp Permet de lancer le serveur
	 *
	 */
	public class ServerStartTCP implements Runnable {
		@Override
		public void run() {
			//clientOutputStreams = new ArrayList();
			//users = new ArrayList();

			try {
				ServerSocket serverSock = new ServerSocket(portTCP);

				while (true) {
					Socket clientSock = serverSock.accept();
					PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
					clientOutputStreams.add(writer);

					Thread listener = new Thread(new ClientHandler(clientSock, writer));
					listener.start();
					textArea1.append("\n Une connexion à été établie \n");
				}
			} catch (Exception ex) {
				textArea1.append("Erreur de connexion (port déjà utilisé) \n");
			}
		}
	}

	/**
	 * Pour tester utilser la classe UDPClientConsole
	 *
	 */
	public class ServerStartUDP implements Runnable {
		@Override
		public void run() {
			byte[] sendData = new byte[1024];
			clientOutputStreams = new ArrayList();
			users = new ArrayList();

			try {
				final DatagramSocket serverSocket = new DatagramSocket(portUDP);
				while (true) {
					DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
					serverSocket.receive(requestPacket);

					// METHODE UDP (SANS POOL DE THREAD)
					// On récupere que le contenu
					/*
					String sentence = new String(requestPacket.getData(), 0, requestPacket.getLength());
					textArea1.append("UDP Message : " + sentence);
					System.out.println("RECEIVED: " + sentence);
					InetAddress IPAddress = requestPacket.getAddress();
					int port = requestPacket.getPort();
					String capitalizedSentence = sentence.toUpperCase();
					sendData = capitalizedSentence.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					serverSocket.send(sendPacket);
					*/
					// FIN

					// METHODE UDP (SANS POOL DE THREAD) (le code a juste été déplacé)
					/*
					ReponseClient rc = new ReponseClient(serverSocket,requestPacket); 
					Thread monitorThread = new Thread(rc);
					monitorThread.start();
					*/
					// FIN

					// METHODE UDP ( POOL DE THREAD) voir aussi ReponseClient.java
					ThreadFactory threadFactory = Executors.defaultThreadFactory();
					RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl(); //dans test
					ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS,
							new ArrayBlockingQueue<Runnable>(2), threadFactory, rejectionHandler);
					executorPool.execute(new ReponseClient(serverSocket, requestPacket));
					// FIN
				}

			} catch (Exception e) {
				System.err.println("Error Server");
				e.printStackTrace();
			} finally {
				// poolThread.shutdown();
				serverSocket.close();
			}
		}
	}

	/**
	 * Envoie d'un message à tous le monde
	 * 
	 * @param message
	 */
	public void sendToEveryone(String message) {
		Iterator it = clientOutputStreams.iterator();
		while (it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next(); //envoie le message
				writer.println(message); //ecrit dans la console
				writer.flush();
				//textArea1.setCaretPosition(textArea1.getDocument().getLength());
			} catch (Exception ex) {
				textArea1.append("Erreur envoie aux clients \n");
			}
		}
	}

}
