package Vue;

import Modele.Jeu;
import Patterns.Observateur;
import Vue.Adaptateurs.AdaptateurAnnuler;
import Vue.Adaptateurs.AdaptateurBoutonTerrain;
import Vue.Adaptateurs.AdaptateurCarte;
import Vue.Adaptateurs.AdaptateurRefaire;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import static Global.Config.*;
import static Vue.Utils.Button.*;

/**
 * Classe représentant l'interface graphique principale du plateau de jeu.
 * Elle observe le modèle (Jeu) et met à jour l'affichage en fonction des événements.
 */
public class PlateauDeJeu extends JPanel implements Observateur {

    // ====== Attributs principaux ======
    private final Jeu jeu;
    private InterfaceGraphique interfaceGraphique;
    private final CollecteurEvenements collecteurEv;

    // Composants de l'interface
    private JPanel terrain;
    private JPanel terrainCartesAnnulerRefaire;
    private JPanel annulerRefaire;
    private JPanel barreIndication;

    private JButton[][] buttonsTerrain;
    private JButton[] buttonsCartes;
    private JButton annuler, refaire;

    private JLabel nomJoueurCourant;
    private JLabel temps;
    private int numRound;

    // Gestion du son
    private Clip clip;
    private boolean musiqueActive = false; // État du son


    /**
     * Constructeur principal du PlateauDeJeu
     * @param jeu modèle de données observé
     * @param collecteurEv gestionnaire des événements
     * @param interfaceGraphique Scène principale
     */
    public PlateauDeJeu(Jeu jeu, CollecteurEvenements collecteurEv, InterfaceGraphique interfaceGraphique) {
        this.jeu = jeu;
        this.collecteurEv = collecteurEv;
        this.interfaceGraphique = interfaceGraphique;

        System.err.println("Interface Plateau de jeu lancée");
        setLayout(new BorderLayout());
        setBackground(COULEUR_PLATEAU);

        jeu.ajouteObservateur(this);
        initialiserInterface();
    }

    /** Initialise l'ensemble de l'interface utilisateur */
    private void initialiserInterface() {
        creerTerrain();
        creerButtonsCartes();
        creerButtonsAnnulerRefaire();
        creerPlateauCartesAnnulerRefaire();
        creerBarreIndication();

        add(barreIndication, BorderLayout.NORTH);
        add(terrainCartesAnnulerRefaire, BorderLayout.CENTER);
    }


