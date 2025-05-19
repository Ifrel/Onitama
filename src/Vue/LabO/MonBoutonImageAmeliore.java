package Vue.LabO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MonBoutonImageAmeliore {

    private static boolean animationActive = false;

    public static JButton creerBoutonAvecImage(
            String cheminImage,
            float epaisseurBordureBase,
            float arrondiBordure,
            Color couleurBordureBase,
            Color couleurFondSurvol
    ) {
        ImageIcon iconeOriginale = new ImageIcon(cheminImage);

        // Paramètres d'animation
        Timer animationTimer = new Timer(30, null);
        float[] angle = {0}; // Position de l'animation
        Color couleurAnimation = Color.MAGENTA;

        JButton bouton = new JButton() {
            private float epaisseurBordure = epaisseurBordureBase;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int arc = (int) arrondiBordure;

                // Effet de survol : agrandit la bordure
                if (getModel().isRollover()) {
                    epaisseurBordure = epaisseurBordureBase + 2f;
                    g2.setColor(couleurFondSurvol);
                    g2.fillRoundRect(0, 0, width, height, arc, arc);
                } else {
                    epaisseurBordure = epaisseurBordureBase;
                }

                // Effet de clic : démarre l'animation
                if (getModel().isPressed() && animationActive) {
                    g2.setColor(new Color(150, 150, 150, 120));
                    g2.fillRoundRect(2, 2, width - 4, height - 4, arc, arc);
                }

                super.paintComponent(g2);

                // Animation : dessine la bordure avec une couleur tournante
                if (animationActive && getModel().isPressed()) {
                    g2.setStroke(new BasicStroke(epaisseurBordure + 1));
                    float dashPhase = angle[0];
                    g2.setColor(couleurAnimation);
                    g2.setStroke(new BasicStroke(epaisseurBordure + 1,
                            BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                            10.0f, new float[]{10, 10}, dashPhase));
                    g2.drawRoundRect(
                            (int) (epaisseurBordure / 2),
                            (int) (epaisseurBordure / 2),
                            width - (int) epaisseurBordure,
                            height - (int) epaisseurBordure,
                            arc, arc
                    );
                } else {
                    // Bordure normale
                    g2.setColor(couleurBordureBase);
                    g2.setStroke(new BasicStroke(epaisseurBordure));
                    g2.drawRoundRect(
                            (int) (epaisseurBordure / 2),
                            (int) (epaisseurBordure / 2),
                            width - (int) epaisseurBordure,
                            height - (int) epaisseurBordure,
                            arc, arc
                    );
                }

                g2.dispose();
            }

            @Override
            public boolean isContentAreaFilled() {
                return false;
            }
        };

        bouton.setFocusPainted(false);
        bouton.setBorderPainted(false);
        bouton.setOpaque(false);
        bouton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bouton.setFocusable(true);

        // Redimensionne l’icône si nécessaire
        Runnable miseAJourIcone = () -> {
            int largeur = bouton.getWidth();
            int hauteur = bouton.getHeight();
            if (largeur > 0 && hauteur > 0) {
                Image imageRedim = iconeOriginale.getImage().getScaledInstance(
                        largeur, hauteur, Image.SCALE_SMOOTH
                );
                bouton.setIcon(new ImageIcon(imageRedim));
            }
        };

        bouton.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && bouton.isShowing()) {
                SwingUtilities.invokeLater(miseAJourIcone);
            }
        });

        bouton.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                miseAJourIcone.run();
            }
        });

        // Animation de bordure clignotante (tourne en boucle si activée)
        animationTimer.addActionListener(e -> {
            angle[0] += 2f; // Vitesse de l’animation
            if (angle[0] > 40) angle[0] = 0;
            bouton.repaint();
        });
        animationTimer.start();

        return bouton;
    }

    // Méthode pour activer ou désactiver l'animation extérieurement
    public static void setAnimationActive(boolean active) {
        animationActive = active;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Bouton Image Amélioré avec Animation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());

            JButton boutonImage = creerBoutonAvecImage(
                    "chemin/vers/votre/image.png",
                    3.0f,
                    15.0f,
                    new Color(70, 130, 180), // Bleu acier
                    new Color(230, 230, 255) // Couleur survol
            );
            boutonImage.setPreferredSize(new Dimension(100, 100));

            // Bouton d'activation/désactivation
            JToggleButton toggle = new JToggleButton("Animation ON/OFF");
            toggle.addActionListener(e -> setAnimationActive(toggle.isSelected()));

            frame.add(boutonImage);
            frame.add(toggle);
            frame.setSize(350, 250);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}






