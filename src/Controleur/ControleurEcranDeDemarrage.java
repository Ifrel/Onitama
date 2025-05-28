package Controleur;

import Global.Config;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.Configuration.InfosDeConfigUI;
import Vue.Utils.AfficheReglesPDF;

import java.awt.*;
import java.util.logging.Logger;

import static Global.Config.CiblesDesCouleurs.PION_TERRAIN_JOUEUR_1;
import static Global.Config.CiblesDesCouleurs.PION_TERRAIN_JOUEUR_2;
import static Global.Config.ID_JOUEUR_1;
import static Global.Config.ID_JOUEUR_2;
import static Global.Config.NIVEAU_IA.*;

public class ControleurEcranDeDemarrage implements CollecteurEvenements {
    private final Jeu jeu;
    private final InfosDeConfigUI infosDeConfigUI = InfosDeConfigUI.getInstance();
    Logger LOGGER = Logger.getLogger(ControleurEcranDeDemarrage.class.getName());

    public ControleurEcranDeDemarrage(Jeu jeu){
        this.jeu = jeu;
    }

    @Override
    public void clavier(String commande) {
        if (commande.equals("regles")) {// Recherche de la fenêtre ayant le focus
            Window fenetreActive = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
            AfficheReglesPDF.ouvrirReglesPDFExterne(fenetreActive);
        } else {
            LOGGER.severe("Commande inconnue : " + commande);
        }
    }

    @Override
    public void tictac() {
        //TODO
    }

    @Override
    public void setNiveauIA(String niveauIA) {
        switch (niveauIA){
            case "Faible":
                jeu.setNiveauIA1(FAIBLE);
                break;
            case "Moyen":
                jeu.setNiveauIA1(MOYEN);
                break;
            case "Fort":
                jeu.setNiveauIA1(FORT);
                break;
            default: break;
        }

    }

    @Override
    public void setNomJoueur(int num, String nom) {
        if (num == 1) jeu.setNomJoueur1(nom);
        else jeu.setNomJoueur2(nom);
    }

    @Override
    public void setModeAuto() {
            jeu.toggleIA1();
            jeu.toggleIA2();
    }

     @Override
    public void setCouleur(Config.CiblesDesCouleurs cible, Color couleur) {
        System.err.println("couleur: " + couleur + " cible: " + cible);
        switch (cible){
            case CASE_ELEVE_JOUEUR_1:
                infosDeConfigUI.setCouleurCaseEleveJoueur(ID_JOUEUR_1, couleur);
                break;
            case CASE_ELEVE_JOUEUR_2:
                infosDeConfigUI.setCouleurCaseEleveJoueur(ID_JOUEUR_2, couleur);
                break;
            case CASE_MAITRE_JOUEUR_1:
                infosDeConfigUI.setCouleurCaseMaitreJoueur(ID_JOUEUR_1, couleur);
                break;
            case CASE_MAITRE_JOUEUR_2:
                infosDeConfigUI.setCouleurCaseMaitreJoueur(ID_JOUEUR_2, couleur);
                break;
            default:break;
        }

     }

    public String getNomCouleurPionJoueur(int idJoueur) {
        return infosDeConfigUI.getNomCouleurPionJoueur(idJoueur);
    }

    public Color getCouleurPionJoueur(int idJoueur) {
        return infosDeConfigUI.getCouleurPionJoueur(idJoueur);
    }

    public Color getCouleurCaseMaitreJoueur(int idJoueur) {
        return infosDeConfigUI.getCouleurCaseMaitreJoueur(idJoueur);
    }

    public Color getCouleurCaseEleveJoueur(int idJoueur) {
        return infosDeConfigUI.getCouleurCaseEleveJoueur(idJoueur);
    }

    public void setCouleurPion(Config.CiblesDesCouleurs cible, String nomCouleurPionJoueur) {
        if (cible == PION_TERRAIN_JOUEUR_1) {
            infosDeConfigUI.setCouleurPion(ID_JOUEUR_1, nomCouleurPionJoueur);
        } else if (cible == PION_TERRAIN_JOUEUR_2) {
            infosDeConfigUI.setCouleurPion(ID_JOUEUR_2, nomCouleurPionJoueur);
        }
    }

    public void reinitialiserCouleurs() {
        infosDeConfigUI.reinitialiserCouleurs();
    }

    @Override
    public void setNouvellePartie(String partieSelectionee) {
        try {
            jeu.chargerJeu(partieSelectionee);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * Définit quel joueur commence la partie
     * @param numJoueur Le numéro du joueur (1 ou 2)
     */

    public void setJoueurQuiCommence(int numJoueur) {
        jeu.joueurQuiCommence(numJoueur);
    }
}
