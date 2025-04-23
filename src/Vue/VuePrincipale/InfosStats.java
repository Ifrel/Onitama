package Vue.VuePrincipale;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

public class InfosStats extends JPanel implements Observateur {
    private int nbrGaufreMange , nbrGaufreDispo ;
    private int nbrDeCase;
    private int nbrCaseMangerJoueurA;
    private int nbrCaseMangerJoueurB;
    private JPanel barreA, barreB;
    private Jeu jeu;

    public InfosStats(Jeu jeu){
        this.jeu = jeu;
        initParametres();

        // Initialisation du conteneur
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setAlignmentX(CENTER_ALIGNMENT);
        this.jeu.ajouteObservateur(this);

        ajouDesComposants();
    }


    @Override
    public void miseAJour() {
        initParametres();
        this.removeAll();
        ajouDesComposants();
    }

    /**************************************
     * *** ** * MÉTHODES UTILES * ** **** *
     **************************************/

    private void initParametres(){
        this.nbrDeCase = jeu.colonnes() * jeu.lignes() ;
        this.nbrCaseMangerJoueurA = jeu.nbCasesMangeesJoueurA();
        this.nbrCaseMangerJoueurB = jeu.nbCasesMangeesJoueurB();
        this.nbrGaufreMange = nbrCaseMangerJoueurA + nbrCaseMangerJoueurB;
        this.nbrGaufreDispo = nbrDeCase - nbrGaufreMange;

    }

    private void ajouDesComposants(){
        this.add(creerTableauStats());
        this.add(barresLateralesAetB());
    }

    /**
     *  Barres verticales A et B (simplifiées ici) */
    private JPanel barresLateralesAetB() {
        JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.X_AXIS));
//        b.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        b.setAlignmentX(CENTER_ALIGNMENT);

        // Barre A
        barreA = creerBarre("A", nbrCaseMangerJoueurA, Color.GREEN.darker());
        b.add(barreA);

        b.add(Box.createHorizontalStrut(10));

        // Barre B
        barreB = creerBarre("B", nbrCaseMangerJoueurB, Color.RED.darker());
        b.add(barreB);

        b.add(Box.createHorizontalStrut(10));

        return b;
    }

    private JPanel creerBarre(String lettre, int valeur, Color couleur) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(CENTER_ALIGNMENT);

        // Lettre au-dessus
        JLabel labelLettre = new JLabel(lettre, SwingConstants.CENTER);
        labelLettre.setFont(new Font("SansSerif", Font.PLAIN, 50));
        labelLettre.setAlignmentX(CENTER_ALIGNMENT);

        // Score en-dessous
        JLabel labelScore = new JLabel(""+valeur, SwingConstants.CENTER);
        labelScore.setFont(new Font("SansSerif", Font.PLAIN, 30));
        labelScore.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Barre verticale
        JProgressBar barre = new JProgressBar(SwingConstants.VERTICAL, 0, nbrDeCase);
        barre.setValue(valeur);
        barre.setForeground(couleur);
        barre.setStringPainted(true); // pour afficher le % dessus
        // barre.setPreferredSize(new Dimension(10, 30)); // taille personnalisée
        // barre.setBorder(new BordArondisJPanel(Color.GRAY, 2, 25)); // couleur, épaisseur, rayon

        barre.setFont(new Font("SansSerif", Font.PLAIN, 30));
        barre.setAlignmentX(CENTER_ALIGNMENT);

        // Ajout dans le panel
        panel.add(labelLettre);
        panel.add(Box.createVerticalStrut(5));
        panel.add(labelScore);
        panel.add(Box.createVerticalStrut(5));
        panel.add(barre);

        return panel;
    }

    private JPanel creerTableauStats() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false); // si tu veux fond transparent + bordure arrondie

        // Titre "Mangés"
        JLabel lblManges = new JLabel("Mangés :");
        lblManges.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblManges.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Valeur mangée
        JLabel lblValeurManges = new JLabel(String.valueOf(nbrGaufreMange));
        lblValeurManges.setFont(new Font("SansSerif", Font.BOLD, 60));
        lblValeurManges.setForeground(Color.ORANGE.darker());
        lblValeurManges.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Titre "Dispos"
        JLabel lblDispos = new JLabel("Dispos :");
        lblDispos.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblDispos.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Valeur dispo
        JLabel lblValeurDispos = new JLabel(String.valueOf(nbrGaufreDispo));
        lblValeurDispos.setFont(new Font("SansSerif", Font.BOLD, 60));
        lblValeurDispos.setForeground(new Color(139, 69, 19)); // couleur chocolat
        lblValeurDispos.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ajout au panneau avec espacement
        panel.add(lblManges);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblValeurManges);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblDispos);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblValeurDispos);

        return panel;
    }


}
