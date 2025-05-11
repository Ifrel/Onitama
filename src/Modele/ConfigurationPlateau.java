package Modele;

import java.awt.*;
import java.util.*;
import java.util.List;

import static Global.Config.TYPECARTE.*;

/**
 * Représentation compacte de l'état du plateau de jeu à un moment donné de la partie
 */
public class ConfigurationPlateau implements Comparable<ConfigurationPlateau> {
    private  byte[] config; // 12 octets = 96 bits
    // 1111 1111  1111 1111  1111  11111 11111 11111 11111  11111  11111 1111 11111 11111 11111 11111 11111 11111 1111 w1 1  XX XXXX
    // ^^^^ ^^^^  ^^^^ ^^^^  ^^^^  ^^^^^^^^^^^^^^^^^^^^^^^^^  ^^^^^^^^^^^^^^^^^^^^^^^^^  ^^^^^ ^^^^^  ^^^^^ ^^^^^  ^^^^^^
    //  |    |     |    |     |               |                          |                |     |      |     |       |
    //  |    |     |    |     |               |                          |                |     |      |     |      6 bits inutilisés
    //  |    |     |    |     |               |                          |                |     |      |    Position Colonne Maitre Joueur 2
    //  |    |     |    |     |               |                          |                |     |     Position Ligne Maitre Joueur 2
    //  |    |     |    |     |               |                          |                |    Position Colonne Maitre Joueur 1
    //  |    |     |    |     |               |                          |               Position Ligne Maitre Joueur 1
    //  |    |     |    |     |               |                         Positions Pions Joueur 2
    //  |    |     |    |     |              Positions Pions Eleves Joueur 1
    //  |    |     |    |    Carte Supplémentaire
    //  |    |     |   Carte de la Main 2 du Joueur 2
    //  |    |    Carte de la Main 1 du Joueur 2
    //  |   Carte de la Main 2 du Joueur 1
    // Carte de la Main 1 du Joueur 1
    // ---------------------------------
    // -> Cartes sur 4 bits car 16 valeurs possibles ( Math.ceiling(log2(16)) )
    // -> Positions pions, impossible d'utilise 2 vecteurs de 5 bits, on obtiendrait des intersections en trop,
    // donc plus de pions que prévu, donc 25 bits pour toutes les positions possibles, 1 si pion à cette position, 0 sinon
    // -> Position Maitres : possible d'utiliser 2 vecteurs de 5 bits car 1 seul maitre par joueur donc aucune ambiguité


    public ConfigurationPlateau(int joueurCourantID, Carte carteEnPlus, List<Carte> cartesJoueur1, List<Carte> cartesJoueur2,  List<Pion> pionsJoueur1, List<Pion> pionsJoueur2) {
        Objects.requireNonNull(carteEnPlus, "La Carte Supplémentaire ne peut pas valoir null");
        Objects.requireNonNull(cartesJoueur1, "La liste des cartes du joueur 1 ne peut pas valoir null");
        Objects.requireNonNull(cartesJoueur2, "La liste des cartes du joueur 2 ne peut pas valoir null");
        Objects.requireNonNull(pionsJoueur1, "La liste des pions du joueur 1 ne peut pas valoir null");
        Objects.requireNonNull(pionsJoueur2, "La liste des pions du joueur 2 ne peut pas valoir null");

        if (cartesJoueur1.size() != 2) {
            throw new RuntimeException("Le Joueur 1 devrait avoir 2 cartes mais en a " + cartesJoueur1.size());
        }
        if (cartesJoueur2.size() != 2) {
            throw new RuntimeException("Le Joueur 2 devrait avoir 2 cartes mais en a " + cartesJoueur2.size());
        }

        if (pionsJoueur1.size() > 5) {
            throw new RuntimeException("Le Joueur 1 devrait avoir au maximum 5 pions mais en a " + pionsJoueur1.size());
        }

        if (pionsJoueur2.size() > 5) {
            throw new RuntimeException("Le Joueur 2 devrait avoir au maximum 5 pions mais en a " + pionsJoueur2.size());
        }

        config = new byte[12]; // 96 bits, 90 utilisés

        byte b, b1, b2;

        b1 = carte2Octet(cartesJoueur1.get(0));
        b2 = carte2Octet(cartesJoueur1.get(1));
        b = (byte) (b1 << 4 | b2);
        config[0] = b;

        b1 = carte2Octet(cartesJoueur2.get(0));
        b2 = carte2Octet(cartesJoueur2.get(1));
        b = (byte) (b1 << 4 | b2);
        config[1] = b;

        b1 = carte2Octet(carteEnPlus);
        List<Point> lp1 = new ArrayList<>();
        
        //b2 =
    }

    public ConfigurationPlateau(byte [] etatJeu) {
        Objects.requireNonNull(etatJeu, "L'état du jeu ne peut pas valoir null");
        this.config = etatJeu.clone();
    }

    /**
     * Renvoie la représentation compacte du plateau de jeu
     * @return un vecteur de 96 bits représentant l'état du jeu
     */
    public byte[] getEtat() {
        return this.config.clone();
    }

    /**
     * Convertit une carte en sa représentation en octet
     * @param c carte à convertir
     * @return l'octet contenant la représentation de la carte
     */
    public byte carte2Octet(Carte c) {
        byte b = 0;
        switch (c.getType()) {
            case TIGRE:
                b = 0;
                break;
            case DRAGON:
                b = 1;
                break;
            case GRENOUILLE:
                b = 2;
                break;
            case LAPIN:
                b = 3;
                break;
            case CRABE:
                b = 4;
                break;
            case ELEPHANT:
                b = 5;
                break;
            case OIE:
                b = 6;
                break;
            case COQ:
                b = 7;
                break;
            case SINGE:
                b = 8;
                break;
            case MANTE:
                b = 9;
                break;
            case CHEVAL:
                b = 10;
                break;
            case BOEUF:
                b = 11;
                break;
            case GRUE:
                b = 12;
                break;
            case SANGLIER:
                b = 13;
                break;
            case ANGUILLE:
                b = 14;
                break;
            case COBRA:
                b = 15;
                break;
        }
        return b;
    }

    /**
     * Convertit un octet représentant une carte en cette même carte
     * @param b octet à convertir
     * @return la carte représentée par l'octet
     */
    public Carte octet2Carte(byte b) {
        Carte c = null;
        switch ((int) b) {
            case 0:
                c = new Carte(TIGRE);
                break;
            case 1:
                c = new Carte(DRAGON);
                break;
            case 2:
                c = new Carte(GRENOUILLE);
                break;
            case 3:
                c = new Carte(LAPIN);
                break;
            case 4:
                c = new Carte(CRABE);
                break;
            case 5:
                c = new Carte(ELEPHANT);
                break;
            case 6:
                c = new Carte(OIE);
                break;
            case 7:
                c = new Carte(COQ);
                break;
            case 8:
                c = new Carte(SINGE);
                break;
            case 9:
                c = new Carte(MANTE);
                break;
            case 10:
                c = new Carte(CHEVAL);
                break;
            case 11:
                c = new Carte(BOEUF);
                break;
            case 12:
                c = new Carte(GRUE);
                break;
            case 13:
                c = new Carte(SANGLIER);
                break;
            case 14:
                c = new Carte(ANGUILLE);
                break;
            case 15:
                c = new Carte(COBRA);
                break;
        }
        return c;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(config);
    }

    @Override
    public int compareTo(ConfigurationPlateau cp) {
        return hashCode() - cp.hashCode();
    }
}
