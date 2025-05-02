package Vue.Utils;

import javax.swing.*;
import java.awt.*;

import static Global.Config.COULEUR_CASE_TERRAIN;
import static Vue.ConfigUI.FONT_LABEL;

public class MethodsStaticsUtils {


    public static JButton creerBoutonTerrain() {
        JButton bouton = new JButton();
        bouton.setBackground(COULEUR_CASE_TERRAIN);
        bouton.setFocusPainted(true);   // Effet focus visible
        bouton.setContentAreaFilled(true);
        bouton.setOpaque(true);         // Important pour respecter le look L&F
        return bouton;
    }


    /**
     * Crée un bouton avec l'image d'une carte qui redimensionne l'image automatiquement avec la taille du bouton
     *
     * @param cheminImageCarte Chemin de l'image de la carte
     * @return JButton     */
    public static JButton creerBoutonCarte(String cheminImageCarte) {
        return new JButton();
    }



    public static JButton creerBoutonAction(String titre) {
        JButton bouton = new JButton(titre);

        // Couleurs
        bouton.setBackground(new Color(140, 140, 140)); // Fond gris clair
        bouton.setForeground(Color.WHITE);              // Texte blanc

        // Apparence
        bouton.setOpaque(true);                         // Rendre le fond visible
        bouton.setContentAreaFilled(true);
        bouton.setFocusPainted(false);
//        bouton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Bordure blanche de 2px

        // Optionnel : un peu plus joli
        bouton.setFont(new Font("Arial", Font.PLAIN, 22));  // Texte un peu plus gros
        bouton.setPreferredSize(new Dimension(150, 80));   // Taille agréable (change selon besoin)

        return bouton;
    }



    public static JButton creerBoutonAvecImage(String cheminImage) {
        ImageIcon icon = new ImageIcon(cheminImage);
        JButton bouton = new JButton(icon);

        bouton.setBorderPainted(true);
        bouton.setFocusPainted(false);
        bouton.setContentAreaFilled(false);
        bouton.setOpaque(false);

        return bouton;
    }



    public static JButton creerBoutonActionMenu(String titre){
        JButton bouton = new JButton(titre);
        return bouton;
    }



    public static JLabel creerTitreOnglets(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 25)); // Police personnalisée
        label.setPreferredSize(new Dimension(160, 40));  // Taille fixe de l’onglet
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }



    /**
     * Crée un JPanel simple avec une étiquette temporaire pour les onglets non implémentés.
     * @param titreOnglet Le titre de l'onglet.
     * @return Un JPanel avec un message temporaire.
     */
    private JPanel creerOngletTemporaire(String titreOnglet) {
        JPanel panneau = new JPanel(new GridBagLayout()); // Utiliser GridBagLayout pour centrer facilement
        JLabel textePlaceholder = new JLabel("Paramètres pour '" + titreOnglet + "' à venir.");
        textePlaceholder.setFont(FONT_LABEL);
        textePlaceholder.setForeground(Color.GRAY);

        GridBagConstraints contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.gridy = 0;
        contraintes.weightx = 1.0;
        contraintes.weighty = 1.0;
        contraintes.anchor = GridBagConstraints.CENTER;
        panneau.add(textePlaceholder, contraintes);

        return panneau;
    }

}




