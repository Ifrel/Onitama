package Vue.LabO;//package Vue.testsUI;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.awt.image.BufferedImage;
//
///**
// * Bouton personnalisé combinant animation de bordure, survol, pressé,
// * image intégrée, et configuration par défaut (forme, fond...).
// */
//public class BoutonAnimeImage extends JButton {
//
//    public enum ConfigurationParDefaut {
//        Cercle,
//        Cercle_transparent,
//        Carre,
//        Carre_transparent,
//        Rectangle,
//        Rectangle_transparent
//    }
//
//    private static final int TIMER_DELAY_MS = 50;
//    private static final float ANIMATION_PHASE_INCREMENT = 0.2f;
//    private static final float BORDER_THICKNESS_ANIMATION_AMPLITUDE = 2.0f;
//    private static final Color PRESSED_BACKGROUND_COLOR = new Color(130, 130, 130, 150);
//
//    private float epaisseurInitiale;
//    private float arrondiBordure;
//    private Color couleurInitiale;
//    private Color couleurFondSurvol;
//
//    private float epaisseurAnimee;
//    private Color couleurAnimee;
//    private boolean animationActivee = false;
//    private float phase = 0;
//    private final Timer timer;
//
//    /**
//     * Crée un bouton avec image, animation et configuration prédéfinie.
//     */
//    public BoutonAnimeImage(String cheminImage, ConfigurationParDefaut config) {
//        super(new ImageIcon(cheminImage));
//
//        // Configuration par défaut selon l’enum
//        switch (config) {
//            case Cercle : {
//                this.arrondiBordure = 1000f;
//                this.couleurFondSurvol = new Color(200, 200, 255);
//                this.couleurInitiale = Color.BLUE;
//                this.epaisseurInitiale = 3f;
//            }
//            case Cercle_transparent: {
//                this.arrondiBordure = 1000f;
//                this.couleurFondSurvol = new Color(0, 0, 0, 0);
//                this.couleurInitiale = Color.CYAN;
//                this.epaisseurInitiale = 2f;
//            }
//            case Carre : {
//                this.arrondiBordure = 20f;
//                this.couleurFondSurvol = new Color(220, 220, 220);
//                this.couleurInitiale = Color.GRAY;
//                this.epaisseurInitiale = 2f;
//            }
//            case Carre_transparent: {
//                this.arrondiBordure = 20f;
//                this.couleurFondSurvol = new Color(0, 0, 0, 0);
//                this.couleurInitiale = Color.DARK_GRAY;
//                this.epaisseurInitiale = 2f;
//            }
//            case Rectangle : {
//                this.arrondiBordure = 10f;
//                this.couleurFondSurvol = new Color(240, 240, 255);
//                this.couleurInitiale = Color.MAGENTA;
//                this.epaisseurInitiale = 3f;
//            }
//            case Rectangle_transparent : {
//                this.arrondiBordure = 10f;
//                this.couleurFondSurvol = new Color(0, 0, 0, 0);
//                this.couleurInitiale = Color.ORANGE;
//                this.epaisseurInitiale = 2f;
//            }
//        }
//
//        this.epaisseurAnimee = epaisseurInitiale;
//        this.couleurAnimee = couleurInitiale;
//
//        setOpaque(false);
//        setFocusPainted(false);
//        setBorderPainted(false);
//        setContentAreaFilled(false);
//        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//
//        this.timer = new Timer(TIMER_DELAY_MS, e -> {
//            phase += ANIMATION_PHASE_INCREMENT;
//            epaisseurAnimee = epaisseurInitiale + (float) (Math.sin(phase) * BORDER_THICKNESS_ANIMATION_AMPLITUDE);
//            couleurAnimee = new Color(
//                    (int) (Math.abs(Math.sin(phase)) * 255),
//                    couleurInitiale.getGreen(),
//                    couleurInitiale.getBlue(),
//                    couleurInitiale.getAlpha()
//            );
//            repaint();
//        });
//
//        addActionListener(e -> toggleAnimationBordure());
//    }
//
//    public void setAnimationBordureActivee(boolean activer) {
//        this.animationActivee = activer;
//        if (activer) {
//            if (!timer.isRunning()) {
//                phase = 0;
//                timer.start();
//            }
//        } else {
//            if (timer.isRunning()) {
//                timer.stop();
//            }
//            epaisseurAnimee = epaisseurInitiale;
//            couleurAnimee = couleurInitiale;
//            repaint();
//        }
//    }
//
//    public void toggleAnimationBordure() {
//        setAnimationBordureActivee(!this.animationActivee);
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        Graphics2D g2 = (Graphics2D) g.create();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        int width = getWidth();
//        int height = getHeight();
//        int arc = (int) arrondiBordure;
//
//        if (getModel().isRollover() || isFocusOwner()) {
//            g2.setColor(couleurFondSurvol);
//            g2.fillRoundRect(0, 0, width, height, arc, arc);
//        }
//
//        if (getModel().isPressed()) {
//            g2.setColor(PRESSED_BACKGROUND_COLOR);
//            g2.fillRoundRect(1, 1, width - 2, height - 2, arc, arc);
//        }
//
//        super.paintComponent(g2);
//
//        g2.setStroke(new BasicStroke(epaisseurAnimee));
//        g2.setColor(animationActivee ? couleurAnimee : couleurInitiale);
//        int offset = (int) (epaisseurAnimee / 2);
//        g2.drawRoundRect(
//                offset,
//                offset,
//                width - (int) epaisseurAnimee,
//                height - (int) epaisseurAnimee,
//                arc, arc
//        );
//
//        g2.dispose();
//    }
//
//    // Accesseurs utiles
//    public boolean isAnimationActivee() {
//        return animationActivee;
//    }
//
//    public void setCouleurFondSurvol(Color c) {
//        this.couleurFondSurvol = c;
//        repaint();
//    }
//
//    public void setCouleurInitiale(Color c) {
//        this.couleurInitiale = c;
//        this.couleurAnimee = c;
//        repaint();
//    }
//
//    public void setEpaisseurInitiale(float e) {
//        this.epaisseurInitiale = e;
//        repaint();
//    }
//
//    public void setArrondiBordure(float a) {
//        this.arrondiBordure = a;
//        repaint();
//    }
//}


