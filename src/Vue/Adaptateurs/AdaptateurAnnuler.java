package Vue.Adaptateurs;

import Vue.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurAnnuler implements ActionListener {
    CollecteurEvenements collecteurEvent;

    public AdaptateurAnnuler(CollecteurEvenements collecteurEvent){

        this.collecteurEvent = collecteurEvent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.err.println("bouton: annuler pressé");
        collecteurEvent.clavier("Annuler");
    }
}
