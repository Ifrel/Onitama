package Vue;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

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
    private static final Logger logger = Logger.getLogger(InfosDeConfigUI.class.getName());

    private String nomCouleurPionJoueur1;
    private String nomCouleurPionJoueur2;

    private Color couleurPionJoueur1;
    private Color couleurPionJoueur2;
    private Color couleurPlateauDejeu       = COULEUR_PLATEAU_DE_JEU;
    private Color couleurCaseTerrain        = COULEUR_CASE_TERRAIN;
    private Color couleurCaseMaitreJoueur1  = COULEUR_CASE_MAITRE_JOUEUR_1;
    private Color couleurCaseMaitreJoueur2  = COULEUR_CASE_MAITRE_JOUEUR_2;
    private Color couleurCaseEleveJoueur1   = COULEUR_CASE_ELEVE_JOUEUR_1;
    private Color couleurCaseEleveJoueur2   = COULEUR_CASE_ELEVE_JOUEUR_2;
    private Color couleurBlocMenu1          = COULEUR_BLOC_MENU;
    private Color couleurBlocMenu2          = new Color(175, 175, 175);

    private int vitesseAnimation            = 50;
    private boolean animerDeplacementPiece  = true;
    private boolean animerSurbrillace       = true;

    private int volumeGeneral               = 50;
    private int volumeEffetSonore           = 50;
    private int volumeMusique               = 50;
    private boolean couperToutSon           = false;

    // Pour la couleur aléatoire
    Color BLEU = new Color(86, 155, 223);
    Color ROUGE = new Color(219, 111, 61);
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
        afficherEtatConfigUI();
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
     * Définit la couleur du pion du joueur 1.
     * @param nomCouleurPionJoueur1 La nouvelle couleur du pion du joueur 1.     */
    public void setNomCouleurPionJoueur1(String nomCouleurPionJoueur1) {
        this.nomCouleurPionJoueur1 = nomCouleurPionJoueur1;
    }


    /**
     * Obtient la couleur du pion du joueur 2.
     * @return La couleur du pion du joueur 2 (une chaîne de caractères).     */
    public String getNomCouleurPionJoueur2() {
        return nomCouleurPionJoueur2;
    }


    /**
     * Définit la couleur du pion du joueur 2.
     * @param nomCouleurPionJoueur2 La nouvelle couleur du pion du joueur 2.    */
    public void setNomCouleurPionJoueur2(String nomCouleurPionJoueur2) {
        this.nomCouleurPionJoueur2 = nomCouleurPionJoueur2;
    }



    /**
     * Obtient la couleur du plateau de jeu.
     * @return La couleur du plateau de jeu (un objet {@code Color}).     */
    public Color getCouleurPlateauDejeu() {
        return couleurPlateauDejeu;
    }


    /**
     * Définit la couleur du plateau de jeu.
     * @param couleurPlateauDejeu La nouvelle couleur du plateau de jeu.     */
    public void setCouleurPlateauDejeu(Color couleurPlateauDejeu) {
        this.couleurPlateauDejeu = couleurPlateauDejeu;
    }


    /**
     * Obtient la couleur des cases de terrain standard.
     * @return La couleur des cases de terrain (un objet {@code Color}).     */
    public Color getCouleurCaseTerrain() {
        return couleurCaseTerrain;
    }


    /**
     * Définit la couleur des cases de terrain standard.
     * @param couleurCaseTerrain La nouvelle couleur des cases de terrain.     */
    public void setCouleurCaseTerrain(Color couleurCaseTerrain) {
        this.couleurCaseTerrain = couleurCaseTerrain;
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
     * Obtient la couleur des cases où se trouvent les élèves du joueur 1.
     * @return La couleur des cases des élèves du joueur 1 (un objet {@code Color}).    */
    public Color getCouleurCaseEleveJoueur1() {
        return couleurCaseEleveJoueur1;
    }


    /**
     * Définit la couleur des cases où se trouvent les élèves du joueur 1.
     * @param couleurCaseEleveJoueur1 La nouvelle couleur des cases des élèves du joueur 1.     */
    public void setCouleurCaseEleveJoueur1(Color couleurCaseEleveJoueur1) {
        this.couleurCaseEleveJoueur1 = couleurCaseEleveJoueur1;
    }


    /**
     * Obtient la couleur des cases où se trouvent les élèves du joueur 2.
     * @return La couleur des cases des élèves du joueur 2 (un objet {@code Color}).     */
    public Color getCouleurCaseEleveJoueur2() {
        return couleurCaseEleveJoueur2;
    }


    /**
     * Définit la couleur des cases où se trouvent les élèves du joueur 2.
     * @param couleurCaseEleveJoueur2 La nouvelle couleur des cases des élèves du joueur 2.     */
    public void setCouleurCaseEleveJoueur2(Color couleurCaseEleveJoueur2) {
        this.couleurCaseEleveJoueur2 = couleurCaseEleveJoueur2;
    }


    /**
     * Obtient la couleur principale des blocs de menu.
     * @return La couleur principale des blocs de menu (un objet {@code Color}).     */
    public Color getCouleurBlocMenu1() {
        return couleurBlocMenu1;
    }


    /**
     * Définit la couleur principale des blocs de menu.
     * @param couleurBlocMenu1 La nouvelle couleur principale des blocs de menu.     */
    public void setCouleurBlocMenu1(Color couleurBlocMenu1) {
        this.couleurBlocMenu1 = couleurBlocMenu1;
    }


    /**
     * Obtient la couleur secondaire des blocs de menu.
     * @return La couleur secondaire des blocs de menu (un objet {@code Color}).     */
    public Color getCouleurBlocMenu2() {
        return couleurBlocMenu2;
    }


    /**
     * Définit la couleur secondaire des blocs de menu.
     * @param couleurBlocMenu2 La nouvelle couleur secondaire des blocs de menu.     */
    public void setCouleurBlocMenu2(Color couleurBlocMenu2) {
        this.couleurBlocMenu2 = couleurBlocMenu2;
    }


    /**
     * Obtient la vitesse de l'animation.
     * @return La vitesse de l'animation en millisecondes.     */
    public int getVitesseAnimation() {
        return vitesseAnimation;
    }


    /**
     * Définit la vitesse de l'animation.
     * @param vitesseAnimation La nouvelle vitesse de l'animation en millisecondes.     */
    public void setVitesseAnimation(int vitesseAnimation) {
        this.vitesseAnimation = vitesseAnimation;
    }


    /**
     * Vérifie si l'animation du déplacement des pièces est activée.
     * @return {@code true} si l'animation du déplacement des pièces est activée, {@code false} sinon.     */
    public boolean isAnimerDeplacementPiece() {
        return animerDeplacementPiece;
    }


    /**
     * Définit l'état de l'animation du déplacement des pièces.
     * @param animerDeplacementPiece {@code true} pour activer l'animation, {@code false} pour la désactiver.     */
    public void setAnimerDeplacementPiece(boolean animerDeplacementPiece) {
        this.animerDeplacementPiece = animerDeplacementPiece;
    }


    /**
     * Vérifie si l'animation de surbrillance est activée.
     * @return {@code true} si l'animation de surbrillance est activée, {@code false} sinon.    */
    public boolean isAnimerSurbrillace() {
        return animerSurbrillace;
    }


    /**
     * Définit l'état de l'animation de surbrillance.
     * @param animerSurbrillace {@code true} pour activer l'animation, {@code false} pour la désactiver.     */
    public void setAnimerSurbrillace(boolean animerSurbrillace) {
        this.animerSurbrillace = animerSurbrillace;
    }


    /**
     * Obtient le volume général de l'application.
     * @return Le volume général (un entier entre 0 et 100).    */
    public int getVolumeGeneral() {
        return volumeGeneral;
    }


    /**
     * Définit le volume général de l'application.
     * @param volumeGeneral Le nouveau volume général (un entier entre 0 et 100).    */
    public void setVolumeGeneral(int volumeGeneral) {
        this.volumeGeneral = volumeGeneral;
    }


    /**
     * Obtient le volume de la musique de fond.
     * @return Le volume de la musique (un entier entre 0 et 100).     */
    public int getVolumeMusique() {
        return volumeMusique;
    }



    /**
     * Définit le volume de la musique de fond.
     * @param volumeMusique Le nouveau volume de la musique (un entier entre 0 et 100).     */
    public void setVolumeMusique(int volumeMusique) {
        this.volumeMusique = volumeMusique;
    }


    /**
     * Obtient le volume des effets sonores.
     * @return Le volume des effets sonores (un entier entre 0 et 100).     */
    public int getVolumeEffetSonore() {
        return volumeEffetSonore;
    }


    /**
     * Définit le volume des effets sonores.
     * @param volumeEffetSonore Le nouveau volume des effets sonores (un entier entre 0 et 100).    */
    public void setVolumeEffetSonore(int volumeEffetSonore) {
        this.volumeEffetSonore = volumeEffetSonore;
    }


    /**
     * Vérifie si tout le son est coupé.
     * @return {@code true} si tout le son est coupé, {@code false} sinon.    */
    public boolean isCouperToutSon() {
        return couperToutSon;
    }


    /**
     * Définit l'état de la coupure du son.
     * @param couperToutSon {@code true} pour couper tout le son, {@code false} pour le réactiver.     */
    public void setCouperToutSon(boolean couperToutSon) {
        this.couperToutSon = couperToutSon;
    }

    /**
     * Retourne la couleur du pion pour le joueur spécifié.
     *
     * @param id l'identifiant du joueur (1 ou 2)
     * @return la couleur du pion du joueur 1 si id vaut 1, sinon celle du joueur 2
     */
    public Color getCouleurPionJoueur(int id) {
        if (id == 1) return getCouleurPionJoueur1();
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
     * Définit la couleur à utiliser pour le pion du joueur 1.
     *
     * @param couleurPionJoueur1 la nouvelle couleur à affecter
     */
    public void setCouleurPionJoueur1(Color couleurPionJoueur1) {
        this.couleurPionJoueur1 = couleurPionJoueur1;
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
     * Définit la couleur à utiliser pour le pion du joueur 2.
     *
     * @param couleurPionJoueur2 la nouvelle couleur à affecter
     */
    public void setCouleurPionJoueur2(Color couleurPionJoueur2) {
        this.couleurPionJoueur2 = couleurPionJoueur2;
    }


    public void afficherEtatConfigUI(){
        logger.info("\nnomCouleurPionJoueur1 :" + nomCouleurPionJoueur1+
                        "\nnomCouleurPionJoueur2 :"+ nomCouleurPionJoueur2 +
                        "\ncouleurPionJoueur1 :"+ couleurPionJoueur1 +
                        "\ncouleurPionJoueur2 :" + couleurPionJoueur2 +
                        "\ncouleurPlateauDejeu :" + couleurPlateauDejeu +
                        "\ncouleurCaseTerrain :" + couleurCaseTerrain +
                        "\ncouleurCaseMaitreJoueur1 :" + couleurCaseMaitreJoueur1 +
                        "\ncouleurCaseMaitreJoueur2 :" + couleurCaseMaitreJoueur2 +
                        "\ncouleurCaseEleveJoueur1 :" + couleurCaseEleveJoueur1 +
                        "\ncouleurCaseEleveJoueur2 :" + couleurCaseEleveJoueur2 +
                        "\ncouleurBlocMenu1 :" + couleurBlocMenu1 +
                        "\ncouleurBlocMenu2 :" + couleurBlocMenu2 +
                        "\nvitesseAnimation :" + vitesseAnimation +
                        "\nanimerDeplacementPiece :" + animerDeplacementPiece +
                        "\nanimerSurbrillace :" + animerSurbrillace +
                        "\nvolumeGeneral :" + volumeGeneral +
                        "\nvolumeEffetSonore :" + volumeEffetSonore +
                        "\nvolumeMusique :" +  volumeMusique+
                        "\ncouperToutSon :" + couperToutSon
        );

    }


    public String getNomCouleurPionJoueur(int idJoueur) {
        if (idJoueur == 1) return getNomCouleurPionJoueur1();
        else return getNomCouleurPionJoueur2();
    }
}