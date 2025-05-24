package Vue.Adaptateurs;

import Vue.CollecteurEvenements;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.jar.JarFile;

public class AdaptateurNouvellePartie implements ActionListener {
    CollecteurEvenements collecteurEvent;

    public AdaptateurNouvellePartie(CollecteurEvenements collecteurEvent){
        this.collecteurEvent = collecteurEvent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        collecteurEvent.clavier("nouvellePartie");
    }
}
