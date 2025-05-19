package Vue.LabO;

import Vue.Utils.Boutons.BoutonCarte;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static Global.Paths.PATH_CARTE;

// --- Les classes CardFlipAnimator et AnimationListener restent les mêmes ---

/**
 * Interface pour les listeners de l'animation de carte.
 * Permet de notifier qu'un rafraîchissement est nécessaire.
 */
interface AnimationListener2 {
    void animationUpdated();
}

/**
 * Gère la logique de l'animation de retournement d'une "carte" en 3D.
 * Cette classe est indépendante des composants graphiques et gère uniquement l'état de l'animation (l'angle).
 */
class CardFlipAnimator2 {
    private static final int FPS_CIBLE = 60; // Images par seconde pour l'animation
    private static final int DUREE_ANIMATION = 500; // Durée de l'animation en millisecondes

    private double angleActuel = 0; // Angle actuel de rotation en radians
    private final Timer timer;
    private boolean enAnimation = false;
    private long debutAnimation;
    private double angleDepart;
    private double angleCible;

    private final List<AnimationListener> listeners = new ArrayList<>();

    /**
     * Constructeur pour l'animateur.
     */
    public CardFlipAnimator2() {
        timer = new Timer(1000 / FPS_CIBLE, this::mettreAJourAnimation);
        timer.setInitialDelay(0); // Assure un démarrage immédiat
    }

    /**
     * Ajoute un listener pour être notifié des mises à jour de l'animation.
     * @param listener Le listener à ajouter.
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
     * Notifie tous les listeners qu'une mise à jour de l'animation a eu lieu.
     */
    private void notifyAnimationUpdated() {
        for (AnimationListener listener : listeners) {
            listener.animationUpdated();
        }
    }

    /**
     * Démarre l'animation de retournement.
     * Gère l'enchaînement si l'animation est déjà en cours.
     */
    public void startAnimation() {
        long tempsActuel = System.nanoTime() / 1_000_000;
        if (enAnimation) {
            double progression = (tempsActuel - debutAnimation) / (double) DUREE_ANIMATION;
            progression = Math.min(progression, 1.0);
            double angleInterpole = angleDepart + (angleCible - angleDepart) * interpoler(progression);
            angleDepart = angleInterpole;
            debutAnimation = tempsActuel;
            angleCible = angleDepart + Math.PI; // Ajouter PI pour un demi-tour
        } else {
            enAnimation = true;
            angleDepart = angleActuel;
            angleCible = angleActuel + Math.PI; // Ajouter PI pour un demi-tour
            debutAnimation = tempsActuel;
            timer.start();
        }
    }

    /**
     * Met à jour l'angle de l'animation à chaque tick de la minuterie.
     */
    private void mettreAJourAnimation(ActionEvent e) {
        long tempsActuel = System.nanoTime() / 1_000_000;
        double progression = (tempsActuel - debutAnimation) / (double) DUREE_ANIMATION;

        if (progression >= 1.0) {
            terminerAnimation();
            return;
        }

        double progressionEased = interpoler(progression);
        angleActuel = angleDepart + (angleCible - angleDepart) * progressionEased;
        notifyAnimationUpdated();
    }

    /**
     * Fonction d'interpolation cosinusoïdale pour un easing naturel.
     * @param t Progression entre 0.0 et 1.0
     * @return Valeur interpolée
     */
    private double interpoler(double t) {
        return 0.5 * (1 - Math.cos(Math.PI * t));
    }

    /**
     * Termine l'animation et met à jour l'état final.
     */
    private void terminerAnimation() {
        timer.stop();
        enAnimation = false;
        angleActuel = Math.round(angleCible / Math.PI) * Math.PI;
        notifyAnimationUpdated();
    }

    /**
     * Obtient l'angle actuel de l'animation.
     * @return L'angle actuel en radians.
     */
    public double getAngleActuel() {
        return angleActuel;
    }
}


// --- Nouvelle classe LayerUI pour appliquer la transformation ---

/**
 * Un LayerUI qui utilise un CardFlipAnimator pour appliquer une transformation 3D
 * à la vue qu'il enveloppe.
 */
class CardFlipLayerUI extends LayerUI<JComponent> { // Peut fonctionner avec n'importe quel JComponent

    private final CardFlipAnimator animator;

    /**
     * Crée un LayerUI qui appliquera l'animation.
     * @param animator L'animateur à utiliser.
     */
    public CardFlipLayerUI(CardFlipAnimator animator) {
        this.animator = animator;
    }

