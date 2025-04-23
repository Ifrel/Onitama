package Vue.VuePrincipale;

import Modele.Jeu;
import Patterns.Observateur;
import Vue.Adaptateurs.*;
import Vue.CollecteurEvenements;
import Vue.EcranVictoire;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;

import static Vue.Styles.Utils.creerTitledBorder;

public class BlocMenu extends JPanel implements Observateur {
    private Dimension dimScene;
    private String joueurA, joueurB;
    private String joueurCourant;
    private Jeu jeu;

    // === Boutons actions ===
    JButton btnAnnuler, btnRefaire, btnNouvellePartie, btnSauvegarder, btnReprendre;
    DefaultListModel<String> listeDesSauvegardes;
    CollecteurEvenements collecteurEvent;

    // === Utils ===
    Font labelFont, boutonFont, panelFont;
    private Instant debut, debut2;
    private Timer timer, timer2;
    private JLabel labelTempsTourBis, labelTempsGlobalBis;
    private EcranVictoire ecranVictoire;

    // === Pour mise à jour de joueurs ===
    JLabel labelNomA, labelNomB;

    public BlocMenu(Jeu jeu, CollecteurEvenements collecteurEvent) {
        this.jeu = jeu;
        this.dimScene = new Dimension(jeu.colonnes(), jeu.lignes());
        this.joueurA = jeu.getNameJoueurA();
        this.joueurB = jeu.getNameJoueurB();
        this.joueurCourant = jeu.nomJoueurCourant();
        this.collecteurEvent = collecteurEvent;
        this.listeDesSauvegardes = new DefaultListModel<>();
        for (String nomFichier : jeu.listeFichiersSauvegarde()) {
            this.listeDesSauvegardes.addElement(nomFichier);
        }


        this.labelFont = new Font("SansSerif", Font.PLAIN, 20);
        this.boutonFont = new Font("SansSerif", Font.BOLD, 20);
        this.panelFont = new Font("SansSerif", Font.BOLD, 35);

        this.jeu.ajouteObservateur(this);

        // === Initialisation du Panel ===
        this.setLayout(new BorderLayout());
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 3), "Menu du jeu", TitledBorder.CENTER, TitledBorder.TOP);
        border.setTitleFont(new Font("SansSerif", Font.PLAIN, 30));
        border.setTitleColor(Color.DARK_GRAY);
        this.setBorder(border);
        setMaximumSize(new Dimension(800, 400));

        // === Ajout des compssants ===
        this.add(creerSectionJoueurs(), BorderLayout.NORTH);
        this.add(creerSectionActions(), BorderLayout.EAST);
        this.add(creerSectionReprendre(), BorderLayout.CENTER);
        this.add(creerSectionTemps(), BorderLayout.WEST);
    }

    @Override
    public void miseAJour() {
        if (jeu.estPartieFinie()){
            if (jeu.getGagnant() == 1)
                ecranVictoire = new EcranVictoire(jeu.getNameJoueurA());
            else
                ecranVictoire = new EcranVictoire(jeu.getNameJoueurB());
        }
        joueurCourant = jeu.nomJoueurCourant();
        joueurA = jeu.getNameJoueurA();
        joueurB = jeu.getNameJoueurB();

        miseAjourCreerSectionJoueurs();
        miseAjourCreerSectionTemps();
        miseAjourCreerSectionTemps2();
        miseAjourCreerSectionActions();
        miseAjourCreerSectionReprendre();

    }


    /**************************************
     * *** ** * MÉTHODES UTILES * ** **** *
     **************************************/

    /// === Section Temps ===
    private JPanel creerSectionTemps() {
        JPanel panelTemps = new JPanel();

        panelTemps.setLayout(new BoxLayout(panelTemps, BoxLayout.Y_AXIS));
        panelTemps.setBorder(BorderFactory.createTitledBorder("Temps"));
        panelTemps.setBorder(creerTitledBorder("Temps", 25, Color.GRAY.darker(), Font.PLAIN));

        JLabel labelTempsGlobal = new JLabel("Total :");
        JLabel labelTempsTour = new JLabel("Partie en cours :");

        labelTempsGlobalBis = new JLabel("00:00");
        labelTempsTourBis = new JLabel("00:00");

        // Alignement
        labelTempsTourBis.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTempsGlobal.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTempsTour.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTempsGlobalBis.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Styles
        labelTempsGlobal.setFont(labelFont);
        labelTempsTour.setFont(labelFont);
        labelTempsGlobalBis.setFont(panelFont);
        labelTempsTourBis.setFont(panelFont);

        // Ajout
        panelTemps.add(labelTempsGlobal);
        panelTemps.add(labelTempsGlobalBis);
        panelTemps.add(labelTempsTour);
        panelTemps.add(labelTempsTourBis);

        debut = Instant.now(); // Marque le début
        debut2 = Instant.now();
        timer = new Timer(1000, e -> miseAjourCreerSectionTemps());
        timer2 = new Timer(1000, e -> miseAjourCreerSectionTemps2());
        timer.start();
        timer2.start();

        return panelTemps;
    }

    /// === Section Joueurs ===
    private JPanel creerSectionJoueurs() {
        JPanel panelJoueurs = new JPanel(new GridLayout(1, 2));
        panelJoueurs.setBorder(creerTitledBorder("Joueurs", 25, Color.GRAY.darker(), Font.PLAIN));

        labelNomA = new JLabel(joueurA, SwingConstants.CENTER);
        labelNomB = new JLabel(joueurB, SwingConstants.CENTER);

        labelNomA.setFont(new Font("SansSerif", Font.BOLD, 30));
        labelNomB.setFont(new Font("SansSerif", Font.BOLD, 30));

        if (joueurCourant == joueurA) labelNomA.setForeground(Color.GREEN.darker());
        else labelNomB.setForeground(Color.GREEN.darker());

        panelJoueurs.add(labelNomA);
        panelJoueurs.add(labelNomB);

        return panelJoueurs;
    }

    /// === Section Actions ===
    private JPanel creerSectionActions() {
        JPanel panelBoutons = new JPanel(new GridLayout(4, 1));
        panelBoutons.setBorder(creerTitledBorder("Actions", 25, Color.GRAY.darker(), Font.PLAIN));

        btnAnnuler = new JButton("Annuler");
        btnAnnuler.setEnabled(jeu.peutAnnulerCoup());
        btnAnnuler.addActionListener(new AdaptateurAnnuler(collecteurEvent));

        btnRefaire = new JButton("Refaire");
        btnRefaire.setEnabled(jeu.peutRefaireCoup());
        btnRefaire.addActionListener(new AdaptateurRefaire(collecteurEvent));

        btnNouvellePartie = new JButton("Nouvelle partie...");
        btnNouvellePartie.addActionListener(new AdaptateurNouvellePartie(collecteurEvent, this));

        btnSauvegarder = new JButton("Sauvegarder");
        btnSauvegarder.addActionListener(new AdaptateurSauvegarder(collecteurEvent));

        for (JButton btn : new JButton[]{btnAnnuler, btnRefaire, btnNouvellePartie, btnSauvegarder}) {
            btn.setFont(boutonFont);
            panelBoutons.add(btn);
        }

        return panelBoutons;
    }

    // === Section Reprendre une partie ===
    private JPanel creerSectionReprendre() {
        JPanel panelReprendre = new JPanel(new BorderLayout(5, 5));
        panelReprendre.setBorder(creerTitledBorder("Reprendre", 25, Color.GRAY.darker(), Font.PLAIN));
        btnReprendre = new JButton("Reprendre une partie...");
        btnReprendre.setEnabled(!listeDesSauvegardes.isEmpty());
        btnReprendre.addActionListener(new AdaptateurReprendre(collecteurEvent, this));
        btnReprendre.setFont(boutonFont);
        panelReprendre.add(btnReprendre, BorderLayout.NORTH);

        JList<String> listeSauvegardes = new JList<>(listeDesSauvegardes);
        JScrollPane scrollSauvegardes = new JScrollPane(listeSauvegardes);
        panelReprendre.add(scrollSauvegardes, BorderLayout.CENTER);

        return panelReprendre;
    }



    ///  === Fonctions de mise à jour ===
    private void miseAjourCreerSectionTemps() {
        Duration duration = Duration.between(debut, Instant.now());
        long minutes = duration.toMinutes();
        long secondes = duration.getSeconds() % 60;
        labelTempsTourBis.setText(String.format("%02d:%02d", minutes, secondes));
    }

    private void miseAjourCreerSectionTemps2() {
        Duration duration = Duration.between(debut2, Instant.now());
        long minutes = duration.toMinutes();
        long secondes = duration.getSeconds() % 60;
        labelTempsGlobalBis.setText(String.format("%02d:%02d", minutes, secondes));
    }

    private void miseAjourCreerSectionJoueurs(){
        labelNomA.setText(joueurA);
        labelNomB.setText(joueurB);
        if (joueurCourant == joueurA) {
            labelNomA.setForeground(Color.GREEN.darker());
            labelNomB.setForeground(Color.BLACK.darker());
        } else {
            labelNomB.setForeground(Color.GREEN.darker());
            labelNomA.setForeground(Color.BLACK.darker());
        }
    }

    private void miseAjourCreerSectionActions(){
        btnAnnuler.setEnabled(jeu.peutAnnulerCoup());
        btnRefaire.setEnabled(jeu.peutRefaireCoup());
    }

    private void miseAjourCreerSectionReprendre(){
        btnReprendre.setEnabled(!listeDesSauvegardes.isEmpty());
    }

    public void stopTimerPartieCourante() {
        labelTempsTourBis.setText("00:00");
        debut = Instant.now();
        timer.stop();
        timer.start();
    }
}

