package Vue;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static Global.Config.COULEUR_BOUTON_PLATEAU;

public class Utils {


    public static JButton creerBoutonTerrain() {
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
     * @return JButton     */
    public static JButton creerBoutonCarte(String cheminImageCarte) {
        return new JButton();
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


    /**
     * Applique une image d'arrière-plan à un composant Swing avec une transparence personnalisée.
     *
     * @param component      Le composant auquel ajouter l'image en arrière-plan.
     * @param imagePath      Le chemin d'accès vers l'image (format supporté par ImageIO : .jpg, .png, etc.).
     * @param transparency   Niveau de transparence de l'image (0.0f = totalement transparent, 1.0f = totalement opaque).
     */
    public static void mettreImageEnFond(JComponent component, String imagePath, float transparency) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));

            JLabel backgroundLabel = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));

                    // Calculer échelle pour remplir sans déformation
                    int panelWidth = getWidth();
                    int panelHeight = getHeight();
                    float ratio = Math.min((float)panelWidth / image.getWidth(), (float)panelHeight / image.getHeight());

                    int imgWidth = (int)(image.getWidth() * ratio);
                    int imgHeight = (int)(image.getHeight() * ratio);

                    int x = (panelWidth - imgWidth) / 2;
                    int y = (panelHeight - imgHeight) / 2;

                    g2d.drawImage(image, x, y, imgWidth, imgHeight, this);
                    g2d.dispose();
                }
            };

            backgroundLabel.setOpaque(false);
            backgroundLabel.setBounds(0, 0, component.getWidth(), component.getHeight());

            component.setLayout(null);
            component.add(backgroundLabel);
            component.setComponentZOrder(backgroundLabel, component.getComponentCount() - 1);

            // Suivre redimensionnement
            component.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentResized(java.awt.event.ComponentEvent evt) {
                    backgroundLabel.setSize(component.getSize());
                    backgroundLabel.repaint();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    public static JButton creerBoutonCarte(String cheminImageCarte) {
        ImageIcon iconOriginal = new ImageIcon(cheminImageCarte);
        JButton bouton = new JButton(new ImageIcon(iconOriginal.getImage())); // Utiliser une copie initiale

        bouton.setHorizontalAlignment(SwingConstants.CENTER);
        bouton.setVerticalAlignment(SwingConstants.CENTER);
        bouton.setBorderPainted(false);  // Optionnel pour rendre plus joli
        bouton.setFocusPainted(false);
        bouton.setContentAreaFilled(false);

        bouton.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = bouton.getWidth();
                int height = bouton.getHeight();
                if (width > 0 && height > 0) {
                    // Redimensionner proprement à partir de l'image d'origine
                    Image imageRedimensionnee = iconOriginal.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    bouton.setIcon(new ImageIcon(imageRedimensionnee));
                }
            }
        });

        return bouton;
    }
*/

    public static JButton creerBoutonActionMenu(String titre){
        JButton bouton = new JButton(titre);
        return bouton;
    }


}


