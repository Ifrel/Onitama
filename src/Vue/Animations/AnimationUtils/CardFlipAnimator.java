package Vue.Animations.AnimationUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;



/**
 * Gère la logique de l'animation de retournement d'une "carte" en 3D.
 * Cette classe est indépendante des composants graphiques et gère uniquement
 * l'état de l'animation (l'angle de rotation) et sa progression dans le temps.
 */
public class CardFlipAnimator {
    // --- Constantes de l'animation ---
    private static final int FPS_CIBLE = 60; // Fréquence de rafraîchissement cible (images par seconde)
    private static final int DUREE_ANIMATION = 500; // Durée de l'animation en millisecondes (1/2 seconde)
    private static final int DELAI_TIMER = 1000 / FPS_CIBLE; // Délai entre chaque tick du timer

    // --- État de l'animation ---
    private double angleActuel = 0; // Angle de rotation actuel en radians
    private final Timer timer;      // Minuteur Swing pour déclencher les mises à jour
    private boolean enAnimation = false; // Indique si une animation est en cours
    private long debutAnimation;     // Temps de début de l'animation en millisecondes (System.nanoTime / 1_000_000)
    private double angleDepart;      // Angle au début de l'animation
    private double angleCible;       // Angle visé à la fin de l'animation

    // --- Listeners ---
    private final List<AnimationListener> listeners = new ArrayList<>();

    
    /**
     * Constructeur pour l'animateur. Initialise le timer.
     */
    public CardFlipAnimator() {
        // Crée un timer qui se déclenchera à la fréquence cible
        timer = new Timer(DELAI_TIMER, this::mettreAJourAnimation);
        // Assure que le premier tick arrive sans délai initial après le démarrage
        timer.setInitialDelay(0);
    }

    
    /**
     * Ajoute un listener qui sera notifié à chaque mise à jour de l'animation.
     * @param listener Le listener à ajouter (doit implémenter AnimationListener).
     */
    public void addAnimationListener(AnimationListener listener) {
        listeners.add(listener);
    }
    

    /**
     * Supprime un listener.
     * @param listener Le listener à supprimer.
     */
    public void removeAnimationListener(AnimationListener listener) {
        listeners.remove(listener);
    }

    
    /**
     * Notifie tous les listeners enregistrés que l'animation a été mise à jour.
     */
    private void notifyAnimationUpdated() {
        // Parcourt une copie de la liste pour éviter ConcurrentModificationException
        // si un listener se désinscrit pendant l'itération.
        new ArrayList<>(listeners).forEach(AnimationListener::animationUpdated);
    }

    /**
     * Démarre l'animation de retournement. Si une animation est déjà en cours,
     * elle "enchaîne" la rotation pour maintenir une vitesse constante.
     * L'angle cible est l'angle actuel + PI (un demi-tour).
     */
    public void startAnimation() {
        long tempsActuel = System.nanoTime() / 1_000_000; // Temps actuel en ms
        if (enAnimation) {
            // Si déjà en animation, on recalcule l'état pour enchaîner fluidement
            double progression = (tempsActuel - debutAnimation) / (double) DUREE_ANIMATION;
            progression = Math.min(progression, 1.0); // Clampe la progression entre 0 et 1
            // Calcule l'angle actuel basé sur la progression et l'interpolation
            double angleInterpole = angleDepart + (angleCible - angleDepart) * interpoler(progression);

            // Le nouvel angle de départ est l'angle actuel interpolé
            angleDepart = angleInterpole;
            // Le début de la nouvelle animation est maintenant
            debutAnimation = tempsActuel;
            // La nouvelle cible est un demi-tour (PI) de plus que le nouvel angle de départ
            angleCible = angleDepart + Math.PI;

        } else {
            // Si pas en animation, on initialise une nouvelle animation
            enAnimation = true;
            angleDepart = angleActuel;       // L'animation commence depuis l'angle actuel
            angleCible = angleActuel + Math.PI; // L'animation vise un demi-tour supplémentaire
            debutAnimation = tempsActuel;    // Marque le début de l'animation
            timer.start();                   // Démarre le minuteur
        }
    }

    /**
     * Méthode appelée à chaque tick du minuteur. Met à jour l'angle de l'animation.
     * @param e L'événement ActionEvent du minuteur.
     */
    private void mettreAJourAnimation(ActionEvent e) {
        long tempsActuel = System.nanoTime() / 1_000_000; // Temps actuel en ms
        double progression = (tempsActuel - debutAnimation) / (double) DUREE_ANIMATION; // Progression [0, 1+]

        if (progression >= 1.0) {
            // Si l'animation est terminée ou dépassée
            terminerAnimation();
            return;
        }

        // Applique une fonction d'interpolation (easing) à la progression
        double progressionEased = interpoler(progression);
        // Calcule l'angle actuel par interpolation linéaire entre angleDepart et angleCible
        angleActuel = angleDepart + (angleCible - angleDepart) * progressionEased;

        // Notifie les listeners que l'angle a changé et qu'un rafraîchissement est nécessaire
        notifyAnimationUpdated();
    }

    /**
     * Fonction d'interpolation (easing) utilisant une courbe cosinusoïdale.
     * Rend l'animation plus douce aux début et fin.
     * @param t Progression linéaire entre 0.0 et 1.0.
     * @return Progression interpolée, également entre 0.0 et 1.0.
     */
    private double interpoler(double t) {
        // Formula: 0.5 * (1 - cos(PI * t))
        // t=0 -> 0.5 * (1 - cos(0)) = 0.5 * (1 - 1) = 0
        // t=0.5 -> 0.5 * (1 - cos(PI/2)) = 0.5 * (1 - 0) = 0.5
        // t=1 -> 0.5 * (1 - cos(PI)) = 0.5 * (1 - (-1)) = 1
        return 0.5 * (1 - Math.cos(Math.PI * t));
    }

    /**
     * Termine l'animation et met à jour l'état final de l'animation.
     * S'assure que l'angle final est un multiple de PI (face ou dos visible).
     */
    private void terminerAnimation() {
        timer.stop();
        enAnimation = false;
        // Arrondit l'angle cible au multiple de PI le plus proche
        angleActuel = Math.round(angleCible / Math.PI) * Math.PI;
        // Normalise l'angle final pour rester dans [0, 2*PI) si nécessaire
        angleActuel = angleActuel % (2 * Math.PI);
        if (angleActuel < 0) {
            angleActuel += 2 * Math.PI;
        }
        // Notifie une dernière fois pour afficher l'état final exact
        notifyAnimationUpdated();
    }

    /**
     * Obtient l'angle actuel de l'animation en radians.
     * Utilisé par le code de dessin pour appliquer la transformation.
     * @return L'angle actuel de rotation.
     */
    public double getAngleActuel() {
        return angleActuel;
    }

    /**
     * Vérifie si une animation est actuellement en cours.
     * @return true si une animation est active, false sinon.
     */
    public boolean isEnAnimation() {
        return enAnimation;
    }
}