//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.image.BufferedImage;
//
///**
// * {@code AnimationBoutonImage} est un bouton personnalisable avec une image,
// * une bordure arrondie animée et une gestion de survol.
// */
//public class AnimationBoutonImage extends JButton {
//
//    // Constantes d'animation
//    private static final int TIMER_DELAY_MS = 50;
//    private static final float ANIMATION_PHASE_INCREMENT = 0.2f;
//    private static final float BORDER_THICKNESS_ANIMATION_AMPLITUDE = 2.0f;
//    private static final Color PRESSED_BACKGROUND_COLOR = new Color(130, 130, 130, 150);
//
//    // Propriétés d'apparence
//    private final float epaisseurInitiale;
//    private final float arrondiBordure;
//    private final Color couleurInitiale;
//    private Color couleurFondSurvol;
//    private final ImageIcon icone;
//
//    // Propriétés d'animation
//    private float epaisseurAnimee;
//    private Color couleurAnimee;
//    private boolean animationActivee = false;
//    private float phase = 0;
//    private final Timer timer;
//
//    /**
//     * Construit un bouton image avec animation de bordure.
//     */
//    public AnimationBoutonImage(ImageIcon icone, float epaisseurBordure, float arrondi, Color couleurBordure, Color fondSurvol) {
//        super(icone);
//        this.icone = icone;
//        this.epaisseurInitiale = epaisseurBordure;
//        this.epaisseurAnimee = epaisseurBordure;
//        this.arrondiBordure = arrondi;
//        this.couleurInitiale = couleurBordure;
//        this.couleurAnimee = couleurBordure;
//        this.couleurFondSurvol = fondSurvol;
//
//        setOpaque(false);
//        setFocusPainted(false);
//        setBorderPainted(false);
//        setContentAreaFilled(false);
//        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//
//        this.timer = new Timer(TIMER_DELAY_MS, e -> {
//            phase += ANIMATION_PHASE_INCREMENT;
//            epaisseurAnimee = epaisseurInitiale + (float) (Math.sin(phase) * BORDER_THICKNESS_ANIMATION_AMPLITUDE);
//            couleurAnimee = new Color(
//                    (int) (Math.abs(Math.sin(phase)) * 255),
//                    couleurInitiale.getGreen(),
//                    couleurInitiale.getBlue(),
//                    couleurInitiale.getAlpha()
//            );
//            repaint();
//        });
//
//        addActionListener((ActionEvent e) -> toggleAnimationBordure());
//    }
//
//    public void setAnimationBordureActivee(boolean activer) {
//        this.animationActivee = activer;
//        if (activer) {
//            if (!timer.isRunning()) {
//                phase = 0;
//                timer.start();
//            }
//        } else {
//            if (timer.isRunning()) {
//                timer.stop();
//            }
//            epaisseurAnimee = epaisseurInitiale;
//            couleurAnimee = couleurInitiale;
//            repaint();
//        }
//    }
//
//    public void toggleAnimationBordure() {
//        setAnimationBordureActivee(!animationActivee);
//    }
//
//    public boolean isAnimationBordureActivee() {
//        return animationActivee;
//    }
//
//    public void setCouleurFondSurvol(Color couleurFondSurvol) {
//        this.couleurFondSurvol = couleurFondSurvol;
//        repaint();
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        Graphics2D g2 = (Graphics2D) g.create();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        int width = getWidth();
//        int height = getHeight();
//        int arc = (int) arrondiBordure;
//
//        if (getModel().isRollover() || isFocusOwner()) {
//            g2.setColor(couleurFondSurvol);
//            g2.fillRoundRect(0, 0, width, height, arc, arc);
//        }
//
//        if (getModel().isPressed()) {
//            g2.setColor(PRESSED_BACKGROUND_COLOR);
//            g2.fillRoundRect(1, 1, width - 2, height - 2, arc, arc);
//        }
//
//        super.paintComponent(g2);
//
//        g2.setStroke(new BasicStroke(epaisseurAnimee));
//        g2.setColor(animationActivee ? couleurAnimee : couleurInitiale);
//
//        int offset = (int) (epaisseurAnimee / 2);
//        g2.drawRoundRect(
//                offset,
//                offset,
//                width - (int) epaisseurAnimee,
//                height - (int) epaisseurAnimee,
//                arc, arc
//        );
//
//        g2.dispose();
//    }
//}


