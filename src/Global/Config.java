package Global;

import java.awt.*;
import java.nio.file.Path;

public class Config {
    public static final boolean graphique = true;

    public static final Point posPoison = new Point(0, 0);

    public static final Color couleurPleine = new Color(139, 69, 19);
    public static final Color couleurVide = new Color(198, 204, 203);
    public static final Color couleurPoison = new Color(184, 4, 178);

    public static final Path saveDir = Path.of("res", "saves");

    public static final int joueurA = 1;
    public static final int joueurB = 2;
    public static final int joueurIA = 3;

}
