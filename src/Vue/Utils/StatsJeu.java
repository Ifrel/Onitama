package Vue.Utils;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

/**
 * Classe singleton gérant les statistiques du jeu.
 * Cette classe centralise toutes les statistiques comme le score, le temps de jeu,
 * le nombre de rounds, etc. Elle assure la cohérence des données entre les différents écrans.
 */
public class StatsJeu {
    private static final Logger LOGGER = Logger.getLogger(StatsJeu.class.getName());
    private static volatile StatsJeu instance;

    // Statistiques de jeu
    private int nombeParties;
    private Duration dureePartie;
    private Instant debutPartie;
    private String nomJoueur1;
    private String nomJoueur2;
    private int scoreJoueur1;
    private int scoreJoueur2;

    // États de la partie
    private boolean partieEnCours;
    private boolean partieEnPause;

    /**
     * Constructeur privé pour le pattern Singleton.
     * Initialise les statistiques avec des valeurs par défaut.
     */
    private StatsJeu() {
        resetStats();
    }

    /**
     * Obtient l'instance unique de StatsJeu.
     * Utilise le pattern "Double-Checked Locking" pour la thread-safety.
     *
     * @return L'instance unique de StatsJeu
     */
    public static StatsJeu getInstance() {
        if (instance == null) {
            synchronized (StatsJeu.class) {
                if (instance == null) {
                    instance = new StatsJeu();
                }
            }
        }
        return instance;
    }

    /**
     * Incrémente le compteur de rounds et notifie le changement.
     */
    public synchronized void incrementerNombreParties() {
        nombeParties++;
        LOGGER.info("Round incrémenté : " + nombeParties);
    }

    /**
     * Met à jour la durée de la partie si elle est en cours.
     */
    public synchronized void updateDureePartie() {
        if (partieEnCours && !partieEnPause) {
            dureePartie = Duration.between(debutPartie, Instant.now());
        }
    }

    /**
     * Réinitialise toutes les statistiques à leurs valeurs par défaut.
     */
    public synchronized void resetStats() {
        nombeParties = 1;
        dureePartie = Duration.ZERO;
        debutPartie = Instant.now();
        scoreJoueur1 = 0;
        scoreJoueur2 = 0;
        nomJoueur1 = "";
        nomJoueur2 = "";
        partieEnCours = false;
        partieEnPause = false;
        LOGGER.info("Statistiques réinitialisées");
    }

    /**
     * Démarre le chronométrage d'une nouvelle partie.
     */
    public synchronized void demarrerPartie() {
        partieEnCours = true;
        partieEnPause = false;
        debutPartie = Instant.now();
        LOGGER.info("Partie démarrée");
    }

    /**
     * Met la partie en pause.
     */
    public synchronized void mettreEnPause() {
        if (partieEnCours) {
            partieEnPause = true;
            LOGGER.info("Partie mise en pause");
        }
    }

    /**
     * Reprend la partie après une pause.
     */
    public synchronized void reprendrePartie() {
        if (partieEnCours && partieEnPause) {
            partieEnPause = false;
            debutPartie = Instant.now().minus(dureePartie);
            LOGGER.info("Partie reprise");
        }
    }

    // Getters
    public synchronized int getNombreParties() { return nombeParties; }
    public synchronized Duration getDureePartie() { return dureePartie; }
    public synchronized String getNomJoueur1() { return nomJoueur1; }
    public synchronized String getNomJoueur2() { return nomJoueur2; }
    public synchronized int getScoreJoueur1() { return scoreJoueur1; }
    public synchronized int getScoreJoueur2() { return scoreJoueur2; }
    public synchronized boolean isPartieEnCours() { return partieEnCours; }
    public synchronized boolean isPartieEnPause() { return partieEnPause; }

    // Setters avec validation
    /**
     * Définit le nom du joueur 1.
     * @param nom Le nouveau nom (non null)
     */
    public synchronized void setNomJoueur1(String nom) {
        this.nomJoueur1 = nom != null ? nom : "Joueur 1";
        LOGGER.info("Nom du joueur 1 défini : " + nomJoueur1);
    }

    /**
     * Définit le nom du joueur 2.
     * @param nom Le nouveau nom (non null)
     */
    public synchronized void setNomJoueur2(String nom) {
        this.nomJoueur2 = nom != null ? nom : "Joueur 2";
        LOGGER.info("Nom du joueur 2 défini : " + nomJoueur2);
    }

    /**
     * Met à jour le score du joueur 1.
     * @param score Le nouveau score (doit être positif)
     */
    public synchronized void setScoreJoueur1(int score) {
        this.scoreJoueur1 = Math.max(0, score);
        LOGGER.info("Score du joueur 1 mis à jour : " + scoreJoueur1);
    }

    /**
     * Met à jour le score du joueur 2.
     * @param score Le nouveau score (doit être positif)
     */
    public synchronized void setScoreJoueur2(int score) {
        this.scoreJoueur2 = Math.max(0, score);
        LOGGER.info("Score du joueur 2 mis à jour : " + scoreJoueur2);
    }

    public void setDuration(Duration duration) {
        this.dureePartie = duration;
    }

    public void setPartieEnCours(boolean partieEnCours) {
    }

}