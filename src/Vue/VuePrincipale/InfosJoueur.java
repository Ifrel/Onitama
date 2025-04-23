package Vue.VuePrincipale;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

import static Global.Config.couleurPleine;

public class InfosJoueur extends JPanel implements Observateur {
    private JLabel joueurLabel;
    private final Jeu jeu;

    public InfosJoueur(Jeu _jeu) {
        this.jeu = _jeu;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setAlignmentX(CENTER_ALIGNMENT);

        String currentPlayer = "C'est au tour de " + jeu.nomJoueurCourant() + " de jouer";

        joueurLabel = new JLabel(currentPlayer, SwingConstants.CENTER);
        joueurLabel.setForeground(couleurPleine);
        joueurLabel.setFont(new Font("SansSerif", Font.BOLD, 35));
        joueurLabel.setAlignmentX(CENTER_ALIGNMENT);
        this.add(joueurLabel);

        JLabel instructionLabel = new JLabel("Clique sur une case pour manger un bout de la gaufre", SwingConstants.CENTER);
        instructionLabel.setForeground(Color.ORANGE.darker());
        instructionLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        instructionLabel.setAlignmentX(CENTER_ALIGNMENT);
        this.add(instructionLabel);

        jeu.ajouteObservateur(this);
    }

    @Override
    public void miseAJour() {
        if (jeu.estPartieFinie()){
            joueurLabel.setText("Partie terminée");
            return;
        }
        joueurLabel.setText("C'est au tour de " + jeu.nomJoueurCourant() + " de jouer");
    }
}





