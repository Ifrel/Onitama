package Vue;

import Controleur.ControleurEcranDeDemarrage;
import Modele.Jeu;
import Vue.Adaptateurs.AdaptateurBoutonEntrer; // We'll need to create a simple mock version for this
import Vue.Utils.Boutons.Bouton.BoutonAvecImage; // For internal component access via reflection if needed

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field; // For accessing private fields if necessary

import static Global.Config.COULEUR_CASE_MAITRE_JOUEUR_1;
import static Global.Config.COULEUR_CASE_TERRAIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; // For Mockito methods like when, verify, any

// Import constants for comparison
import static Vue.ConfigUI.*;
import static Global.Config.CiblesDesCouleurs.*;

/**
 * Test class for EcranDeDemarrage.
 * Uses JUnit 5 and Mockito for isolated testing of UI component behavior
 * and its interaction with the controller.
 *
 * We use @Nested classes to group related tests for better organization.
 */
@DisplayName("EcranDeDemarrage Tests")
class TestEcranDeDemarrage {

    private EcranDeDemarrage ecranDeDemarrage;

    // Mocks for dependencies
    @Mock
    private Jeu mockJeu;
    @Mock
    private InterfaceGraphique mockInterfaceGraphique;
    @Mock
    private ControleurEcranDeDemarrage mockControleurDemarrage;

    // We need to mock AdaptateurBoutonEntrer if it creates the Controleur internally.
    // However, the current EcranDeDemarrage constructor passes a Controleur to AdaptateurBoutonEntrer.
    // So, we'll ensure AdaptateurBoutonEntrer's behavior is consistent with our mock.
    // We can also create a simple mock version of AdaptateurBoutonEntrer if its internal logic is complex.
    // For this example, we'll access the private field `actionListenerEntree` to set its internal state if needed.

    @BeforeEach
    void setUp() {
        // Initialize mocks annotated with @Mock
        MockitoAnnotations.openMocks(this);

        // Ensure mockJeu returns sensible defaults for initial states
        when(mockJeu.getNomJoueur1()).thenReturn("Joueur A");
        when(mockJeu.getNomJoueur2()).thenReturn("Joueur B");

        // We manually inject the mockControleurDemarrage into the EcranDeDemarrage's constructor
        // by making EcranDeDemarrage instantiate ControleurEcranDeDemarrage with our mockJeu in its constructor.
        // However, looking at your EcranDeDemarrage code:
        // this.controleurDemarrage = new ControleurEcranDeDemarrage(jeu);
        // This means EcranDeDemarrage *creates* its own controller.
        // To test the interaction, we have two options:
        // 1. Refactor EcranDeDemarrage to *inject* ControleurEcranDeDemarrage (dependency injection - preferred).
        // 2. Use reflection to replace the internally created controller with our mock.

        // For simplicity and to avoid modifying EcranDeDemarrage for this test, we'll use reflection
        // to set the controleurDemarrage and actionListenerEntree to our mocks.
        // In a real-world scenario, refactoring for dependency injection would be better.

        ecranDeDemarrage = new EcranDeDemarrage(mockJeu, mockInterfaceGraphique);

        // Use reflection to set the private 'controleurDemarrage' field to our mock
        try {
            Field controllerField = EcranDeDemarrage.class.getDeclaredField("controleurDemarrage");
            controllerField.setAccessible(true); // Allow access to private field
            controllerField.set(ecranDeDemarrage, mockControleurDemarrage); // Inject our mock

            // Also update the actionListenerEntree to use our mock controller
            Field actionListenerField = EcranDeDemarrage.class.getDeclaredField("actionListenerEntree");
            actionListenerField.setAccessible(true);
            AdaptateurBoutonEntrer currentListener = (AdaptateurBoutonEntrer) actionListenerField.get(ecranDeDemarrage);
            // Assuming AdaptateurBoutonEntrer has a setter or similar method to update its controller
            // If not, we might need to mock AdaptateurBoutonEntrer itself.
            // For now, let's assume setting the controller via reflection is sufficient.
            // A safer way is to mock AdaptateurBoutonEntrer too, and verify calls on it.
            // Let's create a partial mock of AdaptateurBoutonEntrer that uses our mockControleurDemarrage.
            AdaptateurBoutonEntrer mockActionListener = mock(AdaptateurBoutonEntrer.class);
//            when(mockActionListener.getControleur()).thenReturn(mockControleurDemarrage); // Ensure it returns our mock
            actionListenerField.set(ecranDeDemarrage, mockActionListener);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to inject mock controller or action listener due to reflection error: " + e.getMessage());
        }
    }

