package Vue;

import Modele.Jeu;
import Patterns.Observateur;
import Vue.Adaptateurs.AdaptateurClavier;
//import Vue.VuePrincipale.VuePrincipale;

import javax.swing.*;
import java.awt.*;

public class InterfaceGraphique implements Runnable, InterfaceUser, Observateur {
    private CollecteurEvenements collecteurEvent;
    private boolean maximized;
    private Jeu jeu;
    JFrame frame;

    public InterfaceGraphique(Jeu jeu, CollecteurEvenements collecteurEvent) {
        this.jeu = jeu;
        this.maximized =  false;
        this.collecteurEvent = collecteurEvent;

        this.frame = new JFrame("Onitama");
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(1200, 1000));
        frame.addKeyListener(new AdaptateurClavier(collecteurEvent));

        //jeu.ajouteObservateur(this);

    }



    @Override
    public void run() {
        frame.setContentPane(new PlateauDeJeu(jeu, collecteurEvent));
        System.err.println("Interface graphique lancée");


        // Paramettre de la secene
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void miseAJour() {

    }

    public void lancer(){
        SwingUtilities.invokeLater(new InterfaceGraphique(jeu, collecteurEvent)); // pour exécuter le run()
    }

    @Override
    public void toggleIA() {
    }

    @Override
    public void toggleFullScreen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();

        if (maximized) {
            device.setFullScreenWindow(null);
        } else {
            device.setFullScreenWindow(frame);
        }

        maximized = !maximized;
    }



}
