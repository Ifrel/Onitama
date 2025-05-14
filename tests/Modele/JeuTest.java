package Modele;

import Exceptions.CaseVideException;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static Global.Config.*;
import static Global.Config.ROLEPION.*;
import static Global.Config.TYPECARTE.*;
import static org.junit.jupiter.api.Assertions.*;

// ces tests ne prennent pas encore en compte le joueur IA
class JeuTest {
    /// renvoie un nombre pseudo aléatoire entre 1 et 100
    private int rand(Random r) {
        return Math.min(Math.max(1, r.nextInt()), 100);
    }

    ///  renvoie un string aléatoire de taille 'size'
    private String randString(int size) {
        String src = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQSTUVWXYZ01234567890-_";
        Random r = new Random();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < size; i++) {
            res.append(src.charAt(r.nextInt(src.length())));
        }
        return res.toString();
    }

    @Test
    void dimensions() {
        Jeu jeu;
        jeu = new Jeu();

        assertEquals(5, jeu.lignes());
        assertEquals(5, jeu.colonnes());
    }

    @Test
    void cartesJoueur1Debut() {
        Jeu jeu;
        jeu = new Jeu();

        // taille = 2
        assertEquals(2, jeu.getCartesJoueur1().size());

        // toutes uniques
        HashSet<TYPECARTE> vues = new HashSet<>();
        for (Carte c : jeu.getCartesJoueur1()) {
            assertFalse(vues.contains(c.getType()));
            vues.add(c.getType());
        }
    }

    @Test
    void cartesJoueur2Debut() {
        Jeu jeu;
        jeu = new Jeu();

        // taille = 2
        assertEquals(2, jeu.getCartesJoueur2().size());

        // toutes uniques
        HashSet<TYPECARTE> vues = new HashSet<>();
        for (Carte c : jeu.getCartesJoueur2()) {
            assertFalse(vues.contains(c.getType()));
            vues.add(c.getType());
        }
    }

    @Test
    void cartesTireesDebut() {
        Jeu jeu;
        jeu = new Jeu();

        // taille = 2
        assertEquals(2, jeu.getCartesJoueur1().size());
        // taille = 2
        assertEquals(2, jeu.getCartesJoueur2().size());

        // toutes uniques
        HashSet<TYPECARTE> vues = new HashSet<>();

        vues.add(jeu.getCarteSupplementaire().getType());

        // joueur 1
        for (Carte c : jeu.getCartesJoueur1()) {
            assertFalse(vues.contains(c.getType()));
            vues.add(c.getType());
        }

        // joueur 2
        for (Carte c : jeu.getCartesJoueur2()) {
            assertFalse(vues.contains(c.getType()));
            vues.add(c.getType());
        }
    }

    @Test
    void scenarion1DebutPartie() {
        try {
            Jeu jeu;
            // on choisit un jeu avec des cartes arbitraires et des pions en positions initiale pour tester un scénario simplr
            jeu = new Jeu(CRABE, new ArrayList<>(Arrays.asList(BOEUF, MANTE)), new ArrayList<>(Arrays.asList(COQ, LAPIN)));

            /*
             * | E1 | E1 | M1 | E1 | E1 |
             * |    |    |    |    |    |
             * |    |    |    |    |    |
             * |    |    |    |    |    |
             * | E2 | E2 | M2 | E2 | E2 |
             */

            for (int i = 0; i < jeu.lignes(); i += 4) {
                for (int j = 0; j < jeu.colonnes(); j++) {
                    assertFalse(jeu.estCaseVide(i, j));
                    if (j == 2) {
                        assertEquals(PION_MAITRE, jeu.getRolePionAt(i, j));
                    } else {

                        assertEquals(PION_ETUDIANT, jeu.getRolePionAt(i, j));
                    }
                    assertEquals((i == 0 ? ID_JOUEUR_1 : ID_JOUEUR_2), jeu.getProprietairePionAt(i, j));
                }
            }

            System.err.println(jeu.toString());
            jeu.setCarteSelectionnee(0);
            jeu.setPionSelectionne(new Point(0, 2));
            jeu.jouerCoup(new Coup(new Point(0, 2), new Point(1, 2)));
            System.err.println(jeu.toString());

            jeu.setCarteSelectionnee(1);
            jeu.setPionSelectionne(new Point(4, 0));
            jeu.jouerCoup(new Coup(new Point(4, 0), new Point(3, 1)));
            System.err.println(jeu.toString());


            jeu.setCarteSelectionnee(0);
            jeu.selectionneCase(new Point(1, 2));
            System.err.println(jeu.toString());

            jeu.selectionneCase(new Point(2, 1));
            System.err.println(jeu.toString());

            jeu.setCarteSelectionnee(1);
            jeu.selectionneCase(new Point(3, 1));
            System.err.println(jeu.toString());
            jeu.selectionneCase(new Point(2, 1));
            System.err.println(jeu.toString());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}