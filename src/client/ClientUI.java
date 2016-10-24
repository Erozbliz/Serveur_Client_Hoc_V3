package client;

import java.awt.Color;
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
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import objet.Match;
import serveur.ServeurUI;
import tools.Serialisation;
import tools.Tools;

public class ClientUI extends JFrame implements ActionListener {
	
	//logger log4j voir mylogfile.log
	private static Logger logger = Logger.getLogger(ServeurUI.class);

	// Serveur
	String addressTCP = "localhost";
	static int portTCP = 2222;
	static int portUDP = 3333;

	Boolean isConnected = false;
	//Rafraichissement auto
	Timer timer;
	boolean autoRefresh = false;

	String username = "Se connecter d'abord  ";
	Socket sock;
	BufferedReader reader;
	PrintWriter writer;

	//Contient la liste des matchs recu par le serveur
	static ArrayList<Match> listeDesMatch;

	// Pour l'interface
	static JTextArea textArea1 = new JTextArea("Client Console");
	JButton btClean = new JButton("Effacer");
	JButton btConnectTCP = new JButton("Connexion Pari");
	JButton btSentMsgUDPdefault = new JButton("Auto Actualisation");
	JButton btScore = new JButton("Actualiser");
	JLabel jUser = new JLabel(username);
	JTextField tfPari = new JTextField();
	JLabel jSeparateur = new JLabel("--------------- PARIER ICI ---------------");
	JRadioButton optionEquipe1 = new JRadioButton("Equipe 1");
	JRadioButton optionEquipe2 = new JRadioButton("Equipe 2");
	ButtonGroup group = new ButtonGroup();

	JButton btPari = new JButton("Parier");
	JButton btAfficherPari = new JButton("Afficher Somme");
	// Liste des matchs
	static DefaultListModel model = new DefaultListModel();
	JList list = new JList(model);
	//fin listes
	JButton btInfoTargetList = new JButton("Information sur le match s�lectionn�");
	static JLabel jEquipe = new JLabel("");
	static JLabel jBut = new JLabel("");
	static JLabel jPenalty = new JLabel("");
	static JLabel jStatus = new JLabel("");
	static JLabel jChrono = new JLabel("");
	static JLabel jListeDesBut = new JLabel("Liste des buts");
	// Liste
	static DefaultListModel modelBut1 = new DefaultListModel();
	JList listBut1 = new JList(modelBut1);
	static DefaultListModel modelBut2 = new DefaultListModel();
	JList listBut2 = new JList(modelBut2);
	//fin listes

	// Pour modifier le textArea d'une autre classe (ServeurUI.appendToTextArea("texte");)
	public static void appendToTextArea(String text) {
		textArea1.append(text);
	}


	

