package Vue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import static Vue.Utils.PngText.createPngPanel;

@DisplayName(" PngText Tests")
public class TestPngText {

    @Test
    public void main() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Texte PNG + Fallback");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 250);

            JPanel content = createPngPanel("sauvegarder", 100);
            frame.getContentPane().add(content);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
