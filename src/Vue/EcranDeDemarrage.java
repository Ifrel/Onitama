package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;


import static Vue.ConfigUI.*;
import static Vue.Utils.creerTitreOnglets;

/*
* NOTES:
* Il serait logique que le modèle (Jeu ou une classe de configuration dédiée) ait des méthodes pour obtenir/définir
* ces nouvelles configurations (par exemple, getConfigCouleur(String cle),
* setConfigCouleur(String cle, Color couleur), getConfigIAReflexion(), setConfigIAReflexion(int temps), etc.).
*
* Les méthodes ajouterLigneCouleur, creerOngletIA, etc.,
*  pourraient alors lire les valeurs initiales depuis le modèle pour initialiser les composants graphiques.
* */

/**
 * EcranDeDemarrage : Écran de configuration du jeu utilisant JTabbedPane.
 * Permet de définir le mode de jeu, les noms des joueurs, le niveau de l'IA, etc.
 * Interagit avec le CollecteurEvenements pour signaler les actions de l'utilisateur.
 */
public class EcranDeDemarrage extends JTabbedPane implements Observateur {
    private final Jeu jeu;
    private final CollecteurEvenements collecteurEvenements;
    private final InterfaceGraphique interfaceGraphique;



    public EcranDeDemarrage(Jeu jeu, CollecteurEvenements collecteurEvenements, InterfaceGraphique interfaceGraphique) {
        this.jeu = jeu;
        this.collecteurEvenements = collecteurEvenements;
        this.interfaceGraphique = interfaceGraphique;
        // jeu.ajouteObservateur(this); // Décommenter seulement si cet écran nécessite des mises à jour EN DIRECT depuis le modèle
        creerInterface();
    }


