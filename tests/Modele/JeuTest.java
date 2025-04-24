package Modele;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

// ces tests ne prennent pas encore en compte le joueur IA
class JeuTest {
    /// renvoie un nombre pseudo aléatoire entre 1 et 100
    private int rand(SecureRandom r) {
        return Math.min(Math.max(1, r.nextInt()), 100);
    }

    ///  renvoie un string aléatoire de taille 'size'
    private String randString(int size) {
        String src = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQSTUVWXYZ01234567890-_";
        SecureRandom r = new SecureRandom();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < size; i++) {
            res.append(src.charAt(r.nextInt(src.length())));
        }
        return res.toString();
    }
}