package Modele;

import java.util.ArrayList;

public class Joueur {
    private Jeu _jeu;
    private String _nom;
    private ArrayList<Carte> _main;

    public Joueur(String nom, ArrayList<Carte> main)
    {
        _nom = nom;
        _main = main;
    }
    public String getNom(){ return _nom; }
    public ArrayList<Carte> getMain() { return _main; }
    public void setJeu(Jeu jeu) {_jeu = jeu; }
}
