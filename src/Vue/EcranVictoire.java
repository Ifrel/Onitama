package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EcranVictoire extends JPanel {
    private final InterfaceGraphique interfaceGraphique;
    private final String gagnant;
    private final Timer animationTimer;
    private final List<ConfettiParticle> confettis;
    private static final float TEXT_SCALE_MAX = 1.1f;

    private float textHue = 0.0f;
    private float textScale = 1.0f;
    private boolean textScalingUp = true;
    private static final float TEXT_SCALE_MIN = 0.95f;

    private static final float TEXT_SCALE_SPEED = 0.005f;
    private static final int CONFETTI_COUNT = 250; // Slightly reduced for potentially more complex shapes
    private final Random random = new Random(); // Member for easy access
    private boolean confettisSpawned = false;

    public EcranVictoire(String gagnant, InterfaceGraphique interfaceGraphique) {
        this.gagnant = gagnant;
        this.interfaceGraphique = interfaceGraphique;
        this.confettis = new ArrayList<>();

        setLayout(new BorderLayout());
        // setBackground will be overridden by gradient in paintComponent
        // setOpaque(false); // Set true if this panel is fully opaque with gradient

        // Timer pour l'animation
        animationTimer = new Timer(16, e -> { // Approx 60 FPS
            updateAnimation();
            repaint();
        });
        animationTimer.start();

        // Panneau pour les boutons
        JPanel panneauBoutons = new JPanel(new GridLayout(1, 2, 25, 0)); // Increased gap
        panneauBoutons.setOpaque(false);
        ajouterBoutons(panneauBoutons);

        // Wrapper pour centrer le panneau des boutons sans l'étirer
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(panneauBoutons);

        // Panneau inférieur pour contenir le wrapper des boutons avec marges
        JPanel panneauInferieur = new JPanel(new BorderLayout());
        panneauInferieur.setOpaque(false);
        panneauInferieur.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
        panneauInferieur.add(buttonWrapper, BorderLayout.CENTER);

        add(panneauInferieur, BorderLayout.SOUTH);
    }

    private void ajouterBoutons(JPanel panneauBoutons) {
        JButton boutonMenu = creerBoutonStyle("Quitter");
        boutonMenu.addActionListener(e -> {
            animationTimer.stop();
            interfaceGraphique.getControler().clavier("exit");
        });

        JButton boutonRejouer = creerBoutonStyle("Nouvelle partie");
        boutonRejouer.addActionListener(e -> {
            animationTimer.stop();
            interfaceGraphique.demarrerNouvellePartie();
        });

        panneauBoutons.add(boutonMenu);
        panneauBoutons.add(boutonRejouer);
    }

    private JButton creerBoutonStyle(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Verdana", Font.BOLD, 18));
        bouton.setForeground(Color.WHITE);
        bouton.setBackground(new Color(0, 120, 0));
        bouton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200, 150), 2),
                BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        bouton.setFocusPainted(false);
        bouton.setOpaque(true); // Important for background color to show
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bouton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bouton.setBackground(new Color(0, 150, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bouton.setBackground(new Color(0, 120, 0));
            }
        });
        return bouton;
    }

    private ConfettiParticle createOneConfetti(int screenWidth, int screenHeight) {
        int baseSize = random.nextInt(7) + 8; // Size: 8 to 14
        float initialX = random.nextFloat() * screenWidth;
        // Start above screen, slightly spread out vertically to avoid all appearing at once on same line
        float initialY = -random.nextFloat() * screenHeight * 0.2f - baseSize * 2;

        return new ConfettiParticle(
                initialX, initialY, baseSize,
                random.nextFloat() * 4f - 2f,    // speedX: -2.0 to 2.0
                2.5f + random.nextFloat() * 3.5f, // speedY: 2.5 to 6.0
                Color.getHSBColor(random.nextFloat(), 0.9f, 1.0f), // Vibrant colors
                random
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create(); // Use a copy of the graphics context
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gradient Background
        Color color1 = new Color(20, 90, 20, 230);
        Color color2 = new Color(40, 150, 40, 180);
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Initialize confettis on first valid paintComponent call
        if (getWidth() > 0 && getHeight() > 0) {
            if (!confettisSpawned) {
                for (int i = 0; i < CONFETTI_COUNT; i++) {
                    confettis.add(createOneConfetti(getWidth(), getHeight()));
                }
                confettisSpawned = true;
            }
        }

        // Draw confettis
        if (confettisSpawned) {
            for (ConfettiParticle confetti : confettis) {
                confetti.dessiner(g2d);
            }
        }

        // Draw victory message
        dessinerMessageVictoire(g2d);
        g2d.dispose(); // Dispose of the graphics copy
    }

    private void dessinerMessageVictoire(Graphics2D g2d) {
        String message = "Félicitations " + gagnant + " !";
        Font fontBase = new Font("Verdana", Font.BOLD, 50); // Adjusted size

        FontMetrics fm = g2d.getFontMetrics(fontBase);
        int textActualWidth = fm.stringWidth(message);

        // Center of the panel
        float panelCenterX = getWidth() / 2.0f;
        // Baseline Y for the text (one third down the panel)
        float textBaselineY = getHeight() / 3.0f;

        // Visual center of the text for scaling
        float textVisualCenterX = panelCenterX;
        float textVisualCenterY = textBaselineY - (fm.getAscent() / 2.0f) + (fm.getDescent() / 2.0f);

        AffineTransform originalTransform = g2d.getTransform();

        // Apply scaling transformation around the visual center of the text
        g2d.translate(textVisualCenterX, textVisualCenterY);
        g2d.scale(textScale, textScale);
        g2d.translate(-textVisualCenterX, -textVisualCenterY);

        // Calculate drawing position (text is drawn from its baseline, horizontally centered)
        float drawX = panelCenterX - (textActualWidth / 2.0f);
        float drawY = textBaselineY;

        g2d.setFont(fontBase);

        // Shadow
        int shadowOffset = (int) Math.max(1, 3 * textScale); // Scale shadow offset, min 1px
        g2d.setColor(new Color(0, 0, 0, 100)); // Semi-transparent black for shadow
        g2d.drawString(message, drawX + shadowOffset, drawY + shadowOffset);

        // Actual Text (Rainbow effect)
        g2d.setColor(Color.getHSBColor(textHue, 0.95f, 1.0f)); // High saturation and brightness
        g2d.drawString(message, drawX, drawY);

        g2d.setTransform(originalTransform); // Restore original transform
    }

    private void updateAnimation() {
        // Attempt to spawn confettis if not done yet and dimensions are available
        if (!confettisSpawned && getWidth() > 0 && getHeight() > 0) {
            for (int i = 0; i < CONFETTI_COUNT; i++) {
                confettis.add(createOneConfetti(getWidth(), getHeight()));
            }
            confettisSpawned = true;
        }
        if (!confettisSpawned) return; // Do nothing if no dimensions yet

        // Update confettis
        for (ConfettiParticle confetti : confettis) {
            confetti.update();
            if (confetti.isOutOfView(getWidth(), getHeight())) {
                confetti.reset(getWidth(), getHeight(), random);
            }
        }

        // Update text animation (color hue and scale)
        textHue = (textHue + 0.008f) % 1.0f; // Slightly slower color shift
        if (textScalingUp) {
            textScale += TEXT_SCALE_SPEED;
            if (textScale >= TEXT_SCALE_MAX) {
                textScale = TEXT_SCALE_MAX;
                textScalingUp = false;
            }
        } else {
            textScale -= TEXT_SCALE_SPEED;
            if (textScale <= TEXT_SCALE_MIN) {
                textScale = TEXT_SCALE_MIN;
                textScalingUp = true;
            }
        }
    }

    private static class ConfettiParticle {
        float x, y, speedX, speedY, angle, rotationSpeed;
        Color color;
        Shape shape; // General shape
        float width, height; // Dimensions of the shape's bounding box

        ConfettiParticle(float x, float y, int baseSize, float speedX, float speedY, Color color, Random random) {
            this.x = x;
            this.y = y;
            this.speedX = speedX;
            this.speedY = Math.max(1.0f, speedY); // Ensure speedY is positive and not too slow
            this.color = color;
            this.angle = random.nextFloat() * 360f;
            this.rotationSpeed = random.nextFloat() * 6f - 3f; // Degrees per frame: -3 to +3

            // Randomly choose a shape type and define dimensions
            int shapeType = random.nextInt(3); // 0: rectangle, 1: ellipse, 2: thin ribbon

            if (shapeType == 0) { // Rectangle
                this.width = baseSize * (0.6f + random.nextFloat() * 0.8f); // Width: 0.6 to 1.4 * baseSize
                this.height = baseSize * (0.4f + random.nextFloat() * 0.6f); // Height: 0.4 to 1.0 * baseSize
                this.shape = new Rectangle2D.Float(0, 0, this.width, this.height);
            } else if (shapeType == 1) { // Ellipse
                this.width = this.height = baseSize * (0.8f + random.nextFloat() * 0.4f); // Diameter: 0.8 to 1.2 * baseSize
                this.shape = new Ellipse2D.Float(0, 0, this.width, this.height);
            } else { // Thin Ribbon (long rectangle)
                this.width = baseSize * (1.2f + random.nextFloat()); // Width: 1.2 to 2.2 * baseSize (longer)
                this.height = baseSize * (0.15f + random.nextFloat() * 0.25f); // Height: 0.15 to 0.4 * baseSize (thinner)
                this.shape = new Rectangle2D.Float(0, 0, this.width, this.height);
            }
        }

        void update() {
            this.x += this.speedX;
            this.y += this.speedY;
            this.angle = (this.angle + this.rotationSpeed) % 360f;

            // Optional: Add a gentle horizontal flutter for ribbon-like confetti
            if (this.shape instanceof Rectangle2D && this.width > this.height * 2) { // If it's a ribbon
                this.x += (float) Math.sin(Math.toRadians(this.angle * 1.5 + this.y * 0.1)) * 0.5f;
            }
        }

        void reset(int screenWidth, int screenHeight, Random random) {
            this.x = random.nextFloat() * screenWidth;
            // Reset to a position above the screen, with some vertical spread
            this.y = -random.nextFloat() * screenHeight * 0.1f - this.height;
            this.speedY = Math.max(1.0f, 2.5f + random.nextFloat() * 3.5f);
            this.speedX = random.nextFloat() * 4f - 2f;
            this.angle = random.nextFloat() * 360f;
            this.rotationSpeed = random.nextFloat() * 6f - 3f;
            // Optionally re-randomize color or shape type here if desired
            // this.color = Color.getHSBColor(random.nextFloat(), 0.9f, 1.0f);
        }

        boolean isOutOfView(int screenWidth, int screenHeight) {
            // Check if particle is well below the screen or too far to the sides
            float margin = Math.max(this.width, this.height) * 2; // Extra margin
            return this.y > screenHeight + margin ||
                    this.x < -margin ||
                    this.x > screenWidth + margin;
        }

        void dessiner(Graphics2D g) {
            AffineTransform oldTransform = g.getTransform();

            // Translate to particle's position, then rotate around the shape's center
            g.translate(this.x, this.y); // Move origin to particle's top-left for drawing its shape
            g.rotate(Math.toRadians(this.angle), this.width / 2.0, this.height / 2.0); // Rotate around center of shape

            g.setColor(this.color);
            g.fill(this.shape); // Draw the pre-defined shape (already at its local 0,0)

            g.setTransform(oldTransform);
        }
    }
}