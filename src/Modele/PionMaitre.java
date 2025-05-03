package Modele;

import Global.Config;
import Global.Config.ROLEPION;


public class PionMaitre extends Pion {
    boolean proprietaire;

    public PionMaitre(boolean proprietaire) {
        this.proprietaire = proprietaire;
    }

    public Config.ROLEPION get_role() {
        return Config.ROLEPION.Maitre; // or some custom logic
    }
}
