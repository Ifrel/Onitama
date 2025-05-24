package Vue.Adaptateurs;

import Modele.CasePlateau;
import Modele.Coup;
import Modele.Jeu;
import Patterns.Observateur;
import Vue.CollecteurEvenements;
import Vue.EcranPlateauDeJeu;
import Vue.Utils.Boutons.BoutonTerrain;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Logger;

import static Modele.CasePlateau.TYPE_ELEMENT_SUR_CASE.VIDE;
import static Vue.ConfigUI.COULEUR_FOND_PION_DEPART_SELECTIONE;

public class AdaptateurBoutonTerrain implements ActionListener, Observateur {
    private final Jeu jeu;
    private final BoutonTerrain boutonTerrain;
    private final CasePlateau casePlateau;
    private final CollecteurEvenements collecteurEv;
    private final EcranPlateauDeJeu ecranPlateauDeJeu;
    private List<Coup> coupPossible;

    private Coup coupPrecedantLeDernierCoupJouer;

    private static final Logger logger = Logger.getLogger(AdaptateurBoutonTerrain.class.getName());


    public AdaptateurBoutonTerrain(
            BoutonTerrain boutonTerrain,
            CasePlateau casePlateau,
            CollecteurEvenements collecteurEv,
            EcranPlateauDeJeu ecranPlateauDeJeu) {
        this.boutonTerrain = boutonTerrain;
        this.casePlateau = casePlateau;
        this.collecteurEv = collecteurEv;
        this.ecranPlateauDeJeu = ecranPlateauDeJeu;
        this.jeu = ecranPlateauDeJeu.getJeu();
        this.coupPossible = jeu.getCoupsPossibles(jeu.getCarteSelectionnee(), casePlateau.getCoordonnee());

        jeu.ajouteObservateur(this);
        miseAJour();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        System.err.println("bouton: case pressé: " + casePlateau.getCoordonnee());
        collecteurEv.setCaseSelectionnee(casePlateau.getCoordonnee());

        desactiveAnimationCible();
        activeAnimationCible();
    }

    @Override
    public void miseAJour() {
        this.coupPossible = jeu.getCoupsPossibles(jeu.getCarteSelectionnee(), casePlateau.getCoordonnee());
        desactiveAnimationCible();
        marquerLeCoupPrecedent();
    }


    private void activeAnimationCible() {
        if (casePlateau.getTypeElement() != VIDE && casePlateau.getProprietaire() == jeu.getJoueurCourant().getId()) {
            if (!coupPossible.isEmpty()) {
                collecteurEv.getCollecteurAnimation().activeCibleBoutonTerrain(coupPossible, ecranPlateauDeJeu);
            }
        }
    }

    private void desactiveAnimationCible() {
        if (casePlateau.getTypeElement() != VIDE) {
            collecteurEv.getCollecteurAnimation().desactiveCibleBoutonTerrain(coupPossible);
        }
    }




    private void marquerLeCoupPrecedent(){
        Coup dernierCoupJouer = ecranPlateauDeJeu.getJeu().getDernierCoupJoue();
        if (dernierCoupJouer != null) {

            if (coupPrecedantLeDernierCoupJouer != null) {
                Point departPrecedent = coupPrecedantLeDernierCoupJouer.getDepart();
                Point arrivePrecedent = coupPrecedantLeDernierCoupJouer.getArrivee();

                ecranPlateauDeJeu.getBoutonterrainAt(departPrecedent).enleverCouleurBordure();
                ecranPlateauDeJeu.getBoutonterrainAt(arrivePrecedent).enleverCouleurBordure();
            }

            Point depart = dernierCoupJouer.getDepart();
            Point arrive = dernierCoupJouer.getArrivee();

            ecranPlateauDeJeu.getBoutonterrainAt(depart).chargerCouleurBordure(Color.ORANGE, Color.BLUE);
            ecranPlateauDeJeu.getBoutonterrainAt(arrive).chargerCouleurBordure(Color.CYAN, Color.red);

            coupPrecedantLeDernierCoupJouer = dernierCoupJouer;
        }

        System.err.println("___________________________________________________" +
                "\nCoup precedant le dernier :" + coupPrecedantLeDernierCoupJouer +
                "\nCoup precedent :" + dernierCoupJouer) ;

//        ecranPlateauDeJeu.getBoutonterrainAt(ecranPlateauDeJeu.getJeu().getPionSelectionne().getPosition()).chargerCouleurFont(COULEUR_FOND_PION_DEPART_SELECTIONE);


    }

}

