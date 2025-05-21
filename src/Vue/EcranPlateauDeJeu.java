//package Vue;
//
//import Modele.Carte;
//import Modele.CasePlateau;
//import Modele.Jeu;
//import Patterns.Observateur;
//import Vue.Adaptateurs.AdaptateurAnnuler;
//import Vue.Adaptateurs.AdaptateurBoutonTerrain;
//import Vue.Adaptateurs.AdaptateurCarte;
//import Vue.Adaptateurs.AdaptateurRefaire;
//import Vue.Animations.AnimationUtils.CardFlipAnimator;
//import Vue.Animations.AnimationUtils.CardFlipLayerUI;
//import Vue.LabO.BordureArrondieAvecOmbre;
//import Vue.Utils.Boutons.Bouton;
//import Vue.Utils.Boutons.Bouton.BoutonAvecImage;
//import Vue.Utils.Boutons.BoutonCarte;
//import Vue.Utils.Boutons.BoutonTerrain;
//import Vue.Utils.PanelBruitGris;
//import Vue.Utils.PanelRatioFixe;
//
//import javax.imageio.ImageIO;
//import javax.sound.sampled.AudioInputStream;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.Clip;
//import javax.swing.*;
//import javax.swing.border.Border;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.time.Duration;
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Objects;
//import java.util.logging.Logger;
//
//import static Global.Config.*;
//import static Global.Paths.*;
//import static Vue.ConfigUI.ARRONDI;
//import static Vue.Utils.MethodsStaticsUtils.*;
//
//
///**
// * Classe représentant l'interface graphique principale du plateau de jeu.
// * Elle observe le modèle (Jeu) et met à jour l'affichage en fonction des événements.
// */
//public class EcranPlateauDeJeu extends PanelBruitGris implements Observateur {
//    private static final Logger logger = Logger.getLogger(EcranPlateauDeJeu.class.getName());
//    private final InfosDeConfigUI infosDeConfigUI = InfosDeConfigUI.getInstance();
//
//    private final Jeu jeu;
//    private final InterfaceGraphique interfaceGraphique;
//    private final CollecteurEvenements collecteurEv;
//
//    // Gestion de panels
//    private JPanel cartesNord;
//    private JPanel cartesEst;
//    private JPanel cartesSud;
//
//    private BoutonTerrain[][] buttonsTerrain;
//    private BoutonCarte[] buttonsCartesJoueur1;
//    private BoutonCarte[] buttonsCartesJoueur2;
//    private BoutonCarte carteDeRotation;
//    private BoutonTerrain annuler, refaire;
//    private JButton boutonSon;
//
//    private JLabel nomJoueurCourantLabel;
//    private JLabel tempsLabel;
//    private JLabel roundLabel;
//    private int numRound;
//
//    // Gestion du son
//    private Clip clip;
//    private boolean musiqueActive = false;
//
//    // Gestion du temps
//    private Instant debutTempsPartie;
//    private Timer timerPartie;
//
//
//    // Constantes
//    private static final int ESPACE = 20;
//
//    private JPanel terrain;
//
//    // Chargement des images dans une liste
//    HashMap<TYPECARTE, BufferedImage> imagesCartes = new HashMap<>();
//
//    // Contient les images liées à chaque type d'élément affichable sur le terrain.
//    public static enum TYPE_ELEMENT_SUR_TERRAIN {
//        VIDE,
//        PION_ETUDIANT_J1,
//        PION_ETUDIANT_J2,
//        PION_MAITRE_J1,
//        PION_MAITRE_J2
//    }
//    private final HashMap<TYPE_ELEMENT_SUR_TERRAIN, BufferedImage> imagesCaseTerrain = new HashMap<>();
//
//    // Pour la Gestion des Animations : Renversement des cartes
//    private final ArrayList<CardFlipAnimator>  listeDescardFlipAnimators = new ArrayList<>();
//    private int ID_JOUEUR_PRECEDANT = ID_JOUEUR_2;
//
//
//    /**
//     * Constructeur principal du EcranPlateauDeJeu
//     * @param jeu modèle de données observé
//     * @param collecteurEv gestionnaire des événements
//     * @param interfaceGraphique Scène principale
//     */
//    public EcranPlateauDeJeu(Jeu jeu, CollecteurEvenements collecteurEv, InterfaceGraphique interfaceGraphique) {
//        this.jeu = jeu;
//        this.collecteurEv = collecteurEv;
//        this.interfaceGraphique = interfaceGraphique;
//
//        jeu.ajouteObservateur(this);
//        logger.info("Interface Plateau de jeu lancée");
//
//        setLayout(new BorderLayout());
//        setBackground(COULEUR_PLATEAU_DE_JEU);
//
//        creerButtonsCartes();
//        initialiserInterface();
//
//        miseAJour();
//    }
//
//
//    @Override
//    public void miseAJour() {
//        logger.info("Mise à jour : EcranPlateauDeJeu...");
//        mettreAJourImagesTerrain();
//        mettreAJourImagesCartes();
//        updatePlayerAndRoundInfo();
//        updateUndoRedoButtons();
//        tournerLesCartesDuJoueur();
//        logger.info("Mise à jour : EcranPlateauDeJeu terminée.");
//    }
//
//
//    /**
//     * Initialise l'interface utilisateur avec GridBagLayout. */
//    private void initialiserInterface() {
//        // Creation des composants
//        cartesNord = new JPanel();
//        cartesEst = new JPanel();
//        cartesSud = new JPanel();
//        creerTerrain();
//        creerCartesNord();
//        creerCarteGauche();
//        creerCartesSud();
//
//        //  Conteneur principal avec GridBagLayout
//        JPanel contenu = new JPanel(new GridBagLayout());
//        contenu.setBorder(BorderFactory.createEmptyBorder(ESPACE, ESPACE, ESPACE, ESPACE));
//        contenu.setOpaque(false);
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(0, 0, 0, 0);
//        gbc.anchor = GridBagConstraints.CENTER;
//
//        // Ligne 0 : Haut (son | timer | menu)
//        JPanel barreIndication = new JPanel();
//        barreIndication.setLayout(new FlowLayout(FlowLayout.TRAILING, 10, 10));
//        barreIndication.setOpaque(false);
//        barreIndication.add( creerBoutonSon());
//        barreIndication.add(creerBarredesBoutons());
//        barreIndication.add(Box.createHorizontalStrut(ESPACE));
//        barreIndication.add(Box.createGlue());
//        barreIndication.add(creerPanelRoundTemps());
//        barreIndication.add(Box.createHorizontalStrut(ESPACE));
//        barreIndication.add(creerBoutonMenu());
//
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 1;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.weightx = 1.0;
//        gbc.insets = new Insets(0, 0, ESPACE, 0);
//        contenu.add(barreIndication, gbc);
//
//        // === Ligne 1 : Texte du tour ===
//        gbc.gridy = 1;
//        gbc.fill = GridBagConstraints.NONE;
//        gbc.weightx = 0;
//        gbc.weighty = 0;
//        gbc.insets = new Insets(15, 0, 0, 0);
//        contenu.add(creerPanelNomJoueurCourant(), gbc);
//
//        // === Saut de ligne entre ligne 1 et 2 ===
//        gbc.gridy = 2;
//        gbc.weightx = 0.25;
//        gbc.weighty = 0.2;
//        contenu.add(Box.createGlue(), gbc);
//
//        // === Ligne 3 : Plateau avec layout empilé ===
//        gbc.gridy = 3;
//        gbc.fill = GridBagConstraints.BOTH;
//        gbc.weightx = 1.0;
//        gbc.weighty = 1.0;
//        gbc.insets = new Insets(0, 0, ESPACE*3, 0);
//
//        JPanel panelCentreEmpile = new JPanel(new GridBagLayout());
//        panelCentreEmpile.setOpaque(false);
//        GridBagConstraints centreGbc = new GridBagConstraints();
//        centreGbc.fill = GridBagConstraints.BOTH;
//
//        // Cartes nord
//        centreGbc.gridx = 1;
//        centreGbc.gridy = 0;
//        centreGbc.weightx = 2.5;
//        centreGbc.weighty = 0.37;
//        panelCentreEmpile.add(cartesNord, centreGbc);
//
//        // Carte gauche
//        centreGbc.gridx = 0;
//        centreGbc.gridy = 1;
//        centreGbc.weightx = 1.0;
//        centreGbc.weighty = 0.37;
//        panelCentreEmpile.add(cartesEst, centreGbc);
//
//        // terrain
//        centreGbc.gridx = 1;
//        centreGbc.gridy = 1;
//        centreGbc.weightx = 0.5;
//        centreGbc.weighty = 0.5;
//        centreGbc.insets = new Insets(20, 20, 20, 20);
//        panelCentreEmpile.add(new PanelRatioFixe(terrain, 1), centreGbc);
//        centreGbc.insets = new Insets(0, 0, 0, 0);
//
//        // Boutons à droite
//        centreGbc.gridx = 2;
//        centreGbc.gridy = 1;
//        centreGbc.weightx = 1.0;
//        centreGbc.weighty = 1.0;
//        panelCentreEmpile.add(creerBoutonsDroite(), centreGbc);
//
//        // Cartes sud
//        centreGbc.gridx = 1;
//        centreGbc.gridy = 2;
//        centreGbc.weightx = 2.5;
//        centreGbc.weighty = 0.37;
//        panelCentreEmpile.add(cartesSud, centreGbc);
//
//        contenu.add(panelCentreEmpile, gbc);
//
//        // Ajout du conteneur principal au panneau
//        setLayout(new BorderLayout());
////        add(contenu, BorderLayout.CENTER);
//        add(new PanelRatioFixe(contenu, 1), BorderLayout.CENTER);
//
//        // Démarrage du timer
//        debutTempsPartie = Instant.now();
//        timerPartie = new Timer(1000, e -> miseAjourTemps());
//        timerPartie.start();
//    }
//
//
//
//
//    // =========================================
//    // ============ Création UI ================
//    // =========================================
//
//
//    private void creerTerrain() {
//        terrain = creerPanelArrondiInteractif(
//                Color.WHITE, new Color(230, 230, 250), new Color(200, 200, 255),
//                ARRONDI, Color.GRAY, 2
//        );
//
//        terrain.setLayout(new GridLayout(LIGNES, COLONNES, 0, 0));
//        buttonsTerrain = new BoutonTerrain[LIGNES][COLONNES];
//
//        chargerImagesTerrain();
//        initBoutonsTerrain();
//    }
//
//
//    /**
//     * Initialise les boutons représentant les cartes en main des deux joueurs
//     * ainsi que la carte de rotation, en associant chaque bouton à son image
//     * et à un écouteur d’événement adapté.
//     */
//    private void creerButtonsCartes() {
//        try {
//            // Initialisation des tableaux de boutons pour les cartes des deux joueurs
//            buttonsCartesJoueur1 = new BoutonCarte[NOMBRE_CARTES_MAIN];
//            buttonsCartesJoueur2 = new BoutonCarte[NOMBRE_CARTES_MAIN];
//
//            // --- Carte de rotation (carte supplémentaire) ---
//            Carte carteSupplementaire = jeu.getCarteSupplementaire();
//            BufferedImage imageSupplementaire = ImageIO.read(getCheminImageCarte(carteSupplementaire).toFile());
//
//            carteDeRotation = new BoutonCarte(imageSupplementaire);
//            imagesCartes.put(carteSupplementaire.getType(), imageSupplementaire);
//
//            // Association d’un écouteur pour la carte de rotation
//            carteDeRotation.addActionListener(new AdaptateurCarte(0, 0, carteDeRotation,this, collecteurEv ));
//
//            // --- Cartes des joueurs ---
//            List<Carte> cartesJoueur1 = jeu.getCartesJoueur1();
//            List<Carte> cartesJoueur2 = jeu.getCartesJoueur2();
//
//            for (int i = 0; i < NOMBRE_CARTES_MAIN; i++) {
//                // Chargement des images des cartes
//                Carte carteJ1 = cartesJoueur1.get(i);
//                Carte carteJ2 = cartesJoueur2.get(i);
//
//                BufferedImage imageJ1 = ImageIO.read(getCheminImageCarte(carteJ1).toFile());
//                BufferedImage imageJ2 = ImageIO.read(getCheminImageCarte(carteJ2).toFile());
//
//                // Création des boutons avec leur image respective
//                BoutonCarte boutonJ1 = new BoutonCarte(imageJ1);
//                BoutonCarte boutonJ2 = new BoutonCarte(imageJ2);
//
//                // Stockage des images associées à chaque type de carte
//                imagesCartes.put(carteJ1.getType(), imageJ1);
//                imagesCartes.put(carteJ2.getType(), imageJ2);
//
//                // Association des écouteurs d’événements
//                boutonJ1.addActionListener(new AdaptateurCarte(i, ID_JOUEUR_1, boutonJ1, this, collecteurEv));
//                boutonJ2.addActionListener(new AdaptateurCarte(i, ID_JOUEUR_2, boutonJ2, this, collecteurEv));
//
//                // Stockage des boutons dans les tableaux respectifs
//                buttonsCartesJoueur1[i] = boutonJ1;
//                buttonsCartesJoueur2[i] = boutonJ2;
//            }
//
//        } catch (IOException e) {
//            logger.severe("Erreur lors du chargement des images des cartes : " + e.getMessage());
//        }
//    }
//
//
//    /** Crée les boutons "Annuler" et "Refaire"*/
//    private JPanel creerButtonsAnnulerRefaire() {
//        JPanel boutonsAnnuleRefaire = new JPanel(new GridLayout(6, 1, 0, 10));
//        boutonsAnnuleRefaire.setOpaque(false);
//
//        annuler = new BoutonTerrain(PATH_BTN_ANNULER);
//        refaire = new BoutonTerrain(PATH_BTN_REFAIRE);
//
//        annuler.setPreferredSize(new Dimension(135,60));
//        refaire.setPreferredSize(new Dimension(135,60));
//
//        annuler.setBackground(new Color(207, 207, 207, 44));
//        refaire.setBackground(new Color(207, 207, 207, 44));
//
//        annuler.setOpaque(true);
//        refaire.setOpaque(true);
//
//        annuler.addActionListener(new AdaptateurAnnuler(collecteurEv));
//        refaire.addActionListener(new AdaptateurRefaire(collecteurEv));
//
//        boutonsAnnuleRefaire.add(Box.createGlue());
//        boutonsAnnuleRefaire.add(Box.createGlue());
//        boutonsAnnuleRefaire.add(annuler);
//        boutonsAnnuleRefaire.add(refaire);
//        boutonsAnnuleRefaire.add(Box.createGlue());
//        boutonsAnnuleRefaire.add(Box.createGlue());
//
//        return  boutonsAnnuleRefaire;
//    }
//
//    private void creerCartesNord() {
//        cartesNord.setLayout(new GridLayout(1, 4, 25, 0));
//        cartesNord.setOpaque(false);
//
//        cartesNord.add(Box.createGlue());
//        cartesNord.add(cardFlipAnimator(buttonsCartesJoueur1[0]));
//        cartesNord.add(cardFlipAnimator(buttonsCartesJoueur1[1]));
//        cartesNord.add(Box.createGlue());
//    }
//
//    private void creerCartesSud() {
//        cartesSud.setLayout(new GridLayout(1, 4, 25, 0));
//        cartesSud.setOpaque(false);
//
//        cartesSud.add(Box.createGlue());
//        cartesSud.add(buttonsCartesJoueur2[0]);
//        cartesSud.add(buttonsCartesJoueur2[1]);
//        cartesSud.add(Box.createGlue());
//    }
//
//    private void creerCarteGauche() {
//        JPanel cartesEstbis = new JPanel(new GridLayout(3, 1, 0, 100));
//        cartesEstbis.setOpaque(false);
//        cartesEstbis.add(Box.createVerticalGlue());
//        cartesEstbis.add(cardFlipAnimator(carteDeRotation));
//        cartesEstbis.add(Box.createVerticalGlue());
//
//        cartesEst.setLayout(new BoxLayout(cartesEst, BoxLayout.X_AXIS));
//        cartesEst.add(Box.createGlue());
//        cartesEst.add(cartesEstbis);
//        cartesEst.setOpaque(false);
//
//    }
//
//    private JPanel creerBoutonsDroite() {
//        JPanel droite = new JPanel();
//        droite.setLayout(new BoxLayout(droite, BoxLayout.X_AXIS));
//        droite.add(Box.createGlue());
//        droite.add(creerButtonsAnnulerRefaire());
//        droite.add(Box.createGlue());
//        droite.setOpaque(false);
//
//        return droite;
//    }
//
//    private JButton creerBoutonSon() {
//        boutonSon = Bouton.creerBouton(PATH_BTN_MUET.toString(), Bouton.ConfigurationParDefaut.Carre_transparent);
////        boutonSon.setForeground(Color.WHITE);
////        boutonSon.setFont(new Font("Arial", Font.PLAIN, 30));
////        boutonSon.setBackground(new Color(237, 237, 237, 16));
////        boutonSon.setContentAreaFilled(false);
////        boutonSon.setFocusPainted(false);
//        boutonSon.setPreferredSize(new Dimension(50,50));
//        boutonSon.setText(musiqueActive ? "on" : "off");
//        boutonSon.addActionListener(e -> toggleMusique(boutonSon));
//
//        return boutonSon;
//    }
//
//    private JPanel creerPanelNomJoueurCourant()  {
//        JPanel textNomPanel = new JPanel(); // Renommé
//        textNomPanel.setLayout(new BoxLayout(textNomPanel, BoxLayout.Y_AXIS));
//        textNomPanel.setOpaque(false);
//
//        JLabel txt = new JLabel("C'est au tour de");
//        txt.setFont(new Font("Arial", Font.PLAIN, 20));
//        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
//        txt.setForeground(new Color(232, 231, 231));
//
//        nomJoueurCourantLabel = new JLabel(jeu.getNomJoueurCourant());
//        nomJoueurCourantLabel.setFont(new Font("Arial", Font.BOLD, 30));
//        nomJoueurCourantLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        // Changer la couleur du texte du joueur courant pour qu'elle corresponde à sa couleur de pion
//        nomJoueurCourantLabel.setForeground(infosDeConfigUI.getCouleurPionJoueur(jeu.getJoueurCourant().getId()));
//
//        textNomPanel.add(txt);
//        textNomPanel.add(Box.createRigidArea(new Dimension(0, 2)));
//        textNomPanel.add(nomJoueurCourantLabel);
//
//        return textNomPanel;
//    }
//
//    private JPanel creerPanelRoundTemps() {
//        JPanel panel = creerPanelArrondiInteractif(
//                Color.WHITE, new Color(230, 230, 250), new Color(200, 200, 255),
//                ARRONDI, Color.GRAY, 2
//        );
//        panel.setOpaque(false);
////        JPanel panel = new JPanel();
////        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
////        panel.setBorder(BorderFactory.createCompoundBorder(
////                BorderFactory.createLineBorder(new Color(206, 206, 206), 1, true),
////                BorderFactory.createEmptyBorder(10, 20, 10, 20)
////        ));
////        panel.setBackground(new Color(245, 245, 245, 23));
//
//        numRound = jeu.getNumeroRound();
//        roundLabel = new JLabel("Round: "+numRound);
//        roundLabel.setFont(new Font("Arial", Font.PLAIN, 25));
//        roundLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 50));
//
//        tempsLabel = new JLabel("00:00");
//        tempsLabel.setOpaque(false);
//        tempsLabel.setFont(new Font("Arial", Font.BOLD, 25));
//
//        panel.add(roundLabel);
//        panel.add(tempsLabel);
//
//        return panel;
//    }
//
//    private JButton creerBoutonMenu() {
//        BoutonAvecImage menu = Bouton.creerBouton(PATH_BTN_MENU.toString(), Bouton.ConfigurationParDefaut.Carre_transparent);
////        menu.setOpaque(false);
////        menu.setContentAreaFilled(false);
////        menu.setFocusPainted(false);
////        menu.setForeground(Color.WHITE);
////        menu.setFont(new Font("Arial", Font.PLAIN, 30));
//        menu.setPreferredSize(new Dimension(50,50));
//        menu.addActionListener(e -> interfaceGraphique.ouvrirMenu());
//
//        return menu;
//    }
//
//    private JPanel creerBarredesBoutons() {
//        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
//        panel.setOpaque(false);
//
//        for (int i = 1; i <= 4; i++) {
//            BoutonAvecImage bouton = Bouton.creerBouton("", Bouton.ConfigurationParDefaut.Carre_transparent);
//            bouton.setPreferredSize(new Dimension(50,50));
//            int index = i; // pour l'utiliser dans le lambda
//            bouton.addActionListener(e -> {
//                System.out.println("Bouton " + index + " cliqué !");
//                // Ajoute ici le comportement désiré pour ce bouton
//            });
//            panel.add(bouton);
//        }
//
//        return panel;
//    }
//
//    private JLayer<JButton> cardFlipAnimator(BoutonCarte boutonCarte) {
//        CardFlipAnimator animator = new CardFlipAnimator();                 // 1. Créer un nouvel animateur pour ce bouton
//        CardFlipLayerUI<JButton> layerUI = new CardFlipLayerUI<>(animator);  // 2. Créer un LayerUI qui utilisera cet animateur
//        JLayer<JButton> layer = new JLayer<>(boutonCarte, layerUI);           // 3. Créer un JLayer, enveloppant le bouton original avec le LayerUI
//
//        // Ajouter un écouteur d'animation à l'animateur
//        // Chaque fois que l'animateur met à jour son angle, il notifie ce listener
//        // qui demande alors au JLayer de se repeindre.
//        animator.addAnimationListener(layer::repaint); // Lambda capture 'layer'
//
//        listeDescardFlipAnimators.add(animator);
//
//        return layer;
//    }
//
//    private void tournerLesCartesDuJoueur(){
//        if (jeu.getIdJoueurCourant() == ID_JOUEUR_1 && ID_JOUEUR_1 != ID_JOUEUR_PRECEDANT ||
//                jeu.getIdJoueurCourant() == ID_JOUEUR_2 && ID_JOUEUR_1 == ID_JOUEUR_PRECEDANT ) {
//
//            for (CardFlipAnimator animator: listeDescardFlipAnimators) {
//                animator.startAnimation();
//            }
//            ID_JOUEUR_PRECEDANT = jeu.getIdJoueurCourant();
//        }
//    }
//
//
//
//
//    // =========================================
//    // ========= Gestion Son & Musique =========
//    // =========================================
//
//    private void toggleMusique(JButton bouton) {
//        if (musiqueActive) {
//            if (clip != null && clip.isRunning()) {
//                clip.stop();
//            }
////            bouton.setText("off");
//            bouton.setIcon(new ImageIcon(PATH_BTN_MUET.toString()));
//        } else {
//            // Jouer la musique si elle n'est pas déjà en cours
//            if (clip == null || !clip.isRunning()) {
//                jouerMusique(PATH_SON_1.toString());
//            }
////            bouton.setText("on");
//            bouton.setIcon(new ImageIcon(PATH_BTN_MONTER_LE_SON.toString()));
//        }
//        musiqueActive = !musiqueActive;
//    }
//
//    private void jouerMusique(String chemin) {
//        try {
//            // S'assurer que l'ancien clip est fermé avant d'en ouvrir un nouveau
//            if (clip != null) {
//                clip.close();
//            }
//            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
//                    Objects.requireNonNull(getClass().getResource(chemin), "Ressource audio introuvable: " + chemin) // Message d'erreur plus clair
//            );
//            clip = AudioSystem.getClip();
//            clip.open(audioInputStream);
//            clip.loop(Clip.LOOP_CONTINUOUSLY);
//            clip.start();
//        } catch (Exception e) {
//            logger.severe("Erreur lors de la lecture audio: " + e.getMessage()); // Utiliser System.err pour les erreurs ou un logger
//            // Optionnel : désactiver le bouton son si une erreur se produit
//            if (boutonSon != null) {
//                boutonSon.setEnabled(false);
//                boutonSon.setText("Sound Error");
//            }
//            musiqueActive = false; // Mettre à jour l'état
//        }
//    }
//
//
//    // =========================================
//    // ============== Mise à jour ==============
//    // =========================================
//
//    // Mise à jour du temps (appelée par le Timer)
//    private void miseAjourTemps() {
//        if (debutTempsPartie != null && tempsLabel != null) { // Vérifier si les attributs sont initialisés
//            Duration duration = Duration.between(debutTempsPartie, Instant.now());
//            long minutes = duration.toMinutes();
//            long secondes = duration.getSeconds() % 60;
//            tempsLabel.setText(String.format("%02d:%02d", minutes, secondes));
//        }
//    }
//
//
//    /**
//     * Met à jour les images des boutons de cartes en utilisant les images
//     * déjà chargées et associées aux types de cartes.
//     *
//     * Cette méthode suppose que toutes les cartes utilisées ont déjà leur
//     * image stockée dans la map imagesCartes.
//     */
//    private void mettreAJourImagesCartes() {
//        // Mise à jour de la carte de rotation
//        Carte carteSupp = jeu.getCarteSupplementaire();
//        BufferedImage imageSupp = imagesCartes.get(carteSupp.getType());
//        carteDeRotation.setImageFond(imageSupp);
//
//        // Mise à jour des cartes des deux joueurs
//        List<Carte> cartesJ1 = jeu.getCartesJoueur1();
//        List<Carte> cartesJ2 = jeu.getCartesJoueur2();
//
//        for (int i = 0; i < NOMBRE_CARTES_MAIN; i++) {
//            Carte carteJ1 = cartesJ1.get(i);
//            Carte carteJ2 = cartesJ2.get(i);
//
//            BufferedImage imageJ1 = imagesCartes.get(carteJ1.getType());
//            BufferedImage imageJ2 = imagesCartes.get(carteJ2.getType());
//
//            buttonsCartesJoueur1[i].setImageFond(imageJ1);
//            buttonsCartesJoueur2[i].setImageFond(imageJ2);
//        }
//    }
//
//
//
//    // Met à jour l'affichage du joueur courant et du numéro de round
//    private void updatePlayerAndRoundInfo() {
//        if (jeu != null && nomJoueurCourantLabel != null && roundLabel != null) {
//            String nomJoueur = jeu.getJoueurCourant().getNom();
//            nomJoueurCourantLabel.setText(nomJoueur);
//
//            numRound = jeu.getNumeroRound();
//            roundLabel.setText("Round: " + numRound);
//
//            // Changer la couleur du texte du joueur courant pour qu'elle corresponde à sa couleur de pion
//             nomJoueurCourantLabel.setForeground(infosDeConfigUI.getCouleurPionJoueur(jeu.getJoueurCourant().getId()));
//        }
//    }
//
//    // Met à jour l'état des boutons Annuler/Refaire
//    private void updateUndoRedoButtons() {
//            annuler.setEnabled(jeu.peutAnnulerCoup());
//            refaire.setEnabled(jeu.peutRefaireCoup());
//    }
//
//
//    /**
//     * Charge les images associées à chaque type d'élément de terrain
//     * (VIDE, PION_ETUDIANT_J1, PION_ETUDIANT_J2, PION_MAITRE_J1, PION_MAITRE_J2)
//     * et les stocke dans imagesCaseTerrain.
//     */
//    private void chargerImagesTerrain() {
//        for (TYPE_ELEMENT_SUR_TERRAIN type : TYPE_ELEMENT_SUR_TERRAIN.values()) {
//            try {
//                Path chemin = getCheminImagePion(infosDeConfigUI, type);
//                if (chemin != null && Files.exists(chemin)) {
//                    BufferedImage image = ImageIO.read(chemin.toFile());
//                    imagesCaseTerrain.put(type, image);
//                } else {
//                    logger.warning("Aucune image trouvée pour le type : " + type + " (chemin : " + chemin + ")");
//                }
//            } catch (IOException e) {
//                logger.severe("Erreur lors du chargement de l'image pour le type " + type + " : " + e.getMessage());
//            }
//        }
//    }
//
//
//    /**
//     * Initialise les boutons du terrain avec les images préchargées
//     * selon le contenu initial de chaque case du plateau.
//     */
//    private void initBoutonsTerrain() {
//        for (int row = 0; row < LIGNES; row++) {
//            for (int col = 0; col < COLONNES; col++) {
//                CasePlateau casePlateau = jeu.getCasePlateau(row, col);
//                TYPE_ELEMENT_SUR_TERRAIN typeElement = determinerTypeElement(casePlateau);
//
//                BufferedImage image = imagesCaseTerrain.get(typeElement);
//                BoutonTerrain boutonCase = new BoutonTerrain(image);
//
//                boutonCase.addActionListener(new AdaptateurBoutonTerrain(boutonCase, casePlateau, collecteurEv, this));
//                boutonCase.setPreferredSize(new Dimension(10, 10));
//
//                buttonsTerrain[row][col] = boutonCase;
//                terrain.add(boutonCase);
//            }
//        }
//    }
//
//
//    /**
//     * Détermine le type d'élément présent sur une case du plateau.
//     */
//    private TYPE_ELEMENT_SUR_TERRAIN determinerTypeElement(CasePlateau casePlateau) {
//        switch (casePlateau.getTypeElement()) {
//            case VIDE:
//                break;
//            case PION_ETUDIANT:
//                if (casePlateau.getProprietaire() == ID_JOUEUR_1) return TYPE_ELEMENT_SUR_TERRAIN.PION_ETUDIANT_J1;
//                else return TYPE_ELEMENT_SUR_TERRAIN.PION_ETUDIANT_J2;
//
//            case PION_MAITRE:
//                if (casePlateau.getProprietaire() == ID_JOUEUR_1) return TYPE_ELEMENT_SUR_TERRAIN.PION_MAITRE_J1;
//                else return TYPE_ELEMENT_SUR_TERRAIN.PION_MAITRE_J2;
//        }
//        return TYPE_ELEMENT_SUR_TERRAIN.VIDE;
//    }
//
//
//    /**
//     * Met à jour dynamiquement toutes les cases du plateau
//     * en fonction de leur contenu actuel (sans recharger d’image).
//     */
//    private void mettreAJourImagesTerrain() {
//        for (int row = 0; row < LIGNES; row++) {
//            for (int col = 0; col < COLONNES; col++) {
//                CasePlateau casePlateau = jeu.getCasePlateau(row, col);
//                TYPE_ELEMENT_SUR_TERRAIN type = determinerTypeElement(casePlateau);
//                BufferedImage image = imagesCaseTerrain.get(type);
//
//                buttonsTerrain[row][col].changerImage(image);
//            }
//        }
//        appliquerBordureDynamiqueSurTerrain();
//        terrain.repaint();
//
//    }
//
//
//    private void appliquerBordureDynamiqueSurTerrain() {
//        Color couleurBordure = infosDeConfigUI.getCouleurPionJoueur(jeu.getJoueurCourant().getId());
//
//        Border bordureArrondie = new BordureArrondieAvecOmbre(couleurBordure, 5, ARRONDI);
//        Border margeInterne = BorderFactory.createEmptyBorder(5, 5, 5, 5);
//
//        terrain.setBorder(BorderFactory.createCompoundBorder(bordureArrondie, margeInterne));
//    }
//
//
//
//    // =========================================
//    // ============== Méthodes Utiles ==========
//    // =========================================
//
//    // Méthode pour arrêter le timer et le son (à appeler lors de la fermeture de la fenêtre)
//    public void cleanup() {
//        if (timerPartie != null && timerPartie.isRunning()) {
//            timerPartie.stop();
//        }
//        if (clip != null) {
//            clip.close();
//        }
//        logger.info("Nettoyage de EcranPlateauDeJeu effectué.");
//    }
//
//    public Jeu getJeu() {
//        return jeu;
//    }
//
//    public BoutonTerrain getBoutonterrainAt(Point bouton){
//        return buttonsTerrain[bouton.x][bouton.y];
//    }
//}
package Vue;

