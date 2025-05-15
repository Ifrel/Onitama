package Modele;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

import static Global.Config.TYPECARTE.*;

class HistoriqueTest<T> {
    @Test
    public void peutAnnuler() {
        Historique<Coup> h = new Historique<>();
        assertFalse(h.peutAnnuler());
        h.add(new Coup(new Point(1, 2), new Point(45, 1234),new Carte(COQ)));
        assertTrue(h.peutAnnuler());
    }

    @Test
    public void peutRefaire() {
        Historique<Coup> h = new Historique<>();
        assertFalse(h.peutRefaire());
        h.add(new Coup(new Point(1, 2), new Point(45, 1234),new Carte(COQ)));
        assertFalse(h.peutRefaire());
        h.annuler();
        assertTrue(h.peutRefaire());
    }

    @Test
    public void annuler() {
        Historique<Coup> h = new Historique<>();
        Coup c = new Coup(new Point(1, 2), new Point(45, 1234), new Carte(COQ));
        h.add(c);
        assertEquals(h.annuler(), c);
    }

    @Test
    public void refaire() {
        Historique<Coup> h = new Historique<>();
        Coup c = new Coup(new Point(1, 2), new Point(45, 1234), new Carte(COQ));
        h.add(c);
        assertEquals(h.annuler(), c);
        assertEquals(h.refaire(), c);
    }
}