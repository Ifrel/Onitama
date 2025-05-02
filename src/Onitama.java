import Controleur.Mediateur;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

public class Onitama {
    public static void main(String[] args) {
//        Jeu jeu = new Jeu();
        Jeu jeu = null;
        CollecteurEvenements collecteurEvenements =  new Mediateur(jeu);
        new InterfaceGraphique(jeu, collecteurEvenements).lancer();

        try {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}