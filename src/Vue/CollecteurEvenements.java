package Vue;

import Global.Config;

import java.awt.*;

public interface CollecteurEvenements {
    ///  gère les entiers claviers
    void clavier(String t);

    /// gère les boutons
    void boutonTerrainJeu(Point btnCoords);

    void tictac();

    void carteSelectionne(int numCarte);

    // --- Configuration général (Ecran De démarrage)
    void configChargerPartie(String partieSelectionee);
    void configNiveauIA(String niveauIA);
    void configNomJoueur(int num, String nom);

    // --- IA ---
    void configModeAuto(boolean nouvelEtat);
    void configIAReflexion(int tempsMs);
    void configIAHeuristique(boolean active);
    void configIAAlgorithme(String nomAlgorithme);

    // --- Couleur ---
    // Utiliser l'enum CilblesDesCouleurs défini dans EcranDeDemarrage
    void configCouleur(Config.CiblesDesCouleurs cible, Color couleur);

    // --- Animation ---
    void configAnimationVitesse(int vitesse); // Ou float, selon l'échelle
    void configAnimationPieces(boolean active);
    void configAnimationSurbrillance(boolean active);

    // --- Son ---
    void configSonVolumeGeneral(int volume);
    void configSonVolumeEffets(int volume);
    void configSonVolumeMusique(int volume);
    void configSonMuet(boolean muet);

    // ... autres méthodes (lancerPartie, quitter, etc.)

}

