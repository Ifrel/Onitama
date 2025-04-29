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
    private JFrame frame;

    private JLayeredPane layeredPane;
    private JPanel backgroundBlur;
    private JPanel menuPanel;
    private PlateauDeJeu plateau;


    public InterfaceGraphique(Jeu jeu, CollecteurEvenements collecteurEvent) {
        this.jeu = jeu;
        this.maximized =  false;
        this.collecteurEvent = collecteurEvent;

        this.frame = new JFrame("Onitama");
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(1200, 1000));
        frame.setMinimumSize(new Dimension(900, 700));  // largeur min: 400, hauteur min: 300
        frame.addKeyListener(new AdaptateurClavier(collecteurEvent));

        jeu.ajouteObservateur(this);


        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

    }



//    @Override
//    public void run() {
//        // JLayeredPane pour superposer
//        JLayeredPane layeredPane = new JLayeredPane();
//        layeredPane.add(new PlateauDeJeu(jeu, collecteurEvent), JLayeredPane.DEFAULT_LAYER);
//        frame.setContentPane(layeredPane);
//        System.err.println("Interface graphique lancée");
//
//
//        // Paramettre de la secene
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }

    @Override
    public void run() {
        plateau = new PlateauDeJeu(jeu, collecteurEvent, this);
        plateau.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        layeredPane.add(plateau, JLayeredPane.DEFAULT_LAYER);

        backgroundBlur = new JPanel();
        backgroundBlur.setBackground(new Color(161, 160, 160, 31)); // Alpha 100 pour transparence
        backgroundBlur.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        backgroundBlur.setVisible(false);
        backgroundBlur.setOpaque(true);
        layeredPane.add(backgroundBlur, JLayeredPane.PALETTE_LAYER);


        menuPanel = new JPanel();
        menuPanel.setBackground(new Color(228, 169, 108));
        menuPanel.setBounds(frame.getWidth(), 0, 200, frame.getHeight());
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setVisible(false);
        menuPanel.add(new JLabel("Option 1"));
        menuPanel.add(new JLabel("Option 2"));
        menuPanel.add(new JLabel("Option 3"));
        layeredPane.add(menuPanel, JLayeredPane.MODAL_LAYER);

        backgroundBlur.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                fermerMenu();
            }
        });

        frame.setContentPane(layeredPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                plateau.setBounds(0, 0, frame.getWidth(), frame.getHeight());
                backgroundBlur.setBounds(0, 0, frame.getWidth(), frame.getHeight());
                if (!menuPanel.isVisible())
                    menuPanel.setBounds(frame.getWidth(), 0, 200, frame.getHeight());
                else
                    menuPanel.setBounds(frame.getWidth() - 200, 0, 200, frame.getHeight());
            }
        });
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


    public void ouvrirMenu() {
        if (!menuPanel.isVisible()) {
            backgroundBlur.setVisible(true);
            menuPanel.setVisible(true);

            new Thread(() -> {
                int x = frame.getWidth();
                while (x > frame.getWidth() - 200) {
                    x -= 10;
                    menuPanel.setBounds(x, 0, 200, frame.getHeight());
                    try { Thread.sleep(5); } catch (InterruptedException ignored) {}
                }
                menuPanel.setBounds(frame.getWidth() - 200, 0, 200, frame.getHeight());
            }).start();
        }
    }

    public void fermerMenu() {
        new Thread(() -> {
            int x = menuPanel.getX();
            while (x < frame.getWidth()) {
                x += 10;
                menuPanel.setBounds(x, 0, 200, frame.getHeight());
                try { Thread.sleep(5); } catch (InterruptedException ignored) {}
            }
            menuPanel.setVisible(false);
            backgroundBlur.setVisible(false);
        }).start();
    }


}
