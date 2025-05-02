package Vue.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Un panneau (JPanel) personnalisé qui affiche une image d'arrière-plan
 * redimensionnée pour s'adapter à la taille du panneau, sans déformer le contenu
 * ajouté par-dessus.
 *
 * L'image est étirée pour remplir complètement le panneau, potentiellement
 * au prix d'une déformation si les ratios d'aspect de l'image et du panneau diffèrent.
 */
public class PanelAvecImage extends JPanel {
    private Image image;

    /**
     * Crée un nouveau panneau avec une image d'arrière-plan.
     * L'image sera chargée depuis le chemin spécifié.
     *
     * @param cheminImage Le chemin d'accès au fichier image à utiliser (.jpg, .png, etc.).
     */
    public PanelAvecImage(String cheminImage) {
        this.image = new ImageIcon(cheminImage).getImage();
        setOpaque(false);
    }

    /**
     * Peint le contenu du composant. Cette méthode est appelée par le système graphique Swing
     * chaque fois que le panneau a besoin d'être redessiné (redimensionnement, recouvrement, etc.).
     *
     * @param g L'objet Graphics utilisé pour dessiner.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Appeler super.paintComponent(g) est une bonne pratique.
        // Puisque setOpaque(false) est défini, cette méthode ne va pas effacer l'arrière-plan,
        // mais elle s'occupe de préparer le contexte graphique et d'appeler paintBorder() et paintChildren().
        // En l'appelant APRES avoir dessiné l'image, on s'assure que l'image est peinte EN DESSOUS des enfants.
        super.paintComponent(g);

        // Vérifie si l'image a été chargée correctement (elle pourrait être null en cas d'erreur de chargement)
        // et si le panneau a une taille valide pour dessiner.
        if (image != null && getWidth() > 0 && getHeight() > 0) {
            // Dessiner l'image en fond.
            // g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer)
            // La version utilisée ici étire l'image (sx1..sy2 sont implicitement la taille de l'image source)
            // pour remplir le rectangle de destination (dx1=0, dy1=0, dx2=getWidth(), dy2=getHeight()).
            // Cela redimensionne l'image à la taille exacte du panneau, potentiellement en la déformant.
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }

        // paintBorder() et paintChildren() seront appelés par super.paintComponent(g)
        // après l'exécution de cette méthode si la chaîne de peinture par défaut n'est pas modifiée.
    }

    // Optionnel : Méthodes pour changer l'image dynamiquement, etc.
    // public void setImage(String cheminImage) { ... }
    // public Image getImage() { return image; }
}