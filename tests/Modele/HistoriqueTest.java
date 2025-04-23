package Modele;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoriqueTest<T> {
    @Test
    public void peutAnnuler() {
        Historique<Coup> h = new Historique<>();
        assertFalse(h.peutAnnuler());
        h.add(new Coup(1, 1));
        assertTrue(h.peutAnnuler());
    }

    @Test
    public void peutRefaire() {
        Historique<Coup> h = new Historique<>();
        assertFalse(h.peutRefaire());
        h.add(new Coup(1, 1));
        assertFalse(h.peutRefaire());
        h.annuler();
        assertTrue(h.peutRefaire());
    }

    @Test
    public void annuler() {
        Historique<Coup> h = new Historique<>();
        Coup c = new Coup(42, 42);
        h.add(c);
        assertEquals(h.annuler(), c);
    }

    @Test
    public void refaire() {
        Historique<Coup> h = new Historique<>();
        Coup c = new Coup(42, 42);
        h.add(c);
        assertEquals(h.annuler(), c);
        assertEquals(h.refaire(), c);
    }
}