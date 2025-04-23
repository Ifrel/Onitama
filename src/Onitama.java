import Controleur.Mediateur;
import Global.Config;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

public class Onitama {
    public static void main(String[] args) {
        Jeu j = null;
        CollecteurEvenements collecteurEvent;
        try {
            if (args.length == 0) {
                j = new Jeu(6, 8);
            } else if (args.length == 2) {
                j = new Jeu(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            } else {
                System.err.println("Usage : java Onitama <lignes> <colonnes>");
                System.exit(1);
            }
            if (Config.graphique) {
                collecteurEvent = new Mediateur(j);
                new InterfaceGraphique(j, collecteurEvent);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}