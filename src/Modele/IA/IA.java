package Modele.IA;

import Global.Config.NIVEAU_IA;
import Global.Config.VITESSE_IA;
import Modele.Coup;
import Modele.Joueur;
import Modele.Pion;

import static Global.Config.TYPE_JOUEUR;
import static Global.Config.TYPE_JOUEUR.JOUEUR_IA;


public abstract class IA extends Joueur {
    private VITESSE_IA vitesseIa;

    /**
     * Constructeur pour créer un nouveau joueur.
     * Initialise les propriétés de base et crée des listes vides pour les cartes et les pions.
     *
     * @param id  L'identifiant unique du joueur (par ex. 1 pour Joueur 1).
     * @param nom Le nom du joueur (ne doit pas être null ou vide).
     * @throws NullPointerException     si nom ou couleur est null.
     * @throws IllegalArgumentException si nom est vide ou id est invalide (par ex. < 1).
     */
    public IA(int id, String nom) {
        super(id, nom);
    }

    /**
     * Calcul un Coup à suggérer ou à jouer
     *
     * @return Coup calculé, null si aucun Coup possible
     */
    public abstract Coup calculerCoup();

    /**
     * Renvoie le pion choisi par l'IA
     *
     * @return Référence du pion choisi
     */
    public abstract Pion getPionChoisi();

    /**
     * Renvoie la carte choisie (relative au joueur courant)
     *
     * @return la carte choisie (0 ou 1)
     */
    public abstract int getCarteChoisie();

    /**
     * Vérifie si l'IA est en train de réfléchir (d'effectuer des calculs)
     *
     * @return vrai si l'IA réfléchit, faux sinon
     */
    public abstract boolean isThinking();

    /**
     * Renvoie le niveau de l'IA
     *
     * @return FAIBLE | MOYEN | FORT
     */
    public abstract NIVEAU_IA getNiveau();

    /**
     * Renvoie la vitesse courante de l'IA
     *
     * @return LENTE | MOYENNE | RAPIDE
     */
    public VITESSE_IA getVitesse() {
        return vitesseIa;
    }

    /**
     * Défini la vitesse de calcul de l'IA
     *
     * @param vitesse LENTE | MOYENNE | RAPIDE
     */
    public void setVitesse(VITESSE_IA vitesse) {
        vitesseIa = vitesse;
    }

    /**
     * Renvoie le type du joueur courant
     *
     * @return HUMAIN | IA
     */
    @Override
    public TYPE_JOUEUR getTypeJoueur() {
        return JOUEUR_IA;
    }
}
