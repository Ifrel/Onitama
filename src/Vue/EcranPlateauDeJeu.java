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

        setLayout(new GridBagLayout()); // Garde votre layout actuel
        setBackground(COULEUR_PLATEAU_DE_JEU);

        initialiserInterface();

        // Démarrer la musique au début si souhaité
        // toggleMusique(this.boutonSon); // Peut-être démarrer par défaut ou attendre action utilisateur
        // === Initialiser l'affichage une première fois ===
        miseAJour(); // Appeler miseAJour() après l'initialisation pour afficher l'état initial
    }

    

    private void initialiserInterface() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(50, 50, 50, 50);

        // 1. Création des composants
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


        // 2. Placement des composants via GridBagLayout

//        // Bouton Son
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 2;
//        gbc.gridheight = 2;
////        gbc.anchor = GridBagConstraints.NORTHWEST;
//        gbc.fill = GridBagConstraints.NONE;
//        add(boutonSon, gbc);
//
//        // Contenu centre
//        gbc.gridx = 2;
//        gbc.gridwidth = 2;
//        gbc.gridheight = 1;
//        gbc.weightx = 0;
////        gbc.anchor = GridBagConstraints.NORTH;
//        gbc.fill = GridBagConstraints.NONE;
//        add(contenuCentrePanel, gbc);
//
//        // Panel Round/Temps
//        gbc.gridx = 6;
//        gbc.gridwidth = 2;
////        gbc.anchor = GridBagConstraints.NORTHEAST;
//        gbc.fill = GridBagConstraints.NONE;
//        gbc.weightx = 0;
//        add(roundTempsPanel, gbc);
//
//        // Bouton Menu
//        gbc.gridx = 8;
//        gbc.gridwidth = 2;
//        add(boutonMenu, gbc);

        // Cartes Nord
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 6;
        gbc.gridheight = 2;
        gbc.weightx = 0.2;
        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.CENTER;
//        gbc.fill = GridBagConstraints.BOTH;
//        add(cartesNordPanel, gbc);

        // Carte Gauche
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.gridheight = 6;
        gbc.weightx = 0.5;
        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.EAST;
//        gbc.fill = GridBagConstraints.NONE;
//        add(carteGauchePanel, gbc);

        // Terrain
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 0;
        gbc.gridheight = 0;
//        gbc.weightx = 0.2;
//        gbc.weighty = 1;
        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(terrain, gbc);

        // Panel Annuler/Refaire
        gbc.gridx = 8;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.gridheight = 6;
        gbc.weightx = 0.5;
        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
//        gbc.fill = GridBagConstraints.NONE;
//        add(annulerRefairePanel, gbc);

        // Cartes Sud
        gbc.gridx = 2;
        gbc.gridy = 15;
        gbc.gridwidth = 6;
        gbc.gridheight = 5;
        gbc.weightx = 0.2;
        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.NORTH;
