package Vue.Utils;

import Global.Config.*;
import Global.Config.ROLEPION.*;
import Global.Config.ROLEPION;
import Modele.Carte;
import Modele.CasePlateau;
import Modele.Pion;
import Vue.Animations.Animations;
import Vue.EcranPlateauDeJeu.TYPE_ELEMENT_SUR_TERRAIN;
import Vue.InfosDeConfigUI;
import Vue.Utils.Boutons.Bouton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;

import static Global.Config.ID_JOUEUR_1;
import static Global.Config.ID_JOUEUR_2;
import static Global.Paths.*;
import static Modele.CasePlateau.TYPE_ELEMENT_SUR_CASE.VIDE;

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



    public static Path getCheminImagePion(InfosDeConfigUI infosDeConfigUI, TYPE_ELEMENT_SUR_TERRAIN type) {
        if (type == TYPE_ELEMENT_SUR_TERRAIN.VIDE) {
            return null; // ou retourne un chemin vers une image "vide" si besoin
        }

        String nomCouleurPion;
        String role;
        int numJoueur;

        switch (type) {
            case PION_ETUDIANT_J1:
                nomCouleurPion = infosDeConfigUI.getNomCouleurPionJoueur(1);
                role = "etudiant";
                numJoueur = ID_JOUEUR_1;
                break;
            case PION_ETUDIANT_J2:
                nomCouleurPion = infosDeConfigUI.getNomCouleurPionJoueur(2);
                role = "etudiant";
                numJoueur = ID_JOUEUR_2;
                break;
            case PION_MAITRE_J1:
                nomCouleurPion = infosDeConfigUI.getNomCouleurPionJoueur(1);
                role = "maitre";
                numJoueur = ID_JOUEUR_1;
                break;
            case PION_MAITRE_J2:
                nomCouleurPion = infosDeConfigUI.getNomCouleurPionJoueur(2);
                role = "maitre";
                numJoueur = ID_JOUEUR_2;
                break;
            default: throw new IllegalArgumentException("Type de pion inconnu : " + type);
        }

        String nomFichier = "pion_" + role + "_" + nomCouleurPion + ".png";
        return PATH_DEBUT_PION.resolve(nomFichier);
    }



    public static Path getCheminImagePionClique(Path cheminImageActuelle){
        return Path.of(cheminImageActuelle.toString().split(".png")[0] + "_clique.png");
    }


    public static Path getCheminImageCarte(Carte carte){
        Path path = PATH_CARTE.resolve(carte.getNom() +".png");
//        System.err.println(path);
        return path;
    }



    public static JPanel creerPanelArrondiInteractif(Color fondNormal, Color fondHover, Color fondClic,
                                                     int arondi, Color couleurBordure, int epaisseurBordure) {
        JPanel panel = new JPanel() {
            private Color fondActuel = fondNormal;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                g2.setColor(fondActuel);
                g2.fillRoundRect(0, 0, w, h, arondi, arondi);

                if (couleurBordure != null && epaisseurBordure > 0) {
                    g2.setColor(couleurBordure);
                    g2.setStroke(new BasicStroke(epaisseurBordure));
                    g2.drawRoundRect(epaisseurBordure / 2, epaisseurBordure / 2,
                            w - epaisseurBordure, h - epaisseurBordure,
                            arondi, arondi);
                }

                g2.dispose();
            }

            @Override
            public boolean isOpaque() {
                return false;
            }

            // Permet de modifier dynamiquement la couleur de fond actuelle
            public void setFondActuel(Color c) {
                this.fondActuel = c;
                repaint();
            }
        };

        // Événements souris pour effet hover et clic
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel) e.getSource()).setBackground(fondHover);
                ((JPanel) e.getSource()).setForeground(fondHover);
                ((JPanel) e.getSource()).repaint();
                ((JPanel) e.getSource()).setOpaque(false);
                ((JPanel) e.getSource()).putClientProperty("fondActuel", fondHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JPanel) e.getSource()).putClientProperty("fondActuel", fondNormal);
                panel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                ((JPanel) e.getSource()).putClientProperty("fondActuel", fondClic);
                panel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ((JPanel) e.getSource()).putClientProperty("fondActuel", fondHover);
                panel.repaint();
            }
        });

        return panel;
    }


    public static JPanel creerPanelArrondiDegrade(Color couleurHaut, Color couleurBas,
                                                  int rayon, Color couleurBordure, int epaisseurBordure) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                GradientPaint gp = new GradientPaint(0, 0, couleurHaut, 0, h, couleurBas);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w, h, rayon, rayon);

                if (couleurBordure != null && epaisseurBordure > 0) {
                    g2.setColor(couleurBordure);
                    g2.setStroke(new BasicStroke(epaisseurBordure));
                    g2.drawRoundRect(epaisseurBordure / 2, epaisseurBordure / 2,
                            w - epaisseurBordure, h - epaisseurBordure,
                            rayon, rayon);
                }

                g2.dispose();
            }

            @Override
            public boolean isOpaque() {
                return false;
            }
        };
    }






    /**
     * Crée un bouton transparent avec une image d’arrière-plan personnalisée,
     * encapsulé dans un objet contenant également son panneau image.
     *
     * @param cheminImage le chemin vers l’image à utiliser en arrière-plan
     * @return un objet BoutonAvecImage contenant le JButton et le PanelAvecImage     */
    public static BoutonAvecI creerBoutonAvecImage(Path cheminImage) {
        JButton bouton = new JButton();
        bouton.setBorderPainted(true);
        bouton.setFocusPainted(false);
        bouton.setContentAreaFilled(false);
        bouton.setOpaque(false);

        PanelAvecImage panel = new PanelAvecImage(cheminImage);
        bouton.add(panel);

        BoutonAvecI boutonAvecImage = new BoutonAvecI(bouton, panel);
        boutonAvecImage.setPathBouton(cheminImage);

        return boutonAvecImage;
    }



    /**
     * Classe utilitaire pour encapsuler un bouton avec son panneau image
     * et une animation associée.     */
    public static class BoutonAvecI {
        /** Le bouton Swing principal */
        public JButton bouton;

        /** Le panneau affichant l’image d’arrière-plan */
        public PanelAvecImage panel;

        /** Animation associée au bouton (peut être null) */
        public Animations animation;

        public Path pathBouton;


        /**
         * Constructeur du bouton avec panneau image.
         * @param bouton le bouton à associer
         * @param panel  le panneau image utilisé en arrière-plan         */
        public BoutonAvecI(JButton bouton, PanelAvecImage panel) {
            this.bouton = bouton;
            this.panel = panel;
            this.animation = null;
            this.pathBouton = null;
        }

        /**
         * Associe une animation au bouton.
         * @param animation l’objet animation à lier         */
        public void setAnimation(Animations animation) {
            this.animation = animation;
        }

        public void setPathBouton(Path pathBouton){
            this.pathBouton = pathBouton;
        }
    }



    /**
     * Affiche un dialogue modal centré sur la fenêtre actuellement active,
     * sans avoir besoin de passer explicitement un parent.
     */
    public static void afficherFonctionEnCours() {
        // Recherche de la fenêtre ayant le focus
        Window fenetreActive = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Spinner d'attente
        JProgressBar spinner = new JProgressBar();
        spinner.setIndeterminate(true);
        spinner.setBorderPainted(false);

        // Message
        JLabel message = new JLabel(
                "<html><center>Cette fonctionnalité est en cours de développement.<br>Merci de votre patience !</center></html>",
                SwingConstants.CENTER
        );
        message.setFont(new Font("SansSerif", Font.BOLD, 16));

        panel.add(spinner, BorderLayout.NORTH);
        panel.add(message, BorderLayout.CENTER);

        // Bouton de fermeture
        Bouton.BoutonAvecImage fermer = Bouton.creerBouton(PATH_BOUTON.resolve("exit.png").toString(), Bouton.ConfigurationParDefaut.Cercle_transparent);
        fermer.setPreferredSize(new Dimension(60,60));
        fermer.addActionListener(e -> SwingUtilities.getWindowAncestor(panel).dispose());
        JPanel panelBtn = new JPanel();
        panelBtn.add(fermer);
        panel.add(panelBtn, BorderLayout.SOUTH);

        // Création du JDialog modal, parent = fenêtre active ou null
        JDialog dialog = new JDialog(fenetreActive, "Fonctionnalité en cours", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.setSize(400, 300);
        dialog.setResizable(false);

        // Centre sur la fenêtre active, ou au centre de l'écran si aucune fenêtre n'est active
        if (fenetreActive != null) {
            dialog.setLocationRelativeTo(fenetreActive);
        } else {
            dialog.setLocationRelativeTo(null);
        }

        dialog.setVisible(true);
    }
}
