package Modele;

import java.awt.Point;

import static Modele.IARandom.*;
import static Modele.IAIntermediate.*;
import static Modele.IAStrong.*;

public class IA {
    private Jeu jeu;

    public IA(Jeu jeu) {
        this.jeu = jeu;
    }

    public void jouer(String niveau) {
        Point move = new Point(0, 0);
        switch(niveau) {
            case "easy":
               move = calculerRandom(jeu);
               break;
            case "intermediate":
                move = calculerIntermediate(jeu);
                break;
            case "strong":
                move = calculerStrong(jeu);
                break;
        }
        jeu.jouer(move.x, move.y);
    }
}
