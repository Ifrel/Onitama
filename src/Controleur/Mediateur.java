package Controleur;

import Global.Config;
import Modele.*;
import Vue.CollecteurEvenements;
import Vue.EcranPlateauDeJeu;
import Vue.InfosDeConfigUI;
import Vue.InterfaceUser;

import java.awt.*;
import java.util.logging.Logger;

public class Mediateur implements CollecteurEvenements {
    private final Jeu jeu;
    private InterfaceUser vue;

    // modélise un coup complet joué
    Carte carteSelectionne;
    Pion pionSelectionne;
    Coup coordDeDeplacement;


    private final InfosDeConfigUI infosDeConfigUI = InfosDeConfigUI.getInstance();
    private static final Logger logger = Logger.getLogger(Mediateur.class.getName());


    public Mediateur(Jeu j) {
        this.jeu = j;
        resetDataCoup();
    }


    @Override
    public void clavier(String touche) {
        switch (touche){
            case "exit":
                jeu.setTerminerJeu();
                System.exit(0);
                break;
            case "annuler":
                jeu.annulerCoup();
                break;
            case "refaire":
                jeu.refaireCoup();
                break;
            case "nouvellepartie":
                jeu.nouvellePartie();
                break;
            case "sauvegarder":
                jeu.sauvegarderJeu();
            case "Pause":
                jeu.setPause();
                break;
            case "IA":
                jeu.basculeIA();
                break;
            case "Full":
                vue.toggleFullScreen();
                break;
            default:
                logger.severe("Touche inconnue : " + touche);
                break;
        }
    }

    @Override
    public void tictac() {
        //TODO
    }

    @Override
    public void setCiblePion(CasePlateau casePlateau) {
        jeu.setCasePlateauCible(casePlateau);
    }

    @Override
    public void setCiblePion(int xDest, int yDest) {
        jeu.setCasePlateauCible(xDest, yDest);
    }

    @Override
    public void setCaseSelectionnee(int xDepart, int yDepart) {
        jeu.setPionSelectionne(xDepart, yDepart);
    }

    @Override
    public void setCaseSelectionnee(Point coordonnePion){ jeu.selectionneCase(coordonnePion);};


    @Override
    public void boutonTerrainJeu(CasePlateau casePlateau) {
        jeu.setCasePlateauCible(casePlateau);
    }

    @Override
    public void setCarteSelectionne(int idCarte) { jeu.setCarteSelectionnee(idCarte); }

    @Override
    public void setNouvellePartie(String partieSelectionee) {
        jeu.setNouvellePartie(partieSelectionee);

    }



    /****************************************
     *  Méthodes de gestion du controleur
     *  ************************************/
     private boolean estCoupComplet(){
         return carteSelectionne != null && pionSelectionne != null && coordDeDeplacement != null;
     }

     private void resetDataCoup(){
         carteSelectionne = null;
         pionSelectionne = null;
         coordDeDeplacement = null;
     }
}
