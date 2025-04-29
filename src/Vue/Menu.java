package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;

import static Global.Config.PATH_IMAGE_ARRIERE_PLAN_MENU;
import static Vue.Utils.creerBoutonActionMenu;
import static Vue.Utils.mettreImageEnFond;

/**
 * La classe Menu représente l'écran de menu principal avec les statistiques du jeu.
 * Elle est liée au modèle (Jeu) et agit en tant qu'observateur pour réagir aux mises à jour.
 */
public class Menu extends JPanel implements Observateur {

    // Références au modèle et à la vue
    private final Jeu jeu;
    private final CollecteurEvenements collecteurEv;
    private final InterfaceGraphique interfaceGraphique;

    // Données simulées (exemple) pour affichage
    private int round;
    private Duration duree;
    private String joueurA, joueurB;
    private int scoreJoueurA, scoreJoueurB;


    /**
     * Constructeur du menu principal.
     *
     * @param jeu le modèle du jeu
     * @param collecteurEv le gestionnaire d'événements
     * @param interfaceGraphique l'interface principale
     */
    public Menu(Jeu jeu, CollecteurEvenements collecteurEv, InterfaceGraphique interfaceGraphique){
        this.jeu = jeu;
        this.collecteurEv = collecteurEv;
        this.interfaceGraphique = interfaceGraphique;

        // --- Données factices à afficher ---
        round = 7;
        duree = Duration.ofMinutes(24);
        joueurA = "Kevin";
        joueurB = "IA";
        scoreJoueurA = 4;
        scoreJoueurB = 3;

        setLayout(new GridLayout(5, 1, 40, 40));

        add(creerBoutonRetour());
        add(creerTableauDeStatistiques());
        add(creerBoutonSauvegarde());
        add(creerBoutonsActions());
        add(creerBoutonExit());
    }

    @Override
    public void miseAJour() {
        // À compléter si on veut rafraîchir les stats dynamiquement
    }


    /**
     * Crée le Bouton "Sauvegarde"
     * @return JPanel   */
    private JPanel creerBoutonSauvegarde() {
        JButton btnSauvegarder = new JButton("Sauvegarder");
        JPanel panelSauvegarder = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        panelSauvegarder.setOpaque(false);
        panelSauvegarder.setBackground(new Color(85, 165, 83));
        panelSauvegarder.add(btnSauvegarder);
        return panelSauvegarder;
    }

    /**
     * Crée un Bouton "Retour" en haut à droite sur {@code this}    */
    private JPanel creerBoutonRetour(){
        JButton btnRetour = new JButton("Retour");
        JPanel topPanel = new JPanel(new BorderLayout());
//        topPanel.setOpaque(false);
        topPanel.setBackground(new Color(78, 115, 207));
        topPanel.add(btnRetour, BorderLayout.EAST);
        return topPanel;
    }



    /**
     * Crée des boutons d'action (Mes parties, Nouvelle partie, Didacticiel, Règles),
     * tous de même taille, alignés à gauche, redimensionnables avec la fenêtre.
     * @return JPanel contenant les boutons.
     */
    private JPanel creerBoutonsActions() {
        // Panel principal avec BoxLayout pour centrage horizontal possible
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
//        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0)); // Décalage à gauche

        // Sous-panel avec GridLayout pour garantir même taille
        JPanel menuBoutons = new JPanel(new GridLayout(4, 1, 0, 10)); // 4 lignes, 1 colonne, 10px de marge verticale
//        menuBoutons.setOpaque(false);
        menuBoutons.setPreferredSize(new Dimension(0, 300));
        menuBoutons.setBackground(new Color(218, 88, 189));

        // Boutons + Ajout dans le panneau grid
        menuBoutons.add(creerBoutonActionMenu("Mes parties"));
        menuBoutons.add(creerBoutonActionMenu("Nouvelle partie"));
        menuBoutons.add(creerBoutonActionMenu("Didacticiel"));
        menuBoutons.add(creerBoutonActionMenu("Règles"));

        container.add(menuBoutons);
        container.add(Box.createHorizontalGlue());
        container.add(Box.createHorizontalGlue());
        container.setBackground(new Color(168, 129, 164));

        return container;
    }




