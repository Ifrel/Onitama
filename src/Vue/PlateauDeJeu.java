
package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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

    private Clip clip; // Pour gérer la musique
    private boolean musiqueActive = false; // État du son


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

    /**
     * Crée la barre d'indication en haut de la fenetre
     */
    private void creerBarreIndication() {
        barreIndication = new JPanel(new BorderLayout());
        barreIndication.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        barreIndication.setOpaque(false);

        // Bouton Activer/Désactiver musique
        JButton boutonSon = new JButton("son");
        boutonSon.setPreferredSize(new Dimension(60, 40));
        boutonSon.add(Box.createHorizontalStrut(20)); // Petit espace fixe de 20px
        boutonSon.addActionListener(e -> toggleMusique(boutonSon));

        // Partie texte : "C'est au tour de"
        JLabel txt = new JLabel("C'est au tour de");
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
        txt.setForeground(Color.WHITE); // Texte en blanc

        nomJoueurCourant = new JLabel("Kevin");
        nomJoueurCourant.setFont(new Font("Arial", Font.BOLD, 28));
        nomJoueurCourant.setAlignmentX(Component.CENTER_ALIGNMENT);
        nomJoueurCourant.setForeground(Color.WHITE); // Texte en blanc


        JPanel textNom = new JPanel();
        textNom.setLayout(new BoxLayout(textNom, BoxLayout.Y_AXIS));
        textNom.setOpaque(false);
        textNom.add(txt);
        textNom.add(Box.createRigidArea(new Dimension(0, 5)));
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
        round.setFont(new Font("Arial", Font.PLAIN, 25));
        round.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));

        temps = new JLabel("00:00");
        temps.setFont(new Font("Arial", Font.BOLD, 25));

        Instant debut = Instant.now();
        Timer timer = new Timer(1000, e -> miseAjourCreerSectionTemps(debut));
        timer.start();

        roundTemps.add(round);
        roundTemps.add(temps);

        // Bouton Menu
        JButton menu = new JButton("≡");
        menu.setPreferredSize(new Dimension(60, 40));

        // Assemblage horizontal
        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.X_AXIS));
        contenu.setOpaque(false);

        contenu.add(Box.createHorizontalGlue());
        contenu.add(textNom);
        contenu.add(Box.createRigidArea(new Dimension(120, 0)));
        contenu.add(roundTemps);
        contenu.add(Box.createRigidArea(new Dimension(50, 0)));

        // Placement dans la barreIndication
        barreIndication.add(boutonSon, BorderLayout.WEST); // bouton son à gauche
        barreIndication.add(contenu, BorderLayout.CENTER); // contenu au centre
        barreIndication.add(menu, BorderLayout.EAST);
    }


    /**
     *  Crée le terrain de jeu */
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

    /**
     * Initialise les buttons avec les images des cartes tirées par le jeu  */
    private void creerButtonsCartes(){
        buttonsCartes =  new JButton[NOMBRES_CARTES_PLATEAU];
        for (int row = 0; row < LIGNES; row++) {
            JButton bouton = new JButton("Clique-moi");
            bouton.setPreferredSize(new Dimension(200, 100));

            buttonsCartes[row] = bouton;
        }
    }

    /**
     * Crée les boutons Annuler et Refaire */
    private void creerButtonsAnnulerRefaire() {
        annulerRefaire = new JPanel(new BorderLayout());
        annulerRefaire.setLayout(new GridLayout(2, 1, 10, 10)); // 2 lignes, 1 colonne, 10px d'écart
        annulerRefaire.setPreferredSize(new Dimension(200, 50));

        annuler = creerBoutonAnnuler();
        refaire = creerBoutonRefaire();

        annulerRefaire.add(annuler);
        annulerRefaire.add(refaire);
    }


    /**
     * Met ensemble les cartes, les boutons annuler/refaire et le terrain de jeu */
    private void creerPlateauCartesAnnulerRefaire() {
        terrainCartesAnnulerRefaire = new JPanel(new BorderLayout(80, 40));
        terrainCartesAnnulerRefaire.setOpaque(false);

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


    private void toggleMusique(JButton boutonSon) {
        if (musiqueActive) {
            // Stop musique
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
            boutonSon.setText("off"); // Icône Muet
            musiqueActive = false;
        } else {
            // Lance musique
            jouerMusique("/vue/musique/son_1.wav"); //fichier dans le dossier ressource
            boutonSon.setText("on"); // Icône Son
            musiqueActive = true;
        }
    }


    private void jouerMusique(String chemin) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(chemin));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Musique en boucle
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
