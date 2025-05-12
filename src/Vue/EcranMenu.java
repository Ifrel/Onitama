package Vue;

import Global.Config; // Supposé exister
import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent; // Importer ActionEvent
import java.awt.event.ActionListener; // Importer ActionListener
import java.time.Duration;
import java.util.Locale; // Importer Locale
import java.util.ResourceBundle; // Importer ResourceBundle

// Importer la classe des clés de texte (si utilisée)
// import com.example.config.AppTextKeys;


import static Vue.Utils.MethodsStaticsUtils.creerBouton; // Supposé exister

/**
 * La classe EcranMenu représente l'écran de menu principal avec les statistiques du jeu.
 * Elle est liée au modèle (Jeu) et agit en tant qu'observateur pour réagir aux mises à jour.
 * Gère l'affichage des textes via ResourceBundle pour l'internationalisation.
 */
public class EcranMenu extends JPanel implements Observateur {

    private final Jeu jeu;
    private final CollecteurEvenements collecteurEv;
    private final InterfaceGraphique interfaceGraphique;
    private ResourceBundle messages; // Champ pour le ResourceBundle

    // Champs pour stocker les Labels qui affichent les statistiques, pour pouvoir les mettre à jour.
    private JLabel lblRoundValue;
    private JLabel lblScoreValue;
    private JLabel lblDurationValue;

    // Champs pour stocker les données actuelles (optionnel, on pourrait lire directement depuis le jeu)
    // private int currentRound;
    // private Duration currentDuree;
    // private String currentJoueurA, currentJoueurB;
    // private int currentScoreJoueurA, currentScoreJoueurB;

    Locale locale;

