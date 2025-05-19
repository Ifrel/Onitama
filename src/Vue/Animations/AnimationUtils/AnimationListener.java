package Vue.Animations.AnimationUtils;

/**
 * Interface pour les listeners de l'animation de carte.
 * Permet au CardFlipAnimator de notifier que l'état de l'animation a changé
 * et qu'un rafraîchissement de l'affichage est nécessaire.
 */
public interface AnimationListener {
    /**
     * Appelé chaque fois que l'angle de l'animation est mis à jour.
     * Le listener doit généralement demander un repaint du composant concerné.
     */
    void animationUpdated();
}
