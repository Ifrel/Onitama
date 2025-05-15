package Modele.IA;

import Modele.Carte;
import Modele.Coup;
import Modele.Jeu;
import Modele.Pion;

import java.util.List;
import java.util.Random;

import static Global.Config.NIVEAU_IA;
import static Global.Config.NIVEAU_IA.FAIBLE;

public class IAFaible extends IA {
    private final Jeu jeu;

    public IAFaible(Jeu jeu, int id, String nom) {
        super(id, nom);
        this.jeu = jeu;
    }

    /**
     * Calcul un Coup à suggérer ou à jouer
     *
     * @return Coup calculé, null si aucun Coup possible
     */
    @Override
    public Coup calculerCoup() {
        Coup c = null;
        Random r = new Random();
        List<Carte> cartesIA = jeu.getCartesJoueurCourant();
        List<Pion> pionsIA = jeu.getPionsJoueurCourant();

        Carte carteChoisie;
        Pion pionChoisi;

        /*
         * Preuve de correction totale :
         * - Terminaison : Boucle externe : La taille de la liste des cartes à utiliser diminue à chaque itération
         *                 Boucle interne : La taille de la liste des pions à utiliser diminue à chaque itération
         * - Correction Partielle : Boucle externe : La liste des cartes contient les cartes uniques qui n'ont pas encore été traitées
         *                                           On obtient à chaque itération une carte aléatoire non traitée
         *                          Boucle interne : La liste des pions ne contient que des pions qui n'ont pas encore été traités pour la carte courante
         *                                           On obtient un pion aléatoire à chaque itération
         * Note : le générateur de nombres aléatoire est uniforme
         */
        while (!cartesIA.isEmpty()) {
            int ca = r.nextInt(cartesIA.size());
            System.err.println("taille cartes " + cartesIA.size());
            carteChoisie = cartesIA.remove(ca); // carte sélectionnée de manière aléatoire uniforme
            jeu.setCarteSelectionnee(ca);
            while (!pionsIA.isEmpty()) {
                int pi = r.nextInt(pionsIA.size());
                pionChoisi = pionsIA.remove(pi); // pion sélectionné de manière aléatoire uniforme
                jeu.setPionSelectionne(pionChoisi.getPosition());
                List<Coup> coupsPossibles = jeu.getCoupsPossibles(carteChoisie, pionChoisi.getPosition()); // liste de tous les coups possibles étant donné une carte et un pion
                if (coupsPossibles.isEmpty()) { // pas de coup possible pour la carte et le pion courants
                    continue; // donc on passe au pion suivant
                }
                c = coupsPossibles.get(r.nextInt(coupsPossibles.size())); // coup sélectionné de manière aléatoire uniforme
                return c; // après avoir trouvé un coup, on sort directement, sinon on continue à chercher
            }
            if (!cartesIA.isEmpty()) { // il n'est pas nécessaire de construire une liste de pions qui ne sera pas utilisée
                pionsIA = jeu.getPionsJoueurCourant(); // on change de carte donc on récupère à nouveau les pions
            }
        }
        return c;
    }

    /**
     * Renvoie le niveau de l'IA
     *
     * @return FAIBLE | MOYEN | FORT
     */
    @Override
    public NIVEAU_IA getNiveau() {
        return FAIBLE;
    }

}