    @Override
    public void miseAJour() {
        // TODO : mettre à jour l'affichage selon les changements du modèle (jeu)
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
                JButton bouton = creerBoutonPlateau();
                bouton.addActionListener(new AdaptateurBoutonTerrain(bouton, new Point(row, col), collecteurEv));
                buttonsTerrain[row][col] = bouton;
                terrain.add(bouton);
            }
        }
    }

    /** Crée les boutons représentant les cartes */
    private void creerButtonsCartes() {
        buttonsCartes = new JButton[NOMBRES_CARTES_PLATEAU];
        for (int i = 0; i < buttonsCartes.length; i++) {
            JButton bouton = creerBoutonCarte("res/vue/images/cartes/TIGRE.png");
            bouton.addActionListener(new AdaptateurCarte(new CarteUI(bouton, i), collecteurEv));
            bouton.setPreferredSize(new Dimension(200, 100));
            buttonsCartes[i] = bouton;
        }
    }

    /** Crée les boutons "Annuler" et "Refaire" */
    private void creerButtonsAnnulerRefaire() {
        annulerRefaire = new JPanel(new GridLayout(2, 1, 10, 10));
        annulerRefaire.setOpaque(false);

        annuler = creerBoutonAction("Annuler");
        refaire = creerBoutonAction("Refaire");

        annuler.addActionListener(new AdaptateurAnnuler(collecteurEv));
        refaire.addActionListener(new AdaptateurRefaire(collecteurEv));

        annulerRefaire.add(annuler);
        annulerRefaire.add(refaire);
    }

    /** Assemble le terrain, les cartes, et les boutons Annuler/Refaire */
    private void creerPlateauCartesAnnulerRefaire() {
        terrainCartesAnnulerRefaire = new JPanel(new BorderLayout(80, 40));
        terrainCartesAnnulerRefaire.setOpaque(false);

        terrainCartesAnnulerRefaire.add(creerCartesNord(), BorderLayout.NORTH);
        terrainCartesAnnulerRefaire.add(creerCartesSud(), BorderLayout.SOUTH);
        terrainCartesAnnulerRefaire.add(creerCarteGauche(), BorderLayout.WEST);
        terrainCartesAnnulerRefaire.add(creerBoutonsDroite(), BorderLayout.EAST);
        terrainCartesAnnulerRefaire.add(terrain, BorderLayout.CENTER);
    }

    /** Crée la barre supérieure d'indications */
    private void creerBarreIndication() {
        barreIndication = new JPanel(new BorderLayout());
        barreIndication.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        barreIndication.setOpaque(false);

        barreIndication.add(creerBoutonSon(), BorderLayout.WEST);
        barreIndication.add(creerContenuCentre(), BorderLayout.CENTER);
        barreIndication.add(creerBoutonMenu(this), BorderLayout.EAST);
    }

    // === Sous-méthodes de création d'éléments ===

    private JPanel creerCartesNord() {
        JPanel cartes = new JPanel(new GridLayout(1, 2, 40, 0));
        cartes.setBorder(BorderFactory.createEmptyBorder(50, 400, 0, 400));
        cartes.add(buttonsCartes[0]);
        cartes.add(buttonsCartes[1]);
        cartes.setOpaque(false);
        return cartes;
    }

    private JPanel creerCartesSud() {
        JPanel cartes = new JPanel(new GridLayout(1, 2, 40, 0));
        cartes.setBorder(BorderFactory.createEmptyBorder(0, 400, 50, 400));
        cartes.add(buttonsCartes[2]);
        cartes.add(buttonsCartes[3]);
        cartes.setOpaque(false);
        return cartes;
    }

    private JPanel creerCarteGauche() {
        JPanel carte = new JPanel(new GridLayout(3, 1, 40, 40));
        carte.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        carte.add(Box.createVerticalGlue());
        carte.add(buttonsCartes[4]);
        carte.add(Box.createVerticalGlue());
        carte.setOpaque(false);
        return carte;
    }

    private JPanel creerBoutonsDroite() {
        JPanel droite = new JPanel(new GridLayout(3, 1, 40, 40));
        droite.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));
        droite.add(Box.createVerticalGlue());
        droite.add(annulerRefaire);
        droite.add(Box.createVerticalGlue());
        droite.setOpaque(false);
        return droite;
    }

    private JButton creerBoutonSon() {
        JButton boutonSon = new JButton("son");
        boutonSon.setForeground(Color.WHITE);
        boutonSon.setFont(new Font("Arial", Font.PLAIN, 15));
        boutonSon.setBackground(new Color(237, 237, 237, 16));
        boutonSon.setContentAreaFilled(false);
        boutonSon.setFocusPainted(false);
        boutonSon.setPreferredSize(new Dimension(60, 40));
        boutonSon.addActionListener(e -> toggleMusique(boutonSon));
        return boutonSon;
    }

    private JPanel creerContenuCentre() {
        JPanel textNom = new JPanel();
        textNom.setLayout(new BoxLayout(textNom, BoxLayout.Y_AXIS));
        textNom.setOpaque(false);

        JLabel txt = new JLabel("C'est au tour de");
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
        txt.setForeground(new Color(232, 231, 231));

        nomJoueurCourant = new JLabel("Kevin");
        nomJoueurCourant.setFont(new Font("Arial", Font.BOLD, 28));
        nomJoueurCourant.setAlignmentX(Component.CENTER_ALIGNMENT);
        nomJoueurCourant.setForeground(new Color(218, 214, 214));

        textNom.add(txt);
        textNom.add(Box.createRigidArea(new Dimension(0, 5)));
        textNom.add(nomJoueurCourant);

        JPanel roundTemps = creerPanelRoundTemps();

        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setOpaque(false);
        contenu.add(textNom, BorderLayout.CENTER);
        contenu.add(roundTemps, BorderLayout.EAST);

        return contenu;
    }

    private JPanel creerPanelRoundTemps() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 206, 206), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        panel.setBackground(new Color(245, 245, 245));

        numRound = 1;
        JLabel round = new JLabel("Round: " + numRound);
        round.setFont(new Font("Arial", Font.PLAIN, 25));
        round.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));

        temps = new JLabel("00:00");
        temps.setFont(new Font("Arial", Font.BOLD, 25));

        Instant debut = Instant.now();
        Timer timer = new Timer(1000, e -> miseAjourTemps(debut));
        timer.start();

        panel.add(round);
        panel.add(temps);
        return panel;
    }

    private JPanel creerBoutonMenu(JPanel contentPane) {
        JButton menu = new JButton("≡");
        menu.setOpaque(false);
        menu.setContentAreaFilled(false);
        menu.setFocusPainted(false);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Arial", Font.PLAIN, 46));
        menu.setPreferredSize(new Dimension(60, 40));

        menu.addActionListener(e -> interfaceGraphique.ouvrirMenu());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
        panel.add(menu, BorderLayout.CENTER);
        return panel;
    }



    // =========================================
    // ========= Gestion Son & Musique =========
    // =========================================

    private void toggleMusique(JButton boutonSon) {
        if (musiqueActive) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
            boutonSon.setText("off");
        } else {
            jouerMusique("/vue/musique/son_1.wav");
            boutonSon.setText("on");
        }
        musiqueActive = !musiqueActive;
    }

    private void jouerMusique(String chemin) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    Objects.requireNonNull(getClass().getResource(chemin))
            );
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // =========================================
    // ============== Mise à jour ==============
    // =========================================

    private void miseAjourTemps(Instant debut) {
        Duration duration = Duration.between(debut, Instant.now());
        long minutes = duration.toMinutes();
        long secondes = duration.getSeconds() % 60;
        temps.setText(String.format("%02d:%02d", minutes, secondes));
    }
}
