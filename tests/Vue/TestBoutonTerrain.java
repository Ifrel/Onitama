package Vue;

import Vue.Utils.Boutons.BoutonTerrain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

import static Global.Paths.*;
import static Global.Paths.PATH_PION_BLEU_ETUDIANT;
import static Global.Paths.PATH_PION_NOIR_MAITRE;


@DisplayName("TestBoutonTerrain Tests")
class TestBoutonTerrain {
    @Test
    public void Test1() {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Test complet BoutonTerrain");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

            // Bouton initial avec image par défaut
            BoutonTerrain bouton1 = new BoutonTerrain(Path.of(PATH_PION_NOIR_ETUDIANT.getPath()));
            bouton1.setPreferredSize(new Dimension(120, 120));
            bouton1.setEnabled(false);
            f.add(bouton1);

            // Bouton avec une autre image et taille plus grande
            BoutonTerrain bouton2 = new BoutonTerrain(Path.of(PATH_PION_ROUGE_MAITRE.getPath()));
            bouton2.setPreferredSize(new Dimension(150, 150));
            f.add(bouton2);

            // Bouton testant la méthode de changement d'image dynamique après 3 secondes
            BoutonTerrain bouton3 = new BoutonTerrain(Path.of(PATH_BTN_MODE_AUTO_OFF.getPath()));
            bouton3.setPreferredSize(new Dimension(120, 120));
            f.add(bouton3);

            // Lance un timer pour changer l'image de bouton3 après 3 secondes
            new Timer(3000, e -> {
                ImageIcon nouvelleImage = new ImageIcon(PATH_PION_BLEU_ETUDIANT.toString());
                bouton3.changerImage(nouvelleImage);
                System.out.println("Image changée dynamiquement !");
            }).start();

            // Bouton sans animation activée initialement, on l'active après 5 secondes
            BoutonTerrain bouton4 = new BoutonTerrain(Path.of(PATH_PION_BLEU_ETUDIANT.getPath()));
            bouton4.setPreferredSize(new Dimension(120, 120));
            f.add(bouton4);

            new Timer(5000, e -> {
                bouton4.activerAnimation(true);
                System.out.println("Animation activée !");
            }).start();

            // Bouton testant activation/désactivation animation via clic (par défaut)
            BoutonTerrain bouton5 = new BoutonTerrain(Path.of(PATH_PION_NOIR_MAITRE.getPath()));
            bouton5.setPreferredSize(new Dimension(120, 120));
            f.add(bouton5);

            f.setSize(800, 400);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}