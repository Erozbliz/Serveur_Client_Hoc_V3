package client;

import java.util.TimerTask;

/**
 * On appel une methode toutes les x secondes
 *
 */
public class TimeTask extends TimerTask {
	public void run() {
		System.out.println("auto-refresh");
		try {
			ClientUI.messageUDP("score");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
