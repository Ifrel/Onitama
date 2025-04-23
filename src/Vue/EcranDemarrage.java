package Vue;

import Modele.Jeu;
import Vue.VuePrincipale.VuePrincipale;

import javax.swing.*;
import java.awt.*;

import static java.lang.System.exit;

public class EcranDemarrage extends JPanel {
    private final JTextField champJoueur1, champJoueur2;
    private final JSpinner spinnerCols, spinnerRows;
    private final JButton btnDemarrer, btnQuitter, btnRetour;
    //private final JCheckBox checkIA;
    private final JComboBox<String> cb;
    private InterfaceGraphique interfaceGraphique;
    private VuePrincipale vue;
    Jeu jeu;

    public EcranDemarrage(InterfaceGraphique interfaceGraphique) {
        this.interfaceGraphique = interfaceGraphique;
        this.jeu = interfaceGraphique.getJeu();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        setOpaque(false); // utile si fond personnalisé

        // === Titre ===
        JLabel titre = new JLabel("Bienvenue dans La Gaufre Empoisonnée !", SwingConstants.CENTER);
        titre.setFont(new Font("SansSerif", Font.BOLD, 26));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titre);
        add(Box.createVerticalStrut(30));

        // === Formulaire ===
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 15, 15));
        formPanel.setMaximumSize(new Dimension(1000, 500));
        formPanel.setOpaque(false);

        JLabel jl = new JLabel("Jouer contre l'IA :");
        formPanel.add(jl);
        /*checkIA = new JCheckBox("Activer l’IA");
        checkIA.setFont(new Font("SansSerif", Font.PLAIN, 22));
        checkIA.setOpaque(false);
        formPanel.add(checkIA);
        */
        String[] choices = {"Desactiver", "Facile","Intermédiaire", "Difficile"};
        cb = new JComboBox<String>(choices);
        cb.setVisible(true);
        formPanel.add(cb);

        JLabel jl1 = new JLabel("Nom du Joueur 1 :");
        formPanel.add(jl1);
        champJoueur1 = new JTextField("Joueur 1");
        formPanel.add(champJoueur1);

        JLabel jl2 = new JLabel("Nom du Joueur 2 :");
        formPanel.add(jl2);
        champJoueur2 = new JTextField("Joueur 2");
        formPanel.add(champJoueur2);


        JLabel jl3 =new JLabel("Nombre de colonnes :");
        formPanel.add(jl3);
        spinnerCols = new JSpinner(new SpinnerNumberModel(15, 5, 30, 1));
        spinnerCols.setFont(new Font("SansSerif", Font.PLAIN, 22));
        formPanel.add(spinnerCols);

        JLabel jl4 = new JLabel("Nombre de lignes :");
        formPanel.add(jl4);
        spinnerRows = new JSpinner(new SpinnerNumberModel(15, 5, 30, 1));
        spinnerRows.setFont(new Font("SansSerif", Font.PLAIN, 22));
        formPanel.add(spinnerRows);

        // === Style ===
        JLabel[] boutons = {jl, jl1, jl2, jl3, jl4};
        for (JLabel btn : boutons) {
            btn.setFont(new Font("SansSerif", Font.PLAIN, 22));
            btn.setMaximumSize(new Dimension(300, 45));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        add(formPanel);
        add(Box.createVerticalStrut(30));

        // === Boutons ===
        JPanel boutonsPanel = new JPanel();
        boutonsPanel.setLayout(new BoxLayout(boutonsPanel, BoxLayout.X_AXIS));
        boutonsPanel.setOpaque(false);

        btnDemarrer = new JButton("▶Demarrer la partie");
        btnDemarrer.setFont(new Font("SansSerif", Font.BOLD, 20));
        btnDemarrer.setBackground(Color.GREEN);

        btnQuitter = new JButton("▶Quitter");
        btnQuitter.setFont(new Font("SansSerif", Font.PLAIN, 20));
//        btnQuitter.setBackground(Color.RED.brighter());

        btnRetour = new JButton("▶Retour");
        btnRetour.setFont(new Font("SansSerif", Font.PLAIN, 20));

        boutonsPanel.add(Box.createHorizontalGlue());
        boutonsPanel.add(btnRetour);
        boutonsPanel.add(Box.createHorizontalStrut(20));
        boutonsPanel.add(btnQuitter);
        boutonsPanel.add(Box.createHorizontalStrut(20));
        boutonsPanel.add(btnDemarrer);
        boutonsPanel.add(Box.createHorizontalGlue());

        add(boutonsPanel);
        add(Box.createVerticalGlue());

        // === Action : Activer/désactiver champ joueur 2 + Actions Buttons===
        cb.addActionListener(e -> champJoueur2.setEnabled(!isIAActivee()));
        btnQuitter.addActionListener(e -> exit(0));
        btnRetour.addActionListener(e -> interfaceGraphique.setEcran(interfaceGraphique.getEcranSelection()));
        btnDemarrer.addActionListener(e -> {
            // - Set confif files
            jeu.setNameJoueurA(champJoueur1.getText());
            jeu.setNameJoueurB(champJoueur2.getText());

            vue = new VuePrincipale(jeu, interfaceGraphique.getCollecteurEvent());
            interfaceGraphique.setEcran(vue);});

        // mise à jour du model
        if (isIAActivee()) {
            jeu.toggleIA();
            jeu.setNameJoueurA(getNomJoueur1());
            jeu.recommencer(getLignes(), getColonnes());
        }else{
            jeu.setNameJoueurA(getNomJoueur1());
            jeu.setNameJoueurB(getNomJoueur2());
            jeu.recommencer(getLignes(), getColonnes());
        }


    }

    // === Accesseurs ===
    private String getNomJoueur1() { return champJoueur1.getText().trim(); }
    private String getNomJoueur2() { return champJoueur2.getText().trim(); }
    private int getColonnes() { return (int) spinnerCols.getValue(); }
    private int getLignes() { return (int) spinnerRows.getValue(); }
    private boolean isIAActivee() { return (String) cb.getSelectedItem() != "Desactiver"; }
}