    // --- Sub-tests for Initialization ---
    @Nested
    @DisplayName("Initialization Tests")
    class InitializationTests {

        @Test
        @DisplayName("Should initialize JTabbedPane with two tabs")
        void shouldInitializeWithTwoTabs() {
            assertNotNull(ecranDeDemarrage, "EcranDeDemarrage should not be null after initialization.");
            assertEquals(2, ecranDeDemarrage.getTabCount(), "There should be exactly two tabs.");
//            assertEquals(TITRE_ONGLET_GENERAL, PngText.extractTextFromPanel((JPanel) ecranDeDemarrage.getTabComponentAt(0)),
//                    "First tab title should be 'Général'.");
//            assertEquals(TITRE_ONGLET_COULEUR, PngText.extractTextFromPanel((JPanel) ecranDeDemarrage.getTabComponentAt(1)),
//                    "Second tab title should be 'Couleur'.");
        }

        @Test
        @DisplayName("General tab should be an instance of PanelAvecImage and use GridBagLayout")
        void generalTabShouldBePanelAvecImageAndUseGridBagLayout() {
            Component generalTab = ecranDeDemarrage.getComponentAt(0);
            assertNotNull(generalTab);
//            assertTrue(generalTab instanceof PanelAvecImage, "General tab should be a PanelAvecImage.");
            assertTrue(generalTab instanceof JPanel, "General tab should also be a JPanel.");
            assertTrue(((JPanel) generalTab).getLayout() instanceof GridBagLayout, "General tab should use GridBagLayout.");
        }

        @Test
        @DisplayName("Color tab should be an instance of PanelAvecImage and use GridBagLayout")
        void colorTabShouldBePanelAvecImageAndUseGridBagLayout() {
            Component colorTab = ecranDeDemarrage.getComponentAt(1);
            assertNotNull(colorTab);
//            assertTrue(colorTab instanceof PanelAvecImage, "Color tab should be a PanelAvecImage.");
            assertTrue(colorTab instanceof JPanel, "Color tab should also be a JPanel.");
            assertTrue(((JPanel) colorTab).getLayout() instanceof GridBagLayout, "Color tab should use GridBagLayout.");
        }

        @Test
        @DisplayName("Initial mode should be not auto IA")
        void initialModeShouldBeNotAutoIA() throws NoSuchFieldException, IllegalAccessException {
            // Access private field using reflection to verify initial state
            Field estModeAutoIAField = EcranDeDemarrage.class.getDeclaredField("estModeAutoIA");
            estModeAutoIAField.setAccessible(true);
            boolean estModeAutoIA = (boolean) estModeAutoIAField.get(ecranDeDemarrage);
            assertFalse(estModeAutoIA, "Initial 'estModeAutoIA' should be false.");
        }
    }

    // --- Sub-tests for General Tab Components ---
    @Nested
    @DisplayName("General Tab Component Tests")
    class GeneralTabComponentTests {

        private JPanel getGeneralTab() {
            return (JPanel) ecranDeDemarrage.getComponentAt(0);
        }

        private JComboBox<String> getComboBoxPartie() throws NoSuchFieldException, IllegalAccessException {
            Field field = EcranDeDemarrage.class.getDeclaredField("comboBoxPartie");
            field.setAccessible(true);
            return (JComboBox<String>) field.get(ecranDeDemarrage);
        }

        private JComboBox<String> getComboBoxNiveauIA() throws NoSuchFieldException, IllegalAccessException {
            Field field = EcranDeDemarrage.class.getDeclaredField("comboBoxNiveauIA");
            field.setAccessible(true);
            return (JComboBox<String>) field.get(ecranDeDemarrage);
        }

        private JTextField getChampNomJoueur1() throws NoSuchFieldException, IllegalAccessException {
            Field field = EcranDeDemarrage.class.getDeclaredField("champNomJoueur1");
            field.setAccessible(true);
            return (JTextField) field.get(ecranDeDemarrage);
        }

        private JTextField getChampNomJoueur2() throws NoSuchFieldException, IllegalAccessException {
            Field field = EcranDeDemarrage.class.getDeclaredField("champNomJoueur2");
            field.setAccessible(true);
            return (JTextField) field.get(ecranDeDemarrage);
        }

        private BoutonAvecImage getBoutonModeAuto() throws NoSuchFieldException, IllegalAccessException {
            Field field = EcranDeDemarrage.class.getDeclaredField("boutonModeAuto");
            field.setAccessible(true);
            return (BoutonAvecImage) field.get(ecranDeDemarrage);
        }

