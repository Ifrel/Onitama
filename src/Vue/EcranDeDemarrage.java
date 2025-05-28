package Vue;

import Controleur.ControleurEcranDeDemarrage;
import Global.Config;
import Global.Paths;
import Modele.Jeu;
import Vue.Adaptateurs.AdaptateurBoutonEntrer;
import Vue.Utils.Boutons.Bouton;
import Vue.Utils.Boutons.Bouton.BoutonAvecImage;
import Vue.Utils.PanelAvecImage;
import Vue.Utils.PngText;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Global.Config.*;
import static Global.Config.CiblesDesCouleurs.*;
import static Global.Paths.*;
import static Vue.Configuration.ConfigUI.*;


/**
 EcranDeDemarrage : Écran de configuration du jeu utilisant JTabbedPane.
 Permet de définir le mode de jeu, les noms des joueurs, le niveau de l'IA, etc.
 Interagit avec le {@link ControleurEcranDeDemarrage} pour signaler les actions de l'utilisateur.
 */
public class EcranDeDemarrage extends JTabbedPane {
    // Ajoutez ces constantes en haut de la classe EcranDeDemarrage
    private static final double BUTTON_WIDTH_RATIO = 0.15;  // 15% de la largeur
    private static final double BUTTON_HEIGHT_RATIO = 0.1;  // 10% de la hauteur
    private static final double COMBOBOX_WIDTH_RATIO = 0.25; // 25% de la largeur
    private static final double TEXTFIELD_WIDTH_RATIO = 0.25; // 25% de la largeur
    private final Jeu jeu;
    private final ControleurEcranDeDemarrage CD;
    private final InterfaceGraphique interfaceGraphique;

    // Onglet Général
    private boolean estModeAutoIA;
    private BoutonAvecImage boutonModeAuto;
    private JComboBox<String> comboBoxPartie;
    private JComboBox<String> comboBoxNiveauIA;
    private JTextField champNomJoueur1;
    private JTextField champNomJoueur2;
    private String nomPartieSelectionnee;
    private String niveauIASelectionne;
    private final AdaptateurBoutonEntrer actionListenerEntree;

    // Constantes de Mise en Page et Style
    private static final int GRID_COLUMN_LABEL = 4;
    private static final String FONT_NAME_ARIAL = "Arial";
    private static final Font FONT_TITLE = new Font(FONT_NAME_ARIAL, Font.BOLD, 40);
    private static final Font FONT_LABEL = new Font(FONT_NAME_ARIAL, Font.PLAIN, 25);
    private static final Font FONT_COMPONENT = new Font(FONT_NAME_ARIAL, Font.PLAIN, 20);

    // Pour la réinitialisation des couleurs
    private final Map<CiblesDesCouleurs, Color> couleursInitiales = new HashMap<>();


    /**
     * Constructeur de l'écran de démarrage.
     * Initialise les composants UI et connecte le contrôleur.
     *
     * @param jeu Le modèle de jeu.
     * @param interfaceGraphique L'interface graphique principale.
     */
    public EcranDeDemarrage(Jeu jeu, InterfaceGraphique interfaceGraphique) {
        this.jeu = jeu;
        this.CD = new ControleurEcranDeDemarrage(jeu); // Instancie le contrôleur
        this.interfaceGraphique = interfaceGraphique;
        this.estModeAutoIA = false;
        this.actionListenerEntree = new AdaptateurBoutonEntrer(CD, interfaceGraphique);
        creerInterfaceUtilisateur();
    }


