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
     * @param c : Coup à comparer
     * */
    public boolean equals(Coup c) {
        return getDepart().equals(c.getDepart()) && getArrivee().equals(c.getArrivee());
    }

    /**
     * renvoie le Point correspondant
     * */
    public Point getDepart() {
        return this.depart;
    }

    public Point getArrivee() {
        return this.arrivee;
    }

    ///  renvoie la représentation textuelle d'un coup
    public String toString() {
        StringBuilder S = new StringBuilder();
        S.append(" ");
        return S.toString();
    }
}
