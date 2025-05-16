package Vue.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Panneau affichant un fond de bruit gris léger animé avec des points colorés se déplaçant.
 * L'animation du bruit et des points peut être activée ou désactivée.
 * Une couleur de base est affichée lorsque l'animation est désactivée.
 */
public class PanelBruitGris extends JPanel {

    private BufferedImage noiseTexture;                     // Texture de bruit gris pré-calculée pour l'arrière-plan
    private int textureWidth = 100;                         // Largeur de la texture de bruit (constante, impacte la finesse du bruit)
    private int textureHeight = 100;                        // Hauteur de la texture de bruit (constante, impacte la finesse du bruit)
    private double phaseX = 0;                              // Phase pour l'animation de défilement horizontal du bruit
    private double phaseY = 0;                              // Phase pour l'animation de défilement vertical du bruit
    private static final int DELAI_TIMER = 40;              // Délai en millisecondes entre chaque frame de l'animation (impacte la fluidité)
    private static final double VITESSE_X_BRUIT = 0.1;      // Vitesse de défilement horizontal du bruit (impacte la vitesse de l'animation du fond)
    private static final double VITESSE_Y_BRUIT = 0.05;     // Vitesse de défilement vertical du bruit (impacte la vitesse de l'animation du fond)
    private static final float AMPLITUDE_BRUIT = 0.02f;     // Amplitude des variations de bruit (intensité, 0.0 = gris uni)
    private static final float LUMINOSITE_CENTRALE = 0.5f;  // Valeur de gris de base (0.0 = noir, 1.0 = blanc)

    private List<PointAnime> points = new ArrayList<>();    // Liste pour stocker les informations de chaque point animé
    private Random random = new Random();                   // Générateur de nombres aléatoires
    private static final int NOMBRE_POINTS = 10;            // Nombre de points colorés à afficher (constante)
    private static final int TAILLE_POINT = 3;              // Taille (diamètre) des points en pixels (constante)
    private static final double VITESSE_MAX_POINT = 1.5;    // Vitesse maximale de déplacement des points (impacte la vitesse des points)

    private boolean animationActive = true;                 // Flag pour activer ou désactiver l'animation
    private Color baseBackgroundColor = Color.LIGHT_GRAY;   // Couleur de base à afficher lorsque l'animation est désactivée
    private Timer timer;                                    // Timer pour contrôler la fréquence de l'animation


