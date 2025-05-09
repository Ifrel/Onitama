package Vue;

import Modele.Jeu;
import Patterns.Observateur;
import Vue.Adaptateurs.AdaptateurAnnuler;
import Vue.Adaptateurs.AdaptateurBoutonTerrain;
import Vue.Adaptateurs.AdaptateurCarteUI;
import Vue.Adaptateurs.AdaptateurRefaire;
import Vue.Animations.BruitGrisAvecPointsPanel;
import Modele.Carte;
import Modele.Pion;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static Global.Config.*;
import static Global.Paths.PATH_CARTE_DRAGON;
import static Global.Paths.PATH_SON_1;
import static Vue.ConfigUI.DIM_CARTES;
import static Vue.Utils.MethodsStaticsUtils.*;


/**
 * Classe représentant l'interface graphique principale du plateau de jeu.
 * Elle observe le modèle (Jeu) et met à jour l'affichage en fonction des événements.
 */
public class EcranPlateauDeJeu extends BruitGrisAvecPointsPanel implements Observateur {
    // ===== logger =======
    private static final Logger logger = Logger.getLogger(EcranPlateauDeJeu.class.getName());

    // ====== Attributs principaux ======
    private final Jeu jeu;
    private final InterfaceGraphique interfaceGraphique;
    private final CollecteurEvenements collecteurEv;

    // Éléments de l'interface
    private JPanel terrain;
    private JPanel annulerRefairePanel; // Renommé pour éviter confusion
    private JPanel barreIndication; // Utilisé si vous le décommentez

    private JButton[][] buttonsTerrain;
    private JButton[] buttonsCartes;
    private JButton annuler, refaire;
    private JButton boutonSon; // Ajouté pour pouvoir modifier son texte
    private JButton boutonMenu; // Ajouté pour pouvoir y accéder si besoin

    private JLabel nomJoueurCourantLabel; // Renommé pour clarté
    private JLabel tempsLabel; // Renommé pour clarté
    private JLabel roundLabel; // Ajouté pour afficher le round
    private int numRound; // Géré par le modèle si possible

    // Gestion du son
    private Clip clip;
    private boolean musiqueActive = false; // État du son
    private final String cheminMusique = PATH_SON_1.toString(); // Chemin de la musique


    // Gestion du temps
    private Instant debutTempsPartie;
    private Timer timerPartie; // Référence au timer


    /**
     * Constructeur principal du EcranPlateauDeJeu
     * @param jeu modèle de données observé
     * @param collecteurEv gestionnaire des événements
     * @param interfaceGraphique Scène principale
     */
    public EcranPlateauDeJeu(Jeu jeu, CollecteurEvenements collecteurEv, InterfaceGraphique interfaceGraphique) {
        this.jeu = jeu;
        this.collecteurEv = collecteurEv;
        this.interfaceGraphique = interfaceGraphique;

        jeu.ajouteObservateur(this);

        // Pour le debug, utiliser un logger si possible, sinon commenter pour la production
         logger.info("Interface Plateau de jeu lancée");

        setLayout(new BorderLayout());
        setBackground(COULEUR_PLATEAU_DE_JEU);

        initialiserInterface();

        // Démarrer la musique au début si souhaité
        // toggleMusique(this.boutonSon); // Peut-être démarrer par défaut ou attendre action utilisateur
        // === Initialiser l'affichage une première fois ===
        miseAJour(); // Appeler miseAJour() après l'initialisation pour afficher l'état initial
    }



    // Constantes pour les espacements
    private static final int MAIN_INSET = 20;
    private static final int VERTICAL_GAP_ROW0_ROW1 = 20;
    private static final int VERTICAL_GAP_ROW1_ROW2 = 30;
    private static final int HORIZONTAL_STRUT_SIZE = 20;


    /**
     * Initialise l'interface utilisateur avec GridBagLayout.
     */