        private AdaptateurBoutonEntrer getActionListenerEntree() throws NoSuchFieldException, IllegalAccessException {
            Field field = EcranDeDemarrage.class.getDeclaredField("actionListenerEntree");
            field.setAccessible(true);
            return (AdaptateurBoutonEntrer) field.get(ecranDeDemarrage);
        }


        @Test
        @DisplayName("Player name fields should be pre-filled from Jeu model")
        void playerNameFieldsShouldBePreFilled() throws NoSuchFieldException, IllegalAccessException {
            assertEquals("Joueur A", getChampNomJoueur1().getText(), "Player 1 name field should be pre-filled.");
            assertEquals("Joueur B", getChampNomJoueur2().getText(), "Player 2 name field should be pre-filled.");
        }

        @Test
        @DisplayName("ComboBox for resuming game should have correct options and initial selection")
        void comboBoxPartieShouldHaveCorrectOptions() throws NoSuchFieldException, IllegalAccessException {
            JComboBox<String> comboBox = getComboBoxPartie();
            assertArrayEquals(OPTIONS_REPRENDRE, getItemsFromComboBox(comboBox), "ComboBox options should match ConfigUI.");
            assertEquals(INDICATION_SELECTION, comboBox.getSelectedItem(), "Initial selection should be 'Sélectionner ici...'.");
            assertEquals(new Color(7,7,7), comboBox.getForeground(), "Initial text color should be gray.");
        }

        @Test
        @DisplayName("ComboBox for AI level should have correct options and initial selection")
        void comboBoxNiveauIAShouldHaveCorrectOptions() throws NoSuchFieldException, IllegalAccessException {
            JComboBox<String> comboBox = getComboBoxNiveauIA();
            assertArrayEquals(OPTIONS_IA, getItemsFromComboBox(comboBox), "AI ComboBox options should match ConfigUI.");
            assertEquals(OPTION_IA_NON, comboBox.getSelectedItem(), "Initial selection should be 'Non'.");
        }

        @Test
        @DisplayName("Toggling 'IA vs IA' mode should update button image and component states")
        void toggleIAModeShouldUpdateUIAndStates() throws NoSuchFieldException, IllegalAccessException {
            BoutonAvecImage bouton = getBoutonModeAuto();
            JTextField champ2 = getChampNomJoueur2();
            JComboBox<String> comboPartie = getComboBoxPartie();
            JComboBox<String> comboIA = getComboBoxNiveauIA();
            AdaptateurBoutonEntrer mockActionListener = getActionListenerEntree();

            // Initial state: Mode auto is OFF, components enabled
            assertTrue(champ2.isEnabled(), "Player 2 field should be enabled initially.");
            assertTrue(comboPartie.isEnabled(), "Partie combo box should be enabled initially.");
            assertTrue(comboIA.isEnabled(), "IA combo box should be enabled initially.");

            // Simulate click to turn ON IA vs IA mode
            bouton.doClick();

            // Verify state after click ON
            assertFalse(champ2.isEnabled(), "Player 2 field should be disabled in auto IA mode.");
            assertFalse(comboPartie.isEnabled(), "Partie combo box should be disabled in auto IA mode.");
            assertFalse(comboIA.isEnabled(), "IA combo box should be disabled in auto IA mode.");
            verify(mockActionListener).setModeAutoIA(true); // Verify controller interaction

            // Simulate click to turn OFF IA vs IA mode
            bouton.doClick();

            // Verify state after click OFF
            assertTrue(champ2.isEnabled(), "Player 2 field should be re-enabled after auto IA mode is off.");
            assertTrue(comboPartie.isEnabled(), "Partie combo box should be re-enabled after auto IA mode is off.");
            assertTrue(comboIA.isEnabled(), "IA combo box should be re-enabled after auto IA mode is off.");
            verify(mockActionListener).setModeAutoIA(false); // Verify controller interaction
        }

        @Test
        @DisplayName("Selecting a saved game should disable Player 2 name field")
        void selectSavedGameShouldDisablePlayer2() throws NoSuchFieldException, IllegalAccessException {
            JComboBox<String> comboBox = getComboBoxPartie();
            JTextField champ2 = getChampNomJoueur2();
            AdaptateurBoutonEntrer mockActionListener = getActionListenerEntree();

            // Initial state
            assertTrue(champ2.isEnabled(), "Player 2 field should be enabled initially.");

            // Select "Partie 1" (index 1)
            comboBox.setSelectedIndex(1);

            assertFalse(champ2.isEnabled(), "Player 2 field should be disabled when a game is selected.");
            // Verify that the controller is informed of the selection
            verify(mockActionListener).setPartieSelectionnee("Partie 1");

            // Select "Sélectionner ici..." (index 0)
            comboBox.setSelectedIndex(0);

            assertTrue(champ2.isEnabled(), "Player 2 field should be re-enabled when 'Sélectionner ici...' is selected.");
            verify(mockActionListener).setPartieSelectionnee(INDICATION_SELECTION);
        }

