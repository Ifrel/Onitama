package Vue;

import Exceptions.CaseVideException;
import Modele.Carte;
import Modele.CasePlateau;
import Modele.CasePlateau.TYPE_ELEMENT_SUR_CASE;
import Modele.Jeu;
import Modele.Joueur;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static Global.Config.ID_JOUEUR_1;
import static Global.Config.ID_JOUEUR_2;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@DisplayName("InterfaceTextuelle Class Tests")
public class InterfaceTextuelleTest {

    private Jeu mockJeu;
    private CollecteurEvenements mockCollecteurEv;
    private ByteArrayOutputStream outputStreamCaptor;
    private InputStream originalSystemIn;
    private PrintStream originalSystemOut;

    @BeforeEach
    void setUp() throws CaseVideException {
        mockJeu = mock(Jeu.class);
        mockCollecteurEv = mock(CollecteurEvenements.class);

        // Capture System.out
        outputStreamCaptor = new ByteArrayOutputStream();
        originalSystemOut = System.out;
        System.setOut(new PrintStream(outputStreamCaptor));
        originalSystemIn = System.in;

        // Mocks pour Jeu
        when(mockJeu.getNumeroRound()).thenReturn(1);
        when(mockJeu.getTempsDeJeu()).thenReturn(65L); // 1 min 5 sec
        when(mockJeu.getJoueurCourant()).thenReturn(new Joueur(ID_JOUEUR_1, "Joueur 1"));
        when(mockJeu.getNomJoueur1()).thenReturn("Joueur 1");
        when(mockJeu.getNomJoueur2()).thenReturn("Joueur 2");
        when(mockJeu.estPartieFinie()).thenReturn(false);
        when(mockJeu.peutAnnulerCoup()).thenReturn(true);
        when(mockJeu.peutRefaireCoup()).thenReturn(false);

        // Plateau simulé
        final CasePlateau[][] plateauSimule = new CasePlateau[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                CasePlateau mockCase = mock(CasePlateau.class);
                when(mockCase.getTypeElement()).thenReturn(TYPE_ELEMENT_SUR_CASE.VIDE);
                when(mockCase.getProprietaire()).thenReturn(0);
                plateauSimule[i][j] = mockCase;
            }
        }
        // PION_ETUDIANT (0,0)
        CasePlateau mockPionEtudiant = mock(CasePlateau.class);
        when(mockPionEtudiant.getTypeElement()).thenReturn(TYPE_ELEMENT_SUR_CASE.PION_ETUDIANT);
        when(mockPionEtudiant.getProprietaire()).thenReturn(ID_JOUEUR_1);
        plateauSimule[0][0] = mockPionEtudiant;
        // PION_MAITRE (4,4)
        CasePlateau mockPionMaitre = mock(CasePlateau.class);
        when(mockPionMaitre.getTypeElement()).thenReturn(TYPE_ELEMENT_SUR_CASE.PION_MAITRE);
        when(mockPionMaitre.getProprietaire()).thenReturn(ID_JOUEUR_2);
        plateauSimule[4][4] = mockPionMaitre;

        when(mockJeu.getCasePlateau(anyInt(), anyInt())).thenAnswer(invocation -> {
            int row = invocation.getArgument(0);
            int col = invocation.getArgument(1);
            if (row >= 0 && row < 5 && col >= 0 && col < 5) {
                return plateauSimule[row][col];
            }
            CasePlateau defaultMockCase = mock(CasePlateau.class);
            when(defaultMockCase.getTypeElement()).thenReturn(TYPE_ELEMENT_SUR_CASE.VIDE);
            when(defaultMockCase.getProprietaire()).thenReturn(0);
            return defaultMockCase;
        });

