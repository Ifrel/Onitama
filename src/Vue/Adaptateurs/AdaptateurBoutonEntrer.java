package Vue.Adaptateurs;

import Vue.CollecteurEvenements;
import Vue.Configuration.InfosDeConfigUI;
import Vue.InterfaceGraphique;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import static Vue.Configuration.ConfigUI.INDICATION_SELECTION;
import static Vue.Configuration.ConfigUI.OPTION_IA_NON;

/**
 * Adaptateur pour le bouton "Entrer" de l'interface de configuration.
 * Gère les actions à effectuer lors du clic sur ce bouton,
 * en fonction du mode de jeu sélectionné (Auto IA, IA vs Joueur, Joueur vs Joueur, Reprendre partie).
 */
public class AdaptateurBoutonEntrer implements ActionListener {
    private boolean modeAutoIA;
    private String partieSelectionnee;
    private String niveauIAselectione;
    private JTextField champJoueur1;
    private JTextField champJoueur2;
    private static final Logger logger = Logger.getLogger(AdaptateurBoutonEntrer.class.getName());

    private final CollecteurEvenements collecteurEvent; // Interface pour notifier les événements au contrôleur.
    InterfaceGraphique interfaceGraphique;


    /**
     * Constructeur de l'AdaptateurBoutonEntrer.
     *
     * @param collecteurEvent L'instance du CollecteurEvenements pour notifier les actions.
     * @complexity O(1) - Complexité constante, se contente d'assigner la référence.
     */
    public AdaptateurBoutonEntrer(CollecteurEvenements collecteurEvent, InterfaceGraphique interfaceGraphique) {
        this.collecteurEvent = collecteurEvent;
        this.interfaceGraphique = interfaceGraphique;
    }



    /**
     * Méthode appelée lors du clic sur le bouton "Entrer".
     * Détermine l'action à effectuer en fonction des différents modes de jeu configurés.
     *
     * @param e L'événement ActionEvent généré par le clic sur le bouton.
     * @complexity O(1) en général. La complexité des opérations à l'intérieur des conditions est constante.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info("bouton: Entrer pressé");

        if (modeAutoIA) {
            activerModeAutoIA();
        } else if (!niveauIAselectione.equals(OPTION_IA_NON)) {
            if (!verifierNomSaisi(champJoueur1, 1)) return;
            activerModeIAVsJoueur();
        } else if (!partieSelectionnee.equals(INDICATION_SELECTION)) {
            reprendrePartie();
        } else {
            if (!verifierNomSaisi(champJoueur1, 1)) return;
            if (!verifierNomSaisi(champJoueur2, 2)) return;
            activerModeJoueurVsJoueur();
        }

        System.err.println(InfosDeConfigUI.getInstance());
        interfaceGraphique.lancerPlateauDeJeu();
    }




    private void activerModeAutoIA() {
        collecteurEvent.setModeAuto(true);
        logger.info("Mode Auto (IA vs IA) activé");
    }

    private void activerModeIAVsJoueur() {
        collecteurEvent.setNiveauIA(niveauIAselectione);
        collecteurEvent.setNomJoueur(1, champJoueur1.getText());
        logger.info("Mode IA (" + niveauIAselectione + ") vs " + champJoueur1.getText() + " activé");
    }

    private void reprendrePartie() {
        collecteurEvent.setNouvellePartie(partieSelectionnee);
        logger.info("Mode Reprendre une partie (" + partieSelectionnee + ") activé");
    }

    private void activerModeJoueurVsJoueur() {
        collecteurEvent.setNomJoueur(1, champJoueur1.getText());
        collecteurEvent.setNomJoueur(2, champJoueur2.getText());
        logger.info("Mode " + champJoueur1.getText() + " vs " + champJoueur2.getText() + " activé");
    }

    private boolean verifierNomSaisi(JTextField champ, int numeroJoueur) {
        if (champ.getText().trim().isEmpty()) {
            champ.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            logger.severe("Erreur saisie nom " + numeroJoueur);
            return false;
        }
        return true;
    }


    /**
     * Définit l'état du mode Auto IA.
     *
     * @param modeAutoIA Le nouvel état du mode Auto IA (true pour activé, false pour désactivé).
     * @complexity O(1) - Complexité constante, se contente d'assigner une valeur.
     */
    public void setModeAutoIA(boolean modeAutoIA) {
        this.modeAutoIA = modeAutoIA;
    }


    /**
     * Définit la partie sélectionnée pour être reprise.
     *
     * @param partieSelectionnee Le nom de la partie sélectionnée.
     * @complexity O(1) - Complexité constante, se contente d'assigner une valeur.
     */
    public void setPartieSelectionnee(String partieSelectionnee) {
        this.partieSelectionnee = partieSelectionnee;
    }


    /**
     * Définit le niveau de difficulté de l'IA sélectionné.
     *
     * @param niveauIAselectione Le niveau de l'IA sélectionné.
     * @complexity O(1) - Complexité constante, se contente d'assigner une valeur.
     */
    public void setNiveauIAselectione(String niveauIAselectione) {
        this.niveauIAselectione = niveauIAselectione;
    }



    /**
     * Définit le champ de texte correspondant à un joueur (1 ou 2).
     *
     * @param num        Le numéro du joueur (1 ou 2).
     * @param champJoueur Le JTextField contenant le nom du joueur.
     * @complexity O(1) - Complexité constante, effectue une comparaison et une assignation.
     */
    public void setChampJoueur(int num, JTextField champJoueur) {
        if (num == 1) this.champJoueur1 = champJoueur;
        else this.champJoueur2 = champJoueur;
    }
}