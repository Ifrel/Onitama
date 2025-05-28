package Vue.Utils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.logging.Logger;
import java.util.logging.Level;

import static Global.Config.COULEUR_CASE_TERRAIN;

public class PanelAvecImage extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(PanelAvecImage.class.getName());
    private Image image;
    private URL cheminImage;

    public PanelAvecImage(URL cheminImage) {
        this.cheminImage = cheminImage;
        init();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null && getWidth() > 0 && getHeight() > 0) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void setImage(URL cheminImage) {
        this.cheminImage = cheminImage;
        init();
        repaint();
    }

    private void init() {
        if (cheminImage == null) {
            image = null;
            setBackground(COULEUR_CASE_TERRAIN);
        } else {
            try {
                // Utilisation de ImageIO pour un chargement plus robuste
                image = new ImageIcon(cheminImage).getImage();

                // Vérification que l'image est bien chargée
                if (image.getWidth(this) <= 0) {
                    LOGGER.log(Level.WARNING, "Impossible de charger l'image: " + cheminImage);
                    image = null;
                    setBackground(COULEUR_CASE_TERRAIN);
                } else {
                    setOpaque(false);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erreur lors du chargement de l'image: " + cheminImage, e);
                image = null;
                setBackground(COULEUR_CASE_TERRAIN);
            }
        }
    }

    public URL getCheminImage() {
        return cheminImage;
    }

    public Image getImage() {
        return image;
    }

    public void clearImage() {
        image = null;
        cheminImage = null;
        setBackground(COULEUR_CASE_TERRAIN);
        repaint();
    }
}