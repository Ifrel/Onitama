package Vue.Configuration;

import java.awt.*;


public class ConfigUI {

    // --- INTERFACES
    public final static int WIDTH_MENU = 500;
    public final static int ARRONDI = 20;
    public final static Color COULEUR_FOND_PION_DEPART_SELECTIONE = new Color(6, 157, 185, 72);



    /******************************************
     * ECRAN DE DÉMARRAGE             *
     ******************************************/

    // Titres et textes de l'interface utilisateur pour l'écran de démarrage et la configuration.
    // NOTE : Pour une application multilingue, ces chaînes devraient être gérées via ResourceBundles.
    public static final String TITRE_ONGLET_GENERAL     = "General";
    public static final String TITRE_ONGLET_COULEUR     = "Couleur";

    public static final String LBL_TITRE_CONFIG     = "configuration";
    public static final String LBL_MODE_AUTO        = "IA vs IA";
    public static final String LBL_REPRENDRE        = "reprendre";
    public static final String LBL_JOUER_IA         = "contre ia";
    public static final String LBL_JOUEUR_1         = "joueur 1";
    public static final String LBL_JOUEUR_2         = "joueur 2";

    public static final String INDICATION_SELECTION = "Sélectionner ici...";
    public static final String OPTION_IA_NON = "Non";

    // TODO Exemples de données. À supprimer ou gérer dynamiquement dans la version finale.
    public static final String[] OPTIONS_REPRENDRE = new String[]{INDICATION_SELECTION, "Partie 1", "Partie 2", "Partie 3"}; // <-- SUPPRIMER
    public static final String[] OPTIONS_IA = new String[]{OPTION_IA_NON, "Faible", "Moyen", "Fort"}; // <-- SUPPRIMER



    // Définition des marges (Insets) pour l'espacement des composants.
    public static final Insets MARGES_DEFAUT = new Insets(5, 10, 5, 10); // Espacement par défaut (haut, gauche, bas, droite)
    public static final Insets MARGES_TITRE = new Insets(20, 10, 20, 10); // Espacement pour le titre

    // Dimensions standard des composants.
    public static final int LARGEUR_LISTE_DEROULANTE = 220;
    public static final int HAUTEUR_LISTE_DEROULANTE = 50;
    public static final int LARGEUR_CHAMP_TEXTE = 11; // Nombre de colonnes pour JTextField (taille indicative)

    // Dimension combinée pour les champs et listes déroulantes, basée sur les constantes précédentes.
    public static final Dimension DIMENSION_CHAMP_LISTE_DEROULANTE = new Dimension(LARGEUR_LISTE_DEROULANTE, HAUTEUR_LISTE_DEROULANTE);


    // --- Onglet Couleur ---
    // Constantes spécifiques à l'onglet de personnalisation des couleurs.
    public static final String LBL_TITRE_COULEUR         = "Personnalisation\ndes Couleurs";
    public static final String LBL_CASE_TERRAIN          = "Case Terrain";
    public static final String LBL_CASE_MAITRE_JOUEUR_1  = "Case Maitre Joueur 1";
    public static final String LBL_CASE_MAITRE_JOUEUR_2  = "Case Maitre Joueur 2";
    public static final String LBL_CASE_ELEVE_JOUEUR_1   = "Case Eleve Joueur 1";
    public static final String LBL_CASE_ELEVE_JOUEUR_2   = "Case Eleve Joueur 2";
    public static final String LBL_PION_TERRAIN_JOUEUR_1 = "Pion Joueur 1";
    public static final String LBL_PION_TERRAIN_JOUEUR_2 = "Pion Joueur 2";

    public static final Dimension DIM_PREVIEW_COULEUR   = new Dimension(30, 30);

}
