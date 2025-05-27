package Vue;

import Controleur.Mediateur;
import Modele.Carte;
import Modele.CasePlateau;
import Modele.Jeu;
import Patterns.Observateur;
import Vue.Adaptateurs.*;
import Vue.Animations.AnimationUtils.CardFlipAnimator;
import Vue.Animations.AnimationUtils.CardFlipLayerUI;
import Vue.Configuration.InfosDeConfigUI;
import Vue.LabO.BordureArrondieAvecOmbre;
import Vue.Utils.Boutons.Bouton;
import Vue.Utils.Boutons.Bouton.BoutonAvecImage;
import Vue.Utils.Boutons.BoutonCarte;
import Vue.Utils.Boutons.BoutonTerrain;
import Vue.Utils.PanelAvecImage;
import Vue.Utils.PanelRatioFixe;
import Vue.Utils.PngText;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ItemEvent;
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
import static Vue.Configuration.ConfigUI.ARRONDI;
import static Vue.Configuration.ConfigUI.LBL_TITRE_CONFIG;
import static Vue.Utils.MethodsStaticsUtils.*;


/**
 * Classe représentant l'interface graphique principale du plateau de jeu.
 * Elle observe le modèle (Jeu) et met à jour l'affichage en fonction des événements.
 */
public class EcranPlateauDeJeu extends PanelAvecImage implements Observateur {
    private static final Logger logger = Logger.getLogger(EcranPlateauDeJeu.class.getName());

    // Constantes
    private static final int ESPACE = 20;
    private final InfosDeConfigUI infosDeConfigUI = InfosDeConfigUI.getInstance();
    private final Jeu jeu;
    private final InterfaceGraphique interfaceGraphique;
    private final CollecteurEvenements collecteurEv;
    private final HashMap<TYPE_ELEMENT_SUR_TERRAIN, BufferedImage> imagesCaseTerrain = new HashMap<>();

    // Pour la Gestion des Animations : Renversement des cartes
    private final ArrayList<CardFlipAnimator> listeDescardFlipAnimators = new ArrayList<>();

    // Chargement des images dans une liste
    HashMap<TYPECARTE, BufferedImage> imagesCartes = new HashMap<>();

    // Gestion de panels
    private JPanel cartesNord;
    private JPanel cartesEst;
    private JPanel cartesSud;

    private BoutonTerrain[][] buttonsTerrain;
    private BoutonCarte[] buttonsCartesJoueur1;
    private BoutonCarte[] buttonsCartesJoueur2;
    private BoutonCarte carteDeRotation;
    private BoutonTerrain annuler, refaire, suggestion;

    private JButton boutonSon;

    private JLabel nomJoueurCourantLabel;
    private JLabel tempsLabel;
    private JLabel roundLabel;
    private int numRound;

    // Gestion du son
    Clip clip;
    private boolean musiqueActive = false;

    // Gestion du temps
    private Instant debutTempsPartie;
    Timer timerPartie;
    private JPanel terrain;
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

        creerButtonsCartes();
        initialiserInterface();
        mettreAJourImagesTerrain();

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

        if (jeu.estPartieFinie()) {
            interfaceGraphique.afficherEcranVictoire(jeu.getJoueurCourant().getNom());
        }
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
        JPanel choixJoueur1 = PanelChoixJoueur.creerPanelChoixJoueur(jeu.getNomJoueur1(), ID_JOUEUR_1, collecteurEv);
        JPanel choixJoueur2 = PanelChoixJoueur.creerPanelChoixJoueur(jeu.getNomJoueur2(), ID_JOUEUR_2, collecteurEv);
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
        barreIndication.setLayout(new FlowLayout(FlowLayout.TRAILING,0 , 0));
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

        // === Ligne 1 : Panel choix joueur 1
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(15, 0, 0, 0);
//        contenu.add(choixJoueur1, gbc);

        // === Ligne 1 : Texte du tour ===
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(15, 0, 0, 0);
        contenu.add(creerPanelNomJoueurCourant(), gbc);

        // === Ligne 1 : Panel choix joueur 2
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(15, 0, 0, 0);
//        contenu.add(choixJoueur2, gbc);

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
        gbc.insets = new Insets(0, 0, ESPACE * 3, 0);

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
//        panelCentreEmpile.add(terrain, centreGbc);
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





