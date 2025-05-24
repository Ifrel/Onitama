package Controleur;

import Global.Config;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InfosDeConfigUI;

import java.awt.*;

import static Global.Config.NIVEAU_IA.*;

public class ControleurEcranDeDemarrage implements CollecteurEvenements {
    private final Jeu jeu;

    private final InfosDeConfigUI infosDeConfigUI = InfosDeConfigUI.getInstance();

    public ControleurEcranDeDemarrage(Jeu jeu){
        this.jeu = jeu;
    }

    @Override
    public void clavier(String t) {

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
    public void setModeAuto(boolean nouvelEtat) {
        if (nouvelEtat) {
            jeu.toggleIA1();
            jeu.toggleIA2();
        }else{
            jeu.toggleIA1();
        }
    }

     @Override
    public void setCouleur(Config.CiblesDesCouleurs cible, Color couleur) {
        switch (cible){
            case CASE_TERRAIN:
                infosDeConfigUI.setCouleurCaseTerrain(couleur);
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
}
