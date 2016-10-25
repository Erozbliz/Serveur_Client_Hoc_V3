package test;

import java.util.ArrayList;

import objet.Match;
import tools.Tools;

public final class MyData {


    public static ArrayList<Match> listeMatch() {
        ArrayList<Match> listeDesMatch = new ArrayList<Match>();
        String strDate = Tools.currentStrDate();
        listeDesMatch.add(new Match(0, strDate, "red1", "blue1"));
        listeDesMatch.add(new Match(1, strDate, "red2", "blue2"));
        listeDesMatch.add(new Match(2, strDate, "red3", "blue3"));
        listeDesMatch.add(new Match(3, strDate, "red4", "blue4"));//ajout match
        listeDesMatch.add(new Match(4, strDate, "red5", "blue5"));//ajout match
        listeDesMatch.add(new Match(5, strDate, "red6", "blue6"));//ajout match
        listeDesMatch.add(new Match(6, strDate, "red7", "blue7"));//ajout match
        listeDesMatch.add(new Match(7, strDate, "red8", "blue8"));//ajout match
        listeDesMatch.add(new Match(8, strDate, "red9", "blue9"));//ajout match
        listeDesMatch.add(new Match(9, strDate, "red10", "blue10"));//ajout match
        listeDesMatch.add(new Match(10, strDate, "red11", "blue11"));//ajout match
        for(int i=0 ; i<listeDesMatch.size();i++){
            listeDesMatch.get(i).butPourEquipe1(strDate);
            listeDesMatch.get(i).butPourEquipe1(strDate);
        }
        return listeDesMatch;
    }
}
