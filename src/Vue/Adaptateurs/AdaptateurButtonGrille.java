package Vue.Adaptateurs;

import Vue.CollecteurEvenements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurButtonGrille implements ActionListener {
    CollecteurEvenements collecteurEvent;
    Point coordBtn;

    public AdaptateurButtonGrille(CollecteurEvenements collecteurEvent, Point coordBtn){
        this.collecteurEvent = collecteurEvent;
        this.coordBtn = coordBtn;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        collecteurEvent.buttonGrille(coordBtn);
    }
}
