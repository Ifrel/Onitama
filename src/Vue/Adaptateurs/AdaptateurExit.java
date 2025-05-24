package Vue.Adaptateurs;

import Vue.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class AdaptateurExit implements ActionListener {
    private final CollecteurEvenements collecteurEvenements;
    private static final Logger logger = Logger.getLogger(AdaptateurExit.class.getName());

    public AdaptateurExit(CollecteurEvenements collecteurEvenements){
        this.collecteurEvenements = collecteurEvenements;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info("Bouton \"Exit\" pressé");
        collecteurEvenements.clavier("exit");
    }
}
