package Vue;

import Modele.Carte;
import Modele.CasePlateau;
import Modele.Jeu;
import Patterns.Observateur;


import java.awt.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Classe représentant l'interface textuelle (console) du jeu.
 * Elle affiche l'état du jeu dans le terminal.
 */
public class InterfaceTextuelle implements Observateur {
    private static Logger logger = Logger.getLogger(InterfaceTextuelle.class.getName());
    private static int NOMBRES_CARTES_PLATEAU = 5;
    private static final int LIGNES = 5;
    private static final int COLONNES = 5;
    private final Jeu jeu;
    private final CollecteurEvenements collecteurEv;
    private Scanner scanner;
    private  boolean jeuTermine;
    private int idJoeurEnCours;
    private boolean afficherAllCartes;
    private boolean uneCarteEstSelectionne;


    public  InterfaceTextuelle(Jeu jeu, CollecteurEvenements collecteurEv){
        this.jeu = jeu;
        this.collecteurEv = collecteurEv;
        this.scanner = new Scanner(System.in);
        this.jeuTermine = jeu.estTermine();
        this.idJoeurEnCours = jeu.getJoueurCourant().getId();
        this.afficherAllCartes = false;
        this.uneCarteEstSelectionne = false;

        lancerBoucleJeu();
    }


    @Override
    public void miseAJour(){
        this.jeuTermine = jeu.estTermine();
    }



    /**
     * Met à jour l'affichage complet de l'interface textuelle.
     * Cette méthode combine l'affichage de tous les éléments.*/
    public void miseAJourTextuelle() {
        afficherInfosHaut();
        afficherPlateau();
        afficherCartesEtOptions();
        System.out.println("                                             |");
        System.out.println("---------------------------------------------|\n");
        afficherCartesJeu();
    }



    /**
     * Affiche les informations en haut : joueur, round, temps.     */
    private void afficherInfosHaut() {
        System.out.println("---------------------------------------------|");
        System.out.println("                                             |");
        System.out.println("     --- Informations de la Partie ---       |");
        System.out.printf("         Round : %d | Temps : %02d:%02d           |%n",
                jeu.getNumeroRound(),
                TimeUnit.SECONDS.toMinutes(jeu.getTempsJeuSecondes()),
                jeu.getTempsJeuSecondes() % 60);

        System.out.println("                                             |");
        System.out.println("             C'est au tour de :              |");
        System.out.println("               "+jeu.getJoueurCourant().getNom());
        System.out.println("         ---------------------------         |");
    }



    /**
     * Affiche la grille du plateau de jeu.*/
    private void afficherPlateau() {
        System.out.println("                                             |");
        System.out.println("           --- Plateau de Jeu ---            |");

        // Afficher les indices de colonne
        System.out.print("      ");
        for (int j = 0; j < COLONNES; j++) {
            System.out.printf("    %-2d", j);
        }
        System.out.println("         |");

        // Afficher la ligne de séparation supérieure
        System.out.println("   +------------------------------------+    |");


        for (int i = 0; i < LIGNES; i++) {
            System.out.printf("  %-2d|    ", i); // Indices de ligne alignés à gauche
            for (int j = 0; j < COLONNES; j++) {
                // Afficher le contenu de la case
                System.out.print(getSymboleCase(jeu.getCasePlateau(i,j)) + "   ");
            }
            System.out.println("|     |"); // Bordure droite
            if (i< LIGNES-1) System.out.println("                                             |");
        }

        // Afficher la ligne de séparation inférieure
        System.out.println("   +------------------------------------+    |");
        System.out.println("                                             |");
    }

    

    /**
     * Méthode utilitaire pour obtenir un symbole textuel pour une case.
     * @param casePlateau La case à représenter.
     * @return Une chaîne de 3 caractères représentant le contenu de la case.     */
    private String getSymboleCase(CasePlateau casePlateau) {
        if (casePlateau == null) {
            return " ? ";
        }
        switch (casePlateau.getTypeElement()) {
            case VIDE:
                return " . ";
            case PION_ETUDIANT:
                return " E"+casePlateau.getPion().getProprietaire();
            case PION_MAITRE:
                return " M"+casePlateau.getPion().getProprietaire();
            default:
                return " ? "; // Type inconnu
        }
    }



