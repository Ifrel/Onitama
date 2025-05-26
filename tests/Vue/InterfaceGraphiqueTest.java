package Vue;

import Modele.Jeu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InterfaceGraphiqueTest {

    private Jeu jeuMock;
    private CollecteurEvenements collecteurEvenementsMock;
    private InterfaceGraphique interfaceGraphique;

    @BeforeEach
    public void setUp() {
        jeuMock = mock(Jeu.class);
        collecteurEvenementsMock = mock(CollecteurEvenements.class);
        interfaceGraphique = new InterfaceGraphique(jeuMock, collecteurEvenementsMock);
    }

    @Test
    public void testLancerInterfaceGraphique() {
        assertDoesNotThrow(() -> InterfaceGraphique.lancerInterfaceGraphique(jeuMock, collecteurEvenementsMock));

        // Verify that the observable (Jeu) added the InterfaceGraphique observer
        verify(jeuMock, times(1)).ajouteObservateur(interfaceGraphique);
    }

    @Test
    public void testLancerInterfaceGraphiqueLogsMessages() {
        Logger loggerMock = mock(Logger.class);
        when(loggerMock.isLoggable(any())).thenReturn(true);

        assertDoesNotThrow(() -> InterfaceGraphique.lancerInterfaceGraphique(jeuMock, collecteurEvenementsMock));

        // Verify that logging methods are invoked during the method call
        verify(loggerMock, atLeastOnce()).info(contains("Lancement interface graphique"));
        verify(loggerMock, atLeastOnce()).info(contains("Interface graphique lancée"));
    }

    @Test
    public void testLancerInterfaceGraphiqueInvokesRun() {
        InterfaceGraphique mockGraphique = mock(InterfaceGraphique.class);
        doNothing().when(mockGraphique).run();

        SwingUtilities.invokeLater(() -> {
            InterfaceGraphique.lancerInterfaceGraphique(jeuMock, collecteurEvenementsMock);
            verify(mockGraphique, times(1)).run(); // Ensure run is invoked
        });
    }

    @Test
    public void testFrameInitializationOnRun() {
        SwingUtilities.invokeLater(() -> interfaceGraphique.run());

        try {
            SwingUtilities.invokeAndWait(() -> {
                JFrame frame = (JFrame) SwingUtilities.getRootPane(interfaceGraphique).getParent();
                assertTrue(frame.isVisible(), "Frame should be visible after running the interface.");
                assertEquals("Onitama", frame.getTitle(), "Frame title should be 'Onitama'.");
            });
        } catch (Exception e) {
            fail("SwingUtilities.invokeAndWait should not throw an exception");
        }
    }
}