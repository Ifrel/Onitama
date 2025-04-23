package Vue.Styles;

import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.*;

public class Utils {

    public static TitledBorder creerTitledBorder(String titre, int tailleTexte, Color couleurTexte, int styleTexte) {
        TitledBorder border = BorderFactory.createTitledBorder(titre);
        Font font = new Font("SansSerif", styleTexte, tailleTexte);
        border.setTitleFont(font);
        border.setTitleColor(couleurTexte);
        return border;
    }
}
