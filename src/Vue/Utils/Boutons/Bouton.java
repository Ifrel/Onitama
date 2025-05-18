package Vue.Utils.Boutons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL; // Ajout de l'import pour URL

import static Global.Paths.PATH_PION_NOIR_ETUDIANT;

public class Bouton {

    // Ces champs semblent destinés à être utilisés si Bouton était une classe d'instance
    // wrapper autour d'un JButton, mais votre implémentation actuelle utilise des méthodes statiques
    // pour créer directement des JButtons. Je les laisse pour l'instant mais ils
    // ne sont pas utilisés dans la méthode statique creerBoutonAvecImage telle qu'elle est.
    // Si vous souhaitiez faire de Bouton une classe "wrapper", la structure serait différente.
    private String cheminImage;
    private float epaisseurBordureInitiale;
    private float epaisseurBordureSurvol;
    private float arrondiBordure;
    private Color couleurBordure;
    private Color couleurFondSurvol;

    /**
     * Énumération des configurations par défaut pour les styles de bouton.
     */
    public enum ConfigurationParDefaut {
        Cercle,
        Cercle_transparent,
        Carre,
        Carre_transparent,
        Rectangle,
        Rectangle_transparent
    }

    // Classe interne pour ajouter la méthode changerImage au JButton retourné
    // Cela évite de devoir exposer l'ImageIcon originale ou de modifier JButton.
    public static class BoutonAvecImage extends JButton {
        private ImageIcon iconeOriginale;
        private float epaisseurBordureInitiale;
        private float epaisseurBordureSurvol;
        private float arrondiBordure;
        private Color couleurBordure;
        private Color couleurFondSurvol;

        // État dynamique de l’épaisseur de bordure
        private float epaisseurActuelle;