import Modele.Carte;
import Modele.CasePlateau;
import Modele.Jeu;
import Patterns.Observateur;
import Vue.Adaptateurs.AdaptateurAnnuler;
import Vue.Adaptateurs.AdaptateurBoutonTerrain;
import Vue.Adaptateurs.AdaptateurCarte;
import Vue.Adaptateurs.AdaptateurRefaire;
import Vue.Animations.AnimationUtils.CardFlipAnimator;
import Vue.Animations.AnimationUtils.CardFlipLayerUI;
import Vue.LabO.BordureArrondieAvecOmbre;
import Vue.Utils.Boutons.Bouton;
import Vue.Utils.Boutons.Bouton.BoutonAvecImage;
import Vue.Utils.Boutons.BoutonCarte;
import Vue.Utils.Boutons.BoutonTerrain;
import Vue.Utils.PanelAvecImage;
import Vue.Utils.PanelBruitGris;
import Vue.Utils.PanelRatioFixe;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static Global.Config.*;
import static Global.Paths.*;
import static Vue.ConfigUI.ARRONDI;
import static Vue.Utils.MethodsStaticsUtils.*;


/**
 * Classe représentant l'interface graphique principale du plateau de jeu.
 * Elle observe le modèle (Jeu) et met à jour l'affichage en fonction des événements.
 */
