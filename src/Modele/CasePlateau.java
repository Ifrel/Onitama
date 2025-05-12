package Modele;

import Global.Config.ROLEPION;

import java.awt.*;
import java.nio.file.Path;

/**
 * Représente une case du plateau de jeu, pouvant contenir un pion ou rien. */
public class CasePlateau {
    Jeu jeu;
    Point position;

    public static enum TYPE_ELEMENT_SUR_CASE {
        VIDE,
        PION_ETUDIANT,
        PION_MAITRE
    }

    private TYPE_ELEMENT_SUR_CASE typePion;


    public CasePlateau(Jeu jeu, Point position){
        this.jeu = jeu;
        this.position = position;
    }


    /**
     * Retourne le type de terrain de la case.
     * @return le type d’élément de terrain     */
    public TYPE_ELEMENT_SUR_CASE getTypeElement() {

        // impossible d'obtenir le role d'un pion sur une case vide car pas de pion
        if (jeu.estCaseVide(position.x, position.y)) {
            return TYPE_ELEMENT_SUR_CASE.VIDE;
        }
        TYPE_ELEMENT_SUR_CASE tc = TYPE_ELEMENT_SUR_CASE.VIDE;
        switch (jeu.getRolePionAt(position.x, position.y)) {
            case PION_MAITRE:
                tc = TYPE_ELEMENT_SUR_CASE.PION_MAITRE;
                break;
            case PION_ETUDIANT:
                tc = TYPE_ELEMENT_SUR_CASE.PION_ETUDIANT;
                break;
        }
        return tc;
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
