package Modele;

import java.util.ArrayList;

public class Coup {
    int i, j; // coin supérieur gauche du rectangle
    ArrayList<int []> cases; // liste des cases affectées

    public Coup(int i, int j) {
        this.i = i;
        this.j = j;
        this.cases = new ArrayList<>();
    }

    public boolean equals(Coup c) {
        return this.i == c.i && this.j == c.j;
    }

    ///  renvoie la représentation textuelle d'un coup
    public String toString() {
        StringBuilder S = new StringBuilder();
        S.append("__[").append(i).append(",").append(j).append("]__:");
        S.append("{");
        for (int i = 0; i < cases.size() - 1; i++) {
            int [] ca = cases.get(i);
            S.append("(").append(ca[0]).append(",").append(ca[1]).append("),");
        }
        int [] ca = cases.get(cases.size() - 1);
        S.append("(").append(ca[0]).append(",").append(ca[1]).append(")");
        S.append("}");
        return S.toString();
    }
}
