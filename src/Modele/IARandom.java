package Modele;
import java.util.*;
import java.awt.*;

public class IARandom {
    private static final Random rand = new Random();


    static Point calculerRandom(Jeu jeu) {
        List<Point> coups = new ArrayList<>();
        int nbLignes = jeu.lignes();
        int nbColonnes = jeu.colonnes();

        for(int i = 0; i < nbLignes; i++){
            for(int j = 0; j < nbColonnes; j++){
                if(!(jeu.estVide(i,j) && !(i==0 && j ==0)){
                    coups.add(new Point(i,j));
                }
            }   
        }

        if(coups.isEmpty()){
            return new Point(0,0);
        }    
    
        return coups.get(rand.nextInt(coups.size()));
    
    
    
    
    }
}
