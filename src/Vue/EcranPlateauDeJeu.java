package Vue;

import Modele.Jeu;
import Patterns.Observateur;
import Vue.Adaptateurs.AdaptateurAnnuler;
import Vue.Adaptateurs.AdaptateurBoutonTerrain;
import Vue.Adaptateurs.AdaptateurCarteUI;
import Vue.Adaptateurs.AdaptateurRefaire;
import Vue.Annimations.BruitGrisAvecPointsPanel;
import Modele.Carte; // Assurez-vous d'importer votre classe Carte si elle existe
import Modele.Pion; // Assurez-vous d'importer votre classe Pion si elle existe

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List; // Pour les listes de cartes/pions
import java.util.Objects;

import static Global.Config.*;
import static Vue.Utils.MethodsStaticsUtils.*; // Assurez-vous que ces méthodes existent et sont accessibles

/**
 * Classe représentant l'interface graphique principale du plateau de jeu.
 * Elle observe le modèle (Jeu) et met à jour l'affichage en fonction des événements.
 */
public class EcranPlateauDeJeu extends BruitGrisAvecPointsPanel implements Observateur {

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
    private JPanel boutonMenu; // Ajouté pour pouvoir y accéder si besoin

    private JLabel nomJoueurCourantLabel; // Renommé pour clarté
    private JLabel tempsLabel; // Renommé pour clarté
    private JLabel roundLabel; // Ajouté pour afficher le round
    private int numRound; // Géré par le modèle si possible

    // Gestion du son
    private Clip clip;
    private boolean musiqueActive = false; // État du son
    private final String cheminMusique = "/vue/musique/son_1.wav"; // Chemin de la musique


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

        // === CORRECTION CRITIQUE 1 : Enregistrer l'observateur ===
        jeu.ajouteObservateur(this);

        // Pour le debug, utiliser un logger si possible, sinon commenter pour la production
        // System.err.println("Interface Plateau de jeu lancée"); // À supprimer en production

        setLayout(new GridBagLayout()); // Garde votre layout actuel
        setBackground(COULEUR_PLATEAU_DE_JEU);

        initialiserInterface();

        // Démarrer la musique au début si souhaité
        // toggleMusique(this.boutonSon); // Peut-être démarrer par défaut ou attendre action utilisateur

