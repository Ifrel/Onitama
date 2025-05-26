package Modele;


import Global.Config.ROLEPION;

import static Global.Config.*;
import static Global.Config.ROLEPION.*;

import java.awt.*;
import java.io.Serializable;

/**
 * Représente un pion dans le modèle du jeu.
 * Un pion a une position, une couleur, un propriétaire, une image et un type spécifique. */
public class Pion implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
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
    public int getIDProprietaire() { return proprietaire; }



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

    /**
     * Renvoie la représentation textuelle du pion
     * 'E' pour étudiant, 'M' pour maitre
     * ex: M2:(0,2) -> le pions maitre du joueur 2 est en position (0,2)
     * @return chaine de caractères représentant le pion
     */
    @Override
    public String toString() {
        String S = "";
        S += (getRole() == PION_ETUDIANT ? "E" : "M");
        S += (getIDProprietaire() == ID_JOUEUR_1 ? "1" : "2");
        Point pos = getPosition();
        S += ":(" + pos.x + "," + pos.y + ")";
        return S;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this==obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Pion pion = (Pion) obj;
        if(proprietaire != pion.proprietaire) return false;
        if(role != pion.role) return false;
        return (position != null ? position.equals(pion.position) : pion.position == null);
    }
    @Override
    public Pion clone() {
        try {
            return (Pion) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
