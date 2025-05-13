package Modele;

import Modele.IA.IA;
import Patterns.Observable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Global.Config.ROLEPION;

import static Global.Config.*;
import static Global.Config.ROLEPION.PION_ETUDIANT;
import static Global.Config.ROLEPION.PION_MAITRE;
import static Global.Config.TYPECARTE.*;


public class Jeu extends Observable {
    private Pion [][] grille; // grille de pions
    private Historique<Coup> historique;
    private Joueur joueur1, joueur2;
    private IA IA_1, IA_2;
    private int lignes, colonnes;
    private int idJoueurCourant; // identifiant du joueur courant
    private Carte carteSelectionee;
    private long tempsJeu; // temps écoulé depuis le début de la partie
    private int numRound; // à quel round on en est
    private boolean partieFinie;

    // --- CARTES -- //
    private List<Carte> toutesLesCartes; //Toutes les cartes confondues
    private List<Carte> cartesDuJeu; //5 cartes, les cartes qui circulent dans le jeu.

    // --- Carte d'échange -- //
    private Carte carteEchange; //La carte qui sera en échange

    // -- GRILLE -- //
    private final List<Pion> pionsJoueurUn = new ArrayList<>(); //Grille implicite: Liste de pions (chaque pion est associé à une position) du premier joueur
    private final List<Pion> pionsJoueurDeux = new ArrayList<>(); //idem pour le deuxième joueur




    public Jeu() {
        lignes = colonnes = 5;

        grille = new Pion[LIGNES][COLONNES];

        historique = new Historique<>();

        toutesLesCartes = initCartes();
        cartesDuJeu = initCartesJeu();

        initGrille();

        initJoueurs();

        idJoueurCourant = 1;
        numRound = 1;
        partieFinie = false;

    }


    /**
     * renvoie une liste qui contient toutes les cartes du jeu
     * @return Une ArrayList de Cartes
     */
    private List<Carte> initCartes() {
        ArrayList<Carte> cartes = new ArrayList<>();
        cartes.add(new Carte(TIGRE));
        cartes.add(new Carte(DRAGON));
        cartes.add(new Carte(GRENOUILLE));
        cartes.add(new Carte(LAPIN));
        cartes.add(new Carte(CRABE));
        cartes.add(new Carte(ELEPHANT));
        cartes.add(new Carte(OIE));
        cartes.add(new Carte(COQ));
        cartes.add(new Carte(SINGE));
        cartes.add(new Carte(MANTE));
        cartes.add(new Carte(CHEVAL));
        cartes.add(new Carte(BOEUF));
        cartes.add(new Carte(GRUE));
        cartes.add(new Carte(SANGLIER));
        cartes.add(new Carte(ANGUILLE));
        cartes.add(new Carte(COBRA));
        return cartes;

    }

    private List<Carte> initCartesJeu() {
        //On tire 5 cartes aléatoirement
        Random r = new Random();
        List<Carte> cartesTirees = new ArrayList<>();
        int cartesAtirer = 5;
        // Mécanisme pour assurer qu'on ne tire pas deux foix la meme carte
        ArrayList<Integer> duplique = new ArrayList<>();
        while(cartesAtirer > 0)
        {
            //On récupère un indice aléatoirement
            int indice = r.nextInt(toutesLesCartes.size());
            //Si c'est la première fois qu'on voit cette indice (et du coup c'est la premère fois qu'on voit la carte, car un indice est uniquement associé à une carte)
            if(!duplique.contains(indice))
            {
                //On ajoute l'indice à la liste duplique (car maintenant on l'a vue)
                duplique.add(indice);
                //On stocke la carte dans la liste des cartes du jeu
                Carte carte = toutesLesCartes.get(indice);
                cartesTirees.add(carte);
                cartesAtirer--;
            }

        }
        return cartesTirees;
    }

    private void ajouterPion(List<Point> coordonnees, int proprietaire, ROLEPION role) {
        for (int i = 0; i < coordonnees.size(); i++) {
            Point p = coordonnees.get(i);
            Pion pion = new Pion(proprietaire, p, role);
            grille[p.x][p.y] = pion; // ajout grille jeu
            if (proprietaire == 1) { // ajout dans liste joueur
                pionsJoueurUn.add(pion);
            } else {
                pionsJoueurDeux.add(pion);
            }

        }
    }


