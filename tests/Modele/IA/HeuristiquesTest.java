package Modele.IA;

import Modele.Jeu;
import Modele.Pion;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static Global.Config.TYPECARTE.*;
import static org.junit.jupiter.api.Assertions.*;
import static Modele.IA.Heuristiques.*;
import static Global.Config.*;
import static Global.Config.ROLEPION.*;

public class HeuristiquesTest {

    /**
     * Vérifie que la sous-heuristique qui vérifie l'avantage par rapport au nombre de pions soit correctement calibrée
     * à différents instants de la partie
     */
    @Test
    void verifNbPionsDebutPartie() {
        Jeu jeu;
        jeu = new Jeu();

        /*
         * | E1 | E1 | M1 | E1 | E1 |
         * |    |    |    |    |    |
         * |    |    |    |    |    |
         * |    |    |    |    |    |
         * | E2 | E2 | M2 | E2 | E2 |
         */

        System.err.println(jeu.toString());
        assertEquals(5, nbPionsJ1(jeu));
        assertEquals(5, nbPionsJ2(jeu));
        assertEquals(0, nbPions(jeu));
    }

//    @Test
//    void verifNbPionsScenario1() {
    /**
     * FIXME le constructeur Jeu(pionsJoueur1, pionsJoueur2) n'assigne pas correctement les pions aux joueurs
     * /
//        Jeu jeu;
//        jeu = new Jeu(
//                new ArrayList<>() {{
//                    add(new Pion(ID_JOUEUR_1, new Point(0, 2), PION_MAITRE));
//                    add(new Pion(ID_JOUEUR_1, new Point(1, 1), PION_ETUDIANT));
//                    add(new Pion(ID_JOUEUR_1, new Point(0, 1), PION_ETUDIANT));
//                    add(new Pion(ID_JOUEUR_1, new Point(0, 4), PION_ETUDIANT));
//                }},
//                new ArrayList<>() {{
//                    add(new Pion(ID_JOUEUR_2, new Point(4, 1), PION_MAITRE));
//                    add(new Pion(ID_JOUEUR_2, new Point(4, 0), PION_ETUDIANT));
//                    add(new Pion(ID_JOUEUR_2, new Point(2, 1), PION_ETUDIANT));
//                    add(new Pion(ID_JOUEUR_2, new Point(1, 3), PION_ETUDIANT));
//                    add(new Pion(ID_JOUEUR_2, new Point(4, 4), PION_ETUDIANT));
//                }}
//        );
//        /*
//         * |    | E1 | M1 |    | E1 |
//         * |    | E1 |    | E2 |    |
//         * |    | E2 |    |    |    |
//         * |    |    |    |    |    |
//         * | E2 | M2 |    |    | E2 |
//         */
//
//        assertEquals(4, nbPionsJ1(jeu));
//        assertEquals(5, nbPionsJ1(jeu));
//        assertTrue(nbPions(jeu) < 0);
//    }

    @Test
    void verifierValeurCartes() {
        Jeu jeu;
        jeu = new Jeu(CRABE, new ArrayList<>(Arrays.asList(BOEUF, MANTE)), new ArrayList<>(Arrays.asList(COQ, LAPIN)));

        assertEquals(0, valeurCartes(jeu));

        jeu = new Jeu(MANTE, new ArrayList<>(Arrays.asList(BOEUF, CRABE)), new ArrayList<>(Arrays.asList(COQ, LAPIN)));

        assertEquals(2, valeurCartes(jeu));

        jeu = new Jeu(MANTE, new ArrayList<>(Arrays.asList(BOEUF, CRABE)), new ArrayList<>(Arrays.asList(DRAGON, LAPIN)));

        assertEquals(-2, valeurCartes(jeu));
    }

    @Test
    void verifierDistanceMaitreAdverseTemple() {
        Jeu jeu;
        jeu = new Jeu();
        /*
         * | E1 | E1 | M1 | E1 | E1 |
         * |    |    |    |    |    |
         * |    |    |    |    |    |
         * |    |    |    |    |    |
         * | E2 | E2 | M2 | E2 | E2 |
         */
    }
}
