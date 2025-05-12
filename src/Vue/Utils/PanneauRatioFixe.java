package Vue.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * PanneauRatioFixe est un conteneur qui ajuste dynamiquement la taille et la position
 * d'un composant interne de manière à respecter un ratio largeur/hauteur constant.
 * Ce ratio est conservé quelle que soit la taille du panneau parent.
 * Le contenu est centré dans le panneau. */
public class PanneauRatioFixe extends JPanel {
    private static final Dimension DEFAULT_PREFERRED_SIZE = new Dimension(200, 200);

    private final JComponent contenu;
    private final double ratioLargeurSurHauteur;



    /**
     * Crée un panneau à ratio fixe.
     * Le contenu sera redimensionné et centré pour respecter le ratio spécifié.
     *
     * @param contenu Le composant à afficher à l'intérieur du panneau. Doit être non nul.
     * @param ratioLargeurSurHauteur Le ratio (largeur / hauteur) à respecter (ex : 1.0 pour un carré, 16.0/9.0 pour du 16:9).
     * Doit être strictement supérieur à 0.
     * @throws IllegalArgumentException si le contenu est nul ou si le ratio est <= 0.     */
    public PanneauRatioFixe(JComponent contenu, double ratioLargeurSurHauteur) {
        if (contenu == null) {
            throw new IllegalArgumentException("Le contenu ne peut pas être nul.");
        }
        if (ratioLargeurSurHauteur <= 0) {
            throw new IllegalArgumentException("Le ratio largeur/hauteur doit être strictement supérieur à 0.");
        }

        this.contenu = contenu;
        this.ratioLargeurSurHauteur = ratioLargeurSurHauteur;

        setLayout(null);  // Utilisation d'un layout null pour gérer manuellement la position et la taille du contenu.
        setOpaque(false);
        add(contenu);

        // Écoute les redimensionnements du panneau pour ajuster le contenu.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajusterContenu();
            }
        });

        // Appelle ajusterContenu une première fois au cas où le panneau a déjà une taille lors de l'ajout.
        // Cela peut être nécessaire si le panneau est ajouté à un conteneur déjà visible et dimensionné.
        // SwingUtilities.invokeLater(this::ajusterContenu);
    }



    /**
     * Crée un panneau carré par défaut (ratio 1:1).
     *
     * @param contenu Le composant à intégrer dans le panneau carré. Doit être non nul.
     * @throws IllegalArgumentException si le contenu est nul.
     */
    public PanneauRatioFixe(JComponent contenu) {
        this(contenu, 1.0);
    }



    /**
     * Ajuste la taille et la position du contenu pour respecter le ratio fixé.
     * Cette méthode est appelée lorsque la taille du panneau change.
     * Le contenu est redimensionné pour tenir dans le panneau tout en respectant le ratio, puis centré.
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



    /**
     * Fournit la taille préférée pour ce panneau.
     * Swing utilise cette méthode pour déterminer la taille préférée lors de l'utilisation
     * de gestionnaires de layout dans les conteneurs parents.
     * La taille suggérée est basée sur la taille préférée du contenu et le ratio fixé.
     *
     * @return La taille préférée du panneau qui respecte le ratio fixé.     */
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
        return DEFAULT_PREFERRED_SIZE;
    }
}

