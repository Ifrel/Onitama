package Modele;

import Modele.IA.IA;
import Modele.IA.IAFaible;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IAFaibleTest {

    @Test
    void auMoinsUnCoupDebut() {
        Jeu jeu;
        IA ia;
        Coup c;

        for (int i = 0; i < 100; i++) {
            jeu = new Jeu();
            ia = new IAFaible(jeu);
            c = ia.calculerCoup();
            assertNotNull(c);
        }
    }
}
