package Modele.IA;

import Global.Config.NIVEAU_IA;
import Modele.Coup;
import Modele.Jeu;
import Modele.Pion;

import static Global.Config.NIVEAU_IA.FORT;

public class IAFort extends IA {
    private final Jeu jeu;

    public IAFort(Jeu jeu, int id, String nom) {
        super(id, nom);
        this.jeu = jeu;
    }

    /**
     * Calcul un Coup à suggérer ou à jouer
     *
     * @return Coup calculé, null si aucun Coup possible
     */
    @Override
    public Coup calculerCoup() {
        return null;
    }

    /**
     * Renvoie le pion choisi par l'IA
     *
     * @return Référence du pion choisi
     */
    @Override
    public Pion getPionChoisi() {
        return null;
    }

    /**
     * Renvoie la carte choisie (relative au joueur courant)
     *
     * @return la carte choisie (0 ou 1)
     */
    @Override
    public int getCarteChoisie() {
        return 0;
    }

    /**
     * Vérifie si l'IA est en train de réfléchir (d'effectuer des calculs)
     *
     * @return vrai si l'IA réfléchit, faux sinon
     */
    @Override
    public boolean isThinking() {
        return false;
    }

    /**
     * Renvoie le niveau de l'IA
     *
     * @return FAIBLE | MOYEN | FORT
     */
    @Override
    public NIVEAU_IA getNiveau() {
        return FORT;
    }
}
