package Vue;

import java.awt.*;
import java.util.*;
import java.util.List;

import static Global.Config.*;

/**
 * La classe {@code InfosDeConfigUI} implémente le pattern Singleton pour gérer et stocker
 * les informations de configuration de l'interface utilisateur de l'application.
 * Cela inclut les couleurs des pions, les couleurs des éléments du plateau de jeu,
 * les paramètres d'animation et les réglages audio.
 *
 * <p>L'utilisation du pattern Singleton garantit qu'une seule instance de cette classe
 * existe dans toute l'application, permettant un accès global et cohérent aux
 * informations de configuration.</p>
 */
public class InfosDeConfigUI {
    /**
     * L'instance unique de la classe {@code InfosDeConfigUI}. Elle est initialisée à {@code null}    */
    private static InfosDeConfigUI instance;

    private String nomCouleurPionJoueur1;
    private String nomCouleurPionJoueur2;

    private final Color couleurPionJoueur1;
    private final Color couleurPionJoueur2;
    private Color couleurCaseMaitreJoueur1  = COULEUR_CASE_MAITRE_JOUEUR_1;
    private Color couleurCaseMaitreJoueur2  = COULEUR_CASE_MAITRE_JOUEUR_2;
    private Color couleurCaseEleveJoueur1   = COULEUR_CASE_ELEVE_JOUEUR_1;
    private Color couleurCaseEleveJoueur2   = COULEUR_CASE_ELEVE_JOUEUR_2;

    // Pour la couleur aléatoire
    Color BLEU = new Color(26, 67, 104);
    Color ROUGE = new Color(200, 85, 27);
    Color NOIRE = new Color(1,1,1);
    Map<Color, String> nomsCouleurs = new HashMap<>();


    /**
     * Constructeur privé pour la classe {@code InfosDeConfigUI}.
     * Cela empêche l'instanciation directe de la classe depuis l'extérieur,
     * assurant ainsi le contrôle de l'unicité de l'instance (pattern Singleton).     */
    private InfosDeConfigUI() {
        List<Color> valeurs = new ArrayList<>(Arrays.asList(BLEU, ROUGE, NOIRE));
        Collections.shuffle(valeurs); // Mélange aléatoire
        this.couleurPionJoueur1 = valeurs.get(0);
        this.couleurPionJoueur2 = valeurs.get(1);

        this.nomsCouleurs.put(BLEU, "bleu");
        this.nomsCouleurs.put(ROUGE, "rouge");
        this.nomsCouleurs.put(NOIRE, "noir");
        this.nomCouleurPionJoueur1 = nomsCouleurs.get(couleurPionJoueur1);
        this.nomCouleurPionJoueur2 = nomsCouleurs.get(couleurPionJoueur2);
        // Le constructeur est privé pour implémenter le pattern Singleton.
    }


    /**
     * Retourne l'instance unique de la classe {@code InfosDeConfigUI}.
     * Si aucune instance n'existe, une nouvelle est créée avant d'être retournée.
     *
     * @return L'instance unique de {@code InfosDeConfigUI}.     */
    public static InfosDeConfigUI getInstance() {
        if (instance == null) {
            instance = new InfosDeConfigUI();
        }
        return instance;
    }


    /**
     * Obtient la couleur du pion du joueur 1.
     * @return La couleur du pion du joueur 1 (une chaîne de caractères).    */
    public String getNomCouleurPionJoueur1() {
        return nomCouleurPionJoueur1;
    }


    /**
     * Obtient la couleur du pion du joueur 2.
     * @return La couleur du pion du joueur 2 (une chaîne de caractères).     */
    public String getNomCouleurPionJoueur2() {
        return nomCouleurPionJoueur2;
    }


    /**
     * Obtient la couleur des cases où se trouve le maître du joueur 1.
     * @return La couleur des cases du maître du joueur 1 (un objet {@code Color}).    */
    public Color getCouleurCaseMaitreJoueur1() {
        return couleurCaseMaitreJoueur1;
    }


    /**
     * Définit la couleur des cases où se trouve le maître du joueur 1.
     * @param couleurCaseMaitreJoueur1 La nouvelle couleur des cases du maître du joueur 1.     */
    public void setCouleurCaseMaitreJoueur1(Color couleurCaseMaitreJoueur1) {
        this.couleurCaseMaitreJoueur1 = couleurCaseMaitreJoueur1;
    }


    /**
     * Obtient la couleur des cases où se trouve le maître du joueur 2.
     * @return La couleur des cases du maître du joueur 2 (un objet {@code Color}).     */
    public Color getCouleurCaseMaitreJoueur2() {
        return couleurCaseMaitreJoueur2;
    }


    /**
     * Définit la couleur des cases où se trouve le maître du joueur 2.
     * @param couleurCaseMaitreJoueur2 La nouvelle couleur des cases du maître du joueur 2.    */
    public void setCouleurCaseMaitreJoueur2(Color couleurCaseMaitreJoueur2) {
        this.couleurCaseMaitreJoueur2 = couleurCaseMaitreJoueur2;
    }

