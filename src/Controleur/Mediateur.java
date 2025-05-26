package Controleur;

import Modele.CasePlateau;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceUser;
import Vue.Utils.AfficheReglesPDF;

import java.awt.*;
import java.util.logging.Logger;

import static Vue.Utils.MethodsStaticsUtils.afficherFonctionEnCours;

public class Mediateur implements CollecteurEvenements {
    private final Jeu jeu;
    private InterfaceUser vue;

    private CollecteurEvenements controleurAnimation;

    private static final Logger logger = Logger.getLogger(Mediateur.class.getName());

    public Mediateur(Jeu j) {
        this.jeu = j;
        controleurAnimation = new ControleurAnimation();
    }


    @Override
    public void clavier(String touche) {
        try {
            switch (touche) {
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
                case "reprendre":
                case "didacticiel":
                case "mesParties":
                    afficherFonctionEnCours();
                    break;
                case "nouvellePartie":
                    jeu.nouvellePartie();
                    break;
                case "sauvegarder":
                    jeu.sauvegarderJeu();
                    break;
                case "regles":
                    // Recherche de la fenêtre ayant le focus
                    Window fenetreActive = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
                    AfficheReglesPDF.ouvrirReglesPDFExterne(fenetreActive);
                    break;
                case "charger":
                    jeu.chargerJeu();
                    break;
                case "pause":
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
        } catch (Exception e) {
            throw new RuntimeException(e);
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
        jeu.selectionneCase(new Point(xDest, yDest));
    }

    @Override
    public void setCaseSelectionnee(int xDepart, int yDepart) {
        jeu.selectionneCase(new Point(xDepart, yDepart));
    }

    @Override
    public void setCaseSelectionnee(Point coordonnePion) {
        jeu.selectionneCase(coordonnePion);
    }

    ;


    @Override
    public void boutonTerrainJeu(CasePlateau casePlateau) {
        jeu.setCasePlateauCible(casePlateau);
    }

    @Override
    public void setCarteSelectionne(int idCarte) {
        jeu.setCarteSelectionnee(idCarte);
    }

    @Override
    public void setNouvellePartie(String partieSelectionee) {
        jeu.setNouvellePartie(partieSelectionee);

    }




    /****************************************
     *  Méthodes de gestion du controleur
     *  ************************************/
    @Override
    public CollecteurEvenements getCollecteurAnimation(){
        return controleurAnimation;
    }
}