import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Supposons que cette classe Global.Paths existe pour votre test main.
// Pour la classe AnimationBoutonImage elle-même, il vaut mieux ne pas avoir de dépendances statiques spécifiques.
// import static Global.Paths.PATH_PION_BLEU_ETUDIANT_CLIQUE; // Commenté car spécifique au projet utilisateur

/**
 * {@code AnimationBoutonImage} est un {@link JButton} personnalisé qui affiche une image
 * et propose plusieurs effets d'animation :
 * 1. Une animation de la bordure au survol de la souris (épaisseur changeante).
 * 2. Une animation principale de "pulsation" de la bordure, activable par un appel à {@link #toggleAnimation()}.
 * Le bouton gère également un effet visuel lorsqu'il est pressé.
 */
public class AnimationBoutonImage extends JButton {

    // --- Constantes ---
    private static final int DEFAULT_ARC_DIAMETER = 30;
    private static final int DEFAULT_IMAGE_PADDING = 10; // Espace autour de l'image

    private static final float DEFAULT_BORDER_THICKNESS = 2f;
    private static final float DEFAULT_MAX_HOVER_BORDER_THICKNESS = 5f;
    private static final int HOVER_ANIMATION_TIMER_DELAY_MS = 15;
    private static final float HOVER_BORDER_THICKNESS_STEP = 0.3f;

    private static final int MAIN_ANIMATION_TIMER_DELAY_MS = 40;
    private static final float MAIN_ANIMATION_PROGRESS_STEP = 0.05f;
    private static final float MAIN_ANIMATION_BORDER_THICKNESS = 4f; // Épaisseur fixe pour l'animation principale
    private static final int MAIN_ANIMATION_BORDER_LAYERS = 4; // Nombre de couches pour l'effet de pulsation

    // --- Propriétés de l'animation et de l'état ---
    private boolean mainAnimationActive = false;
    private float mainAnimationProgress = 0f;
    private float currentHoverBorderWidth;

    private final Timer mainAnimationTimer;
    private final Timer hoverAnimationTimer;
    private boolean isHoverBorderIncreasing = false; // Direction de l'animation de survol

    private BufferedImage buttonImage;
    private boolean isHovered = false;
    private boolean isPressed = false;

