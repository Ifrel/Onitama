package Controleur;

import Modele.CasePlateau;
import Modele.Coup;
import Vue.CollecteurEvenements;
import Vue.EcranPlateauDeJeu;
import Vue.Utils.Boutons.BoutonTerrain;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class ControleurAnimation implements CollecteurEvenements {
    private boolean peutDesactiver;
    private List<Coup> coupPrecedant;
    EcranPlateauDeJeu ecranPlateauDeJeu;

    public ControleurAnimation(){
        peutDesactiver = false;
        coupPrecedant = new ArrayList<>();
    }

    @Override
    public void clavier(String t) {
    }

    @Override
    public void tictac() {
    }


    @Override
    public void activeCibleBoutonTerrain(List<Coup> coupPossible, EcranPlateauDeJeu ecranPlateauDeJeu) {
        this.ecranPlateauDeJeu = ecranPlateauDeJeu;
        coupPrecedant.clear();
        coupPrecedant.addAll(coupPossible);

        for (Coup coup : coupPossible) {
            Point cible = coup.getArrivee();
            ecranPlateauDeJeu.getBoutonterrainAt(cible).activerAnimationBordure(true);
            System.err.println("bouton animation activé:" + cible);
        }

        peutDesactiver = true;
    }


    @Override
    public void desactiveCibleBoutonTerrain(List<Coup> coupPossible){
        if (peutDesactiver){
            for (Coup coup : coupPossible) {
                Point cible = coup.getArrivee();
                ecranPlateauDeJeu.getBoutonterrainAt(cible).activerAnimationBordure(false);
                System.err.println("bouton animation désactivé:" + cible);
            }

            peutDesactiver = false;
        }
    }

}