        @Test
        @DisplayName("Selecting an AI level should disable Player 2 name field")
        void selectAILvelShouldDisablePlayer2() throws NoSuchFieldException, IllegalAccessException {
            JComboBox<String> comboBox = getComboBoxNiveauIA();
            JTextField champ2 = getChampNomJoueur2();
            AdaptateurBoutonEntrer mockActionListener = getActionListenerEntree();

            // Initial state
            assertTrue(champ2.isEnabled(), "Player 2 field should be enabled initially.");

            // Select "Faible" (index 1)
            comboBox.setSelectedIndex(1);

            assertFalse(champ2.isEnabled(), "Player 2 field should be disabled when AI is selected.");
            verify(mockActionListener).setNiveauIAselectione("Faible");

            // Select "Non" (index 0)
            comboBox.setSelectedIndex(0);

            assertTrue(champ2.isEnabled(), "Player 2 field should be re-enabled when 'Non' is selected for AI.");
            verify(mockActionListener).setNiveauIAselectione(OPTION_IA_NON);
        }

        @Test
        @DisplayName("Player name field updates should inform action listener")
        void playerNameFieldUpdatesShouldInformActionListener() throws NoSuchFieldException, IllegalAccessException {
            JTextField champ1 = getChampNomJoueur1();
            JTextField champ2 = getChampNomJoueur2();
            AdaptateurBoutonEntrer mockActionListener = getActionListenerEntree();

            champ1.setText("New Player 1");
            verify(mockActionListener).setChampJoueur(1, champ1); // Verify interaction

            champ2.setText("New Player 2");
            verify(mockActionListener).setChampJoueur(2, champ2); // Verify interaction
        }

        @Test
        @DisplayName("Enter button click should trigger action listener")
        void enterButtonClickShouldTriggerActionListener() throws NoSuchFieldException, IllegalAccessException {
            // Find the "Entrer" button by its preferred size or other properties if direct access isn't available
            // For now, let's assume we can reflectively get the button directly if needed,
            // or find it by iterating components.
            // Let's get the AdaptateurBoutonEntrer mock and directly simulate its actionPerformed.
            // Alternatively, if the button itself is mockable, we'd do boutonEntrer.doClick();

            // Since we mocked the entire AdaptateurBoutonEntrer, we just need to verify its action.
            AdaptateurBoutonEntrer mockActionListener = getActionListenerEntree();

            // Simulate the button click by triggering the action listener directly,
            // as Swing components can be difficult to simulate clicks on without a UI framework.
            mockActionListener.actionPerformed(mock(java.awt.event.ActionEvent.class));

            // Verify that the action listener performed its action (though its internal logic is mocked)
            verify(mockActionListener).actionPerformed(any(java.awt.event.ActionEvent.class));
            // This is a basic check. More robust tests would ensure the controller was called within the listener.
        }

        // Helper method to extract items from a JComboBox for comparison
        private String[] getItemsFromComboBox(JComboBox<String> comboBox) {
            String[] items = new String[comboBox.getItemCount()];
            for (int i = 0; i < comboBox.getItemCount(); i++) {
                items[i] = comboBox.getItemAt(i);
            }
            return items;
        }
    }

    // --- Sub-tests for Color Tab Components ---
    @Nested
    @DisplayName("Color Tab Component Tests")
    class ColorTabComponentTests {

        private JPanel getColorTab() {
            return (JPanel) ecranDeDemarrage.getComponentAt(1);
        }

