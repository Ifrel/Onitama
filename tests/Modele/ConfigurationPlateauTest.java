package Modele;

import static Global.Config.TYPECARTE.*;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationPlateauTest {
    @Test
    void constructionDeBase() {

        assertThrows(NullPointerException.class, () -> {new ConfigurationPlateau(null);});
        assertThrows(NullPointerException.class, () -> {new ConfigurationPlateau(1, null, null, null, null, null);});
        assertThrows(NullPointerException.class, () -> {new ConfigurationPlateau(1, new Carte(COBRA), null, null, null, null);});
        List<Carte> lc1 = new ArrayList<>() {{
            add(new Carte(OIE));
            add(new Carte(COQ));
        }};
        assertThrows(NullPointerException.class, () -> {new ConfigurationPlateau(1, new Carte(BOEUF), lc1, null, null, null);});
        List<Carte> lc2 = new ArrayList<>() {{
            add(new Carte(CRABE));
            add(new Carte(SINGE));
        }};
        assertThrows(NullPointerException.class, () -> {new ConfigurationPlateau(1, new Carte(GRUE), lc1, lc2, null, null);});
        List<Pion> lp1 = new ArrayList<>() {{
            add(new PionMaitre(1, new Point(0, 2)));
            add(new PionEtudiant(1, new Point(0, 0)));
            add(new PionEtudiant(1, new Point(0, 1)));
            add(new PionEtudiant(1, new Point(0, 3)));
            add(new PionEtudiant(1, new Point(0, 4)));
        }};
        assertThrows(NullPointerException.class, () -> {new ConfigurationPlateau(1, new Carte(SANGLIER), lc1, lc2, lp1, null);});
        List<Pion> lp2 = new ArrayList<>() {{
            add(new PionMaitre(2, new Point(4, 2)));
            add(new PionEtudiant(2, new Point(4, 0)));
            add(new PionEtudiant(2, new Point(4, 1)));
            add(new PionEtudiant(2, new Point(4, 3)));
            add(new PionEtudiant(2, new Point(4, 4)));

        }};
        new ConfigurationPlateau(1, new Carte(LAPIN), lc1, lc2, lp1, lp2);
    }

    @Test
    void valeursCartesDebutPartie() {
        ConfigurationPlateau cp;
        byte [] etat;

        List<Carte> lc1 = new ArrayList<>() {{
            add(new Carte(OIE));
            add(new Carte(COQ));
        }};

        List<Carte> lc2 = new ArrayList<>() {{
            add(new Carte(CRABE));
            add(new Carte(SINGE));
        }};

        List<Pion> lp1 = new ArrayList<>() {{
            add(new PionMaitre(1, new Point(0, 2)));
            add(new PionEtudiant(1, new Point(0, 0)));
            add(new PionEtudiant(1, new Point(0, 1)));
            add(new PionEtudiant(1, new Point(0, 3)));
            add(new PionEtudiant(1, new Point(0, 4)));
        }};

        List<Pion> lp2 = new ArrayList<>() {{
            add(new PionMaitre(2, new Point(4, 2)));
            add(new PionEtudiant(2, new Point(4, 0)));
            add(new PionEtudiant(2, new Point(4, 1)));
            add(new PionEtudiant(2, new Point(4, 3)));
            add(new PionEtudiant(2, new Point(4, 4)));

        }};

        cp = new ConfigurationPlateau(1, new Carte(MANTE), lc1, lc2, lp1, lp2);

        etat = cp.getEtat();

        // OIE = 6; COQ = 7
        // 0110 0111 = 103
        assertEquals(103, etat[0]);

        // CRABE = 4; SINGE = 8
        // 0100 1000 = 72
        assertEquals(72, etat[1]);

        // MANTE = 9; 4 / 25 bits
        // 1001 1101 = 157
        assertEquals(157, etat[2]);

    }

    @Test
    void valeursVecteurDebutPartie() {
        ConfigurationPlateau cp;
        byte [] etat;

        List<Carte> lc1 = new ArrayList<>() {{
            add(new Carte(OIE));
            add(new Carte(COQ));
        }};

        List<Carte> lc2 = new ArrayList<>() {{
            add(new Carte(CRABE));
            add(new Carte(SINGE));
        }};

        List<Pion> lp1 = new ArrayList<>() {{
            add(new PionMaitre(1, new Point(0, 2)));
            add(new PionEtudiant(1, new Point(0, 0)));
            add(new PionEtudiant(1, new Point(0, 1)));
            add(new PionEtudiant(1, new Point(0, 3)));
            add(new PionEtudiant(1, new Point(0, 4)));
        }};

        List<Pion> lp2 = new ArrayList<>() {{
            add(new PionMaitre(2, new Point(4, 2)));
            add(new PionEtudiant(2, new Point(4, 0)));
            add(new PionEtudiant(2, new Point(4, 1)));
            add(new PionEtudiant(2, new Point(4, 3)));
            add(new PionEtudiant(2, new Point(4, 4)));

        }};

        cp = new ConfigurationPlateau(1, new Carte(MANTE), lc1, lc2, lp1, lp2);

        etat = cp.getEtat();

        // OIE = 6; COQ = 7
        // 0110 0111 = 103
        assertEquals(103, etat[0]);

        // CRABE = 4; SINGE = 8
        // 0100 1000 = 72
        assertEquals(72, etat[1]);

        // MANTE = 9; 4 / 25 bits
        // 1001 1101 = 157
        assertEquals(157, etat[2]);



        // ----------------------------------------
        // 1 00000 00 = 128
        assertEquals(0, etat[3]);

        // 000 00000
        assertEquals(0, etat[4]);

        // ----------------------------------------
        // 00000 110
        assertEquals(6, etat[5]);

        // 11 00000 0
        assertEquals(192, etat[6]);

        // 0000 0000
        assertEquals(0, etat[7]);


        // 0 00000 10
        assertEquals(2, etat[8]);

        // 000 00100
        assertEquals(4, etat[9]);

        // 00001 001
        assertEquals(9, etat[10]);

        // 00 000 000
        assertEquals(0, etat[11]);
    }
}
