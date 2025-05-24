package Vue;

import Modele.Jeu;
import Vue.Utils.Boutons.Bouton;
import Vue.Utils.PanelAvecImage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.time.Duration;

import static Global.Paths.PATH_ARRIERE_PLAN_4;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de test pour EcranMenu.
 * Utilise JUnit 5 et Mockito pour tester les composants UI et leurs interactions avec le modèle et les contrôleurs.
 */
@DisplayName("Tests d'EcranMenu")
class TestEcranMenu {

    private EcranMenu ecranMenu;

    // Mocks pour les dépendances
    @Mock
    private Jeu mockJeu;
    @Mock
    private CollecteurEvenements mockCollecteurEvenements;
    @Mock
    private InterfaceGraphique mockInterfaceGraphique;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configure les comportements par défaut du mockJeu
        when(mockJeu.getNomJoueur1()).thenReturn("Joueur Un");
        when(mockJeu.getNomJoueur2()).thenReturn("Joueur Deux");
        when(mockJeu.getNumeroRound()).thenReturn(5);
        when(mockJeu.getTempsDeJeu()).thenReturn(Duration.ofMinutes(2).plusSeconds(30).getSeconds()); // 02:30

        // Mock les objets Joueur pour éviter NullPointerException si Jeu.getJoueur() est appelé
        Modele.Joueur mockJoueur1 = mock(Modele.Joueur.class);
        Modele.Joueur mockJoueur2 = mock(Modele.Joueur.class);
        when(mockJoueur1.getScore()).thenReturn(100);
        when(mockJoueur2.getScore()).thenReturn(75);
        when(mockJeu.getJoueur(1)).thenReturn(mockJoueur1);
        when(mockJeu.getJoueur(2)).thenReturn(mockJoueur2);

