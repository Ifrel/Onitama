package Vue;

import Modele.Jeu;
import Patterns.Observateur;
import Vue.Adaptateurs.AdaptateurClavier;
import Vue.VuePrincipale.VuePrincipale;

import javax.swing.*;
import java.awt.*;

public class InterfaceGraphique extends JFrame implements InterfaceUser, Observateur {
    private CollecteurEvenements collecteurEvent;
    private VuePrincipale vuePrincipale;
    private EcranSelection ecranSelection;
    private boolean maximized;
    private Jeu jeu;

    public InterfaceGraphique(Jeu jeu, CollecteurEvenements collecteurEvent) {
        this.jeu = jeu;
        this.maximized =  false;
        this.collecteurEvent = collecteurEvent;
        this.vuePrincipale = new VuePrincipale(jeu, collecteurEvent);

        setTitle("La gaufre empoisonnée");
        setPreferredSize(new Dimension(1200, 1000));
        setLayout(new BorderLayout());
        addKeyListener(new AdaptateurClavier(collecteurEvent));

        jeu.ajouteObservateur(this);

        ecranSelection = new EcranSelection(this);
        this.setContentPane(ecranSelection);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    @Override
    public void toggleIA() {
        // Ajoute ici la logique pour activer/désactiver l'IA
    }

    @Override
    public void toggleFullScreen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();

        if (maximized) {
            device.setFullScreenWindow(null);
        } else {
            device.setFullScreenWindow(this);
        }

        maximized = !maximized;
    }

    public void setEcran(EcranSelection ecranSelection){
        setContentPane(ecranSelection);
        revalidate();
        repaint();
    }

    public void setEcran(EcranDemarrage ecranDemarrage){
        setContentPane(ecranDemarrage);
        revalidate();
        repaint();
    }

    public void setEcran(VuePrincipale vuePrincipale){
        setContentPane(vuePrincipale);
        revalidate();
        repaint();
    }

    public EcranSelection getEcranSelection(){
        return ecranSelection;
    }

    public CollecteurEvenements getCollecteurEvent(){
        return collecteurEvent;
    }

    public Jeu getJeu() {
        return jeu;
    }


    @Override
    public void miseAJour() {
        if (vuePrincipale != null) {
            vuePrincipale.revalidate();
            vuePrincipale.repaint();
        }
    }
}