    /**
     * Initialize la grille avec l'ensemble des cases vides et des pions maitres et étudiants.
     *
     */
    private void initGrille()
    {


        // Ajouter pion étudiant
        // Pour le joueur 1 et 2, on ajoute les positions initiales des pions étudiants et maitres, selon le joueur bien sur
        ajouterPion(new ArrayList<Point>()
        {{
            add(new Point(0,0));
            add(new Point(0,1));
            add(new Point(0,3));
            add(new Point(0,4));
        }}, 1, PION_ETUDIANT);
        ajouterPion(new ArrayList<Point>()
        {{
            add(new Point(4,0));
            add(new Point(4,1));
            add(new Point(4,3));
            add(new Point(4,4));
        }}, 2, PION_ETUDIANT);

        // Ajouter pion maitre
        ajouterPion(new ArrayList<Point>(){{add(new Point(0, 2));}}, 1, PION_MAITRE);
        ajouterPion(new ArrayList<Point>(){{add(new Point(4, 2));}}, 2, PION_MAITRE);
    }

    private void initJoueurs() {
        //Initialisation des joueurs de la partie
        //Pour chaque joueur, on accorde deux cartes des 5 cartes de la partie:
        Carte carte1Joueur1 = cartesDuJeu.get(0);
        Carte carte2Joueur1 = cartesDuJeu.get(1);
        Carte carte1Joueur2 = cartesDuJeu.get(2);
        Carte carte2Joueur2 = cartesDuJeu.get(3);
        //La carte qui reste est la carte d'échange
        carteEchange = cartesDuJeu.get(4);
        //On crée la classe des deux joueurs
        joueur1 = new Joueur(1, "Joueur 1");
        joueur2 = new Joueur(2, "Joueur 2");

        joueur1.addCard(carte1Joueur1);
        joueur1.addCard(carte2Joueur1);
        joueur2.addCard(carte1Joueur2);
        joueur2.addCard(carte2Joueur2);
    }



// ######## ANNULER / REFAIRE ########

    public boolean peutAnnulerCoup() {
        return historique.peutAnnuler();
    }

    public boolean peutRefaireCoup() {
        return historique.peutRefaire();
    }

    public void annulerCoup() {
        if (! peutAnnulerCoup()) {
            System.err.println("Impossible d'annuler un Coup");
            return;
        }

        if (estPartieFinie()) {
            System.err.println("Impossible d'annuler un Coup\nLA PARTIE EST TERMINEE :)");
            return;

        }
        Coup c = historique.annuler();

        // faire des choses avec le coup

        // met à jour l'interface
        metAJour();
    }

    public void refaireCoup() {
        if (! peutRefaireCoup()) {
            System.err.println("Impossible de refaire un Coup");
            return;
        }

        Coup c = historique.refaire();

        // faire des choses avec le coup

        // met à jour l'interface
        metAJour();
    }

    // ######### CHARGER / SAUVEGARDER ########

    public void sauvegarderJeu() {
        return;
    }

    public void chargerJeu(String fichier) {
        return;
    }

    public List<String> listerSauvegardes() {
        return null;
    }

// ######## STATUT / DONNEES ########

    public int lignes() {
        return 5;
    }

    public int colonnes() {
        return 5;
    }

    public int casesTotales() {
        return lignes() * colonnes();
    }

    private void verifieSiDansGrille(int i, int j) {
        if (i < 0 || i > lignes() || j < 0 || j > colonnes()) {
            throw new RuntimeException("Tentative d'accèder à la case [" + i + "," + j + "] dans un Jeu de taille " + lignes() + "x" + colonnes());
        }
    }

