//package Vue.testsUI;
//
//
//import Global.Config;
//import Modele.Jeu;
//import Vue.Adaptateurs.AdaptateurBoutonEntrer;
//import Vue.Animations.BruitGrisAvecPointsPanel;
//import Vue.CollecteurEvenements;
//import Vue.InterfaceGraphique;
//import Vue.Utils.JPanelAvecCouleurDebraille;
//import Vue.Utils.PanelAvecImage;
//
//import javax.swing.*;
//import javax.swing.event.DocumentEvent;
//import javax.swing.event.DocumentListener;
//import java.awt.*;
//import java.util.Locale; // Importer Locale
//import java.util.ResourceBundle; // Importer ResourceBundle
//
//// Importer la classe des clés de texte
//import Vue.testsUI.AppTextKeys;
//
//// Constantes qui ne sont PAS du texte UI (chemins, dimensions, etc.)
//import static Global.Config.*;
//import static Global.Paths.*;
//import static Vue.Configuration.ConfigUI.*;
//import static Vue.Utils.MethodsStaticsUtils.*; // Assurez-vous que creerBoutonAvecImage, creerJPanel sont ici
//
///**
// * EcranDeDemarrage : Écran de configuration du jeu utilisant JTabbedPane.
// * Permet de définir le mode de jeu, les noms des joueurs, le niveau de l'IA, etc.
// * Interagit avec le CollecteurEvenements pour signaler les actions de l'utilisateur.
// * Gère l'affichage des textes via ResourceBundle pour l'internationalisation.
// */
//public class EcranDeDemarrage extends JTabbedPane {
//    private final Jeu jeu;
//    private final CollecteurEvenements collecteurEvenements;
//    private final InterfaceGraphique interfaceGraphique;
//    private final AdaptateurBoutonEntrer adaptateurBoutonEntrer;
//    private ResourceBundle messages; // Champ pour le ResourceBundle
//
//    // --- Onglet General
//    private boolean modeAutoIA;
//    private JButton boutonModeAuto;
//    private JComboBox<String> listeDeroulanteReprendre, listeDeroulanteIA;
//    private JTextField champJoueur1, champJoueur2;
//    private String partieSelectionnee, niveauIASelectionne; // Corrigé faute de frappe
//
//
//    public EcranDeDemarrage(Jeu jeu, CollecteurEvenements collecteurEvenements, InterfaceGraphique interfaceGraphique, Locale locale) {
//        this.jeu = jeu;
//        this.collecteurEvenements = collecteurEvenements;
//        this.interfaceGraphique = interfaceGraphique;
//        this.modeAutoIA = false; // État initial par défaut
//        this.adaptateurBoutonEntrer = new AdaptateurBoutonEntrer(collecteurEvenements, interfaceGraphique);
//
//        // --- Charger le ResourceBundle ---
//        try {
//            this.messages = ResourceBundle.getBundle("AppTextes", locale); // "AppTextes" est le basename
//        } catch (Exception e) {
//            System.err.println("Erreur lors du chargement du ResourceBundle 'AppTextes' pour la locale " + locale + " : " + e.getMessage());
//            // Gérer l'erreur : soit utiliser les clés comme texte, soit afficher un message d'erreur visible.
//            // Pour cet exemple, si messages est nul, on utilisera les clés.
//            this.messages = null;
//        }
//
//
//        creerInterface();
//        initialiserEtatComposantsGeneral(); // Initialiser l'état activé/désactivé au démarrage
//    }
//
//    // Helper pour obtenir le texte localisé, ou la clé si le bundle est manquant
//    private String getText(String key) {
//        return messages != null ? messages.getString(key) : key;
//    }
//
//
//    /** Création de l'Interface */
//    private void creerInterface() {
//        // l'apparence du JTabbedPane si désiré (par exemple, position des onglets)
//        // Attention : Le dernier border appliqué écrase les précédents.
//        // Si vous voulez à la fois un LineBorder ET un EmptyBorder, utilisez CompoundBorder.
//        // this.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3)); // Exemple si voulu
//        this.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Marge intérieure
//
//        // Créer et ajouter les onglets
//        // Utiliser getText() pour les titres des onglets
//        addTab(null, creerOngletGeneral());
//        setTabComponentAt(0, creerJPanel(getText(AppTextKeys.TITRE_ONGLET_GENERAL))); // creerJPanel doit probablement prendre une String pour le JLabel
//
//        addTab(null, creerOngletIA());
//        setTabComponentAt(1, creerJPanel(getText(AppTextKeys.TITRE_ONGLET_IA)));
//
//        addTab(null, creerOngletCouleur());
//        setTabComponentAt(2, creerJPanel(getText(AppTextKeys.TITRE_ONGLET_COULEUR)));
//
//        addTab(null, creerOngletAnimation());
//        setTabComponentAt(3, creerJPanel(getText(AppTextKeys.TITRE_ONGLET_ANIMATION)));
//
//        addTab(null, creerOngletSon());
//        setTabComponentAt(4, creerJPanel(getText(AppTextKeys.TITRE_ONGLET_SON)));
//    }
//
//
//
//
//    /**
//     * Crée l'onglet principal "Général" de configuration en utilisant GridBagLayout.
//     * Utilise les textes du ResourceBundle.
//     * @return Le JPanel de l'onglet Général.
//     */
//    private JPanel creerOngletGeneral() {
//        PanelAvecImage ongletGeneral = new PanelAvecImage(PATH_ARRIERE_PLAN_ED_O2);
//        ongletGeneral.setLayout(new GridBagLayout());
//
//        // --- Titre ---
//        GridBagConstraints contraintes = new GridBagConstraints(); // Nouvelle instance
//        contraintes.fill = GridBagConstraints.VERTICAL;
//        contraintes.weighty = 0.2;
//        contraintes.gridx = COLONNE_ETIQUETTE;
//        contraintes.gridy = 3;
//        contraintes.gridwidth = 2; // S'étend sur les deux colonnes
//        contraintes.insets = MARGES_TITRE;
//        contraintes.anchor = GridBagConstraints.CENTER;
//
//        JLabel titre = new JLabel(getText(AppTextKeys.LBL_TITRE_CONFIG)); // Texte localisé
//        titre.setFont(FONT_TITRE);
//        ongletGeneral.add(titre, contraintes);
//
//        int ligneCourante = 6; // Commencer à ajouter les composants à partir de cette ligne
//
//
//        // --- Ligne 1 : Mode Auto ---
//        // Bouton avec image (texte non pertinent)
//        boutonModeAuto = creerBoutonAvecImage(PATH_BTN_MODE_AUTO_OFF).bouton; // État par défaut
//        boutonModeAuto.setPreferredSize(new Dimension(62,35));
//        boutonModeAuto.setFont(FONT_COMPOSANT);
//        boutonModeAuto.addActionListener(e -> {
//            modeAutoIA = ! modeAutoIA;
//            ImageIcon iconOFF = new ImageIcon(PATH_BTN_MODE_AUTO_OFF.toString());
//            ImageIcon iconON = new ImageIcon(PATH_BTN_MODE_AUTO_ON.toString());
//            if (modeAutoIA) boutonModeAuto.setIcon(iconON);
//            else  boutonModeAuto.setIcon(iconOFF);
//            adaptateurBoutonEntrer.setModeAutoIA(modeAutoIA);
//            mettreAJourEtatComposantsGeneral(); // Appelle la méthode centralisée
//        });
//        // Utiliser getText() pour le libellé
//        ajouterLigne(ongletGeneral, getText(AppTextKeys.LBL_MODE_AUTO), boutonModeAuto, ligneCourante++, FONT_LABEL);
//
//
//        // --- Ligne 2 : Reprendre une partie ---
//        // Les options doivent être chargées depuis le bundle ou un modèle si elles sont dynamiques.
//        // Ici, pour l'exemple, on les charge depuis le bundle si disponible.
//        String indicationSelection = getText(AppTextKeys.INDICATION_SELECTION);
//        String[] optionsReprendre;
//        if (messages != null) {
//            // Supposons que le bundle contient une clé comme "OPTIONS_REPRENDRE_LIST=Partie 1,Partie 2,Partie 3"
//            // Il faudrait une méthode pour charger et parser cette chaîne en tableau.
//            // Pour l'exemple, on utilise le tableau codé en dur ou chargé simplement si possible.
//            // Idealement, ce tableau viendrait du modèle 'jeu'.
//            optionsReprendre = new String[]{indicationSelection, "Partie 1", "Partie 2", "Partie 3"}; // TODO charger depuis jeu/config
//        } else {
//            optionsReprendre = new String[]{indicationSelection, "Partie 1", "Partie 2", "Partie 3"}; // Fallback
//        }
//
//        listeDeroulanteReprendre = creerListeDeroulanteAvecIndication(optionsReprendre, indicationSelection); // Passer l'indication localisée
//        listeDeroulanteReprendre.setPreferredSize(DIMENSION_CHAMP_LISTE_DEROULANTE);
//        // L'adaptateur doit stocker la clé ou une représentation indépendante de la langue si possible
//        adaptateurBoutonEntrer.setPartieSelectionnee(indicationSelection); // Utilisez l'indication localisée
//        listeDeroulanteReprendre.addActionListener(e -> {
//            partieSelectionnee = (String) listeDeroulanteReprendre.getSelectedItem(); // Récupère la valeur affichée
//            adaptateurBoutonEntrer.setPartieSelectionnee(partieSelectionnee); // Notifie l'adaptateur
//            mettreAJourEtatComposantsGeneral(); // Appelle la méthode centralisée
//        });
//        ajouterLigne(ongletGeneral, getText(AppTextKeys.LBL_REPRENDRE), listeDeroulanteReprendre, ligneCourante++, FONT_LABEL); // Texte localisé
//
//
//        // --- Ligne 3 : Jouer avec l'IA ---
//        // Les options doivent être chargées depuis le bundle ou un modèle.
//        String optionIANon = getText(AppTextKeys.OPTION_IA_NON); // Obtenir le texte "Non" localisé
//        String[] optionsIA;
//        if (messages != null) {
//            // Supposons une clé comme "OPTIONS_IA_LIST=Non,Facile,Intermédiaire,Difficile"
//            optionsIA = new String[]{optionIANon, "Facile", "Intermédiaire", "Difficile"}; // TODO charger depuis jeu/config ou bundle
//        } else {
//            optionsIA = new String[]{optionIANon, "Facile", "Intermédiaire", "Difficile"}; // Fallback
//        }
//
//        listeDeroulanteIA = creerListeDeroulanteAvecIndication(optionsIA, optionIANon); // Utiliser le texte "Non" localisé comme indication
//        listeDeroulanteIA.setPreferredSize(DIMENSION_CHAMP_LISTE_DEROULANTE);
//        // L'adaptateur stocke le texte localisé sélectionné
//        adaptateurBoutonEntrer.setNiveauIAselectione(optionIANon); // Stocke l'indication localisée
//        listeDeroulanteIA.addActionListener(e -> {
//            // Stocke la valeur affichée (localisée)
//            niveauIASelectionne = (String) listeDeroulanteIA.getSelectedItem(); // Corrigé nom variable
//            adaptateurBoutonEntrer.setNiveauIAselectione(niveauIASelectionne); // Notifie l'adaptateur
//            mettreAJourEtatComposantsGeneral(); // Appelle la méthode centralisée
//        });
//        ajouterLigne(ongletGeneral, getText(AppTextKeys.LBL_JOUER_IA), listeDeroulanteIA, ligneCourante++, FONT_LABEL); // Texte localisé
//
//
//        // --- Ligne 4 : Nom Joueur 1 ---
//        champJoueur1 = new JTextField(LARGEUR_CHAMP_TEXTE);
//        champJoueur1.setFont(FONT_COMPOSANT);
//        champJoueur1.getDocument().addDocumentListener(new DocumentListener() {
//            public void changedUpdate(DocumentEvent e) { mettreAjourJoueur1(); } // Si le style du texte change
//            public void removeUpdate(DocumentEvent e) { mettreAjourJoueur1(); }  // Si le texte est supprimé
//            public void insertUpdate(DocumentEvent e) { mettreAjourJoueur1(); }  // Si un nouveau texte est inséré
//            private void mettreAjourJoueur1(){
//                // Le border reset ici est discutable, peut-être le gérer après validation dans l'adaptateur
//                // champJoueur1.setBorder(UIManager.getBorder("TextField.border"));
//                adaptateurBoutonEntrer.setChampJoueur(1, champJoueur1);
//            }
//        });
//        // TODO: Pré-remplir avec un nom par défaut depuis modèle/config
//        champJoueur1.setText("Rinel"); // Texte par défaut (peut-être aussi géré via config/bundle pour des suggestions)
//        ajouterLigne(ongletGeneral, getText(AppTextKeys.LBL_JOUEUR_1), champJoueur1, ligneCourante++, FONT_LABEL); // Texte localisé
//
//
//        // --- Ligne 5 : Nom Joueur 2 ---
//        champJoueur2 = new JTextField(LARGEUR_CHAMP_TEXTE);
//        champJoueur2.setFont(FONT_COMPOSANT); // Appliquer la police standard
//        champJoueur2.getDocument().addDocumentListener(new DocumentListener() {
//            public void changedUpdate(DocumentEvent e) { mettreAjourJoueur2(); }
//            public void removeUpdate(DocumentEvent e) { mettreAjourJoueur2(); }
//            public void insertUpdate(DocumentEvent e) { mettreAjourJoueur2(); }
//            private void mettreAjourJoueur2(){
//                // Le border reset ici est discutable
//                // champJoueur2.setBorder(UIManager.getBorder("TextField.border"));
//                adaptateurBoutonEntrer.setChampJoueur(2, champJoueur2);
//            }
//        });
//        // TODO: Pré-remplir avec un nom par défaut depuis modèle/config
//        champJoueur2.setText("Arthur"); // Texte par défaut
//        ajouterLigne(ongletGeneral, getText(AppTextKeys.LBL_JOUEUR_2), champJoueur2, ligneCourante++, FONT_LABEL); // Texte localisé
//
//        // -- bouton enter
//        JButton entrer = creerBoutonAvecImage(PATH_BTN_ENTRER).bouton;
//        entrer.setPreferredSize(new Dimension(98, 98));
//        contraintes = new GridBagConstraints(); // Nouvelle instance pour le bouton Entrer
//        contraintes.gridx = 6; // Placé dans une colonne à part
//        contraintes.gridy = ligneCourante - 1 ; // Placer dans la ligne du dernier champ ou en dessous
//        contraintes.fill = GridBagConstraints.NONE;
//        contraintes.anchor = GridBagConstraints.LINE_END; // Aligner à droite
//        contraintes.insets = new Insets(0, 20, 0, 0); // Marge à gauche
//        contraintes.gridheight = 2; // S'étend sur deux lignes
//        entrer.addActionListener(adaptateurBoutonEntrer);
//        ongletGeneral.add(entrer, contraintes);
//
//        // --- Espace Vertical Flexible (Glue) ---
//        // Pousse tous les composants vers le haut lorsque le panneau est redimensionné verticalement.
//        contraintes = new GridBagConstraints(); // Nouvelle instance
//        contraintes.gridx = 0;
//        contraintes.gridy = ligneCourante; // Placer dans la prochaine ligne disponible
//        contraintes.weighty = 0.5;        // Prend tout l'espace vertical supplémentaire
//        contraintes.fill = GridBagConstraints.VERTICAL;
//        contraintes.gridwidth = GridBagConstraints.REMAINDER; // Prend toute la largeur restante si besoin (sécurité)
//        ongletGeneral.add(Box.createVerticalGlue(), contraintes);
//
//        return ongletGeneral;
//    }
//
//    /**
//     * Met à jour l'état activé/désactivé des composants de l'onglet Général
//     * en fonction des sélections actuelles (mode auto, partie sauvegardée, IA).
//     */
//    private void initialiserEtatComposantsGeneral() {
//        // Lire les valeurs initiales depuis le modèle/config (TODO)
//        // Exemple : boolean partieRepriseChargee = jeu.isPartieRepriseChargee();
//        // Exemple : boolean iaChoisieInitialement = !jeu.getNiveauIA().equals("Non"); // ou une autre logique
//        boolean partieRepriseChargee = !getText(AppTextKeys.INDICATION_SELECTION).equals(partieSelectionnee); // Base sur la sélection actuelle
//        boolean iaChoisieInitialement = !getText(AppTextKeys.OPTION_IA_NON).equals(niveauIASelectionne); // Base sur la sélection actuelle
//
//        // Applique l'état initial basé sur les valeurs de démarrage
//        boutonModeAuto.setIcon(modeAutoIA ? new ImageIcon(PATH_BTN_MODE_AUTO_ON.toString()) : new ImageIcon(PATH_BTN_MODE_AUTO_OFF.toString()));
//        listeDeroulanteReprendre.setEnabled(!modeAutoIA);
//        listeDeroulanteIA.setEnabled(!modeAutoIA);
//        champJoueur1.setEnabled(!modeAutoIA);
//        champJoueur2.setEnabled(!modeAutoIA); // Désactivé par défaut si mode auto
//
//        // Ajustements supplémentaires si une partie est reprise ou si l'IA est choisie (et pas en mode auto)
//        if (!modeAutoIA) {
//            listeDeroulanteReprendre.setEnabled(true); // On peut toujours choisir de reprendre
//            listeDeroulanteIA.setEnabled(true);     // On peut toujours choisir l'IA
//
//            // Si une partie est reprise, on gèle tout sauf le nom du joueur 1
//            if (partieRepriseChargee) {
//                listeDeroulanteIA.setEnabled(false);
//                champJoueur1.setEnabled(true);
//                champJoueur2.setEnabled(false);
//            } else { // Pas de partie reprise
//                champJoueur1.setEnabled(true); // On peut toujours saisir J1
//                // Si IA est choisie, on gèle J2
//                if (iaChoisieInitialement) {
//                    champJoueur2.setEnabled(false);
//                } else { // Pas d'IA choisie et pas de partie reprise
//                    champJoueur2.setEnabled(true); // On peut saisir J2
//                }
//            }
//        }
//        // S'assurer que la couleur de l'indication est correcte au démarrage
//        if (listeDeroulanteReprendre.getSelectedIndex() == 0) {
//            listeDeroulanteReprendre.setForeground(Color.GRAY);
//        } else {
//            listeDeroulanteReprendre.setForeground(Color.BLACK);
//        }
//        if (listeDeroulanteIA.getSelectedIndex() == 0) {
//            listeDeroulanteIA.setForeground(Color.GRAY);
//        } else {
//            listeDeroulanteIA.setForeground(Color.BLACK);
//        }
//
//        adaptateurBoutonEntrer.setModeAutoIA(modeAutoIA);
//        adaptateurBoutonEntrer.setPartieSelectionnee(partieSelectionnee); // Utilisez la valeur localisée
//        adaptateurBoutonEntrer.setNiveauIAselectione(niveauIASelectionne); // Utilisez la valeur localisée
//        // Notifier l'adaptateur des noms initiaux (même s'ils sont par défaut)
//        adaptateurBoutonEntrer.setChampJoueur(1, champJoueur1);
//        adaptateurBoutonEntrer.setChampJoueur(2, champJoueur2);
//
//    }
//
//    /**
//     * Méthode centralisée pour mettre à jour l'état activé/désactivé des composants
//     * de l'onglet Général après une interaction utilisateur.
//     */
//    private void mettreAJourEtatComposantsGeneral() {
//        // Geler toutes les options si le mode auto est actif
//        if (modeAutoIA) {
//            listeDeroulanteReprendre.setEnabled(false);
//            listeDeroulanteIA.setEnabled(false);
//            champJoueur1.setEnabled(false);
//            champJoueur2.setEnabled(false);
//        } else {
//            // Activer par défaut quand on n'est PAS en mode auto
//            listeDeroulanteReprendre.setEnabled(true);
//            listeDeroulanteIA.setEnabled(true);
//            champJoueur1.setEnabled(true);
//            champJoueur2.setEnabled(true); // Activer par défaut J2 en mode JcJ
//
//            boolean partieSelectionneeValide = !getText(AppTextKeys.INDICATION_SELECTION).equals(partieSelectionnee);
//            boolean iaSelectionnee = !getText(AppTextKeys.OPTION_IA_NON).equals(niveauIASelectionne);
//
//            // Si une partie est sélectionnée (non-indication)
//            if (partieSelectionneeValide) {
//                // Geler les options d'IA et le champ Joueur 2
//                listeDeroulanteIA.setEnabled(false);
//                champJoueur2.setEnabled(false);
//            }
//            // Si une IA est sélectionnée (non-"Non")
//            else if (iaSelectionnee) {
//                // Geler l'option "Reprendre" et le champ Joueur 2
//                listeDeroulanteReprendre.setEnabled(false);
//                champJoueur2.setEnabled(false);
//            }
//            // Sinon (mode JcJ par défaut, pas de partie reprise, pas d'IA) : tout est actif sauf mode auto (déjà géré)
//        }
//
//        // Mettre à jour la couleur du texte de l'indication dans les JComboBox
//        // C'est redondant si le renderer fonctionne bien, mais ajouté pour illustration.
//        // Idéalement, le renderer seul devrait suffire.
//        if (listeDeroulanteReprendre.getSelectedIndex() == 0) {
//            listeDeroulanteReprendre.setForeground(Color.GRAY);
//        } else {
//            listeDeroulanteReprendre.setForeground(Color.BLACK);
//        }
//        if (listeDeroulanteIA.getSelectedIndex() == 0) {
//            listeDeroulanteIA.setForeground(Color.GRAY);
//        } else {
//            listeDeroulanteIA.setForeground(Color.BLACK);
//        }
//
//        // Forcer le rafraîchissement des listes déroulantes pour que la couleur s'applique si le renderer est simple
//        listeDeroulanteReprendre.repaint();
//        listeDeroulanteIA.repaint();
//    }
//
//
//
//    /**
//     * Crée l'onglet de configuration de l'IA.
//     * Utilise les textes du ResourceBundle.
//     * @return Le JPanel de l'onglet IA.
//     */
//    private JPanel creerOngletIA() {
//        JPanelAvecCouleurDebraille ongletIA = new JPanelAvecCouleurDebraille(new Color(127, 157, 172),new Color(112, 112, 112) );
//        ongletIA.setLayout(new GridBagLayout());
//        GridBagConstraints contraintes; // Déclaration ici, nouvelle instance pour le titre
//        int ligneCourante = 0;
//
//        // --- Titre ---
//        contraintes = new GridBagConstraints(); // Nouvelle instance
//        contraintes.gridx = COLONNE_ETIQUETTE;
//        contraintes.gridy = ligneCourante++;
//        contraintes.weighty = 0.5;
//        contraintes.fill = GridBagConstraints.VERTICAL;
//        contraintes.gridwidth = 4; // S'étend sur plusieurs colonnes
//        contraintes.anchor = GridBagConstraints.CENTER;
//        contraintes.insets = MARGES_TITRE;
//
//        JLabel titre = new JLabel(getText(AppTextKeys.LBL_TITRE_IA)); // Texte localisé
//        titre.setFont(FONT_TITRE);
//        ongletIA.add(titre, contraintes);
//
//        // --- Temps de réflexion IA ---
//        JSlider sliderTempsReflexion = new JSlider(100, 5000, 1000); // min, max, valeur initiale (en ms)
//        sliderTempsReflexion.setMajorTickSpacing(1000);
//        sliderTempsReflexion.setMinorTickSpacing(100);
//        sliderTempsReflexion.setPaintTicks(true);
//        sliderTempsReflexion.setPaintLabels(true); // Afficher les valeurs numériques majeures
//        sliderTempsReflexion.setPreferredSize(new Dimension(300,50));
//        sliderTempsReflexion.addChangeListener(e -> {
//            if (!sliderTempsReflexion.getValueIsAdjusting()) { // Agir seulement quand on relâche le curseur
//                collecteurEvenements.configIAReflexion(sliderTempsReflexion.getValue());
//            }
//        });
//        // TODO Pré-remplir avec la valeur actuelle du modèle/config si disponible
//        // sliderTempsReflexion.setValue(jeu.getConfigIAReflexion());
//        ajouterLigne(ongletIA, getText(AppTextKeys.LBL_TEMPS_REFLEXION), sliderTempsReflexion, ligneCourante++, FONT_LABEL); // Texte localisé
//
//        // --- Heuristique Avancée ---
//        JCheckBox checkHeuristique = new JCheckBox();
//        checkHeuristique.setFont(FONT_COMPOSANT);
//        checkHeuristique.addActionListener(e -> {
//            collecteurEvenements.configIAHeuristique(checkHeuristique.isSelected());
//        });
//        // TODO Pré-remplir avec la valeur actuelle du modèle/config si disponible
//        // checkHeuristique.setSelected(jeu.getConfigIAHeuristique());
//        ajouterLigne(ongletIA, getText(AppTextKeys.LBL_HEURISTIQUE_AVANCEE), checkHeuristique, ligneCourante++, FONT_LABEL); // Texte localisé
//
//
//        // --- Choix Algorithme IA ---
//        // Les options doivent être chargées depuis le bundle ou un modèle.
//        String[] optionsAlgoIA;
//        if (messages != null) {
//            // Supposons une clé comme "OPTIONS_ALGORITHME_IA_LIST=Minimax Simple,Alpha-Beta,Monte Carlo"
//            optionsAlgoIA = new String[]{"Minimax Simple", "Alpha-Beta", "Monte Carlo"}; // TODO charger depuis jeu/config ou bundle
//        } else {
//            optionsAlgoIA = new String[]{"Minimax Simple", "Alpha-Beta", "Monte Carlo"}; // Fallback
//        }
//
//        JComboBox<String> comboAlgoIA = new JComboBox<>(optionsAlgoIA);
//        comboAlgoIA.setFont(FONT_COMPOSANT);
//        comboAlgoIA.addActionListener(e -> {
//            String selection = (String) comboAlgoIA.getSelectedItem();
//            // L'adaptateur/contrôleur doit savoir mapper le texte localisé à une valeur interne
//            collecteurEvenements.configIAAlgorithme(selection); // Passe la valeur localisée
//        });
//        // TODO Pré-remplir avec la valeur actuelle du modèle/config si disponible
//        // comboAlgoIA.setSelectedItem(jeu.getConfigIAAlgorithme()); // Attention si la valeur stockée est une clé ou la valeur localisée
//        ajouterLigne(ongletIA, getText(AppTextKeys.LBL_ALGORITHME_IA), comboAlgoIA, ligneCourante++, FONT_LABEL); // Texte localisé
//
//
//        // --- Espace Vertical Flexible ---
//        contraintes = new GridBagConstraints(); // Nouvelle instance
//        contraintes.gridx = 0;
//        contraintes.gridy = ligneCourante;
//        contraintes.weighty = 1.0;
//        contraintes.fill = GridBagConstraints.VERTICAL;
//        contraintes.gridwidth = GridBagConstraints.REMAINDER; // Prend toute la largeur restante
//        ongletIA.add(Box.createVerticalGlue(), contraintes);
//
//        return ongletIA;
//    }
//
//
//
//
//    /**
//     * Crée l'onglet de personnalisation des couleurs.
//     * Utilise les textes du ResourceBundle.
//     * @return Le JPanel de l'onglet Couleur.
//     */
//    private JPanel creerOngletCouleur() {
//        JPanel ongletCouleur = new JPanel(new GridBagLayout());
//        GridBagConstraints contraintes; // Déclaration ici, nouvelle instance pour le titre
//        int ligneCourante = 0;
//
//        // --- Titre ---
//        contraintes = new GridBagConstraints(); // Nouvelle instance
//        contraintes.gridx = 0;
//        contraintes.gridy = ligneCourante++;
//        contraintes.weighty = 0.5;
//        contraintes.gridwidth = 3; // S'étend sur 3 colonnes (Label, Preview, Button)
//        contraintes.anchor = GridBagConstraints.CENTER;
//        contraintes.insets = MARGES_TITRE;
//        JLabel titre = new JLabel(getText(AppTextKeys.LBL_TITRE_COULEUR)); // Texte localisé
//        titre.setFont(FONT_TITRE);
//        ongletCouleur.add(titre, contraintes);
//
//        // Ajouter les sélecteurs de couleur - utiliser getText() pour les libellés
//        ajouterLigneCouleur(ongletCouleur, getText(AppTextKeys.LBL_PLATEAU_DE_JEU), COULEUR_PLATEAU_DE_JEU, ligneCourante++, Config.CiblesDesCouleurs.PLATEAU_DE_JEU);
//        ajouterLigneCouleur(ongletCouleur, getText(AppTextKeys.LBL_CASE_TERRAIN), COULEUR_CASE_TERRAIN, ligneCourante++, Config.CiblesDesCouleurs.CASE_TERRAIN);
//        ajouterLigneCouleur(ongletCouleur, getText(AppTextKeys.LBL_CASE_MAITRE_JOUEUR_1),COULEUR_CASE_MAITRE_JOUEUR_1, ligneCourante++, Config.CiblesDesCouleurs.CASE_MAITRE_JOUEUR_1);
//        ajouterLigneCouleur(ongletCouleur, getText(AppTextKeys.LBL_CASE_MAITRE_JOUEUR_2), COULEUR_CASE_MAITRE_JOUEUR_2, ligneCourante++, Config.CiblesDesCouleurs.CASE_MAITRE_JOUEUR_2);
//        ajouterLigneCouleur(ongletCouleur, getText(AppTextKeys.LBL_CASE_ELEVE_JOUEUR_1), COULEUR_CASE_ELEVE_JOUEUR_1, ligneCourante++, Config.CiblesDesCouleurs.CASE_ELEVE_JOUEUR_1);
//        ajouterLigneCouleur(ongletCouleur, getText(AppTextKeys.LBL_CASE_ELEVE_JOUEUR_2), COULEUR_CASE_ELEVE_JOUEUR_2, ligneCourante++, Config.CiblesDesCouleurs.CASE_ELEVE_JOUEUR_2);
//        ajouterLigneCouleur(ongletCouleur, getText(AppTextKeys.LBL_BLOC_MENU), COULEUR_BLOC_MENU, ligneCourante++, Config.CiblesDesCouleurs.BLOC_MENU);
//
//
//        // --- Espace Vertical Flexible ---
//        contraintes = new GridBagConstraints(); // Nouvelle instance
//        contraintes.gridx = 0;
//        contraintes.gridy = ligneCourante;
//        contraintes.weighty = 1.0;
//        contraintes.fill = GridBagConstraints.VERTICAL;
//        contraintes.gridwidth = GridBagConstraints.REMAINDER; // Prend toute la largeur restante
//        ongletCouleur.add(Box.createVerticalGlue(), contraintes);
//
//        return ongletCouleur;
//    }
//
//
//
//
//    /**
//     * Méthode utilitaire pour ajouter une ligne de sélection de couleur.
//     * Utilise les textes du ResourceBundle.
//     * @param panneau        Le JPanel utilisant GridBagLayout.
//     * @param texteEtiquette Le texte pour le JLabel (déjà localisé).
//     * @param couleurInitiale La couleur initiale du preview.
//     * @param ligne          La valeur gridy pour cette ligne.
//     * @param cible          La cible de la couleur pour la configuration.
//     */
//    private void ajouterLigneCouleur(JPanel panneau, String texteEtiquette, Color couleurInitiale, int ligne, Config.CiblesDesCouleurs cible) {
//        // --- Étiquette ---
//        GridBagConstraints contraintesLabel = new GridBagConstraints();
//        contraintesLabel.gridx = 0;
//        contraintesLabel.gridy = ligne;
//        contraintesLabel.anchor = GridBagConstraints.LINE_END;
//        contraintesLabel.insets = MARGES_DEFAUT;
//        contraintesLabel.weightx = 0.1; // Donne un peu d'espace à l'étiquette si le panneau s'étire
//        contraintesLabel.fill = GridBagConstraints.HORIZONTAL; // Permet à l'étiquette de prendre la place horizontale
//
//        JLabel etiquette = new JLabel(texteEtiquette); // Texte déjà localisé
//        etiquette.setFont(FONT_LABEL);
//        panneau.add(etiquette, contraintesLabel);
//
//        // --- Panneau de prévisualisation ---
//        JPanel previewPanel = new JPanel();
//        previewPanel.setPreferredSize(DIM_PREVIEW_COULEUR);
//        previewPanel.setBackground(couleurInitiale);
//        previewPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
//        GridBagConstraints contraintesPreview = new GridBagConstraints();
//        contraintesPreview.gridx = 1;
//        contraintesPreview.gridy = ligne;
//        contraintesPreview.insets = new Insets(5, 5, 5, 5); // Marges plus serrées
//        contraintesPreview.anchor = GridBagConstraints.CENTER;
//
//        panneau.add(previewPanel, contraintesPreview);
//
//        // --- Bouton Choisir ---
//        JButton boutonChoisir = new JButton(getText(AppTextKeys.BTN_CHOISIR_COULEUR)); // Texte localisé
//        boutonChoisir.setFont(FONT_COMPOSANT);
//        boutonChoisir.addActionListener(e -> {
//            Color couleurChoisie = JColorChooser.showDialog(
//                    EcranDeDemarrage.this, // Parent component
//                    getText(AppTextKeys.BTN_CHOISIR_COULEUR) + " " + texteEtiquette, // Titre de la boîte de dialogue
//                    previewPanel.getBackground() // Couleur initiale
//            );
//            if (couleurChoisie != null) {
//                previewPanel.setBackground(couleurChoisie); // Mettre à jour la prévisualisation
//                collecteurEvenements.configCouleur(cible, couleurChoisie); // Notifier le contrôleur avec la couleur choisie ET la cible
//            }
//        });
//        GridBagConstraints contraintesBouton = new GridBagConstraints();
//        contraintesBouton.gridx = 2;
//        contraintesBouton.gridy = ligne;
//        contraintesBouton.anchor = GridBagConstraints.LINE_START;
//        contraintesBouton.insets = MARGES_DEFAUT;
//        contraintesBouton.weightx = 0.1; // Donne un peu d'espace au bouton si le panneau s'étire
//        contraintesBouton.fill = GridBagConstraints.HORIZONTAL; // Permet au bouton de prendre la place horizontale
//
//        panneau.add(boutonChoisir, contraintesBouton);
//
//        // Ajouter un peu de "glue" horizontale après les colonnes fixes pour pousser le reste à gauche si besoin
//        GridBagConstraints horizontalGlueConstraints = new GridBagConstraints();
//        horizontalGlueConstraints.gridx = 3; // Colonne après le bouton
//        horizontalGlueConstraints.gridy = ligne;
//        horizontalGlueConstraints.weightx = 1.0; // Prend tout l'espace horizontal restant
//        horizontalGlueConstraints.fill = GridBagConstraints.HORIZONTAL;
//        panneau.add(Box.createHorizontalGlue(), horizontalGlueConstraints);
//    }
//
//
//
//
//    /**
//     * Crée l'onglet de configuration des animations.
//     * Utilise les textes du ResourceBundle.
//     * @return Le JPanel de l'onglet Animation.
//     */
//    private JPanel creerOngletAnimation() {
//        BruitGrisAvecPointsPanel ongletAnimation = new BruitGrisAvecPointsPanel();
//        ongletAnimation.setLayout(new GridBagLayout());
//        GridBagConstraints contraintes; // Déclaration ici, nouvelle instance pour le titre
//        int ligneCourante = 0;
//
//        // --- Titre ---
//        contraintes = new GridBagConstraints(); // Nouvelle instance
//        contraintes.gridx = COLONNE_ETIQUETTE;
//        contraintes.gridy = ligneCourante++;
//        contraintes.weighty = 0.5;
//        contraintes.fill = GridBagConstraints.VERTICAL;
//        contraintes.gridwidth = 4; // S'étend sur plusieurs colonnes
//        contraintes.anchor = GridBagConstraints.CENTER;
//        contraintes.insets = MARGES_TITRE;
//
//        JLabel titre = new JLabel(getText(AppTextKeys.LBL_TITRE_ANIMATION)); // Texte localisé
//        titre.setFont(FONT_TITRE);
//        ongletAnimation.add(titre, contraintes);
//
//        // --- Vitesse d'animation ---
//        JSlider sliderVitesse = new JSlider(0, 100, 50); // Exemple : 0=Instantanné, 100=Très lent, 50=Normal
//        sliderVitesse.setMajorTickSpacing(25);
//        sliderVitesse.setMinorTickSpacing(5);
//        sliderVitesse.setPaintTicks(true);
//        sliderVitesse.setPaintLabels(true);
//        sliderVitesse.addChangeListener(e -> {
//            if (!sliderVitesse.getValueIsAdjusting()) {
//                collecteurEvenements.configAnimationVitesse(sliderVitesse.getValue());
//            }
//        });
//        // TODO potentiellement une valeur par défaut
//        //  sliderVitesse.setValue(jeu.getConfigAnimationVitesse());
//        ajouterLigne(ongletAnimation, getText(AppTextKeys.LBL_VITESSE_ANIMATION), sliderVitesse, ligneCourante++, FONT_LABEL); // Texte localisé
//
//        // --- Activer/Désactiver Animation Pièces ---
//        JCheckBox checkAnimPieces = new JCheckBox();
//        checkAnimPieces.setFont(FONT_COMPOSANT);
//        checkAnimPieces.addActionListener(e -> {
//            collecteurEvenements.configAnimationPieces(checkAnimPieces.isSelected());
//        });
//        // TODO potentiellement une valeur par défaut
//        //  checkAnimPieces.setSelected(jeu.getConfigAnimationPieces());
//        ajouterLigne(ongletAnimation, getText(AppTextKeys.LBL_ANIMATION_PIECES), checkAnimPieces, ligneCourante++, FONT_LABEL); // Texte localisé
//
//        // --- Activer/Désactiver Animation Surbrillance ---
//        JCheckBox checkAnimSurbrillance = new JCheckBox();
//        checkAnimSurbrillance.setFont(FONT_COMPOSANT);
//        checkAnimSurbrillance.addActionListener(e -> {
//            collecteurEvenements.configAnimationSurbrillance(checkAnimSurbrillance.isSelected());
//        });
//        // TODO potentiellement une valeur par défaut
//        //  checkAnimSurbrillance.setSelected(jeu.getConfigAnimationSurbrillance());
//        ajouterLigne(ongletAnimation, getText(AppTextKeys.LBL_ANIMATION_SURBRILLANCE), checkAnimSurbrillance, ligneCourante++, FONT_LABEL); // Texte localisé
//
//        // --- Espace Vertical Flexible ---
//        contraintes = new GridBagConstraints(); // Nouvelle instance
//        contraintes.gridx = 0;
//        contraintes.gridy = ligneCourante;
//        contraintes.weighty = 1.0;
//        contraintes.fill = GridBagConstraints.VERTICAL;
//        contraintes.gridwidth = GridBagConstraints.REMAINDER; // Prend toute la largeur restante
//        ongletAnimation.add(Box.createVerticalGlue(), contraintes);
//
//        return ongletAnimation;
//    }
//
//
//
//
//    /**
//     * Crée l'onglet de configuration audio.
//     * Utilise les textes du ResourceBundle.
//     * @return Le JPanel de l'onglet Son.
//     */
//    private JPanel creerOngletSon() {
//        JPanel ongletSon = new JPanel(new GridBagLayout());
//        GridBagConstraints contraintes; // Déclaration ici, nouvelle instance pour le titre
//        int ligneCourante = 0;
//
//        // --- Titre ---
//        contraintes = new GridBagConstraints(); // Nouvelle instance
//        contraintes.gridx = COLONNE_ETIQUETTE;
//        contraintes.gridy = ligneCourante++;
//        contraintes.weighty = 0.5;
//        contraintes.fill = GridBagConstraints.VERTICAL;
//        contraintes.gridwidth = 4; // S'étend sur plusieurs colonnes
//        contraintes.anchor = GridBagConstraints.CENTER;
//        contraintes.insets = MARGES_TITRE;
//
//        JLabel titre = new JLabel(getText(AppTextKeys.LBL_TITRE_SON)); // Texte localisé
//        titre.setFont(FONT_TITRE);
//        ongletSon.add(titre, contraintes);
//
//        // --- Volume Général ---
//        JSlider sliderVolumeGeneral = new JSlider(0, 100, 75); // 0=Muet, 100=Max
//        sliderVolumeGeneral.setMajorTickSpacing(25);
//        sliderVolumeGeneral.setPaintTicks(true);
//        sliderVolumeGeneral.setPaintLabels(true);
//        sliderVolumeGeneral.addChangeListener(e -> {
//            if (!sliderVolumeGeneral.getValueIsAdjusting()) {
//                collecteurEvenements.configSonVolumeGeneral(sliderVolumeGeneral.getValue());
//                // Peut-être ajuster les autres sliders ou l'état Muet dans le contrôleur
//            }
//        });
//        // TODO potentiellement une valeur par défaut
//        //  sliderVolumeGeneral.setValue(jeu.getConfigSonVolumeGeneral());
//        ajouterLigne(ongletSon, getText(AppTextKeys.LBL_VOLUME_GENERAL), sliderVolumeGeneral, ligneCourante++, FONT_LABEL); // Texte localisé
//
//        // --- Volume Effets Sonores ---
//        JSlider sliderVolumeEffets = new JSlider(0, 100, 80);
//        sliderVolumeEffets.setMajorTickSpacing(25);
//        sliderVolumeEffets.setPaintTicks(true);
//        sliderVolumeEffets.setPaintLabels(true);
//        sliderVolumeEffets.addChangeListener(e -> {
//            if (!sliderVolumeEffets.getValueIsAdjusting()) {
//                collecteurEvenements.configSonVolumeEffets(sliderVolumeEffets.getValue());
//            }
//        });
//        // TODO potentiellement une valeur par défaut
//        //  sliderVolumeEffets.setValue(jeu.getConfigSonVolumeEffets());
//        ajouterLigne(ongletSon, getText(AppTextKeys.LBL_VOLUME_EFFETS), sliderVolumeEffets, ligneCourante++, FONT_LABEL); // Texte localisé
//
//
//        // --- Volume Musique ---
//        JSlider sliderVolumeMusique = new JSlider(0, 100, 60);
//        sliderVolumeMusique.setMajorTickSpacing(25);
//        sliderVolumeMusique.setPaintTicks(true);
//        sliderVolumeMusique.setPaintLabels(true);
//        sliderVolumeMusique.addChangeListener(e -> {
//            if (!sliderVolumeMusique.getValueIsAdjusting()) {
//                collecteurEvenements.configSonVolumeMusique(sliderVolumeMusique.getValue());
//            }
//        });
//        // TODO potentiellement une valeur par défaut
//        //  sliderVolumeMusique.setValue(jeu.getConfigSonVolumeMusique());
//        ajouterLigne(ongletSon, getText(AppTextKeys.LBL_VOLUME_MUSIQUE), sliderVolumeMusique, ligneCourante++, FONT_LABEL); // Texte localisé
//
//
//        // --- Muet ---
//        JCheckBox checkMuet = new JCheckBox();
//        checkMuet.setFont(FONT_COMPOSANT);
//        checkMuet.addActionListener(e -> {
//            boolean estMuet = checkMuet.isSelected();
//            collecteurEvenements.configSonMuet(estMuet);
//            // Désactiver les sliders si muet est coché
//            sliderVolumeGeneral.setEnabled(!estMuet);
//            sliderVolumeEffets.setEnabled(!estMuet);
//            sliderVolumeMusique.setEnabled(!estMuet);
//        });
//        // TODO potentiellement une valeur par défaut et ajuster les sliders initiaux
//        //  boolean isMuted = jeu.getConfigSonMuet();
//        //  checkMuet.setSelected(isMuted);
//        //  sliderVolumeGeneral.setEnabled(!isMuted);
//        //  sliderVolumeEffets.setEnabled(!isMuted);
//        //  sliderVolumeMusique.setEnabled(!isMuted);
//        ajouterLigne(ongletSon, getText(AppTextKeys.LBL_SON_MUET), checkMuet, ligneCourante++, FONT_LABEL); // Texte localisé
//
//
//        // --- Espace Vertical Flexible ---
//        contraintes = new GridBagConstraints(); // Nouvelle instance
//        contraintes.gridx = 0;
//        contraintes.gridy = ligneCourante;
//        contraintes.weighty = 1.0;
//        contraintes.fill = GridBagConstraints.VERTICAL;
//        contraintes.gridwidth = GridBagConstraints.REMAINDER; // Prend toute la largeur restante
//        ongletSon.add(Box.createVerticalGlue(), contraintes);
//
//        return ongletSon;
//    }
//
//
//
//
//    /**
//     * Méthode utilitaire pour ajouter une ligne (étiquette + composant) au GridBagLayout.
//     * Utilise les textes du ResourceBundle pour l'étiquette (qui est passée localisée).
//     *
//     * @param panneau        Le JPanel utilisant GridBagLayout.
//     * @param texteEtiquette Le texte localisé pour le JLabel.
//     * @param composant      Le JComponent à ajouter (ex: JButton, JComboBox).
//     * @param ligne          La valeur gridy pour cette ligne.
//     * @param policeEtiquette La police (Font) pour l'étiquette.
//     */
//
//    private void ajouterLigne(JPanel panneau, String texteEtiquette, JComponent composant, int ligne, Font policeEtiquette) {
//        // --- Contraintes de l'Étiquette ---
//        GridBagConstraints contraintesLabel = new GridBagConstraints();
//        contraintesLabel.gridx = COLONNE_ETIQUETTE;
//        contraintesLabel.gridy = ligne;
//        contraintesLabel.anchor = GridBagConstraints.LINE_END; // Aligner le texte de l'étiquette à droite
//        contraintesLabel.insets = MARGES_DEFAUT;              // Marge autour de l'étiquette
//        contraintesLabel.weighty = 0.1; // Poids vertical pour pousser les lignes vers le haut
//        // contraintesLabel.ipadx = 40; // Cette contrainte peut rendre l'alignement moins flexible, préférer insets ou weightx
//
//        JLabel etiquette = new JLabel(texteEtiquette); // Texte déjà localisé
//        etiquette.setFont(policeEtiquette);
//        panneau.add(etiquette, contraintesLabel);
//
//        // --- Contraintes du Composant ---
//        GridBagConstraints contraintesComp = new GridBagConstraints();
//        contraintesComp.gridx = COLONNE_COMPOSANT;
//        contraintesComp.gridy = ligne;
//        contraintesComp.anchor = GridBagConstraints.LINE_START; // Aligner le composant à gauche
//        contraintesComp.weighty = 0.1; // Poids vertical pour pousser les lignes vers le haut
//        contraintesComp.insets = MARGES_DEFAUT;              // Marge autour du composant
//        contraintesComp.fill = GridBagConstraints.HORIZONTAL; // Permet au composant de s'étirer horizontalement
//        contraintesComp.weightx = 1.0; // Prend l'espace horizontal restant dans sa colonne
//
//        // Appliquer la police standard
//        composant.setFont(FONT_COMPOSANT);
//
//        panneau.add(composant, contraintesComp);
//    }
//
//
//
//
//    /**
//     * Crée une JComboBox avec une indication grisée comme premier élément.
//     * Gère la couleur de l'indication via le Renderer uniquement.
//     *
//     * @param options Les options pour la liste déroulante, incluant l'indication en premier.
//     * @param indicationText Le texte exact de l'indication (localisé).
//     * @return La JComboBox configurée.
//     */
//    private JComboBox<String> creerListeDeroulanteAvecIndication(String[] options, String indicationText) {
//        JComboBox<String> listeDeroulante = new JComboBox<>(options);
//        listeDeroulante.setFont(FONT_COMPOSANT);
//        listeDeroulante.setPreferredSize(new Dimension(LARGEUR_LISTE_DEROULANTE, HAUTEUR_LISTE_DEROULANTE));
//
//        // Utilisation du Renderer pour gérer la couleur de l'indication
//        listeDeroulante.setRenderer(new DefaultListCellRenderer() {
//            @Override
//            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//                // Change la couleur du texte UNIQUEMENT si :
//                // 1. L'élément est l'indication
//                // 2. L'élément est actuellement l'élément SELECTIONNÉ affiché dans la JComboBox
//                // 3. L'élément n'est PAS en train d'être sélectionné dans la liste déroulante (pas 'isSelected')
//                if (value != null && value.equals(indicationText) && listeDeroulante.getSelectedIndex() == index && !isSelected) {
//                    setForeground(Color.GRAY);
//                } else {
//                    setForeground(Color.BLACK); // Couleur par défaut pour les autres options ou quand sélectionné/dans la liste
//                }
//                return this;
//            }
//        });
//        listeDeroulante.setSelectedIndex(0); // Commencer avec l'indication sélectionnée
//
//        // L'ActionListener ne s'occupe plus de la couleur, seulement de notifier l'adaptateur si besoin
//        // Il peut rester si d'autres actions doivent se produire lors de la sélection
//
//        return listeDeroulante;
//    }
//
//    // Exemple d'utilisation dans une JFrame pour tester
//    public static void main(String[] args) {
//        // --- Choix de la Locale ---
//        // Pour tester différentes langues, changez la locale par défaut AVANT de créer l'UI.
//        Locale.setDefault(Locale.ENGLISH); // Test Anglais
//        // Locale.setDefault(Locale.FRENCH); // Test Français
//        // Locale.setDefault(new Locale("es", "ES")); // Test Espagnol (nécessiterait AppTextes_es_ES.properties)
//
//
//        SwingUtilities.invokeLater(() -> {
//            // Créer des mock objects pour les dépendances (simplifié pour l'exemple)
//            Jeu mockJeu = null; // Remplacer par une vraie instance si besoin de tester les TODO
//            CollecteurEvenements mockCollecteur = new CollecteurEvenements() {
//                @Override public void configIAReflexion(int temps) { System.out.println("Config IA Réflexion: " + temps + "ms"); }
//                @Override public void configIAHeuristique(boolean active) { System.out.println("Config IA Heuristique: " + active); }
//                @Override public void configIAAlgorithme(String algo) { System.out.println("Config IA Algorithme: " + algo); }
//                @Override public void configCouleur(Config.CiblesDesCouleurs cible, Color couleur) { System.out.println("Config Couleur " + cible + ": " + couleur); }
//                @Override public void configAnimationVitesse(int vitesse) { System.out.println("Config Animation Vitesse: " + vitesse); }
//                @Override public void configAnimationPieces(boolean active) { System.out.println("Config Animation Pièces: " + active); }
//                @Override public void configAnimationSurbrillance(boolean active) { System.out.println("Config Animation Surbrillance: " + active); }
//                @Override public void configSonVolumeGeneral(int volume) { System.out.println("Config Son Volume Général: " + volume); }
//                @Override public void configSonVolumeEffets(int volume) { System.out.println("Config Son Volume Effets: " + volume); }
//                @Override public void configSonVolumeMusique(int volume) { System.out.println("Config Son Volume Musique: " + volume); }
//                @Override public void configSonMuet(boolean muet) { System.out.println("Config Son Muet: " + muet); }
//                @Override public void nouvellePartie(String nomJoueur1, String nomJoueur2, String niveauIA) { /* ... */ }
//                @Override public void reprendrePartie(String nomPartie) { /* ... */ }
//
//                // Implémentations vides ou mock pour les autres méthodes du CollecteurEvenements si nécessaire
//                @Override public void commencerPartie(String nomJoueur1, String nomJoueur2, String niveauIA) {
//                    System.out.println("Commencer Partie - J1:" + nomJoueur1 + ", J2:" + nomJoueur2 + ", IA:" + niveauIA);
//                }
//                @Override public void annuler() { System.out.println("Annuler"); }
//                // ... autres méthodes du CollecteurEvenements ...
//            };
//            InterfaceGraphique mockInterface = null; // Remplacer si besoin
//
//            JFrame frame = new JFrame("Écran de Démarrage Test");
//            // Passer la locale par défaut au constructeur
//            EcranDeDemarrage ecran = new EcranDeDemarrage(mockJeu, mockCollecteur, mockInterface, Locale.getDefault());
//            frame.getContentPane().add(ecran); // Ajouter le JTabbedPane directement
//
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(600, 600); // Ajuster la taille si nécessaire
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
//}
