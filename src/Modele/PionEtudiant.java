package Modele;


import Global.Config.TYPE_ELEMENT_TERRAIN;
import Global.Config.ROLEPION;

import java.awt.*;
import java.nio.file.Path;

import static Global.Config.*;
import static Global.Config.ROLEPION.Etudiant;
import static Global.Config.ROLEPION.Maitre;
import static Global.Config.TYPE_ELEMENT_TERRAIN.*;
import static Global.Paths.*;

public class PionEtudiant implements Pion {
    private final int proprietaire;
    private Path chemainImage;
    private final TYPE_ELEMENT_TERRAIN type;
    private Color couleur;
    private final ROLEPION role;
    private Point position;


    public PionEtudiant(int proprietaire, Point position) {
        this.proprietaire = proprietaire;
        this.type = PION_ETUDIANT;
        this.role = Etudiant;
        this.position = position;

        if (this.proprietaire == 1) {
            this.couleur = COULEUR_CASE_ELEVE_JOUEUR_1;
            this.chemainImage = PATH_PION_ROUGE_ETUDIANT;
        }
        else {
            this.couleur = COULEUR_CASE_ELEVE_JOUEUR_2;
            this.chemainImage = PATH_PION_BLEU_ETUDIANT;
        }
    }






   @Override
    public ROLEPION getStatut() { return role; }


    @Override
    public Color getCouleur() { return couleur; }

    @Override
    public int getProprietaire() { return proprietaire; }

    @Override
    public Path getCheminImage() {  return chemainImage; }

    @Override
    public Point getPosition(){ return position; }

    @Override
    public TYPE_ELEMENT_TERRAIN getType() { return type;}

    @Override
    public void setNewPosition(Point position) {
        this.position = position;
    }

    @Override
    public void setCouleur(Color couleur) { this.couleur = couleur; }

    @Override
    public void setChemainImage(Path chemainImage) { this.chemainImage = chemainImage; }
}