//        gbc.fill = GridBagConstraints.NONE;
//        add(cartesSudPanel, gbc);

        // 3. Démarrage du timer
        debutTempsPartie = Instant.now();
        timerPartie = new Timer(1000, e -> miseAjourTemps());
        timerPartie.start();
    }




    /**
     * Initialise l'ensemble de l'interface utilisateur avec GridBagLayout.
     */
    /*
    private void initialiserInterface() {
        // Utilisation de GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


        // 1. Création des composants
        creerTerrain();
        creerButtonsCartes();
        creerButtonsAnnulerRefaire();
        boutonSon = creerBoutonSon();                       // Stocke la référence du bouton son
        boutonMenu = creerBoutonMenu();                     // Stocke la référence du bouton menu
        JPanel roundTempsPanel = creerPanelRoundTemps();    // Crée le panel round/temps
        JPanel contenuCentrePanel = creerContenuCentre();   // Crée le contenu central
        JPanel cartesNordPanel = creerCartesNord();         // Crée le panel des cartes Nord
        JPanel carteGauchePanel = creerCarteGauche();       // Crée le panel de la carte Gauche
        JPanel cartesSudPanel = creerCartesSud();           // Crée le panel des cartes Sud


        // 2. Placement des composants via GridBagLayout
        // --- Ligne du haut (Son, Infos Centre, Round/Temps, Menu) - Sans espace vide ---
        // Configuration des contraintes par défaut pour tous les composants
        gbc.insets = new Insets(50, 50, 50, 50); // Marge entre les composant
        gbc.gridy = 0; // Positionnement sur la première ligne (index 0)
        gbc.fill = GridBagConstraints.VERTICAL; // Les composants ne s'étirent pas par défaut


        // Bouton Son : Ancré à gauche
        gbc.gridx = 0;      // Commence à la colonne 0
        gbc.gridwidth = 2;  // Occupe 2 colonnes
        gbc.gridheight = 2; // Peut occuper 2 lignes (si le bouton est plus grand)
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(boutonSon, gbc);
        // réinitialisation
        gbc.gridheight = 1;
//        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;


        // Contenu Centre (Infos) : Positionné dans l'espace restant, prend l'espace principal, décalé par son point de départ.
        gbc.gridx = 2;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        add(contenuCentrePanel, gbc);
        // réinitialisation
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
//        gbc.fill = GridBagConstraints.NONE;


        // Panel Round/Temps : Ancré à droite, partie gauche du groupe de droite.
        gbc.gridx = 6; // Commence à la colonne 6. Ajustez ce nombre si nécessaire pour positionner le groupe de droite.
        gbc.gridwidth = 2; // Occupe 2 colonnes
        gbc.weightx = 0; // **Ce composant ne prend pas d'espace supplémentaire**
        gbc.anchor = GridBagConstraints.NORTHEAST;
        add(roundTempsPanel, gbc);




        // Bouton Menu : Ancré à droite, partie droite du groupe de droite.
        gbc.gridx = 8; // Commence juste après le panel Round/Temps (colonne 8)
        gbc.gridwidth = 2; // Occupe 2 colonnes
        gbc.weightx = 0; // **Ce composant ne prend pas d'espace supplémentaire**
        gbc.anchor = GridBagConstraints.NORTHEAST;
        add(boutonMenu, gbc);
        // réinitialisation
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;




        gbc = new GridBagConstraints();
        gbc.insets = new Insets(50, 50, 50, 50); // Marge entre les composant

        //  (Cartes Nord, Terrain, etc.)
        // --- Ligne suivante (Cartes Nord) ---
        gbc.gridy = 4; // Deuxième ligne (après le bouton Son et la première ligne)
        gbc.gridx = 2; // Centré approximativement sous les infos/round/temps
        gbc.gridwidth = 6; // Occupe une largeur significative
        gbc.gridheight = 2; // Occupe 2 lignes
        add(cartesNordPanel, gbc);

        // Rétablir la hauteur par défaut
        gbc.gridheight = 1;

        // --- Ligne du milieu (Carte Gauche, Terrain, Annuler/Refaire Droite) ---
        gbc.gridy = 8; // Ligne du milieu

        // Carte Gauche
        gbc.gridx = 0; // Colonne 0
        gbc.gridwidth = 2;
        gbc.gridheight = 4; // Occupe plus de hauteur
        add(carteGauchePanel, gbc);

        // Rétablir la hauteur et largeur par défaut
        gbc.gridheight = 1;
        gbc.gridwidth = 1;


        // Terrain de jeu (élément central et principal)
        gbc.gridx = 2; // Colonne 2
        gbc.gridwidth = 6; // Grande largeur
        gbc.gridheight = 6; // Grande hauteur
        add(terrain, gbc); // Assurez-vous que 'terrain' est bien le JPanel

        // Rétablir les valeurs par défaut pour les prochains composants
        gbc.gridheight = 1;
        gbc.gridwidth = 1;


        // Panel Annuler/Refaire
        gbc.gridx = 8; // Colonne 8 (à droite du terrain)
        gbc.gridwidth = 2;
        gbc.gridheight = 4; // Occupe la même hauteur que le terrain
        add(annulerRefairePanel, gbc); // Assurez-vous que 'annulerRefairePanel' est bien le JPanel

        // Rétablir la hauteur et largeur par défaut
        gbc.gridheight = 1;
        gbc.gridwidth = 1;


        // --- Ligne du bas (Cartes Sud) ---
        gbc.gridy = 15; // Ligne après le terrain, carte gauche et annuler/refaire
        gbc.gridx = 2; // Centré sous le terrain
        gbc.gridwidth = 6; // Occupe une largeur significative
        gbc.gridheight = 2; // Occupe 2 lignes
        add(cartesSudPanel, gbc);

        // 3. Démarrage du timer
        // Démarrer le timer après que les labels tempsLabel et roundLabel (supposés être dans roundTempsPanel) soient créés
        // === Gestion du Timer : Démarrage ===
        debutTempsPartie = Instant.now(); // Assurez-vous que debutTempsPartie est une variable d'instance Instant
        timerPartie = new Timer(1000, e -> miseAjourTemps()); // Utilise la nouvelle méthode sans paramètre
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
        buttonsCartes = new JButton[NOMBRES_CARTES_PLATEAU]; // Supposons que NOMBRES_CARTES_PLATEAU = 5
        for (int i = 0; i < buttonsCartes.length; i++) {
            // On crée juste le bouton, l'image correcte sera mise dans updateCartes()
            JButton bouton = creerBoutonCarte(PATH_CARTE_DRAGON); // Utilisez null ou une image par défaut/vide
            bouton.addActionListener(new AdaptateurCarteUI(new CarteUI(bouton, i), collecteurEv));
            buttonsCartes[i] = bouton;
        }
    }


    /** Crée les boutons "Annuler" et "Refaire" */
    private void creerButtonsAnnulerRefaire() {
        annulerRefairePanel = new JPanel(new GridLayout(2, 1, 10, 10)); // Renommé
        annulerRefairePanel.setOpaque(false);

        annuler = creerBoutonAction("Annuler"); // Méthode statique
        refaire = creerBoutonAction("Refaire"); // Méthode statique

        annuler.addActionListener(new AdaptateurAnnuler(collecteurEv));
        refaire.addActionListener(new AdaptateurRefaire(collecteurEv));

        annulerRefairePanel.add(annuler);
        annulerRefairePanel.add(refaire);

        // L'état activé/désactivé sera géré dans updateUndoRedoButtons()
    }


    // --- Méthodes de création des conteneurs de layout spécifiques ---
    // Ces méthodes créent des JPanel pour regrouper d'autres composants,
    // à placer ensuite dans le GridBagLayout principal.

    private JPanel creerCartesNord() {
        JPanel cartes = new JPanel(new GridLayout(1, 2, 40, 0));
        cartes.setBorder(BorderFactory.createEmptyBorder(50, 400, 0, 400)); // Marge autour du groupe de cartes
        cartes.add(buttonsCartes[0]); // Assurez-vous que l'index correspond à la carte correcte
        cartes.add(buttonsCartes[1]); // Assurez-vous que l'index correspond à la carte correcte
        cartes.setOpaque(false);
        return cartes;
    }

    private JPanel creerCartesSud() {
        JPanel cartes = new JPanel(new GridLayout(1, 2, 40, 0));
        cartes.setBorder(BorderFactory.createEmptyBorder(0, 400, 50, 400)); // Marge autour du groupe de cartes
        cartes.add(buttonsCartes[2]); // Assurez-vous que l'index correspond à la carte correcte
        cartes.add(buttonsCartes[3]); // Assurez-vous que l'index correspond à la carte correcte
        cartes.setOpaque(false);
        return cartes;
    }

    private JPanel creerCarteGauche() {
        JPanel carte = new JPanel(new GridLayout(3, 1, 40, 40)); // 3 lignes, 1 colonne
        carte.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0)); // Marge à gauche
        // Utilisation de Box.createVerticalGlue() si le GridLayout se comporte comme attendu,
        // sinon un BoxLayout pourrait être plus approprié pour le Glue.
        carte.add(Box.createVerticalGlue());
        carte.add(buttonsCartes[4]); // Assurez-vous que l'index correspond à la carte correcte
        carte.add(Box.createVerticalGlue());
        carte.setOpaque(false);
        return carte;
    }

    private JPanel creerBoutonsDroite() {
        JPanel droite = new JPanel(new GridLayout(3, 1, 40, 40)); // 3 lignes, 1 colonne
        droite.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50)); // Marge à droite
        droite.add(Box.createVerticalGlue());
        droite.add(annulerRefairePanel); // Ajoute le panneau annuler/refaire
        droite.add(Box.createVerticalGlue());
        droite.setOpaque(false);
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
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
        txt.setForeground(new Color(232, 231, 231));

        nomJoueurCourantLabel = new JLabel("Nom Joueur"); // Initialisé ici, mis à jour dans miseAJour()
        nomJoueurCourantLabel.setFont(new Font("Arial", Font.BOLD, 28));
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
        panel.setBackground(new Color(245, 245, 245));

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















