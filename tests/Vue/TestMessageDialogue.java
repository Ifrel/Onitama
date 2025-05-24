package Vue;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import static Vue.Utils.MethodsStaticsUtils.afficherFonctionEnCours;

public class TestMessageDialogue {
    @Test
    public void principal() {
        SwingUtilities.invokeLater(() -> {
            JFrame cadre = new JFrame("Fenêtre principale");
            cadre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            cadre.setSize(300, 150);
            cadre.setLocationRelativeTo(null);

            JButton bouton = new JButton("Tester le dialog");
            bouton.addActionListener(e -> afficherFonctionEnCours());

            cadre.add(bouton);
            cadre.setVisible(true);
        });
    }
}

