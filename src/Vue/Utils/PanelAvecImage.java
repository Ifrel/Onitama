package Vue.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;

import static Global.Config.COULEUR_CASE_TERRAIN;



/**
 * Un panneau (JPanel) personnalisé qui affiche une image d'arrière-plan
 * redimensionnée pour s'adapter à la taille du panneau, sans déformer le contenu
 * ajouté par-dessus.
 *
 * L'image est étirée pour remplir complètement le panneau, potentiellement
 * au prix d'une déformation si les ratios d'aspect de l'image et du panneau diffèrent. */
public class PanelAvecImage extends JPanel {
    private Image image;
    private Path cheminImage;


    /**
     * Crée un nouveau panneau avec une image d'arrière-plan.
     * L'image sera chargée depuis le chemin spécifié.
     *
     * @param cheminImage Le chemin d'accès au fichier image à utiliser (.jpg, .png, etc.).     */
     public PanelAvecImage(Path cheminImage) {
        this.cheminImage = cheminImage;
        init();
     }



    /**
     * Peint le contenu du composant. Cette méthode est appelée par le système graphique Swing
     * chaque fois que le panneau a besoin d'être redessiné (redimensionnement, recouvrement, etc.).
     *
     * @param g L'objet Graphics utilisé pour dessiner.     */
     @Override
     protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null && getWidth() > 0 && getHeight() > 0) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }

     }



     public void setImage(Path cheminImage) {
        this.cheminImage = cheminImage;
        init();
        repaint();
     }





     private void init(){
        if(cheminImage == null) {
            image = null;
            setBackground(COULEUR_CASE_TERRAIN);
        }
        else {
            image = new ImageIcon(cheminImage.toString()).getImage();
            setOpaque(false);
        }
     }
}