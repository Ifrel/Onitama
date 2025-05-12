package Modele.IA;

import Modele.Coup;
import Modele.Jeu;


public abstract class IA {

    /**
     * Calcul un Coup à suggérer ou à jouer
     * @return Coup calculé
     */
    abstract Coup calculerCoup();
}