    /**
     * Intercepte et modifie le processus de peinture de la vue.
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        // c est le JComponent wrappé (la vue)

        Graphics2D g2d = (Graphics2D) g.create(); // Crée une copie du contexte graphique

        // Appliquer l'effet de qualité de rendu (optionnel)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        double angle = animator.getAngleActuel();
        double width = c.getWidth(); // Utilise la taille du composant wrappé
        double height = c.getHeight();

        // Calculer l'échelle dynamique (simule l'effet 3D)
        double facteurEchelle = 1.0 - 0.3 * Math.abs(Math.sin(angle));

        // Créer la transformation centrée sur le composant
        AffineTransform transform = new AffineTransform();
        transform.translate(width / 2.0, height / 2.0);
        transform.rotate(angle);
        transform.scale(facteurEchelle, 1.0);
        transform.translate(-width / 2.0, -height / 2.0);

        // Sauvegarder la transformation actuelle
        AffineTransform originalTransform = g2d.getTransform();

        // Appliquer la transformation
        g2d.transform(transform);

        // Peindre la vue wrappée avec la transformation appliquée
        super.paint(g2d, c); // Peint le composant 'c' en utilisant le contexte g2d modifié

        // Restaurer la transformation originale
        g2d.setTransform(originalTransform);

        g2d.dispose(); // Libère les ressources graphiques de la copie
    }

    // On pourrait aussi intercepter les événements souris/clavier ici si nécessaire,
    // mais pour déclencher l'animation, ajouter un ActionListener direct au bouton est plus simple.
}

// --- Classe Utilitaire pour appliquer l'animation ---

/**
 * Méthodes utilitaires pour appliquer des animations à des composants existants.
 */
class AnimationUtils {

    /**
     * Applique l'animation de retournement de carte 3D à un bouton existant.
     * Le bouton sera wrappé dans un JLayer.
     * @param button Le JButton auquel appliquer l'animation.
     * @return Le JLayer contenant le bouton animé. C'est ce JLayer qu'il faut ajouter au conteneur.
     */
    public static JLayer<JComponent> applyCardFlipAnimation(JButton button) {
        CardFlipAnimator animator = new CardFlipAnimator();
        CardFlipLayerUI layerUI = new CardFlipLayerUI(animator);
        JLayer<JComponent> layer = new JLayer<>(button, layerUI); // Wrap le bouton dans un JLayer

        // Ajoute l'ActionListener au bouton original pour déclencher l'animation
        button.addActionListener(e -> animator.startAnimation());

        // Ajoute un AnimationListener à l'animateur qui demande au JLayer de se repeindre
        // quand l'angle change.
        animator.addAnimationListener(() -> layer.repaint()); // La lambda capture la variable 'layer'

        // Il est souvent utile de rendre le bouton non opaque et non "filled" pour que le
        // fond du conteneur ou les autres éléments ne soient pas peints avant la transformation
        // si vous avez des effets complexes ou des images de fond.
        // button.setOpaque(false);
        // button.setContentAreaFilled(false); // Peut être nécessaire si l'effet masque le fond

        return layer; // Retourne le JLayer à ajouter au conteneur
    }

    // Vous pourriez ajouter d'autres méthodes utilitaires ici pour d'autres types d'animation
}


// --- Classe de Test ---

class TestApplyAnimationToExistingButton {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Exemple Appliquer Animation à Bouton Existant");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());

            // 1. Créer un bouton standard EXISTANT
//            JButton existingButton = new JButton("Bouton Standard (avant animation)");
//            existingButton.setFont(new Font("Arial", Font.BOLD, 14));
//            existingButton.setPreferredSize(new Dimension(250, 70));
//            existingButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Clique sur le bouton animé !")); // Un listener standard fonctionne toujours

            Path testImagePath = PATH_CARTE.resolve("TIGRE.png");
            BoutonCarte existingButton = new BoutonCarte(testImagePath);
            existingButton.setPreferredSize(new Dimension(200,100));
            existingButton.setToolTipText("TIGRE");




            // 2. Créer un autre bouton standard pour comparaison
            JButton standardButton = new JButton("Bouton Standard (sans animation)");
            standardButton.setFont(new Font("Arial", Font.BOLD, 14));
            standardButton.setPreferredSize(new Dimension(250, 70));


            // 3. Appliquer l'animation au bouton existant en utilisant la méthode utilitaire
            JLayer<JComponent> animatedLayer = AnimationUtils.applyCardFlipAnimation(existingButton);

            // 4. Ajouter le JLayer (qui contient le bouton animé) à la fenêtre
            frame.add(animatedLayer);

            // 5. Ajouter le bouton standard non animé pour comparaison
            frame.add(standardButton);


            frame.pack();
            frame.setSize(600, 200); // Ajuster la taille pour voir les deux boutons
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
