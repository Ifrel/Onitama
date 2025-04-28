package Vue.Utlis;

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

    public static JButton creerBoutonPlateau(){
        JButton bouton = new JButton();
        bouton.setBackground(COULEUR_BOUTON_PLATEAU);
        bouton.setFocusPainted(true);   // Effet focus visible
        bouton.setContentAreaFilled(true);
        bouton.setOpaque(true);         // Important pour respecter le look L&F
        return bouton;
    }

    /**
     * Crée un bouton avec l'image d'une carte qui redimensionne l'image automatiquement avec la taille du bouton
     * @param cheminImageCarte Chemin de l'image de la carte
     * @return JButton
     */
    public static JButton creerBoutonCarte(String cheminImageCarte){
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


    public static JButton creerBoutonAnnuler(){
        JButton bouton = new JButton();
//        bouton.setBackground(new Color(175,175,175));
        bouton.setText("Annuler");
//        bouton.setFont(new Font("Arial", Font.PLAIN, 18));
//        bouton.setFocusPainted(true);   // Effet focus visible
//        bouton.setContentAreaFilled(true);
//        bouton.setPreferredSize(new Dimension(50,25));
        return bouton;
    }

    public static JButton creerBoutonRefaire(){
        JButton bouton = new JButton();
//        bouton.setBackground(new Color(175,175,175));
        bouton.setText("Refaire");
//        bouton.setFont(new Font("Arial", Font.PLAIN, 18));
//        bouton.setFocusPainted(true);   // Effet focus visible
//        bouton.setContentAreaFilled(true);
//        bouton.setPreferredSize(new Dimension(50,25));
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

