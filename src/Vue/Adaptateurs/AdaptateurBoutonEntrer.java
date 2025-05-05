package Vue.Adaptateurs;

import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import static Vue.ConfigUI.INDICATION_SELECTION;
import static Vue.ConfigUI.OPTION_IA_NON;

/**
 * Adaptateur pour le bouton "Entrer" de l'interface de configuration.
 * Gère les actions à effectuer lors du clic sur ce bouton,
 * en fonction du mode de jeu sélectionné (Auto IA, IA vs Joueur, Joueur vs Joueur, Reprendre partie).
 */
public class AdaptateurBoutonEntrer implements ActionListener {
    private boolean modeAutoIA;             // Indique si le mode de jeu Auto IA (IA vs IA) est activé.
    private String partieSelectionnee;      // Nom de la partie sélectionnée pour être reprise.
    private String niveauIAselectione;      // Niveau de difficulté de l'IA sélectionné.
    private JTextField champJoueur1;        // Champ de texte pour le nom du joueur 1.
    private JTextField champJoueur2;        // Champ de texte pour le nom du joueur 2.

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
        System.err.println("bouton: Entrer préssé");

        // Si le mode Auto IA est activé (IA vs IA)
        if (modeAutoIA) {
            collecteurEvent.configModeAuto(modeAutoIA); // Notifie le collecteur d'événements du mode Auto IA.
            System.err.println("Mode Auto (IA vs IA) activé");
        }
        // Si le mode Auto IA n'est pas activé
        else {
            // Si un niveau d'IA autre que "Non" est sélectionné (IA vs Joueur)
            if (!niveauIAselectione.equals(OPTION_IA_NON)) {
                // Vérifie si le nom du joueur 1 a été saisi
                if (Objects.equals(champJoueur1.getText(), "")) {
                    champJoueur1.setBorder(BorderFactory.createLineBorder(Color.RED, 2)); // Définit une bordure rouge pour indiquer une erreur de saisie.
                    System.err.println("Erreur saisie nom 1");
                    return;
                } else {
                    collecteurEvent.configNiveauIA(niveauIAselectione); // Notifie le collecteur du niveau de l'IA.
                    collecteurEvent.configNomJoueur(1, champJoueur1.getText()); // Notifie le collecteur du nom du joueur 1.
                    System.err.println("Mode IA (" + niveauIAselectione + ") vs " + champJoueur1.getText() + " activé");
                }
            }
            // Si aucun niveau d'IA n'est sélectionné ("Non")
            else {
                // Si une partie à reprendre a été sélectionnée
                if (!partieSelectionnee.equals(INDICATION_SELECTION)) {
                    collecteurEvent.configChargerPartie(partieSelectionnee); // Notifie le collecteur de la partie à charger.
                    System.err.println("Mode Reprendre une partie (" + partieSelectionnee + ") activé");
                }
                // Si aucune partie à reprendre n'a été sélectionnée (nouveau jeu Joueur vs Joueur)
                else {
                    // Vérifie si le nom du joueur 1 a été saisi
                    if (Objects.equals(champJoueur1.getText(), "")) {
                        champJoueur1.setBorder(BorderFactory.createLineBorder(Color.RED, 2)); // Indique une erreur de saisie pour le joueur 1.
                        System.err.println("Erreur saisie nom 1");
                        return;
                    }
                    // Vérifie si le nom du joueur 2 a été saisi
                    else if (Objects.equals(champJoueur2.getText(), "")) {
                        champJoueur2.setBorder(BorderFactory.createLineBorder(Color.RED, 2)); // Indique une erreur de saisie pour le joueur 2.
                        System.err.println("Erreur saisie nom 2");
                        return;
                    }
                    // Si les noms des deux joueurs ont été saisis
                    else {
                        collecteurEvent.configNomJoueur(1, champJoueur1.getText()); // Notifie le nom du joueur 1.
                        collecteurEvent.configNomJoueur(2, champJoueur2.getText()); // Notifie le nom du joueur 2.
                        System.err.println("Mode " + champJoueur1.getText() + " vs " + champJoueur2.getText() + " activé");
                    }
                }
            }
        }


        interfaceGraphique.lancerPlatauDeJeu();
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