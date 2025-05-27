package Global;

import java.nio.file.Path;

public class Paths {
    /********************************************
     *              IMAGES RACINE              *
     *******************************************/
    public static final Path VUE = Path.of("res", "vue");
    public static final Path IMAGES = VUE.resolve("images");

    /********************************************
     *              IMAGES CARTES              *
     *******************************************/
    public static final Path PATH_CARTE = IMAGES.resolve("cartes");

    /********************************************
     *              IMAGES BOUTONS             *
     *******************************************/
    public static final Path PATH_BTN = IMAGES.resolve("buttons");
    public static final Path PATH_BTN_MODE_AUTO_OFF = PATH_BTN.resolve("button_off.png");
    public static final Path PATH_BTN_MODE_AUTO_ON = PATH_BTN.resolve("button_on.png");
    public static final Path PATH_BTN_ENTRER = PATH_BTN.resolve("button_entrer.png");
    public static final Path PATH_BTN_ANNULER = PATH_BTN.resolve("button_annuler.png");
    public static final Path PATH_BTN_REFAIRE = PATH_BTN.resolve("button_refaire.png");
    public static final Path PATH_BTN_MENU = PATH_BTN.resolve("menu.png");
    public static final Path PATH_BTN_MUET = PATH_BTN.resolve("muet.png");
    public static final Path PATH_BTN_MONTER_LE_SON = PATH_BTN.resolve("monter-le-son.png");
    public static final Path PATH_BOUTON_ANNULER_ROUGE = PATH_BTN.resolve("button_annuler_rouge.png");

    /********************************************
     *              IMAGES ARRIERE PLANS       *
     *******************************************/
    public static final Path PATH_ARRIERE = IMAGES.resolve("arrierePlans");
    public static final Path PATH_ARRIERE_PLAN_01 = PATH_ARRIERE.resolve("arrierePlan1.png");
    public static final Path PATH_ARRIERE_PLAN_O2 = PATH_ARRIERE.resolve("arrierePlan2.png");
    public static final Path PATH_ARRIERE_PLAN_03 = PATH_ARRIERE.resolve("arrierePlan10.png");
    public static final Path PATH_ARRIERE_PLAN_4 = PATH_ARRIERE.resolve("arrierePlan4.png");
    public static final Path PATH_ARRIERE_PLAN_8 = PATH_ARRIERE.resolve("arrierePlan8.png");

    /********************************************
     *              IMAGES PIONS               *
     *******************************************/
    public static final Path PATH_PION = IMAGES.resolve("pions");
    public static final Path PATH_PION_BLEU_ETUDIANT_CLIQUE = PATH_PION.resolve("pion_etudiant_bleu_clique.png");
    public static final Path PATH_PION_ROUGE_ETUDIANT = PATH_PION.resolve("pion_etudiant_rouge.png");
    public static final Path PATH_PION_BLEU_ETUDIANT = PATH_PION.resolve("pion_etudiant_bleu.png");
    public static final Path PATH_PION_NOIR_ETUDIANT = PATH_PION.resolve("pion_etudiant_noir.png");
    public static final Path PATH_PION_ROUGE_MAITRE = PATH_PION.resolve("pion_maitre_rouge.png");
    public static final Path PATH_PION_NOIR_MAITRE = PATH_PION.resolve("pion_maitre_noir.png");
    public static final Path PATH_PION_BLEU_MAITRE = PATH_PION.resolve("pion_maitre_bleu.png");

    /********************************************
     *              MUSIQUES                   *
     *******************************************/
    public static final Path MUSIQUE = VUE.resolve("musique");
    public static final Path PATH_SON_1 = MUSIQUE.resolve("son_1.wav");

    /********************************************
     *              DEBUT PATHS                *
     *******************************************/
    public static final Path PATH_DEBUT_PION = PATH_PION;

    /********************************************
     *              INDICATEURS                *
     *******************************************/
    public static final Path PATH_LBL_TXT = IMAGES.resolve("font_zuma").resolve("png");

    /********************************************
     *              REGLES                     *
     *******************************************/
    public static final Path REGLES = VUE.resolve("regles");
}
