package Modele;

import java.util.List;

/**
 * Représentation compacte de l'état du plateau de jeu à un moment donné de la partie
 */
public class ConfigurationPlateau {
    private byte joueurCourantID;
    private byte[] cartesJ1;
    private byte[] cartesJ2; // utile ???
    private byte carteRetrait;
    private byte[] pionsJ1;
    private byte[] pionsJ2;

    ConfigurationPlateau(int joueurCourantID, List<Carte> cartesJoueur1, List<Carte> cartesJoueur2, Carte carteEnPlus, List<Pion> pionsJoueur1, List<Pion> pionsJoueur2) {
        this.joueurCourantID = joueurID2Octet(joueurCourantID);
        this.cartesJ1 = cartes2ListeOctets(cartesJoueur1);
        this.cartesJ2 = cartes2ListeOctets(cartesJoueur2);

    }

    /**
     * Convertit l'identifiant du joueur fourni en octet et le renvoie
     * @param id identifiant du joueur dont on veut récupérer l'identifiant sour forme d'octet
     * @return l'octet représentant l'identifiant du joueur
     */
    public byte joueurID2Octet(int id) {
        if (id < 0 || id > 255) {
            throw new RuntimeException("LA classe " + ConfigurationPlateau.class.getName() + " ne traite que des octets positifs (donc valeur entre 0 et 255");
        }
        return (byte) id;
    }

    /**
     * Convertit l'octet représentant l'identifiant du jouer courant en un entier
     * @param b l'octet représentant l'identifiant du joueur
     * @return identifiant du joueur courant sous forme d'entier
     */
    public int octet2JoueurID(byte b) {
        return (int) b;
    }

    /**
     * Convertit une liste de carte en une liste d'octets
     * @param listeCartes liste de cartes à convertir
     * @return liste d'octets représentant les cartes
     */
    public byte[] cartes2ListeOctets (List<Carte> listeCartes) {
        byte[] listeOctets = new byte[listeCartes.size()];
        for (int i = 0; i < listeCartes.size(); i++) {
            Carte c = listeCartes.get(i);
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
            listeOctets[i] = b;
        }
        return listeOctets;
    }

//    /**
//     * Convertit une liste d'octets représentant des cartes en une liste de cartes
//     * @param listeOctets liste de cartes en octets à convertir
//     * @return liste d'octets représentant les cartes
//     */
//    public List<Carte> cartes2ListeOctets (byte[] listeOctets) {
//        byte[] listeOctets = new byte[listeCartes.size()];
//        for (int i = 0; i < listeCartes.size(); i++) {
//            Carte c = listeCartes.get(i);
//            byte b = 0;
//            switch (c.getType()) {
//                case TIGRE:
//                    b = 0;
//                    break;
//                case DRAGON:
//                    b = 1;
//                    break;
//                case GRENOUILLE:
//                    b = 2;
//                    break;
//                case LAPIN:
//                    b = 3;
//                    break;
//                case CRABE:
//                    b = 4;
//                    break;
//                case ELEPHANT:
//                    b = 5;
//                    break;
//                case OIE:
//                    b = 6;
//                    break;
//                case COQ:
//                    b = 7;
//                    break;
//                case SINGE:
//                    b = 8;
//                    break;
//                case MANTE:
//                    b = 9;
//                    break;
//                case CHEVAL:
//                    b = 10;
//                    break;
//                case BOEUF:
//                    b = 11;
//                    break;
//                case GRUE:
//                    b = 12;
//                    break;
//                case SANGLIER:
//                    b = 13;
//                    break;
//                case ANGUILLE:
//                    b = 14;
//                    break;
//                case COBRA:
//                    b = 15;
//                    break;
//            }
//            listeOctets[i] = b;
//        }
//        return listeOctets;
//    }
}
