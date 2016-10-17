package objet;

import java.util.ArrayList;
import java.util.List;

/**
 *  Gère la liste des matchs du jour
 *
 */
public class ListeDesMatchs {
	
	private int numListDesMatchs;
	private List<Match> listeMatchs;

	/**
	 * 
	 * @param numListDesMatchs
	 */
	public ListeDesMatchs(int numListDesMatchs) {
		this.numListDesMatchs = numListDesMatchs;
		this.listeMatchs = new ArrayList<Match>();
	}
	
	public void ajouteMatch(Match match) {
		this.listeMatchs.add(match);
	}

	//----- GETTER ET SETTER ----
	
	public int getNumListDesMatchs() {
		return numListDesMatchs;
	}

	public void setNumListDesMatchs(int numListDesMatchs) {
		this.numListDesMatchs = numListDesMatchs;
	}

	public List<Match> getListeMatchs() {
		return listeMatchs;
	}

	public void setListeMatchs(List<Match> listeMatchs) {
		this.listeMatchs = listeMatchs;
	}
	
	


}