        // Cartes
        Carte mockCarte1 = mock(Carte.class);
        when(mockCarte1.getNom()).thenReturn("Carte A");
        Carte mockCarte2 = mock(Carte.class);
        when(mockCarte2.getNom()).thenReturn("Carte B");
        Carte mockCarte3 = mock(Carte.class);
        when(mockCarte3.getNom()).thenReturn("Carte C");
        Carte mockCarte4 = mock(Carte.class);
        when(mockCarte4.getNom()).thenReturn("Carte D");
        Carte mockCarte5 = mock(Carte.class);
        when(mockCarte5.getNom()).thenReturn("Carte E");

        when(mockJeu.getCartesJoueur1()).thenReturn(Arrays.asList(mockCarte1, mockCarte2));
        when(mockJeu.getCartesJoueur2()).thenReturn(Arrays.asList(mockCarte3, mockCarte4));
        when(mockJeu.getCarteSupplementaire()).thenReturn(mockCarte5);

        when(mockJeu.getCartesSurLeTerrain(0)).thenReturn(mockCarte1);
        when(mockJeu.getCartesSurLeTerrain(1)).thenReturn(mockCarte2);
        when(mockJeu.getCartesSurLeTerrain(2)).thenReturn(mockCarte3);
        when(mockJeu.getCartesSurLeTerrain(3)).thenReturn(mockCarte4);
        when(mockJeu.getCartesSurLeTerrain(4)).thenReturn(mockCarte5);
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
    }

    // Helpers
    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access private field " + fieldName + ": " + e.getMessage());
            return null;
        }
    }

    private void setPrivateField(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set private field " + fieldName + ": " + e.getMessage());
        }
    }

    @Test
    @DisplayName("InterfaceTextuelle initializes correctly and adds itself as observer")
    void testConstructorAndObserverAddition() {
        InterfaceTextuelle interfaceTextuelle = new InterfaceTextuelle(mockJeu, mockCollecteurEv, true);
        verify(mockJeu).ajouteObservateur(interfaceTextuelle);
        assertFalse((boolean) getPrivateField(interfaceTextuelle, "jeuTermine"));
    }

    @Test
    @DisplayName("miseAJour calls rafraichirAffichage and updates game state")
    void testMiseAJour() {
        InterfaceTextuelle interfaceTextuelle = Mockito.spy(new InterfaceTextuelle(mockJeu, mockCollecteurEv, true));
        interfaceTextuelle.miseAJour();
        verify(interfaceTextuelle, atLeastOnce()).rafraichirAffichage();
        verify(mockJeu).estPartieFinie();
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("--- Informations de la Partie ---"));
        assertTrue(output.contains("--- Plateau de Jeu ---"));
        assertTrue(output.contains("--- Cartes du jeu ---"));
    }

    @Test
    @DisplayName("miseAJour updates jeuTermine correctly when game is not finished")
    void testMiseAJour_GameStateUpdatedAfterRefresh() {
        when(mockJeu.estPartieFinie()).thenReturn(false);

        InterfaceTextuelle interfaceTextuelle = new InterfaceTextuelle(mockJeu, mockCollecteurEv, true);
        assertFalse((boolean) getPrivateField(interfaceTextuelle, "jeuTermine")); // Verify initial state

        interfaceTextuelle.miseAJour();

        assertFalse((boolean) getPrivateField(interfaceTextuelle, "jeuTermine")); // Verify game is not marked as finished
    }

    @Test
    @DisplayName("miseAJour updates jeuTermine correctly when game is finished")
    void testMiseAJour_GameStateUpdatedWhenGameIsFinished() {
        when(mockJeu.estPartieFinie()).thenReturn(true);

        InterfaceTextuelle interfaceTextuelle = new InterfaceTextuelle(mockJeu, mockCollecteurEv, true);
        assertFalse((boolean) getPrivateField(interfaceTextuelle, "jeuTermine")); // Verify initial state

        interfaceTextuelle.miseAJour();

        assertTrue((boolean) getPrivateField(interfaceTextuelle, "jeuTermine")); // Verify game is marked as finished
    }

    @Test
    @DisplayName("rafraichirAffichage displays all sections correctly")
    void testRafraichirAffichage() {
        InterfaceTextuelle interfaceTextuelle = new InterfaceTextuelle(mockJeu, mockCollecteurEv, true);
        interfaceTextuelle.rafraichirAffichage();
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("---------------------------------------------|"));
        assertTrue(output.contains("--- Informations de la Partie ---"));
        assertTrue(output.contains("Round : 1 | Temps : 01:05"));
        assertTrue(output.contains("C'est au tour de :"));
        assertTrue(output.contains("Joueur 1"));
        assertTrue(output.contains("--- Plateau de Jeu ---"));
        assertTrue(output.contains(" E1"));
        assertTrue(output.contains(" M2"));
        assertTrue(output.contains("--- Cartes du jeu ---"));
        assertTrue(output.contains("Carte A"));
        assertTrue(output.contains("Carte B"));
        assertTrue(output.contains("Carte C"));
        assertTrue(output.contains("Carte D"));
        assertTrue(output.contains("Carte E"));
        assertTrue(output.contains("A|a: Annuler(oui)"));
        assertTrue(output.contains("R|r: Refaire(non)"));
    }

    @Test
    @DisplayName("getSymboleCase returns correct symbol for VIDE")
    void testGetSymboleCaseVide() throws Exception {
        InterfaceTextuelle interfaceTextuelle = new InterfaceTextuelle(mockJeu, mockCollecteurEv, true);
        java.lang.reflect.Method method = InterfaceTextuelle.class.getDeclaredMethod("getSymboleCase", CasePlateau.class);
        method.setAccessible(true);

        CasePlateau mockVideCase = mock(CasePlateau.class);
        when(mockVideCase.getTypeElement()).thenReturn(TYPE_ELEMENT_SUR_CASE.VIDE);

        String symbol = (String) method.invoke(interfaceTextuelle, mockVideCase);
        assertEquals(" . ", symbol);
    }

    @Test
    @DisplayName("getSymboleCase returns correct symbol for PION_ETUDIANT")
    void testGetSymboleCasePionEtudiant() throws Exception {
        InterfaceTextuelle interfaceTextuelle = new InterfaceTextuelle(mockJeu, mockCollecteurEv, true);
        java.lang.reflect.Method method = InterfaceTextuelle.class.getDeclaredMethod("getSymboleCase", CasePlateau.class);
        method.setAccessible(true);

        CasePlateau mockEtudiantCase = mock(CasePlateau.class);
        when(mockEtudiantCase.getTypeElement()).thenReturn(TYPE_ELEMENT_SUR_CASE.PION_ETUDIANT);
        when(mockEtudiantCase.getProprietaire()).thenReturn(1);

        String symbol = (String) method.invoke(interfaceTextuelle, mockEtudiantCase);
        assertEquals(" E1", symbol);
    }

    @Test
    @DisplayName("getSymboleCase returns correct symbol for PION_MAITRE")
    void testGetSymboleCasePionMaitre() throws Exception {
        InterfaceTextuelle interfaceTextuelle = new InterfaceTextuelle(mockJeu, mockCollecteurEv, true);
        java.lang.reflect.Method method = InterfaceTextuelle.class.getDeclaredMethod("getSymboleCase", CasePlateau.class);
        method.setAccessible(true);

        CasePlateau mockMaitreCase = mock(CasePlateau.class);
        when(mockMaitreCase.getTypeElement()).thenReturn(TYPE_ELEMENT_SUR_CASE.PION_MAITRE);
        when(mockMaitreCase.getProprietaire()).thenReturn(2);

        String symbol = (String) method.invoke(interfaceTextuelle, mockMaitreCase);
        assertEquals(" M2", symbol);
    }

    @Test
    @DisplayName("espace returns correct number of spaces when a > b")
    void testEspaceGreaterThan() {
        assertEquals("   ", InterfaceTextuelle.espace(5, 2));
    }

    @Test
    @DisplayName("espace returns single space when a <= b")
    void testEspaceLessThanOrEqualTo() {
        assertEquals(" ", InterfaceTextuelle.espace(5, 5));
        assertEquals(" ", InterfaceTextuelle.espace(2, 5));
    }