    /**
     * Affiche les informations sur les cartes et les options Annuler/Refaire.*/
    private void afficherCartesEtOptions() {
        System.out.println("                                             |");
        System.out.println("           --- Cartes du jeu ---             |");
        System.out.printf("    %s             %s%n",jeu.getJoueur(1).getNom(), jeu.getJoueur(2).getNom());
        System.out.printf("Carte %d: %s          Carte %d: %s%n", 1, jeu.getCartesSurLeTerrain(0).getNom(), 3, jeu.getCartesSurLeTerrain(2).getNom());
        System.out.printf("Carte %d: %s          Carte %d: %s%n", 2, jeu.getCartesSurLeTerrain(1).getNom(), 4, jeu.getCartesSurLeTerrain(3).getNom());
        System.out.printf("\n         Carte %d: %s  %s%n", 5, jeu.getCartesSurLeTerrain(4).getNom(), "(reservée)");


        System.out.println();

        System.out.println("           ----- Options -----               |");
        System.out.println("A|a: Annuler("+ jeu.peutAnnulerCoup()+")      C|c: aff. Cartes     |");
        System.out.println("R|r: Refaire("+ jeu.peutRefaireCoup()+")      N|n: Nouv. partie    |");
        System.out.println("Q|q: Quitter            S|s: Sauvegarder     |");
        System.out.println("Z|z: <--                Y|y: -->             |");
    }



    /**
     * Lance la boucle principale de l'interface textuelle pour le jeu.
     * Cette boucle affiche l'état du jeu et attend les commandes de l'utilisateur.     */
    private void lancerBoucleJeu() {
        String input;
        Integer numCarteSelectionne = 0;

        while (!jeuTermine) {
            miseAJourTextuelle();
            System.out.print("Choisissez une Carte: \n_> ");

            input = scanner.nextLine().trim(); // Lire la ligne et retirer les espaces blancs début/fin

            // Traiter la commande saisie
            switch (input){
                case  "":
                case "z":
                case "Z":
                    break;
                case "q":
                case "Q":
                    System.out.println("Quitter le jeu...");
                    collecteurEv.clavier("exit");
                    return;
                    // break; TODO à revoir si moteur de jeu complet
                case "a":
                case "A":
                    System.out.println("Annuler Coup");
                    collecteurEv.clavier("annuler");
                    break;
                case "r":
                case "R":
                    System.out.println("Refaire Coup");
                    collecteurEv.clavier("refaire");
                    break;
                case "n":
                case "N":
                    System.out.println("Nouvelle partie");
                    collecteurEv.clavier("nouvellePartie");
                    break;
                case "s":
                case "S":
                    System.out.println("Sauvegarder la partie");
                    collecteurEv.clavier("sauvegarder");
                    break;
                case "c":
                case "C":
                        afficherAllCartes = true;
                    break;
                case "1":
                case "2":
                case "3":
                case "4":
                    numCarteSelectionne = Integer.parseInt(input);
                    Carte carte = jeu.getCartesSurLeTerrain(numCarteSelectionne);
                    collecteurEv.carteSelectionne(numCarteSelectionne);

                    while (uneCarteEstSelectionne) {
                        System.out.println("Carte sélectionnée : " + carte.getNom());
                        System.out.print("Choisissez un Pion (ex: 4 2 ): \n_>");

                        input = scanner.nextLine().trim();

                        switch (input) {
                            case "r":
                            case "R":
                                uneCarteEstSelectionne = false;
                                break;
                            case "y":
                            case "Y":
                                break;
                            default:
                                int x = Integer.parseInt(String.valueOf(input.charAt(0)));
                                int y = Integer.parseInt(String.valueOf(input.charAt(1)));

                                if (x > 0 && x < 5 && y > 0 && y < 5) {
                                    System.out.println("Carte sélectionnée : " + carte.getNom());
                                    System.out.println("Pion selection :(" + x + ", " + y + ")");
                                }
                                break;
                        }
                    }
                    break;
                default:
                    System.out.println("Commande non valide. Entrez une commande valide.");
                    break;
            }

            jeuTermine = jeu.estTermine();
        }

        // Fermer le scanner lorsque la boucle se termine
        scanner.close();
        System.out.println("Interface textuelle terminée.");
    }




