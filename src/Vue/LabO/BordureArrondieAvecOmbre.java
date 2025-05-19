package Vue.LabO;

import Vue.Utils.Boutons.BoutonCarte;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

public class BordureArrondieAvecOmbre extends AbstractBorder {
    private final int arc;
    private Color couleurBordure;
    private final int epaisseur;

    public BordureArrondieAvecOmbre(Color couleur, int epaisseur, int arc) {
        this.couleurBordure = couleur;
        this.epaisseur = epaisseur;
        this.arc = arc;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Ombre simulée
        g2d.setColor(new Color(0, 0, 0, 30)); // transparent shadow
        g2d.fillRoundRect(x + 2, y + 2, width - 4, height - 4, arc, arc);

        // Bordure principale
        g2d.setColor(couleurBordure);
        g2d.setStroke(new BasicStroke(epaisseur));
        g2d.drawRoundRect(x + epaisseur / 2, y + epaisseur / 2, width - epaisseur, height - epaisseur, arc, arc);

        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(epaisseur, epaisseur, epaisseur, epaisseur);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(epaisseur, epaisseur, epaisseur, epaisseur);
        return insets;
    }

}


 class TestBoutonCarteAvecBordure {

    public static void main(String[] args) {
        // Crée la fenêtre
        JFrame frame = new JFrame("Test BoutonCarte avec bordure arrondie et ombre");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 150);
        frame.setLocationRelativeTo(null);

        // Panneau avec layout horizontal
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));
        panel.setBackground(new Color(240, 240, 240));

        // Création et ajout des BoutonCarte avec bordure personnalisée
        for (int i = 1; i <= 5; i++) {
            BoutonCarte bouton = new BoutonCarte("Carte " + i);

            // Ajoute la bordure arrondie avec ombre
            bouton.setBorder(new BordureArrondieAvecOmbre(new Color(0, 120, 215), 2, 20));
            bouton.setPreferredSize(new Dimension(100, 40));
            bouton.setFocusPainted(false);
            bouton.setContentAreaFilled(false); // pour voir la bordure arrondie correctement

            panel.add(bouton);
        }

        frame.add(panel);
        frame.setVisible(true);
    }
}
