//package Vue.testsUI;
//
//import java.awt.*;
//import java.util.concurrent.TimeUnit; // Pour formater le temps
//
///**
// * Classe représentant l'interface textuelle (console) du jeu.
// * Elle affiche l'état du jeu dans le terminal.
// */
//public class InterfaceTextuellePlateau {
//    public  int NOMBRES_CARTES_PLATEAU = 5;
//
//    // Dimensions du plateau (utiliser les constantes de votre modèle si possible)
//    private static final int LIGNES = 5; // Exemple, ajustez selon votre modèle
//    private static final int COLONNES = 7; // Exemple, ajustez selon votre modèle
//
//    /**
//     * Efface la console (méthode basique, peut ne pas fonctionner sur tous les systèmes).
//     */
//    private void clearConsole() {
//        // Cette méthode est très basique et peut ne pas fonctionner sur tous les OS ou IDE.
//        // Pour un effacement plus fiable, il faudrait des bibliothèques spécifiques ou dépendre de l'environnement.
//        try {
//            final String os = System.getProperty("os.name");
//            if (os.contains("Windows")) {
//                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
//            } else {
//                System.out.print("\033[H\033[2J");
//                System.out.flush();
//            }
//        } catch (final Exception e) {
//            // En cas d'erreur (par exemple, pas de support ou pas de terminal), ne rien faire.
//            // System.out.println("Could not clear console: " + e.getMessage()); // Débogage optionnel
//        }
//    }
//
//
//
//
//    /**
//     * Met à jour l'affichage complet de l'interface textuelle.
//     * Cette méthode combine l'affichage de tous les éléments.
//     *
//     * @param plateauData L'état actuel du plateau (vos objets CasePlateau).
//     * @param joueurCourant Le joueur dont c'est le tour.
//     * @param numeroRound Le numéro du round actuel.
//     * @param tempsJeuSecondes Le temps écoulé en secondes.
//     * @param cartesSurTerrain Les cartes actuellement sur le terrain (vos objets Carte).
//     * @param peutAnnuler Indique si l'action annuler est possible.
//     * @param peutRefaire Indique si l'action refaire est possible.
//     */
//    public void miseAJourTextuelle(CasePlateau[][] plateauData, Joueur joueurCourant, int numeroRound, long tempsJeuSecondes, Carte[] cartesSurTerrain, boolean peutAnnuler, boolean peutRefaire) {
//        clearConsole(); // Essayer d'effacer la console avant d'afficher
//
//        afficherInfosHaut(joueurCourant, numeroRound, tempsJeuSecondes);
//        afficherPlateau(plateauData);
//        afficherCartesEtOptions(cartesSurTerrain, peutAnnuler, peutRefaire);
//
//        // Ajoutez une ligne vide pour séparer les tours
//        System.out.println("\n------------------------------------\n");
//    }
//
//    /**
//     * Affiche les informations en haut : joueur, round, temps.
//     */
//    private void afficherInfosHaut(Joueur joueurCourant, int numeroRound, long tempsJeuSecondes) {
//        System.out.println("--- Informations de la Partie ---");
//        System.out.printf("Round : %d | Temps : %02d:%02d%n",
//                numeroRound,
//                TimeUnit.SECONDS.toMinutes(tempsJeuSecondes),
//                tempsJeuSecondes % 60);
//
//        // Supposons que Joueur a une méthode getNom() et getCouleur()
//        // Afficher la couleur textuellement peut être fait avec des codes ANSI
//        // ou simplement afficher le nom de la couleur si disponible dans Joueur.
//        String couleurJoueur = (joueurCourant != null && joueurCourant.getCouleur() != null)
//                ? joueurCourant.getCouleur().toString() // Ou une méthode getNomCouleur() si elle existe
//                : "Couleur inconnue";
//
//        System.out.printf("C'est au tour de : %s (%s)%n",
//                (joueurCourant != null ? joueurCourant.getNom() : "Aucun joueur"),
//                couleurJoueur);
//        System.out.println("-------------------------------");
//    }
//
//    /**
//     * Affiche la grille du plateau de jeu.
//     *
//     * @param plateauData L'état actuel du plateau.
//     */
//    private void afficherPlateau(CasePlateau[][] plateauData) {
//        System.out.println("\n--- Plateau de Jeu ---");
//
//        // Afficher les indices de colonne
//        System.out.print("  ");
//        for (int j = 0; j < COLONNES; j++) {
//            System.out.printf(" %d ", j); // Indices de 0 à COLONNES-1
//        }
//        System.out.println();
//
//        // Afficher la ligne de séparation supérieure
//        System.out.print(" +");
//        for (int j = 0; j < COLONNES; j++) {
//            System.out.print("---");
//        }
//        System.out.println("-+");
//
//
//        for (int i = 0; i < LIGNES; i++) {
//            System.out.printf("%d|", i); // Indices de ligne de 0 à LIGNES-1
//            for (int j = 0; j < COLONNES; j++) {
//                // Afficher le contenu de la case
//                // Utilisez une méthode de votre objet CasePlateau pour obtenir le symbole
//                String symbole = (plateauData != null && i < plateauData.length && j < plateauData[i].length && plateauData[i][j] != null)
//                        ? getSymboleCase(plateauData[i][j]) // Méthode pour obtenir le symbole textuel
//                        : " ? "; // Symbole par défaut si données manquantes
//                System.out.print(symbole);
//            }
//            System.out.println("|"); // Bordure droite
//        }
//
//        // Afficher la ligne de séparation inférieure
//        System.out.print(" +");
//        for (int j = 0; j < COLONNES; j++) {
//            System.out.print("---");
//        }
//        System.out.println("-+");
//
//        System.out.println("----------------------");
//    }
//
//    /**
//     * Méthode utilitaire pour obtenir un symbole textuel pour une case.
//     * Vous devez adapter cela en fonction de votre classe CasePlateau.
//     *
//     * @param casePlateau La case à représenter.
//     * @return Une chaîne de 3 caractères représentant le contenu de la case.
//     */
//    private String getSymboleCase(CasePlateau casePlateau) {
//        if (casePlateau == null) {
//            return " ? ";
//        }
//        // Supposons que CasePlateau a une méthode getTypeElement() qui retourne un Enum
//        // et éventuellement getPion() si c'est une case avec un pion
//        switch (casePlateau.getTypeElement()) {
//            case VIDE:
//                return " . "; // Case vide
//            case PION_ETUDIANT:
//                // Supposons que Pion a une méthode getSymboleTextuel() ou getCouleur()
//                // Ici, on pourrait afficher la première lettre de la couleur du pion par exemple
//                return " E "; // Pion Étudiant (exemple générique)
//            case PION_MAITRE:
//                // Idem pour le pion maître
//                return " M "; // Pion Maître (exemple générique)
//            // Ajoutez d'autres types de cases si nécessaire (obstacles, objectifs, etc.)
//            // case OBSTACLE: return " # ";
//            // case OBJECTIF: return " O ";
//            default:
//                return " ? "; // Type inconnu
//        }
//    }
//
//    /**
//     * Affiche les informations sur les cartes et les options Annuler/Refaire.
//     *
//     * @param cartesSurTerrain Les cartes disponibles.
//     * @param peutAnnuler Indique si annuler est possible.
//     * @param peutRefaire Indique si refaire est possible.
//     */
//    private void afficherCartesEtOptions(Carte[] cartesSurTerrain, boolean peutAnnuler, boolean peutRefaire) {
//        System.out.println("\n--- Cartes Disponibles ---");
//        if (cartesSurTerrain != null && cartesSurTerrain.length > 0) {
//            for (int i = 0; i < cartesSurTerrain.length; i++) {
//                // Supposons que Carte a une méthode getNom() ou getDescription()
//                System.out.printf("Carte %d: %s%n", i + 1,
//                        (cartesSurTerrain[i] != null ? cartesSurTerrain[i].getNom() : "Carte vide"));
//            }
//        } else {
//            System.out.println("Aucune carte disponible.");
//        }
//        System.out.println("--------------------------");
//
//        System.out.println("\n--- Options ---");
//        System.out.printf("Annuler : %s%n", peutAnnuler ? "Disponible" : "Indisponible");
//        System.out.printf("Refaire : %s%n", peutRefaire ? "Disponible" : "Indisponible");
//        System.out.println("---------------");
//    }
//
//
//
//    // =========================================
//    // ============ Programme Principal ========
//    // =========================================
//
//    /**
//     * Programme principal simple pour démontrer l'interface textuelle.
//     * Il simule un état de jeu et appelle la mise à jour textuelle.
//     */
//    public static void main(String[] args) {
//        InterfaceTextuellePlateau interfaceTextuelle = new InterfaceTextuellePlateau();
//
//        // --- Simulation de données du jeu ---
//        // Créez des objets factices pour Joueur, CasePlateau, Carte pour la démo.
//        // Dans votre vraie application, ces données viendraient de votre modèle Jeu.
//
//        // Joueur factice
//        Joueur joueur1 = new Joueur("Alice", Color.BLUE); // Supposons que Joueur stocke un nom et une couleur
//        Joueur joueur2 = new Joueur("Bob", Color.RED);
//
//        // Plateau factice
//        CasePlateau[][] plateauSimule = new CasePlateau[LIGNES][COLONNES];
//        for (int i = 0; i < LIGNES; i++) {
//            for (int j = 0; j < COLONNES; j++) {
//                // Simuler quelques types de cases
//                if (i == 0 || i == LIGNES - 1 || j == 0 || j == COLONNES - 1) {
//                    plateauSimule[i][j] = new CasePlateau(i, j, CasePlateau.TypeElement.OBSTACLE, null); // Exemple : Bordures obstacles
//                } else if (i == LIGNES/2 && j == COLONNES/2) {
//                    plateauSimule[i][j] = new CasePlateau(i, j, CasePlateau.TypeElement.PION_MAITRE, new Pion(joueur1, Pion.TypePion.MAITRE)); // Exemple : Maître
//                }
//                else if (i % 2 == 0 && j % 2 != 0) {
//                    plateauSimule[i][j] = new CasePlateau(i, j, CasePlateau.TypeElement.PION_ETUDIANT, new Pion(joueur2, Pion.TypePion.ETUDIANT)); // Exemple : Étudiants
//                }
//                else {
//                    plateauSimule[i][j] = new CasePlateau(i, j, CasePlateau.TypeElement.VIDE, null); // Exemple : Cases vides
//                }
//            }
//        }
//
//
//        // Cartes factices
//        Carte[] cartesSimulees = new Carte[interfaceTextuelle.NOMBRES_CARTES_PLATEAU];
//        for (int i = 0; i < cartesSimulees.length; i++) {
//            cartesSimulees[i] = new Carte("Carte Spéciale " + (i + 1), "Description...", "chemin/image/carte" + (i+1) + ".png"); // Exemple carte
//        }
//
//
//        // --- Première mise à jour ---
//        System.out.println("Affichage initial...");
//        interfaceTextuelle.miseAJourTextuelle(plateauSimule, joueur1, 1, 0, cartesSimulees, false, false);
//
//        // --- Simulation d'un changement de tour (attente) ---
//        try {
//            TimeUnit.SECONDS.sleep(3); // Attendre 3 secondes
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // --- Deuxième mise à jour (simulation nouveau tour, temps écoulé, options disponibles) ---
//        System.out.println("Simulation du tour suivant...");
//        // Modifier légèrement le plateau simulé (ex: déplacer un pion)
//        plateauSimule[1][1] = new CasePlateau(1, 1, CasePlateau.TypeElement.VIDE, null);
//        if (LIGNES > 2 && COLONNES > 2) {
//            plateauSimule[2][2] = new CasePlateau(2, 2, CasePlateau.TypeElement.PION_MAITRE, new Pion(joueur1, Pion.TypePion.MAITRE));
//        }
//
//        interfaceTextuelle.miseAJourTextuelle(plateauSimule, joueur2, 2, 45, cartesSimulees, true, false); // Annuler devient possible
//
//
//        // --- Simulation d'un autre changement ---
//        try {
//            TimeUnit.SECONDS.sleep(3); // Attendre 3 secondes
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // --- Troisième mise à jour (simulation d'un refaire possible) ---
//        System.out.println("Simulation après une annulation...");
//        interfaceTextuelle.miseAJourTextuelle(plateauSimule, joueur2, 2, 55, cartesSimulees, true, true); // Annuler et Refaire possibles
//
//        System.out.println("\nDémonstration terminée.");
//    }
//}
//
//
//
//
//
//// --- Classes factices minimales pour que l'exemple compile ---
//// Dans votre projet réel, utilisez vos vraies classes Modele.CasePlateau, Modele.Joueur, Modele.Carte, Modele.Pion.
//class CasePlateau {
//    public enum TypeElement { VIDE, PION_ETUDIANT, PION_MAITRE, OBSTACLE, OBJECTIF, AUTRE } // Ajoutez les types nécessaires
//    private TypeElement type;
//    private Pion pion; // Peut être null si pas de pion
//    private int ligne, colonne; // Pour référence
//
//    public CasePlateau(int l, int c, TypeElement type, Pion pion) {
//        this.ligne = l;
//        this.colonne = c;
//        this.type = type;
//        this.pion = pion;
//    }
//    public TypeElement getTypeElement() { return type; }
//    public Pion getPion() { return pion; }
//    // Ajoutez d'autres getters si CasePlateau contient plus d'infos importantes
//}
//
//
//
//class Carte {
//    private String nom;
//    private String description;
//    private String cheminImage; // Même si on n'affiche pas l'image, l'attribut existe
//
//    public Carte(String nom, String description, String cheminImage) {
//        this.nom = nom;
//        this.description = description;
//        this.cheminImage = cheminImage;
//    }
//    public String getNom() { return nom; }
//    public String getDescription() { return description; }
//    public String getCheminImage() { return cheminImage; }
//}
//
//
//
//class Joueur {
//    private String nom;
//    private Color couleur; // Stockons la couleur pour la démo, même si affichage textuel basique
//    private Pion.TypePion typePion; // Type de pion principal (Maitre/Etudiant)
//
//    public Joueur(String nom, Color couleur) {
//        this.nom = nom;
//        this.couleur = couleur;
//        // Dans un vrai jeu, le type de pion principal pourrait être défini ailleurs
//        this.typePion = (nom.equals("Alice")) ? Pion.TypePion.MAITRE : Pion.TypePion.ETUDIANT; // Exemple
//    }
//    public String getNom() { return nom; }
//    public Color getCouleur() { return couleur; } // Retourne l'objet Color
//    // Vous pourriez aussi avoir une méthode pour obtenir le nom de la couleur
//    public String getNomCouleur() {
//        if (Color.BLUE.equals(couleur)) return "Bleu";
//        if (Color.RED.equals(couleur)) return "Rouge";
//        if (Color.GREEN.equals(couleur)) return "Vert";
//        if (Color.YELLOW.equals(couleur)) return "Jaune";
//        return "Inconnu";
//    }
//}
//
//
//
//class Pion {
//    public enum TypePion { ETUDIANT, MAITRE }
//    private Joueur proprietaire;
//    private TypePion type;
//
//    public Pion(Joueur proprietaire, TypePion type) {
//        this.proprietaire = proprietaire;
//        this.type = type;
//    }
//    public Joueur getProprietaire() { return proprietaire; }
//    public TypePion getType() { return type; }
//}