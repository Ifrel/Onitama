package Vue.Animations;

import javax.swing.*;
import java.awt.*;

public class AnimateurDeCartes {
    private static final int DUREE_ANIMATION = 500; // en millisecondes
    private static final int DELAI_FRAME = 16; // ~60 FPS
    private static final double INCREMENT_PROGRESSION = 0.05;
    
    private Timer timer;
    private Point depart;
    private Point destination;
    private JComponent carte;
    private double progression;
    
    public void animerDeplacement(JComponent carte, Point destination) {
        if (carte == null || destination == null) {
            throw new IllegalArgumentException("La carte et la destination ne peuvent pas être null");
        }
        
        this.carte = carte;
        this.depart = carte.getLocation();
        this.destination = destination;
        this.progression = 0.0;
        
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        
        timer = new Timer(DELAI_FRAME, e -> {
            progression += INCREMENT_PROGRESSION;
            if (progression >= 1.0) {
                terminerAnimation();
            } else {
                mettreAJourPosition();
            }
        });
        timer.start();
    }
    
    private void mettreAJourPosition() {
        double easing = Math.sin(progression * Math.PI / 2);
        int x = (int) (depart.x + (destination.x - depart.x) * easing);
        int y = (int) (depart.y + (destination.y - depart.y) * easing);
        carte.setLocation(x, y);
    }
    
    private void terminerAnimation() {
        if (timer != null) {
            timer.stop();
        }
        if (carte != null && destination != null) {
            carte.setLocation(destination);
        }
    }
    
    public void dispose() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        carte = null;
        depart = null;
        destination = null;
    }
}