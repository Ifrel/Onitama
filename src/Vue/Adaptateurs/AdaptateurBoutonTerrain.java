package Vue.Adaptateurs;

import Modele.CasePlateau;
import Vue.CollecteurEvenements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurBoutonTerrain implements ActionListener {
    private final CollecteurEvenements collecteurEvent;
    private CasePlateau casePlateau;

    public AdaptateurBoutonTerrain(CasePlateau casePlateau, CollecteurEvenements collecteurEvent){
        this.collecteurEvent = collecteurEvent;
        this.casePlateau = casePlateau;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.err.println("bouton: case préssé");
        collecteurEvent.boutonTerrainJeu(casePlateau);
    }
}
