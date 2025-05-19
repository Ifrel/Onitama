package Vue.Adaptateurs;

import Modele.CasePlateau;
import Modele.Coup;
import Modele.Jeu;
import Patterns.Observateur;
import Vue.CollecteurEvenements;
import Vue.EcranPlateauDeJeu;
import Vue.Utils.Boutons.BoutonTerrain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static Modele.CasePlateau.TYPE_ELEMENT_SUR_CASE.VIDE;

public class AdaptateurBoutonTerrain implements ActionListener, Observateur {
    private final Jeu jeu;
    private final BoutonTerrain boutonTerrain;
    private final CasePlateau casePlateau;
    private final CollecteurEvenements collecteurEv;
    private final EcranPlateauDeJeu ecranPlateauDeJeu;
    private List<Coup> coupPossible;

    public  AdaptateurBoutonTerrain(
                                    BoutonTerrain boutonTerrain,
                                    CasePlateau casePlateau,
                                    CollecteurEvenements collecteurEv,
                                    EcranPlateauDeJeu ecranPlateauDeJeu){
        this.boutonTerrain = boutonTerrain;
        this.casePlateau = casePlateau;
        this.collecteurEv = collecteurEv;
        this.ecranPlateauDeJeu = ecranPlateauDeJeu;
        this.jeu = ecranPlateauDeJeu.getJeu();
        this.coupPossible = jeu.getCoupsPossibles(jeu.getCarteSelectionnee(), casePlateau.getCoordonnee());

        jeu.ajouteObservateur(this);
        miseAJour();
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        System.err.println("bouton: case pressé: "+casePlateau.getCoordonnee());
        collecteurEv.setCaseSelectionnee(casePlateau.getCoordonnee());

        desactiveAnimationCible();
        activeAnimationCible();
    }




    private void activeAnimationCible(){
        if (casePlateau.getTypeElement() != VIDE && casePlateau.getProprietaire() == jeu.getJoueurCourant().getId()) {
            if (!coupPossible.isEmpty()) {
                collecteurEv.getCollecteurAnimation().activeCibleBoutonTerrain(coupPossible, ecranPlateauDeJeu);
            }
        }
    }

    private void desactiveAnimationCible(){
        if (casePlateau.getTypeElement() != VIDE) {
            collecteurEv.getCollecteurAnimation().desactiveCibleBoutonTerrain(coupPossible);
        }
    }


    @Override
    public void miseAJour() {
        this.coupPossible = jeu.getCoupsPossibles(jeu.getCarteSelectionnee(), casePlateau.getCoordonnee());
        desactiveAnimationCible();
    }
}
