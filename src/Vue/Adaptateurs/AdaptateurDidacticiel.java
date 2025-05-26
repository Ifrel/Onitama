package Vue.Adaptateurs;

import Vue.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import static Vue.Utils.MethodsStaticsUtils.afficherFonctionEnCours;

public class AdaptateurDidacticiel implements ActionListener {
    private final CollecteurEvenements collecteurEvenements;

    public AdaptateurDidacticiel(CollecteurEvenements collecteurEvenements){
        this.collecteurEvenements = collecteurEvenements;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        collecteurEvenements.clavier("didacticiel");
    }
}
