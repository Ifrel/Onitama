package Vue.Utils.Boutons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.nio.file.Path;

import static Global.Paths.*;
import static Vue.ConfigUI.ARONDI;

public class BoutonTerrain extends JButton {

    private final float epaisseurInitiale;
    private final float arrondiBordure;
    private Color couleurBordureInactive = new Color(122, 120, 120, 255);
    private Color couleurBordureActive = new Color(214, 17, 199, 255);
    private float epaisseurAnimee;
    private boolean animationActivee = false;
    private float phase = 0;
    private final Timer timer;

    private Image imageDeFond; // Stocke l'image à afficher
    private Color couleurdeFond;
    private boolean aCouleurDeFond = false;
    private boolean aCouleurBordure = true;


    public BoutonTerrain(ImageIcon icone, float epaisseurBordure, float arrondi) {
        super();
        this.epaisseurInitiale = epaisseurBordure;
        this.epaisseurAnimee = epaisseurBordure;
        this.arrondiBordure = arrondi;

        this.imageDeFond = icone.getImage();

        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        timer = new Timer(40, e -> {
            phase += 0.1f;
            if (phase > 2 * Math.PI) phase -= 2 * Math.PI;
            repaint();
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) { // si le bouton est actif
                    epaisseurAnimee = epaisseurInitiale + 3f;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) {
                    epaisseurAnimee = epaisseurInitiale;
                    repaint();
                }
            }
        });

    }

    public BoutonTerrain(Path cheminImage) {
        this(new ImageIcon(cheminImage.toString()), 2.0f, ARONDI);
    }


    public BoutonTerrain(BufferedImage image) {
        this((image != null) ? new ImageIcon(image) : new ImageIcon(""), 2.0f, ARONDI);
    }


    public void activerAnimation(boolean activer) {
        if (!isEnabled()) return;

        this.animationActivee = activer;

        if (activer) {
            epaisseurAnimee = epaisseurInitiale + 2.5f;
            timer.start();
        } else {
            timer.stop();
            epaisseurAnimee = epaisseurInitiale;
            repaint();
        }
    }

    public void changerImage(Path cheminImage) {
        changerImage(new ImageIcon(cheminImage.toString()));
    }

    public void changerImage(ImageIcon nouvelleImage) {
        this.imageDeFond = nouvelleImage.getImage();
        repaint();
    }

    public void changerImage(BufferedImage nouvelleImage) {
        this.imageDeFond = nouvelleImage;
        repaint();
    }

    public void chargerCouleurFont(Color color) {
        this.aCouleurDeFond = true;
        this.couleurdeFond = color;
        repaint();
    }

    public void enleverCouleurFont() {
        this.aCouleurDeFond = false;
        this.couleurdeFond = new Color(0, 0, 0, 0);
        repaint();
    }

    public void chargerCouleurBordure(Color active, Color inactive) {
        this.aCouleurBordure = true;
        this.couleurBordureActive = active;
        this.couleurBordureInactive = inactive;
        repaint();
    }

    public void enleverCouleurBordure() {
        this.aCouleurBordure = false;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = (int) arrondiBordure;

        // 1. Fond
        if (isOpaque()) {
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, w, h, arc, arc);
        }

        float epaisseurBordureTotale = epaisseurAnimee + (animationActivee ? 2.5f : 0f);
        int marge = (int) (epaisseurBordureTotale + 2f);

        // 2. Dessin de l'image
        if (imageDeFond != null) {
            int iw = imageDeFond.getWidth(this);
            int ih = imageDeFond.getHeight(this);

            if (iw > 0 && ih > 0) {
                int availableWidth = w - 2 * marge;
                int availableHeight = h - 2 * marge - 10;

                float scale = Math.min((float) availableWidth / iw, (float) availableHeight / ih);
                int nw = (int) (iw * scale);
                int nh = (int) (ih * scale);

                int x = marge + (availableWidth - nw) / 2;
                int y = marge + (availableHeight - nh) / 2 + 3;

                g2.drawImage(imageDeFond, x, y, nw, nh, this);
            }
        }

        // 3. Bordure
        g2.setStroke(new BasicStroke(epaisseurBordureTotale));
        g2.setColor(animationActivee ? couleurBordureActive : couleurBordureInactive);
        g2.drawRoundRect(marge, marge, w - 2 * marge, h - 2 * marge, arc, arc);

        // 4. Animation
        if (animationActivee) {
            float cx = w / 2f;
            float cy = h / 2f;

            float centreRadius = 10f;
            g2.setColor(new Color(30, 200, 10));
            g2.fill(new Ellipse2D.Float(cx - centreRadius, cy - centreRadius, 2 * centreRadius, 2 * centreRadius));

            float pulse1 = 20f + 10f * (float) Math.abs(Math.sin(phase));
            float pulse2 = 35f + 15f * (float) Math.abs(Math.sin(phase + Math.PI / 2));

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(198, 71, 207, 150));
            g2.draw(new Ellipse2D.Float(cx - pulse1, cy - pulse1, 2 * pulse1, 2 * pulse1));

            g2.setColor(new Color(198, 71, 207, 100));
            g2.draw(new Ellipse2D.Float(cx - pulse2, cy - pulse2, 2 * pulse2, 2 * pulse2));
        }

        // 5. Si le bouton est désactivé
        if (!isEnabled()) {
            g2.setColor(new Color(176, 174, 174, 63));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        }

        // 6. Si une couleur de fond chargée
        if (aCouleurDeFond && couleurdeFond != null) {
            g2.setColor(couleurdeFond);
            g2.fillRoundRect(0, 0, w, h, arc, arc);
        }


        // 7. Si des couleurs de bordures chargées
        if (aCouleurBordure) {
            g2.setStroke(new BasicStroke(epaisseurBordureTotale));
            g2.setColor(animationActivee ? couleurBordureActive : couleurBordureInactive);
            g2.drawRoundRect(marge, marge, w - 2 * marge, h - 2 * marge, arc, arc);
        }


        g2.dispose();
    }
}


