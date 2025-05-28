package Vue;

import Modele.Jeu;
import Patterns.Observateur;
import Vue.Adaptateurs.AdaptateurClavier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static Global.Config.DIM_SCENE;
import static Vue.Configuration.ConfigUI.WIDTH_MENU;

public class InterfaceGraphique extends Component implements Runnable, InterfaceUser, Observateur {
    private final CollecteurEvenements collecteurEvent;
    private boolean maximized;
    private final Jeu jeu;

    JFrame frame;
    private final JLayeredPane layeredPane;
    private final JPanel backgroundBlur;

    private EcranMenu ecranMenu;
    private EcranPlateauDeJeu ecranPlateauDeJeu;
    private EcranDeDemarrage ecranDeDemarrage;

    private static final Logger logger = Logger.getLogger(InterfaceGraphique.class.getName());

//    private static InterfaceGraphique instance;

    private static final AtomicReference<InterfaceGraphique> instance = new AtomicReference<>();
    private final ExecutorService animationExecutor = Executors.newSingleThreadExecutor();
    private EcranVictoire ecranVictoire;


    /**
     * Gestionnaire de toutes les interfaces graphiques
     * @param jeu modèle de données observé
     * @param collecteurEvent gestionnaire des événements */
    public InterfaceGraphique(Jeu jeu, CollecteurEvenements collecteurEvent) {
        this.jeu = jeu;
        this.collecteurEvent = collecteurEvent;
        this.maximized = false;
        this.frame = initialiserLaScene();
        this.layeredPane = initialiserLayeredPane();
        this.backgroundBlur = new JPanel();
        jeu.ajouteObservateur(this);

    }

    /**
     * Lance l'interface graphique
     */
    public static void lancerInterfaceGraphique(Jeu jeu, CollecteurEvenements collecteurEvenements) {
        try {
            logger.info("Lancement interface graphique");
            SwingUtilities.invokeLater(() -> {
                InterfaceGraphique ig = new InterfaceGraphique(jeu, collecteurEvenements);
                instance.set(ig);
                new Thread(ig).start();
            });
            logger.info("Interface graphique lancée");
        } catch (Exception e) {
            logger.severe(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public static InterfaceGraphique getInstance() {
        return instance.get();
    }


    /**
     * Initialise la fenêtre principale (JFrame)*/
    private JFrame initialiserLaScene() {
        JFrame frame = new JFrame("Onitama");
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(DIM_SCENE);
        frame.setMinimumSize(DIM_SCENE);
        frame.addKeyListener(new AdaptateurClavier(collecteurEvent));
        ajouterEcouteurFermeture(frame);
        return frame;
    }


    /**
     * Initialise le JLayeredPane qui superpose les composants*/
    private JLayeredPane initialiserLayeredPane() {
        JLayeredPane pane = new JLayeredPane();
        pane.setLayout(null);
        return pane;
    }


    /**
     * Ajoute un écouteur pour gérer la fermeture de la fenêtre proprement
     */
    private void ajouterEcouteurFermeture(JFrame frame) {
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                fermerApplication();
            }
        });
    }




    @Override
    public void miseAJour() {
        logger.info("Mise à jour \"InterfaceGraphique\"");
        SwingUtilities.invokeLater(this::rafraichirInterface);
    }

    private void rafraichirInterface() {
        frame.revalidate();
        frame.repaint();
    }



    // --- Lancement graphique ---
    @Override
    public void run() {
        initialiserComposants();
        configurerFenetre();
    }



    private void initialiserComposants() {
        initialiserEcranDemarrage();
        initialiserPlateau();
        initialiserBackgroundBlur();
        initialiserMenu();
        ajouterComportementRedimensionnement();
    }

