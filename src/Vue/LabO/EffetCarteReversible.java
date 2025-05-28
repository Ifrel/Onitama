package Vue.LabO;

import Global.Paths;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 * Classe représentant une fenêtre avec un effet de carte réversible en 3D.
 * L'animation garde une vitesse constante même si l'utilisateur clique plusieurs fois rapidement.
 */
public class EffetCarteReversible extends JFrame {
    private static final int FPS_CIBLE = 60; // Images par seconde pour l'animation
    private static final int DUREE_ANIMATION = 500; // Durée de l'animation en millisecondes

    private BufferedImage image;
    private double angleActuel = 0;
    private JPanel panneauImage;
    private final Timer minuterie;
    private boolean enAnimation = false;
    private long debutAnimation;
    private double angleDepart;
    private double angleCible;

    /**
     * Constructeur qui charge l'image et configure la fenêtre.
     * @param cheminImage Chemin vers l'image à afficher
     */
    public EffetCarteReversible(String cheminImage) {
        chargerImage(cheminImage);
        configurerFenetre();
        configurerComposants();
        minuterie = creerMinuterieAnimation();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Charge l'image depuis un fichier.
     * @param cheminImage Chemin vers l'image
     */
    private void chargerImage(String cheminImage) {
        try {
            image = ImageIO.read(Paths.getCartePath(cheminImage).openStream());
        } catch (IOException e) {
            afficherErreur("Erreur de chargement : " + e.getMessage());
            dispose();
        }
    }

    /**
     * Configure les paramètres de la fenêtre.
     */
    private void configurerFenetre() {
        setTitle("Carte Réversible 3D");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(calculerTailleFenetre());
    }

    /**
     * Calcule la taille préférée de la fenêtre en fonction de l'image.
     * @return Dimension de la fenêtre
     */
    private Dimension calculerTailleFenetre() {
        int largeurBase = image != null ? image.getWidth() : 300;
        int hauteurBase = image != null ? image.getHeight() : 300;
        return new Dimension(largeurBase + 100, hauteurBase + 150);
    }


    /**
     * Configure les composants graphiques de la fenêtre.
     */
    private void configurerComposants() {
        panneauImage = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dessinerCarte((Graphics2D) g);
            }

            @Override
            public Dimension getPreferredSize() {
                if (image == null) return new Dimension(300, 300);
                return new Dimension(image.getWidth(), image.getHeight());
            }
        };
        panneauImage.setDoubleBuffered(true);

        JButton boutonRetourner = creerBoutonRetourner();
        JPanel panneauControle = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        panneauControle.add(boutonRetourner);

        add(panneauImage, BorderLayout.CENTER);
        add(panneauControle, BorderLayout.SOUTH);
    }

    /**
     * Crée le bouton pour retourner la carte.
     * @return JButton configuré
     */
    private JButton creerBoutonRetourner() {
        JButton bouton = new JButton("Retourner la Carte");
        bouton.setFont(new Font("Arial", Font.BOLD, 14));
        bouton.addActionListener(this::demarrerAnimationRetour);
        return bouton;
    }

    /**
     * Crée la minuterie pour gérer l'animation.
     * @return Timer configuré
     */
    private Timer creerMinuterieAnimation() {
        return new Timer(1000 / FPS_CIBLE, e -> mettreAJourAnimation());
    }

    /**
     * Dessine la carte avec la transformation 3D.
     * @param g2d Contexte graphique 2D
     */
    private void dessinerCarte(Graphics2D g2d) {
        if (image == null) return;

        configurerQualiteRendu(g2d);
        AffineTransform transform = creerTransformationCarte();
        g2d.drawImage(image, transform, this);
    }

    /**
     * Configure la qualité du rendu graphique.
     * @param g2d Contexte graphique 2D
     */
    private void configurerQualiteRendu(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }

    /**
     * Crée la transformation affine pour l'effet de rotation et d'échelle.
     * @return AffineTransform configuré
     */
    private AffineTransform creerTransformationCarte() {
        double centreX = panneauImage.getWidth() / 2.0;
        double centreY = panneauImage.getHeight() / 2.0;
        double facteurEchelle = calculerEchelleDynamique();

        AffineTransform transform = new AffineTransform();
        transform.translate(centreX, centreY);
        transform.rotate(angleActuel);
        transform.scale(facteurEchelle, 1.0);
        transform.translate(-image.getWidth() / 2.0, -image.getHeight() / 2.0);
        return transform;
    }

    /**
     * Calcule l'échelle dynamique en fonction de l'angle actuel.
     * @return Facteur d'échelle
     */
    private double calculerEchelleDynamique() {
        return 1.0 - 0.3 * Math.abs(Math.sin(angleActuel));
    }

    /**
     * Démarre l'animation de retournement de la carte.
     * Assure que la vitesse reste constante même en cas de clics multiples.
     * @param e Événement d'action
     */
    private void demarrerAnimationRetour(ActionEvent e) {
        long tempsActuel = System.nanoTime() / 1_000_000;
        if (enAnimation) {
            // Si l'animation est déjà en cours, on "enchaîne" la rotation :
            // On recalcule la progression, on ajuste l'angle de départ et la cible pour garantir la vitesse.
            double progression = (tempsActuel - debutAnimation) / (double) DUREE_ANIMATION;
            progression = Math.min(progression, 1.0);
            double angleInterpole = angleDepart + (angleCible - angleDepart) * interpoler(progression);
            angleDepart = angleInterpole;
            debutAnimation = tempsActuel;
            angleCible = angleDepart + Math.PI;
        } else {
            enAnimation = true;
            angleDepart = angleActuel;
            angleCible = angleActuel + Math.PI;
            debutAnimation = tempsActuel;
            minuterie.start();
        }
    }

    /**
     * Met à jour l'animation à chaque tick de la minuterie.
     * Termine l'animation si la durée est atteinte.
     */
    private void mettreAJourAnimation() {
        long tempsActuel = System.nanoTime() / 1_000_000;
        double progression = (tempsActuel - debutAnimation) / (double) DUREE_ANIMATION;

        if (progression >= 1.0) {
            terminerAnimation();
            return;
        }

        double progressionEased = interpoler(progression);
        angleActuel = angleDepart + (angleCible - angleDepart) * progressionEased;
        panneauImage.repaint();
    }

    /**
     * Fonction d'interpolation cosinusoïdale pour un easing naturel.
     * @param t Progression entre 0 et 1
     * @return Valeur interpolée
     */
    private double interpoler(double t) {
        return 0.5 * (1 - Math.cos(Math.PI * t));
    }

    /**
     * Termine l'animation et remet à jour l'état.
     */
    private void terminerAnimation() {
        minuterie.stop();
        enAnimation = false;
        angleActuel = angleCible % (2 * Math.PI);
        panneauImage.repaint();
    }

    /**
     * Affiche un message d'erreur dans une boîte de dialogue.
     * @param message Message d'erreur
     */
    private void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}




class test {
    /**
     * Point d'entrée principal de l'application.
     *
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EffetCarteReversible(Paths.getCartePath("TIGRE.png").toString()));
    }
}