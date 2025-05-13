package Controleur;

import Global.Config;
import Modele.Carte;
import Modele.CasePlateau;
import Modele.Jeu;
import Modele.Pion;
import Vue.CollecteurEvenements;
import Vue.InfosDeConfigUI;
import Vue.InterfaceUser;

import java.awt.*;
import java.util.logging.Logger;

public class Mediateur implements CollecteurEvenements {
    private final Jeu jeu;
    private InterfaceUser vue;
    private final InfosDeConfigUI infosDeConfigUI = InfosDeConfigUI.getInstance();
    private static final Logger logger = Logger.getLogger(Mediateur.class.getName());


    public Mediateur(Jeu j) {this.jeu = j;   }


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
    public void boutonTerrainJeu(CasePlateau casePlateau) {
        jeu.setCasePlateauCible(casePlateau);
    }


    @Override
    public void tictac() {
        //TODO
    }

    @Override
    public void carteSelectionne(Carte carte) {
        jeu.setCarteSelectionne(carte);
    }

    @Override
    public void setNouvellePartie(String partieSelectionee) {
        jeu.setNouvellePartie(partieSelectionee);

    }

    @Override
    public void setNiveauIA(String niveauIA) {
        jeu.setNiveauIA(niveauIA);

    }

    @Override
    public void setNomJoueur(int num, String nom) {
        if (num == 1) jeu.setNomJoueur1(nom);
        else jeu.setNomJoueur2(nom);
    }

    @Override
    public void setModeAuto(boolean nouvelEtat) {
        jeu.setModeAuto(nouvelEtat);
    }

    @Override
    public void setIAReflexion(int tempsMs) {
        jeu.setIAReflexion(tempsMs);
    }

    @Override
    public void setIAHeuristique(boolean active) {
        jeu.setIAHeuristique(active);
    }

    @Override
    public void setIAAlgorithme(String nomAlgorithme) {
        jeu.setIAAlgorithme(nomAlgorithme);

    }

    @Override
    public void setCouleur(Config.CiblesDesCouleurs cible, Color couleur) {
        switch (cible){
            case BLOC_MENU:
                infosDeConfigUI.setCouleurBlocMenu1(couleur);
                break;
            case CASE_TERRAIN:
                infosDeConfigUI.setCouleurCaseTerrain(couleur);
                break;
            case PLATEAU_DE_JEU:
                infosDeConfigUI.setCouleurPlateauDejeu(couleur);
                break;
            case CASE_ELEVE_JOUEUR_1:
                infosDeConfigUI.setCouleurCaseEleveJoueur1(couleur);
                break;
            case CASE_ELEVE_JOUEUR_2:
                infosDeConfigUI.setCouleurCaseEleveJoueur2(couleur);
                break;
            case CASE_MAITRE_JOUEUR_1:
                infosDeConfigUI.setCouleurCaseMaitreJoueur1(couleur);
                break;
            case CASE_MAITRE_JOUEUR_2:
                infosDeConfigUI.setCouleurCaseMaitreJoueur2(couleur);
                break;
            default:break;
        }

    }

    @Override
    public void setAnimationVitesse(int vitesse) {
        infosDeConfigUI.setVitesseAnimation(vitesse);
    }

    @Override
    public void setAnimationPieces(boolean active) {
        infosDeConfigUI.setAnimerDeplacementPiece(active);
    }

    @Override
    public void setAnimationSurbrillance(boolean active) {
        infosDeConfigUI.setAnimerSurbrillace(active);
    }

    @Override
    public void setSonVolumeGeneral(int volume) {
        infosDeConfigUI.setVolumeGeneral(volume);
    }

    @Override
    public void setSonVolumeEffets(int volume) {
        infosDeConfigUI.setVolumeEffetSonore(volume);
    }

    @Override
    public void setSonVolumeMusique(int volume) {
        infosDeConfigUI.setVolumeMusique(volume);
    }

    @Override
    public void setSonMuet(boolean muet) {
        infosDeConfigUI.setCouperToutSon(muet);
    }

    @Override
    public void setCiblePion(int xDest, int yDest) {
        jeu.setCasePlateauCible(xDest, yDest);
    }

    @Override
    public void setCiblePion(CasePlateau casePlateau) {
        jeu.setCasePlateauCible(casePlateau);
    }

    @Override
    public void setPionSelectionne(int xDepart, int yDepart) {
        jeu.setPionSelectionne(xDepart, yDepart);
    }

    @Override
    public void setPionSelectionne(Pion pion) {
        jeu.setPionSelectionne(pion);
    }

}