    /**
     * Crée le Bouton "Exit" en bas à droite
     * @return JPanel     */
    private JPanel creerBoutonExit() {
        JButton btnExit = new JButton("Exit");
        JPanel bottomPanel = new JPanel(new BorderLayout());
//        bottomPanel.setOpaque(false);
        bottomPanel.add(btnExit, BorderLayout.EAST);
        bottomPanel.setBackground(new Color(221, 156, 116));
        return bottomPanel;
    }


    /**
     * Crée un panneau contenant les statistiques du jeu (round, score, durée).
     *
     * @return le panneau de statistiques     */
    private JPanel creerTableauDeStatistiques() {
        JPanel tableau = new JPanel();
        tableau.setLayout(new BoxLayout(tableau, BoxLayout.X_AXIS));
//        tableau.setOpaque(false); // Fond transparent
        tableau.setBackground(new Color(74, 172, 153));

        Font font = new Font("SansSerif", Font.BOLD, 18);

        // --- Partie gauche : libellés alignés à droite ---
        JPanel gauche = new JPanel();
        gauche.setLayout(new BoxLayout(gauche, BoxLayout.Y_AXIS));
        gauche.setOpaque(false);
        gauche.add(creerLigneDroite("Round :", font));
        gauche.add(creerLigneDroite("Victoires :", font));
        gauche.add(creerLigneDroite("Durée totale :", font));

        // --- Partie droite : valeurs dynamiques alignées à gauche ---
        JPanel droite = new JPanel();
        droite.setLayout(new BoxLayout(droite, BoxLayout.Y_AXIS));
        droite.setOpaque(false);

        // Round actuel
        droite.add(creerLigneGauche(String.valueOf(round), font));

        // Score des joueurs avec couleur dynamique selon le gagnant
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
        droite.add(creerLigneGauche(lblScore, font));

        // Formatage de la durée en hh:mm:ss
        long totalSeconds = duree.getSeconds();
        long heures = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long secondes = totalSeconds % 60;
        String temps = String.format("%02d:%02d:%02d", heures, minutes, secondes);
        droite.add(creerLigneGauche(temps, font));

        // --- Composition finale ---
        tableau.add(Box.createHorizontalGlue());
        tableau.add(gauche);
        tableau.add(Box.createHorizontalStrut(20)); // Espace entre gauche et droite
        tableau.add(droite);
        tableau.add(Box.createHorizontalGlue());

        return tableau;
    }


    /**
     * Crée une ligne contenant un libellé aligné à droite.
     *
     * @param texte le texte du libellé
     * @param font la police à utiliser
     * @return le panneau contenant le libellé    */
    private JPanel creerLigneDroite(String texte, Font font) {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ligne.setOpaque(false);
        JLabel label = new JLabel(texte);
        label.setFont(font);
        ligne.add(label);
        return ligne;
    }



    /**
     * Crée une ligne contenant une valeur textuelle alignée à gauche.
     *
     * @param texte la valeur à afficher
     * @param font la police à utiliser
     * @return le panneau contenant la valeur     */
    private JPanel creerLigneGauche(String texte, Font font) {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ligne.setOpaque(false);
        JLabel label = new JLabel(texte);
        label.setFont(font);
        ligne.add(label);
        return ligne;
    }


    /**
     * Crée une ligne contenant un JLabel personnalisé aligné à gauche.
     *
     * @param label le JLabel déjà configuré
     * @param font la police à appliquer
     * @return le panneau contenant le JLabel     */
    private JPanel creerLigneGauche(JLabel label, Font font) {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ligne.setOpaque(false);
        label.setFont(font);
        ligne.add(label);
        return ligne;
    }
}
