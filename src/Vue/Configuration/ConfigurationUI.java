package Vue.Configuration;

import java.awt.*;

/**
 * Interface définissant le contrat pour la gestion de la configuration UI du jeu
 * Fournit les méthodes nécessaires pour accéder et modifier les couleurs des joueurs
 */
public interface ConfigurationUI {

    /**
     * Récupère le nom de la couleur du pion d'un joueur
     *
     * @param idJoueur Identifiant du joueur
     * @return Le nom de la couleur du pion
     */
    String getNomCouleurPionJoueur(int idJoueur);

    /**
     * Récupère la couleur du pion d'un joueur
     *
     * @param idJoueur Identifiant du joueur
     * @return La couleur du pion
     */
    Color getCouleurPionJoueur(int idJoueur);

    /**
     * Récupère la couleur de la case maître d'un joueur
     *
     * @param idJoueur Identifiant du joueur
     * @return La couleur de la case maître
     */
    Color getCouleurCaseMaitreJoueur(int idJoueur);

    /**
     * Définit la couleur de la case maître d'un joueur
     *
     * @param idJoueur Identifiant du joueur
     * @param couleur  Nouvelle couleur à définir
     */
    void setCouleurCaseMaitreJoueur(int idJoueur, Color couleur);

    /**
     * Récupère la couleur de la case élève d'un joueur
     *
     * @param idJoueur Identifiant du joueur
     * @return La couleur de la case élève
     */
    Color getCouleurCaseEleveJoueur(int idJoueur);

    /**
     * Définit la couleur de la case élève d'un joueur
     *
     * @param idJoueur Identifiant du joueur
     * @param couleur  Nouvelle couleur à définir
     */
    void setCouleurCaseEleveJoueur(int idJoueur, Color couleur);

    /**
     * Définit la couleur des pion d'un joueur
     *
     * @param idJoueur             Identifiant du Joueur
     * @param nomCouleurPionJoueur nom de la couleur
     */
    void setCouleurPion(int idJoueur, String nomCouleurPionJoueur);


    /**
     * Réinitialise les couleurs aux valeurs par défaut.
     * Cette méthode est utilisée pour restaurer les paramètres de couleur
     * des pions et des cases au sein de l'application ou du jeu,
     * en supprimant toutes les personnalisations effectuées.
     */
    void reinitialiserCouleurs();


    /**
     * Fournit une représentation textuelle de la configuration
     *
     * @return Une chaîne décrivant la configuration actuelle
     */
    @Override
    String toString();


}
