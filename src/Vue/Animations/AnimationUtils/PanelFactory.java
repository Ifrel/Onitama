package Vue.Animations.AnimationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelFactory {

    public static JPanel creerPanelArrondiInteractif(Color fondNormal, Color fondHover, Color fondClic,
                                                     int arondi, Color couleurBordure, int epaisseurBordure,
                                                     boolean estCarre) { // Nouveau paramètre
        // Utilisation d'une variable finale effective pour y accéder depuis la classe anonyme
        // Nous créons une instance de notre classe anonyme JPanel
        // pour pouvoir appeler sa méthode setFondActuel.
        // Pour cela, nous devons la déclarer explicitement ou utiliser une astuce.
        // Ici, nous allons la stocker dans une variable pour y faire référence.

        // Déclarons une classe interne nommée ou utilisons une approche où le panel
        // peut se référer à lui-même de manière typée.
        // Pour simplifier, nous allons rendre 'panel' accessible aux listeners
        // et utiliser une méthode pour changer la couleur.

        // Création du JPanel personnalisé
        // Pour pouvoir appeler setFondActuel depuis les listeners,
        // il faut que le panel soit une instance d'une classe qui expose cette méthode.
        // La classe anonyme actuelle le fait, mais pour y accéder depuis les listeners,
        // il faut une référence au type correct.
        // Une solution simple est de rendre la variable 'panel' finale effective et d'y accéder.

        // Note: La méthode setFondActuel est déjà dans la classe anonyme.
        // Donc, 'panel.setFondActuel(...)' fonctionnera si 'panel' est la référence
        // à l'instance de cette classe anonyme.

        // Définition du panel avec une méthode pour changer la couleur de fond
        // Il est important de noter que la variable 'panel' ci-dessous
        // doit être "effectively final" pour être utilisée dans les classes anonymes (MouseAdapter).
        // Pour appeler une méthode spécifique de notre panel personnalisé (comme setFondActuel),
        // nous aurions besoin d'une référence typée.
        // Dans ce cas, 'panel' sera l'instance correcte.

        // Pour que les MouseListeners puissent appeler panel.setFondActuel,
        // la variable 'panel' doit être déclarée finale ou être "effectively final".
        // On va donc déclarer le panel en dehors de son assignation directe pour
        // qu'il soit accessible dans les listeners.

        // On ne peut pas faire "panel = new JPanel() { ... panel.setFondActuel() ... }" directement.
        // On va créer une classe interne ou une variable qui peut stocker le panel.
        // Solution: Utiliser une référence au panel lui-même au sein du MouseAdapter,
        // mais il faut s'assurer que c'est bien notre panel personnalisé.

        class InteractiveRoundedPanel extends JPanel {
            private Color fondActuel = fondNormal;
            private final boolean makeSquare = estCarre; // Stocker la valeur de estCarre
            private final int borderRadius = arondi;
            private final Color borderColor = couleurBordure;
            private final int borderThickness = epaisseurBordure;

            public InteractiveRoundedPanel() {
                super();
                setOpaque(false); // Important pour la transparence et le dessin personnalisé
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int originalW = getWidth();
                int originalH = getHeight();
                int w = originalW;
                int h = originalH;
                int x = 0;
                int y = 0;

                if (makeSquare) {
                    int side = Math.min(originalW, originalH);
                    w = side;
                    h = side;
                    x = (originalW - side) / 2; // Centrer horizontalement
                    y = (originalH - side) / 2; // Centrer verticalement
                }

                g2.setColor(fondActuel);
                g2.fillRoundRect(x, y, w, h, borderRadius, borderRadius);

                if (borderColor != null && borderThickness > 0) {
                    g2.setColor(borderColor);
                    g2.setStroke(new BasicStroke(borderThickness));
                    // Ajustement pour que la bordure soit bien positionnée
                    int halfThickness = borderThickness / 2;
                    g2.drawRoundRect(x + halfThickness, y + halfThickness,
                            w - borderThickness, h - borderThickness,
                            borderRadius, borderRadius);
                }
                g2.dispose();
            }

            // isOpaque est déjà surchargé pour retourner false par défaut dans le code original
            // mais il est bon de le garder explicite si on veut un fond transparent.
            // Si le fond du panel parent doit être visible, c'est nécessaire.
            // Surcharger isOpaque() n'est pas toujours nécessaire si setOpaque(false) est appelé.
            // Cependant, pour un dessin personnalisé complet, c'est une bonne pratique.
            @Override
            public boolean isOpaque() {
                return false;
            }

            public void setFondActuel(Color c) {
                if (this.fondActuel != c) { // Optimisation : redessiner seulement si la couleur change
                    this.fondActuel = c;
                    repaint();
                }
            }
        }

        InteractiveRoundedPanel panel = new InteractiveRoundedPanel();

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setFondActuel(fondHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setFondActuel(fondNormal);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                panel.setFondActuel(fondClic);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Si la souris est toujours sur le composant, remettre la couleur hover,
                // sinon la couleur normale.
                if (panel.getBounds().contains(e.getPoint())) {
                    panel.setFondActuel(fondHover);
                } else {
                    panel.setFondActuel(fondNormal);
                }
            }
        });

        return panel;
    }

    // Exemple d'utilisation
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Panel Arrondi et Carré");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new GridLayout(2, 2, 10, 10)); // Pour voir différents panels

            Color fondNormal = new Color(70, 130, 180); // SteelBlue
            Color fondHover = new Color(100, 149, 237); // CornflowerBlue
            Color fondClic = new Color(30, 144, 255);   // DodgerBlue
            Color bordure = Color.BLACK;

            JPanel panelRectArrondi = creerPanelArrondiInteractif(
                    fondNormal, fondHover, fondClic, 30, bordure, 3, false);
            panelRectArrondi.setPreferredSize(new Dimension(200, 100));

            JPanel panelCarreArrondi = creerPanelArrondiInteractif(
                    fondNormal.darker(), fondHover.darker(), fondClic.darker(), 50, Color.DARK_GRAY, 5, true);
            panelCarreArrondi.setPreferredSize(new Dimension(150, 150)); // La taille pref est carrée

            JPanel panelCarreDansRect = creerPanelArrondiInteractif(
                    new Color(255,160,122), new Color(255,127,80), new Color(255,99,71),
                    20, Color.WHITE, 2, true);
            panelCarreDansRect.setPreferredSize(new Dimension(200, 100)); // Sera un carré dans cet espace

            JPanel panelPetitCarre = creerPanelArrondiInteractif(
                    new Color(152,251,152), new Color(144,238,144), new Color(60,179,113),
                    10, null, 0, true); // Pas de bordure
            panelPetitCarre.setPreferredSize(new Dimension(80, 120));


            frame.add(new JLabel("Panel Rectangulaire Arrondi:", SwingConstants.CENTER));
            frame.add(panelRectArrondi);
            frame.add(new JLabel("Panel Carré Arrondi (taille pref carrée):", SwingConstants.CENTER));
            frame.add(panelCarreArrondi);
            frame.add(new JLabel("Panel Carré dans un espace Rectangulaire:", SwingConstants.CENTER));
            frame.add(panelCarreDansRect);
            frame.add(new JLabel("Panel Petit Carré (pas de bordure):", SwingConstants.CENTER));
            frame.add(panelPetitCarre);


            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
