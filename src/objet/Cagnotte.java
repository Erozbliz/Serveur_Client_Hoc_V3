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
			listeUserPourEquipe1.remove(user);
			listeUserPourEquipe2.remove(user);
			listeUserPourEquipe1.put(user, newSomme);
		} else {
			listeUserPourEquipe1.remove(user);
			listeUserPourEquipe2.remove(user);
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
	
	public int getSommeTotalDesGagnant(int numEquipeGagnant) {
		int mySomme = 0;
		if(numEquipeGagnant==1){
			Set<String> keys = listeUserPourEquipe1.keySet();
			for (String key : keys) {
				mySomme = listeUserPourEquipe1.get(key) + mySomme;
			}
		}else{
			Set<String> keys2 = listeUserPourEquipe2.keySet();
			for (String key : keys2) {
				mySomme = listeUserPourEquipe2.get(key) + mySomme;
			}
		}
		return mySomme;
	}

	//On obtient la somme que les participants ont gagné
	public int getSommeForEachUserEquipe1() {
		int sommeTotal = getSommeTotal();
		int sommeRecuParChacun = sommeTotal;
		int nombreDeParticpant = listeUserPourEquipe1.size();
		if (nombreDeParticpant != 0) {
			sommeRecuParChacun = sommeTotal / nombreDeParticpant;
		}
		return sommeRecuParChacun;
	}

	//On obtient la somme que les participants ont gagné
	public int getSommeForEachUserEquipe2() {
		int sommeTotal = getSommeTotal();
		int sommeRecuParChacun = sommeTotal;
		int nombreDeParticpant = listeUserPourEquipe2.size();
		if (nombreDeParticpant != 0) {
			sommeRecuParChacun = sommeTotal / nombreDeParticpant;
		}
		return sommeRecuParChacun;
	}
	
	public String getStrListUserEquipe1(){
		Set<String> keys = listeUserPourEquipe1.keySet();
		String strList = "";
		for (String key : keys) {
			//key= key+",";
			strList = strList + key+",";
		}
		return strList;
	}
	
	public String getStrListUserEquipe2(){
		Set<String> keys = listeUserPourEquipe2.keySet();
		String strList = "";
		for (String key : keys) {
			//key= key+",";
			strList = strList + key+",";
		}
		return strList;
	}
	
	
	public String getStrListUserEquipe1AvecGain(){
		Set<String> keys = listeUserPourEquipe1.keySet();
		String strList = "";
		int sommeTotaleGagnant = getSommeTotalDesGagnant(1);
		//pour controler si il y une somme
		boolean pasDeSomme = false;
		if(sommeTotaleGagnant==0){
			pasDeSomme = true;
		}
		
		int sommeUser=0;
		float sommeUserRemporte = 0;
		//key contient le name 
		for (String key : keys) {
			//key= key+",";
			sommeUser= listeUserPourEquipe1.get(key);
			if(pasDeSomme==false){
				sommeUserRemporte=getCagnotteRemportee(sommeUser,sommeTotaleGagnant);
			}
			strList = strList + key+" a "+sommeUserRemporte+",";
		}
		return strList;
	}
	
	public String getStrListUserEquipe2AvecGain(){
		Set<String> keys = listeUserPourEquipe2.keySet();
		String strList = "";
		int sommeTotaleGagnant = getSommeTotalDesGagnant(2);
		//pour controler si il y une somme
		boolean pasDeSomme = false;
		if(sommeTotaleGagnant==0){
			pasDeSomme = true;
		}
		
		int sommeUser=0;
		float sommeUserRemporte = 0;
		//key contient le name 
		for (String key : keys) {
			//key= key+",";
			sommeUser= listeUserPourEquipe2.get(key);
			if(pasDeSomme==false){
				sommeUserRemporte=getCagnotteRemportee(sommeUser,sommeTotaleGagnant);
			}
			strList = strList + key+" a "+sommeUserRemporte+",";
		}
		return strList;
	}
	
	public float getCagnotteRemportee(int sommeMisee, int sommeTotaleGagnant){
		float cagnotte;
	//	if(sommeTotaleGagnant!=0){
			int sommeTotal = getSommeTotal();
			float sommeReversee = (float) (0.75*sommeTotal);
			float percentage = ((float) sommeMisee) / sommeTotaleGagnant;
			//float div = (int) (sommeMisee/sommeTotaleGagnant);
			//int perce = Math.round(percentage);
			cagnotte = sommeReversee*percentage;
	//	}
		System.out.println("-cagnotte " + cagnotte + " sommeReversee"+ sommeReversee+" sommeMisee"+ sommeMisee+ " sommeTotaleGagnant"+sommeTotaleGagnant+ " perce"+percentage);
		return cagnotte;
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
