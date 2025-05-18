package Vue.Utils.Boutons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.nio.file.Path;

import static Global.Paths.*;
import static Vue.ConfigUI.ARONDI;

public class BoutonTerrain extends JButton {

    private final float epaisseurInitiale;
    private final float arrondiBordure;
    private final Color couleurBordureInactive = new Color(122, 120, 120, 255);
    private final Color couleurBordureActive = new Color(214, 17, 199, 255);
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
            phase += 0.1f;
            if (phase > 2 * Math.PI) phase -= 2 * Math.PI;
            repaint();
        });

        // addActionListener(e -> activerAnimationBordure(!animationActivee));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                epaisseurAnimee = epaisseurInitiale + 3f;
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
        this(new ImageIcon(cheminImage.toString()), 2.0f, ARONDI);
    }

    public void activerAnimationBordure(boolean activer) {
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
        setIcon(nouvelleImage);   // Remplace l'icône actuelle par la nouvelle
        repaint();                // Demande la mise à jour graphique pour afficher la nouvelle image
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = (int) arrondiBordure;

        // 1. Peindre le fond transparent ou la couleur de fond du bouton
        if (isOpaque()) {
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, w, h, arc, arc);
        }

        // Calcul épaisseur totale bordure
        float epaisseurBordureTotale = epaisseurAnimee;
        if (animationActivee) {
            epaisseurBordureTotale += 2.5f;
        }
        int marge = (int) (epaisseurBordureTotale + 2f); // + effet bouton qui diminue et grossit

        // 2. Dessiner l’image redimensionnée et centrée en tenant compte de la marge
        Icon icon = getIcon();                          // Récupère l’icône actuelle du bouton
        if (icon instanceof ImageIcon) {                // Vérifie que l’icône est bien une ImageIcon (contient une image)
            ImageIcon imageIcon = (ImageIcon) icon;      // Convertit l’icône en ImageIcon pour accéder à l’image
            Image image = imageIcon.getImage();          // Récupère l’objet Image depuis l’ImageIcon

            int iw = image.getWidth(this);       // Obtient la largeur naturelle de l’image
            int ih = image.getHeight(this);      // Obtient la hauteur naturelle de l’image

            if (iw > 0 && ih > 0) {                       // Vérifie que les dimensions de l’image sont valides (positives)
                int availableWidth = w - 2 * marge ;   // Calcul de la largeur disponible pour dessiner l’image : largeur bouton moins marges et espace (0 px)
                int availableHeight = h - 2 * marge - 10;  // Calcul de la hauteur disponible pour dessiner l’image : hauteur bouton moins marges et espace (10 px)

                float scale = Math.min((float) availableWidth / iw, (float) availableHeight / ih);  // Calcule le facteur d’échelle pour que l’image rentre dans la zone disponible sans déformation
                int nw = (int) (iw * scale);    // Largeur finale de l’image redimensionnée selon le facteur d’échelle
                int nh = (int) (ih * scale);    // Hauteur finale de l’image redimensionnée selon le facteur d’échelle

                int x = marge + (availableWidth - nw) / 2;     // Position X pour centrer horizontalement l’image dans la zone disponible (marge + moitié du reste)
                int y = marge + (availableHeight - nh) / 2 +3;     // Position Y à +3 px pour centrer verticalement l’image dans la zone disponible (marge + moitié du reste)

                // Dessine l’image redimensionnée à la position calculée
                g2.drawImage(image, x, y, nw, nh, this);
            }
    }

        // 3. Dessiner la bordure arrondie
        g2.setStroke(new BasicStroke(epaisseurBordureTotale));
        g2.setColor(animationActivee ? couleurBordureActive : couleurBordureInactive);
        g2.drawRoundRect(marge, marge, w - 2 * marge, h - 2 * marge, arc, arc);

        // 4. Animation si activée
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
                bouton4.activerAnimationBordure(true);
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
