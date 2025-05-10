package Vue.Adaptateurs;

import Modele.Carte;
import Vue.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurCarte implements ActionListener {
    private final CollecteurEvenements collecteurEvent;
    private final Carte carte;
    private final int num;

    public AdaptateurCarte(Carte carte, int num, CollecteurEvenements collecteurEvent){
        this.collecteurEvent = collecteurEvent;
        this.carte = carte;
        this.num = num;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.err.println("CarteUI: "+ carte.getNom()+" préssé");
        collecteurEvent.carteSelectionne(num);
    }
}