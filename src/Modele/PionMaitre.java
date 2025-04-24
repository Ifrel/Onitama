package Modele;

import Global.Config;
import Global.Config.ROLEPION;


public class PionMaitre extends Pion {

    public PionMaitre(boolean proprietaire) {
        super(proprietaire);
    }

    @Override
    public Config.ROLEPION get_role() {
        return Config.ROLEPION.Maitre; // or some custom logic
    }
}
