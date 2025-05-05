package Vue;

import Modele.Jeu;

import java.awt.*;
import java.nio.file.Path;


public class ConfigUI {

     // --- INTERFACES
    public final static int WIDTH_MENU = 500;

    // --- PATHS
    public final static String PATH_IMAGE_ARRIERE_PLAN_MENU = "res/vue/images/arrierePlans/menu.png";
    public static final String PATH_BTN_MODE_AUTO_OFF       = "res/vue/images/buttons/button_off.png";
    public static final String PATH_BTN_MODE_AUTO_ON        = "res/vue/images/buttons/button_on.png";
    public static final String PATH_BTN_ENTRER              = "res/vue/images/buttons/button_entrer.png";
    public static final String PATH_ARRIERE_PLAN_ED_O1      = "res/vue/images/arrierePlans/ecran_de_demarrage.png";
    public static final Path PATH_ARRIERE_PLAN_ED_O2        = Path.of("res/vue/images/arrierePlans/ecran_de_demarrage2.png");


    // --- POLICES
    public  final static String POLICE_1 = "Arial";
    public  final static String POLICE_2 = "Times New Roman";
    public  final static String POLICE_3 = "Georgia";





    /******************************************
     *         ECRAN DE DÉMARRAGE             *
     ******************************************/

    // Titres et textes de l'interface utilisateur
    public static final String TITRE_ONGLET_GENERAL     = "Général";
    public static final String TITRE_ONGLET_IA          = "IA";
    public static final String TITRE_ONGLET_COULEUR     = "Couleur";
    public static final String TITRE_ONGLET_ANIMATION   = "Animation";
    public static final String TITRE_ONGLET_SON         = "Son";

    public static final String LBL_TITRE_CONFIG     = "Configuration";
    public static final String LBL_MODE_AUTO        = "Mode auto (IA vs IA)";
    public static final String LBL_REPRENDRE        = "Reprendre une partie";
    public static final String LBL_JOUER_IA         = "Jouer contre l'IA";
    public static final String LBL_JOUEUR_1         = "Joueur 1";
    public static final String LBL_JOUEUR_2         = "Joueur 2";

    public static final String INDICATION_SELECTION = "Sélectionner ici...";
    public static final String OPTION_IA_NON = "Non";
    // exemples
    public static final String[] OPTIONS_REPRENDRE = new String[]{INDICATION_SELECTION, "Partie 1", "Partie 2", "Partie 3"}; // <-- SUPPRIMER
    public static final String[] OPTIONS_IA = new String[]{OPTION_IA_NON, "Facile", "Intermédiaire", "Difficile"}; // <-- SUPPRIMER


    // Polices (Fonts)
    public static final Font FONT_TITRE = new Font(POLICE_1, Font.BOLD, 40); // Taille ajustée
    public static final Font FONT_LABEL = new Font(POLICE_1, Font.PLAIN, 25); // Taille ajustée
    public static final Font FONT_COMPOSANT = new Font(POLICE_1, Font.PLAIN, 20); // Police pour les composants

    // Marges (Insets)
    public static final Insets MARGES_DEFAUT = new Insets(5, 10, 5, 10); // Espacement par défaut
    public static final Insets MARGES_TITRE = new Insets(20, 10, 20, 10); // Espacement pour le titre

    // Dimensions des composants
    public static final int LARGEUR_LISTE_DEROULANTE = 220;
    public static final int HAUTEUR_LISTE_DEROULANTE = 30;
    public static final int LARGEUR_CHAMP_TEXTE = 11; // Colonnes pour JTextField
    public static final Dimension DIMENSION_CHAMP_LISTE_DEROULANTE = new Dimension(210, 30);

    // Constantes pour GridBagConstraints
    public static final int COLONNE_ETIQUETTE = 4;
    public static final int COLONNE_COMPOSANT = 5;

    // --- Onglet IA ---
    public static final String LBL_TITRE_IA             = "Paramètres de l'IA";
    public static final String LBL_TEMPS_REFLEXION      = "Temps de réflexion IA (ms)";
    public static final String LBL_HEURISTIQUE_AVANCEE  = "Activer heuristique avancée";
    public static final String LBL_ALGORITHME_IA        = "Algorithme IA";
    public static final String[] OPTIONS_ALGORITHME_IA  = {"Minimax Simple", "Alpha-Beta", "Monte Carlo"}; // Exemple

    // --- Onglet Couleur ---
    public static final String LBL_TITRE_COULEUR         = "Personnalisation des Couleurs";
    public static final String LBL_PLATEAU_DE_JEU        = "Plateau de Jeu";
    public static final String LBL_CASE_TERRAIN          = "Case Terain";
    public static final String LBL_CASE_MAITRE_JOUEUR_1  = "Case Maitre Joueur 1";
    public static final String LBL_CASE_MAITRE_JOUEUR_2  =  "Case Maitre Joueur 2";
    public static final String LBL_CASE_ELEVE_JOUEUR_1   = "Case Elève Joueur 1";
    public static final String LBL_CASE_ELEVE_JOUEUR_2   = "Case Elève Joueur 2";
    public static final String LBL_BLOC_MENU             = "Bloc menu";

    public static final String BTN_CHOISIR_COULEUR      = "Choisir...";
    public static final Dimension DIM_PREVIEW_COULEUR   = new Dimension(30, 30);

    // --- Onglet Animation ---
    public static final String LBL_TITRE_ANIMATION          = "Paramètres d'Animation";
    public static final String LBL_VITESSE_ANIMATION        = "Vitesse d'animation";
    public static final String LBL_ANIMATION_PIECES         = "Animer le déplacement des pièces";
    public static final String LBL_ANIMATION_SURBRILLANCE   = "Animer la surbrillance";

    // --- Onglet Son ---
    public static final String LBL_TITRE_SON        = "Paramètres Audio";
    public static final String LBL_VOLUME_GENERAL   = "Volume Général";
    public static final String LBL_VOLUME_EFFETS    = "Volume Effets Sonores";
    public static final String LBL_VOLUME_MUSIQUE   = "Volume Musique";
    public static final String LBL_SON_MUET         = "Muet (couper tout son)";












}
