package Vue;

import Global.Config;
import Modele.CasePlateau;
import Modele.Coup;

import java.awt.*;
import java.util.List;

/**
 * Interface représentant un collecteur d'événements provenant de l'interface utilisateur.
 * Les classes qui implémentent cette interface sont responsables de traiter
 * les interactions de l'utilisateur (clavier, clics, changements de configuration)
 * et de les traduire en actions du jeu ou de l'application.
 */
public interface CollecteurEvenements {

    /**
     * Gère un événement de saisie au clavier.
     *
     * @param t La chaîne de caractères saisie au clavier. */
    void clavier(String t);


    /**
     * Gère un clic ou une interaction avec une case spécifique du plateau de jeu.
     * @param casePlateau L'objet CasePlateau correspondant à la case interagie.    */
    default void boutonTerrainJeu(CasePlateau casePlateau){};


    /**
     * Gère un événement de "tictac", généralement utilisé pour un timer de jeu
     * ou pour déclencher des actions périodiques.     */
    void tictac();


    /**
     * Gère la sélection d'une carte par l'utilisateur.
     * @param idCarte La carte sélectionnée.     */
    default void setCarteSelectionne(int idCarte){};


    /**
     * Définit la case de destination du pion sélectionné sur le Plateau.
     * @param casePlateau la case du plateau représentant la cible du déplacement.    */
    default void setCiblePion(CasePlateau casePlateau){};

    /**
     * Définit le pion sélectionné du joueur courant.
     * @param coordonnePion les coordonnées localisant le pion sélectionné.     */
    default void setCaseSelectionnee(Point coordonnePion){};


    /*****************************************************
     *      ÉCRAN DE DÉMARRAGE / CONFIGURATION         ***
     * (Événements venant des menus de config) ***
     *****************************************************/

    /**
     * Gère la sélection d'une partie sauvegardée à charger.
     * @param partieSelectionee Le nom ou l'identifiant de la partie à charger.     */
    default void setNouvellePartie(String partieSelectionee){};


    /**
     * Gère le réglage du niveau de difficulté pour une IA.
     * @param niveauIA Une chaîne représentant le niveau de l'IA (ex: "Facile", "Normal", "Fort").     */
    default void setNiveauIA(String niveauIA){};


    /**
     * Gère le chargement du nom d'un joueur.
     * @param num Le numéro du joueur (ex: 1 pour joueur 1, 2 pour joueur 2).
     * @param nom Le nouveau nom du joueur.     */
    default void setNomJoueur(int num, String nom){};


    /**
     * Gère le changement de couleur pour un élément spécifique de l'interface ou du jeu.
     * Nécessite l'énumération CiblesDesCouleurs pour identifier ce qui doit être coloré.
     * @param cible La cible du changement de couleur (définie dans Global.Config.CiblesDesCouleurs).
     * @param couleur La nouvelle couleur à appliquer.     */
    default void setCouleur(Config.CiblesDesCouleurs cible, Color couleur){};


    /*****************************************************
     *               INTERFACE TEXTUELLE                ***
     *****************************************************/

    /**
     * Définit la case de destination du pion sélectionné à l'aide de coordonnées.
     * Cette méthode est appelée après que le joueur a choisi la case cible vers laquelle déplacer un pion.
     * @param xDest La coordonnée X de la case cible.
     * @param yDest La coordonnée Y de la case cible.   */
    default void setCiblePion(int xDest, int yDest){};


    /**
     * Définit le pion sélectionné pour un déplacement en spécifiant ses coordonnées actuelles.
     * Cette méthode est utilisée lorsqu'un joueur choisit un pion à déplacer.
     * @param xDepart La coordonnée X du pion sélectionné.
     * @param yDepart La coordonnée Y du pion sélectionné.     */
    default void setCaseSelectionnee(int xDepart, int yDepart){};


    default void activeCibleBoutonTerrain(List<Coup> coupPossible, EcranPlateauDeJeu ecranPlateauDeJeu){};

    default  CollecteurEvenements getCollecteurAnimation(){return null;}

    default void desactiveCibleBoutonTerrain(List<Coup> coupPossible){};


    default void desactiveSuggestion(Coup coup, EcranPlateauDeJeu ecranPlateauDeJeu){};

    default void activeSuggestion(Coup coup, EcranPlateauDeJeu ecranPlateauDeJeu){};

}