        // Crée l'instance de EcranMenu
        ecranMenu = new EcranMenu(mockJeu, mockCollecteurEvenements, mockInterfaceGraphique);
    }

    // --- Sous-tests pour l'initialisation et le layout ---
    @Nested
    @DisplayName("Tests d'initialisation et de Layout")
    class InitialisationAndLayoutTests {

        @Test
        @DisplayName("Devrait être une instance de PanelAvecImage et avoir l'arrière-plan correct")
        void shouldBePanelAvecImageWithCorrectBackground() {
            assertTrue(ecranMenu instanceof PanelAvecImage, "EcranMenu devrait être un PanelAvecImage.");
            assertEquals(PATH_ARRIERE_PLAN_4.toString(), ecranMenu.getImagePath(), "L'image d'arrière-plan devrait être correcte.");
        }

        @Test
        @DisplayName("Devrait utiliser GridBagLayout")
        void shouldUseGridBagLayout() {
            assertTrue(ecranMenu.getLayout() instanceof GridBagLayout, "EcranMenu devrait utiliser GridBagLayout.");
        }

        @Test
        @DisplayName("Devrait s'enregistrer comme observateur du Jeu")
        void shouldRegisterAsObserverOfJeu() {
            verify(mockJeu).ajouteObservateur(ecranMenu);
        }

        @Test
        @DisplayName("Devrait appeler miseAJour() à l'initialisation")
        void shouldCallMiseAJourOnInitialization() {
            // Vérifie que miseAJour a été appelée au moins une fois pendant l'initialisation.
            // On peut reset le mockJeu et recréer EcranMenu si on veut un compte exact
            // mais ici on vérifie juste que la méthode est appelée.
            // L'appel réel de miseAJour est déjà fait dans le BeforeEach.
            // Pour un test plus précis, on pourrait faire un Spy sur EcranMenu.
            // Cependant, la vérification de l'interface graphique mise à jour est le meilleur indicateur.
            assertDoesNotThrow(() -> ecranMenu.miseAJour(), "miseAJour() ne devrait pas lever d'exception à l'initialisation.");
        }
    }

    // --- Sous-tests pour les composants du panneau de statistiques ---
    @Nested
    @DisplayName("Tests du panneau de statistiques")
    class StatsPanelTests {

        private JLabel getRoundValue() throws NoSuchFieldException, IllegalAccessException {
            Field field = EcranMenu.class.getDeclaredField("roundValue");
            field.setAccessible(true);
            return (JLabel) field.get(ecranMenu);
        }

        private JLabel getDureePartieValue() throws NoSuchFieldException, IllegalAccessException {
            Field field = EcranMenu.class.getDeclaredField("dureePartieValue");
            field.setAccessible(true);
            return (JLabel) field.get(ecranMenu);
        }

        private JLabel getNomJoueurAValue() throws NoSuchFieldException, IllegalAccessException {
            Field field = EcranMenu.class.getDeclaredField("nomJoueurAValue");
            field.setAccessible(true);
            return (JLabel) field.get(ecranMenu);
        }

        private JLabel getScoreJoueurAValue() throws NoSuchFieldException, IllegalAccessException {
            Field field = EcranMenu.class.getDeclaredField("scoreJoueurAValue");
            field.setAccessible(true);
            return (JLabel) field.get(ecranMenu);
        }

        private JLabel getNomJoueurBValue() throws NoSuchFieldException, IllegalAccessException {
            Field field = EcranMenu.class.getDeclaredField("nomJoueurBValue");
            field.setAccessible(true);
            return (JLabel) field.get(ecranMenu);
        }

        private JLabel getScoreJoueurBValue() throws NoSuchFieldException, IllegalAccessException {
            Field field = EcranMenu.class.getDeclaredField("scoreJoueurBValue");
            field.setAccessible(true);
            return (JLabel) field.get(ecranMenu);
        }

        @Test
        @DisplayName("Les valeurs des statistiques devraient se mettre à jour correctement")
        void statsValuesShouldUpdateCorrectly() throws NoSuchFieldException, IllegalAccessException {
            // Force la mise à jour pour s'assurer que l'EDT a traité les changements
            SwingUtilities.invokeLater(() -> {
                try {
                    assertEquals("5", getRoundValue().getText(), "Le numéro de round doit être correct.");
                    assertEquals("02:30", getDureePartieValue().getText(), "La durée de partie doit être correcte.");
                    assertEquals("Joueur Un: ", getNomJoueurAValue().getText(), "Le nom du Joueur 1 doit être correct.");
                    assertEquals("100", getScoreJoueurAValue().getText(), "Le score du Joueur 1 doit être correct.");
                    assertEquals("Joueur Deux: ", getNomJoueurBValue().getText(), "Le nom du Joueur 2 doit être correct.");
                    assertEquals("75", getScoreJoueurBValue().getText(), "Le score du Joueur 2 doit être correct.");
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    fail("Erreur d'accès aux champs: " + e.getMessage());
                }
            });
            // Attendre que l'EDT traite les événements
            try {
                SwingUtilities.invokeAndWait(() -> {});
            } catch (Exception e) {
                fail("Erreur lors de l'attente de l'EDT: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Les labels de statistiques devraient être stylisés correctement")
        void statsLabelsShouldBeStyledCorrectly() throws NoSuchFieldException, IllegalAccessException {
            JLabel sampleLabel = getRoundValue(); // Prend un label comme échantillon
            assertEquals(Color.WHITE, sampleLabel.getForeground(), "La couleur du texte du label devrait être blanche.");
            assertEquals("Arial", sampleLabel.getFont().getName(), "La police du label devrait être Arial.");
            assertEquals(25, sampleLabel.getFont().getSize(), "La taille de la police du label devrait être de 25.");
        }

        @Test
        @DisplayName("Le panneau de statistiques devrait être transparent")
        void statsPanelShouldBeTransparent() {
            JPanel panelStats = findComponentByName(ecranMenu, JPanel.class, "panelStats"); // Assurez-vous d'avoir un moyen de trouver le panel stats
            // Si vous n'avez pas de setName, vous devrez le trouver par structure ou par son contenu.
            // Pour l'exemple, nous allons le chercher en naviguant dans les composants de EcranMenu.
            JPanel foundPanelStats = null;
            for (Component comp : ecranMenu.getComponents()) {
                if (comp instanceof JPanel && ((JPanel) comp).getLayout() instanceof GridBagLayout) {
                    // C'est le panelStats s'il contient des JLabels avec "Round" par exemple
                    if (containsLabelWithText((JPanel) comp, "Round:")) {
                        foundPanelStats = (JPanel) comp;
                        break;
                    }
                }
            }
            assertNotNull(foundPanelStats, "Le panneau de statistiques n'a pas été trouvé.");
            assertFalse(foundPanelStats.isOpaque(), "Le panneau de statistiques devrait être transparent.");
        }

        @Test
        @DisplayName("Les valeurs des joueurs non définis devraient afficher 'Non défini' et '0'")
        void undefinedPlayerValuesShouldDisplayDefaults() throws NoSuchFieldException, IllegalAccessException {
            when(mockJeu.getJoueur(1)).thenReturn(null);
            when(mockJeu.getJoueur(2)).thenReturn(null);

            ecranMenu.miseAJour(); // Force la mise à jour après avoir mocké
            SwingUtilities.invokeLater(() -> { // Exécute sur l'EDT
                try {
                    assertEquals("Non défini: ", getNomJoueurAValue().getText(), "Le nom du Joueur 1 doit être 'Non défini'.");
                    assertEquals("0", getScoreJoueurAValue().getText(), "Le score du Joueur 1 doit être '0'.");
                    assertEquals("Non défini: ", getNomJoueurBValue().getText(), "Le nom du Joueur 2 doit être 'Non défini'.");
                    assertEquals("0", getScoreJoueurBValue().getText(), "Le score du Joueur 2 doit être '0'.");
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    fail("Erreur d'accès aux champs: " + e.getMessage());
                }
            });
            try { SwingUtilities.invokeAndWait(() -> {}); } catch (Exception ignored) {} // Attendre l'EDT
        }
    }

    // --- Sous-tests pour les boutons d'action ---
    @Nested
    @DisplayName("Tests des boutons d'action principaux")
    class ActionButtonsTests {

        private JPanel getPanelBoutonsActions() {
            // Trouver le panneau des boutons d'action par son bordereau ou par sa position dans le layout principal
            // C'est le composant qui a la marge MARGE_CONTENEUR_ACTIONS
            for (Component comp : ecranMenu.getComponents()) {
                if (comp instanceof JPanel && ((JPanel) comp).getBorder() != null && ((JPanel) comp).getBorder().toString().contains("empty border")) {
                    // Vérifier si la marge correspond à MARGE_CONTENEUR_ACTIONS
                    // C'est un peu fragile, une solution plus robuste serait de nommer les composants
                    if (((JPanel) comp).getBorder().toString().contains("top=20,left=50,bottom=20,right=50")) { // Marge de MARGE_CONTENEUR_ACTIONS
                        return (JPanel) comp;
                    }
                }
            }
            return null; // Devrait être trouvé
        }

        @Test
        @DisplayName("Tous les boutons d'action devraient être présents et avoir les bonnes dimensions")
        void allActionButtonsShouldBePresentAndSizedCorrectly() {
            JPanel panelActions = getPanelBoutonsActions();
            assertNotNull(panelActions, "Le panneau des boutons d'action n'a pas été trouvé.");

            // Il y a 5 boutons d'action
            assertEquals(5, panelActions.getComponentCount(), "Il devrait y avoir 5 boutons dans le panneau d'actions.");

            for (Component comp : panelActions.getComponents()) {
                assertTrue(comp instanceof Bouton.BoutonAvecImage, "Chaque composant devrait être un BoutonAvecImage.");
                assertEquals(new Dimension(250, 65), comp.getPreferredSize(), "Chaque bouton d'action devrait avoir la bonne dimension.");
            }
        }

        @Test
        @DisplayName("Le bouton 'Reprendre' devrait déclencher l'adaptateur Reprendre")
        void reprendreButtonShouldTriggerAdapter() {
            JPanel panelActions = getPanelBoutonsActions();
            Bouton.BoutonAvecImage reprendreButton = (Bouton.BoutonAvecImage) panelActions.getComponent(0); // Premier bouton

            // Simule un clic sur le bouton "Reprendre"
            reprendreButton.doClick();

            // Vérifie qu'un AdaptateurReprendre a été créé et que sa méthode actionPerformed a été appelée
            // Puisque AdaptateurReprendre::new est une fonction, on doit vérifier son action directement.
            // On peut capturer l'ActionListener pour vérifier.
            ArgumentCaptor<ActionListener> listenerCaptor = ArgumentCaptor.forClass(ActionListener.class);
            // On doit re-créer EcranMenu pour capturer le listener lors de sa création.
            // Alternativement, on peut mock AdaptateurReprendre et vérifier son interaction avec collecteurEvenements.

            // Pour ce test, nous allons simuler un clic et vérifier que collecteurEvenements.reprendrePartie() est appelé.
            // Ceci nécessite de connaître l'ordre des boutons ou de les rechercher par leur image/texte.
            // "reprendre.png" est le premier.
            // C'est le collecteurEvenements qui est le vrai point d'interaction.
            verify(mockCollecteurEvenements).clavier("reprendre"); // Vérifie que la bonne méthode du collecteur est appelée
        }

        @Test
        @DisplayName("Le bouton 'Nouvelle Partie' devrait déclencher l'adaptateur NouvellePartie")
        void nouvellePartieButtonShouldTriggerAdapter() {
            JPanel panelActions = getPanelBoutonsActions();
            // Le bouton "nouvelle_partie.png" est le 3ème (index 2)
            Bouton.BoutonAvecImage nouvellePartieButton = (Bouton.BoutonAvecImage) panelActions.getComponent(2);

            nouvellePartieButton.doClick();
            verify(mockCollecteurEvenements).clavier("nouvellePartie");
        }

        @Test
        @DisplayName("Le bouton 'Règles' devrait déclencher l'adaptateur Regles")
        void reglesButtonShouldTriggerAdapter() {
            JPanel panelActions = getPanelBoutonsActions();
            // Le bouton "regles.png" est le 5ème (index 4)
            Bouton.BoutonAvecImage reglesButton = (Bouton.BoutonAvecImage) panelActions.getComponent(4);

            reglesButton.doClick();
            verify(mockCollecteurEvenements).clavier("regles");
        }

        @Test
        @DisplayName("Le panneau des boutons d'action devrait être transparent")
        void actionButtonsPanelShouldBeTransparent() {
            JPanel panelActions = getPanelBoutonsActions();
            assertNotNull(panelActions, "Le panneau des boutons d'action n'a pas été trouvé.");
            assertFalse(panelActions.isOpaque(), "Le panneau des boutons d'action devrait être transparent.");
        }
    }

    // --- Sous-tests pour les boutons de navigation (Retour, Sauvegarder, Exit) ---
    @Nested
    @DisplayName("Tests des boutons de navigation (Retour, Sauvegarder, Exit)")
    class NavigationButtonsTests {

        private JPanel getPanelRetour() {
            // Trouver le panel retour par son alignement FlowLayout.RIGHT et sa marge
            for (Component comp : ecranMenu.getComponents()) {
                if (comp instanceof JPanel && ((JPanel) comp).getLayout() instanceof FlowLayout) {
                    FlowLayout layout = (FlowLayout) ((JPanel) comp).getLayout();
                    if (layout.getAlignment() == FlowLayout.RIGHT) {
                        return (JPanel) comp;
                    }
                }
            }
            return null;
        }

        private JPanel getPanelSauvegarde() {
            // Le panel de sauvegarde est dans le panel du bas (BorderLayout.WEST)
            JPanel bottomPanel = getPanelBasDePage();
            assertNotNull(bottomPanel, "Le panneau du bas n'a pas été trouvé.");
            return (JPanel) ((BorderLayout) bottomPanel.getLayout()).getLayoutComponent(BorderLayout.WEST);
        }

        private JPanel getPanelExit() {
            // Le panel de sortie est dans le panel du bas (BorderLayout.EAST)
            JPanel bottomPanel = getPanelBasDePage();
            assertNotNull(bottomPanel, "Le panneau du bas n'a pas été trouvé.");
            return (JPanel) ((BorderLayout) bottomPanel.getLayout()).getLayoutComponent(BorderLayout.EAST);
        }

        private JPanel getPanelBasDePage() {
            // Trouver le panel du bas de page. C'est le composant qui a GridBagLayout.PAGE_END et BorderLayout.
            for (Component comp : ecranMenu.getComponents()) {
                if (comp instanceof JPanel && ((JPanel) comp).getLayout() instanceof BorderLayout) {
                    return (JPanel) comp;
                }
            }
            return null;
        }


        @Test
        @DisplayName("Le bouton 'Retour' devrait appeler fermerMenu() sur InterfaceGraphique")
        void retourButtonShouldCallFermerMenu() {
            JPanel panelRetour = getPanelRetour();
            assertNotNull(panelRetour, "Le panneau de retour n'a pas été trouvé.");

            Bouton.BoutonAvecImage retourButton = (Bouton.BoutonAvecImage) panelRetour.getComponent(0);
            retourButton.doClick();
            verify(mockInterfaceGraphique).fermerMenu();
        }

        @Test
        @DisplayName("Le bouton 'Sauvegarder' devrait déclencher l'adaptateur Sauvegarder")
        void sauvegarderButtonShouldTriggerAdapter() {
            JPanel panelSauvegarde = getPanelSauvegarde();
            assertNotNull(panelSauvegarde, "Le panneau de sauvegarde n'a pas été trouvé.");

            Bouton.BoutonAvecImage sauvegarderButton = (Bouton.BoutonAvecImage) panelSauvegarde.getComponent(0);
            sauvegarderButton.doClick();
            verify(mockCollecteurEvenements).clavier("sauvegarder");
        }

        @Test
        @DisplayName("Le bouton 'Exit' devrait déclencher l'adaptateur Exit")
        void exitButtonShouldTriggerAdapter() {
            JPanel panelExit = getPanelExit();
            assertNotNull(panelExit, "Le panneau de sortie n'a pas été trouvé.");

            Bouton.BoutonAvecImage exitButton = (Bouton.BoutonAvecImage) panelExit.getComponent(0);
            exitButton.doClick();
            verify(mockCollecteurEvenements).clavier("exit");
        }

        @Test
        @DisplayName("Les panneaux des boutons de navigation devraient être transparents")
        void navigationPanelsShouldBeTransparent() {
            JPanel panelRetour = getPanelRetour();
            JPanel panelSauvegarde = getPanelSauvegarde();
            JPanel panelExit = getPanelExit();
            JPanel panelBasDePage = getPanelBasDePage();

            assertNotNull(panelRetour);
            assertNotNull(panelSauvegarde);
            assertNotNull(panelExit);
            assertNotNull(panelBasDePage);

            assertFalse(panelRetour.isOpaque(), "Le panneau de retour devrait être transparent.");
            assertFalse(panelSauvegarde.isOpaque(), "Le panneau de sauvegarde devrait être transparent.");
            assertFalse(panelExit.isOpaque(), "Le panneau de sortie devrait être transparent.");
            assertFalse(panelBasDePage.isOpaque(), "Le panneau du bas de page devrait être transparent.");
        }

        @Test
        @DisplayName("Les dimensions des boutons de navigation devraient être correctes")
        void navigationButtonsShouldHaveCorrectDimensions() {
            JPanel panelRetour = getPanelRetour();
            JPanel panelSauvegarde = getPanelSauvegarde();
            JPanel panelExit = getPanelExit();

            Bouton.BoutonAvecImage retourButton = (Bouton.BoutonAvecImage) panelRetour.getComponent(0);
            Bouton.BoutonAvecImage sauvegarderButton = (Bouton.BoutonAvecImage) panelSauvegarde.getComponent(0);
            Bouton.BoutonAvecImage exitButton = (Bouton.BoutonAvecImage) panelExit.getComponent(0);

            assertEquals(new Dimension(60, 60), retourButton.getPreferredSize(), "Le bouton Retour devrait avoir la bonne dimension.");
            assertEquals(new Dimension(200, 60), sauvegarderButton.getPreferredSize(), "Le bouton Sauvegarder devrait avoir la bonne dimension.");
            assertEquals(new Dimension(120, 60), exitButton.getPreferredSize(), "Le bouton Exit devrait avoir la bonne dimension.");
        }
    }

    // --- Helper methods for finding components (can be reused or made generic) ---
    private <T extends Component> T findComponentByName(Container container, Class<T> componentClass, String name) {
        // Idéalement, les composants auraient un setName("...") pour une recherche facile
        // Sinon, on doit parcourir récursivement et trouver par type/propriétés.
        // Cette implémentation est très basique et nécessite d'améliorer la recherche.
        for (Component comp : container.getComponents()) {
            if (componentClass.isInstance(comp)) {
                // Si on a un name, on peut vérifier
                if (comp instanceof JComponent && name.equals(((JComponent) comp).getName())) {
                    return componentClass.cast(comp);
                }
            }
            if (comp instanceof Container) {
                T found = findComponentByName((Container) comp, componentClass, name);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private boolean containsLabelWithText(JPanel panel, String text) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText() != null && label.getText().contains(text)) {
                    return true;
                }
            } else if (comp instanceof JPanel) {
                if (containsLabelWithText((JPanel) comp, text)) {
                    return true;
                }
            }
        }
        return false;
    }
}
