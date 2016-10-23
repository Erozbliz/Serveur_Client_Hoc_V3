package objet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * S’occupe d'enregistrer les paris pour un utilisateur.
 *
 */
public class Cagnotte implements Serializable {

	private int somme;
	private Map<String, Integer> listeUserPourEquipe1;
	private Map<String, Integer> listeUserPourEquipe2;

	/**
	 * 
	 * @param equipe
	 * @param somme
	 * @param utilisateur
	 */
	public Cagnotte() {
		this.somme = 0;
		this.listeUserPourEquipe1 = new HashMap<String, Integer>();
		this.listeUserPourEquipe2 = new HashMap<String, Integer>();
	}

	public void addParis(String user, int newSomme, int numEquipe) {
		if (numEquipe == 1) {
			listeUserPourEquipe1.put(user, newSomme);
		} else {
			listeUserPourEquipe2.put(user, newSomme);
		}
	}

	public int getSommeTotal() {
		// Get keys.
		Set<String> keys = listeUserPourEquipe1.keySet();
		Set<String> keys2 = listeUserPourEquipe2.keySet();
		int mySomme = 0;

		for (String key : keys) {
			mySomme = listeUserPourEquipe1.get(key) + mySomme;
		}

		for (String key : keys2) {
			mySomme = listeUserPourEquipe2.get(key) + mySomme;
		}
		return mySomme;
	}

	//On obtient la somme que les participants ont gagné
	public int getSommeForEachUserEquipe1() {
		int sommeTotal = getSommeTotal();
		int sommeRecuParChacun = sommeTotal;
		int nombreDeParticpant = listeUserPourEquipe1.size();
		if(nombreDeParticpant!=0){
			sommeRecuParChacun = sommeTotal / nombreDeParticpant;
		}
		return sommeRecuParChacun;
	}

	//On obtient la somme que les participants ont gagné
	public int getSommeForEachUserEquipe2() {
		int sommeTotal = getSommeTotal();
		int sommeRecuParChacun = sommeTotal;
		int nombreDeParticpant = listeUserPourEquipe2.size();
		if(nombreDeParticpant!=0){
			sommeRecuParChacun = sommeTotal / nombreDeParticpant;
		}
		return sommeRecuParChacun;
	}

	//----- GETTER ET SETTER ----

	public int getSomme() {
		return somme;
	}

	public void setSomme(int somme) {
		this.somme = somme;
	}

	public Map<String, Integer> getListeUserPourEquipe1() {
		return listeUserPourEquipe1;
	}

	public void setListeUserPourEquipe1(Map<String, Integer> listeUserPourEquipe1) {
		this.listeUserPourEquipe1 = listeUserPourEquipe1;
	}

	public Map<String, Integer> getListeUserPourEquipe2() {
		return listeUserPourEquipe2;
	}

	public void setListeUserPourEquipe2(Map<String, Integer> listeUserPourEquipe2) {
		this.listeUserPourEquipe2 = listeUserPourEquipe2;
	}

}
