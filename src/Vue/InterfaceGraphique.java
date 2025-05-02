package Vue;

import Modele.Jeu;
import Patterns.Observateur;
import Vue.Adaptateurs.AdaptateurClavier;

import javax.swing.*;
import java.awt.*;

import static Vue.ConfigUI.WIDTH_MENU;


/**
 * Classe principale pour l'affichage graphique du jeu Onitama.
 * Gère la fenêtre, le plateau, les couches superposées, et les interactions comme le menu latéral.*/
public class InterfaceGraphique implements Runnable, InterfaceUser, Observateur {
    // --- Attributs principaux ---
    private CollecteurEvenements collecteurEvent;
    private boolean maximized;
    private Jeu jeu;

    // --- MethodsStaticsUtils Swing ---
    private JFrame frame;
    private JLayeredPane layeredPane;
    private JPanel backgroundBlur;

    private EcranMenu ecranMenu;
    private EcranPlateauDeJeu ecranPlateauDeJeu;
    private EcranDeDemarrage ecranDeDemarrage;


    /**
     * Gestionaire de toutes les interfaces graphiques
     * @param jeu modèle de données observé
     * @param collecteurEvent gestionnaire des événements */
    public InterfaceGraphique(Jeu jeu, CollecteurEvenements collecteurEvent) {
        this.jeu = jeu;
        this.collecteurEvent = collecteurEvent;
        this.maximized = false;

        InitiliserLaScene();
        initialiserLayeredPane();

        // Observer pour mettre à jour l'affichage si nécessaire
//        jeu.ajouteObservateur(this);
    }





    /**
     * Initialise la fenêtre principale (JFrame)*/
    private void InitiliserLaScene() {
        frame = new JFrame("Onitama");
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(1200, 1000));
        frame.setMinimumSize(new Dimension(900, 700));
        frame.addKeyListener(new AdaptateurClavier(collecteurEvent));
    }

    /**
     * Initialise le JLayeredPane qui superpose les composants*/
    private void initialiserLayeredPane() {
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
    }

    // --- Lancement graphique ---
    @Override
    public void run() {
        initialiserEcranDeDemarage();
        initialiserPlateau();
        initialiserBackgroundBlur();
        initialiserMenu();
        ajouterComportementRedimensionnement();

        frame.setContentPane(ecranDeDemarrage);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    @Override
    public void miseAJour() {
        // (Réagir aux changements du modèle ici si nécessaire)
    }


    private void initialiserEcranDeDemarage(){
        ecranDeDemarrage = new EcranDeDemarrage(jeu, collecteurEvent, this);
        ecranDeDemarrage.setBounds(0, 0, frame.getWidth(), frame.getHeight());
    }

    /**
     * Initialise le plateau de jeu */
    private void initialiserPlateau() {
        ecranPlateauDeJeu = new EcranPlateauDeJeu(jeu, collecteurEvent, this);
        ecranPlateauDeJeu.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        layeredPane.add(ecranPlateauDeJeu, JLayeredPane.DEFAULT_LAYER);
    }

    /**
     * Crée un panneau gris semi-transparent pour le flou de fond*/
    private void initialiserBackgroundBlur() {
        backgroundBlur = new JPanel();
        backgroundBlur.setToolTipText("JEU EN PAUSE");
        backgroundBlur.setBackground(new Color(161, 160, 160, 50)); // Gris transparent
        backgroundBlur.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        backgroundBlur.setOpaque(false);
        backgroundBlur.setVisible(false);

        backgroundBlur.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                fermerMenu();
            }
        });
        layeredPane.add(backgroundBlur, JLayeredPane.PALETTE_LAYER);
    }

    /**
     * Initialise le menu latéral (caché par défaut)*/
    private void initialiserMenu() {
        ecranMenu = new EcranMenu(jeu, collecteurEvent, this);
        ecranMenu.setBounds(frame.getWidth(), 0, WIDTH_MENU, frame.getHeight());
        ecranMenu.setVisible(false);

        layeredPane.add(ecranMenu, JLayeredPane.MODAL_LAYER);
    }

    /**
     * Gère le redimensionnement dynamique de tous les éléments*/
    private void ajouterComportementRedimensionnement() {
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ecranPlateauDeJeu.setBounds(0, 0, frame.getWidth(), frame.getHeight());
                backgroundBlur.setBounds(0, 0, frame.getWidth(), frame.getHeight());

                if (ecranMenu.isVisible()) {
                    ecranMenu.setBounds(frame.getWidth() - WIDTH_MENU, 0, WIDTH_MENU, frame.getHeight());
                } else {
                    ecranMenu.setBounds(frame.getWidth(), 0, WIDTH_MENU, frame.getHeight());
                }
            }
        });
    }


    // --- Gestion des menus ---
    /**
     * Ouvre le menu latéral avec animation*/
    public void ouvrirMenu() {
        if (!ecranMenu.isVisible()) {
            backgroundBlur.setVisible(true);
            ecranMenu.setVisible(true);

            new Thread(() -> {
                int x = frame.getWidth();
                while (x > frame.getWidth() - WIDTH_MENU) {
                    x -= 10;
                    ecranMenu.setBounds(x, 0, WIDTH_MENU, frame.getHeight());
                    try { Thread.sleep(2); } catch (InterruptedException ignored) {}
                }
                ecranMenu.setBounds(frame.getWidth() - WIDTH_MENU, 0, WIDTH_MENU, frame.getHeight());
            }).start();
        }
    }

    /**
     * Ferme le menu latéral avec animation*/
    public void fermerMenu() {
        new Thread(() -> {
            int x = ecranMenu.getX();
            while (x < frame.getWidth()) {
                x += 10;
                ecranMenu.setBounds(x, 0, WIDTH_MENU, frame.getHeight());
                try { Thread.sleep(2); } catch (InterruptedException ignored) {}
            }
            ecranMenu.setVisible(false);
            backgroundBlur.setVisible(false);
        }).start();
    }


    // --- Méthodes InterfaceUser ---
    @Override
    public void toggleIA() {
        // (Pas encore implémenté)
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


    /**
     * Lance l'interface graphique (à utiliser depuis le collecteur)     */
    public void lancer() {
        SwingUtilities.invokeLater(new InterfaceGraphique(jeu, collecteurEvent));
    }
    public void lancerPlatauDeJeu(){frame.setContentPane(layeredPane);}
}
