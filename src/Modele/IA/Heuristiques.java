package Modele.IA;

import Modele.Carte;
import Modele.Coup;
import Modele.Jeu;
import Modele.Pion;

import java.awt.*;
import java.util.Hashtable;
import java.util.List;

import static Global.Config.*;
import static Global.Config.ROLEPION.PION_MAITRE;
import static Global.Config.TYPECARTE.*;

/**
 * Calcule la valeur d'une configuration du Jeu
 */
// TODO CONSIDERER 2 CONFIGS DIFFERENTES OU UN JOUEUR POSSEDE LES CARTES A ET B, ET B ET A IDENTIQUES POUR DIVISER PAR 2 LE NOMBRE POTENTIEL DE COUPS A STOCKER / CONSIDERER
public class Heuristiques {

    private static final Hashtable<TYPECARTE, Integer> VALEURS_CARTES = new Hashtable<>() {{
        /*
         *   +1 pour chaque déplacement possible depuis l'origine
         *   +n où n est la distance qu'on peut atteindre au maximum (en terme d'anneaux) depuis l'origine
         *      ex : si la carte permet de se rendre dans une des 8 cases voisines, on ajoute 0, si on passe à l'anneau supérieur +1, etc
         *   +1 par quart atteignable (on découpe le terrain en 4 zones / quarts : Carré supérieur gauche, supérieur droit, etc ; qui se rejoignent tous à l'origine)
         *      si on tombe sur les lignes du milieu (les deux lignes qui se croisent à l'origine), on considère que c'est comme si on atteignait seulement 1 quart
         */
        put(TIGRE, 5); // 2 + 1 + 2
        put(DRAGON, 10); //  4 + 2 + 4
        put(GRENOUILLE, 6); // 3 + 1 + 2
        put(LAPIN, 6); // 3 + 1 + 2
        put(CRABE, 8); // 3 + 2 + 3
        put(ELEPHANT, 6); // 4 + 0 + 2
        put(OIE, 6); // 4 + 0 + 2
        put(COQ, 6); // 4 + 0 + 2
        put(SINGE, 8); // 4 + 0 + 4
        put(MANTE, 6); // 3 + 0 + 3
        put(CHEVAL, 6); // 3 + 0 + 3
        put(BOEUF, 6); // 3 + 0 + 3
        put(GRUE, 6); // 3 + 0 + 3
        put(SANGLIER, 6); // 3 + 0 + 3
        put(ANGUILLE, 6); // 3 + 0 + 3
        put(COBRA, 6); // 3 + 0 + 3
    }};

    /**
     * Heuristique servant à calculer la valeur d'une configuration du jeu à partir :
     * - Du nombre de pions du joueur courant face au nombre de pions de l'adversaire
     * - La distance de tous les pions du joueur courant par rapport au pion ma�tre adverse
     * - La distance du pion ma�tre du joueur courant par rapport à la case temple adverse
     *
     * @param jeu référence du jeu
     * @return la valeur d'une configuration du jeu, plus la valeur est élevée, plus la configuration est intéressante
     */
    public static double heuristiqueDeBase(Jeu jeu) {
        // TODO cette méthode devra probablement être un wrapper de son équivalent utilisant un vecteur de bits
        return 1.5 * nbPions(jeu) + -2 * distancePionsMaitre(jeu) + -2 * distanceMaitreTemple(jeu);
    }

    // TODO
    public static int heuristiqueDeBase(int idJoueurCourant, EtatJeu etatjeu) {
        return 0;
    }

    /**
     * Heuristique servant à calculer la valeur d'une configuration du jeu à partir :
     * - Du nombre de pions du joueur courant face au nombre de pions de l'adversaire
     * - La valeur des cartes possédées par le joueur courant
     * - Le nombre de coups qu'on peut jouer et s'ils permettent de capturer un pion
     * - La distance de tous les pions du joueur courant par rapport au pion ma�tre adverse
     * - La distance du pion ma�tre du joueur courant par rapport à la case temple adverse
     *
     * @param jeu référence du jeu
     * @return la valeur d'une configuration du jeu, plus la valeur est élevée, plus la configuration est intéressante
     */
    public static double heuristiqueAvancee(Jeu jeu) {
        // TODO cette méthode devra probablement être un wrapper de son équivalent utilisant un vecteur de bits
        return 1.5 * nbPions(jeu) + valeurCartes(jeu) + 1.5 * successeursNombreCaptures(jeu) + -3 * distancePionsMaitre(jeu) + -3 * distanceMaitreTemple(jeu);
    }

    // TODO
    public static int heuristiqueAvancee(int idJoueurCourant, EtatJeu etatJeu) {
        return 0;
    }


