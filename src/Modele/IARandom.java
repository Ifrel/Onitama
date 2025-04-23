package Modele;

import Global.Config;

import java.awt.*;
import java.security.SecureRandom;

public class IARandom {

  static Point calculerRandom(Jeu jeu) {
    SecureRandom r = new SecureRandom();
    int x, y;

    do {
        if(jeu.nbCasesLibres() == 1) {
            return new Point(Config.posPoison.x,Config.posPoison.y);
        }
      x = r.nextInt(jeu.lignes());
      y = r.nextInt(jeu.colonnes());
    } while (jeu.estVide(x, y) || jeu.estPoison(x, y));

    return new Point(x, y);
  }
}
