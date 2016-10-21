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
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
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

	// Serveur
	String addressTCP = "localhost";
	int portTCP = 2222;
	int portUDP = 3333;

	Boolean isConnected = false;

	String username = "Se connecter d'abord  ";
	Socket sock;
	BufferedReader reader;
	PrintWriter writer;
	
	//Contient la liste des matchs recu par le serveur
	ArrayList<Match> listeDesMatch;


	// Pour l'interface
	static JTextArea textArea1 = new JTextArea("Client Console");
	JButton btClean = new JButton("Effacer");
	JButton btConnectTCP = new JButton("Connexion TCP");
	JButton btSentMsgUDPdefault = new JButton("Message UDP");
	JButton btScore = new JButton("Actualiser");
	JLabel jUser = new JLabel(username);
	JTextField tfPari = new JTextField();
	JButton btPari = new JButton("Parier");
	JButton btAfficherPari = new JButton("Afficher Somme");
	// Liste des matchs
	DefaultListModel model = new DefaultListModel();
	JList list = new JList(model);
	//fin listes
	JButton btInfoTargetList = new JButton("Information sur le match sélectionné");
	JLabel jEquipe = new JLabel("Equipe 1 / Equipe 2");
	JLabel jBut = new JLabel("But 	  : 0 / 0");
	JLabel jPenalty = new JLabel("Pénalty : 0 / 0");
	JLabel jStatus = new JLabel("EN COURS");
	JLabel jChrono = new JLabel("0000");
	JLabel jListeDesBut = new JLabel("Liste des buts");
	// Liste
	DefaultListModel modelBut1 = new DefaultListModel();
	JList listBut1 = new JList(modelBut1);
	DefaultListModel modelBut2 = new DefaultListModel();
	JList listBut2 = new JList(modelBut2);
	//fin listes

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
		pan3.setLayout(new GridLayout(6, 1));
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
		onglet1.setPreferredSize(new Dimension(600, 400));
		onglets.addTab("Menu", onglet1);

		// Déclaration texte
		textArea1.setEditable(false); // disable editing
		// Taille de la console
		JScrollPane scrollPane = new JScrollPane(textArea1);
		scrollPane.setPreferredSize(new Dimension(500, 100));
		JScrollPane paneList = new JScrollPane(list);
		paneList.setPreferredSize(new Dimension(50, 12));
		JScrollPane paneBut1 = new JScrollPane(listBut1);
		paneBut1.setPreferredSize(new Dimension(50, 12));
		JScrollPane paneBut2 = new JScrollPane(listBut2);
		paneBut2.setPreferredSize(new Dimension(50, 12));

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
		btInfoTargetList.addActionListener(this);

		// Ajout dans le JPanel
		pan1.add(btClean);
		pan1.add(btConnectTCP);
		pan1.add(btSentMsgUDPdefault);
		pan1.add(btScore);
		pan2.add(scrollPane);
		
		// prend 1 colonne et 1 ligne pour panListeBtParis
		panListeBtParis.add(jUser);
		panListeBtParis.add(tfPari);
		panListeBtParis.add(btPari);
		panListeBtParis.add(btAfficherPari);
		
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
		pan3.add(panListeBtParis); //Groupement de bouton paris
		
		/*model.addElement("n.");
		modelBut1.addElement("111111111");
		modelBut1.addElement("111111111");
		modelBut1.addElement("111111111");
		modelBut2.addElement("2222");*/


		
	

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
			//Bouton Afficher la liste des matchs
			try {
				model.removeAllElements(); //ne pas oublier
				messageUDP("score");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == btPari) {
			//Bouton faire un pari
			sendToServer("pari", "pari");
		}
		else if (e.getSource() == btInfoTargetList) {
			//Bouton Information sur le match sélectionné
			if(list.getSelectedIndex()!=-1){
				//-1 si pas d'item sélectionné
				int index = list.getSelectedIndex();
				printInfoSelectMatch(index);
			}else{
				textArea1.append("\nSelectionner un match");
			}
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
		

		// Datagram reçu par le serveur
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);

		//on récupere la liste des matchs dans la réponse du serveur
		 listeDesMatch = (ArrayList<Match>) Serialisation.deserialize2(receivePacket.getData());

		 //a garder
		 //String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
		
		textArea1.append("\n Il y a "+listeDesMatch.size()+" matchs");
		
		//int i=0;
		for(int i=0;i< listeDesMatch.size(); i++){
			model.addElement("Match n."+(i+1)+ " " + listeDesMatch.get(i).getNameEquipe1()+" vs " +  listeDesMatch.get(i).getNameEquipe2()+ " le " +listeDesMatch.get(i).getDate());
		}
		//On affiche les info pour le 1er match
		printInfoSelectMatch(0);
	

		clientSocket.close(); // On ferme la socket
		System.out.println("Socket Client close");
	}
	
	/**
	 * Affiche les informations d'un match sélectionné
	 * @param numMatch = numéro du match dans la liste (attention pour match 1 mettre 0)
	 */
	public void printInfoSelectMatch(int numMatch){
		if(listeDesMatch.size()>=1){
			textArea1.append("\n Affichage des données pour le match "+(numMatch+1));
			jEquipe.setText("Equipe : " + listeDesMatch.get(numMatch).getNameEquipe1() +" / " +listeDesMatch.get(numMatch).getNameEquipe2());
			jBut.setText("But : " + listeDesMatch.get(numMatch).getButEquipe1() +" / " +listeDesMatch.get(numMatch).getButEquipe2());
			jPenalty.setText("Pénalty : " + listeDesMatch.get(numMatch).getPenaltyEquipe1() +" / " +listeDesMatch.get(numMatch).getPenaltyEquipe2());
			jStatus.setText("Status : " + listeDesMatch.get(numMatch).getStatusMatch());
			modelBut1.removeAllElements();
			modelBut2.removeAllElements();
			for(int i = 0; i < listeDesMatch.get(numMatch).getListeButEquipe1().size(); i++) {
				modelBut1.addElement("\nBut "+(i+1)+" le "+listeDesMatch.get(numMatch).getListeButEquipe1().get(i));
	        }
			for(int i = 0; i < listeDesMatch.get(numMatch).getListeButEquipe2().size(); i++) {
				modelBut2.addElement("\nBut "+(i+1)+" le "+listeDesMatch.get(numMatch).getListeButEquipe2().get(i));
	        }
		}
	}

}
