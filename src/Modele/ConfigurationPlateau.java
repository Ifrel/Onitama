package Modele;

import java.util.ArrayList;
import java.util.List;

import static Global.Config.TYPECARTE.*;

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
        this.cartesJ1 = listeCartes2ListeOctets(cartesJoueur1);
        this.cartesJ2 = listeCartes2ListeOctets(cartesJoueur2);
        this.carteRetrait = carte2Octet(carteEnPlus);

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
    public byte[] listeCartes2ListeOctets (List<Carte> listeCartes) {
        byte[] listeOctets = new byte[listeCartes.size()];
        for (int i = 0; i < listeCartes.size(); i++) {
            listeOctets[i] = carte2Octet(listeCartes.get(i));
        }
        return listeOctets;
    }

    /**
     * Convertit une liste d'octets représentant des cartes en une liste de cartes
     * @param listeOctets liste de cartes en octets à convertir
     * @return liste d'octets représentant les cartes
     */
    public List<Carte> listeOctets2ListeCartes (byte[] listeOctets) {
        List<Carte> listeCartes = new ArrayList<>();
        for (byte octet : listeOctets) {
            listeCartes.add(octet2Carte(octet));
        }
        return listeCartes;
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
}
