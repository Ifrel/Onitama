package Modele;

import static Global.Config.TYPECARTE.*;
import static Global.Config.*;
import static Global.Config.ROLEPION.*;

import Modele.IA.IA;
import Patterns.Observable;

import java.awt.*;
import java.time.Duration;
import java.util.*;
import java.util.List;


public class Jeu extends Observable {
    private Pion [][] grille;
    private Historique<Coup> historique;
    private Joueur joueur1, joueur2;
    private IA IA_1, IA_2;
    private int lignes, colonnes;
    private int idJoueurCourant;

    // --- CARTES -- //
    private List<Carte> toutesLesCartes; //Toutes les cartes confondues
    private List<Carte> cartesDuJeu; //5 cartes, les cartes qui circulent dans le jeu.

    // --- Carte d'échange -- //
    private Carte carteEchange; //La carte qui sera en échange

    // -- GRILLE -- //
    private ArrayList<Pion> pionJoueurUn = new ArrayList<>(); //Grille implicite: Liste de pions (chaque pion est associé à une position) du premier joueur
    private ArrayList<Pion> pionJoueurDeux = new ArrayList<>(); //idem pour le deuxième joueur




    public Jeu() {
        lignes = colonnes = 5;
        historique = new Historique<>();

        toutesLesCartes = initCartes();
        cartesDuJeu = initCartesJeu();

        initGrille();

        idJoueurCourant = 1;

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

    /**
     * Ajoute un certain type de pion à une certaine position sur la grille.
     * Assigne un propriétaire également à ce nouveau pion.
     */
    void ajouterPion(List<Point> _coordonnes, int _proprietaire, ROLEPION _role)
    {
        //Si on est le joueur 1
        if(_proprietaire == 1)
        {
            //Si il s'agit d'un pion étudiant
            if(_role == PION_ETUDIANT)
            {
                //Alors pour chaque point initiale:
                for (Point p: _coordonnes)
                {
                    //On associe le propriétaire et la position a un pion étudiant
                    Pion pionEtudiant1 = new Pion(_proprietaire,p, PION_ETUDIANT);
                    //On mémorise ce pion dans la liste des pions du premier joueur
                    pionJoueurUn.add(pionEtudiant1);
                }
            }
            else{
                //Idem, mais pour un pion maitre (on passe une seule coordonée lorsqu'il s'agit d'un pion maitre)
                Pion pionMaitre1 = new Pion(_proprietaire,  _coordonnes.get(0), PION_MAITRE);
                pionJoueurUn.add(pionMaitre1);
            }
        }
        //Sinon si on est le deuxième joueur
        else{
            //Meme principe, sauf que on stocke les pions dans la liste du second joueur
            if(_role == PION_ETUDIANT)
            {
                for (Point p: _coordonnes)
                {
                    Pion pionEtudiant2 = new Pion(_proprietaire, p, PION_ETUDIANT);
                    pionJoueurDeux.add(pionEtudiant2);
                }
            }
            else{
                Pion pionMaitre2 = new Pion(_proprietaire,_coordonnes.get(0), PION_MAITRE);
                pionJoueurDeux.add(pionMaitre2);
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
        //Pour le joueur 1 et 2, on ajoute les positions initiales des pions étudiants et maitres, selon le joueur bien sur
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

    private void initialiserJoueurs() {
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
    return;
}

public void refaireCoup() {
    if (! peutRefaireCoup()) {
        System.err.println("Impossible de refaire un Coup");
        return;
    }

    Coup c = historique.refaire();

    // faire des choses avec le coup
    return;
}

// ######### CHARGER / SAUVEGARDER ########

public void sauvegarderJeu(String fichier) {
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

public boolean estCaseVide(int i, int j) {
    return true;
}

public boolean estPionEtudiantJoueur1(int i, int j) {
    return false;
}

public boolean estPionEtudiantJoueur2(int i, int j) {
    return true;
}

public boolean estPionMaitreJoueur1(int i, int j) {
    return false;
}

public boolean estPionMaitreJoueur2(int i, int j) {
    return false;
}

public List<Pion> getPionsJoueur1() {
    return null;
}

public List<Pion> getPionsJoueur2() {
    return null;
}

public List<Carte> getCartesJoueur1() {
    return null;
}

public List<Carte> getCartesJoueur2() {
    return null;
}

public Carte getCarteSupplementaire() {
    return null;
}

public Carte getCarteJouee() {
    return null;
}

public boolean estPionDuJoueurCourant(int i, int j) {
    return false;
}

public boolean estCoupConforme(int i, int j) {
    return false;
}

public int getProprietairePionAt(int i, int j) {
    return 1;
}

public ROLEPION getRolePionAt(int i, int j) {
    return PION_ETUDIANT;
}

// ######## PARTIE ########

public void nouvellePartie() {
    return;
}

public long getTempsDeJeu() {
    return 1234;
}

public void setTempsDeJeu(long temp) {
        return;
}

public void setNomJoueur1() {
    return;
}

public void setNomJoueur2() {
    return;
}

public String getNomJoueur1() {
    return "Abcd";
}

public String getNomJoueur2() {
    return "bonjour";
}

public String getNomJoueurCourant() {
    return "bonjour";
}

public Joueur getJoueurCourant() {
    return new Joueur(1, "nom Joueur 1");
}

public int getNumeroRound() {
    return 1;
}

public CasePlateau getCasePlateau(int row, int col) {
    //TODO
    return new CasePlateau(this, new Point(row, col));
}

public Carte getCartesSurLeTerrain(int i) {
    return null;
}

public Joueur getJoueur(int id) {
    return null;
}

public boolean estDeplacementConforme(int i, int j) {
    return false;
}

public boolean estPartieFinie() {
    return false;
}

public List<Coup> getCoupsPossibles(Point positionPion, Carte carteJoue) {
    return null;
}

public void jouerCoup(Coup c) {

}
}
