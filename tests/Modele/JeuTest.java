package Modele;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;

import static Global.Config.TYPECARTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
}