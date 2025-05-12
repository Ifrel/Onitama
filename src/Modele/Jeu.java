package Modele;

import static Global.Config.TYPECARTE.*;
import static Global.Config.*;
import static Global.Config.ROLEPION.*;
import Patterns.Observable;

import java.awt.*;
import java.time.Duration;
import java.util.*;
import java.util.List;


public class Jeu extends Observable {
// ######## ANNULER / REFAIRE ########

public boolean peutAnnulerCoup() {
    return true;
}

public boolean peutRefaireCoup() {
    return true;
}

public void annulerCoup() {
    return;
}

public void refaireCoup() {
    return;
}

// ######### CHARGER / SAUVEGARDER ########

public void sauvegarderJeu(String fichier) {
    return;
}

public void chargerJeu(String fichier) {
    return;
}

public List<String> listerSauvegardes() {
    return null;
}

// ######## STATUT / DONNEES ########

public int lignes() {
    return 5;
}

public int colonnes() {
    return 5;
}

public int casesTotales() {
    return lignes() * colonnes();
}

public boolean estCaseVide(int i, int j) {
    return true;
}

public boolean estPionEtudiantJoueur1(int i, int j) {
    return false;
}

public boolean estPionEtudiantJoueur2(int i, int j) {
    return true;
}

public boolean estPionMaitreJoueur1(int i, int j) {
    return false;
}

public boolean estPionMaitreJoueur2(int i, int j) {
    return false;
}

public List<Pion> getPionsJoueur1() {
    return null;
}

public List<Pion> getPionsJoueur2() {
    return null;
}

public List<Carte> getCartesJoueur1() {
    return null;
}

public List<Carte> getCartesJoueur2() {
    return null;
}

public Carte getCarteSupplementaire() {
    return null;
}

public Carte getCarteJouee() {
    return null;
}

public boolean estPionDuJoueurCourant(int i, int j) {
    return false;
}

public boolean estCoupConforme(int i, int j) {
    return false;
}

public int getProprietairePionAt(int i, int j) {
    return 1;
}

public ROLEPION getRolePionAt(int i, int j) {
    return PION_ETUDIANT;
}

// ######## PARTIE ########

public void nouvellePartie() {
    return;
}

public long tempsDeJeu() {
    return 1234;
}

public void setNomJoueur1() {
    return;
}

public void setNomJoueur2() {
    return;
}

public String getNomJoueur1() {
    return "Abcd";
}

public String getNomJoueur2() {
    return "bonjour";
}

public String getNomJoueurCourant() {
    return "bonjour";
}

public boolean partieEstFinie() {
    return false;
}

public List<Coup> getCoupsPossibles(Point positionPion, Carte carteJoue) {
    return null;
}

public void jouerCoup(Coup c) {

}
}
