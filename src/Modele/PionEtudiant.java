package Modele;


import Global.Config;
import Global.Config.ROLEPION;

public class PionEtudiant extends Pion {

    public PionEtudiant(boolean proprietaire) {
        super(proprietaire);
    }

    @Override
    public Config.ROLEPION get_role() {
        return ROLEPION.Etudiant; // or some custom logic
    }
}
