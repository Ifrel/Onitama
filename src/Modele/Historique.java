package Modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Gère la possibilité d'annuler et de refaire une série d'actions
 * @param <T> Action à stocker dans l'historique
 */
public class Historique<T> {

    private Stack<T> passe;
    private Stack<T> futur;

    /**
     * Initialise un historique vide
     */
    public Historique() {
        passe = new Stack<T>();
        futur = new Stack<T>();
    }

    /**
     * Vide l'historique (le remet à zéro)
     */
    public void reset() {
        passe = new Stack<T>();
        futur = new Stack<T>();
    }

    /**
     * Indique si on peut annuler la dernière action effectuée
     * @return vrai si on peut annuler la dernière action, faux sinon
     */
    public boolean peutAnnuler() {
        return ! passe.isEmpty();
    }

    /**
     * Indique si on peut refaire la dernière action annulée
     * @return vrai si on peut refaire la dernière action après l'avoir annulée, faux sinon
     */
    public boolean peutRefaire() {
        return ! futur.isEmpty();
    }

    /**
     * Annule la dernière action effectuée et la renvoie
     * @return la dernière action effectuée, considérée comme annulée
     */
    public T annuler() {
        if (! peutAnnuler()) {
            return null;
        }
        T action = passe.pop();
        futur.push(action);
        return action;

    }

    /**
     * Refait la dernière action et la renvoie
     * @return la dernière action annulée, considérée comme refaite
     */
    public T refaire() {
        if (! peutRefaire()) {
            return null;
        }
        T action = futur.pop();
        passe.push(action);
        return action;
    }

    /**
     * Déroulement normal d'une suite d'action, ajoute une action sans annuler ni refaire
     * @param action l'action à ajouter à l'historique
     */
    public void add(T action) {
        passe.push(action);
        futur.clear();
    }

    /**
     * Fixe la valeur de l'historique
     * @param passe Référence vers une liste contenant une liste d'actions passées
     * @param futur Référence vers une liste contenant une liste d'actions annulées pretes à etre refaites
     */
    public void setHistorique(List<T> passe, List <T> futur) {
        reset();
        for (T action : passe) {
            this.passe.push(action);
        }
        for (T action : futur) {
            this.futur.push(action);
        }
    }

    /**
     * Renvoie la taille de la partie "passé" de l'historique
     * @return la taille de la partie "passé" de l'historique
     */
    public int passeSize() {
        return passe.size();
    }

    /**
     * Renvoie la taille de la partie "futur" de l'historique
     * @return la taille de la partie "futur" de l'historique
     */
    public int futurSize() {
        return futur.size();
    }

    /**
     * Fourni une liste des actions effectuées
     * @return le contenu du "passé"
     */
    public List<T> dumpPasse() {
        ArrayList<T> liste = new ArrayList<>();
        for (int i = 0; i < passeSize(); i++) {
            liste.add(passe.get(i));
        }
        return liste;
    }

    /**
     * Fourni une liste des actions annulées pretes à etre refaites
     * @return le contenu du "futur"
     */
    public List<T> dumpFutur() {
        ArrayList<T> liste = new ArrayList<>();
        for (int i = 0; i < futurSize(); i++) {
            liste.add(futur.get(i));
        }
        return liste;
    }


    /**
     * Génère et renvoie une chaine de caractère qui représente l'état de l'historique
     * @return la réprésentation textuelle de l'historique
     */
    @Override
    public String toString() {
        StringBuilder S = new StringBuilder();
        S.append("Passe:{");
        S.append("coups:{");
        for (int i = 0; i < passe.size() - 1; i++) {
            S.append("{").append(passe.get(i).toString()).append("}");
            S.append(",");
        }
        if (passe.size() > 0) {
            S.append(passe.get(passe.size() - 1).toString());
        }
        S.append("}");
        S.append("}");
        S.append("\n");
        S.append("Futur:{");
        S.append("coups:{");
        for (int i = 0; i < futur.size() - 1; i++) {
            S.append("{").append(futur.get(i).toString()).append("}");
            S.append(",");
        }
        if (futur.size() > 0) {
            S.append(futur.get(futur.size() - 1).toString());
        }
        S.append("}");
        S.append("}");

        return S.toString();
    }
}
