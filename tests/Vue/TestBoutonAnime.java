package Vue;

import Vue.Utils.Bouton;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

import static Global.Paths.PATH_CARTE;

public class TestBoutonAnime {


    @Test
    public void main() {
        // Run the GUI creation on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        // Create the main window
        JFrame frame = new JFrame("Animation Button Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Simple layout

        // --- Define a placeholder path for the test image ---
        // *** IMPORTANT: Replace this with the actual path to an image file on your system ***
        Path testImagePath = PATH_CARTE.resolve("TIGRE.png");
        // Example for a file in the same directory: Path testImagePath = Paths.get("test_image.png");
        // Example for a file in a specific absolute path: Path testImagePath = Paths.get("C:/Users/YourUser/Pictures/my_image.jpg");
        // ---------------------------------------------------


        // Create buttons demonstrating different features

        // 1. Button with default style (Color background)
        Bouton defaultButton = new Bouton("Default Style");

        // 2. Button with custom colors and border thickness (Color background)
        Bouton customButton1 = new Bouton(
                "Custom Colors",
                5f,                          // Border thickness
                Color.MAGENTA.darker(),      // Base border color
                Color.ORANGE,                // Animation border color
                new Color(220, 255, 220),    // Background color (light green)
                new Color(180, 255, 180, 150), // Rollover background (more opaque light green)
                15                           // Arc radius
        );

        // 3. Button with custom style using setters (Color background)
        Bouton customButton2 = new Bouton("Custom Setters");
        customButton2.setEpaisseurBordure(2f);
        customButton2.setCouleurBordureNormale(Color.BLUE);
        customButton2.setCouleurBordureAnimation(Color.RED);
        customButton2.setCouleurFond(new Color(240, 240, 255)); // Light blue background
        customButton2.setCouleurFondSurvol(new Color(200, 200, 255)); // More opaque light blue
        customButton2.setArrondiCoins(30); // More rounded corners
        customButton2.setForeground(Color.DARK_GRAY); // Set text color

        // 4. Button with semi-transparent background (Color background)
        Bouton customButton3 = new Bouton(
                "Semi-Transparent BG",
                4f,
                new Color(50, 50, 50),      // Dark gray base border
                new Color(0, 255, 255),     // Cyan animation
                new Color(255, 255, 255, 100), // Semi-transparent white background
                new Color(255, 255, 255, 200),// More opaque white rollover
                10
        );
        customButton3.setForeground(Color.BLACK); // Ensure text is visible on light background

        // --- New Test Cases Demonstrating Image Background ---

        // 5. Button with a background image from Path
        Bouton imageButton1 = new Bouton("Image Background");
        imageButton1.setImageFond(testImagePath);
        imageButton1.setForeground(Color.WHITE); // Set text color to be visible on potentially dark image

        // 6. Button with image background and custom border colors
        Bouton imageButton2 = new Bouton(
                "Image + Custom Border",
                6f,                           // Thicker border
                Color.YELLOW,                 // Yellow base border
                Color.GREEN,                  // Green animation color
                new Color(0,0,0,0),           // Background color (ignored if image exists)
                new Color(0,0,0,50),          // Rollover overlay (slightly dark overlay)
                25,                           // Custom arc
                null                          // No image in constructor, will set later
        );
        imageButton2.setImageFond(testImagePath); // Set image using setter
        imageButton2.setForeground(Color.CYAN); // Set text color

        // 7. Button with image background and different animation colors
        Bouton imageButton3 = new Bouton("Image + Anim Color");
        imageButton3.setImageFond(testImagePath);
        imageButton3.setCouleurBordureAnimation(Color.RED); // Change animation color
        imageButton3.setEpaisseurBordure(3f);
        imageButton3.setArrondiCoins(5); // Less rounded
        imageButton3.setForeground(Color.PINK);

        // 8. Button with image background and a more visible rollover overlay
        Bouton imageButton4 = new Bouton("Image + Rollover Overlay");
        imageButton4.setImageFond(testImagePath);
        // Set a semi-transparent black rollover color for a darkening effect
        imageButton4.setCouleurFondSurvol(new Color(0, 0, 0, 100));
        imageButton4.setForeground(Color.ORANGE);


        // 9. Button with image background and thinner border
        Bouton imageButton5 = new Bouton("Image + Thin Border");
        imageButton5.setImageFond(testImagePath);
        imageButton5.setEpaisseurBordure(1.5f); // Thinner border
        imageButton5.setCouleurBordureNormale(Color.GRAY);
        imageButton5.setCouleurBordureAnimation(Color.WHITE);
        imageButton5.setForeground(Color.BLACK); // Set text color
        imageButton2.setPreferredSize(new Dimension(200,100));

        // Add action listeners (optional, but good for testing functionality)
        defaultButton.addActionListener(e -> System.out.println("Default button clicked!"));
        imageButton1.addActionListener(e -> System.out.println("Image button 1 clicked!"));


        // Add the buttons to the frame
        frame.add(defaultButton);
        frame.add(customButton1);
        frame.add(customButton2);
        frame.add(customButton3);
        frame.add(imageButton1); // Add new buttons
        frame.add(imageButton2);
        frame.add(imageButton3);
        frame.add(imageButton4);
        frame.add(imageButton5);


        // Pack the frame and make it visible
        frame.pack(); // Adjusts the window size to fit the components
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }

}



class Main {
    @Test
    public void main() {
        // Run the GUI creation on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        // Create the main window
        JFrame frame = new JFrame("Bouton Fixed Layout Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300); // Set a initial size

        // Create a JPanel to hold the button
        JPanel panel = new JPanel();

        // Use BorderLayout for the panel
        // BorderLayout.CENTER makes the component fill the available space
        panel.setLayout(new BorderLayout());
        // Add some padding around the button using an EmptyBorder if desired
        // panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create your custom button instance
        // Example with text
        Vue.testsUI.Bouton myButton = new Vue.testsUI.Bouton(PATH_CARTE.resolve("TIGRE.png"));

        // Example with image (assuming PATH_CARTE is correctly defined and image exists)
        // Path testImagePath = PATH_CARTE.resolve("TIGRE.png");
        // Bouton myButton = new Bouton(testImagePath);


        // Add the button to the CENTER of the panel's BorderLayout
        panel.add(myButton, BorderLayout.CENTER);

        // Add the panel to the frame
        frame.add(panel);

        // Pack the frame to its preferred size (based on components' preferred sizes,
        // but BorderLayout CENTER will stretch the button to fill the panel)
        // frame.pack(); // You might use pack() if you want the frame to size to its contents initially

        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);

        // When you resize the frame, the panel resizes, and BorderLayout.CENTER
        // makes the button resize to fill the panel, keeping it centered
        // and occupying the available space.
    }
}