    /** Création de l'Interface */
    private void creerInterface() {
        // Configurer l'apparence du JTabbedPane si désiré (par exemple, position des onglets)
        // setTabPlacement(JTabbedPane.TOP);

        // Créer et ajouter les onglets
        addTab(null, creerOngletGeneral());
        setTabComponentAt(0, creerTitreOnglets(TITRE_ONGLET_GENERAL)); // Supposons que creerTitreOnglets fonctionne

//        addTab(null, creerOngletTemporaire(TITRE_ONGLET_IA));
//        setTabComponentAt(1, creerTitreOnglets(TITRE_ONGLET_IA));
//
//        addTab(null, creerOngletTemporaire(TITRE_ONGLET_COULEUR));
//        setTabComponentAt(2, creerTitreOnglets(TITRE_ONGLET_COULEUR));
//
//        addTab(null, creerOngletTemporaire(TITRE_ONGLET_ANIMATION));
//        setTabComponentAt(3, creerTitreOnglets(TITRE_ONGLET_ANIMATION));
//
//        addTab(null, creerOngletTemporaire(TITRE_ONGLET_SON));
//        setTabComponentAt(4, creerTitreOnglets(TITRE_ONGLET_SON));
        // Utiliser les nouvelles méthodes de création pour chaque onglet
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
        JPanel ongletGeneral = new JPanel(new GridBagLayout());
        GridBagConstraints contraintes; // Utiliser des contraintes locales pour la clarté

        // --- Titre ---
        contraintes = new GridBagConstraints();
        contraintes.gridx = COLONNE_ETIQUETTE;
        contraintes.gridy = 0;
        contraintes.gridwidth = 2; // S'étend sur les deux colonnes
        contraintes.anchor = GridBagConstraints.CENTER;
        contraintes.insets = MARGES_TITRE;
        JLabel titre = new JLabel(LBL_TITRE_CONFIG);
        titre.setFont(FONT_TITRE);
        ongletGeneral.add(titre, contraintes);

        int ligneCourante = 1; // Commencer à ajouter les composants à partir de la ligne 1

        // --- Ligne 1 : Mode Auto ---
        JButton boutonModeAuto = new JButton(BTN_MODE_AUTO_OFF); // État par défaut
        boutonModeAuto.setFont(FONT_COMPOSANT);
        boutonModeAuto.addActionListener(e -> {
            // Basculer le texte du bouton et notifier le contrôleur
            boolean estActif = boutonModeAuto.getText().equals(BTN_MODE_AUTO_ON);
            boolean nouvelEtat = !estActif;
            boutonModeAuto.setText(nouvelEtat ? BTN_MODE_AUTO_ON : BTN_MODE_AUTO_OFF);
//            collecteurEvenements.configModeAuto(nouvelEtat); // Notifier le contrôleur
        });
        ajouterLigne(ongletGeneral, LBL_MODE_AUTO, boutonModeAuto, ligneCourante++, FONT_LABEL);

        // --- Ligne 2 : Reprendre une partie ---
        JComboBox<String> listeDeroulanteReprendre = creerListeDeroulanteAvecIndication(OPTIONS_REPRENDRE);
        listeDeroulanteReprendre.addActionListener(e -> {
            int indexSelectionne = listeDeroulanteReprendre.getSelectedIndex();
            if (indexSelectionne > 0) { // Ignorer l'indication
                String partieSelectionnee = (String) listeDeroulanteReprendre.getSelectedItem();
//                collecteurEvenements.configChargerPartie(partieSelectionnee); // Notifier le contrôleur
            } else {
//                collecteurEvenements.configChargerPartie(null); // Indiquer aucune sélection ou défaut
            }
            // Potentiellement désactiver d'autres options si une partie est chargée
        });
        ajouterLigne(ongletGeneral, LBL_REPRENDRE, listeDeroulanteReprendre, ligneCourante++, FONT_LABEL);

        // --- Ligne 3 : Jouer avec l'IA ---
        JComboBox<String> listeDeroulanteIA = creerListeDeroulanteAvecIndication(OPTIONS_IA);
        listeDeroulanteIA.addActionListener(e -> {
            String selection = (String) listeDeroulanteIA.getSelectedItem();
            // Vérifier que la sélection n'est pas "Non" et n'est pas l'indication
            boolean iaActive = !selection.equals(OPTION_IA_NON) && listeDeroulanteIA.getSelectedIndex() > 0;
//            collecteurEvenements.configNiveauIA(iaActive ? selection : null); // Notifier le contrôleur (passer niveau ou null)
            // Potentiellement activer/désactiver le champ Joueur 2 basé sur la sélection
        });
        ajouterLigne(ongletGeneral, LBL_JOUER_IA, listeDeroulanteIA, ligneCourante++, FONT_LABEL);

        // --- Ligne 4 : Nom Joueur 1 ---
        JTextField champJoueur1 = new JTextField(LARGEUR_CHAMP_TEXTE);
        champJoueur1.setFont(FONT_COMPOSANT);
        champJoueur1.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { miseAJourChamp(); }
            public void removeUpdate(DocumentEvent e) { miseAJourChamp(); }
            public void insertUpdate(DocumentEvent e) { miseAJourChamp(); }
            public void miseAJourChamp() {
//                collecteurEvenements.configNomJoueur(1, champJoueur1.getText()); // Notifier le contrôleur
            }
        });
        // Pré-remplir éventuellement avec un nom par défaut depuis modèle/config
        // champJoueur1.setText(jeu.getNomJoueur(1));
        ajouterLigne(ongletGeneral, LBL_JOUEUR_1, champJoueur1, ligneCourante++, FONT_LABEL);

        // --- Ligne 5 : Nom Joueur 2 ---
        JTextField champJoueur2 = new JTextField(LARGEUR_CHAMP_TEXTE);
        champJoueur2.setFont(FONT_COMPOSANT);
        champJoueur2.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { miseAJourChamp(); }
            public void removeUpdate(DocumentEvent e) { miseAJourChamp(); }
            public void insertUpdate(DocumentEvent e) { miseAJourChamp(); }
            public void miseAJourChamp() {
//                collecteurEvenements.configNomJoueur(2, champJoueur2.getText()); // Notifier le contrôleur
            }
        });
        // Pré-remplir éventuellement avec un nom par défaut depuis modèle/config
        // champJoueur2.setText(jeu.getNomJoueur(2));
        // Envisager d'activer/désactiver basé sur la sélection de listeDeroulanteIA
        ajouterLigne(ongletGeneral, LBL_JOUEUR_2, champJoueur2, ligneCourante++, FONT_LABEL);


        // --- Espace Vertical Flexible (Glue) ---
        // Pousse tous les composants vers le haut lorsque le panneau est redimensionné verticalement.
        contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante; // Placer dans la prochaine ligne disponible
        contraintes.weighty = 1.0;        // Prend tout l'espace vertical supplémentaire
        contraintes.fill = GridBagConstraints.VERTICAL;
        ongletGeneral.add(Box.createVerticalGlue(), contraintes);

        return ongletGeneral;
    }



    // === Création des Onglets Spécifiques ===

    /**
     * Crée l'onglet de configuration de l'IA.
     * @return Le JPanel de l'onglet IA.
     */
    private JPanel creerOngletIA() {
        JPanel ongletIA = new JPanel(new GridBagLayout());
        GridBagConstraints contraintes;
        int ligneCourante = 0;

        // --- Titre ---
        contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante++;
        contraintes.gridwidth = 2;
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
        sliderTempsReflexion.addChangeListener(e -> {
            if (!sliderTempsReflexion.getValueIsAdjusting()) { // Agir seulement quand on relâche le curseur
//                collecteurEvenements.configIAReflexion(sliderTempsReflexion.getValue());
            }
        });
        // Pré-remplir avec la valeur actuelle du modèle/config si disponible
        // sliderTempsReflexion.setValue(jeu.getConfigIAReflexion());
        ajouterLigne(ongletIA, LBL_TEMPS_REFLEXION, sliderTempsReflexion, ligneCourante++, FONT_LABEL);

        // --- Heuristique Avancée ---
        JCheckBox checkHeuristique = new JCheckBox();
        checkHeuristique.setFont(FONT_COMPOSANT);
        checkHeuristique.addActionListener(e -> {
//            collecteurEvenements.configIAHeuristique(checkHeuristique.isSelected());
        });
        // Pré-remplir avec la valeur actuelle du modèle/config si disponible
        // checkHeuristique.setSelected(jeu.getConfigIAHeuristique());
        ajouterLigne(ongletIA, LBL_HEURISTIQUE_AVANCEE, checkHeuristique, ligneCourante++, FONT_LABEL);


        // --- Choix Algorithme IA ---
        JComboBox<String> comboAlgoIA = new JComboBox<>(OPTIONS_ALGORITHME_IA);
        comboAlgoIA.setFont(FONT_COMPOSANT);
        comboAlgoIA.addActionListener(e -> {
            String selection = (String) comboAlgoIA.getSelectedItem();
//            collecteurEvenements.configIAAlgorithme(selection);
        });
        // Pré-remplir avec la valeur actuelle du modèle/config si disponible
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
        contraintes.gridwidth = 3; // S'étend sur 3 colonnes (Label, Preview, Button)
        contraintes.anchor = GridBagConstraints.CENTER;
        contraintes.insets = MARGES_TITRE;
        JLabel titre = new JLabel(LBL_TITRE_COULEUR);
        titre.setFont(FONT_TITRE);
        ongletCouleur.add(titre, contraintes);

        // Ajouter les sélecteurs de couleur
//        ajouterLigneCouleur(ongletCouleur, LBL_COULEUR_CASE_CLAIRE, jeu.getConfigCouleur("caseClaire"), ligneCourante++, CouleurCible.CASE_CLAIRE);
//        ajouterLigneCouleur(ongletCouleur, LBL_COULEUR_CASE_FONCEE, jeu.getConfigCouleur("caseFoncee"), ligneCourante++, CouleurCible.CASE_FONCEE);
//        ajouterLigneCouleur(ongletCouleur, LBL_COULEUR_JOUEUR_1, jeu.getConfigCouleur("joueur1"), ligneCourante++, CouleurCible.JOUEUR_1);
//        ajouterLigneCouleur(ongletCouleur, LBL_COULEUR_JOUEUR_2, jeu.getConfigCouleur("joueur2"), ligneCourante++, CouleurCible.JOUEUR_2);
//        ajouterLigneCouleur(ongletCouleur, LBL_COULEUR_SURBRILLANCE, jeu.getConfigCouleur("surbrillance"), ligneCourante++, CouleurCible.SURBRILLANCE);
        ajouterLigneCouleur(ongletCouleur, LBL_COULEUR_CASE_CLAIRE, new Color(42, 145, 200), ligneCourante++, CouleurCible.CASE_CLAIRE);
        ajouterLigneCouleur(ongletCouleur, LBL_COULEUR_CASE_FONCEE, new Color(188, 210, 221), ligneCourante++, CouleurCible.CASE_FONCEE);
        ajouterLigneCouleur(ongletCouleur, LBL_COULEUR_JOUEUR_1, new Color(74, 168, 49), ligneCourante++, CouleurCible.JOUEUR_1);
        ajouterLigneCouleur(ongletCouleur, LBL_COULEUR_JOUEUR_2, new Color(179, 128, 61), ligneCourante++, CouleurCible.JOUEUR_2);
        ajouterLigneCouleur(ongletCouleur, LBL_COULEUR_SURBRILLANCE, new Color(118, 46, 154), ligneCourante++, CouleurCible.SURBRILLANCE);


        // --- Espace Vertical Flexible ---
        contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante;
        contraintes.weighty = 1.0;
        contraintes.fill = GridBagConstraints.VERTICAL;
        ongletCouleur.add(Box.createVerticalGlue(), contraintes);

        return ongletCouleur;
    }

    // Enum pour identifier la cible de la couleur (simplifie le listener)
    private enum CouleurCible { CASE_CLAIRE, CASE_FONCEE, JOUEUR_1, JOUEUR_2, SURBRILLANCE }

    /**
     * Méthode utilitaire pour ajouter une ligne de sélection de couleur.
     */
    private void ajouterLigneCouleur(JPanel panneau, String texteEtiquette, Color couleurInitiale, int ligne, CouleurCible cible) {
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
                // Notifier le contrôleur avec la couleur choisie ET la cible
//                collecteurEvenements.configCouleur(cible, couleurChoisie);
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
        JPanel ongletAnimation = new JPanel(new GridBagLayout());
        GridBagConstraints contraintes;
        int ligneCourante = 0;

        // --- Titre ---
        contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante++;
        contraintes.gridwidth = 2;
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
//                collecteurEvenements.configAnimationVitesse(sliderVitesse.getValue());
            }
        });
        // sliderVitesse.setValue(jeu.getConfigAnimationVitesse());
        ajouterLigne(ongletAnimation, LBL_VITESSE_ANIMATION, sliderVitesse, ligneCourante++, FONT_LABEL);

        // --- Activer/Désactiver Animation Pièces ---
        JCheckBox checkAnimPieces = new JCheckBox();
        checkAnimPieces.setFont(FONT_COMPOSANT);
        checkAnimPieces.addActionListener(e -> {
//            collecteurEvenements.configAnimationPieces(checkAnimPieces.isSelected());
        });
        // checkAnimPieces.setSelected(jeu.getConfigAnimationPieces());
        ajouterLigne(ongletAnimation, LBL_ANIMATION_PIECES, checkAnimPieces, ligneCourante++, FONT_LABEL);

        // --- Activer/Désactiver Animation Surbrillance ---
        JCheckBox checkAnimSurbrillance = new JCheckBox();
        checkAnimSurbrillance.setFont(FONT_COMPOSANT);
        checkAnimSurbrillance.addActionListener(e -> {
//            collecteurEvenements.configAnimationSurbrillance(checkAnimSurbrillance.isSelected());
        });
        // checkAnimSurbrillance.setSelected(jeu.getConfigAnimationSurbrillance());
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
        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante++;
        contraintes.gridwidth = 2;
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
//                collecteurEvenements.configSonVolumeGeneral(sliderVolumeGeneral.getValue());
                // Peut-être ajuster les autres sliders ou l'état Muet
            }
        });
        // sliderVolumeGeneral.setValue(jeu.getConfigSonVolumeGeneral());
        ajouterLigne(ongletSon, LBL_VOLUME_GENERAL, sliderVolumeGeneral, ligneCourante++, FONT_LABEL);

        // --- Volume Effets Sonores ---
        JSlider sliderVolumeEffets = new JSlider(0, 100, 80);
        sliderVolumeEffets.setMajorTickSpacing(25);
        sliderVolumeEffets.setPaintTicks(true);
        sliderVolumeEffets.setPaintLabels(true);
        sliderVolumeEffets.addChangeListener(e -> {
            if (!sliderVolumeEffets.getValueIsAdjusting()) {
//                collecteurEvenements.configSonVolumeEffets(sliderVolumeEffets.getValue());
            }
        });
        // sliderVolumeEffets.setValue(jeu.getConfigSonVolumeEffets());
        ajouterLigne(ongletSon, LBL_VOLUME_EFFETS, sliderVolumeEffets, ligneCourante++, FONT_LABEL);


        // --- Volume Musique ---
        JSlider sliderVolumeMusique = new JSlider(0, 100, 60);
        sliderVolumeMusique.setMajorTickSpacing(25);
        sliderVolumeMusique.setPaintTicks(true);
        sliderVolumeMusique.setPaintLabels(true);
        sliderVolumeMusique.addChangeListener(e -> {
            if (!sliderVolumeMusique.getValueIsAdjusting()) {
//                collecteurEvenements.configSonVolumeMusique(sliderVolumeMusique.getValue());
            }
        });
        // sliderVolumeMusique.setValue(jeu.getConfigSonVolumeMusique());
        ajouterLigne(ongletSon, LBL_VOLUME_MUSIQUE, sliderVolumeMusique, ligneCourante++, FONT_LABEL);


        // --- Muet ---
        JCheckBox checkMuet = new JCheckBox();
        checkMuet.setFont(FONT_COMPOSANT);
        checkMuet.addActionListener(e -> {
            boolean estMuet = checkMuet.isSelected();
//            collecteurEvenements.configSonMuet(estMuet);
            // Désactiver les sliders si muet est coché
            sliderVolumeGeneral.setEnabled(!estMuet);
            sliderVolumeEffets.setEnabled(!estMuet);
            sliderVolumeMusique.setEnabled(!estMuet);
        });
        // boolean isMuted = jeu.getConfigSonMuet();
        // checkMuet.setSelected(isMuted);
        // sliderVolumeGeneral.setEnabled(!isMuted);
        // sliderVolumeEffets.setEnabled(!isMuted);
        // sliderVolumeMusique.setEnabled(!isMuted);
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

        JLabel etiquette = new JLabel(texteEtiquette);
        etiquette.setFont(policeEtiquette);
        panneau.add(etiquette, contraintesLabel);

        // --- Contraintes du Composant ---
        GridBagConstraints contraintesComp = new GridBagConstraints();
        contraintesComp.gridx = COLONNE_COMPOSANT;
        contraintesComp.gridy = ligne;
        contraintesComp.anchor = GridBagConstraints.LINE_START; // Aligner le composant à gauche
        contraintesComp.fill = GridBagConstraints.HORIZONTAL; // Permettre au composant de s'étirer horizontalement
        contraintesComp.weightx = 1.0;                       // Permettre à cette colonne de prendre l'espace horizontal supplémentaire
        contraintesComp.insets = MARGES_DEFAUT;              // Marge autour du composant

        // Appliquer la police standard si le composant n'en a pas une spécifique
        if (composant.getFont() == null) {
            composant.setFont(FONT_COMPOSANT);
        }
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
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
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

    /**
     * Crée un JPanel simple avec une étiquette temporaire pour les onglets non implémentés.
     * @param titreOnglet Le titre de l'onglet.
     * @return Un JPanel avec un message temporaire.
     */
    private JPanel creerOngletTemporaire(String titreOnglet) {
        JPanel panneau = new JPanel(new GridBagLayout()); // Utiliser GridBagLayout pour centrer facilement
        JLabel textePlaceholder = new JLabel("Paramètres pour '" + titreOnglet + "' à venir.");
        textePlaceholder.setFont(FONT_LABEL);
        textePlaceholder.setForeground(Color.GRAY);

        GridBagConstraints contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.gridy = 0;
        contraintes.weightx = 1.0;
        contraintes.weighty = 1.0;
        contraintes.anchor = GridBagConstraints.CENTER;
        panneau.add(textePlaceholder, contraintes);

        return panneau;
    }




    // === Implémentation Observateur ===

    @Override
    public void miseAJour() {
        // Cette méthode est appelée si le modèle Jeu change ET si cette classe est enregistrée comme observateur.
        // Implémenter la logique ici si l'écran de démarrage doit réagir aux changements du modèle.
        // Exemple : Recharger la liste des sauvegardes si une nouvelle partie est sauvegardée extérieurement.
        System.out.println("EcranDeDemarrage: miseAJour() appelée (si observateur enregistré)");

        // Exemple : Mettre à jour la liste des parties sauvegardées (si listeDeroulanteReprendre nécessite une màj dynamique)
        // DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) listeDeroulanteReprendre.getModel();
        // model.removeAllElements();
        // model.addElement(INDICATION_SELECTION);
        // List<String> sauvegardes = jeu.getListeSauvegardes(); // Supposant que Jeu a cette méthode
        // for (String partie : sauvegardes) {
        //     model.addElement(partie);
        // }
        // listeDeroulanteReprendre.setSelectedIndex(0); // Réinitialiser la sélection à l'indication
    }
}


















