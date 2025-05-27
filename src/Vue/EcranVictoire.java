package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe représentant l'écran de victoire affiché lorsque la partie est terminée.
 */
public class EcranVictoire extends JPanel {
    private final InterfaceGraphique interfaceGraphique;
    private final String gagnant;

    /**
     * Constructeur principal d'EcranVictoire
     *
     * @param gagnant Nom du joueur gagnant.
     * @param interfaceGraphique Référence à l'interface graphique principale pour la navigation.
     */
    public EcranVictoire(String gagnant, InterfaceGraphique interfaceGraphique) {
        this.gagnant = gagnant;
        this.interfaceGraphique = interfaceGraphique;
        initialiserInterface();
    }

    /**
     * Initialise l'interface utilisateur pour l'écran de victoire.
     */
    private void initialiserInterface() {
        setLayout(new BorderLayout());
        setBackground(new Color(34, 139, 34)); // Vert foncé pour célébrer la victoire

        // Message principal
        JLabel labelVictoire = new JLabel("Félicitations " + gagnant + " !");
        labelVictoire.setFont(new Font("Arial", Font.BOLD, 36));
        labelVictoire.setForeground(Color.WHITE);
        labelVictoire.setHorizontalAlignment(SwingConstants.CENTER);

        // Bouton pour revenir au menu principal
        JButton boutonMenuPrincipal = creerBoutonNavigation("Retour au menu principal", e -> retournerAuMenu());

        // Bouton pour rejouer une nouvelle partie
        JButton boutonRejouer = creerBoutonNavigation("Rejouer", e -> rejouerPartie());

        // Mise en page des boutons
        JPanel panelBoutons = new JPanel();
        panelBoutons.setLayout(new GridLayout(1, 2, 20, 20));
        panelBoutons.setOpaque(false);
        panelBoutons.add(boutonMenuPrincipal);
        panelBoutons.add(boutonRejouer);

        // Espacement en bas
        JPanel panelInferieur = new JPanel(new BorderLayout());
        panelInferieur.setOpaque(false);
        panelInferieur.add(panelBoutons, BorderLayout.CENTER);
        panelInferieur.add(Box.createVerticalStrut(20), BorderLayout.SOUTH);

        // Ajout des composants à la fenêtre
        add(labelVictoire, BorderLayout.CENTER);
        add(panelInferieur, BorderLayout.SOUTH);
    }

    /**
     * Crée un bouton avec une action d'écoute.
     *
     * @param texte Texte du bouton.
     * @param action Événement de clic du bouton.
     * @return JButton créé.
     */
    private JButton creerBoutonNavigation(String texte, ActionListener action) {
        JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Arial", Font.PLAIN, 20));
        bouton.setBackground(Color.WHITE);
        bouton.setFocusable(false);
        bouton.addActionListener(action);
        return bouton;
    }

    /**
     * Action pour retourner au menu principal.
     */
    private void retournerAuMenu() {
        interfaceGraphique.ouvrirMenu();
    }

    /**
     * Action pour rejouer une partie.
     */
    private void rejouerPartie() {
        interfaceGraphique.lancerPlateauDeJeu();
    }
}