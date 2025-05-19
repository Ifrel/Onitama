package Vue.LabO;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExempleDesktopPane {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Fenêtre principale
            JFrame frame = new JFrame("Exemple avec JDesktopPane");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            // Création du bureau virtuel
            JDesktopPane desktop = new JDesktopPane();
            frame.setContentPane(desktop);

            // Bouton pour ouvrir une "fenêtre interne"
            JButton ouvrirOnglet = new JButton("Ouvrir une option");
            ouvrirOnglet.setBounds(20, 20, 200, 30);
            desktop.add(ouvrirOnglet);

            ouvrirOnglet.addActionListener((ActionEvent e) -> {
                // Crée une nouvelle fenêtre interne
                JInternalFrame internal = new JInternalFrame("Option", true, true, true, true);
                internal.setSize(300, 200);
                internal.setLocation((int) (Math.random() * 400), (int) (Math.random() * 300));
                internal.setVisible(true);

                // Contenu de l'onglet interne
                JPanel panel = new JPanel();
                panel.add(new JLabel("Choisissez une option :"));
                JComboBox<String> combo = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3"});
                panel.add(combo);

                internal.setContentPane(panel);
                desktop.add(internal);
                try {
                    internal.setSelected(true);
                } catch (java.beans.PropertyVetoException ignored) {}
            });

            frame.setVisible(true);
        });
    }
}
