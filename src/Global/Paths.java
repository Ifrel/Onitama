
package Global;

import java.net.URL;

public class Paths {
    /********************************************
     *              IMAGES RACINE              *
     *******************************************/
    private static final String BASE_PATH = "/vue/";
    private static final String IMAGES_PATH = BASE_PATH + "images/";

    /********************************************
     *              IMAGES CARTES              *
     *******************************************/
    private static final String CARTES_PATH = IMAGES_PATH + "cartes/";
    public static URL getCartePath(String filename) {
        return Paths.class.getResource(CARTES_PATH + filename);
    }

    /********************************************
     *              IMAGES BOUTONS             *
     *******************************************/
    private static final String BUTTONS_PATH = IMAGES_PATH + "buttons/";
    public static URL getButtonPath(String filename) {
        return Paths.class.getResource(BUTTONS_PATH + filename);
    }

    public static final URL PATH_BTN_MODE_AUTO_OFF = getButtonPath("button_off.png");
    public static final URL PATH_BTN_MODE_AUTO_ON = getButtonPath("button_on.png");
    public static final URL PATH_BTN_ENTRER = getButtonPath("button_entrer.png");
    public static final URL PATH_BTN_ANNULER = getButtonPath("button_annuler.png");
    public static final URL PATH_BTN_REFAIRE = getButtonPath("button_refaire.png");
    public static final URL PATH_BTN_MENU = getButtonPath("menu.png");
    public static final URL PATH_BTN_MUET = getButtonPath("muet.png");
    public static final URL PATH_BTN_MONTER_LE_SON = getButtonPath("monter-le-son.png");
    public static final URL PATH_BOUTON_ANNULER_ROUGE = getButtonPath("button_annuler_rouge.png");

    /********************************************
     *              IMAGES ARRIERE PLANS       *
     *******************************************/
    private static final String ARRIERE_PLANS_PATH = IMAGES_PATH + "arrierePlans/";
    public static URL getArrierePlanPath(String filename) {
        System.out.println("abcd " + ARRIERE_PLANS_PATH + filename);
        System.out.println("bonjour " + Paths.class.getResource(ARRIERE_PLANS_PATH + filename));
        return Paths.class.getResource(ARRIERE_PLANS_PATH + filename);
    }

    public static final URL PATH_ARRIERE_PLAN_01 = getArrierePlanPath("arrierePlan1.png");
    public static final URL PATH_ARRIERE_PLAN_O2 = getArrierePlanPath("arrierePlan2.png");
    public static final URL PATH_ARRIERE_PLAN_03 = getArrierePlanPath("arrierePlan10.png");
    public static final URL PATH_ARRIERE_PLAN_4 = getArrierePlanPath("arrierePlan4.png");
    public static final URL PATH_ARRIERE_PLAN_8 = getArrierePlanPath("arrierePlan8.png");

    /********************************************
     *              IMAGES PIONS               *
     *******************************************/
    private static final String PIONS_PATH = IMAGES_PATH + "pions/";
    public static URL getPionPath(String filename) {
        return Paths.class.getResource(PIONS_PATH + filename);
    }

    public static final URL PATH_PION_BLEU_ETUDIANT_CLIQUE = getPionPath("pion_etudiant_bleu_clique.png");
    public static final URL PATH_PION_ROUGE_ETUDIANT = getPionPath("pion_etudiant_rouge.png");
    public static final URL PATH_PION_BLEU_ETUDIANT = getPionPath("pion_etudiant_bleu.png");
    public static final URL PATH_PION_NOIR_ETUDIANT = getPionPath("pion_etudiant_noir.png");
    public static final URL PATH_PION_ROUGE_MAITRE = getPionPath("pion_maitre_rouge.png");
    public static final URL PATH_PION_NOIR_MAITRE = getPionPath("pion_maitre_noir.png");
    public static final URL PATH_PION_BLEU_MAITRE = getPionPath("pion_maitre_bleu.png");

    /********************************************
     *              MUSIQUES                   *
     *******************************************/
    private static final String MUSIQUE_PATH = "/vue/musique/";
    public static URL getMusiquePath(String filename) {
        return Paths.class.getResource(MUSIQUE_PATH + filename);
    }

    public static final URL PATH_SON_1 = getMusiquePath("son_1.wav");

    /********************************************
     *              INDICATEURS                *
     *******************************************/
    private static final String FONT_PATH = IMAGES_PATH + "font_zuma/png/";
    public static URL getFontPath(String filename) {
        return Paths.class.getResource(FONT_PATH + filename);
    }

    /********************************************
     *              REGLES                     *
     *******************************************/
    private static final String REGLES_PATH = BASE_PATH + "regles/";
    public static URL getReglesPath(String filename) {
        return Paths.class.getResource(REGLES_PATH + filename);
    }
    
    
    
//    Bouton.creerBouton(Paths.getButtonPath("regles.png").toString(), ...)
//
//    private static URL checkResource(String path) {
//        URL resource = Paths.class.getResource(path);
//        if (resource == null) {
//            throw new RuntimeException("Ressource non trouvée : " + path);
//        }
//        return resource;
//    }
}