public class EcranPlateauDeJeu extends PanelAvecImage implements Observateur {
    private static final Logger logger = Logger.getLogger(EcranPlateauDeJeu.class.getName());
    private final InfosDeConfigUI infosDeConfigUI = InfosDeConfigUI.getInstance();

    private final Jeu jeu;
    private final InterfaceGraphique interfaceGraphique;
    private final CollecteurEvenements collecteurEv;

    // Gestion de panels
    private JPanel cartesNord;
    private JPanel cartesEst;
    private JPanel cartesSud;

    private BoutonTerrain[][] buttonsTerrain;
    private BoutonCarte[] buttonsCartesJoueur1;
    private BoutonCarte[] buttonsCartesJoueur2;
    private BoutonCarte carteDeRotation;
    private BoutonTerrain annuler, refaire;
    private JButton boutonSon;

    private JLabel nomJoueurCourantLabel;
    private JLabel tempsLabel;
    private JLabel roundLabel;
    private int numRound;

    // Gestion du son
    private Clip clip;
    private boolean musiqueActive = false;

    // Gestion du temps
    private Instant debutTempsPartie;
    private Timer timerPartie;

    // Constantes
    private static final int ESPACE = 20;

    private JPanel terrain;

    // Chargement des images dans une liste
    HashMap<TYPECARTE, BufferedImage> imagesCartes = new HashMap<>();

