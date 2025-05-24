package Vue.Adaptateurs;

import Vue.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class AdaptateurAnnuler implements ActionListener {
    CollecteurEvenements collecteurEvent;
    private static final Logger logger = Logger.getLogger(AdaptateurAnnuler.class.getName());

    public AdaptateurAnnuler(CollecteurEvenements collecteurEvent){

        this.collecteurEvent = collecteurEvent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info("Bouton \"Annuler\" pressé");
        collecteurEvent.clavier("annuler");
    }
}
