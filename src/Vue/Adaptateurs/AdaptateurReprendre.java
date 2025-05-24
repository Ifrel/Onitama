package Vue.Adaptateurs;

import Vue.CollecteurEvenements;
//import Vue.VuePrincipale.BlocMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurReprendre implements ActionListener {
    CollecteurEvenements collecteurEvent;

    public AdaptateurReprendre(CollecteurEvenements collecteurEvent){
        this.collecteurEvent = collecteurEvent;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        collecteurEvent.clavier("reprendre");
    }
}
