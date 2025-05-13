package Modele.IA;

import static Global.Config.NIVEAU_IA.*;
import Global.Config.NIVEAU_IA;
import Global.Config;
import Modele.Coup;
import Modele.Jeu;

public class IAFort extends IA {
    private Jeu jeu;

    public IAFort(Jeu jeu) {
        this.jeu = jeu;
    }

    /**
     * Calcul un Coup à suggérer ou à jouer
     * @return Coup calculé, null si aucun Coup possible
     */
    @Override
    Coup calculerCoup() {
        return null;
    }

    /**
     * Renvoie le niveau de l'IA
     * @return FAIBLE | MOYEN | FORT
     */
    @Override
    NIVEAU_IA getNiveau() {
        return FORT;
    }
}