/**************************************
 * ***      AUTRE VERSION   ***********
 * ***********************************/

/*
public class BoutonTerrain extends JButton {

    private final float epaisseurInitiale;
    private final float arrondiBordure;
    private final Color couleurBordureInactive = Color.GRAY;
    private final Color couleurBordureActive = new Color(198, 71, 207);
    private final float epaisseurMax = 10.0f;
    private float epaisseurAnimee;
    private boolean animationActivee = false;
    private float phase = 0;
    private final Timer timer;

    public BoutonTerrain(ImageIcon icone, float epaisseurBordure, float arrondi) {
        super(icone);
        this.epaisseurInitiale = epaisseurBordure;
        this.epaisseurAnimee = epaisseurBordure;
        this.arrondiBordure = arrondi;

        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        timer = new Timer(40, e -> {
            phase += 0.07f;  // contrôle la vitesse de pulsation
            if (phase > 2 * Math.PI) {
                phase -= 2 * Math.PI;
            }
            repaint();
        });

        addActionListener(e -> activerAnimationBordure(!animationActivee));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                epaisseurAnimee = epaisseurMax;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                epaisseurAnimee = epaisseurInitiale;
                repaint();
            }
        });
    }

    public BoutonTerrain(Path cheminImage) {
        this(new ImageIcon(cheminImage.toString()), 4.0f, ARONDI);
    }

    public void activerAnimationBordure(boolean activer) {
        this.animationActivee = activer;
        if (activer) {
            timer.start();
        } else {
            timer.stop();
            epaisseurAnimee = epaisseurInitiale;
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

        super.paintComponent(g2);

        // Dessiner la bordure
        g2.setStroke(new BasicStroke(epaisseurAnimee));
        g2.setColor(animationActivee ? couleurBordureActive : couleurBordureInactive);
        g2.drawRoundRect((int)(epaisseurAnimee / 2), (int)(epaisseurAnimee / 2),
                w - (int) epaisseurAnimee, h - (int) epaisseurAnimee, arc, arc);

        // Animation cible centrale si activée
        if (animationActivee) {
            float cx = w / 2f;
            float cy = h / 2f;

            // Paramètres de pulsation (oscillation entre 0.7 et 1.3)
            float pulsation = 1.0f + 0.3f * (float)Math.sin(phase);

            // Couleur violette semi-transparente
            Color violet = new Color(198, 71, 207, 180);

            g2.setStroke(new BasicStroke(3));

            // 3 cercles concentriques pulsants
            for (int i = 3; i >= 1; i--) {
                float radius = i * 15 * pulsation;
                float x = cx - radius;
                float y = cy - radius;

                // Le cercle intérieur est plus opaque, le plus grand plus transparent
                int alpha = (int)(180 / i);
                g2.setColor(new Color(violet.getRed(), violet.getGreen(), violet.getBlue(), alpha));
                g2.draw(new Ellipse2D.Float(x, y, 2 * radius, 2 * radius));
            }

            // Halo lumineux au centre (rempli avec dégradé simple)
            RadialGradientPaint halo = new RadialGradientPaint(
                    cx, cy, 20 * pulsation,
                    new float[]{0f, 1f},
                    new Color[]{new Color(198, 71, 207, 150), new Color(198, 71, 207, 0)}
            );
            g2.setPaint(halo);
            g2.fill(new Ellipse2D.Float(cx - 20 * pulsation, cy - 20 * pulsation, 40 * pulsation, 40 * pulsation));
        }

        g2.dispose();
    }
}
*/


class TestBoutonTerrain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Test complet BoutonTerrain");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

            // Bouton initial avec image par défaut
            BoutonTerrain bouton1 = new BoutonTerrain(PATH_PION_NOIR_ETUDIANT);
            bouton1.setPreferredSize(new Dimension(120, 120));
            bouton1.setEnabled(false);
            f.add(bouton1);

            // Bouton avec une autre image et taille plus grande
            BoutonTerrain bouton2 = new BoutonTerrain(PATH_PION_ROUGE_MAITRE);
            bouton2.setPreferredSize(new Dimension(150, 150));
            f.add(bouton2);

            // Bouton testant la méthode de changement d'image dynamique après 3 secondes
            BoutonTerrain bouton3 = new BoutonTerrain(PATH_BTN_MODE_AUTO_OFF);
            bouton3.setPreferredSize(new Dimension(120, 120));
            f.add(bouton3);

            // Lance un timer pour changer l'image de bouton3 après 3 secondes
            new Timer(3000, e -> {
                ImageIcon nouvelleImage = new ImageIcon(PATH_PION_BLEU_ETUDIANT.toString());
                bouton3.changerImage(nouvelleImage);
                System.out.println("Image changée dynamiquement !");
            }).start();

            // Bouton sans animation activée initialement, on l'active après 5 secondes
            BoutonTerrain bouton4 = new BoutonTerrain(PATH_PION_BLEU_ETUDIANT);
            bouton4.setPreferredSize(new Dimension(120, 120));
            f.add(bouton4);

            new Timer(5000, e -> {
                bouton4.activerAnimation(true);
                System.out.println("Animation activée !");
            }).start();

            // Bouton testant activation/désactivation animation via clic (par défaut)
            BoutonTerrain bouton5 = new BoutonTerrain(PATH_PION_NOIR_MAITRE);
            bouton5.setPreferredSize(new Dimension(120, 120));
            f.add(bouton5);

            f.setSize(800, 400);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
