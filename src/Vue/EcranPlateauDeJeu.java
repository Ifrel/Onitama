package Vue;

import Controleur.ControleurAnimation;
import Modele.CasePlateau;
import Modele.Jeu;
import Patterns.Observateur;
import Vue.Adaptateurs.*;
import Modele.Carte;
import Vue.Utils.Boutons.BoutonTerrain;
import Vue.Utils.PanelBruitGris;
import Vue.Utils.PanelRatioFixe;
import Vue.Utils.Boutons.BoutonCarte;

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
import static Global.Paths.*;
import static Vue.Utils.MethodsStaticsUtils.*;


/**
 * Classe représentant l'interface graphique principale du plateau de jeu.
 * Elle observe le modèle (Jeu) et met à jour l'affichage en fonction des événements.
 */
public class EcranPlateauDeJeu extends PanelBruitGris implements Observateur {
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

    JPanel terrain;


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
        logger.info("Interface Plateau de jeu lancée");

        setLayout(new BorderLayout());
        setBackground(COULEUR_PLATEAU_DE_JEU);

        creerButtonsCartes();
        initialiserInterface();

        miseAJour();
    }


    @Override
    public void miseAJour() {
        logger.info("Mise à jour de l'interface...");
        updateTerrain();
//        initBoutonsTerrain();
        updateCartes();
        updatePlayerAndRoundInfo();
        updateUndoRedoButtons();
        logger.info("Mise à jour de l'interface terminée.");
    }


    /**
     * Initialise l'interface utilisateur avec GridBagLayout. */
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
        barreIndication.setLayout(new BoxLayout(barreIndication, BoxLayout.X_AXIS));
        barreIndication.setOpaque(false);
        barreIndication.add( creerBoutonSon());
