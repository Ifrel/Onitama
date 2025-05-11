package Modele;

import static Global.Config.NIVEAU_IA.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IATest {

    @Test
    void fonctionnementDeBase() {
        Jeu jeu;
        IA ia;
        // L'IA a bien le niveau souhaité

        // Dès l'instanciation.
        jeu = new Jeu();
        ia = new IA(jeu, FAIBLE);
        assertEquals(FAIBLE, ia.getNiveau());
        ia = new IA(jeu, MOYEN);
        assertEquals(MOYEN, ia.getNiveau());
        ia = new IA(jeu, FORT);
        assertEquals(FORT, ia.getNiveau());

        // Avec des changements via la méthode setNiveau()
        ia = new IA(jeu, FAIBLE);

        ia.setNiveau(FAIBLE);
        assertEquals(FAIBLE, ia.getNiveau());
        ia.setNiveau(MOYEN);
        assertEquals(MOYEN, ia.getNiveau());
        ia.setNiveau(FORT);
        assertEquals(FORT, ia.getNiveau());

    }

//    @Test
//    void faibleRandom() {
//        // teste le fait que l'IA joue bien au moins un coup aléatoire si un coup et possible
//        Jeu jeu;
//        IA ia;
//
//        // test à implémenter
//        assertTrue(false);
//
//    }
}