class BoutonAnime extends JButton {

    private boolean animationActive = false;
    private float epaisseurBordure = 3f;
    private Color couleurAnimation = Color.CYAN;
    private Color couleurBordureBase = Color.GRAY;
    private float[] angle = {5f}; // Dash phase

    private Timer timer;

    public BoutonAnime(String texte) {
        super(texte);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Clique pour activer/désactiver l'animation
        addActionListener(e -> {
            animationActive = !animationActive;
            if (animationActive) {
                demarrerAnimation();
            } else {
                arreterAnimation();
            }
            repaint();
        });

        // Timer pour animer l’effet de bordure
        timer = new Timer(30, e -> {
            angle[0] += 2f; // vitesse de rotation
            if (angle[0] > 1000f) angle[0] = 0f;
            repaint();
        });
    }

    private void demarrerAnimation() {
        timer.start();
    }

    private void arreterAnimation() {
        timer.stop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arc = 20;

        // Fond doux au survol
        if (getModel().isRollover() || isFocusOwner()) {
            g2.setColor(new Color(200, 220, 255, 100));
            g2.fillRoundRect(0, 0, width, height, arc, arc);
        }

        if (animationActive) {
            int segments = 8;  // nombre de maillons visibles
            float baseEpaisseur = epaisseurBordure + 1;
            float dashLength = 12f;
            float gap = 10f;

            for (int i = 0; i < segments; i++) {
                float progress = (float) i / segments;
                float alpha = (float) Math.pow(1 - progress, 2); // l'avant est plus visible

                Color couleurSegment = new Color(
                        couleurAnimation.getRed(),
                        couleurAnimation.getGreen(),
                        couleurAnimation.getBlue(),
                        (int) (alpha * 255)
                );

                g2.setColor(couleurSegment);

                float phase = angle[0] + (i * (dashLength + gap));

                g2.setStroke(new BasicStroke(baseEpaisseur,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER,
                        10f, new float[]{dashLength, gap}, phase));

                g2.drawRoundRect(
                        (int) (epaisseurBordure / 2),
                        (int) (epaisseurBordure / 2),
                        width - (int) epaisseurBordure,
                        height - (int) epaisseurBordure,
                        arc, arc
                );
            }
        } else {
            g2.setColor(couleurBordureBase);
            g2.setStroke(new BasicStroke(epaisseurBordure));
            g2.drawRoundRect(
                    (int) (epaisseurBordure / 2),
                    (int) (epaisseurBordure / 2),
                    width - (int) epaisseurBordure,
                    height - (int) epaisseurBordure,
                    arc, arc
            );
        }

        g2.dispose();
    }

}

class TestAnimation {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Animation Bordure Fluide");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLayout(new FlowLayout());

            BoutonAnime bouton = new BoutonAnime("Clique-moi !");
            bouton.setPreferredSize(new Dimension(200, 60));
            frame.add(bouton);

            frame.setVisible(true);
        });
    }
}









class BoutonAnime2 extends JButton {

    private boolean animationActive = false;
    private float epaisseurBordure = 3f;
    private final Color couleurBordureBase = new Color(60, 120, 180);
    private final Color couleurAnimation = new Color(100, 200, 255);
    private final float[] angle = {0};

    private Timer timer = null;

