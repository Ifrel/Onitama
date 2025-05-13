package Vue;

import Modele.Carte;
import Modele.CasePlateau;
import Modele.Jeu;
import Patterns.Observateur;


import java.awt.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Classe représentant l'interface textuelle (console) du jeu.
 * Elle affiche l'état du jeu dans le terminal.
 */
public class InterfaceTextuelle implements Observateur {
    private static final int LIGNES = 5;
    private static final int COLONNES = 5;

    private final Jeu jeu;
    private final CollecteurEvenements collecteurEv;
    private Scanner scanner;
    private  boolean jeuTermine;

    private boolean afficherToutesCartes;
    private boolean carteSelectionnee;
    private boolean rafraichirInterface;


    public  InterfaceTextuelle(Jeu jeu, CollecteurEvenements collecteurEv){
        this.jeu = jeu;
        this.collecteurEv = collecteurEv;
        this.scanner = new Scanner(System.in);
        this.jeuTermine = jeu.estPartieFinie();
        this.afficherToutesCartes = false;
        this.carteSelectionnee = false;
        this.rafraichirInterface = true;

        lancerBoucleJeu();
    }


    @Override
    public void miseAJour(){
        rafraichirAffichage();
        this.jeuTermine = jeu.estPartieFinie();
    }



    /**
     * Met à jour l'affichage complet de l'interface textuelle.
     * Cette méthode combine l'affichage de tous les éléments.*/
    public void rafraichirAffichage() {
        if (rafraichirInterface) {
            afficherInfosHaut();
            afficherPlateau();
            afficherCartesEtOptions();
            System.out.println("                                             |");
            System.out.println("---------------------------------------------|\n");
            afficherCartesJeu();
        }
        else {
            rafraichirInterface = true;
        }
    }



    /**
     * Affiche les informations en haut : joueur, round, temps.     */
    private void afficherInfosHaut() {
        System.out.println("---------------------------------------------|");
        System.out.println("                                             |");
        System.out.println("     --- Informations de la Partie ---       |");
        System.out.printf("         Round : %d | Temps : %02d:%02d           |%n",
                jeu.getNumeroRound(),
                TimeUnit.SECONDS.toMinutes(jeu.getTempsDeJeu()),
                jeu.getTempsDeJeu() % 60);

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
        switch (casePlateau.getTypeElement()) {
            case VIDE: return " . ";
            case PION_ETUDIANT: return " E"+casePlateau.getProprietaire();
            case PION_MAITRE: return " M"+casePlateau.getId();
            default: return " ? "; // Type inconnu
        }
    }



    /**
     * Affiche les informations sur les cartes et les options Annuler/Refaire.*/
    private void afficherCartesEtOptions() {
        int espace = 22;
        System.out.println("                                             |");
        System.out.println("           --- Cartes du jeu ---             |");
        System.out.printf("      %s%s%s%n",jeu.getNomJoueur1(), espace(espace, jeu.getNomJoueur1().length()), jeu.getNomJoueur2());
        System.out.printf("  Carte %d: %s%sCarte %d: %s%n", 1, jeu.getCartesSurLeTerrain(0).getNom(), espace(espace-10,jeu.getCartesSurLeTerrain(0).getNom().length()), 3, jeu.getCartesSurLeTerrain(2).getNom());
        System.out.printf("  Carte %d: %s%sCarte %d: %s%n", 2, jeu.getCartesSurLeTerrain(1).getNom(), espace(espace-10,jeu.getCartesSurLeTerrain(1).getNom().length()), 4, jeu.getCartesSurLeTerrain(3).getNom());
        System.out.printf("\n         Carte %d: %s  %s%n", 5, jeu.getCartesSurLeTerrain(4).getNom(), "(reservée)");


        System.out.println("                                             |");

        System.out.println("           ----- Options -----               |");
        String peutAnnuler = jeu.peutAnnulerCoup() ? "oui": "non";
        String peutRefaire = jeu.peutRefaireCoup() ? "oui": "non";

        System.out.println("A|a: Annuler("+ peutAnnuler +")       C|c: aff. Cartes     |");
        System.out.println("R|r: Refaire("+ peutRefaire +")       N|n: Nouv. partie    |");
        System.out.println("Q|q: Quitter            S|s: Sauvegarder     |");
        System.out.println("Z|z: <--                                     |");
    }



