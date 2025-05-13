package Modele.IA;

import Global.Config.NIVEAU_IA;
import Modele.Coup;
import Modele.Jeu;

import static Global.Config.NIVEAU_IA.FORT;

public class IAFort extends IA {
    private final Jeu jeu;

    public IAFort(Jeu jeu) {
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
     * Renvoie le niveau de l'IA
     *
     * @return FAIBLE | MOYEN | FORT
     */
    @Override
    public NIVEAU_IA getNiveau() {
        return FORT;
    }
}
