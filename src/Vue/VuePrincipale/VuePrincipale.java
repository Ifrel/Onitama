package Vue.VuePrincipale;

import Modele.Jeu;
import Patterns.Observateur;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

import javax.swing.*;
import java.awt.*;

import static Global.Config.*;

public class VuePrincipale extends JPanel implements Observateur {
    private static final int ROWS = 15;
    private static final int COLS = 15;
    private InterfaceGraphique interfaceGraphique;
    private InfosJoueur infosJoueur;
    private Grille grille;
    InfosStats infosStats;
    BlocMenu blocMenu;
    private Dimension dimScene;
    private  Jeu jeu;

    CollecteurEvenements collecteurEvent;

    public VuePrincipale(Jeu jeu, CollecteurEvenements collecteurEvent) {
        this.jeu = jeu;
        this.dimScene = new Dimension(jeu.colonnes(), jeu.lignes());
        this.setPreferredSize(dimScene); // plus propre
        this.setLayout(new BorderLayout());
        this.collecteurEvent= collecteurEvent;
        this.interfaceGraphique = interfaceGraphique;

        // Top panel — indication du infosJoueur
        infosJoueur = new InfosJoueur(jeu);
        this.add(infosJoueur, BorderLayout.NORTH);

        // Centre — grille du jeu avec lettres et chiffres
        grille = new Grille(jeu, infosJoueur, collecteurEvent);
        this.add(grille, BorderLayout.CENTER);

        // Panel à droite : infos et stats
        infosStats = new InfosStats(jeu);
        this.add(infosStats, BorderLayout.EAST);

        // Panel en bas : menu de contrôle
        blocMenu = new BlocMenu(jeu, collecteurEvent); // évite Dimension vide ici
        this.add(blocMenu, BorderLayout.SOUTH);

        jeu.ajouteObservateur(this);
    }


    @Override
    public void miseAJour() {

    }

    /**************************************
     * *** ** * MÉTHODES UTILES * ** **** *
     **************************************/

}
