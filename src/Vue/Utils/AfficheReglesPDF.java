package Vue.Utils;

import Global.Paths;
import Vue.Utils.Boutons.Bouton;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static Global.Paths.*;

/**
 * Classe utilitaire pour ouvrir le fichier PDF des règles du jeu
 * dans le lecteur PDF par défaut de l'utilisateur.
 * Offre un affichage de secours des règles de base d'Onitama
 * si le fichier PDF ne peut pas être ouvert.
 */
public class AfficheReglesPDF {

    // Constructeur privé pour empêcher l'instanciation, car c'est une classe utilitaire.
    private AfficheReglesPDF() {
        // Non destinée à être instanciée.
    }



    /**
     * Ouvre le fichier PDF des règles du jeu en utilisant le lecteur PDF par défaut du système d'exploitation.
     * En cas d'échec de l'ouverture, un ensemble de règles de base d'Onitama est affiché dans une boîte de dialogue.
     *
     * @param composantParent Le composant parent pour les dialogues, utilisé pour le centrage.
     */
    public static void ouvrirReglesPDFExterne(Component composantParent) {
        File fichierRegles = REGLES.resolve("regles_du_jeu.pdf").toFile();

        // Vérification de l'existence du fichier
        if (!fichierRegles.exists()) {
            afficherReglesDeBase(composantParent, "Le fichier des règles (regles_du_jeu.pdf) n'a pas été trouvé à l'emplacement spécifié.");
            return;
        }

        // Vérification de la prise en charge de l'API Desktop
        if (!Desktop.isDesktopSupported()) {
            afficherReglesDeBase(composantParent, "L'ouverture automatique de fichiers n'est pas supportée sur ce système.");
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        // Vérification de la prise en charge de l'action d'ouverture
        if (!desktop.isSupported(Desktop.Action.OPEN)) {
            afficherReglesDeBase(composantParent, "L'action d'ouverture de fichier n'est pas supportée par votre environnement de bureau.");
            return;
        }

        try {
            desktop.open(fichierRegles);
        } catch (IOException ex) {
            // Erreur d'ouverture du PDF, affichage des règles de base en secours.
            afficherReglesDeBase(composantParent,
                    "Impossible d'ouvrir le fichier PDF. Veuillez vérifier qu'un lecteur PDF est installé et que le fichier n'est pas corrompu.\n" +
                            "Détails : " + ex.getMessage());
            ex.printStackTrace(); // Enregistrer l'exception pour le débogage.
        } catch (Exception ex) {
            // Toute autre erreur inattendue, affichage des règles de base en secours.
            afficherReglesDeBase(composantParent,
                    "Une erreur inattendue est survenue lors de l'ouverture du PDF.\n" +
                            "Détails : " + ex.getMessage());
            ex.printStackTrace(); // Enregistrer l'exception pour le débogage.
        }
    }



    /**
     * Affiche un ensemble de règles de base d'Onitama dans une boîte de dialogue défilante.
     * Ceci est utilisé comme solution de secours si le fichier PDF ne peut pas être ouvert.
     *
     * @param composantParent Le composant parent pour la boîte de dialogue.
     * @param messageErreur   Un message d'erreur optionnel à ajouter au début des règles.
     */
    private static void afficherReglesDeBase(Component composantParent, String messageErreur) {
        String reglesDeBase =
                "Règles de base d'Onitama :\n\n" +
                        "1. Objectif : Capturez le Grand Maître (pièce principale) de votre adversaire OU déplacez votre Grand Maître sur la case Temple de votre adversaire.\n\n" +
                        "2. Plateau : Un plateau de 5x5 cases. Chaque joueur commence avec 4 pions et 1 Grand Maître.\n\n" +
                        "3. Cartes de Mouvement : Cinq cartes de mouvement sont en jeu. Chaque carte représente un mouvement spécifique que vos pièces peuvent effectuer (par rapport à leur position actuelle).\n\n" +
                        "4. Tour de Jeu : À votre tour, choisissez une de vos deux cartes de mouvement disponibles. Déplacez l'une de vos pièces (pion ou Grand Maître) selon le schéma de mouvement de la carte choisie. Après le mouvement, placez la carte utilisée au centre du plateau, et prenez la carte qui s'y trouvait pour la remplacer dans votre main.\n\n" +
                        "5. Temple : La case centrale de la ligne de départ de chaque joueur est son temple. Si le Grand Maître adverse atteint votre temple, vous perdez.\n\n" +
                        "6. Capture : Si une pièce se déplace sur une case occupée par une pièce adverse, la pièce adverse est capturée et retirée du jeu.\n\n" +
                        "7. La Voie du Maître Esprit : La cinquième carte (centrale) est temporairement hors jeu. Elle entre en jeu quand une carte est jouée, remplaçant la carte utilisée. La carte jouée va alors à la place de la carte centrale.\n\n" +
                        "Pour des règles complètes, veuillez consulter le fichier PDF original si possible.";

        String texteFinal = (messageErreur != null && !messageErreur.isEmpty())
                ? messageErreur + "\n\n" + reglesDeBase
                : reglesDeBase;

        // 1. Créer un JTextArea transparent
        JTextArea textArea = new JTextArea(texteFinal);
        textArea.setOpaque(false); // Fond transparent
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));
        textArea.setForeground(new Color(0, 0, 0, 255));
        textArea.setCaretPosition(0);
        textArea.setMargin(new Insets(20, 12, 12, 20));

        // 2. JScrollPane transparent
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        // 3. Panel personnalisé qui dessine l'image de fond
        JPanel fondPanel = new JPanel() {
            Image img = new ImageIcon(PATH_ARRIERE.resolve("arrierePlan4.png").toString()).getImage(); // Mets le chemin de ton image ici
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Redimensionne l'image pour remplir le panel
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        fondPanel.setLayout(new BorderLayout());
        fondPanel.add(scrollPane, BorderLayout.CENTER);

        // 4. Bouton de fermeture
        Bouton.BoutonAvecImage fermer = Bouton.creerBouton(Paths.PATH_BTN.resolve("exit.png").toString(), Bouton.ConfigurationParDefaut.Cercle_transparent);
        fermer.setPreferredSize(new Dimension(60,60));
        fermer.addActionListener(e -> SwingUtilities.getWindowAncestor(fondPanel).dispose());
        JPanel panelBtn = new JPanel();
        panelBtn.setOpaque(false); // Pour voir l'image derrière le bouton
        panelBtn.add(fermer);
        fondPanel.add(panelBtn, BorderLayout.SOUTH);

        // 5. JDialog redimensionnable
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(composantParent),
                (messageErreur != null && !messageErreur.isEmpty())
                        ? "Erreur : Règles de Base d'Onitama"
                        : "Règles de Base d'Onitama",
                Dialog.ModalityType.APPLICATION_MODAL);

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(fondPanel);
        dialog.setSize(600, 500);
        dialog.setMinimumSize(new Dimension(400, 300));
        dialog.setLocationRelativeTo(composantParent);
        dialog.setResizable(true);
        dialog.setVisible(true);
    }
}