package Modele;

import Exceptions.CaseVideException;
import Exceptions.DeplacementIllegalExcpetion;
import Modele.IA.IA;
import Patterns.Observable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import static Global.Config.*;
import static Global.Config.ROLEPION.*;
import static Global.Config.TYPECARTE.*;
import static Modele.Utils.*;


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
    private boolean IA1Activee, IA2Activee;
    private boolean partieACommence;
    private boolean roiMort;

    // --- CARTES -- //
    private List<Carte> toutesLesCartes; //Toutes les cartes confondues
    private List<Carte> cartesDuJeu; // 5 cartes, les cartes qui circulent dans le jeu.

    // --- Carte d'échange -- //
    private Carte carteEchange; //La carte qui sera en échange

    // -- GRILLE -- //
    private final List<Pion> pionsJoueurUn = new ArrayList<>(); //Grille implicite: Liste de pions (chaque pion est associé à une position) du premier joueur
    private final List<Pion> pionsJoueurDeux = new ArrayList<>(); //idem pour le deuxième joueur

    CasePlateau casePlateau ;

    private static final Logger logger = Logger.getLogger(Jeu.class.getName());

    /**
     * Crée un Jeu de base, avec des paramètres par défaut
     */
    public Jeu() {
        try {
            _Jeu();

            cartesDuJeu = initCartesJeu();

            initGrille();

            initJoueursCartes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * Crée un jeu avec les cartes fournies en paramètres et des pions positionnés par défaut
     *
     * @param carteEnPlus   carte supplémentaire du jeu
     * @param cartesJoueur1 cartes du joueur 1
     * @param cartesJoueur2 cartes du joueur 2
     */
    public Jeu(TYPECARTE carteEnPlus, List<TYPECARTE> cartesJoueur1, List<TYPECARTE> cartesJoueur2) {
        try {
            verifierSelectionCartesConforme(carteEnPlus, cartesJoueur1, cartesJoueur2);

            _Jeu();

            setCarteSupplementaire(new Carte(carteEnPlus));
            joueur1.addCardsType(cartesJoueur1);
            joueur2.addCardsType(cartesJoueur2);

            initGrille();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crée un jeu avec les cartes et les pions fournis en paramètres
     *
     * @param carteEnPlus     carte supplémentaire du jeu
     * @param cartesJoueur1   cartes du joueur 1
     * @param cartesJoueur2   cartes du joueur 2
     * @param pionsJoueur1   pions du joueur 1
     * @param pionsJoueur2 pions du joueur 2
     */
    public Jeu(TYPECARTE carteEnPlus, List<TYPECARTE> cartesJoueur1, List<TYPECARTE> cartesJoueur2, List<Pion> pionsJoueur1, List<Pion> pionsJoueur2) {
        try {
            verifierSelectionCartesConforme(carteEnPlus, cartesJoueur1, cartesJoueur2);
            verifierSelectionPionsConforme(pionsJoueurUn, pionsJoueurDeux);

            _Jeu();

            setCarteSupplementaire(new Carte(carteEnPlus));
            joueur1.addCardsType(cartesJoueur1);
            joueur2.addCardsType(cartesJoueur2);

            setPionsJeu(pionsJoueur1, pionsJoueur2);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crée un jeu avec les pions fournis en paramètres et des cartes tirées aléatoirement
     * @param pionsJoueur1 pions du joueur 1
     * @param pionsJoueur2 pions du joueur 2
     */
    public Jeu(List<Pion> pionsJoueur1, List<Pion> pionsJoueur2) {
        try {
            verifierSelectionPionsConforme(pionsJoueur1, pionsJoueur2);

            _Jeu();

            toutesLesCartes = initCartes();
            cartesDuJeu = initCartesJeu();
            initJoueursCartes();

            setPionsJeu(pionsJoueur1, pionsJoueur2);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * pseudo constructeur commun aux constructeurs publics
     */
    private void _Jeu() {
        try {
            lignes = colonnes = 5;
            grille = new Pion[LIGNES][COLONNES];
            historique = new Historique<>();
            idJoueurCourant = ID_JOUEUR_1;
            numRound = 1;
            partieFinie = false;
            tempsJeu = 0;
            joueur1 = new Joueur(1, "Joueur 1");
            joueur2 = new Joueur(2, "Joueur 2");
            toutesLesCartes = initCartes();
            carteSelectionee = null;
            partieACommence = false;
            roiMort = false;

            IA1Activee = IA2Activee = false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setPionsJeu(List<Pion> pionsJoueur1, List<Pion> pionsJoueur2) {
        List<Pion> listePions = new ArrayList<>();
        listePions.addAll(pionsJoueur1);
        listePions.addAll(pionsJoueur2);

        resetGrilleAvecPions(listePions);
    }

    /**
     * Rempli la grille de pions suivant les coordonnées de chaque pion dans la liste, le reste de la grille est vide
     * @param pions liste de pions
     */
    private void resetGrilleAvecPions(List<Pion> pions) {
        resetGrille();
        for (Pion p : pions) {
            Point pos = p.getPosition();
            setCase(pos.x, pos.y, p);
        }
    }

    /**
     * Remet la grille du jeu à zéro, toutes les cases sont vides
     */
    private void resetGrille() {
        grille = new Pion[LIGNES][COLONNES];
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
        for (Point p : coordonnees) {
            Pion pion = new Pion(proprietaire, p, role);
            setCase(p.x, p.y, pion); // ajout grille jeu
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

    private void initJoueursCartes() {
        //Initialisation des joueurs de la partie
        //Pour chaque joueur, on accorde deux cartes des 5 cartes de la partie:
        Carte carte1Joueur1 = cartesDuJeu.get(0);
        Carte carte2Joueur1 = cartesDuJeu.get(1);
        Carte carte1Joueur2 = cartesDuJeu.get(2);
        Carte carte2Joueur2 = cartesDuJeu.get(3);
        //La carte qui reste est la carte d'échange
        carteEchange = cartesDuJeu.get(4);
        //On crée la classe des deux joueurs

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
        if (!peutAnnulerCoup()) {
            logger.info("Impossible d'annuler un Coup");
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
        if (!peutRefaireCoup()) {
            logger.info("Impossible de refaire un Coup");
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
        return lignes;
    }

    public int colonnes() {
        return colonnes;
    }

    public int casesTotales() {
        return lignes() * colonnes();
    }

    public void verifieSiDansGrille(int i, int j) {
        if (i < 0 || i >= lignes() || j < 0 || j >= colonnes()) {
            throw new IndexOutOfBoundsException("Tentative d'accèder à la case [" + i + "," + j + "] dans un Jeu de taille " + lignes() + "x" + colonnes());
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

    public boolean estPionEtudiantJoueur1(int i, int j) throws CaseVideException {
        try {
            Pion p = getCase(i, j);
            return p.getIDProprietaire() == ID_JOUEUR_1 && p.getRole() == PION_ETUDIANT;
        } catch (NullPointerException e) {
            throw new CaseVideException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean estPionEtudiantJoueur2(int i, int j) throws CaseVideException {
        try {
            Pion p = getCase(i, j);
            return p.getIDProprietaire() == ID_JOUEUR_2 && p.getRole() == PION_ETUDIANT;
        } catch (NullPointerException e) {
            throw new CaseVideException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean estPionMaitreJoueur1(int i, int j) throws CaseVideException {
        try {
            Pion p = getCase(i, j);
            return p.getIDProprietaire() == ID_JOUEUR_1 && p.getRole() == PION_MAITRE;
        } catch (NullPointerException e) {
            throw new CaseVideException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean estPionMaitreJoueur2(int i, int j) throws CaseVideException {
        try {
            Pion p = getCase(i, j);
            return p.getIDProprietaire() == ID_JOUEUR_2 && p.getRole() == PION_MAITRE;
        } catch (NullPointerException e) {
            throw new CaseVideException(e);
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

    public Carte getCarteSelectionnee() {
        return carteSelectionee;
    }

    public void setCarteSelectionnee(Carte c) {
        carteSelectionee = c;
    }

    public boolean estPionDuJoueurCourant(int i, int j) throws CaseVideException {
        try {
            Pion p = getCase(i, j);
            return p.getIDProprietaire() == getIdJoueurCourant();
        } catch (NullPointerException e) {
            throw new CaseVideException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getProprietairePionAt(int i, int j) throws CaseVideException {
        try {
            Pion p = getCase(i, j);
            return p.getIDProprietaire();
        } catch (NullPointerException e) {
            throw new CaseVideException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ROLEPION getRolePionAt(int i, int j) throws CaseVideException {
        try {
            Pion p = getCase(i, j);
            return p.getRole();
        } catch (NullPointerException e) {
            throw new CaseVideException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ######## PARTIE ########

    public void activerIA1() {
        IA1Activee = true;
    }

    public void activerIA2() {
        IA2Activee = true;
    }

    public void nouvellePartie() {
        return;
    }

    public long getTempsDeJeu() {
        return tempsJeu;
    }

    public void setTempsDeJeu(long secondes) {
        this.tempsJeu = secondes;
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
        return new CasePlateau(this,new Point(row,col));
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

    public List<Coup> getCoupsPossibles(Carte carteSelectionee, Point positionPion) throws IllegalStateException {
        return Utils.getCoupsPossibles(this, carteSelectionee, positionPion);
    }

    private void changerJoueur() {
        idJoueurCourant = (idJoueurCourant % 2) + 1;

    }

    // code santiago
    // --------------------
    private void deplacerPion(Point depart, Point arrivee) {
        try {
            // Récupère l'éventuel pion présent sur la case d'arrivée
            Pion cible = getCase(arrivee.x, arrivee.y);
            // Si c'est un pion adverse, il sera écrasé par le setCase suivant
            if (cible != null && cible.getIDProprietaire() != idJoueurCourant) {

            }
            // On déplace le pion
            Pion p = getCase(depart.x, depart.y);
            setCase(depart.x, depart.y, null);
            if (getRolePionAt(arrivee.x, arrivee.y) == PION_MAITRE) {
                roiMort = true;
            }
            setCase(arrivee.x, arrivee.y, p);
            // Mise à jour des listes de pions
            majPions();
        } catch (CaseVideException ignored) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void majPionsJoueur1() {
        pionsJoueurUn.clear();
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                Pion p = grille[i][j];
                if (! estCaseVide(i, j) && p.getIDProprietaire() == ID_JOUEUR_1) {
                    pionsJoueurUn.add(p);
                }
            }
        }
    }


    private void majPionsJoueur2() {
        pionsJoueurDeux.clear();
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                Pion p = grille[i][j];
                if (! estCaseVide(i, j) && p.getIDProprietaire() == ID_JOUEUR_2) {
                    pionsJoueurDeux.add(p);
                }
            }
        }
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
    // --------------------

    private void faireSetup() {
        if (partieACommence) {
            return;
        } else {
            partieACommence = true;
        }


    }

    public Coup preparerCoup(Carte carteSelectionee, Point depart, Point arrivee) {
        Coup c = new Coup(depart, arrivee);
        List<Coup> coupsPossibles = getCoupsPossibles(carteSelectionee, depart);


       for (Coup cp : coupsPossibles) {
           if (cp.equals(c)) {
               return c;
           }
       }
       return null;
    }

    public Coup preparerCoup(Carte carteSelectionee, CasePlateau depart, CasePlateau arrivee) {
        Point d = depart.position;
        Point a = arrivee.position;
        Coup c = new Coup(d, a);
        List<Coup> coupsPossibles = getCoupsPossibles(carteSelectionee, d);

        if (! coupsPossibles.contains(c)) {
            return null;
        }

        return c;
    }

    private boolean verifierVictoire() {
        try {
            return (getPionsJoueur1().isEmpty() && getIdJoueurCourant() == ID_JOUEUR_2)
                    || (getPionsJoueur2().isEmpty() && getIdJoueurCourant() == ID_JOUEUR_1)
                    || roiMort
                    || (getRolePionAt(TEMPLE_JOUEUR_1.x, TEMPLE_JOUEUR_1.y) == PION_MAITRE && getProprietairePionAt(TEMPLE_JOUEUR_1.x, TEMPLE_JOUEUR_1.y) == ID_JOUEUR_2)
                    || (getRolePionAt(TEMPLE_JOUEUR_2.x, TEMPLE_JOUEUR_2.y) == PION_MAITRE && getProprietairePionAt(TEMPLE_JOUEUR_2.x, TEMPLE_JOUEUR_2.y) == ID_JOUEUR_1);
        } catch (CaseVideException ignored) {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void jouerCoup(Coup c) {
        try {
            if (carteSelectionee == null) {
                throw new IllegalStateException("Il faut d'abord choisir une carte avant de jouer un Coup");
            }
            if (c == null) {
                logger.info("Coup invalide");
                return;
            }

            faireSetup();

            Point depart = c.getDepart();
            Point arrivee = c.getArrivee();
            int x, y;
            x = arrivee.x;
            y = arrivee.y;
            verifieSiDansGrille(x, y);

            if (!estCaseVide(x, y) && getProprietairePionAt(x, y) == getIdJoueurCourant()) {
                throw new DeplacementIllegalExcpetion("Impossible de capturer son propre pion");
            }

            // met à jour la grille
            deplacerPion(depart, arrivee);
            historique.add(c);
            if(verifierVictoire()) {
                partieFinie = true;
                metAJour();
                return;
            }


            echangerCartes(getJoueurCourant(), getCarteSelectionnee());
            changerJoueur();
            //carteSelectionee = null;

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
     * @param niveauIA Chaîne représentant le niveau (ex: "Facile", "Moyen", "Fort").
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

    /**
     * Renvoie la représentation textuelle du jeu
      * @return chaine de caractères représentant le jeu
     */
    @Override
    public String toString() {
        try {
            StringBuilder S = new StringBuilder();
            for (int i = 0; i < lignes(); i++) {
                for (int j = 0; j < colonnes(); j++) {
                    if (estCaseVide(i, j)) {
                        S.append("  ");
                    } else if (estPionEtudiantJoueur1(i, j)) {
                        S.append("E1");
                    } else if (estPionEtudiantJoueur2(i, j)) {
                        S.append("E2");
                    } else if (estPionMaitreJoueur1(i, j)) {
                        S.append("M1");
                    } else if (estPionMaitreJoueur2(i, j)) {
                        S.append("M2");
                    } else {
                        S.append("??");
                    }
                }
                S.append("\n");
            }

            return S.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
