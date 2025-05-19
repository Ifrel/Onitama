package Controleur;

import Modele.Coup;
import Vue.Animations.AnimationUtils.CardFlipAnimator;
import Vue.CollecteurEvenements;
import Vue.EcranPlateauDeJeu;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Global.Config.ID_JOUEUR_1;
import static Vue.ConfigUI.COULEUR_FOND_PION_DEPART_SELECTIONE;


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
        ecranPlateauDeJeu.getBoutonterrainAt(coupPossible.get(0).getDepart()).chargerCouleurFont(COULEUR_FOND_PION_DEPART_SELECTIONE);
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
            ecranPlateauDeJeu.getBoutonterrainAt(coupPrecedant.get(0).getDepart()).enleverCouleurFont();
            peutDesactiver = false;
        }
    }

    @Override
    public void activeAnimationDeRotation(CardFlipAnimator animator, int idJoueurCourant){
        if (idJoueurCourant == ID_JOUEUR_1) {
//            CardFlipAnimator animator = new CardFlipAnimator();                 // 1. Créer un nouvel animateur pour ce bouton
//            CardFlipLayerUI<JButton> layerUI = new CardFlipLayerUI<>(animator);  // 2. Créer un LayerUI qui utilisera cet animateur
//            JLayer<JButton> layer = new JLayer<>(boutonCarte, layerUI);           // 3. Créer un JLayer, enveloppant le bouton original avec le LayerUI
                animator.startAnimation();
//
//            // 5. Ajouter un écouteur d'animation à l'animateur
//            // Chaque fois que l'animateur met à jour son angle, il notifie ce listener
//            // qui demande alors au JLayer de se repeindre.
//            animator.addAnimationListener(layer::repaint); // Lambda capture 'layer'
        }
    }

}
