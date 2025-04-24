package Global;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class StyleButton {

    public static void boutonBleuArrondi(JButton b) {
        b.setBackground(new Color(70, 130, 180));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
    }

    public static void boutonRougeDanger(JButton b) {
        b.setBackground(new Color(220, 20, 60));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    public static void boutonVertValide(JButton b) {
        b.setBackground(new Color(34, 139, 34));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Verdana", Font.BOLD, 13));
    }

    public static void boutonNoirMinimaliste(JButton b) {
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Courier New", Font.PLAIN, 14));
    }

    public static void boutonGrisClairPlat(JButton b) {
        b.setBackground(new Color(230, 230, 230));
        b.setForeground(Color.DARK_GRAY);
        b.setBorderPainted(false);
        b.setFont(new Font("Tahoma", Font.PLAIN, 13));
    }

    public static void boutonJauneSunshine(JButton b) {
        b.setBackground(new Color(255, 215, 0));
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Georgia", Font.BOLD, 14));
    }

    public static void boutonOrangePunch(JButton b) {
        b.setBackground(new Color(255, 140, 0));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 14));
    }

    public static void boutonTransparent(JButton b) {
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setForeground(Color.BLUE);
        b.setFont(new Font("SansSerif", Font.ITALIC, 13));
    }

    public static void boutonVioletDeep(JButton b) {
        b.setBackground(new Color(138, 43, 226));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Lucida", Font.BOLD, 15));
    }

    public static void boutonGris3D(JButton b) {
        b.setBackground(new Color(169, 169, 169));
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Dialog", Font.PLAIN, 14));
        b.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    public static void boutonBleuCiel(JButton b) {
        b.setBackground(new Color(135, 206, 250));
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Verdana", Font.BOLD, 12));
    }

    public static void boutonRoseBonbon(JButton b) {
        b.setBackground(new Color(255, 105, 180));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
    }

    public static void boutonVertMenthe(JButton b) {
        b.setBackground(new Color(152, 251, 152));
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Arial", Font.PLAIN, 13));
    }

    public static void boutonBleuMarine(JButton b) {
        b.setBackground(new Color(25, 25, 112));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Times New Roman", Font.BOLD, 14));
    }

    public static void boutonShadow(JButton b) {
        b.setBackground(new Color(105, 105, 105));
        b.setForeground(Color.LIGHT_GRAY);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    public static void boutonFlatMinimal(JButton b) {
        b.setBackground(Color.WHITE);
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Arial", Font.PLAIN, 13));
        b.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    public static void boutonSansBord(JButton b) {
        b.setBorderPainted(false);
        b.setBackground(new Color(200, 200, 200));
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Calibri", Font.BOLD, 14));
    }

    public static void boutonBleuFoncéArrondi(JButton b) {
        b.setBackground(new Color(0, 0, 128));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Verdana", Font.BOLD, 13));
    }

    public static void boutonGlassEffect(JButton b) {
        b.setBackground(new Color(224, 255, 255));
        b.setForeground(new Color(0, 128, 128));
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
    }

    public static void boutonBlackOut(JButton b) {
        b.setBackground(new Color(30, 30, 30));
        b.setForeground(new Color(220, 220, 220));
        b.setFont(new Font("Consolas", Font.BOLD, 13));
    }

    public static void boutonCarbone(JButton b) {
        b.setBackground(new Color(54, 69, 79));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 13));
    }
}


