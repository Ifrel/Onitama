package Vue.LabO;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * {@code AnimationCaseCible} est une classe personnalisée héritant de {@link JButton}
 * qui affiche une icône et présente une bordure animée. L'animation de la bordure
 * (épaisseur et couleur) peut être activée ou désactivée.
 * Le bouton change également d'apparence au survol de la souris et lorsqu'il est pressé.
 */
public class AnimationCaseCible2 extends JButton {

    // Constantes pour l'animation et l'apparence
    private static final int TIMER_DELAY_MS = 50;                           // Délai du timer en millisecondes
    private static final float ANIMATION_PHASE_INCREMENT = 0.2f;            // Incrément de la phase d'animation
    private static final float BORDER_THICKNESS_ANIMATION_AMPLITUDE = 2.0f; // Amplitude de variation de l'épaisseur
    private static final Color PRESSED_BACKGROUND_COLOR = new Color(130, 130, 130, 150); // Couleur de fond lorsque pressé

    // Propriétés finales définies à la construction
    private final float epaisseurInitiale;      // Épaisseur initiale de la bordure
    private final float arrondiBordure;         // Rayon de l'arrondi des coins de la bordure
    private final Color couleurInitiale;        // Couleur initiale de la bordure (lorsque non animée)
    private Color couleurFondSurvol;            // Couleur de fond lorsque la souris survole le bouton

    // Propriétés d'état pour l'animation
    private float epaisseurAnimee;              // Épaisseur actuelle de la bordure (peut changer pendant l'animation)
    private Color couleurAnimee;                // Couleur actuelle de la bordure (peut changer pendant l'animation)
    private boolean animationActivee = false;   // Indique si l'animation de la bordure est active
    private float phase = 0;                    // Phase actuelle de l'animation (utilisée pour les calculs sinusoïdaux)

    private final Timer timer;                  // Timer pour gérer les étapes de l'animation

    /**
     * Construit un nouveau {@code AnimationCaseCible}.
     *
     * @param icone             L'icône à afficher sur le bouton.
     * @param epaisseurBordure  L'épaisseur initiale de la bordure.
     * @param arrondi           Le rayon de l'arrondi pour les coins du bouton et de sa bordure.
     * @param couleurBordure    La couleur initiale de la bordure.
     * @param fondSurvol        La couleur de fond du bouton lorsque la souris le survole.
     */
    public AnimationCaseCible2(ImageIcon icone, float epaisseurBordure, float arrondi, Color couleurBordure, Color fondSurvol) {
        super(icone);

        // Initialisation des propriétés
        this.epaisseurInitiale = epaisseurBordure;
        this.epaisseurAnimee = epaisseurBordure; // Commence avec l'épaisseur initiale
        this.arrondiBordure = arrondi;
        this.couleurInitiale = couleurBordure;
        this.couleurAnimee = couleurBordure; // Commence avec la couleur initiale
        this.couleurFondSurvol = fondSurvol;

        // Configuration de l'apparence de base du JButton
        setOpaque(false); // Permet de dessiner notre propre fond arrondi
        setFocusPainted(false); // Ne pas dessiner l'indicateur de focus standard
        setBorderPainted(false); // Ne pas dessiner la bordure standard
        setContentAreaFilled(false); // Important pour que setOpaque(false) fonctionne bien et pour le survol
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Curseur main au survol

        // Initialisation du Timer pour l'animation de la bordure
        this.timer = new Timer(TIMER_DELAY_MS, e -> {
            phase += ANIMATION_PHASE_INCREMENT;
            // L'épaisseur oscille autour de l'épaisseur initiale
            epaisseurAnimee = epaisseurInitiale + (float) (Math.sin(phase) * BORDER_THICKNESS_ANIMATION_AMPLITUDE);
            // La composante rouge de la couleur animée oscille, les autres restent fixes
            couleurAnimee = new Color(
                    (int) (Math.abs(Math.sin(phase)) * 255), // Rouge varie de 0 à 255
                    couleurInitiale.getGreen(),
                    couleurInitiale.getBlue(),
                    couleurInitiale.getAlpha() // Conserver l'alpha original
            );
            repaint(); // Redessiner le bouton pour refléter les changements
        });

        // Ajout d'un ActionListener pour activer/désactiver l'animation au clic
        addActionListener(e -> toggleAnimationBordure());
    }

