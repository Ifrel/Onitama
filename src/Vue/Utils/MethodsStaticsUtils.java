package Vue.Utils;

import Modele.Carte;
import Vue.Animations.Animations;
import Vue.EcranPlateauDeJeu.TYPE_ELEMENT_SUR_TERRAIN;
import Vue.InfosDeConfigUI;
import Vue.Utils.Boutons.Bouton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;

import static Global.Config.ID_JOUEUR_1;
import static Global.Config.ID_JOUEUR_2;
import static Global.Paths.*;

/**
 * Classe utilitaire regroupant des méthodes statiques pour la création
 * d'éléments d'interface utilisateur personnalisés. */
public class MethodsStaticsUtils {


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




    /**
     * Retourne le chemin du fichier image correspondant à la carte donnée.
     *
     * @param carte La carte dont on souhaite obtenir l'image.
     * @return Le chemin relatif vers l'image de la carte (format PNG), construit à partir du nom de la carte.
     *
     * Exemple : pour une carte nommée "tigre", le chemin retourné sera "PATH_CARTE/tigre.png".
     */

    public static Path getCheminImageCarte(Carte carte){
        Path path = PATH_CARTE.resolve(carte.getNom() +".png");
        return path;
    }



    /**
     * Crée un JPanel personnalisé avec des coins arrondis, une bordure facultative, et un comportement interactif
     * qui change la couleur de fond lors du survol, du clic ou du relâchement de la souris.
     *
     * @param fondNormal        Couleur de fond par défaut du panneau.
     * @param fondHover         Couleur de fond lors du survol de la souris.
     * @param fondClic          Couleur de fond lors du clic de la souris.
     * @param arondi            Rayon des coins arrondis.
     * @param couleurBordure    Couleur de la bordure (null pour aucune bordure).
     * @param epaisseurBordure  Épaisseur de la bordure en pixels (0 pour aucune bordure).
     * @return Un JPanel interactif avec un rendu arrondi et personnalisable.
     *
     * Remarques :
     * - Le panneau n'est pas opaque afin de permettre la transparence.
     * - Les changements de couleur ne prennent effet que si on utilise correctement la méthode setFondActuel().
     * - Pour un comportement interactif fonctionnel, il est conseillé de remplacer les appels à putClientProperty(...)
     *   par des appels à la méthode setFondActuel(Color).
     */
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
