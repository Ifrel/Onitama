package Vue;

import Global.Config;
import Modele.Jeu;
import Vue.Adaptateurs.AdaptateurBoutonEntrer;
import Vue.Annimations.BruitGrisAvecPointsPanel;
import Vue.Utils.JPanelAvecCouleurDebraille;
import Vue.Utils.PanelAvecImage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;


import static Global.Config.*;
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

    // --- Onglet General
    private boolean modeAutoIA;
    JButton boutonModeAuto;
    JComboBox<String> listeDeroulanteReprendre, listeDeroulanteIA;
    JTextField champJoueur1, champJoueur2;
    String partieSelectionnee, niveauIAselectione;
    AdaptateurBoutonEntrer adaptateurBoutonEntrer;




    public EcranDeDemarrage(Jeu jeu, CollecteurEvenements collecteurEvenements, InterfaceGraphique interfaceGraphique) {
        this.jeu = jeu;
        this.collecteurEvenements = collecteurEvenements;
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
        setTabComponentAt(0, creerTitreOnglets(TITRE_ONGLET_GENERAL)); // Supposons que creerTitreOnglets fonctionne

        addTab(null, creerOngletIA());
        setTabComponentAt(1, creerTitreOnglets(TITRE_ONGLET_IA));

        addTab(null, creerOngletCouleur());
        setTabComponentAt(2, creerTitreOnglets(TITRE_ONGLET_COULEUR));

        addTab(null, creerOngletAnimation());
        setTabComponentAt(3, creerTitreOnglets(TITRE_ONGLET_ANIMATION));

        addTab(null, creerOngletSon());
        setTabComponentAt(4, creerTitreOnglets(TITRE_ONGLET_SON));
    }




    /**
     * Crée l'onglet principal "Général" de configuration en utilisant GridBagLayout.
     * @return Le JPanel de l'onglet Général.
     */
    private JPanel creerOngletGeneral() {
//        JPanel ongletGeneral = new JPanel(new GridBagLayout());
        PanelAvecImage ongletGeneral = new PanelAvecImage(PATH_ARRIERE_PLAN_ED_O2);
        ongletGeneral.setLayout(new GridBagLayout());

        // --- Titre ---
        GridBagConstraints contraintes = new GridBagConstraints();
        contraintes.fill = GridBagConstraints.VERTICAL;
        contraintes.weighty = 0.2;
        contraintes.gridx = COLONNE_ETIQUETTE;
        contraintes.gridy = 3;
        contraintes.gridwidth = 2; // S'étend sur les deux colonnes
        contraintes.insets = MARGES_TITRE;
        JLabel titre = new JLabel(LBL_TITRE_CONFIG);
        titre.setFont(FONT_TITRE);
        ongletGeneral.add(titre, contraintes);

        int ligneCourante = 6; // Commencer à ajouter les composants à partir de la ligne 2


        // --- Ligne 1 : Mode Auto ---
        boutonModeAuto = creerBoutonAvecImage(PATH_BTN_MODE_AUTO_OFF); // État par défaut
        boutonModeAuto.setPreferredSize(new Dimension(62,35));
        boutonModeAuto.setFont(FONT_COMPOSANT);
        boutonModeAuto.addActionListener(e -> {
            modeAutoIA = ! modeAutoIA;
            ImageIcon iconOFF = new ImageIcon(PATH_BTN_MODE_AUTO_OFF);
            ImageIcon iconON = new ImageIcon(PATH_BTN_MODE_AUTO_ON);
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

            //On gele toutes les autres options de config
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
            public void changedUpdate(DocumentEvent e) {mettreAjour();} // Si le style du texte change
            public void removeUpdate(DocumentEvent e) {mettreAjour(); }  // Si le texte est suppromer
            public void insertUpdate(DocumentEvent e) {mettreAjour(); }  // Si un nouveau text est insérer
            private void mettreAjour(){
                champJoueur1.setBorder(UIManager.getBorder("TextField.border"));
                adaptateurBoutonEntrer.setChampJoueur(1, champJoueur1);
            }
        });
        // Pré-remplir éventuellement avec un nom par défaut depuis modèle/config
        // champJoueur1.setText(jeu.getNomJoueur(1)); TODO à décider
        champJoueur1.setText("Rinel");
        ajouterLigne(ongletGeneral, LBL_JOUEUR_1, champJoueur1, ligneCourante++, FONT_LABEL);


        // --- Ligne 5 : Nom Joueur 2 ---
        champJoueur2 = new JTextField(LARGEUR_CHAMP_TEXTE);
        champJoueur2.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {mettreAjour();  }
            public void removeUpdate(DocumentEvent e) {mettreAjour(); }
            public void insertUpdate(DocumentEvent e) {mettreAjour(); }
            private void mettreAjour(){
                champJoueur2.setBorder(UIManager.getBorder("TextField.border"));
                adaptateurBoutonEntrer.setChampJoueur(2, champJoueur2);
            }
        });
        // Pré-remplir éventuellement avec un nom par défaut depuis modèle/config
        // champJoueur2.setText(jeu.getNomJoueur(2)); TODO à décider
        champJoueur2.setText("Arthur");
        ajouterLigne(ongletGeneral, LBL_JOUEUR_2, champJoueur2, ligneCourante++, FONT_LABEL);

        // -- bouton enter
        JButton entrer = creerBoutonAvecImage(PATH_BTN_ENTRER);
        entrer.setPreferredSize(new Dimension(98, 98));
        contraintes = new GridBagConstraints();
        contraintes.gridx = 6;
        contraintes.gridy = ligneCourante ; // Placer dans la prochaine ligne disponible
        contraintes.fill = GridBagConstraints.NONE;
        entrer.addActionListener(adaptateurBoutonEntrer);
        ongletGeneral.add(entrer, contraintes);

        // --- Espace Vertical Flexible (Glue) ---
        // Pousse tous les composants vers le haut lorsque le panneau est redimensionné verticalement.
        contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante; // Placer dans la prochaine ligne disponible
        contraintes.weighty = 0.5;        // Prend tout l'espace vertical supplémentaire
        contraintes.fill = GridBagConstraints.VERTICAL;
        ongletGeneral.add(Box.createVerticalGlue(), contraintes);

        return ongletGeneral;
    }




    /**
     * Crée l'onglet de configuration de l'IA.
     * @return Le JPanel de l'onglet IA.
     */
    private JPanel creerOngletIA() {
//        JPanel ongletIA = new JPanel(new GridBagLayout());

        JPanelAvecCouleurDebraille ongletIA = new JPanelAvecCouleurDebraille(new Color(127, 157, 172),new Color(112, 112, 112) );
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

        // --- Temps de réflexion IA ---
        JSlider sliderTempsReflexion = new JSlider(100, 5000, 1000); // min, max, valeur initiale (en ms)
        sliderTempsReflexion.setMajorTickSpacing(1000);
        sliderTempsReflexion.setMinorTickSpacing(100);
        sliderTempsReflexion.setPaintTicks(true);
        sliderTempsReflexion.setPaintLabels(true); // Afficher les valeurs numériques majeures
        sliderTempsReflexion.setPreferredSize(new Dimension(300,50));
        sliderTempsReflexion.addChangeListener(e -> {
            if (!sliderTempsReflexion.getValueIsAdjusting()) { // Agir seulement quand on relâche le curseur
                collecteurEvenements.configIAReflexion(sliderTempsReflexion.getValue());
            }
        });
        // TODO Pré-remplir avec la valeur actuelle du modèle/config si disponible
        // sliderTempsReflexion.setValue(jeu.getConfigIAReflexion());
        ajouterLigne(ongletIA, LBL_TEMPS_REFLEXION, sliderTempsReflexion, ligneCourante++, FONT_LABEL);

        // --- Heuristique Avancée ---
        JCheckBox checkHeuristique = new JCheckBox();
        checkHeuristique.setFont(FONT_COMPOSANT);
        checkHeuristique.addActionListener(e -> {
            collecteurEvenements.configIAHeuristique(checkHeuristique.isSelected());
        });
        // TODO Pré-remplir avec la valeur actuelle du modèle/config si disponible
        // checkHeuristique.setSelected(jeu.getConfigIAHeuristique());
        ajouterLigne(ongletIA, LBL_HEURISTIQUE_AVANCEE, checkHeuristique, ligneCourante++, FONT_LABEL);


        // --- Choix Algorithme IA ---
        JComboBox<String> comboAlgoIA = new JComboBox<>(OPTIONS_ALGORITHME_IA);
        comboAlgoIA.setFont(FONT_COMPOSANT);
        comboAlgoIA.addActionListener(e -> {
            String selection = (String) comboAlgoIA.getSelectedItem();
            collecteurEvenements.configIAAlgorithme(selection);
        });
        // TODO Pré-remplir avec la valeur actuelle du modèle/config si disponible
        // comboAlgoIA.setSelectedItem(jeu.getConfigIAAlgorithme());
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
        contraintes.gridwidth = 3; // S'étend sur 3 colonnes (Label, Preview, Button)
        contraintes.anchor = GridBagConstraints.CENTER;
        contraintes.insets = MARGES_TITRE;
        JLabel titre = new JLabel(LBL_TITRE_COULEUR);
        titre.setFont(FONT_TITRE);
        ongletCouleur.add(titre, contraintes);

        // Ajouter les sélecteurs de couleur
        ajouterLigneCouleur(ongletCouleur, LBL_PLATEAU_DE_JEU, COULEUR_PLATEAU_DE_JEU, ligneCourante++, Config.CiblesDesCouleurs.PLATEAU_DE_JEU);
        ajouterLigneCouleur(ongletCouleur, LBL_CASE_TERRAIN, COULEUR_CASE_TERRAIN, ligneCourante++, Config.CiblesDesCouleurs.CASE_TERRAIN);
        ajouterLigneCouleur(ongletCouleur, LBL_CASE_MAITRE_JOUEUR_1,COULEUR_CASE_MAITRE_JOUEUR_1, ligneCourante++, Config.CiblesDesCouleurs.CASE_MAITRE_JOUEUR_1);
        ajouterLigneCouleur(ongletCouleur, LBL_CASE_MAITRE_JOUEUR_2, COULEUR_CASE_MAITRE_JOUEUR_2, ligneCourante++, Config.CiblesDesCouleurs.CASE_MAITRE_JOUEUR_2);
        ajouterLigneCouleur(ongletCouleur, LBL_CASE_ELEVE_JOUEUR_1, COULEUR_CASE_ELEVE_JOUEUR_1, ligneCourante++, Config.CiblesDesCouleurs.CASE_ELEVE_JOUEUR_1);
        ajouterLigneCouleur(ongletCouleur, LBL_CASE_ELEVE_JOUEUR_2, COULEUR_CASE_ELEVE_JOUEUR_2, ligneCourante++, Config.CiblesDesCouleurs.CASE_ELEVE_JOUEUR_2);
        ajouterLigneCouleur(ongletCouleur, LBL_BLOC_MENU, COULEUR_BLOC_MENU, ligneCourante++, Config.CiblesDesCouleurs.BLOC_MENU);


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
     * Méthode utilitaire pour ajouter une ligne de sélection de couleur.
     */
    private void ajouterLigneCouleur(JPanel panneau, String texteEtiquette, Color couleurInitiale, int ligne, Config.CiblesDesCouleurs cible) {
        // --- Étiquette ---
        GridBagConstraints contraintesLabel = new GridBagConstraints();
        contraintesLabel.gridx = 0;
        contraintesLabel.gridy = ligne;
        contraintesLabel.anchor = GridBagConstraints.LINE_END;
        contraintesLabel.insets = MARGES_DEFAUT;
        JLabel etiquette = new JLabel(texteEtiquette);
        etiquette.setFont(FONT_LABEL);
        panneau.add(etiquette, contraintesLabel);

        // --- Panneau de prévisualisation ---
        JPanel previewPanel = new JPanel();
        previewPanel.setPreferredSize(DIM_PREVIEW_COULEUR);
        previewPanel.setBackground(couleurInitiale);
        previewPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        GridBagConstraints contraintesPreview = new GridBagConstraints();
        contraintesPreview.gridx = 1;
        contraintesPreview.gridy = ligne;
        contraintesPreview.insets = new Insets(5, 5, 5, 5); // Marges plus serrées
        panneau.add(previewPanel, contraintesPreview);

        // --- Bouton Choisir ---
        JButton boutonChoisir = new JButton(BTN_CHOISIR_COULEUR);
        boutonChoisir.setFont(FONT_COMPOSANT);
        boutonChoisir.addActionListener(e -> {
            Color couleurChoisie = JColorChooser.showDialog(
                    EcranDeDemarrage.this, // Parent component
                    "Choisir " + texteEtiquette, // Titre de la boîte de dialogue
                    previewPanel.getBackground() // Couleur initiale
            );
            if (couleurChoisie != null) {
                previewPanel.setBackground(couleurChoisie); // Mettre à jour la prévisualisation
                collecteurEvenements.configCouleur(cible, couleurChoisie); // Notifier le contrôleur avec la couleur choisie ET la cible
            }
        });
        GridBagConstraints contraintesBouton = new GridBagConstraints();
        contraintesBouton.gridx = 2;
        contraintesBouton.gridy = ligne;
        contraintesBouton.anchor = GridBagConstraints.LINE_START;
        contraintesBouton.insets = MARGES_DEFAUT;
        panneau.add(boutonChoisir, contraintesBouton);
    }




    /**
     * Crée l'onglet de configuration des animations.
     * @return Le JPanel de l'onglet Animation.
     */
    private JPanel creerOngletAnimation() {
//        JPanel ongletAnimation = new JPanel(new GridBagLayout());
        BruitGrisAvecPointsPanel ongletAnimation = new BruitGrisAvecPointsPanel();
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
                collecteurEvenements.configAnimationVitesse(sliderVitesse.getValue());
            }
        });
        // TODO potentiellement une valeur par défaut
        //  sliderVitesse.setValue(jeu.getConfigAnimationVitesse());
        ajouterLigne(ongletAnimation, LBL_VITESSE_ANIMATION, sliderVitesse, ligneCourante++, FONT_LABEL);

        // --- Activer/Désactiver Animation Pièces ---
        JCheckBox checkAnimPieces = new JCheckBox();
        checkAnimPieces.setFont(FONT_COMPOSANT);
        checkAnimPieces.addActionListener(e -> {
            collecteurEvenements.configAnimationPieces(checkAnimPieces.isSelected());
        });
        // TODO potentiellement une valeur par défaut
        //  checkAnimPieces.setSelected(jeu.getConfigAnimationPieces());
        ajouterLigne(ongletAnimation, LBL_ANIMATION_PIECES, checkAnimPieces, ligneCourante++, FONT_LABEL);

        // --- Activer/Désactiver Animation Surbrillance ---
        JCheckBox checkAnimSurbrillance = new JCheckBox();
        checkAnimSurbrillance.setFont(FONT_COMPOSANT);
        checkAnimSurbrillance.addActionListener(e -> {
            collecteurEvenements.configAnimationSurbrillance(checkAnimSurbrillance.isSelected());
        });
        // TODO potentiellement une valeur par défaut
        //  checkAnimSurbrillance.setSelected(jeu.getConfigAnimationSurbrillance());
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
                collecteurEvenements.configSonVolumeGeneral(sliderVolumeGeneral.getValue());
                // Peut-être ajuster les autres sliders ou l'état Muet
            }
        });
        // TODO potentiellement une valeur par défaut
        //  sliderVolumeGeneral.setValue(jeu.getConfigSonVolumeGeneral());
        ajouterLigne(ongletSon, LBL_VOLUME_GENERAL, sliderVolumeGeneral, ligneCourante++, FONT_LABEL);

        // --- Volume Effets Sonores ---
        JSlider sliderVolumeEffets = new JSlider(0, 100, 80);
        sliderVolumeEffets.setMajorTickSpacing(25);
        sliderVolumeEffets.setPaintTicks(true);
        sliderVolumeEffets.setPaintLabels(true);
        sliderVolumeEffets.addChangeListener(e -> {
            if (!sliderVolumeEffets.getValueIsAdjusting()) {
                collecteurEvenements.configSonVolumeEffets(sliderVolumeEffets.getValue());
            }
        });
        // TODO potentiellement une valeur par défaut
        //  sliderVolumeEffets.setValue(jeu.getConfigSonVolumeEffets());
        ajouterLigne(ongletSon, LBL_VOLUME_EFFETS, sliderVolumeEffets, ligneCourante++, FONT_LABEL);


        // --- Volume Musique ---
        JSlider sliderVolumeMusique = new JSlider(0, 100, 60);
        sliderVolumeMusique.setMajorTickSpacing(25);
        sliderVolumeMusique.setPaintTicks(true);
        sliderVolumeMusique.setPaintLabels(true);
        sliderVolumeMusique.addChangeListener(e -> {
            if (!sliderVolumeMusique.getValueIsAdjusting()) {
                collecteurEvenements.configSonVolumeMusique(sliderVolumeMusique.getValue());
            }
        });
        // TODO potentiellement une valeur par défaut
        //  sliderVolumeMusique.setValue(jeu.getConfigSonVolumeMusique());
        ajouterLigne(ongletSon, LBL_VOLUME_MUSIQUE, sliderVolumeMusique, ligneCourante++, FONT_LABEL);


        // --- Muet ---
        JCheckBox checkMuet = new JCheckBox();
        checkMuet.setFont(FONT_COMPOSANT);
        checkMuet.addActionListener(e -> {
            boolean estMuet = checkMuet.isSelected();
            collecteurEvenements.configSonMuet(estMuet);
            // Désactiver les sliders si muet est coché
            sliderVolumeGeneral.setEnabled(!estMuet);
            sliderVolumeEffets.setEnabled(!estMuet);
            sliderVolumeMusique.setEnabled(!estMuet);
        });
        // TODO potentiellement une valeur par défaut
        //  boolean isMuted = jeu.getConfigSonMuet();
        //  checkMuet.setSelected(isMuted);
        //  sliderVolumeGeneral.setEnabled(!isMuted);
        //  sliderVolumeEffets.setEnabled(!isMuted);
        //  sliderVolumeMusique.setEnabled(!isMuted);
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
        contraintesComp.gridx = COLONNE_COMPOSANT;
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
