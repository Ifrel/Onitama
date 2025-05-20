package Modele.IA;

import Modele.Carte;
import Modele.Coup;
import Modele.Pion;
import Modele.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static Global.Config.*;
import static Global.Config.ROLEPION.*;

public class EtatJeu {
    //private Jeu jeu;
    private Coup coup;
    private Pion pionChoisi;
    private int carteChoisie;
    private final int idJoueurCourant;
    private final TYPECARTE carteEnPlus;
    private final List<TYPECARTE> cartesJoueur1;
    private final List<TYPECARTE> cartesJoueur2;
    private final List<Pion> pionsJoueur1;
    private final List<Pion> pionsJoueur2;

    public EtatJeu(int joueurCourantID, Carte carteEnPlus, List<Carte> cartesJoueur1, List<Carte> cartesJoueur2, List<Pion> pionsJoueur1, List<Pion> pionsJoueur2) {
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

//        this.jeu = jeu;

        this.idJoueurCourant = joueurCourantID;
        this.carteEnPlus = carteEnPlus.getType();

        this.cartesJoueur1 = new ArrayList<>();
        for (Carte c : cartesJoueur1) {
            this.cartesJoueur1.add(c.getType());
        }

        this.cartesJoueur2 = new ArrayList<>();
        for (Carte c : cartesJoueur2) {
            this.cartesJoueur2.add(c.getType());
        }

        this.pionsJoueur1 = new ArrayList<>();
        this.pionsJoueur1.addAll(pionsJoueur1);

        this.pionsJoueur2 = new ArrayList<>();
        this.pionsJoueur2.addAll(pionsJoueur2);
    }

    public int getIdJoueurCourant() {
        return this.idJoueurCourant;
    }

    public TYPECARTE getTypeCarteSupplementaire() {
        return carteEnPlus;
    }

    public List<Pion> getPionsJoueur1() {
        return this.pionsJoueur1;
    }

    public List<Pion> getPionsJoueur2() {
        return this.pionsJoueur2;
    }

    public List<Pion> getPionsJoueurCourant() {
        if (getIdJoueurCourant() == ID_JOUEUR_1) {
            return getPionsJoueur1();
        } else {
            return getPionsJoueur2();
        }
    }

    public List<Carte> getCartesJoueur1() {
        List<Carte> res = new ArrayList<>();
        for (TYPECARTE tc : cartesJoueur1) {
            res.add(new Carte(tc));
        }
        return res;
    }

    public List<Carte> getCartesJoueur2() {
        List<Carte> res = new ArrayList<>();
        for (TYPECARTE tc : cartesJoueur2) {
            res.add(new Carte(tc));
        }
        return res;
    }

    public List<Carte> getCartesJoueurCourant() {
        if (getIdJoueurCourant() == ID_JOUEUR_1) {
            return getCartesJoueur1();
        } else {
            return getCartesJoueur2();
        }
    }

    public Coup getCoup() {
        return coup;
    }

    public void setCoup(Coup c) {
        this.coup = c;
    }

    public List<EtatJeu> getSuccesseurs() {
        List<EtatJeu> lej = new ArrayList<>();
        List<Carte> cartesJoueurCourant = this.getCartesJoueurCourant();
        List<Pion> pionsJoueurCourant = this.getPionsJoueurCourant();
        int ca = 0;
        for (Carte c : cartesJoueurCourant) {
            for (Pion p : pionsJoueurCourant) {
                List<Coup> coupsPossibles = Utils.getCoupsPossibles(this, c.getType(), p.getPosition());
                for (Coup cp : coupsPossibles) {
                    Point arrivee = cp.getArrivee();
//                        if (!jeu.estCaseVide(arrivee.x, arrivee.y) && jeu.getProprietairePionAt(arrivee.x, arrivee.y) != jeu.getIdJoueurCourant()) {
//                            res += 1;
//                        }
                    List<Pion> pionsAdverse;
                    if (getIdJoueurCourant() == ID_JOUEUR_1) {
                        pionsAdverse = new ArrayList<>(this.pionsJoueur2);
                    } else {
                        pionsAdverse = new ArrayList<>(this.pionsJoueur1);
                    }
                    List<Pion> _pionsAdverse = new ArrayList<>(pionsAdverse);
                    for (Pion pa : _pionsAdverse) {
                        if (pa.getPosition().equals(arrivee)) {
                            pionsAdverse.remove(pa);
                        }
                    }
                    List<Pion> pionsCourant = new ArrayList<>(getPionsJoueurCourant());
                    for (Pion pjc : getPionsJoueurCourant()) {
                        if (pjc.getPosition().equals(cp.getDepart())) {
                            ROLEPION rp = pjc.getRole();
                            pionsCourant.remove(pjc);
                            pionsCourant.add(new Pion(getIdJoueurCourant(), arrivee, rp));

                        }
                    }
                    List<Carte> cJ1 = new ArrayList<>();
                    for (TYPECARTE tc : this.cartesJoueur1) {
                        cJ1.add(new Carte(tc));
                    }
                    List<Carte> cJ2 = new ArrayList<>();
                    for (TYPECARTE tc : this.cartesJoueur2) {
                        cJ2.add(new Carte(tc));
                    }
                    List<Carte> newCards = new ArrayList<>(cartesJoueurCourant);
                    Carte carteSup;
                    carteSup = c;
                    newCards.remove(c);
                    newCards.add(new Carte(this.carteEnPlus));
                    EtatJeu ej = new EtatJeu(
                            (this.idJoueurCourant % 2) + 1,
                            carteSup,
                            this.idJoueurCourant == ID_JOUEUR_1 ? newCards : cJ1,
                            this.idJoueurCourant == ID_JOUEUR_2 ? newCards : cJ2,
                            this.idJoueurCourant == ID_JOUEUR_1 ? pionsCourant : pionsAdverse,
                            this.idJoueurCourant == ID_JOUEUR_2 ? pionsCourant : pionsAdverse
                            );
                    ej.setCoup(cp);
                    ej.setCarteChoisie(ca);
                    ej.setPionChoisi(p);
                    lej.add(ej);
                }
            }
            ca++;
        }

        return lej;

    }

    public void setCarteChoisie(int carteChoisie) {
        this.carteChoisie = carteChoisie;
    }

    public int getCarteChoisie() {
        return carteChoisie;
    }

    public void setPionChoisi(Pion pionChoisi) {
        this.pionChoisi = pionChoisi;
    }

    public Pion getPionChoisi() {
        return pionChoisi;
    }

    public boolean estEtatFinal() {
        if (pionsJoueur1.size() == 0 || pionsJoueur2.size() == 0) {
            return true;
        }
        boolean maitre1, maitre2;
        maitre1 = maitre2 = true;
        for (Pion p : pionsJoueur1) {
            if (p.getRole() == PION_MAITRE) {
                if (p.getPosition().equals(TEMPLE_JOUEUR_2)) {
                    return true;
                }
                maitre1 = false;
            }
        }
        for (Pion p : pionsJoueur2) {
            if (p.getRole() == PION_MAITRE) {
                if (p.getPosition().equals(TEMPLE_JOUEUR_1)) {
                    return true;
                }
                maitre2 = false;
            }
        }

        return maitre1;
    }
}
