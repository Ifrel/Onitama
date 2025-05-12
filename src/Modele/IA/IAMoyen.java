package Modele.IA;

import Modele.Coup;
import Modele.Jeu;

public class IAMoyen extends IA {
    private Jeu jeu;

    public IAMoyen(Jeu jeu) {
        this.jeu = jeu;
    }

    /**
     * Calcul un Coup à suggérer ou à jouer
     * @return Coup calculé
     */
    @Override
    Coup calculerCoup() {
        return null;
    }
}
