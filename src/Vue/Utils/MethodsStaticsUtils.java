package Vue.Utils;

import Global.Paths;
import Modele.Carte;
import Vue.Animations.Animations;
import Vue.Configuration.InfosDeConfigUI;
import Vue.EcranPlateauDeJeu.TYPE_ELEMENT_SUR_TERRAIN;
import Vue.Utils.Boutons.Bouton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.net.URL;

import static Global.Config.ID_JOUEUR_1;
import static Global.Config.ID_JOUEUR_2;
import static Global.Paths.*;
import static Vue.Configuration.ConfigUI.ARRONDI;

/**
 * Classe utilitaire regroupant des méthodes statiques pour la création
 * d'éléments d'interface utilisateur personnalisés. */
public class MethodsStaticsUtils {



    /**
     * Retourne le chemin du fichier image correspondant au pion donné, en fonction de son type (étudiant ou maître)
     * et du joueur auquel il appartient, selon les couleurs configurées dans l'interface utilisateur.
     *
     * @param infosDeConfigUI  Objet contenant les informations de configuration de l’interface utilisateur,
     *                         notamment les couleurs des pions des joueurs.
     * @param type             Type de l'élément présent sur le terrain (étudiant/maître, joueur 1/2, etc.).
     * @return Le chemin relatif vers l'image correspondant au pion, ou {@code null} si le type est VIDE.
     * @throws IllegalArgumentException si le type de pion n'est pas reconnu.
     *
     * Nom de fichier généré : "pion_[role]_[couleur].png"
     * Exemple : "pion_etudiant_bleu.png" ou "pion_maitre_rouge.png"
     *
     * Remarques :
     * - Le chemin est construit en résolvant le nom du fichier par rapport à PATH_DEBUT_PION.
     * - Le rôle peut être "etudiant" ou "maitre", et la couleur est récupérée dynamiquement selon le joueur.
     */
    public static URL getCheminImagePion(InfosDeConfigUI infosDeConfigUI, TYPE_ELEMENT_SUR_TERRAIN type) {
        if (type == TYPE_ELEMENT_SUR_TERRAIN.VIDE) {
            return null; // ou retourne un chemin vers une image "vide" si besoin
        }

        String nomCouleurPion = "";
        String role = "";

        switch (type) {
            case TRONE:
                ArrayList<String> valeurs = new ArrayList<>(Arrays.asList("1", "2", "3", "4"));
                Collections.shuffle(valeurs); // Mélange aléatoire
                role = "cible_trone" + valeurs.get(0);
                break;
            case PION_ETUDIANT_J1:
                nomCouleurPion = infosDeConfigUI.getNomCouleurPionJoueur(ID_JOUEUR_1);
                role = "pion_etudiant_";
                break;
            case PION_ETUDIANT_J2:
                nomCouleurPion = infosDeConfigUI.getNomCouleurPionJoueur(ID_JOUEUR_2);
                role = "pion_etudiant_";
                break;
            case PION_MAITRE_J1:
                nomCouleurPion = infosDeConfigUI.getNomCouleurPionJoueur(ID_JOUEUR_1);
                role = "pion_maitre_";
                break;
            case PION_MAITRE_J2:
                nomCouleurPion = infosDeConfigUI.getNomCouleurPionJoueur(ID_JOUEUR_2);
                role = "pion_maitre_";
                break;
            default: throw new IllegalArgumentException("Type de pion inconnu : " + type);
        }

        String nomFichier = role + nomCouleurPion + ".png";
        return getPionPath(nomFichier);
    }

    


    /**
     * Crée un panneau Swing personnalisé avec des bords arrondis, un fond semi-transparent,
     * et une bordure dorée.
     * Le panneau est non opaque pour permettre la transparence et utilise des
     * techniques de rendu pour dessiner les formes arrondies.
     *
     * @return un objet JPanel avec une apparence arrondie et une transparence stylisée
     */
    public static JPanel creerPanelArrondi() {
        return new JPanel() {
            private Color borderColor = new Color(212, 175, 55); // Couleur de bordure par défaut
            private float borderThickness = 3f;                  // Épaisseur de bordure par défaut
            private int arcSize = ARRONDI;                       // Taille des coins arrondis

            {
                setOpaque(false);

                // Ajout d'un écouteur de souris pour effet hover
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        borderColor = borderColor.brighter();
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        borderColor = new Color(212, 175, 55);
                        repaint();
                    }
                });
            }

            public void setBorderColor(Color color) {
                this.borderColor = color;
                repaint();
            }

            public void setBorderThickness(float thickness) {
                this.borderThickness = thickness;
                repaint();
            }

            public void setArcSize(int size) {
                this.arcSize = size;
                repaint();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();

                // Configuration du rendu
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                // Création du rectangle arrondi
                Shape roundedRect = new RoundRectangle2D.Double(
                        borderThickness/2,        // x
                        borderThickness/2,        // y
                        getWidth()-borderThickness,   // largeur
                        getHeight()-borderThickness,  // hauteur
                        arcSize,                      // arcWidth
                        arcSize                       // arcHeight
                );

                // Dessin du fond avec la couleur définie
                if (getBackground() != null) {
                    g2.setColor(getBackground());
                    g2.fill(roundedRect);
                }

                // Dessin de la bordure avec effet de lueur
                g2.setStroke(new BasicStroke(borderThickness));

                // Effet de lueur (glow effect)
                float alpha = 0.1f;
                for (int i = 0; i < 4; i++) {
                    g2.setColor(new Color(
                            borderColor.getRed(),
                            borderColor.getGreen(),
                            borderColor.getBlue(),
                            (int)(255 * alpha)
                    ));
                    g2.setStroke(new BasicStroke(borderThickness + i * 2));
                    g2.draw(roundedRect);
                }

                // Bordure principale
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(borderThickness));
                g2.draw(roundedRect);

                g2.dispose();
            }
        };
    }




    /**
     * Crée un bouton transparent avec une image d’arrière-plan personnalisée,
     * encapsulé dans un objet contenant également son panneau image.
     *
     * @param nomImage le chemin vers l’image à utiliser en arrière-plan
     * @return un objet BoutonAvecImage contenant le JButton et le PanelAvecImage     */
    public static BoutonAvecI creerBoutonAvecImage(String nomImage) {
        JButton bouton = new JButton();
        bouton.setBorderPainted(true);
        bouton.setFocusPainted(false);
        bouton.setContentAreaFilled(false);
        bouton.setOpaque(false);

        URL imageUrl = Paths.getButtonPath(nomImage);
        PanelAvecImage panel = new PanelAvecImage(imageUrl);
        bouton.add(panel);

        BoutonAvecI boutonAvecImage = new BoutonAvecI(bouton, panel);
        boutonAvecImage.setUrlBouton(imageUrl);

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

        public URL urlBouton;


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

        public void setUrlBouton(URL urlBouton){
            this.urlBouton = urlBouton;
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
        Bouton.BoutonAvecImage fermer = Bouton.creerBouton(Paths.getButtonPath("exit.png"), Bouton.ConfigurationParDefaut.Cercle_transparent);
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