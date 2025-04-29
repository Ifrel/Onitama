package Vue.Adaptateurs;

import Vue.CollecteurEvenements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurBoutonTerrain implements ActionListener {
    private CollecteurEvenements collecteurEvent;
    private Point bntCoord;
    private JButton bouton;

    public AdaptateurBoutonTerrain(JButton bouton, Point bntCoord, CollecteurEvenements collecteurEvent){
        this.collecteurEvent = collecteurEvent;
        this.bntCoord = bntCoord;
        this.bouton = bouton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.err.println("bouton: ("+bntCoord.x+", "+bntCoord.y+") préssé");
        collecteurEvent.boutonTerrainJeu(bntCoord);
    }
}