    /**
     * Crée et configure l'interface utilisateur de l'écran de démarrage,
     * y compris les onglets "Général" et "Couleur".
     */
    private void creerInterfaceUtilisateur() {
        // Ajout d'une bordure décorative autour du JTabbedPane
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLUE, 3),
                BorderFactory.createEmptyBorder(0, 10, 10, 10)
        ));

        // Créer et ajouter les onglets
        addTab(null, creerOngletGeneral());
        // Utilise un JPanel personnalisé pour le titre de l'onglet, permettant des images PNG
        setTabComponentAt(0, creerPanelTitreOnglet(TITRE_ONGLET_GENERAL));

        addTab(null, creerOngletCouleur());
        setTabComponentAt(1, creerPanelTitreOnglet(TITRE_ONGLET_COULEUR));
    }


    /**
     * Crée un JPanel avec un titre en format PNG, utilisé comme composant de l'onglet.
     *
     * @param titre Le texte du titre à afficher dans le PNG.
     * @return Un JPanel contenant le titre sous forme d'image PNG.     */
    private JPanel creerPanelTitreOnglet(String titre) {
        JPanel panelTitre = PngText.createPngPanel(titre, 25);
        panelTitre.setOpaque(false); // Rendre le panneau transparent
        return panelTitre;
    }


    /**
     * Crée l'onglet principal "Général" de configuration en utilisant GridBagLayout.
     * Cet onglet permet de configurer le mode de jeu, les noms des joueurs et le niveau de l'IA.
     *
     * @return Le JPanel de l'onglet Général.     */
    private JPanel creerOngletGeneral() {
        PanelAvecImage ongletGeneral = new PanelAvecImage(PATH_ARRIERE_PLAN_03);
        ongletGeneral.setLayout(new GridBagLayout());

        // Titre de l'onglet Général
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weighty = 0.2;
        gbc.gridx = GRID_COLUMN_LABEL;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.insets = MARGES_TITRE;
        JPanel titreOnglet = PngText.createPngPanel(LBL_TITRE_CONFIG, 50);
        titreOnglet.setOpaque(false);
        ongletGeneral.add(titreOnglet, gbc);

        int ligneCourante = 6;

        // Ligne 1 : Reprendre une partie
        List<String> listeParties = jeu.listerSauvegardes();
        listeParties.add(0, INDICATION_SELECTION);
        comboBoxPartie = creerListeDeroulanteAvecIndication(listeParties.toArray(new String[0]));
        comboBoxPartie.setPreferredSize(DIMENSION_CHAMP_LISTE_DEROULANTE);
        actionListenerEntree.setPartieSelectionnee(INDICATION_SELECTION);
        comboBoxPartie.addActionListener(e -> {
            int indexSelectionne = comboBoxPartie.getSelectedIndex();
            nomPartieSelectionnee = comboBoxPartie.getItemAt(indexSelectionne);
            actionListenerEntree.setPartieSelectionnee(nomPartieSelectionnee);

            // Gèle le champ du joueur 2 si une partie est sélectionnée pour être reprise
            champNomJoueur2.setEnabled(indexSelectionne == 0);
        });
        ajouterLigneConfiguration(ongletGeneral, LBL_REPRENDRE, comboBoxPartie, ligneCourante++, FONT_LABEL);


        // Ligne 2 : Mode Auto IA
        boutonModeAuto = Bouton.creerBouton(PATH_BTN_MODE_AUTO_OFF.toString(), Bouton.ConfigurationParDefaut.SansBordure_transparent);
        boutonModeAuto.setPreferredSize(new Dimension(125, 60));
        boutonModeAuto.addActionListener(e -> {
            estModeAutoIA = !estModeAutoIA;
            boutonModeAuto.changerImage(estModeAutoIA ? PATH_BTN_MODE_AUTO_ON.toString() : PATH_BTN_MODE_AUTO_OFF.toString());
            actionListenerEntree.setModeAutoIA(estModeAutoIA);

            // Geler/dégeler les autres options de configuration dans l'onglet général
            comboBoxPartie.setEnabled(!estModeAutoIA);
            comboBoxNiveauIA.setEnabled(!estModeAutoIA);
            champNomJoueur1.setEnabled(!estModeAutoIA);
            champNomJoueur2.setEnabled(!estModeAutoIA);
        });
        ajouterLigneConfiguration(ongletGeneral, LBL_MODE_AUTO, boutonModeAuto, ligneCourante++, FONT_LABEL);



        // Ligne 3 : Jouer avec l'IA
        comboBoxNiveauIA = creerListeDeroulanteAvecIndication(OPTIONS_IA);
        comboBoxNiveauIA.setPreferredSize(DIMENSION_CHAMP_LISTE_DEROULANTE);
        actionListenerEntree.setNiveauIAselectione(OPTION_IA_NON);
        comboBoxNiveauIA.addActionListener(e -> {
            niveauIASelectionne = comboBoxNiveauIA.getItemAt(comboBoxNiveauIA.getSelectedIndex());
            // Vérifie si la sélection n'est pas "Non" et n'est pas l'indication
            boolean iaActive = !niveauIASelectionne.equals(OPTION_IA_NON);
            actionListenerEntree.setNiveauIAselectione(niveauIASelectionne);

            // Gèle la saisie du nom du joueur 2 si l'IA est activée
            champNomJoueur2.setEnabled(!iaActive);
        });
        ajouterLigneConfiguration(ongletGeneral, LBL_JOUER_IA, comboBoxNiveauIA, ligneCourante++, FONT_LABEL);


        // Ligne 4 : Nom Joueur 1
        champNomJoueur1 = new JTextField(LARGEUR_CHAMP_TEXTE);
        champNomJoueur1.setBackground(new Color(255, 255, 255, 255));
        champNomJoueur1.setPreferredSize(DIMENSION_CHAMP_LISTE_DEROULANTE);
        champNomJoueur1.setFont(FONT_COMPONENT);
        champNomJoueur1.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updateChampJoueur1(); }
            public void removeUpdate(DocumentEvent e) { updateChampJoueur1(); }
            public void insertUpdate(DocumentEvent e) { updateChampJoueur1(); }
            private void updateChampJoueur1() {
                champNomJoueur1.setBorder(UIManager.getBorder("TextField.border"));
                actionListenerEntree.setChampJoueur(1, champNomJoueur1);
            }
        });
        // Préremplir avec un nom par défaut depuis le modèle
        champNomJoueur1.setText(jeu.getNomJoueur1());
        ajouterLigneConfiguration(ongletGeneral, LBL_JOUEUR_1, champNomJoueur1, ligneCourante++, FONT_LABEL);


        // Ligne 5 : Nom Joueur 2
        champNomJoueur2 = new JTextField(LARGEUR_CHAMP_TEXTE);
        champNomJoueur2.setBackground(new Color(255, 255, 255, 255)); // Fond opaque blanc
        champNomJoueur2.setPreferredSize(DIMENSION_CHAMP_LISTE_DEROULANTE);
        champNomJoueur2.setFont(FONT_COMPONENT);
        champNomJoueur2.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updateChampJoueur2(); }
            public void removeUpdate(DocumentEvent e) { updateChampJoueur2(); }
            public void insertUpdate(DocumentEvent e) { updateChampJoueur2(); }
            private void updateChampJoueur2() {
                champNomJoueur2.setBorder(UIManager.getBorder("TextField.border"));
                actionListenerEntree.setChampJoueur(2, champNomJoueur2);
            }
        });
        // Préremplir avec un nom par défaut depuis le modèle
        champNomJoueur2.setText(jeu.getNomJoueur2());
        ajouterLigneConfiguration(ongletGeneral, LBL_JOUEUR_2, champNomJoueur2, ligneCourante++, FONT_LABEL);


        // Bouton règles && Bouton Entrer
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelBoutons.setOpaque(false);

        // Bouton "Regles"
        BoutonAvecImage regles = Bouton.creerBouton(Paths.PATH_BTN.resolve("regles.png").toString(), Bouton.ConfigurationParDefaut.SansBordure_transparent);
        regles.setPreferredSize(new Dimension(200, 90));
        regles.setToolTipText("Voir les Règles du jeu");
        regles.addActionListener(e -> { CD.clavier("regles");});
        panelBoutons.add(regles);

        // Bouton "Entrer"
        BoutonAvecImage boutonEntrer = Bouton.creerBouton(PATH_BTN_ENTRER.toString(), Bouton.ConfigurationParDefaut.SansBordure_transparent);
        boutonEntrer.setPreferredSize(new Dimension(200, 98));
        boutonEntrer.addActionListener(actionListenerEntree);
        boutonEntrer.setToolTipText("Lancer le jeu");
        panelBoutons.add(boutonEntrer);

        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = ligneCourante;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        ongletGeneral.add(panelBoutons, gbc);

        // Espace Vertical Flexible en bas
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = ligneCourante;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.VERTICAL;
        ongletGeneral.add(Box.createVerticalGlue(), gbc);

        return ongletGeneral;
    }


    /**
     * Crée l'onglet de personnalisation des couleurs.
     * Permet à l'utilisateur de modifier les couleurs des différents éléments du jeu.
     *
     * @return Le JPanel de l'onglet Couleur.     */
    private JPanel creerOngletCouleur() {
        PanelAvecImage ongletCouleur = new PanelAvecImage(PATH_ARRIERE_PLAN_03);
        ongletCouleur.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        int ligneCourante = 0;

        // Titre de l'onglet Couleur
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = ligneCourante++;
        gbc.weighty = 0.5;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = MARGES_TITRE;
        JPanel titreOnglet = PngText.createPngPanel(LBL_TITRE_COULEUR, 45);
        titreOnglet.setOpaque(false);
        titreOnglet.setFont(FONT_TITLE);
        ongletCouleur.add(titreOnglet, gbc);

        // Ajouter les sélecteurs de couleur pour chaque cible
        ajouterLigneSecteurCouleur(ongletCouleur, LBL_PION_TERRAIN_JOUEUR_1, CD.getCouleurPionJoueur(ID_JOUEUR_1), ligneCourante++, PION_TERRAIN_JOUEUR_1);
        ajouterLigneSecteurCouleur(ongletCouleur, LBL_CASE_MAITRE_JOUEUR_1, CD.getCouleurCaseMaitreJoueur(ID_JOUEUR_1), ligneCourante++, CASE_MAITRE_JOUEUR_1);
        ajouterLigneSecteurCouleur(ongletCouleur, LBL_CASE_ELEVE_JOUEUR_1, CD.getCouleurCaseEleveJoueur(ID_JOUEUR_1), ligneCourante++, CASE_ELEVE_JOUEUR_1);
        ajouterLigneSecteurCouleur(ongletCouleur, LBL_PION_TERRAIN_JOUEUR_2, CD.getCouleurPionJoueur(ID_JOUEUR_2), ligneCourante++, PION_TERRAIN_JOUEUR_2);
        ajouterLigneSecteurCouleur(ongletCouleur, LBL_CASE_MAITRE_JOUEUR_2, CD.getCouleurCaseMaitreJoueur(ID_JOUEUR_2), ligneCourante++, CASE_MAITRE_JOUEUR_2);
        ajouterLigneSecteurCouleur(ongletCouleur, LBL_CASE_ELEVE_JOUEUR_2, CD.getCouleurCaseEleveJoueur(ID_JOUEUR_2), ligneCourante++, CASE_ELEVE_JOUEUR_2);


        // Panel pour les boutons en bas
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBoutons.setOpaque(false);

        // Bouton "Réinitialiser"
        BoutonAvecImage boutonReinitialiser = Bouton.creerBouton(
                Paths.PATH_BTN.resolve("button_reset_all.png").toString(),
                Bouton.ConfigurationParDefaut.Cercle_transparent);
        boutonReinitialiser.setPreferredSize(new Dimension(98, 98));
        boutonReinitialiser.setToolTipText("Réinitialiser toutes les couleurs");
        boutonReinitialiser.addActionListener(e -> {
            int choix = JOptionPane.showConfirmDialog(
                    EcranDeDemarrage.this,
                    "Voulez-vous vraiment réinitialiser toutes les couleurs ?",
                    "Confirmation de réinitialisation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choix == JOptionPane.YES_OPTION) {
                // Réinitialiser les couleurs
                CD.reinitialiserCouleurs();

                // Recharger l'onglet des couleurs
                removeTabAt(1);
                addTab(null, creerOngletCouleur());
                setTabComponentAt(1, creerPanelTitreOnglet(TITRE_ONGLET_COULEUR));

                JOptionPane.showMessageDialog(
                        EcranDeDemarrage.this,
                        "Les couleurs ont été réinitialisées avec succès.",
                        "Réinitialisation réussie",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        panelBoutons.add(boutonReinitialiser);

        // Bouton "Entrer"
        BoutonAvecImage boutonEntrer = Bouton.creerBouton(
                PATH_BTN_ENTRER.toString(),
                Bouton.ConfigurationParDefaut.SansBordure_transparent);
        boutonEntrer.setPreferredSize(new Dimension(200, 98));
        boutonEntrer.addActionListener(actionListenerEntree);
        panelBoutons.add(boutonEntrer);

        // Ajouter le panel des boutons
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = ligneCourante += 2;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        ongletCouleur.add(panelBoutons, gbc);

        // Espace Vertical Flexible en bas
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = ligneCourante + 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        ongletCouleur.add(Box.createVerticalGlue(), gbc);

        return ongletCouleur;
    }




    /**
     * Ajoute une ligne dans le panneau de l'onglet "Couleur" permettant la sélection d'une couleur.
     * Chaque ligne comprend une étiquette, un bouton de prévisualisation de couleur et un bouton de réinitialisation.
     *
     * @param panneau Le conteneur (JPanel) dans lequel ajouter les composants.
     * @param texteEtiquette Le texte affiché à gauche de la ligne (ex: "Plateau de jeu").
     * @param couleurInitiale La couleur initiale à afficher dans le bouton de prévisualisation.
     * @param ligne L'index de la ligne dans le GridBagLayout.
     * @param cible La cible de la configuration de couleur (énumération {@link CiblesDesCouleurs}).     */
    private void ajouterLigneSecteurCouleur(JPanel panneau, String texteEtiquette, Color couleurInitiale, int ligne, Config.CiblesDesCouleurs cible) {
        couleursInitiales.put(cible, couleurInitiale);

        // Étiquette descriptive
        JPanel etiquettePanel = PngText.createPngPanel(texteEtiquette, 20);
        etiquettePanel.setOpaque(false);
        etiquettePanel.setFont(FONT_LABEL);
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0;
        gbcLabel.gridy = ligne;
        gbcLabel.anchor = GridBagConstraints.LINE_END;
        gbcLabel.insets = MARGES_DEFAUT;
        gbcLabel.fill = GridBagConstraints.VERTICAL;
        gbcLabel.weightx = 0.5;
        gbcLabel.weighty = 0.25;
        panneau.add(etiquettePanel, gbcLabel);

        // Espace extensible entre l'étiquette et le bouton de couleur
        GridBagConstraints gbcSpace = new GridBagConstraints();
        gbcSpace.gridx = 1;
        gbcSpace.gridy = ligne;
        gbcSpace.fill = GridBagConstraints.BOTH;
        gbcSpace.weightx = 0.04;
        gbcSpace.weighty = 0.25;
        panneau.add(Box.createGlue(), gbcSpace);

        // Bouton de prévisualisation et sélection de couleur
        BoutonAvecImage boutonSelectionCouleur = Bouton.creerBouton("", Bouton.ConfigurationParDefaut.Carre_transparent);
        boutonSelectionCouleur.setPreferredSize(DIM_PREVIEW_COULEUR);
        boutonSelectionCouleur.chargerCouleurFont(couleurInitiale);
        boutonSelectionCouleur.setFocusPainted(true);
        boutonSelectionCouleur.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        if (cible == PION_TERRAIN_JOUEUR_1 || cible == PION_TERRAIN_JOUEUR_2) {
            JComboBox<String> choixCouleur = new JComboBox<>(new String[]{"noir", "rouge", "bleu"});
            choixCouleur.setFont(FONT_COMPONENT);
            choixCouleur.setBackground(new Color(255, 255, 255, 255));

            boutonSelectionCouleur.addActionListener(e -> {
                int result = JOptionPane.showConfirmDialog(
                        EcranDeDemarrage.this,
                        choixCouleur,
                        "Sélectionner une couleur pour " + texteEtiquette,
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {
                    Color couleurChoisie;
                    switch (choixCouleur.getSelectedItem().toString()) {
                        case "noir":
                            couleurChoisie = new Color(1, 1, 1);
                            break;
                        case "rouge":
                            couleurChoisie = new Color(200, 85, 27);
                            break;
                        case "bleu":
                            couleurChoisie = new Color(26, 67, 104);
                            break;
                        default:
                            return;
                    }
                    boutonSelectionCouleur.chargerCouleurFont(couleurChoisie);
                    CD.setCouleurPion(cible, choixCouleur.getSelectedItem().toString());
                }
            });
        } else {
            boutonSelectionCouleur.addActionListener(e -> {
                Color couleurChoisie = JColorChooser.showDialog(
                        EcranDeDemarrage.this,
                        "Sélectionner une couleur pour " + texteEtiquette,
                        boutonSelectionCouleur.getBackground()
                );
                if (couleurChoisie != null) {
                    boutonSelectionCouleur.chargerCouleurFont(couleurChoisie);
                    CD.setCouleur(cible, couleurChoisie);
                }
            });
        }
        GridBagConstraints gbcBouton = new GridBagConstraints();
        gbcBouton.gridx = 2;
        gbcBouton.gridy = ligne;
        gbcBouton.anchor = GridBagConstraints.LINE_START;
        gbcBouton.insets = MARGES_DEFAUT;
        gbcBouton.fill = GridBagConstraints.BOTH;
        gbcBouton.weightx = 0.05;
        gbcBouton.weighty = 0.01;
        panneau.add(boutonSelectionCouleur, gbcBouton);

        // Bouton de réinitialisation de la couleur
        BoutonAvecImage boutonReinitialiser = Bouton.creerBouton(Paths.PATH_BTN.resolve("button_annuler_rouge.png").toString(), Bouton.ConfigurationParDefaut.Carre_transparent);
        boutonReinitialiser.setPreferredSize(DIM_PREVIEW_COULEUR);
        boutonReinitialiser.setToolTipText("Réinitialiser à la couleur par défaut");
        boutonReinitialiser.addActionListener(e -> {
            Color couleurDefaut = couleursInitiales.get(cible);
            boutonSelectionCouleur.chargerCouleurFont(couleurDefaut);
            CD.setCouleur(cible, couleurDefaut);
        });

        GridBagConstraints gbcReset = new GridBagConstraints();
        gbcReset.gridx = 3;
        gbcReset.gridy = ligne;
        gbcReset.anchor = GridBagConstraints.LINE_START;
        gbcReset.insets = MARGES_DEFAUT;
        gbcReset.fill = GridBagConstraints.BOTH;
        gbcReset.weightx = 0.05;
        gbcReset.weighty = 0.01;
        panneau.add(boutonReinitialiser, gbcReset);

        // Espace Horizontal Flexible à droite
        GridBagConstraints gbcHorizontalGlue = new GridBagConstraints();
        gbcHorizontalGlue.gridx = 4;
        gbcHorizontalGlue.gridy = ligne;
        gbcHorizontalGlue.weightx = 0.55;
        gbcHorizontalGlue.fill = GridBagConstraints.HORIZONTAL;
        panneau.add(Box.createHorizontalGlue(), gbcHorizontalGlue);
    }


    /**
     * Méthode utilitaire pour ajouter une ligne (étiquette PNG + composant) au GridBagLayout
     * dans l'onglet de configuration générale.
     *
     * @param panneau Le JPanel utilisant GridBagLayout.
     * @param texteEtiquette Le texte pour l'étiquette PNG.
     * @param composant Le JComponent à ajouter (ex: JButton, JComboBox, JTextField).
     * @param ligne La valeur gridy pour cette ligne.
     * @param policeEtiquette La police (Font) pour l'étiquette (bien que l'étiquette soit un PNG, cela pourrait être utile pour d'autres types de labels).     */
    private void ajouterLigneConfiguration(JPanel panneau, String texteEtiquette, JComponent composant, int ligne, Font policeEtiquette) {
        // Contraintes de l'Étiquette
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = GRID_COLUMN_LABEL;
        gbcLabel.gridy = ligne;
        gbcLabel.anchor = GridBagConstraints.LINE_END;
        gbcLabel.insets = MARGES_DEFAUT;
        gbcLabel.weighty = 0.1;
        gbcLabel.ipadx = 40;

        JPanel etiquettePanel = PngText.createPngPanel(texteEtiquette, 30);
        etiquettePanel.setOpaque(false);
        panneau.add(etiquettePanel, gbcLabel);

        // --- Contraintes du Composant ---
        GridBagConstraints gbcComponent = new GridBagConstraints();
        gbcComponent.gridx = 5;
        gbcComponent.gridy = ligne;
        gbcComponent.anchor = GridBagConstraints.LINE_START;
        gbcComponent.weighty = 0.1;
        gbcComponent.insets = MARGES_DEFAUT;

        // Appliquer la police standard aux composants interactifs
        composant.setFont(FONT_COMPONENT);

        panneau.add(composant, gbcComponent);
    }


    /**
     * Crée une JComboBox avec une indication grisée comme premier élément.
     * L'indication est affichée en gris et redevient noire lors de la sélection d'une vraie option.
     *
     * @param options Les options pour la liste déroulante. Le premier élément est traité comme l'indication.
     * @return La JComboBox configurée.     */
    private JComboBox<String> creerListeDeroulanteAvecIndication(String[] options) {
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setFont(FONT_COMPONENT);
        comboBox.setPreferredSize(new Dimension(LARGEUR_LISTE_DEROULANTE, HAUTEUR_LISTE_DEROULANTE));

        // Rendre la JComboBox transparente visuellement (fond blanc opaque pour le texte)
        comboBox.setBackground(new Color(255, 255, 255, 255));
        comboBox.setForeground(new Color(7, 7, 7, 255));
        comboBox.setBorder(null);

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                // Griser le premier élément (l'indication) s'il n'est pas sélectionné et est l'élément actuellement affiché
                if (index == 0 && !isSelected && comboBox.getSelectedIndex() == 0) {
                    setForeground(Color.GRAY);
                } else {
                    setForeground(Color.BLACK);
                }
                return this;
            }
        });
        comboBox.setSelectedIndex(0);

        // Ajouter un écouteur d'action pour ajuster la couleur du texte une fois qu'un vrai choix est fait
        comboBox.addActionListener(e -> {
            if (comboBox.getSelectedIndex() != 0) {
                comboBox.setForeground(Color.BLACK);
            }
            // Forcer le rafraîchissement de la liste déroulante elle-même après sélection pour mettre à jour la couleur
            comboBox.repaint();
        });

        return comboBox;
    }


    /**
     * Crée un JLabel avec une image redimensionnée.
     * (Note: Cette méthode est moins utilisée maintenant que PngText.createPngPanel est préféré pour les labels PNG).
     *
     * @param cheminImage Le chemin vers l'image.
     * @param width La largeur désirée de l'image.
     * @param height La hauteur désirée de l'image.
     * @return Un JLabel contenant l'image redimensionnée.     */
    private JLabel creerLabelAvecImage(Path cheminImage, int width, int height) {
        JLabel label = new JLabel();
        ImageIcon icon = new ImageIcon(cheminImage.toString());
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(image);
        label.setIcon(scaledIcon);
        return label;
    }
}