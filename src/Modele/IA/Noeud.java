package Modele.IA;

import java.util.ArrayList;
import java.util.List;

public class Noeud {
    private EtatJeu etatJeu;
    private double valeur;
    private List<Noeud> successeurs;


    public Noeud(EtatJeu etatJeu, List<Noeud> successeurs) {
        this.etatJeu = etatJeu;
        this.successeurs = successeurs;
        this.successeurs = new ArrayList<>();
    }

    public boolean estFeuille() {
        return etatJeu.estEtatFinal();
    }

    public double getValeur() {
        return this.valeur;
    }

    public double setValeur(double valeur) {
        this.valeur = valeur;
        return this.valeur;
    }

    public EtatJeu getEtatJeu() {
        return this.etatJeu;
    }

    public void setEtatJeu(EtatJeu etatJeu) {
        this.etatJeu = etatJeu;
    }

    public List<Noeud> getSuccesseurs() {
        return this.successeurs;
    }

    public void setSuccesseurs(List<Noeud> successeurs) {
        this.successeurs = successeurs;
    }

    public void addSucc(Noeud n) {
       this.successeurs.add(n);
    }

    public void addAllSucc(List<Noeud> ln) {
        this.successeurs.addAll(ln);
    }

    public void clearSucc() {
        this.successeurs = new ArrayList<>();
    }

    public void removeSucc(Noeud n) {
        this.successeurs.remove(n);
    }
}
