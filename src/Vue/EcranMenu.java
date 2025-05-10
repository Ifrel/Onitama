package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.Duration;

import static Vue.Utils.MethodsStaticsUtils.creerBouton;

/**
 * La classe EcranMenu représente l'écran de menu principal avec les statistiques du jeu.
 * Elle est liée au modèle (Jeu) et agit en tant qu'observateur pour réagir aux mises à jour.
 */
public class EcranMenu extends JPanel implements Observateur {

    // Références au modèle, au gestionnaire d'événements et à l'interface graphique principale
    private final Jeu jeu;
    private final CollecteurEvenements collecteurEv;
    private final InterfaceGraphique interfaceGraphique;

    // Données pour l'affichage des statistiques du jeu
    private int round; // Le numéro du round actuel
    private Duration duree; // La durée totale de la session de jeu
    private String joueurA, joueurB; // Les noms des joueurs
    private int scoreJoueurA, scoreJoueurB; // Les scores des joueurs

    /**
     * Constructeur du menu principal. Initialise les composants et les données affichées.
     *
     * @param jeu               Le modèle du jeu.
     * @param collecteurEv      Le gestionnaire d'événements pour interagir avec l'utilisateur.
     * @param interfaceGraphique L'interface graphique principale contenant ce menu.
     */
    public EcranMenu(Jeu jeu, CollecteurEvenements collecteurEv, InterfaceGraphique interfaceGraphique) {
        this.jeu = jeu;
        this.collecteurEv = collecteurEv;
        this.interfaceGraphique = interfaceGraphique;

        // Initialisation des données de statistiques (données factices pour l'exemple)
        round = 7;
        duree = Duration.ofMinutes(24);
        joueurA = "Kevin";
        joueurB = "IA";
        scoreJoueurA = 4;
        scoreJoueurB = 3;

        // Configuration du layout manager pour organiser les composants
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20); // Marges autour des composants
        gbc.fill = GridBagConstraints.HORIZONTAL; // Étirement horizontal des composants
        gbc.weightx = 1.0; // Distribution de l'espace horizontal supplémentaire
        gbc.gridx = 0; // Tous les composants sont dans la même colonne

        // Ajout du bouton Retour en haut à droite
        JPanel panelRetour = creerPanelRetour();
        gbc.gridy = 0; // Première ligne
        gbc.anchor = GridBagConstraints.NORTHEAST; // Alignement en haut à droite
        add(panelRetour, gbc);

        // Ajout du tableau de statistiques au centre
        JPanel panelStats = creerTableauDeStatistiques();
        gbc.gridy = 1; // Deuxième ligne
        gbc.weighty = 0.5; // Prend la moitié de l'espace vertical disponible
        gbc.fill = GridBagConstraints.BOTH; // Étirement horizontal et vertical
        gbc.anchor = GridBagConstraints.CENTER; // Alignement au centre
        add(panelStats, gbc);

        // Ajout des boutons d'action au centre
        JPanel panelActions = creerBoutonsActions();
        gbc.gridy = 2; // Troisième ligne
        gbc.weighty = 0.5; // Prend l'autre moitié de l'espace vertical disponible
        gbc.fill = GridBagConstraints.BOTH; // Étirement horizontal et vertical
        gbc.anchor = GridBagConstraints.CENTER; // Alignement au centre
        add(panelActions, gbc);

        // Ajout du bouton Sauvegarder en bas à gauche
        JPanel panelSauvegarde = creerBoutonSauvegarde();
        gbc.gridy = 3; // Quatrième ligne
        gbc.weighty = 0.1; // Prend un peu d'espace vertical
        gbc.fill = GridBagConstraints.HORIZONTAL; // Étirement horizontal
        gbc.anchor = GridBagConstraints.SOUTHWEST; // Alignement en bas à gauche
        add(panelSauvegarde, gbc);

        // Ajout du bouton Exit en bas à droite
        JPanel panelExit = creerBoutonExit();
        gbc.gridy = 3; // Quatrième ligne
        gbc.anchor = GridBagConstraints.SOUTHEAST; // Alignement en bas à droite
        add(panelExit, gbc);

