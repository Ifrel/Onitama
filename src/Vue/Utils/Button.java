package Vue.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static Global.Config.COULEUR_BOUTON_PLATEAU;

public class Button {

    public static JButton creerBouton(Color couleur, String texteOuCheminImage) {
        JButton bouton = new JButton();
        textOuImage(bouton, texteOuCheminImage);// Détection : texte ou image
        bouton.setBackground(couleur);
        bouton.setFocusPainted(true);   // Effet focus visible
        bouton.setContentAreaFilled(true);
        bouton.setOpaque(false);         // Important pour respecter le look L&F
        return bouton;
    }

    public static JButton creerBoutonPlateau() {
        JButton bouton = new JButton();
        bouton.setBackground(COULEUR_BOUTON_PLATEAU);
        bouton.setFocusPainted(true);   // Effet focus visible
        bouton.setContentAreaFilled(true);
        bouton.setOpaque(true);         // Important pour respecter le look L&F
        return bouton;
    }

    /**
     * Crée un bouton avec l'image d'une carte qui redimensionne l'image automatiquement avec la taille du bouton
     *
     * @param cheminImageCarte Chemin de l'image de la carte
     * @return JButton
     */
    public static JButton creerBoutonCarte(String cheminImageCarte) {
        ImageIcon icon = new ImageIcon(cheminImageCarte);
        JButton bouton = new JButton(icon);

        bouton.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = bouton.getWidth();
                int height = bouton.getHeight();

                // Redimensionne l'image à la nouvelle taille du bouton
                Image imageRedimensionnee = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                bouton.setIcon(new ImageIcon(imageRedimensionnee));

            }
        });

//        // Centre bien l'image
//        bouton.setHorizontalAlignment(SwingConstants.CENTER);
//        bouton.setVerticalAlignment(SwingConstants.CENTER);
//
//        // Supprime les bordures moches si besoin
//        bouton.setBorderPainted(true);
//        bouton.setFocusPainted(true);
//        bouton.setContentAreaFilled(false);


        return bouton;
    }


    public static JButton creerBoutonAction(String titre) {
        JButton bouton = new JButton(titre);

        // Couleurs
        bouton.setBackground(new Color(140, 140, 140)); // Fond gris clair
        bouton.setForeground(Color.WHITE);              // Texte blanc

        // Apparence
        bouton.setOpaque(true);                         // Rendre le fond visible
        bouton.setContentAreaFilled(true);
        bouton.setFocusPainted(false);
//        bouton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Bordure blanche de 2px

        // Optionnel : un peu plus joli
        bouton.setFont(new Font("Arial", Font.PLAIN, 22));  // Texte un peu plus gros
        bouton.setPreferredSize(new Dimension(150, 80));   // Taille agréable (change selon besoin)

        return bouton;
    }



    /************************************************************************************
     * ******************* FONTIONS OXILIAIRES ** ***************************************
     * **********************************************************************************/

    private static void textOuImage(JButton bouton, String texteOuCheminImage) {
        if (texteOuCheminImage.toLowerCase().endsWith(".png")
                || texteOuCheminImage.toLowerCase().endsWith(".jpg")
                || texteOuCheminImage.toLowerCase().endsWith(".jpeg")
                || texteOuCheminImage.toLowerCase().endsWith(".gif")
        ) {
            // C'est une image
            ImageIcon icon = new ImageIcon(texteOuCheminImage);
            bouton.setIcon(icon);
        } else {
            // C'est du texte
            bouton.setText(texteOuCheminImage);
            bouton.setFont(new Font("Arial", Font.PLAIN, 100));
        }
    }
}

