package Modele.IA;

import Global.Config.NIVEAU_IA;
import Modele.Carte;
import Modele.Coup;
import Modele.Jeu;
import Modele.Pion;

import java.util.List;
import java.util.Random;

import static Global.Config.NIVEAU_IA.FAIBLE;

public class IAFaible extends IA {
    private final Jeu jeu;

    public IAFaible(Jeu jeu) {
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


        while (!cartesIA.isEmpty()) {
            carteChoisie = cartesIA.remove(r.nextInt(cartesIA.size()));
            while (!pionsIA.isEmpty()) {
                pionChoisi = pionsIA.remove(r.nextInt(pionsIA.size()));
                List<Coup> coupsPossibles = jeu.getCoupsPossibles(carteChoisie, pionChoisi.getPosition());
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
