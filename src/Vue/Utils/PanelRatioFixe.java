package Vue.Utils; // Ou votre package approprié

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
// N'oubliez pas l'import pour Timer
import javax.swing.Timer;

/**
 * PanneauRatioFixe est un conteneur qui ajuste dynamiquement la taille et la position
 * d'un composant interne (le "contenu") de manière à ce que ce contenu respecte
 * un ratio largeur/hauteur constant.
 * Ce ratio est conservé quelle que soit la taille du PanneauRatioFixe lui-même.
 * Le contenu est toujours centré à l'intérieur du PanneauRatioFixe.
 * Utilise un Timer pour "déclencher" (debounce) les mises à jour de layout lors du redimensionnement rapide.
 */
public class PanelRatioFixe extends JPanel {
    private static final Dimension DEFAULT_PREFERRED_SIZE = new Dimension(200, 200);
    private static final int RESIZE_DEBOUNCE_DELAY_MS = 30; // Délai en millisecondes pour le debouncing

    private final JComponent contenu;
    private final double ratioLargeurSurHauteur;
    private final Timer resizeDebounceTimer;

    /**
     * Crée un panneau à ratio fixe.
     * Le contenu sera redimensionné et centré pour respecter le ratio spécifié.
     *
     * @param contenu Le composant à afficher à l'intérieur du panneau. Ne doit pas être nul.
     * @param ratioLargeurSurHauteur Le ratio (largeur / hauteur) que le contenu doit respecter
     * (ex : 1.0 pour un carré, 16.0/9.0 pour du 16:9).
     * Doit être strictement supérieur à 0.
     * @throws IllegalArgumentException si le contenu est nul ou si le ratio est inférieur ou égal à 0.
     */
    public PanelRatioFixe(JComponent contenu, double ratioLargeurSurHauteur) {
        if (contenu == null) {
            throw new IllegalArgumentException("Le contenu ne peut pas être nul.");
        }
        if (ratioLargeurSurHauteur <= 0) {
            throw new IllegalArgumentException("Le ratio largeur/hauteur doit être strictement supérieur à 0.");
        }

        this.contenu = contenu;
        this.ratioLargeurSurHauteur = ratioLargeurSurHauteur;

        setLayout(null);
        setOpaque(false); // Par défaut, transparent. Le composant ou l'utilisateur peut changer cela.
        add(this.contenu);

        // Initialisation du Timer pour le debouncing
        // L'ActionListener du Timer appellera la méthode d'ajustement.
        this.resizeDebounceTimer = new Timer(RESIZE_DEBOUNCE_DELAY_MS, e -> {
            ajusterGeometrieContenu();
        });
        this.resizeDebounceTimer.setRepeats(false); // Le timer ne se déclenche qu'une fois par appel à start()

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // À chaque événement de redimensionnement, on (re)démarre le timer.
                // L'ajustement réel ne se fera que lorsque le timer expirera (après une pause dans les redimensionnements).
                if (getWidth() > 0 && getHeight() > 0) { // S'assurer qu'on a des dimensions valides
                    resizeDebounceTimer.restart();
                } else {
                    // Si les dimensions sont nulles (ex: le composant est retiré ou caché),
                    // on peut vouloir annuler un timer en cours et/ou faire un ajustement immédiat.
                    resizeDebounceTimer.stop();
                    ajusterGeometrieContenu(); // Ajuster avec les dimensions potentiellement nulles pour cacher/réinitialiser le contenu.
                }
            }
        });

        // Ajustement initial, différé pour s'assurer que la hiérarchie des composants est établie.
        SwingUtilities.invokeLater(this::ajusterGeometrieContenu);
    }

    /**
     * Crée un panneau avec un contenu qui maintiendra un ratio carré (1:1) par défaut.
     *
     * @param contenu Le composant à intégrer dans le panneau carré. Ne doit pas être nul.
     * @throws IllegalArgumentException si le contenu est nul.
     */
    public PanelRatioFixe(JComponent contenu) {
        this(contenu, 1.0);
    }

    /**
     * Ajuste la taille et la position du contenu pour respecter le ratio fixé et le centrer.
     * Cette méthode est typiquement appelée lorsque la taille de ce PanneauRatioFixe change
     * (souvent via le resizeDebounceTimer).
     */
    private void ajusterGeometrieContenu() {
        int largeurPanneau = getWidth();
        int hauteurPanneau = getHeight();

        if (largeurPanneau <= 0 || hauteurPanneau <= 0) {
            // Si le panneau n'a pas de dimensions valides, on peut cacher ou réinitialiser le contenu.
            // Mettre la taille à 0,0 le rendra effectivement invisible.
            contenu.setBounds(0, 0, 0, 0);
            // Un repaint est souhaitable pour effacer l'ancienne position si le panneau parent est opaque
            // et si ce panneau était opaque avant.
            if (isOpaque()) repaint(); else if (getParent() != null) getParent().repaint();

            return;
        }

        int nouvelleLargeurContenu;
        int nouvelleHauteurContenu;

        double ratioActuelPanneau = (double) largeurPanneau / hauteurPanneau;

        if (ratioActuelPanneau > this.ratioLargeurSurHauteur) {
            nouvelleHauteurContenu = hauteurPanneau;
            nouvelleLargeurContenu = (int) Math.round(nouvelleHauteurContenu * this.ratioLargeurSurHauteur);
        } else {
            nouvelleLargeurContenu = largeurPanneau;
            nouvelleHauteurContenu = (int) Math.round(nouvelleLargeurContenu / this.ratioLargeurSurHauteur);
        }

        int x = (largeurPanneau - nouvelleLargeurContenu) / 2;
        int y = (hauteurPanneau - nouvelleHauteurContenu) / 2;

        // Appliquer les nouvelles limites au contenu
        // Il est important de vérifier si les limites ont réellement changé avant d'appeler setBounds
        // pour éviter des cycles de validation/repaint inutiles si rien n'a changé.
        if (contenu.getX() != x || contenu.getY() != y ||
                contenu.getWidth() != nouvelleLargeurContenu || contenu.getHeight() != nouvelleHauteurContenu) {
            contenu.setBounds(x, y, nouvelleLargeurContenu, nouvelleHauteurContenu);
        }
        // Un repaint du PanelRatioFixe est nécessaire si son apparence dépend de la position/taille
        // du contenu (par exemple, s'il dessine une bordure autour ou si le contenu est transparent
        // et que le PanelRatioFixe a un fond). Si le PanelRatioFixe est setOpaque(false) (par défaut)
        // et n'a pas de peinture custom, le repaint du contenu lui-même (déclenché par setBounds)
        // est souvent suffisant. Cependant, un repaint() ici est plus sûr.
        repaint();
    }

    /**
     * Fournit la taille préférée pour ce panneau.
     * La taille suggérée est basée sur la taille préférée du contenu, ajustée pour
     * respecter le ratio fixé pour le contenu.
     *
     * @return La taille préférée du panneau, calculée pour respecter le ratio du contenu.
     */
    @Override
    public Dimension getPreferredSize() {
        if (contenu == null) {
            // Peut arriver si getPreferredSize est appelé par la superclasse avant initialisation complète
            return DEFAULT_PREFERRED_SIZE;
        }

        Dimension prefContenu = contenu.getPreferredSize();

        if (prefContenu == null || (prefContenu.width <= 0 && prefContenu.height <= 0)) {
            // Si le contenu n'a pas de taille préférée significative, on utilise notre défaut,
            // en essayant de lui appliquer le ratio.
            // C'est un cas limite, le comportement exact peut dépendre des besoins.
            // Pour la simplicité, on retourne une taille par défaut qui respecte le ratio.
            if (ratioLargeurSurHauteur >= 1.0) { // Plus large ou carré
                return new Dimension(DEFAULT_PREFERRED_SIZE.width, (int)Math.round(DEFAULT_PREFERRED_SIZE.width / ratioLargeurSurHauteur));
            } else { // Plus haut
                return new Dimension((int)Math.round(DEFAULT_PREFERRED_SIZE.height * ratioLargeurSurHauteur), DEFAULT_PREFERRED_SIZE.height);
            }
        }

        // Assurer que les dimensions préférées du contenu sont positives pour le calcul
        double prefLargeurContenu = Math.max(1, prefContenu.width);
        double prefHauteurContenu = Math.max(1, prefContenu.height);

        int finalLargeur, finalHauteur;

        // On veut que la preferredSize du PanelRatioFixe soit telle que le contenu puisse s'y loger
        // en respectant le ratio. On part de la preferredSize du contenu.
        // Si le ratio pref du contenu est plus "large" que le ratio cible:
        if ((prefLargeurContenu / prefHauteurContenu) > ratioLargeurSurHauteur) {
            // On utilise la largeur du contenu pref comme dimension dominante.
            finalLargeur = (int) Math.round(prefLargeurContenu);
            finalHauteur = (int) Math.round(finalLargeur / ratioLargeurSurHauteur);
        } else {
            // Le contenu pref est plus "haut" (ou égal) que le ratio cible.
            // On utilise la hauteur du contenu pref comme dimension dominante.
            finalHauteur = (int) Math.round(prefHauteurContenu);
            finalLargeur = (int) Math.round(finalHauteur * ratioLargeurSurHauteur);
        }

        // S'assurer que les dimensions ne sont pas nulles si les dimensions préférées du contenu étaient valides
        finalLargeur = Math.max(1, finalLargeur);
        finalHauteur = Math.max(1, finalHauteur);

        return new Dimension(finalLargeur, finalHauteur);
    }
}
