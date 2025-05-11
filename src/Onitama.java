import Controleur.Mediateur;
import Global.LogManagerSetup;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;
import Vue.InterfaceTextuelle;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Onitama {
    private static final Logger logger = Logger.getLogger(Onitama.class.getName());

    public static void main(String[] args) {
        try {
            // Choisir les niveaux :
            // → Console : INFO et plus grave
            // → Fichier : tout (ALL)
            LogManagerSetup.setupLogger(Level.INFO, Level.INFO);

            logger.info("Lancement du modèle du jeu");
            Jeu jeu = new Jeu();

            logger.info("Initialisation du collecteur d'évènements");
            CollecteurEvenements collecteurEvenements = new Mediateur(jeu);

//            logger.info("Lancement de l'interface graphique du jeu");
//            InterfaceGraphique.lancerInterfaceGraphique(jeu, collecteurEvenements);

            logger.info("Lancement de l'interface Textuelle du jeu");
            InterfaceTextuelle.lancerInterfaceTextuelle(jeu, collecteurEvenements);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur au démarrage de l'application : " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
