package Vue.Animations.AnimationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;


/**
 * Méthodes utilitaires pour appliquer l'animation de retournement de carte
 * à des composants Swing existants, notamment des JButtons.
 * Cette classe est la seule classe publique dans ce fichier.
 */
public class AnimationUtils {

    /**
     * Applique l'animation de retournement de carte 3D à un JButton existant.
     * Le bouton est enveloppé dans un JLayer, et cet JLayer est configuré
     * pour se mettre à jour en fonction d'un CardFlipAnimator.
     *
     * @param button Le JButton auquel appliquer l'animation. Ce bouton ne doit PAS
     * avoir été ajouté à un conteneur avant cet appel.
     * @return Le JLayer contenant le bouton animé. C'est CE JLayer qui doit
     * être ajouté au conteneur à la place du bouton original.
     */
    public static JLayer<JButton> applyCardFlipAnimation(JButton button) {
        // 1. Créer un nouvel animateur pour ce bouton
        CardFlipAnimator animator = new CardFlipAnimator();

        // 2. Créer un LayerUI qui utilisera cet animateur
        CardFlipLayerUI<JButton> layerUI = new CardFlipLayerUI<>(animator);

        // 3. Créer un JLayer, enveloppant le bouton original avec le LayerUI
        JLayer<JButton> layer = new JLayer<>(button, layerUI);

        // 4. Ajouter un écouteur d'action au bouton original pour démarrer l'animation
        // Lorsque le bouton est cliqué, l'animateur démarre.
        button.addActionListener(e -> animator.startAnimation());

        // 5. Ajouter un écouteur d'animation à l'animateur
        // Chaque fois que l'animateur met à jour son angle, il notifie ce listener
        // qui demande alors au JLayer de se repeindre.
        animator.addAnimationListener(() -> layer.repaint()); // Lambda capture 'layer'

        // Facultatif mais souvent utile : rendre le bouton non opaque/non "filled"
        // pour permettre à la transformation d'affecter toutes les zones de rendu,
        // y compris potentiellement un fond ou une image sous-jacente.
        // button.setOpaque(false);
        // button.setContentAreaFilled(false);

        // Retourne le JLayer prêt à être ajouté à un conteneur
        return layer;
    }

    // Note: Si vous vouliez appliquer l'animation à un autre type de JComponent,
    // vous adapteriez le paramètre et le type générique de JLayer/LayerUI,
    // par exemple: JLayer<JPanel> applyPanelAnimation(JPanel panel) {...}
}


/*
 * =============================================================================
 * 6. Exemple d'Utilisation (Classe TestAnimationUtils)
 * =============================================================================
 *
 * Cette classe de test démontre comment utiliser la méthode utilitaire
 * AnimationUtils.applyCardFlipAnimation sur différents JButtons existants.
 * Exécutez la méthode main de cette classe pour voir les exemples en action.
 */
class TestAnimationUtils {

    public static void main(String[] args) {
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
