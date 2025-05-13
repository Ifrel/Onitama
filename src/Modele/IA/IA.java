package Modele.IA;

import Global.Config.NIVEAU_IA;
import Modele.Coup;


public abstract class IA {

    /**
     * Calcul un Coup à suggérer ou à jouer
     * @return Coup calculé, null si aucun Coup possible
     */
    abstract Coup calculerCoup();


    /**
     * Renvoie le niveau de l'IA
     * @return FAIBLE | MOYEN | FORT
     */
    abstract NIVEAU_IA getNiveau();
}