    /**
     * Lance la boucle principale de l'interface textuelle pour le jeu.
     * L'utilisateur interagit via la console : sélection de carte, pion, et coordonnées de destination.
     */
    private void lancerBoucleJeu() {
        String input;
        int indiceCarte = 0;

        while (!jeuTermine) {
            rafraichirAffichage();
            System.out.print("Choisissez une Carte (1-4) :\n_> ");
            input = scanner.nextLine().trim();

            switch (input.toLowerCase()) {
                case "":
                case "z":
                    break;
                case "q":
                    collecteurEv.clavier("exit");
                    break;
                case "a":
                    collecteurEv.clavier("annuler");
                    break;
                case "r":
                    collecteurEv.clavier("refaire");
                    break;
                case "n":
                    collecteurEv.clavier("nouvellePartie");
                    break;
                case "s":
                    collecteurEv.clavier("sauvegarder");
                    break;
                case "c":
                    afficherToutesCartes = true;
                    break;
                case "5":
                    rafraichirInterface = false;
                    System.out.println("carte réservée");
                    break;
                case "1":
                case "2":
                case "3":
                case "4":
                    try {
                        indiceCarte = Integer.parseInt(input)-1;

                        // verification si choix valide selon du joueur courant
                        int idJoueur = jeu.getJoueurCourant().getId();
                        if ((idJoueur == 1 && (indiceCarte != 0 && indiceCarte != 1)) ||
                                (idJoueur == 2 && (indiceCarte != 2 && indiceCarte != 3))) {
                            System.out.println("Cette carte n'appartient pas à votre camp.");
                            rafraichirInterface = false;
                            break;
                        }

                        Carte carte = jeu.getCartesSurLeTerrain(indiceCarte);
                        collecteurEv.carteSelectionne(carte);
                        carteSelectionnee = true;

                        while (carteSelectionnee) {
                            System.out.println("Carte sélectionnée : " + carte.getNom());
                            System.out.print("Choisissez un pion (ex: 2 3) ou Z|z pour annuler :\n_> ");
                            input = scanner.nextLine().trim();

                            if (input.equalsIgnoreCase("z")) {
                                carteSelectionnee = false;
                                System.out.println("Sélection annulée.");
                                break;
                            }

                            String[] coordDepart = input.split("\\s+");
                            if (coordDepart.length == 2) {
                                try {
                                    int xDepart = Integer.parseInt(coordDepart[0]);
                                    int yDepart = Integer.parseInt(coordDepart[1]);

                                    if (xDepart >= 0 && xDepart < 5 && yDepart >= 0 && yDepart < 5 && jeu.estPionDuJoueurCourant(xDepart,yDepart)) {
                                        System.out.println("Pion sélectionné à (" + xDepart + ", " + yDepart + ")");
                                        collecteurEv.setPionSelectionne(xDepart,yDepart);

                                        // Étape suivante : demander la position cible
                                        System.out.print("Coordonnées de destination (ex: 1 2) ou Z pour annuler :\n_> ");
                                        input = scanner.nextLine().trim();

                                        if (input.equalsIgnoreCase("z")) {
                                            System.out.println("Déplacement annulé.");
                                            continue;  // Retourne à la sélection du pion
                                        }

                                        String[] coordDest = input.split("\\s+");
                                        if (coordDest.length == 2) {
                                            int xDest = Integer.parseInt(coordDest[0]);
                                            int yDest = Integer.parseInt(coordDest[1]);

                                            if (xDest >= 0 && xDest < 5 && yDest >= 0 && yDest < 5 && jeu.estDeplacementConforme(xDest, yDest)) {
                                                System.out.println("Déplacement de (" + xDepart + ", " + yDepart + ") vers (" + xDest + ", " + yDest + ")");

                                                // Informer le modèle du déplacement
                                                collecteurEv.setCiblePion(xDest, yDest);

                                                carteSelectionnee = false; // Fin de sélection
                                            } else {
                                                System.out.println("Coordonnées cibles invalides.");
                                            }
                                        } else {
                                            System.out.println("Format invalide pour la destination. Entrez deux chiffres séparés par un espace.");
                                        }
                                    } else {
                                        System.out.println("Coordonnées du pion incorrectes.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Entrée invalide. Utilisez des chiffres pour les coordonnées.");
                                }
                            } else {
                                System.out.println("Format invalide. Entrez deux coordonnées séparées par un espace.");
                            }
                        }
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.out.println("Carte invalide. Choisissez un numéro entre 1 et 4.");
                    }
                    break;

                default:
                    rafraichirInterface = false;
                    System.out.println("Commande inconnue : \"" + input + "\". Réessayez.");
                    break;
            }

            jeuTermine = jeu.estPartieFinie();
        }

        scanner.close();
        System.out.println("Interface textuelle terminée.");
    }





    /**
     * Affiche les 5 cartes actuellement sur le terrain sous forme de grilles 5x5 si {@code afficheAllCartes ==  true} */
    private void afficherCartesJeu() {
        if (afficherToutesCartes) {
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

            // Affiche les noms des cartes au-dessus
            for (int i = 0; i < nbrCartes; i++) {
                Carte carte = jeu.getCartesSurLeTerrain(i);
                String espace = espace(15, carte.getNom().length());
                System.out.print(carte.getNom()+ espace);
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


            afficherToutesCartes = false; // resset
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


    /**
     * Renvoie une chaîne composée d'espaces dont la longueur est
     * la différence entre a et b, uniquement si a > b.
     * Sinon, renvoie une chaîne vide.
     *
     * @param a Premier entier
     * @param b Deuxième entier
     * @return Une chaîne de (a - b) espaces si a > b, sinon ""
     */
    public static String espace(int a, int b) {
        if (a > b) {
            return " ".repeat(a - b);
        } else {
            return " ";
        }
    }



    /**
     * Lance l'interface textuelle du jeu en initialisant l'interface
     *
     * @param jeu Le jeu à exécuter.
     * @param collecteurEvenements Le collecteur d'événements pour gérer les actions de l'utilisateur.     */
    public static void lancerInterfaceTextuelle(Jeu jeu, CollecteurEvenements collecteurEvenements) {
        InterfaceTextuelle interfaceTextuelle = new InterfaceTextuelle(jeu, collecteurEvenements);
    }

}
