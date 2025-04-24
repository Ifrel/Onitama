package Modele;

import static Global.Config.TYPECARTE.*;
import static Global.Config.*;
import Patterns.Observable;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Jeu extends Observable {
    private int lignes, colonnes;
    private static Historique<Coup> historique; // historique des coups
    private int joueurCourant;
    private int gagnant; // joueurA | joueurB | joueurIA
    private boolean fini;
    private boolean avecIA; // Le joueur B est remplacé par une IA
    private boolean aCommence; // Le jeu a commencé, impossible de lancer l'IA
    private String nameA;
    private String nameB;
    private int nbMangeesA; // nombre de cases mangées par le joueur A
    private int nbMangeesB; // nombre de cases mangées par le joueur B
    private int nbTotalCases; // nombres de cases constituiant la gaufre
    private IA joueurIA;


    // -- GRILLE -- //
    private Pion [][] grille;

    // --- CARTES -- //
    private List<Carte> toutesLesCartes;

    // ------------------------ INIT ------------------------

    public Jeu() {
        _Jeu(LIGNES, COLONNES);
    }

    /// réalise l'initialisation des champs, existe pour être appelée à plusieurs endroits, en dehors du constructeur
    private void _Jeu(int l, int c) {
        // -- Lignes et colonnes
        lignes = l;
        colonnes = c;

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
        initCartes();
        if (MODEDEBUG) {
            afficherCartes();
            afficherGrille();
        }
    }

    /* initGrille: Créer la grille et place les pions. */
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

    /* initCartes: Initialize toutes les cartes disponibles dans le jeu */
    private void initCartes()
    {
        toutesLesCartes = new ArrayList<>();
        toutesLesCartes.add(new Carte(TIGRE));
        toutesLesCartes.add(new Carte(DRAGON));
        toutesLesCartes.add(new Carte(GRENOUILLE));
        toutesLesCartes.add(new Carte(LAPIN));
        toutesLesCartes.add(new Carte(CRABE));
        toutesLesCartes.add(new Carte(ELEPHANT));
        toutesLesCartes.add(new Carte(OIE));
        toutesLesCartes.add(new Carte(COQ));
        toutesLesCartes.add(new Carte(SINGE));
        toutesLesCartes.add(new Carte(MANTE));
        toutesLesCartes.add(new Carte(CHEVAL));
        toutesLesCartes.add(new Carte(BOEUF));
        toutesLesCartes.add(new Carte(GRUE));
        toutesLesCartes.add(new Carte(SANGLIER));
        toutesLesCartes.add(new Carte(ANGUILLE));
        toutesLesCartes.add(new Carte(COBRA));
    }

    private void afficherCartes()
    {
        for (Carte c: toutesLesCartes)
        {
            System.err.println(c.getName());
            c.getMoves(new Point(2, 2));
        }
    }

    private void afficherGrille() {
        for (int i = 0; i < lignes(); i++) {
            for (int j = 0; j < colonnes(); j++) {
                System.out.print(grille[i][j] + " ");
            }
            System.out.println(); // go to next line after each row
        }
    }

    // -- GETTER ET SETTER
    /// renvoie le nombre de lignes constituant la gaufre
    public int lignes() {
        return lignes;
    }

    /// renvoie le nombre de colonnes constituant la gaufre
    public int colonnes() {
        return colonnes;
    }

    // --- Cases
    private void setCase()
    {
        // TODO:
        // Verifier que la case soit bien dans les
        // bornes disponibles et qu'elle soit vide.
    }

    // --- Pions
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
    /*
    // ------------------------ ACTIONS  ------------------------

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
