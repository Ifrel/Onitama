package Modele.IA;

import Modele.Jeu;

/**
 * Calcule la valeur d'une configuration du Jeu
 */
public class Heuristiques {

    public static int heuristiqueDeBase(Jeu jeu) {
        // TODO cette méthode devra probablement être un wrapper de son équivalent utilisant un vecteur de bits
        return 0;
    }

    public static int heuristiqueDeBase(int idJoueurCourant, EtatJeu etatjeu) {
        return 0;
    }

    public static int heuristiqueAvancee(Jeu jeu) {
        // TODO cette méthode devra probablement être un wrapper de son équivalent utilisant un vecteur de bits
        // TODO utiliser des sous méthodes privées pour créer une heuristique composée
        /*
         * - Distance du maitre par rapport à la case temple adverse
         * - Nombre de pions restants joueur courant
         * - Une carte = une valeur numérique (Nombre de déplacements possibles ET Distance atteignable)
         * - Nombre de coups possibles ? (nombre d'états successeurs)
         * - Possibilité de manger un pion adverse en un coup
         * - Distance de tous les pions par rapport au maitre adverse
         * - Position sur le terrain (ensemble de pion / maitre) (centre mieux que bords ?)
         *
         */
        return 0;
    }

    public static int heuristiqueAvancee(int idJoueurCourant, EtatJeu etatJeu) {
        return 0;
    }
}
