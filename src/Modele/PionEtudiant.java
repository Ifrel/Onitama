package Modele;


import Global.Config;
import Global.Config.ROLEPION;

public class PionEtudiant extends Pion {
    boolean proprietaire;

    public PionEtudiant(boolean proprietaire) {
//        super();
        this.proprietaire = proprietaire;
    }

    public Config.ROLEPION get_role() {
        return ROLEPION.Etudiant; // or some custom logic
    }
}
