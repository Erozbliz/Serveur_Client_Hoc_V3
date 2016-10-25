package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;

import org.junit.Test;

import objet.Match;
import tools.Serialisation;

public class JUnitTest {

	@Test
	public void test() throws IOException, ClassNotFoundException {
	    //TU POUR SERIALISATION ET DESERIALISATION
        //Teste l'intégriter de l' ArrayList<Match> lors de la sérialisation et la désérialisation
        byte[] sendData = new byte[1024];
        ArrayList<Match> listeDesMatchEnvoye = MyData.listeMatch();
        sendData = Serialisation.serialize2(listeDesMatchEnvoye); //Sérialisation de la liste des matchs
        byte[] receiveData = new byte[1024];
        receiveData = sendData;
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        ArrayList<Match> listeDesMatchRecu = (ArrayList<Match>) Serialisation.deserialize2(receivePacket.getData());//désérialisation de la liste des matchs
        assertNotNull("Objet not null",listeDesMatchRecu);
       // assertSame(listeDesMatchEnvoye,listeDesMatchRecu);
        for(int i=0;i<listeDesMatchRecu.size();i++){
            assertEquals(listeDesMatchEnvoye.get(i).getListeButEquipe1(),listeDesMatchRecu.get(i).getListeButEquipe1());
            assertEquals(listeDesMatchEnvoye.get(i).getListeButEquipe2(),listeDesMatchRecu.get(i).getListeButEquipe2());
        }

	}

}
