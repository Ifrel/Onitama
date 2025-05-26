package Vue;

import Vue.Animations.AnimationUtils.AnimationUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/*
 * =============================================================================
 * 6. Exemple d'Utilisation (Classe TestAnimationUtils)
 * =============================================================================
 *
 * Cette classe de test démontre comment utiliser la méthode utilitaire
 * AnimationUtils.applyCardFlipAnimation sur différents JButtons existants.
 * Exécutez la méthode main de cette classe pour voir les exemples en action.
 */

@DisplayName("TestAnimationUtils Tests")
class TestAnimationUtils {

    @Test
    public void test1() {
        // Exécute le code de l'interface graphique sur le Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Exemple Appliquer Animation à Bouton Existant");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // Utilise un FlowLayout pour simplement placer les boutons côte à côte
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Espacement horizontal et vertical

            // --- Créer des boutons standard existants ---

            // Bouton avec texte
            JButton textButton = new JButton("Retourner Moi !");
            textButton.setFont(new Font("Arial", Font.BOLD, 16));
            textButton.setPreferredSize(new Dimension(200, 70)); // Fixer une taille préférée

            // Bouton avec icône
            Icon icon = UIManager.getIcon("OptionPane.informationIcon"); // Icône Swing standard
            if (icon == null) {
                try {
                    // Tentative de charger une image depuis les ressources (ajuster le chemin si besoin)
                    // Cette partie dépend de votre structure de projet et où se trouvent vos images.
                    // Remplacez "/Vue/LabO/images/icon_test.png" par le chemin correct si vous avez une image.
                    URL imageUrl = TestAnimationUtils.class.getResource("/Vue/LabO/images/icon_test.png");
                    if (imageUrl != null) {
                        ImageIcon loadedIcon = new ImageIcon(imageUrl);
                        Image img = loadedIcon.getImage();
                        Image resizedImg = img.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
                        icon = new ImageIcon(resizedImg);
                    } else {
                        System.err.println("Ressource icône non trouvée. Création d'une icône de secours.");
                        // Créer une icône de secours simple (carré transparent)
                        icon = new ImageIcon(new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB));
                    }
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement/création de l'icône: " + e.getMessage());
                    icon = new ImageIcon(new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB)); // Icône transparente vide
                }
            }
            JButton iconButton = new JButton(icon);
            iconButton.setPreferredSize(new Dimension(70, 70));

            // Bouton avec texte et icône
            JButton textIconButton = new JButton("Animé", icon);
            textIconButton.setFont(new Font("Arial", Font.PLAIN, 12));
            textIconButton.setPreferredSize(new Dimension(150, 60));
            textIconButton.setHorizontalTextPosition(SwingConstants.CENTER); // Texte sous l'icône
            textIconButton.setVerticalTextPosition(SwingConstants.BOTTOM);


            // Bouton standard qui ne sera PAS animé, pour comparaison
            JButton standardButton = new JButton("Bouton Normal");
            standardButton.setFont(new Font("Arial", Font.PLAIN, 14));
            standardButton.setPreferredSize(new Dimension(200, 70));


            // --- Appliquer l'animation aux boutons existants ---

            // Applique l'animation au bouton texte
            JLayer<JButton> animatedTextLayer = AnimationUtils.applyCardFlipAnimation(textButton);

            // Applique l'animation au bouton icône
            JLayer<JButton> animatedIconLayer = AnimationUtils.applyCardFlipAnimation(iconButton);

            // Applique l'animation au bouton texte et icône
            JLayer<JButton> animatedTextIconLayer = AnimationUtils.applyCardFlipAnimation(textIconButton);


            // --- Ajouter les JLayer (qui contiennent les boutons animés) et le bouton standard à la fenêtre ---

            frame.add(animatedTextLayer);
            frame.add(animatedIconLayer);
            frame.add(animatedTextIconLayer);
            frame.add(standardButton); // Ajouter le bouton standard pour voir la différence


            // Configuration finale de la fenêtre
            frame.pack(); // Ajuste la taille de la fenêtre au contenu préféré
            frame.setSize(frame.getPreferredSize().width + 50, frame.getPreferredSize().height + 50); // Ajoute un peu d'espace
            frame.setLocationRelativeTo(null); // Centre la fenêtre
            frame.setVisible(true);           // Rend la fenêtre visible
        });
    }
}
