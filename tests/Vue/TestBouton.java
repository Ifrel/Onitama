package Vue;

import Vue.Utils.Boutons.Bouton;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static Global.Paths.PATH_PION_NOIR_ETUDIANT;


class TestBouton {

    @Test
    public void main() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Bouton avec Image Personnalisé Amélioré");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());

            // Exemple d'utilisation de la configuration par défaut Cercle
            // Assurez-vous d'avoir un fichier "icone.png" ou remplacez par un chemin valide
            Bouton.BoutonAvecImage boutonCercle = Bouton.creerBouton(
                    PATH_PION_NOIR_ETUDIANT.toString(), // REMPLACER PAR UN CHEMIN VALIDE VERS VOTRE IMAGE
                    Bouton.ConfigurationParDefaut.Cercle
            );
            boutonCercle.setPreferredSize(new Dimension(100, 100)); // Taille pour bien voir l'effet cercle

            // Exemple d'utilisation de la configuration par défaut Rectangle
            Bouton.BoutonAvecImage boutonRectangle = Bouton.creerBouton(
                    PATH_PION_NOIR_ETUDIANT.toString(), // REMPLACER PAR UN AUTRE CHEMIN VALIDE
                    Bouton.ConfigurationParDefaut.Rectangle
            );
            boutonRectangle.setPreferredSize(new Dimension(120, 80));


            // Exemple d'utilisation de la méthode simplifiée
            Bouton.BoutonAvecImage boutonSimple = Bouton.creerBouton(
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