package Modele.IA;

import Global.Config.NIVEAU_IA;
import Modele.Coup;
import Modele.Jeu;
import Modele.Pion;

import java.util.Objects;

import static Global.Config.NIVEAU_IA.MOYEN;
import static Global.Config.VITESSE_IA.MOYENNE;

public class IAMoyen extends IA {
    private final Jeu jeu;
    private Pion pionChoisi;
    private int carteChoisie;
    private boolean isThinking;
    private ArbreMinMax arbreMinMax;

    public IAMoyen(Jeu jeu, int id, String nom) {
        super(id, nom);
        this.jeu = Objects.requireNonNull(jeu, "Le jeu fourni à l'IA ne peut pas être null");
        this.isThinking = false;
        this.pionChoisi = null;
        this.carteChoisie = 0;
        setVitesse(MOYENNE);
        arbreMinMax = new ArbreMinMax();
    }

    /**
     * Calcul un Coup à suggérer ou à jouer
     *
     * @return Coup calculé, null si aucun Coup possible
     */
    @Override
    public Coup calculerCoup() {
        Coup coup = arbreMinMax.choisirCoup(
                getId(),
                new Noeud(
                        new EtatJeu(
                                jeu.getIdJoueurCourant(),
                                jeu.getCarteSupplementaire(),
                                jeu.getCartesJoueur1(),
                                jeu.getCartesJoueur2(),
                                jeu.getPionsJoueur1(),
                                jeu.getPionsJoueur2()
                        ),
                        null),
                4,
                MOYEN);

        pionChoisi = arbreMinMax.getPionChoisi();
        carteChoisie = arbreMinMax.getCarteChoisie();

        return coup;
    }

    /**
     * Renvoie le pion choisi par l'IA
     *
     * @return Référence du pion choisi
     */
    @Override
    public Pion getPionChoisi() {
        return pionChoisi;
    }

    /**
     * Renvoie la carte choisie (relative au joueur courant)
     *
     * @return la carte choisie (0 ou 1)
     */
    @Override
    public int getCarteChoisie() {
        return carteChoisie;
    }

    /**
     * Vérifie si l'IA est en train de réfléchir (d'effectuer des calculs)
     *
     * @return vrai si l'IA réfléchit, faux sinon
     */
    @Override
    public boolean isThinking() {
        return isThinking;
    }

    /**
     * Termine le processus de calcul de l'IA de manière aussi propre que possible
     */
    @Override
    public void stop() {
        isThinking = false;
    }


    /**
     * Renvoie le niveau de l'IA
     *
     * @return FAIBLE | MOYEN | FORT
     */
    @Override
    public NIVEAU_IA getNiveau() {
        return MOYEN;
    }
}
