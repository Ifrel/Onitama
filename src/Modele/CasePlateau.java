package Modele;

import java.awt.*;
import java.nio.file.Path;

import static Global.Config.TYPE_ELEMENT_TERRAIN;
import static Global.Config.TYPE_ELEMENT_TERRAIN.*;

/**
 * Représente une case du plateau de jeu, pouvant contenir un pion ou rien. */
public class CasePlateau {
    private Pion pion;
    private TYPE_ELEMENT_TERRAIN type;
    private final Point coordonnes;
    private Path cheminImagePion;


    /**
     * Constructeur à partir d’un pion. Les coordonnées sont récupérées depuis le pion.
     * @param pion le pion placé sur la case     */
    public CasePlateau(Pion pion){
        this.pion = pion;
        this.type = pion.getType();
        this.coordonnes = pion.getPosition();
        this.cheminImagePion = pion.getCheminImage();
    }

    /**
     * Constructeur à partir d’un pion et de coordonnées explicites.
     * @param pion le pion placé sur la case
     * @param coordonnes coordonnées de la case     */
    public CasePlateau(Pion pion, Point coordonnes){
        this.pion = pion;
        this.type = pion.getType();
        this.coordonnes = coordonnes;
        this.cheminImagePion = pion.getCheminImage();
    }


    /**
     * Constructeur d’une case vide à une position donnée.
     * @param coordonnes les coordonnées de la case     */
    public CasePlateau(Point coordonnes){
        this.type = VIDE;
        this.pion = null;
        this.coordonnes = coordonnes;
        this.cheminImagePion = null;
    }


    /**
     * Supprime le pion de la case, et met à jour le type et l'image en conséquence.     */
    public void removePion(){
        this.pion = null;
        this.type = VIDE;
        this.cheminImagePion = null;
    }


    /**
     * Attribue un pion à la case, et met à jour son type et son image.
     * @param pion le nouveau pion à placer   */
    public void setPion(Pion pion){
        this.pion = pion;
        this.type = pion.getType();
        this.cheminImagePion = pion.getCheminImage();
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
        return cheminImagePion;
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