    private void creerTerrain() {
        terrain = creerPanelArrondi();
        terrain.setBackground(COULEUR_CASE_TERRAIN);
        terrain.setLayout(new GridLayout(LIGNES, COLONNES));
        buttonsTerrain = new BoutonTerrain[LIGNES][COLONNES];

        chargerImagesTerrain();
        initBoutonsTerrain();
    }




    // =========================================
    // ============ Création UI ================
    // =========================================

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
            carteDeRotation.addActionListener(new AdaptateurCarte(0, 0, carteDeRotation, this, collecteurEv));

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
        JPanel boutonsAnnuleRefaire = new JPanel(new GridLayout(7, 1, 0, 10));
        boutonsAnnuleRefaire.setOpaque(false);

        annuler = new BoutonTerrain(PATH_BTN_ANNULER);
        refaire = new BoutonTerrain(PATH_BTN_REFAIRE);
        suggestion = new BoutonTerrain(PATH_BTN.resolve("suggestion.png"));

//        annuler = Bouton.creerBouton(PATH_BTN_ANNULER.toString(), Bouton.ConfigurationParDefaut.Rectangle_transparent);
//        refaire = Bouton.creerBouton(PATH_BTN_REFAIRE.toString(), Bouton.ConfigurationParDefaut.Rectangle_transparent);
//        suggestion = Bouton.creerBouton(PATH_BTN.resolve("suggestion.png").toString(), Bouton.ConfigurationParDefaut.Cercle_transparent);


        annuler.setPreferredSize(new Dimension(135, 60));
        refaire.setPreferredSize(new Dimension(135, 60));
        suggestion.setPreferredSize(new Dimension(135, 60));

        annuler.setBackground(new Color(207, 207, 207, 44));
        refaire.setBackground(new Color(207, 207, 207, 44));
        suggestion.setBackground(new Color(207, 207, 207, 44));

        annuler.setOpaque(true);
        refaire.setOpaque(true);
        suggestion.setOpaque(true);

        annuler.addActionListener(new AdaptateurAnnuler(collecteurEv));
        refaire.addActionListener(new AdaptateurRefaire(collecteurEv));
        suggestion.addActionListener(new AdaptateurSuggestion(collecteurEv, this));

        boutonsAnnuleRefaire.add(Box.createGlue());
//        boutonsAnnuleRefaire.add(Box.createGlue());
        boutonsAnnuleRefaire.add(annuler);
        boutonsAnnuleRefaire.add(refaire);
        boutonsAnnuleRefaire.add(suggestion);
//        boutonsAnnuleRefaire.add(Box.createGlue());
//        boutonsAnnuleRefaire.add(Box.createGlue());

        return boutonsAnnuleRefaire;
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
        boutonSon.setPreferredSize(new Dimension(50, 50));
        boutonSon.setText(musiqueActive ? "on" : "off");
        boutonSon.addActionListener(e -> toggleMusique(boutonSon));