	/**
	 * Interface
	 */
	public ClientUI() {

		JFrame frame = new JFrame();
		frame.setTitle("Client Hoc");
		frame.setSize(600, 500);
		frame.setResizable(false);
		JPanel pannel = new JPanel();

		JTabbedPane onglets = new JTabbedPane(SwingConstants.TOP);

		// Premier Onglet
		JPanel onglet1 = new JPanel();

		//Panel pour les rang�es ligne,colonne
		JPanel pan1 = new JPanel();
		pan1.setLayout(new GridLayout(1, 4));
		JPanel pan2 = new JPanel();
		pan2.setLayout(new GridLayout(1, 1));
		JPanel pan3 = new JPanel();
		pan3.setLayout(new GridLayout(9, 1));
		JPanel panListeBtParis = new JPanel(); //liste des boutons pari
		panListeBtParis.setLayout(new GridLayout(1, 4));
		JPanel panListeJLabel1 = new JPanel(); //liste des labels
		panListeJLabel1.setLayout(new GridLayout(1, 4));
		JPanel panListeJLabel2 = new JPanel(); //liste des labels
		panListeJLabel2.setLayout(new GridLayout(1, 4));
		JPanel panListeList3 = new JPanel(); //liste des liste des buts
		panListeList3.setLayout(new GridLayout(1, 2));

		JLabel titreOnglet1 = new JLabel("");
		onglet1.add(titreOnglet1);
		onglet1.setPreferredSize(new Dimension(600, 500));
		onglets.addTab("Menu", onglet1);

		// D�claration texte
		textArea1.setEditable(false); // disable editing
		// Taille de la console
		JScrollPane scrollPane = new JScrollPane(textArea1);
		scrollPane.setPreferredSize(new Dimension(500, 100));
		JScrollPane paneList = new JScrollPane(list);
		paneList.setPreferredSize(new Dimension(500, 12));
		JScrollPane paneBut1 = new JScrollPane(listBut1);
		paneBut1.setPreferredSize(new Dimension(50, 12));
		JScrollPane paneBut2 = new JScrollPane(listBut2);
		paneBut2.setPreferredSize(new Dimension(50, 12));

		// D�clartion bouton en public

		// Ajout listener
		btClean.addActionListener(this);
		btConnectTCP.addActionListener(this);
		btSentMsgUDPdefault.addActionListener(this);
		btScore.addActionListener(this);
		btPari.addActionListener(this);
		btPari.setEnabled(false); // il faut que l'utilisateur se connecte d'abord
		btAfficherPari.addActionListener(this);
		btAfficherPari.setEnabled(false);
		btInfoTargetList.addActionListener(this);
		optionEquipe1.addActionListener(this);
		optionEquipe1.setActionCommand("1"); //donn�e r�cup�r�
		optionEquipe1.setSelected(true); //par defaut equipe 1
		optionEquipe2.addActionListener(this);
		optionEquipe2.setActionCommand("2"); //donn�e r�cup�r�

		// Ajout dans le JPanel
		pan1.add(btClean);
		pan1.add(btConnectTCP);
		pan1.add(btSentMsgUDPdefault);
		pan1.add(btScore);
		pan2.add(scrollPane);

		// prend 1 colonne et 1 ligne pour panListeBtParis
		//panListeBtParis.add(jUser);
		group.add(optionEquipe1);
		group.add(optionEquipe2);
		panListeBtParis.add(optionEquipe1);
		panListeBtParis.add(optionEquipe2);
		panListeBtParis.add(tfPari);
		panListeBtParis.add(btPari);
		//panListeBtParis.add(btAfficherPari);

		// prend 1 colonne et 1 ligne pour panListeBtParis
		panListeJLabel1.add(jEquipe);
		panListeJLabel1.add(jBut);
		panListeJLabel1.add(jPenalty);
		panListeJLabel1.add(jStatus);

		// prend 1 colonne et 1 ligne pour panListeBtParis
		panListeList3.add(paneBut1);
		panListeList3.add(paneBut2);

		pan3.add(paneList); //liste de selection du match
		pan3.add(btInfoTargetList); //bouton de selection du match
		pan3.add(panListeJLabel1); //Affichage des information sur un match
		pan3.add(jListeDesBut); //Affiche "Liste des buts"
		pan3.add(panListeList3);
		jSeparateur.setHorizontalAlignment(JLabel.CENTER);
		jSeparateur.setVerticalAlignment(JLabel.CENTER);
		pan3.add(jSeparateur);
		pan3.add(jUser);
		pan3.add(panListeBtParis); //Groupement de bouton paris
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
		logger.info("Construction de l'interface Serveur");
	}

