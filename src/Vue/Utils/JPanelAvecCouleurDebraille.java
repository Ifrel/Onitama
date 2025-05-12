package Vue.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Un panneau personnalisé avec un fond en dégradé linéaire allant
 * du coin supérieur gauche (couleur1) au coin inférieur droit (couleur2).
 * Les composants ajoutés à ce panneau seront affichés par-dessus ce dégradé. */
public class JPanelAvecCouleurDebraille extends JPanel {

    private Color couleur1;
    private Color couleur2;


    /**
     * Construit un panneau avec un fond dégradé entre deux couleurs.
     *
     * @param couleur1 La couleur de départ (en haut à gauche).
     * @param couleur2 La couleur de fin (en bas à droite).     */
    public JPanelAvecCouleurDebraille(Color couleur1, Color couleur2) {
        this.couleur1 = couleur1;
        this.couleur2 = couleur2;
    }



    /**
     * Dessine le fond du panneau avec un dégradé linéaire.
     *
     * @param g L'objet Graphics utilisé pour le dessin.     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Efface le fond précédent si opaque

        Graphics2D g2 = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();

        GradientPaint gradient = new GradientPaint(0, 0, couleur1, width, height, couleur2);
        g2.setPaint(gradient);
        g2.fillRect(0, 0, width, height);

        g2.dispose(); // Libère les ressources
    }




    /**
     * Modifie dynamiquement les couleurs du dégradé.
     *
     * @param c1 Nouvelle couleur de départ.
     * @param c2 Nouvelle couleur de fin.     */
    public void setCouleurs(Color c1, Color c2) {
        this.couleur1 = c1;
        this.couleur2 = c2;
        repaint(); // Redessine le panneau avec les nouvelles couleurs
    }



    /**
     * @return La couleur de départ du dégradé.     */
    public Color getCouleur1() {
        return couleur1;
    }



    /**
     * @return La couleur de fin du dégradé.     */
    public Color getCouleur2() {
        return couleur2;
    }
}
