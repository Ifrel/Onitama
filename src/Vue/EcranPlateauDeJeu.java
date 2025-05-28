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

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

import static Global.Config.*;
import static Global.Paths.*;
import static Vue.Configuration.ConfigUI.ARRONDI;
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
    private JButton annuler, refaire, suggestion;

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


    // Configuration Mode Joueur
    private JButton boutonFlottant;
    private JPanel panelTemporaire;
    private Timer timerDisparition;
    private boolean isPanelHovered = false;
    private boolean estCliqueBoutonFlotant = false;


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
        // Initialisation des composants principaux
        initialiserComposantsPrincipaux();

        // Création du conteneur principal
        JPanel contenu = new JPanel(new GridBagLayout());
        contenu.setBorder(BorderFactory.createEmptyBorder(ESPACE, ESPACE, ESPACE, ESPACE));
        contenu.setOpaque(false);

        // Configuration de la barre supérieure
        ajouterBarreSuperieure(contenu);

        // Configuration du panneau central
        ajouterPanneauCentral(contenu);

        // Configuration finale
        this.setLayout(new BorderLayout());
        this.add(new PanelRatioFixe(contenu, 1), BorderLayout.CENTER);


        // Démarrage du timer
        debutTempsPartie = Instant.now();
        timerPartie = new Timer(1000, e -> miseAjourTemps());
        timerPartie.start();
    }


    private void initialiserComposantsPrincipaux() {
        cartesNord = new JPanel();
        cartesEst = new JPanel();
        cartesSud = new JPanel();
        creerTerrain();
        creerCartesNord();
        creerCarteGauche();
        creerCartesSud();
        creerBoutonFlottant();
        creerPanelTemporaire();
    }


    private void ajouterBarreSuperieure(JPanel contenu) {
        // Création de la barre d'indication
        JPanel barreIndication = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 10));
        barreIndication.setOpaque(false);

        // Ajout des composants à la barre
        barreIndication.add(creerBoutonSon());
        barreIndication.add(creerBoutonIA());
        barreIndication.add(Box.createHorizontalStrut(ESPACE));
        barreIndication.add(Box.createGlue());
        barreIndication.add(creerPanelRoundTemps());
        barreIndication.add(Box.createHorizontalStrut(ESPACE));
        barreIndication.add(creerBoutonMenu());

        // Configuration et ajout de la barre
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, ESPACE, 0);
        contenu.add(barreIndication, gbc);

        // Ajout du panneau nom joueur
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(15, 0, 0, 0);
        contenu.add(creerPanelNomJoueurCourant(), gbc);

        // Ajout de l'espace
        gbc.gridy = 2;
        gbc.weightx = 0.25;
        gbc.weighty = 0.2;
        contenu.add(Box.createGlue(), gbc);
    }


    private void ajouterPanneauCentral(JPanel contenu) {
        JPanel panelCentreEmpile = new JPanel(new GridBagLayout());
        panelCentreEmpile.setOpaque(false);

        // Configuration des contraintes pour le panneau central
        GridBagConstraints centreGbc = new GridBagConstraints();
        centreGbc.fill = GridBagConstraints.BOTH;

        // Ajout des composants au panneau central
        ajouterComposantsCentraux(panelCentreEmpile, centreGbc);

        // Ajout du panneau central au conteneur principal
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
//        gbc.insets = new Insets(0, 0, ESPACE * 3, 0);
        contenu.add(panelCentreEmpile, gbc);
    }


    /**
     * Ajoute les composants au panneau central avec une répartition équitable de l'espace
     * et maintient le terrain carré.
     */
    private void ajouterComposantsCentraux(JPanel panel, GridBagConstraints gbc) {
        // Configuration de base des contraintes
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Panneau nord (cartes)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 2.0;
        gbc.weighty = 1.0;
        panel.add(cartesNord, gbc);

        // Panneau est (cartes)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 2.0;
        panel.add(cartesEst, gbc);

        // Terrain central (maintenu carré)
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 5.0;
        gbc.weighty = 5.0;
        gbc.insets = new Insets(20, 20, 20, 20);

        // Utilisation de PanelRatioFixe pour maintenir le ratio 1:1 du terrain
        panel.add(new PanelRatioFixe(terrain, 1.0), gbc);
        gbc.insets = new Insets(0, 0, 0, 0);

        // Panneau ouest (boutons)
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 2.0;
        JPanel boutonsDroite = creerBoutonsDroite();
        panel.add(boutonsDroite, gbc);

        // Panneau sud (cartes)
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 2.0;
        gbc.weighty = 1.0;
        panel.add(cartesSud, gbc);
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

    private void creerCartesNord() {
        // Utilisation de GridBagLayout pour un meilleur contrôle
        cartesNord.setLayout(new GridBagLayout());
//        cartesNord.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.weighty = 0.2;

        // Espacement élastique à gauche
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        cartesNord.add(Box.createHorizontalGlue(), gbc);

        // Première carte
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(0, 10, 0, 10);
        cartesNord.add(buttonsCartesJoueur1[0], gbc);

        // Deuxième carte
        gbc.gridx = 2;
        cartesNord.add(buttonsCartesJoueur1[1], gbc);

        // Espacement élastique à droite
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 10);
        cartesNord.add(Box.createHorizontalGlue(), gbc);
    }

    private void creerCartesSud() {
        // Utilisation de GridBagLayout pour un meilleur contrôle
        cartesSud.setLayout(new GridBagLayout());
//        cartesSud.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.weighty = 0.2;

        // Espacement élastique à gauche
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        cartesSud.add(Box.createHorizontalGlue(), gbc);

        // Première carte
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(0, 10, 0, 10);
        cartesSud.add(buttonsCartesJoueur2[0], gbc);

        // Deuxième carte
        gbc.gridx = 2;
        cartesSud.add(buttonsCartesJoueur2[1], gbc);

        // Espacement élastique à droite
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 10);
        cartesSud.add(Box.createHorizontalGlue(), gbc);
    }

    private void creerCarteGauche() {
        JPanel cartesEstbis = new JPanel(new GridBagLayout());
        cartesEstbis.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;


        // Espacement vertical en haut
        gbc.gridy = 0;
        gbc.weightx = 2;
        gbc.weighty = 1.1;
        cartesEstbis.add(Box.createVerticalGlue(), gbc);

        // Carte de rotation au centre
        gbc.gridy = 1;
        gbc.weightx = 0.45;
        gbc.weighty = 0.45;
        cartesEstbis.add(cardFlipAnimator(carteDeRotation), gbc);

        // Espacement vertical en bas
        gbc.gridy = 2;
        gbc.weightx = 1.1;
        gbc.weighty = 1.1;
        cartesEstbis.add(Box.createVerticalGlue(), gbc);

        // Configuration du panel Est
        cartesEst.setLayout(new BoxLayout(cartesEst, BoxLayout.X_AXIS));
        cartesEst.setOpaque(false);

        // Ajout d'un espacement horizontal et du panel de la carte
        cartesEst.add(Box.createHorizontalGlue());
        cartesEst.add(cartesEstbis);
    }

    private JPanel creerBoutonsDroite() {
        // Création du panel principal avec GridBagLayout
        JPanel droite = new JPanel(new GridBagLayout());
        droite.setOpaque(false);

        // Création des boutons
        annuler = Bouton.creerBouton(PATH_BTN_ANNULER.toString(), Bouton.ConfigurationParDefaut.Rectangle_transparent_V2);
        refaire = Bouton.creerBouton(PATH_BTN_REFAIRE.toString(), Bouton.ConfigurationParDefaut.Rectangle_transparent_V2);
        suggestion = Bouton.creerBouton(PATH_BTN.resolve("suggestion_on.png").toString(), Bouton.ConfigurationParDefaut.Rectangle_transparent_V2);

        // Configuration des dimensions
        Dimension dimensionBoutonNormal = new Dimension(70, 70);
        Dimension dimensionBoutonSuggestion = new Dimension(70, 100);

        annuler.setPreferredSize(dimensionBoutonNormal);
        refaire.setPreferredSize(dimensionBoutonNormal);
        suggestion.setPreferredSize(dimensionBoutonSuggestion);

        // Configuration de l'apparence
        Color couleurFond = new Color(207, 207, 207, 44);
        annuler.setBackground(couleurFond);
        refaire.setBackground(couleurFond);
        suggestion.setBackground(couleurFond);

        // Ajout des écouteurs
        annuler.addActionListener(new AdaptateurAnnuler(collecteurEv));
        refaire.addActionListener(new AdaptateurRefaire(collecteurEv));
        suggestion.addActionListener(new AdaptateurSuggestion(collecteurEv, this));

        // Configuration du GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 0, 5, 0); // Espacement vertical entre les boutons

        // Ajout de l'espace élastique en haut
        gbc.gridy = 0;
        droite.add(Box.createVerticalGlue(), gbc);
        gbc.gridy++;
        droite.add(Box.createVerticalGlue(), gbc);

        // Ajout des boutons
        gbc.gridy++;
        droite.add(annuler, gbc);

        gbc.gridy++;
        droite.add(refaire, gbc);

        gbc.gridy++;
        droite.add(suggestion, gbc);

        // Ajout de l'espace élastique en bas
        gbc.gridy++;
        droite.add(Box.createVerticalGlue(), gbc);
        gbc.gridy++;
        droite.add(Box.createVerticalGlue(), gbc);

        return droite;
    }

    private JButton creerBoutonSon() {
        boutonSon = Bouton.creerBouton(PATH_BTN_MUET.toString(), Bouton.ConfigurationParDefaut.SansBordure_transparent);
        boutonSon.setPreferredSize(new Dimension(80, 80));
        boutonSon.setToolTipText("Musique");
        boutonSon.addActionListener(e -> toggleMusique(boutonSon));

        return boutonSon;
    }

    private JPanel creerPanelNomJoueurCourant() {
        JPanel textNomPanel = ConfigModeJoueur.creerPanelCoinsArondiAvecBordure();
        textNomPanel.setPreferredSize(new Dimension(300, 120));
        textNomPanel.setLayout(new BoxLayout(textNomPanel, BoxLayout.Y_AXIS));
        textNomPanel.setBackground(new Color(214, 214, 214, 107));
        textNomPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textNomPanel.setToolTipText("Nom du joueur qui joue actuellement");

        JLabel txt = new JLabel("C'est au tour de");
        txt.setFont(new Font("Arial", Font.BOLD, 20));
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
        txt.setForeground(new Color(197, 7, 184));

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
        JPanel panel = ConfigModeJoueur.creerPanelCoinsdArrondi();
        panel.setPreferredSize(new Dimension(300, 80));
        panel.setBackground(new Color(221, 221, 221, 131));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setToolTipText("Nombre de partie jouée et temps écoulé");

        numRound = jeu.getNumeroRound();
        roundLabel = new JLabel("Partie : " + numRound);
        roundLabel.setOpaque(false);
        roundLabel.setFont(new Font("Arial", Font.PLAIN, 25));

        tempsLabel = new JLabel("00:00");
        tempsLabel.setOpaque(false);
        tempsLabel.setFont(new Font("Arial", Font.BOLD, 25));

        panel.add(Box.createGlue());
        panel.add(roundLabel);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(tempsLabel);
        panel.add(Box.createGlue());

        return panel;
    }

    private JButton creerBoutonMenu() {
        BoutonAvecImage menu = Bouton.creerBouton(PATH_BTN_MENU.toString(), Bouton.ConfigurationParDefaut.SansBordure_transparent);
        menu.setPreferredSize(new Dimension(100, 100));
        menu.setToolTipText("Menu");
        menu.addActionListener(e -> interfaceGraphique.ouvrirMenu());
        return menu;
    }

    private JPanel creerBoutonIA() {
        Boolean [] isActive = {false};
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setOpaque(false);

        BoutonAvecImage bouton = Bouton.creerBouton(PATH_BTN.resolve("ia.png").toString(), Bouton.ConfigurationParDefaut.SansBordure_transparent);
        bouton.setPreferredSize(new Dimension(100, 100));
        bouton.setToolTipText("IA vs IA");
        bouton.addActionListener(e -> {
            collecteurEv.clavier("ia vs ia");
            isActive[0] = !isActive[0];
            bouton.changerImage(PATH_BTN.resolve("ia" + (isActive[0] ? "_on" : "") + ".png").toString());
        });
        panel.add(bouton);
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
            roundLabel.setText("Partie: " + numRound);

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
        // Configuration par défaut des boutons
        Dimension dimensionBouton = new Dimension(10, 10);

        // Utilisation de GridBagLayout pour une meilleure gestion du positionnement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Initialisation des boutons
        for (int row = 0; row < LIGNES; row++) {
            gbc.gridy = row;

            for (int col = 0; col < COLONNES; col++) {
                gbc.gridx = col;

                // Récupération des informations de la case
                CasePlateau casePlateau = jeu.getCasePlateau(row, col);
                TYPE_ELEMENT_SUR_TERRAIN typeElement = determinerTypeElement(casePlateau);
                BufferedImage image = imagesCaseTerrain.get(typeElement);

                // Création et configuration du bouton
                BoutonTerrain boutonCase = new BoutonTerrain(image);
                boutonCase.setPreferredSize(dimensionBouton);

                // Application des configurations utilisateur
                apliquerConfifUtilisateur(boutonCase, row, col);

                // Ajout de l'écouteur d'événements
                boutonCase.addActionListener(new AdaptateurBoutonTerrain(
                        boutonCase,
                        casePlateau,
                        collecteurEv,
                        this
                ));

                // Stockage du bouton dans le tableau
                buttonsTerrain[row][col] = boutonCase;

                // Ajout au terrain avec les contraintes
                terrain.add(boutonCase, gbc);
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
//        if (row == 0 && col == 2) { // case maitre joueur 1
//            boutonCase.chargerCouleurFond(infosDeConfigUI.getCouleurCaseMaitreJoueur(ID_JOUEUR_1));
//        } else if (row == 4 && col == 2) { // case maitre joueur 2
//            boutonCase.chargerCouleurFond(infosDeConfigUI.getCouleurCaseMaitreJoueur(ID_JOUEUR_2));
//        } else if (row == 0) {
//            boutonCase.chargerCouleurFond(infosDeConfigUI.getCouleurCaseEleveJoueur(ID_JOUEUR_1));
//        } else if (row == 4) {
//            boutonCase.chargerCouleurFond(infosDeConfigUI.getCouleurCaseEleveJoueur(ID_JOUEUR_2));
//        }

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




    private JPanel creerPanelConfig() {
        JPanel mainPanel = ConfigModeJoueur.creerPanelCoinsdArrondi();
        mainPanel.setLayout(new GridLayout(3, 1));
        mainPanel.setBackground(ConfigModeJoueur.COULEUR_FOND_PRINCIPAL);
        mainPanel.setPreferredSize(new Dimension(500, 200));

        JPanel joueur1 = ConfigModeJoueur.creerPanelChoixJoueur("Rinel", 1, new Mediateur(null));
        JPanel joueur2 = ConfigModeJoueur.creerPanelChoixJoueur("Raphael", 2, new Mediateur(null));

        mainPanel.add(joueur1);
        mainPanel.add(Box.createHorizontalStrut(20));
        mainPanel.add(joueur2);

        return mainPanel;
    }


    private void creerPanelTemporaire() {
        // Création du panel
        panelTemporaire = ConfigModeJoueur.creerPanelCoinsdArrondi();
        panelTemporaire.setLayout(new BoxLayout(panelTemporaire, BoxLayout.Y_AXIS));
        panelTemporaire.setBackground(ConfigModeJoueur.COULEUR_FOND_PRINCIPAL);

        // Contenu
        panelTemporaire.add(new JLabel("Menu Temporaire"));
        panelTemporaire.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTemporaire.add(new JSeparator());
        JPanel joueur1 = ConfigModeJoueur.creerPanelChoixJoueur(jeu.getNomJoueur1(), 1, collecteurEv);
        JPanel joueur2 = ConfigModeJoueur.creerPanelChoixJoueur(jeu.getNomJoueur2(), 2, collecteurEv);

        panelTemporaire.add(joueur1);
        panelTemporaire.add(Box.createHorizontalStrut(100));
        panelTemporaire.add(joueur2);

        // Taille et style
        panelTemporaire.setSize(new Dimension(300, 400));

        // Par défaut, le panel est invisible
        panelTemporaire.setVisible(false);

        // Ajout au conteneur principal
        this.add(panelTemporaire);

        // Création du timer
        timerDisparition = new Timer(5000, e -> {
            if (!isPanelHovered) {
                panelTemporaire.setVisible(false);
                timerDisparition.stop();
                estCliqueBoutonFlotant = false;
            }
        });
        timerDisparition.setRepeats(false);

        // Gestion du survol
        panelTemporaire.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isPanelHovered = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isPanelHovered = false;
                // Redémarrer le timer quand la souris quitte le panel
                timerDisparition.restart();
            }
        });
    }


    private void creerBoutonFlottant() {
        boutonFlottant = Bouton.creerBouton(PATH_BTN.resolve("optionJoueur.png").toString(), Bouton.ConfigurationParDefaut.Rectangle_transparent_V2);
        boutonFlottant.setSize(120, 120);

        // Rendre le bouton déplaçable
        MouseAdapter dragListener = new MouseAdapter() {
            private Point offset;

            @Override
            public void mousePressed(MouseEvent e) {
                offset = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (boutonFlottant.getParent() != null) {
                    int newX = boutonFlottant.getX() + e.getX() - offset.x;
                    int newY = boutonFlottant.getY() + e.getY() - offset.y;

                    // Limiter le déplacement dans les bornes du parent
                    newX = Math.max(0, Math.min(newX, boutonFlottant.getParent().getWidth() - boutonFlottant.getWidth()));
                    newY = Math.max(0, Math.min(newY, boutonFlottant.getParent().getHeight() - boutonFlottant.getHeight()));

                    boutonFlottant.setLocation(newX, newY);
                }
            }
        };

        boutonFlottant.addMouseListener(dragListener);
        boutonFlottant.addMouseMotionListener(dragListener);

        // Style du bouton
        boutonFlottant.setBackground(new Color(64, 158, 255));
        boutonFlottant.setForeground(Color.WHITE);
        boutonFlottant.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        boutonFlottant.setFocusPainted(false);

        // Position initiale
        boutonFlottant.setLocation(100, 80);
        boutonFlottant.setVisible(true);
        boutonFlottant.setToolTipText("Option de Configuration des Joueurs");

        boutonFlottant.addActionListener(e -> {
            // Positionner le panel près du bouton flottant
            Point boutonLocation = boutonFlottant.getLocation();
            panelTemporaire.setLocation(
                    boutonLocation.x - (panelTemporaire.getWidth()/2 - boutonFlottant.getWidth()/2),
                    boutonLocation.y + boutonFlottant.getHeight() + 5
            );

            // Afficher le panel
            estCliqueBoutonFlotant = !estCliqueBoutonFlotant;
            if (estCliqueBoutonFlotant) {
                panelTemporaire.setVisible(true);
                timerDisparition.restart();
            } else {
                panelTemporaire.setVisible(false);
                timerDisparition.stop();
            }

        });

        // Ajouter au panel principal avec un layout null pour permettre le positionnement absolu
        this.setLayout(null);
        this.add(boutonFlottant);
    }

}



