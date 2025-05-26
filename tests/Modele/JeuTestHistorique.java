package Modele;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;


import Global.Config.ROLEPION;
import Global.Config.TYPECARTE;

public class JeuTestHistorique {
    @Test
    public void testVictoireParEliminationPionsJoueur1() {
        ArrayList<Pion> lp1 = new ArrayList<>();
        for(int j = 0; j<5;++j)
        {
            if (j == 2) {
                lp1.add(new Pion(1, new Point(0, j), ROLEPION.PION_MAITRE));
            } else {
                lp1.add(new Pion(1, new Point(0, j), ROLEPION.PION_ETUDIANT));
            }
        }
        ArrayList<Pion> lp2 = new ArrayList<>();
        Jeu jeu = new Jeu(lp1,lp2);
        assertTrue(jeu.verifierVictoire());
    }

    @Test
    //Meme idée, mais c'est l'inverse
    public void testVictoireParEliminationPionsJoueur2() {
        ArrayList<Pion> lp2 = new ArrayList<>();
        for(int j = 0; j<5;++j)
        {
            if (j == 2) {
                lp2.add(new Pion(2, new Point(4, j), ROLEPION.PION_MAITRE));
            } else {
                lp2.add(new Pion(2, new Point(4, j), ROLEPION.PION_ETUDIANT));
            }
        }
        ArrayList<Pion> lp1 = new ArrayList<>();
        Jeu jeu = new Jeu(lp1,lp2);
        //jeu.setPremierJoueur(2); Pas encore implementé
        assertTrue(jeu.verifierVictoire());
    }

    @Test
    //Tester avec un pion maitre mort
    public void testVictoireParMaitreMort() {
        TYPECARTE carteEnPlus = TYPECARTE.COQ;
        ArrayList<TYPECARTE> cJ1 = new ArrayList<>();
        cJ1.add(TYPECARTE.SANGLIER);
        cJ1.add(TYPECARTE.BOEUF);

        ArrayList<TYPECARTE> cJ2 = new ArrayList<>();
        cJ2.add(TYPECARTE.CHEVAL);
        cJ2.add(TYPECARTE.COBRA);

        ArrayList<Pion> lp2 = new ArrayList<>();

        for(int j = 0; j<5;++j)
        {
            if (j == 2) {
                lp2.add(new Pion(2, new Point(4, j), ROLEPION.PION_MAITRE));
            } else {
                lp2.add(new Pion(2, new Point(4, j), ROLEPION.PION_ETUDIANT));
            }
        }

        ArrayList<Pion> lp1 = new ArrayList<>();
        for(int j = 0; j<5;++j)
        {
            if (j == 2) {
                lp1.add(new Pion(1, new Point(0, j), ROLEPION.PION_MAITRE));
            } else if(j==1)
            {
                lp1.add(new Pion(1, new Point(3, 2), ROLEPION.PION_ETUDIANT));

            }
            else {
                lp1.add(new Pion(1, new Point(0, j), ROLEPION.PION_ETUDIANT));
            }
        }


        Jeu jeu = new Jeu(carteEnPlus, cJ1, cJ2, lp1, lp2);
        jeu.setCarteSelectionnee(0);
        jeu.setPionSelectionne(new Point(3, 2));
        Coup coup = new Coup(new Point(3, 2), new Point(4, 2), jeu.getCarteSupplementaire());
        jeu.jouerCoup(coup);
        assertTrue(jeu.verifierVictoire());
    }

    @Test
    //Tester si il y'a une victoire dans le cas où le pion 
    public void testVictoireParOccupationTemple2() {
        ArrayList<Pion> lp2 = new ArrayList<>();

        for(int j = 0; j<5;++j)
        {
            if (j == 2) {
                lp2.add(new Pion(2, new Point(3, j), ROLEPION.PION_MAITRE));
            } else {
                lp2.add(new Pion(2, new Point(4, j), ROLEPION.PION_ETUDIANT));
            }
        }

        ArrayList<Pion> lp1 = new ArrayList<>();
        for(int j = 0; j<5;++j)
        {
            if (j == 2) {
                lp1.add(new Pion(1, new Point(4, j), ROLEPION.PION_MAITRE));
            } else {
                lp1.add(new Pion(1, new Point(0, j), ROLEPION.PION_ETUDIANT));
            }
        }
        Jeu jeu = new Jeu(lp1,lp2);
        
        assertTrue(jeu.verifierVictoire());
    }

    @Test
    //Tester si il y'a une victoire dans le cas où le pion
    public void testVictoireParOccupationTemple1() {
        ArrayList<Pion> lp2 = new ArrayList<>();

        for(int j = 0; j<5;++j)
        {
            if (j == 2) {
                lp2.add(new Pion(2, new Point(0, j), ROLEPION.PION_MAITRE));
            } else {
                lp2.add(new Pion(2, new Point(4, j), ROLEPION.PION_ETUDIANT));
            }
        }

        ArrayList<Pion> lp1 = new ArrayList<>();
        for(int j = 0; j<5;++j)
        {
            if (j == 2) {
                lp1.add(new Pion(1, new Point(2, j), ROLEPION.PION_MAITRE));
            } else {
                lp1.add(new Pion(1, new Point(0, j), ROLEPION.PION_ETUDIANT));
            }
        }
        
        Jeu jeu = new Jeu(lp1,lp2);
        
        assertTrue(jeu.verifierVictoire());
    }

