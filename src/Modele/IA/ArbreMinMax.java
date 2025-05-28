package Modele.IA;

import Modele.Coup;
import Modele.Pion;

import java.util.List;
import java.util.logging.Logger;

import static Modele.IA.Heuristiques.heuristiqueAvancee;

public class ArbreMinMax {
    private int carteChoisie;
    private Pion pionChoisi;
    private int nbFeuilles, nbEtats;
    private static final Logger logger = Logger.getLogger(ArbreMinMax.class.getName());

    public ArbreMinMax() {
        this.nbFeuilles = this.nbEtats = 0;
    }

    public Coup choisirCoup(int idJoueur, Noeud n, int profondeur) {
        this.nbFeuilles = this.nbEtats = 0;
        double val = n.setValeur(joueur1(idJoueur, n, profondeur, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        logger.info("Nombre de noeuds total parcouru : " + nbEtats + ", dont feuilles : " + nbFeuilles);
        for (Noeud nSucc : n.getSuccesseurs()) {
            if (nSucc.getValeur() == val) {
                Coup coup = nSucc.getEtatJeu().getCoup();
                carteChoisie = nSucc.getEtatJeu().getCarteChoisie();
                pionChoisi = nSucc.getEtatJeu().getPionChoisi();
                return coup;
            }
        }
        return null;
    }

    private double joueur1(int idJoueur, Noeud n, int profondeur, double alpha, double beta) {
        nbEtats++;
        if (n.estFeuille() || profondeur == 0) {
            nbFeuilles++;
            return n.setValeur(heuristiqueAvancee(idJoueur, n.getEtatJeu()));
        }
        double valeur = Double.NEGATIVE_INFINITY;

        List<EtatJeu> successeurs = n.getEtatJeu().getSuccesseurs();
        for (EtatJeu ej : successeurs) {
            Noeud newNoeud = new Noeud(ej, null);
            n.addSucc(newNoeud);
            valeur = Math.max(valeur, joueur2(idJoueur, newNoeud, profondeur - 1, alpha, beta));
            if (valeur >= beta) {
                return n.setValeur(beta);
            }
            alpha = Math.max(alpha, valeur);
            n.setValeur(valeur);
        }

        return n.setValeur(valeur);
    }

    private double joueur2(int idJoueur, Noeud n, int profondeur, double alpha, double beta) {
        nbEtats++;
        if (n.estFeuille() || profondeur == 0) {
            nbFeuilles++;
            return n.setValeur(heuristiqueAvancee(idJoueur, n.getEtatJeu()));
        }
        double valeur = Double.POSITIVE_INFINITY;

        List<EtatJeu> successeurs = n.getEtatJeu().getSuccesseurs();
        for (EtatJeu ej : successeurs) {
            Noeud newNoeud = new Noeud(ej, null);
            n.addSucc(newNoeud);
            valeur = Math.min(valeur, joueur1(idJoueur, newNoeud, profondeur - 1, alpha, beta));
            if (valeur <= alpha) {
                return n.setValeur(alpha);
            }
            beta = Math.min(beta, valeur);
            n.setValeur(valeur);
        }

        return n.setValeur(valeur);
    }

    public int getCarteChoisie() {
        return carteChoisie;
    }

    public Pion getPionChoisi() {
        return pionChoisi;
    }
}
