package Modele;

import java.awt.*;
import java.nio.file.Path;

import static Global.Config.ROLEPION;
import static Global.Config.ROLEPION.*;

/**
 * Représente une case du plateau de jeu, pouvant contenir un pion ou rien. */
public class CasePlateau {
    Jeu jeu;
    Point position;

    public static enum TYPE_ELEMEMNT_SUR_CASE  {
        VIDE,
        PION_ETUDIANT,
        PION_MAITRE
    }

    private TYPE_ELEMEMNT_SUR_CASE typePion;


    public CasePlateau(Jeu jeu, Point position){
        this.jeu = jeu;
        this.position = position;
    }


    /**
     * Retourne le type de terrain de la case.
     * @return le type d’élément de terrain     */
    public TYPE_ELEMEMNT_SUR_CASE getTypeElement() {
        switch (jeu.getRolePionAt(position.x, position.y)){
            case PION_MAITRE: return  TYPE_ELEMEMNT_SUR_CASE.PION_MAITRE;
            case PION_ETUDIANT: return  TYPE_ELEMEMNT_SUR_CASE.PION_ETUDIANT;
            default: return  TYPE_ELEMEMNT_SUR_CASE.VIDE;
        }
    }


    public Path getCheminImage() {
        return Path.of("jzefheuhf");
    }

    public int getProprietaire() {
        return jeu.getProprietairePionAt(position.x, position.y);
    }

    public int getId() {
        return getProprietaire();
    }
}
