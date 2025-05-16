package Vue;

import Vue.Utils.PanelRatioFixe;

import javax.swing.*;
import java.awt.*;

public class TestPanelRatioFixe {
    /**
     * Méthode utilitaire pour des tests simples.
     * @param args non utilisés
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test PanelRatioFixe");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // Contenu simple : un JLabel avec un fond coloré
            JLabel contenuLabel = new JLabel("Contenu (16:9)", SwingConstants.CENTER);
            contenuLabel.setOpaque(true);
            contenuLabel.setBackground(Color.ORANGE);
            contenuLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            // On donne une taille préférée au contenu pour que getPreferredSize de PanelRatioFixe fonctionne bien
            contenuLabel.setPreferredSize(new Dimension(320, 180));


            // Panneau avec ratio 16:9 pour le label
            PanelRatioFixe panelRatio169 = new PanelRatioFixe(contenuLabel, 16.0/9.0);
            panelRatio169.setBorder(BorderFactory.createTitledBorder("Panneau 16:9"));
            panelRatio169.setBackground(Color.LIGHT_GRAY); // Pour voir les limites du PanelRatioFixe
            panelRatio169.setOpaque(true); // Rendre le PanelRatioFixe opaque pour voir son fond

            // Contenu carré
            JPanel contenuCarre = new JPanel();
            contenuCarre.setBackground(Color.CYAN);
            contenuCarre.setPreferredSize(new Dimension(150,150)); // Taille préférée pour le contenu
            JLabel labelCarre = new JLabel("Carré (1:1)");
            contenuCarre.add(labelCarre);


            PanelRatioFixe panelCarre = new PanelRatioFixe(contenuCarre); // Ratio 1:1 par défaut
            panelCarre.setBorder(BorderFactory.createTitledBorder("Panneau Carré (1:1)"));
            panelCarre.setBackground(Color.DARK_GRAY);
            panelCarre.setOpaque(true);


            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelRatio169, panelCarre);
            splitPane.setResizeWeight(0.5); // Distribue l'espace équitablement lors du redimensionnement

            frame.add(splitPane, BorderLayout.CENTER);

            // Un simple bouton pour observer le comportement avec d'autres composants
            JButton button = new JButton("Un bouton en bas");
            frame.add(button, BorderLayout.SOUTH);

            frame.setSize(800, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // Forcer un ajustement initial après affichage, au cas où.
            // Normalement, componentResized et l'invokeLater dans le constructeur devraient suffire.
            // panelRatio169.ajusterGeometrieContenu();
            // panelCarre.ajusterGeometrieContenu();
        });
    }
}