    /**
     *
     * Affiche les 5 cartes actuellement sur le terrain sous forme de grilles 5x5 si {@code afficheAllCartes ==  true},
     * chacune encadrée par des bordures, affichées côte à côte avec leurs numéros respectifs.   */
    private void afficherCartesJeu() {
        if (afficherAllCartes) {
            // Récupère les versions affichables (grilles 5x5) des cartes
            String[][] carte1 = getVersionAffichable(jeu.getCartesSurLeTerrain(0));
            String[][] carte2 = getVersionAffichable(jeu.getCartesSurLeTerrain(1));
            String[][] carte3 = getVersionAffichable(jeu.getCartesSurLeTerrain(2));
            String[][] carte4 = getVersionAffichable(jeu.getCartesSurLeTerrain(3));
            String[][] carte5 = getVersionAffichable(jeu.getCartesSurLeTerrain(4));

            // Tableau contenant les 5 cartes à afficher
            String[][][] listCartes = new String[][][]{carte1, carte2, carte3, carte4, carte5};

            int nbrLigne = 5;
            int nbrColonne = 5;
            int nbrCartes = 5;

            // Affiche les numéros des cartes au-dessus
            for (int i = 0; i < nbrCartes; i++) {
                System.out.print(jeu.getCartesSurLeTerrain(i).getNom()+ "         ");
            }
            System.out.println();

            // Affiche la ligne supérieure des cadres
            for (int i = 0; i < nbrCartes; i++) {
                System.out.print("+----------+   ");
            }
            System.out.println();

            // Affiche le contenu ligne par ligne
            for (int ligne = 0; ligne < nbrLigne; ligne++) {
                for (int carte = 0; carte < nbrCartes; carte++) {
                    System.out.print("|");
                    for (int colonne = 0; colonne < nbrColonne; colonne++) {
                        System.out.print(listCartes[carte][ligne][colonne]+" ");
                    }
                    System.out.print("|   ");
                }
                System.out.println();
            }

            // Affiche la ligne inférieure des cadres
            for (int i = 0; i < nbrCartes; i++) {
                System.out.print("+----------+   ");
            }
            System.out.println();


            afficherAllCartes = false; // resset
        }
    }




    /**
     * Génère une représentation textuelle 5x5 d'une carte avec :
     * - '.' pour les cases neutres
     * - '*' pour les mouvements possibles
     * - 'X' pour le point de départ au centre (2,2)
     *
     * @param carte La carte à convertir en grille affichable
     * @return Un tableau 2D de chaînes représentant la carte
     */
    private String[][] getVersionAffichable(Carte carte) {
        int ligne = 5;
        int colonne = 5;
        List<Point> mouvements = carte.getMoves(); // mouvements relatifs au centre
        int centreX = 2;
        int centreY = 2;

        // Initialisation de la grille avec des points
        String[][] grille = new String[ligne][colonne];
        for (int i = 0; i < ligne; i++) {
            for (int j = 0; j < colonne; j++) {
                grille[i][j] = ".";
            }
        }

        // Ajoute les mouvements possibles
        for (Point move : mouvements) {
            int x = centreX + move.x;
            int y = centreY + move.y;
            if (x >= 0 && x < ligne && y >= 0 && y < colonne) {
                grille[x][y] = "*";
            }
        }

        // Marque le point central
        grille[centreX][centreY] = "X";

        return grille;
    }



    public static void lancerInterfaceTextuelle(Jeu jeu, CollecteurEvenements collecteurEvenements){
        new InterfaceTextuelle(jeu, collecteurEvenements);
    }
}