//package Vue;
//
//import Modele.Jeu;
//import Patterns.Observateur;
//
//import javax.swing.*;
//import java.awt.*;
//
//import static Global.Config.*;
//import static Vue.Utils.creerTitreOnglets;
//
//
///*
//| Attribut           | Type     | Description |
//|--------------------|----------|-------------|
//| `gridx`            | `int`    | Colonne de départ |
//| `gridy`            | `int`    | Ligne de départ |
//| `gridwidth`        | `int`    | Nombre de colonnes à occuper |
//| `gridheight`       | `int`    | Nombre de lignes à occuper |
//| `weightx`          | `double` | Priorité horizontale pour étirement |
//| `weighty`          | `double` | Priorité verticale pour étirement |
//| `fill`             | `int`    | Façon dont le composant remplit sa cellule (`NONE`, `HORIZONTAL`, `VERTICAL`, `BOTH`) |
//| `anchor`           | `int`    | Position dans la cellule si non rempli (`CENTER`, `NORTH`, etc.) |
//| `insets`           | `Insets` | Marge intérieure (haut, gauche, bas, droite) |
//| `ipadx`, `ipady`   | `int`    | Espace **interne** ajouté au composant (en pixels) |
// */
//public class EcranDeDemarrage extends JTabbedPane implements Observateur {
//    Jeu jeu;
//    CollecteurEvenements collecteurEvent;
//    InterfaceGraphique interfaceGraphique;
//
//    GridBagConstraints gbc;
//    JPanel ongletGeneral;
//
//    public EcranDeDemarrage(Jeu jeu, CollecteurEvenements collecteurEvent, InterfaceGraphique interfaceGraphique) {
//        this.jeu = jeu;
//        this.collecteurEvent = collecteurEvent;
//        this.interfaceGraphique = interfaceGraphique;
////        jeu.ajouteObservateur(this);
//
//        creerOngletGeneral();
//
//        // Ajout des Onglets
//        addTab(null, ongletGeneral);
//        setTabComponentAt(0, creerTitreOnglets("Général"));
//
//        addTab(null, new JPanel());
//        setTabComponentAt(1, creerTitreOnglets("IA"));
//
//        addTab(null, new JPanel());
//        setTabComponentAt(2, creerTitreOnglets("Couleur"));
//
//        addTab(null, new JPanel());
//        setTabComponentAt(3, creerTitreOnglets("Animation"));
//
//        addTab(null, new JPanel());
//        setTabComponentAt(4, creerTitreOnglets("Son"));
//    }
//
//
//
//    @Override
//    public void miseAJour() {
//        // À compléter selon logique MVC
//    }
//
//
//
//    private void creerOngletGeneral(){
//        // Premier onglet : Configuration
//        ongletGeneral = new JPanel(new GridBagLayout());
//        gbc = new GridBagConstraints();
//        setGbc(0,0,1,1,0.25,0.25,
//                GridBagConstraints.VERTICAL,GridBagConstraints.WEST,
//                new Insets(10, 10, 10, 10), 40, 10);
//
//        // panneau
//        JPanel panneau = new JPanel();
//        panneau.setPreferredSize(new Dimension(200,500));
//        panneau.setBackground(new Color(211, 129, 23));
//        gbc.gridwidth = 2;
//        gbc.gridheight = 15;
//        ongletGeneral.add(panneau, gbc);
//
//
//        // Titre centré sur la ligne 3 (index 2)
//        JLabel titre = new JLabel("Configuration");
//        titre.setFont(new Font(POLICE_1, Font.BOLD, 50));
//
//        gbc.gridx = 1;
//        gbc.gridy = 2;
//        gbc.gridwidth = 12; // couvre 12 colonnes pour recentrer le titre
//        gbc.gridheight = 1;
//        gbc.anchor = GridBagConstraints.CENTER;
//        gbc.fill = GridBagConstraints.NONE;
//        ongletGeneral.add(titre, gbc);
//
//
//        // Reset pour la suite
//        Font font = new Font(POLICE_1, Font.PLAIN, 20);
//        gbc.gridwidth = 6; // couvre 6 colonnes pour recentrer
//        gbc.gridheight = 1;
//        gbc.anchor = GridBagConstraints.CENTER;
//        gbc.fill = GridBagConstraints.NONE;
//
//
//
//        // Ligne 1 : Mode Auto
//        ajouterLigne(ongletGeneral, "Mode auto (IA vs IA)", new JButton("Off"), 7,1, font);
//
//        // Ligne 2 : Reprendre une partie
//        ajouterLigne(ongletGeneral, "Reprendre une partie", creerComboAvecPrompt(new String[]{
//                "Sélectionner ici...", "Partie 1", "Partie 2", "Partie 3" }), 8,1, font);
//
//        // Ligne 3 : Jouer avec l'IA
//        ajouterLigne(ongletGeneral, "Jouer avec l'IA", creerComboAvecPrompt(new String[]{
//                "Non", "Facile", "Intermédiaire", "Difficile"}), 9,1, font);
//
//        // Ligne 4 : Nom Joueur 1
//        ajouterLigne(ongletGeneral, "Joueur 1", new JTextField(18), 10,1, font);
//
//        // Ligne 5 : Nom Joueur 2
//        gbc.weighty = 1.0;
//        ajouterLigne(ongletGeneral, "Joueur 2", new JTextField(18), 11,1, font);
//
//    }
//
//    private void ajouterLigne(JPanel panel, String labelTexte, JComponent composant, int ligne, int colonne, Font font) {
//        gbc.gridx = colonne;
//        gbc.gridy = ligne;
//        gbc.anchor = GridBagConstraints.LINE_END;
//        JLabel label = new JLabel(labelTexte);
//        label.setFont(font);
//        panel.add(label, gbc);
//
//        gbc.gridx = colonne+1;
//        gbc.anchor = GridBagConstraints.LINE_START;
//        panel.add(composant, gbc);
//    }
//
//    private JComboBox<String> creerComboAvecPrompt(String[] options) {
//        JComboBox<String> combo = new JComboBox<>(options);
//        combo.setRenderer(new DefaultListCellRenderer() {
//            @Override
//            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
//                                                          boolean isSelected, boolean cellHasFocus) {
//                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//                if (index == 0 && !isSelected) {
//                    setForeground(Color.GRAY);
//                } else {
//                    setForeground(Color.BLACK);
//                }
//                return this;
//            }
//        });
//        combo.setSelectedIndex(0);
//        combo.setPreferredSize(new Dimension(200, 30));
//        return combo;
//    }
//
//    private void setGbc(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int fill, int anchor, Insets insets, int ipadx, int ipady){
//        gbc.gridx = gridx;
//        gbc.gridy = gridy;
//        gbc.gridwidth = gridwidth;
//        gbc.gridheight = gridheight;
//        gbc.weightx = weightx;
//        gbc.weighty = weighty;
//        gbc.fill = fill;
//        gbc.anchor = anchor;
//        gbc.insets = insets;
//        gbc.ipadx = ipadx;
//        gbc.ipady = ipady;
//    }
//
//    private void resetGbc(){
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 1;
//        gbc.gridheight = 1;
//        gbc.weightx = 0.0;
//        gbc.weighty = 0.0;
//        gbc.fill = GridBagConstraints.NONE;
//        gbc.anchor = GridBagConstraints.CENTER;
//        gbc.insets = new Insets(0, 0, 0, 0);
//        gbc.ipadx = 0;
//        gbc.ipady = 0;
//    }
//}
