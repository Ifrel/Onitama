package Vue.LabO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class MonBoutonImage {

    private String cheminImage;
    private float epaisseurBordureInitiale;
    private float epaisseurBordureSurvol;
    private float arrondiBordure;
    private Color couleurBordure;
    private Color couleurFondSurvol;

    public enum ConfigurationParDefaut{
        Cercle,
        Cercle_transparnt,
        Carre,
        Carre_transparent,
        Rectangle,
        Rectangle_transparant
    }



    /**
     * Crée un bouton personnalisé avec une image, une bordure arrondie, un fond dynamique et une mise à l’échelle automatique de l’image.
     * @param cheminImage               Le chemin vers l’image à utiliser comme icône du bouton.
     * @param epaisseurBordureInitiale  L’épaisseur de la bordure à dessiner.
     * @param epaisseurBordureSurvol
     * @param arrondiBordure            Le rayon d’arrondi des coins de la bordure
     * @param couleurBordure
     * @param couleurFondSurvol
     * @return                          Un bouton personnalisé prêt à être utilisé dans une interface Swing.
     */
    public static JButton creerBoutonAvecImage(
            String cheminImage,
            float epaisseurBordureInitiale,
            float epaisseurBordureSurvol,
            float arrondiBordure,
            Color couleurBordure,
            Color couleurFondSurvol
    ) {


        ImageIcon iconeOriginale = new ImageIcon(cheminImage);

        // État dynamique de l’épaisseur de bordure
        final float[] epaisseurActuelle = {epaisseurBordureInitiale};

        JButton bouton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = (int) arrondiBordure;
                int width = getWidth();
                int height = getHeight();

                boolean survol = getModel().isRollover();
                boolean clique = getModel().isPressed();
                boolean focus = isFocusOwner();

                // Fond si survol ou focus
                if (survol || focus) {
                    g2.setColor(couleurFondSurvol);
                    g2.fillRoundRect(0, 0, width, height, arc, arc);
                }

                // Fond si cliqué
                if (clique) {
                    g2.setColor(new Color(100, 100, 100, 80));
                    g2.fillRoundRect(2, 2, width - 4, height - 4, arc, arc);
                }

                // Icône
                super.paintComponent(g2);

                // Bordure dynamique
                g2.setColor(couleurBordure);
                g2.setStroke(new BasicStroke(epaisseurActuelle[0]));
                g2.drawRoundRect(
                        (int) (epaisseurActuelle[0] / 2),
                        (int) (epaisseurActuelle[0] / 2),
                        width - (int) epaisseurActuelle[0],
                        height - (int) epaisseurActuelle[0],
                        arc, arc
                );

                g2.dispose();
            }

            @Override
            public boolean isContentAreaFilled() {
                return false;
            }
        };

        bouton.setFocusPainted(false);
        bouton.setBorderPainted(false);
        bouton.setOpaque(false);
        bouton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bouton.setFocusable(true);

        // Redimension de l’icône
        Runnable miseAJourIcone = () -> {
            int largeur = bouton.getWidth();
            int hauteur = bouton.getHeight();
            if (largeur > 0 && hauteur > 0) {
                Image imageRedim = iconeOriginale.getImage().getScaledInstance(
                        largeur, hauteur, Image.SCALE_SMOOTH
                );
                bouton.setIcon(new ImageIcon(imageRedim));
            }
        };

        bouton.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && bouton.isShowing()) {
                SwingUtilities.invokeLater(miseAJourIcone);
            }
        });

        bouton.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                miseAJourIcone.run();
            }
        });

        // Gère l’effet au survol
        bouton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                epaisseurActuelle[0] = epaisseurBordureSurvol;
                bouton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                epaisseurActuelle[0] = epaisseurBordureInitiale;
                bouton.repaint();
            }
        });

        return bouton;
    }





    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Bouton avec Image Personnalisé Amélioré");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());

            JButton boutonImage = MonBoutonImage.creerBoutonAvecImage(
                    "icone.png",     // Image
                    2f,              // Épaisseur normale
                    5f,              // Épaisseur au survol
                    100f,             // Arrondi
                    new Color(52, 127, 202),      // Couleur bordure
                    new Color(230, 240, 250) // Fond au survol
            );


            boutonImage.setPreferredSize(new Dimension(100, 100));

            frame.add(boutonImage);
            frame.setSize(300, 200);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