    private void initialiserInterface() {
        // === 1. Création des composants ===
        creerTerrain();
        creerButtonsCartes();
        creerButtonsAnnulerRefaire();
        boutonSon = creerBoutonSon();
        boutonMenu = creerBoutonMenu();
        JPanel roundTempsPanel = creerPanelRoundTemps();
        JPanel contenuCentrePanel = creerContenuCentre();
        JPanel cartesNordPanel = creerCartesNord();
        JPanel carteGauchePanel = creerCarteGauche();
        JPanel cartesSudPanel = creerCartesSud();
        JPanel annulerRefaire = creerBoutonsDroite();

        // === 2. Conteneur principal avec GridBagLayout ===
        JPanel contenu = new JPanel(new GridBagLayout());
        contenu.setBorder(BorderFactory.createEmptyBorder(MAIN_INSET, MAIN_INSET, MAIN_INSET, MAIN_INSET));
        contenu.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // === Ligne 0 : Haut (son | timer | menu) ===
        JPanel ligne0 = new JPanel();
        ligne0.setLayout(new BoxLayout(ligne0, BoxLayout.X_AXIS));
        ligne0.setOpaque(false);
        ligne0.add(boutonSon);
        ligne0.add(Box.createHorizontalGlue());
        ligne0.add(roundTempsPanel);
        ligne0.add(Box.createHorizontalStrut(HORIZONTAL_STRUT_SIZE));
        ligne0.add(boutonMenu);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, VERTICAL_GAP_ROW0_ROW1, 0);
        contenu.add(ligne0, gbc);

        // === Ligne 1 : Texte du tour ===
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(15, 0, 0, 0);
        contenu.add(contenuCentrePanel, gbc);

        // === Saut de ligne entre ligne 1 et 2 ===
        gbc.gridy = 2;
        gbc.weightx = 0.25;
        gbc.weighty = 0.25;
        contenu.add(Box.createGlue(), gbc);

        // === Ligne 3 : Plateau avec layout empilé ===
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel panelCentreEmpile = new JPanel(new GridBagLayout());
        panelCentreEmpile.setOpaque(false);
        GridBagConstraints centreGbc = new GridBagConstraints();
        centreGbc.fill = GridBagConstraints.BOTH;

        // Cartes nord
        centreGbc.gridx = 1;
        centreGbc.gridy = 0;
        centreGbc.weightx = 1.0;
        centreGbc.weighty = 1.0;
        centreGbc.insets = new Insets(20, 20, 20, 20);
        panelCentreEmpile.add(cartesNordPanel, centreGbc);

        // Espace gauche
        centreGbc.gridx = 0;
        centreGbc.gridy = 1;
        centreGbc.weightx = 1.0;
        centreGbc.weighty = 1.0;
        centreGbc.insets = new Insets(20, 20, 20, 20);
        panelCentreEmpile.add(creerCarteGauche(), centreGbc);

        // terrain
        centreGbc.gridx = 1;
        centreGbc.gridy = 1;
        centreGbc.weightx = 0.5;
        centreGbc.weighty = 0.5;
        centreGbc.insets = new Insets(20, 20, 20, 20);
        panelCentreEmpile.add(terrain, centreGbc);

        // Espace droite
        centreGbc.gridx = 2;
        centreGbc.gridy = 1;
        centreGbc.weightx = 1.0;
        centreGbc.weighty = 1.0;
        centreGbc.insets = new Insets(20, 20, 20, 20);
        panelCentreEmpile.add(annulerRefaire, centreGbc);

        // Cartes sud
        centreGbc.gridx = 1;
        centreGbc.gridy = 2;
        centreGbc.weightx = 1.0;
        centreGbc.weighty = 1.0;
        centreGbc.insets = new Insets(20, 20, 20, 20);
        panelCentreEmpile.add(cartesSudPanel, centreGbc);

        contenu.add(panelCentreEmpile, gbc);

        // === 3. Ajout du conteneur principal au panneau ===
        setLayout(new BorderLayout());
        add(contenu, BorderLayout.CENTER);

