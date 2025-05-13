package Modele;


import Global.Config.ROLEPION;

import java.awt.*;

import static Global.Config.*;

/**
 * Représente un pion dans le modèle du jeu.
 * Un pion a une position, une couleur, un propriétaire, une image et un type spécifique. */
public class Pion {
    private final int proprietaire;
    private final ROLEPION role;
    private Point position;


    public Pion(int proprietaire, Point position, ROLEPION role) {
        this.proprietaire = proprietaire;
        this.role = role;
        this.position = position;
    }

    /**
     * Retourne l'identifiant du joueur propriétaire de ce pion.
     * @return l'ID du propriétaire (habituellement un entier associé à un joueur)     */
    public int getProprietaire() { return proprietaire; }



    /**
     * Retourne le role d'élément du terrain que représente ce pion.
     * @return le role d'élément terrain (e.g., ETUDIANT ou MAITRE.)     */
    public ROLEPION getRole() { return role;}



    /**
     * Retourne la position actuelle du pion sur la grille.
     * @return la position du pion sous forme de Point (x, y)   */
    public Point getPosition(){ return position; }


    /**
     * revoie le statut di pion : Maitre ou Eleve
     * @return un type ROLEPION     */
    public ROLEPION getStatut() { return role; }




    /**
     * Définit une nouvelle position pour le pion.
     * @param position la nouvelle position à attribuer     */
    public void setNewPosition(Point position) {
        this.position = position;
    }

}
