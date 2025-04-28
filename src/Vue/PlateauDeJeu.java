
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
import java.util.Objects;

import static Global.Config.*;
import static Vue.Utils.Button.*;

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
     * Crée la barre d'indication en haut de la fenêtre  */
    private void creerBarreIndication() {
        barreIndication = new JPanel(new BorderLayout());
        barreIndication.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        barreIndication.setOpaque(false);

        // Bouton Activer/Désactiver musique
        JButton boutonSon = new JButton("son");
        boutonSon.setForeground(Color.WHITE);
        boutonSon.setFont(new Font("Arial", Font.PLAIN, 15));
        boutonSon.setBackground(new Color(237, 237, 237, 16));
        boutonSon.setContentAreaFilled(false);
        boutonSon.setFocusPainted(false);
        boutonSon.setPreferredSize(new Dimension(60, 40));
        boutonSon.addActionListener(e -> toggleMusique(boutonSon));

        // Partie texte
        JLabel txt = new JLabel("C'est au tour de");
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
        txt.setForeground(new Color(232, 231, 231));

        nomJoueurCourant = new JLabel("Kevin");
        nomJoueurCourant.setFont(new Font("Arial", Font.BOLD, 28));
        nomJoueurCourant.setAlignmentX(Component.CENTER_ALIGNMENT);
        nomJoueurCourant.setForeground(new Color(218, 214, 214));

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
        menu.setOpaque(false);
        menu.setContentAreaFilled(false);
        menu.setFocusPainted(false);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Arial", Font.PLAIN, 46));
        menu.setPreferredSize(new Dimension(60, 40));

        // --------- AJOUT : Wrapper avec un JPanel pour ajouter l'espacement
        JPanel panelMenu = new JPanel(new BorderLayout());
        panelMenu.setOpaque(false);
        panelMenu.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0)); // 20 pixels d'espace à droite
        panelMenu.add(menu, BorderLayout.CENTER);
        // ---------------------------------------------------------------

        // Assemblage horizontal
        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setOpaque(false);

        contenu.add(textNom, BorderLayout.CENTER);  // Texte centré
        contenu.add(roundTemps, BorderLayout.EAST); // Round/Temps à droite

        // Placement dans la barreIndication
        barreIndication.add(boutonSon, BorderLayout.WEST);  // Bouton son à gauche
        barreIndication.add(contenu, BorderLayout.CENTER);  // Texte au centre
        barreIndication.add(panelMenu, BorderLayout.EAST);  // Menu à droite avec espace
    }



    /**
     *  Crée le terrain de jeu */
    private void creerTerrain() {
        terrain = new JPanel(new GridLayout(LIGNES, COLONNES, 0, 0)); // Grille régulière avec marges 0px
        buttonsTerrain = new JButton[LIGNES][COLONNES];

        // Bordure jolie avec coins arrondis
        terrain.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 206, 206), 5, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
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
        JPanel carteGauche = new JPanel(new GridLayout(3, 1, 40, 40));
        carteGauche.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        carteGauche.add(Box.createHorizontalGlue());
        carteGauche.add(buttonsCartes[4]);
        carteGauche.add(Box.createHorizontalGlue());

        // Est : annuler/refaire empilés verticalement
        JPanel droite = new JPanel(new GridLayout(3, 1, 40, 40));
        droite.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));
        droite.add(Box.createHorizontalGlue());
        droite.add(annulerRefaire);
        droite.add(Box.createHorizontalGlue());

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
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(chemin)));
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
