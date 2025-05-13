package Modele; // Exemple de package, ajustez si nécessaire

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Modele.Carte;

import static Global.Config.*;

/**
 * Représente un joueur participant à la partie.
 * Un joueur possède un nom, une couleur, un identifiant, un score,
 * une main de cartes et potentiellement des pions sur le plateau. */
public class Joueur {
    private String nom;
    private final int id;
    private int score;
    private List<Carte> mainCartes;
    private List<Pion> pions;


    /**
     * Constructeur pour créer un nouveau joueur.
     * Initialise les propriétés de base et crée des listes vides pour les cartes et les pions.
     *
     * @param id L'identifiant unique du joueur (par ex. 1 pour Joueur 1).
     * @param nom Le nom du joueur (ne doit pas être null ou vide).
     * @throws NullPointerException si nom ou couleur est null.
     * @throws IllegalArgumentException si nom est vide ou id est invalide (par ex. < 1).*/
    public Joueur(int id, String nom) {
        // Validation des paramètres d'entrée
        if (id < 1)  throw new IllegalArgumentException("L'identifiant du joueur doit être supérieur ou égal à 1.");
        this.nom = Objects.requireNonNull(nom, "Le nom du joueur ne peut pas être null.");
        if (this.nom.trim().isEmpty()) throw new IllegalArgumentException("Le nom du joueur ne peut pas être vide.");

        this.id = id;

        // Initialisation des attributs variables
        this.score = 0;
        this.mainCartes = new ArrayList<>(); // Utiliser ArrayList pour une liste dynamique
        this.pions = new ArrayList<>(); // Utiliser ArrayList pour une liste dynamique
    }



    // =========================================
    // =========== Getters (Accesseurs) ========
    // =========================================

