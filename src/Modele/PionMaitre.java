package Modele;

import Global.Config.TYPE_ELEMENT_TERRAIN;
import Global.Config.ROLEPION;

import java.awt.*;
import java.nio.file.Path;

import static Global.Config.COULEUR_CASE_MAITRE_JOUEUR_1;
import static Global.Config.COULEUR_CASE_MAITRE_JOUEUR_2;
import static Global.Config.ROLEPION.*;
import static Global.Config.TYPE_ELEMENT_TERRAIN.PION_MAITRE;
import static Global.Paths.PATH_PION_BLEU_MAITRE;
import static Global.Paths.PATH_PION_ROUGE_MAITRE;


public class PionMaitre implements Pion {
    private final int idJoueur;
    private Path chemainImage;
    private final TYPE_ELEMENT_TERRAIN type;
    private Color couleur;
    private final ROLEPION role;
    private Point position;



    public PionMaitre(int idJoueur, Point position) {
        this.idJoueur = idJoueur;
        this.type = PION_MAITRE;
        this.role = Maitre;
        this.position = position;

        if (this.idJoueur == 1) {
            this.couleur = COULEUR_CASE_MAITRE_JOUEUR_1;
            this.chemainImage = PATH_PION_ROUGE_MAITRE;
        }
        else {
            this.couleur = COULEUR_CASE_MAITRE_JOUEUR_2;
            this.chemainImage = PATH_PION_BLEU_MAITRE;
        }
    }






    @Override
    public ROLEPION getStatut() { return role; }

    @Override
    public Color getCouleur() { return couleur; }

    @Override
    public int getProprietaire() { return idJoueur; }

    @Override
    public Path getCheminImage() {  return chemainImage; }

    @Override
    public Point getPosition(){ return position; }

    @Override
    public TYPE_ELEMENT_TERRAIN getType() { return type;}

    @Override
    public void setNewPosition(Point position) {  this.position = position;  }

    @Override
    public void setCouleur(Color couleur) { this.couleur = couleur; }

    @Override
    public void setChemainImage(Path chemainImage) { this.chemainImage = chemainImage; }
}
