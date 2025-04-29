package Controleur;

import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;
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
    public void boutonTerrainJeu(Point btnCoords) {

    }

        // répercuter sur la vue ??????

    @Override
    public void tictac() {

    }

    @Override
    public void carteSelectionne(int numCarte) {

    }
}
