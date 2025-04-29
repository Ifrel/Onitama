package Vue;

import java.awt.*;

public interface CollecteurEvenements {
    ///  gère les entiers claviers
    void clavier(String t);

    /// gère les boutons
    void boutonTerrainJeu(Point btnCoords);

    void tictac();

    void carteSelectionne(int numCarte);
}

