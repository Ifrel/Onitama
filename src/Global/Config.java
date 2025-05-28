package Global;

import java.awt.*;
import java.nio.file.Path;
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

    public static final Path SAVE_DIR = Path.of("saves");

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
        ETAT_DEFAUT,
        FIN,
        J1_A_JOUE,
        J2_A_JOUE,
    }

    public static enum TYPE_JOUEUR {
        JOUEUR_HUMAIN,
        JOUEUR_IA
    }

    // -- DIMENSION --
    public static final Dimension DIM_SCENE = new Dimension(900, 600);

    // --- DEBUG --
    public static final boolean MODEDEBUG = true;

    // -- GRILLE --
    public final static int LIGNES = 5;
    public final static int COLONNES = 5;

    // -- COULEURS DE BASE --
    public static final Color COULEUR_CASE_TERRAIN = new Color(255, 255, 255, 255);
    public static final Color COULEUR_CASE_MAITRE_JOUEUR_1  = new Color(255, 255, 255, 0);
    public static final Color COULEUR_CASE_MAITRE_JOUEUR_2  = new Color(255, 255, 255, 0);
    public static final Color COULEUR_CASE_ELEVE_JOUEUR_1   = new Color(255, 255, 255, 0);
    public static final Color COULEUR_CASE_ELEVE_JOUEUR_2   = new Color(255, 255, 255, 0);
    // Enum pour identifier la cible de la couleur (simplifie le listener)
    public enum CiblesDesCouleurs {
        CASE_TERRAIN,
        CASE_MAITRE_JOUEUR_1,
        CASE_MAITRE_JOUEUR_2,
        CASE_ELEVE_JOUEUR_1,
        CASE_ELEVE_JOUEUR_2,
        PION_TERRAIN_JOUEUR_1,
        PION_TERRAIN_JOUEUR_2
    }


    // -- CARTES --
    public final static int NOMBRES_CARTES = 16;
    public final static int NOMBRE_CARTES_MAIN = 2;
    public final static int NOMBRES_CARTES_PLATEAU = 5;
    public enum TYPECARTE
    {
        /**
         * <blockquote><pre>{@code
         * TIGRE
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   | o |   |   |
         *   |   |   |   |   |   |
         *   |   |   | X |   |   |
         *   |   |   | o |   |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
        TIGRE,
        /**
         * <blockquote><pre>{@code
         * DRAGON
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   |   |   |   |
         *   | o |   |   |   | o |
         *   |   |   | X |   |   |
         *   |   | o |   | o |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
        DRAGON,
        /**
         * <blockquote><pre>{@code
         * GRENOUILLE
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   |   |   |   |
         *   |   | o |   |   |   |
         *   | o |   | X |   |   |
         *   |   |   |   | o |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
        GRENOUILLE,
        /**
         * <blockquote><pre>{@code
         * LAPIN
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   |   |   |   |
         *   |   |   |   | o |   |
         *   |   |   | X |   | o |
         *   |   | o |   |   |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
        LAPIN,
        /**
         * <blockquote><pre>{@code
         * CRABE
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   |   |   |   |
         *   |   |   | o |   |   |
         *   | o |   | X |   | o |
         *   |   |   |   |   |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
        CRABE,
        /**
         * <blockquote><pre>{@code
         * ELEPHANT
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   |   |   |   |
         *   |   | o |   | o |   |
         *   |   | o | X | o |   |
         *   |   |   |   |   |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
        ELEPHANT,
        /**
         * <blockquote><pre>{@code
         * OIE
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   |   |   |   |
         *   |   | o |   |   |   |
         *   |   | o | X | o |   |
         *   |   |   |   | o |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
        OIE,
        /**
         * <blockquote><pre>{@code
         * COQ
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   |   |   |   |
         *   |   |   |   | o |   |
         *   |   | o | X | o |   |
         *   |   | o |   |   |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
        COQ,
        /**
         * <blockquote><pre>{@code
         * SINGE
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   |   |   |   |
         *   |   | o |   | o |   |
         *   |   |   | X |   |   |
         *   |   | o |   | o |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
        SINGE,
        /**
         * <blockquote><pre>{@code
         * MANTE
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   |   |   |   |
         *   |   | o |   | o |   |
         *   |   |   | X |   |   |
         *   |   |   | o |   |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
        MANTE,
        /**
         * <blockquote><pre>{@code
         * CHEVAL
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   |   |   |   |
         *   |   |   | o |   |   |
         *   |   | o | X |   |   |
         *   |   |   | o |   |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
        CHEVAL,
        /**
         * <blockquote><pre>{@code
         * BOEUF
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   |   |   |   |
         *   |   |   | o |   |   |
         *   |   |   | X | o |   |
         *   |   |   | o |   |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
        BOEUF,
        /**
         * <blockquote><pre>{@code
         * GRUE
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   |   |   |   |
         *   |   |   | o |   |   |
         *   |   |   | X |   |   |
         *   |   | o |   | o |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
        GRUE,
        /**
         * <blockquote><pre>{@code
         * SANGLIER
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   |   |   |   |
         *   |   |   | o |   |   |
         *   |   | o | X | o |   |
         *   |   |   |   |   |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
        SANGLIER,
        /**
         * <blockquote><pre>{@code
         * ANGUILLE
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   |   |   |   |
         *   |   | o |   |   |   |
         *   |   |   | X | o |   |
         *   |   | o |   |   |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
        ANGUILLE,
        /**
         * <blockquote><pre>{@code
         * COBRA
         * Origine : X ; Déplacement : o
         * (0,0)
         *   |   |   |   |   |   |
         *   |   |   |   | o |   |
         *   |   | o | X |   |   |
         *   |   |   |   | o |   |
         *   |   |   |   |   |   |
         *   }</pre></blockquote>
         */
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