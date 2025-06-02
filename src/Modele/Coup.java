package Modele;

import java.io.Serializable;
import java.awt.*;
import static Global.Config.TYPECARTE;
//import java.util.ArrayList;

public class Coup implements Serializable{
    private static final long serialVersionUID = 1L;
    private Point depart;
    private Point arrivee;
    private Carte carteRecue;
    private Carte carteDonnee;
    private TYPECARTE typeCarteEnPlus;
    private TYPECARTE typeCarteDonnee;
    private boolean aMangerPion;

    public Coup(Point depart, Point arrivee, Carte carteRecue, Carte carteDonnee) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.carteRecue = carteRecue;
        this.carteDonnee = carteDonnee;
    }

    public Coup(Point depart, Point arrivee, TYPECARTE carteRecue, TYPECARTE carteDonnee) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.typeCarteEnPlus = carteRecue;
        this.typeCarteDonnee = carteDonnee;
    }

    /**
     * renvoie vrai si deux coups sont égaux
     * @param c Coup à comparer
     * */
    public boolean equals(Coup c) {
        boolean b1 = getDepart().equals(c.getDepart()) && getArrivee().equals(c.getArrivee());
        boolean b2 = carteRecue.equals(c.getCarteRecue());
        boolean b3 = aMangerPion == c.getPionMange();
        return b1 && b2 && b3;

    }

    /**
     * renvoie le Point correspondant au point de départ du coup
     * @return Le point de départ du coup
     * */
    public Point getDepart() {
        return this.depart;
    }

    /**
     * renvoie le Point correspondant à l'arrivée du coup
     * @return Le point d'arrivée du coup
     * */
    public Point getArrivee() {
        return this.arrivee;
    }

    public Carte getCarteRecue() {
        if (carteRecue == null) {
            return new Carte(typeCarteEnPlus);
        }
        return carteRecue.clone();
    }

    public Carte getCarteDonnee() {
        if (carteDonnee == null) {
            return new Carte(typeCarteDonnee);
        }
        return carteDonnee;
    }

    public boolean getPionMange()
    {
        return aMangerPion;
    }

    public void setAMangerPion(boolean aMP)
    {
        this.aMangerPion = aMP;
    }

    /**
     * renvoie la représentation textuelle d'un coup
     * @return Un string représentant le coup
     */
    public String toString() {
        StringBuilder S = new StringBuilder();
        S.append("(").append((int) depart.getX()).append(",").append((int) depart.getY()).append(")");
        S.append(" -> ");
        S.append("(").append((int) arrivee.getX()).append(",").append((int) arrivee.getY()).append(")");
        return S.toString();
    }

    /**
     * Calcule la complexité d'un coup pour ajuster le délai de l'IA
     * La complexité est basée sur plusieurs facteurs:
     * - Si le coup mange un pion adverse
     * - Si le coup implique le pion maître
     * - La distance parcourue par le pion
     * 
     * @return Un entier représentant la complexité du coup (0-10)
     */
    public int getComplexite() {
        int complexite = 0;

        // Si le coup mange un pion adverse, c'est plus complexe
        if (aMangerPion) {
            complexite += 3;
        }

        // Calculer la distance parcourue (Manhattan distance)
        int distanceX = Math.abs((int)arrivee.getX() - (int)depart.getX());
        int distanceY = Math.abs((int)arrivee.getY() - (int)depart.getY());
        int distance = distanceX + distanceY;

        // Plus la distance est grande, plus le coup est complexe
        complexite += Math.min(distance, 4);

        // Limiter la complexité à une valeur entre 0 et 10
        return Math.min(complexite, 10);
    }
}
