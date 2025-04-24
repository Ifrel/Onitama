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
        switch (touche) {
            case "Annuler":
            case "Undo":
                jeu.annulerCoup();
                break;
            case "Refaire":
            case "Redo":
                jeu.refaireCoup();
                break;
            case "Sauve":
                jeu.sauvegarderJeu("sauvegarde.txt");
                break;
            case "NewPartie":
//                jeu.recommencer(10,10);
                jeu.recommencer();
                break;
            case "Reprendre":
                jeu.chargerJeu("sauvegarde.txt");
                break;
            case "Quit":
                System.exit(0);
                break;
            case "IA":
                //jeu.toggleIA();
                break;
            case "Full":
                //vue.toggleFullScreen();
                break;
            default:
                System.out.println("Touche inconnue : " + touche);
        }
    }


    @Override
    public void buttonGrille(Point btnCoords) {
        jeu.jouer(btnCoords.x, btnCoords.y);
        System.err.println("Coup joué en " + btnCoords.x + ", " + btnCoords.y);
        System.err.println("---------------------------");
        System.err.println(jeu.toString());



        // répercuter sur la vue ??????
    }

    @Override
    public void tictac() {

    }
}
