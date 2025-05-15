package Modele;

import java.awt.*;
import java.util.ArrayList;

public class Coup {
    private Point depart;
    private Point arrivee;
    private Carte carteEchangee;
    private boolean aMangerPion;

    public Coup(Point depart, Point arrivee, Carte carteEchangee) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.carteEchangee = carteEchangee;
    }

    /**
     * renvoie vrai si deux coups sont égaux
     * @param c Coup à comparer
     * */
    public boolean equals(Coup c) {
        boolean b1 = getDepart().equals(c.getDepart()) && getArrivee().equals(c.getArrivee());
        boolean b2 = carteEchangee.equals(c.getCarteEchangee());
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

    public Carte getCarteEchangee()
    {
        return carteEchangee;
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
}
