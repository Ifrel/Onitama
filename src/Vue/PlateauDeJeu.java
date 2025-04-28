
package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

import static Global.Config.*;
import static Vue.Utlis.Button.*;

public class PlateauDeJeu extends JPanel implements Observateur {
    private final Jeu jeu;
    private final CollecteurEvenements collecteurEv;

    private JPanel terrain;
    private JPanel terrainCartesAnnulerRefaire;
    private JPanel annulerRefaire;
    private JPanel barreIndication;

    private JButton[][] buttonsTerrain;
    private JButton[] buttonsCartes;
    private JButton annuler, refaire;

    private JLabel nomJoueurCourant;
    private JLabel temps;

    private int numRound;


    //    private final ThreadLocal<JPanel> plateauEtCartes = new ThreadLocal<JPanel>();

    /** Crée le plateau du de jeu  */
    public PlateauDeJeu(Jeu jeu, CollecteurEvenements collecteurEv) {
        this.jeu = jeu;
        this.collecteurEv = collecteurEv;

        System.err.println("Interface Plateau de jeu lancée");
        setLayout(new BorderLayout());
        setBackground(COULEUR_PLATEAU); // Gris foncé

        creerTerrain();
        creerButtonsCartes();
        creerBarreIndication();
        creerButtonsAnnulerRefaire();
        creerPlateauCartesAnnulerRefaire();

        add(barreIndication, BorderLayout.NORTH);
        add(terrainCartesAnnulerRefaire, BorderLayout.CENTER);


    }

    @Override
    public void miseAJour() {
        //TODO
    }


    private void creerBarreIndication() {
        barreIndication = new JPanel(new BorderLayout());
        barreIndication.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // marges internes
        barreIndication.setBackground(Color.WHITE);

        // Partie texte : "C'est au tour de Kevin"
        JLabel txt = new JLabel("C'est au tour de");
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);

        nomJoueurCourant = new JLabel("Kevin");
        nomJoueurCourant.setFont(new Font("Arial", Font.BOLD, 28));
        nomJoueurCourant.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel textNom = new JPanel();
        textNom.setLayout(new BoxLayout(textNom, BoxLayout.Y_AXIS));
        textNom.setOpaque(false); // Pas de fond gris
        textNom.add(txt);
        textNom.add(Box.createRigidArea(new Dimension(0, 5))); // espace vertical
        textNom.add(nomJoueurCourant);