        return boutonSon;
    }

    private JPanel creerPanelNomJoueurCourant() {
        JPanel textNomPanel = new JPanel();
        textNomPanel.setLayout(new BoxLayout(textNomPanel, BoxLayout.Y_AXIS));
        textNomPanel.setBackground(new Color(214, 214, 214, 107));

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
        JPanel panel = creerPanelArrondi();
        panel.setBackground(new Color(255, 255, 255, 255));

        numRound = jeu.getNumeroRound();
        roundLabel = new JLabel("Round: " + numRound);
        roundLabel.setOpaque(false);
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
        menu.setPreferredSize(new Dimension(50, 50));
        menu.addActionListener(e -> interfaceGraphique.ouvrirMenu());

        return menu;
    }

    private JPanel creerBarredesBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setOpaque(false);

        for (int i = 1; i <= 4; i++) {
            BoutonAvecImage bouton = Bouton.creerBouton("", Bouton.ConfigurationParDefaut.Carre_transparent);
            bouton.setPreferredSize(new Dimension(50, 50));
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

    private void tournerLesCartesDuJoueur() {
        if (jeu.getIdJoueurCourant() == ID_JOUEUR_1 && ID_JOUEUR_1 != ID_JOUEUR_PRECEDANT ||
                jeu.getIdJoueurCourant() == ID_JOUEUR_2 && ID_JOUEUR_1 == ID_JOUEUR_PRECEDANT) {

            for (CardFlipAnimator animator : listeDescardFlipAnimators) {
                animator.startAnimation();
            }
            ID_JOUEUR_PRECEDANT = jeu.getIdJoueurCourant();
        }
    }

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

    // =========================================
    // ========= Gestion Son & Musique =========
    // =========================================

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

    private void miseAjourTemps() {
        if (debutTempsPartie != null && tempsLabel != null) {
            Duration duration = Duration.between(debutTempsPartie, Instant.now());
            long minutes = duration.toMinutes();
            long secondes = duration.getSeconds() % 60;
            tempsLabel.setText(String.format("%02d:%02d", minutes, secondes));
        }
    }

    // =========================================
    // ============== Mise à jour ==============
    // =========================================

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
                boutonCase.setPreferredSize(new Dimension(10, 10));
                apliquerConfifUtilisateur(boutonCase, row, col);

                boutonCase.addActionListener(new AdaptateurBoutonTerrain(boutonCase, casePlateau, collecteurEv, this));
                buttonsTerrain[row][col] = boutonCase;
                terrain.add(boutonCase);
            }
        }
    }


    /**
     * Applique les configurations utilisateur sur un bouton de terrain.
     *
     * @param boutonCase le bouton sur lequel appliquer les configurations utilisateur
     * @param row la ligne de la case du plateau
     * @param col la colonne de la case du plateau
     */
    private void apliquerConfifUtilisateur(BoutonTerrain boutonCase, int row, int col) {
        if (row == 0 && col == 2) { // case maitre joueur 1
            boutonCase.chargerCouleurFond(infosDeConfigUI.getCouleurCaseMaitreJoueur(ID_JOUEUR_1));
        } else if (row == 4 && col == 2) { // case maitre joueur 2
            boutonCase.chargerCouleurFond(infosDeConfigUI.getCouleurCaseMaitreJoueur(ID_JOUEUR_2));
        } else if (row == 0) {
            boutonCase.chargerCouleurFond(infosDeConfigUI.getCouleurCaseEleveJoueur(ID_JOUEUR_1));
        } else if (row == 4) {
            boutonCase.chargerCouleurFond(infosDeConfigUI.getCouleurCaseEleveJoueur(ID_JOUEUR_2));
        }

    }


    /**
     * Détermine le type d'élément présent sur une case du plateau.
     */
    private TYPE_ELEMENT_SUR_TERRAIN determinerTypeElement(CasePlateau casePlateau) {
        switch (casePlateau.getTypeElement()) {
            case VIDE:
                if (casePlateau.getCoordonnee().equals(new Point(0,2)) || casePlateau.getCoordonnee().equals(new Point(4,2)))
                    return TYPE_ELEMENT_SUR_TERRAIN.TRONE;
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

                apliquerConfifUtilisateur(buttonsTerrain[row][col], row, col);
                buttonsTerrain[row][col].changerImage(image);
            }
        }
        appliquerBordureDynamiqueSurTerrain();
    }


    private void appliquerBordureDynamiqueSurTerrain() {
        Color couleurBordure = infosDeConfigUI.getCouleurPionJoueur(jeu.getJoueurCourant().getId());
        Border bordureArrondie = new BordureArrondieAvecOmbre(couleurBordure, 5, ARRONDI);
        Border margeInterne = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        terrain.setBorder(BorderFactory.createCompoundBorder(bordureArrondie, margeInterne));
    }

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

    // =========================================
    // ============== Méthodes Utiles ==========
    // =========================================

    public Jeu getJeu() {
        return jeu;
    }

    public BoutonTerrain getBoutonterrainAt(Point bouton) {
        return buttonsTerrain[bouton.x][bouton.y];
    }

    // Contient les images liées à chaque type d'élément affichable sur le terrain.
    public enum TYPE_ELEMENT_SUR_TERRAIN {
        VIDE,
        TRONE,
        PION_ETUDIANT_J1,
        PION_ETUDIANT_J2,
        PION_MAITRE_J1,
        PION_MAITRE_J2
    }

    public Duration getDebutTempsPartie() {
        return Duration.between(debutTempsPartie, Instant.now());
    }


    /**
     * Méthode appelée lorsque le jeu se termine.
     * Affiche l'écran de victoire avec le nom du joueur gagnant.
     *
     * @param nomGagnant Nom du joueur qui a gagné.
     */
    public void afficherEcranVictoire(String nomGagnant) {
        logger.info("Partie terminée. Gagnant : " + nomGagnant);
        EcranVictoire ecranVictoire = new EcranVictoire(nomGagnant, interfaceGraphique);
        interfaceGraphique.setContentPane(ecranVictoire);
        interfaceGraphique.mettreAJourDispositions();
        SwingUtilities.invokeLater(() -> {
            interfaceGraphique.frame.revalidate();
            interfaceGraphique.frame.repaint();
        });
    }



}