    /**
     * Si le joueur courant a plus de pions, cette heuristique renvoie un nombre positif non nul (> 0).
     * Si le joueur courant est désavantagé cette heuristique renvoie un nombre négatif (< 0).
     * Si égalité, renvoie 0.
     *
     * @param jeu référence du jeu
     */
    public static int nbPions(Jeu jeu) {
        try {
            if (jeu.getIdJoueurCourant() == ID_JOUEUR_1) {
                return nbPionsJ1(jeu) - nbPionsJ2(jeu);
            } else {
                return nbPionsJ2(jeu) - nbPionsJ1(jeu);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Renvoie le nombre de pions possédés par la joueur 1
     *
     * @param jeu
     * @return le nombre de pions appartenant au joueur 1
     */
    public static int nbPionsJ1(Jeu jeu) {
        return jeu.getPionsJoueur1().size();
    }

    /**
     * Renvoie le nombre de pions possédés par la joueur 1
     *
     * @param jeu
     * @return le nombre de pions appartenant au joueur 1
     */
    public static int nbPionsJ2(Jeu jeu) {
        return jeu.getPionsJoueur2().size();
    }

    /**
     * Si le joueur courant a de meilleures cartes, cette heuristique renvoie un nombre positif non nul (> 0).
     * Si le joueur courant est désavantagé cette heuristique renvoie un nombre négatif (< 0).
     * Si égalité, renvoie 0.
     *
     * @param jeu référence du jeu
     */
    public static int valeurCartes(Jeu jeu) {
        try {
            if (jeu.getIdJoueurCourant() == ID_JOUEUR_1) {
                return valeurCartesJ1(jeu) - valeurCartesJ2(jeu);
            } else {
                return valeurCartesJ2(jeu) - valeurCartesJ1(jeu);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Renvoie la somme des valeurs de cartes du joueur 1
     *
     * @param jeu référence du jeu
     * @return la valeur totale des cartes du joueur 1
     */
    public static int valeurCartesJ1(Jeu jeu) {
        try {
            List<Carte> lc1 = jeu.getCartesJoueur1();
            return VALEURS_CARTES.get(lc1.get(0).getType()) + VALEURS_CARTES.get(lc1.get(1).getType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Renvoie la somme des valeurs de cartes du joueur 2
     *
     * @param jeu référence du jeu
     * @return la valeur totale des cartes du joueur 2
     */
    public static int valeurCartesJ2(Jeu jeu) {
        try {
            List<Carte> lc2 = jeu.getCartesJoueur2();
            return VALEURS_CARTES.get(lc2.get(0).getType()) + VALEURS_CARTES.get(lc2.get(1).getType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Renvoie le nombre d'états successeurs possibles pour une config donnée
     * + le nombre de pions pouvant �tre capturé dans chaque successeur
     *
     * @param jeu référence du jeu
     * @return nombre d'états successeurs + le nombre de pions qu'on peut capturer dans chaque config
     */
    public static int successeursNombreCaptures(Jeu jeu) {
        try {
            int res = 0;
            List<Carte> cartesJoueurCourant = jeu.getCartesJoueurCourant();
            List<Pion> pionsJoueurCourant = jeu.getPionsJoueurCourant();
            for (Carte c : cartesJoueurCourant) {
                for (Pion p : pionsJoueurCourant) {
                    List<Coup> coupsPossibles = jeu.getCoupsPossibles(c, p.getPosition());
                    for (Coup cp : coupsPossibles) {
                        res += 1;
                        Point arrivee = cp.getArrivee();
                        if (!jeu.estCaseVide(arrivee.x, arrivee.y) && jeu.getProprietairePionAt(arrivee.x, arrivee.y) != jeu.getIdJoueurCourant()) {
                            res += 1;
                        }
                    }
                }
            }
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Renvoie la négation de la somme de toutes les distances euclidiennes entre le pion ma�tre adverse
     * et les pions du joueur courant
     *
     * @param jeu référence du jeu
     * @return distance de tous les pions du joueur courant par rapport au pion ma�tre adverse
     */
    public static int distancePionsMaitre(Jeu jeu) {
        int res = 0;
        Point maitreAdverse = null;
        List<Pion> pionsJoueurCourant = jeu.getPionsJoueurCourant();
        List<Pion> pionsAdverse;

        if (jeu.getIdJoueurCourant() == ID_JOUEUR_1) {
            pionsAdverse = jeu.getPionsJoueur2();
        } else {
            pionsAdverse = jeu.getPionsJoueur1();
        }

        for (Pion p : pionsAdverse) {
            if (p.getRole() == PION_MAITRE) {
                maitreAdverse = p.getPosition();
            }
        }

        assert maitreAdverse != null;
        for (Pion p : pionsJoueurCourant) {
            Point positionPion = p.getPosition();
            res += (int) (Math.sqrt(Math.pow(Math.abs(positionPion.x - maitreAdverse.x), 2) + Math.pow(Math.abs(positionPion.y - maitreAdverse.y), 2)));
        }

        return -1 * res;
    }

    /**
     * Renvoie la négation de la distance euclidienne entre le pion maître du joueur courant et la case temple adverse
     *
     * @param jeu référence du jeu
     * @return distance entre le pion maître du joueur courant et la case temple adverse
     */
    public static int distanceMaitreTemple(Jeu jeu) {
        Point positionMaitre = null;
        Point templeAdverse;
        List<Pion> pionsJoueurCourant = jeu.getPionsJoueurCourant();

        for (Pion p : pionsJoueurCourant) {
            if (p.getRole() == PION_MAITRE) {
                positionMaitre = p.getPosition();
            }
        }

        if (jeu.getIdJoueurCourant() == ID_JOUEUR_1) {
            templeAdverse = TEMPLE_JOUEUR_2;
        } else {
            templeAdverse = TEMPLE_JOUEUR_1;
        }

        assert positionMaitre != null;
        return (int) (-1 * Math.sqrt(Math.pow(Math.abs(templeAdverse.x - positionMaitre.x), 2) + Math.pow(Math.abs(templeAdverse.y - positionMaitre.y), 2)));
    }

}