    // Contient les images liées à chaque type d'élément affichable sur le terrain.
    public static enum TYPE_ELEMENT_SUR_TERRAIN {
        VIDE,
        PION_ETUDIANT_J1,
        PION_ETUDIANT_J2,
        PION_MAITRE_J1,
        PION_MAITRE_J2
    }
    private final HashMap<TYPE_ELEMENT_SUR_TERRAIN, BufferedImage> imagesCaseTerrain = new HashMap<>();

    // Pour la Gestion des Animations : Renversement des cartes
    private final ArrayList<CardFlipAnimator>  listeDescardFlipAnimators = new ArrayList<>();
    private int ID_JOUEUR_PRECEDANT = ID_JOUEUR_2;

    /**
     * Constructeur principal du EcranPlateauDeJeu
     * @param jeu modèle de données observé
     * @param collecteurEv gestionnaire des événements
     * @param interfaceGraphique Scène principale
     */
    public EcranPlateauDeJeu(Jeu jeu, CollecteurEvenements collecteurEv, InterfaceGraphique interfaceGraphique) {
        super(PATH_ARRIERE_PLAN_8);
        this.jeu = jeu;
        this.collecteurEv = collecteurEv;
        this.interfaceGraphique = interfaceGraphique;

        jeu.ajouteObservateur(this);
        logger.info("Interface Plateau de jeu lancée");
        setLayout(new BorderLayout());
        setBackground(COULEUR_PLATEAU_DE_JEU);

        creerButtonsCartes();
        initialiserInterface();

        miseAJour();
    }

