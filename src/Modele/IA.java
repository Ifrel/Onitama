package Modele;

import java.util.logging.Logger;

import static Global.Config.DIFFICULTE_IA;

public class IA {
    private Jeu jeu;
    private DIFFICULTE_IA difficulte;
    private static final Logger logger = Logger.getLogger(IA.class.getName());

    public IA(Jeu jeu, DIFFICULTE_IA niveau) {
        this.jeu = jeu;
        this.difficulte = niveau;
    }

    /**
     * L'IA joue un coup conforme à la difficulté attribuée
     */
    public void jouer() {
        Coup c = calculerCoup();

        // jeu.jouerCoup(c);
        // logger.info("L'IA de niveau + " + difficulte.name() + "vient de jouer le coup calculé");
    }

    /**
     * L'IA suggère un coup conforme à la difficulté attribuée
     * (possible seulement s'il y a au moins 1 joueur humain).
     */
    public Coup suggererCoup() {
        return calculerCoup();
    }

    /**
     * Calcule un coup à jouer ou à suggérer
     * @return Le coup calculé par l'IA
     */
    private Coup calculerCoup() {

        Coup c = null;

        logger.info("Calcul d'un coup à jouer par l'IA avec une (difficulté = " + difficulte.name() + ")");
        switch(difficulte) {
            case FACILE:
                c = coupRandom();
                break;
            case MOYEN:
                c = coupIntermediaire();
                break;
            case DIFFICILE:
                c = coupFort();
                break;
        }
        logger.info("Coup calculé par l'IA : " + (c != null ? c.toString() : "null"));

        return c;
    }

    /**
     * Change la difficulté de l'IA en cours de partie
     * @param difficulte nouveau niveau de difficulté de l'IA
     */
    public void changerDifficulte(DIFFICULTE_IA difficulte) {
        this.difficulte = difficulte;
    }

    /**
     * Joue un coup aléatoire de niveau facile
     * @return un coup aléatoire
     */
    private Coup coupRandom() {
        return null;
    }

    /**
     * Joue un coup de niveau Intermédiaire (Gagnant ou Perdant)
     * @return un coup de niveau Intermédiaire
     */
    private Coup coupIntermediaire() {
        return null;
    }

    /**
     * Joue un coup de niveau Fort (Arbre Min/Max)
     * @return un coup de niveau Fort
     */
    private Coup coupFort() {
        return null;
    }

   // private getConfiguration() {}
}
