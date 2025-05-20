package Vue;

import Modele.Jeu;
import Patterns.Observateur;
import Vue.Utils.JPanelAvecCouleurDebraille;
import Vue.Utils.PanelAvecImage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.Duration;

import static Global.Config.COULEUR_BLOC_MENU;
import static Global.Paths.PATH_ARRIERE_IMG1;
import static Vue.Utils.MethodsStaticsUtils.creerBouton;

/**
 * La classe EcranMenu représente l'écran de menu principal avec les statistiques du jeu.
 * Elle est liée au modèle (Jeu) et agit en tant qu'observateur pour réagir aux mises à jour.
 */
public class EcranMenu extends PanelAvecImage implements Observateur {

    private final Jeu jeu;
    private final CollecteurEvenements collecteurEv;
    private final InterfaceGraphique interfaceGraphique;

    private int round;
    private Duration duree;
    private String joueurA, joueurB;
    private int scoreJoueurA, scoreJoueurB;



    /**
     * Constructeur du menu principal. Initialise les composants et les données affichées.
     *
     * @param jeu               Le modèle du jeu.
     * @param collecteurEv      Le gestionnaire d'événements pour interagir avec l'utilisateur.
     * @param interfaceGraphique L'interface graphique principale contenant ce menu.
     */
    public EcranMenu(Jeu jeu, CollecteurEvenements collecteurEv, InterfaceGraphique interfaceGraphique) {
//        super(COULEUR_BLOC_MENU, new Color(185, 185, 185, 255));
        super(PATH_ARRIERE_IMG1);
        this.jeu = jeu;
        this.collecteurEv = collecteurEv;
        this.interfaceGraphique = interfaceGraphique;

        round = 7;
        duree = Duration.ofMinutes(24);
        joueurA = "Kevin";
        joueurB = "IA";
        scoreJoueurA = 4;
        scoreJoueurB = 3;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 50, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Ajout du bouton Retour en haut à droite
        JPanel panelRetour = creerPanelRetour();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        add(panelRetour, gbc);

        // Ajout du tableau de statistiques au centre
        JPanel panelStats = creerTableauDeStatistiques();
        gbc.gridy = 1;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        add(panelStats, gbc);

        // Ajout des boutons d'action au centre
        JPanel panelActions = creerBoutonsActions();
        gbc.gridy = 2;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        add(panelActions, gbc);

        // Ajout du bouton Sauvegarder en bas à gauche
        JPanel panelSauvegarde = creerBoutonSauvegarde();
        gbc.gridy = 3;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        add(panelSauvegarde, gbc);

        // Ajout du bouton Exit en bas à droite
        JPanel panelExit = creerBoutonExit();
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        add(panelExit, gbc);
     }


    /**
     * Méthode appelée lorsque l'objet observé (le modèle Jeu) notifie un changement.
     * Dans ce cas, elle pourrait être utilisée pour mettre à jour les statistiques affichées.     */
    @Override
    public void miseAJour() {

    }


