package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;

import static Global.Config.PATH_IMAGE_ARRIERE_PLAN_MENU;
import static Vue.Utils.mettreImageEnFond;

public class Menu extends JPanel implements Observateur {
    Jeu jeu;
    CollecteurEvenements collecteurEv;
    InterfaceGraphique interfaceGraphique;

    private int round;
    private Duration duree;
    private String joueurA, joueurB;
    private int scoreJoueurA, scoreJoueurB;

    public Menu(Jeu jeu, CollecteurEvenements collecteurEv, InterfaceGraphique interfaceGraphique){
        this.jeu = jeu;
        this.collecteurEv = collecteurEv;
        this.interfaceGraphique = interfaceGraphique;
        mettreImageEnFond(this, PATH_IMAGE_ARRIERE_PLAN_MENU, 1.0f);

        round = 7;
        duree = Duration.ofMinutes(24);
        joueurA = "Kevin";
        joueurB = "IA";
        scoreJoueurA = 4;
        scoreJoueurB = 3;

        setLayout(new BorderLayout());

        JPanel statsPanel = creerTableauDeStatistiques();
        add(statsPanel, BorderLayout.NORTH);
    }

    @Override
    public void miseAJour() {
        // Méthode d'observateur (à implémenter)
    }

    private JPanel creerTableauDeStatistiques() {
        JPanel tableau = new JPanel();
        tableau.setLayout(new BoxLayout(tableau, BoxLayout.Y_AXIS));
        tableau.setOpaque(false);

        Font font = new Font("SansSerif", Font.BOLD, 18);

        // --- Ligne 1 : Round ---
        JPanel ligneRound = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ligneRound.setOpaque(false);
        JLabel lblRound = new JLabel("Round :");
        lblRound.setFont(font);
        JLabel lblRoundValue = new JLabel(String.valueOf(round));
        lblRoundValue.setFont(font);
        ligneRound.add(lblRound);
        ligneRound.add(lblRoundValue);

        // --- Ligne 2 : Victoires ---
        JPanel ligneVictoires = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ligneVictoires.setOpaque(false);
        JLabel lblVictoires = new JLabel("Victoires :");
        lblVictoires.setFont(font);
        JLabel lblScore = new JLabel();
        lblScore.setFont(font);

        if (scoreJoueurA > scoreJoueurB) {
            lblScore.setText("<html><font color='green'>" + joueurA + "</font>: " + scoreJoueurA +
                    ", <font color='red'>" + joueurB + "</font>: " + scoreJoueurB + "</html>");
        } else if (scoreJoueurA < scoreJoueurB) {
            lblScore.setText("<html><font color='red'>" + joueurA + "</font>: " + scoreJoueurA +
                    ", <font color='green'>" + joueurB + "</font>: " + scoreJoueurB + "</html>");
        } else {
            lblScore.setText(joueurA + ": " + scoreJoueurA + ", " + joueurB + ": " + scoreJoueurB);
        }

        ligneVictoires.add(lblVictoires);
        ligneVictoires.add(lblScore);

        // --- Ligne 3 : Durée ---
        JPanel ligneDuree = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ligneDuree.setOpaque(false);
        JLabel lblDuree = new JLabel("Durée totale :");
        lblDuree.setFont(font);

        long totalSeconds = duree.getSeconds();
        long heures = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long secondes = totalSeconds % 60;

        JLabel lblDureeValue = new JLabel(String.format("%02d:%02d:%02d", heures, minutes, secondes));
        lblDureeValue.setFont(font);

        ligneDuree.add(lblDuree);
        ligneDuree.add(lblDureeValue);

        // --- Ajouter tout au tableau ---
        tableau.add(ligneRound);
        tableau.add(ligneVictoires);
        tableau.add(ligneDuree);

        return tableau;
    }



}
