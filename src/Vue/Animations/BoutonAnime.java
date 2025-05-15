package Vue.Animations;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D; // Needed for clipping
import java.awt.image.BufferedImage; // Needed for image
import java.nio.file.Path; // Needed for Path
import javax.imageio.ImageIO; // Needed for reading images
import java.io.IOException; // Needed for handling file read errors

import static Global.Paths.PATH_CARTE;
import static Global.Paths.PATH_PION_BLEU_ETUDIANT_CLIQUE;


/**
 * Bouton personnalisé avec animation de bordure fluide lorsqu'elle est activée.
 * En mode inactif, la bordure réagit au survol et au clic.
 * Peut avoir une couleur de fond et/ou une image de fond qui s'adapte.
 */
public class BoutonAnime extends JButton {

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
     * Constructeur avec texte par défaut et styles par défaut.
     *
     * @param texte Le texte affiché sur le bouton.
     */
    public BoutonAnime(String texte) {
        this(
                texte,
                3f,
                new Color(157, 154, 154),
                new Color(205, 27, 181),
                new Color(255, 255, 255, 0),
                new Color(200, 220, 255, 100),
                20,
                null // No background image by default
        );
    }


    /**
     * Constructeur avec styles personnalisables.
     *
     * @param texte Le texte affiché sur le bouton.
     * @param epaisseurBordure Épaisseur de la bordure.
     * @param couleurBordureNormale Couleur de bordure en mode inactif.
     * @param couleurBordureAnimation Couleur utilisée dans l’animation.
     * @param couleurFond Couleur de fond normale.
     * @param couleurFondSurvol Couleur de fond lors du survol.
     * @param arrondiCoins Rayon des coins arrondis.
     */
    public BoutonAnime(String texte, float epaisseurBordure, Color couleurBordureNormale, Color couleurBordureAnimation, Color couleurFond, Color couleurFondSurvol, int arrondiCoins) {
        this(
                texte,
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
    public BoutonAnime(
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



    public void setEpaisseurBordure(float epaisseur) {
        this.epaisseurBordure = epaisseur;
        repaint();
    }

    public void setCouleurBordureNormale(Color couleur) {
        this.couleurBordureNormale = couleur;
        repaint();
    }

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
        repaint();
    }



    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int largeur = getWidth();
        int hauteur = getHeight();

        // Create a rounded rectangle shape for clipping
        RoundRectangle2D.Float roundedRect = new RoundRectangle2D.Float(0, 0, largeur, hauteur, arrondiCoins, arrondiCoins);

        // Set the clip shape
        g2.setClip(roundedRect);

        // Draw background image if available, otherwise draw background color
        if (imageFond != null) {
            // Draw the image scaled to the button's size within the clip
            g2.drawImage(imageFond, 0, 0, largeur, hauteur, null);

            // Draw rollover background color ON TOP of the image if rollover (optional, based on desired effect)
            // This makes the rollover color act as an overlay
            if (!animationActive && getModel().isRollover()) {
                g2.setColor(couleurFondSurvol);
                g2.fillRoundRect(0, 0, largeur, hauteur, arrondiCoins, arrondiCoins); // Fill within the clip
            }

        } else {
            // Draw normal background color
            g2.setColor(couleurFond);
            g2.fillRoundRect(0, 0, largeur, hauteur, arrondiCoins, arrondiCoins);

            // Draw rollover background color if not animating and is rollover
            if (!animationActive && getModel().isRollover()) {
                g2.setColor(couleurFondSurvol);
                g2.fillRoundRect(0, 0, largeur, hauteur, arrondiCoins, arrondiCoins);
            }
        }


        // Reset the clip so the border is not clipped
        g2.setClip(null);

        // Draw border (animated or normal)
        if (animationActive) {
            int segments = 8;
            float epaisseur = epaisseurBordure + 1;
            float longueurTrait = 12f;
            float espace = 10f;

            for (int i = 0; i < segments; i++) {
                float progress = (float) i / segments;
                float alpha = (float) Math.pow(1 - progress, 2); // Plus transparent vers la fin

                Color couleurSegment = new Color(
                        couleurBordureAnimation.getRed(),
                        couleurBordureAnimation.getGreen(),
                        couleurBordureAnimation.getBlue(),
                        (int) (alpha * 255)
                );

                g2.setColor(couleurSegment);

                float decalage = phase[0] + (i * (longueurTrait + espace));

                g2.setStroke(new BasicStroke(epaisseur,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER,
                        10f, new float[]{longueurTrait, espace}, decalage));

                g2.drawRoundRect(
                        (int) (epaisseurBordure / 2),
                        (int) (epaisseurBordure / 2),
                        largeur - (int) epaisseurBordure,
                        hauteur - (int) epaisseurBordure,
                        arrondiCoins, arrondiCoins
                );
            }
        } else {
            // Bordure simple
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

        g2.dispose();
        super.paintComponent(g); // Affichage du texte
    }



    private void demarrerAnimation() {
        minuteur.start();
    }

    private void arreterAnimation() {
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

        // --- Define a placeholder path for the test image ---
        // *** IMPORTANT: Replace this with the actual path to an image file on your system ***
        Path testImagePath = PATH_CARTE.resolve("TIGRE.png");
        // Example for a file in the same directory: Path testImagePath = Paths.get("test_image.png");
        // Example for a file in a specific absolute path: Path testImagePath = Paths.get("C:/Users/YourUser/Pictures/my_image.jpg");
        // ---------------------------------------------------


        // Create buttons demonstrating different features

        // 1. Button with default style (Color background)
        BoutonAnime defaultButton = new BoutonAnime("Default Style");

        // 2. Button with custom colors and border thickness (Color background)
        BoutonAnime customButton1 = new BoutonAnime(
                "Custom Colors",
                5f,                          // Border thickness
                Color.MAGENTA.darker(),      // Base border color
                Color.ORANGE,                // Animation border color
                new Color(220, 255, 220),    // Background color (light green)
                new Color(180, 255, 180, 150), // Rollover background (more opaque light green)
                15                           // Arc radius
        );

        // 3. Button with custom style using setters (Color background)
        BoutonAnime customButton2 = new BoutonAnime("Custom Setters");
        customButton2.setEpaisseurBordure(2f);
        customButton2.setCouleurBordureNormale(Color.BLUE);
        customButton2.setCouleurBordureAnimation(Color.RED);
        customButton2.setCouleurFond(new Color(240, 240, 255)); // Light blue background
        customButton2.setCouleurFondSurvol(new Color(200, 200, 255)); // More opaque light blue
        customButton2.setArrondiCoins(30); // More rounded corners
        customButton2.setForeground(Color.DARK_GRAY); // Set text color

        // 4. Button with semi-transparent background (Color background)
        BoutonAnime customButton3 = new BoutonAnime(
                "Semi-Transparent BG",
                4f,
                new Color(50, 50, 50),      // Dark gray base border
                new Color(0, 255, 255),     // Cyan animation
                new Color(255, 255, 255, 100), // Semi-transparent white background
                new Color(255, 255, 255, 200),// More opaque white rollover
                10
        );
        customButton3.setForeground(Color.BLACK); // Ensure text is visible on light background

        // --- New Test Cases Demonstrating Image Background ---

        // 5. Button with a background image from Path
        BoutonAnime imageButton1 = new BoutonAnime("Image Background");
        imageButton1.setImageFond(testImagePath);
        imageButton1.setForeground(Color.WHITE); // Set text color to be visible on potentially dark image

        // 6. Button with image background and custom border colors
        BoutonAnime imageButton2 = new BoutonAnime(
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

        // 7. Button with image background and different animation colors
        BoutonAnime imageButton3 = new BoutonAnime("Image + Anim Color");
        imageButton3.setImageFond(testImagePath);
        imageButton3.setCouleurBordureAnimation(Color.RED); // Change animation color
        imageButton3.setEpaisseurBordure(3f);
        imageButton3.setArrondiCoins(5); // Less rounded
        imageButton3.setForeground(Color.PINK);

        // 8. Button with image background and a more visible rollover overlay
        BoutonAnime imageButton4 = new BoutonAnime("Image + Rollover Overlay");
        imageButton4.setImageFond(testImagePath);
        // Set a semi-transparent black rollover color for a darkening effect
        imageButton4.setCouleurFondSurvol(new Color(0, 0, 0, 100));
        imageButton4.setForeground(Color.ORANGE);


        // 9. Button with image background and thinner border
        BoutonAnime imageButton5 = new BoutonAnime("Image + Thin Border");
        imageButton5.setImageFond(testImagePath);
        imageButton5.setEpaisseurBordure(1.5f); // Thinner border
        imageButton5.setCouleurBordureNormale(Color.GRAY);
        imageButton5.setCouleurBordureAnimation(Color.WHITE);
        imageButton5.setForeground(Color.BLACK); // Set text color
        imageButton2.setPreferredSize(new Dimension(200,100));

        // Add action listeners (optional, but good for testing functionality)
        defaultButton.addActionListener(e -> System.out.println("Default button clicked!"));
        imageButton1.addActionListener(e -> System.out.println("Image button 1 clicked!"));


        // Add the buttons to the frame
        frame.add(defaultButton);
        frame.add(customButton1);
        frame.add(customButton2);
        frame.add(customButton3);
        frame.add(imageButton1); // Add new buttons
        frame.add(imageButton2);
        frame.add(imageButton3);
        frame.add(imageButton4);
        frame.add(imageButton5);


        // Pack the frame and make it visible
        frame.pack(); // Adjusts the window size to fit the components
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }
}


