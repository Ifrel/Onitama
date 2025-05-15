package Global;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config {
    // -- MODE INTERFACE --

    public static final boolean MODE_GRAPHIQUE = true;


    // -- IA --
    public static enum NIVEAU_IA {
        FAIBLE,
        MOYEN,
        FORT
    }

    public static enum VITESSE_IA {
        LENTE,
        MOYENNE,
        RAPIDE
    }

    public static final int TAILLE_VECTEUR_BITS = 96;

    public static final int ID_JOUEUR_1 = 1;
    public static final int ID_JOUEUR_2 = 2;

    public static final Point TEMPLE_JOUEUR_1 = new Point(0,2);
    public static final Point TEMPLE_JOUEUR_2 = new Point(4,2);

    // -- TYPE ELEMENT SUR LE TERRAIN DE JEU --
    public static enum ROLEPION {
        PION_ETUDIANT,
        PION_MAITRE
    }

    // automate, inspiré de Kevin
    public static enum ETAT_GRILLE {
        DEFAUT,
        PION_SELECTIONNE,
    }

    public static enum ETAT_JEU {
        DEBUT,
        FIN,
        DEBUT_IA,
        J1_A_JOUE,
        J2_A_JOUE,
        IA1_A_JOUE,
        IA2_A_JOUE
    }

    public static enum TYPE_JOUEUR {
        JOUEUR_HUMAIN,
        JOUEUR_IA
    }

    // -- DIMENSION --
    public static final Dimension DIM_SCENE = new Dimension(1000, 900);

    // --- DEBUG --
    public static final boolean MODEDEBUG = true;

    // -- GRILLE --
    public final static int LIGNES = 5;
    public final static int COLONNES = 5;

    // -- COULEURS DE BASE --
    public static final Color COULEUR_PLATEAU_DE_JEU        = new Color(104, 104, 104);
    public static final Color COULEUR_CASE_TERRAIN          = new Color(255, 255, 255);
    public static final Color COULEUR_CASE_MAITRE_JOUEUR_1  = new Color(255, 255, 255);
    public static final Color COULEUR_CASE_MAITRE_JOUEUR_2  = new Color(255, 255, 255);
    public static final Color COULEUR_CASE_ELEVE_JOUEUR_1   = new Color(255, 255, 255);
    public static final Color COULEUR_CASE_ELEVE_JOUEUR_2   = new Color(255, 255, 255);
    public static final Color COULEUR_BLOC_MENU             = new Color(31, 116, 116);
    // Enum pour identifier la cible de la couleur (simplifie le listener)
    public enum CiblesDesCouleurs {
        PLATEAU_DE_JEU,
        CASE_TERRAIN,
        CASE_MAITRE_JOUEUR_1,
        CASE_MAITRE_JOUEUR_2,
        CASE_ELEVE_JOUEUR_1,
        CASE_ELEVE_JOUEUR_2,
        BLOC_MENU
    }


    // -- CARTES --
    public final static int NOMBRES_CARTES = 16;
    public final static int NOMBRE_CARTES_MAIN = 2;
    public final static int NOMBRES_CARTES_PLATEAU = 5;
    public enum TYPECARTE
    {
        TIGRE,
        DRAGON,
        GRENOUILLE,
        LAPIN,
        CRABE,
        ELEPHANT,
        OIE,
        COQ,
        SINGE,
        MANTE,
        CHEVAL,
        BOEUF,
        GRUE,
        SANGLIER,
        ANGUILLE,
        COBRA,
    }

    public static HashMap<TYPECARTE, List<Point>> MOUVEMENTCARTE =
            new HashMap<TYPECARTE, List<Point>>() {{
                /* TIGRE
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   | o |   |   |
                 *   |   |   |   |   |   |
                 *   |   |   | X |   |   |
                 *   |   |   | o |   |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.TIGRE, new ArrayList<Point>()
                {{
                    add(new Point(-2,0));
                    add(new Point(1,0));
                }});
                /* DRAGON
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   |   |   |   |
                 *   | o |   |   |   | o |
                 *   |   |   | X |   |   |
                 *   |   | o |   | o |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.DRAGON, new ArrayList<Point>()
                {{
                    add(new Point(-1, -2));
                    add(new Point(1,-1));
                    add(new Point(-1,2));
                    add(new Point(1,1));
                }});
                /* GRENOUILLE
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   |   |   |   |
                 *   |   | o |   |   |   |
                 *   | o |   | X |   |   |
                 *   |   |   |   | o |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.GRENOUILLE, new ArrayList<Point>()
                {{
                    add(new Point(0,-2));
                    add(new Point(-1,-1));
                    add(new Point(1,1));
                }});
                /* LAPIN
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   |   |   |   |
                 *   |   |   |   | o |   |
                 *   |   |   | X |   | o |
                 *   |   | o |   |   |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.LAPIN, new ArrayList<Point>()
                {{
                    add(new Point(1,-1));
                    add(new Point(-1,1));
                    add(new Point(0,2));
                }});
                /* CRABE
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   |   |   |   |
                 *   |   |   | o |   |   |
                 *   | o |   | X |   | o |
                 *   |   |   |   |   |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.CRABE, new ArrayList<Point>()
                {{
                    add(new Point(0,-2));
                    add(new Point(-1,0));
                    add(new Point(0,2));
                }});
                /* ELEPHANT
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   |   |   |   |
                 *   |   | o |   | o |   |
                 *   |   | o | X | o |   |
                 *   |   |   |   |   |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.ELEPHANT, new ArrayList<Point>()
                {{
                    add(new Point(0,-1));
                    add(new Point(-1,-1));
                    add(new Point(-1,1));
                    add(new Point(0,1));
                }});
                /* OIE
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   |   |   |   |
                 *   |   | o |   |   |   |
                 *   |   | o | X | o |   |
                 *   |   |   |   | o |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.OIE, new ArrayList<Point>()
                {{
                    add(new Point(0,-1));
                    add(new Point(-1,-1));
                    add(new Point(0,1));
                    add(new Point(1,1));
                }});
                /* COQ
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   |   |   |   |
                 *   |   |   |   | o |   |
                 *   |   | o | X | o |   |
                 *   |   | o |   |   |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.COQ, new ArrayList<Point>()
                {{
                    add(new Point(1,-1));
                    add(new Point(0,-1));
                    add(new Point(0,1));
                    add(new Point(-1,1));
                }});
                /* SINGE
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   |   |   |   |
                 *   |   | o |   | o |   |
                 *   |   |   | X |   |   |
                 *   |   | o |   | o |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.SINGE, new ArrayList<Point>()
                {{
                    add(new Point(-1,-1));
                    add(new Point(1,-1));
                    add(new Point(-1,1));
                    add(new Point(1,1));
                }});
                /* MANTE
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   |   |   |   |
                 *   |   | o |   | o |   |
                 *   |   |   | X |   |   |
                 *   |   |   | o |   |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.MANTE, new ArrayList<Point>()
                {{
                    add(new Point(-1,-1));
                    add(new Point(1,0));
                    add(new Point(-1,1));
                }});
                /* CHEVAL
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   |   |   |   |
                 *   |   |   | o |   |   |
                 *   |   | o | X |   |   |
                 *   |   |   | o |   |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.CHEVAL, new ArrayList<Point>()
                {{
                    add(new Point(0,-1));
                    add(new Point(-1,0));
                    add(new Point(1,0));
                }});
                /* BOEUF
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   |   |   |   |
                 *   |   |   | o |   |   |
                 *   |   |   | X | o |   |
                 *   |   |   | o |   |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.BOEUF, new ArrayList<Point>()
                {{
                    add(new Point(-1,0));
                    add(new Point(1,0));
                    add(new Point(0,1));
                }});
                /* GRUE
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   |   |   |   |
                 *   |   |   | o |   |   |
                 *   |   |   | X |   |   |
                 *   |   | o |   | o |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.GRUE, new ArrayList<Point>()
                {{
                    add(new Point(1,-1));
                    add(new Point(-1,0));
                    add(new Point(1,1));
                }});
                /* SANGLIER
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   |   |   |   |
                 *   |   |   | o |   |   |
                 *   |   | o | X | o |   |
                 *   |   |   |   |   |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.SANGLIER, new ArrayList<Point>()
                {{
                    add(new Point(0,-1));
                    add(new Point(-1,0));
                    add(new Point(0,1));
                }});
                /* ANGUILLE
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   |   |   |   |
                 *   |   | o |   |   |   |
                 *   |   |   | X | o |   |
                 *   |   | o |   |   |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.ANGUILLE, new ArrayList<Point>()
                {{
                    add(new Point(-1,-1));
                    add(new Point(1,-1));
                    add(new Point(0,1));
                }});
                /* COBRA
                 * Origine : X ; Déplacement : o
                 * (0,0)
                 *   |   |   |   |   |   |
                 *   |   |   |   | o |   |
                 *   |   | o | X |   |   |
                 *   |   |   |   | o |   |
                 *   |   |   |   |   |   |
                 */
                put(TYPECARTE.COBRA, new ArrayList<Point>()
                {{
                    add(new Point(0,-1));
                    add(new Point(-1,1));
                    add(new Point(1,1));
                }});
    }};
}