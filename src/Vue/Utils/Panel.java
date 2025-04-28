package Vue.Utils;

import javax.swing.*;
import java.awt.*;

public class Panel {
    public static JPanel creerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Pour centrer ce qu'on ajoutera dedans
        //panel.setLayout(new BorderLayout());
        return panel;
    }
}