        // Partie Round et Temps
        JPanel roundTemps = new JPanel();
        roundTemps.setLayout(new BoxLayout(roundTemps, BoxLayout.X_AXIS));
        roundTemps.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 206, 206), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        roundTemps.setBackground(new Color(245, 245, 245));

        numRound = 1;
        JLabel round = new JLabel("Round: " + numRound);
        round.setFont(new Font("Arial", Font.PLAIN, 20));
        round.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20)); // espace entre round et temps

        temps = new JLabel("00:00");
        temps.setFont(new Font("Arial", Font.PLAIN, 20));

        Instant debut = Instant.now();
        Timer timer = new Timer(1000, e -> miseAjourCreerSectionTemps(debut));
        timer.start();

        roundTemps.add(round);
        roundTemps.add(temps);

        // Bouton Menu
        JButton menu = new JButton("≡");
        menu.setPreferredSize(new Dimension(60, 40));

        // Assemblage horizontal : textNom | espace | roundTemps | espace | menu
        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.X_AXIS));
        contenu.setOpaque(false);

        contenu.add(textNom);
        contenu.add(Box.createHorizontalGlue()); // pousse tout vers les extrémités
        contenu.add(roundTemps);
        contenu.add(Box.createRigidArea(new Dimension(20, 0))); // petit espace fixe
        contenu.add(menu);

        barreIndication.add(contenu, BorderLayout.CENTER);
    }




    /** Crée le terrain de jeu */
    private void creerTerrain() {
        terrain = new JPanel(new GridLayout(LIGNES, COLONNES, 0, 0)); // Grille régulière avec marges 0px
        buttonsTerrain = new JButton[LIGNES][COLONNES];

        // Bordure jolie avec coins arrondis
        terrain.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 206, 206), 5, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        terrain.setBackground(new Color(226, 226, 226));

        for (int row = 0; row < LIGNES; row++) {
            for (int col = 0; col < COLONNES; col++) {
                JButton bouton = creerBoutonPlateau();
                buttonsTerrain[row][col] = bouton;
                terrain.add(bouton);
            }
        }
    }

    /** Initialise les buttons avec les images des cartes tirées par le jeu  */
    private void creerButtonsCartes(){
        buttonsCartes =  new JButton[NOMBRES_CARTES_PLATEAU];
        for (int row = 0; row < LIGNES; row++) {
            JButton bouton = new JButton("Clique-moi");
            bouton.setPreferredSize(new Dimension(200, 100));

            buttonsCartes[row] = bouton;
        }
    }

    /** Crée les boutons Annuler et Refaire */
    private void creerButtonsAnnulerRefaire() {
        annulerRefaire = new JPanel(new BorderLayout());
        annulerRefaire.setLayout(new GridLayout(2, 1, 10, 10)); // 2 lignes, 1 colonne, 10px d'écart
        annulerRefaire.setPreferredSize(new Dimension(200, 50));

        annuler = creerBoutonAnnuler();
        refaire = creerBoutonRefaire();

        annulerRefaire.add(annuler);
        annulerRefaire.add(refaire);
    }


    /** Met ensemble les cartes, les boutons annuler/refaire et le terrain de jeu */
    private void creerPlateauCartesAnnulerRefaire() {
        terrainCartesAnnulerRefaire = new JPanel(new BorderLayout(80, 40));

        // Nord : 2 cartes alignées au centre
        JPanel cartesAuNord = new JPanel(new GridLayout(1, 2, 40, 0));
        cartesAuNord.setBorder(BorderFactory.createEmptyBorder(50, 400, 0, 400));
        cartesAuNord.add(buttonsCartes[0]);
        cartesAuNord.add(buttonsCartes[1]);


        // Sud : 2 cartes alignées au centre
        JPanel cartesAuSud = new JPanel(new GridLayout(1, 2, 40, 0));
        cartesAuSud.setBorder(BorderFactory.createEmptyBorder(0, 400, 50, 400));
        cartesAuSud.add(buttonsCartes[2]);
        cartesAuSud.add(buttonsCartes[3]);

        // Ouest : 1 carte centrée verticalement
        JPanel carteGauche = new JPanel(new GridLayout(1, 1, 40, 40));
        carteGauche.setBorder(BorderFactory.createEmptyBorder(225, 50, 225, 0));
        carteGauche.add(buttonsCartes[4]);

        // Est : annuler/refaire empilés verticalement
        JPanel droite = new JPanel(new GridLayout(1, 1, 40, 40));
        droite.setBorder(BorderFactory.createEmptyBorder(200, 0, 200, 50));
        droite.add(annulerRefaire);

        // Centre : le terrain
        JPanel centre = new JPanel(new BorderLayout());
        centre.add(terrain, BorderLayout.CENTER);

        // Placement général
        terrainCartesAnnulerRefaire.add(cartesAuNord, BorderLayout.NORTH);
        terrainCartesAnnulerRefaire.add(cartesAuSud, BorderLayout.SOUTH);
        terrainCartesAnnulerRefaire.add(carteGauche, BorderLayout.WEST);
        terrainCartesAnnulerRefaire.add(droite, BorderLayout.EAST);
        terrainCartesAnnulerRefaire.add(centre, BorderLayout.CENTER);
    }


    ///  === Fonctions de mise à jour ===
    private void miseAjourCreerSectionTemps(Instant debut) {
        Duration duration = Duration.between(debut, Instant.now());
        long minutes = duration.toMinutes();
        long secondes = duration.getSeconds() % 60;
        temps.setText(String.format("%02d:%02d", minutes, secondes));
    }

}


