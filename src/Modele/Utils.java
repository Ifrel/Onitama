package Modele;

import Exceptions.CaseVideException;
import Exceptions.ConfigurationIllegaleException;
import Global.Config;

import java.awt.*;
import java.util.*;
import java.util.List;

import static Global.Config.*;
import static Global.Config.ROLEPION.PION_ETUDIANT;

/**
 * Classe qui a pour but de stocker des méthodes utilitaires et aussi génériques que possibles,
 * qui servent notamment à garantir les propriétés du jeu
 */
public class Utils {

    /**
     * Vérifie que chaque carte de la liste est unique
     *
     * @param listeCartes liste de cartes
     * @throws ConfigurationIllegaleException
     */
    public static void cartesToutesDifferentes(List<Config.TYPECARTE> listeCartes) throws ConfigurationIllegaleException {
        HashSet<Config.TYPECARTE> hs = new HashSet<>();

        for (Config.TYPECARTE tc : listeCartes) {
            if (hs.contains(tc)) {
                throw new ConfigurationIllegaleException("ERREUR La carte " + tc + " apparait plus d'une fois, chaque carte doit etre unique");
            }
            hs.add(tc);
        }
    }

    /**
     * Vérifie que la séléction de cartes est conforme aux règles du jeu (toutes les cartes sont uniques, 2 cartes seulement par joueur + 1 carte supplémentaire)
     *
     * @param carteEnPlus   carte supplémentaire du jeu
     * @param cartesJoueur1 cartes du joueur 1
     * @param cartesJoueur2 cartes du joueur 2
     * @throws ConfigurationIllegaleException
     */
    public static void verifierSelectionCartesConforme(TYPECARTE carteEnPlus, List<TYPECARTE> cartesJoueur1, List<TYPECARTE> cartesJoueur2) throws ConfigurationIllegaleException {
        try {
            List<TYPECARTE> cartesSelectionnees = new ArrayList<>(cartesJoueur1);
            cartesSelectionnees.addAll(cartesJoueur2);
            cartesSelectionnees.add(carteEnPlus);
            cartesToutesDifferentes(cartesSelectionnees);
        } catch (ConfigurationIllegaleException e) {
            throw new ConfigurationIllegaleException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Vérifie que la séléction des pions est conforme aux règles du jeu
     *
     * @param pionsJoueur1 liste des pions du joueur 1
     * @param pionsJoueur2 liste des pions du joueur 2
     * @throws ConfigurationIllegaleException
     */
    public static void verifierSelectionPionsConforme(List<Pion> pionsJoueur1, List<Pion> pionsJoueur2) throws ConfigurationIllegaleException {
        if (pionsJoueur1.size() > 5) {
            throw new ConfigurationIllegaleException("Le joueur 1 ne peux pas avoir plus de 5 pions, pourtant " + pionsJoueur1.size() + " ont été fournis");
        }
        if (pionsJoueur2.size() > 5) {
            throw new ConfigurationIllegaleException("Le joueur 2 ne peux pas avoir plus de 5 pions, pourtant " + pionsJoueur2.size() + " ont été fournis");
        }

        HashSet<Point> coordonneesOccupees = new HashSet<>();
        int nbEleves, nbMaitre;

        HashMap<Integer, List<Pion>> listePions = new HashMap<>();
        listePions.put(ID_JOUEUR_1, pionsJoueur1);
        listePions.put(ID_JOUEUR_2, pionsJoueur2);

        for (int i = 0; i < 2; i++) {
            List<Pion> lp = listePions.get(i + 1);
            nbEleves = nbMaitre = 0;
            for (Pion p : lp) {
                Point pos = p.getPosition();
                if (coordonneesOccupees.contains(pos)) {
                    throw new ConfigurationIllegaleException("Il y a déjà un pion à la position (" + pos.x + "," + pos.y + ")");
                }
                coordonneesOccupees.add(pos);
                if (p.getRole() == PION_ETUDIANT) {
                    nbEleves++;
                } else {
                    nbMaitre++;
                }
                int prop = p.getIDProprietaire();
                if (prop != i + 1) {
                    throw new ConfigurationIllegaleException("Le pion " + p + " est possédé par " + prop + " alors qu'il devrait etre possédé par " + (i + 1));
                }
            }

            if (nbEleves > 4) {
                throw new ConfigurationIllegaleException("Le joueur " + (i + 1) + " a plus de 4 pions élèves (" + nbEleves + "), impossible");
            }

            if (nbMaitre > 1) {
                throw new ConfigurationIllegaleException("Le joueur " + (i + 1) + " a plus d'un pions maitre (" + nbMaitre + "), impossible");
            }
        }
    }

    /**
     * Renvoie la liste de tous les coups possibles étant donné une carte et un pion
     * @param jeu référence au jeu courant
     * @param carteSelectionee carte séléctionnée qui indique les déplacements théoriques relativement à la position du pion
     * @param positionPion position du pion séléctionné
     * @return liste des coups possibles
     * @throws IllegalStateException
     */
    public static List<Coup> getCoupsPossibles(Jeu jeu, Carte carteSelectionee, Point positionPion) throws IllegalStateException {
        Objects.requireNonNull(jeu, "Nécessite une référence non null au jeu");
        Objects.requireNonNull(carteSelectionee, "Nécessite une référence non null à la carte séléctionnée");
        Objects.requireNonNull(positionPion, "Nécessite une référence non null au point qui contient la position du point");

        List<Coup> coups = new ArrayList<>();

        List<Point> deplacements = carteSelectionee.getMoves();

        int idJoueurCourant = jeu.getIdJoueurCourant();
        Point direction;
        if (idJoueurCourant == ID_JOUEUR_1) {
            direction = new Point(-1, -1);
        } else {
            direction = new Point(1, 1);
        }

        int x, y;
        for (Point deplacement : deplacements) {
            x = positionPion.x + deplacement.x * direction.x;
            y = positionPion.y + deplacement.y * direction.y;

            try {
                jeu.verifieSiDansGrille(x, y);
            } catch (Exception e) {
                continue;
            }

            // ne pas manger son propre pion
            try {
                if (jeu.getProprietairePionAt(x, y) == jeu.getIdJoueurCourant()) {
                    continue;
                }
            } catch (CaseVideException ignored) {
            }

            // origine / position pion -> case arrivée possible
            coups.add(new Coup(new Point(positionPion.x, positionPion.y), new Point(x, y), jeu.getIdJoueurCourant(),carteSelectionee,jeu.getCarteSupplementaire()));
        }


        return coups;
    }


    /**
     * Vérifie si un coup est présent dans une liste de coups
     *
     * @param listeCoups liste de coups contenant ou non le coup
     * @param coup       coup dont on veut vérifier l'appartenance à la liste de coups
     * @return vrai si le coup appartient à la liste, faux sinon
     */
    public static boolean estDansListeDeCoups(List<Coup> listeCoups, Coup coup) {

        for (Coup c : listeCoups) {
            if(c.equals(coup)) {
                return true;
            }
        }
        return false;
    }
}
