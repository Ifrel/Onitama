package Vue;

import Modele.Jeu;
import Patterns.Observateur;
import Vue.Adaptateurs.*;
import Vue.Utils.Boutons.Bouton;
import Vue.Utils.PanelAvecImage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

import static Global.Paths.PATH_ARRIERE_PLAN_4;
import static Global.Paths.PATH_BOUTON;

/**
 * La classe {@code EcranMenu} représente l'écran de menu principal du jeu.
 * Elle affiche des options de navigation comme "Nouvelle Partie", "Reprendre", "Règles", etc.,
 * et affiche désormais des statistiques de jeu.
 * Elle implémente l'interface {@link Observateur} pour réagir aux mises à jour du modèle {@link Jeu}.
 * L'arrière-plan de cet écran est une image.
 *
 * @see PanelAvecImage
 * @see Observateur
 * @see Jeu
 * @see CollecteurEvenements
 * @see InterfaceGraphique
 */
public class EcranMenu extends PanelAvecImage implements Observateur {

    private static final Logger LOGGER = Logger.getLogger(EcranMenu.class.getName());
    private final Jeu jeu;

    // Constantes pour les dimensions et espacements
    private static final Dimension DIM_BOUTON_ACTION = new Dimension(250, 65);
    private final InterfaceGraphique interfaceGraphique;
    private static final Dimension DIM_BOUTON_RETOUR = new Dimension(60, 60);
    private static final Dimension DIM_BOUTON_SAUVEGARDER = new Dimension(200, 60);
    private static final Dimension DIM_BOUTON_EXIT = new Dimension(120, 60);
    private static final Insets MARGE_BOUTONS_ACTION = new Insets(10, 0, 10, 0);
    private static final Insets MARGE_PANEL_RETOUR = new Insets(10, 0, 0, 20);
    private static final Insets MARGE_PANEL_SAUVEGARDER = new Insets(0, 50, 20, 0);
    private static final Insets MARGE_PANEL_EXIT = new Insets(0, 0, 20, 50);
    private static final Insets MARGE_CONTENEUR_ACTIONS = new Insets(20, 50, 20, 50);
    private static final Insets MARGE_PANEL_STATS = new Insets(20, 20, 20, 20);
    private final CollecteurEvenements collecteurEvenements;

    // Composants UI pour les statistiques
    private JLabel roundValue;
    private JLabel dureePartieValue;
    private JLabel nomJoueurAValue;
    private JLabel scoreJoueurAValue;
    private JLabel nomJoueurBValue;
    private JLabel scoreJoueurBValue;

    /**
     * Constructeur de l'écran de menu principal.
     * Initialise les composants graphiques, les écouteurs d'événements et met à jour les données initiales.
     *
     * @param jeu                L'instance du modèle de jeu, fournissant les données.
     * @param collecteurEv       Le collecteur d'événements pour gérer les interactions utilisateur.
     * @param interfaceGraphique L'interface graphique principale, permettant par exemple de fermer ce menu.
     */
    public EcranMenu(Jeu jeu, CollecteurEvenements collecteurEv, InterfaceGraphique interfaceGraphique) {
        super(PATH_ARRIERE_PLAN_4);
        this.jeu = jeu;
        this.collecteurEvenements = collecteurEv;
        this.interfaceGraphique = interfaceGraphique;

        this.jeu.ajouteObservateur(this);

        initialiserLayout();
        miseAJour();
    }



    /**
     * Méthode appelée lorsque le modèle {@link Jeu} notifie un changement.
     * Met à jour les données affichées ou utilisées par cet écran.
     * <p>
     * Note: Les mises à jour des composants Swing sont effectuées sur l'Event Dispatch Thread (EDT)
     * via {@code SwingUtilities.invokeLater()} pour garantir la sécurité des threads.
     * </p>
     */
    @Override
    public void miseAJour() {
        LOGGER.info("Mise à jour des données de EcranMenu depuis le modèle Jeu.");

        SwingUtilities.invokeLater(() -> {
            roundValue.setText(String.valueOf(jeu.getNumeroRound()));

            Duration duree = Duration.ofSeconds(jeu.getTempsDeJeu());
            long minutes = duree.toMinutes();
            long secondes = duree.minusMinutes(minutes).getSeconds();
            dureePartieValue.setText(String.format("%02d:%02d", minutes, secondes));

            // Mise à jour des infos Joueur 1
            if (jeu.getJoueur(1) != null) {
                nomJoueurAValue.setText(jeu.getNomJoueur1() + ": ");
                scoreJoueurAValue.setText("" + jeu.getJoueur(1).getScore());
            } else {
                nomJoueurAValue.setText("Non défini: ");
                scoreJoueurAValue.setText("0");
            }

            // Mise à jour des infos Joueur 2
            if (jeu.getJoueur(2) != null) {
                nomJoueurBValue.setText(jeu.getNomJoueur2() + ": ");
                scoreJoueurBValue.setText("" + jeu.getJoueur(2).getScore());
            } else {
                nomJoueurBValue.setText("Non défini: ");
                scoreJoueurBValue.setText("0");
            }
        });

        LOGGER.info("Mise à jour de EcranMenu terminée.");
    }