	/**
	 * Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Lancement du programme Client");
		// Lancement de l'interface
		new ClientUI();
	}

	/**
	 * M�thode appel�e sur clic
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btClean) {
			textArea1.setText("Clean\n");
			//textArea1.setText(group.getSelection().getActionCommand());
		} else if (e.getSource() == btConnectTCP) {
			//Bouton "Connexion TCP"
			if (isConnected == false) {
				//G�n�re un utilisateur al�atoire
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
					writer.println(user + ": est connect� pour le pari:Connect");
					writer.flush();
					isConnected = true;
					btConnectTCP.setEnabled(false);
					btPari.setEnabled(true); //maintenant l'utilisateur peut parier
					btAfficherPari.setEnabled(true);//maintenant l'utilisateur peut afficher les scores
				} catch (Exception ex) {
					logger.debug("Erreur : Serveur non trouv� "+ex);
					textArea1.append("\n Erreur : Serveur non trouv� \n");
					//tf_username.setEditable(true);
				}
				ListenThread();
			} else if (isConnected == true) {
				textArea1.append("Vous etes deja connect� \n");
			}
			//on rafraichie les matchs
			try {
				model.removeAllElements(); //ne pas oublier
				messageUDP("score");
			} catch (Exception e1) {
				logger.debug("Erreur : "+e1);
				e1.printStackTrace();
			}

		} else if (e.getSource() == btSentMsgUDPdefault) {
			if (autoRefresh == false) {
				autoRefresh = true;
				timer = new Timer();//D�claration d'un nouveau timer
				model.removeAllElements(); //ne pas oublier
				timer.schedule(new TimeTask(), 0, 120000);
				textArea1.append("\n rafraichissement auto activ�");
				btSentMsgUDPdefault.setBackground(Color.GREEN);
			} else {
				autoRefresh = false;
				timer.cancel();
				timer.purge();
				textArea1.append("\n rafraichissement auto d�sactiv�");
				btSentMsgUDPdefault.setBackground(null);
			}
		} else if (e.getSource() == btScore) {
			//Bouton Afficher la liste des matchs
			try {
				model.removeAllElements(); //ne pas oublier
				messageUDP("score");//demande le score
			} catch (Exception e1) {
				logger.debug("Erreur : "+e1);
				e1.printStackTrace();
			}
		} else if (e.getSource() == btPari) {
			//Bouton faire un pari = somme:numMatch:equipe:Pari(code)
			//le num�ro de l'�quipe
			String monEquipe = group.getSelection().getActionCommand();
			if (list.getSelectedIndex() != -1) {
				int i = list.getSelectedIndex();
				boolean a = listeDesMatch.get(i).getStatusMatch().equals("PERIODE 3");
				boolean b = listeDesMatch.get(i).getStatusMatch().equals("TERMINE");
				//Impossible de parier pour un match en periode 3 ou termin�
				if (!a || !b) {
					String monNumMatch = Integer.toString(list.getSelectedIndex());
					sendToServer2(tfPari.getText(), monNumMatch, monEquipe, "Pari");
				} else {
					textArea1.append("\nImpossible de voter car P�riode 3 ou Match Termin�\n");
				}
			} else {
				textArea1.append("\nSelectionner un match dans la liste au dessus\n");
			}
		} else if (e.getSource() == btInfoTargetList) {
			//Bouton Information sur le match s�lectionn�
			if (list.getSelectedIndex() != -1) {
				//-1 si pas d'item s�lectionn�
				int index = list.getSelectedIndex();
				printInfoSelectMatch(index);
			} else {
				textArea1.append("\nSelectionner un match");
			}
		} else if (e.getSource() == btAfficherPari) {
			//Bouton afficher somme pari
			//le dernier parametre et important car c'est le code
			sendToServer2("49", "PariInfo", "PariInfo", "PariInfo");
		}
	}

	/**
	 * 
	 * @param somme
	 * @param numMatch 1 ou 2 ou 3 etc
	 * @param equipe 1 ou 2
	 * @param code Pari ou autre
	 */
	public void sendToServer2(String somme, String numMatch, String equipe, String code) {
		if (code.equals("Pari")) {
			try {
				//si c'est un nombre
				if (Tools.isInteger(somme)) {
					//name:somme:numMatch:equipe:Pari(code)
					writer.println(username + ":" + somme + ":" + numMatch + ":" + equipe + ":" + code);
					writer.flush();
					tfPari.setText("");
					tfPari.setBackground(Color.green);
					tfPari.requestFocus();
				} else {
					textArea1.append("Rentrer un chiffre\n");
					//tfPari.setText("");
					tfPari.setBackground(Color.red);
					tfPari.requestFocus();
				}
			} catch (Exception ex) {
				logger.debug("Erreur : message non envoy� "+ex);
				textArea1.append("Le message n'a pas �t� envoy�\n");
			}
		} else {
			//textArea1.append("\n"+username + ":" + somme+ ":" + msg2 + ":"+code);
			writer.println(username + ":" + somme + ":" + numMatch + ":" + equipe + ":" + code);
			writer.flush();
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
			String pari = "Pari";
			String connect = "Connect";
			String disconnect = "Disconnect";
			String chat = "Chat";

			try {
				while ((stream = reader.readLine()) != null) {
					data = stream.split(":");
					if (data[2].equals("Chat")) {
						textArea1.append(data[0] + ": " + data[1] + "\n");
						textArea1.setCaretPosition(textArea1.getDocument().getLength());
					} else if (data[2].equals(connect)) {
						//textArea1.removeAll();
						//userAdd(data[0]);
					} else if (data[2].equals(pari)) {
						textArea1.append(data[0] + ": " + data[1] + "\n");
						textArea1.setCaretPosition(textArea1.getDocument().getLength());
						//userRemove(data[0]);
					} else if (data[2].equals(done)) {
						// users.setText("");
						//writeUsers();
						//users.clear();
					}
				}
			} catch (Exception ex) {
				logger.debug(ex);
			}
		}
	}

