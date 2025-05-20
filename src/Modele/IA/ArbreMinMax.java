package Modele.IA;

import Modele.Coup;
import Modele.Jeu;
import Modele.Pion;

import java.util.List;

import static Global.Config.NIVEAU_IA;
import static Modele.IA.Heuristiques.heuristiqueAvancee;

public class ArbreMinMax {
    private final Jeu jeu;
    private int profondeur;
    private int carteChoisie;
    private Pion pionChoisi;

    public ArbreMinMax(Jeu jeu) {
        this.jeu = jeu;
    }


    public Coup choisirNoeud(Noeud n, int profondeur, NIVEAU_IA niveau) {

        List<EtatJeu> successeurs = n.getId().getSuccesseurs();
//        for (EtatJeu succ : successeurs) {
//            n.addSucc(new Noeud(succ, null));
//        }
        double val = joueur1(n, profondeur, niveau);
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

    private double joueur1(Noeud n, int profondeur, NIVEAU_IA niveau) {
        if (n.estFeuille() || profondeur == 0) {
            return heuristiqueAvancee(n.getId());
        }
        double valeur = Double.NEGATIVE_INFINITY;

        List<EtatJeu> successeurs = n.getId().getSuccesseurs();
        for (EtatJeu ej : successeurs) {
            Noeud newNoeud = new Noeud(ej, null);
            n.addSucc(newNoeud);
            valeur = Math.max(valeur, joueur2(newNoeud, profondeur - 1, niveau));
        }

        n.setValeur(valeur);
        return valeur;
    }

    private double joueur2(Noeud n, int profondeur, NIVEAU_IA niveau) {
        if (n.estFeuille() || profondeur == 0) {
            return heuristiqueAvancee(n.getId());
        }
        double valeur = Double.POSITIVE_INFINITY;

        List<EtatJeu> successeurs = n.getId().getSuccesseurs();
        for (EtatJeu ej : successeurs) {
            Noeud newNoeud = new Noeud(ej, null);
            n.addSucc(newNoeud);
            valeur = Math.min(valeur, joueur1(newNoeud, profondeur - 1, niveau));
        }

        n.setValeur(valeur);
        return valeur;
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
