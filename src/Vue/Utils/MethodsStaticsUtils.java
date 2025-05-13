package Vue.Utils;

import Vue.Animations.Animations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;

/**
 * Classe utilitaire regroupant des méthodes statiques pour la création
 * d'éléments d'interface utilisateur personnalisés. */
public class MethodsStaticsUtils {


    /**
     * Crée un bouton standard avec un texte donné.
     *
     * @param titre le texte à afficher sur le bouton
     * @return un JButton configuré     */
    public static JButton creerBouton(String titre) {
        JButton bouton = new JButton(titre);
        return bouton;
    }



    /**
     * Crée un JLabel stylisé pouvant être utilisé comme un onglet ou une étiquette.
     *
     * @param text le texte à afficher
     * @return un JLabel avec police et taille prédéfinies     */
    public static JLabel creerJPanel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 25)); // Police personnalisée
        label.setPreferredSize(new Dimension(160, 40));  // Taille fixe de l’onglet
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }


    public static JButton creerBoutonAvecImage(String cheminImage) {
        // Chargement de l'image d'origine
        ImageIcon iconeOriginale = new ImageIcon(cheminImage);

        // Création du bouton sans texte
        JButton bouton = new JButton();
        bouton.setContentAreaFilled(false);   // Fond désactivé par défaut
        bouton.setBorderPainted(false);       // Bordure désactivée par défaut
        bouton.setFocusPainted(false);
        bouton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Redimensionne l’image selon la taille du bouton
        Runnable miseAJourIcone = () -> {
            int largeur = bouton.getWidth();
            int hauteur = bouton.getHeight();
            if (largeur > 0 && hauteur > 0) {
                Image imageRedimensionnee = iconeOriginale.getImage().getScaledInstance(
                        largeur, hauteur, Image.SCALE_SMOOTH
                );
                bouton.setIcon(new ImageIcon(imageRedimensionnee));
            }
        };

        // Met à jour l’icône au premier affichage
        bouton.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && bouton.isShowing()) {
                SwingUtilities.invokeLater(miseAJourIcone);
            }
        });

        // Met à jour l’icône lors du redimensionnement
        bouton.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                miseAJourIcone.run();
            }
        });

        // Comportement naturel de survol et clic (bordure/fond visibles temporairement)
        bouton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bouton.setContentAreaFilled(true);
                bouton.setBorderPainted(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                bouton.setContentAreaFilled(false);
                bouton.setBorderPainted(false);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                bouton.setContentAreaFilled(true);
                bouton.setBorderPainted(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (bouton.contains(e.getPoint())) {
                    bouton.setContentAreaFilled(true);  // Si la souris est toujours dessus
                    bouton.setBorderPainted(true);
                } else {
                    bouton.setContentAreaFilled(false);
                    bouton.setBorderPainted(false);
                }
            }
        });

        return bouton;
    }


    public static JButton creerBoutonAvecImage(
            String cheminImage,
            float epaisseurBordure,
            float arrondiBordure,
            Color couleurBordure
    ) {
        ImageIcon iconeOriginale = new ImageIcon(cheminImage);

        JButton bouton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = (int) arrondiBordure;
                int width = getWidth();
                int height = getHeight();

                // Fond sur survol ou focus clavier
                if (getModel().isRollover() || isFocusOwner()) {
                    g2.setColor(new Color(200, 200, 200, 80)); // survol ou focus
                    g2.fillRoundRect(0, 0, width, height, arc, arc);
                }

                // Effet visuel du clic (ombrage plus fort)
                if (getModel().isPressed()) {
                    g2.setColor(new Color(150, 150, 150, 120));
                    g2.fillRoundRect(0, 0, width, height, arc, arc);
                }

                // Appelle le dessin standard (pour afficher l’icône)
                super.paintComponent(g2);

                // Bordure personnalisée
                g2.setColor(couleurBordure);
                g2.setStroke(new BasicStroke(epaisseurBordure));
                g2.drawRoundRect(
                        (int) (epaisseurBordure / 2),
                        (int) (epaisseurBordure / 2),
                        width - (int) epaisseurBordure,
                        height - (int) epaisseurBordure,
                        arc, arc
                );

                g2.dispose();
            }

            @Override
            public boolean isContentAreaFilled() {
                return false;
            }
        };

        bouton.setFocusPainted(true);
        bouton.setBorderPainted(false);
        bouton.setOpaque(false);
        bouton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bouton.setFocusable(true); // pour activer le focus clavier

        // Redimensionner l’icône automatiquement
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

        return bouton;
    }









    /**
     * Crée un bouton transparent avec une image d’arrière-plan personnalisée,
     * encapsulé dans un objet contenant également son panneau image.
     *
     * @param cheminImage le chemin vers l’image à utiliser en arrière-plan
     * @return un objet BoutonAvecImage contenant le JButton et le PanelAvecImage     */
    public static BoutonAvecImage creerBoutonAvecImage(Path cheminImage) {
        JButton bouton = new JButton();
        bouton.setBorderPainted(true);
        bouton.setFocusPainted(false);
        bouton.setContentAreaFilled(false);
        bouton.setOpaque(false);

        PanelAvecImage panel = new PanelAvecImage(cheminImage);
        bouton.add(panel);

        return new BoutonAvecImage(bouton, panel);
    }



    /**
     * Classe utilitaire pour encapsuler un bouton avec son panneau image
     * et une animation associée.     */
    public static class BoutonAvecImage {
        /** Le bouton Swing principal */
        public JButton bouton;

        /** Le panneau affichant l’image d’arrière-plan */
        public PanelAvecImage panel;

        /** Animation associée au bouton (peut être null) */
        public Animations animation;


        /**
         * Constructeur du bouton avec panneau image.
         * @param bouton le bouton à associer
         * @param panel  le panneau image utilisé en arrière-plan         */
        public BoutonAvecImage(JButton bouton, PanelAvecImage panel) {
            this.bouton = bouton;
            this.panel = panel;
            this.animation = null;
        }

        /**
         * Associe une animation au bouton.
         * @param animation l’objet animation à lier         */
        public void setAnimation(Animations animation) {
            this.animation = animation;
        }
    }
}
