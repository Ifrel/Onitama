package Global;

import javax.swing.*;
import java.awt.*;

public class StylePanel {

      public static void panelFondBleuClairArrondi(JPanel p) {
        p.setBackground(new Color(173, 216, 230));
        p.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
    }

    public static void panelGrisShadow(JPanel p) {
        p.setBackground(new Color(245, 245, 245));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
    }

    public static void panelNoirAvecContourRouge(JPanel p) {
        p.setBackground(Color.BLACK);
        p.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
    }

    public static void panelTransparent(JPanel p) {
        p.setOpaque(false);
    }

    public static void panelVertMenthe(JPanel p) {
        p.setBackground(new Color(152, 251, 152));
        p.setBorder(BorderFactory.createEtchedBorder());
    }

    public static void panelRougeAvecTitre(JPanel p) {
        p.setBackground(new Color(255, 99, 71));
        p.setBorder(BorderFactory.createTitledBorder("Zone Critique"));
    }

    public static void panelBleuMarineArrondi(JPanel p) {
        p.setBackground(new Color(25, 25, 112));
        p.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
    }

    public static void panelPastel(JPanel p) {
        p.setBackground(new Color(255, 228, 225));
        p.setBorder(BorderFactory.createLineBorder(new Color(255, 182, 193), 2));
    }

    public static void panelGrisMetal(JPanel p) {
        p.setBackground(new Color(192, 192, 192));
        p.setBorder(BorderFactory.createLoweredBevelBorder());
    }

    public static void panelAvecDoubleBordure(JPanel p) {
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                BorderFactory.createDashedBorder(Color.LIGHT_GRAY)));
    }

    public static void panelOrangeAccent(JPanel p) {
        p.setBackground(new Color(255, 165, 0));
        p.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    public static void panelVertLime(JPanel p) {
        p.setBackground(new Color(50, 205, 50));
        p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    public static void panelBleuGris(JPanel p) {
        p.setBackground(new Color(96, 130, 182));
        p.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY));
    }

    public static void panelFondImage(JPanel p, Image img) {
        p.setLayout(new BorderLayout());
        p = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
    }

    public static void panelAvecBordureDotted(JPanel p) {
        p.setBorder(BorderFactory.createDashedBorder(Color.GRAY, 5, 5));
    }

    public static void panelNoirTransparent(JPanel p) {
        p.setBackground(new Color(0, 0, 0, 150));
    }

    public static void panelVertFoncéArrondi(JPanel p) {
        p.setBackground(new Color(0, 100, 0));
        p.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2, true));
    }

    public static void panelStyleCarte(JPanel p) {
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    }

    public static void panelRosePaleArrondi(JPanel p) {
        p.setBackground(new Color(255, 182, 193));
        p.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3, true));
    }

    public static void panelBrillant(JPanel p) {
        p.setBackground(new Color(230, 230, 250));
        p.setBorder(BorderFactory.createEtchedBorder());
    }

    public static void panelBordureArcEnCiel(JPanel p) {
        p.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 3, true));
        p.setBackground(Color.WHITE);
    }

}
