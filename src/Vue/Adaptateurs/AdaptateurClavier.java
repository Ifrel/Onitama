package Vue.Adaptateurs;

/*
 * Sokoban - Encore une nouvelle version (à but pédagogique) du célèbre jeu
 * Copyright (C) 2018 Guillaume Huard
 *
 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique Générale GNU publiée par la
 * Free Software Foundation (version 2 ou bien toute autre version ultérieure
 * choisie par vous).
 *
 * Ce programme est distribué car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but spécifique. Reportez-vous à la
 * Licence Publique Générale GNU pour plus de détails.
 *
 * Vous devez avoir reçu une copie de la Licence Publique Générale
 * GNU en même temps que ce programme ; si ce n'est pas le cas, écrivez à la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * États-Unis.
 *
 * Contact:
 *          Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'Hères
 */

import Vue.CollecteurEvenements;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

/**
 * Adaptateur qui transforme les événements clavier en commandes pour le jeu.
 * Cette classe implémente KeyListener et peut être attachée à n'importe quel composant Swing.
 */
public class AdaptateurClavier implements KeyListener {
    private static final Logger logger = Logger.getLogger(AdaptateurClavier.class.getName());
    private final CollecteurEvenements control;

    /**
     * Constructeur de l'adaptateur clavier.
     * 
     * @param c Le collecteur d'événements qui recevra les commandes générées par les touches du clavier
     */
    public AdaptateurClavier(CollecteurEvenements c) {
        control = c;
    }

    /**
     * Gère les événements de touche pressée et les transmet au collecteur d'événements.
     * 
     * @param event L'événement de touche pressée
     */
    @Override
    public void keyPressed(KeyEvent event) {
        String commande = null;

        switch (event.getKeyCode()) {
            // Gestion du jeu
            case KeyEvent.VK_U:
                commande = "annuler";
                break;
            case KeyEvent.VK_R:
                commande = "refaire";
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_P:
                commande = "pause";
                break;

            // Navigation
            case KeyEvent.VK_ESCAPE:
                commande = "full";
                break;
            case KeyEvent.VK_F:
                commande = "full";
                break;
            case KeyEvent.VK_ENTER:
                commande = "demarrer";
                break;
            case KeyEvent.VK_M:
                commande = "menu";
                break;

            // Actions
            case KeyEvent.VK_Q:
            case KeyEvent.VK_A:
                commande = "exit";
                break;
            case KeyEvent.VK_S:
                commande = "sauvegarder";
                break;
            case KeyEvent.VK_N:
                commande = "nouvellePartie";
                break;
            case KeyEvent.VK_H:
                commande = "regles";
                break;

            // IA
            case KeyEvent.VK_I:
                commande = "ia";
                break;

            // Navigation dans le menu avec les flèches
            case KeyEvent.VK_UP:
                commande = "haut";
                break;
            case KeyEvent.VK_DOWN:
                commande = "bas";
                break;
            case KeyEvent.VK_LEFT:
                commande = "gauche";
                break;
            case KeyEvent.VK_RIGHT:
                commande = "droite";
                break;
        }

        if (commande != null) {
            logger.info("Touche clavier: " + commande);
            control.clavier(commande);
        }
    }

    /**
     * Méthode appelée lorsqu'une touche est relâchée.
     * Cette méthode est requise par l'interface KeyListener mais n'est pas utilisée dans cette implémentation.
     * 
     * @param e L'événement de touche relâchée
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // Pas utilisé, mais doit être implémenté pour l'interface KeyListener.
    }

    /**
     * Méthode appelée lorsqu'une touche est tapée (pressée puis relâchée).
     * Cette méthode est requise par l'interface KeyListener mais n'est pas utilisée dans cette implémentation.
     * 
     * @param e L'événement de touche tapée
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // Non utilisé, mais doit être implémenté pour l'interface KeyListener.
    }


}
