package Vue.Utils;

import Vue.Animations.Animations;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

import static Vue.ConfigUI.FONT_LABEL;

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