        // === Initialiser l'affichage une première fois ===
        miseAJour(); // Appeler miseAJour() après l'initialisation pour afficher l'état initial
    }

    /** Initialise l'ensemble de l'interface utilisateur */
    private void initialiserInterface() {
        // Crée les composants
        creerTerrain();
        creerButtonsCartes();
        creerButtonsAnnulerRefaire();
        boutonSon = creerBoutonSon(); // Stocke la référence
        boutonMenu = creerBoutonMenu(); // Stocke la référence

        // Création et placement des composants via GridBagLayout
        GridBagConstraints contraintes = new GridBagConstraints();
        contraintes.fill = GridBagConstraints.BOTH; // Permet aux composants de s'étirer
        contraintes.insets = new Insets(5, 5, 5, 5); // Marge entre les composants

        // Ligne du haut (Son, Infos Centre, Menu)
        contraintes.gridy = 0;
        contraintes.gridx = 0;
        contraintes.weightx = 0.1; // Poids pour le bouton son
        contraintes.anchor = GridBagConstraints.NORTHWEST; // Ancrer en haut à gauche
        add(boutonSon, contraintes);

        contraintes.gridx = 1; // Colonne 1 à 7 (pour centrer)
        contraintes.gridwidth = 7; // Span sur plusieurs colonnes
        contraintes.weightx = 0.8; // Poids pour le contenu central
        contraintes.anchor = GridBagConstraints.CENTER; // Ancrer au centre
        add(creerContenuCentre(), contraintes);

        contraintes.gridx = 8; // Colonne 8 (pour le menu)
        contraintes.gridwidth = 1; // Réinitialiser le span
        contraintes.weightx = 0.1; // Poids pour le bouton menu
        contraintes.anchor = GridBagConstraints.NORTHEAST; // Ancrer en haut à droite
        add(boutonMenu, contraintes);

        // Ligne des cartes Nord (si applicable à votre layout)
        // Adapter les coordonnées gridx/gridy et le span gridwidth/gridheight à votre layout réel
        // L'utilisation de GridBagLayout ici est complexe et dépend fortement de votre design souhaité.
        // L'exemple ci-dessous est une adaptation TENTATIVE de votre structure, à ajuster.

        // Exemple d'adaptation pour votre structure de cartes/terrain
        // Supposons une grille conceptuelle:
        // [Son] [Infos Centre] [Temps/Round] [Menu]
        // [Carte N1]  [Carte N2]
        // [Carte G]   [Terrain]  [Annul/Refait]
        // [Carte S1]  [Carte S2]

        // Ligne 1 (Cartes Nord)
        contraintes = new GridBagConstraints();
        contraintes.fill = GridBagConstraints.NONE; // Ne pas étirer
        contraintes.insets = new Insets(10, 10, 10, 10); // Marge
        contraintes.gridy = 1;
        contraintes.gridx = 1; // Ajuster la colonne
        contraintes.gridwidth = 2; // Ajuster le span
        contraintes.anchor = GridBagConstraints.SOUTH; // Ancrer en bas de la cellule
        add(creerCartesNord(), contraintes);


        // Ligne 2 (Carte Gauche, Terrain, Annuler/Refaire Droite)
        contraintes = new GridBagConstraints();
        contraintes.fill = GridBagConstraints.NONE;
        contraintes.insets = new Insets(10, 10, 10, 10);
        contraintes.gridy = 2;

        contraintes.gridx = 0; // Colonne pour carte gauche
        contraintes.anchor = GridBagConstraints.EAST; // Ancrer à droite de la cellule
        add(creerCarteGauche(), contraintes);

        contraintes.gridx = 1; // Colonne pour le terrain (peut s'étirer)
        contraintes.gridwidth = 6; // Largeur du terrain
        contraintes.gridheight = 1; // Hauteur du terrain
        contraintes.weightx = 1.0; // Permettre au terrain de prendre de l'espace
        contraintes.weighty = 1.0; // Permettre au terrain de prendre de l'espace
        contraintes.fill = GridBagConstraints.BOTH; // Étirer le terrain
        contraintes.anchor = GridBagConstraints.CENTER; // Ancrer au centre
        add(terrain, contraintes); // Ajouter le JPanel terrain créé précédemment

        contraintes.gridx = 7; // Colonne pour annuler/refaire (peut s'étirer en hauteur)
        contraintes.gridwidth = 1; // Réinitialiser la largeur
        contraintes.gridheight = 1; // Réinitialiser la hauteur
        contraintes.weightx = 0; // Ne pas prendre d'espace horizontal supplémentaire
        contraintes.weighty = 0; // Ne pas prendre d'espace vertical supplémentaire (ou petit poids si besoin)
        contraintes.fill = GridBagConstraints.VERTICAL; // Étirer verticalement
        contraintes.anchor = GridBagConstraints.WEST; // Ancrer à gauche de la cellule
        add(annulerRefairePanel, contraintes); // Ajouter le JPanel annulerRefaire créé précédemment


        // Ligne 3 (Cartes Sud)
        contraintes = new GridBagConstraints();
        contraintes.fill = GridBagConstraints.NONE;
        contraintes.insets = new Insets(10, 10, 10, 10);
        contraintes.gridy = 3;
        contraintes.gridx = 1; // Ajuster la colonne
        contraintes.gridwidth = 2; // Ajuster le span
        contraintes.anchor = GridBagConstraints.NORTH; // Ancrer en haut de la cellule
        add(creerCartesSud(), contraintes);


        // Démarrer le timer après que les labels tempsLabel et roundLabel soient créés
        // === Gestion du Timer : Démarrage ===
        debutTempsPartie = Instant.now();
        timerPartie = new Timer(1000, e -> miseAjourTemps()); // Utilise la nouvelle méthode sans paramètre
        timerPartie.start();

        // Ajoutez un WindowListener à la fenêtre principale pour arrêter le timer et le son
        // lorsque l'application est fermée. Cela se fait généralement dans la classe
        // InterfaceGraphique qui gère le JFrame.
        // Exemple (à ajouter dans InterfaceGraphique où le JFrame est géré) :
         /*
         frame.addWindowListener(new java.awt.event.WindowAdapter() {
             @Override
             public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                 if (ecranPlateauDeJeu.timerPartie != null && ecranPlateauDeJeu.timerPartie.isRunning()) {
                     ecranPlateauDeJeu.timerPartie.stop();
                 }
                 if (ecranPlateauDeJeu.clip != null && ecranPlateauDeJeu.clip.isRunning()) {
                     ecranPlateauDeJeu.clip.stop();
                 }
                 // Autres nettoyages...
                 System.exit(0); // ou dispose();
             }
         });
         */


    }


    // === CORRECTION CRITIQUE 2 & 3 : Implémentation de miseAJour() ===
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
            JButton bouton = creerBoutonCarte(null); // Utilisez null ou une image par défaut/vide
            bouton.addActionListener(new AdaptateurCarteUI(new CarteUI(bouton, i), collecteurEv));
            bouton.setPreferredSize(new Dimension(200, 100)); // Ajustez la taille si besoin
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
        bouton.setFont(new Font("Arial", Font.PLAIN, 15));
        bouton.setBackground(new Color(237, 237, 237, 16));
        bouton.setContentAreaFilled(false);
        bouton.setFocusPainted(false);
        bouton.setPreferredSize(new Dimension(60, 40));
        // Texte initial "son" ou "off" si musique désactivée par défaut
        bouton.setText(musiqueActive ? "on" : "off");
        bouton.addActionListener(e -> toggleMusique(bouton));
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

        // Intègre le panel round/temps
        JPanel roundTempsPanel = creerPanelRoundTemps(); // Renommé

        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setOpaque(false);
        contenu.add(textNomPanel, BorderLayout.CENTER);
        // Assurez-vous que creerPanelRoundTemps() retourne un JPanel contenant les labels tempsLabel et roundLabel
        contenu.add(roundTempsPanel, BorderLayout.EAST); // Ou une autre position appropriée

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


    private JPanel creerBoutonMenu() {
        JButton menu = new JButton("≡");
        menu.setOpaque(false);
        menu.setContentAreaFilled(false);
        menu.setFocusPainted(false);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Arial", Font.PLAIN, 46)); // Grande taille pour le symbole
        menu.setPreferredSize(new Dimension(60, 40)); // Ajuster la taille si besoin

        menu.addActionListener(e -> interfaceGraphique.ouvrirMenu());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0)); // Marge à droite
        panel.add(menu, BorderLayout.CENTER);
        return panel;
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
            annuler.setEnabled(jeu.peutAnnuler()); // Méthode à implémenter
            refaire.setEnabled(jeu.peutRefaire()); // Méthode à implémenter
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

    // Le reste du code (main) peut rester tel quel pour un exemple conceptuel.
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