        // === 4. Démarrage du timer ===
        debutTempsPartie = Instant.now();
        timerPartie = new Timer(1000, e -> miseAjourTemps());
        timerPartie.start();
    }


    /*
    private void initialiserInterface() {
        // === 1. Création des composants ===
        creerTerrain();
        creerButtonsCartes();
        creerButtonsAnnulerRefaire();
        boutonSon = creerBoutonSon();
        boutonMenu = creerBoutonMenu();
        JPanel roundTempsPanel = creerPanelRoundTemps();
        JPanel contenuCentrePanel = creerContenuCentre();
        JPanel cartesNordPanel = creerCartesNord();
        JPanel carteGauchePanel = creerCarteGauche();
        JPanel cartesSudPanel = creerCartesSud();
        JPanel annulerRefaire = creerBoutonsDroite();

        // === 2. Conteneur principal avec GridBagLayout ===
        JPanel contenu = new JPanel(new GridBagLayout());
        contenu.setBorder(BorderFactory.createEmptyBorder(MAIN_INSET, MAIN_INSET, MAIN_INSET, MAIN_INSET));
        contenu.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        // === Ligne 0 : Haut (son | timer | menu) ===
        JPanel ligne0 = new JPanel();
        ligne0.setLayout(new BoxLayout(ligne0, BoxLayout.X_AXIS));
        ligne0.setOpaque(false);
        ligne0.add(boutonSon);
        ligne0.add(Box.createHorizontalGlue());
        ligne0.add(roundTempsPanel);
        ligne0.add(Box.createHorizontalStrut(HORIZONTAL_STRUT_SIZE));
        ligne0.add(boutonMenu);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0, 0, VERTICAL_GAP_ROW0_ROW1, 0);
        contenu.add(ligne0, gbc);

        // === Ligne 1 : Texte du tour ===
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(15, 0, VERTICAL_GAP_ROW1_ROW2 + 15, 0);
        contenu.add(contenuCentrePanel, gbc);

        // === Ligne 2 : Plateau de jeu centré ===
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);

        // === Centre empilé : cartes nord, terrain, cartes sud ===
        JPanel panelCentreEmpile = new JPanel(new GridBagLayout());
        panelCentreEmpile.setOpaque(false);

        GridBagConstraints centreGbc = new GridBagConstraints();
        centreGbc.insets = new Insets(0, 0, 0, 0);
        centreGbc.fill = GridBagConstraints.BOTH;

        // Cartes nord
        centreGbc.gridx = 1;
        centreGbc.gridy = 0;
        centreGbc.weightx = 0.8;
        centreGbc.weighty = 1.0;
        panelCentreEmpile.add(cartesNordPanel, centreGbc);

        // Carte à gauche
        centreGbc.gridx = 0;
        centreGbc.gridy = 1;
        centreGbc.weightx = 0.1;
        centreGbc.weighty = 1.0;
        centreGbc.insets = new Insets(20, 20, 20, 20);
        panelCentreEmpile.add(carteGauchePanel, centreGbc);

        // Terrain
        centreGbc.gridx = 1;
        centreGbc.gridy = 1;
        centreGbc.weightx = 0;
        centreGbc.weighty = 0;
        centreGbc.insets = new Insets(40, 20, 40, 20);
        panelCentreEmpile.add(terrain, centreGbc);
        centreGbc.insets = new Insets(20, 20, 20, 20);

        // Boutons annuler/refaire
        centreGbc.gridx = 2;
        centreGbc.gridy = 1;
        centreGbc.weightx = 0.1;
        centreGbc.weighty = 1.0;
        panelCentreEmpile.add(annulerRefaire, centreGbc);
        centreGbc.insets = new Insets(0, 0, 0, 0); // reset

        // Cartes sud
        centreGbc.gridx = 1;
        centreGbc.gridy = 2;
        centreGbc.weightx = 0.8;
        centreGbc.weighty = 1.0;
        panelCentreEmpile.add(cartesSudPanel, centreGbc);


        contenu.add(panelCentreEmpile, gbc);

        // === 3. Ajout du conteneur principal au panneau ===
        setLayout(new BorderLayout());
        add(contenu, BorderLayout.CENTER);

        // === 4. Démarrage du timer ===
        debutTempsPartie = Instant.now();
        timerPartie = new Timer(1000, e -> miseAjourTemps());
        timerPartie.start();
    }
*/



    @Override
    public void miseAJour() {
        // Cette méthode est appelée chaque fois que le modèle 'jeu' notifie ses observateurs.
        // Elle doit lire l'état actuel du jeu et mettre à jour l'interface graphique.

        System.out.println("Mise à jour de l'interface..."); // Pour vérifier si elle est appelée

        // 1. Mettre à jour le plateau de jeu (les boutons du terrain)
        updateTerrain();

        // 2. Mettre à jour l'affichage des cartes du joueur courant
        updateCartes();

        // 3. Mettre à jour les informations du joueur courant et du round
        updatePlayerAndRoundInfo();

        // 4. Mettre à jour l'état (activé/désactivé) des boutons Annuler/Refaire
        updateUndoRedoButtons();

        // Si d'autres éléments dépendent de l'état du jeu, ajoutez leurs mises à jour ici.

        // Note : Le timer est géré séparément par son propre TimerTask.
    }



    // =========================================
    // ============ Création UI ================
    // =========================================

    /** Crée la grille du terrain de jeu */
    private void creerTerrain() {
        terrain = new JPanel(new GridLayout(LIGNES, COLONNES, 0, 0));
        buttonsTerrain = new JButton[LIGNES][COLONNES];

        terrain.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 206, 206), 5, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        terrain.setBackground(new Color(226, 226, 226));

        for (int row = 0; row < LIGNES; row++) {
            for (int col = 0; col < COLONNES; col++) {
                JButton bouton = creerBoutonTerrain(); // Méthode statique de MethodsStaticsUtils
                // L'action listener dépend de la position et du collecteur d'événements
                bouton.addActionListener(new AdaptateurBoutonTerrain(bouton, new Point(row, col), collecteurEv));
                buttonsTerrain[row][col] = bouton;
                terrain.add(bouton);
            }
        }
        // L'affichage initial du contenu des cases (pions) se fait dans updateTerrain()
    }


    /** Crée les boutons représentant les cartes (structure vide, l'affichage sera fait dans updateCartes)*/
    private void creerButtonsCartes() {
        buttonsCartes = new JButton[NOMBRES_CARTES_PLATEAU];
        for (int i = 0; i < buttonsCartes.length; i++) {
            JButton bouton = creerBoutonCarte(PATH_CARTE_DRAGON);
            bouton.addActionListener(new AdaptateurCarteUI(new CarteUI(bouton, i), collecteurEv));
            buttonsCartes[i] = bouton;
        }
    }


    /** Crée les boutons "Annuler" et "Refaire" */
    private void creerButtonsAnnulerRefaire() {
        annulerRefairePanel = new JPanel(new GridLayout(4, 1, 20, 10));
//        annulerRefairePanel.setOpaque(false);

        annuler = creerBoutonAction("Annuler");
        refaire = creerBoutonAction("Refaire");

        annuler.addActionListener(new AdaptateurAnnuler(collecteurEv));
        refaire.addActionListener(new AdaptateurRefaire(collecteurEv));

        annulerRefairePanel.add(Box.createVerticalGlue());
        annulerRefairePanel.add(annuler);
        annulerRefairePanel.add(refaire);
        annulerRefairePanel.add(Box.createVerticalGlue());
    }


    // --- Méthodes de création des conteneurs de layout spécifiques ---

    private JPanel creerCartesNord() {
        JPanel cartes = new JPanel();
        cartes.setLayout(new BoxLayout(cartes, BoxLayout.X_AXIS));
        cartes.setPreferredSize(DIM_CARTES);
        cartes.setOpaque(false);

        cartes.add(Box.createHorizontalGlue());
        cartes.add(Box.createHorizontalGlue());
        cartes.add(buttonsCartes[0]);
        cartes.add(Box.createHorizontalStrut(25));
        cartes.add(buttonsCartes[1]);
        cartes.add(Box.createHorizontalGlue());
        cartes.add(Box.createHorizontalGlue());

        return cartes;
    }


    private JPanel creerCartesSud() {
        JPanel cartes = new JPanel();
        cartes.setLayout(new BoxLayout(cartes, BoxLayout.X_AXIS));
        cartes.setPreferredSize(DIM_CARTES);
        cartes.setOpaque(false);

        cartes.add(Box.createHorizontalGlue());
        cartes.add(Box.createHorizontalGlue());
        cartes.add(buttonsCartes[2]);
        cartes.add(Box.createHorizontalStrut(25));
        cartes.add(buttonsCartes[3]);
        cartes.add(Box.createHorizontalGlue());
        cartes.add(Box.createHorizontalGlue());
        return cartes;
    }

    private JPanel creerCarteGauche() {
        JPanel carte = new JPanel();
        carte.setLayout(new BoxLayout(carte, BoxLayout.Y_AXIS));
        carte.setOpaque(false);
        carte.add(Box.createVerticalGlue());
        carte.add(buttonsCartes[4]);
        carte.add(Box.createVerticalGlue());

        JPanel gauche = new JPanel();
        gauche.setLayout(new BoxLayout(gauche, BoxLayout.X_AXIS));
        gauche.add(Box.createHorizontalGlue());
        gauche.setPreferredSize(DIM_CARTES);
        gauche.add(carte);
//        gauche.setOpaque(false);

        return gauche;
    }

    private JPanel creerBoutonsDroite() {
        JPanel droite = new JPanel();
        droite.setLayout(new BoxLayout(droite, BoxLayout.X_AXIS));
        droite.setPreferredSize(DIM_CARTES);
        droite.add(annulerRefairePanel);
        droite.add(Box.createHorizontalGlue());
//        droite.setOpaque(false);
        return droite;
    }

    private JButton creerBoutonSon() {
        JButton bouton = new JButton("son");
        bouton.setForeground(Color.WHITE);
        bouton.setFont(new Font("Arial", Font.PLAIN, 20));
        bouton.setBackground(new Color(237, 237, 237, 16));
        bouton.setContentAreaFilled(false);
        bouton.setFocusPainted(false);
        bouton.setPreferredSize(new Dimension(60, 40));
        // Texte initial "son" ou "off" si musique désactivée par défaut
        bouton.setText(musiqueActive ? "on" : "off");
        bouton.addActionListener(e -> toggleMusique(bouton));

//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setOpaque(false);
////        panel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0)); // Marge à droite
//        panel.add(bouton, BorderLayout.CENTER);
        return bouton;
    }

    private JPanel creerContenuCentre() {
        JPanel textNomPanel = new JPanel(); // Renommé
        textNomPanel.setLayout(new BoxLayout(textNomPanel, BoxLayout.Y_AXIS));
        textNomPanel.setOpaque(false);

        JLabel txt = new JLabel("C'est au tour de");
        txt.setFont(new Font("Arial", Font.PLAIN, 20));
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
        txt.setForeground(new Color(232, 231, 231));

        nomJoueurCourantLabel = new JLabel("Nom Joueur"); // Initialisé ici, mis à jour dans miseAJour()
        nomJoueurCourantLabel.setFont(new Font("Arial", Font.BOLD, 38));
        nomJoueurCourantLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nomJoueurCourantLabel.setForeground(new Color(218, 214, 214));

        textNomPanel.add(txt);
        textNomPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textNomPanel.add(nomJoueurCourantLabel);


        return textNomPanel;
    }

    private JPanel creerPanelRoundTemps() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 206, 206), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        panel.setBackground(new Color(245, 245, 245, 23));

        // numRound est géré dans miseAJour(), ici on initialise le label
        roundLabel = new JLabel("Round: ?"); // Initialisé ici
        roundLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        roundLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));

        tempsLabel = new JLabel("00:00"); // Initialisé ici
        tempsLabel.setFont(new Font("Arial", Font.BOLD, 25));

        panel.add(roundLabel);
        panel.add(tempsLabel);

        // Le timer sera démarré après que tous les composants soient créés dans initialiserInterface()
        return panel;
    }

    private JButton creerBoutonMenu() {
        JButton menu = new JButton("≡");
        menu.setOpaque(false);
        menu.setContentAreaFilled(false);
        menu.setFocusPainted(false);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Arial", Font.PLAIN, 46)); // Grande taille pour le symbole
        menu.setPreferredSize(new Dimension(60, 40)); // Ajuster la taille si besoin

        menu.addActionListener(e -> interfaceGraphique.ouvrirMenu());

//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setOpaque(false);
//        panel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0)); // Marge à droite
//        panel.add(menu, BorderLayout.CENTER);
        return menu;
    }


    // =========================================
    // ========= Gestion Son & Musique =========
    // =========================================
    // Ces méthodes semblent correctes pour une gestion simple du son.
    // Pensez à gérer la fermeture du clip à la fin de l'application (voir note dans initialiserInterface)

    private void toggleMusique(JButton bouton) {
        if (musiqueActive) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
            bouton.setText("off");
        } else {
            // Jouer la musique si elle n'est pas déjà en cours
            if (clip == null || !clip.isRunning()) {
                jouerMusique(cheminMusique); // Utilise l'attribut cheminMusique
            }
            bouton.setText("on");
        }
        musiqueActive = !musiqueActive;
    }

    private void jouerMusique(String chemin) {
        try {
            // S'assurer que l'ancien clip est fermé avant d'en ouvrir un nouveau
            if (clip != null) {
                clip.close();
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    Objects.requireNonNull(getClass().getResource(chemin), "Ressource audio introuvable: " + chemin) // Message d'erreur plus clair
            );
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture audio: " + e.getMessage()); // Utiliser System.err pour les erreurs ou un logger
            // e.printStackTrace(); // Ne pas imprimer la stack trace complète en production
            // Optionnel : désactiver le bouton son si une erreur se produit
            if (boutonSon != null) {
                boutonSon.setEnabled(false);
                boutonSon.setText("Sound Error");
            }
            musiqueActive = false; // Mettre à jour l'état
        }
    }


    // =========================================
    // ============== Mise à jour ==============
    // =========================================
    // Ces méthodes sont appelées par miseAJour() pour actualiser l'UI

    // Mise à jour du temps (appelée par le Timer)
    private void miseAjourTemps() { // Supprimé le paramètre Instant debut
        if (debutTempsPartie != null && tempsLabel != null) { // Vérifier si les attributs sont initialisés
            Duration duration = Duration.between(debutTempsPartie, Instant.now());
            long minutes = duration.toMinutes();
            long secondes = duration.getSeconds() % 60;
            tempsLabel.setText(String.format("%02d:%02d", minutes, secondes));
        }
    }


    // Met à jour l'affichage du terrain en fonction de l'état du jeu
    private void updateTerrain() {
        // Exemple conceptuel : suppose que jeu.getPlateau() retourne un tableau 2D ou similaire
        // et que jeu.getPionAt(ligne, colonne) retourne le pion (ou null) à cette position.
        if (jeu != null && buttonsTerrain != null) {
            for (int row = 0; row < LIGNES; row++) {
                for (int col = 0; col < COLONNES; col++) {
                    Pion pion = jeu.getPionAt(row, col); // Méthode à implémenter dans votre classe Jeu
                    JButton bouton = buttonsTerrain[row][col];

                    if (pion != null) {
                        // Afficher le pion sur le bouton. Ex: changer l'icône ou le texte.
                        // Cela dépend de la manière dont vous représentez les pions.
                        // Exemple simple avec texte :
                        bouton.setText(pion.getType() + ""); // Afficher l'initiale du type de pion
                        bouton.setForeground(pion.getCouleur()); // Changer la couleur du texte selon le joueur

                        // Exemple avec icône (nécessite une méthode pour obtenir l'icône du pion)
                        // ImageIcon icon = getIconForPion(pion); // Méthode utilitaire à créer
                        // bouton.setIcon(icon);
                        // bouton.setText(""); // Enlever le texte si icône
                    } else {
                        // Case vide
                        bouton.setText("");
                        // bouton.setIcon(null);
                    }

                    // Optionnel : Activer/désactiver le bouton si la case est jouable dans l'état actuel
                    // boolean isPlayable = jeu.isCasePlayable(row, col); // Méthode à implémenter dans Jeu
                    // bouton.setEnabled(isPlayable);
                }
            }
        }
    }

    // Met à jour l'affichage des cartes du joueur courant
    private void updateCartes() {
        // Exemple conceptuel : suppose que jeu.getJoueurCourant() et joueur.getCartesEnMain() existent
        if (jeu != null && buttonsCartes != null) {
            List<Carte> cartesEnMain = jeu.getJoueurCourant().getCartesEnMain(); // Méthodes à implémenter

            // Assurez-vous que le nombre de cartes en main correspond au nombre de boutons de cartes
            // ou gérez les index en conséquence.
            for (int i = 0; i < buttonsCartes.length; i++) {
                JButton bouton = buttonsCartes[i];

                if (i < cartesEnMain.size()) {
                    Carte carte = cartesEnMain.get(i);
                    // Afficher l'image de la carte sur le bouton
                    ImageIcon icon = getIconForCarte(carte); // Méthode utilitaire à créer
                    bouton.setIcon(icon);
                    bouton.setEnabled(true); // La carte est en main, donc potentiellement utilisable
                    // Peut-être désactiver si le coup n'est pas valide pour la carte dans l'état actuel
                    // boolean isCardPlayable = jeu.isCarteJouable(carte); // Méthode à implémenter
                    // bouton.setEnabled(isCardPlayable);
                } else {
                    // Pas de carte à cet index pour le joueur courant (main plus petite que NOMBRES_CARTES_PLATEAU)
                    bouton.setIcon(null); // Ou une icône vide
                    bouton.setText(""); // Assurez-vous que le texte est vide
                    bouton.setEnabled(false); // Pas de carte, bouton désactivé
                }
            }
        }
    }

    // Méthode utilitaire pour obtenir l'icône d'une carte (à implémenter)
    private ImageIcon getIconForCarte(Carte carte) {
        // Exemple : Charger une image basée sur le type ou le nom de la carte
        String cheminImage = "res/vue/images/cartes/" + carte.getNom() + ".png"; // Supposons que Carte a un getNom()
        java.net.URL imgURL = getClass().getResource(cheminImage);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Ressource d'image de carte introuvable : " + cheminImage);
            return null; // Ou retourner une icône par défaut/vide
        }
    }


    // Met à jour l'affichage du joueur courant et du numéro de round
    private void updatePlayerAndRoundInfo() {
        if (jeu != null && nomJoueurCourantLabel != null && roundLabel != null) {
            // Suppose que jeu.getJoueurCourant() retourne l'objet Joueur courant
            // et que Joueur a une méthode getName()
            String nomJoueur = jeu.getJoueurCourant().getNom(); // Méthodes à implémenter
            nomJoueurCourantLabel.setText(nomJoueur);

            // Suppose que jeu.getRoundNumber() retourne le numéro du round
            numRound = jeu.getNumeroRound(); // Méthode à implémenter
            roundLabel.setText("Round: " + numRound);

            // Optionnel : Changer la couleur du texte du joueur courant pour qu'elle corresponde à sa couleur de pion
            // nomJoueurCourantLabel.setForeground(jeu.getJoueurCourant().getCouleurPion()); // Méthode à implémenter
        }
    }

    // Met à jour l'état des boutons Annuler/Refaire
    private void updateUndoRedoButtons() {
        if (jeu != null && annuler != null && refaire != null) {
            // Suppose que jeu.peutAnnuler() et jeu.peutRefaire() existent et retournent boolean
            annuler.setEnabled(jeu.peutAnnulerCoup());
            refaire.setEnabled(jeu.peutRefaireCoup());
        }
    }

    // =========================================
    // ============== Méthodes Utiles ==============
    // =========================================

    // Méthode pour arrêter le timer et le son (à appeler lors de la fermeture de la fenêtre)
    public void cleanup() {
        if (timerPartie != null && timerPartie.isRunning()) {
            timerPartie.stop();
        }
        if (clip != null) {
            clip.close();
        }
        System.out.println("Nettoyage de EcranPlateauDeJeu effectué.");
    }

}
