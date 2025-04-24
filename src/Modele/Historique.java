package Modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Historique<T> {

    private Stack<T> passe;
    private Stack<T> futur;

    public Historique() {
        passe = new Stack<T>();
        futur = new Stack<T>();
    }

    ///  vide l'historique
    public void reset() {
        passe = new Stack<T>();
        futur = new Stack<T>();
    }

    ///  vérifie si on peut annuler la dernière action
    public boolean peutAnnuler() {
        return ! passe.isEmpty();
    }

    ///  vérifie si on peut refaire la dernière action après l'avoir annulée
    public boolean peutRefaire() {
        return ! futur.isEmpty();
    }

    ///  annule la dernière action
    public T annuler() {
        if (! peutAnnuler()) {
            return null;
        }
        T e = passe.pop();
        futur.push(e);
        return e;

    }

    /// refais la dernière action annulée
    public T refaire() {
        if (! peutRefaire()) {
            return null;
        }
        T e = futur.pop();
        passe.push(e);
        return e;
    }

    ///  ajoute à l'historique l'élément 'e'
    public void add(T e) {
        passe.push(e);
        futur.clear();
    }

    ///  fixe valeur de l'historique
    public void setHistorique(Stack<T> passe, Stack <T> futur) {
        this.passe = passe;
        this.futur = futur;
    }

    /// renvoie la taille de la partie "passé" de l'historique
    public int passeSize() {
        return passe.size();
    }

    /// renvoie la taille de la partie "passé" de l'historique
    public int futurSize() {
        return futur.size();
    }

    /// renvoie le contenu du "passé"
    public List<T> dumpPasse() {
        ArrayList<T> liste = new ArrayList<>();
        for (int i =0; i < passeSize(); i++) {
            liste.add(passe.get(i));
        }
        return liste;
    }

    /// renvoie le contenu du "passé"
    public List<T> dumpFutur() {
        ArrayList<T> liste = new ArrayList<>();
        for (int i =0; i < futurSize(); i++) {
            liste.add(futur.get(i));
        }
        return liste;
    }


    @Override
    ///  renvoie la représentation textuelle de l'historique
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
