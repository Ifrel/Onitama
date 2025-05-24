package Vue;

import Vue.Utils.Boutons.BoutonCarte;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static Global.Paths.PATH_CARTE;

public class TestBoutonCarte {
        @Test
        public void test1() {
            // Create the main window
            JFrame frame = new JFrame("Animation Button Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Simple layout

            Path testImagePath = PATH_CARTE.resolve("TIGRE.png");



            BoutonCarte imageButton2 = new BoutonCarte(
                    "Image + Custom Border",
                    6f,                           // Thicker border
                    Color.YELLOW,                 // Yellow base border
                    Color.GREEN,                  // Green animation color
                    new Color(0,0,0,0),           // Background color (ignored if image exists)
                    new Color(0,0,0,50),          // Rollover overlay (slightly dark overlay)
                    25,                           // Custom arc
                    null                          // No image in constructor, will set later
            );
            imageButton2.setImageFond(testImagePath); // Set image using setter
            imageButton2.setForeground(Color.CYAN); // Set text color



            // 9. Button with image background and thinner border
            BoutonCarte imageButton5 = new BoutonCarte(testImagePath);
            imageButton5.setPreferredSize(new Dimension(200,100));
            imageButton5.setToolTipText("TIGRE");


            frame.add(imageButton2);
            frame.add(imageButton5);


            // Pack the frame and make it visible
            frame.pack(); // Adjusts the window size to fit the components
            frame.setLocationRelativeTo(null); // Center the window
            frame.setVisible(true);
        }

        @Test
        public void test2() {
            // Chargement des images dans une liste
            ArrayList<BufferedImage> images = new ArrayList<>();
            try {
                images.add(ImageIO.read(PATH_CARTE.resolve("TIGRE.png").toFile()));
                images.add(ImageIO.read(PATH_CARTE.resolve("COQ.png").toFile()));
                images.add(ImageIO.read(PATH_CARTE.resolve("COBRA.png").toFile()));
                images.add(ImageIO.read(PATH_CARTE.resolve("DRAGON.png").toFile()));
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Erreur lors du chargement des images.");
                System.exit(1);
            }

            // Création du bouton avec la première image
            BoutonCarte bouton = new BoutonCarte();
            bouton.setPreferredSize(new Dimension(200,100));
            bouton.setImageFond(images.get(0));

            // Fenêtre Swing simple
            JFrame frame = new JFrame("Test BoutonCarte");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());

            frame.add(bouton);

            // Index pour parcourir les images
            final int[] index = {0};

            // ActionListener pour changer l'image à chaque clic
            bouton.addActionListener((ActionEvent e) -> {
                index[0] = (index[0] + 1) % images.size();  // passage à l'image suivante
                bouton.setImageFond(images.get(index[0]));
                bouton.repaint();
            });

            frame.setSize(500, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
}