        // Configuration de l'image de fond du menu
//        mettreImageEnFond(this, PATH_IMAGE_ARRIERE_PLAN_MENU, 0);
    }

    /**
     * Méthode appelée lorsque l'objet observé (le modèle Jeu) notifie un changement.
     * Dans ce cas, elle pourrait être utilisée pour mettre à jour les statistiques affichées.
     */
    @Override
    public void miseAJour() {
        // À compléter si on veut rafraîchir les stats dynamiquement en fonction des changements du modèle
    }

    /**
     * Crée le panneau contenant le bouton "Retour" aligné à droite en haut.
     *
     * @return Le JPanel contenant le bouton "Retour".
     */
    private JPanel creerPanelRetour() {
        JButton btnRetour = creerBouton("Retour");
        btnRetour.setPreferredSize(new Dimension(120, 30)); // Taille préférée du bouton
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Layout pour aligner à droite
        panel.setOpaque(false); // Rend le fond du panneau transparent
        panel.add(btnRetour);
        panel.setBorder(new EmptyBorder(20, 0, 0, 50)); // Marge haute et droite
        return panel;
    }

    /**
     * Crée le panneau contenant le bouton "Sauvegarder" aligné à gauche en bas.
     *
     * @return Le JPanel contenant le bouton "Sauvegarder".
     */
    private JPanel creerBoutonSauvegarde() {
        JButton btnSauvegarder = creerBouton("Sauvegarder");
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Layout pour aligner à gauche
        panel.setOpaque(false); // Rend le fond du panneau transparent
        panel.add(btnSauvegarder);
        panel.setBorder(new EmptyBorder(0, 50, 20, 0)); // Marge gauche et basse
        return panel;
    }

    /**
     * Crée le panneau contenant les boutons d'action principaux (Mes parties, Nouvelle partie, Didacticiel, Règles)
     * disposés verticalement au centre.
     *
     * @return Le JPanel contenant les boutons d'action.
     */
    private JPanel creerBoutonsActions() {
        JPanel container = new JPanel(new GridBagLayout()); // Utilisation de GridBagLayout pour centrer les boutons
        container.setOpaque(false); // Rend le fond du panneau transparent
        GridBagConstraints gbcBouton = new GridBagConstraints();
        gbcBouton.fill = GridBagConstraints.HORIZONTAL; // Étirement horizontal des boutons
        gbcBouton.insets = new Insets(10, 0, 10, 0); // Marges autour des boutons
        gbcBouton.weightx = 1.0; // Distribution de l'espace horizontal supplémentaire

        String[] labels = {"Mes parties", "Nouvelle partie", "Didacticiel", "Règles"};
        for (int i = 0; i < labels.length; i++) {
            JButton bouton = creerBouton(labels[i]);
            gbcBouton.gridy = i; // Chaque bouton sur une nouvelle ligne
            container.add(bouton, gbcBouton);
        }
        container.setBorder(new EmptyBorder(50, 50, 50, 50)); // Marges autour du conteneur de boutons
        return container;
    }

    /**
     * Crée le panneau contenant le bouton "Exit" aligné à droite en bas.
     *
     * @return Le JPanel contenant le bouton "Exit".
     */
    private JPanel creerBoutonExit() {
        JButton btnExit = new JButton("Exit");
        btnExit.setPreferredSize(new Dimension(100, 30)); // Taille préférée du bouton
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Layout pour aligner à droite
        panel.setOpaque(false); // Rend le fond du panneau transparent
        panel.add(btnExit);
        panel.setBorder(new EmptyBorder(0, 0, 20, 50)); // Marge basse et droite
        return panel;
    }

    /**
     * Crée le panneau affichant les statistiques du jeu (round, victoires, durée totale).
     *
     * @return Le JPanel contenant le tableau de statistiques.
     */
    private JPanel creerTableauDeStatistiques() {
        JPanel tableau = new JPanel(new GridBagLayout()); // Utilisation de GridBagLayout pour organiser les labels et les valeurs
        tableau.setOpaque(false); // Rend le fond du panneau transparent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Marges autour des labels et valeurs
        gbc.anchor = GridBagConstraints.WEST; // Alignement à gauche par défaut

        Font font = new Font("SansSerif", Font.BOLD, 18); // Police pour les statistiques
        Color textColor = Color.BLACK; // Couleur du texte

        // Ligne 1 : Affichage du round actuel
        gbc.gridx = 0; // Première colonne
        gbc.gridy = 0; // Première ligne
        tableau.add(creerLabelStat("Round :", font, textColor, GridBagConstraints.EAST), gbc); // Label "Round :" aligné à droite
        gbc.gridx = 1; // Deuxième colonne
        gbc.weightx = 1.0; // La valeur prend l'espace horizontal disponible
        gbc.fill = GridBagConstraints.HORIZONTAL; // Étirement horizontal de la valeur
        tableau.add(creerLabelStat(String.valueOf(round), font, textColor, GridBagConstraints.WEST), gbc); // Valeur du round alignée à gauche
        gbc.weightx = 0.0; // Réinitialisation du poids
        gbc.fill = GridBagConstraints.NONE; // Réinitialisation du fill

        // Ligne 2 : Affichage du score des joueurs
        gbc.gridx = 0; // Première colonne
        gbc.gridy = 1; // Deuxième ligne
        tableau.add(creerLabelStat("Victoires :", font, textColor, GridBagConstraints.EAST), gbc); // Label "Victoires :" aligné à droite
        gbc.gridx = 1; // Deuxième colonne
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
        gbc.gridx = 0; // Première colonne
        gbc.gridy = 2; // Troisième ligne
        tableau.add(creerLabelStat("Durée totale :", font, textColor, GridBagConstraints.EAST), gbc); // Label "Durée totale :" aligné à droite
        gbc.gridx = 1; // Deuxième colonne
        long totalSeconds = duree.getSeconds();
        long heures = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long secondes = totalSeconds % 60;
        String temps = String.format("%02d:%02d:%02d", heures, minutes, secondes); // Formatage de la durée en HH:MM:SS
        tableau.add(creerLabelStat(temps, font, textColor, GridBagConstraints.WEST), gbc); // Valeur de la durée alignée à gauche

        // Ajout d'une bordure esthétique autour du tableau de statistiques
        tableau.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 2), // Bordure extérieure grise
                new EmptyBorder(20, 20, 20, 20) // Marge intérieure
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
     * @return Le JLabel créé.
     */
    private JLabel creerLabelStat(String texte, Font font, Color couleur, int alignement) {
        JLabel label = new JLabel(texte);
        label.setFont(font);
        label.setForeground(couleur);
//        label.setHorizontalAlignment(alignement);
        return label;
    }
}