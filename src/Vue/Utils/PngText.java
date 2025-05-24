package Vue.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

import javax.imageio.ImageIO;

import static Global.Paths.PATH_LBL_TXT;

/**
 * Affiche un texte sous forme d'images PNG personnalisées.
 * Si une image est absente, le caractère est affiché avec un JLabel standard.
 */
public class PngText {

    private static final Logger logger = Logger.getLogger(PngText.class.getName());
    private static final Map<String, BufferedImage> imageCache = new HashMap<>();


    /**
     * Crée un JPanel contenant des JLabel d'images PNG ou de texte en secours.
     *
     * @param text Texte à afficher
     * @param size Taille cible (largeur/hauteur des images ou taille police)
     * @return Panneau prêt à être inséré dans une interface
     */
    public static JPanel createPngPanel(String text, int size) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JPanel currentLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        currentLine.setOpaque(false);
        container.add(currentLine);

        for (char c : text.toCharArray()) {
            if (c == '\n') {
                // Saut de ligne → nouvelle ligne de JLabel
                currentLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                currentLine.setOpaque(false);
                container.add(currentLine);
                continue;
            }

            String filename = Character.toUpperCase(c) + ".png";
            String path = PATH_LBL_TXT.resolve(filename).toString();

            BufferedImage img = loadImage(path);
            JLabel label;

            if (img != null) {
                Image scaled = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
                label = new JLabel(new ImageIcon(addGlowEffect(toBufferedImage(scaled))));
            } else {
                logger.warning("Aucune image pour '" + c + "' — affichage texte par défaut.");
                label = createFallbackLabel(c, size);
            }

            currentLine.add(label);
        }

        return container;
    }


    /**
     * Charge une image depuis le disque ou depuis le cache.
     */
    private static BufferedImage loadImage(String path) {
        if (imageCache.containsKey(path)) return imageCache.get(path);

        try {
            BufferedImage img = ImageIO.read(new File(path));
            imageCache.put(path, img);
            logger.info("Image chargée : " + path);
            return img;
        } catch (IOException e) {
            logger.fine("Image non trouvée : " + path);
            return null;
        }
    }


    /**
     * Convertit une Image en BufferedImage.
     */
    private static BufferedImage toBufferedImage(Image img) {
        BufferedImage bImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bImage.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        return bImage;
    }


    /**
     * Ajoute un effet lumineux autour de l'image.
     */
    private static BufferedImage addGlowEffect(BufferedImage image) {
        int w = image.getWidth(), h = image.getHeight();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();

        for (int i = 1; i <= 3; i++) {
            g.drawImage(applyOpacity(image, 0.05f * (4 - i)), i, i, null);
            g.drawImage(applyOpacity(image, 0.05f * (4 - i)), -i, -i, null);
            g.drawImage(applyOpacity(image, 0.05f * (4 - i)), i, -i, null);
            g.drawImage(applyOpacity(image, 0.05f * (4 - i)), -i, i, null);
        }

        g.drawImage(image, 0, 0, null);
        g.dispose();
        return result;
    }

    /**
     * Applique une opacité personnalisée à une image.
     */
    private static BufferedImage applyOpacity(BufferedImage img, float alpha) {
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImg.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return newImg;
    }

    /**
     * Crée un JLabel simple pour afficher un caractère manquant en texte.
     */
    private static JLabel createFallbackLabel(char c, int size) {
        JLabel label = new JLabel(String.valueOf(c));
        label.setFont(new Font("Monospaced", Font.PLAIN, size));
        label.setForeground(Color.LIGHT_GRAY);
        label.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2)); // un peu d'espacement
        return label;
    }

}
