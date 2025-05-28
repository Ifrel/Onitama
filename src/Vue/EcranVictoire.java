package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EcranVictoire extends JPanel {
    private final InterfaceGraphique interfaceGraphique;
    private final String gagnant;
    private final Timer animationTimer;
    private final List<ConfettiParticle> confettis;
    private float textHue = 0.0f;
    private float textScale = 1.0f;
    private boolean textScalingUp = true;
    private static final float TEXT_SCALE_SPEED = 0.005f;
    private static final int CONFETTI_COUNT = 300;

    public EcranVictoire(String gagnant, InterfaceGraphique interfaceGraphique) {
        this.gagnant = gagnant;
        this.interfaceGraphique = interfaceGraphique;
        this.confettis = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(new Color(34, 139, 34, 200)); // Vert foncé semi-transparent

        // Initialisation des confettis
        Random random = new Random();
        for (int i = 0; i < CONFETTI_COUNT; i++) {
            confettis.add(createConfetti(random));
        }

        // Timer pour l'animation
        animationTimer = new Timer(16, e -> {
            updateAnimation();
            repaint();
        });
        animationTimer.start();

        // Panneau pour les boutons avec espacement
        JPanel panneauBoutons = new JPanel(new GridLayout(1, 2, 20, 0));
        panneauBoutons.setOpaque(false);

        // Création des boutons avec style
        ajouterBoutons(panneauBoutons);

        // Ajout du panneau de boutons avec marge
        JPanel panneauInferieur = new JPanel(new BorderLayout());
        panneauInferieur.setOpaque(false);
        panneauInferieur.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
        panneauInferieur.add(panneauBoutons, BorderLayout.CENTER);

        add(panneauInferieur, BorderLayout.SOUTH);
    }

    private void ajouterBoutons(JPanel panneauBoutons) {
        JButton boutonMenu = creerBoutonStyle("Quitter");
        boutonMenu.addActionListener(e -> {
            // Arrêter les animations avant de fermer
            animationTimer.stop();
            interfaceGraphique.getControler().clavier("exit");
        });

        JButton boutonRejouer = creerBoutonStyle("Nouvelle partie");
        boutonRejouer.addActionListener(e -> {
            // Arrêter les animations avant de fermer
            animationTimer.stop();
            interfaceGraphique.demarrerNouvellePartie();
        });

        panneauBoutons.add(boutonMenu);
        panneauBoutons.add(boutonRejouer);
    }

    private JButton creerBoutonStyle(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Arial", Font.BOLD, 20));
        bouton.setForeground(Color.WHITE);
        bouton.setBackground(new Color(0, 100, 0));
        bouton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effet de survol
        bouton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bouton.setBackground(new Color(0, 130, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bouton.setBackground(new Color(0, 100, 0));
            }
        });

        return bouton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessiner les confettis
        for (ConfettiParticle confetti : confettis) {
            confetti.dessiner(g2d);
        }

        // Message de victoire avec effet d'échelle et de couleur
        dessinerMessageVictoire(g2d);
    }

    private void dessinerMessageVictoire(Graphics2D g2d) {
        String message = "Félicitations " + gagnant + " !";
        Font fontBase = new Font("Arial", Font.BOLD, 48);

        // Appliquer l'échelle
        FontMetrics fm = g2d.getFontMetrics(fontBase);
        int textWidth = fm.stringWidth(message);
        int x = (getWidth() - textWidth) / 2;
        int y = getHeight() / 3;

        // Transformation pour l'échelle
        AffineTransform ancien = g2d.getTransform();
        g2d.translate(x + textWidth/2, y);
        g2d.scale(textScale, textScale);
        g2d.translate(-(x + textWidth/2), -y);

        // Dessiner le texte avec effet arc-en-ciel
        g2d.setFont(fontBase);
        g2d.setColor(Color.getHSBColor(textHue, 0.8f, 1.0f));
        g2d.drawString(message, x, y);

        // Restaurer la transformation
        g2d.setTransform(ancien);
    }

    private void updateAnimation() {
        // Mise à jour des confettis
        for (ConfettiParticle confetti : confettis) {
            confetti.update();
            if (confetti.y > getHeight()) {
                confetti.reset();
            }
        }

        // Mise à jour des effets de texte
        textHue = (textHue + 0.01f) % 1.0f;
        if (textScalingUp) {
            textScale += TEXT_SCALE_SPEED;
            if (textScale >= 1.15f) textScalingUp = false;
        } else {
            textScale -= TEXT_SCALE_SPEED;
            if (textScale <= 1.0f) textScalingUp = true;
        }
    }

    private ConfettiParticle createConfetti(Random random) {
        return new ConfettiParticle(
                random.nextInt(getWidth()+1),
                -random.nextInt(getHeight()+1),
                random.nextInt(8) + 4,
                random.nextFloat() * 2 - 1,
                2 + random.nextFloat() * 2,
                Color.getHSBColor(random.nextFloat(), 0.8f, 1.0f)
        );
    }

    private static class ConfettiParticle {
        float x, y, speedX, speedY;
        int size;
        Color color;

        ConfettiParticle(int x, int y, int size, float speedX, float speedY, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.speedX = speedX;
            this.speedY = speedY;
            this.color = color;
        }

        void update() {
            x += speedX;
            y += speedY;
        }

        void reset() {
            y = -size;
            x = (float) (Math.random() * 800);
        }

        void dessiner(Graphics2D g) {
            g.setColor(color);
            g.fillRect((int)x, (int)y, size, size);
        }
    }
}