    /**
     * Crée le panneau contenant le bouton "Retour" aligné à droite en haut.
     *
     * @return Le JPanel contenant le bouton "Retour".     */
    private JPanel creerPanelRetour() {
        JButton btnRetour = creerBouton("Retour");
        btnRetour.setPreferredSize(new Dimension(120, 30));
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);
        panel.add(btnRetour);
        panel.setBorder(new EmptyBorder(20, 0, 0, 50));
        return panel;
    }


    /**
     * Crée le panneau contenant le bouton "Sauvegarder" aligné à gauche en bas.
     *
     * @return Le JPanel contenant le bouton "Sauvegarder".     */
    private JPanel creerBoutonSauvegarde() {
        JButton btnSauvegarder = creerBouton("Sauvegarder");
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        panel.add(btnSauvegarder);
        panel.setBorder(new EmptyBorder(0, 50, 20, 0));
        return panel;
    }


    /**
     * Crée le panneau contenant les boutons d'action principaux (Mes parties, Nouvelle partie, Didacticiel, Règles)
     * disposés verticalement au centre.
     *
     * @return Le JPanel contenant les boutons d'action.     */
    private JPanel creerBoutonsActions() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        GridBagConstraints gbcBouton = new GridBagConstraints();
        gbcBouton.fill = GridBagConstraints.HORIZONTAL;
        gbcBouton.insets = new Insets(10, 0, 10, 0);
        gbcBouton.weightx = 1.0;

        // TODO à connecter avec le modele
        String[] labels = {"Mes parties", "Nouvelle partie", "Didacticiel", "Règles"};
        for (int i = 0; i < labels.length; i++) {
            JButton bouton = creerBouton(labels[i]);
            gbcBouton.gridy = i;
            container.add(bouton, gbcBouton);
        }
        container.setBorder(new EmptyBorder(50, 50, 50, 50));
        return container;
    }



    /**
     * Crée le panneau contenant le bouton "Exit" aligné à droite en bas.
     *
     * @return Le JPanel contenant le bouton "Exit".     */
    private JPanel creerBoutonExit() {
        JButton btnExit = new JButton("Exit");
        btnExit.setPreferredSize(new Dimension(100, 30));
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);
        panel.add(btnExit);
        panel.setBorder(new EmptyBorder(0, 0, 20, 50));
        return panel;
    }



    /**
     * Crée le panneau affichant les statistiques du jeu (round, victoires, durée totale).
     *
     * @return Le JPanel contenant le tableau de statistiques.     */
    private JPanel creerTableauDeStatistiques() {
        JPanel tableau = new JPanel(new GridBagLayout());
        tableau.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        Font font = new Font("SansSerif", Font.BOLD, 18);
        Color textColor = Color.BLACK;

        // Ligne 1 : Affichage du round actuel
        gbc.gridx = 0;
        gbc.gridy = 0;
        tableau.add(creerLabelStat("Round :", font, textColor, GridBagConstraints.EAST), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tableau.add(creerLabelStat(String.valueOf(round), font, textColor, GridBagConstraints.WEST), gbc);
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;

        // Ligne 2 : Affichage du score des joueurs
        gbc.gridx = 0;
        gbc.gridy = 1;
        tableau.add(creerLabelStat("Victoires :", font, textColor, GridBagConstraints.EAST), gbc);
        gbc.gridx = 1;
        JLabel lblScore = new JLabel();
        lblScore.setFont(font);
        // Affichage du score avec une couleur différente pour le joueur ayant le score le plus élevé
        if (scoreJoueurA > scoreJoueurB) {
            lblScore.setText(String.format("<html><font color='green'>%s</font>: %d, <font color='red'>%s</font>: %d</html>", joueurA, scoreJoueurA, joueurB, scoreJoueurB));
        } else if (scoreJoueurA < scoreJoueurB) {
            lblScore.setText(String.format("<html><font color='red'>%s</font>: %d, <font color='green'>%s</font>: %d</html>", joueurA, scoreJoueurA, joueurB, scoreJoueurB));
        } else {
            lblScore.setText(joueurA + ": " + scoreJoueurA + ", " + joueurB + ": " + scoreJoueurB);
        }
        tableau.add(lblScore, gbc);

        // Ligne 3 : Affichage de la durée totale de la session
        gbc.gridx = 0;
        gbc.gridy = 2;
        tableau.add(creerLabelStat("Durée totale :", font, textColor, GridBagConstraints.EAST), gbc);
        gbc.gridx = 1;
        long totalSeconds = duree.getSeconds();
        long heures = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long secondes = totalSeconds % 60;
        String temps = String.format("%02d:%02d:%02d", heures, minutes, secondes);
        tableau.add(creerLabelStat(temps, font, textColor, GridBagConstraints.WEST), gbc);

        // Ajout d'une bordure esthétique autour du tableau de statistiques
        tableau.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 2),
                new EmptyBorder(20, 20, 20, 20)
        ));
        return tableau;
    }


    /**
     * Crée un JLabel avec la police, la couleur et l'alignement spécifiés.
     *
     * @param texte     Le texte du label.
     * @param font      La police à utiliser.
     * @param couleur   La couleur du texte.
     * @param alignement L'alignement du texte (SwingConstants.LEFT ou SwingConstants.RIGHT).
     * @return Le JLabel créé.     */
    private JLabel creerLabelStat(String texte, Font font, Color couleur, int alignement) {
        JLabel label = new JLabel(texte);
        label.setFont(font);
        label.setForeground(couleur);
        return label;
    }
}