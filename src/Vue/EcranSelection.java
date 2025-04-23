package Vue;

import Vue.Styles.BordArondisJPanel;

import javax.swing.*;
import java.awt.*;

import static java.lang.System.exit;

public class EcranSelection extends JPanel {
    private final JButton btnEcranDramarrage, btnReprendrePartie, annuler;
    private InterfaceGraphique interfaceGraphique;
    EcranDemarrage accueil;

    public EcranSelection(InterfaceGraphique interfaceGraphique) {
        this.interfaceGraphique = interfaceGraphique;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // === Logo ===
        ImageIcon icon = new ImageIcon("res/vue/images/UFR_IM2AG_2020.png");
        Image image = icon.getImage().getScaledInstance(300, 160, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(image));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(30));
        add(logo);

        // === Texte descriptif ===
        JTextPane description = new JTextPane();
        description.setContentType("text/html");
        description.setText(
                "<html><div style='text-align: center; font-family: SansSerif; font-size: 25pt; color: #333;'>"
                        + "<p><b>Bienvenue dans le jeu de la gaufre empoisonnée !</b></p>"
                        + "<p>Dans ce jeu de stratégie, chaque joueur à tour de rôle mange une partie de la gaufre.<br>"
                        + "Mais attention : celui qui mange la case empoisonnée perd !</p>"
                        + "<p><i>Ce jeu mettra à l’épreuve votre logique et votre sens de l’anticipation.</i></p>"
                        + "<p style='margin-top:20px; font-size:20pt; color:#555;'>"
                        + "UGA - IM2AG - PRÉPROJET PROG6 2024-25<br>"
                        + "Ifrel Rinel MAKOUNDIKA<br>"
                        + "Arthur D'HERIN<br>"
                        + "Killian Mbappé<br>"
                        + "...</p>"
                        + "</div></html>"
        );
        description.setEditable(false);
        description.setFocusable(false);
        description.setOpaque(false);
        description.setBorder(null);

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBorder(new BordArondisJPanel(new Color(128, 0, 128), 2, 10));
        descriptionPanel.setOpaque(false);
        descriptionPanel.setMaximumSize(new Dimension(750, 300));
        descriptionPanel.add(description, BorderLayout.CENTER);
        descriptionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(20));
        add(descriptionPanel);

        // === Boutons de jeu ===
        btnEcranDramarrage = new JButton("Commencer");
        btnReprendrePartie = new JButton("Reprendre une partie");
        annuler = new JButton("Quitter");

        JButton[] boutons = {btnEcranDramarrage, btnReprendrePartie, annuler};
        for (JButton btn : boutons) {
            btn.setFont(new Font("SansSerif", Font.PLAIN, 22));
            btn.setMaximumSize(new Dimension(300, 45));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(Box.createVerticalStrut(15));
            add(btn);
        }

        // Actions Buttons
        annuler.addActionListener(e -> exit(0));

        btnEcranDramarrage.addActionListener(e -> {
            accueil = new EcranDemarrage(interfaceGraphique);
            interfaceGraphique.setEcran(accueil);});

        btnReprendrePartie.addActionListener(e ->{
            System.err.println("Ecran de selection se sauvegarde à faire");
        });

        add(Box.createVerticalGlue());
    }
}
