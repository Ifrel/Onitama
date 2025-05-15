package Modele;

import Modele.IA.IA;
import Modele.IA.IAFaible;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static Global.Config.*;
import static Global.Config.TYPECARTE.*;
import static org.junit.jupiter.api.Assertions.*;

public class IAFaibleTest {

    // pour un nombre suffisant de parties dans leur état initial, il devrait exister au moins un coup
    @Test
    void auMoinsUnCoupDebut() {
        Jeu jeu;
        IA ia;
        Coup c;

        for (int i = 0; i < 10_000; i++) {
            jeu = new Jeu();
            ia = new IAFaible(jeu, ID_JOUEUR_1, "IA 1");
            c = ia.calculerCoup();
            assertNotNull(c);
        }
    }

//    @Test
//    void scenario1() {
//        Jeu jeu;
//        IA ia;
//        Coup c;
//
//        jeu = new Jeu(CRABE, new ArrayList<>(Arrays.asList(BOEUF, MANTE)), new ArrayList<>(Arrays.asList(COQ, LAPIN)));
//        ia = new IAFaible(jeu);
//
//        /*
//         * | E1 | E1 | M1 | E1 | E1 |
//         * |    |    |    |    |    |
//         * |    |    |    |    |    |
//         * |    |    |    |    |    |
//         * | E2 | E2 | M2 | E2 | E2 |
//         */
//
//        System.err.println(jeu.toString());
//        jeu.setCarteSelectionnee(0);
//        jeu.setPionSelectionne(new Point(0, 2));
//        jeu.jouerCoup(new Coup(new Point(0, 2), new Point(1, 2), 1, jeu.getCarteSelectionnee(), jeu.getCarteSupplementaire()));
//        System.err.println(jeu.toString());
//
//        c = ia.calculerCoup();
//
//        assertTrue(c.getArrivee().equals());
//
//    }
}