@Nested
    @DisplayName("Display Elements Tests")
    class DisplayElementsTests {

        private InterfaceTextuelle interfaceTextuelle;

        @BeforeEach
        void setup() {
            interfaceTextuelle = new InterfaceTextuelle(mockJeu, mockCollecteurEv, true);
            outputStreamCaptor.reset(); // Clear output from constructor
        }

        @Test
        @DisplayName("getSymboleCase returns correct symbol for VIDE")
        void testGetSymboleCaseVide() throws Exception {
            java.lang.reflect.Method method = InterfaceTextuelle.class.getDeclaredMethod("getSymboleCase", CasePlateau.class);
            method.setAccessible(true);

            // Create a mock CasePlateau for VIDE type
            CasePlateau mockVideCase = mock(CasePlateau.class);
            when(mockVideCase.getTypeElement()).thenReturn(TYPE_ELEMENT_SUR_CASE.VIDE);

            String symbol = (String) method.invoke(interfaceTextuelle, mockVideCase);
            assertEquals(" . ", symbol);
        }

        @Test
        @DisplayName("getSymboleCase returns correct symbol for PION_ETUDIANT")
        void testGetSymboleCasePionEtudiant() throws Exception {
            java.lang.reflect.Method method = InterfaceTextuelle.class.getDeclaredMethod("getSymboleCase", CasePlateau.class);
            method.setAccessible(true);

            // Create a mock CasePlateau for PION_ETUDIANT
            CasePlateau mockEtudiantCase = mock(CasePlateau.class);
            when(mockEtudiantCase.getTypeElement()).thenReturn(TYPE_ELEMENT_SUR_CASE.PION_ETUDIANT);
            when(mockEtudiantCase.getIdJoueur()).thenReturn(1);

            String symbol = (String) method.invoke(interfaceTextuelle, mockEtudiantCase);
            assertEquals(" E1", symbol);
        }

        @Test
        @DisplayName("getSymboleCase returns correct symbol for PION_MAITRE")
        void testGetSymboleCasePionMaitre() throws Exception {
            java.lang.reflect.Method method = InterfaceTextuelle.class.getDeclaredMethod("getSymboleCase", CasePlateau.class);
            method.setAccessible(true);

            // Create a mock CasePlateau for PION_MAITRE
            CasePlateau mockMaitreCase = mock(CasePlateau.class);
            when(mockMaitreCase.getTypeElement()).thenReturn(TYPE_ELEMENT_SUR_CASE.PION_MAITRE);
            when(mockMaitreCase.getIdJoueur()).thenReturn(2);

            String symbol = (String) method.invoke(interfaceTextuelle, mockMaitreCase);
            assertEquals(" M2", symbol);
        }

        @Test
        @DisplayName("afficherCartesJeu displays all cards when afficherToutesCartes is true")
        void testAfficherCartesJeu() throws NoSuchFieldException, IllegalAccessException {
            // Set private field afficherToutesCartes to true using reflection
            setPrivateField(interfaceTextuelle, "afficherToutesCartes", true);

            // Call rafraichirAffichage, which will then call afficherCartesJeu
            interfaceTextuelle.rafraichirAffichage();

            String output = outputStreamCaptor.toString();
            // Check for card names and their grid representations
            assertTrue(output.contains("Carte A"));
            assertTrue(output.contains("Carte B"));
            assertTrue(output.contains("Carte C"));
            assertTrue(output.contains("Carte D"));
            assertTrue(output.contains("Carte E"));
            assertTrue(output.contains("+----------+")); // Check for card frames
            assertTrue(output.contains("X")); // Check for central point
            assertTrue(output.contains("*")); // Check for move points (from mocked getMoves)

            // Verify afficherToutesCartes is reset to false after display
            assertFalse((boolean) getPrivateField(interfaceTextuelle, "afficherToutesCartes"));
        }

        @Test
        @DisplayName("getVersionAffichable correctly represents a card with moves")
        void testGetVersionAffichable() throws Exception {
            Carte mockCardWithMoves = mock(Carte.class);
            when(mockCardWithMoves.getMoves()).thenReturn(Arrays.asList(
                    new Point(-1, 0), // Up
                    new Point(1, 0),  // Down
                    new Point(0, -1), // Left
                    new Point(0, 1)   // Right
            ));

            java.lang.reflect.Method method = InterfaceTextuelle.class.getDeclaredMethod("getVersionAffichable", Carte.class);
            method.setAccessible(true);
            String[][] grid = (String[][]) method.invoke(interfaceTextuelle, mockCardWithMoves);

            // Expected grid (X at 2,2, stars around it)
            String[][] expectedGrid = {
                    {".", ".", ".", ".", "."},
                    {".", ".", "*", ".", "."},
                    {".", "*", "X", "*", "."},
                    {".", ".", "*", ".", "."},
                    {".", ".", ".", ".", "."}
            };

            for (int i = 0; i < 5; i++) {
                assertArrayEquals(expectedGrid[i], grid[i], "Mismatch at row " + i);
            }
        }

        @Test
        @DisplayName("espace returns correct number of spaces when a > b")
        void testEspaceGreaterThan() {
            assertEquals("   ", InterfaceTextuelle.espace(5, 2));
        }

        @Test
        @DisplayName("espace returns single space when a <= b (as per original code logic)")
        void testEspaceLessThanOrEqualTo() {
            assertEquals(" ", InterfaceTextuelle.espace(5, 5));
            assertEquals(" ", InterfaceTextuelle.espace(2, 5));
        }
    }


    @Nested
    @DisplayName("lancerBoucleJeu Interaction Tests")
    class LancerBoucleJeuTests {

        private InterfaceTextuelle interfaceTextuelleSpy;
        private java.lang.reflect.Method lancerBoucleJeuMethod;

        @BeforeEach
        void setup() throws Exception {
            // Create a spy to verify internal method calls made by lancerBoucleJeu
            interfaceTextuelleSpy = Mockito.spy(new InterfaceTextuelle(mockJeu, mockCollecteurEv, true));
            // Reset the output captor for specific lancerBoucleJeu tests
            outputStreamCaptor.reset();

            // Access the private lancerBoucleJeu method via reflection
            lancerBoucleJeuMethod = InterfaceTextuelle.class.getDeclaredMethod("lancerBoucleJeu");
            lancerBoucleJeuMethod.setAccessible(true);
        }

        @Test
        @DisplayName("Quitting the game with 'q' command")
        void testQuitGameCommand() throws Exception {
            provideInput("q\n"); // Simulate typing 'q' then Enter
            lancerBoucleJeuMethod.invoke(interfaceTextuelleSpy);

            verify(mockCollecteurEv).clavier("exit");
            assertTrue(outputStreamCaptor.toString().contains("Interface textuelle terminée."));
            assertTrue((boolean) getPrivateField(interfaceTextuelleSpy, "jeuTermine"));
        }

        @Test
        @DisplayName("Annuling action with 'a' command")
        void testAnnulerActionCommand() throws Exception {
            provideInput("a\nq\n"); // Press 'a' then 'q' to exit
            lancerBoucleJeuMethod.invoke(interfaceTextuelleSpy);
            verify(mockCollecteurEv).clavier("annuler");
        }

        @Test
        @DisplayName("Redoing action with 'r' command")
        void testRefaireActionCommand() throws Exception {
            provideInput("r\nq\n"); // Press 'r' then 'q' to exit
            lancerBoucleJeuMethod.invoke(interfaceTextuelleSpy);
            verify(mockCollecteurEv).clavier("refaire");
        }

        @Test
        @DisplayName("Starting new game with 'n' command")
        void testNouvellePartieCommand() throws Exception {
            provideInput("n\nq\n"); // Press 'n' then 'q' to exit
            lancerBoucleJeuMethod.invoke(interfaceTextuelleSpy);
            verify(mockCollecteurEv).clavier("nouvellePartie");
        }

        @Test
        @DisplayName("Saving game with 's' command")
        void testSauvegarderCommand() throws Exception {
            provideInput("s\nq\n"); // Press 's' then 'q' to exit
            lancerBoucleJeuMethod.invoke(interfaceTextuelleSpy);
            verify(mockCollecteurEv).clavier("sauvegarder");
        }

        @Test
        @DisplayName("Viewing all cards with 'c' command")
        void testViewAllCardsCommand() throws Exception {
            provideInput("c\nq\n"); // Press 'c' then 'q' to exit
            lancerBoucleJeuMethod.invoke(interfaceTextuelleSpy);

            String output = outputStreamCaptor.toString();
            assertTrue(output.contains("Carte A")); // Check if card details were printed
            assertTrue(output.contains("+----------+")); // Check for card frame
            // Verify that the afficherToutesCartes flag was set and then reset
            assertFalse((boolean) getPrivateField(interfaceTextuelleSpy, "afficherToutesCartes"));
        }

        @Test
        @DisplayName("Selecting reserved card '5' shows message and does not trigger a full refresh (only one info header)")
        void testReservedCard5() throws Exception {
            provideInput("5\nq\n"); // Select '5' then 'q'
            lancerBoucleJeuMethod.invoke(interfaceTextuelleSpy);

            String output = outputStreamCaptor.toString();
            assertTrue(output.contains("carte réservée"));
            int infoSectionCount = (output.split("--- Informations de la Partie ---", -1).length - 1);
            assertEquals(1, infoSectionCount, "Should only have one 'Informations' section if refresh is skipped");
        }

        @Test
        @DisplayName("Invalid command shows error and does not trigger a full refresh (only one info header)")
        void testInvalidCommand() throws Exception {
            provideInput("xyz\nq\n"); // Invalid command, then 'q'
            lancerBoucleJeuMethod.invoke(interfaceTextuelleSpy);

            String output = outputStreamCaptor.toString();
            assertTrue(output.contains("Commande inconnue : \"xyz\". Réessayez."));
            int infoSectionCount = (output.split("--- Informations de la Partie ---", -1).length - 1);
            assertEquals(1, infoSectionCount, "Should only have one 'Informations' section if refresh is skipped after invalid command");
        }

        @Test
        @DisplayName("Successful card and pion selection leading to a move")
        void testSuccessfulMoveWorkflow() throws Exception {
            // Mock estPionDuJoueurCourant for successful pion selection
            when(mockJeu.estPionDuJoueurCourant(0, 0)).thenReturn(true);
            when(mockJeu.getJoueurCourant().getId()).thenReturn(ID_JOUEUR_1);

            provideInput("1\n0 0\n0 1\nq\n");

            lancerBoucleJeuMethod.invoke(interfaceTextuelleSpy);

            verify(mockCollecteurEv).setCarteSelectionne(anyInt()); // Verify card was set (0 for Carte A)
            verify(mockCollecteurEv).setCaseSelectionnee(0, 0); // Verify pion was set
            verify(mockCollecteurEv).setCiblePion(0, 1); // Verify target was set
            assertFalse((boolean) getPrivateField(interfaceTextuelleSpy, "carteSelectionnee")); // Should be false after a move
        }

        @Test
        @DisplayName("Cancelling pion selection with 'z' returns to card selection")
        void testCancelPionSelectionWithZ() throws Exception {
            when(mockJeu.getJoueurCourant().getId()).thenReturn(ID_JOUEUR_1);

            provideInput("1\nz\nq\n");

            lancerBoucleJeuMethod.invoke(interfaceTextuelleSpy);

            verify(mockCollecteurEv).setCarteSelectionne(0); // Card was initially selected
            verify(mockCollecteurEv, never()).setCaseSelectionnee(anyInt(), anyInt()); // Pion was NOT set
            verify(mockCollecteurEv, never()).setCiblePion(anyInt(), anyInt()); // Target was NOT set

            String output = outputStreamCaptor.toString();
            assertTrue(output.contains("Sélection annulée."));
            assertTrue(output.contains("Choisissez une Carte (1-4) :")); // Should go back to card selection prompt
            assertFalse((boolean) getPrivateField(interfaceTextuelleSpy, "carteSelectionnee")); // Should be false after canceling pion
        }

        @Test
        @DisplayName("Cancelling destination selection with 'z' returns to pion selection")
        void testCancelDestinationSelectionWithZ() throws Exception {
            when(mockJeu.estPionDuJoueurCourant(0, 0)).thenReturn(true);
            when(mockJeu.getJoueurCourant().getId()).thenReturn(ID_JOUEUR_1);

            provideInput("1\n0 0\nz\nq\n");

            lancerBoucleJeuMethod.invoke(interfaceTextuelleSpy);

            verify(mockCollecteurEv).setCarteSelectionne(0);
            verify(mockCollecteurEv).setCaseSelectionnee(0, 0); // Pion was set
            verify(mockCollecteurEv, never()).setCiblePion(anyInt(), anyInt()); // Target was NOT set

            String output = outputStreamCaptor.toString();
            assertTrue(output.contains("Déplacement annulé."));
            assertTrue(output.contains("Choisissez un pion (ex: 2 3) ou Z|z pour annuler :")); // Should go back to pion selection prompt
            assertTrue((boolean) getPrivateField(interfaceTextuelleSpy, "carteSelectionnee")); // Should still be true (inside pion selection loop)
        }

        @Test
        @DisplayName("Invalid pion coordinates show error and prompt again")
        void testInvalidPionCoordinates() throws Exception {
            when(mockJeu.estPionDuJoueurCourant(anyInt(), anyInt())).thenReturn(false); // No pion at any coord
            when(mockJeu.getJoueurCourant().getId()).thenReturn(ID_JOUEUR_1);

            provideInput("1\n9 9\nq\n");
            lancerBoucleJeuMethod.invoke(interfaceTextuelleSpy);

            String output = outputStreamCaptor.toString();
            assertTrue(output.contains("Coordonnées du pion incorrectes."));
            assertTrue(output.contains("Choisissez un pion (ex: 2 3) ou Z|z pour annuler :"));
            verify(mockCollecteurEv, never()).setCaseSelectionnee(anyInt(), anyInt());
        }

        @Test
        @DisplayName("Invalid destination coordinates show error and prompt again")
        void testInvalidDestinationCoordinates() throws Exception {
            when(mockJeu.estPionDuJoueurCourant(0, 0)).thenReturn(true);
            when(mockJeu.getJoueurCourant().getId()).thenReturn(ID_JOUEUR_1);

            provideInput("1\n0 0\n9 9\nq\n");

            lancerBoucleJeuMethod.invoke(interfaceTextuelleSpy);

            String output = outputStreamCaptor.toString();
            assertTrue(output.contains("Coordonnées cibles invalides."));
            assertTrue(output.contains("Coordonnées de destination (ex: 1 2) ou Z pour annuler :"));
            verify(mockCollecteurEv).setCaseSelectionnee(0, 0);
            verify(mockCollecteurEv, never()).setCiblePion(anyInt(), anyInt());
        }

        @Test
        @DisplayName("Choosing a card not belonging to the current player")
        void testChoosingOtherPlayersCard() throws Exception {
            // Assume current player is J1, trying to pick card 3 (J2's card)
            when(mockJeu.getJoueurCourant().getId()).thenReturn(ID_JOUEUR_1);

            provideInput("3\nq\n");
            lancerBoucleJeuMethod.invoke(interfaceTextuelleSpy);

            String output = outputStreamCaptor.toString();
            assertTrue(output.contains("Cette carte n'appartient pas à Joueur 1"));
            assertTrue(output.contains("Choisissez une Carte (1-4) :"));
            verify(mockCollecteurEv, never()).setCarteSelectionne(anyInt());
        }
    }

}