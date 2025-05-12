package Vue;

import Global.Config;
import Modele.CasePlateau;
import Modele.Pion;

import java.awt.*;

/**
 * Interface représentant un collecteur d'événements provenant de l'interface utilisateur.
 * Les classes qui implémentent cette interface sont responsables de traiter
 * les interactions de l'utilisateur (clavier, clics, changements de configuration)
 * et de les traduire en actions du jeu ou de l'application.
 */
public interface CollecteurEvenements {

    /**
     * Gère un événement de saisie au clavier.
     * @param t La chaîne de caractères saisie au clavier.     */
    void clavier(String t);


    /**
     * Gère un clic ou une interaction avec une case spécifique du plateau de jeu.
     * @param casePlateau L'objet CasePlateau correspondant à la case interagie.    */
    void boutonTerrainJeu(CasePlateau casePlateau);


    /**
     * Gère un événement de "tictac", généralement utilisé pour un timer de jeu
     * ou pour déclencher des actions périodiques.     */
    void tictac();


    /**
     * Gère la sélection d'une carte par l'utilisateur.
     * @param numCarte Le numéro ou l'identifiant de la carte sélectionnée.     */
    void carteSelectionne(int numCarte);



    /**
     * Définit la case de destination du pion sélectionné sur le Plateau.
     * @param casePlateau la case du plateau représentant la cible du déplacement.    */
    void setCiblePion(CasePlateau casePlateau);


    /**
     * Définit le pion sélectionné du joueur courant.
     * @param pion L'objet représentant le pion sélectionné.     */
    void setPionSelectionne(Pion pion);



    /*****************************************************
     *      ÉCRAN DE DÉMARRAGE / CONFIGURATION         ***
     * (Événements venant des menus de config) ***
     *****************************************************/

    /**
     * Gère la sélection d'une partie sauvegardée à charger.
     * @param partieSelectionee Le nom ou l'identifiant de la partie à charger.     */
    void configChargerPartie(String partieSelectionee);


    /**
     * Gère le réglage du niveau de difficulté pour une IA.
     * @param niveauIA Une chaîne représentant le niveau de l'IA (ex: "Facile", "Normal", "Difficile").     */
    void configNiveauIA(String niveauIA);


    /**
     * Gère le chargement du nom d'un joueur.
     * @param num Le numéro du joueur (ex: 1 pour joueur 1, 2 pour joueur 2).
     * @param nom Le nouveau nom du joueur.     */
    void configNomJoueur(int num, String nom);



    /**
     * Gère l'activation ou la désactivation d'un mode de jeu automatique
     * impliquant potentiellement des IA.
     * @param nouvelEtat Le nouvel état du mode automatique (true pour activé, false pour désactivé).     */
    void configModeAuto(boolean nouvelEtat);


    /**
     * Gère le réglage du temps de réflexion accordé à une IA (en millisecondes).
     * @param tempsMs Le temps de réflexion en millisecondes.     */
    void configIAReflexion(int tempsMs);



    /**
     * Gère l'activation ou la désactivation d'une heuristique spécifique pour l'IA.
     * @param active True pour activer l'heuristique, false pour la désactiver.     */
    void configIAHeuristique(boolean active);



    /**
     * Gère la sélection de l'algorithme utilisé par l'IA.
     * @param nomAlgorithme Le nom de l'algorithme d'IA sélectionné.     */
    void configIAAlgorithme(String nomAlgorithme);



    /**
     * Gère le changement de couleur pour un élément spécifique de l'interface ou du jeu.
     * Nécessite l'énumération CiblesDesCouleurs pour identifier ce qui doit être coloré.
     * @param cible La cible du changement de couleur (définie dans Global.Config.CiblesDesCouleurs).
     * @param couleur La nouvelle couleur à appliquer.     */
    void configCouleur(Config.CiblesDesCouleurs cible, Color couleur);



    /**
     * Gère le réglage de la vitesse des animations dans le jeu.
     * @param vitesse La nouvelle vitesse des animations (valeur typiquement comprise dans une plage prédéfinie).     */
    void configAnimationVitesse(int vitesse);



    /**
     * Gère l'activation ou la désactivation des animations pour les pièces (pions, etc.) sur le plateau.
     * @param active True pour activer les animations des pièces, false pour les désactiver.     */
    void configAnimationPieces(boolean active);


    /**
     * Gère l'activation ou la désactivation des animations de surbrillance
     * (par exemple, pour indiquer les coups possibles ou les éléments sélectionnés).
     * @param active True pour activer la surbrillance animée, false pour la désactiver.*/
    void configAnimationSurbrillance(boolean active);



    /**
     * Gère le réglage du volume général du son de l'application.
     * @param volume Le nouveau niveau de volume général.   */
    void configSonVolumeGeneral(int volume);



    /**
     * Gère le réglage du volume des effets sonores.
     * @param volume Le nouveau niveau de volume des effets.    */
    void configSonVolumeEffets(int volume);



    /**
     * Gère le réglage du volume de la musique de fond.
     * @param volume Le nouveau niveau de volume de la musique.   */
    void configSonVolumeMusique(int volume);



    /**
     * Gère l'activation ou la désactivation du mode muet.
     * @param muet True pour activer le mode muet, false pour le désactiver.     */
    void configSonMuet(boolean muet);




    /*****************************************************
     *               INTERFACE TEXTUELLE                ***
     *****************************************************/

    /**
     * Définit la case de destination du pion sélectionné à l'aide de coordonnées.
     * Cette méthode est appelée après que le joueur a choisi la case cible vers laquelle déplacer un pion.
     * @param xDest La coordonnée X de la case cible.
     * @param yDest La coordonnée Y de la case cible.   */
    void setCiblePion(int xDest, int yDest);


    /**
     * Définit le pion sélectionné pour un déplacement en spécifiant ses coordonnées actuelles.
     * Cette méthode est utilisée lorsqu'un joueur choisit un pion à déplacer.
     * @param xDepart La coordonnée X du pion sélectionné.
     * @param yDepart La coordonnée Y du pion sélectionné.     */
    void setPionSelectionne(int xDepart, int yDepart);

}