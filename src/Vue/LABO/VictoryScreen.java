//package Vue.testsUI;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
///**
// * Fenêtre principale affichant l'écran de victoire.
// */
//public class VictoryScreen extends JFrame {
//
//    public VictoryScreen() {
//        setTitle("Victoire !");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setResizable(false);
//
//        VictoryPanel victoryPanel = new VictoryPanel();
//        add(victoryPanel);
//
//        pack(); // Ajuste la taille de la fenêtre au contenu
//        setLocationRelativeTo(null); // Centre la fenêtre
//        setVisible(true);
//
//        // Démarre l'animation
//        victoryPanel.startAnimation();
//    }
//
//    public static void main(String[] args) {
//        // Assure que la création de l'interface graphique se fait sur l'Event Dispatch Thread
//        SwingUtilities.invokeLater(() -> new VictoryScreen());
//    }
//}
//
///**
// * Panneau personnalisé pour dessiner l'écran de victoire et l'animation.
// */
//class VictoryPanel extends JPanel implements ActionListener {
//
//    private static final int PANEL_WIDTH = 500;
//    private static final int PANEL_HEIGHT = 400;
//    private static final int CONFETTI_COUNT = 150;
//    private static final int ANIMATION_DELAY = 16; // Approx 60 FPS (1000ms / 60)
//
//    private Timer timer;
//    private List<ConfettiParticle> confetti;
//    private Random random;
//    private boolean animating = false;
//    private float textHue = 0.0f; // Pour l'animation de couleur du texte
//
//    public VictoryPanel() {
//        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
//        setBackground(Color.BLACK);
//        random = new Random();
//        confetti = new ArrayList<>();
//
//        // Initialiser les confettis
//        for (int i = 0; i < CONFETTI_COUNT; i++) {
//            confetti.add(createRandomConfetti(true)); // Start off-screen initially
//        }
//
//        // Initialiser le timer pour l'animation
//        timer = new Timer(ANIMATION_DELAY, this);
//        // Ne pas démarrer le timer ici, attendre l'appel à startAnimation()
//    }
//
//    /** Crée une particule de confetti aléatoire. */
//    private ConfettiParticle createRandomConfetti(boolean startOffScreen) {
//        int x = random.nextInt(PANEL_WIDTH);
//        // Start above the screen or randomly within if not startOffScreen=false
//        int y = startOffScreen ? -random.nextInt(PANEL_HEIGHT) : random.nextInt(PANEL_HEIGHT);
//        int size = random.nextInt(5) + 3; // Taille entre 3 et 7
//        float speedY = random.nextFloat() * 2.0f + 1.0f; // Vitesse entre 1.0 et 3.0
//        float speedX = random.nextFloat() * 1.0f - 0.5f; // Légère dérive horizontale
//        Color color = Color.getHSBColor(random.nextFloat(), 0.7f + random.nextFloat() * 0.3f, 0.9f + random.nextFloat() * 0.1f);
//        return new ConfettiParticle(x, y, size, speedX, speedY, color);
//    }
//
//    /** Démarre l'animation. */
//    public void startAnimation() {
//        if (!animating) {
//            timer.start();
//            animating = true;
//        }
//    }
//
//    /** Arrête l'animation. */
//    public void stopAnimation() {
//        if (animating) {
//            timer.stop();
//            animating = false;
//        }
//    }
//
//    /** Méthode appelée par le Timer à chaque "tick". */
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        updateAnimation();
//        repaint(); // Redemande le dessin du panneau
//    }
//
//    /** Met à jour la position des éléments animés. */
//    private void updateAnimation() {
//        for (int i = 0; i < confetti.size(); i++) {
//            ConfettiParticle p = confetti.get(i);
//            p.update();
//
//            // Si une particule sort par le bas, la réinitialise en haut
//            if (p.y > PANEL_HEIGHT) {
//                confetti.set(i, createRandomConfetti(true)); // Reset above screen
//            }
//            // Si une particule sort par les côtés (moins probable avec speedX faible)
//            else if (p.x > PANEL_WIDTH || p.x < -p.size) {
//                confetti.set(i, createRandomConfetti(true)); // Reset above screen
//            }
//        }
//
//        // Faire varier la couleur du texte "Victoire!"
//        textHue += 0.01f;
//        if (textHue > 1.0f) {
//            textHue = 0.0f;
//        }
//    }
//
//    /** Dessine les composants sur le panneau. */
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g); // Important: dessine le fond et nettoie
//
//        Graphics2D g2d = (Graphics2D) g;
//
//        // Activer l'anti-aliasing pour un rendu plus lisse
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        // Dessiner les confettis
//        for (ConfettiParticle p : confetti) {
//            p.draw(g2d);
//        }
//
//        // Dessiner le texte "Victoire!"
//        String victoryText = "VICTOIRE !";
//        g2d.setFont(new Font("SansSerif", Font.BOLD, 72));
//        FontMetrics fm = g2d.getFontMetrics();
//        int textWidth = fm.stringWidth(victoryText);
//        int textHeight = fm.getAscent() - fm.getDescent(); // Hauteur approx
//
//        int x = (PANEL_WIDTH - textWidth) / 2;
//        int y = (PANEL_HEIGHT + textHeight) / 2; // Centre verticalement
//
//        // Appliquer la couleur arc-en-ciel changeante
//        g2d.setColor(Color.getHSBColor(textHue, 1.0f, 1.0f));
//        g2d.drawString(victoryText, x, y);
//
//        // Ajouter une petite ombre ou contour pour la lisibilité
//        g2d.setColor(Color.DARK_GRAY);
//        g2d.drawString(victoryText, x + 2, y + 2); // Ombre légère
//
//    }
//
//    /** Classe interne pour représenter une particule de confetti. */
//    private static class ConfettiParticle {
//        float x, y;
//        int size;
//        float speedX, speedY;
//        Color color;
//
//        ConfettiParticle(float x, float y, int size, float speedX, float speedY, Color color) {
//            this.x = x;
//            this.y = y;
//            this.size = size;
//            this.speedX = speedX;
//            this.speedY = speedY;
//            this.color = color;
//        }
//
//        void update() {
//            y += speedY;
//            x += speedX;
//        }
//
//        void draw(Graphics2D g) {
//            g.setColor(color);
//            g.fillRect((int) x, (int) y, size, size);
//        }
//    }
//}