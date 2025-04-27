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
       //TODO
    }




    /** Crée le terrain de jeu */
    private void creerTerrain() {
        terrain = new JPanel(new GridLayout(LIGNES, COLONNES, 0, 0)); // Grille régulière avec marges 0px
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
        annulerRefaire = new JPanel(new GridBagLayout());
//        annulerRefaire.setLayout(new GridLayout(2, 1, 10, 10)); // 2 lignes, 1 colonne, 10px d'écart
        annulerRefaire.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 25));

        annuler = creerBoutonAnnuler();
        refaire = creerBoutonRefaire();

        annulerRefaire.add(annuler);
        annulerRefaire.add(refaire);
    }



    /** Met ensemble les cartes, les boutons annuler/refaire et le terrain de jeu */
    private void creerPlateauCartesAnnulerRefaire() {
        terrainCartesAnnulerRefaire = new JPanel(new BorderLayout());

        // Nord : 2 cartes alignées au centre
        JPanel cartesAuNord = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        cartesAuNord.add(buttonsCartes[0]);
        cartesAuNord.add(buttonsCartes[1]);

        // Sud : 2 cartes alignées au centre
        JPanel cartesAuSud = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        cartesAuSud.add(buttonsCartes[2]);
        cartesAuSud.add(buttonsCartes[3]);

        // Ouest : 1 carte centrée verticalement
        JPanel carteGauche = new JPanel(new GridBagLayout());
        carteGauche.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));
        carteGauche.add(buttonsCartes[4]);

        // Est : annuler/refaire empilés verticalement
        JPanel droite = new JPanel(new GridBagLayout());
        droite.add(annulerRefaire);

        // Centre : le terrain
        JPanel centre = new JPanel(new BorderLayout());
        centre.add(terrain, BorderLayout.CENTER);

        // Placement général
        terrainCartesAnnulerRefaire.add(cartesAuNord, BorderLayout.NORTH);
        terrainCartesAnnulerRefaire.add(cartesAuSud, BorderLayout.SOUTH);
        terrainCartesAnnulerRefaire.add(carteGauche, BorderLayout.WEST);
        terrainCartesAnnulerRefaire.add(annulerRefaire, BorderLayout.EAST);
        terrainCartesAnnulerRefaire.add(centre, BorderLayout.CENTER);
    }


}
