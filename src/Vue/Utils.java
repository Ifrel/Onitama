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


    public static JLabel creerTitreOnglets(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18)); // Police personnalisée
        label.setPreferredSize(new Dimension(160, 30));  // Taille fixe de l’onglet
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }









    /**
     * <p><strong>GBCBuilder</strong> est une classe utilitaire conçue pour faciliter la construction fluide
     * et lisible d'objets {@link java.awt.GridBagConstraints} dans le cadre de la mise en page
     * avec {@link java.awt.GridBagLayout}.</p>
     *
     * <p>Elle encapsule et simplifie la configuration complexe des contraintes de placement
     * des composants Swing dans une grille.</p>
     *
     * <h3><strong>Principaux paramètres configurés :</strong></h3>
     * <ul>
     *   <li><strong><code>gridx</code></strong> : Position en <em>colonne</em> (0 = première colonne)</li>
     *   <li><strong><code>gridy</code></strong> : Position en <em>ligne</em> (0 = première ligne)</li>
     *   <li><strong><code>gridwidth</code></strong> : Nombre de <em>colonnes</em> occupées</li>
     *   <li><strong><code>gridheight</code></strong> : Nombre de <em>lignes</em> occupées</li>
     *   <li><strong><code>weightx</code></strong> : Poids <em>horizontal</em> (expansion horizontale)</li>
     *   <li><strong><code>weighty</code></strong> : Poids <em>vertical</em> (expansion verticale)</li>
     *   <li><strong><code>fill</code></strong> : Stratégie de <em>remplissage</em>
     *       (<code>NONE</code>, <code>HORIZONTAL</code>, <code>VERTICAL</code>, <code>BOTH</code>)</li>
     *   <li><strong><code>anchor</code></strong> : Position dans la cellule
     *       (<code>NORTH</code>, <code>CENTER</code>, etc.)</li>
     *   <li><strong><code>insets</code></strong> : <em>Marges externes</em> (haut, gauche, bas, droite)</li>
     *   <li><strong><code>ipadx</code>, <code>ipady</code></strong> : Remplissage <em>interne</em> (padding)</li>
     * </ul>
     *
     * <h3><strong>Exemple d'utilisation :</strong></h3>
     * <pre>{@code
     * JPanel panel = new JPanel(new GridBagLayout());
     *
     * GridBagConstraints gbc = new GridBagConstraints()
     * GBCBuilder gbcBuild = new GBCBuilder(gbc)
     *      .setTaille(2, 1)
     *      .setPoids(1.0, 1.0)
     *      .setRemplissage(GridBagConstraints.BOTH)
     *      .setMarges(10, 10, 10, 10);
     *      .reset()
     *
     * panel.add(new JButton("Valider"), gbc);
     * }</pre>
     *
     * @author Ifrel Rinel MAKOUNDIKA KIDZOUNOU
     * @version 1.0
     */
    public static class GBCBuilder {
        private final GridBagConstraints gbc;

        /**
         * Constructeur avec position obligatoire (gridx, gridy).
         *
         * @param Gbc l'objet GridBagConstraints entièrement à configurer.
         * @param colonne position horizontale de départ dans la grille
         * @param ligne position verticale de départ dans la grille  */
        public GBCBuilder(GridBagConstraints Gbc, int ligne, int colonne) {
            gbc = Gbc;
            reset();
            setPosition(ligne, colonne);
        }

        /**
         *
         * @param Gbc l'objet GridBagConstraints entièrement à configurer.
         * {@code colonne=0} position horizontale de départ dans la grille
         * {@code ligne=0} position verticale de départ dans la grille  */
        public GBCBuilder(GridBagConstraints Gbc) {
            gbc = Gbc;
            reset();
        }

        /** Définit la ligne et la colonne de départ */
        public void setPosition(int ligne, int colonne){
            gbc.gridy = ligne;
            gbc.gridx = colonne;
        }


        /**
           * Définit le nombre de lignes et de collones à occuper.   */
        public void setTaille(int largeur, int hauteur) {
            gbc.gridwidth = largeur;
            gbc.gridheight = hauteur;
        }


        /**
         * Définit l’importance (pondération) horizontale et verticale.   */
        public void setPoids(double poidsX, double poidsY) {
            gbc.weightx = poidsX;
            gbc.weighty = poidsY;
        }

        /**
         * Définit la manière dont le composant remplit sa cellule.
         *
         * {@code NONE ou 0} Pas d'agrandissement
         * {@code HORIZONTAL ou 2} Prend toute la largeur
         * {@code VERTICAL ou 3} Prend toute la hauteur
         * {@code BOTH ou 1} Prend toute la largeur et hauteur */
        public void setRemplissage(int mode) {
            switch (mode) {
                case GridBagConstraints.NONE:
                case GridBagConstraints.HORIZONTAL:
                case GridBagConstraints.VERTICAL:
                case GridBagConstraints.BOTH:
                    gbc.fill = mode;
                    break;
                default:
                    gbc.fill = GridBagConstraints.BOTH;
                    break;
            }
        }

        /**
         * Définit l’ancrage du composant dans la cellule.
         * public static final int CENTER = 10;
         *
         * {@code NORTH ou 11}
         * {@code NORTHEAST ou 12}
         * {@code EAST ou 13}
         * {@code SOUTHEAST ou 14}
         * {@code SOUTH ou 15}
         * {@code SOUTHWEST ou 16}
         * {@code WEST ou 17}
         * {@code NORTHWEST ou 18}, etc.
         * Pour les autre voir ici {@link java.awt.GridBagConstraints} */
        public void setAlignement(int ancrage) {
            gbc.anchor = ancrage;
        }

        /**
         * Définit les marges autour du composant.         */
        public void setMarges(int haut, int gauche, int bas, int droite) {
            gbc.insets = new Insets(haut, gauche, bas, droite);
        }

        /**
         * Définit le remplissage interne (espace à l’intérieur du composant).         */
        public void setPadding(int padX, int padY) {
            gbc.ipadx = padX;
            gbc.ipady = padY;
        }

          /**
           * Réinitialise un objet GridBagConstraints à ses valeurs par défaut.*/
          public void reset() {
              gbc.gridx = 0;
              gbc.gridy = 0;
              gbc.gridwidth = 1;
              gbc.gridheight = 1;
              gbc.weightx = 0.0;
              gbc.weighty = 0.0;
              gbc.fill = GridBagConstraints.NONE;
              gbc.anchor = GridBagConstraints.CENTER;
              gbc.insets = new Insets(0, 0, 0, 0);
              gbc.ipadx = 0;
              gbc.ipady = 0;
          }

      }

}




