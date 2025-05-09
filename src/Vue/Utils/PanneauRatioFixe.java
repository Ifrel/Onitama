package Vue.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * PanneauRatioFixe est un conteneur qui ajuste dynamiquement la taille et la position
 * d'un composant interne de manière à respecter un ratio largeur/hauteur constant.
 * Ce ratio est conservé quelle que soit la taille du panneau parent.
 */
public class PanneauRatioFixe extends JPanel {
    private final JComponent contenu;
    private final double ratioLargeurSurHauteur;

    /**
     * Crée un panneau à ratio fixe.
     *
     * @param contenu Le composant à afficher à l'intérieur du panneau.
     * @param ratioLargeurSurHauteur Le ratio (largeur / hauteur) à respecter (ex : 1.0 pour un carré, 16.0/9.0 pour du 16:9).
     */
    public PanneauRatioFixe(JComponent contenu, double ratioLargeurSurHauteur) {
        this.contenu = contenu;
        this.ratioLargeurSurHauteur = ratioLargeurSurHauteur;

        setLayout(null); // Gestion manuelle de la position et taille du contenu
        setOpaque(false); // Permet de rendre le fond transparent
        add(contenu);

        // Écoute les redimensionnements du panneau
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajusterContenu();
            }
        });
    }

    /**
     * Crée un panneau carré par défaut (ratio 1:1).
     *
     * @param contenu Le composant à intégrer dans le panneau carré.
     */
    public PanneauRatioFixe(JComponent contenu) {
        this(contenu, 1.0);
    }

    /**
     * Ajuste la taille et la position du contenu pour respecter le ratio fixé.
     */
    private void ajusterContenu() {
        int largeurPanneau = getWidth();
        int hauteurPanneau = getHeight();

        if (largeurPanneau == 0 || hauteurPanneau == 0) return;

        // Calcul de la taille maximale respectant le ratio à l'intérieur du panneau
        double ratioPanel = (double) largeurPanneau / hauteurPanneau;
        int nouvelleLargeur, nouvelleHauteur;

        if (ratioPanel > ratioLargeurSurHauteur) {
            // Le panneau est trop large, on ajuste la largeur
            nouvelleHauteur = hauteurPanneau;
            nouvelleLargeur = (int) (nouvelleHauteur * ratioLargeurSurHauteur);
        } else {
            // Le panneau est trop haut, on ajuste la hauteur
            nouvelleLargeur = largeurPanneau;
            nouvelleHauteur = (int) (nouvelleLargeur / ratioLargeurSurHauteur);
        }

        // Centrage du contenu dans le panneau
        int x = (largeurPanneau - nouvelleLargeur) / 2;
        int y = (hauteurPanneau - nouvelleHauteur) / 2;
        contenu.setBounds(x, y, nouvelleLargeur, nouvelleHauteur);

        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        if (contenu != null) {
            Dimension pref = contenu.getPreferredSize();
            double largeur = pref.width;
            double hauteur = pref.height;

            // Ajuste la taille suggérée pour respecter le ratio
            if (largeur / hauteur > ratioLargeurSurHauteur) {
                largeur = hauteur * ratioLargeurSurHauteur;
            } else {
                hauteur = largeur / ratioLargeurSurHauteur;
            }

            return new Dimension((int) largeur, (int) hauteur);
        }
        return new Dimension(200, 200); // Taille par défaut
    }
}