    /**
     * Constructeur du menu principal. Initialise les composants et les données affichées.
     * Charge le ResourceBundle. Enregistre le panneau comme observateur du modèle.
     *
     * @param jeu               Le modèle du jeu.
     * @param collecteurEv      Le gestionnaire d'événements pour interagir avec l'utilisateur.
     * @param interfaceGraphique L'interface graphique principale contenant ce menu.
     */
    public EcranMenu(Jeu jeu, CollecteurEvenements collecteurEv, InterfaceGraphique interfaceGraphique) {
        this.jeu = jeu;
        this.collecteurEv = collecteurEv;
        this.interfaceGraphique = interfaceGraphique;
        locale=null;


















        // --- Charger le ResourceBundle ---
        try {
            this.messages = ResourceBundle.getBundle("AppTextes", locale); // "AppTextes" est le basename
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du ResourceBundle 'AppTextes' pour la locale " + locale + " : " + e.getMessage());
            this.messages = null; // Gérer l'erreur : utiliser les clés comme texte par défaut
        }


        setLayout(new GridBagLayout());
        // GridBagConstraints est mieux d'être créé pour chaque groupe de contraintes ou composant
        // GridBagConstraints gbc = new GridBagConstraints(); // Ne pas réutiliser un seul objet comme ça globalement

        // Ajout du bouton Retour en haut à droite
        JPanel panelRetour = creerPanelRetour();
        GridBagConstraints gbcRetour = new GridBagConstraints();
        gbcRetour.gridy = 0;
        gbcRetour.gridx = 1; // Placé dans la 2ème colonne pour aligner à droite
        gbcRetour.anchor = GridBagConstraints.NORTHEAST;
        gbcRetour.insets = new Insets(10, 0, 10, 10); // Ajuster les marges
        gbcRetour.weightx = 0.1; // Donne un peu de poids pour pousser vers la droite
        add(panelRetour, gbcRetour);

        // Ajout du tableau de statistiques au centre
        JPanel panelStats = creerTableauDeStatistiques();
        GridBagConstraints gbcStats = new GridBagConstraints();
        gbcStats.gridy = 1;
        gbcStats.gridx = 0; // Placé dans la 1ère colonne
        gbcStats.gridwidth = 2; // S'étend sur 2 colonnes pour être centré
        gbcStats.weighty = 0.5;
        gbcStats.weightx = 1.0; // Prend l'espace horizontal
        gbcStats.fill = GridBagConstraints.BOTH; // S'étire dans les deux directions
        gbcStats.anchor = GridBagConstraints.CENTER;
        gbcStats.insets = new Insets(10, 10, 10, 10); // Marges autour du panneau stats
        add(panelStats, gbcStats);

        // Ajout des boutons d'action au centre
        JPanel panelActions = creerBoutonsActions();
        GridBagConstraints gbcActions = new GridBagConstraints();
        gbcActions.gridy = 2;
        gbcActions.gridx = 0; // Placé dans la 1ère colonne
        gbcActions.gridwidth = 2; // S'étend sur 2 colonnes pour être centré
        gbcActions.weighty = 0.5;
        gbcActions.weightx = 1.0; // Prend l'espace horizontal
        gbcActions.fill = GridBagConstraints.BOTH; // S'étire dans les deux directions
        gbcActions.anchor = GridBagConstraints.CENTER;
        gbcActions.insets = new Insets(10, 10, 10, 10); // Marges autour du panneau actions
        add(panelActions, gbcActions);

        // Ajout du bouton Sauvegarder en bas à gauche
        JPanel panelSauvegarde = creerBoutonSauvegarde();
        GridBagConstraints gbcSauvegarde = new GridBagConstraints();
        gbcSauvegarde.gridy = 3;
        gbcSauvegarde.gridx = 0; // Placé dans la 1ère colonne
        gbcSauvegarde.weighty = 0.1; // Moins de poids vertical
        gbcSauvegarde.anchor = GridBagConstraints.SOUTHWEST;
        gbcSauvegarde.insets = new Insets(10, 10, 10, 0); // Ajuster les marges
        gbcSauvegarde.weightx = 0.1; // Donne un peu de poids pour pousser vers la gauche
        add(panelSauvegarde, gbcSauvegarde);

        // Ajout du bouton Exit en bas à droite
        JPanel panelExit = creerBoutonExit();
        GridBagConstraints gbcExit = new GridBagConstraints();
        gbcExit.gridy = 3;
        gbcExit.gridx = 1; // Placé dans la 2ème colonne
        gbcExit.weighty = 0.1; // Moins de poids vertical
        gbcExit.anchor = GridBagConstraints.SOUTHEAST;
        gbcExit.insets = new Insets(10, 0, 10, 10); // Ajuster les marges
        gbcExit.weightx = 0.1; // Donne un peu de poids pour pousser vers la droite
        add(panelExit, gbcExit);


        // --- 3. Enregistrer ce panneau comme observateur du jeu ---
        if (this.jeu != null) { // S'assurer que le jeu n'est pas null (important pour les mocks)
            this.jeu.ajouteObservateur(this); // Assurez-vous que Jeu a cette méthode
            miseAJour(); // Appeler miseAJour une première fois pour afficher les données initiales
        } else {
            System.err.println("Attention : Modèle 'Jeu' est null. Les statistiques ne seront pas mises à jour.");
            // Optionnel : afficher des valeurs par défaut ou un message d'erreur dans l'UI
        }

    }

    // Helper pour obtenir le texte localisé, ou la clé si le bundle est manquant
    private String getText(String key) {
        // TODO: Importer com.example.config.AppTextKeys et l'utiliser partout
        // Remplacer "key" par AppTextKeys.NOM_CLE
        return messages != null ? messages.getString(key) : key;
    }


