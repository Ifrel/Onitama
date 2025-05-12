package Modele.IA;

import Modele.Coup;
import Modele.Jeu;


public abstract class IA {

    /**
     * Calcul un Coup à suggérer ou à jouer
     * @return Coup calculé, null si aucun Coup possible
     */
    abstract Coup calculerCoup();
}
