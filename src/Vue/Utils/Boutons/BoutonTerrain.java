package Vue.Utils.Boutons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.nio.file.Path;

import static Vue.Configuration.ConfigUI.ARRONDI;

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
    private Color backgroundColor = new Color(255, 255, 255, 255);


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
        this(new ImageIcon(cheminImage.toString()), 2.0f, ARRONDI);
    }

    public BoutonTerrain(BufferedImage image) {
        this((image != null) ? new ImageIcon(image) : new ImageIcon(""), 2.0f, ARRONDI);
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

    public void chargerCouleurFond(Color color) {
        this.aCouleurDeFond = true;
        this.couleurdeFond = color;
        repaint();
    }

    public void enleverCouleurFond() {
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
        this.couleurBordureInactive = new Color(122, 120, 120, 255);
        this.couleurBordureActive = new Color(214, 17, 199, 255);
        repaint();
    }



    /**
     * Définit la couleur d'arrière-plan du composant.
     * Cette méthode redéfinit la méthode setBackground de la classe parente
     * et met à jour la couleur interne du composant.
     *
     * @param bg la nouvelle couleur d'arrière-plan à appliquer
     */
//    public void setBackColor(Color bg) {
////        super.setBackground(bg);
//        this.backgroundColor = bg;
//        setOpaque(true);
//        repaint();
//    }


    /**
     * Dessine le composant avec tous ses éléments graphiques.
     * Cette méthode gère le rendu complet du composant incluant :
     * - Le fond avec coins arrondis
     * - L'image de fond (si présente)
     * - Les bordures
     * - Les effets d'animation
     * - Les états désactivés
     * - Les couleurs personnalisées
     *
     * @param g le contexte graphique dans lequel effectuer le rendu
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = (int) arrondiBordure;

        // 1. Fond
//        if (isOpaque()) {
//            // Utilise la couleur d'arrière-plan si elle est définie, sinon utilise la couleur par défaut
//            g2.setColor(backgroundColor);
//            g2.fillRoundRect(0, 0, w, h, arc, arc);
//        }

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