    @Override
    public void miseAJour() {
        logger.info("Mise à jour : EcranPlateauDeJeu...");
        mettreAJourImagesTerrain();
        mettreAJourImagesCartes();
        updatePlayerAndRoundInfo();
        updateUndoRedoButtons();
        tournerLesCartesDuJoueur();
        logger.info("Mise à jour : EcranPlateauDeJeu terminée.");
    }

    /**
     * Initialise l'interface utilisateur avec GridBagLayout.
     */
    private void initialiserInterface() {
        // Creation des composants
        cartesNord = new JPanel();
        cartesEst = new JPanel();
        cartesSud = new JPanel();
        creerTerrain();
        creerCartesNord();
        creerCarteGauche();
        creerCartesSud();

        //  Conteneur principal avec GridBagLayout
        JPanel contenu = new JPanel(new GridBagLayout());
        contenu.setBorder(BorderFactory.createEmptyBorder(ESPACE, ESPACE, ESPACE, ESPACE));
        contenu.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Ligne 0 : Haut (son | timer | menu)
        JPanel barreIndication = new JPanel();
        barreIndication.setLayout(new FlowLayout(FlowLayout.TRAILING, 10, 10));
        barreIndication.setOpaque(false);
        barreIndication.add(creerBoutonSon());
        barreIndication.add(creerBarredesBoutons());
        barreIndication.add(Box.createHorizontalStrut(ESPACE));
        barreIndication.add(Box.createGlue());
        barreIndication.add(creerPanelRoundTemps());
        barreIndication.add(Box.createHorizontalStrut(ESPACE));
        barreIndication.add(creerBoutonMenu());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, ESPACE, 0);
        contenu.add(barreIndication, gbc);