//package Vue;
//
//import Modele.Jeu;
//import Patterns.Observateur;
//import Vue.Adaptateurs.AdaptateurAnnuler;
//import Vue.Adaptateurs.AdaptateurBoutonTerrain;
//import Vue.Adaptateurs.AdaptateurCarteUI;
//import Vue.Adaptateurs.AdaptateurRefaire;
//import Vue.Annimations.BruitGrisAvecPointsPanel;
//
//import javax.sound.sampled.AudioInputStream;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.Clip;
//import javax.swing.*;
//import java.awt.*;
//import java.time.Duration;
//import java.time.Instant;
//import java.util.Objects;
//
//import static Global.Config.*;
//import static Vue.Utils.MethodsStaticsUtils.*;
//
///**
// * Classe représentant l'interface graphique principale du plateau de jeu.
// * Elle observe le modèle (Jeu) et met à jour l'affichage en fonction des événements.
// */
//public class EcranPlateauDeJeu extends BruitGrisAvecPointsPanel implements Observateur {
//
//    // ====== Attributs principaux ======
//    private final Jeu jeu;
//    private InterfaceGraphique interfaceGraphique;
//    private final CollecteurEvenements collecteurEv;
//
//    // MethodsStaticsUtils de l'interface
//    private JPanel terrain;
//    private JPanel terrainCartesAnnulerRefaire;
//    private JPanel annulerRefaire;
//    private JPanel barreIndication;
//
//    private JButton[][] buttonsTerrain;
//    private JButton[] buttonsCartes;
//    private JButton annuler, refaire;
//
//    private JLabel nomJoueurCourant;
//    private JLabel temps;
//    private int numRound;
//
//    // Gestion du son
//    private Clip clip;
//    private boolean musiqueActive = false; // État du son
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
//        System.err.println("Interface Plateau de jeu lancée");
//        setLayout(new BorderLayout());
//        setBackground(COULEUR_PLATEAU_DE_JEU);
//
////        jeu.ajouteObservateur(this);
//        initialiserInterface();
//    }
//
//    /** Initialise l'ensemble de l'interface utilisateur */
//    private void initialiserInterface() {
//        creerTerrain();
//        creerButtonsCartes();
//        creerButtonsAnnulerRefaire();
//        creerPlateauCartesAnnulerRefaire();
//        creerBarreIndication();
//
//        add(barreIndication, BorderLayout.NORTH);
//        add(terrainCartesAnnulerRefaire, BorderLayout.CENTER);
//    }
//
//
//    @Override
//    public void miseAJour() {
//        // TODO : mettre à jour l'affichage selon les changements du modèle (jeu)
//    }
//
//
//
//    // =========================================
//    // ============ Création UI ================
//    // =========================================
//
//    /** Crée la grille du terrain de jeu */
//    private void creerTerrain() {
//        terrain = new JPanel(new GridLayout(LIGNES, COLONNES, 0, 0));
//        buttonsTerrain = new JButton[LIGNES][COLONNES];
//
//        terrain.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(new Color(206, 206, 206), 5, true),
//                BorderFactory.createEmptyBorder(15, 15, 15, 15)
//        ));
//        terrain.setBackground(new Color(226, 226, 226));
//
//        for (int row = 0; row < LIGNES; row++) {
//            for (int col = 0; col < COLONNES; col++) {
//                JButton bouton = creerBoutonTerrain();
//                bouton.addActionListener(new AdaptateurBoutonTerrain(bouton, new Point(row, col), collecteurEv));
//                buttonsTerrain[row][col] = bouton;
//                terrain.add(bouton);
//            }
//        }
//    }
//
//
//
//    /** Crée les boutons représentant les cartes */
//    private void creerButtonsCartes() {
//        buttonsCartes = new JButton[NOMBRES_CARTES_PLATEAU];
//        for (int i = 0; i < buttonsCartes.length; i++) {
//            JButton bouton = creerBoutonCarte("res/vue/images/cartes/TIGRE.png");
//            bouton.addActionListener(new AdaptateurCarteUI(new CarteUI(bouton, i), collecteurEv));
//            bouton.setPreferredSize(new Dimension(200, 100));
//            buttonsCartes[i] = bouton;
//        }
//    }
//
//
//
//    /** Crée les boutons "Annuler" et "Refaire" */
//    private void creerButtonsAnnulerRefaire() {
//        annulerRefaire = new JPanel(new GridLayout(2, 1, 10, 10));
//        annulerRefaire.setOpaque(false);
//
//        annuler = creerBoutonAction("Annuler");
//        refaire = creerBoutonAction("Refaire");
//
//        annuler.addActionListener(new AdaptateurAnnuler(collecteurEv));
//        refaire.addActionListener(new AdaptateurRefaire(collecteurEv));
//
//        annulerRefaire.add(annuler);
//        annulerRefaire.add(refaire);
//    }
//
//
//
//
//    /** Assemble le terrain, les cartes, et les boutons Annuler/Refaire */
//    private void creerPlateauCartesAnnulerRefaire() {
//        terrainCartesAnnulerRefaire = new JPanel(new BorderLayout(80, 40));
//        terrainCartesAnnulerRefaire.setOpaque(false);
//
//        terrainCartesAnnulerRefaire.add(creerCartesNord(), BorderLayout.NORTH);
//        terrainCartesAnnulerRefaire.add(creerCartesSud(), BorderLayout.SOUTH);
//        terrainCartesAnnulerRefaire.add(creerCarteGauche(), BorderLayout.WEST);
//        terrainCartesAnnulerRefaire.add(creerBoutonsDroite(), BorderLayout.EAST);
//        terrainCartesAnnulerRefaire.add(terrain, BorderLayout.CENTER);
//    }
//
//
//
//
//    /** Crée la barre supérieure d'indications */
//    private void creerBarreIndication() {
//        barreIndication = new JPanel(new BorderLayout());
//        barreIndication.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
//        barreIndication.setOpaque(false);
//
//        barreIndication.add(creerBoutonSon(), BorderLayout.WEST);
//        barreIndication.add(creerContenuCentre(), BorderLayout.CENTER);
//        barreIndication.add(creerBoutonMenu(this), BorderLayout.EAST);
//    }
//
//
//
//    /** Sous-méthodes de création d'éléments */
//    private JPanel creerCartesNord() {
//        JPanel cartes = new JPanel(new GridLayout(1, 2, 40, 0));
//        cartes.setBorder(BorderFactory.createEmptyBorder(50, 400, 0, 400));
//        cartes.add(buttonsCartes[0]);
//        cartes.add(buttonsCartes[1]);
//        cartes.setOpaque(false);
//        return cartes;
//    }
//
//
//    /** Sous-méthodes de création d'éléments */
//    private JPanel creerCartesSud() {
//        JPanel cartes = new JPanel(new GridLayout(1, 2, 40, 0));
//        cartes.setBorder(BorderFactory.createEmptyBorder(0, 400, 50, 400));
//        cartes.add(buttonsCartes[2]);
//        cartes.add(buttonsCartes[3]);
//        cartes.setOpaque(false);
//        return cartes;
//    }
//
//
//    /** Sous-méthodes de création d'éléments */
//    private JPanel creerCarteGauche() {
//        JPanel carte = new JPanel(new GridLayout(3, 1, 40, 40));
//        carte.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
//        carte.add(Box.createVerticalGlue());
//        carte.add(buttonsCartes[4]);
//        carte.add(Box.createVerticalGlue());
//        carte.setOpaque(false);
//        return carte;
//    }
//
//
//
//    /** Sous-méthodes de création d'éléments */
//    private JPanel creerBoutonsDroite() {
//        JPanel droite = new JPanel(new GridLayout(3, 1, 40, 40));
//        droite.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));
//        droite.add(Box.createVerticalGlue());
//        droite.add(annulerRefaire);
//        droite.add(Box.createVerticalGlue());
//        droite.setOpaque(false);
//        return droite;
//    }
//
//
//    /** Sous-méthodes de création d'éléments */
//    private JButton creerBoutonSon() {
//        JButton boutonSon = new JButton("son");
//        boutonSon.setForeground(Color.WHITE);
//        boutonSon.setFont(new Font("Arial", Font.PLAIN, 15));
//        boutonSon.setBackground(new Color(237, 237, 237, 16));
//        boutonSon.setContentAreaFilled(false);
//        boutonSon.setFocusPainted(false);
//        boutonSon.setPreferredSize(new Dimension(60, 40));
//        boutonSon.addActionListener(e -> toggleMusique(boutonSon));
//        return boutonSon;
//    }
//
//
//
//    /** Sous-méthodes de création d'éléments */
//    private JPanel creerContenuCentre() {
//        JPanel textNom = new JPanel();
//        textNom.setLayout(new BoxLayout(textNom, BoxLayout.Y_AXIS));
//        textNom.setOpaque(false);
//
//        JLabel txt = new JLabel("C'est au tour de");
//        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
//        txt.setForeground(new Color(232, 231, 231));
//
//        nomJoueurCourant = new JLabel("Kevin");
//        nomJoueurCourant.setFont(new Font("Arial", Font.BOLD, 28));
//        nomJoueurCourant.setAlignmentX(Component.CENTER_ALIGNMENT);
//        nomJoueurCourant.setForeground(new Color(218, 214, 214));
//
//        textNom.add(txt);
//        textNom.add(Box.createRigidArea(new Dimension(0, 5)));
//        textNom.add(nomJoueurCourant);
//
//        JPanel roundTemps = creerPanelRoundTemps();
//
//        JPanel contenu = new JPanel(new BorderLayout());
//        contenu.setOpaque(false);
//        contenu.add(textNom, BorderLayout.CENTER);
//        contenu.add(roundTemps, BorderLayout.EAST);
//
//        return contenu;
//    }
//
//
//    /** Sous-méthodes de création d'éléments */
//    private JPanel creerPanelRoundTemps() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
//        panel.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(new Color(206, 206, 206), 1, true),
//                BorderFactory.createEmptyBorder(10, 20, 10, 20)
//        ));
//        panel.setBackground(new Color(245, 245, 245));
//
//        numRound = 1;
//        JLabel round = new JLabel("Round: " + numRound);
//        round.setFont(new Font("Arial", Font.PLAIN, 25));
//        round.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));
//
//        temps = new JLabel("00:00");
//        temps.setFont(new Font("Arial", Font.BOLD, 25));
//
//        Instant debut = Instant.now();
//        Timer timer = new Timer(1000, e -> miseAjourTemps(debut));
//        timer.start();
//
//        panel.add(round);
//        panel.add(temps);
//        return panel;
//    }
//
//
//
//    private JPanel creerBoutonMenu(JPanel contentPane) {
//        JButton menu = new JButton("≡");
//        menu.setOpaque(false);
//        menu.setContentAreaFilled(false);
//        menu.setFocusPainted(false);
//        menu.setForeground(Color.WHITE);
//        menu.setFont(new Font("Arial", Font.PLAIN, 46));
//        menu.setPreferredSize(new Dimension(60, 40));
//
//        menu.addActionListener(e -> interfaceGraphique.ouvrirMenu());
//
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setOpaque(false);
//        panel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
//        panel.add(menu, BorderLayout.CENTER);
//        return panel;
//    }
//
//
//
//    // =========================================
//    // ========= Gestion Son & Musique =========
//    // =========================================
//
//    private void toggleMusique(JButton boutonSon) {
//        if (musiqueActive) {
//            if (clip != null && clip.isRunning()) {
//                clip.stop();
//            }
//            boutonSon.setText("off");
//        } else {
//            jouerMusique("/vue/musique/son_1.wav");
//            boutonSon.setText("on");
//        }
//        musiqueActive = !musiqueActive;
//    }
//
//    private void jouerMusique(String chemin) {
//        try {
//            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
//                    Objects.requireNonNull(getClass().getResource(chemin))
//            );
//            clip = AudioSystem.getClip();
//            clip.open(audioInputStream);
//            clip.loop(Clip.LOOP_CONTINUOUSLY);
//            clip.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    // =========================================
//    // ============== Mise à jour ==============
//    // =========================================
//
//    private void miseAjourTemps(Instant debut) {
//        Duration duration = Duration.between(debut, Instant.now());
//        long minutes = duration.toMinutes();
//        long secondes = duration.getSeconds() % 60;
//        temps.setText(String.format("%02d:%02d", minutes, secondes));
//    }
//}