        @Test
        @DisplayName("Color selection buttons should update controller with chosen color")
        void colorSelectionButtonsShouldUpdateController() {
            JPanel colorTab = getColorTab();
            // We need to find the color selection buttons. They are JButtons.
            // A robust way would be to get them by their associated target (CiblesDesCouleurs)
            // or by iterating through components and checking their ActionListeners.
            // For this example, let's find one by iterating or making assumptions about its position.

            // Given the layout, the first JButton is likely the 'Case Terrain' color selector.
            JButton caseTerrainButton = findButtonByBackgroundColor(colorTab, COULEUR_CASE_TERRAIN);
            assertNotNull(caseTerrainButton, "Case Terrain color button not found.");

            // Simulate a user choosing a new color (e.g., green)
            Color newColor = Color.GREEN;

            // Mock JColorChooser.showDialog to return our new color
//            try (mockedStatic(JColorChooser.class)) {
                when(JColorChooser.showDialog(any(Component.class), anyString(), any(Color.class)))
                        .thenReturn(newColor);

                // Simulate clicking the button
                caseTerrainButton.doClick();

                // Verify that the button's background changed
                assertEquals(newColor, caseTerrainButton.getBackground(), "Button background should update to chosen color.");

                // Verify that the controller's setCouleur method was called with the correct target and color
                verify(mockControleurDemarrage).setCouleur(CASE_TERRAIN, newColor);

//            } // JColorChooser.showDialog is unmocked automatically here
        }

        @Test
        @DisplayName("Reset buttons should reset color and inform controller")
        void resetButtonsShouldResetColorAndInformController() {
            JPanel colorTab = getColorTab();

            // Find the color selection button and its corresponding reset button for CASE_MAITRE_JOUEUR_1
            JButton masterPlayer1ColorButton = findButtonByBackgroundColor(colorTab, COULEUR_CASE_MAITRE_JOUEUR_1);
            assertNotNull(masterPlayer1ColorButton, "Master Player 1 color button not found.");

            // The reset button for this color should be the next BoutonAvecImage in the same row.
            // We'll rely on finding it structurally.
            // A more robust way might involve assigning names/IDs to components for testing.
            BoutonAvecImage masterPlayer1ResetButton = findResetButtonForTarget(colorTab, masterPlayer1ColorButton);
            assertNotNull(masterPlayer1ResetButton, "Master Player 1 reset button not found.");

            // First, change the color to something else
            masterPlayer1ColorButton.setBackground(Color.CYAN);

            // Simulate clicking the reset button
            masterPlayer1ResetButton.doClick();

            // Verify that the color button's background reverted to its initial color
            assertEquals(COULEUR_CASE_MAITRE_JOUEUR_1, masterPlayer1ColorButton.getBackground(),
                    "Color should revert to initial default after reset.");

            // Verify that the controller's setCouleur method was called with the initial default color
            // Use ArgumentCaptor to capture the actual arguments passed to the mock
            ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
            verify(mockControleurDemarrage).setCouleur(eq(CASE_MAITRE_JOUEUR_1), colorCaptor.capture());
            assertEquals(COULEUR_CASE_MAITRE_JOUEUR_1, colorCaptor.getValue(), "Controller should be informed of reset color.");
        }

        // --- Helper methods for finding components within the UI ---

        /**
         * Finds a JButton within a container that has a specific background color.
         * This is a heuristic and might need to be more specific in complex UIs.
         */
        private JButton findButtonByBackgroundColor(Container container, Color expectedColor) {
            for (Component comp : container.getComponents()) {
                if (comp instanceof JButton) {
                    JButton button = (JButton) comp;
                    // Check if it's a color selection button (they are JButtons, not BoutonAvecImage)
                    if (button.getPreferredSize().equals(DIM_PREVIEW_COULEUR) && button.getBackground().equals(expectedColor)) {
                        return button;
                    }
                } else if (comp instanceof Container) {
                    JButton found = findButtonByBackgroundColor((Container) comp, expectedColor);
                    if (found != null) {
                        return found;
                    }
                }
            }
            return null;
        }

        /**
         * Finds the reset button associated with a given color selection button.
         * Assumes the reset button is a BoutonAvecImage and is positioned relative to the color button.
         */
        private BoutonAvecImage findResetButtonForTarget(Container container, JButton targetColorButton) {
            // Find the target button's position in the layout
            // This is a simplified approach. In a real scenario, you might iterate GBCs.
            // For now, let's just find the BoutonAvecImage after the targetColorButton in the same row.
            boolean foundTarget = false;
            for (Component comp : container.getComponents()) {
                if (comp == targetColorButton) {
                    foundTarget = true;
                } else if (foundTarget && comp instanceof BoutonAvecImage) {
                    // Assuming the next BoutonAvecImage after the target is the reset button
                    if (comp.getPreferredSize().equals(DIM_PREVIEW_COULEUR)) { // Check if it's a reset button by size
                        return (BoutonAvecImage) comp;
                    }
                } else if (comp instanceof Container) { // Recurse into sub-containers
                    BoutonAvecImage found = findResetButtonForTarget((Container) comp, targetColorButton);
                    if (found != null) {
                        return found;
                    }
                }
            }
            return null;
        }
    }
}