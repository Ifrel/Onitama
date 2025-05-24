package Vue.Adaptateurs;

import Vue.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class AdaptateurRegles implements ActionListener {
    private final CollecteurEvenements collecteurEvenements;
    private static final Logger logger = Logger.getLogger(AdaptateurRegles.class.getName());

    public AdaptateurRegles(CollecteurEvenements collecteurEvenements){
        this.collecteurEvenements = collecteurEvenements;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info("Bouton \"regles\" pressé");
        collecteurEvenements.clavier("regles");
    }
}
