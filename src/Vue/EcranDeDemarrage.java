package Vue;

import Controleur.ControleurEcranDeDemarrage;
import Global.Config;
import Modele.Jeu;
import Vue.Adaptateurs.AdaptateurBoutonEntrer;
import Vue.Utils.Boutons.Bouton;
import Vue.Utils.Boutons.Bouton.BoutonAvecImage;
import Vue.Utils.PanelBruitGris;
import Vue.Utils.JPanelAvecCouleurDebraille;
import Vue.Utils.PanelAvecImage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


import static Global.Config.*;
import static Global.Config.CiblesDesCouleurs.*;
import static Global.Paths.*;
import static Vue.ConfigUI.*;
import static Vue.Utils.MethodsStaticsUtils.*;


/**
 * EcranDeDemarrage : Écran de configuration du jeu utilisant JTabbedPane.
 * Permet de définir le mode de jeu, les noms des joueurs, le niveau de l'IA, etc.
 * Interagit avec le CollecteurEvenements pour signaler les actions de l'utilisateur.
 */
public class EcranDeDemarrage extends JTabbedPane {
    private final Jeu jeu;
    private final CollecteurEvenements collecteurEvenements;
    private final InterfaceGraphique interfaceGraphique;

    // Onglet General
    private boolean modeAutoIA;
    private JButton boutonModeAuto;
    private JComboBox<String> listeDeroulanteReprendre, listeDeroulanteIA;
    private JTextField champJoueur1, champJoueur2;
    private String partieSelectionnee, niveauIAselectione;
    private final AdaptateurBoutonEntrer adaptateurBoutonEntrer;

    InfosDeConfigUI infosDeConfigUI = InfosDeConfigUI.getInstance();

    // Constantes
    private final Color COLOR_ONGLET_IA_1 = new Color(127, 157, 172);
    private final Color COLOR_ONGLET_IA_2 = new Color(112, 112, 112);
    private final int COLONNE_ETIQUETTE = 4;
    private final String POLICE_1 = "Arial";
    private final Font FONT_TITRE = new Font(POLICE_1, Font.BOLD, 40);
    private final Font FONT_LABEL = new Font(POLICE_1, Font.PLAIN, 25);
    private final Font FONT_COMPOSANT = new Font(POLICE_1, Font.PLAIN, 20);
    // Pour la reinitialisation des couleurs
    private final Map<CiblesDesCouleurs, Color> couleursInitiales = new HashMap<>();


    public EcranDeDemarrage(Jeu jeu, InterfaceGraphique interfaceGraphique) {
        this.jeu = jeu;
        this.collecteurEvenements = new ControleurEcranDeDemarrage(jeu);
        this.interfaceGraphique = interfaceGraphique;
        this.modeAutoIA = false;
        this.adaptateurBoutonEntrer = new AdaptateurBoutonEntrer(collecteurEvenements, interfaceGraphique);
        creerInterface();
    }


    /** Création de l'Interface */
    private void creerInterface() {
        // l'apparence du JTabbedPane si désiré (par exemple, position des onglets)
        this.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        this.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        // Créer et ajouter les onglets
        addTab(null, creerOngletGeneral());
        setTabComponentAt(0, creerJPanel(TITRE_ONGLET_GENERAL));

        addTab(null, creerOngletIA());
        setTabComponentAt(1, creerJPanel(TITRE_ONGLET_IA));

        addTab(null, creerOngletCouleur());
        setTabComponentAt(2, creerJPanel(TITRE_ONGLET_COULEUR));

        addTab(null, creerOngletAnimation());
        setTabComponentAt(3, creerJPanel(TITRE_ONGLET_ANIMATION));

        addTab(null, creerOngletSon());
        setTabComponentAt(4, creerJPanel(TITRE_ONGLET_SON));
    }


