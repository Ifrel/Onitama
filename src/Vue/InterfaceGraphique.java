package Vue;

import Modele.Jeu;
import Patterns.Observateur;
import Vue.Adaptateurs.AdaptateurClavier;

import javax.swing.*;
import java.awt.*;

/**
 * Classe principale pour l'affichage graphique du jeu Onitama.
 * Gère la fenêtre, le plateau, les couches superposées, et les interactions comme le menu latéral.*/
public class InterfaceGraphique implements Runnable, InterfaceUser, Observateur {
    // --- Attributs principaux ---
    private CollecteurEvenements collecteurEvent;
    private boolean maximized;
    private Jeu jeu;

    // --- Composants Swing ---
    private JFrame frame;
    private JLayeredPane layeredPane;
    private JPanel backgroundBlur;
    private JPanel menuPanel;
    private PlateauDeJeu plateau;

    /**
     * Gestionaire de toutes les interfaces graphiques
     * @param jeu modèle de données observé
     * @param collecteurEvent gestionnaire des événements */
    public InterfaceGraphique(Jeu jeu, CollecteurEvenements collecteurEvent) {
        this.jeu = jeu;
        this.collecteurEvent = collecteurEvent;
        this.maximized = false;

        initialiserFenetrePrincipale();
        initialiserLayeredPane();

        // Observer pour mettre à jour l'affichage si nécessaire
        jeu.ajouteObservateur(this);
    }

    /**
     * Initialise la fenêtre principale (JFrame)*/
    private void initialiserFenetrePrincipale() {
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
        initialiserPlateau();
        initialiserBackgroundBlur();
        initialiserMenuPanel();
        ajouterComportementRedimensionnement();

        frame.setContentPane(layeredPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    @Override
    public void miseAJour() {
        // (Réagir aux changements du modèle ici si nécessaire)
    }


    /**
     * Initialise le plateau de jeu */
    private void initialiserPlateau() {
        plateau = new PlateauDeJeu(jeu, collecteurEvent, this);
        plateau.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        layeredPane.add(plateau, JLayeredPane.DEFAULT_LAYER);
    }

    /**
     * Crée un panneau gris semi-transparent pour le flou de fond*/
    private void initialiserBackgroundBlur() {
        backgroundBlur = new JPanel();
        backgroundBlur.setBackground(new Color(161, 160, 160, 50)); // Gris transparent
        backgroundBlur.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        backgroundBlur.setOpaque(true);
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
    private void initialiserMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setBackground(new Color(228, 169, 108)); // Couleur du menu
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBounds(frame.getWidth(), 0, 200, frame.getHeight());
        menuPanel.setVisible(false);

        // Ajouter quelques options au menu
        menuPanel.add(new JLabel("Option 1"));
        menuPanel.add(new JLabel("Option 2"));
        menuPanel.add(new JLabel("Option 3"));

        layeredPane.add(menuPanel, JLayeredPane.MODAL_LAYER);
    }

    /**
     * Gère le redimensionnement dynamique de tous les éléments*/
    private void ajouterComportementRedimensionnement() {
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                plateau.setBounds(0, 0, frame.getWidth(), frame.getHeight());
                backgroundBlur.setBounds(0, 0, frame.getWidth(), frame.getHeight());

                if (menuPanel.isVisible()) {
                    menuPanel.setBounds(frame.getWidth() - 200, 0, 200, frame.getHeight());
                } else {
                    menuPanel.setBounds(frame.getWidth(), 0, 200, frame.getHeight());
                }
            }
        });
    }


    // --- Gestion des menus ---
    /**
     * Ouvre le menu latéral avec animation*/
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

    /**
     * Ferme le menu latéral avec animation*/
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
}
