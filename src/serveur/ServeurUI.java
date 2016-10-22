package serveur;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
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
import objet.Paris;
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
	Socket socketUnique;

	// Pour Serveur UDP
	private DatagramSocket serverSocket; // datagramme
	//private ExecutorService poolThread; // pool de thread
	private byte buffer[] = new byte[1024]; // buffer

	// Pour la liste des matchs
	static ArrayList<Match> listeDesMatch = new ArrayList<Match>();
	int numeroMatch = 1;
	
	// Pour la liste des paris
	//Une liste associé à chaque match contient plusieurs paris 
	//HashMap<String,Paris> car un utilisateur peut voter que une fois (Hasmap contient des clé unique)
	static HashMap<String,HashMap<String,Paris>> listeDesParis = new HashMap<String, HashMap<String,Paris>>();
	int numeroParis = 1;


	// Pour l'interface
	static JTextArea textArea1 = new JTextArea("Serveur Console");
	JButton btClean = new JButton("Effacer");
	JButton btLaunch = new JButton("Lancer Serveur");
	JButton btSent = new JButton("Envoyer message aux clients");
	JButton btPariInformation = new JButton("Info Paris");

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
	
	public static int getSizeListeMatch() {
		return listeDesMatch.size();
	}
	
	public static ArrayList<Match> getListeMatch() {
		return listeDesMatch;
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
		btPariInformation.addActionListener(this);
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
		pan1.add(btPariInformation);
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
		} else if (e.getSource() == btPariInformation) {
			textArea1.append("\nDéconnexion des clients\n");
		} else if (e.getSource() == btLaunch) {
			textArea1.append("\nLancement du serveur TCP sur port : " + portTCP);
			btLaunch.setEnabled(false);
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
			//Bouton ajouter un match (ajoute aussi une liste pour les paris)
			String equipe1 = "red"+numeroMatch;
			String equipe2 = "blue"+numeroMatch;
			String crDate = Tools.currentStrDate();
			listeDesMatch.add(new Match(numeroMatch, crDate, equipe1, equipe2));//ajout match
			
			HashMap<String, Paris> valParis = new HashMap<String, Paris>();//ajout d'une liste de paris
			String stringToInt = Integer.toString(numeroMatch);
			listeDesParis.put(stringToInt, valParis);//ajout d'une liste de paris
			
			//valParis.put("user1", new Paris(1,"red1",300,"user1"));
			//valParis.put("user2", new Paris(2,"blue2",400,"user2"));
			
			//int somme = listeDesParis.get("1").get("user1").getSomme();
			//textArea1.append("\nsomme: "+somme);
			
		
			//affiche des informations sur les paris
			printAllInfoParis();


			/*HashMap<String,Paris> acctyp = new HashMap<String,Paris>();
			HashMap<String, HashMap<String,Paris>> gens = new HashMap<String,HashMap<String,Paris>>();
			acctyp.put("user1", nouveauParis);
			gens.put("1", acctyp);*/

			//ajout dans la liste de selection
			model.addElement("n." + numeroMatch + " " + equipe1 + " vs " + equipe2);
			//dans la console
			textArea1.append("\nAjout d'un Match : " + "n." + numeroMatch + " " + equipe1 + " vs " + equipe2);
			numeroMatch++;
		} else if (e.getSource() == btBut1) {
			String crDate = Tools.currentStrDate();
			int select = list.getSelectedIndex();
			if(select!=-1){
				textArea1.append("\n" + crDate + " But equipe 1 du match n." + select);
				listeDesMatch.get(select).butPourEquipe1(crDate); //ajoute un but + date
			}else{
				textArea1.append("\nSelectionner un match");
			}
			
		} else if (e.getSource() == btBut2) {
			String crDate = Tools.currentStrDate();
			int select = list.getSelectedIndex();
			if(select!=-1){
				textArea1.append("\n" + crDate + " But equipe 2 du match n." + select);
				listeDesMatch.get(select).butPourEquipe2(crDate); //ajoute un but
			}else{
				textArea1.append("\nSelectionner un match");
			}
		} else if (e.getSource() == btPenalty1) {
			String crDate = Tools.currentStrDate();
			int select = list.getSelectedIndex();
			if(select!=-1){
				textArea1.append("\n" + crDate + " Pénalty equipe 1 du match n." + select);
				listeDesMatch.get(select).penaltyPourEquipe1(); //ajoute un but
			}else{
				textArea1.append("\nSelectionner un match");
			}
		} else if (e.getSource() == btPenalty2) {
			String crDate = Tools.currentStrDate();
			int select = list.getSelectedIndex();
			if(select!=-1){
				textArea1.append("\n" + crDate + " Pénalty equipe 2 du match n." + select);
				listeDesMatch.get(select).penaltyPourEquipe2(); //ajoute un but
			}else{
				textArea1.append("\nSelectionner un match");
			}
		}else if (e.getSource() == btAfficheInfo) {
			String crDate = Tools.currentStrDate();
			if(list.getSelectedIndex()!=-1){
				//-1 si pas d'item sélectionné
				String strName = (String) list.getSelectedValue();
				int strIndex = list.getSelectedIndex();
				textArea1.append("\n" + crDate + " Information sur le match n." + strIndex);
				int strButEquipe1 = listeDesMatch.get(strIndex).getButEquipe1();
				int strButEquipe2 = listeDesMatch.get(strIndex).getButEquipe2();
				String strNameEquipe1 = listeDesMatch.get(strIndex).getNameEquipe1();
				String strNameEquipe2 = listeDesMatch.get(strIndex).getNameEquipe2();
				int strPenalty1 = listeDesMatch.get(strIndex).getPenaltyEquipe1();
				int strPenalty2 = listeDesMatch.get(strIndex).getPenaltyEquipe2();
				List<String> listeButEquipe1 = listeDesMatch.get(strIndex).getListeButEquipe1();
				List<String> listeButEquipe2 = listeDesMatch.get(strIndex).getListeButEquipe2();
				String strStatusMatch = listeDesMatch.get(strIndex).getStatusMatch();
				textArea1.append("\n Information sur " + strName);
				textArea1.append("\n Equipe "+strNameEquipe1+"/"+strNameEquipe2);
				textArea1.append("\n Score  "+strButEquipe1+"/"+strButEquipe2);
				textArea1.append("\n Pénaty "+strPenalty1+"/"+strPenalty2 +"\n"+strStatusMatch);
				textArea1.append("\n Equipe 1 ");
				for(int i = 0; i < listeButEquipe1.size(); i++) {
		            textArea1.append("\nBut "+(i+1)+" le "+listeButEquipe1.get(i));
		        }
				textArea1.append("\n Equipe 2 ");
				for(int i = 0; i < listeButEquipe2.size(); i++) {
					textArea1.append("\nBut "+(i+1)+" le "+listeButEquipe2.get(i));
		        }
			}else{
				textArea1.append("\nSelectionner un match");
			}
			
			

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
			String pariInfo = "PariInfo";
			String score = "Score";
			String[] data;

			try {
				// message = user558:has connected.:msg2:Connect
				// Structure name:message1:message2:Chat/Connect/Disconnect/Pari
				while ((message = reader.readLine()) != null) {

					// textArea1.append("Received: " + message + "\n");
					data = message.split(":");


					if (data[2].equals(connect)) {
						// si name:message/Connect
						textArea1.append(data[0] + " : " + data[1]+"\n");
						sendToEveryone((data[0] + ":" + data[1] + ":" + chat));
					} else if (data[2].equals(disconnect)) {
						// sinon si name:message/Disconnect
						sendToEveryone((data[0] + ":has disconnected." + ":" + chat));
						// userRemove(data[0]);
					} else if (data[2].equals(chat)) {
						// sinon si name:message/Chat
						sendToEveryone(message);
					} else if (data[3].equals("Pari")) {
						// sinon si name:message:equipe:Pari
						textArea1.append(data[0] + " : a parié " + data[1] + "$ pour l'équipe"+data[2]+"\n");
						sendToEveryone((data[0] + ": a parié " + data[1] + "$ pour l'équipe"+data[2]+":" + pari));
					} else if (data[3].equals("PariInfo")) {
						//sinon si name:message:equipe:PariInfo
						textArea1.append(data[0] + " : a demandé des information sur le pari ");
						//sendToEveryone((data[0] + ": La somme du pari est de 100$:" + pari));
						//On envoie simplement a la personne qui le demande
						sendToOne((data[0] + ": La somme du pari est de 1880$:" + pari));
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
	 * pour udp Permet de lancer le serveur
	 *
	 */
	public class ServerStartTCP implements Runnable {
		@Override
		public void run() {
			clientOutputStreams = new ArrayList();
			users = new ArrayList();

			try {
				ServerSocket serverSock = new ServerSocket(portTCP);

				while (true) {
					Socket clientSock = serverSock.accept();
					PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
					clientOutputStreams.add(writer);
					socketUnique = clientSock;
					Thread listener = new Thread(new ClientHandler(clientSock, writer));
					listener.start();
					textArea1.append("\n Une connexion à été établie pour les paris \n");
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
		//	textArea1.append("-----------"+clientOutputStreams.size()+ ""+ it.next());
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
	
	/**
	 *  A CORRIGER
	 * @param message
	 * @throws IOException 
	 */
	public void sendToOne(String message) throws IOException {

		PrintWriter printWriter = new PrintWriter(socketUnique.getOutputStream());
          printWriter.println(message);
          printWriter.flush();

	}
	
	public void printAllInfoParis(){
		// keys
		Set<String> keysListe = listeDesParis.keySet();
		int sommeEquipe1 = 0;
		int sommeEquipe2 = 0;
		// affiche tout les utilisateur dans la liste 1
		for (String key : keysListe) {
		    System.out.println("Paris pour match : "+key);
		    Set<String> keys1 = listeDesParis.get(key).keySet();
		    for (String keyUser : keys1) {
			    System.out.println("Dans match "+key +" "+keyUser+" a parié "+listeDesParis.get(key).get(keyUser).getSomme()+"$ pour l'équipe "+listeDesParis.get(key).get(keyUser).getNameEquipe());
			    if(listeDesParis.get(key).get(keyUser).getNumEquipe()==1){
			    	sommeEquipe1 = sommeEquipe1 + listeDesParis.get(key).get(keyUser).getSomme();
			    }else{
			    	sommeEquipe2 = sommeEquipe1 + listeDesParis.get(key).get(keyUser).getSomme();
			    }
		    }
		    System.out.println("Paris pour equipe 1 : "+sommeEquipe1);
		    System.out.println("Paris pour equipe 2 : "+sommeEquipe2);
		    sommeEquipe1=0;
		    sommeEquipe2=0;
		}
	}

}
