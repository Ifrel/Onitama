
package Vue.LabO.OnitamaSwingProject.OnitamaSwing.src.main.java.com.rinel.onitama;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class FenetreJeu extends JFrame {

    public FenetreJeu() {
        setTitle("Onitama");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);

        JLabel titre = new JLabel("Onitama");
        titre.setHorizontalAlignment(SwingConstants.CENTER);

        // Charger la police depuis les ressources
        try (InputStream is = getClass().getResourceAsStream("/fonts/ZenMaruGothic-Regular.ttf")) {
            if (is != null) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(42f);
                titre.setFont(font);
            } else {
                System.err.println("Police introuvable !");
                titre.setFont(new Font("Serif", Font.BOLD, 42));
            }
        } catch (Exception e) {
            e.printStackTrace();
            titre.setFont(new Font("Serif", Font.BOLD, 42));
        }

        add(titre, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FenetreJeu::new);
    }
}
