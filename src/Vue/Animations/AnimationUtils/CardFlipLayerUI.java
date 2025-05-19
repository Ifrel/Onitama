package Vue.Animations.AnimationUtils;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.awt.geom.AffineTransform;


/**
 * Un LayerUI qui utilise un CardFlipAnimator pour appliquer une transformation 3D
 * (rotation et mise à l'échelle) à la vue qu'il enveloppe avant de la peindre.
 * <T extends JComponent> indique que ce LayerUI peut s'appliquer à n'importe quel composant Swing.
 */
public class CardFlipLayerUI<T extends JComponent> extends LayerUI<T> {

    private final CardFlipAnimator animator; // L'animateur qui fournit l'angle

    /**
     * Crée un LayerUI qui appliquera l'animation.
     * @param animator L'animateur à utiliser pour obtenir l'angle de rotation actuel.
     */
    public CardFlipLayerUI(CardFlipAnimator animator) {
        this.animator = animator;
    }

    /**
     * Intercepte et modifie le processus de peinture de la vue (le composant enveloppé).
     * Cette méthode est appelée par le JLayer lorsque sa vue doit être peinte.
     * @param g Le contexte graphique.
     * @param c Le JComponent parent, qui est le JLayer.
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        // g: Le contexte graphique pour dessiner sur le JLayer.
        // c: Le JComponent parent, qui est le JLayer.
        // l.getView(): Obtient le composant enveloppé (la vue).

        Graphics2D g2d = (Graphics2D) g.create(); // Crée une copie du contexte graphique pour appliquer des transformations

        // Appliquer des hints de rendu pour améliorer la qualité visuelle
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // Obtenir l'angle actuel de l'animateur
        double angle = animator.getAngleActuel();
        // Obtenir les dimensions du JLayer (qui correspondent à celles du composant wrappé si bien dimensionné)
        double width = c.getWidth();
        double height = c.getHeight();

        // Calculer le facteur d'échelle dynamique pour simuler la perspective 3D (plus petit quand vu de profil)
        // Utilise la valeur absolue du sinus de l'angle. À 0 et PI (face/dos), sin(angle) est 0, échelle 1.
        // À PI/2 et 3PI/2 (profil), sin(angle) est 1 ou -1, abs(sin(angle)) est 1, échelle minimum (1 - 0.3 = 0.7).
        double facteurEchelle = 1.0 - 0.3 * Math.abs(Math.sin(angle));

        // Créer la transformation affine pour rotation et échelle centrée sur le composant
        AffineTransform transform = new AffineTransform();
        // 1. Translate au centre du composant
        transform.translate(width / 2.0, height / 2.0);
        // 2. Applique la rotation par l'angle actuel
        transform.rotate(angle);
        // 3. Applique la mise à l'échelle (uniquement sur l'axe X *après* rotation)
        transform.scale(facteurEchelle, 1.0);
        // 4. Translate de retour à l'origine
        transform.translate(-width / 2.0, -height / 2.0);

        // Sauvegarder la transformation actuelle du contexte graphique
        AffineTransform originalTransform = g2d.getTransform();

        // Appliquer la transformation calculée au contexte graphique
        g2d.transform(transform);

        // Peindre la vue originale (le composant wrappé) en utilisant le contexte graphique modifié.
        // Le composant se dessine normalement, mais son output est transformé.
        super.paint(g2d, c);

        // Restaurer la transformation originale pour ne pas affecter les dessins ultérieurs
        g2d.setTransform(originalTransform);

        g2d.dispose(); // Libère les ressources de la copie du contexte graphique
    }

    // On peut aussi surcharger processMouseEvent, processKeyEvent, etc. dans LayerUI
    // pour intercepter et modifier les événements avant qu'ils n'atteignent la vue,
    // mais ici, l'ActionListener sur le bouton original est suffisant pour déclencher l'animation.
}
