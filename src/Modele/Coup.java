package Modele;

import java.awt.*;
import java.util.ArrayList;

public class Coup {
    private Point depart;
    private Point arrivee;

    public Coup(Point depart, Point arrivee) {
        this.depart = depart;
        this.arrivee = arrivee;
    }

    /**
     * renvoie vrai si deux coups sont égaux
     * @param c Coup à comparer
     * */
    public boolean equals(Coup c) {
        return getDepart().equals(c.getDepart()) && getArrivee().equals(c.getArrivee());
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

    /**
     * renvoie la représentation textuelle d'un coup
     * @return Un string représentant le coup
     */
    public String toString() {
        StringBuilder S = new StringBuilder();
        S.append("(").append(depart.getX()).append(",").append(depart.getY()).append(")");
        S.append(" -> ");
        S.append("(").append(arrivee.getX()).append(",").append(arrivee.getY()).append(")");
        return S.toString();
    }
}
