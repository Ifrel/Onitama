package Modele;

import javax.swing.*;
import java.awt.*;

/**
 * Placeholder pour la classe Pion.
 * Vous devez implémenter votre propre classe Pion avec les attributs
 * et méthodes pertinents pour votre jeu (par ex. position, type, couleur, etc.).
 * Cette classe est ici uniquement pour permettre la compilation de Joueur.
 */
public class Pion {
     // Exemple d'attributs nécessaires basés sur l'utilisation dans EcranPlateauDeJeu
     private Color couleur;     // La couleur du pion, souvent liée à la couleur du joueur propriétaire
     private String type;       // Le type du pion (ex: "TIGRE", "ELEPHANT")
     private int prorietaire;
     // Ajoutez position, etc.

     public Pion(Color couleur, String type) {
          this.couleur = couleur;
          this.type = type;
          this.prorietaire = 0;
     }

    public Pion() {
        // je ne sais pas pourquoi pas ultil pour la class pionEtudiant/pionMaitre (à rechercher pourquoi)
    }

    public Color getCouleur() {
          return couleur;
     }

     public String getType() {
          return type;
     }

     public int getProprietaire(){
         return prorietaire;
     }

    public void setProrietaire(int prorietaire) {
        this.prorietaire = prorietaire;
    }

    public ImageIcon getIcon() {
        return null;
    }

// Ajoutez d'autres getters/setters si nécessaire
}