package Modele;

import static Global.Config.DIFFICULTE_IA;

public class IA {
    private Jeu jeu;
    private DIFFICULTE_IA difficulte;

    public IA(Jeu jeu, DIFFICULTE_IA niveau) {
        this.jeu = jeu;
        this.difficulte = niveau;
    }

    /**
     * L'IA joue un coup conforme à la difficulté attribuée
     */
    public void jouer() {
        Coup c;

        switch (difficulte) {
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
