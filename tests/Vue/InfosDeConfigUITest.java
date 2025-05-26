package Vue;

import Vue.Configuration.InfosDeConfigUI;
import org.junit.jupiter.api.Test;

import static Global.Config.ID_JOUEUR_1;
import static org.junit.jupiter.api.Assertions.*;

class InfosDeConfigUITest {

    @Test
    void testGetNomCouleurPionJoueurValidId() {
        InfosDeConfigUI configUI = InfosDeConfigUI.getInstance();

        // Verify that a valid player returns the correct color name
        String couleurJoueur1 = configUI.getNomCouleurPionJoueur(ID_JOUEUR_1);
        assertNotNull(couleurJoueur1, "La couleur pour le joueur 1 ne devrait pas être null");
    }

    @Test
    void testGetNomCouleurPionJoueurInvalidId() {
        InfosDeConfigUI configUI = InfosDeConfigUI.getInstance();

        // Verify that an invalid player ID throws an exception
        assertThrows(IllegalArgumentException.class,
                () -> configUI.getNomCouleurPionJoueur(-1),
                "Un ID de joueur invalide devrait lancer une exception IllegalArgumentException");
    }

    /**
     * Tests for the InfosDeConfigUI class.
     * This class serves as a singleton that stores information regarding the configuration UI, such as player colors.
     * The method getInstance() is used to retrieve the singleton instance of the class.
     */

    @Test
    void testGetInstanceReturnsSameInstance() {
        InfosDeConfigUI firstInstance = InfosDeConfigUI.getInstance();
        InfosDeConfigUI secondInstance = InfosDeConfigUI.getInstance();

        // Assert that both instances are the same
        assertSame(firstInstance, secondInstance, "getInstance should always return the same instance");
    }

    @Test
    void testGetInstanceThreadSafety() throws InterruptedException {
        final InfosDeConfigUI[] instances = new InfosDeConfigUI[10];

        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            final int index = i;
            threads[i] = new Thread(() -> instances[index] = InfosDeConfigUI.getInstance());
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // Assert that all threads returned the same instance
        for (int i = 1; i < instances.length; i++) {
            assertSame(instances[0], instances[i], "getInstance should return the same instance across all threads");
        }
    }

    @Test
    void testGetInstanceNotNull() {
        InfosDeConfigUI instance = InfosDeConfigUI.getInstance();

        // Assert that the instance is not null
        assertNotNull(instance, "getInstance should not return null");
    }
}