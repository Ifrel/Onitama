package Modele;

import Exceptions.CaseVideException;
import Global.Config;
import Modele.IA.IA;
import Modele.IA.IAFaible;
import Modele.IA.IAFort;
import Modele.IA.IAMoyen;
import Patterns.Observable;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import static Global.Config.*;
import static Global.Config.ETAT_GRILLE.DEFAUT;
import static Global.Config.ETAT_GRILLE.PION_SELECTIONNE;
import static Global.Config.ETAT_JEU.*;
import static Global.Config.ROLEPION.PION_ETUDIANT;
import static Global.Config.ROLEPION.PION_MAITRE;
import static Global.Config.TYPECARTE.*;
import static Global.Config.TYPE_JOUEUR.JOUEUR_HUMAIN;
import static Modele.Utils.*;


public class Jeu extends Observable implements Runnable {
    private Pion [][] grille; // grille de pions
    private Historique<Coup> historique;
    private Joueur joueur1, joueur2;
    private Joueur JOUEUR_1, JOUEUR_2; // probablement pas incroyable, mais on garde une référence des deux joueurs au cas où, comme pour les IA
    private Joueur joueurCourant;
    private int idJoueurCourant; // identifiant du joueur courant
    private int numCarteSelectionee;
    private long tempsJeu; // temps écoulé depuis le début de la partie
    private int numRound; // à quel round on en est
    private boolean partieFinie;
    private boolean IA1Activee, IA2Activee;
    private IA IA_1, IA_2;
    private boolean partieACommence;
    private boolean maitreMort;
    private Pion pionSelectionne;
    private ETAT_GRILLE etatGrille;
    private ETAT_JEU etatJeu;
    private Coup dernierCoupJoue;


    // --- CARTES -- //
    private List<Carte> toutesLesCartes; //Toutes les cartes confondues
    private List<Carte> cartesDuJeu; // 5 cartes, les cartes qui circulent dans le jeu.

    // --- Carte d'échange -- //
    private Carte carteSupplementaire; //La carte qui sera en échange

    // -- GRILLE -- //
    private final List<Pion> pionsJoueur1 = new ArrayList<>(); //Grille implicite: Liste de pions (chaque pion est associé à une position) du premier joueur
    private final List<Pion> pionsJoueur2 = new ArrayList<>(); //idem pour le deuxième joueur

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
            verifierSelectionPionsConforme(this.pionsJoueur1, this.pionsJoueur2);

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
            grille = new Pion[LIGNES][COLONNES];
            historique = new Historique<>();
            idJoueurCourant = ID_JOUEUR_1;
            numRound = 1;
            partieFinie = false;
            tempsJeu = 0;
            joueur1 = JOUEUR_1 = new Joueur(1, "Joueur 1");
            joueur2 = JOUEUR_2 = new Joueur(2, "Joueur 2");
            toutesLesCartes = initCartes();
            numCarteSelectionee = 0;
            pionSelectionne = null;
            partieACommence = false;
            maitreMort = false;
            etatGrille = DEFAUT;
            etatJeu = DEBUT;
            dernierCoupJoue = null;
            joueurCourant = joueur1;

            IA_1 = IA_2 = null;
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
                pionsJoueur1.add(pion);
            } else {
                pionsJoueur2.add(pion);
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
        ajouterPion(new ArrayList<>()
        {{
            add(new Point(0,0));
            add(new Point(0,1));
            add(new Point(0,3));
            add(new Point(0,4));
        }}, ID_JOUEUR_1, PION_ETUDIANT);
        ajouterPion(new ArrayList<>()
        {{
            add(new Point(4,0));
            add(new Point(4,1));
            add(new Point(4,3));
            add(new Point(4,4));
        }}, ID_JOUEUR_2, PION_ETUDIANT);

        // Ajouter pion maitre
        ajouterPion(new ArrayList<>(){{add(new Point(0, 2));}}, ID_JOUEUR_1, PION_MAITRE);
        ajouterPion(new ArrayList<>(){{add(new Point(4, 2));}}, ID_JOUEUR_2, PION_MAITRE);
    }

    private void initJoueursCartes() {
        //Initialisation des joueurs de la partie
        //Pour chaque joueur, on accorde deux cartes des 5 cartes de la partie:
        Carte carte1Joueur1 = cartesDuJeu.get(0);
        carte1Joueur1.setProprietaire(ID_JOUEUR_1);
        Carte carte2Joueur1 = cartesDuJeu.get(1);
        carte2Joueur1.setProprietaire(ID_JOUEUR_1);
        Carte carte1Joueur2 = cartesDuJeu.get(2);
        carte1Joueur2.setProprietaire(ID_JOUEUR_2);
        Carte carte2Joueur2 = cartesDuJeu.get(3);
        carte2Joueur2.setProprietaire(ID_JOUEUR_2);
        //La carte qui reste est la carte d'échange
        carteSupplementaire = cartesDuJeu.get(4);
        //On crée la classe des deux joueurs

        joueur1.clearHand();
        joueur1.addCard(carte1Joueur1);
        joueur1.addCard(carte2Joueur1);

        joueur2.clearHand();
        joueur2.addCard(carte1Joueur2);
        joueur2.addCard(carte2Joueur2);
    }



