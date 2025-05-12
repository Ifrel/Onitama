package Modele;


import java.awt.*;
import java.util.*;
import java.util.List;

import static Global.Config.TAILLE_VECTEUR_BITS;
import static Global.Config.ROLEPION;

/**
 * Représentation compacte de l'état du plateau de jeu à un moment donné de la partie
 */
public class ConfigurationPlateau implements Comparable<ConfigurationPlateau> {
    private byte [] config; // 12 octets = 96 bits; BIG ENDIAN
    //  4    4     4    4     4           25 (5 x 5)                 25 (5 x 5)            5     5      5     5      6
    // 1111 1111  1111 1111  1111  1111111111111111111111111  1111111111111111111111111  11111 11111  11111 11111  XXXXXX
    // ^^^^ ^^^^  ^^^^ ^^^^  ^^^^  ^^^^^^^^^^^^^^^^^^^^^^^^^  ^^^^^^^^^^^^^^^^^^^^^^^^^  ^^^^^ ^^^^^  ^^^^^ ^^^^^  ^^^^^^
    //  |    |     |    |     |               |                          |                |     |      |     |       |
    //  |    |     |    |     |               |                          |                |     |      |     |      6 bits inutilisés
    //  |    |     |    |     |               |                          |                |     |      |    Position Colonne Maitre Joueur 2
    //  |    |     |    |     |               |                          |                |     |     Position Ligne Maitre Joueur 2
    //  |    |     |    |     |               |                          |                |    Position Colonne Maitre Joueur 1
    //  |    |     |    |     |               |                          |               Position Ligne Maitre Joueur 1
    //  |    |     |    |     |               |                         Positions Pions Etudiants Joueur 2
    //  |    |     |    |     |              Positions Pions Etudiants Joueur 1
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

        //config = new byte[12]; // 96 bits, 90 utilisés

        BitSet bitset = new BitSet(TAILLE_VECTEUR_BITS);


        ecrireVecteur(0, carteToBits(cartesJoueur1.get(0)), bitset);
        ecrireVecteur(4, carteToBits(cartesJoueur1.get(1)), bitset);

        ecrireVecteur(8, carteToBits(cartesJoueur2.get(0)), bitset);
        ecrireVecteur(12, carteToBits(cartesJoueur2.get(1)), bitset);

        ecrireVecteur(16, carteToBits(carteEnPlus), bitset);

        ecrireVecteur(20, pionsEtudiantsToBits(pionsJoueur1), bitset);
        ecrireVecteur(45, pionsEtudiantsToBits(pionsJoueur2), bitset);

        ecrireVecteur(70, pionMaitreToBits(pionsJoueur1), bitset);
        ecrireVecteur(80, pionMaitreToBits(pionsJoueur2), bitset);

        config = bitsetToCompactByteArray(bitset);

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
    private boolean [] carteToBits(Carte c) {
        int b = -1;
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
        String binaire = Integer.toBinaryString(b);
        if (binaire.length() < 4) {
            binaire = "0" + binaire;
        }
        int bl = binaire.length();
        boolean [] res = new boolean[bl];

        for (int i = 0; i < bl; i++) {
            if (binaire.charAt(i) == '1') {
                res[i] = true;
            }
        }
        return res;
    }

    /**
     * Renvoie un vecteur de booléens qui indique la valeur d'une séquence de bits (relatif à un point de départ)
     * Seulement pour les pions étudiants
     * @param lp Liste de pions
     * @return Un vecteur de booléen indiquant des valeurs de bit
     */
    private boolean [] pionsEtudiantsToBits(List<Pion> lp) {
        boolean [] res = new boolean[25];
        for (Pion p: lp) {
            if (p.getStatut() == ROLEPION.PION_ETUDIANT) {
                Point pos = p.getPosition();
                int x, y;
                x = pos.x;
                y = pos.y;

                res[x * 5 + y] = true;
            }
        }
        return res;
    }

    /**
     * Renvoie un vecteur de booléens qui indique la valeur d'une séquence de bits (relatif à un point de départ)
     * Seulement pour les pions maîtres
     * @param lp Liste de pions
     * @return Un vecteur de booléen indiquant des valeurs de bit
     */
    private boolean [] pionMaitreToBits(List<Pion> lp) {
        boolean [] res = new boolean[10];
        for (Pion p: lp) {
            if (p.getStatut() == ROLEPION.PION_MAITRE) {
                Point pos = p.getPosition();
                int x, y;
                x = pos.x;
                y = pos.y;

                res[x] = true;
                res[5 + y] = true;
            }
        }
        return res;
    }

    /**
     * Défini les valeurs des bits dans 'bs' conformément à 'pos', en partant de 'début', jusqu'à atteindre la taille de 'pos'
     * @param debut Point de départ dans le vecteur de bits
     * @param pos Vecteur de booléen de taille variable indiquant les valeurs des bits (relatif à 'debut')
     * @param bs Bitset (séquence de bits) où on écrit
     */
    private void ecrireVecteur(int debut, boolean [] pos, BitSet bs) {
        int pl = pos.length;
        for (int i = 0; i < pl; i++) {
            if (pos[i]) {
                bs.set(debut + i);
            } else {
                bs.clear(debut + i);
            }
        }
    }

    /**
     * Convertit un Bitset (séquence de bits) en un vecteur de bits plus compact
     * @param bs Bitset à convertir
     * @return Vecteur de bits compact
     */
    private byte [] bitsetToCompactByteArray(BitSet bs) {
        // int bl = bs.size();
        int bl = TAILLE_VECTEUR_BITS;
        byte [] res = new byte[bl / 8];
        byte buffer = 0;
        for (int i = 0; i < bl; i += 8 ) {
            buffer = 0;
            for (int j = 0; j < 8; j++) {
                int b;
                if (bs.get(i + j)) {
                    b = 1;
                } else {
                    b = 0;
                }
                System.err.print(b);
                buffer = (byte) (buffer | (byte)(b << (7 - j)));
                // System.err.println("i : " + i + " buffer : " + buffer);
            }
            res[i / 8] = buffer;
        }
        System.err.println();
        return res;
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
