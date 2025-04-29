package Vue.Adaptateurs;

import Vue.CarteUI;
import Vue.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurCarteUI implements ActionListener {
    private CollecteurEvenements collecteurEvent;
    private CarteUI carteUI;

    public AdaptateurCarteUI(CarteUI carteUI, CollecteurEvenements collecteurEvent){
        this.collecteurEvent = collecteurEvent;
        this.carteUI = carteUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.err.println("CarteUI: "+ carteUI.getNum()+" préssé");
        collecteurEvent.carteSelectionne(carteUI.getNum());
    }
}