package Vue.Adaptateurs;

import Vue.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class AdaptateurMesParties implements ActionListener {
    private final CollecteurEvenements collecteurEvenements;

    public AdaptateurMesParties(CollecteurEvenements collecteurEvenements) {
        this.collecteurEvenements = collecteurEvenements;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        collecteurEvenements.clavier("mesParties");
    }
}
