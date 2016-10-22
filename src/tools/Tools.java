package tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Classe qui contient des outils
 *
 */
public final class Tools {

	/**
	 * Savoir si un string est un int
	 * @param s
	 * @return true si int sinon false
	 */
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
	
	/**
	 *  G�nere un string al�atoire qui commence par user
	 * @return user111
	 */
	public static String randUser(){
		String user = "user";
		Random generator = new Random();
		int i = generator.nextInt(999) + 1;
		String is = String.valueOf(i);
		user = user.concat(is);
		return user;
	}
	
	/**
	 *  G�nere un string al�atoire qui contient que des chiffre
	 * @return 156
	 */
	public static String randString(){
		String user = "1";
		Random generator = new Random();
		int i = generator.nextInt(999) + 1;
		String is = String.valueOf(i);
		user = user.concat(is);
		return user;
	}
	
	/**
	 * 
	 * @return la date actuelle au format string
	 */
	public static String currentStrDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date(); 
		return dateFormat.format(date);
	}

}