// ######## ANNULER / REFAIRE ########

    public boolean peutAnnulerCoup() {
        return historique.peutAnnuler() && ! estActiveIA2();
    }

    public boolean peutRefaireCoup() {
        return historique.peutRefaire() && ! estActiveIA2();
    }

    public void annulerCoup() {
        try {
            if (!peutAnnulerCoup()) {
                logger.info("Impossible d'annuler un Coup");
                return;
            }

            if (estPartieFinie()) {
                System.err.println("Impossible d'annuler un Coup\nLA PARTIE EST TERMINEE :)");
                return;

            }

            int nbAAnnuler = 1;
            if (estActiveIA1()) {
                nbAAnnuler = 2;
            }

            for (int i = 0; i < nbAAnnuler; i++) {
                Coup c = historique.annuler();
                Point depart = c.getDepart();
                Point arrivee = c.getArrivee();
                boolean pionMange = c.getPionMange();
                //Restaurer la grille avant de jouer le coup
                restaurerGrille(depart, arrivee, pionMange);
                //Restaurer la main du joueur avant de joueur le coup
                changerJoueur();
                //Restaurer le joueur qui avait joué le coup
                restaurerMainJoueur(c.getCarteEchangee(), getCarteSupplementaire());
                // met à jour l'interface
                metAJour();

                if (i == 0 && nbAAnnuler == 2) {
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void restaurerMainJoueur(Carte c1, Carte c2) {
        Joueur joueur = getJoueurCourant();
        joueur.removeCard(c1);
        c1.setProprietaire(0);
        setCarteSupplementaire(c1);
        joueur.addCard(c2);
        c2.setProprietaire(joueur.getId());
    }


    private void restaurerGrille(Point depart, Point arrivee, boolean pionMange) {
        //Mon pion de ce coup est maintenant à la position arrivee (car le coup a été joué)
        // Il faut le deplacer à la position départ
        //recupération du pion à la case vide
        deplacerPion(arrivee, depart);
        //Si un pion a été mangé
        if(pionMange)
        {
            //Necessairement, il s'agit d'un pion du joueur actuel (car c'est celui qui doit jouer maintenant, autrement dit c'est celui qui n'a pas joué le dernier coup)
            //et nécessairement, il s'agit d'un pion étudiant (sinon la partie est terminé!)
            //La position de ce pion est la position d'arrivée (car il a été mangé)
            Pion pionM = new Pion(idJoueurCourant, arrivee, PION_ETUDIANT);
            setCase((int) arrivee.getX(), (int) arrivee.getY(), pionM);
        }
        majPions();


    }


    public Coup suggererCoup() {
        // sera probablement à modifier, fait de cette manière pour accélerer l'intégration de cette fonctionnalité
        IA ia_coup = new IAFort(this, getIdJoueurCourant(), "IA suggestion coup");
        return ia_coup.calculerCoup();
    }


    public void refaireCoup() {
        if (!peutRefaireCoup()) {
            logger.info("Impossible de refaire un Coup");
            return;
        }

        int nbAAnnuler = 1;
        if (estActiveIA1()) {
            nbAAnnuler = 2;
        }

        for (int i = 0; i < nbAAnnuler; i++) {

            Coup c = historique.refaire();

            //repliquer le mouvement original
            deplacerPion(c.getDepart(), c.getArrivee());
            echangerCartes(getJoueurCourant(), c.getCarteEchangee());
            //restaurer selection /viasual
            setPionSelectionne(c.getArrivee());

            changerJoueur();
            //restaurerMainJoueur(getCarteSupplementaire(), c.getCarteEchangee());


            // met à jour l'interface
            metAJour();
            if (i == 0 && nbAAnnuler == 2) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // ######### CHARGER / SAUVEGARDER ########

    public void sauvegarderJeu() throws IOException {
        System.err.println("UTILISER LA METHODE sauvegarderJeu(String nomFichier)");
    }
    public void sauvegarderJeu(String fichier) throws IOException {

        try(ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(SAVE_DIR.resolve(fichier))))
        {
            out.writeObject(idJoueurCourant);
            out.writeObject(grille);
            out.writeObject(joueur1);
            out.writeObject(joueur2);
            out.writeObject(carteSupplementaire);
            out.writeObject(historique);

        }
    }


    public void chargerJeu() throws IOException, ClassNotFoundException{
        System.err.println("UTILISER LA METHODE chargerJeu(String nomFichier)");
    }


    @SuppressWarnings("unchecked")
    public void chargerJeu(String fichier) throws IOException, ClassNotFoundException {
        try(ObjectInputStream in = new ObjectInputStream(Files.newInputStream(SAVE_DIR.resolve(fichier))))
        {
            idJoueurCourant = (Integer) in.readObject();
            grille = (Pion[][]) in.readObject();
            joueur1 = (Joueur) in.readObject();
            joueur2 = (Joueur) in.readObject();
            carteSupplementaire = (Carte) in.readObject();
            historique = (Historique<Coup>) in.readObject();
            majPions();
        }
    }

    public List<String> listerSauvegardes() {
        ArrayList<String> liste = new ArrayList<>();
        File saveDir = new File(SAVE_DIR.toString());
        if (! saveDir.exists()) {
            return liste;
        }
        File[] files = saveDir.listFiles();

        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                liste.add(name);
            }
        }
        return liste;
    }

    // ######## STATUT / DONNEES ########

    public void verifieSiDansGrille(int i, int j) {
        if (i < 0 || i >= LIGNES || j < 0 || j >= COLONNES) {
            throw new IndexOutOfBoundsException("Tentative d'accèder à la case [" + i + "," + j + "] dans un Jeu de taille " + LIGNES + "x" + COLONNES);
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

    public List<Pion> getPionsJoueur1() {
        return new ArrayList<>(pionsJoueur1);
    }

    public List<Pion> getPionsJoueur2() {
        return new ArrayList<>(pionsJoueur2);
    }

    public List<Pion> getPionsJoueurCourant() {
        return joueurCourant.getPions();
    }

    public List<Carte> getCartesJoueur1() {
        return joueur1.getCartesEnMain();
    }

    public List<Carte> getCartesJoueur2() {
        return joueur2.getCartesEnMain();
    }

    public List<Carte> getCartesJoueurCourant() {
        return joueurCourant.getCartesEnMain();
    }

    public Carte getCarteSupplementaire() {
        return carteSupplementaire.clone();
    }

    public void setCarteSupplementaire(Carte c) {
        carteSupplementaire = c;
    }

    public int getNumCarteSelectionnee() {
        return this.numCarteSelectionee;
    }


    public Pion getPionSelectionne() {
        return this.pionSelectionne.clone();
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


    public void launchIA() {
        if (! estActiveIA1() || ! estActiveIA2()) {
            logger.info("Il faut d'abord activer les IA avant de les lancer");
            return;
        }

        etatJeu = DEBUT_IA;
    }

    /**
     * Active / désactive l'IA 1
     */
    public void toggleIA1() {
        if (partieACommence) {
            logger.info("Impossible de changer l'état de l'IA 1, la partie a commencé");
            return;
        }
        IA1Activee = !IA1Activee; // si l'IA est activée, la désactive, vice-versa

        if (estActiveIA1()) {
            int id = ID_JOUEUR_2;
            if (estActiveIA2()) {
                id = ID_JOUEUR_1;
            }
            IA_1 = new IAFaible(this, id, "IA 1");
        }

        if (! estActiveIA1() && estActiveIA2()) {
            toggleIA2();
        }
        // si l'IA 1 est activée, elle sera joueur 1 si face à l'IA 2, joueur 2 si face à un humain (joueur 1)
        if (estActiveIA1()) {
            if (estActiveIA2()) {
                joueur1 = IA_1;
                joueur2 = IA_2;
            } else {
                joueur1 = JOUEUR_1;
                joueur2 = IA_1;
            }
        } else {
            joueur1 = JOUEUR_1;
            joueur2 = JOUEUR_2;
        }
        joueurCourant = joueur1;
        idJoueurCourant = ID_JOUEUR_1;
        initJoueursCartes();
    }

    /**
     * Active / désactive l'IA 2 (ne peut as être activée sans que l'IA 1 ne soit activée)
     */
    public void toggleIA2() {
        if (partieACommence) {
            logger.info("Impossible de changer l'état de l'IA 2, la partie a commencé");
            return;
        }
        if (!estActiveIA1()) {
            logger.info("L'IA 2 ne peut pas être utilisée sans l'IA 1");
            return;
        }
        IA2Activee = !IA2Activee;
        if (estActiveIA2()) {
            IA_2 = new IAFaible(this, ID_JOUEUR_2, "IA 2");
        }
        if (estActiveIA2()) {
            joueur1 = IA_1;
            joueur2 = IA_2;
        } else {
            if (estActiveIA1()) {
                joueur1 = JOUEUR_1;
                joueur2 = IA_1;
            } else {
                joueur1 = JOUEUR_1;
                joueur2 = JOUEUR_2;
            }
        }
        joueurCourant = joueur1;
        idJoueurCourant = ID_JOUEUR_1;
        initJoueursCartes();
    }

    /**
     * Vérifie si l'IA 1 est activée
     *
     * @return vrai si l'IA 1 est activée
     */
    public boolean estActiveIA1() {
        return IA1Activee;
    }

    /**
     * Vérifie si l'IA 2 est activée
     *
     * @return vrai si l'IA 2 est activée
     */
    public boolean estActiveIA2() {
        return IA2Activee;
    }

    /**
     * Définie le niveau de l'IA 1, si et seulement si l'IA 1 est active
     *
     * @param niveau FAIBLE | MOYEN | FORT
     */
    public void setNiveauIA1(NIVEAU_IA niveau) {
        if (!estActiveIA1()) {
            logger.info("L'IA 1 n'est pas active, impossible de définir son niveau");
            return;
        }
        int id = ID_JOUEUR_1;
        String nom = "IA 1";
        if (! estActiveIA2()) {
            id = ID_JOUEUR_2;
            nom = "IA";
        }
        switch (niveau) {
            case FAIBLE:
                IA_1 = new IAFaible(this, id, nom);
                break;
            case MOYEN:
                IA_1 = new IAMoyen(this, id, nom);
                break;
            case FORT:
                IA_1 = new IAFort(this, id, nom);
                break;
        }
        if (id == ID_JOUEUR_1) {
            joueur1 = IA_1;
        } else {
            joueur2 = IA_1;
        }
        initJoueursCartes();
    }

    /**
     * Définie le niveau de l'IA 2, si et seulement si l'IA 2 est active
     *
     * @param niveau FAIBLE | MOYEN | FORT
     */
    public void setNiveauIA2(NIVEAU_IA niveau) {
        if (!estActiveIA2()) {
            logger.info("L'IA 2 n'est pas active, impossible de définir son niveau");
            return;
        }
        switch (niveau) {
            case FAIBLE:
                IA_2 = new IAFaible(this, ID_JOUEUR_2, "IA 2");
                break;
            case MOYEN:
                IA_2 = new IAMoyen(this, ID_JOUEUR_2, "IA 2");
                break;
            case FORT:
                IA_2 = new IAFort(this, ID_JOUEUR_2, "IA 2");
                break;

        }
        joueur2 = IA_2;
        initJoueursCartes();
    }

    public void nouvellePartie() {
    }

    public long getTempsDeJeu() {
        return tempsJeu;
    }

    public void setTempsDeJeu(long secondes) {
        this.tempsJeu = secondes;
    }

    public void setNomJoueur1(String nom) {
        this.joueur1.setNom(nom);
        metAJour();
    }

    public void setNomJoueur2(String nom) {
        this.joueur2.setNom(nom);
        metAJour();
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

    public Coup getDernierCoupJoue() {
        return this.dernierCoupJoue;
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

    public boolean estPartieFinie() {
        return partieFinie;
    }

    public List<Coup> getCoupsPossibles(Carte carteSelectionee, Point positionPion) throws IllegalStateException {
        return Utils.getCoupsPossibles(this, carteSelectionee, positionPion);
    }

    private int changerJoueur() {
        int previous = idJoueurCourant;
        if (previous == ID_JOUEUR_1) {
            joueurCourant = joueur2;
        } else {
            joueurCourant = joueur1;
        }
        idJoueurCourant = (idJoueurCourant % 2) + 1;
        return previous;
    }

    private boolean deplacerPion(Point depart, Point arrivee) {
        try {
            boolean aManger = false;
            // Récupère l'éventuel pion présent sur la case d'arrivée
            Pion cible = getCase(arrivee.x, arrivee.y);
            // Si c'est un pion adverse, il sera écrasé par le setCase suivant
            if (cible != null && cible.getIDProprietaire() != idJoueurCourant) {
                aManger = true;
            }
            // On déplace le pion
            Pion p = getCase(depart.x, depart.y);
            setCase(depart.x, depart.y, null);
            if (! estCaseVide(arrivee.x, arrivee.y) && getRolePionAt(arrivee.x, arrivee.y) == PION_MAITRE) {
                logger.info("Le maitre adverse vient d'etre capturé");
                maitreMort = true;
            }
            setCase(arrivee.x, arrivee.y, p);
            p.setNewPosition(arrivee);
            // Mise à jour des listes de pions
            majPions();
            return aManger;
        } catch (CaseVideException ignored) {
            return false;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void majPionsJoueur1() {
        pionsJoueur1.clear();
        for (int i = 0; i < LIGNES; i++) {
            for (int j = 0; j < COLONNES; j++) {
                Pion p = grille[i][j];
                if (! estCaseVide(i, j) && p.getIDProprietaire() == ID_JOUEUR_1) {
                    pionsJoueur1.add(p);
                }
            }
        }
    }

    private void majPionsJoueur2() {
        pionsJoueur2.clear();
        for (int i = 0; i < LIGNES; i++) {
            for (int j = 0; j < COLONNES; j++) {
                Pion p = grille[i][j];
                if (! estCaseVide(i, j) && p.getIDProprietaire() == ID_JOUEUR_2) {
                    pionsJoueur2.add(p);
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
        carteSelectionne.setProprietaire(0);
        setCarteSupplementaire(carteSelectionne);
        joueur.addCard(nouvelleCarteJoueurCourant);
        nouvelleCarteJoueurCourant.setProprietaire(joueur.getId());


    }

    public Carte getCarteSelectionnee()
    {
        switch (numCarteSelectionee) {
            case 0:
                switch (idJoueurCourant) {
                    case ID_JOUEUR_1:
                        return joueur1.getCartesEnMain().get(numCarteSelectionee);
                    case ID_JOUEUR_2:
                        return joueur2.getCartesEnMain().get(numCarteSelectionee);
                }
            case 1:
                switch (idJoueurCourant) {
                    case ID_JOUEUR_1:
                        return joueur1.getCartesEnMain().get(numCarteSelectionee);
                    case ID_JOUEUR_2:
                        return joueur2.getCartesEnMain().get(numCarteSelectionee);
                }

        }
        return null;

    }

    public void setCarteSelectionnee(int c) {
        if (c > 1 || c < 0) {
            throw new IllegalStateException("La carte à choisir est 0 ou 1 pas " + c);
        }
        logger.info("Carte " + c + " sélectionnée (" + getCartesJoueurCourant().get(c).getNom() +")");
        this.numCarteSelectionee = c;

        metAJour();
    }

    public boolean setPionSelectionne(Point positionPion) {
        try {
            if (estCaseVide(positionPion.x, positionPion.y)) {
                logger.info("La case sélectionnée n'est pas un pion");
                return false;
            }
            if (getProprietairePionAt(positionPion.x, positionPion.y) != getIdJoueurCourant()) {
                logger.info("La pion sélectionné n'appartient pas au joueur courant");
                return false;
            }
            logger.info("Pion à la position (" + positionPion.x + "," + positionPion.y + ") séléctionné");
            this.pionSelectionne = getCase(positionPion.x, positionPion.y);
            metAJour();
            return true;
        } catch (CaseVideException ignored) {

        } catch (Exception e) {
            throw new RuntimeException();
        }
        return false;
    }

    /**
     * Sélectionne un pion à déplacer via ses coordonnées.
     * @param x Abscisse du pion.
     * @param y Ordonnée du pion.
     */
    public boolean setPionSelectionne(int x, int y) {
        try {
            if (estCaseVide(x, y)) {
                logger.info("La case sélectionnée n'est pas un pion");
                return false;
            }
            if (getProprietairePionAt(x, y) != getIdJoueurCourant()) {
                logger.info("La pion sélectionné n'appartient pas au joueur courant");
                return false;
            }
            logger.info("Pion à la position (" + x + "," + y + ") sélectionné");
            this.pionSelectionne = getCase(x, y);
            metAJour();
            return true;
        } catch (CaseVideException ignored) {

        } catch (Exception e) {
            throw new RuntimeException();
        }
        return false;
    }

    private void resetPionSelectionne() {
        this.pionSelectionne = null;
        metAJour();
    }

    public boolean verifierVictoire() {
        //si tous les pions adversaires sont morts
        if(getPionsJoueur1().isEmpty() && getIdJoueurCourant() == ID_JOUEUR_2){
            return true;
        }

        if(getPionsJoueur2().isEmpty() && getIdJoueurCourant() == ID_JOUEUR_1){
            return true;
        }

        //si le pion maitre de l'adversaire est mort
        if(maitreMort){
            return true;
        }

        //Le maitre occupe le temple ennemi
        Pion p1 = getCase(TEMPLE_JOUEUR_1.x, TEMPLE_JOUEUR_1.y);
        if(p1 != null && p1.getRole() == PION_MAITRE && p1.getIDProprietaire() == ID_JOUEUR_2){
            //Le maitre du joueur 2 occupe le temple du joueur 1
            return true;
        }

        Pion p2 = getCase(TEMPLE_JOUEUR_2.x, TEMPLE_JOUEUR_2.y);
        if(p2 != null && p2.getRole() == PION_MAITRE && p2.getIDProprietaire() == ID_JOUEUR_1){
            //Le maitre du joueur 1 occupe le temple du joueur 2
            return true;
        }

        return false;
    }


    public void selectionneCase(Point p) {
        try {
            switch (etatGrille) {
                case DEFAUT:
                    if (estCaseVide(p.x, p.y)) {
                        return;
                    }
                    if (setPionSelectionne(p)) {
                        etatGrille = PION_SELECTIONNE;
                    }
                    break;
                case PION_SELECTIONNE:
                    if (!estCaseVide(p.x, p.y) && getProprietairePionAt(p.x, p.y) == getIdJoueurCourant()) {
                        setPionSelectionne(p);
                        return;
                    }
                    if (!jouerCoup(new Coup(getPionSelectionne().getPosition(), p, getCarteSupplementaire()))) {
                        return;
                    }
                    etatGrille = DEFAUT;
                    break;

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean jouerCoup(Coup c) {
        try {
            if (pionSelectionne == null) {
                throw new IllegalStateException("Il faut d'abord choisir un pion avant de jouer un Coup");
            }
            if (estPartieFinie()) {
                logger.info("La partie est finie, impossible de jouer un coup");
                return false;
            }
            if (c == null) {
                logger.info("Aucun coup fourni, au tour du joueur suivant");
                changerJoueur();
                return false;
            }

            if (!partieACommence) {
                partieACommence = true;
            }

            Point depart = c.getDepart();
            Point arrivee = c.getArrivee();
            List<Coup> coupsPossibles = getCoupsPossibles(getCartesJoueurCourant().get(this.numCarteSelectionee), getPionSelectionne().getPosition());

            if (! estDansListeDeCoupsPossibles(coupsPossibles, c)) {
                logger.info("Le coup fourni est invalide, il ne sera pas joué");
                return false;
            }

            // met à jour la grille
            boolean mangerPion = deplacerPion(depart, arrivee);
            c.setAMangerPion(mangerPion);
            historique.add(c);
            this.dernierCoupJoue = c;



            resetPionSelectionne();

            if(verifierVictoire()) {
                partieFinie = true;
                logger.info("Le joueur" + getIdJoueurCourant() + " a gagné !!!!!!!!!");
                metAJour();
                etatJeu = FIN;
                return true;
            }


            echangerCartes(getJoueurCourant(), getCartesJoueurCourant().get(getNumCarteSelectionnee()));
            int previous = changerJoueur();
            //carteSelectionee = null;

            // met à jour l'interface
            metAJour();
            // met à jour l'automate
            if (previous == ID_JOUEUR_1 && joueur1.getTypeJoueur() == JOUEUR_HUMAIN) {
                etatJeu = J1_A_JOUE;
            } else if (previous == ID_JOUEUR_2 && joueur2.getTypeJoueur() == JOUEUR_HUMAIN) {
                etatJeu = J2_A_JOUE;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    /**
     * Démarre une nouvelle partie à partir d'une sélection donnée.
     * @param partieSelectionee Identifiant ou nom de la partie sélectionnée.
     */
    public void setNouvellePartie(String partieSelectionee) {
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
     * Renvoie la représentation textuelle du jeu
     * @return chaine de caractères représentant le jeu
     */
    @Override
    public String toString() {
        try {
            StringBuilder S = new StringBuilder();
            for (int i = 0; i < LIGNES; i++) {
                for (int j = 0; j < COLONNES; j++) {
                    if (estCaseVide(i, j)) {
                        S.append(" ");
                        continue;
                    }
                    if (getRolePionAt(i, j) == PION_MAITRE) {
                        S.append("M");
                    } else {
                        S.append("E");
                    }
                    S.append(getProprietairePionAt(i, j));
                }
                S.append("\n");
            }

            List<Carte> lc1 = getCartesJoueur1();
            List<Carte> lc2 = getCartesJoueur2();
            Carte c;
            S.append("J1 : ");
            if (lc1 != null) {
                c = lc1.get(0);
                if (c != null) {
                    S.append(c.getNom());
                    S.append(", ");
                }
                c = lc1.get(1);
                if (c != null) {
                    S.append(c.getNom());
                }
            }
            S.append("\n");
            S.append("J2 : ");
            if (lc2 != null) {
                c = lc2.get(0);
                if (c != null) {
                    S.append(c.getNom());
                    S.append(", ");
                }
                c = lc2.get(1);
                if (c != null) {
                    S.append(c.getNom());
                }
            }
            S.append("\n");
            S.append("Carte Supp : ");
            c = getCarteSupplementaire();
            if (c != null) {
                S.append(c.getNom());
            }
            S.append("\n");

            return S.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            int delai = 1500;
            boucle:
            while (! estPartieFinie()) {
                Coup c;
                synchronized (this) {
                    switch (etatJeu) {
                        case DEBUT:
                            break;
                        case DEBUT_IA:
                        case IA2_A_JOUE:
                            c = IA_1.calculerCoup();
                            Thread.sleep(delai);
                            setCarteSelectionnee(IA_1.getCarteChoisie());
                            setPionSelectionne(IA_1.getPionChoisi().getPosition());
                            jouerCoup(c);
                            etatJeu = IA1_A_JOUE;
                            break;
                        case J1_A_JOUE:
                            if (estActiveIA1()) {
                                c = IA_1.calculerCoup();
                                Thread.sleep(delai);
                                setCarteSelectionnee(IA_1.getCarteChoisie());
                                setPionSelectionne(IA_1.getPionChoisi().getPosition());
                                jouerCoup(c);
                                etatJeu = IA1_A_JOUE;
                            }
                            break;
                        case J2_A_JOUE:
                            break;
                        case IA1_A_JOUE:
                            if (estActiveIA2()) {
                                c = IA_2.calculerCoup();
                                Thread.sleep(delai);
                                setCarteSelectionnee(IA_2.getCarteChoisie());
                                setPionSelectionne(IA_2.getPionChoisi().getPosition());
                                jouerCoup(c);
                                etatJeu = IA2_A_JOUE;
                            }
                            break;
                        case FIN:
                            break boucle;

                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
