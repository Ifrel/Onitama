package Vue.VuePrincipale;

import Modele.Jeu;
import Patterns.Observateur;
import Vue.Adaptateurs.AdaptateurButtonGrille;
import Vue.CollecteurEvenements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static Global.Config.*;

public class Grille extends JPanel implements Observateur {
    private final int COLS, ROWS;
    private int DIM = 30;
    private final JButton[][] gridButtons;
    private final JLabel[] gridLettres;
    private final JLabel[] gridChiffres;
    private Jeu jeu;
    private InfosJoueur infosJoueur;
    CollecteurEvenements collecteurEvent;

    public Grille(Jeu jeu, InfosJoueur _Infos_joueur, CollecteurEvenements collecteurEvent) {
        infosJoueur = _Infos_joueur;
        this.jeu = jeu;
        this.COLS = jeu.colonnes();
        this.ROWS = jeu.lignes();
        this.collecteurEvent = collecteurEvent;
        this.gridButtons = new JButton[ROWS][COLS];

        this.gridLettres = new JLabel[COLS];
        this.gridChiffres = new JLabel[ROWS];

        setLayout(new GridBagLayout());
        construireGrille();
        ajouterListenerRedimensionnement();
        jeu.ajouteObservateur(this);
    }

    private void construireGrille() {
        GridBagConstraints gbc = new GridBagConstraints();

        // Ligne des lettres (A à O)
        for (int col = 0; col < COLS; col++) {
            gbc.gridx = col + 1;
            gbc.gridy = 0;
            JLabel label = new JLabel(String.valueOf((char) ('A' + col)), SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.PLAIN, DIM));
            gridLettres[col] = label;
            this.add(label, gbc);
        }

        // Colonne des chiffres + boutons
        for (int row = 0; row < ROWS; row++) {
            // Numéros à gauche
            gbc.gridx = 0;
            gbc.gridy = row + 1;
            JLabel label = new JLabel(String.valueOf(row + 1), SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.PLAIN, DIM));
            gridChiffres[row] = label;
            this.add(label, gbc);

            for (int col = 0; col < COLS; col++) {
                gbc.gridx = col + 1;
                gbc.gridy = row + 1;

                JButton btn = new JButton();
                btn.setBackground(setCouleur(row, col));

                btn.setFocusable(false);
                gridButtons[row][col] = btn;

                int finalRow = row + 1;
                char finalCol = (char) ('A' + col);

                btn.addActionListener(new AdaptateurButtonGrille(collecteurEvent, new Point(row, col)));
                this.add(btn, gbc);
            }
        }
    }

    private void ajouterListenerRedimensionnement() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensionnerGrille();
            }
        });
    }


    private void redimensionnerGrille() {
        int largeurCase = getWidth() / (COLS + 1); // +1 pour les chiffres
        int hauteurCase = getHeight() / (ROWS + 1); // +1 pour les lettres
        int taille = Math.min(largeurCase, hauteurCase) / 2;

        DIM = Math.max(12, taille); // Ne pas descendre trop bas

        // Redimensionner les boutons
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                gridButtons[row][col].setPreferredSize(new Dimension(largeurCase, hauteurCase));
            }
        }

        // Redimensionner les labels
        for (int col = 0; col < COLS; col++) {
            gridLettres[col].setFont(new Font("SansSerif", Font.PLAIN, DIM));
        }

        for (int row = 0; row < ROWS; row++) {
            gridChiffres[row].setFont(new Font("SansSerif", Font.PLAIN, DIM));
        }

        revalidate();
        repaint();
    }

    @Override
    public void miseAJour() {
        majGrille();
        //majTurn();
    }


    private void majGrille() {
        for (int row = 0; row < ROWS; row++) {;
            for (int col = 0; col < COLS; col++) {

                /* Button mis à jour */
                JButton btn = gridButtons[row][col];
                btn.setBackground(setCouleur(row, col));
                btn.revalidate();
                btn.repaint();
            }
        }
    }


    private Color setCouleur(int row, int col) {
        if (jeu.estPoison(row, col)){
            return couleurPoison;
        }else if (jeu.estVide(row, col)){
            return couleurVide;
        }else if(jeu.estPleine(row, col)){
            return couleurPleine;
        }else return null;
    }
}