class PanelChoixJoueur {

    // Constantes pour le style et les dimensions
    private static final int PANEL_WIDTH = 320;
    private static final int PANEL_HEIGHT = 170;
    private static final int COMBO_WIDTH = 220;
    private static final int COMBO_HEIGHT = 35;
    private static final int CORNER_RADIUS = ARRONDI;
    private static final Font COMBO_FONT = new Font("SansSerif", Font.PLAIN, 15); // Police moderne
    private static final Color COULEUR_FOND_PRINCIPAL = new Color(238, 242, 245); // Bleu très clair/gris
    private static final Color COULEUR_PANEL_CENTRAL = Color.WHITE;
    private static final Color COULEUR_OMBRE = new Color(0, 0, 0, 30); // Ombre un peu plus visible
    private static final Color COULEUR_BORDURE_PANEL = new Color(200, 205, 210); // Bordure subtile
    private static final Color COULEUR_SELECTION_COMBO = new Color(200, 220, 255); // Bleu clair pour la sélection
    private static final Insets MARGES_PANEL_PRINCIPAL = new Insets(25, 25, 25, 25);
    private static final Insets MARGES_INTERNES_GB = new Insets(15, 15, 15, 15); // Espacement pour GridBag
    private static final Insets MARGES_COMBO_GB = new Insets(10, 15, 20, 15); // Espacement spécifique pour la ComboBox

    // Énumération pour les types de joueurs
    public enum TypeJoueur {
        HUMAIN("Humain"),
        IA_FACILE("IA - Facile"), // Labels plus descriptifs
        IA_MOYEN("IA - Moyen"),
        IA_DIFFICILE("IA - Difficile");

        private final String libelle;

        TypeJoueur(String libelle) {
            this.libelle = libelle;
        }

        @Override
        public String toString() {
            return libelle;
        }
    }

    public static JPanel creerPanelChoixJoueur(String nomJoueur, int idJoueur, CollecteurEvenements collecteurEv) {
        JPanel mainPanel = creerMainPanel();
        JPanel panelCentral = creerPanelCentral();
        JPanel panelNom = creerPanelNom(nomJoueur);
        JComboBox<TypeJoueur> comboType = creerComboType(idJoueur, collecteurEv);

        assemblerComposants(mainPanel, panelCentral, panelNom, comboType);

        return mainPanel;
    }

