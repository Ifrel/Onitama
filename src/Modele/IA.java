package Modele;

import java.util.logging.Logger;

import static Global.Config.NIVEAU_IA;

public class IA {
    private Jeu jeu;
    private NIVEAU_IA niveau;
    private static final Logger logger = Logger.getLogger(IA.class.getName());

    public IA(Jeu jeu, NIVEAU_IA niveau) {
        this.jeu = jeu;
        this.niveau = niveau;
    }

    /**
     * L'IA joue un coup conforme au niveau attribué
     */
    public void jouer() {
        Coup c = calculerCoup();

        // jeu.jouerCoup(c);
        // logger.info("L'IA de niveau + " + niveau.name() + "vient de jouer le coup calculé");
    }

    /**
     * L'IA suggère un coup conforme au niveau attribué
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

        logger.info("Calcul d'un coup à jouer par l'IA avec une (niveau = " + niveau.name() + ")");
        switch(niveau) {
            case FAIBLE:
                c = coupRandom();
                break;
            case MOYEN:
                c = coupMoyen();
                break;
            case FORT:
                c = coupFort();
                break;
        }
        logger.info("Coup calculé par l'IA : " + (c != null ? c.toString() : "null"));

        return c;
    }

    /**
     * Change la niveau de l'IA en cours de partie
     * @param niveau nouveau niveau de l'IA
     */
    public void setNiveau(NIVEAU_IA niveau) {
        this.niveau = niveau;
    }

    /**
     * Renvoie le niveau courant de l'IA FAIBLE | MOYEN | FORT
     * @return Le niveau courant de l'IA
     */
    public NIVEAU_IA getNiveau() {
        return this.niveau;
    }

    /**
     * Joue un coup aléatoire de niveau FAIBLE
     * @return un coup aléatoire
     */
    private Coup coupRandom() {
        return null;
    }

    /**
     * Joue un coup de niveau Moyen (Gagnant ou Perdant)
     * @return un coup de niveau Moyen
     */
    private Coup coupMoyen() {
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
