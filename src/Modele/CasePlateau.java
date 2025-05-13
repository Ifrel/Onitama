package Modele;

import Global.Config;
import Vue.InfosDeConfigUI;

import java.awt.*;

/**
 * Représente une case du plateau de jeu, pouvant contenir un pion ou rien. */
public class CasePlateau {
    /**L'instance unique de la classe {@code CasePlateau}. Elle est initialisée à {@code null}    */
    private static CasePlateau instance;

    Jeu jeu;
    Point position;

    public static enum TYPE_ELEMENT_SUR_CASE {
        VIDE,
        PION_ETUDIANT,
        PION_MAITRE
    }
    private TYPE_ELEMENT_SUR_CASE typePion;






    private CasePlateau(Jeu jeu){
        this.jeu = jeu;
        this.position = new Point();
        // Le constructeur est privé pour implémenter le pattern Singleton.
    }



    /**
     * Retourne l'instance unique de la classe {@code CasePlateau}.
     * Si aucune instance n'existe, une nouvelle est créée avant d'être retournée.
     *
     * @return L'instance unique de {@code CasePlateau}.     */
    public static CasePlateau getInstance(Jeu jeu) {
        if (instance == null) {
            instance = new CasePlateau(jeu);
        }
        return instance;
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


    public int getProprietaire() {
        if (jeu.estCaseVide(position.x, position.y)) return 0;
        return jeu.getProprietairePionAt(position.x, position.y);
    }

    public Config.ROLEPION getRole(){
        return  jeu.getRolePionAt(position.x, position.y);
    }

    public int getId() {
        return getProprietaire();
    }


    public CasePlateau getCasePlateau(int row, int col) {
        this.position.x = row;
        this.position.y = col;
        return instance;
    }


}
