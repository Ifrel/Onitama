package Modele.IA;

import Modele.Coup;
import Modele.Jeu;
import Modele.Pion;

import java.util.List;

import static Global.Config.NIVEAU_IA;
import static Modele.IA.Heuristiques.heuristiqueAvancee;

public class ArbreMinMax {
    private int profondeur;
    private int carteChoisie;
    private Pion pionChoisi;

    public ArbreMinMax() {
    }


    public Coup choisirNoeud(int idJoueur, Noeud n, int profondeur, NIVEAU_IA niveau) {

        //List<EtatJeu> successeurs = n.getId().getSuccesseurs();
//        for (EtatJeu succ : successeurs) {
//            n.addSucc(new Noeud(succ, null));
//        }
        double val = joueur1(idJoueur, n, profondeur, niveau);
        n.setValeur(val);
        for (Noeud nSucc : n.getSuccesseurs()) {
            if (nSucc.getValeur() == val) {
                Coup coup = nSucc.getId().getCoup();
                carteChoisie = nSucc.getId().getCarteChoisie();
                pionChoisi = nSucc.getId().getPionChoisi();
                return coup;
            }
        }
        return null;
    }

    private double joueur1(int idJoueur, Noeud n, int profondeur, NIVEAU_IA niveau) {
        if (n.estFeuille() || profondeur == 0) {
            return n.setValeur(heuristiqueAvancee(idJoueur, n.getId()));
        }
        double valeur = Double.NEGATIVE_INFINITY;

        List<EtatJeu> successeurs = n.getId().getSuccesseurs();
        for (EtatJeu ej : successeurs) {
            Noeud newNoeud = new Noeud(ej, null);
            n.addSucc(newNoeud);
            valeur = Math.max(valeur, joueur2((idJoueur % 2) + 1, newNoeud, profondeur - 1, niveau));
        }

        return n.setValeur(valeur);
    }

    private double joueur2(int idJoueur, Noeud n, int profondeur, NIVEAU_IA niveau) {
        if (n.estFeuille() || profondeur == 0) {
            return n.setValeur(heuristiqueAvancee(idJoueur, n.getId()));
        }
        double valeur = Double.POSITIVE_INFINITY;

        List<EtatJeu> successeurs = n.getId().getSuccesseurs();
        for (EtatJeu ej : successeurs) {
            Noeud newNoeud = new Noeud(ej, null);
            n.addSucc(newNoeud);
            valeur = Math.min(valeur, joueur1((idJoueur % 2) + 1, newNoeud, profondeur - 1, niveau));
        }

        return n.setValeur(valeur);
    }

    public int getProfondeur() {
        return profondeur;
    }

    public void setProfondeur(int profondeur) {
        this.profondeur = profondeur;
    }

    public int getCarteChoisie() {
        return carteChoisie;
    }

    public Pion getPionChoisi() {
        return pionChoisi;
    }
}