    private Pion getCase(int i, int j) {
        try {
            verifieSiDansGrille(i, j);
            return grille[i][j];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setCase(int i, int j, Pion p) {
        try {
            verifieSiDansGrille(i, j);
            grille[i][j] = p;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean estCaseVide(int i, int j) {
        try {
            return getCase(i, j) == null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean estPionEtudiantJoueur1(int i, int j) {
        try {
            Pion p = getCase(i, j);
            return p.getProprietaire() == ID_JOUEUR_1 && p.getRole() == PION_ETUDIANT;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean estPionEtudiantJoueur2(int i, int j) {
        try {
            Pion p = getCase(i, j);
            return p.getProprietaire() == ID_JOUEUR_2 && p.getRole() == PION_ETUDIANT;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean estPionMaitreJoueur1(int i, int j) {
        try {
            Pion p = getCase(i, j);
            return p.getProprietaire() == ID_JOUEUR_1 && p.getRole() == PION_MAITRE;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean estPionMaitreJoueur2(int i, int j) {
        try {
            Pion p = getCase(i, j);
            return p.getProprietaire() == ID_JOUEUR_2 && p.getRole() == PION_MAITRE;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pion> getPionsJoueur1() {
        return new ArrayList<>(pionsJoueurUn);
    }

    public List<Pion> getPionsJoueur2() {
        return new ArrayList<>(pionsJoueurDeux);
    }

    public List<Pion> getPionsJoueurCourant() {
        if (getIdJoueurCourant() == ID_JOUEUR_1) {
            return new ArrayList<>(pionsJoueurUn);
        } else {
            return new ArrayList<>(pionsJoueurDeux);
        }
    }

    public List<Carte> getCartesJoueur1() {
        return joueur1.getCartesEnMain();
    }

    public List<Carte> getCartesJoueur2() {
        return joueur2.getCartesEnMain();
    }

    public List<Carte> getCartesJoueurCourant() {
        if (getIdJoueurCourant() == ID_JOUEUR_1) {
            return joueur1.getCartesEnMain();
        } else {
            return joueur2.getCartesEnMain();
        }
    }

    public Carte getCarteSupplementaire() {
        return carteEchange;
    }

    public void setCarteSupplementaire(Carte c) {
        carteEchange = c;
    }

    public Carte getCarteJouee() {
        return carteSelectionee;
    }

    public boolean estPionDuJoueurCourant(int i, int j) {
        try {
            Pion p = getCase(i, j);
            return p.getProprietaire() == idJoueurCourant ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean estCoupConforme(int i, int j) {
        return false;
    }

    public int getProprietairePionAt(int i, int j) {
        try {
            Pion p = getCase(i, j);
            return p.getProprietaire();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ROLEPION getRolePionAt(int i, int j) {
        try {
            Pion p = getCase(i, j);
            return p.getRole();
        } catch (NullPointerException e) {
            throw new NullPointerException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

// ######## PARTIE ########

    public void nouvellePartie() {
        return;
    }

    public long getTempsDeJeu() {
        return tempsJeu;
    }

    public void setTempsDeJeu(long temp) {
        this.tempsJeu = temp;
    }

    public void setNomJoueur1(String nom) {
        this.joueur1.setNom(nom);
    }

    public void setNomJoueur2(String nom) {
        this.joueur2.setNom(nom);
    }

    public String getNomJoueur1() {
        return joueur1.getNom();
    }

    public String getNomJoueur2() {
        return joueur2.getNom();
    }

    public String getNomJoueurCourant() {
        return getJoueurCourant().getNom();
    }

    public Joueur getJoueurCourant() {
        return (idJoueurCourant == ID_JOUEUR_1) ? joueur1 : joueur2;
    }

    public int getIdJoueurCourant() {
        return idJoueurCourant;
    }

    public int getNumeroRound() {
        return numRound;
    }

    public CasePlateau getCasePlateau(int row, int col) {
        //TODO
        return new CasePlateau(this, new Point(row, col));
    }

    public Carte getCartesSurLeTerrain(int i) {
       try {
           return cartesDuJeu.get(i);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    public Joueur getJoueur(int id) {
        return (id == ID_JOUEUR_1) ? joueur1 : joueur2;
    }

    public boolean estDeplacementConforme(int i, int j) {
        return false;
    }

    public boolean estPartieFinie() {
        return partieFinie;
    }

    public List<Coup> getCoupsPossibles(Point positionPion, Carte carteJoue) {
        List<Coup> coups = new ArrayList<>();

        List<Point> deplacements = carteJoue.getMoves();

        int idJoueurCourant = getIdJoueurCourant();
        Point direction;
        if (idJoueurCourant == ID_JOUEUR_1) {
            direction = new Point(1, 1);
        } else {
            direction = new Point(-1, -1);
        }

        int x, y;
        for (int i = 0; i < deplacements.size(); i++) {
            Point deplacement = deplacements.get(i);
            x = positionPion.x + deplacement.x * direction.x;
            y = positionPion.y + deplacement.y * direction.y;

            try {
                verifieSiDansGrille(x, y);
            } catch (Exception e) {
                continue;
            }

            // ne pas manger son propre pion
            if (getProprietairePionAt(x, y) == getIdJoueurCourant()) {
                continue;
            }

            // origine / position pion -> case arrivée possible
            coups.add(new Coup(new Point(positionPion.x, positionPion.y), new Point(x, y)));
        }


        return coups;
    }

    private void changerJoueur() {
        idJoueurCourant = (idJoueurCourant % 2) + 1;
    }

    private void deplacerPion(Point depart, Point arrivee) {
        Pion p = getCase(depart.x, depart.y);
        setCase(depart.x, depart.y, null);
        setCase(arrivee.x, arrivee.y, p);
        majPions();
    }

    private void majPionsJoueur1() {

    }

    private void majPionsJoueur2() {

    }

    private void majPions() {
        majPionsJoueur1();
        majPionsJoueur2();
    }

    private void echangerCartes(Joueur joueur, Carte carteSelectionne) {
       
        joueur.removeCard(carteSelectionne);
        Carte nouvelleCarteJoueurCourant = getCarteSupplementaire();
        setCarteSupplementaire(carteSelectionne);
        joueur.addCard(nouvelleCarteJoueurCourant);

    }

    private boolean verifierVictoire() {
        return false;
    }

    public void jouerCoup(Coup c) {
        try {
            Point depart = c.getDepart();
            Point arrivee = c.getArrivee();
            int x, y;
            x = arrivee.x;
            y = arrivee.y;
            verifieSiDansGrille(x, y);

            if (!estCaseVide(x, y) && getProprietairePionAt(x, y) == getIdJoueurCourant()) {
                throw new RuntimeException("Impossible de capturer son propre pion");
            }

            // met à jour la grille
            deplacerPion(depart, arrivee);
            if(verifierVictoire()) {
                partieFinie = true;
                return;
            }

            // met à jour la liste de pions des joueurs
            echangerCartes(getJoueurCourant(), getCarteJouee());
            changerJoueur();

            // met à jour l'interface
            metAJour();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * @return Temps de réflexion en millisecondes configuré pour l'IA.
     */
    public int getConfigIAReflexion() {
        //TODO À implémenter
        return 1000;
    }

    /**
     * @return true si l'IA utilise une heuristique, false sinon.
     */
    public boolean getConfigIAHeuristique() {
        //TODO À implémenter
        return false;
    }

    /**
     * @return Nom de l'algorithme actuellement utilisé par l'IA (ex: "Minimax Simple").
     */
    public String getConfigIAAlgorithme() {
        //TODO À implémenter
        return "Minimax Simple";
    }


    /**
     * @return Vitesse d'animation configurée.
     */
    public int getConfigAnimationVitesse() {
        //TODO À implémenter
        return 0;
    }

    /**
     * Change l'algorithme d'IA utilisé.
     * @param nomAlgorithme Le nom de l'algorithme (ex: "Minimax", "AlphaBeta", etc.)
     */
    public void setIAAlgorithme(String nomAlgorithme) {
        //TODO À implémenter
    }


    /**
     * Active ou désactive l'utilisation d'une heuristique par l'IA.
     * @param active true pour activer l'heuristique, false pour la désactiver.
     */
    public void setIAHeuristique(boolean active) {
        //TODO À implémenter
    }


    /**
     * Définit le temps de réflexion alloué à l'IA.
     * @param tempsMs Temps en millisecondes.
     */
    public void setIAReflexion(int tempsMs) {
        //TODO À implémenter
    }


    /**
     * Active ou désactive le mode automatique (jeu sans intervention utilisateur).
     * @param nouvelEtat true pour activer le mode automatique, false pour le désactiver.
     */
    public void setModeAuto(boolean nouvelEtat) {
        //TODO À implémenter
    }


    /**
     * Définit le niveau de difficulté de l’IA.
     * @param niveauIA Chaîne représentant le niveau (ex: "Facile", "Moyen", "Difficile").
     */
    public void setNiveauIA(String niveauIA) {
        //TODO À implémenter
    }


    /**
     * Démarre une nouvelle partie à partir d'une sélection donnée.
     * @param partieSelectionee Identifiant ou nom de la partie sélectionnée.
     */
    public void setNouvellePartie(String partieSelectionee) {
        //TODO À implémenter
    }


    /**
     * Définit la carte sélectionnée pour le jeu (ex: avant placement).
     * @param carte Carte sélectionnée.
     */
    public void setCarteSelectionne(Carte carte) {
        //TODO À implémenter
    }


    /**
     * Termine la partie en cours avec sauvegarde (ex: quitter).
     */
    public void setTerminerJeu() {
        //TODO À implémenter
    }


    /**
     * Met le jeu en pause ou le reprend.
     */
    public void setPause() {
        //TODO À implémenter
    }


    /**
     * Active ou désactive la présence de l'IA (mode manuel <-> IA).
     */
    public void basculeIA() {
        //TODO À implémenter
    }


    /**
     * Définit la case de destination ciblée sur le plateau.
     * @param casePlateau La case cible.
     */
    public void setCasePlateauCible(CasePlateau casePlateau) {
        //TODO À implémenter
    }


    /**
     * Définit la case de destination ciblée sur le plateau via ses coordonnées.
     * @param xDest Abscisse de la case cible.
     * @param yDest Ordonnée de la case cible.
     */
    public void setCasePlateauCible(int xDest, int yDest) {
        //TODO À implémenter
    }


    /**
     * Sélectionne un pion à déplacer via ses coordonnées.
     * @param xDepart Abscisse du pion.
     * @param yDepart Ordonnée du pion.
     */
    public void setPionSelectionne(int xDepart, int yDepart) {
        //TODO À implémenter
    }


    /**
     * Sélectionne un pion à déplacer via une instance de Pion.
     * @param pion Le pion à sélectionner.
     */
    public void setPionSelectionne(Pion pion) {
        //TODO À implémenter
    }

}