    /**
     * Crée l'onglet principal "Général" de configuration en utilisant GridBagLayout.
     * @return Le JPanel de l'onglet Général.
     */
    private JPanel creerOngletGeneral() {
        PanelAvecImage ongletGeneral = new PanelAvecImage(PATH_ARRIERE_PLAN_ED_O2);
        ongletGeneral.setLayout(new GridBagLayout());

        // --- Titre ---
        GridBagConstraints contraintes = new GridBagConstraints();
        contraintes.fill = GridBagConstraints.VERTICAL;
        contraintes.weighty = 0.2;
        contraintes.gridx = COLONNE_ETIQUETTE;
        contraintes.gridy = 3;
        contraintes.gridwidth = 2;
        contraintes.insets = MARGES_TITRE;
        JLabel titre = new JLabel(LBL_TITRE_CONFIG);
        titre.setFont(FONT_TITRE);
        ongletGeneral.add(titre, contraintes);

        int ligneCourante = 6;


        // --- Ligne 1 : Mode Auto ---
        boutonModeAuto = creerBoutonAvecImage(PATH_BTN_MODE_AUTO_OFF.toString()); // État par défaut
        boutonModeAuto.setPreferredSize(new Dimension(62,35));
        boutonModeAuto.setFont(FONT_COMPOSANT);
        boutonModeAuto.addActionListener(e -> {
            modeAutoIA = ! modeAutoIA;
            ImageIcon iconOFF = new ImageIcon(PATH_BTN_MODE_AUTO_OFF.toString());
            ImageIcon iconON = new ImageIcon(PATH_BTN_MODE_AUTO_ON.toString());
            if (modeAutoIA) boutonModeAuto.setIcon(iconON);
            else  boutonModeAuto.setIcon(iconOFF);
            adaptateurBoutonEntrer.setModeAutoIA(modeAutoIA);

            // On gele les autres options de config dans l'onglet général
            listeDeroulanteReprendre.setEnabled(!modeAutoIA);
            listeDeroulanteIA.setEnabled(!modeAutoIA);
            champJoueur1.setEnabled(!modeAutoIA);
            champJoueur2.setEnabled(!modeAutoIA);
        });
        ajouterLigne(ongletGeneral, LBL_MODE_AUTO, boutonModeAuto, ligneCourante++, FONT_LABEL);


        // --- Ligne 2 : Reprendre une partie ---
        listeDeroulanteReprendre = creerListeDeroulanteAvecIndication(OPTIONS_REPRENDRE);
        listeDeroulanteReprendre.setPreferredSize(DIMENSION_CHAMP_LISTE_DEROULANTE);
        adaptateurBoutonEntrer.setPartieSelectionnee(INDICATION_SELECTION);
        listeDeroulanteReprendre.addActionListener(e -> {
            int indexSelectionne = listeDeroulanteReprendre.getSelectedIndex();
            partieSelectionnee = listeDeroulanteReprendre.getItemAt(indexSelectionne);
            adaptateurBoutonEntrer.setPartieSelectionnee(partieSelectionnee);

            //On gele le champ du joueur 2
            champJoueur2.setEnabled(indexSelectionne == 0);
        });
        ajouterLigne(ongletGeneral, LBL_REPRENDRE, listeDeroulanteReprendre, ligneCourante++, FONT_LABEL);


        // --- Ligne 3 : Jouer avec l'IA ---
        listeDeroulanteIA = creerListeDeroulanteAvecIndication(OPTIONS_IA);
        listeDeroulanteIA.setPreferredSize(DIMENSION_CHAMP_LISTE_DEROULANTE);
        adaptateurBoutonEntrer.setNiveauIAselectione(OPTION_IA_NON);
        listeDeroulanteIA.addActionListener(e -> {
            niveauIAselectione = listeDeroulanteIA.getItemAt(listeDeroulanteIA.getSelectedIndex());
            // Vérification que la sélection n'est pas "Non" et n'est pas l'indication
            boolean iaActive = !niveauIAselectione.equals(OPTION_IA_NON) ;
            adaptateurBoutonEntrer.setNiveauIAselectione(niveauIAselectione);

            // On gèle la saisie du nom du joueur 2
            champJoueur2.setEnabled(!iaActive);
        });
        ajouterLigne(ongletGeneral, LBL_JOUER_IA, listeDeroulanteIA, ligneCourante++, FONT_LABEL);


        // --- Ligne 4 : Nom Joueur 1 ---
        champJoueur1 = new JTextField(LARGEUR_CHAMP_TEXTE);
        champJoueur1.setFont(FONT_COMPOSANT);
        champJoueur1.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {update();} // Si le style du texte change
            public void removeUpdate(DocumentEvent e) {update(); }  // Si le texte est supprimer
            public void insertUpdate(DocumentEvent e) {update(); }  // Si un nouveau text est insérer
            private void update(){
                champJoueur1.setBorder(UIManager.getBorder("TextField.border"));
                adaptateurBoutonEntrer.setChampJoueur(1, champJoueur1);
            }
        });
        // Pré-remplir éventuellement avec un nom par défaut depuis modèle/config
        champJoueur1.setText(jeu.getNomJoueur1());
        ajouterLigne(ongletGeneral, LBL_JOUEUR_1, champJoueur1, ligneCourante++, FONT_LABEL);


        // --- Ligne 5 : Nom Joueur 2 ---
        champJoueur2 = new JTextField(LARGEUR_CHAMP_TEXTE);
        champJoueur2.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void insertUpdate(DocumentEvent e) { update(); }
            private void update(){
                champJoueur2.setBorder(UIManager.getBorder("TextField.border"));
                adaptateurBoutonEntrer.setChampJoueur(2, champJoueur2);
            }
        });
        // Pré-remplir éventuellement avec un nom par défaut depuis modèle/config
        champJoueur2.setText(jeu.getNomJoueur2());
        ajouterLigne(ongletGeneral, LBL_JOUEUR_2, champJoueur2, ligneCourante++, FONT_LABEL);

        // -- bouton enter
        BoutonAvecImage entrer = Bouton.creerBouton(PATH_BTN_ENTRER.toString(), Bouton.ConfigurationParDefaut.Rectangle_transparent);
        entrer.setPreferredSize(new Dimension(200, 98));
        contraintes = new GridBagConstraints();
        contraintes.gridx = 6;
        contraintes.gridy = ligneCourante ;
        contraintes.fill = GridBagConstraints.NONE;
        entrer.addActionListener(adaptateurBoutonEntrer);
        ongletGeneral.add(entrer, contraintes);

        contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante;
        contraintes.weighty = 0.5;
        contraintes.fill = GridBagConstraints.VERTICAL;
        ongletGeneral.add(Box.createVerticalGlue(), contraintes);

        return ongletGeneral;
    }


    /**
     * Crée l'onglet de configuration de l'IA.
     * @return Le JPanel de l'onglet IA.
     */
    private JPanel creerOngletIA() {
        JPanelAvecCouleurDebraille ongletIA = new JPanelAvecCouleurDebraille(COLOR_ONGLET_IA_1, COLOR_ONGLET_IA_2);
        ongletIA.setLayout(new GridBagLayout());
        GridBagConstraints contraintes;
        int ligneCourante = 0;

        // --- Titre ---
        contraintes = new GridBagConstraints();
        contraintes.gridx = COLONNE_ETIQUETTE;
        contraintes.gridy = ligneCourante++;
        contraintes.weighty = 0.5;
        contraintes.fill = GridBagConstraints.VERTICAL;
        contraintes.gridwidth = 4;
        contraintes.anchor = GridBagConstraints.CENTER;
        contraintes.insets = MARGES_TITRE;

        JLabel titre = new JLabel(LBL_TITRE_IA);
        titre.setFont(FONT_TITRE);
        ongletIA.add(titre, contraintes);

        // Temps de réflexion IA
        JSlider sliderTempsReflexion = new JSlider(100, 5000, 1000); // min, max, valeur initiale (en ms)
        sliderTempsReflexion.setMajorTickSpacing(1000);
        sliderTempsReflexion.setMinorTickSpacing(100);
        sliderTempsReflexion.setPaintTicks(true);
        sliderTempsReflexion.setPaintLabels(true); // Afficher les valeurs numériques majeures
        sliderTempsReflexion.setPreferredSize(new Dimension(300,50));
        sliderTempsReflexion.addChangeListener(e -> {
            if (!sliderTempsReflexion.getValueIsAdjusting()) { // Agir seulement quand on relâche le curseur
                collecteurEvenements.setIAReflexion(sliderTempsReflexion.getValue());
            }
        });
        //Préremplir avec la valeur actuelle du modèle/config si disponible
        sliderTempsReflexion.setValue(jeu.getConfigIAReflexion());
        ajouterLigne(ongletIA, LBL_TEMPS_REFLEXION, sliderTempsReflexion, ligneCourante++, FONT_LABEL);

        // --- Heuristique Avancée ---
        JCheckBox checkHeuristique = new JCheckBox();
        checkHeuristique.setFont(FONT_COMPOSANT);
        checkHeuristique.addActionListener(e -> {
            collecteurEvenements.setIAHeuristique(checkHeuristique.isSelected());
        });
        // Préremplir avec la valeur actuelle du modèle/config si disponible
         checkHeuristique.setSelected(jeu.getConfigIAHeuristique());
        ajouterLigne(ongletIA, LBL_HEURISTIQUE_AVANCEE, checkHeuristique, ligneCourante++, FONT_LABEL);


        // --- Choix Algorithme IA ---
        JComboBox<String> comboAlgoIA = new JComboBox<>(OPTIONS_ALGORITHME_IA);
        comboAlgoIA.setFont(FONT_COMPOSANT);
        comboAlgoIA.addActionListener(e -> {
            String selection = (String) comboAlgoIA.getSelectedItem();
            collecteurEvenements.setIAAlgorithme(selection);
        });
        // Préremplir avec la valeur actuelle du modèle/config si disponible
        comboAlgoIA.setSelectedItem(jeu.getConfigIAAlgorithme());
        ajouterLigne(ongletIA, LBL_ALGORITHME_IA, comboAlgoIA, ligneCourante++, FONT_LABEL);


        // --- Espace Vertical Flexible ---
        contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante;
        contraintes.weighty = 1.0;
        contraintes.fill = GridBagConstraints.VERTICAL;
        ongletIA.add(Box.createVerticalGlue(), contraintes);

        return ongletIA;
    }


    /**
     * Crée l'onglet de personnalisation des couleurs.
     * @return Le JPanel de l'onglet Couleur.
     */
    private JPanel creerOngletCouleur() {
        JPanel ongletCouleur = new JPanel(new GridBagLayout());
        GridBagConstraints contraintes;
        int ligneCourante = 0;

        // --- Titre ---
        contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante++;
        contraintes.weighty = 0.5;
        contraintes.gridwidth = 5;
        contraintes.anchor = GridBagConstraints.CENTER;
        contraintes.insets = MARGES_TITRE;
        JLabel titre = new JLabel(LBL_TITRE_COULEUR);
        titre.setFont(FONT_TITRE);
        ongletCouleur.add(titre, contraintes);

        // Ajouter les sélecteurs de couleur
        ajouterLigneCouleur(ongletCouleur, LBL_PLATEAU_DE_JEU, COULEUR_PLATEAU_DE_JEU, ligneCourante++, PLATEAU_DE_JEU);
        ajouterLigneCouleur(ongletCouleur, LBL_CASE_TERRAIN, COULEUR_CASE_TERRAIN, ligneCourante++, CASE_TERRAIN);
        ajouterLigneCouleur(ongletCouleur, LBL_CASE_MAITRE_JOUEUR_1,COULEUR_CASE_MAITRE_JOUEUR_1, ligneCourante++, CASE_MAITRE_JOUEUR_1);
        ajouterLigneCouleur(ongletCouleur, LBL_CASE_MAITRE_JOUEUR_2, COULEUR_CASE_MAITRE_JOUEUR_2, ligneCourante++, CASE_MAITRE_JOUEUR_2);
        ajouterLigneCouleur(ongletCouleur, LBL_CASE_ELEVE_JOUEUR_1, COULEUR_CASE_ELEVE_JOUEUR_1, ligneCourante++, CASE_ELEVE_JOUEUR_1);
        ajouterLigneCouleur(ongletCouleur, LBL_CASE_ELEVE_JOUEUR_2, COULEUR_CASE_ELEVE_JOUEUR_2, ligneCourante++, CASE_ELEVE_JOUEUR_2);
        ajouterLigneCouleur(ongletCouleur, LBL_BLOC_MENU, COULEUR_BLOC_MENU, ligneCourante++, BLOC_MENU);


        // --- Espace Vertical Flexible ---
        contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante;
        contraintes.weighty = 1.0;
        contraintes.fill = GridBagConstraints.VERTICAL;
        ongletCouleur.add(Box.createVerticalGlue(), contraintes);

        return ongletCouleur;
    }


    /**
     * Ajoute une ligne dans le panneau pour permettre la sélection d'une couleur.
     * *
     * @param panneau         Le conteneur dans lequel ajouter les composants.
     * @param texteEtiquette  Le texte affiché à gauche de la ligne (ex: "Plateau de jeu").
     * @param couleurInitiale La couleur initiale à afficher dans le bouton de prévisualisation.
     * @param ligne           L'index de la ligne dans le GridBagLayout.
     * @param cible           La cible de la configuration de couleur.
     */
    private void ajouterLigneCouleur(JPanel panneau, String texteEtiquette, Color couleurInitiale, int ligne, Config.CiblesDesCouleurs cible) {
        couleursInitiales.put(cible, couleurInitiale); // Enregistre la couleur de départ

        // Étiquette descriptive
        JLabel etiquette = new JLabel(texteEtiquette);
        etiquette.setFont(FONT_LABEL);
        GridBagConstraints contraintesLabel = new GridBagConstraints();
        contraintesLabel.gridx = 0;
        contraintesLabel.gridy = ligne;
        contraintesLabel.anchor = GridBagConstraints.LINE_END;
        contraintesLabel.insets = MARGES_DEFAUT;
        contraintesLabel.fill = GridBagConstraints.VERTICAL;
        contraintesLabel.weightx = 0.5;
        contraintesLabel.weighty = 0.25;
        panneau.add(etiquette, contraintesLabel);

        // Espace extensible
        GridBagConstraints contraintesEspace = new GridBagConstraints();
        contraintesEspace.gridx = 1;
        contraintesEspace.gridy = ligne;
        contraintesEspace.fill = GridBagConstraints.BOTH;
        contraintesEspace.weightx = 0.04;
        contraintesEspace.weighty = 0.25;
        panneau.add(Box.createGlue(), contraintesEspace);

        // Bouton de prévisualisation de couleur
        JButton boutonCouleur = new JButton();
        boutonCouleur.setPreferredSize(DIM_PREVIEW_COULEUR);
        boutonCouleur.setBackground(couleurInitiale);
        boutonCouleur.setFocusPainted(true);
        boutonCouleur.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        boutonCouleur.addActionListener(e -> {
            Color couleurChoisie = JColorChooser.showDialog(
                    EcranDeDemarrage.this,
                    "Couleur " + texteEtiquette,
                    boutonCouleur.getBackground()
            );
            if (couleurChoisie != null) {
                boutonCouleur.setBackground(couleurChoisie);
                collecteurEvenements.setCouleur(cible, couleurChoisie);
            }
        });

        GridBagConstraints contraintesBouton = new GridBagConstraints();
        contraintesBouton.gridx = 2;
        contraintesBouton.gridy = ligne;
        contraintesBouton.anchor = GridBagConstraints.LINE_START;
        contraintesBouton.insets = MARGES_DEFAUT;
        contraintesBouton.fill = GridBagConstraints.BOTH;
        contraintesBouton.weightx = 0.05;
        contraintesBouton.weighty = 0.01;
        panneau.add(boutonCouleur, contraintesBouton);

        // Bouton de réinitialisation
        JButton boutonReset = creerBoutonAvecImage(PATH_BOUTON_ANNULER_ROUGE.toString(),0,17,Color.GRAY);
        boutonReset.setPreferredSize(DIM_PREVIEW_COULEUR);
        boutonReset.setToolTipText("Réinitialiser à la couleur par défaut");
        boutonReset.addActionListener(e -> {
            Color couleurDefaut = couleursInitiales.get(cible);
            boutonCouleur.setBackground(couleurDefaut);
            collecteurEvenements.setCouleur(cible, couleurDefaut);
        });


        GridBagConstraints contraintesReset = new GridBagConstraints();
        contraintesReset.gridx = 3;
        contraintesReset.gridy = ligne;
        contraintesReset.anchor = GridBagConstraints.LINE_START;
        contraintesReset.insets = MARGES_DEFAUT;
        contraintesReset.fill = GridBagConstraints.BOTH;
        contraintesReset.weightx = 0.05;
        contraintesReset.weighty = 0.01;
        panneau.add(boutonReset, contraintesReset);

        // --- Espace Horizontale Flexible ---
        contraintesReset.gridx = 4;
        contraintesReset.gridy = ligne;
        contraintesReset.weightx = 0.55;
        contraintesReset.fill = GridBagConstraints.HORIZONTAL;
        panneau.add(Box.createVerticalGlue(), contraintesReset);
    }


    /**
     * Crée l'onglet de configuration des animations.
     * @return Le JPanel de l'onglet Animation.
     */
    private JPanel creerOngletAnimation() {
        PanelBruitGris ongletAnimation = new PanelBruitGris();
        ongletAnimation.setLayout(new GridBagLayout());
        GridBagConstraints contraintes;
        int ligneCourante = 0;

        // --- Titre ---
        contraintes = new GridBagConstraints();
        contraintes.gridx = COLONNE_ETIQUETTE;
        contraintes.gridy = ligneCourante++;
        contraintes.weighty = 0.5;
        contraintes.fill = GridBagConstraints.VERTICAL;
        contraintes.gridwidth = 4;
        contraintes.anchor = GridBagConstraints.CENTER;
        contraintes.insets = MARGES_TITRE;

        JLabel titre = new JLabel(LBL_TITRE_ANIMATION);
        titre.setFont(FONT_TITRE);
        ongletAnimation.add(titre, contraintes);

        // --- Vitesse d'animation ---
        JSlider sliderVitesse = new JSlider(0, 100, 50); // Exemple : 0=Instantanné, 100=Très lent, 50=Normal
        sliderVitesse.setMajorTickSpacing(25);
        sliderVitesse.setMinorTickSpacing(5);
        sliderVitesse.setPaintTicks(true);
        sliderVitesse.setPaintLabels(true);
        sliderVitesse.addChangeListener(e -> {
            if (!sliderVitesse.getValueIsAdjusting()) {
                collecteurEvenements.setAnimationVitesse(sliderVitesse.getValue());
            }
        });
        // potentiellement une valeur par défaut
        sliderVitesse.setValue(jeu.getConfigAnimationVitesse());
        ajouterLigne(ongletAnimation, LBL_VITESSE_ANIMATION, sliderVitesse, ligneCourante++, FONT_LABEL);

        // --- Activer/Désactiver Animation Pièces ---
        JCheckBox checkAnimPieces = new JCheckBox();
        checkAnimPieces.setFont(FONT_COMPOSANT);
        checkAnimPieces.addActionListener(e -> {
            collecteurEvenements.setAnimationPieces(checkAnimPieces.isSelected());
        });
        // potentiellement une valeur par défaut
        checkAnimPieces.setSelected(infosDeConfigUI.isAnimerDeplacementPiece());
        ajouterLigne(ongletAnimation, LBL_ANIMATION_PIECES, checkAnimPieces, ligneCourante++, FONT_LABEL);

        // --- Activer/Désactiver Animation Surbrillance ---
        JCheckBox checkAnimSurbrillance = new JCheckBox();
        checkAnimSurbrillance.setFont(FONT_COMPOSANT);
        checkAnimSurbrillance.addActionListener(e -> {
            collecteurEvenements.setAnimationSurbrillance(checkAnimSurbrillance.isSelected());
        });
        // potentiellement une valeur par défaut
        checkAnimSurbrillance.setSelected(infosDeConfigUI.isAnimerSurbrillace());
        ajouterLigne(ongletAnimation, LBL_ANIMATION_SURBRILLANCE, checkAnimSurbrillance, ligneCourante++, FONT_LABEL);

        // --- Espace Vertical Flexible ---
        contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante;
        contraintes.weighty = 1.0;
        contraintes.fill = GridBagConstraints.VERTICAL;
        ongletAnimation.add(Box.createVerticalGlue(), contraintes);

        return ongletAnimation;
    }


    /**
     * Crée l'onglet de configuration audio.
     * @return Le JPanel de l'onglet Son.
     */
    private JPanel creerOngletSon() {
        JPanel ongletSon = new JPanel(new GridBagLayout());
        GridBagConstraints contraintes;
        int ligneCourante = 0;

        // --- Titre ---
        contraintes = new GridBagConstraints();
        contraintes.gridx = COLONNE_ETIQUETTE;
        contraintes.gridy = ligneCourante++;
        contraintes.weighty = 0.5;
        contraintes.fill = GridBagConstraints.VERTICAL;
        contraintes.gridwidth = 4;
        contraintes.anchor = GridBagConstraints.CENTER;
        contraintes.insets = MARGES_TITRE;

        JLabel titre = new JLabel(LBL_TITRE_SON);
        titre.setFont(FONT_TITRE);
        ongletSon.add(titre, contraintes);

        // --- Volume Général ---
        JSlider sliderVolumeGeneral = new JSlider(0, 100, 75); // 0=Muet, 100=Max
        sliderVolumeGeneral.setMajorTickSpacing(25);
        sliderVolumeGeneral.setPaintTicks(true);
        sliderVolumeGeneral.setPaintLabels(true);
        sliderVolumeGeneral.addChangeListener(e -> {
            if (!sliderVolumeGeneral.getValueIsAdjusting()) {
                collecteurEvenements.setSonVolumeGeneral(sliderVolumeGeneral.getValue());
                // Peut-être ajuster les autres sliders ou l'état Muet
            }
        });
        // potentiellement une valeur par défaut
          sliderVolumeGeneral.setValue(infosDeConfigUI.getConfigSonVolumeGeneral());
        ajouterLigne(ongletSon, LBL_VOLUME_GENERAL, sliderVolumeGeneral, ligneCourante++, FONT_LABEL);

        // --- Volume Effets Sonores ---
        JSlider sliderVolumeEffets = new JSlider(0, 100, 80);
        sliderVolumeEffets.setMajorTickSpacing(25);
        sliderVolumeEffets.setPaintTicks(true);
        sliderVolumeEffets.setPaintLabels(true);
        sliderVolumeEffets.addChangeListener(e -> {
            if (!sliderVolumeEffets.getValueIsAdjusting()) {
                collecteurEvenements.setSonVolumeEffets(sliderVolumeEffets.getValue());
            }
        });
        // potentiellement une valeur par défaut
        sliderVolumeEffets.setValue(infosDeConfigUI.getConfigSonVolumeEffets());
        ajouterLigne(ongletSon, LBL_VOLUME_EFFETS, sliderVolumeEffets, ligneCourante++, FONT_LABEL);


        // --- Volume Musique ---
        JSlider sliderVolumeMusique = new JSlider(0, 100, 60);
        sliderVolumeMusique.setMajorTickSpacing(25);
        sliderVolumeMusique.setPaintTicks(true);
        sliderVolumeMusique.setPaintLabels(true);
        sliderVolumeMusique.addChangeListener(e -> {
            if (!sliderVolumeMusique.getValueIsAdjusting()) {
                collecteurEvenements.setSonVolumeMusique(sliderVolumeMusique.getValue());
            }
        });
        // potentiellement une valeur par défaut
        sliderVolumeMusique.setValue(infosDeConfigUI.getConfigSonVolumeMusique());
        ajouterLigne(ongletSon, LBL_VOLUME_MUSIQUE, sliderVolumeMusique, ligneCourante++, FONT_LABEL);


        // --- Muet ---
        JCheckBox checkMuet = new JCheckBox();
        checkMuet.setFont(FONT_COMPOSANT);
        checkMuet.addActionListener(e -> {
            boolean estMuet = checkMuet.isSelected();
            collecteurEvenements.setSonMuet(estMuet);
            // Désactiver les sliders si muet est coché
            sliderVolumeGeneral.setEnabled(!estMuet);
            sliderVolumeEffets.setEnabled(!estMuet);
            sliderVolumeMusique.setEnabled(!estMuet);
        });
        // potentiellement une valeur par défaut
          boolean isMuted = infosDeConfigUI.getConfigSonMuet();
          checkMuet.setSelected(isMuted);
          sliderVolumeGeneral.setEnabled(!isMuted);
          sliderVolumeEffets.setEnabled(!isMuted);
          sliderVolumeMusique.setEnabled(!isMuted);
        ajouterLigne(ongletSon, LBL_SON_MUET, checkMuet, ligneCourante++, FONT_LABEL);


        // --- Espace Vertical Flexible ---
        contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante;
        contraintes.weighty = 1.0;
        contraintes.fill = GridBagConstraints.VERTICAL;
        ongletSon.add(Box.createVerticalGlue(), contraintes);

        return ongletSon;
    }


    /**
     * Méthode utilitaire pour ajouter une ligne (étiquette + composant) au GridBagLayout.
     *
     * @param panneau        Le JPanel utilisant GridBagLayout.
     * @param texteEtiquette Le texte pour le JLabel.
     * @param composant      Le JComponent à ajouter (ex: JButton, JComboBox).
     * @param ligne          La valeur gridy pour cette ligne.
     * @param policeEtiquette La police (Font) pour l'étiquette.
     */

    private void ajouterLigne(JPanel panneau, String texteEtiquette, JComponent composant, int ligne, Font policeEtiquette) {
        // --- Contraintes de l'Étiquette ---
        GridBagConstraints contraintesLabel = new GridBagConstraints();
        contraintesLabel.gridx = COLONNE_ETIQUETTE;
        contraintesLabel.gridy = ligne;
        contraintesLabel.anchor = GridBagConstraints.LINE_END; // Aligner le texte de l'étiquette à droite
        contraintesLabel.insets = MARGES_DEFAUT;              // Marge autour de l'étiquette
        contraintesLabel.weighty = 0.1;
        contraintesLabel.ipadx = 40;

        JLabel etiquette = new JLabel(texteEtiquette);
        etiquette.setFont(policeEtiquette);
        panneau.add(etiquette, contraintesLabel);

        // --- Contraintes du Composant ---
        GridBagConstraints contraintesComp = new GridBagConstraints();
        contraintesComp.gridx = 5;
        contraintesComp.gridy = ligne;
        contraintesComp.anchor = GridBagConstraints.LINE_START; // Aligner le composant à gauche
        contraintesComp.weighty = 0.1;
        contraintesComp.insets = MARGES_DEFAUT;              // Marge autour du composant

        // Appliquer la police standard
        composant.setFont(FONT_COMPOSANT);

        panneau.add(composant, contraintesComp);
    }


    /**
     * Crée une JComboBox avec une indication grisée comme premier élément.
     * @param options Les options pour la liste déroulante, incluant l'indication en premier.
     * @return La JComboBox configurée.
     */
    private JComboBox<String> creerListeDeroulanteAvecIndication(String[] options) {
        JComboBox<String> listeDeroulante = new JComboBox<>(options);
        listeDeroulante.setFont(FONT_COMPOSANT);
        listeDeroulante.setPreferredSize(new Dimension(LARGEUR_LISTE_DEROULANTE, HAUTEUR_LISTE_DEROULANTE));
        listeDeroulante.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                // Griser le premier élément (l'indication) s'il n'est pas sélectionné ET est l'élément affiché
                if (index == 0 && !isSelected && listeDeroulante.getSelectedIndex() == 0) {
                    setForeground(Color.GRAY);
                } else {
                    setForeground(Color.BLACK); // Couleur de texte par défaut
                }
                return this;
            }
        });
        listeDeroulante.setSelectedIndex(0); // Commencer avec l'indication sélectionnée

        // Ajouter un écouteur d'action pour remettre la couleur en noir une fois qu'un vrai choix est fait
        listeDeroulante.addActionListener(e -> {
            if (listeDeroulante.getSelectedIndex() != 0) {
                listeDeroulante.setForeground(Color.BLACK); // Assurer que le texte est noir après sélection
            }
            // Forcer le rafraîchissement de la liste déroulante elle-même après sélection pour màj la couleur
            listeDeroulante.repaint();
        });

        return listeDeroulante;
    }

}