    /**
     * Méthode appelée lorsque l'objet observé (le modèle Jeu) notifie un changement.
     * Met à jour les statistiques affichées dans l'interface.
     * Cette méthode est appelée sur l'EDT si le modèle notifie correctement.
     */
    @Override
    public void miseAJour() {
        // --- 4. Récupérer les données actualisées du modèle ---
        // Les données stockées dans les champs de la classe (round, duree, etc.)
        // doivent être lues depuis le modèle 'jeu' ici.
        int currentRound = jeu.getNumeroRound(); // Assurez-vous que ces méthodes existent dans la classe Jeu
        Duration currentDuree = jeu.getDureePartie(); // Assurez-vous que ces méthodes existent dans la classe Jeu
        String currentJoueurA = jeu.getJoueur(1).getNom(); // Assurez-vous que ces méthodes existent dans la classe Jeu
        String currentJoueurB = jeu.getJoueur(2).getNom(); // Assurez-vous que ces méthodes existent dans la classe Jeu
        int currentScoreJoueurA = jeu.getJoueur(1).getScore(); // Assurez-vous que ces méthodes existent dans la classe Jeu
        int currentScoreJoueurB = jeu.getJoueur(2).getScore(); // Assurez-vous que ces méthodes existent dans la classe Jeu


        // --- 5. Mettre à jour le texte des Labels d'affichage ---

        // Mise à jour du round
        // Assurez-vous que lblRoundValue est un champ de la classe
        if (lblRoundValue != null) {
            lblRoundValue.setText(String.valueOf(currentRound));
        }


        // Mise à jour du score
        // Assurez-vous que lblScoreValue est un champ de la classe
        if (lblScoreValue != null) {
            // Recalculer le texte HTML basé sur les nouvelles données
            if (currentScoreJoueurA > currentScoreJoueurB) {
                lblScoreValue.setText(String.format("<html><font color='green'>%s</font>: %d, <font color='red'>%s</font>: %d</html>", currentJoueurA, currentScoreJoueurA, currentJoueurB, currentScoreJoueurB));
            } else if (currentScoreJoueurA < currentScoreJoueurB) {
                lblScoreValue.setText(String.format("<html><font color='red'>%s</font>: %d, <font color='green'>%s</font>: %d</html>", currentJoueurA, currentScoreJoueurA, currentJoueurB, currentScoreJoueurB));
            } else {
                lblScoreValue.setText(currentJoueurA + ": " + currentScoreJoueurA + ", " + currentJoueurB + ": " + currentScoreJoueurB);
            }
        }


        // Mise à jour de la durée
        // Assurez-vous que lblDurationValue est un champ de la classe
        if (lblDurationValue != null) {
            long totalSeconds = currentDuree.getSeconds();
            long heures = totalSeconds / 3600;
            long minutes = (totalSeconds % 3600) / 60;
            long secondes = totalSeconds % 60;
            String temps = String.format("%02d:%02d:%02d", heures, minutes, secondes); // Formatage en HH:MM:SS
            lblDurationValue.setText(temps);
        }

        // Optionnel : Appeler revalidate() et repaint() si la taille des composants a changé
        // ou si la mise à jour n'entraîne pas un repaint automatique (rare pour setText sur JLabel)
        // revalidate();
        // repaint();

    }

    /**
     * Crée le panneau contenant le bouton "Retour" aligné à droite en haut.
     * Ajoute un écouteur d'action.
     * @return Le JPanel contenant le bouton "Retour".
     */
    private JPanel creerPanelRetour() {
        // TODO: Remplacer "Retour" par getText(AppTextKeys.BTN_RETOUR)
        JButton btnRetour = creerBouton("Retour");
        btnRetour.setPreferredSize(new Dimension(120, 30)); // Taille préférée du bouton
//        btnRetour.addActionListener(e -> collecteurEv.retourMenuPrincipal()); // TODO: Assurez-vous que cette méthode existe dans CollecteurEvenements

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Layout pour aligner à droite
        panel.setOpaque(false); // Rend le fond du panneau transparent
        panel.add(btnRetour);
        panel.setBorder(new EmptyBorder(20, 0, 0, 50)); // Marge haute et droite
        return panel;
    }

    /**
     * Crée le panneau contenant le bouton "Sauvegarder" aligné à gauche en bas.
     * Ajoute un écouteur d'action.
     * @return Le JPanel contenant le bouton "Sauvegarder".
     */
    private JPanel creerBoutonSauvegarde() {
        // TODO: Remplacer "Sauvegarder" par getText(AppTextKeys.BTN_SAUVEGARDER)
        JButton btnSauvegarder = creerBouton("Sauvegarder");
//        btnSauvegarder.addActionListener(e -> collecteurEv.sauvegarderPartie()); // TODO: Assurez-vous que cette méthode existe

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Layout pour aligner à gauche
        panel.setOpaque(false); // Rend le fond du panneau transparent
        panel.add(btnSauvegarder);
        panel.setBorder(new EmptyBorder(0, 50, 20, 0)); // Marge gauche et basse
        return panel;
    }

