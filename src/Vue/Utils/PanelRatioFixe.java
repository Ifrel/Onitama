package Vue.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Panneau qui maintient un ratio d'aspect constant pour son contenu
 */
public class PanelRatioFixe extends JPanel {
    private final JComponent contenu;
    private final double ratio;
    private boolean ajustementEnCours = false;
    private Rectangle derniereBounds = new Rectangle();

    public PanelRatioFixe(JComponent contenu, double ratio) {
        if (contenu == null || ratio <= 0) {
            throw new IllegalArgumentException("Contenu invalide ou ratio <= 0");
        }

        this.contenu = contenu;
        this.ratio = ratio;

        setOpaque(false);
        setLayout(null);
        add(contenu);

        // Optimisation du listener de redimensionnement
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (!ajustementEnCours) {
                    ajustementEnCours = true;
                    SwingUtilities.invokeLater(() -> {
                        ajusterContenu();
                        ajustementEnCours = false;
                    });
                }
            }
        });
    }

    public PanelRatioFixe(JComponent contenu) {
        this(contenu, 1.0);
    }

    private void ajusterContenu() {
        int w = getWidth();
        int h = getHeight();

        if (w <= 0 || h <= 0) return;

        double ratioPanel = (double) w / h;
        int newW, newH, x, y;

        if (ratioPanel > ratio) {
            newH = h;
            newW = (int) (h * ratio);
        } else {
            newW = w;
            newH = (int) (w / ratio);
        }

        x = (w - newW) / 2;
        y = (h - newH) / 2;

        // Vérifie si un changement est nécessaire
        Rectangle nouvelleBounds = new Rectangle(x, y, newW, newH);
        if (!nouvelleBounds.equals(derniereBounds)) {
            contenu.setBounds(nouvelleBounds);
            derniereBounds = nouvelleBounds;
            repaint();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = contenu.getPreferredSize();
        if (d.width <= 0 || d.height <= 0) {
            return new Dimension(200, (int)(200 / ratio));
        }
        return d;
    }
}