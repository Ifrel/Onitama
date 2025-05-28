package Controleur;

import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;
import Vue.Utils.AfficheReglesPDF;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

import static Global.Config.NIVEAU_IA.*;
import static Vue.Utils.MethodsStaticsUtils.afficherFonctionEnCours;

public class Mediateur implements CollecteurEvenements {
    private final Jeu jeu;
    private final CollecteurEvenements controleurAnimation;
    private InterfaceGraphique vue;

    private static final Logger logger = Logger.getLogger(Mediateur.class.getName());

    public Mediateur(Jeu j) {
        this.jeu = j;
        controleurAnimation = new ControleurAnimation();
    }

    @Override
   synchronized public void clavier(String touche) {
        logger.info("Touche clavier : " + touche);
        try {
            vue = InterfaceGraphique.getInstance();
            switch (touche) {
                case "exit":
//                    jeu.setTerminerJeu();
                    System.exit(0);
                    break;
                case "exit-menu":
                    if (demanderSauvegardeAvantQuitter()) {
                        System.exit(0);
                    }
                    break;
                case "annuler":
                    jeu.annulerCoup();
                    break;
                case "refaire":
                    jeu.refaireCoup();
                    break;
                case "didacticiel":
                case "mesParties":
                    afficherFonctionEnCours();
                    break;
                case "nouvellePartie":
                    vue.demarrerNouvellePartie();
                    break;
                case "sauvegarder":
                    String nomFichier = demanderNomFichierSauvegarde();
                    if (nomFichier != null) {
                        // La sauvegarde a réussi et l'utilisateur a été informé
                        logger.info("Sauvegarde effectuée dans : " + nomFichier);
                    } else {
                        // Soit l'utilisateur a annulé, soit une erreur s'est produite
                        logger.info("Sauvegarde annulée ou échouée");
                    }
                    break;
                case "regles":
                    // Recherche de la fenêtre ayant le focus
                    Window fenetreActive = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
                    AfficheReglesPDF.ouvrirReglesPDFExterne(fenetreActive);
                    break;
                case "pause":
                    jeu.setPause();
                    break;
                case "full":
                    vue.toggleFullScreen();
                    break;
                case "demarrer":
                    vue.lancerPlateauDeJeu();
                    break;
                case "Humain-1":
                    if (jeu.estActiveIA1())
                        jeu.toggleIA1();
                    break;
                case "IA - Facile-1":
                    if (!jeu.estActiveIA1())
                        jeu.toggleIA1();
                    jeu.setNiveauIA1(FAIBLE);
                    break;
                case "IA - Moyen-1":
                    if (!jeu.estActiveIA1())
                        jeu.toggleIA1();
                    jeu.setNiveauIA1(MOYEN);
                    break;
                case "IA - Difficile-1":
                    if (!jeu.estActiveIA1())
                        jeu.toggleIA1();
                    jeu.setNiveauIA1(FORT);
                    break;
                case "Humain-2":
                    if (jeu.estActiveIA2())
                        jeu.toggleIA2();
                    break;
                case "IA - Facile-2":
                    if (!jeu.estActiveIA2())
                        jeu.toggleIA2();
                    jeu.setNiveauIA2(FAIBLE);
                    break;
                case "IA - Moyen-2":
                    if (!jeu.estActiveIA2())
                        jeu.toggleIA2();
                    jeu.setNiveauIA2(MOYEN);
                    break;
                case "IA - Difficile-2":
                    if (!jeu.estActiveIA2())
                        jeu.toggleIA2();
                    jeu.setNiveauIA2(FORT);
                    break;
                case "ia vs ia":
                    if (!jeu.estActiveIA1()) {
                        jeu.toggleIA1();
                    }

                    if (!jeu.estActiveIA2()) {
                        jeu.toggleIA2();
                    }

                    Thread.sleep(1000);
                    logger.info("Lancement du mode IA(" + jeu.getNiveauIA1() + ") vs IA(" + jeu.getNiveauIA2() + ")");
                    jeu.toggleIAvsIA();
                    break;
                default:
                    logger.severe("Touche inconnue : " + touche);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void tictac() {
        //TODO
    }


    @Override
    public void setCiblePion(int xDest, int yDest) {
        jeu.selectionneCase(new Point(xDest, yDest));
    }

    @Override
    public void setCaseSelectionnee(int xDepart, int yDepart) {
        jeu.selectionneCase(new Point(xDepart, yDepart));
    }

    @Override
    public void setCaseSelectionnee(Point coordonnePion) {
        jeu.selectionneCase(coordonnePion);
    }

    @Override
    public void setCarteSelectionne(int idCarte) {
        jeu.setCarteSelectionnee(idCarte);
    }




    /****************************************
     *  Méthodes de gestion du controleur
     *  ************************************/
    @Override
    public CollecteurEvenements getCollecteurAnimation(){
        return controleurAnimation;
    }


    public String demanderNomFichierSauvegarde() {
        // Récupérer la liste des sauvegardes existantes
        String[] sauvegardesExistantes = jeu.listerSauvegardes().toArray(new String[0]);

        // Créer le panneau personnalisé
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridLayout(3, 1));

        // Ajouter un label explicatif
        panel.add(new javax.swing.JLabel("Choisir une sauvegarde existante :"));

        // Créer la liste déroulante avec les sauvegardes existantes
        JComboBox<String> comboBox = new JComboBox<>(sauvegardesExistantes);
        comboBox.setEditable(true);
        panel.add(comboBox);

        // Ajouter un label pour la nouvelle sauvegarde
        panel.add(new JLabel("Ou entrez un nouveau nom :"));

        // Afficher la boîte de dialogue
        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Sauvegarde du jeu",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        // Traiter le résultat
        if (result == JOptionPane.OK_OPTION) {
            String nomFichier = comboBox.getSelectedItem().toString().trim();

            if (nomFichier.isEmpty()) {
                logger.severe("Nom de fichier vide");
                return null;
            }

            // Ajouter l'extension .sav si nécessaire
            if (!nomFichier.toLowerCase().endsWith(".sav")) {
                nomFichier += ".sav";
            }

            // Si le fichier existe déjà, demander confirmation
            if (java.util.Arrays.asList(sauvegardesExistantes).contains(nomFichier)) {
                int confirmer = javax.swing.JOptionPane.showConfirmDialog(
                        null,
                        "Le fichier " + nomFichier + " existe déjà. Voulez-vous le remplacer ?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmer != JOptionPane.YES_OPTION) {
                    return null;
                }
            }

            try {
                // Effectuer la sauvegarde ici
                jeu.sauvegarderJeu(nomFichier);

                // Afficher le message de confirmation avec une icône de succès
                JOptionPane.showMessageDialog(
                        null,
                        "La partie a été sauvegardée avec succès dans :\n" + nomFichier,
                        "Sauvegarde réussie",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return nomFichier;
            } catch (Exception e) {
                // En cas d'erreur, afficher un message d'erreur
                JOptionPane.showMessageDialog(
                        null,
                        "Erreur lors de la sauvegarde :\n" + e.getMessage(),
                        "Erreur de sauvegarde",
                        JOptionPane.ERROR_MESSAGE
                );
                return null;
            }
        }
        return null;
    }


    public boolean demanderSauvegardeAvantQuitter() {
        // Demander à l'utilisateur s'il veut sauvegarder
        int choix = javax.swing.JOptionPane.showConfirmDialog(
                null,
                "Voulez-vous sauvegarder la partie avant de quitter ?",
                "Sauvegarder avant de quitter ?",
                javax.swing.JOptionPane.YES_NO_CANCEL_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE
        );

        switch (choix) {
            case javax.swing.JOptionPane.YES_OPTION:
                // L'utilisateur veut sauvegarder
                String nomFichier = demanderNomFichierSauvegarde();
                if (nomFichier != null) {
                    // La sauvegarde a réussi, on peut quitter
                    return true;
                } else {
                    // La sauvegarde a été annulée ou a échoué
                    return false;
                }

            case javax.swing.JOptionPane.NO_OPTION:
                // L'utilisateur ne veut pas sauvegarder, on peut quitter
                return true;

            case javax.swing.JOptionPane.CANCEL_OPTION:
            case javax.swing.JOptionPane.CLOSED_OPTION:
            default:
                // L'utilisateur a annulé ou fermé la fenêtre, on ne quitte pas
                return false;
        }
    }



}
