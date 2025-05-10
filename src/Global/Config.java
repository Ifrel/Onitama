package Global;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config {
    // -- IA --
    public static enum NIVEAU_IA {
        FAIBLE,
        MOYEN,
        FORT
    }

    // -- TYPE ELEMENT SUR LE TERRAIN DE JEU --
    public static enum TYPE_ELEMENT_TERRAIN {
        VIDE,
        PION_ETUDIANT,
        PION_MAITRE
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
    public static final Color COULEUR_BLOC_MENU             = new Color(211, 142, 44);
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
                put(TYPECARTE.TIGRE, new ArrayList<Point>()
                {{
                    add(new Point(0,2));
                    add(new Point(0,-1));
                }});
                put(TYPECARTE.DRAGON, new ArrayList<Point>()
                {{
                    add(new Point(-2,1));
                    add(new Point(-1,-1));
                    add(new Point(1,-1));
                    add(new Point(2,1));
                }});
                put(TYPECARTE.GRENOUILLE, new ArrayList<Point>()
                {{
                    add(new Point(-2,0));
                    add(new Point(-1,1));
                    add(new Point(1,-1));
                }});
                put(TYPECARTE.LAPIN, new ArrayList<Point>()
                {{
                    add(new Point(-1,-1));
                    add(new Point(1,1));
                    add(new Point(2,0));
                }});
                put(TYPECARTE.CRABE, new ArrayList<Point>()
                {{
                    add(new Point(-2,0));
                    add(new Point(0,1));
                    add(new Point(2,0));
                }});
                put(TYPECARTE.ELEPHANT, new ArrayList<Point>()
                {{
                    add(new Point(-1,0));
                    add(new Point(-1,1));
                    add(new Point(1,0));
                    add(new Point(1,1));
                }});
                put(TYPECARTE.OIE, new ArrayList<Point>()
                {{
                    add(new Point(-1,0));
                    add(new Point(-1,1));
                    add(new Point(1,0));
                    add(new Point(1,-1));
                }});
                put(TYPECARTE.COQ, new ArrayList<Point>()
                {{
                    add(new Point(-1,0));
                    add(new Point(-1,-1));
                    add(new Point(1,0));
                    add(new Point(1,1));
                }});
                put(TYPECARTE.SINGE, new ArrayList<Point>()
                {{
                    add(new Point(-1,1));
                    add(new Point(-1,-1));
                    add(new Point(1,1));
                    add(new Point(1,-1));
                }});
                put(TYPECARTE.MANTE, new ArrayList<Point>()
                {{
                    add(new Point(-1,1));
                    add(new Point(0,-1));
                    add(new Point(1,1));
                }});
                put(TYPECARTE.CHEVAL, new ArrayList<Point>()
                {{
                    add(new Point(-1,0));
                    add(new Point(0,1));
                    add(new Point(0,-1));
                }});
                put(TYPECARTE.BOEUF, new ArrayList<Point>()
                {{
                    add(new Point(1,0));
                    add(new Point(0,1));
                    add(new Point(0,-1));
                }});
                put(TYPECARTE.GRUE, new ArrayList<Point>()
                {{
                    add(new Point(0,1));
                    add(new Point(-1,-1));
                    add(new Point(1,-1));
                }});
                put(TYPECARTE.SANGLIER, new ArrayList<Point>()
                {{
                    add(new Point(0,1));
                    add(new Point(-1,0));
                    add(new Point(1,0));
                }});
                put(TYPECARTE.ANGUILLE, new ArrayList<Point>()
                {{
                    add(new Point(-1,1));
                    add(new Point(-1,-1));
                    add(new Point(1,0));
                }});
                put(TYPECARTE.COBRA, new ArrayList<Point>()
                {{
                    add(new Point(-1,0));
                    add(new Point(1,1));
                    add(new Point(1,-1));
                }});
    }};

    // -- PIONS --
    public enum ROLEPION { Maitre, Etudiant }
}