    /**
     * Constructeur du panneau. Initialise la texture de bruit, les points animés et le timer pour l'animation.
     * Complexité de l'initialisation : O(NOMBRE_POINTS) pour la création des points + O(textureWidth * textureHeight) pour la création de la texture (constant ici).
     */
    public PanelBruitGris() {
        // Créer la texture de bruit pour l'arrière-plan une seule fois.
        // Complexité : O(textureWidth * textureHeight), constant car les dimensions de la texture sont fixes.
        noiseTexture = createNoiseTexture(textureWidth, textureHeight);

        // Initialiser les points colorés qui se baladent.
        // Complexité : O(NOMBRE_POINTS), linéaire en fonction du nombre de points.
        for (int i = 0; i < NOMBRE_POINTS; i++) {
            Color couleur = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), 180);
            double x = random.nextDouble() * getWidth();
            double y = random.nextDouble() * getHeight();
            double vx = (random.nextDouble() * 2 - 1) * VITESSE_MAX_POINT;
            double vy = (random.nextDouble() * 2 - 1) * VITESSE_MAX_POINT;
            points.add(new PointAnime(x, y, vx, vy, couleur));
        }

        // Créer un timer pour gérer l'animation (mise à jour du bruit et des points).
        // L'action du timer est exécutée toutes les DELAI_TIMER millisecondes.
        timer = new Timer(DELAI_TIMER, e -> {
            // Vérifier si l'animation est active
            if (animationActive) {
                // Mettre à jour la phase pour animer le défilement de la texture de bruit.
                phaseX += VITESSE_X_BRUIT;
                phaseY += VITESSE_Y_BRUIT;

                // Mettre à jour la position de chaque point animé.
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                // Complexité : O(NOMBRE_POINTS), linéaire en fonction du nombre de points.
                for (PointAnime point : points) {
                    point.x += point.vx;
                    point.y += point.vy;

                    // Gestion des bords : si un point sort de l'écran, le faire réapparaître de l'autre côté.
                    if (point.x < 0) point.x += panelWidth;
                    if (point.x > panelWidth) point.x -= panelWidth;
                    if (point.y < 0) point.y += panelHeight;
                    if (point.y > panelHeight) point.y -= panelHeight;
                }
                repaint(); // Demander au panneau de se redessiner. Complexité analysée dans paintComponent.
            }
        });
        timer.start(); // Démarrer le timer pour lancer l'animation dès la création du panneau.
    }



    /**
     * Méthode pour activer ou désactiver l'animation.
     * Complexité : O(1).
     * @param active true pour activer l'animation, false pour la désactiver.
     */
    public void setAnimationActive(boolean active) {
        this.animationActive = active;
        // Si l'animation est désactivée, forcer un redessin pour afficher la couleur de base.
        if (!active) {
            repaint(); // Complexité analysée dans paintComponent.
        }
    }



    /**
     * Méthode pour définir la couleur de fond statique lorsque l'animation est désactivée.
     * Complexité : O(1).
     * @param color La couleur à afficher en arrière-plan lorsque l'animation est inactive.
     */
    public void setBaseBackgroundColor(Color color) {
        this.baseBackgroundColor = color;
        // Si l'animation est actuellement désactivée, forcer un redessin avec la nouvelle couleur de base.
        if (!animationActive) {
            repaint(); // Complexité analysée dans paintComponent.
        }
    }




    /**
     * Crée une image (BufferedImage) remplie de bruit gris aléatoire.
     * Complexité : O(width * height), où width et height sont textureWidth et textureHeight (constants).
     * @param width La largeur de la texture à créer.
     * @param height La hauteur de la texture à créer.
     * @return L'image contenant la texture de bruit.
     */
    private BufferedImage createNoiseTexture(int width, int height) {
        BufferedImage texture = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = texture.createGraphics();
        Random random = new Random();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float bruit = random.nextFloat() * 2 - 1;
                float brightness = LUMINOSITE_CENTRALE + bruit * AMPLITUDE_BRUIT;
                brightness = Math.max(0f, Math.min(1f, brightness));
                int gray = Math.round(brightness * 255);
                g2.setColor(new Color(gray, gray, gray));
                g2.fillRect(x, y, 1, 1);
            }
        }
        g2.dispose();
        return texture;
    }



    /**
     * Méthode appelée par le système de peinture de Swing pour dessiner le composant.
     * Complexité : O(getWidth() * getHeight()) si l'animation est active (pour le dessin en mosaïque du bruit),
     * O(NOMBRE_POINTS) si l'animation est active (pour le dessin des points),
     * O(1) si l'animation est désactivée (remplissage d'un rectangle).
     * @param g L'objet Graphics utilisé pour dessiner sur le composant.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Dessiner l'arrière-plan par défaut du JPanel.

        // Vérifier si l'animation est active
        if (animationActive) {
            // Si l'animation est active, dessiner le fond de bruit animé
            if (noiseTexture != null) {
                int width = getWidth();
                int height = getHeight();
                int textureW = noiseTexture.getWidth();
                int textureH = noiseTexture.getHeight();

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                int offsetX = (int) (phaseX % 1 * textureW);
                int offsetY = (int) (phaseY % 1 * textureH);
                // Complexité : O((width / textureW) * (height / textureH)), qui est O(width * height) car textureW et textureH sont constants.
                for (int x = -offsetX; x < width; x += textureW) {
                    for (int y = -offsetY; y < height; y += textureH) {
                        g2.drawImage(noiseTexture, x, y, this);
                    }
                }

                // Dessiner les points colorés animés par-dessus le fond de bruit
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Complexité : O(NOMBRE_POINTS), linéaire en fonction du nombre de points.
                for (PointAnime point : points) {
                    g2.setColor(point.couleur);
                    g2.fillOval(Math.round((float) point.x - TAILLE_POINT / 2f),
                            Math.round((float) point.y - TAILLE_POINT / 2f),
                            TAILLE_POINT, TAILLE_POINT);
                }
                g2.dispose();
            }
        } else {
            // Si l'animation est désactivée, remplir le panneau avec la couleur de base
            // Complexité : O(1).
            g.setColor(baseBackgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }



    /**
     * Classe interne pour représenter un point animé avec sa position, sa vitesse et sa couleur.
     */
    private static class PointAnime {
        double x, y;    // Position du point
        double vx, vy;  // Vitesse du point (composantes horizontale et verticale)
        Color couleur; // Couleur du point

        public PointAnime(double x, double y, double vx, double vy, Color couleur) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.couleur = couleur;
        }
    }

}