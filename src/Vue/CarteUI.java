package Vue;

import javax.swing.*;

public class CarteUI {
    private JButton bouton;
    private int numBouton;

    public CarteUI(JButton bouton, int numBouton){
        this.bouton = bouton;
        this.numBouton = numBouton;
    }


    public int getNum(){
        return  numBouton;
    }
}