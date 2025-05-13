package Vue.Adaptateurs;

import Modele.Carte;
import Vue.CollecteurEvenements;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Logger;

public class AdaptateurCarte implements ActionListener {
    private final CollecteurEvenements collecteurEvent;
    private final Carte carte;
    private final int  idCarte;
    private final List<Carte> carteList;

    private final Logger logger = Logger.getLogger(AdaptateurCarte.class.getName());

    public AdaptateurCarte(int idCarte, Carte carte, List<Carte> carteList, CollecteurEvenements collecteurEvent){
        this.collecteurEvent = collecteurEvent;
        this.carte = carte;
        this.carteList = carteList;
        this.idCarte = idCarte;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            logger.info("CarteUI: " + carte.getNom() + " pressé pour le Joueur "+idCarte);



            collecteurEvent.carteSelectionne(carte);
        } catch (Exception ex) {
            logger.severe("" + ex);
            throw new RuntimeException(ex);
        }
    }
}