    /**
     * Crée le panneau contenant les boutons d'action principaux (Mes parties, Nouvelle partie, Didacticiel, Règles)
     * disposés verticalement au centre.
     * Ajoute des écouteurs d'action.
     * @return Le JPanel contenant les boutons d'action.
     */
    private JPanel creerBoutonsActions() {
        JPanel container = new JPanel(new GridBagLayout()); // Utilisation de GridBagLayout pour centrer les boutons
        container.setOpaque(false); // Rend le fond du panneau transparent
        GridBagConstraints gbcBouton = new GridBagConstraints(); // Nouvelle instance pour les boutons
        gbcBouton.fill = GridBagConstraints.HORIZONTAL; // Étirement horizontal des boutons
        gbcBouton.insets = new Insets(10, 0, 10, 0); // Marges autour des boutons
        gbcBouton.weightx = 1.0; // Distribution de l'espace horizontal supplémentaire
        gbcBouton.anchor = GridBagConstraints.CENTER; // Centrer chaque bouton dans sa cellule

        // TODO: Remplacer les labels codés en dur par des clés ResourceBundle
        String[] labels = {"Mes parties", "Nouvelle partie", "Didacticiel", "Règles"};
        String[] keys = {
                "BTN_MES_PARTIES", // Clé correspondante dans ResourceBundle
                "BTN_NOUVELLE_PARTIE",
                "BTN_DIDACTICIEL",
                "BTN_REGLES"
        }; // Exemple de clés
        // Utiliser les textes localisés pour les labels des boutons
        String[] localizedLabels = new String[labels.length];
        for (int i = 0; i < labels.length; i++) {
            localizedLabels[i] = getText(keys[i]); // Utiliser la clé correspondante
        }


        for (int i = 0; i < localizedLabels.length; i++) {
            JButton bouton = creerBouton(localizedLabels[i]); // Utiliser le texte localisé
            gbcBouton.gridy = i; // Chaque bouton sur une nouvelle ligne
            container.add(bouton, gbcBouton);

            // Ajouter l'écouteur d'action pour chaque bouton
            // Il faudrait une logique pour distinguer quel bouton a été cliqué et appeler la méthode du contrôleur appropriée.
            // Une approche est de passer la clé ou un identifiant dans la commande d'action du bouton.
            String actionCommand = keys[i]; // Utiliser la clé comme action command
            bouton.setActionCommand(actionCommand); // Stocker la clé dans le bouton

            bouton.addActionListener(e -> {
                String command = e.getActionCommand();
                switch (command) {
                    case "BTN_MES_PARTIES":
//                        collecteurEv.afficherPartiesSauvegardees(); // TODO
                        break;
                    case "BTN_NOUVELLE_PARTIE":
//                        collecteurEv.afficherEcranConfiguration(); // TODO ou nouvellePartie(...)
                        break;
                    case "BTN_DIDACTICIEL":
//                        collecteurEv.afficherDidacticiel(); // TODO
                        break;
                    case "BTN_REGLES":
//                        collecteurEv.afficherRegles(); // TODO
                        break;
                    default:
                        System.err.println("Action non reconnue pour le bouton : " + command);
                }
            });
        }
        container.setBorder(new EmptyBorder(50, 50, 50, 50)); // Marges autour du conteneur de boutons
        return container;
    }

    /**
     * Crée le panneau contenant le bouton "Exit" aligné à droite en bas.
     * Ajoute un écouteur d'action.
     * @return Le JPanel contenant le bouton "Exit".
     */
    private JPanel creerBoutonExit() {
        // TODO: Remplacer "Exit" par getText(AppTextKeys.BTN_EXIT)
        JButton btnExit = new JButton("Exit");
        btnExit.setPreferredSize(new Dimension(100, 30)); // Taille préférée du bouton
        // Ajouter l'écouteur d'action
        btnExit.addActionListener(e -> collecteurEv.clavier("exit")); // TODO: Assurez-vous que cette méthode existe et gère la sortie proprement

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Layout pour aligner à droite
        panel.setOpaque(false); // Rend le fond du panneau transparent
        panel.add(btnExit);
        panel.setBorder(new EmptyBorder(0, 0, 20, 50)); // Marge basse et droite
        return panel;
    }