    private static JPanel creerMainPanel() {
        JPanel mainPanel = creerPanelCentral();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(
                MARGES_PANEL_PRINCIPAL.top, MARGES_PANEL_PRINCIPAL.left,
                MARGES_PANEL_PRINCIPAL.bottom, MARGES_PANEL_PRINCIPAL.right));
        mainPanel.setBackground(COULEUR_FOND_PRINCIPAL);
        return mainPanel;
    }

    private static JPanel creerPanelCentral() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // Important pour la propreté du rendu
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                int width = getWidth();
                int height = getHeight();

                // Ombre portée subtile
                g2.setColor(COULEUR_OMBRE);
                g2.fillRoundRect(3, 3, width - 4, height - 4, CORNER_RADIUS, CORNER_RADIUS); // Ombre légèrement décalée

                // Fond du panel
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, width - 1, height - 1, CORNER_RADIUS, CORNER_RADIUS);

                // Bordure
                g2.setColor(COULEUR_BORDURE_PANEL);
                g2.setStroke(new BasicStroke(1f)); // Bordure fine
                g2.drawRoundRect(0, 0, width - 1, height - 1, CORNER_RADIUS, CORNER_RADIUS);
                g2.dispose();
            }
        };
        panel.setBackground(COULEUR_PANEL_CENTRAL);
        panel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false); // Nécessaire car on dessine notre propre fond et ombre
        return panel;
    }

    private static JPanel creerPanelNom(String nomJoueur) {
        JPanel nomPanel = PngText.createPngPanel(nomJoueur, 20); // Gardons la taille originale pour l'instant
        nomPanel.setOpaque(false);
        return nomPanel;
    }

    private static JComboBox<TypeJoueur> creerComboType(int idJoueur, CollecteurEvenements collecteurEv) {
        JComboBox<TypeJoueur> comboType = new JComboBox<>(TypeJoueur.values());
        styliserComboBox(comboType);
        ajouterEcouteurComboBox(comboType, idJoueur, collecteurEv);
        return comboType;
    }

    private static void styliserComboBox(JComboBox<TypeJoueur> comboType) {
        comboType.setPreferredSize(new Dimension(COMBO_WIDTH, COMBO_HEIGHT));
        comboType.setFont(COMBO_FONT);
        comboType.setBackground(Color.WHITE); // Fond blanc pour la ComboBox
        comboType.setForeground(new Color(50, 50, 50)); // Texte foncé

        // Renderer pour l'élément sélectionné et la liste déroulante
        comboType.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); // Padding généreux

                if (isSelected) {
                    setBackground(COULEUR_SELECTION_COMBO);
                    setForeground(new Color(30, 30, 30));
                } else {
                    setBackground(Color.WHITE); // Fond des items non sélectionnés
                    setForeground(new Color(50, 50, 50));
                }
                return this;
            }
        });

        // Pour enlever la bordure par défaut de la ComboBox si souhaité (plus complexe, via UI delegate)
        // ((JComponent) comboType.getRenderer()).setBorder(BorderFactory.createEmptyBorder(2,5,2,0));
        // comboType.setBorder(BorderFactory.createLineBorder(COULEUR_BORDURE_PANEL)); // Bordure personnalisée
    }

    private static void ajouterEcouteurComboBox(JComboBox<TypeJoueur> comboType, int idJoueur, CollecteurEvenements collecteurEv) {
        comboType.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                TypeJoueur typeSelectionne = (TypeJoueur) e.getItem();
                if (typeSelectionne != null && collecteurEv != null) {
                    // Format de l'événement : EPDT-<LibelleType>-<idJoueur>
                    // EPDT = Ecran Plateau De Jeu
                    collecteurEv.clavier("EPDT-" + typeSelectionne.toString() + "-" + idJoueur);
                }
            }
        });
    }

    private static void assemblerComposants(JPanel mainPanel, JPanel panelCentral, JPanel panelNom, JComboBox<TypeJoueur> comboType) {
        GridBagConstraints gbc = new GridBagConstraints();

        // Ajout du panel du nom
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weighty = 0.5; // Donne plus d'espace en haut
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = MARGES_INTERNES_GB;
        panelCentral.add(panelNom, gbc);

        // Ajout de la ComboBox
        gbc.gridy = 1;
        gbc.weighty = 0.5; // Donne plus d'espace en bas
        gbc.fill = GridBagConstraints.HORIZONTAL; // Permet à la ComboBox de prendre la largeur si nécessaire
        gbc.anchor = GridBagConstraints.PAGE_END; // Ancrer en bas de son espace
        gbc.insets = MARGES_COMBO_GB;
        panelCentral.add(comboType, gbc);

        mainPanel.add(panelCentral, BorderLayout.CENTER);
    }




    // Méthode main pour tester (nécessite PngText et CollecteurEvenements stubs)
    public static void main(String[] args) {
        // Stubs pour les classes manquantes (à remplacer par vos vraies implémentations)
        // Ces stubs sont juste pour que le code compile et s'exécute pour la démo.
        // Vous devrez les remplacer par vos classes PngText, CollecteurEvenements, et Mediateur.


        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Configuration du Joueur - Améliorée");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBackground(COULEUR_FOND_PRINCIPAL); // Assortir le fond de la frame

            JPanel joueurPanel = PanelChoixJoueur.creerPanelChoixJoueur("Rinel", 1, new Mediateur(null));
            frame.add(joueurPanel, BorderLayout.CENTER);

            frame.pack();
            frame.setLocationRelativeTo(null); // Centrer à l'écran
            frame.setVisible(true);
        });
    }
}