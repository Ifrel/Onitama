package Modele.IA;

import Modele.*;

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
     * @param etatJeu référence du jeu
     * @return la valeur d'une configuration du jeu, plus la valeur est élevée, plus la configuration est intéressante
     */
    public static double heuristiqueDeBase(EtatJeu etatJeu) {
        // TODO cette méthode devra probablement être un wrapper de son équivalent utilisant un vecteur de bits
        return 1.5 * nbPions(etatJeu) + -2 * distancePionsCourantMaitreAdverse(etatJeu) + -2 * distanceMaitreAdverseTemple(etatJeu);
    }

    // TODO
    public static int heuristiqueDeBase(int idJoueurCourant, EtatJeuCompact etatjeu) {
        return 0;
    }

    /**
     * Heuristique servant à calculer la valeur d'une configuration du jeu à partir :
     * - Du nombre de pions du joueur courant face au nombre de pions de l'adversaire
     * - La valeur des cartes possédées par le joueur courant
     * - Le nombre de coups qu'on peut jouer et s'ils permettent de capturer un pion
     * - La distance de tous les pions du joueur courant par rapport au pion ma�tre adverse
     * - La distance du pion ma�tre du joueur courant par rapport à la case temple adverse
     * - La distance du pion ma�tre par rapport à sa case temple (plus il est proche plus l'adversaire peut converger en un point)
     *
     * @param etatJeu référence du jeu
     * @return la valeur d'une configuration du jeu, plus la valeur est élevée, plus la configuration est intéressante
     */
    public static double heuristiqueAvancee(int idJoueur, EtatJeu etatJeu) {
        // TODO cette méthode devra probablement être un wrapper de son équivalent utilisant un vecteur de bits
        return 100 * nbPions(etatJeu)
                + valeurCartes(etatJeu)
                + 2.5 * successeursNombreCaptures(etatJeu)
                + -3 * distancePionsCourantMaitreAdverse(etatJeu)
                + 5 * distancePionsAdverseMaitreCourant(etatJeu)
                + -3 * distanceMaitreAdverseTemple(etatJeu)
                + 1.5 * distanceMaitreCourantTemple(etatJeu)
                + victoireDefaite(idJoueur, etatJeu);
    }

    // TODO
    public static int heuristiqueAvancee(int idJoueurCourant, EtatJeuCompact etatJeuCompact) {
        return 0;
    }


    /**
     * Si le joueur courant a plus de pions, cette heuristique renvoie un nombre positif non nul (> 0).
     * Si le joueur courant est désavantagé cette heuristique renvoie un nombre négatif (< 0).
     * Si égalité, renvoie 0.
     *
     * @param etatJeu référence du jeu
     */
    public static int nbPions(EtatJeu etatJeu) {
        try {
            if (etatJeu.getIdJoueurCourant() == ID_JOUEUR_1) {
                return nbPionsJ1(etatJeu) - nbPionsJ2(etatJeu);
            } else {
                return nbPionsJ2(etatJeu) - nbPionsJ1(etatJeu);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Renvoie le nombre de pions possédés par la joueur 1
     *
     * @param etatJeu
     * @return le nombre de pions appartenant au joueur 1
     */
    public static int nbPionsJ1(EtatJeu etatJeu) {
        return etatJeu.getPionsJoueur1().size();
    }

    /**
     * Renvoie le nombre de pions possédés par la joueur 1
     *
     * @param etatJeu
     * @return le nombre de pions appartenant au joueur 1
     */
    public static int nbPionsJ2(EtatJeu etatJeu) {
        return etatJeu.getPionsJoueur2().size();
    }

    /**
     * Si le joueur courant a de meilleures cartes, cette heuristique renvoie un nombre positif non nul (> 0).
     * Si le joueur courant est désavantagé cette heuristique renvoie un nombre négatif (< 0).
     * Si égalité, renvoie 0.
     *
     * @param etatJeu référence du jeu
     */
    public static int valeurCartes(EtatJeu etatJeu) {
        try {
            if (etatJeu.getIdJoueurCourant() == ID_JOUEUR_1) {
                return valeurCartesJ1(etatJeu) - valeurCartesJ2(etatJeu);
            } else {
                return valeurCartesJ2(etatJeu) - valeurCartesJ1(etatJeu);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Renvoie la somme des valeurs de cartes du joueur 1
     *
     * @param etatJeu référence du jeu
     * @return la valeur totale des cartes du joueur 1
     */
    public static int valeurCartesJ1(EtatJeu etatJeu) {
        try {
            List<Carte> lc1 = etatJeu.getCartesJoueur1();
            return VALEURS_CARTES.get(lc1.get(0).getType()) + VALEURS_CARTES.get(lc1.get(1).getType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Renvoie la somme des valeurs de cartes du joueur 2
     *
     * @param etatJeu référence du jeu
     * @return la valeur totale des cartes du joueur 2
     */
    public static int valeurCartesJ2(EtatJeu etatJeu) {
        try {
            List<Carte> lc2 = etatJeu.getCartesJoueur2();
            return VALEURS_CARTES.get(lc2.get(0).getType()) + VALEURS_CARTES.get(lc2.get(1).getType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Renvoie le nombre d'états successeurs possibles pour une config donnée
     * + le nombre de pions pouvant �tre capturé dans chaque successeur
     *
     * @param etatJeu référence du jeu
     * @return nombre d'états successeurs + le nombre de pions qu'on peut capturer dans chaque config
     */
    public static int successeursNombreCaptures(EtatJeu etatJeu) {
        try {
            int res = 0;
            List<Carte> cartesJoueurCourant = etatJeu.getCartesJoueurCourant();
            List<Pion> pionsJoueurCourant = etatJeu.getPionsJoueurCourant();
            List<Pion> pionsAdverse;
            if (etatJeu.getIdJoueurCourant() == ID_JOUEUR_1) {
                pionsAdverse = etatJeu.getPionsJoueur1();
            } else {
                pionsAdverse = etatJeu.getPionsJoueur2();
            }
            for (Carte c : cartesJoueurCourant) {
                for (Pion p : pionsJoueurCourant) {
                    List<Coup> coupsPossibles = Utils.getCoupsPossibles(etatJeu, etatJeu.getTypeCarteSupplementaire(), p.getPosition());
                    for (Coup cp : coupsPossibles) {
                        res += 1;
                        Point arrivee = cp.getArrivee();
                        for (Pion pa : pionsAdverse) {
                            if (pa.getPosition().equals(arrivee)) {
                                res += 1_000;
                            }
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
     * et les pions du joueur courant (plus il est proche mieux c'est)
     *
     * @param etatJeu référence du jeu
     * @return distance de tous les pions du joueur courant par rapport au pion ma�tre adverse
     */
    public static int distancePionsCourantMaitreAdverse(EtatJeu etatJeu) {
        int res = 0;
        Point maitreAdverse = null;
        List<Pion> pionsJoueurCourant = etatJeu.getPionsJoueurCourant();
        List<Pion> pionsAdverse;

        if (etatJeu.getIdJoueurCourant() == ID_JOUEUR_1) {
            pionsAdverse = etatJeu.getPionsJoueur2();
        } else {
            pionsAdverse = etatJeu.getPionsJoueur1();
        }

        for (Pion p : pionsAdverse) {
            if (p.getRole() == PION_MAITRE) {
                maitreAdverse = p.getPosition();
            }
        }

        if (maitreAdverse == null) {
            return 0;
        }
        for (Pion p : pionsJoueurCourant) {
            Point positionPion = p.getPosition();
            res += (int) (Math.sqrt(Math.pow(Math.abs(positionPion.x - maitreAdverse.x), 2) + Math.pow(Math.abs(positionPion.y - maitreAdverse.y), 2)));
        }

        return -1 * res;
    }

    /**
     * Renvoie la somme de toutes les distances euclidiennes entre le pion ma�tre courant
     * et les pions du joueur adverse (plus il est éloigné mieux c'est)
     *
     * @param etatJeu référence du jeu
     * @return distance de tous les pions du joueur courant par rapport au pion ma�tre adverse
     */
    public static int distancePionsAdverseMaitreCourant(EtatJeu etatJeu) {
        int res = 0;
        Point maitreCourant = null;
        List<Pion> pionsAdverse;

        if (etatJeu.getIdJoueurCourant() == ID_JOUEUR_1) {
            pionsAdverse = etatJeu.getPionsJoueur2();
        } else {
            pionsAdverse = etatJeu.getPionsJoueur1();
        }

        for (Pion p : etatJeu.getPionsJoueurCourant()) {
            if (p.getRole() == PION_MAITRE) {
                maitreCourant = p.getPosition();
            }
        }

        if (maitreCourant == null) {
            return 0;
        }
        for (Pion p : pionsAdverse) {
            Point positionPion = p.getPosition();
            res += (int) (Math.sqrt(Math.pow(Math.abs(positionPion.x - maitreCourant.x), 2) + Math.pow(Math.abs(positionPion.y - maitreCourant.y), 2)));
        }

        return res;
    }

    /**
     * Renvoie la négation de la distance euclidienne entre le pion maître du joueur courant et la case temple adverse
     *
     * @param etatJeu référence du jeu
     * @return distance entre le pion maître du joueur courant et la case temple adverse
     */
    public static int distanceMaitreAdverseTemple(EtatJeu etatJeu) {
        Point positionMaitre = null;
        Point templeAdverse;
        List<Pion> pionsJoueurCourant = etatJeu.getPionsJoueurCourant();

        for (Pion p : pionsJoueurCourant) {
            if (p.getRole() == PION_MAITRE) {
                positionMaitre = p.getPosition();
            }
        }

        if (etatJeu.getIdJoueurCourant() == ID_JOUEUR_1) {
            templeAdverse = TEMPLE_JOUEUR_2;
        } else {
            templeAdverse = TEMPLE_JOUEUR_1;
        }

        if (positionMaitre == null) {
            return 0;
        }
        return (int) (-1 * Math.sqrt(Math.pow(Math.abs(templeAdverse.x - positionMaitre.x), 2) + Math.pow(Math.abs(templeAdverse.y - positionMaitre.y), 2)));
    }

    /**
     * Renvoie la distance euclidienne entre le temple du joueur courant et son pion ma�tre, on considère que plus ils sont éloignés, moins l'ennemi à de raisons de converger
     *
     * @param etatJeu référence du jeu
     * @return proximité entre la case temple et le pion ma�tre du joueur courant
     */
    public static int distanceMaitreCourantTemple(EtatJeu etatJeu) {
        Point positionMaitre = null;
        Point temple;

        if (etatJeu.getIdJoueurCourant() == ID_JOUEUR_1) {
            temple = TEMPLE_JOUEUR_1;
        } else {
            temple = TEMPLE_JOUEUR_2;
        }

        for (Pion p : etatJeu.getPionsJoueurCourant()) {
            if (p.getRole() == PION_MAITRE) {
                positionMaitre = p.getPosition();
            }
        }

        if (positionMaitre == null) {
            return 0;
        }
        return (int) (Math.sqrt(Math.pow(Math.abs(temple.x - positionMaitre.x), 2) + Math.pow(Math.abs(temple.y - positionMaitre.y), 2)));
    }

    public static int victoireDefaite(int idJoueur, EtatJeu etatJeu) {
        if (!etatJeu.estEtatFinal()) {
            return 0;
        }

        if (etatJeu.getGagnant() == idJoueur) {
            return 1_000_000;
        } else {
            return -1_000_000;
        }
    }
}
