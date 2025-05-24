package Modele;

import static Global.Config.*;

import Exceptions.CaseVideException;
import Vue.InfosDeConfigUI;

import java.awt.*;

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
        // Le constructeur est privé pour implémenter le pattern Singleton.
    }


    public int getIdJoueur() throws CaseVideException {
       return  jeu.getProprietairePionAt(position.x, position.y);
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
        ROLEPION rp;
        try {
            rp = jeu.getRolePionAt(position.x, position.y);
        } catch (CaseVideException e) {
            throw new RuntimeException(e);
        }
        switch (rp) {
            case PION_MAITRE:
                tc = TYPE_ELEMENT_SUR_CASE.PION_MAITRE;
                break;
            case PION_ETUDIANT:
                tc = TYPE_ELEMENT_SUR_CASE.PION_ETUDIANT;
                break;
        }
        return tc;
    }


    public int getProprietaire() {
        if (jeu.estCaseVide(position.x, position.y)) return 0;
        try {
            return jeu.getProprietairePionAt(position.x, position.y);
        } catch (CaseVideException e) {
            throw new RuntimeException(e);
        }
    }

    public ROLEPION getRole(){
        try {
            return  jeu.getRolePionAt(position.x, position.y);
        } catch (CaseVideException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return getProprietaire();
    }


    public Point getCoordonnee() {
        return position;
    }

}