        // === Ligne 1 : Texte du tour ===
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(15, 0, 0, 0);
        contenu.add(creerPanelNomJoueurCourant(), gbc);

        // === Saut de ligne entre ligne 1 et 2 ===
        gbc.gridy = 2;
        gbc.weightx = 0.25;
        gbc.weighty = 0.2;
        contenu.add(Box.createGlue(), gbc);

        // === Ligne 3 : Plateau avec layout empilé ===
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, ESPACE*3, 0);

        JPanel panelCentreEmpile = new JPanel(new GridBagLayout());
        panelCentreEmpile.setOpaque(false);
        GridBagConstraints centreGbc = new GridBagConstraints();
        centreGbc.fill = GridBagConstraints.BOTH;

        // Cartes nord
        centreGbc.gridx = 1;
        centreGbc.gridy = 0;
        centreGbc.weightx = 2.5;
        centreGbc.weighty = 0.37;
        panelCentreEmpile.add(cartesNord, centreGbc);

        // Carte gauche
        centreGbc.gridx = 0;
        centreGbc.gridy = 1;
        centreGbc.weightx = 1.0;
        centreGbc.weighty = 0.37;
        panelCentreEmpile.add(cartesEst, centreGbc);

        // terrain
        centreGbc.gridx = 1;
        centreGbc.gridy = 1;
        centreGbc.weightx = 0.5;
        centreGbc.weighty = 0.5;
        centreGbc.insets = new Insets(20, 20, 20, 20);
        panelCentreEmpile.add(new PanelRatioFixe(terrain, 1), centreGbc);
        centreGbc.insets = new Insets(0, 0, 0, 0);

        // Boutons à droite
        centreGbc.gridx = 2;
        centreGbc.gridy = 1;
        centreGbc.weightx = 1.0;
        centreGbc.weighty = 1.0;
        panelCentreEmpile.add(creerBoutonsDroite(), centreGbc);

        // Cartes sud
        centreGbc.gridx = 1;
        centreGbc.gridy = 2;
        centreGbc.weightx = 2.5;
        centreGbc.weighty = 0.37;
        panelCentreEmpile.add(cartesSud, centreGbc);

        contenu.add(panelCentreEmpile, gbc);

        // Ajout du conteneur principal au panneau
        setLayout(new BorderLayout());
        add(new PanelRatioFixe(contenu, 1), BorderLayout.CENTER);

        // Démarrage du timer
        debutTempsPartie = Instant.now();
        timerPartie = new Timer(1000, e -> miseAjourTemps());
        timerPartie.start();
    }

    // =========================================
    // ============ Création UI ================
    // =========================================

    private void creerTerrain() {
        terrain = creerPanelArrondiInteractif(
                Color.WHITE, new Color(230, 230, 250), new Color(200, 200, 255),
                ARRONDI, Color.GRAY, 2
        );
        terrain.setLayout(new GridLayout(LIGNES, COLONNES, 0, 0));
        buttonsTerrain = new BoutonTerrain[LIGNES][COLONNES];

        chargerImagesTerrain();
        initBoutonsTerrain();
    }

    /**
     * Initialise les boutons représentant les cartes en main des deux joueurs
     * ainsi que la carte de rotation, en associant chaque bouton à son image
     * et à un écouteur d’événement adapté.
     */
    private void creerButtonsCartes() {
        try {
            // Initialisation des tableaux de boutons pour les cartes des deux joueurs
            buttonsCartesJoueur1 = new BoutonCarte[NOMBRE_CARTES_MAIN];
            buttonsCartesJoueur2 = new BoutonCarte[NOMBRE_CARTES_MAIN];

            // --- Carte de rotation (carte supplémentaire) ---
            Carte carteSupplementaire = jeu.getCarteSupplementaire();
            BufferedImage imageSupplementaire = ImageIO.read(getCheminImageCarte(carteSupplementaire).toFile());
            carteDeRotation = new BoutonCarte(imageSupplementaire);
            imagesCartes.put(carteSupplementaire.getType(), imageSupplementaire);

            // Association d’un écouteur pour la carte de rotation
            carteDeRotation.addActionListener(new AdaptateurCarte(0, 0, carteDeRotation,this, collecteurEv ));

            // --- Cartes des joueurs ---
            List<Carte> cartesJoueur1 = jeu.getCartesJoueur1();
            List<Carte> cartesJoueur2 = jeu.getCartesJoueur2();

            for (int i = 0; i < NOMBRE_CARTES_MAIN; i++) {
                // Chargement des images des cartes
                Carte carteJ1 = cartesJoueur1.get(i);
                Carte carteJ2 = cartesJoueur2.get(i);

                BufferedImage imageJ1 = ImageIO.read(getCheminImageCarte(carteJ1).toFile());
                BufferedImage imageJ2 = ImageIO.read(getCheminImageCarte(carteJ2).toFile());

                // Création des boutons avec leur image respective
                BoutonCarte boutonJ1 = new BoutonCarte(imageJ1);
                BoutonCarte boutonJ2 = new BoutonCarte(imageJ2);

                // Stockage des images associées à chaque type de carte
                imagesCartes.put(carteJ1.getType(), imageJ1);
                imagesCartes.put(carteJ2.getType(), imageJ2);

                // Association des écouteurs d’événements
                boutonJ1.addActionListener(new AdaptateurCarte(i, ID_JOUEUR_1, boutonJ1, this, collecteurEv));
                boutonJ2.addActionListener(new AdaptateurCarte(i, ID_JOUEUR_2, boutonJ2, this, collecteurEv));

                // Stockage des boutons dans les tableaux respectifs
                buttonsCartesJoueur1[i] = boutonJ1;
                buttonsCartesJoueur2[i] = boutonJ2;
            }

        } catch (IOException e) {
            logger.severe("Erreur lors du chargement des images des cartes : " + e.getMessage());
        }
    }

    /** Crée les boutons "Annuler" et "Refaire"*/
    private JPanel creerButtonsAnnulerRefaire() {
        JPanel boutonsAnnuleRefaire = new JPanel(new GridLayout(6, 1, 0, 10));
        boutonsAnnuleRefaire.setOpaque(false);

        annuler = new BoutonTerrain(PATH_BTN_ANNULER);
        refaire = new BoutonTerrain(PATH_BTN_REFAIRE);

        annuler.setPreferredSize(new Dimension(135,60));
        refaire.setPreferredSize(new Dimension(135,60));

        annuler.setBackground(new Color(207, 207, 207, 44));
        refaire.setBackground(new Color(207, 207, 207, 44));

        annuler.setOpaque(true);
        refaire.setOpaque(true);

        annuler.addActionListener(new AdaptateurAnnuler(collecteurEv));
        refaire.addActionListener(new AdaptateurRefaire(collecteurEv));

        boutonsAnnuleRefaire.add(Box.createGlue());
        boutonsAnnuleRefaire.add(Box.createGlue());
        boutonsAnnuleRefaire.add(annuler);
        boutonsAnnuleRefaire.add(refaire);
        boutonsAnnuleRefaire.add(Box.createGlue());
        boutonsAnnuleRefaire.add(Box.createGlue());

        return  boutonsAnnuleRefaire;
    }

    private void creerCartesNord() {
        cartesNord.setLayout(new GridLayout(1, 4, 25, 0));
        cartesNord.setOpaque(false);

        cartesNord.add(Box.createGlue());
        cartesNord.add(cardFlipAnimator(buttonsCartesJoueur1[0]));
        cartesNord.add(cardFlipAnimator(buttonsCartesJoueur1[1]));
        cartesNord.add(Box.createGlue());
    }

    private void creerCartesSud() {
        cartesSud.setLayout(new GridLayout(1, 4, 25, 0));
        cartesSud.setOpaque(false);

        cartesSud.add(Box.createGlue());
        cartesSud.add(buttonsCartesJoueur2[0]);
        cartesSud.add(buttonsCartesJoueur2[1]);
        cartesSud.add(Box.createGlue());
    }

    private void creerCarteGauche() {
        JPanel cartesEstbis = new JPanel(new GridLayout(3, 1, 0, 100));
        cartesEstbis.setOpaque(false);
        cartesEstbis.add(Box.createVerticalGlue());
        cartesEstbis.add(cardFlipAnimator(carteDeRotation));
        cartesEstbis.add(Box.createVerticalGlue());

        cartesEst.setLayout(new BoxLayout(cartesEst, BoxLayout.X_AXIS));
        cartesEst.add(Box.createGlue());
        cartesEst.add(cartesEstbis);
        cartesEst.setOpaque(false);
    }

    private JPanel creerBoutonsDroite() {
        JPanel droite = new JPanel();
        droite.setLayout(new BoxLayout(droite, BoxLayout.X_AXIS));
        droite.add(Box.createGlue());
        droite.add(creerButtonsAnnulerRefaire());
        droite.add(Box.createGlue());
        droite.setOpaque(false);

        return droite;
    }

    private JButton creerBoutonSon() {
        boutonSon = Bouton.creerBouton(PATH_BTN_MUET.toString(), Bouton.ConfigurationParDefaut.Carre_transparent);
        boutonSon.setPreferredSize(new Dimension(50,50));
        boutonSon.setText(musiqueActive ? "on" : "off");
        boutonSon.addActionListener(e -> toggleMusique(boutonSon));

        return boutonSon;
    }

    private JPanel creerPanelNomJoueurCourant()  {
        JPanel textNomPanel = new JPanel();
        textNomPanel.setLayout(new BoxLayout(textNomPanel, BoxLayout.Y_AXIS));
        textNomPanel.setOpaque(false);

        JLabel txt = new JLabel("C'est au tour de");
        txt.setFont(new Font("Arial", Font.PLAIN, 20));
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
        txt.setForeground(new Color(232, 231, 231));

        nomJoueurCourantLabel = new JLabel(jeu.getNomJoueurCourant());
        nomJoueurCourantLabel.setFont(new Font("Arial", Font.BOLD, 30));
        nomJoueurCourantLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nomJoueurCourantLabel.setForeground(infosDeConfigUI.getCouleurPionJoueur(jeu.getJoueurCourant().getId()));

        textNomPanel.add(txt);
        textNomPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        textNomPanel.add(nomJoueurCourantLabel);

        return textNomPanel;
    }

    private JPanel creerPanelRoundTemps() {
        JPanel panel = creerPanelArrondiInteractif(
                Color.WHITE, new Color(230, 230, 250), new Color(200, 200, 255),
                ARRONDI, Color.GRAY, 2
        );
        panel.setOpaque(false);

        numRound = jeu.getNumeroRound();
        roundLabel = new JLabel("Round: "+numRound);
        roundLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        roundLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 50));

        tempsLabel = new JLabel("00:00");
        tempsLabel.setOpaque(false);
        tempsLabel.setFont(new Font("Arial", Font.BOLD, 25));

        panel.add(roundLabel);
        panel.add(tempsLabel);

        return panel;
    }

    private JButton creerBoutonMenu() {
        BoutonAvecImage menu = Bouton.creerBouton(PATH_BTN_MENU.toString(), Bouton.ConfigurationParDefaut.Carre_transparent);
        menu.setPreferredSize(new Dimension(50,50));
        menu.addActionListener(e -> interfaceGraphique.ouvrirMenu());

        return menu;
    }

    private JPanel creerBarredesBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setOpaque(false);

        for (int i = 1; i <= 4; i++) {
            BoutonAvecImage bouton = Bouton.creerBouton("", Bouton.ConfigurationParDefaut.Carre_transparent);
            bouton.setPreferredSize(new Dimension(50,50));
            int index = i;
            bouton.addActionListener(e -> {
                System.out.println("Bouton " + index + " cliqué !");
            });
            panel.add(bouton);
        }

        return panel;
    }

    private JLayer<JButton> cardFlipAnimator(BoutonCarte boutonCarte) {
        CardFlipAnimator animator = new CardFlipAnimator();
        CardFlipLayerUI<JButton> layerUI = new CardFlipLayerUI<>(animator);
        JLayer<JButton> layer = new JLayer<>(boutonCarte, layerUI);

        animator.addAnimationListener(layer::repaint);

        listeDescardFlipAnimators.add(animator);

        return layer;
    }

    private void tournerLesCartesDuJoueur(){
        if (jeu.getIdJoueurCourant() == ID_JOUEUR_1 && ID_JOUEUR_1 != ID_JOUEUR_PRECEDANT ||
                jeu.getIdJoueurCourant() == ID_JOUEUR_2 && ID_JOUEUR_1 == ID_JOUEUR_PRECEDANT ) {

            for (CardFlipAnimator animator: listeDescardFlipAnimators) {
                animator.startAnimation();
            }
            ID_JOUEUR_PRECEDANT = jeu.getIdJoueurCourant();
        }
    }

    // =========================================
    // ========= Gestion Son & Musique =========
    // =========================================

    private void toggleMusique(JButton bouton) {
        if (musiqueActive) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
            bouton.setIcon(new ImageIcon(PATH_BTN_MUET.toString()));
        } else {
            if (clip == null || !clip.isRunning()) {
                jouerMusique(PATH_SON_1.toString());
            }
            bouton.setIcon(new ImageIcon(PATH_BTN_MONTER_LE_SON.toString()));
        }
        musiqueActive = !musiqueActive;
    }

    private void jouerMusique(String chemin) {
        try {
            if (clip != null) {
                clip.close();
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    Objects.requireNonNull(getClass().getResource(chemin), "Ressource audio introuvable: " + chemin)
            );
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            logger.severe("Erreur lors de la lecture audio: " + e.getMessage());
            if (boutonSon != null) {
                boutonSon.setEnabled(false);
                boutonSon.setText("Sound Error");
            }
            musiqueActive = false;
        }
    }

    // =========================================
    // ============== Mise à jour ==============
    // =========================================

    private void miseAjourTemps() {
        if (debutTempsPartie != null && tempsLabel != null) {
            Duration duration = Duration.between(debutTempsPartie, Instant.now());
            long minutes = duration.toMinutes();
            long secondes = duration.getSeconds() % 60;
            tempsLabel.setText(String.format("%02d:%02d", minutes, secondes));
        }
    }

    /**
     * Met à jour les images des boutons de cartes en utilisant les images
     * déjà chargées et associées aux types de cartes.
     *
     * Cette méthode suppose que toutes les cartes utilisées ont déjà leur
     * image stockée dans la map imagesCartes.
     */
    private void mettreAJourImagesCartes() {
        Carte carteSupp = jeu.getCarteSupplementaire();
        BufferedImage imageSupp = imagesCartes.get(carteSupp.getType());
        carteDeRotation.setImageFond(imageSupp);

        List<Carte> cartesJ1 = jeu.getCartesJoueur1();
        List<Carte> cartesJ2 = jeu.getCartesJoueur2();

        for (int i = 0; i < NOMBRE_CARTES_MAIN; i++) {
            Carte carteJ1 = cartesJ1.get(i);
            Carte carteJ2 = cartesJ2.get(i);

            BufferedImage imageJ1 = imagesCartes.get(carteJ1.getType());
            BufferedImage imageJ2 = imagesCartes.get(carteJ2.getType());

            buttonsCartesJoueur1[i].setImageFond(imageJ1);
            buttonsCartesJoueur2[i].setImageFond(imageJ2);
        }
    }

    private void updatePlayerAndRoundInfo() {
        if (jeu != null && nomJoueurCourantLabel != null && roundLabel != null) {
            String nomJoueur = jeu.getJoueurCourant().getNom();
            nomJoueurCourantLabel.setText(nomJoueur);

            numRound = jeu.getNumeroRound();
            roundLabel.setText("Round: " + numRound);

            nomJoueurCourantLabel.setForeground(infosDeConfigUI.getCouleurPionJoueur(jeu.getJoueurCourant().getId()));
        }
    }

    private void updateUndoRedoButtons() {
        annuler.setEnabled(jeu.peutAnnulerCoup());
        refaire.setEnabled(jeu.peutRefaireCoup());
    }

    /**
     * Charge les images associées à chaque type d'élément de terrain
     * (VIDE, PION_ETUDIANT_J1, PION_ETUDIANT_J2, PION_MAITRE_J1, PION_MAITRE_J2)
     * et les stocke dans imagesCaseTerrain.
     */
    private void chargerImagesTerrain() {
        for (TYPE_ELEMENT_SUR_TERRAIN type : TYPE_ELEMENT_SUR_TERRAIN.values()) {
            try {
                Path chemin = getCheminImagePion(infosDeConfigUI, type);
                if (chemin != null && Files.exists(chemin)) {
                    BufferedImage image = ImageIO.read(chemin.toFile());
                    imagesCaseTerrain.put(type, image);
                } else {
                    logger.warning("Aucune image trouvée pour le type : " + type + " (chemin : " + chemin + ")");
                }
            } catch (IOException e) {
                logger.severe("Erreur lors du chargement de l'image pour le type " + type + " : " + e.getMessage());
            }
        }
    }

    /**
     * Initialise les boutons du terrain avec les images préchargées
     * selon le contenu initial de chaque case du plateau.
     */
    private void initBoutonsTerrain() {
        for (int row = 0; row < LIGNES; row++) {
            for (int col = 0; col < COLONNES; col++) {
                CasePlateau casePlateau = jeu.getCasePlateau(row, col);
                TYPE_ELEMENT_SUR_TERRAIN typeElement = determinerTypeElement(casePlateau);

                BufferedImage image = imagesCaseTerrain.get(typeElement);
                BoutonTerrain boutonCase = new BoutonTerrain(image);

                boutonCase.addActionListener(new AdaptateurBoutonTerrain(boutonCase, casePlateau, collecteurEv, this));
                boutonCase.setPreferredSize(new Dimension(10, 10));

                buttonsTerrain[row][col] = boutonCase;
                terrain.add(boutonCase);
            }
        }
    }

    /**
     * Détermine le type d'élément présent sur une case du plateau.
     */
    private TYPE_ELEMENT_SUR_TERRAIN determinerTypeElement(CasePlateau casePlateau) {
        switch (casePlateau.getTypeElement()) {
            case VIDE:
                break;
            case PION_ETUDIANT:
                if (casePlateau.getProprietaire() == ID_JOUEUR_1) return TYPE_ELEMENT_SUR_TERRAIN.PION_ETUDIANT_J1;
                else return TYPE_ELEMENT_SUR_TERRAIN.PION_ETUDIANT_J2;

            case PION_MAITRE:
                if (casePlateau.getProprietaire() == ID_JOUEUR_1) return TYPE_ELEMENT_SUR_TERRAIN.PION_MAITRE_J1;
                else return TYPE_ELEMENT_SUR_TERRAIN.PION_MAITRE_J2;
        }
        return TYPE_ELEMENT_SUR_TERRAIN.VIDE;
    }

    /**
     * Met à jour dynamiquement toutes les cases du plateau
     * en fonction de leur contenu actuel (sans recharger d’image).
     */
    private void mettreAJourImagesTerrain() {
        for (int row = 0; row < LIGNES; row++) {
            for (int col = 0; col < COLONNES; col++) {
                CasePlateau casePlateau = jeu.getCasePlateau(row, col);
                TYPE_ELEMENT_SUR_TERRAIN type = determinerTypeElement(casePlateau);
                BufferedImage image = imagesCaseTerrain.get(type);
                buttonsTerrain[row][col].changerImage(image);
            }
        }
        appliquerBordureDynamiqueSurTerrain();
        terrain.repaint();
    }

    private void appliquerBordureDynamiqueSurTerrain() {
        Color couleurBordure = infosDeConfigUI.getCouleurPionJoueur(jeu.getJoueurCourant().getId());

        Border bordureArrondie = new BordureArrondieAvecOmbre(couleurBordure, 5, ARRONDI);
        Border margeInterne = BorderFactory.createEmptyBorder(5, 5, 5, 5);

        terrain.setBorder(BorderFactory.createCompoundBorder(bordureArrondie, margeInterne));
    }

    // =========================================
    // ============== Méthodes Utiles ==========
    // =========================================

    // Méthode pour arrêter le timer et le son (à appeler lors de la fermeture de la fenêtre)
    public void cleanup() {
        if (timerPartie != null && timerPartie.isRunning()) {
            timerPartie.stop();
        }
        if (clip != null) {
            clip.close();
        }
        logger.info("Nettoyage de EcranPlateauDeJeu effectué.");
    }

    public Jeu getJeu() {
        return jeu;
    }

    public BoutonTerrain getBoutonterrainAt(Point bouton){
        return buttonsTerrain[bouton.x][bouton.y];
    }
}
