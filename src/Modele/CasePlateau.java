package Modele;

import java.awt.*;
import java.nio.file.Path;

import static Global.Config.TYPE_ELEMENT_TERRAIN;
import static Global.Config.TYPE_ELEMENT_TERRAIN.*;

/**
 * Représente une case du plateau de jeu, pouvant contenir un pion ou rien. */
public class CasePlateau {
    private Pion pion;                      // Le pion actuellement sur la case, s'il y en a un
    private TYPE_ELEMENT_TERRAIN type;      //Le type de terrain de la case (ex. : VIDE, PION_MAITRE ou PION_ETUDIANT, etc.) */
    private final Point coordonnes;         // Coordonnées (ligne, colonne) de la case sur le plateau
    private Path chemainImagePion;          // Chemin vers l’image du pion présent sur la case, ou null


    /**
     * Constructeur à partir d’un pion. Les coordonnées sont récupérées depuis le pion.
     * @param pion le pion placé sur la case     */
    public CasePlateau(Pion pion){
        this.pion = pion;
        this.type = pion.getType();
        this.coordonnes = pion.getPosition();
        this.chemainImagePion = pion.getChemainImage();
    }

    /**
     * Constructeur à partir d’un pion et de coordonnées explicites.
     * @param pion le pion placé sur la case
     * @param coordonnes coordonnées de la case     */
    public CasePlateau(Pion pion, Point coordonnes){
        this.pion = pion;
        this.type = pion.getType();
        this.coordonnes = coordonnes;
        this.chemainImagePion = pion.getChemainImage();
    }


    /**
     * Constructeur d’une case vide à une position donnée.
     * @param coordonnes les coordonnées de la case     */
    public CasePlateau(Point coordonnes){
        this.type = VIDE;
        this.pion = null;
        this.coordonnes = coordonnes;
        this.chemainImagePion = null;
    }


    /**
     * Supprime le pion de la case, et met à jour le type et l'image en conséquence.     */
    public void removePion(){
        this.pion = null;
        this.type = VIDE;
        this.chemainImagePion = null;
    }


    /**
     * Attribue un pion à la case, et met à jour son type et son image.
     * @param pion le nouveau pion à placer   */
    public void setPion(Pion pion){
        this.pion = pion;
        this.type = pion.getType();
        this.chemainImagePion = pion.getChemainImage();
    }


    /**
     * Retourne les coordonnées de la case.
     * @return les coordonnées (Point) de la case   */
    public Point getCoordonnes(){
        return coordonnes;
    }


    /**
     * Retourne le chemin de l’image du pion présent sur la case.
     * @return le chemin d’image ou null si aucun pion    */
    public Path getCheminImage(){
        return chemainImagePion;
    }

    /**
     * Retourne le type de terrain de la case.
     * @return le type d’élément de terrain     */
    public TYPE_ELEMENT_TERRAIN getTypeElement() {
        return type;
    }


    /**
     * Retourne le pion actuellement sur la case.
     * @return le pion, ou null si la case est vide    */
    public Pion getPion(){
        return pion;
    }
}
