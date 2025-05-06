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
        assertEquals(ia.getNiveau(), FAIBLE);
        ia = new IA(jeu, MOYEN);
        assertEquals(ia.getNiveau(), MOYEN);
        ia = new IA(jeu, FORT);
        assertEquals(ia.getNiveau(), FORT);

        // Avec des changements via la méthode setNiveau()
        ia = new IA(jeu, FAIBLE);

        ia.setNiveau(FAIBLE);
        assertEquals(ia.getNiveau(), FAIBLE);
        ia.setNiveau(MOYEN);
        assertEquals(ia.getNiveau(), MOYEN);
        ia.setNiveau(FORT);
        assertEquals(ia.getNiveau(), FORT);

    }

    @Test
    void faibleRandom() {
        // teste le fait que l'IA joue bien au moins un coup aléatoire si un coup et possible
        Jeu jeu;
        IA ia;

        // test à implémenter
        assertTrue(false);

    }
}
