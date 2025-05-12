package Modele;

import static Global.Config.TYPECARTE.*;
import static Global.Config.ROLEPION.*;


import Global.Config;
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
        List<Modele.Pion> lp1 = new ArrayList<>() {{
            add(new Modele.Pion(1, new Point(0, 2), PION_MAITRE));
            add(new Modele.Pion(1, new Point(0, 0), PION_ETUDIANT));
            add(new Modele.Pion(1, new Point(0, 1), PION_ETUDIANT));
            add(new Modele.Pion(1, new Point(0, 3), PION_ETUDIANT));
            add(new Modele.Pion(1, new Point(0, 4), PION_ETUDIANT));
        }};
        assertThrows(NullPointerException.class, () -> {new ConfigurationPlateau(1, new Carte(SANGLIER), lc1, lc2, lp1, null);});
        List<Modele.Pion> lp2 = new ArrayList<>() {{
            add(new Modele.Pion(2, new Point(4, 2), PION_MAITRE));
            add(new Modele.Pion(2, new Point(4, 0), PION_ETUDIANT));
            add(new Modele.Pion(2, new Point(4, 1), PION_ETUDIANT));
            add(new Modele.Pion(2, new Point(4, 3), PION_ETUDIANT));
            add(new Modele.Pion(2, new Point(4, 4), PION_ETUDIANT));

        }};
        new ConfigurationPlateau(1, new Carte(LAPIN), lc1, lc2, lp1, lp2);
    }

    @Test
    void valeursCartesJ1DebutPartie() {
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

        List<Modele.Pion> lp1 = new ArrayList<>() {{
            add(new Modele.Pion(1, new Point(0, 2), PION_MAITRE));
            add(new Pion(1, new Point(0, 0), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 1), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 3), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 4), PION_ETUDIANT));
        }};

        List<Modele.Pion> lp2 = new ArrayList<>() {{
            add(new Modele.Pion(2, new Point(4, 2), PION_MAITRE));
            add(new Pion(2, new Point(4, 0), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 1), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 3), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 4), PION_ETUDIANT));

        }};

        cp = new ConfigurationPlateau(1, new Carte(MANTE), lc1, lc2, lp1, lp2);

        etat = cp.getEtat();

        // OIE = 6; COQ = 7
        // 0110 0111 = 103
        assertEquals(103, etat[0]);
    }

    @Test
    void valeursCartesJ2DebutPartie() {
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

        List<Modele.Pion> lp1 = new ArrayList<>() {{
            add(new Modele.Pion(1, new Point(0, 2), PION_MAITRE));
            add(new Pion(1, new Point(0, 0), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 1), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 3), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 4), PION_ETUDIANT));
        }};

        List<Modele.Pion> lp2 = new ArrayList<>() {{
            add(new Modele.Pion(2, new Point(4, 2), PION_MAITRE));
            add(new Pion(2, new Point(4, 0), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 1), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 3), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 4), PION_ETUDIANT));

        }};

        cp = new ConfigurationPlateau(1, new Carte(MANTE), lc1, lc2, lp1, lp2);

        etat = cp.getEtat();

        // CRABE = 4; SINGE = 8
        // 0100 1000 = 72
        assertEquals(72, etat[1]);
    }

    @Test
    void valeursCarteEnPlusDebutPartie() {
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

        List<Modele.Pion> lp1 = new ArrayList<>() {{
            add(new Modele.Pion(1, new Point(0, 2), PION_MAITRE));
            add(new Pion(1, new Point(0, 0), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 1), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 3), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 4), PION_ETUDIANT));
        }};

        List<Modele.Pion> lp2 = new ArrayList<>() {{
            add(new Modele.Pion(2, new Point(4, 2), PION_MAITRE));
            add(new Pion(2, new Point(4, 0), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 1), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 3), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 4), PION_ETUDIANT));

        }};

        cp = new ConfigurationPlateau(1, new Carte(MANTE), lc1, lc2, lp1, lp2);

        etat = cp.getEtat();

        // MANTE = 9; 1-4 / 25 bits (Positions Pions Joueur 1)
        // 1001 1101 = 157 = -99
        assertEquals(-99, etat[2]);

    }

    @Test
    void valeursPionsJ1DebutPartie() {
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

        List<Modele.Pion> lp1 = new ArrayList<>() {{
            add(new Modele.Pion(1, new Point(0, 2), PION_MAITRE));
            add(new Modele.Pion(1, new Point(0, 0), PION_ETUDIANT));
            add(new Modele.Pion(1, new Point(0, 1), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 3), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 4), PION_ETUDIANT));
        }};

        List<Modele.Pion> lp2 = new ArrayList<>() {{
            add(new Modele.Pion(2, new Point(4, 2), PION_MAITRE));
            add(new Pion(2, new Point(4, 0), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 1), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 3), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 4), PION_ETUDIANT));

        }};

        cp = new ConfigurationPlateau(1, new Carte(MANTE), lc1, lc2, lp1, lp2);

        etat = cp.getEtat();

        // MANTE = 9; 1-4 / 25 bits (Positions Pions Joueur 1)
        // 1001 1101 = 157 = -99
        assertEquals(-99, etat[2]);



        // -------------- 5-12 / 25 bits (Positions Pions Joueur 1)
        // 1 00000 00 = 128 = -128
        assertEquals(-128, etat[3]);


        // -------------- 13-20 / 25 bits (Positions Pions Joueur 1)
        // 000 00000
        assertEquals(0, etat[4]);


        // -------------- 21-25 / 25 bits (Positions Pions Joueur 1)
        // -------------- 1-3 / 25 bits (Positions Pions Joueur 2)
        // 00000 000
        assertEquals(0, etat[5]);
    }

    @Test
    void valeursPionsJ2DebutPartie() {
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

        List<Modele.Pion> lp1 = new ArrayList<>() {{
            add(new Modele.Pion(1, new Point(0, 2), PION_MAITRE));
            add(new Pion(1, new Point(0, 0), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 1), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 3), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 4), PION_ETUDIANT));
        }};

        List<Modele.Pion> lp2 = new ArrayList<>() {{
            add(new Modele.Pion(2, new Point(4, 2), PION_MAITRE));
            add(new Pion(2, new Point(4, 0), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 1), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 3), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 4), PION_ETUDIANT));

        }};

        cp = new ConfigurationPlateau(1, new Carte(MANTE), lc1, lc2, lp1, lp2);

        etat = cp.getEtat();

        // -------------- 21-25 / 25 bits (Positions Pions Joueur 1)
        // -------------- 1-3 / 25 bits (Positions Pions Joueur 2)
        // 00000 OO0
        assertEquals(0, etat[5]);

        // -------------- 4-11 / 25 bits (Positions Pions Joueur 2)
        // 00 00000 0
        assertEquals(0, etat[6]);

        // -------------- 12-19 / 25 bits (Positions Pions Joueur 2)
        // 0000 0000
        assertEquals(0, etat[7]);


        // -------------- 20-25 / 25 bits (Positions Pions Joueur 2)
        // -------------- 1-2 / 5 bits (Positions Ligne Pion Maitre Joueur 1)
        // 0 11011 10 = 110
        assertEquals(110, etat[8]);
    }

    @Test
    void valeursPionMaitreJ1DebutPartie() {
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

        List<Modele.Pion> lp1 = new ArrayList<>() {{
            add(new Modele.Pion(1, new Point(0, 2), PION_MAITRE));
            add(new Pion(1, new Point(0, 0), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 1), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 3), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 4), PION_ETUDIANT));
        }};

        List<Modele.Pion> lp2 = new ArrayList<>() {{
            add(new Modele.Pion(2, new Point(4, 2), PION_MAITRE));
            add(new Pion(2, new Point(4, 0), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 1), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 3), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 4), PION_ETUDIANT));

        }};

        cp = new ConfigurationPlateau(1, new Carte(MANTE), lc1, lc2, lp1, lp2);

        etat = cp.getEtat();

        // -------------- 20-25 / 25 bits (Positions Pions Joueur 2)
        // -------------- 1-2 / 5 bits (Positions Ligne Pion Maitre Joueur 1)
        // 0 11011 10 = 110
        assertEquals(110, etat[8]);

        // -------------- 3-5 / 5 bits (Positions Ligne Pion Maitre Joueur 1)
        // -------------- 1-5 / 5 bits (Positions Colonne Pion Maitre Joueur 1)
        // 000 00100
        assertEquals(4, etat[9]);
    }

    @Test
    void valeursPionMaitreJ2DebutPartie() {
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

        List<Modele.Pion> lp1 = new ArrayList<>() {{
            add(new Modele.Pion(1, new Point(0, 2), PION_MAITRE));
            add(new Pion(1, new Point(0, 0), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 1), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 3), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 4), PION_ETUDIANT));
        }};

        List<Modele.Pion> lp2 = new ArrayList<>() {{
            add(new Modele.Pion(2, new Point(4, 2), PION_MAITRE));
            add(new Pion(2, new Point(4, 0), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 1), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 3), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 4), PION_ETUDIANT));

        }};

        cp = new ConfigurationPlateau(1, new Carte(MANTE), lc1, lc2, lp1, lp2);

        etat = cp.getEtat();

        // -------------- 1-5 / 5 bits (Positions Ligne Pion Maitre Joueur 2)
        // -------------- 1-3 / 5 bits (Positions Colonne Pion Maitre Joueur 2)
        // 00001 001
        assertEquals(9, etat[10]);

        // -------------- 4-5 / 5 bits (Positions Colonne Pion Maitre Joueur 2)
        // -------------- 1-6 / 6 bits inutilisés
        // 00 000 000
        assertEquals(0, etat[11]);
    }

    @Test
    void valeursVecteurTotalDebutPartie() {
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

        List<Modele.Pion> lp1 = new ArrayList<>() {{
            add(new Modele.Pion(1, new Point(0, 2), PION_MAITRE));
            add(new Pion(1, new Point(0, 0), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 1), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 3), PION_ETUDIANT));
            add(new Pion(1, new Point(0, 4), PION_ETUDIANT));
        }};

        List<Modele.Pion> lp2 = new ArrayList<>() {{
            add(new Modele.Pion(2, new Point(4, 2), PION_MAITRE));
            add(new Pion(2, new Point(4, 0), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 1), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 3), PION_ETUDIANT));
            add(new Pion(2, new Point(4, 4), PION_ETUDIANT));

        }};

        cp = new ConfigurationPlateau(1, new Carte(MANTE), lc1, lc2, lp1, lp2);

        etat = cp.getEtat();

        // OIE = 6; COQ = 7
        // 0110 0111 = 103
        assertEquals(103, etat[0]);

        // CRABE = 4; SINGE = 8
        // 0100 1000 = 72
        assertEquals(72, etat[1]);

        // MANTE = 9; 1-4 / 25 bits (Positions Pions Joueur 1)
        // 1001 1101 = 157 = -99
        assertEquals(-99, etat[2]);



        // -------------- 5-12 / 25 bits (Positions Pions Joueur 1)
        // 1 00000 00 = 128 = -128
        assertEquals(-128, etat[3]);


        // -------------- 13-20 / 25 bits (Positions Pions Joueur 1)
        // 000 00000
        assertEquals(0, etat[4]);


        // -------------- 21-25 / 25 bits (Positions Pions Joueur 1)
        // -------------- 1-3 / 25 bits (Positions Pions Joueur 2)
        // 00000 OO0
        assertEquals(0, etat[5]);

        // -------------- 4-11 / 25 bits (Positions Pions Joueur 2)
        // 00 00000 0
        assertEquals(0, etat[6]);

        // -------------- 12-19 / 25 bits (Positions Pions Joueur 2)
        // 0000 0000
        assertEquals(0, etat[7]);


        // -------------- 20-25 / 25 bits (Positions Pions Joueur 2)
        // -------------- 1-2 / 5 bits (Positions Ligne Pion Maitre Joueur 1)
        // 0 11011 10 = 110
        assertEquals(110, etat[8]);

        // -------------- 3-5 / 5 bits (Positions Ligne Pion Maitre Joueur 1)
        // -------------- 1-5 / 5 bits (Positions Colonne Pion Maitre Joueur 1)
        // 000 00100
        assertEquals(4, etat[9]);

        // -------------- 1-5 / 5 bits (Positions Ligne Pion Maitre Joueur 2)
        // -------------- 1-3 / 5 bits (Positions Colonne Pion Maitre Joueur 2)
        // 00001 001
        assertEquals(9, etat[10]);

        // -------------- 4-5 / 5 bits (Positions Colonne Pion Maitre Joueur 2)
        // -------------- 1-6 / 6 bits inutilisés
        // 00 000 000
        assertEquals(0, etat[11]);
    }

    @Test
    void valeurVecteurTotalScenario1() {
        ConfigurationPlateau cp;

        byte [] etat;

        List<Carte> lc1 = new ArrayList<>() {{
            add(new Carte(SANGLIER));
            add(new Carte(GRENOUILLE));
        }};

        List<Carte> lc2 = new ArrayList<>() {{
            add(new Carte(BOEUF));
            add(new Carte(GRUE));
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

        cp = new ConfigurationPlateau(1, new Carte(COQ), lc1, lc2, lp1, lp2);

        etat = cp.getEtat();

        // SANGLIER = 13; GRENOUILLE = 2
        // 1101 0010 = 210 = -46
        assertEquals(-46, etat[0]);

        // BOEUF = 11; GRUE = 12
        // 1011 1100 = 188 = -68
        assertEquals(-68, etat[1]);

        // COQ = 7; 1-4 / 25 bits (Positions Pions Joueur 1)
        // 0111 1000 = 120
        assertEquals(120, etat[2]);



        // -------------- 5-12 / 25 bits (Positions Pions Joueur 1)
        // 1 00100 00 = 144 = -112
        assertEquals(-112, etat[3]);


        // -------------- 13-20 / 25 bits (Positions Pions Joueur 1)
        // 100 00000 = 128 = -128
        assertEquals(-128, etat[4]);


        // -------------- 21-25 / 25 bits (Positions Pions Joueur 1)
        // -------------- 1-3 / 25 bits (Positions Pions Joueur 2)
        // 00000 O10
        assertEquals(2, etat[5]);

        // -------------- 4-11 / 25 bits (Positions Pions Joueur 2)
        // 00 01000 0 = 16
        assertEquals(16, etat[6]);

        // -------------- 12-19 / 25 bits (Positions Pions Joueur 2)
        // 0100 0010 = 66
        assertEquals(66, etat[7]);


        // -------------- 20-25 / 25 bits (Positions Pions Joueur 2)
        // -------------- 1-2 / 5 bits (Positions Ligne Pion Maitre Joueur 1)
        // 0 00000 00
        assertEquals(0, etat[8]);

        // -------------- 3-5 / 5 bits (Positions Ligne Pion Maitre Joueur 1)
        // -------------- 1-5 / 5 bits (Positions Colonne Pion Maitre Joueur 1)
        // 100 00001 = 129 = -127
        assertEquals(-127, etat[9]);

        // -------------- 1-5 / 5 bits (Positions Ligne Pion Maitre Joueur 2)
        // -------------- 1-3 / 5 bits (Positions Colonne Pion Maitre Joueur 2)
        // 01001 100 = 76
        assertEquals(76, etat[10]);

        // -------------- 4-5 / 5 bits (Positions Colonne Pion Maitre Joueur 2)
        // -------------- 1-6 / 6 bits inutilisés
        // 00 000 000
        assertEquals(0, etat[11]);
    }
}
