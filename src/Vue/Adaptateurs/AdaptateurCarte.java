package Vue.Adaptateurs;

import Modele.Carte;
import Modele.Jeu;
import Patterns.Observateur;
import Vue.CollecteurEvenements;
import Vue.EcranPlateauDeJeu;
import Vue.Utils.MethodsStaticsUtils.BoutonAvecImage;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Logger;

import static Global.Config.ID_JOUEUR_1;
import static Global.Config.ID_JOUEUR_2;

public class AdaptateurCarte implements ActionListener, Observateur {
    private final CollecteurEvenements collecteurEvent;
    private final Carte carte;
    private final int  idCarte;
    private BoutonAvecImage boutonCarte = null;
    private Boolean estSelectionne;
    private EcranPlateauDeJeu ecranPlateauDeJeu;
    private final Jeu jeu;

    private final Logger logger = Logger.getLogger(AdaptateurCarte.class.getName());

    public AdaptateurCarte(
                            int idCarte,
                            BoutonAvecImage boutonCarte,
                            Carte carte,
                            EcranPlateauDeJeu ecranPlateauDeJeu,
                            CollecteurEvenements collecteurEvent){
        this.collecteurEvent = collecteurEvent;
        this.carte = carte;
        this.idCarte = idCarte;
        this.boutonCarte = boutonCarte;
        this.ecranPlateauDeJeu = ecranPlateauDeJeu;
        this.jeu = ecranPlateauDeJeu.jeu;
        this.estSelectionne = jeu.getNumCarteSelectionnee() == idCarte && carte.getProprietaire()==jeu.getJoueurCourant().getId();

        //TODO voir Prof: pourquoi si activer, une exception est levée au niveau du pattern Obsevable/Observateur
        // jeu.ajouteObservateur(this);
        miseAJour();
    }



    // État modifié
    Color activeBg = Color.CYAN;
    Color activeFg = Color.BLACK;
    Border activeBorder = BorderFactory.createLineBorder(Color.BLUE, 4);


    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info("CarteUI n° "+idCarte+ ": " + carte.getNom() + " pressé pour le Joueur "+carte.getProprietaire());

        animationCarte();

       collecteurEvent.setCarteSelectionne(idCarte);
    }


    @Override
    public void miseAJour() {
        estSelectionne = jeu.getNumCarteSelectionnee() == idCarte && carte.getProprietaire()==jeu.getJoueurCourant().getId();
        logger.info("CarteUI n° "+idCarte+ ": " + carte.getNom() + " Sélectionnée pour le Joueur "+carte.getProprietaire());
        animationCarte();
    }



    private void animationCarte(){
        boutonCarte.bouton.setEnabled(carte.getProprietaire() == jeu.getJoueurCourant().getId());
        if (estSelectionne) {
            boutonCarte.bouton.setBackground(activeBg);
            boutonCarte.bouton.setForeground(activeFg);
            boutonCarte.bouton.setBorder(activeBorder);
        } else {
            boutonCarte.bouton.setBackground(null);
            boutonCarte.bouton.setForeground(null);
            boutonCarte.bouton.setPreferredSize(null);
            boutonCarte.bouton.setBorder(null);
        }
        estSelectionne = !estSelectionne;

        // Nécessaire pour redessiner la taille
        boutonCarte.bouton.revalidate();
        boutonCarte.bouton.repaint();
    }
}