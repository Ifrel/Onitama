package Vue.Adaptateurs;

import Modele.CasePlateau;
import Vue.CollecteurEvenements;
import Vue.Utils.MethodsStaticsUtils.BoutonAvecImage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdaptateurBoutonTerrain implements ActionListener {
    private final CollecteurEvenements collecteurEvent;
    private final CasePlateau casePlateau;
    private final BoutonAvecImage boutonAvecImage;

    public AdaptateurBoutonTerrain(BoutonAvecImage boutonAvecImage, CasePlateau casePlateau, CollecteurEvenements collecteurEvent){
        this.collecteurEvent = collecteurEvent;
        this.casePlateau = casePlateau;
        this.boutonAvecImage = boutonAvecImage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        if (casePlateau.getTypeElement() != VIDE){
//            boutonAvecImage.panel.setImage(getCheminImagePionClique(boutonAvecImage.pathBouton));
//        }

        boutonAvecImage.bouton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        System.err.println("bouton: case pressé: "+casePlateau.getCoordonnee());
        collecteurEvent.setCaseSelectionnee(casePlateau.getCoordonnee());
    }
}