    private void configurerFenetre() {
        frame.setContentPane(ecranDeDemarrage);
        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void initialiserEcranDemarrage() {
        ecranDeDemarrage = new EcranDeDemarrage(jeu, this);
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
        backgroundBlur.setToolTipText("JEU EN PAUSE");
        backgroundBlur.setBackground(new Color(161, 160, 160, 50));
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
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                mettreAJourDispositions();
            }
        });
    }


    public void ouvrirMenu() {
        if (!ecranMenu.isVisible()) {
            logger.info("Ouverture menu");
            backgroundBlur.setVisible(true);
            ecranMenu.setVisible(true);
            animerDeplacementEcran(ecranMenu.getX(), frame.getWidth() - WIDTH_MENU, () -> {});
        }
    }

    public void fermerMenu() {
        logger.info("Fermeture menu");
        int xCourant = ecranMenu.getX();
        animerDeplacementEcran(xCourant, frame.getWidth(), () -> {
            ecranMenu.setVisible(false);
            backgroundBlur.setVisible(false);
        });
    }



    private void animerDeplacementEcran(int xDepart, int xArrivee, Runnable callback) {
        animationExecutor.execute(new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                int x = xDepart;
                int pas = xArrivee > xDepart ? 50 : -50;
                while ((pas > 0 && x < xArrivee) || (pas < 0 && x > xArrivee)) {
                    x += pas;
                    if ((pas > 0 && x > xArrivee) || (pas < 0 && x < xArrivee)) {
                        x = xArrivee;
                    }
                    publish(x);
                    Thread.sleep(25);
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                int xFinal = chunks.get(chunks.size() - 1);
                ecranMenu.setBounds(xFinal, 0, WIDTH_MENU, frame.getHeight());
            }

            @Override
            protected void done() {
                if (callback != null) {
                    SwingUtilities.invokeLater(callback);
                }
            }
        });
    }


    private void initialiserEcranDeDemarage() {
        ecranDeDemarrage = new EcranDeDemarrage(jeu, this);
        ecranDeDemarrage.setBounds(0, 0, frame.getWidth(), frame.getHeight());
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


    void mettreAJourDispositions() {
        logger.fine("Mise à jour des dispositions suite au redimensionnement.");
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();

        if (ecranDeDemarrage != null ) {
            ecranDeDemarrage.setBounds(0, 0, frameWidth, frameHeight);
            ecranDeDemarrage.setBounds(0, 0, frameWidth, frameHeight);
        }

        if (ecranPlateauDeJeu != null) {
            ecranPlateauDeJeu.setBounds(0, 0, frameWidth, frameHeight);
        }


        if (backgroundBlur != null) {
            backgroundBlur.setBounds(0, 0, frameWidth, frameHeight);
        }

        if (ecranMenu != null) {
            // Ajuster la position du menu s'il est visible ou selon sa logique
            if (ecranMenu.isVisible()) {
                ecranMenu.setBounds(frameWidth - WIDTH_MENU, 0, WIDTH_MENU, frameHeight);
            } else {
                ecranMenu.setBounds(frameWidth, 0, WIDTH_MENU, frameHeight);
            }
        }
        if (ecranVictoire != null && ecranVictoire.isVisible()) {
            ecranVictoire.setBounds(0, 0, frameWidth, frameHeight);
        }

        frame.revalidate();
        frame.repaint();
    }

    /**
     * Ferme proprement l'application en arrêtant tous les timers et clips
     */
    private void fermerApplication() {
        if (ecranPlateauDeJeu != null) {
            if (ecranPlateauDeJeu.timerPartie != null && ecranPlateauDeJeu.timerPartie.isRunning()) {
                ecranPlateauDeJeu.timerPartie.stop();
            }
            if (ecranPlateauDeJeu.clip != null && ecranPlateauDeJeu.clip.isRunning()) {
                ecranPlateauDeJeu.clip.stop();
            }
        }
        animationExecutor.shutdown();
        frame.dispose();
        System.exit(0);
    }

    public EcranPlateauDeJeu getEcranPlateauDeJeu(){
        return ecranPlateauDeJeu;
    }


    public void setContentPane(Container component) {
        frame.setContentPane(component);
    }





    /**
     * Affiche l'écran de victoire dans l'application.
     *
     * @param nomGagnant Le nom du joueur gagnant.
     */
    public void afficherEcranVictoire(String nomGagnant) {
        // Créer et configurer l'écran de victoire
        ecranVictoire = new EcranVictoire(nomGagnant, this);

        // Ajuster la taille de l'écran de victoire
        ecranVictoire.setBounds(0, 0, frame.getWidth(), frame.getHeight());

        // Ajouter l'écran de victoire au layeredPane avec la plus haute priorité
        layeredPane.add(ecranVictoire, JLayeredPane.POPUP_LAYER);
        layeredPane.moveToFront(ecranVictoire);

        // Mettre à jour l'affichage
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Ferme l'écran de victoire et retourne au menu principal
     */
    public void retournerAuMenu() {
        if (ecranVictoire != null) {
            layeredPane.remove(ecranVictoire);
            ecranVictoire = null;

            // Réinitialiser et afficher le menu
            initialiserMenu();
            ouvrirMenu();

            // Mettre à jour l'affichage
            frame.revalidate();
            frame.repaint();
        }
    }

    /**
     * Ferme l'écran de victoire et lance une nouvelle partie
     */
    public void demarrerNouvellePartie() {
        // 1. Réinitialiser le jeu
        jeu.nouvellePartie();

        // 2. Nettoyer le layeredPane
        layeredPane.removeAll();

        // 3. Réinitialiser les composants du jeu
        initialiserPlateau();
        initialiserBackgroundBlur();
        initialiserMenu();

        // 4. Relancer l'affichage du plateau
        lancerPlateauDeJeu();

        // 5. Mettre à jour l'affichage
        SwingUtilities.invokeLater(() -> {
            frame.revalidate();
            frame.repaint();
        });

        // 6. Réinitialiser la référence à l'écran de victoire
        ecranVictoire = null;

    }



    private void secouerFenetre() {
        final Point positionOriginale = frame.getLocation();
        final int nombreSecousses = 5;
        final int amplitude = 10;
        final int delai = 50;

        Timer timer = new Timer(delai, null);
        final int[] compteur = {0};

        timer.addActionListener(e -> {
            if (compteur[0] >= nombreSecousses * 2) {
                timer.stop();
                frame.setLocation(positionOriginale);
                return;
            }

            int deplacement = (compteur[0] % 2 == 0) ? amplitude : -amplitude;
            frame.setLocation(positionOriginale.x + deplacement, positionOriginale.y);
            compteur[0]++;
        });

        SwingUtilities.invokeLater(() -> timer.start());
        System.out.println("Secouers");
    }


    public void signalerActionInvalide() {
        secouerFenetre();
    }



    public CollecteurEvenements getControler() {
        return collecteurEvent;
    }
}