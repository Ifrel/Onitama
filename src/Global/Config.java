package Global;

import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config {

    // --- DEBUG --
    public final static boolean MODEDEBUG = true;

    // -- GRILLE --
    public final static int LIGNES = 5;
    public final static int COLONNES = 5;

    // -- COULEURS
    public final static Color COULEUR_PLATEAU = new Color(104, 104, 104);
    public final static Color COULEUR_BOUTON_PLATEAU = new Color(255, 255, 255);

    // --- INTERFACES
    public final static int WIDTH_MENU = 500;

    // --- PATHS
    public final static String PATH_IMAGE_ARRIERE_PLAN_MENU = "res/vue/images/arrierePlans/menu.png";

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
    /*
    public static final boolean graphique = true;

    public static final Point posPoison = new Point(0, 0);

    public static final Color couleurPleine = new Color(139, 69, 19);
    public static final Color couleurVide = new Color(198, 204, 203);
    public static final Color couleurPoison = new Color(184, 4, 178);

    public static final Path saveDir = Path.of("res", "saves");

    public static final int joueurA = 1;
    public static final int joueurB = 2;
    public static final int joueurIA = 3;
    */
}