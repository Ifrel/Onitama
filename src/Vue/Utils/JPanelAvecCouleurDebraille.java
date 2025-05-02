package Vue.Utils;

import javax.swing.*; // Importe les classes de base pour l'interface graphique Swing
import java.awt.*; // Importe les classes AWT pour le graphisme, les couleurs, etc.

/**
 * Un panneau (JPanel) personnalisé qui affiche un dégradé linéaire
 * entre deux couleurs spécifiées. Le dégradé va du coin supérieur gauche
 * (couleur1) au coin inférieur droit (couleur2).
 * Les composants ajoutés à ce panneau seront affichés par-dessus le dégradé.
 *
 */
public class JPanelAvecCouleurDebraille extends JPanel {
    private Color couleur1;
    private Color couleur2;


    /**
     * Crée un nouveau panneau avec un dégradé linéaire.
     *
     * @param couleur1 La couleur de départ du dégradé (en haut à gauche).
     * @param couleur2 La couleur de fin du dégradé (en bas à droite).
     */
    public JPanelAvecCouleurDebraille(Color couleur1, Color couleur2){
        this.couleur1 = couleur1;
        this.couleur2 = couleur2;
        // Par défaut, un JPanel est opaque. paintComponent effacera l'arrière-plan
        // avant de dessiner le dégradé, ce qui est le comportement souhaité ici.
        // Si on voulait dessiner *sous* des enfants transparents, il faudrait setOpaque(false).
        // Ici, le dégradé est le fond, donc opaque est correct (ou laisser par défaut).
        // setOpaque(true); // C'est la valeur par défaut, donc cette ligne n'est pas strictement nécessaire, mais peut clarifier l'intention.
    }


    /**
     * Peint le contenu du composant. Cette méthode est override pour dessiner
     * le dégradé de couleur en tant qu'arrière-plan du panneau.
     *
     * @param g L'objet Graphics utilisé pour dessiner.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Appeler super.paintComponent(g) d'abord permet au panneau de se peindre
        // normalement (ici, effacer l'arrière-plan avec sa couleur par défaut si opaque).
        // Bien que notre dégradé recouvre tout, c'est une bonne pratique.
        // Si le panneau était non-opaque, cet appel ne ferait pas d'effacement.
        super.paintComponent(g);

        // Crée un contexte Graphics2D à partir du Graphics original.
        // Graphics2D offre plus de fonctionnalités, notamment les dégradés (GradientPaint).
        Graphics2D g2 = (Graphics2D) g.create();

        // Récupère les dimensions actuelles du panneau.
        int width = getWidth();
        int height = getHeight();

        // Crée un objet GradientPaint pour définir le dégradé.
        // Le dégradé va du point (0, 0) utilisant couleur1 au point (width, height) utilisant couleur2.
        // Le dégradé s'étend proportionnellement sur toute la surface du panneau.
        GradientPaint gradient = new GradientPaint(0, 0, couleur1, width, height, couleur2);

        // Définit la "peinture" à utiliser pour les opérations de dessin suivantes dans g2.
        // Ici, on indique que les remplissages doivent utiliser notre dégradé.
        g2.setPaint(gradient);

        // Remplit le rectangle (0, 0, width, height) avec la peinture actuellement définie (le dégradé).
        // Cela couvre toute la surface du panneau.
        g2.fillRect(0, 0, width, height);

        // Libère les ressources graphiques allouées par g.create().
        // Très important pour éviter les fuites de ressources.
        g2.dispose();

        // paintBorder() et paintChildren() seront appelés par super.paintComponent(g)
        // après l'exécution de cette méthode, assurant que la bordure et les enfants
        // sont dessinés par-dessus le dégradé.
    }

    // Optionnel : Méthodes pour changer les couleurs dynamiquement, etc.
    // public void setCouleurs(Color c1, Color c2) { this.couleur1 = c1; this.couleur2 = c2; repaint(); }
    // public Color getCouleur1() { return couleur1; }
    // public Color getCouleur2() { return couleur2; }
}