    /**
     * Active ou désactive l'animation de la bordure.
     * Si l'animation est activée, le timer démarre.
     * Si elle est désactivée, le timer s'arrête et la bordure reprend son apparence initiale.
     *
     * @param activer {@code true} pour activer l'animation, {@code false} pour la désactiver.
     */
    public void setAnimationBordureActivee(boolean activer) {
        this.animationActivee = activer;
        if (activer) {
            if (!timer.isRunning()) {
                phase = 0; // Réinitialiser la phase pour commencer l'animation proprement
                timer.start();
            }
        } else {
            if (timer.isRunning()) {
                timer.stop();
            }
            // Réinitialiser l'apparence à l'état initial non animé
            epaisseurAnimee = epaisseurInitiale;
            couleurAnimee = couleurInitiale;
            repaint();
        }
    }


    /**
     * Inverse l'état actuel de l'animation de la bordure.
     */
    public void toggleAnimationBordure() {
        setAnimationBordureActivee(!this.animationActivee);
    }


    /**
     * Vérifie si l'animation de la bordure est actuellement active.
     *
     * @return {@code true} si l'animation est active, {@code false} sinon.
     */
    public boolean isAnimationBordureActivee() {
        return animationActivee;
    }



    /**
     * Méthode principale de dessin du composant.
     * S'occupe de dessiner le fond (survol, pressé) et la bordure animée.
     *
     * @param g L'objet {@link Graphics} fourni par le système de peinture.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        // Activer l'antialiasing pour des bords plus lisses
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arc = (int) arrondiBordure; // Conversion en int pour les méthodes drawRoundRect/fillRoundRect

        // Dessiner le fond si la souris est dessus ou si le bouton a le focus
        if (getModel().isRollover() || isFocusOwner()) {
            g2.setColor(couleurFondSurvol);
            g2.fillRoundRect(0, 0, width, height, arc, arc);

        }

        // Dessiner un effet de fond différent si le bouton est pressé
        if (getModel().isPressed()) {
            g2.setColor(PRESSED_BACKGROUND_COLOR);
            // Léger décalage pour un effet d'enfoncement
            g2.fillRoundRect(1, 1, width - 2, height - 2, arc, arc);
        }

        // Laisser JButton dessiner l'icône et le texte (si présent) par-dessus notre fond
        super.paintComponent(g2);

        // Dessiner la bordure
        // L'épaisseur de la stroke affecte comment drawRoundRect est dessiné (centré sur le chemin)
        g2.setStroke(new BasicStroke(epaisseurAnimee));
        g2.setColor(animationActivee ? couleurAnimee : couleurInitiale);

        // Ajuster les coordonnées et dimensions pour que la bordure soit visible et bien placée
        // en tenant compte de son épaisseur.
        int offset = (int) (epaisseurAnimee / 2);
        g2.drawRoundRect(
                offset,
                offset,
                width - (int) epaisseurAnimee,
                height - (int) epaisseurAnimee,
                arc, arc
        );



        g2.dispose(); // Toujours libérer les ressources graphiques créées
    }




    /**
     * Retourne l'épaisseur initiale de la bordure.
     * @return L'épaisseur initiale.
     */
    public float getEpaisseurInitiale() {
        return epaisseurInitiale;
    }

    /**
     * Retourne le rayon de l'arrondi des coins.
     * @return Le rayon de l'arrondi.
     */
    public float getArrondiBordure() {
        return arrondiBordure;
    }

