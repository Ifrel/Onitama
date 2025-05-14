package Modele.IA;

import Global.Config.NIVEAU_IA;
import Global.Config.VITESSE_IA;
import Modele.Coup;


public abstract class IA {
    private VITESSE_IA vitesseIa;

    /**
     * Calcul un Coup à suggérer ou à jouer
     *
     * @return Coup calculé, null si aucun Coup possible
     */
    public abstract Coup calculerCoup();


    /**
     * Renvoie le niveau de l'IA
     *
     * @return FAIBLE | MOYEN | FORT
     */
    public abstract NIVEAU_IA getNiveau();

    /**
     * Renvoie la vitesse courante de l'IA
     *
     * @return LENTE | MOYENNE | RAPIDE
     */
    public VITESSE_IA getVitesse() {
        return vitesseIa;
    }

    /**
     * Défini la vitesse de calcul de l'IA
     *
     * @param vitesse LENTE | MOYENNE | RAPIDE
     */
    public void setVitesse(VITESSE_IA vitesse) {
        vitesseIa = vitesse;
    }
}
