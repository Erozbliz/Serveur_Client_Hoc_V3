package objet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Un objet Match pour chaque partie
 * Cet objet contient les donn�es sur la partie: �quipe, pointage, 
 * temps au chronom�tre, buts et p�nalit�s
 *
 */
public class Match {

	private int numMatch;
	private Date date;
	private String statusMatch;
	private String nameEquipe1;
	private String nameEquipe2;
	private int butEquipe1;
	private int butEquipe2;
	private List<Date> listeButEquipe1;
	private List<Date> listeButEquipe2;
	private int penaltyEquipe1;
	private int penaltyEquipe2;

	/**
	* Constructeur de l'objet Match
	* @param numMatch = le num�ro du match
	* @param date = la date du match
	* @param nameEquipe1 = le nom de l'�quipe 1
	* @param nameEquipe2 = le nom de l'�quipe 2
	*/
	public Match(int numMatch, Date date, String nameEquipe1, String nameEquipe2) {

		this.numMatch = numMatch;
		this.date = date;
		this.nameEquipe1 = nameEquipe1;
		this.nameEquipe2 = nameEquipe2;

		butEquipe1 = 0;
		butEquipe2 = 0;
		listeButEquipe1 = new ArrayList<Date>();
		listeButEquipe2 = new ArrayList<Date>();
		penaltyEquipe1 = 0;
		penaltyEquipe2 = 0;
		statusMatch = "EN COURS";

	}

	public void butPourEquipe1(Date date) {
		this.butEquipe1++;
		this.listeButEquipe1.add(date);
	}

	public void butPourEquipe2(Date date) {
		this.butEquipe2++;
		this.listeButEquipe2.add(date);
	}

	public void penaltyPourEquipe1() {
		this.penaltyEquipe1++;
	}

	public void penaltyPourEquipe2() {
		this.penaltyEquipe2++;
	}

	public void statusDuMatch(String status) {
		this.statusMatch = status;
	}
	
	
	//---- GETTER ET SETTER ---
	
	public int getNumMatch() {
		return numMatch;
	}

	public void setNumMatch(int numMatch) {
		this.numMatch = numMatch;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStatusMatch() {
		return statusMatch;
	}

	public void setStatusMatch(String statusMatch) {
		this.statusMatch = statusMatch;
	}

	public String getNameEquipe1() {
		return nameEquipe1;
	}

	public void setNameEquipe1(String nameEquipe1) {
		this.nameEquipe1 = nameEquipe1;
	}

	public String getNameEquipe2() {
		return nameEquipe2;
	}

	public void setNameEquipe2(String nameEquipe2) {
		this.nameEquipe2 = nameEquipe2;
	}

	public int getButEquipe1() {
		return butEquipe1;
	}

	public void setButEquipe1(int butEquipe1) {
		this.butEquipe1 = butEquipe1;
	}

	public int getButEquipe2() {
		return butEquipe2;
	}

	public void setButEquipe2(int butEquipe2) {
		this.butEquipe2 = butEquipe2;
	}

	public List<Date> getListeButEquipe1() {
		return listeButEquipe1;
	}

	public void setListeButEquipe1(List<Date> listeButEquipe1) {
		this.listeButEquipe1 = listeButEquipe1;
	}

	public List<Date> getListeButEquipe2() {
		return listeButEquipe2;
	}

	public void setListeButEquipe2(List<Date> listeButEquipe2) {
		this.listeButEquipe2 = listeButEquipe2;
	}

	public int getPenaltyEquipe1() {
		return penaltyEquipe1;
	}

	public void setPenaltyEquipe1(int penaltyEquipe1) {
		this.penaltyEquipe1 = penaltyEquipe1;
	}

	public int getPenaltyEquipe2() {
		return penaltyEquipe2;
	}

	public void setPenaltyEquipe2(int penaltyEquipe2) {
		this.penaltyEquipe2 = penaltyEquipe2;
	}

	
	

}