    @Test
    public void testSauvegarderEtChargerJeu() throws Exception {
        

        TYPECARTE carteEnPlus = TYPECARTE.COQ;
        ArrayList<TYPECARTE> cJ1 = new ArrayList<>();
        cJ1.add(TYPECARTE.SANGLIER);
        cJ1.add(TYPECARTE.BOEUF);

        ArrayList<TYPECARTE> cJ2 = new ArrayList<>();
        cJ2.add(TYPECARTE.CHEVAL);
        cJ2.add(TYPECARTE.COBRA);

        ArrayList<Pion> lp2 = new ArrayList<>();

        for(int j = 0; j<5;++j)
        {
            if (j == 2) {
                lp2.add(new Pion(2, new Point(4, j), ROLEPION.PION_MAITRE));
            } else {
                lp2.add(new Pion(2, new Point(4, j), ROLEPION.PION_ETUDIANT));
            }
        }

        ArrayList<Pion> lp1 = new ArrayList<>();
        for(int j = 0; j<5;++j)
        {
            if (j == 2) {
                lp1.add(new Pion(1, new Point(0, j), ROLEPION.PION_MAITRE));
            } else {
                lp1.add(new Pion(1, new Point(0, j), ROLEPION.PION_ETUDIANT));
            }
        }


        Jeu jeuOriginal = new Jeu(carteEnPlus, cJ1, cJ2, lp1, lp2);
        //Modification de l'etat du jeu pour voir si vraiment le jeu sauvegarde et charge
        jeuOriginal.setCarteSelectionnee(0);
        jeuOriginal.setPionSelectionne(new Point(0, 1));
        Coup coup = new Coup(new Point(0, 1), new Point(1, 1), jeuOriginal.getCarteSupplementaire());
        jeuOriginal.jouerCoup(coup);
        Path tempFile = Files.createTempFile(Paths.get("res/fichier_de_sauvegarde"), "fich1", ".txt");
        jeuOriginal.sauvegarderJeu(tempFile.toString());
        Jeu jeuCharge = new Jeu();
        jeuCharge.chargerJeu(tempFile.toString());

        // Assurer que tout est le meme
        assertEquals(jeuOriginal.getIdJoueurCourant(), jeuCharge.getIdJoueurCourant());
        assertEquals(jeuOriginal.getPionsJoueur1(), jeuCharge.getPionsJoueur1());
        assertEquals(jeuOriginal.getPionsJoueur2(), jeuCharge.getPionsJoueur2());
        assertEquals(jeuOriginal.getCarteSupplementaire(), jeuCharge.getCarteSupplementaire());
        assertEquals(jeuOriginal.getCartesJoueur1(), jeuCharge.getCartesJoueur1());
        assertEquals(jeuOriginal.getCartesJoueur2(), jeuCharge.getCartesJoueur2());
    }


    @Test
    public void testRefaireCoup() {
        TYPECARTE carteEnPlus = TYPECARTE.COQ;
        ArrayList<TYPECARTE> cJ1 = new ArrayList<>();
        cJ1.add(TYPECARTE.SANGLIER);
        cJ1.add(TYPECARTE.BOEUF);

        ArrayList<TYPECARTE> cJ2 = new ArrayList<>();
        cJ2.add(TYPECARTE.CHEVAL);
        cJ2.add(TYPECARTE.COBRA);

        ArrayList<Pion> lp2 = new ArrayList<>();

        for(int j = 0; j<5;++j)
        {
            if (j == 2) {
                lp2.add(new Pion(2, new Point(4, j), ROLEPION.PION_MAITRE));
            } else {
                lp2.add(new Pion(2, new Point(4, j), ROLEPION.PION_ETUDIANT));
            }
        }

        ArrayList<Pion> lp1 = new ArrayList<>();
        for(int j = 0; j<5;++j)
        {
            if (j == 2) {
                lp1.add(new Pion(1, new Point(0, j), ROLEPION.PION_MAITRE));
            } else {
                lp1.add(new Pion(1, new Point(0, j), ROLEPION.PION_ETUDIANT));
            }
        }


        Jeu jeu = new Jeu(carteEnPlus, cJ1, cJ2, lp1, lp2);
        jeu.setPionSelectionne(new Point(0, 1));
        //Initialisation: On creé un coup
        Coup coup = new Coup(new Point(0, 1), new Point(1, 1), jeu.getCarteSupplementaire());
        jeu.jouerCoup(coup);
        //On créee le coup qu'on vient de faire et on l'ajoute à l'historique
        //On annule et on refait
        jeu.annulerCoup();
        jeu.refaireCoup();
        //On test si tout va bien
        //Pion p = jeu.getCase(1, 1);
        //assertNotNull(p);
    }



    
}