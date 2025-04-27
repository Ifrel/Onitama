package Vue;

import Modele.Jeu;
import Patterns.Observateur;
import javax.swing.*;
import java.awt.*;

import static Global.Config.*;
import static Vue.Utlis.Button.*;
import static Vue.Utlis.Panel.creerPanel;

public class PlateauDeJeu extends JPanel implements Observateur {
    private Jeu jeu;
    private CollecteurEvenements collecteurEv;

    private JPanel terrain;
    private JPanel terrainCartesAnnulerRefaire;
    private JPanel annulerRefaire;

    private JButton[][] buttonsTerrain;
    private JButton[] buttonsCartes;
    private JButton annuler, refaire;


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
        creerButtonsAnnulerRefaire();
        creerPlateauCartesAnnulerRefaire();
        add(terrainCartesAnnulerRefaire, BorderLayout.CENTER);
    }

    @Override
    public void miseAJour() {
        // À compléter si besoin plus tard
    }




    /** Crée le terrain de jeu */
    private void creerTerrain() {
        terrain = new JPanel(new GridLayout(LIGNES, COLONNES, 0, 0)); // Grille régulière avec marges 5px
        buttonsTerrain = new JButton[LIGNES][COLONNES];

        // Bordure jolie avec coins arrondis
        terrain.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 206, 206), 5, true),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
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
            JButton bouton = creerBoutonCarte("res/vue/images/cartes/TIGRE.png");
            buttonsCartes[row] = bouton;
        }
    }

    /** Crée les boutons Annuler et Refaire */
    private void creerButtonsAnnulerRefaire() {
        annulerRefaire = creerPanel();
        annulerRefaire.setLayout(new GridLayout(2, 1, 10, 10)); // 2 lignes, 1 colonne, 10px d'écart
        annulerRefaire.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        annuler = creerBoutonAnnuler();
        refaire = creerBoutonRefaire();

        annulerRefaire.add(annuler);
        annulerRefaire.add(refaire);
    }



    /** Met ensemble les cartes, les boutons annuler/refaire et le terrain de jeu */
    private void creerPlateauCartesAnnulerRefaire() {
        terrainCartesAnnulerRefaire = new JPanel(new GridLayout(3, 3, 0, 0));
        //terrainCartesAnnulerRefaire.setLayout(new BorderLayout());

        // Nord : les cartes en haut
        JPanel cartesAuNord = new JPanel(new GridLayout(1, 2, 40, 0));
        cartesAuNord.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        cartesAuNord.add(buttonsCartes[0]);
        cartesAuNord.add(buttonsCartes[1]);


        // Sud : les cartes en bas
        JPanel cartesAuSud = new JPanel(new GridLayout(1, 2, 40, 0));
        cartesAuSud.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        cartesAuSud.add(buttonsCartes[2]);
        cartesAuSud.add(buttonsCartes[3]);


        // Centre gauche : une carte à gauche
        JPanel carteGauche = new JPanel(new GridLayout(1, 1, 0, 0));
        carteGauche.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        carteGauche.add(buttonsCartes[4]);


        terrainCartesAnnulerRefaire.add(new JPanel()); // Composant vide pour l'espace
        terrainCartesAnnulerRefaire.add(cartesAuNord);
        terrainCartesAnnulerRefaire.add(new JPanel());

        terrainCartesAnnulerRefaire.add(carteGauche);
        terrainCartesAnnulerRefaire.add(terrain);
        terrainCartesAnnulerRefaire.add(annulerRefaire);

        terrainCartesAnnulerRefaire.add(new JPanel());
        terrainCartesAnnulerRefaire.add(cartesAuSud);
        terrainCartesAnnulerRefaire.add(new JPanel());

    }

}