    /**
     * Crée le panneau affichant les statistiques du jeu (round, victoires, durée totale).
     * Stocke les Labels des valeurs comme champs de classe pour pouvoir les mettre à jour.
     *
     * @return Le JPanel contenant le tableau de statistiques.
     */
    private JPanel creerTableauDeStatistiques() {
        JPanel tableau = new JPanel(new GridBagLayout()); // Utilisation de GridBagLayout pour organiser les labels et les valeurs
        tableau.setOpaque(false); // Rend le fond du panneau transparent
        GridBagConstraints gbc = new GridBagConstraints(); // Nouvelle instance pour les contraintes internes
        gbc.insets = new Insets(5, 10, 5, 10); // Marges autour des labels et valeurs

        Font font = new Font("SansSerif", Font.BOLD, 18); // Police pour les statistiques
        Color textColor = Color.BLACK; // Couleur du texte

        // Ligne 1 : Affichage du round actuel
        gbc.gridx = 0; // Première colonne
        gbc.gridy = 0; // Première ligne
        gbc.anchor = GridBagConstraints.EAST; // Aligner le label à droite
        // TODO: Remplacer "Round :" par getText(AppTextKeys.LBL_ROUND)
        tableau.add(creerLabelStat(getText("LBL_ROUND"), font, textColor, SwingConstants.RIGHT), gbc); // Label "Round :" localisé
        gbc.gridx = 1; // Deuxième colonne
        gbc.anchor = GridBagConstraints.WEST; // Aligner la valeur à gauche
        gbc.weightx = 1.0; // La valeur prend l'espace horizontal disponible
        gbc.fill = GridBagConstraints.HORIZONTAL; // Étirement horizontal de la valeur
        this.lblRoundValue = creerLabelStat("...", font, textColor, SwingConstants.LEFT); // Créer le Label, texte initial "...", le stocker dans le champ
        tableau.add(this.lblRoundValue, gbc);
        gbc.weightx = 0.0; // Réinitialisation du poids
        gbc.fill = GridBagConstraints.NONE; // Réinitialisation du fill

        // Ligne 2 : Affichage du score des joueurs
        gbc.gridx = 0; // Première colonne
        gbc.gridy = 1; // Deuxième ligne
        gbc.anchor = GridBagConstraints.EAST; // Aligner le label à droite
        // TODO: Remplacer "Victoires :" par getText(AppTextKeys.LBL_VICTOIRES)
        tableau.add(creerLabelStat(getText("LBL_VICTOIRES"), font, textColor, SwingConstants.RIGHT), gbc); // Label "Victoires :" localisé
        gbc.gridx = 1; // Deuxième colonne
        gbc.anchor = GridBagConstraints.WEST; // Aligner la valeur à gauche
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.lblScoreValue = creerLabelStat("...", font, textColor, SwingConstants.LEFT); // Créer le Label pour le score, stocker dans le champ
        // Le texte HTML sera mis à jour dans miseAJour()
        tableau.add(this.lblScoreValue, gbc);
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;

        // Ligne 3 : Affichage de la durée totale de la session
        gbc.gridx = 0; // Première colonne
        gbc.gridy = 2; // Troisième ligne
        gbc.anchor = GridBagConstraints.EAST; // Aligner le label à droite
        // TODO: Remplacer "Durée totale :" par getText(AppTextKeys.LBL_DUREE_TOTALE)
        tableau.add(creerLabelStat(getText("LBL_DUREE_TOTALE"), font, textColor, SwingConstants.RIGHT), gbc); // Label "Durée totale :" localisé
        gbc.gridx = 1; // Deuxième colonne
        gbc.anchor = GridBagConstraints.WEST; // Aligner la valeur à gauche
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.lblDurationValue = creerLabelStat("...", font, textColor, SwingConstants.LEFT); // Créer le Label pour la durée, stocker dans le champ
        // Le calcul et le formatage de la durée seront mis à jour dans miseAJour()
        tableau.add(this.lblDurationValue, gbc);
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;


        // Ajouter un peu de "glue" verticale à la fin pour pousser les lignes vers le haut
        GridBagConstraints verticalGlueConstraints = new GridBagConstraints();
        verticalGlueConstraints.gridx = 0;
        verticalGlueConstraints.gridy = 3; // Sous la dernière ligne
        verticalGlueConstraints.weighty = 1.0; // Prend tout l'espace vertical restant
        verticalGlueConstraints.fill = GridBagConstraints.VERTICAL;
        verticalGlueConstraints.gridwidth = 2; // S'étend sur les deux colonnes
        tableau.add(Box.createVerticalGlue(), verticalGlueConstraints);


        // Ajout d'une bordure esthétique autour du tableau de statistiques
        tableau.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 2), // Bordure extérieure grise
                new EmptyBorder(20, 20, 20, 20) // Marge intérieure
        ));
        return tableau;
    }

    /**
     * Crée un JLabel avec la police, la couleur et l'alignement spécifiés.
     * L'alignement est appliqué via setHorizontalAlignment.
     *
     * @param texte     Le texte du label.
     * @param font      La police à utiliser.
     * @param couleur   La couleur du texte.
     * @param alignement L'alignement horizontal du texte (SwingConstants.LEFT, RIGHT, CENTER).
     * @return Le JLabel créé.
     */
    private JLabel creerLabelStat(String texte, Font font, Color couleur, int alignement) {
        JLabel label = new JLabel(texte);
        label.setFont(font);
        label.setForeground(couleur);
        label.setHorizontalAlignment(alignement); // Décommenté pour appliquer l'alignement
        return label;
    }

    // TODO: Ajouter une méthode pour désenregistrer l'observateur si nécessaire
    public void deconnecterObservateur() {
        if (this.jeu != null) {
            // this.jeu.retirerObservateur(this); // Assurez-vous que Jeu a cette méthode
        }
    }
}



