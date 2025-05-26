package Controleur;

import Modele.Coup;
import Vue.Animations.AnimationUtils.CardFlipAnimator;
import Vue.CollecteurEvenements;
import Vue.EcranPlateauDeJeu;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Global.Config.ID_JOUEUR_1;
import static Vue.Configuration.ConfigUI.COULEUR_FOND_PION_DEPART_SELECTIONE;


public class ControleurAnimation implements CollecteurEvenements {
    private boolean peutDesactiver;
    private final List<Coup> coupPrecedant;
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
            ecranPlateauDeJeu.getBoutonterrainAt(cible).activerAnimation(true);
            System.err.println("bouton animation activé:" + cible);
        }

        ecranPlateauDeJeu.getBoutonterrainAt(coupPossible.get(0).getDepart()).chargerCouleurFond(COULEUR_FOND_PION_DEPART_SELECTIONE);
        peutDesactiver = true;
    }

    @Override
    public void desactiveCibleBoutonTerrain(List<Coup> coupPossible){
        if (peutDesactiver){
            for (Coup coup : coupPrecedant) {
                Point cible = coup.getArrivee();
                ecranPlateauDeJeu.getBoutonterrainAt(cible).activerAnimation(false);
                System.err.println("bouton animation désactivé:" + cible);
            }

            ecranPlateauDeJeu.getBoutonterrainAt(coupPrecedant.get(0).getDepart()).enleverCouleurFond();
            peutDesactiver = false;
        }
    }




    @Override
    public void activeAnimationDeRotation(CardFlipAnimator animator, int idJoueurCourant){
        if (idJoueurCourant == ID_JOUEUR_1) {
                animator.startAnimation();
        }
    }

}
