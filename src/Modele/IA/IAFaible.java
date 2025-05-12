package Modele.IA;

import Modele.Carte;
import Modele.Coup;
import Modele.Jeu;
import Modele.Pion;

import java.util.List;
import java.util.Random;

public class IAFaible extends IA {
    private Jeu jeu;

    public IAFaible(Jeu jeu) {
        this.jeu = jeu;
    }

    /**
     * Calcul un Coup à suggérer ou à jouer
     * @return Coup calculé, null si aucun Coup possible
     */
    @Override
    Coup calculerCoup() {
        Coup c = null;
        Random r = new Random();
        List<Carte> cartesIA = jeu.getCartesJoueurCourant();
        List<Pion> pionsIA = jeu.getPionsJoueurCourant();

        Carte carteChoisie;
        Pion pionChoisi;


        while (! cartesIA.isEmpty()) {
            carteChoisie = cartesIA.remove(r.nextInt(cartesIA.size()));
            while (! pionsIA.isEmpty()) {
                pionChoisi = pionsIA.remove(r.nextInt(pionsIA.size()));
                List<Coup> coupsPossibles = jeu.getCoupsPossibles(pionChoisi.getPosition(), carteChoisie);
                if (coupsPossibles.isEmpty()) { // pas de coup possible pour la carte et le pion courants
                    continue;
                }
                c = coupsPossibles.get(r.nextInt(coupsPossibles.size()));
                return c; // après avoir trouvé un coup, on sort directement, sinon on continue à chercher
            }
            pionsIA = jeu.getPionsJoueurCourant(); // on change de carte donc on récupère à nouveau les pions

        }
        return c;
    }
}
