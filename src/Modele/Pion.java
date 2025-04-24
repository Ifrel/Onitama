package Modele;

import Global.Config;
import Global.Config.ROLEPION;

public class Pion {
    protected ROLEPION _role;
    private boolean _proprietaire;

    public Pion(boolean proprietaire)
    {
        _proprietaire = proprietaire;
    }

    public ROLEPION get_role() {
        return _role;
    }
}
