package Vue.Adaptateurs;

import Vue.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurRegles implements ActionListener {
    private final CollecteurEvenements collecteurEvenements;

    public AdaptateurRegles(CollecteurEvenements collecteurEvenements){
        this.collecteurEvenements = collecteurEvenements;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        collecteurEvenements.clavier("regles");
    }
}
