package Vue;

import Modele.Jeu;

import java.awt.*;


public class ConfigUI {

    // -- COULEURS
    public final static Color COULEUR_PLATEAU = new Color(104, 104, 104);
    public final static Color COULEUR_BOUTON_PLATEAU = new Color(255, 255, 255);

    // --- INTERFACES
    public final static int WIDTH_MENU = 500;

    // --- PATHS
    public final static String PATH_IMAGE_ARRIERE_PLAN_MENU = "res/vue/images/arrierePlans/menu.png";

    // --- POLICES
    public  final static String POLICE_1 = "Arial";
    public  final static String POLICE_2 = "Times New Roman";
    public  final static String POLICE_3 = "Georgia";





    /******************************************
     *         ECRAN DE DÉMARRAGE             *
     ******************************************/

    // Titres et textes de l'interface utilisateur
    public static final String TITRE_ONGLET_GENERAL = "Général";
    public static final String TITRE_ONGLET_IA = "IA";
    public static final String TITRE_ONGLET_COULEUR = "Couleur";
    public static final String TITRE_ONGLET_ANIMATION = "Animation";
    public static final String TITRE_ONGLET_SON = "Son";

    public static final String LBL_TITRE_CONFIG = "Configuration";
    public static final String LBL_MODE_AUTO = "Mode auto (IA vs IA)";
    public static final String LBL_REPRENDRE = "Reprendre une partie";
    public static final String LBL_JOUER_IA = "Jouer contre l'IA";
    public static final String LBL_JOUEUR_1 = "Joueur 1";
    public static final String LBL_JOUEUR_2 = "Joueur 2";
    public static final String BTN_MODE_AUTO_OFF = "Off"; // Ou "Désactivé"
    public static final String BTN_MODE_AUTO_ON = "On";   // Ou "Activé"

    public static final String INDICATION_SELECTION = "Sélectionner ici...";
    // Exemple, charger dynamiquement ?
    public static final String[] OPTIONS_REPRENDRE = {INDICATION_SELECTION, "Partie 1", "Partie 2", "Partie 3"};
    public static final String[] OPTIONS_IA = {"Non", "Facile", "Intermédiaire", "Difficile"};
    public static final String OPTION_IA_NON = "Non";

    // Polices (Fonts)
    public static final Font FONT_TITRE = new Font(POLICE_1, Font.BOLD, 36); // Taille ajustée
    public static final Font FONT_LABEL = new Font(POLICE_1, Font.PLAIN, 18); // Taille ajustée
    public static final Font FONT_COMPOSANT = new Font(POLICE_1, Font.PLAIN, 16); // Police pour les composants

    // Marges (Insets)
    public static final Insets MARGES_DEFAUT = new Insets(5, 10, 5, 10); // Espacement par défaut
    public static final Insets MARGES_TITRE = new Insets(20, 10, 20, 10); // Espacement pour le titre

    // Dimensions des composants
    public static final int LARGEUR_LISTE_DEROULANTE = 220;
    public static final int HAUTEUR_LISTE_DEROULANTE = 30;
    public static final int LARGEUR_CHAMP_TEXTE = 18; // Colonnes pour JTextField

    // Constantes pour GridBagConstraints
    public static final int COLONNE_ETIQUETTE = 0;
    public static final int COLONNE_COMPOSANT = 1;


    // === Constantes (Suite) ===

    // --- Onglet IA ---
    public static final String LBL_TITRE_IA = "Paramètres de l'Intelligence Artificielle";
    public static final String LBL_TEMPS_REFLEXION = "Temps de réflexion IA (ms)";
    public static final String LBL_HEURISTIQUE_AVANCEE = "Activer heuristique avancée";
    public static final String LBL_ALGORITHME_IA = "Algorithme IA";
    public static final String[] OPTIONS_ALGORITHME_IA = {"Minimax Simple", "Alpha-Beta", "Monte Carlo"}; // Exemple

    // --- Onglet Couleur ---
    public static final String LBL_TITRE_COULEUR = "Personnalisation des Couleurs";
    public static final String LBL_COULEUR_CASE_CLAIRE = "Case Claire";
    public static final String LBL_COULEUR_CASE_FONCEE = "Case Foncée";
    public static final String LBL_COULEUR_JOUEUR_1 = "Pièces Joueur 1";
    public static final String LBL_COULEUR_JOUEUR_2 = "Pièces Joueur 2";
    public static final String LBL_COULEUR_SURBRILLANCE = "Case en Surbrillance";
    public static final String BTN_CHOISIR_COULEUR = "Choisir...";
    public static final Dimension DIM_PREVIEW_COULEUR = new Dimension(30, 30);

    // --- Onglet Animation ---
    public static final String LBL_TITRE_ANIMATION = "Paramètres d'Animation";
    public static final String LBL_VITESSE_ANIMATION = "Vitesse d'animation";
    public static final String LBL_ANIMATION_PIECES = "Animer le déplacement des pièces";
    public static final String LBL_ANIMATION_SURBRILLANCE = "Animer la surbrillance";

    // --- Onglet Son ---
    public static final String LBL_TITRE_SON = "Paramètres Audio";
    public static final String LBL_VOLUME_GENERAL = "Volume Général";
    public static final String LBL_VOLUME_EFFETS = "Volume Effets Sonores";
    public static final String LBL_VOLUME_MUSIQUE = "Volume Musique";
    public static final String LBL_SON_MUET = "Muet (couper tout son)";
    public static final String BTN_TEST_SON = "Tester"; // Optionnel












}
