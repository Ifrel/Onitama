package tests;
import static org.junit.Assert.*;
import org.junit.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import Global.Config.ROLEPION;
import Modele.Coup;
import Modele.Jeu;
import Modele.Pion;
import Modele.Position;
import Modele.Onitama;

public class Test {
    @Test
    public void testVictoireParEliminationPionsJoueur1() {
        Jeu jeu = new Jeu();
        Setup: joueur1 n'a pas de pions, c'est le tour de joueur2
        jeu.setPionsJoueur1(Collections.emptyList());
        jeu.setIdJoueurCourant(Onitama.ID_JOUEUR_2);
        assertTrue(jeu.verifierVictoire());
    }

    @Test
    //Meme idée, mais c'est l'inverse
    public void testVictoireParEliminationPionsJoueur2() {
        Jeu jeu = new Jeu();
        jeu.setPionsJoueur2(Collections.emptyList());
        jeu.setIdJoueurCourant(Onitama.ID_JOUEUR_1);
        assertTrue(jeu.verifierVictoire());
    }

    @Test
    //Tester avec un pion maitre mort
    public void testVictoireParMaitreMort() {
        Jeu jeu = new Jeu();
        jeu.setMaitreMort(true);
        assertTrue(jeu.verifierVictoire());
    }

    @Test
    //Tester si il y'a une victoire dans le cas où le pion
    public void testVictoireParOccupationTemple2() {
        Jeu jeu = new Jeu();
        jeu.setCase(4, 2, null);
        Pion p = new Pion(1, new Position(4, 2), ROLEPION.PION_MAITRE);
        jeu.setCase(4, 2, p);
        assertTrue(jeu.verifierVictoire());
    }

    @Test
    //Tester si il y'a une victoire dans le cas où le pion
    public void testVictoireParOccupationTemple1() {
        Jeu jeu = new Jeu();
        jeu.setCase(0, 2, null);
        Pion p = new Pion(2, new Position(0, 2), ROLEPION.PION_MAITRE);
        jeu.setCase(0, 2, p);
        assertTrue(jeu.verifierVictoire());
    }

    @Test
    public void testSauvegarderEtChargerJeu() throws Exception {
        Jeu jeuOriginal = new Jeu();
        //Modification de l'etat du jeu pour voir si vraiment le jeu sauvegarde et charge
        Jeu jeu = new Jeu();
        jeu.setCase(0, 2, null);
        Pion p = new Pion(2, new Position(0, 2), ROLEPION.PION_MAITRE);
        jeu.setCase(0, 2, p);
        Path tempFile = Files.createTempFile("src/res", "fich1.txt");
        jeuOriginal.sauvegarderJeu(tempFile);
        Jeu jeuCharge = new Jeu();
        jeuCharge.chargerJeu(tempFile);

        // Assurer que tout est le meme (surtout la grille!)
        assertEquals(jeuOriginal.getIdJoueurCourant(), jeuCharge.getIdJoueurCourant());
        assertArrayEquals(jeuOriginal.getGrille(), jeuCharge.getGrille());
        assertEquals(jeuOriginal.getCarteEchange(), jeuCharge.getCarteEchange());
    }


    @Test
    public void testRefaireCoup() {
        Jeu jeu = new Jeu();
        Initialisation: On creé un coup
        Position depart = new Position(2, 0);
        Position arrivee = new Position(3, 1);
        Carte carte = jeu.getCarteEchange();
        //On deplace un pion
        jeu.deplacerPion(depart, arrivee);
        //Et l'echange de carte
        jeu.echangerCartes(jeu.getJoueurCourant(), carte);
        jeu.changerJoueur();
        //On créee le coup qu'on vient de faire et on l'ajoute à l'historique
        jeu.getHistorique().ajouterCoup(new Coup(depart, arrivee, carte));
        //On annule et on refait
        jeu.annulerCoup();
        jeu.refaireCoup();
        //On test si tout va bien
        Pion p = jeu.getCase(3, 1);
        assertNotNull(p);
        assertEquals(depart, jeu.getHistorique().dernierCoup().getDepart());
    }



    
}


