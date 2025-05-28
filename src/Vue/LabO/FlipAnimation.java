package Vue.LabO;

import Global.Paths;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface pour les listeners de l'animation de carte.
 * Permet de notifier qu'un rafraîchissement est nécessaire.
 */
interface AnimationListener {
    void animationUpdated();
}

/**
 * Gère la logique de l'animation de retournement d'une "carte" en 3D.
 * Cette classe est indépendante des composants graphiques et gère uniquement l'état de l'animation (l'angle).
 */
class CardFlipAnimator {
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
    public CardFlipAnimator() {
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
            // Si l'animation est déjà en cours, on "enchaîne" la rotation :
            // On recalcule la progression, on ajuste l'angle de départ et la cible pour garantir la vitesse.
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
        notifyAnimationUpdated(); // Notifie les listeners de se rafraîchir
    }

    /**
     * Fonction d'interpolation cosinusoïdale pour un easing naturel (ralentit aux début et fin).
     * @param t Progression entre 0.0 et 1.0
     * @return Valeur interpolée
     */
    private double interpoler(double t) {
        // Fonction smoothstep légèrement modifiée ou easing cosinusoïdal
        return 0.5 * (1 - Math.cos(Math.PI * t));
    }

    /**
     * Termine l'animation et met à jour l'état final.
     */
    private void terminerAnimation() {
        timer.stop();
        enAnimation = false;
        // S'assurer que l'angle final est un multiple de PI (face visible ou dos)
        angleActuel = Math.round(angleCible / Math.PI) * Math.PI;
        notifyAnimationUpdated(); // Notifie une dernière fois après avoir réglé l'angle final
    }

    /**
     * Obtient l'angle actuel de l'animation.
     * @return L'angle actuel en radians.
     */
    public double getAngleActuel() {
        return angleActuel;
    }
}

/**
 * Un JButton qui utilise CardFlipAnimator pour appliquer un effet de retournement 3D lors de son affichage.
 * L'animation est déclenchée par un clic sur le bouton.
 */
class AnimatedCardButton extends JButton implements AnimationListener {

    private final CardFlipAnimator animator;
    private final boolean showBack; // Indique si on montre le "dos" quand angle ~ PI, 3PI, etc.

    /**
     * Crée un AnimatedCardButton.
     * @param text Le texte du bouton.
     * @param showBack True si le bouton doit simuler l'affichage du dos (pas utilisé dans cet exemple simple,
     * mais utile si vous aviez une image différente pour le dos).
     */
    public AnimatedCardButton(String text, boolean showBack) {
        super(text);
        this.showBack = showBack;
        animator = new CardFlipAnimator();
        animator.addAnimationListener(this); // Le bouton s'enregistre comme listener
        addActionListener(e -> animator.startAnimation()); // Le clic déclenche l'animation
        setOpaque(false); // Permet de voir l'effet de transformation sur le fond si le parent n'est pas opaque
        setContentAreaFilled(false); // Important pour voir l'effet de transformation sur la zone de contenu
    }

