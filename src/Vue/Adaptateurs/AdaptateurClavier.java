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


public class AdaptateurClavier implements KeyListener {
    CollecteurEvenements control;

    public AdaptateurClavier(CollecteurEvenements c) {
        control = c;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_U:
                control.clavier("annuler");
                System.out.println("annuler");
                break;
            case KeyEvent.VK_R:
                control.clavier("refaire");
                System.out.println("refaire");
                break;
            case KeyEvent.VK_Q:
            case KeyEvent.VK_A:
                control.clavier("exit");
                System.out.println("exit");
                break;
            case KeyEvent.VK_I:
                control.clavier("ia");
                System.out.println("ia");
                break;
            case KeyEvent.VK_ESCAPE:
                control.clavier("full");
                System.out.println("full");
                break;
            case KeyEvent.VK_ENTER:
                control.clavier("demarrer");
                break;
                case KeyEvent.VK_SPACE:
                    control.clavier("pause");
                    break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Pas utilisé, mais doit être implémenté.
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Non utilisé, mais doit être implémenté.
    }


}