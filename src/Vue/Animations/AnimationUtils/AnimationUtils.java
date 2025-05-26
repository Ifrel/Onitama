package Vue.Animations.AnimationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;


/**
 * Méthodes utilitaires pour appliquer l'animation de retournement de carte
 * à des composants Swing existants, notamment des JButtons.
 * Cette classe est la seule classe publique dans ce fichier.
 */
public class AnimationUtils {


    /**
     * Applique l'animation de retournement de carte 3D à un JButton existant.
     * Le bouton est enveloppé dans un JLayer, et cet JLayer est configuré
     * pour se mettre à jour en fonction d'un CardFlipAnimator.
     *
     * @param button Le JButton auquel appliquer l'animation. Ce bouton ne doit PAS
     * avoir été ajouté à un conteneur avant cet appel.
     * @return Le JLayer contenant le bouton animé. C'est CE JLayer qui doit
     * être ajouté au conteneur à la place du bouton original.
     */
    public static JLayer<JButton> applyCardFlipAnimation(JButton button) {
        // 1. Créer un nouvel animateur pour ce bouton
        CardFlipAnimator animator = new CardFlipAnimator();

        // 2. Créer un LayerUI qui utilisera cet animateur
        CardFlipLayerUI<JButton> layerUI = new CardFlipLayerUI<>(animator);

        // 3. Créer un JLayer, enveloppant le bouton original avec le LayerUI
        JLayer<JButton> layer = new JLayer<>(button, layerUI);

        // 4. Ajouter un écouteur d'action au bouton original pour démarrer l'animation
        // Lorsque le bouton est cliqué, l'animateur démarre.
        button.addActionListener(e -> animator.startAnimation());

        // 5. Ajouter un écouteur d'animation à l'animateur
        animator.addAnimationListener(() -> layer.repaint()); // Lambda capture 'layer'

        return layer;
    }

}
