package Vue;

import java.awt.*;


public class ConstantesConfigurationUI {

    // --- INTERFACES
    public final static int WIDTH_MENU = 500;


    // --- POLICES
    // Constantes pour les noms de polices utilisées.



    /******************************************
     * ECRAN DE DÉMARRAGE             *
     ******************************************/

    // Titres et textes de l'interface utilisateur pour l'écran de démarrage et la configuration.
    // NOTE : Pour une application multilingue, ces chaînes devraient être gérées via ResourceBundles.
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

    // TODO Exemples de données. À supprimer ou gérer dynamiquement dans la version finale.
    public static final String[] OPTIONS_REPRENDRE = new String[]{INDICATION_SELECTION, "Partie 1", "Partie 2", "Partie 3"}; // <-- SUPPRIMER
    public static final String[] OPTIONS_IA = new String[]{OPTION_IA_NON, "Faible", "Moyen", "Fort"}; // <-- SUPPRIMER


    // Définition des polices (Fonts) utilisées avec des tailles ajustées.


    // Définition des marges (Insets) pour l'espacement des composants.
    public static final Insets MARGES_DEFAUT = new Insets(5, 10, 5, 10); // Espacement par défaut (haut, gauche, bas, droite)
    public static final Insets MARGES_TITRE = new Insets(20, 10, 20, 10); // Espacement pour le titre

    // Dimensions standard des composants.
    public static final int LARGEUR_LISTE_DEROULANTE = 220;
    public static final int HAUTEUR_LISTE_DEROULANTE = 30;
    public static final int LARGEUR_CHAMP_TEXTE = 11; // Nombre de colonnes pour JTextField (taille indicative)

    // Dimension combinée pour les champs et listes déroulantes, basée sur les constantes précédentes.
    public static final Dimension DIMENSION_CHAMP_LISTE_DEROULANTE = new Dimension(LARGEUR_LISTE_DEROULANTE, HAUTEUR_LISTE_DEROULANTE);

    // Constantes pour GridBagConstraints, indiquant les colonnes standard pour libellés et composants.



    // --- Onglet IA ---
    // Constantes spécifiques à l'onglet de configuration de l'IA.
    public static final String LBL_TITRE_IA             = "Paramètres de l'IA";
    public static final String LBL_TEMPS_REFLEXION      = "Temps de réflexion IA (ms)";
    public static final String LBL_HEURISTIQUE_AVANCEE  = "Activer heuristique avancée";
    public static final String LBL_ALGORITHME_IA        = "Algorithme IA";
    public static final String[] OPTIONS_ALGORITHME_IA  = {"Minimax Simple", "Alpha-Beta", "Monte Carlo"}; // Exemple. À gérer dynamiquement ?


    // --- Onglet Couleur ---
    // Constantes spécifiques à l'onglet de personnalisation des couleurs.
    public static final String LBL_TITRE_COULEUR         = "Personnalisation des Couleurs";
    public static final String LBL_PLATEAU_DE_JEU        = "Plateau de Jeu";
    public static final String LBL_CASE_TERRAIN          = "Case Terrain"; // Note : faute de frappe "Terain" au lieu de "Terrain" ?
    public static final String LBL_CASE_MAITRE_JOUEUR_1  = "Case Maitre Joueur 1"; // Note : faute de frappe "Maitre" au lieu de "Maître" ?
    public static final String LBL_CASE_MAITRE_JOUEUR_2  = "Case Maitre Joueur 2"; // Note : faute de frappe "Maitre" au lieu de "Maître" ?
    public static final String LBL_CASE_ELEVE_JOUEUR_1   = "Case Elève Joueur 1"; // Note : faute de frappe "Elève" au lieu de "Élève" ?
    public static final String LBL_CASE_ELEVE_JOUEUR_2   = "Case Elève Joueur 2"; // Note : faute de frappe "Elève" au lieu de "Élève" ?
    public static final String LBL_BLOC_MENU             = "Bloc menu";

    public static final String BTN_CHOISIR_COULEUR      = "Choisir...";
    public static final Dimension DIM_PREVIEW_COULEUR   = new Dimension(30, 30);


    // --- Onglet Animation ---
    // Constantes spécifiques à l'onglet de configuration d'animation.
    public static final String LBL_TITRE_ANIMATION          = "Paramètres d'Animation";
    public static final String LBL_VITESSE_ANIMATION        = "Vitesse d'animation";
    public static final String LBL_ANIMATION_PIECES         = "Animer le déplacement des pièces";
    public static final String LBL_ANIMATION_SURBRILLANCE   = "Animer la surbrillance";


    // --- Onglet Son ---
    // Constantes spécifiques à l'onglet de configuration audio.
    public static final String LBL_TITRE_SON        = "Paramètres Audio";
    public static final String LBL_VOLUME_GENERAL   = "Volume Général";
    public static final String LBL_VOLUME_EFFETS    = "Volume Effets Sonores";
    public static final String LBL_VOLUME_MUSIQUE   = "Volume Musique";
    public static final String LBL_SON_MUET         = "Muet (couper tout son)";


}
