package Vue;


import javax.swing.*;
import java.awt.*;

public class EcranVictoire extends JWindow {
    public EcranVictoire(String nomGagnant) {
        // Définir la taille de la fenêtre et la centrer
        setSize(500, 300);
        setLocationRelativeTo(null); // centre l'écran

        // === Panneau principal ===
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dégradé de fond
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, Color.GREEN, getWidth(), getHeight(), Color.RED);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel texteVictoire = new JLabel(" Victoire de " + nomGagnant + " ! ", SwingConstants.CENTER);
        texteVictoire.setFont(new Font("SansSerif", Font.BOLD, 30));
        texteVictoire.setForeground(Color.WHITE);
        texteVictoire.setHorizontalAlignment(SwingConstants.CENTER);
        texteVictoire.setVerticalAlignment(SwingConstants.CENTER);

        panel.add(texteVictoire, BorderLayout.CENTER);
        setContentPane(panel);

        // Afficher la fenêtre
        setVisible(true);

        // Fermer automatiquement après 3 secondes
        new Timer(3000, e -> dispose()).start();
    }
}