    public BoutonAnime2(String texte) {
        super(texte);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFocusable(true);

        // Clic pour activer/désactiver l'animation
        addActionListener(e -> {
            animationActive = !animationActive;
            if (animationActive) {
                timer.start();
            } else {
                timer.stop();
                repaint();
            }
        });

        // Timer qui fait tourner l'angle
        timer = new Timer(50, e -> {
            angle[0] += 5f;
            if (angle[0] > 1000f) angle[0] = 0f;
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arc = 20;

        // Fond doux au survol
        if (getModel().isRollover() || isFocusOwner()) {
            g2.setColor(new Color(200, 220, 255, 100));
            g2.fillRoundRect(0, 0, width, height, arc, arc);
        }

        if (animationActive) {
            int segments = 8;             // nombre de maillons visibles
            float baseEpaisseur = epaisseurBordure + 1;
            float dashLength = 12f;
            float gap = 10f;

            for (int i = 0; i < segments; i++) {
                float progress = (float) i / segments;
                float alpha = (float) Math.pow(1 - progress, 2); // l’avant est plus visible

                Color couleurSegment = new Color(
                        couleurAnimation.getRed(),
                        couleurAnimation.getGreen(),
                        couleurAnimation.getBlue(),
                        (int) (alpha * 255)
                );

                g2.setColor(couleurSegment);

                float phase = angle[0] + (i * (dashLength + gap));

                g2.setStroke(new BasicStroke(baseEpaisseur,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER,
                        10f, new float[]{dashLength, gap}, phase));

                g2.drawRoundRect(
                        (int) (epaisseurBordure / 2),
                        (int) (epaisseurBordure / 2),
                        width - (int) epaisseurBordure,
                        height - (int) epaisseurBordure,
                        arc, arc
                );
            }
        } else {
            // Bordure normale
            g2.setColor(couleurBordureBase);
            g2.setStroke(new BasicStroke(epaisseurBordure));
            g2.drawRoundRect(
                    (int) (epaisseurBordure / 2),
                    (int) (epaisseurBordure / 2),
                    width - (int) epaisseurBordure,
                    height - (int) epaisseurBordure,
                    arc, arc
            );
        }

        g2.dispose();
    }
}

class TestAnimation2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Bouton animé");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);

            BoutonAnime2 bouton = new BoutonAnime2("Clique-moi");
            bouton.setPreferredSize(new Dimension(200, 80));

            JPanel panel = new JPanel();
            panel.setBackground(Color.WHITE);
            panel.add(bouton);

            frame.setContentPane(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}












class MonBoutonAnime2 extends JButton {

    private final float epaisseurInitiale;
    private final float arrondiBordure;
    private final Color couleurInitiale;
    private final Color couleurFondSurvol;
    private float epaisseurAnimee;
    private Color couleurAnimee;
    private boolean animationActivee = false;
    private float phase = 0;
    private final Timer timer;

    public MonBoutonAnime2(ImageIcon icone, float epaisseurBordure, float arrondi, Color couleurBordure, Color fondSurvol) {
        super(icone);
        this.epaisseurInitiale = epaisseurBordure;
        this.epaisseurAnimee = epaisseurBordure;
        this.arrondiBordure = arrondi;
        this.couleurInitiale = couleurBordure;
        this.couleurAnimee = couleurBordure;
        this.couleurFondSurvol = fondSurvol;

        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Timer d'animation
        timer = new Timer(50, e -> {
            phase += 0.2f;
            epaisseurAnimee = epaisseurInitiale + (float) (Math.sin(phase) * 2);
            couleurAnimee = new Color(
                    (int) (Math.abs(Math.sin(phase)) * 255),
                    couleurInitiale.getGreen(),
                    couleurInitiale.getBlue()
            );
            repaint();
        });

        addActionListener(e -> activerAnimationBordure(!animationActivee));
    }

    public void activerAnimationBordure(boolean activer) {
        this.animationActivee = activer;
        if (activer) {
            timer.start();
        } else {
            timer.stop();
            epaisseurAnimee = epaisseurInitiale;
            couleurAnimee = couleurInitiale;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = (int) arrondiBordure;

        if (getModel().isRollover() || isFocusOwner()) {
            g2.setColor(couleurFondSurvol);
            g2.fillRoundRect(0, 0, w, h, arc, arc);
        }

        if (getModel().isPressed()) {
            g2.setColor(new Color(130, 130, 130, 150));
            g2.fillRoundRect(2, 2, w - 4, h - 4, arc, arc);
        }

        super.paintComponent(g2);

        g2.setStroke(new BasicStroke(epaisseurAnimee));
        g2.setColor(animationActivee ? couleurAnimee : couleurInitiale);
        g2.drawRoundRect(
                (int) (epaisseurAnimee / 2),
                (int) (epaisseurAnimee / 2),
                w - (int) epaisseurAnimee,
                h - (int) epaisseurAnimee,
                arc, arc
        );

        g2.dispose();
    }
}

class TestAnimation3 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Test animation");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLayout(new FlowLayout());

            ImageIcon icone = new ImageIcon("chemin/vers/image.png"); // remplace avec une image réelle

            MonBoutonAnime2 bouton = new MonBoutonAnime2(
                    icone,
                    3.0f,
                    20.0f,
                    Color.BLUE,
                    new Color(200, 200, 200, 120)
            );
            bouton.setPreferredSize(new Dimension(100, 100));

            f.add(bouton);
            f.setSize(300, 200);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
