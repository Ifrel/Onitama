package Vue.LABO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ConfigurationWindow extends JFrame {

    public ConfigurationWindow() {
        setTitle("Configuration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600)); // Adjust size as needed
        setLayout(new BorderLayout());

        // Main panel with background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage backgroundImage = ImageIO.read(new File("background.jpg")); // Replace with your image path
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    g.setColor(new Color(245, 222, 179)); // Beige-like background if image fails
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Left decorative panel
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage backgroundImage = ImageIO.read(new File("left_panel_background.png")); // Replace with your image path
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    g.setColor(new Color(205, 92, 92)); // Maroon-like background if image fails
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        leftPanel.setPreferredSize(new Dimension(220, 0)); // Adjust width as needed
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add some padding

        try {
            BufferedImage logoImage = ImageIO.read(new File("onitama_logo.png")); // Replace with your logo path
            JLabel logoLabel = new JLabel(new ImageIcon(logoImage.getScaledInstance(180, 60, Image.SCALE_SMOOTH)));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftPanel.add(Box.createVerticalGlue());
            leftPanel.add(logoLabel);
            leftPanel.add(Box.createVerticalGlue());

            BufferedImage artworkImage = ImageIO.read(new File("onitama_artwork.png")); // Replace with your artwork path
            JLabel artworkLabel = new JLabel(new ImageIcon(artworkImage.getScaledInstance(180, 180, Image.SCALE_SMOOTH)));
            artworkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftPanel.add(artworkLabel);
            leftPanel.add(Box.createVerticalGlue());

        } catch (IOException e) {
            JLabel logoLabel = new JLabel("Onitama");
            logoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            logoLabel.setForeground(Color.WHITE);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftPanel.add(Box.createVerticalGlue());
            leftPanel.add(logoLabel);
            leftPanel.add(Box.createVerticalGlue());
        }

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Right configuration panel
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setOpaque(false); // Make background transparent
        configPanel.setBorder(new EmptyBorder(50, 50, 50, 50)); // Add padding

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Configuration");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK);
        configPanel.add(titleLabel, gbc);

        gbc.gridy++;
        configPanel.add(createConfigRow("Mode auto (IA vs IA)", createOnOffToggle()), gbc);

        gbc.gridy++;
        configPanel.add(createConfigRow("Reprendre une partie", new JTextField("Selectionner ici", 15)), gbc);

        gbc.gridy++;
        configPanel.add(createConfigRow("Jouer avec l'IA", new JTextField("Non", 15)), gbc);

        gbc.gridy++;
        configPanel.add(createConfigRow("Joueur 1", new JTextField(15)), gbc);

        gbc.gridy++;
        configPanel.add(createConfigRow("Joueur 2", new JTextField(15)), gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        JButton nextButton = new JButton(new ImageIcon(createArrowIcon()));
        nextButton.setBackground(new Color(0, 150, 0)); // Green color
        nextButton.setForeground(Color.WHITE);
        nextButton.setFocusPainted(false);
        nextButton.setBorderPainted(false);
        nextButton.setPreferredSize(new Dimension(60, 60));
        configPanel.add(nextButton, gbc);

        mainPanel.add(configPanel, BorderLayout.CENTER);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createConfigRow(String labelText, JComponent component) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rowPanel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        label.setForeground(Color.BLACK);
        label.setPreferredSize(new Dimension(200, 30)); // Adjust width as needed
        rowPanel.add(label);
        rowPanel.add(component);

        return rowPanel;
    }

    private JToggleButton createOnOffToggle() {
        JToggleButton toggleButton = new JToggleButton("OFF");
        toggleButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        toggleButton.setForeground(Color.WHITE);
        toggleButton.setBackground(new Color(200, 0, 0)); // Red for OFF
        toggleButton.setPreferredSize(new Dimension(80, 30));
        toggleButton.setFocusPainted(false);

        toggleButton.addActionListener(e -> {
            if (toggleButton.isSelected()) {
                toggleButton.setText("ON");
                toggleButton.setBackground(new Color(0, 150, 0)); // Green for ON
            } else {
                toggleButton.setText("OFF");
                toggleButton.setBackground(new Color(200, 0, 0)); // Red for OFF
            }
        });
        return toggleButton;
    }

    private Image createArrowIcon() {
        BufferedImage image = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(5));
        g.drawLine(15, 30, 45, 30); // Shaft
        g.drawLine(35, 20, 45, 30); // Top arrow head
        g.drawLine(35, 40, 45, 30); // Bottom arrow head
        g.dispose();
        return image;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConfigurationWindow::new);
    }
}