	/**
	 * Envoie d'un message UDP
	 * @param sentence
	 * @throws Exception
	 */
	public static void messageUDP(String sentence) throws Exception {
		System.out.println("--CLIENT--");
		//BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portUDP);
		clientSocket.setSoTimeout(2000);
		try {
			clientSocket.send(sendPacket);
		} catch (SocketTimeoutException e) {
			logger.debug("Erreur : pas de serveur "+e);
			textArea1.append("\n pas de serveur ");
		}

		// Datagram re�u par le serveur
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try {
			clientSocket.receive(receivePacket);
		} catch (SocketTimeoutException e) {
			logger.debug("Erreur : pas de serveur "+e);
			textArea1.append("\n pas de serveur ");
		}

		//on r�cupere la liste des matchs dans la r�ponse du serveur
		try {
			listeDesMatch = (ArrayList<Match>) Serialisation.deserialize2(receivePacket.getData());
			//a garder
			//String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());

			textArea1.append("\n Il y a " + listeDesMatch.size() + " matchs");

			//int i=0;
			for (int i = 0; i < listeDesMatch.size(); i++) {
				model.addElement("Match n." + (i + 1) + " " + listeDesMatch.get(i).getNameEquipe1() + " vs "
						+ listeDesMatch.get(i).getNameEquipe2() + " le " + listeDesMatch.get(i).getDate());
			}
			//On affiche les info pour le 1er match
			printInfoSelectMatch(0);

		} catch (IOException e) {
			logger.debug("Erreur objet vide :  "+e);
			textArea1.append("\n objet vide ");
		}

		clientSocket.close(); // On ferme la socket
		System.out.println("Socket Client close");
	}

	/**
	 * Affiche les informations d'un match s�lectionn�
	 * @param numMatch = num�ro du match dans la liste (attention pour match 1 mettre 0)
	 */
	static public void printInfoSelectMatch(int numMatch) {
		if (listeDesMatch.size() >= 1) {
			textArea1.append("\n Affichage des donn�es pour le match " + (numMatch + 1) + "\n");
			jEquipe.setText("Equipe : " + listeDesMatch.get(numMatch).getNameEquipe1() + " / "
					+ listeDesMatch.get(numMatch).getNameEquipe2());
			jBut.setText("But : " + listeDesMatch.get(numMatch).getButEquipe1() + " / "
					+ listeDesMatch.get(numMatch).getButEquipe2());
			jPenalty.setText("P�nalty : " + listeDesMatch.get(numMatch).getPenaltyEquipe1() + " / "
					+ listeDesMatch.get(numMatch).getPenaltyEquipe2());
			jStatus.setText("Status : " + listeDesMatch.get(numMatch).getStatusMatch());
			modelBut1.removeAllElements();
			modelBut2.removeAllElements();
			for (int i = 0; i < listeDesMatch.get(numMatch).getListeButEquipe1().size(); i++) {
				modelBut1.addElement(
						"\nBut " + (i + 1) + " le " + listeDesMatch.get(numMatch).getListeButEquipe1().get(i));
			}
			for (int i = 0; i < listeDesMatch.get(numMatch).getListeButEquipe2().size(); i++) {
				modelBut2.addElement(
						"\nBut " + (i + 1) + " le " + listeDesMatch.get(numMatch).getListeButEquipe2().get(i));
			}
		}
	}

}
