package Vue.LabO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Fenêtre principale affichant l'écran de victoire.
 */
public class VictoryScreen2 extends JFrame {

    public VictoryScreen2() {
        setTitle("VICTOIRE !");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        VictoryPanel victoryPanel = new VictoryPanel();
        add(victoryPanel);

        pack(); // Ajuste la taille de la fenêtre au contenu
        setLocationRelativeTo(null); // Centre la fenêtre
        setVisible(true);

        // Démarre l'animation
        victoryPanel.startAnimation();
    }

    public static void main(String[] args) {
        // Assure que la création de l'interface graphique se fait sur l'Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new VictoryScreen2());
    }
}

/**
 * Panneau personnalisé pour dessiner l'écran de victoire et l'animation.
 */
class VictoryPanel extends JPanel implements ActionListener {

    private static final int PANEL_WIDTH = 600; // Augmenter la largeur
    private static final int PANEL_HEIGHT = 500; // Augmenter la hauteur
    private static final int CONFETTI_COUNT = 300; // Plus de confettis
    private static final int ANIMATION_DELAY = 16;
    private static final float TEXT_SIZE = 80f; // Taille du texte augmentée

    private Timer timer;
    private List<ConfettiParticle> confetti;
    private Random random;
    private boolean animating = false;
    private float textHue = 0.0f;
    private float textScale = 1.0f; // Échelle initiale du texte
    private float textScaleSpeed = 0.005f; // Vitesse de changement d'échelle
    private boolean textScalingUp = true; // Indique si le texte grandit ou rétrécit

    public VictoryPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(new Color(255, 230, 153)); // Fond jaune clair festif (remplace Color.BLACK)
        random = new Random();
        confetti = new ArrayList<>();

        for (int i = 0; i < CONFETTI_COUNT; i++) {
            confetti.add(createRandomConfetti(true));
        }

        timer = new Timer(ANIMATION_DELAY, this);
    }

    private ConfettiParticle createRandomConfetti(boolean startOffScreen) {
        int x = random.nextInt(PANEL_WIDTH);
        int y = startOffScreen ? -random.nextInt(PANEL_HEIGHT) : random.nextInt(PANEL_HEIGHT);
        int size = random.nextInt(8) + 4; // Confettis légèrement plus grands
        float speedY = random.nextFloat() * 3.0f + 1.5f; // Vitesse accrue
        float speedX = random.nextFloat() * 1.5f - 0.75f;
        Color color = Color.getHSBColor(random.nextFloat(), 0.8f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.1f);
        return new ConfettiParticle(x, y, size, speedX, speedY, color);
    }

    public void startAnimation() {
        if (!animating) {
            timer.start();
            animating = true;
        }
    }

    public void stopAnimation() {
        if (animating) {
            timer.stop();
            animating = false;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        updateAnimation();
        repaint();
    }

    private void updateAnimation() {
        for (int i = 0; i < confetti.size(); i++) {
            ConfettiParticle p = confetti.get(i);
            p.update();

            if (p.y > PANEL_HEIGHT) {
                confetti.set(i, createRandomConfetti(true));
            } else if (p.x > PANEL_WIDTH || p.x < -p.size) {
                confetti.set(i, createRandomConfetti(true));
            }
        }

        textHue += 0.01f;
        if (textHue > 1.0f) {
            textHue = 0.0f;
        }

        // Animation de l'échelle du texte
        if (textScalingUp) {
            textScale += textScaleSpeed;
            if (textScale > 1.15f) { // Augmentation maximale
                textScalingUp = false;
            }
        } else {
            textScale -= textScaleSpeed;
            if (textScale < 1.0f) { // Retour à la taille originale
                textScalingUp = true;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (ConfettiParticle p : confetti) {
            p.draw(g2d);
        }

        String victoryText = "VICTOIRE !";
        Font font = new Font("SansSerif", Font.BOLD, (int) TEXT_SIZE); // Utiliser une taille flottante
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(victoryText);
        int textHeight = fm.getAscent() - fm.getDescent();

        int x = (PANEL_WIDTH - textWidth) / 2;
        int y = (PANEL_HEIGHT + textHeight) / 2;

        // Appliquer l'échelle au texte
        g2d.translate(x + textWidth / 2, y); // Déplacer l'origine au centre du texte
        g2d.scale(textScale, textScale); // Appliquer l'échelle
        g2d.translate(-(x + textWidth / 2), -y); // Retour à l'origine initiale

        g2d.setColor(Color.getHSBColor(textHue, 1.0f, 1.0f));
        g2d.drawString(victoryText, x, y);

        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(victoryText, x + 2, y + 2); // Ombre

        // Réinitialiser la transformation pour éviter des problèmes de dessin ultérieurs
        g2d.setTransform(new AffineTransform());

    }

    private static class ConfettiParticle {
        float x, y;
        int size;
        float speedX, speedY;
        Color color;

        ConfettiParticle(float x, float y, int size, float speedX, float speedY, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.speedX = speedX;
            this.speedY = speedY;
            this.color = color;
        }

        void update() {
            y += speedY;
            x += speedX;
        }

        void draw(Graphics2D g) {
            g.setColor(color);
            g.fillRect((int) x, (int) y, size, size);
        }
    }
}