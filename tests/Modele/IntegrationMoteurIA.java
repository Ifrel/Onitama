package Modele;

import static Global.Config.NIVEAU_IA.*;
        import org.junit.jupiter.api.Test;

public class IntegrationMoteurIA {

    @Test
    void scenario1() {
        Jeu jeu;
        jeu = new Jeu();
        Thread t1 = new Thread(jeu);
        t1.start();

        jeu.toggleIA1();
        jeu.toggleIA2();
        jeu.setNiveauIA1(FAIBLE);
        jeu.setNiveauIA2(FAIBLE);
        jeu.toggleIAvsIA();
    }

}
