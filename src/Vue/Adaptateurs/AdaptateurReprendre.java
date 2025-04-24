package Vue.Adaptateurs;

import Vue.CollecteurEvenements;
//import Vue.VuePrincipale.BlocMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurReprendre implements ActionListener {
    CollecteurEvenements collecteurEvent;
    //BlocMenu bm;

//    public AdaptateurReprendre(CollecteurEvenements collecteurEvent, BlocMenu bm){
//        this.collecteurEvent = collecteurEvent;
//        this.bm =bm;
//    }
    @Override
    public void actionPerformed(ActionEvent e) {
//        bm.stopTimerPartieCourante();
//        collecteurEvent.clavier("Reprendre");
    }
}
