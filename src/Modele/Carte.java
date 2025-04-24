package Modele;

import Global.Config;
import Global.Config.TYPECARTE;
import static Global.Config.MOUVEMENTCARTE;
import javax.swing.event.CaretEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Carte {
    private String _name;
    private TYPECARTE _type;
    private boolean _proprietaire;

    public Carte(TYPECARTE type)
    {
        _type = type;
        _name = type.name();
    }

    public String getName() { return _name; }
    public TYPECARTE getType() { return _type; }
    public List<Coup> getMoves(Point origin)
    {
        List<Coup> allMoves = new ArrayList<Coup>();
        for (Point p : MOUVEMENTCARTE.get(_type)) {
            Point nouveauPoint = new Point(origin.x + p.x, origin.y + p.y);
            Coup nouveauCoup = new Coup(origin, nouveauPoint);
            allMoves.add(nouveauCoup);
        }
        return allMoves;
    }
    public void DefinirProprietaire(boolean proprietaire){ _proprietaire = proprietaire; }
}