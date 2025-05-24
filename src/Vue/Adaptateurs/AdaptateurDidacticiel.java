package Vue.Adaptateurs;

import Vue.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class AdaptateurDidacticiel implements ActionListener {
    private final CollecteurEvenements collecteurEvenements;
    private static final Logger logger = Logger.getLogger(AdaptateurDidacticiel.class.getName());

    public AdaptateurDidacticiel(CollecteurEvenements collecteurEvenements){
        this.collecteurEvenements = collecteurEvenements;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info("Bouton \"Didactitiel\" pressé");
        collecteurEvenements.clavier("didactitiel");
    }
}
