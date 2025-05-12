package Controleur;

import Global.Config;
import Modele.CasePlateau;
import Modele.Jeu;
import Modele.Pion;
import Vue.CollecteurEvenements;
import Vue.InterfaceUser;

import java.awt.*;

public class Mediateur implements CollecteurEvenements {
    InterfaceUser vue;
    Jeu jeu;

    public Mediateur(Jeu j) {
        this.jeu = j;
    }


    @Override
    public void clavier(String touche) {
    }

    @Override
    public void boutonTerrainJeu(CasePlateau casePlateau) {

    }


    @Override
    public void tictac() {

    }

    @Override
    public void carteSelectionne(int numCarte) {

    }

    @Override
    public void configChargerPartie(String partieSelectionee) {

    }

    @Override
    public void configNiveauIA(String niveauIA) {

    }

    @Override
    public void configNomJoueur(int num, String nom) {

    }

    @Override
    public void configModeAuto(boolean nouvelEtat) {

    }

    @Override
    public void configIAReflexion(int tempsMs) {

    }

    @Override
    public void configIAHeuristique(boolean active) {

    }

    @Override
    public void configIAAlgorithme(String nomAlgorithme) {

    }

    @Override
    public void configCouleur(Config.CiblesDesCouleurs cible, Color couleur) {

    }

    @Override
    public void configAnimationVitesse(int vitesse) {

    }

    @Override
    public void configAnimationPieces(boolean active) {

    }

    @Override
    public void configAnimationSurbrillance(boolean active) {

    }

    @Override
    public void configSonVolumeGeneral(int volume) {

    }

    @Override
    public void configSonVolumeEffets(int volume) {

    }

    @Override
    public void configSonVolumeMusique(int volume) {

    }

    @Override
    public void configSonMuet(boolean muet) {

    }

    @Override
    public void setCiblePion(int xDest, int yDest) {

    }

    @Override
    public void setCiblePion(CasePlateau casePlateau) {

    }

    @Override
    public void setPionSelectionne(int xDepart, int yDepart) {

    }

    @Override
    public void setPionSelectionne(Pion pion) {

    }

}