    /**
     * Retourne la couleur initiale de la bordure (lorsque non animée).
     * @return La couleur initiale de la bordure.
     */
    public Color getCouleurInitiale() {
        return couleurInitiale;
    }

    /**
     * Retourne la couleur de fond utilisée lorsque la souris survole le bouton.
     * @return La couleur de fond au survol.
     */
    public Color getCouleurFondSurvol() {
        return couleurFondSurvol;
    }

    /**
     * Retourne l'épaisseur actuelle de la bordure (peut être en cours d'animation).
     * @return L'épaisseur animée actuelle.
     */
    public float getEpaisseurAnimee() {
        return epaisseurAnimee;
    }

    /**
     * Retourne la couleur actuelle de la bordure (peut être en cours d'animation).
     * @return La couleur animée actuelle.
     */
    public Color getCouleurAnimee() {
        return couleurAnimee;
    }

    // --- Mutateurs (Setters) ---

    /**
     * Définit la couleur de fond utilisée lorsque la souris survole le bouton.
     * @param couleurFondSurvol La nouvelle couleur de fond au survol.
     */
    public void setCouleurFondSurvol(Color couleurFondSurvol) {
        this.couleurFondSurvol = couleurFondSurvol;
        repaint(); // Redessiner si la couleur change alors que le bouton est survolé
    }

    /**
     * Méthode utilitaire pour basculer l'animation via l'action listener interne.
     * Renommée de `activerAnimationBordure(boolean activer)` à `setAnimationBordureActivee(boolean activer)`
     * et ajout d'une méthode `toggle` pour plus de clarté.
     *
     * Note: L'ancienne méthode `activerAnimationBordure(boolean)` a été renommée en
     * `setAnimationBordureActivee(boolean)` pour suivre les conventions de nommage Java
     * pour les setters.
     */

    // Exemple d'utilisation (peut être placé dans une classe Main)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test MonBoutonAnime2");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Pour espacer les boutons
            frame.getContentPane().setBackground(Color.DARK_GRAY);

            // Essayer de charger une icône (remplacez par un chemin valide ou laissez null)
            ImageIcon testIcon = null;
            try {
                // Tentez de charger une icône depuis le classpath.
                // Par exemple, si vous avez une image "icon.png" dans un dossier "resources"
                // qui est dans le classpath.
                // testIcon = new ImageIcon(MonBoutonAnime2.class.getResource("/icon.png"));

                // Sinon, pour un test rapide, on peut créer une icône simple :
                Image image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
                Graphics g = image.getGraphics();
                g.setColor(Color.WHITE);
                g.fillOval(5, 5, 22, 22);
                g.setColor(Color.BLACK);
                g.drawString("!", 13, 22);
                g.dispose();
                testIcon = new ImageIcon(image);

            } catch (Exception e) {
                System.err.println("Impossible de charger l'icône de test : " + e.getMessage());
            }

            AnimationCaseCible2 bouton1 = new AnimationCaseCible2(
                    testIcon,
                    2f,
                    20f,
                    Color.CYAN,
                    new Color(80, 80, 80, 150)
            );
            bouton1.setToolTipText("Cliquez pour animer/arrêter la bordure cyan");

            AnimationCaseCible2 bouton2 = new AnimationCaseCible2(
                    testIcon, // Réutiliser l'icône ou utiliser une autre
                    3f,
                    10f,
                    Color.MAGENTA,
                    new Color(Color.DARK_GRAY.brighter().getRed(), Color.DARK_GRAY.brighter().getGreen(), Color.DARK_GRAY.brighter().getBlue(), 100)
            );
            bouton2.setToolTipText("Cliquez pour animer/arrêter la bordure magenta");
            bouton2.setAnimationBordureActivee(true); // Démarrer ce bouton avec l'animation active

            frame.add(bouton1);
            frame.add(bouton2);

            frame.setSize(300, 200);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