//package Vue;
//
//import Modele.Jeu;
//import Patterns.Observateur;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import java.awt.*;
//import java.time.Duration;
//
//import static Vue.Utils.MethodsStaticsUtils.creerBouton;
//
///**
// * La classe EcranMenu représente l'écran de menu principal avec les statistiques du jeu.
// * Elle est liée au modèle (Jeu) et agit en tant qu'observateur pour réagir aux mises à jour.
// */
//public class EcranMenu extends JPanel implements Observateur {
//
//    private final Jeu jeu;
//    private final CollecteurEvenements collecteurEv;
//    private final InterfaceGraphique interfaceGraphique;
//
//    private int round;
//    private Duration duree;
//    private String joueurA, joueurB;
//    private int scoreJoueurA, scoreJoueurB;
//
//
//
//    /**
//     * Constructeur du menu principal. Initialise les composants et les données affichées.
//     *
//     * @param jeu               Le modèle du jeu.
//     * @param collecteurEv      Le gestionnaire d'événements pour interagir avec l'utilisateur.
//     * @param interfaceGraphique L'interface graphique principale contenant ce menu.
//     */
//    public EcranMenu(Jeu jeu, CollecteurEvenements collecteurEv, InterfaceGraphique interfaceGraphique) {
//        this.jeu = jeu;
//        this.collecteurEv = collecteurEv;
//        this.interfaceGraphique = interfaceGraphique;
//
//        round = 7;
//        duree = Duration.ofMinutes(24);
//        joueurA = "Kevin";
//        joueurB = "IA";
//        scoreJoueurA = 4;
//        scoreJoueurB = 3;
//
//        setLayout(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 20, 10, 20);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.weightx = 1.0;
//        gbc.gridx = 0;
//
//        // Ajout du bouton Retour en haut à droite
//        JPanel panelRetour = creerPanelRetour();
//        gbc.gridy = 0;
//        gbc.anchor = GridBagConstraints.NORTHEAST;
//        add(panelRetour, gbc);
//
//        // Ajout du tableau de statistiques au centre
//        JPanel panelStats = creerTableauDeStatistiques();
//        gbc.gridy = 1;
//        gbc.weighty = 0.5;
//        gbc.fill = GridBagConstraints.BOTH;
//        gbc.anchor = GridBagConstraints.CENTER;
//        add(panelStats, gbc);
//
//        // Ajout des boutons d'action au centre
//        JPanel panelActions = creerBoutonsActions();
//        gbc.gridy = 2;
//        gbc.weighty = 0.5;
//        gbc.fill = GridBagConstraints.BOTH;
//        gbc.anchor = GridBagConstraints.CENTER;
//        add(panelActions, gbc);
//
//        // Ajout du bouton Sauvegarder en bas à gauche
//        JPanel panelSauvegarde = creerBoutonSauvegarde();
//        gbc.gridy = 3;
//        gbc.weighty = 0.1;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.anchor = GridBagConstraints.SOUTHWEST;
//        add(panelSauvegarde, gbc);
//
//        // Ajout du bouton Exit en bas à droite
//        JPanel panelExit = creerBoutonExit();
//        gbc.gridy = 3;
//        gbc.anchor = GridBagConstraints.SOUTHEAST;
//        add(panelExit, gbc);
//     }
//
//
//    /**
//     * Méthode appelée lorsque l'objet observé (le modèle Jeu) notifie un changement.
//     * Dans ce cas, elle pourrait être utilisée pour mettre à jour les statistiques affichées.
//     */
//    @Override
//    public void miseAJour() {
//
//    }
//
//    /**
//     * Crée le panneau contenant le bouton "Retour" aligné à droite en haut.
//     *
//     * @return Le JPanel contenant le bouton "Retour".
//     */
//    private JPanel creerPanelRetour() {
//        JButton btnRetour = creerBouton("Retour");
//        btnRetour.setPreferredSize(new Dimension(120, 30)); // Taille préférée du bouton
//        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Layout pour aligner à droite
//        panel.setOpaque(false); // Rend le fond du panneau transparent
//        panel.add(btnRetour);
//        panel.setBorder(new EmptyBorder(20, 0, 0, 50)); // Marge haute et droite
//        return panel;
//    }
//
//    /**
//     * Crée le panneau contenant le bouton "Sauvegarder" aligné à gauche en bas.
//     *
//     * @return Le JPanel contenant le bouton "Sauvegarder".
//     */
//    private JPanel creerBoutonSauvegarde() {
//        JButton btnSauvegarder = creerBouton("Sauvegarder");
//        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Layout pour aligner à gauche
//        panel.setOpaque(false); // Rend le fond du panneau transparent
//        panel.add(btnSauvegarder);
//        panel.setBorder(new EmptyBorder(0, 50, 20, 0)); // Marge gauche et basse
//        return panel;
//    }
//
//    /**
//     * Crée le panneau contenant les boutons d'action principaux (Mes parties, Nouvelle partie, Didacticiel, Règles)
//     * disposés verticalement au centre.
//     *
//     * @return Le JPanel contenant les boutons d'action.
//     */
//    private JPanel creerBoutonsActions() {
//        JPanel container = new JPanel(new GridBagLayout()); // Utilisation de GridBagLayout pour centrer les boutons
//        container.setOpaque(false); // Rend le fond du panneau transparent
//        GridBagConstraints gbcBouton = new GridBagConstraints();
//        gbcBouton.fill = GridBagConstraints.HORIZONTAL; // Étirement horizontal des boutons
//        gbcBouton.insets = new Insets(10, 0, 10, 0); // Marges autour des boutons
//        gbcBouton.weightx = 1.0; // Distribution de l'espace horizontal supplémentaire
//
//        String[] labels = {"Mes parties", "Nouvelle partie", "Didacticiel", "Règles"};
//        for (int i = 0; i < labels.length; i++) {
//            JButton bouton = creerBouton(labels[i]);
//            gbcBouton.gridy = i; // Chaque bouton sur une nouvelle ligne
//            container.add(bouton, gbcBouton);
//        }
//        container.setBorder(new EmptyBorder(50, 50, 50, 50)); // Marges autour du conteneur de boutons
//        return container;
//    }
//
//    /**
//     * Crée le panneau contenant le bouton "Exit" aligné à droite en bas.
//     *
//     * @return Le JPanel contenant le bouton "Exit".
//     */
//    private JPanel creerBoutonExit() {
//        JButton btnExit = new JButton("Exit");
//        btnExit.setPreferredSize(new Dimension(100, 30)); // Taille préférée du bouton
//        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Layout pour aligner à droite
//        panel.setOpaque(false); // Rend le fond du panneau transparent
//        panel.add(btnExit);
//        panel.setBorder(new EmptyBorder(0, 0, 20, 50)); // Marge basse et droite
//        return panel;
//    }
//
//    /**
//     * Crée le panneau affichant les statistiques du jeu (round, victoires, durée totale).
//     *
//     * @return Le JPanel contenant le tableau de statistiques.
//     */
//    private JPanel creerTableauDeStatistiques() {
//        JPanel tableau = new JPanel(new GridBagLayout()); // Utilisation de GridBagLayout pour organiser les labels et les valeurs
//        tableau.setOpaque(false); // Rend le fond du panneau transparent
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 10, 5, 10); // Marges autour des labels et valeurs
//        gbc.anchor = GridBagConstraints.WEST; // Alignement à gauche par défaut
//
//        Font font = new Font("SansSerif", Font.BOLD, 18); // Police pour les statistiques
//        Color textColor = Color.BLACK; // Couleur du texte
//
//        // Ligne 1 : Affichage du round actuel
//        gbc.gridx = 0; // Première colonne
//        gbc.gridy = 0; // Première ligne
//        tableau.add(creerLabelStat("Round :", font, textColor, GridBagConstraints.EAST), gbc); // Label "Round :" aligné à droite
//        gbc.gridx = 1; // Deuxième colonne
//        gbc.weightx = 1.0; // La valeur prend l'espace horizontal disponible
//        gbc.fill = GridBagConstraints.HORIZONTAL; // Étirement horizontal de la valeur
//        tableau.add(creerLabelStat(String.valueOf(round), font, textColor, GridBagConstraints.WEST), gbc); // Valeur du round alignée à gauche
//        gbc.weightx = 0.0; // Réinitialisation du poids
//        gbc.fill = GridBagConstraints.NONE; // Réinitialisation du fill
//
//        // Ligne 2 : Affichage du score des joueurs
//        gbc.gridx = 0; // Première colonne
//        gbc.gridy = 1; // Deuxième ligne
//        tableau.add(creerLabelStat("Victoires :", font, textColor, GridBagConstraints.EAST), gbc); // Label "Victoires :" aligné à droite
//        gbc.gridx = 1; // Deuxième colonne
//        JLabel lblScore = new JLabel();
//        lblScore.setFont(font);
//        // Affichage du score avec une couleur différente pour le joueur ayant le score le plus élevé
//        if (scoreJoueurA > scoreJoueurB) {
//            lblScore.setText(String.format("<html><font color='green'>%s</font>: %d, <font color='red'>%s</font>: %d</html>", joueurA, scoreJoueurA, joueurB, scoreJoueurB));
//        } else if (scoreJoueurA < scoreJoueurB) {
//            lblScore.setText(String.format("<html><font color='red'>%s</font>: %d, <font color='green'>%s</font>: %d</html>", joueurA, scoreJoueurA, joueurB, scoreJoueurB));
//        } else {
//            lblScore.setText(joueurA + ": " + scoreJoueurA + ", " + joueurB + ": " + scoreJoueurB);
//        }
//        tableau.add(lblScore, gbc);
//
//        // Ligne 3 : Affichage de la durée totale de la session
//        gbc.gridx = 0; // Première colonne
//        gbc.gridy = 2; // Troisième ligne
//        tableau.add(creerLabelStat("Durée totale :", font, textColor, GridBagConstraints.EAST), gbc); // Label "Durée totale :" aligné à droite
//        gbc.gridx = 1; // Deuxième colonne
//        long totalSeconds = duree.getSeconds();
//        long heures = totalSeconds / 3600;
//        long minutes = (totalSeconds % 3600) / 60;
//        long secondes = totalSeconds % 60;
//        String temps = String.format("%02d:%02d:%02d", heures, minutes, secondes); // Formatage de la durée en HH:MM:SS
//        tableau.add(creerLabelStat(temps, font, textColor, GridBagConstraints.WEST), gbc); // Valeur de la durée alignée à gauche
//
//        // Ajout d'une bordure esthétique autour du tableau de statistiques
//        tableau.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(new Color(150, 150, 150), 2), // Bordure extérieure grise
//                new EmptyBorder(20, 20, 20, 20) // Marge intérieure
//        ));
//        return tableau;
//    }
//
//    /**
//     * Crée un JLabel avec la police, la couleur et l'alignement spécifiés.
//     *
//     * @param texte     Le texte du label.
//     * @param font      La police à utiliser.
//     * @param couleur   La couleur du texte.
//     * @param alignement L'alignement du texte (SwingConstants.LEFT ou SwingConstants.RIGHT).
//     * @return Le JLabel créé.
//     */
//    private JLabel creerLabelStat(String texte, Font font, Color couleur, int alignement) {
//        JLabel label = new JLabel(texte);
//        label.setFont(font);
//        label.setForeground(couleur);
////        label.setHorizontalAlignment(alignement);
//        return label;
//    }
//}