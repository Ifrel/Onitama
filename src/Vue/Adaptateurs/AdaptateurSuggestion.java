package Vue.Adaptateurs;

import Modele.Coup;
import Modele.Jeu;
import Patterns.Observateur;
import Vue.CollecteurEvenements;
import Vue.EcranPlateauDeJeu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import static Modele.CasePlateau.TYPE_ELEMENT_SUR_CASE.VIDE;

public class AdaptateurSuggestion implements ActionListener, Observateur {
    private final Jeu jeu;
    private final CollecteurEvenements collecteurEv;
    private final EcranPlateauDeJeu ecranPlateauDeJeu;
    Coup coup;

    public AdaptateurSuggestion(CollecteurEvenements collecteurEvent, EcranPlateauDeJeu ecranPlateauDeJeu){
        this.ecranPlateauDeJeu = ecranPlateauDeJeu;
        this.jeu = ecranPlateauDeJeu.getJeu();
        this.collecteurEv = collecteurEvent.getCollecteurAnimation();

        jeu.ajouteObservateur(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        coup = jeu.suggererCoup();
        jeu.selectionneCase(coup.getDepart());
        collecteurEv.activeSuggestion(coup, ecranPlateauDeJeu);

        System.err.println("Adaptateur suggestion: actionPerformed");
    }


    @Override
    public void miseAJour() {
        collecteurEv.desactiveSuggestion(coup, ecranPlateauDeJeu);
    }
}