    /**
     * Retourne l'identifiant unique du joueur.
     * @return L'ID du joueur.
     */
    public int getId() {
        return id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le nom du joueur.
     * @return Le nom du joueur.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne le score actuel du joueur.
     * @return Le score du joueur.
     */
    public int getScore() {
        return score;
    }


    /**
     * Retourne la liste des cartes que le joueur a actuellement en main.
     * NOTE : Retourne une copie pour éviter que des modifications externes
     * n'affectent directement la main du joueur.
     * @return Une nouvelle liste contenant les cartes en main du joueur.
     */
    public List<Carte> getCartesEnMain() { // Méthode utilisée dans EcranPlateauDeJeu.updateCartes
        return new ArrayList<>(mainCartes); // Retourne une copie
    }


    /**
     * Retourne la liste des pions appartenant à ce joueur.
     * NOTE : Retourne une copie pour éviter des modifications externes inattendues.
     * @return Une nouvelle liste contenant les pions du joueur.
     */
    public List<Pion> getPions() {
        return new ArrayList<>(pions); // Retourne une copie
    }


    // Ajoutez des getters pour d'autres attributs si vous en ajoutez

    // =========================================
    // =========== Setters (Mutateurs) =========
    // =========================================

    /**
     * Définit le score du joueur.
     * @param score Le nouveau score du joueur.
     */
    public void setScore(int score) {
        this.score = score;
        // Dans une application réelle, vous pourriez notifier des observateurs ici
        // si d'autres parties du système doivent réagir aux changements de score.
    }



    // =========================================
    // ========= Gestion des Cartes ==========
    // =========================================

    /**
     * Ajoute une carte à la main du joueur.
     * @param carte La carte à ajouter (ne doit pas être null).
     * @throws NullPointerException si la carte est null.
     */
    public void addCard(Carte carte) {
        Objects.requireNonNull(carte, "La carte à ajouter ne peut pas être null.");
        this.mainCartes.add(carte);
        // Logique de notification si besoin (ex: main changée)
    }

    /**
     * Ajoute une carte à la main du joueur.
     * @param cartes liste des cartes à ajouter (ne doit pas être null).
     * @throws NullPointerException si la carte est null.
     */
    public void addCards(List<Carte> cartes) {
        Objects.requireNonNull(cartes, "Les cartes à ajouter ne peuvent pas être null.");
        this.mainCartes.addAll(cartes);
        // Logique de notification si besoin (ex: main changée)
    }


    /**
     * Ajoute une carte à la main du joueur.
     *
     * @param typeCartes liste des cartes à ajouter (ne doit pas être null).
     * @throws NullPointerException si la carte est null.
     */
    public void addCardsType(List<TYPECARTE> typeCartes) {
        Objects.requireNonNull(typeCartes, "Les cartes à ajouter ne peuvent pas être null.");
        for (int i = 0; i < typeCartes.size(); i++) {
            this.mainCartes.add(new Carte(typeCartes.get(i)));
        }
        // Logique de notification si besoin (ex: main changée)
    }


    /**
     * Retire une carte spécifique de la main du joueur.
     * Utilise la méthode equals() de la classe Carte pour trouver la carte.
     *
     * @param carte La carte à retirer (ne doit pas être null).
     * @return true si la carte a été trouvée et retirée, false sinon.
     * @throws NullPointerException si la carte est null.
     */
    public boolean removeCard(Carte carte) {
        Objects.requireNonNull(carte, "La carte à retirer ne peut pas être null.");
        return this.mainCartes.remove(carte);
    }

    /**
     * Retire une carte de la main du joueur à un index spécifique.
     * Utile si l'ordre des cartes est important pour l'UI.
     *
     * @param index L'index de la carte à retirer (doit être valide).
     * @return La carte qui a été retirée.
     * @throws IndexOutOfBoundsException si l'index est hors des limites de la main.
     */
    public Carte removeCard(int index) {
        return this.mainCartes.remove(index);
    }


    /**
     * Vide la main de cartes du joueur.
     */
    public void clearHand() {
        this.mainCartes.clear();
        // Logique de notification si besoin
    }

    // =========================================
    // ========= Gestion des Pions ===========
    // =========================================
    // Ces méthodes dépendent de la manière dont les pions sont gérés dans votre jeu.
    // Si les pions sont créés une fois au début et restent attachés au joueur,
    // vous pouvez les ajouter dans le constructeur ou une méthode d'initialisation.
    // Si les pions peuvent être ajoutés ou retirés dynamiquement, utilisez ces méthodes.

    /**
     * Ajoute un pion à la liste des pions du joueur.
     * @param pion Le pion à ajouter (ne doit pas être null).
     * @throws NullPointerException si le pion est null.
     */
    public void addPion(Pion pion) {
        Objects.requireNonNull(pion, "Le pion à ajouter ne peut pas être null.");
        this.pions.add(pion);
        // Logique de notification si besoin
    }


    /**
     * Retire un pion spécifique de la liste des pions du joueur.
     * @param pion Le pion à retirer (ne doit pas être null).
     * @return true si le pion a été trouvé et retiré, false sinon.
     * @throws NullPointerException si le pion est null.
     */
    public boolean removePion(Pion pion) {
        Objects.requireNonNull(pion, "Le pion à retirer ne peut pas être null.");
        return this.pions.remove(pion);
    }

    // =========================================
    // ===== Méthodes Utilitaires Standard =====
    // =========================================

    /**
     * Compare ce joueur à un autre objet pour vérifier l'égalité.
     * Deux joueurs sont considérés égaux s'ils ont le même identifiant.
     *
     * @param o L'objet à comparer.
     * @return true si les objets sont égaux, false sinon.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Joueur joueur = (Joueur) o;
        // Comparer par l'identifiant unique
        return id == joueur.id;
    }

    /**
     * Retourne le code de hachage pour ce joueur.
     * Compatible avec la méthode equals().
     *
     * @return Le code de hachage.
     */
    @Override
    public int hashCode() {
        // Utiliser l'identifiant pour le hashCode
        return Objects.hash(id);
    }

    /**
     * Retourne une représentation textuelle du joueur.
     * Utile pour le débogage.
     *
     * @return Une chaîne de caractères représentant le joueur.
     */
    @Override
    public String toString() {
        return "Joueur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", score=" + score +
                ", cartesEnMain=" + mainCartes.size() + // Afficher juste le nombre de cartes
                '}';
    }



}