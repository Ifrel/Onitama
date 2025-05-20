package Modele.IA;

import Modele.Coup;

import java.util.ArrayList;
import java.util.List;

public class Noeud {
    private EtatJeu id;
    private double valeur;
    private List<Noeud> successeurs;


    public Noeud(EtatJeu id, List<Noeud> successeurs) {
        this.id = id;
        this.successeurs = successeurs;
        this.successeurs = new ArrayList<>();
    }

    public boolean estFeuille() {
        return id.estEtatFinal();
    }

    public double getValeur() {
        return this.valeur;
    }

    public double setValeur(double valeur) {
        this.valeur = valeur;
        return this.valeur;
    }

    public EtatJeu getId() {
        return this.id;
    }

    public void setId(EtatJeu id) {
        this.id = id;
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
