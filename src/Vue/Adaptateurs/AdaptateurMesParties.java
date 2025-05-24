package Vue.Adaptateurs;

import Vue.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class AdaptateurMesParties implements ActionListener {
    private static final Logger logger = Logger.getLogger(AdaptateurMesParties.class.getName());
    private final CollecteurEvenements collecteurEvenements;

    public AdaptateurMesParties(CollecteurEvenements collecteurEvenements) {
        this.collecteurEvenements = collecteurEvenements;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info("Bouton \"Mes Parties\" pressé");
        collecteurEvenements.clavier("mesParties");
    }
}
