package Modele;

import Global.Config.ROLEPION;
import Global.Config.TYPE_ELEMENT_TERRAIN;

import java.awt.*;
import java.nio.file.Path;

/**
 * Représente un pion dans le modèle du jeu.
 * Un pion a une position, une couleur, un propriétaire, une image et un type spécifique. */
public interface Pion {

    /**
     * Retourne la couleur actuelle du pion.
     * @return la couleur du pion   */
    Color getCouleur();


    /**
     * Retourne l'identifiant du joueur propriétaire de ce pion.
     * @return l'ID du propriétaire (habituellement un entier associé à un joueur)     */
     int getProprietaire();



    /**
     * Retourne le chemin vers l'image associée à ce pion.
     * @return le chemin de l'image du pion    */
     Path getCheminImage();



    /**
     * Retourne le type d'élément du terrain que représente ce pion.
     * @return le type d'élément terrain (e.g., PION_ETUDIANT ou PION_MAITRE.)     */
     TYPE_ELEMENT_TERRAIN getType();


    /**
     * Retourne la position actuelle du pion sur la grille.
     * @return la position du pion sous forme de Point (x, y)   */
     Point getPosition();


    /**
     * revoie le statut di pion : Maitre ou Eleve
     * @return un type ROLEPION     */
    public ROLEPION getStatut();



    /**
     * Définit une nouvelle position pour le pion.
     * @param position la nouvelle position à attribuer     */
    void setNewPosition(Point position);


    /**
     * Modifie la couleur du pion.
     * @param couleur la nouvelle couleur   */
    void setCouleur(Color couleur);



    /**
     * Modifie le chemin de l'image représentant ce pion.
     * @param chemainImage le nouveau chemin de l'image     */
    void setChemainImage(Path chemainImage);
}
