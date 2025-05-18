package Modele;

import Global.Config.TYPECARTE;
import static Global.Config.MOUVEMENTCARTE;

import java.awt.*;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Représente une carte dans le jeu.
 * Une carte possède un nom, un type, une description de son effet, et potentiellement un chemin vers son image.
 * Cette classe est axée sur les propriétés de la carte; la logique d'application de l'effet
 * sera généralement gérée ailleurs (par exemple, dans la classe Jeu ou un gestionnaire d'effets).
 */
public class Carte implements Serializable{
    private static final long serialVersionUID = 1L;
    private String nom;                 // Nom unique ou identifiant de la carte
    private TYPECARTE type;             // Type de la carte
    private int proprietaire;

    public Carte(TYPECARTE type){
        this.type = type;
        nom = type.name();
    }

    /**
     * Retourne le nom de la carte.
     * @return Le nom de la carte.
     */
    public String getNom() { return nom; }


    /**
     * Retourne le type de la carte.
     * @return Le type de la carte.
     */
    public TYPECARTE getType() { return type; }


//    public List<Coup> getMoves(Point origin, int joueurAct, Carte CE)
//    {
//        List<Coup> allMoves = new ArrayList<Coup>();
//        for (Point p : MOUVEMENTCARTE.get(type)) {
//            Point nouveauPoint = new Point(origin.x + p.x, origin.y + p.y);
//            Coup nouveauCoup = new Coup(origin, nouveauPoint,joueurAct, this,CE );
//            allMoves.add(nouveauCoup);
//        }
//        return allMoves;
//    }

    public List<Point> getMoves() {
        return MOUVEMENTCARTE.get(type);
    }


    public int getProprietaire() {
        return this.proprietaire;
    }

    public void setProprietaire(int p) {
        if (p > 2 || p < 0) {
            throw new RuntimeException("Propriétaire invalide, devrait 0, 1 ou 2, pas " + p);
        }
       this.proprietaire = p;
    }



    // =========================================
    // ===== Méthodes Utilitaires Standard =====
    // =========================================

    /**
     * Compare cette carte à un autre objet pour vérifier l'égalité.
     * Deux cartes sont considérées égales si elles ont le même nom et le même type.
     *
     * @param o L'objet à comparer.
     * @return true si les objets sont égaux, false sinon.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carte carte = (Carte) o;
        // Comparer les attributs qui définissent l'identité unique d'une carte.
        // Ici, on utilise le nom et le type comme identifiants.
        return Objects.equals(nom, carte.nom) && type == carte.type;
    }


    /**
     * Retourne le code de hachage pour cette carte.
     * Compatible avec la méthode equals().
     *
     * @return Le code de hachage.
     */
    @Override
    public int hashCode() {
        // Utiliser les mêmes attributs que dans equals() pour calculer le hashCode
        return Objects.hash(nom, type);
    }



    /**
     * Retourne une représentation textuelle de la carte.
     * Utile pour le débogage.
     *
     * @return Une chaîne de caractères représentant la carte.
     */
    @Override
    public String toString() {
        return "Carte{" +
                "nom='" + nom + '\'' +
                ", type=" + type +
                '}'; // Omet la description et l'image pour une chaîne courte
    }
}