    public AnimatedCardButton(Icon icon, boolean showBack) {
        super(icon);
        this.showBack = showBack;
        animator = new CardFlipAnimator();
        animator.addAnimationListener(this);
        addActionListener(e -> animator.startAnimation());
        setOpaque(false);
        setContentAreaFilled(false);
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create(); // Crée une copie du contexte graphique

        // Appliquer l'effet de qualité de rendu (optionnel mais recommandé)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        double angle = animator.getAngleActuel();
        double width = getWidth();
        double height = getHeight();

        // Calculer l'échelle dynamique (simule l'effet 3D de rétrécissement au centre)
        double facteurEchelle = 1.0 - 0.3 * Math.abs(Math.sin(angle)); // Rétrécit jusqu'à 30% au maximum de rotation

        // Créer la transformation centrée sur le bouton
        AffineTransform transform = new AffineTransform();
        transform.translate(width / 2.0, height / 2.0); // Déplacer l'origine au centre du bouton
        transform.rotate(angle);                         // Appliquer la rotation
        transform.scale(facteurEchelle, 1.0);            // Appliquer la mise à l'échelle (seulement sur l'axe X local après rotation)
        transform.translate(-width / 2.0, -height / 2.0); // Revenir à l'origine du bouton

        // Sauvegarder la transformation actuelle pour la restaurer plus tard
        AffineTransform originalTransform = g2d.getTransform();

        // Appliquer la transformation
        g2d.transform(transform);

        // Déterminer si le "dos" devrait être visible (angles près de PI, 3*PI, etc.)
        // Dans cet exemple simple, on ne change pas le contenu, mais on pourrait
        // dessiner une image différente ou rien du tout ici en fonction de 'showBack'
        // et de l'angle (par exemple, si angle est proche de PI, on dessine l'image du dos).
        boolean isFlipped = Math.abs(Math.sin(angle)) < 0.1 && (Math.cos(angle) < 0); // Proche de PI, 3PI, etc.

        // Si vous aviez une image de dos, vous la dessinerez ici quand isFlipped est vrai
        // et si showBack est vrai. Sinon, vous laissez paintComponent dessiner le contenu standard.

        // Peindre le contenu normal du bouton (texte, icône, fond si opaque, bordure)
        // avec la transformation appliquée.
        super.paintComponent(g2d);

        // Restaurer la transformation originale
        g2d.setTransform(originalTransform);

        g2d.dispose(); // Libère les ressources graphiques de la copie
    }

    /**
     * Implémentation de AnimationListener. Appelée par CardFlipAnimator
     * lorsque l'angle de l'animation change.
     */
    @Override
    public void animationUpdated() {
        // Demander un rafraîchissement du bouton pour qu'il se redessine avec le nouvel angle.
        repaint();
    }

    // Vous pouvez ajouter d'autres méthodes pour configurer l'animateur si nécessaire,
    // par exemple, pour changer la durée de l'animation.
}





// Classe de test pour démontrer l'utilisation
class TestAnimatedButton {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Exemple Bouton Animé");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());

            // Exemple d'utilisation avec du texte
            AnimatedCardButton boutonTexte = new AnimatedCardButton("Cliquez pour retourner", false);
            boutonTexte.setFont(new Font("Arial", Font.BOLD, 16));
            boutonTexte.setPreferredSize(new Dimension(200, 80)); // Définir une taille préférée

            // Exemple d'utilisation avec une icône (assurez-vous d'avoir une image valide)
            try {
                // Remplacez "chemin/vers/votre/image.png" par un vrai chemin d'accès ou une ressource
                BufferedImage img = ImageIO.read(Paths.getCartePath("TIGRE.png").openStream());
                 Icon icon = new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH)); // Redimensionner l'icône si besoin
                 AnimatedCardButton boutonImage = new AnimatedCardButton(icon, false);
                 boutonImage.setPreferredSize(new Dimension(80, 80)); // Définir une taille pour l'icône

                // Ajout d'un bouton simple pour le test sans image externe
                AnimatedCardButton boutonSimple = new AnimatedCardButton("Animer", false);
                boutonSimple.setPreferredSize(new Dimension(100, 50));

                frame.add(boutonTexte);
                 frame.add(boutonImage); // Ajouter le bouton image si vous l'utilisez
                frame.add(boutonSimple);


            } catch (Exception ex) {
                System.err.println("Impossible de charger l'image pour le bouton icône: " + ex.getMessage());
                // Gérer l'erreur ou simplement ne pas ajouter le bouton image
                // Si l'image ne se charge pas, ajoutez juste le bouton texte
                AnimatedCardButton boutonSimple = new AnimatedCardButton("Animer", false);
                boutonSimple.setPreferredSize(new Dimension(100, 50));
                frame.add(boutonSimple); // Assurez-vous qu'il y a au moins un bouton
            }


            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}