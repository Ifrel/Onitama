package Vue.Utlis;
import javax.swing.*;
import java.awt.*;

public  class StyleLabel{
    public static void labelTitreBleu(JLabel l) {
        l.setForeground(new Color(0, 102, 204));
        l.setFont(new Font("Arial", Font.BOLD, 20));
    }

    public static void labelRougeAlerte(JLabel l) {
        l.setForeground(new Color(220, 20, 60));
        l.setFont(new Font("Segoe UI", Font.BOLD, 18));
    }

    public static void labelGrisSousTitre(JLabel l) {
        l.setForeground(Color.GRAY);
        l.setFont(new Font("Verdana", Font.ITALIC, 16));
    }

    public static void labelVertSuccess(JLabel l) {
        l.setForeground(new Color(0, 128, 0));
        l.setFont(new Font("Tahoma", Font.PLAIN, 15));
    }

    public static void labelBlancSurFondSombre(JLabel l) {
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial", Font.BOLD, 16));
    }

    public static void labelBleuNeon(JLabel l) {
        l.setForeground(new Color(0, 255, 255));
        l.setFont(new Font("Courier New", Font.BOLD, 17));
    }

    public static void labelStyleMinimaliste(JLabel l) {
        l.setForeground(Color.BLACK);
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    public static void labelOrangeFlash(JLabel l) {
        l.setForeground(new Color(255, 140, 0));
        l.setFont(new Font("Georgia", Font.BOLD, 15));
    }

    public static void labelVioletRoyal(JLabel l) {
        l.setForeground(new Color(148, 0, 211));
        l.setFont(new Font("Lucida", Font.BOLD, 16));
    }

    public static void labelVertMenthe(JLabel l) {
        l.setForeground(new Color(60, 179, 113));
        l.setFont(new Font("Arial", Font.ITALIC, 14));
    }

    public static void labelNoirItalic(JLabel l) {
        l.setForeground(Color.BLACK);
        l.setFont(new Font("Times New Roman", Font.ITALIC, 15));
    }

    public static void labelBleuFonceBold(JLabel l) {
        l.setForeground(new Color(25, 25, 112));
        l.setFont(new Font("Dialog", Font.BOLD, 16));
    }

    public static void labelRosePale(JLabel l) {
        l.setForeground(new Color(255, 182, 193));
        l.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
    }

    public static void labelCyanLight(JLabel l) {
        l.setForeground(new Color(0, 206, 209));
        l.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    public static void labelTransparent(JLabel l) {
        l.setOpaque(false);
        l.setFont(new Font("Calibri", Font.PLAIN, 14));
    }

    public static void labelGrisFoncéCenter(JLabel l) {
        l.setForeground(Color.DARK_GRAY);
        l.setFont(new Font("Verdana", Font.BOLD, 16));
        l.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public static void labelBoldBlack(JLabel l) {
        l.setForeground(Color.BLACK);
        l.setFont(new Font("Segoe UI", Font.BOLD, 18));
    }

    public static void labelItalicBlue(JLabel l) {
        l.setForeground(new Color(30, 144, 255));
        l.setFont(new Font("Arial", Font.ITALIC, 16));
    }

    public static void labelYellowBright(JLabel l) {
        l.setForeground(new Color(255, 255, 0));
        l.setFont(new Font("Verdana", Font.BOLD, 15));
    }

    public static void labelDeepGreen(JLabel l) {
        l.setForeground(new Color(0, 100, 0));
        l.setFont(new Font("Courier", Font.BOLD, 15));
    }

    public static void labelChampagne(JLabel l) {
        l.setForeground(new Color(250, 214, 165));
        l.setFont(new Font("Arial", Font.BOLD, 15));
        }
}