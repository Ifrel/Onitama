package Vue.Utils.Boutons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static Global.Paths.PATH_CARTE;
import static Vue.ConfigUI.ARONDI;


/**
 * Bouton personnalisé avec animation de bordure fluide lorsqu'elle est activée.
 * En mode inactif, la bordure réagit au survol et au clic.
 * Peut avoir une couleur de fond et/ou une image de fond qui s'adapte.
 */
public class BoutonCarte extends JButton {

    private boolean animationActive = false;
    private float epaisseurBordure = 3f;
    private Color couleurBordureAnimation = new Color(205, 27, 181);
    private Color couleurBordureNormale = new Color(157, 154, 154);
    private Color couleurFond = new Color(255, 255, 255, 0); // Transparent par défaut
    private Color couleurFondSurvol = new Color(200, 220, 255, 100); // Survol souris
    private BufferedImage imageFond; // Added for background image
    private final float[] phase = {0f}; // Phase pour l’animation de la bordure

    private final Timer minuteur;
    private int arrondiCoins = 20;


    /**
     * Constructeur styles par défaut.     */
    public BoutonCarte(Path cheminImage) {
        this(
                "",
                2.5f,
                new Color(157, 154, 154),
                new Color(182, 12, 159),
                new Color(255, 255, 255, 0),
                new Color(160, 161, 165, 124),
                ARONDI,
                null // No background image by default
        );
        setImageFond(cheminImage);
    }

    /**
     * Constructeur styles par défaut.     */
    public BoutonCarte(BufferedImage image) {
        this(
                "",
                2.5f,
                new Color(157, 154, 154),
                new Color(182, 12, 159),
                new Color(255, 255, 255, 0),
                new Color(160, 161, 165, 124),
                ARONDI,
                null // No background image by default
        );
        setImageFond(image);
    }


    /**
     * Constructeur styles par défaut.     */
    public BoutonCarte() {
        this(
                "",
                3f,
                new Color(157, 154, 154),
                new Color(205, 27, 181),
                new Color(255, 255, 255, 0),
                new Color(200, 220, 255, 100),
                ARONDI,
                null // No background image by default
        );
    }


    /**
     * Constructeur styles par défaut.     */
    public BoutonCarte(String texte) {
        this(
                texte,
                3f,
                new Color(157, 154, 154),
                new Color(205, 27, 181),
                new Color(255, 255, 255, 0),
                new Color(200, 220, 255, 100),
                ARONDI,
                null // No background image by default
        );
    }



    /**
     * Constructeur avec styles personnalisables sans image.
     *
     * @param texte Le texte affiché sur le bouton.
     * @param epaisseurBordure Épaisseur de la bordure.
     * @param couleurBordureNormale Couleur de bordure en mode inactif.
     * @param couleurBordureAnimation Couleur utilisée dans l’animation.
     * @param couleurFond Couleur de fond normale.
     * @param couleurFondSurvol Couleur de fond lors du survol.
     * @param arrondiCoins Rayon des coins arrondis.
     */
    public BoutonCarte(
            String texte,
            float epaisseurBordure,
            Color couleurBordureNormale,
            Color couleurBordureAnimation,
            Color couleurFond,
            Color couleurFondSurvol,
            int arrondiCoins)
    {
        this(   texte,
                epaisseurBordure,
                couleurBordureNormale,
                couleurBordureAnimation,
                couleurFond, couleurFondSurvol,
                arrondiCoins,
                null);
    }


