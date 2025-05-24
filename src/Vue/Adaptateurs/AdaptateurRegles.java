package Vue.Adaptateurs;

import Vue.CollecteurEvenements;
import Vue.EcranMenu;
import Vue.Utils.AfficheReglesPDF;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class AdaptateurRegles implements ActionListener {
    private final CollecteurEvenements collecteurEvenements;

    public AdaptateurRegles(CollecteurEvenements collecteurEvenements){
        this.collecteurEvenements = collecteurEvenements;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        collecteurEvenements.clavier("regles");
        // Recherche de la fenêtre ayant le focus
        Window fenetreActive = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        AfficheReglesPDF.ouvrirReglesPDFExterne(fenetreActive);
    }
}
