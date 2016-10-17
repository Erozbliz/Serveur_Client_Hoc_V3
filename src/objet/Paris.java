package objet;

/**
 * S’occupe d'enregistrer les paris sur les matchs.
 *
 */
public class Paris {

	private Match match;
	private int somme;
	private String equipe;
	private String utilisateur;

	/**
	 * 
	 * @param match
	 * @param equipe
	 * @param somme
	 * @param utilisateur
	 */
	public Paris(Match match, String equipe, int somme, String utilisateur) {
		this.match = match;
		this.equipe = equipe;
		this.somme = somme;
		this.utilisateur = utilisateur;
	}

	//----- GETTER ET SETTER ----

	
	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public int getSomme() {
		return somme;
	}

	public void setSomme(int somme) {
		this.somme = somme;
	}

	public String getEquipe() {
		return equipe;
	}

	public void setEquipe(String equipe) {
		this.equipe = equipe;
	}

	public String getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}

	
}