//package Vue;
//
//import Modele.Jeu;
//import Patterns.Observateur;
//import javax.swing.*;
//import java.awt.*;
//
//import static Global.Config.*;
//import static Vue.Utlis.Button.*;
//import static Vue.Utlis.Panel.creerPanel;
//
//public class PlateauDeJeu extends JPanel implements Observateur {
//    private Jeu jeu;
//    private CollecteurEvenements collecteurEv;
//
//    private JPanel terrain;
//    private JPanel terrainCartesAnnulerRefaire;
//    private JPanel annulerRefaire;
//
//    private JButton[][] buttonsTerrain;
//    private JButton[] buttonsCartes;
//    private JButton annuler, refaire;
//
//
//    //    private final ThreadLocal<JPanel> plateauEtCartes = new ThreadLocal<JPanel>();
//
//    /** Crée le plateau du de jeu  */
//    public PlateauDeJeu(Jeu jeu, CollecteurEvenements collecteurEv) {
//        this.jeu = jeu;
//        this.collecteurEv = collecteurEv;
//
//        System.err.println("Interface Plateau de jeu lancée");
//        setLayout(new BorderLayout());
//        setBackground(COULEUR_PLATEAU); // Gris foncé
//
//        creerTerrain();
//        creerButtonsCartes();
//        creerButtonsAnnulerRefaire();
//        creerPlateauCartesAnnulerRefaire();
//        add(terrainCartesAnnulerRefaire, BorderLayout.CENTER);
//    }
//
//    @Override
//    public void miseAJour() {
//       //TODO
//    }
//
//
//
//
//    /** Crée le terrain de jeu */
//    private void creerTerrain() {
//        terrain = new JPanel(new GridLayout(LIGNES, COLONNES, 0, 0)); // Grille régulière avec marges 0px
//        buttonsTerrain = new JButton[LIGNES][COLONNES];
//
//        // Bordure jolie avec coins arrondis
//        terrain.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(new Color(206, 206, 206), 5, true),
//                BorderFactory.createEmptyBorder(30, 30, 30, 30)
//        ));
//        terrain.setBackground(new Color(226, 226, 226));
//
//        for (int row = 0; row < LIGNES; row++) {
//            for (int col = 0; col < COLONNES; col++) {
//                JButton bouton = creerBoutonPlateau();
//                buttonsTerrain[row][col] = bouton;
//                terrain.add(bouton);
//            }
//        }
//    }
//
//    /** Initialise les buttons avec les images des cartes tirées par le jeu  */
//    private void creerButtonsCartes(){
//        buttonsCartes =  new JButton[NOMBRES_CARTES_PLATEAU];
//        for (int row = 0; row < LIGNES; row++) {
//            JButton bouton = new JButton("Clique-moi");
//            bouton.setPreferredSize(new Dimension(150, 70));
//
//            buttonsCartes[row] = bouton;
//        }
//    }
//
//    /** Crée les boutons Annuler et Refaire */
//    private void creerButtonsAnnulerRefaire() {
//        annulerRefaire = new JPanel(new BorderLayout());
//        annulerRefaire.setLayout(new GridLayout(2, 1, 10, 10)); // 2 lignes, 1 colonne, 10px d'écart
//        annulerRefaire.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 25));
//
//        annuler = creerBoutonAnnuler();
//        refaire = creerBoutonRefaire();
//
//        annulerRefaire.add(annuler);
//        annulerRefaire.add(refaire);
//    }
//
//
//
//    /** Met ensemble les cartes, les boutons annuler/refaire et le terrain de jeu */
//    private void creerPlateauCartesAnnulerRefaire() {
//        terrainCartesAnnulerRefaire = new JPanel(new BorderLayout());
//
//        // Nord : 2 cartes alignées au centre
//        JPanel cartesAuNord = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
//        cartesAuNord.add(buttonsCartes[0]);
//        cartesAuNord.add(buttonsCartes[1]);
//
//        // Sud : 2 cartes alignées au centre
//        JPanel cartesAuSud = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
//        cartesAuSud.add(buttonsCartes[2]);
//        cartesAuSud.add(buttonsCartes[3]);
//
//        // Ouest : 1 carte centrée verticalement
//        JPanel carteGauche = new JPanel(new BorderLayout());
//        carteGauche.setBorder(BorderFactory.createEmptyBorder(100, 25, 0, 25));
//        carteGauche.add(buttonsCartes[4]);
//
//        // Est : annuler/refaire empilés verticalement
//        JPanel droite = new JPanel(new GridBagLayout());
//        droite.add(annulerRefaire);
//
//        // Centre : le terrain
//        JPanel centre = new JPanel(new BorderLayout());
//        centre.add(terrain, BorderLayout.CENTER);
//
//        // Placement général
//        terrainCartesAnnulerRefaire.add(cartesAuNord, BorderLayout.NORTH);
//        terrainCartesAnnulerRefaire.add(cartesAuSud, BorderLayout.SOUTH);
//        terrainCartesAnnulerRefaire.add(carteGauche, BorderLayout.WEST);
//        terrainCartesAnnulerRefaire.add(droite, BorderLayout.EAST);
//        terrainCartesAnnulerRefaire.add(terrain, BorderLayout.CENTER);
//    }
//
//
//}
