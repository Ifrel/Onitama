package Vue;

import Modele.Jeu;
import Patterns.Observateur;
import Vue.Adaptateurs.AdaptateurClavier;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

import static Global.Config.DIM_SCENE;
import static Vue.Configuration.ConfigUI.WIDTH_MENU;

public class InterfaceGraphique extends Component implements Runnable, InterfaceUser, Observateur {
    private final CollecteurEvenements collecteurEvent;
    private boolean maximized;
    private final Jeu jeu;

    private JFrame frame;
    private JLayeredPane layeredPane;
    private JPanel backgroundBlur;

    private EcranMenu ecranMenu;
    private EcranPlateauDeJeu ecranPlateauDeJeu;
    private EcranDeDemarrage ecranDeDemarrage;

    private static final Logger logger = Logger.getLogger(InterfaceGraphique.class.getName());

    private static InterfaceGraphique instance;

    /**
     * Gestionnaire de toutes les interfaces graphiques
     * @param jeu modèle de données observé
     * @param collecteurEvent gestionnaire des événements */
    public InterfaceGraphique(Jeu jeu, CollecteurEvenements collecteurEvent) {
        this.jeu = jeu;
        this.collecteurEvent = collecteurEvent;
        this.maximized = false;

        InitiliserLaScene();
        initialiserLayeredPane();

        // Observer pour mettre à jour l'affichage si nécessaire
        jeu.ajouteObservateur(this);
    }

    /**
     * Lance l'interface graphique
     */
    public static void lancerInterfaceGraphique(Jeu jeu, CollecteurEvenements collecteurEvenements) {
        try {
            logger.info("Lancement interface graphique");
            SwingUtilities.invokeLater(() -> {
                instance = new InterfaceGraphique(jeu, collecteurEvenements);
                new Thread(instance).start();
            });
            logger.info("Interface graphique lancée");
        } catch (Exception e) {
            logger.severe(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public static InterfaceGraphique getInstance() {
        return instance;
    }

    /**
     * Initialise la fenêtre principale (JFrame)*/
    private void InitiliserLaScene() {
        frame = new JFrame("Onitama");
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(DIM_SCENE);
        frame.setMinimumSize(DIM_SCENE);
        frame.addKeyListener(new AdaptateurClavier(collecteurEvent));
        ajouterEcouteurFermeture();
    }

    /**
     * Initialise le JLayeredPane qui superpose les composants*/
    private void initialiserLayeredPane() {
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
    }

    /**
     * Ajoute un écouteur pour gérer la fermeture de la fenêtre proprement
     */
    private void ajouterEcouteurFermeture() {
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                fermerApplication();
            }
        });
    }

    @Override
    public void miseAJour() {
        // (Réagir aux changements du modèle ici si nécessaire)
        logger.info("Mise à jour \"InterfaceGraphique\"");
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
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // La fermeture sera gérée par l'écouteur windowClosing
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

    private void initialiserEcranDeDemarage() {
        ecranDeDemarrage = new EcranDeDemarrage(jeu, this);
        ecranDeDemarrage.setBounds(0, 0, frame.getWidth(), frame.getHeight());
    }

    /**
     * Gère le redimensionnement dynamique de tous les éléments*/
    private void ajouterComportementRedimensionnement() {
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                mettreAJourDispositions();
            }
        });
    }

    // --- Gestion des menus ---

    /**
     * Met à jour la disposition des composants en fonction de la taille de la fenêtre
     */
    private void mettreAJourDispositions() {
        int width = frame.getWidth();
        int height = frame.getHeight();
        ecranPlateauDeJeu.setBounds(0, 0, width, height);
        backgroundBlur.setBounds(0, 0, width, height);
        if (ecranMenu.isVisible()) {
            ecranMenu.setBounds(width - WIDTH_MENU, 0, WIDTH_MENU, height);
        } else {
            ecranMenu.setBounds(width, 0, WIDTH_MENU, height);
        }
    }

    /**
     * Ouvre le menu latéral avec animation*/
    public void ouvrirMenu() {
        logger.info("Ouverture menu");
        if (!ecranMenu.isVisible()) {
            backgroundBlur.setVisible(true);
            ecranMenu.setVisible(true);
            animerDeplacementEcran(ecranMenu.getX(), frame.getWidth() - WIDTH_MENU, () -> {});
        }
    }

    /**
     * Ferme le menu latéral avec animation*/
    public void fermerMenu() {
        logger.info("Fermeture menu");
        int xCourant = ecranMenu.getX();
        animerDeplacementEcran(xCourant, frame.getWidth(), () -> {
            ecranMenu.setVisible(false);
            backgroundBlur.setVisible(false);
        });
    }

    // --- Méthodes InterfaceUser ---
    @Override
    public void toggleIA() {
        // (Pas encore implémenté)
    }

    /**
     * Animation générique pour déplacer l'écran (ou tout autre composant)
     */
    private void animerDeplacementEcran(int xDepart, int xArrivee, Runnable callback) {
        new Thread(() -> {
            int x = xDepart;
            int pas = xArrivee > xDepart ? 50 : -50;
            while ((pas > 0 && x < xArrivee) || (pas < 0 && x > xArrivee)) {
                x += pas;
                if ((pas > 0 && x > xArrivee) || (pas < 0 && x < xArrivee)) {
                    x = xArrivee;
                }
                final int xFinal = x;
                SwingUtilities.invokeLater(() -> ecranMenu.setBounds(xFinal, 0, WIDTH_MENU, frame.getHeight()));
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ignored) {
                }
            }
            if (callback != null) {
                SwingUtilities.invokeLater(callback);
            }
        }).start();
    }

    @Override
    public void toggleFullScreen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();

        if (maximized) {
            device.setFullScreenWindow(null);
            logger.info("Mode plein écran désactivé");
        } else {
            device.setFullScreenWindow(frame);
            logger.info("Mode plein écran activé");
        }
        maximized = !maximized;
    }


    /**
     * Lance la partie en affichant le plateau
     */
    public void lancerPlateauDeJeu() {
        frame.setContentPane(layeredPane);
        mettreAJourDispositions();
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Ferme proprement l'application en arrêtant tous les timers et clips
     */
    private void fermerApplication() {
        if (ecranPlateauDeJeu != null && ecranPlateauDeJeu.timerPartie != null && ecranPlateauDeJeu.timerPartie.isRunning()) {
            ecranPlateauDeJeu.timerPartie.stop();
        }
        if (ecranPlateauDeJeu != null && ecranPlateauDeJeu.clip != null && ecranPlateauDeJeu.clip.isRunning()) {
            ecranPlateauDeJeu.clip.stop();
        }
        frame.dispose();
        System.exit(0);
    }


    public EcranPlateauDeJeu getEcranPlateauDeJeu(){
        return ecranPlateauDeJeu;
    }
}