package Modele;

import static Global.Config.TYPECARTE.*;
import static Global.Config.*;
import Patterns.Observable;

import java.awt.*;
import java.util.*;
import java.util.List;


public class Jeu extends Observable {
    private int lignes, colonnes, casesTotales;
    private static Historique<Coup> historique; // historique des coups

    // -- Boucle de jeu -- //
    private Pion pionSelectionne;   // Pion en cours de selection
    private etatCoup etatCoupEnCours;       // Etat du coup en cours
    public enum etatCoup
    {
        aucun,
        selectionné,
        joué,
        annulé,
        terminé
    }

    // -- JOUEURS -- //
    private int joueurCourant;
    private HashMap<Integer, Joueur> joueursEnPartie;

    // -- GRILLE -- //
    private Pion [][] grille;

    // --- CARTES -- //
    private ArrayList<Carte> toutesLesCartes;

    // ------------------------ INIT ------------------------

    public Jeu() {
        _Jeu(LIGNES, COLONNES);
    }

    int round;
    boolean peutRefaire, peutAnnuler;



    /**
     * Crée l'instance de jeu
     *
     */
    private void _Jeu(int l, int c) {
        // -- Lignes et colonnes
        lignes = l;
        colonnes = c;
        casesTotales = l*c;
        round = 1;
        peutRefaire = peutAnnuler = false;

        // -- Coups et actions
        etatCoupEnCours = etatCoup.aucun;

        // -- Créer notre grille de jeu
        grille = new Pion[lignes][colonnes];
        initGrille();
        /*
        lignes = l;
        colonnes = c;
        joueurCourant = Config.joueurA;
        gagnant = -1;
        fini = false;
        historique = new Historique<>();
        avecIA = false;
        aCommence = false;
        // noms par défaut
        nameA = "Joueur A";
        nameB = "Joueur B";
        nbMangeesA = 0;
        nbMangeesB = 0;
        nbTotalCases = l * c;
        joueurIA = new IA(this);

        jeu = new boolean [l][c];
        initGrille();
        */

        // -- Cartes
        toutesLesCartes = new ArrayList<>();
        toutesLesCartes = initCartes();

        // -- Joueurs
        joueurCourant = 2;
        Joueur joueur1 = new Joueur(1,"Joueur 1");
        Joueur joueur2 = new Joueur(2,"Joueur 2");
        joueur1.addCard(tirerCartesAuHasard(NOMBRE_CARTES_MAIN));
        joueur2.addCard(tirerCartesAuHasard(NOMBRE_CARTES_MAIN));
        joueursEnPartie = new HashMap<Integer, Joueur>() {{
            put(0, joueur1);
            put(2, joueur2);
        }};
        //- Donne une référence du jeu à chaque joueur
        //joueursEnPartie.get(joueurCourant).setJeu(this);
        //joueursEnPartie.get(!joueurCourant).setJeu(this);

        // -- Récupère toutes les cartes
        toutesLesCartes = initCartes();

        if (MODEDEBUG) {
            afficherCartes();
            afficherGrille();
            afficherNomJoueur(true);
            afficherMainJoueur(joueursEnPartie.get(joueurCourant));
            afficherMainJoueur(joueursEnPartie.get(joueurCourant));
        }
    }

    /**
     * Initialize la grille avec l'ensemble des cases vides et des pions maitres et étudiants.
     *
     */
    private void initGrille()
    {
        // Mise à zéro de la grille.
        for (int i = 0; i < lignes(); i++) {
            for (int j = 0; j < colonnes(); j++) {
                grille[i][j] = null;
            }
        }
        // Ajouter pion étudiant
        ajouterPion(new ArrayList<Point>()
        {{
            add(new Point(0,0));
            add(new Point(0,1));
            add(new Point(0,3));
            add(new Point(0,4));
        }}, false, ROLEPION.Etudiant);
        ajouterPion(new ArrayList<Point>()
        {{
            add(new Point(4,0));
            add(new Point(4,1));
            add(new Point(4,3));
            add(new Point(4,4));
        }}, true, ROLEPION.Etudiant);

        // Ajouter pion maitre
        ajouterPion(new ArrayList<Point>(){{add(new Point(0, 2));}}, false, ROLEPION.Maitre);
        ajouterPion(new ArrayList<Point>(){{add(new Point(4, 2));}}, true, ROLEPION.Maitre);
    }