class ConfigModeJoueur {

    // Constantes pour le style et les dimensions
    private static final int PANEL_WIDTH = 320;
    private static final int PANEL_HEIGHT = 170;
    private static final int CORNER_RADIUS = ARRONDI;
    private static final Font COMBO_FONT = new Font("SansSerif", Font.PLAIN, 15); // Police moderne
    static final Color COULEUR_FOND_PRINCIPAL = new Color(238, 242, 245); // Bleu très clair/gris
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
        JPanel mainPanel = creerPanelCoinsArondiAvecBordure();
        JPanel panelCentral = creerPanelCoinsdArrondi();
        // JPanel panelNom = creerPanelNom(nomJoueur);
        JLabel panelNom = new JLabel(nomJoueur);
        panelNom.setFont(new Font("SansSerif", Font.BOLD, 18));

        JComboBox<TypeJoueur> comboType = creerComboType(idJoueur, collecteurEv);

        assemblerComposants(mainPanel, panelCentral, panelNom, comboType);

        return mainPanel;
    }


    public static JPanel creerPanelCoinsArondiAvecBordure() {
        JPanel mainPanel = creerPanelCoinsdArrondi();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(
                MARGES_PANEL_PRINCIPAL.top, MARGES_PANEL_PRINCIPAL.left,
                MARGES_PANEL_PRINCIPAL.bottom, MARGES_PANEL_PRINCIPAL.right));
        mainPanel.setBackground(COULEUR_FOND_PRINCIPAL);
        return mainPanel;
    }


    public static JPanel creerPanelCoinsdArrondi() {
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


    private static JComboBox<TypeJoueur> creerComboType(int idJoueur, CollecteurEvenements collecteurEv) {
        JComboBox<TypeJoueur> comboType = new JComboBox<>(TypeJoueur.values());
        styliserComboBox(comboType);
        ajouterEcouteurComboBox(comboType, idJoueur, collecteurEv);
        return comboType;
    }


    private static void styliserComboBox(JComboBox<TypeJoueur> comboType) {
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

    }


    private static void ajouterEcouteurComboBox(JComboBox<TypeJoueur> comboType, int idJoueur, CollecteurEvenements collecteurEv) {
        comboType.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                TypeJoueur typeSelectionne = (TypeJoueur) e.getItem();
                if (typeSelectionne != null && collecteurEv != null) {
                    collecteurEv.clavier(typeSelectionne + "-" + idJoueur);
                }
            }
        });
    }


    private static void assemblerComposants(JPanel mainPanel, JPanel panelCentral, JLabel panelNom, JComboBox<TypeJoueur> comboType) {
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
}