    // --- Propriétés configurables ---
    private float minBorderWidth;
    private float maxHoverBorderWidth;
    private int imagePadding;
    private int arcDiameter;

    private Color defaultBackgroundColor = new Color(240, 240, 240);
    private Color pressedBackgroundColor = new Color(200, 200, 200, 200); // Ajout d'alpha pour effet de translucidité
    private Color hoverBorderColor = new Color(100, 150, 255);
    private Color mainAnimationBorderColorBase = new Color(0, 127, 255); // 0f, 0.5f, 1f

    /**
     * Construit un nouveau AnimationBoutonImage avec une icône.
     *
     * @param icon L'icône à afficher sur le bouton.
     */
    public AnimationBoutonImage(ImageIcon icon) {
        if (icon == null) {
            throw new IllegalArgumentException("L'icône ne peut pas être nulle.");
        }
        // Copie de l'icône dans un BufferedImage pour un meilleur contrôle
        this.buttonImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = this.buttonImage.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();

        this.minBorderWidth = DEFAULT_BORDER_THICKNESS;
        this.maxHoverBorderWidth = DEFAULT_MAX_HOVER_BORDER_THICKNESS;
        this.currentHoverBorderWidth = this.minBorderWidth;
        this.imagePadding = DEFAULT_IMAGE_PADDING;
        this.arcDiameter = DEFAULT_ARC_DIAMETER;

        // Configuration de l'apparence de base
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false); // Nous dessinons notre propre bordure
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setPreferredSize(new Dimension(
                icon.getIconWidth() + (2 * imagePadding) + (2 * (int)this.maxHoverBorderWidth), // Espace pour image, padding et bordure max
                icon.getIconHeight() + (2 * imagePadding) + (2 * (int)this.maxHoverBorderWidth)
        ));

