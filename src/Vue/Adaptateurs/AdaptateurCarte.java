package Vue.Adaptateurs;

import Modele.Carte;
import Modele.Jeu;
import Patterns.Observateur;
import Vue.CollecteurEvenements;
import Vue.EcranPlateauDeJeu;
import Vue.Utils.Boutons.BoutonCarte;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static Global.Config.ID_JOUEUR_1;

public class AdaptateurCarte implements ActionListener, Observateur {
    private Carte carte;
    private final int  idCarte;
    private List<Carte> listeDesCartes;

    private final int idJoueur;
    private BoutonCarte boutonCarte = null;
    private Boolean estSelectionne;

    private EcranPlateauDeJeu ecranPlateauDeJeu;
    private final Jeu jeu;

    private final CollecteurEvenements collecteurEv;
    private final Logger logger = Logger.getLogger(AdaptateurCarte.class.getName());

    public AdaptateurCarte(
                            int idCarte,
                            int idJoueur,
                            BoutonCarte boutonCarte,
                            EcranPlateauDeJeu ecranPlateauDeJeu,
                            CollecteurEvenements collecteurEv){
        this.collecteurEv = collecteurEv;
        this.idCarte = idCarte;
        this.idJoueur = idJoueur;
        this.boutonCarte = boutonCarte;
        this.ecranPlateauDeJeu = ecranPlateauDeJeu;
        this.jeu = ecranPlateauDeJeu.getJeu();
        this.listeDesCartes = idJoueur == ID_JOUEUR_1 ? jeu.getCartesJoueur1() : jeu.getCartesJoueur2();
        this.carte = listeDesCartes.get(idCarte);
        this.estSelectionne = jeu.getNumCarteSelectionnee() == idCarte && carte.getProprietaire()==jeu.getJoueurCourant().getId();

        jeu.ajouteObservateur(this);
        miseAJour();
        System.err.println("Adaptateur carte: constructeur");
    }




    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info("CarteUI n° "+idCarte+ ": " + carte.getNom() + " pressé pour le Joueur "+carte.getProprietaire());

        activeAnimation();
        collecteurEv.setCarteSelectionne(idCarte);
        desactiveAnimation();
        System.err.println("_____________________________________________________" +
                "\n Nom carte: "+carte.getNom() +
                "\nAdaptateur carte: actionPerformed\n" +
                "carte.getProprietaire(): "+carte.getProprietaire() +
                "\njeu.getJoueurCourant().getId() :"+ jeu.getJoueurCourant().getId() +
                "\njeu.getIdJoueurCourant() :"+jeu.getIdJoueurCourant());
    }



    private void activeAnimation(){
        if (estSelectionne) {
            boutonCarte.demarrerAnimation();
        }
//        collecteurEv.getCollecteurAnimation().activeAnimationDeRotation(boutonCarte, carte, jeu.getJoueurCourant().getId());

    }

    private void desactiveAnimation(){
        boutonCarte.arreterAnimation();
//        collecteurEv.getCollecteurAnimation().desactiveAnimationDeRotation(boutonCarte, carte, jeu.getIdJoueurCourant());
    }



    @Override
    public void miseAJour() {
        boutonCarte.setEnabled(carte.getProprietaire() == jeu.getJoueurCourant().getId());

        this.listeDesCartes = idJoueur == ID_JOUEUR_1 ? jeu.getCartesJoueur1() : jeu.getCartesJoueur2();
        this.carte = listeDesCartes.get(idCarte);
        this.estSelectionne = jeu.getNumCarteSelectionnee() == idCarte && carte.getProprietaire()==jeu.getJoueurCourant().getId();


        System.err.println("_______________________________________________" +
                "\nAdaptateur carte ("+carte.getNom() +"): miseAJour");
        desactiveAnimation();
        activeAnimation();


    }
}