    /**
     * Définit la couleur des cases où se trouvent les élèves du joueur 1.
     * @param couleurCaseEleveJoueur1 La nouvelle couleur des cases des élèves du joueur 1.     */
    public void setCouleurCaseEleveJoueur1(Color couleurCaseEleveJoueur1) {
        this.couleurCaseEleveJoueur1 = couleurCaseEleveJoueur1;
    }


    /**
     * Définit la couleur des cases où se trouvent les élèves du joueur 2.
     * @param couleurCaseEleveJoueur2 La nouvelle couleur des cases des élèves du joueur 2.     */
    public void setCouleurCaseEleveJoueur2(Color couleurCaseEleveJoueur2) {
        this.couleurCaseEleveJoueur2 = couleurCaseEleveJoueur2;
    }


    /**
     * Retourne la couleur du pion pour le joueur spécifié.
     *
     * @param id l'identifiant du joueur (1 ou 2)
     * @return la couleur du pion du joueur 1 si id vaut 1, sinon celle du joueur 2
     */
    public Color getCouleurPionJoueur(int id) {
        if (id == ID_JOUEUR_1) return getCouleurPionJoueur1();
        else return getCouleurPionJoueur2();
    }


    /**
     * Retourne la couleur actuellement affectée au pion du joueur 1.
     *
     * @return la couleur du pion du joueur 1
     */
    public Color getCouleurPionJoueur1() {
        return couleurPionJoueur1;
    }


    /**
     * Retourne la couleur actuellement affectée au pion du joueur 2.
     *
     * @return la couleur du pion du joueur 2
     */
    public Color getCouleurPionJoueur2() {
        return couleurPionJoueur2;
    }



    /**
     * Obtient le nom de la couleur du pion pour le joueur spécifié.
     *
     * @param idJoueur l'identifiant du joueur (1 ou 2)
     * @return le nom de la couleur du pion du joueur (une chaîne de caractères)
     */
    public String getNomCouleurPionJoueur(int idJoueur) {
        if (idJoueur == ID_JOUEUR_1) return getNomCouleurPionJoueur1();
        else return getNomCouleurPionJoueur2();
    }


    /**
     * Retourne la couleur de la case des élèves pour le joueur spécifié.
     *
     * @param idJoueur l'identifiant du joueur (1 ou 2)
     * @return la couleur de la case des élèves du joueur spécifié (un objet {@code Color})
     */
    public Color getCouleurCaseEleveJoueur(int idJoueur) {
        if (idJoueur == ID_JOUEUR_1) return getCouleurCaseEleveJoueur1();
        else return getCouleurCaseEleveJoueur2();
    }


    /**
     * Retourne la couleur des cases où se trouvent les élèves du joueur 1.
     *
     * @return la couleur des cases des élèves du joueur 1 (un objet {@code Color})
     */
    public Color getCouleurCaseEleveJoueur1() {
        return couleurCaseEleveJoueur1;
    }


    /**
     * Retourne la couleur des cases où se trouvent les élèves du joueur 2.
     *
     * @return la couleur des cases des élèves du joueur 2 (un objet {@code Color})
     */
    public Color getCouleurCaseEleveJoueur2() {
        return couleurCaseEleveJoueur2;
    }


    /**
     * Définit la couleur des cases associées aux élèves d'un joueur spécifié.
     *
     * @param idJoueur L'identifiant du joueur (1 pour le joueur 1, tout autre identifiant pour le joueur 2).
     * @param couleur La nouvelle couleur des cases associées aux élèves du joueur.
     */
    public void setCouleurCaseEtudiantJoueur(int idJoueur, Color couleur) {
        if (idJoueur == ID_JOUEUR_1) setCouleurCaseEleveJoueur1(couleur);
        else setCouleurCaseEleveJoueur2(couleur);
    }



    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'état actuel
     * de la configuration de l'interface utilisateur, incluant toutes les couleurs
     * définies pour les différents éléments du jeu.
     *
     * @return une chaîne de caractères décrivant l'état de la configuration
     */
    @Override
    public String toString() {
        return  "\n   nomCouleurPionJoueur1 : " + nomCouleurPionJoueur1 +
                "\n   nomCouleurPionJoueur2 : " + nomCouleurPionJoueur2 +
                "\n      couleurPionJoueur1 : " + couleurPionJoueur1 +
                "\n      couleurPionJoueur2 : " + couleurPionJoueur2 +
                "\ncouleurCaseMaitreJoueur1 : " + couleurCaseMaitreJoueur1 +
                "\ncouleurCaseMaitreJoueur2 : " + couleurCaseMaitreJoueur2 +
                "\n couleurCaseEleveJoueur1 : " + couleurCaseEleveJoueur1 +
                "\nncouleurCaseEleveJoueur2 : " + couleurCaseEleveJoueur2;
    }
}