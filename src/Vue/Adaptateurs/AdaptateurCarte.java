package Vue.Adaptateurs;

import Modele.Carte;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.EcranPlateauDeJeu;
import Vue.Utils.Boutons.BoutonCarte;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class AdaptateurCarte implements ActionListener {
    private final CollecteurEvenements collecteurEv;
    private final Carte carte;
    private final int  idCarte;
    private BoutonCarte boutonCarte = null;
    private Boolean estSelectionne;
    private EcranPlateauDeJeu ecranPlateauDeJeu;
    private final Jeu jeu;

    private final Logger logger = Logger.getLogger(AdaptateurCarte.class.getName());

    public AdaptateurCarte(
                            int idCarte,
                            BoutonCarte boutonCarte,
                            Carte carte,
                            EcranPlateauDeJeu ecranPlateauDeJeu,
                            CollecteurEvenements collecteurEv){
        this.collecteurEv = collecteurEv;
        this.carte = carte;
        this.idCarte = idCarte;
        this.boutonCarte = boutonCarte;
        this.ecranPlateauDeJeu = ecranPlateauDeJeu;
        this.jeu = ecranPlateauDeJeu.getJeu();
        this.estSelectionne = jeu.getNumCarteSelectionnee() == idCarte && carte.getProprietaire()==jeu.getJoueurCourant().getId();
        activeAnimation();
    }




    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info("CarteUI n° "+idCarte+ ": " + carte.getNom() + " pressé pour le Joueur "+carte.getProprietaire());
        boutonCarte.setEnabled(carte.getProprietaire() == jeu.getJoueurCourant().getId());

//        activeAnimation();
        collecteurEv.setCarteSelectionne(idCarte);
        desactiveAnimation();
    }



    private void activeAnimation(){
        estSelectionne = jeu.getNumCarteSelectionnee() == idCarte && carte.getProprietaire()==jeu.getJoueurCourant().getId();
        if (estSelectionne) {
            boutonCarte.demarrerAnimation();
        }
        //    collecteurEv.getCollecteurAnimation().activeCibleBoutonTerrain(coupPossible, ecranPlateauDeJeu);

    }

    private void desactiveAnimation(){
        boutonCarte.arreterAnimation();
//        collecteurEv.getCollecteurAnimation().desactiveCibleBoutonTerrain(coupPossible);
    }
}