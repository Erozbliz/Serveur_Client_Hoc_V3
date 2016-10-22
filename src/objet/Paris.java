package objet;

import java.io.Serializable;

/**
 * S’occupe d'enregistrer les paris sur les matchs.
 *
 */
public class Paris implements Serializable{

	private int somme;
	private int numEquipe; // 1 ou 2
	private String nameEquipe;
	private String utilisateur;

	/**
	 * 
	 * @param equipe
	 * @param somme
	 * @param utilisateur
	 */
	public Paris(int numEquipe, String nameEquipe, int somme, String utilisateur) {
		this.numEquipe = numEquipe;
		this.nameEquipe = nameEquipe;
		this.somme = somme;
		this.utilisateur = utilisateur;
	}
	
	//----- GETTER ET SETTER ----

	public int getSomme() {
		return somme;
	}

	public void setSomme(int somme) {
		this.somme = somme;
	}

	public int getNumEquipe() {
		return numEquipe;
	}

	public void setNumEquipe(int numEquipe) {
		this.numEquipe = numEquipe;
	}

	public String getNameEquipe() {
		return nameEquipe;
	}

	public void setNameEquipe(String nameEquipe) {
		this.nameEquipe = nameEquipe;
	}

	public String getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}


	


	
}