    /**
     * renvoie une liste qui contient toutes les cartes du jeu
     * @return Une ArrayList de Cartes
     */
    private ArrayList<Carte> initCartes()
    {
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

    /**
     * Fonction de DEBUG: Affiche le nom d'un des deux joueurs.
     *
     */
    private void afficherNomJoueur(boolean afficheJoueurCourant)
    {
        if (afficheJoueurCourant)
        {
            System.err.println("Nom joueur courant: " + joueursEnPartie.get(joueurCourant).getNom());
        }
        else
        {
            for (int cle : joueursEnPartie.keySet()) System.err.println("Nom joueur : " + joueursEnPartie.get(cle).getNom());
        }
    }

    /**
     * Fonction de DEBUG: Affiche toutes les cartes disponibles
     *
     */
    private void afficherCartes()
    {
        for (Carte c: toutesLesCartes)
        {
            System.err.println(c.getNom());
            c.getMoves(new Point(2, 2));
        }
    }

    /**
     * Fonction de DEBUG: Affiche la main d'un joueur
     *
     */
    private void afficherMainJoueur(Joueur joueur)
    {
        System.err.println("Cartes en main pour le joueur : " + joueur.getNom());
        List<Carte> mainJoueur = joueur.getCartesEnMain();
        for (Carte c: mainJoueur) System.err.println(c.getNom() + " ");
    }

    /**
     * Fonction de DEBUG: Affiche les entités présentes sur la grille.
     *
     */
    private void afficherGrille() {
        for (int i = 0; i < lignes(); i++) {
            for (int j = 0; j < colonnes(); j++) {
                System.out.print(grille[i][j] + " ");
            }
            System.out.println();
        }
    }



    public Pion getPionAt(int row, int col){
        return grille[row][col];
    }


    public  Joueur getJoueurCourant(){
        return joueursEnPartie.get(joueurCourant);
    }


    public int getNumeroRound(){
        return round;
    }

    public void setNumeroRound(int round){
        this.round = round;
    }

    public boolean peutAnnuler(){
        return peutAnnuler;
    }

    public boolean peutRefaire(){
        return peutRefaire;
    }



    /**
     * renvoie les lignes de la grille
     * @return Un integer
     */
    public int lignes() {
        return lignes;
    }

    /**
     * renvoie les colonnes de la grille
     * @return Un integer
     */
    public int colonnes() {
        return colonnes;
    }

    /**
     * renvoie le nombre de cases totales
     * @return Un integer
     */
    public int casesTotales()
    {
        return casesTotales;
    }

    /**
     * Pas sur de l'utilité de cette fonction
     * // TODO
     */
    private void setCase()
    {
        // TODO:
        // Verifier que la case soit bien dans les
        // bornes disponibles et qu'elle soit vide.
    }

    /**
     * Ajoute un certain type de pion à une certaine position sur la grille.
     * Assigne un propriétaire également à ce nouveau pion.
     */
    void ajouterPion(List<Point> _coordonnes, boolean _proprietaire, ROLEPION _role)
    {
        for (Point p: _coordonnes)
        {
            if (_role == ROLEPION.Etudiant){
                grille[p.x][p.y] = new PionEtudiant(_proprietaire);
            }else{
                grille[p.x][p.y] = new PionMaitre(_proprietaire);
            }
        }
    }

    // ------------------------ ACTIONS  ------------------------

    /**
     * renvoie si il est possible de selectionne le pion ou non.
     * Si oui, alors le selectionne.
     * @return Un boolean
     */
    private boolean selectionnePion(Point coordonnees)
    {
        Pion pionClique = grille[coordonnees.x][coordonnees.y];
        if (pionClique==null) return false;
        if (pionClique.getProprietaire() != joueurCourant)
            return false;
        pionSelectionne = pionClique;
        return true;
    }

    /**
     * Ensemble des opérations théoriques pour modifier le tour d'un
     * joueur. Mdrr ça fait rien pour l'instant.
     */
    private void changerLeJoueurEnCours()
    {
        // Changer le joueur en cours
        // Changer les cartes.
    }

    /**
     * Tire un certain nombre de cartes au hasard.
     * @return Une ArrayList de CarteUI
     */
    private ArrayList<Carte> tirerCartesAuHasard(int nombre)
    {
        ArrayList<Carte> CartesTireesAuHasard = new ArrayList<Carte>();
        Random r = new Random();
        for (int i = 0; i < nombre; i++) {
            Carte CarteTiree = toutesLesCartes.get(r.nextInt(toutesLesCartes.size()));
            CartesTireesAuHasard.add(CarteTiree);
            toutesLesCartes.remove(CarteTiree);
        }
        return CartesTireesAuHasard;
    }

    // PROTOTYPES A AJOUTER
    /**
     * Débute une nouvelle partie en partant de 0.
     *
     */
    void nouvellePartie(){}

    /**
     * Sauvegarde dans un fichier l'état actuel de la
     * partie en cours.
     */
    void sauvegarderJeu(String fichier){}

    /**
     * Rétabli l'état actuel de la partie en cours
     * depuis un fichier de sauvegarde.
     */
    void chargerJeu(String fichier){}

    /**
     * Renvoie la liste des noms de tous les fichiers de sauvegarde
     * @return Une liste de String
     */
    List<String> listerSauvegardes(){ return null; }

    /**
     * Joue un coup en fonction de l'état du pion et du
     * tour du joueur/IA
     */
    private void jouerCoup(Point p)
    {
        if (PointInvalide(p)) return;
        switch (etatCoupEnCours)
        {
            case aucun:
                // Si le pion ne nous appartient pas, on ne joue pas le coup.
                if (!selectionnePion(p)) return;
                etatCoupEnCours = etatCoup.selectionné;
                metAJour();
                break;
            case selectionné:

                break;

            case joué:
                break;
            case annulé:
                pionSelectionne = null;
                etatCoupEnCours = etatCoup.aucun;
                break;
            case terminé:
                pionSelectionne = null;
                changerLeJoueurEnCours();
                break;
        }
    }

    /**
     * Détermine si le point peut appartenir à
     * @return Un boolean
     */
    boolean PointInvalide(Point p)
    {
        if (p.x > lignes || p.x < colonnes) return false;
        if (p.y > lignes || p.y < colonnes) return false;
        return true;
    }
    /**
     * Annule un coup générique.
     */
    void annulerCoup(){}

    /**
     * Rétablie un coup générique.
     */
    void refaireCoup(){}

    /**
     * Détermine si un coup peut être annulé.
     * @return Un boolean
     */
    boolean peutAnnulerCoup(){ return false; }

    /**
     * Détermine si un coup peut être refait.
     * @return Un boolean
     */
    boolean peutRefaireCoup(){ return false; }

/*
    /// jouer un coup IA
    public void jouerIA(String niveau) {
        joueurIA.jouer(niveau);
    }

    /// recommencer le jeu, avec les mêmes dimensions
    public void recommencer() {
        // recommencer le jeu en gardant les mêmes proportions
        _Jeu(lignes(), colonnes());
        metAJour();
    }

    /// recommencer le jeu, avec 'l' lignes et 'c' colonnes
    public void recommencer(int l, int c) {
        // changer la taille du Jeu depuis l'interface
        _Jeu(l, c);
        metAJour();
    }

    /// manger la case aux coordonnées (i, j)
    /// retourne un booléen qui indique si la méthode a eu un effet sur la gaufre
    public boolean jouer(int i, int j) {
        try {

            if (!aCommence) {
                aCommence = true;
            }
            if (estVide(i, j)) {
                return false;
            }
            Coup c = new Coup(i, j);
            if (estPoison(i, j)) {
                manger(c);
                historique.add(c);
                gagnant = joueurCourant();
                fini = true;
                metAJour();
                return true;
            }
            manger(c);
            historique.add(c);
            metAJour();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// sauvegarde l'état courant du jeu dans le fichier 'fichier'
    /// renvoit false si la sauvgarde échoue
    public boolean sauvegarderJeu(String fichier) {

        if (!ecrireSauvegarde(this, fichier)) {
            return false;
        }
        return true;
    }

    /// charge l'état du jeu depuis le fichier 'fichier'
    /// renvoit false si la charge échoue
    public boolean chargerJeu(String fichier) {

        if (!lireSauvegarde(this, fichier)) {
            return false;
        }
        return true;
    }

    /// renvoit la liste des sauvegardes présentes dans le dossier par défaut
    public List<String> listeFichiersSauvegarde() {
        ArrayList<String> liste = new ArrayList<>();
        File saveDir = new File(Config.saveDir.toString());
        File[] files = saveDir.listFiles();

        // Print name of the all files present in that path
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                System.err.println(name);
                liste.add(name);
            }
        }
        return liste;
    }

    /// annule le dernier coup, nécessite d'avoir joué au moins un coup
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

        recouvrir(c);
        metAJour();
    }

    /// rejoue un coup annulé, nécessite d'avoir annulé au moins un coup
    public void refaireCoup() {
        if (! peutRefaireCoup()) {
            System.err.println("Impossible de refaire un Coup");
            return;
        }
        Coup c = historique.refaire();

        manger(c);
        metAJour();
    }

    ///  indique si on peut annuler le dernier coup joué
    public boolean peutAnnulerCoup() {
        return historique.peutAnnuler() && ! estPartieFinie();
    }

    /// indique si on peut refaire le dernier coup annulé
    public boolean peutRefaireCoup() {
        return historique.peutRefaire();
    }

    /// fonction probablement mal positionnée, on verra
    public void toggleIA() {
        // active ou désactive le mode IA
        avecIA = true;
        setNameJoueurB("IA");
    }

    /// mange toutes les cases constituant un rectangle depuis (i, j) vers le coin (lignes(), colonnes())
    private void manger(Coup c) {
        try {
            int i, j;
            i = c.i;
            j = c.j;

            boolean flip = false;

            checkEstDansJeu(i, j);
            for (int k = i; k < lignes(); k++) {
                for (int l = j; l < colonnes(); l++) {
                    if(vide(k, l)) {
                        flip = true;
                        int [] x = {k, l};
                        switch (joueurCourant()) {
                            case Config.joueurA:
                                nbMangeesA++;
                                break;
                            case Config.joueurB:
                            case Config.joueurIA:
                                nbMangeesB++;
                                break;
                        }
                        c.cases.add(x);
                    }
                }
            }
            if (flip) {
                flipJoueur();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// rempli à nouveau toutes les cases constituant un rectangle depuis (i, j) vers le coin (lignes(), colonnes())
    private void recouvrir(Coup c) {
        try {
            flipJoueur();
            // pour toutes les cases affectées par le coup 'c', les remplir
            for (int k = 0; k < c.cases.size(); k++) {
                int [] x = c.cases.get(k);
                switch (joueurCourant()) {
                    case Config.joueurA:
                        nbMangeesA--;
                        break;
                    case Config.joueurB:
                    case Config.joueurIA:
                        nbMangeesB--;
                        break;
                }
                rempli(x[0], x[1]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ------------------------ GESTION JEU ------------------------
    public boolean lireSauvegarde(Jeu jeu, String fichier) {
        try {
            Jeu newJeu;
            File f = new File(Config.saveDir.resolve(fichier).toString());
            Scanner sc = new Scanner(f);

            this.lignes = sc.nextInt();
            this.colonnes = sc.nextInt();

            this.joueurCourant = sc.nextInt();

            /// ignorer poison car statique dans cette version, ne varie pas
            sc.nextInt();
            sc.nextInt();




                sc.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean ecrireSauvegarde(Jeu jeu, String fichier) {
        try {
            // 6 8 // lignes colonnes
            // 1 // avec IA
            // 2 // joueur courant
            // 0 0 // coordonnées poison
            // 1 1 1 1 1 1 // état gauffre
            // 1 0 0 0 0 0
            // 1 0 0 0 0 0
            // 1 0 0 0 0 0
            // 1 0 0 0 0 0
            //
            // Passe
            // 3 // taille passe
            // x y 2 3 4 5 // coordonnées de base x y avec leurs effets
            //
            // Futur
            // 2 // taille futur
            // x y 2 3 4 5 // coordonnées de base x y avec leurs effets
            FileWriter fw = new FileWriter(Config.saveDir.resolve(fichier).toString());
            fw.write(jeu.lignes() + " " + jeu.colonnes() + "\n");
            char avecIA = (jeu.partieAvecIA() ? '1' : '0');
            fw.write(avecIA + "\n");
            fw.write(jeu.joueurCourant() + "\n");
            // position poison
            fw.write(Config.posPoison.x + " " + Config.posPoison.y + "\n");
            // grille
            for (int i = 0; i < jeu.lignes(); i++) {
                for (int j = 0; j < jeu.colonnes(); j++) {
                    if (jeu.estPleine(i, j)) {
                        fw.write("1 ");
                    } else if (jeu.estVide(i, j)) {
                        fw.write("0 ");
                    }
                }
                fw.write("\n");
            }

            fw.write("\n");
            fw.write("Passe\n");
            fw.write(historique.passeSize() + "\n");
            List<Coup> listeCoupsPasse = historique.dumpPasse();
            for (int i = 0; i < historique.passeSize(); i++) {
                Coup c = listeCoupsPasse.get(i);
                fw.write(c.i + " " + c.j + " ");
                ArrayList<int []> casesListe = c.cases;
                for (int j = 0; j < c.cases.size(); j++) {
                    int [] coords = casesListe.get(i);
                    fw.write(coords[0] + " " + coords[1] + " ");
                }
                fw.write("\n");
            }

            fw.write("\n");
            fw.write("Futur\n");
            List<Coup> listeCoupsFutur = historique.dumpFutur();
            for (int i = 0; i < historique.futurSize(); i++) {
                Coup c = listeCoupsFutur.get(i);
                fw.write(c.i + " " + c.j + " ");
                ArrayList<int []> casesListe = c.cases;
                for (int j = 0; j < c.cases.size(); j++) {
                    int [] coords = casesListe.get(i);
                    fw.write(coords[0] + " " + coords[1] + " ");
                }
                fw.write("\n");
            }


            fw.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    /// vérifie si les coordonnées (i, j) sont bien présentes dans la gaufre
    public void checkEstDansJeu(int i, int j) {
        if (i < 0 || i >= lignes() || j < 0 || j >= colonnes()) {
            throw new RuntimeException("Tentative d'accéder à 'jeu[" + i + "," + j + "]' dans un jeu de taille " + lignes() + "x" + colonnes());
        }
    }

    // à la fin d'un coup, c'est à l'autre joueur de jouer.
    /// renvoit le nouveau joueur courant si besoin
    public int flipJoueur() {
        int jc, nj;
        jc = joueurCourant();

        if (avecIA && jc == Config.joueurA) {
        nj = Config.joueurIA;
        } else if (avecIA && jc == Config.joueurIA){
        nj = Config.joueurA;
        } else if (jc == Config.joueurA) {
            nj = Config.joueurB;
        } else if (jc == Config.joueurB) {
            nj = Config.joueurA;
        } else {
            throw new RuntimeException("Situation impossible");
        }
        joueurCourant = nj;
        return nj;
    }

    ///  fixe le nom du joueur A
    public void setNameJoueurA(String nameA) {
        this.nameA = nameA;
    }

    ///  fixe le nom du joueur B
    public void setNameJoueurB(String nameB) {
        this.nameB = nameB;
    }

    /// renvoie la valeur de la case à la position (i, j)
    private boolean getCase(int i, int j) {
        try {
            checkEstDansJeu(i, j);
            return jeu[i][j];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// fixe la valeur de la case à la position (i, j) à 'value'
    private void setCase(int i, int j, boolean value) {
        try {
            checkEstDansJeu(i, j);
            jeu[i][j] = value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// rend la case (i, j) pleine si vide
    /// retourne un booléen qui indique si la méthode a eu un effet sur la gaufre
    private boolean rempli(int i, int j) {
        if (estPleine(i, j)) {
            return false;
        }
        setCase(i, j, true);
        return true;
    }

    /// rend la case (i, j) vide si pleine
    /// retourne un booléen qui indique si la méthode a eu un effet sur la gaufre
    private boolean vide(int i, int j) {
        if (estVide(i, j)) {
            return false;
        }
        setCase(i, j, false);
        return true;
    }

    // ------------------------ STATUT JEU ------------------------
    ///  indique si la partie est jouée contre une IA
    public boolean partieAvecIA() {
        return avecIA;
    }

    /// renvoie le nombre de cases constituant la gaufre
    public int getNbTotalCases() {
        return nbTotalCases;
    }

    /// renvoie le nombre case mangée par le joueur A
    public int nbCasesMangeesJoueurA() {
        return nbMangeesA;
    }

    /// renvoie le nombre case mangée par le joueur B
    public int nbCasesMangeesJoueurB() {
        return nbMangeesB;
    }


    /// indique si la partie est finie
    public boolean estPartieFinie() {
        return fini;
    }

    /// vérifie si la case à la position (i, j) est vide
    public boolean estVide(int i, int j) {
        return !getCase(i, j);
    }

    /// vérifie si la case à la position (i, j) est pleine
    public boolean estPleine (int i, int j) {
        return getCase(i, j);
    }

    /// vérifie si la case à la position (i, j) contient du poison
    public boolean estPoison (int i, int j) {
        return (i == Config.posPoison.x && j == Config.posPoison.y) && ! estVide(i, j);
    }

    /// renvoie le joueur courant : joueurA | joueurB | joueurIA
    public int joueurCourant() {
        return joueurCourant;
    }

    ///  renvoie le nom du joueur courant : | Joueur A | Joueur B | IA
    public String nomJoueurCourant() {
        switch (joueurCourant()) {
            case Config.joueurA:
                return getNameJoueurA();
            case Config.joueurB:
                return getNameJoueurB();
            case Config.joueurIA:
                return "IA";
            default:
                return "";
        }
    }

    /// renvoie le joueur gagnant : joueurA | joueurB | joueurIA
    public int getGagnant() {
        return gagnant;
    }

    /// renvoie le nombre de lignes constituant la gaufre
    public int lignes() {
        return lignes;
    }

    /// renvoie le nombre de colonnes constituant la gaufre
    public int colonnes() {
        return colonnes;
    }

    ///  renvoie le nom du joueur A
    public String getNameJoueurA() {
        return this.nameA;
    }

    ///  renvoie le nom du joueur B
    public String getNameJoueurB() {
        return this.nameB;
    }

//    ///  renvoie sous forme textuelle le contenu de l'historique
//    /// UTILISER SEULEMENT POUR DEBUGGER ET TESTER
//    public String historyContent() {
//        return historique.toString();
//    }

    @Override
    ///  renvoie la représentation textuelle de la gauffre
    ///  P : poison, G : morceau de gaufre
    public String toString () {
        StringBuilder S = new StringBuilder();
        for (int i = 0; i < lignes(); i++) {
            for (int j = 0; j < colonnes(); j++) {
                if (estPoison(i, j)) {
                    S.append("P");
                } else if (estPleine(i, j)) {
                    S.append("G");
                } else if (estVide(i, j)) {
                    S.append(" ");
                } else {
                    S.append("x");
                }
            }
            S.append("\n");
        }
        for (int i = 0; i < colonnes() + 2; i++) {
            S.append("-");
        }
        S.append("\n");
        S.append(historique.toString());
        S.append("\n");
        return S.toString();
    }
    */
}
