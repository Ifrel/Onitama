package Vue.Adaptateurs;

import Vue.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurRefaire implements ActionListener {
    CollecteurEvenements collecteurEvent;

    public AdaptateurRefaire(CollecteurEvenements collecteurEvent){

        this.collecteurEvent = collecteurEvent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.err.println("bouton: refaire préssé");
        collecteurEvent.clavier("Refaire");
    }

}