    /**
     * Initialise et configure le layout et les composants de l'écran de menu.
     */
    private void initialiserLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Ligne -1 : Espace en haut
        gbc.gridy = -1;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(Box.createVerticalStrut(200), gbc); // Espace fixe en haut
        gbc.weighty = 0;

        // Ligne 0 : Bouton Retour (en haut à droite)
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        add(creerPanelRetour(), gbc);

        // Ligne 1 : Panneau des statistiques (en haut à gauche)
        gbc.gridy = 1;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(creerPanelStats(), gbc);

        // Ligne 2 : Remplissage pour pousser le contenu vers le bas
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(Box.createVerticalGlue(), gbc);

        // Ligne 3 : Panneau des boutons d'action principaux (au centre)
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(creerPanelBoutonsActions(), gbc);

        // Ligne 4 : Petit espace avant le bas de page
        gbc.gridy = 4;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(Box.createVerticalStrut(20), gbc);

        // Ligne 5 : Panneau du bas (Sauvegarder à gauche, Exit à droite)
        gbc.gridy = 5;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(creerPanelBasDePage(), gbc);

        // Ligne 6 : Espace en bas
        gbc.gridy = 6;
        gbc.weighty = 0.25;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(Box.createVerticalStrut(20), gbc); // Espace fixe en bas
    }




    /**
     * Crée et configure le panneau d'affichage des statistiques de jeu.
     * Les labels sont initialisés ici, mais leurs valeurs sont mises à jour dans {@code miseAJour()}.
     *
     * @return Le {@link JPanel} contenant les statistiques.
     */
    private JPanel creerPanelStats() {
        JPanel panelStats = new JPanel(new GridBagLayout());
        panelStats.setOpaque(false);
        panelStats.setBorder(new EmptyBorder(MARGE_PANEL_STATS));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.fill = GridBagConstraints.VERTICAL;

        // Round
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST; // aligné à droite
        panelStats.add(createStyledLabel("Round:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // aligné à gauche
        roundValue = createStyledLabel("N/A");
        panelStats.add(roundValue, gbc);

        // Durée de la partie
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panelStats.add(createStyledLabel("Durée:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dureePartieValue = createStyledLabel("N/A");
        panelStats.add(dureePartieValue, gbc);

        // Séparateur
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelStats.add(Box.createVerticalStrut(30), gbc);
        gbc.gridwidth = 1;

        // Joueur 1 (Nom + Score)
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        nomJoueurAValue = createStyledLabel("N/A");
        panelStats.add(nomJoueurAValue, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        scoreJoueurAValue = createStyledLabel("N/A");
        panelStats.add(scoreJoueurAValue, gbc);

        // Joueur 2 (Nom + Score)
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        nomJoueurBValue = createStyledLabel("N/A");
        panelStats.add(nomJoueurBValue, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        scoreJoueurBValue = createStyledLabel("N/A");
        panelStats.add(scoreJoueurBValue, gbc);

        return panelStats;
    }


    /**
     * Helper method to create a styled JLabel.
     *
     * @param text The initial text for the label.
     * @return A styled JLabel instance.
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE); // Text color
        label.setFont(new Font("Arial", Font.PLAIN, 25)); // Font and size
        return label;
    }


    /**
     * Crée un panel contenant un bouton configuré, typiquement pour les boutons
     * en haut ou en bas de l'écran.
     *
     * @param imagePath           Chemin vers l'image du bouton.
     * @param dimension           Dimension souhaitée pour le bouton.
     * @param listener            Action à exécuter lors du clic sur le bouton.
     * @param flowLayoutAlignment Alignement du bouton dans le panel (e.g., FlowLayout.LEFT).
     * @param borderInsets        Marges intérieures pour le panel.
     * @return Un {@link JPanel} configuré avec le bouton.
     */
    private JPanel creerPanelAvecBouton(String imagePath, Dimension dimension, ActionListener listener, int flowLayoutAlignment, Insets borderInsets) {
        Bouton.BoutonAvecImage bouton = Bouton.creerBouton(imagePath, Bouton.ConfigurationParDefaut.SansBordure_transparent);
        bouton.setPreferredSize(dimension);
        if (listener != null) {
            bouton.addActionListener(listener);
        }

        JPanel panel = new JPanel(new FlowLayout(flowLayoutAlignment));
        panel.setOpaque(false); // Transparent pour voir l'arrière-plan de EcranMenu
        panel.add(bouton);
        if (borderInsets != null) {
            panel.setBorder(new EmptyBorder(borderInsets));
        }
        return panel;
    }


    /**
     * Crée le panneau contenant le bouton "Retour".
     *
     * @return Le {@link JPanel} avec le bouton "Retour".
     */
    private JPanel creerPanelRetour() {
        return creerPanelAvecBouton(
                PATH_BOUTON.resolve("decliner.png").toString(),
                DIM_BOUTON_RETOUR,
                e -> interfaceGraphique.fermerMenu(),
                FlowLayout.RIGHT,
                MARGE_PANEL_RETOUR
        );
    }


    /**
     * Crée le panneau contenant le bouton "Sauvegarder".
     *
     * @return Le {@link JPanel} avec le bouton "Sauvegarder".
     */
    private JPanel creerPanelSauvegarde() {
        return creerPanelAvecBouton(
                PATH_BOUTON.resolve("sauvegarder.png").toString(),
                DIM_BOUTON_SAUVEGARDER,
                new AdaptateurSauvegarder(collecteurEvenements),
                FlowLayout.LEFT,
                MARGE_PANEL_SAUVEGARDER
        );
    }



    /**
     * Crée le panneau contenant le bouton "Exit".
     *
     * @return Le {@link JPanel} avec le bouton "Exit".
     */
    private JPanel creerPanelExit() {
        return creerPanelAvecBouton(
                PATH_BOUTON.resolve("exit.png").toString(),
                DIM_BOUTON_EXIT,
                new AdaptateurExit(collecteurEvenements),
                FlowLayout.RIGHT,
                MARGE_PANEL_EXIT
        );
    }



    /**
     * Crée le panneau central contenant les boutons d'action principaux (Nouvelle partie, Règles, etc.).
     * Les boutons sont disposés verticalement.
     *
     * @return Le {@link JPanel} contenant les boutons d'action.
     */
    private JPanel creerPanelBoutonsActions() {
        JPanel panelActions = new JPanel(new GridBagLayout());
        panelActions.setOpaque(false);
        panelActions.setBorder(new EmptyBorder(MARGE_CONTENEUR_ACTIONS));

        GridBagConstraints gbcBouton = new GridBagConstraints();
        gbcBouton.gridx = 0;
        gbcBouton.fill = GridBagConstraints.HORIZONTAL; // Les boutons s'étirent horizontalement
        gbcBouton.weightx = 1.0; // Prend toute la largeur disponible dans le panelActions
        gbcBouton.insets = MARGE_BOUTONS_ACTION; // Marge entre les boutons

        // Liste des configurations pour les boutons d'action
        List<BoutonConfig> configs = Arrays.asList(
                new BoutonConfig("reprendre.png", AdaptateurReprendre::new),
                new BoutonConfig("mes_parties.png", AdaptateurMesParties::new),
                new BoutonConfig("nouvelle_partie.png", AdaptateurNouvellePartie::new),
                new BoutonConfig("didacticiel.png", AdaptateurDidacticiel::new),
                new BoutonConfig("regles.png", AdaptateurRegles::new)
        );

        for (int i = 0; i < configs.size(); i++) {
            BoutonConfig config = configs.get(i);
            Bouton.BoutonAvecImage bouton = Bouton.creerBouton(
                    PATH_BOUTON.resolve(config.nomImage).toString(),
                    Bouton.ConfigurationParDefaut.SansBordure_transparent
            );
            bouton.setPreferredSize(DIM_BOUTON_ACTION);
            bouton.addActionListener(config.adaptateurBuilder.apply(collecteurEvenements));

            gbcBouton.gridy = i;
            panelActions.add(bouton, gbcBouton);
        }
        return panelActions;
    }



    /**
     * Crée le panneau du bas de page, contenant le bouton de sauvegarde à gauche
     * et le bouton de sortie à droite.
     *
     * @return Le {@link JPanel} du bas de page.
     */
    private JPanel creerPanelBasDePage() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false); // Transparent

        bottomPanel.add(creerPanelSauvegarde(), BorderLayout.WEST);
        bottomPanel.add(creerPanelExit(), BorderLayout.EAST);

        return bottomPanel;
    }


    /**
     * Structure pour définir la configuration d'un bouton d'action.
     */
    private static class BoutonConfig {
        final String nomImage;
        final Function<CollecteurEvenements, ActionListener> adaptateurBuilder;

        BoutonConfig(String nomImage, Function<CollecteurEvenements, ActionListener> adaptateurBuilder) {
            this.nomImage = nomImage;
            this.adaptateurBuilder = adaptateurBuilder;
        }
    }


    public String getImagePath() {
        return PATH_ARRIERE_PLAN_4.toString();
    }
}