package Vue.Adaptateurs;

import Modele.CasePlateau;
import Modele.Coup;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.EcranPlateauDeJeu;
import Vue.Utils.Boutons.BoutonTerrain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static Modele.CasePlateau.TYPE_ELEMENT_SUR_CASE.VIDE;

public class AdaptateurBoutonTerrain implements ActionListener {
    private final Jeu jeu;
    private final BoutonTerrain boutonTerrain;
    private final CasePlateau casePlateau;
    private final CollecteurEvenements collecteurEv;
    private final EcranPlateauDeJeu ecranPlateauDeJeu;
    private final List<Coup> coupPossible;

    public  AdaptateurBoutonTerrain(BoutonTerrain boutonTerrain, CasePlateau casePlateau, CollecteurEvenements collecteurEv, EcranPlateauDeJeu ecranPlateauDeJeu){
        this.boutonTerrain = boutonTerrain;
        this.casePlateau = casePlateau;
        this.collecteurEv = collecteurEv;
        this.ecranPlateauDeJeu = ecranPlateauDeJeu;
        this.jeu = ecranPlateauDeJeu.getJeu();
        this.coupPossible = jeu.getCoupsPossibles(jeu.getCarteSelectionnee(), casePlateau.getCoordonnee());
        mettreAJour();
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        System.err.println("bouton: case pressé: "+casePlateau.getCoordonnee());
        collecteurEv.setCaseSelectionnee(casePlateau.getCoordonnee());

        activeAnimation();

    }



    private void mettreAJour() {
        desactiveAnimation();
    }



    private void activeAnimation(){
        if (casePlateau.getTypeElement() != VIDE) {
            if (!coupPossible.isEmpty()) {
                collecteurEv.getCollecteurAnimation().activeCibleBoutonTerrain(coupPossible, ecranPlateauDeJeu);
            }
        }
    }

    private void desactiveAnimation(){
        if (casePlateau.getTypeElement() != VIDE) {
            if (!coupPossible.isEmpty()) {
                collecteurEv.getCollecteurAnimation().desactiveCibleBoutonTerrain(coupPossible);
            }
        }
    }
}
