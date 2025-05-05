package Modele;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class HistoriqueTest<T> {
    @Test
    public void peutAnnuler() {
        Historique<Coup> h = new Historique<>();
        assertFalse(h.peutAnnuler());
        h.add(new Coup(new Point(1, 2), new Point(45, 1234)));
        assertTrue(h.peutAnnuler());
    }

    @Test
    public void peutRefaire() {
        Historique<Coup> h = new Historique<>();
        assertFalse(h.peutRefaire());
        h.add(new Coup(new Point(1, 2), new Point(45, 1234)));
        assertFalse(h.peutRefaire());
        h.annuler();
        assertTrue(h.peutRefaire());
    }

    @Test
    public void annuler() {
        Historique<Coup> h = new Historique<>();
        Coup c = new Coup(new Point(42, 23), new Point(26, 1234));
        h.add(c);
        assertEquals(h.annuler(), c);
    }

    @Test
    public void refaire() {
        Historique<Coup> h = new Historique<>();
        Coup c = new Coup(new Point(42, 23), new Point(26, 1234));
        h.add(c);
        assertEquals(h.annuler(), c);
        assertEquals(h.refaire(), c);
    }
}