        public BoutonAvecImage(
                ImageIcon iconeOriginale,
                float epaisseurBordureInitiale,
                float epaisseurBordureSurvol,
                float arrondiBordure,
                Color couleurBordure,
                Color couleurFondSurvol
        ) {
            this.iconeOriginale = iconeOriginale;
            this.epaisseurBordureInitiale = epaisseurBordureInitiale;
            this.epaisseurBordureSurvol = epaisseurBordureSurvol;
            this.arrondiBordure = arrondiBordure;
            this.couleurBordure = couleurBordure;
            this.couleurFondSurvol = couleurFondSurvol;
            this.epaisseurActuelle = epaisseurBordureInitiale; // Initialisation

            // Configuration standard du JButton
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFocusable(true);

            // Redimension de l’icône
            Runnable miseAJourIcone = () -> {
                int largeur = getWidth();
                int hauteur = getHeight();
                if (largeur > 0 && hauteur > 0 && this.iconeOriginale != null && this.iconeOriginale.getImage() != null) {
                    try {
                        Image imageRedim = this.iconeOriginale.getImage().getScaledInstance(
                                largeur, hauteur, Image.SCALE_SMOOTH
                        );
                        setIcon(new ImageIcon(imageRedim));
                    } catch (Exception e) {
                        System.err.println("Erreur lors du redimensionnement de l'icône: " + e.getMessage());
                        // Gérer l'erreur, par exemple en ne mettant pas d'icône
                        setIcon(null);
                    }
                } else {
                    setIcon(null); // Pas de taille ou pas d'image, pas d'icône
                }
            };


            addHierarchyListener(e -> {
                if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                    SwingUtilities.invokeLater(miseAJourIcone);
                }
            });

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    miseAJourIcone.run();
                }
            });

            // Gère l’effet au survol
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    epaisseurActuelle = epaisseurBordureSurvol;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    epaisseurActuelle = epaisseurBordureInitiale;
                    repaint();
                }
            });

            // Effectuer une première mise à jour de l'icône si le bouton est déjà visible
            if (isShowing()) {
                miseAJourIcone.run();
            }
        }

        /**
         * Change l'image affichée par le bouton.
         * @param cheminNouvelleImage Le chemin vers la nouvelle image.
         */
        public void changerImage(String cheminNouvelleImage) {
            if (cheminNouvelleImage == null || cheminNouvelleImage.isEmpty()) {
                this.iconeOriginale = null;
            } else {
                URL imageURL = getClass().getResource(cheminNouvelleImage);
                if (imageURL == null) {
                    System.err.println("Image non trouvée: " + cheminNouvelleImage);
                    this.iconeOriginale = null;
                } else {
                    this.iconeOriginale = new ImageIcon(imageURL);
                }
            }
            // Déclencher une mise à jour de l'icône pour redimensionner la nouvelle image
            // si le bouton est déjà dimensionné.
            if (getWidth() > 0 && getHeight() > 0) {
                Runnable miseAJourIcone = () -> {
                    int largeur = getWidth();
                    int hauteur = getHeight();
                    if (largeur > 0 && hauteur > 0 && this.iconeOriginale != null && this.iconeOriginale.getImage() != null) {
                        try {
                            Image imageRedim = this.iconeOriginale.getImage().getScaledInstance(
                                    largeur, hauteur, Image.SCALE_SMOOTH
                            );
                            setIcon(new ImageIcon(imageRedim));
                        } catch (Exception e) {
                            System.err.println("Erreur lors du redimensionnement de l'icône: " + e.getMessage());
                            setIcon(null);
                        }
                    } else {
                        setIcon(null);
                    }
                };
                miseAJourIcone.run(); // Exécuter directement si la taille est connue
            } else {
                setIcon(null); // Pas de taille connue, l'icône sera définie lors du redimensionnement/affichage
            }
            repaint(); // Redessiner le bouton
        }


        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int width = getWidth();
            int height = getHeight();
            int arc = (int) arrondiBordure;

            boolean survol = getModel().isRollover();
            boolean clique = getModel().isPressed();
            boolean focus = isFocusOwner();

            // Fond survol ou focus
            if (survol || focus) {
                g2.setColor(couleurFondSurvol);
                g2.fillRoundRect(0, 0, width, height, arc, arc);
            }

            // Fond appuyé
            if (clique) {
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRoundRect(0, 0, width, height, arc, arc);
            }

            // Calcul marge dynamique — ajusté pour éviter un rayon négatif ou 0
            int tailleMin = Math.min(width, height);
            float margeArrondie = arc * 0.15f; // marge liée à la forme arrondie
            float margeFixe = 4f;
            float margeMin = 2f;
            float marge = Math.max(margeFixe, Math.max(epaisseurActuelle, margeArrondie));
            marge = Math.min(marge, tailleMin / 6.5f); // empêche que la marge prenne tout

            // Dessin de l'image centrée
            Icon icon = getIcon();
            if (icon instanceof ImageIcon) {                // Vérifie que l’icône est bien une ImageIcon (contient une image)
                ImageIcon imageIcon = (ImageIcon) icon;      // Convertit l’icône en ImageIcon pour accéder à l’image
                Image image = imageIcon.getImage();          // Récupère l’objet Image depuis l’ImageIcon

                int iw = image.getWidth(this);
                int ih = image.getHeight(this);

                if (iw > 0 && ih > 0) {
                    int availableWidth = (int) (width - 2 * marge);
                    int availableHeight = (int) (height - 2 * marge);

                    float scale = Math.min((float) availableWidth / iw, (float) availableHeight / ih);
                    int nw = (int) (iw * scale);
                    int nh = (int) (ih * scale);

                    int x = (width - nw) / 2;
                    int y = (height - nh) / 2;

                    g2.drawImage(image, x, y, nw, nh, this);
                }
            }

            // Bordure
            g2.setColor(couleurBordure);
            g2.setStroke(new BasicStroke(epaisseurActuelle));
            float halfStroke = epaisseurActuelle / 2f;
            g2.drawRoundRect(
                    (int) halfStroke,
                    (int) halfStroke,
                    (int) (width - epaisseurActuelle),
                    (int) (height - epaisseurActuelle),
                    arc, arc
            );

            g2.dispose();
        }


        @Override
        public boolean isContentAreaFilled() {
            // Permet au paintComponent personnalisé de dessiner le fond
            return false;
        }

        // Assurer que getPreferredSize renvoie une taille raisonnable si pas d'icône ou de texte
        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            if (size.width == 0 || size.height == 0) {
                // Fournir une taille par défaut si aucune icône ou texte n'est présent
                return new Dimension(50, 50); // Exemple de taille par défaut
            }
            return size;
        }

        // Assurer que getMinimumSize et getMaximumSize sont également gérés si nécessaire
        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize(); // Par défaut, minimum est préféré
        }

        @Override
        public Dimension getMaximumSize() {
            // Peut retourner super.getMaximumSize() ou une taille spécifique
            return super.getMaximumSize();
        }
    }


    /**
     * Crée un bouton personnalisé avec une image et un style spécifié.
     * @param cheminImage Le chemin vers l’image à utiliser.
     * @param config La configuration de style par défaut.
     * @return Un bouton personnalisé.
     */
    public static BoutonAvecImage creerBouton(String cheminImage, ConfigurationParDefaut config) {
        float epaisseurInitiale, epaisseurSurvol, arrondi;
        Color couleurBordure, couleurFondSurvol;

        // Définir les paramètres en fonction de la configuration par défaut
        switch (config) {
            case Cercle:
                epaisseurInitiale = 2f;
                epaisseurSurvol = 5f;
                arrondi = 999f; // Grand pour un effet circulaire sur un bouton carré
                couleurBordure = new Color(52, 127, 202); // Bleu
                couleurFondSurvol = new Color(52, 127, 202, 50); // Bleu semi-transparent
                break;
            case Cercle_transparent:
                epaisseurInitiale = 2f;
                epaisseurSurvol = 5f;
                arrondi = 999f;
                couleurBordure = new Color(200, 200, 200); // Gris clair
                couleurFondSurvol = new Color(200, 200, 200, 50); // Gris clair semi-transparent
                break;
            case Carre:
                epaisseurInitiale = 2f;
                epaisseurSurvol = 5f;
                arrondi = 10f; // Léger arrondi
                couleurBordure = new Color(150, 100, 50); // Marron
                couleurFondSurvol = new Color(150, 100, 50, 50); // Marron semi-transparent
                break;
            case Carre_transparent:
                epaisseurInitiale = 2f;
                epaisseurSurvol = 5f;
                arrondi = 10f;
                couleurBordure = new Color(200, 200, 200); // Gris clair
                couleurFondSurvol = new Color(200, 200, 200, 50); // Gris clair semi-transparent
                break;
            case Rectangle:
                epaisseurInitiale = 2f;
                epaisseurSurvol = 5f;
                arrondi = 10f; // Léger arrondi
                couleurBordure = new Color(50, 150, 100); // Vert
                couleurFondSurvol = new Color(50, 150, 100, 50); // Vert semi-transparent
                break;
            case Rectangle_transparent:
                epaisseurInitiale = 2f;
                epaisseurSurvol = 5f;
                arrondi = 10f;
                couleurBordure = new Color(200, 200, 200); // Gris clair
                couleurFondSurvol = new Color(200, 200, 200, 50); // Gris clair semi-transparent
                break;
            default:
                // Configuration par défaut générique si l'énumération n'est pas reconnue
                epaisseurInitiale = 1f;
                epaisseurSurvol = 3f;
                arrondi = 5f;
                couleurBordure = Color.GRAY;
                couleurFondSurvol = new Color(150, 150, 150, 50);
        }

        // Créer l'icône à partir du chemin
        ImageIcon iconeOriginale = null;
        if (cheminImage != null && !cheminImage.isEmpty()) {
//            URL imageURL = Bouton.class.getResource(cheminImage);
//            if (imageURL == null) {
//                System.err.println("Image non trouvée pour la configuration " + config + ": " + cheminImage);
//            } else {
//                iconeOriginale = new ImageIcon(imageURL);
//            }
            iconeOriginale = new ImageIcon(cheminImage);
        }


        // Utiliser la méthode de création détaillée avec les paramètres définis
        return creerBouton(
                iconeOriginale, // Passer l'ImageIcon
                epaisseurInitiale,
                epaisseurSurvol,
                arrondi,
                couleurBordure,
                couleurFondSurvol
        );
    }


    /**
     * Crée un bouton personnalisé avec une image, une bordure arrondie, un fond dynamique et une mise à l’échelle automatique de l’image.
     * Note : Utilise maintenant une ImageIcon directement.
     * @param iconeOriginale            L’ImageIcon à utiliser comme icône du bouton.
     * @param epaisseurBordureInitiale  L’épaisseur de la bordure à dessiner.
     * @param epaisseurBordureSurvol    L’épaisseur de la bordure au survol.
     * @param arrondiBordure            Le rayon d’arrondi des coins de la bordure.
     * @param couleurBordure            La couleur de la bordure.
     * @param couleurFondSurvol         La couleur du fond au survol.
     * @return                          Un bouton personnalisé encapsulé dans BoutonAvecImage.
     */
    public static BoutonAvecImage creerBouton(
            ImageIcon iconeOriginale,
            float epaisseurBordureInitiale,
            float epaisseurBordureSurvol,
            float arrondiBordure,
            Color couleurBordure,
            Color couleurFondSurvol
    ) {
        // Retourne directement une instance de la classe interne BoutonAvecImage
        return new BoutonAvecImage(iconeOriginale,
                epaisseurBordureInitiale,
                epaisseurBordureSurvol,
                arrondiBordure,
                couleurBordure,
                couleurFondSurvol);
    }

    // Méthode simplifiée pour créer un bouton avec juste une image (utilise une config par défaut implicite ou minimale)
    public static BoutonAvecImage creerBouton(String cheminImage) {
        // Utilise une configuration par défaut simple, par exemple Rectangle_transparent
        return creerBouton(cheminImage, ConfigurationParDefaut.Rectangle_transparent);
    }

    // Méthode pour créer un bouton avec une icône (utilise une config par défaut implicite ou minimale)
    public static BoutonAvecImage creerBouton(ImageIcon icone) {
        return creerBouton(icone,
                1f, 2f, 5f, // Paramètres par défaut pour les bordures
                Color.GRAY, // Couleur par défaut
                new Color(150, 150, 150, 50) // Fond survol par défaut
        );
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Bouton avec Image Personnalisé Amélioré");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());

            // Exemple d'utilisation de la configuration par défaut Cercle
            // Assurez-vous d'avoir un fichier "icone.png" ou remplacez par un chemin valide
            BoutonAvecImage boutonCercle = Bouton.creerBouton(
                    PATH_PION_NOIR_ETUDIANT.toString(), // REMPLACER PAR UN CHEMIN VALIDE VERS VOTRE IMAGE
                    Bouton.ConfigurationParDefaut.Cercle
            );
            boutonCercle.setPreferredSize(new Dimension(100, 100)); // Taille pour bien voir l'effet cercle

            // Exemple d'utilisation de la configuration par défaut Rectangle
            BoutonAvecImage boutonRectangle = Bouton.creerBouton(
                    PATH_PION_NOIR_ETUDIANT.toString(), // REMPLACER PAR UN AUTRE CHEMIN VALIDE
                    Bouton.ConfigurationParDefaut.Rectangle
            );
            boutonRectangle.setPreferredSize(new Dimension(120, 80));


            // Exemple d'utilisation de la méthode simplifiée
            BoutonAvecImage boutonSimple = Bouton.creerBouton(
                    " " // REMPLACER PAR UN AUTRE CHEMIN VALIDE
            );
            boutonSimple.setPreferredSize(new Dimension(70, 70));

//            // Exemple d'utilisation de changerImage
//            Timer timerChangerImage = new Timer(2000, e -> {
//                // Remplacez par des chemins valides vers vos images
//                String[] images = {
//                        " ",
//                        PATH_PION_NOIR_ETUDIANT.toString(),
//                        null // Tester avec une image nulle pour voir le comportement
//                };
//                int randomIndex = (int) (Math.random() * (images.length));
//                String nouvelleImage = images[randomIndex];
//                System.out.println("Changement d'image vers: " + nouvelleImage);
//                boutonSimple.changerImage(nouvelleImage);
//            });
//            timerChangerImage.start();


            frame.add(boutonCercle);
            frame.add(boutonRectangle);
            frame.add(boutonSimple); // Ajouter le bouton simple pour voir le changement d'image

            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}