    /**
     * Constructeur avec styles personnalisables et option d'image de fond.
     *
     * @param texte Le texte affiché sur le bouton.
     * @param epaisseurBordure Épaisseur de la bordure.
     * @param couleurBordureNormale Couleur de bordure en mode inactif.
     * @param couleurBordureAnimation Couleur utilisée dans l’animation.
     * @param couleurFond Couleur de fond normale (ignorée si une image de fond est définie).
     * @param couleurFondSurvol Couleur de fond lors du survol (ignorée si une image de fond est définie).
     * @param arrondiCoins Rayon des coins arrondis.
     * @param imageFond L'image de fond à afficher (peut être null).
     */
    public BoutonCarte(
            String texte,
            float epaisseurBordure,
            Color couleurBordureNormale,
            Color couleurBordureAnimation,
            Color couleurFond,
            Color couleurFondSurvol,
            int arrondiCoins,
            BufferedImage imageFond)
    {
        super(texte);
        this.epaisseurBordure = epaisseurBordure;
        this.couleurBordureNormale = couleurBordureNormale;
        this.couleurBordureAnimation = couleurBordureAnimation;
        this.couleurFond = couleurFond;
        this.couleurFondSurvol = couleurFondSurvol;
        this.arrondiCoins = arrondiCoins;
        this.imageFond = imageFond; // Initialize background image

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false); // Needed to allow custom painting of background/image
        setOpaque(false); // Ensure custom background/image is painted
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Écouteur de souris pour gérer les effets visuels lors du survol
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                repaint();
            }
        });

        // Clic pour activer/désactiver l’animation
        addActionListener(e -> {
            animationActive = !animationActive;
            if (animationActive) {
                demarrerAnimation();
            } else {
                arreterAnimation();
            }
            repaint();
        });

        // Minuteur pour animer la bordure
        minuteur = new Timer(30, e -> {
            phase[0] += 2f;
            if (phase[0] > 1000f) phase[0] = 0f;
            repaint();
        });

    }



    @Override
    protected void paintComponent(Graphics g) {
        // Création d'une copie de l'objet Graphics avec les propriétés 2D
        Graphics2D g2 = (Graphics2D) g.create();

        // Activation de l'anticrénelage pour un rendu plus lisse
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int largeur = getWidth();
        int hauteur = getHeight();

        // Création d'un rectangle arrondi pour le découpage (clipping)
        RoundRectangle2D.Float roundedRect = new RoundRectangle2D.Float(0, 0, largeur, hauteur, arrondiCoins, arrondiCoins);

        // Application du découpage (clip) au contexte graphique
        g2.setClip(roundedRect);

        // Dessin du fond : image si disponible, sinon couleur
        if (imageFond != null) {
            // Dessiner l’image ajustée à la taille du bouton
            g2.drawImage(imageFond, 0, 0, largeur, hauteur, null);

            // Si l'animation est désactivée et que la souris survole le bouton, appliquer une couleur de survol par-dessus l’image
            if (!animationActive && getModel().isRollover()) {
                g2.setColor(couleurFondSurvol);
                g2.fillRoundRect(0, 0, largeur, hauteur, arrondiCoins, arrondiCoins);
            }

        } else {
            // Si aucune image n’est disponible, dessiner la couleur de fond normale
            g2.setColor(couleurFond);
            g2.fillRoundRect(0, 0, largeur, hauteur, arrondiCoins, arrondiCoins);

            // En cas de survol et si aucune animation n’est active, appliquer la couleur de survol
            if (!animationActive && getModel().isRollover()) {
                g2.setColor(couleurFondSurvol);
                g2.fillRoundRect(0, 0, largeur, hauteur, arrondiCoins, arrondiCoins);
            }
        }

//        // Suppression du découpage pour permettre un dessin non limité de la bordure
//        g2.setClip(null);

        // Dessin de la bordure (soit animée, soit simple)
        if (animationActive) {
            // Bordure animée : segments avec effets de transparence dégressive
            int segments = 8; // Nombre de segments animés
            float epaisseur = epaisseurBordure + 1;
            float longueurTrait = 12f; // Longueur de chaque trait
            float espace = 10f;        // Espace entre les traits

            for (int i = 0; i < segments; i++) {
                float progress = (float) i / segments;
                float alpha = (float) Math.pow(1 - progress, 2); // Transparence croissante vers la fin

                // Définition de la couleur du segment avec transparence
                Color couleurSegment = new Color(
                        couleurBordureAnimation.getRed(),
                        couleurBordureAnimation.getGreen(),
                        couleurBordureAnimation.getBlue(),
                        (int) (alpha * 255)
                );

                g2.setColor(couleurSegment);

                // Décalage du motif animé (basé sur la phase)
                float decalage = phase[0] + (i * (longueurTrait + espace));

                // Définition du trait avec motif pointillé animé
                g2.setStroke(new BasicStroke(epaisseur,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER,
                        10f, new float[]{longueurTrait, espace}, decalage));

                // Dessin du rectangle arrondi représentant la bordure
                g2.drawRoundRect(
                        (int) (epaisseurBordure / 2),
                        (int) (epaisseurBordure / 2),
                        largeur - (int) epaisseurBordure,
                        hauteur - (int) epaisseurBordure,
                        arrondiCoins, arrondiCoins
                );
            }
        } else {
            // Bordure simple sans animation
            g2.setColor(couleurBordureNormale);
            g2.setStroke(new BasicStroke(epaisseurBordure));
            g2.drawRoundRect(
                    (int) (epaisseurBordure / 2),
                    (int) (epaisseurBordure / 2),
                    largeur - (int) epaisseurBordure,
                    hauteur - (int) epaisseurBordure,
                    arrondiCoins, arrondiCoins
            );
        }

        // Libération des ressources graphiques utilisées
        g2.dispose();

        // Appel au composant parent pour dessiner le texte ou autres éléments de Swing
        super.paintComponent(g);
    }

    // --- MODIFIED getPreferredSize ---
    @Override
    public Dimension getPreferredSize() {
        // Remove the problematic calculation based on parent size.
        // Return a sensible default size or rely on the superclass
        // for text-based preferred size.
        if (getText() != null && !getText().isEmpty()) {
            // If text exists, let JButton calculate based on text/font
            Dimension superSize = super.getPreferredSize();
            // Add some padding for the custom border and corners
            int padding = (int) (epaisseurBordure * 2 + Math.max(0, arrondiCoins / 2));
            return new Dimension(superSize.width + padding, superSize.height + padding);
        } else if (imageFond != null) {
            // If image exists but no text, return a default size or size based on image aspect ratio?
            // For filling layouts, a default minimum is sufficient.
            return new Dimension(120, 50); // Default size for image-only button (example)
        }
        // Default size for an empty button
        return new Dimension(100, 50); // Example sensible default minimum
    }

    @Override
    public Dimension getMinimumSize() {
        // Define a minimum size to prevent it from becoming too small
        if (getText() != null && !getText().isEmpty()) {
            Dimension superSize = super.getMinimumSize();
            int padding = (int) (epaisseurBordure * 2 + Math.max(0, arrondiCoins / 2));
            return new Dimension(superSize.width + padding, superSize.height + padding);
        } else if (imageFond != null) {
            return new Dimension(50, 50); // Minimum size for image-only
        }
        return new Dimension(40, 20); // Minimum size for empty button
    }

    @Override
    public Dimension getMaximumSize() {
        // Allow it to expand freely when in a layout that permits it
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }


    // -----------------------------
    // Méthodes de configuration (rest unchanged)
    // -----------------------------


    /**
     * Définit l'épaisseur de la bordure et redessine le composant.
     */
    public void setEpaisseurBordure(float epaisseur) {
        this.epaisseurBordure = epaisseur;
        // Recalculate preferred size as border thickness affects it
        revalidate(); // Inform parent that size might have changed
        repaint();
    }



    /**
     * Définit la couleur normale de la bordure (sans animation) et redessine.
     */
    public void setCouleurBordureNormale(Color couleur) {
        this.couleurBordureNormale = couleur;
        repaint();
    }



    /**
     * Définit la couleur utilisée pour l'animation de la bordure et redessine.
     */
    public void setCouleurBordureAnimation(Color couleur) {
        this.couleurBordureAnimation = couleur;
        repaint();
    }




    /**
     * Définit la couleur de fond du bouton.
     * Note : La couleur de fond est visible si aucune image de fond n'est définie.
     * @param couleur La nouvelle couleur de fond.
     */
    public void setCouleurFond(Color couleur) {
        this.couleurFond = couleur;
        // Opaque si la couleur est entièrement opaque ET qu'il n'y a pas d'image de fond
        setOpaque(couleur.getAlpha() == 255 && imageFond == null);
        repaint();
    }



    /**
     * Définit la couleur de fond lors du survol du bouton.
     * Note : La couleur de fond de survol est visible si aucune animation n'est active
     * et qu'aucune image de fond n'est définie.
     * @param couleur La nouvelle couleur de fond de survol.
     */
    public void setCouleurFondSurvol(Color couleur) {
        this.couleurFondSurvol = couleur;
        repaint();
    }

    public void setArrondiCoins(int rayon) {
        this.arrondiCoins = rayon;
        // Recalculate preferred size as corner radius can affect paintable area
        revalidate(); // Inform parent that size might have changed
        repaint();
    }



    /**
     * Définit l'image à utiliser comme fond du bouton.
     * L'image sera redimensionnée pour s'adapter à la taille du bouton.
     * Si une image est définie, les couleurs de fond normale et de survol
     * ne seront pas utilisées pour le fond principal.
     * @param imagePath Le chemin vers le fichier image. Peut être null pour supprimer l'image.
     */
    public void setImageFond(Path imagePath) {
        if (imagePath == null) {
            this.imageFond = null;
        } else {
            try {
                // Load the image from the Path
                this.imageFond = ImageIO.read(imagePath.toFile());
                // Re-evaluate opaque state based on the presence of an image
                setOpaque(couleurFond.getAlpha() == 255 && imageFond == null);
            } catch (IOException e) {
                System.err.println("Erreur lors du chargement de l'image : " + imagePath);
                e.printStackTrace();
                this.imageFond = null; // Ensure imageFond is null on error
                setOpaque(couleurFond.getAlpha() == 255 && imageFond == null);
            }
        }
        // Recalculate preferred size as image affects it
        revalidate(); // Inform parent that size might have changed
        repaint(); // Repaint to show the new background
    }


    /**
     * Définit l'image à utiliser comme fond du bouton directement à partir d'un BufferedImage.
     * @param image L'image de fond à utiliser (peut être null).
     */
    public void setImageFond(BufferedImage image) {
        this.imageFond = image;
        // Re-evaluate opaque state based on the presence of an image
        setOpaque(couleurFond.getAlpha() == 255 && imageFond == null);
        // Recalculate preferred size as image affects it
        revalidate(); // Inform parent that size might have changed
        repaint();
    }


    /**
     * Supprime l'image de fond et réaffiche le fond en couleur.
     */
    public void removeImageFond() {
        this.imageFond = null;
        repaint();
    }



    public void demarrerAnimation() {
        animationActive = true;
        minuteur.start();
    }

    public void arreterAnimation() {
        animationActive = false;
        minuteur.stop();
    }

}




class Main {

    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
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
}


class TestBoutonCarte {

    public static void main(String[] args) {
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