//        barreIndication.add(creerPanelBoutonIA());
//        barreIndication.add(Box.createHorizontalStrut(ESPACE));
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
        centreGbc.weighty = 1.0;
        panelCentreEmpile.add(cartesEst, centreGbc);

        // terrain
        centreGbc.gridx = 1;
        centreGbc.gridy = 1;
        centreGbc.weightx = 0.5;
        centreGbc.weighty = 0.5;
        centreGbc.insets = new Insets(20, 20, 20, 20);
        panelCentreEmpile.add(terrain, centreGbc);
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
        add(contenu, BorderLayout.CENTER);

        // Démarrage du timer
        debutTempsPartie = Instant.now();
        timerPartie = new Timer(1000, e -> miseAjourTemps());
        timerPartie.start();
    }




    // =========================================
    // ============ Création UI ================
    // =========================================

    private void initBoutonsTerrain() {
        for (int row = 0; row < LIGNES; row++) {
            for (int col = 0; col < COLONNES; col++) {
                CasePlateau casePlateau = jeu.getCasePlateau(row, col);
                BoutonTerrain boutonCase = new BoutonTerrain(getCheminImagePion(infosDeConfigUI, casePlateau));
                boutonCase.addActionListener(new AdaptateurBoutonTerrain(boutonCase, casePlateau, collecteurEv, this));
                boutonCase.setPreferredSize(new Dimension(10,10));
                buttonsTerrain[row][col] = boutonCase;
                terrain.add(boutonCase);
            }
        }
    }

    /** Crée la grille du terrain de jeu */
    private void creerTerrain() {
        terrain = new JPanel(new GridLayout(LIGNES, COLONNES, 0, 0));
        buttonsTerrain = new BoutonTerrain[LIGNES][COLONNES];

        terrain.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 206, 206), 5, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        terrain.setBackground(new Color(226, 226, 226));

        initBoutonsTerrain();
    }


    /** Crée les boutons représentant les cartes */
    private void creerButtonsCartes() {
        buttonsCartesJoueur1 = new BoutonCarte[NOMBRE_CARTES_MAIN];
        buttonsCartesJoueur2 = new BoutonCarte[NOMBRE_CARTES_MAIN];

        carteDeRotation = new BoutonCarte(getCheminImageCarte(jeu.getCarteSupplementaire()));
        carteDeRotation.addActionListener(new AdaptateurCarte(
                0, carteDeRotation,
                jeu.getCarteSupplementaire(),
                this,
                collecteurEv)
        );

        List<Carte> carteListJ1 = jeu.getCartesJoueur1();
        List<Carte> carteListJ2 = jeu.getCartesJoueur2();
        for (int idCarte = 0; idCarte < NOMBRE_CARTES_MAIN; idCarte++) {
            BoutonCarte boutonCarteJ1 = new BoutonCarte(getCheminImageCarte(carteListJ1.get(idCarte)));
            BoutonCarte boutonCarteJ2 = new BoutonCarte(getCheminImageCarte(carteListJ2.get(idCarte)));
            boutonCarteJ1.addActionListener(new AdaptateurCarte(
                    idCarte, boutonCarteJ1,
                    carteListJ1.get(idCarte),
                    this,
                    collecteurEv)
            );

            boutonCarteJ2.addActionListener(new AdaptateurCarte(
                    idCarte, boutonCarteJ2,
                    carteListJ2.get(idCarte),
                    this,
                    collecteurEv)
            );

            buttonsCartesJoueur1[idCarte] = boutonCarteJ1;
            buttonsCartesJoueur2[idCarte] = boutonCarteJ2;
        }

    }

    /** Crée les boutons "Annuler" et "Refaire" */
    private JPanel creerButtonsAnnulerRefaire() {
        JPanel boutonsAnnuleRefaire = new JPanel(new GridLayout(6, 1, 0, 10));
        boutonsAnnuleRefaire.setOpaque(false);

        annuler = new BoutonTerrain(PATH_BTN_ANNULER);
        refaire = new BoutonTerrain(PATH_BTN_REFAIRE);

        annuler.setPreferredSize(new Dimension(100,50));
        refaire.setPreferredSize(new Dimension(100,50));

//        annuler.setBackground(new Color(207, 207, 207, 44));
//        refaire.setBackground(new Color(207, 207, 207, 44));

//        annuler.setOpaque(true);
//        refaire.setOpaque(true);

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
        cartesNord.add(buttonsCartesJoueur1[0]);
        cartesNord.add(buttonsCartesJoueur1[1]);
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
        // Panel vertical contenant la carte, centré verticalement
        JPanel cartesEstbis = new JPanel(new GridLayout(3, 1, 0, 70));
        cartesEstbis.setOpaque(false);
        cartesEstbis.add(Box.createVerticalGlue());
        cartesEstbis.add(carteDeRotation);
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
        boutonSon = new JButton("son");
        boutonSon.setForeground(Color.WHITE);
        boutonSon.setFont(new Font("Arial", Font.PLAIN, 30));
        boutonSon.setBackground(new Color(237, 237, 237, 16));
        boutonSon.setContentAreaFilled(false);
        boutonSon.setFocusPainted(false);
        boutonSon.setText(musiqueActive ? "on" : "off");
        boutonSon.addActionListener(e -> toggleMusique(boutonSon));

        return boutonSon;
    }

    private JPanel creerPanelNomJoueurCourant()  {
        JPanel textNomPanel = new JPanel(); // Renommé
        textNomPanel.setLayout(new BoxLayout(textNomPanel, BoxLayout.Y_AXIS));
        textNomPanel.setOpaque(false);

        JLabel txt = new JLabel("C'est au tour de");
        txt.setFont(new Font("Arial", Font.PLAIN, 20));
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
        txt.setForeground(new Color(232, 231, 231));

        nomJoueurCourantLabel = new JLabel(jeu.getNomJoueurCourant());
        nomJoueurCourantLabel.setFont(new Font("Arial", Font.BOLD, 30));
        nomJoueurCourantLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Changer la couleur du texte du joueur courant pour qu'elle corresponde à sa couleur de pion
        nomJoueurCourantLabel.setForeground(infosDeConfigUI.getCouleurPionJoueur(jeu.getJoueurCourant().getId()));

        textNomPanel.add(txt);
        textNomPanel.add(Box.createRigidArea(new Dimension(0, 2)));
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

        numRound = jeu.getNumeroRound();
        roundLabel = new JLabel("Round: "+numRound);
        roundLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        roundLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));

        tempsLabel = new JLabel("00:00");
        tempsLabel.setOpaque(false);
        tempsLabel.setFont(new Font("Arial", Font.BOLD, 25));

        panel.add(roundLabel);
        panel.add(tempsLabel);

        return panel;
    }

    private JButton creerBoutonMenu() {
        JButton menu = new JButton("≡");
        menu.setOpaque(false);
        menu.setContentAreaFilled(false);
        menu.setFocusPainted(false);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Arial", Font.PLAIN, 30));
        menu.setPreferredSize(new Dimension(60, 40));
        menu.addActionListener(e -> interfaceGraphique.ouvrirMenu());

        return menu;
    }





    // =========================================
    // ========= Gestion Son & Musique =========
    // =========================================

    private void toggleMusique(JButton bouton) {
        if (musiqueActive) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
            bouton.setText("off");
        } else {
            // Jouer la musique si elle n'est pas déjà en cours
            if (clip == null || !clip.isRunning()) {
                jouerMusique(PATH_SON_1.toString());
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
            logger.severe("Erreur lors de la lecture audio: " + e.getMessage()); // Utiliser System.err pour les erreurs ou un logger
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

    // Mise à jour du temps (appelée par le Timer)
    private void miseAjourTemps() {
        if (debutTempsPartie != null && tempsLabel != null) { // Vérifier si les attributs sont initialisés
            Duration duration = Duration.between(debutTempsPartie, Instant.now());
            long minutes = duration.toMinutes();
            long secondes = duration.getSeconds() % 60;
            tempsLabel.setText(String.format("%02d:%02d", minutes, secondes));
        }
    }


    // Met à jour l'affichage du terrain en fonction de l'État du jeu
    private void updateTerrain() {
            terrain.removeAll();
            initBoutonsTerrain();
            terrain.revalidate();
            terrain.repaint();
    }


    // Met à jour l'affichage des cartes du joueur courant
    private void updateCartes() {
        cartesNord.removeAll();
        cartesEst.removeAll();
        cartesSud.removeAll();

        List<Carte> carteListJ1 = jeu.getCartesJoueur1();
        List<Carte> carteListJ2 = jeu.getCartesJoueur2();
        for (int idCarte = 0; idCarte < NOMBRE_CARTES_MAIN; idCarte++) {
            BoutonCarte boutonCarteJ1 = new BoutonCarte(getCheminImageCarte(carteListJ1.get(idCarte)));
            BoutonCarte boutonCarteJ2 = new BoutonCarte(getCheminImageCarte(carteListJ2.get(idCarte)));
//            configurerBoutonCarte(boutonCarte, carte );
            boutonCarteJ1.addActionListener(new AdaptateurCarte(
                    idCarte, boutonCarteJ1,
                    carteListJ1.get(idCarte),
                    this,
                    collecteurEv)
            );

            boutonCarteJ2.addActionListener(new AdaptateurCarte(
                    idCarte, boutonCarteJ2,
                    carteListJ2.get(idCarte),
                    this,
                    collecteurEv)
            );

            buttonsCartesJoueur1[idCarte] = boutonCarteJ1;
            buttonsCartesJoueur2[idCarte] = boutonCarteJ2;
        }

        carteDeRotation = new BoutonCarte(getCheminImageCarte(jeu.getCarteSupplementaire()));
        carteDeRotation.addActionListener(new AdaptateurCarte(
                0, carteDeRotation,
                jeu.getCarteSupplementaire(),
                this,
                collecteurEv)
        );

        creerCartesNord();
        creerCarteGauche();
        creerCartesSud();
//        cartesNord.revalidate();
//        cartesEst.revalidate();
//        cartesSud.revalidate();
//        cartesNord.repaint();
//        cartesEst.repaint();
//        cartesSud.repaint();
    }


    // Met à jour l'affichage du joueur courant et du numéro de round
    private void updatePlayerAndRoundInfo() {
        if (jeu != null && nomJoueurCourantLabel != null && roundLabel != null) {
            String nomJoueur = jeu.getJoueurCourant().getNom();
            nomJoueurCourantLabel.setText(nomJoueur);

            numRound = jeu.getNumeroRound();
            roundLabel.setText("Round: " + numRound);

            // Changer la couleur du texte du joueur courant pour qu'elle corresponde à sa couleur de pion
             nomJoueurCourantLabel.setForeground(infosDeConfigUI.getCouleurPionJoueur(jeu.getJoueurCourant().getId()));
        }
    }

    // Met à jour l'état des boutons Annuler/Refaire
    private void updateUndoRedoButtons() {
        if (jeu != null && annuler != null && refaire != null) {
            annuler.setEnabled(jeu.peutAnnulerCoup());
            refaire.setEnabled(jeu.peutRefaireCoup());
        }
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