        initMouseListeners();
        this.hoverAnimationTimer = initHoverAnimationTimer();
        this.mainAnimationTimer = initMainAnimationTimer();
    }

    private void initMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                if (!mainAnimationActive) {
                    isHoverBorderIncreasing = true;
                    hoverAnimationTimer.start();
                }
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                if (!mainAnimationActive) {
                    isHoverBorderIncreasing = false;
                    hoverAnimationTimer.start();
                }
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    private Timer initHoverAnimationTimer() {
        return new Timer(HOVER_ANIMATION_TIMER_DELAY_MS, e -> {
            if (isHoverBorderIncreasing) {
                if (currentHoverBorderWidth < maxHoverBorderWidth) {
                    currentHoverBorderWidth += HOVER_BORDER_THICKNESS_STEP;
                    if (currentHoverBorderWidth > maxHoverBorderWidth) {
                        currentHoverBorderWidth = maxHoverBorderWidth;
                    }
                } else {
                    currentHoverBorderWidth = maxHoverBorderWidth;
                    ((Timer) e.getSource()).stop();
                }
            } else { // decreasing
                if (currentHoverBorderWidth > minBorderWidth) {
                    currentHoverBorderWidth -= HOVER_BORDER_THICKNESS_STEP;
                    if (currentHoverBorderWidth < minBorderWidth) {
                        currentHoverBorderWidth = minBorderWidth;
                    }
                } else {
                    currentHoverBorderWidth = minBorderWidth;
                    ((Timer) e.getSource()).stop();
                }
            }
            repaint();
        });
    }

    private Timer initMainAnimationTimer() {
        return new Timer(MAIN_ANIMATION_TIMER_DELAY_MS, e -> {
            mainAnimationProgress += MAIN_ANIMATION_PROGRESS_STEP;
            if (mainAnimationProgress > 1f) {
                mainAnimationProgress = 0f; // Boucle l'animation
            }
            repaint();
        });
    }

    /**
     * Active ou désactive l'animation principale de la bordure.
     */
    public void toggleAnimation() {
        mainAnimationActive = !mainAnimationActive;
        if (mainAnimationActive) {
            mainAnimationTimer.start();
            hoverAnimationTimer.stop(); // Arrête l'animation de survol si elle est en cours
            // Optionnel: définir currentHoverBorderWidth à une valeur neutre ou la laisser
        } else {
            mainAnimationTimer.stop();
            mainAnimationProgress = 0f; // Réinitialiser la progression
            // Si la souris est toujours dessus, relancer l'animation de survol appropriée
            if (isHovered) {
                isHoverBorderIncreasing = true;
                hoverAnimationTimer.start();
            } else {
                currentHoverBorderWidth = minBorderWidth; // Ou animer vers minBorderWidth
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Important pour certains LookAndFeels, bien que nous redessinions beaucoup
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // --- Dessin du fond ---
        if (isPressed && !mainAnimationActive) {
            g2.setColor(pressedBackgroundColor);
        } else {
            g2.setColor(getBackground() != null ? getBackground() : defaultBackgroundColor);
        }
        g2.fillRoundRect(0, 0, width, height, arcDiameter, arcDiameter);

        // --- Dessin de l'image centrée ---
        if (buttonImage != null) {
            int imgX = (width - buttonImage.getWidth()) / 2;
            int imgY = (height - buttonImage.getHeight()) / 2;
            g2.drawImage(buttonImage, imgX, imgY, this);
        }

        // --- Dessin de la bordure ---
        float actualBorderThickness;
        Shape borderShape;

        if (mainAnimationActive) {
            actualBorderThickness = MAIN_ANIMATION_BORDER_THICKNESS;
            // Calcul des coordonnées pour que la bordure soit bien visible
            float offset = actualBorderThickness / 2.0f;
            borderShape = new RoundRectangle2D.Float(offset, offset,
                    width - actualBorderThickness, height - actualBorderThickness,
                    arcDiameter - actualBorderThickness, arcDiameter - actualBorderThickness);

            float phase = mainAnimationProgress;
            for (int i = 0; i < MAIN_ANIMATION_BORDER_LAYERS; i++) {
                // L'alpha oscille pour chaque couche avec un décalage de phase
                float alpha = (float) Math.abs(Math.sin(Math.PI * (phase + i * (1.0 / MAIN_ANIMATION_BORDER_LAYERS))));
                g2.setColor(new Color(
                        mainAnimationBorderColorBase.getRed() / 255f,
                        mainAnimationBorderColorBase.getGreen() / 255f,
                        mainAnimationBorderColorBase.getBlue() / 255f,
                        alpha)
                );
                g2.setStroke(new BasicStroke(actualBorderThickness));
                g2.draw(borderShape);
            }
        } else { // Animation de survol ou état par défaut
            actualBorderThickness = currentHoverBorderWidth;
            float offset = actualBorderThickness / 2.0f;
            borderShape = new RoundRectangle2D.Float(offset, offset,
                    width - actualBorderThickness, height - actualBorderThickness,
                    arcDiameter - actualBorderThickness, arcDiameter - actualBorderThickness);
            g2.setColor(hoverBorderColor);
            g2.setStroke(new BasicStroke(actualBorderThickness));
            g2.draw(borderShape);
        }
        g2.dispose();
    }

    // --- Getters et Setters pour la configuration ---

    public boolean isMainAnimationActive() {
        return mainAnimationActive;
    }

    public float getMinBorderWidth() {
        return minBorderWidth;
    }

    public void setMinBorderWidth(float minBorderWidth) {
        this.minBorderWidth = minBorderWidth;
        if (this.currentHoverBorderWidth < minBorderWidth) this.currentHoverBorderWidth = minBorderWidth;
        repaint();
    }

    public float getMaxHoverBorderWidth() {
        return maxHoverBorderWidth;
    }

    public void setMaxHoverBorderWidth(float maxHoverBorderWidth) {
        this.maxHoverBorderWidth = maxHoverBorderWidth;
        if (this.currentHoverBorderWidth > maxHoverBorderWidth) this.currentHoverBorderWidth = maxHoverBorderWidth;
        // Mettre à jour la taille préférée si elle dépend de cela
        setPreferredSizeBasedOnContent();
        revalidate(); // Pour que le parent réévalue la disposition
        repaint();
    }

    public int getImagePadding() {
        return imagePadding;
    }

    public void setImagePadding(int imagePadding) {
        this.imagePadding = imagePadding;
        setPreferredSizeBasedOnContent();
        revalidate();
        repaint();
    }

    public int getArcDiameter() {
        return arcDiameter;
    }

    public void setArcDiameter(int arcDiameter) {
        this.arcDiameter = arcDiameter;
        repaint();
    }

    public Color getDefaultBackgroundColor() {
        return defaultBackgroundColor;
    }

    public void setDefaultBackgroundColor(Color defaultBackgroundColor) {
        this.defaultBackgroundColor = defaultBackgroundColor;
        repaint();
    }

    public Color getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public void setPressedBackgroundColor(Color pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
        repaint();
    }

    public Color getHoverBorderColor() {
        return hoverBorderColor;
    }

    public void setHoverBorderColor(Color hoverBorderColor) {
        this.hoverBorderColor = hoverBorderColor;
        repaint();
    }

    public Color getMainAnimationBorderColorBase() {
        return mainAnimationBorderColorBase;
    }

    public void setMainAnimationBorderColorBase(Color mainAnimationBorderColorBase) {
        this.mainAnimationBorderColorBase = mainAnimationBorderColorBase;
        repaint();
    }

    /**
     * Recalcule et définit la taille préférée en fonction de l'image, du padding et de la bordure maximale.
     */
    private void setPreferredSizeBasedOnContent() {
        if (buttonImage != null) {
            setPreferredSize(new Dimension(
                    buttonImage.getWidth() + (2 * imagePadding) + (2 * (int)this.maxHoverBorderWidth),
                    buttonImage.getHeight() + (2 * imagePadding) + (2 * (int)this.maxHoverBorderWidth)
            ));
        }
    }

    /**
     * Définit une nouvelle image pour le bouton.
     * @param icon La nouvelle icône à afficher.
     */
    public void setImage(ImageIcon icon) {
        if (icon == null) {
            this.buttonImage = null;
        } else {
            this.buttonImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = this.buttonImage.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
        }
        setPreferredSizeBasedOnContent();
        revalidate();
        repaint();
    }
}

/**
 * Classe de test pour AnimationBoutonImage.
 */
class TestAnimationBoutonImage {
    // Remplacez ceci par un chemin d'accès réel à une image pour le test
    private static final String DEFAULT_ICON_PATH = "path/to/your/icon.png"; // MODIFIEZ CECI

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test AnimationBoutonImage");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
            frame.getContentPane().setBackground(Color.LIGHT_GRAY);

            ImageIcon testIcon = null;
            try {
                // Essayez de charger l'icône spécifiée.
                // Pour que cela fonctionne, DEFAULT_ICON_PATH doit être valide.
                // testIcon = new ImageIcon(DEFAULT_ICON_PATH);

                // Si l'icône par défaut n'est pas trouvée ou pour un test rapide, créez une icône générique :
                if (testIcon == null || testIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                    System.err.println("Chemin d'icône par défaut non trouvé ou invalide ("+DEFAULT_ICON_PATH+"). Utilisation d'une icône générique.");
                    BufferedImage genericImg = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = genericImg.createGraphics();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(0,100,200));
                    g2d.fillOval(4,4,40,40);
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 24));
                    g2d.drawString("i", 18, 32);
                    g2d.dispose();
                    testIcon = new ImageIcon(genericImg);
                }

            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'icône: " + e.getMessage());
                // Création d'une icône de remplacement si le chargement échoue
                BufferedImage errorImg = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
                Graphics g = errorImg.getGraphics();
                g.setColor(Color.RED);
                g.fillRect(0,0,32,32);
                g.dispose();
                testIcon = new ImageIcon(errorImg);
            }


            AnimationBoutonImage bouton1 = new AnimationBoutonImage(testIcon);
            bouton1.setToolTipText("Cliquez pour activer/désactiver l'animation principale");
            bouton1.addActionListener(e -> bouton1.toggleAnimation()); // Action pour basculer l'animation

            AnimationBoutonImage bouton2 = new AnimationBoutonImage(testIcon);
            bouton2.setToolTipText("Bouton avec animation principale activée par défaut");
            bouton2.setMainAnimationBorderColorBase(new Color(255,100,0)); // Orange
            bouton2.setHoverBorderColor(new Color(255,150,50));
            bouton2.addActionListener(e -> bouton2.toggleAnimation());
            bouton2.toggleAnimation(); // Activer l'animation principale pour celui-ci

            frame.add(bouton1);
            frame.add(bouton2);

            frame.pack(); // Ajuste la taille du frame au contenu
            // frame.setSize(400, 200); // Ou définir une taille fixe
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}