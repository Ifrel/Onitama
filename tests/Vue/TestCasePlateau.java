package Vue;

import Vue.Utils.Boutons.BoutonCarte;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@DisplayName("BoutonCarte Class Tests")
class TestCasePlateau {

    // Mocking Timer as it's an internal dependency with state
    private Timer mockTimer;

    @BeforeEach
    void setUp() {
        // Mock the Timer before each test
        mockTimer = mock(Timer.class);
        // Using Mockito's `whenNew` to intercept Timer creation
        // This requires the 'mockito-inline' dependency for mocking final classes/methods
        try (var mockedStatic = Mockito.mockConstruction(Timer.class, (mock, context) -> {
            // Store the created mock and capture its ActionListener
            when(mock.getDelay()).thenReturn((Integer) context.arguments().get(0));
            when(mock.getActionListeners()).thenReturn(new ActionListener[]{(ActionListener) context.arguments().get(1)});
        })) {
            // We need to instantiate a dummy BoutonCarte here to trigger the Timer constructor for mocking
            // This is a bit of a setup hack for mocking `new` calls.
            // In a real application, you might use dependency injection for the Timer.
            new BoutonCarte();
            mockTimer = mockedStatic.constructed().get(0); // Get the first (and only) constructed Timer
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to mock Timer: " + e.getMessage());
        }
    }

    // Helper method to create a dummy BufferedImage
    private BufferedImage createDummyImage() {
        return new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Default constructor initializes with correct default values")
        void testDefaultConstructor() {
            BoutonCarte button = new BoutonCarte();
            assertEquals("", button.getText());
            assertEquals(3f, getPrivateField(button, "epaisseurBordure"), 0.001);
            assertEquals(new Color(157, 154, 154), getPrivateField(button, "couleurBordureNormale"));
            assertEquals(new Color(205, 27, 181), getPrivateField(button, "couleurBordureAnimation"));
            assertEquals(new Color(255, 255, 255, 0), getPrivateField(button, "couleurFond"));
            assertEquals(new Color(200, 220, 255, 100), getPrivateField(button, "couleurFondSurvol"));
            assertEquals(20, (Integer) getPrivateField(button, "arrondiCoins"));
            assertNull(getPrivateField(button, "imageFond"));
        }

        @Test
        @DisplayName("Constructor with text initializes correctly")
        void testTextConstructor() {
            String testText = "Test Button";
            BoutonCarte button = new BoutonCarte(testText);
            assertEquals(testText, button.getText());
            assertEquals(3f, getPrivateField(button, "epaisseurBordure"), 0.001);
        }

        @Test
        @DisplayName("Constructor with Path initializes image and default values")
        void testPathConstructor() {
            // Create a dummy file for Path. This won't actually be read by ImageIO unless it exists.
            // For a robust test, you'd mock ImageIO.read.
            // Here, we'll assume a dummy path for constructor call, but the imageFond will be null
            // unless we mock ImageIO.read.
            Path dummyPath = Paths.get("nonexistent.png");

            // Mock ImageIO.read to return a dummy image
            try (var mockedStatic = Mockito.mockStatic(javax.imageio.ImageIO.class)) {
                BufferedImage dummyImage = createDummyImage();
                mockedStatic.when(() -> javax.imageio.ImageIO.read(any(java.io.File.class))).thenReturn(dummyImage);

                BoutonCarte button = new BoutonCarte(dummyPath);
                assertNotNull(getPrivateField(button, "imageFond"));
                assertEquals(dummyImage, getPrivateField(button, "imageFond"));
                assertEquals(2.5f, getPrivateField(button, "epaisseurBordure"), 0.001); // Specific to this constructor
            }
        }

        @Test
        @DisplayName("Constructor with BufferedImage initializes image and default values")
        void testBufferedImageConstructor() {
            BufferedImage testImage = createDummyImage();
            BoutonCarte button = new BoutonCarte(testImage);
            assertNotNull(getPrivateField(button, "imageFond"));
            assertEquals(testImage, getPrivateField(button, "imageFond"));
            assertEquals(2.5f, getPrivateField(button, "epaisseurBordure"), 0.001); // Specific to this constructor
        }

        @Test
        @DisplayName("Full constructor without image initializes correctly")
        void testFullConstructorNoImage() {
            BoutonCarte button = new BoutonCarte("Custom", 5f, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, 10);
            assertEquals("Custom", button.getText());
            assertEquals(5f, getPrivateField(button, "epaisseurBordure"), 0.001);
            assertEquals(Color.RED, getPrivateField(button, "couleurBordureNormale"));
            assertEquals(Color.BLUE, getPrivateField(button, "couleurBordureAnimation"));
            assertEquals(Color.GREEN, getPrivateField(button, "couleurFond"));
            assertEquals(Color.YELLOW, getPrivateField(button, "couleurFondSurvol"));
            assertEquals(10, (Integer) getPrivateField(button, "arrondiCoins"));
            assertNull(getPrivateField(button, "imageFond"));
        }

        @Test
        @DisplayName("Full constructor with image initializes correctly")
        void testFullConstructorWithImage() {
            BufferedImage testImage = createDummyImage();
            BoutonCarte button = new BoutonCarte("ImageBtn", 1f, Color.BLACK, Color.WHITE, Color.CYAN, Color.MAGENTA, 5, testImage);
            assertEquals("ImageBtn", button.getText());
            assertEquals(1f, getPrivateField(button, "epaisseurBordure"), 0.001);
            assertEquals(testImage, getPrivateField(button, "imageFond"));
        }
    }

    @Nested
    @DisplayName("Setter Tests")
    class SetterTests {
        private BoutonCarte button;

        @BeforeEach
        void setup() {
            button = spy(new BoutonCarte()); // Use spy to check repaint/revalidate calls
            // Make sure the mock Timer from the outer BeforeEach is associated
            setPrivateField(button, "minuteur", mockTimer);
        }

        @Test
        @DisplayName("setEpaisseurBordure updates value and triggers repaint/revalidate")
        void testSetEpaisseurBordure() {
            button.setEpaisseurBordure(7f);
            assertEquals(7f, getPrivateField(button, "epaisseurBordure"), 0.001);
            verify(button, atLeastOnce()).revalidate(); // Revalidate should be called
            verify(button, atLeastOnce()).repaint(); // Repaint should be called
        }

        @Test
        @DisplayName("setCouleurBordureNormale updates value and triggers repaint")
        void testSetCouleurBordureNormale() {
            button.setCouleurBordureNormale(Color.ORANGE);
            assertEquals(Color.ORANGE, getPrivateField(button, "couleurBordureNormale"));
            verify(button, atLeastOnce()).repaint();
        }

        @Test
        @DisplayName("setCouleurBordureAnimation updates value and triggers repaint")
        void testSetCouleurBordureAnimation() {
            button.setCouleurBordureAnimation(Color.PINK);
            assertEquals(Color.PINK, getPrivateField(button, "couleurBordureAnimation"));
            verify(button, atLeastOnce()).repaint();
        }

        @Test
        @DisplayName("setCouleurFond updates value, opaque state, and triggers repaint")
        void testSetCouleurFond() {
            button.setCouleurFond(new Color(10, 20, 30, 100)); // Transparent
            assertEquals(new Color(10, 20, 30, 100), getPrivateField(button, "couleurFond"));
            assertFalse(button.isOpaque()); // Should be false if transparent color
            verify(button, atLeastOnce()).repaint();

            button.setCouleurFond(Color.RED); // Opaque
            assertTrue(button.isOpaque()); // Should be true if opaque color and no image
            verify(button, atLeastOnce()).repaint();
        }

        @Test
        @DisplayName("setCouleurFondSurvol updates value and triggers repaint")
        void testSetCouleurFondSurvol() {
            button.setCouleurFondSurvol(Color.CYAN);
            assertEquals(Color.CYAN, getPrivateField(button, "couleurFondSurvol"));
            verify(button, atLeastOnce()).repaint();
        }

        @Test
        @DisplayName("setArrondiCoins updates value and triggers repaint/revalidate")
        void testSetArrondiCoins() {
            button.setArrondiCoins(15);
            assertEquals(15, (Integer) getPrivateField(button, "arrondiCoins"));
            verify(button, atLeastOnce()).revalidate();
            verify(button, atLeastOnce()).repaint();
        }

        @Test
        @DisplayName("setImageFond(Path) updates image and triggers repaint/revalidate")
        void testSetImageFondPath() {
            Path dummyPath = Paths.get("someimage.png");
            BufferedImage testImage = createDummyImage();

            try (var mockedStatic = Mockito.mockStatic(javax.imageio.ImageIO.class)) {
                mockedStatic.when(() -> javax.imageio.ImageIO.read(any(java.io.File.class))).thenReturn(testImage);
                button.setImageFond(dummyPath);
                assertEquals(testImage, getPrivateField(button, "imageFond"));
                verify(button, atLeastOnce()).revalidate();
                verify(button, atLeastOnce()).repaint();
            }
        }

        @Test
        @DisplayName("setImageFond(BufferedImage) updates image and triggers repaint/revalidate")
        void testSetImageFondBufferedImage() {
            BufferedImage testImage = createDummyImage();
            button.setImageFond(testImage);
            assertEquals(testImage, getPrivateField(button, "imageFond"));
            verify(button, atLeastOnce()).revalidate();
            verify(button, atLeastOnce()).repaint();
        }

        @Test
        @DisplayName("removeImageFond sets image to null and triggers repaint")
        void testRemoveImageFond() {
            button.setImageFond(createDummyImage()); // Set an image first
            button.removeImageFond();
            assertNull(getPrivateField(button, "imageFond"));
            verify(button, atLeastOnce()).repaint();
        }
    }

    @Nested
    @DisplayName("Animation Control Tests")
    class AnimationControlTests {
        private BoutonCarte button;

        @BeforeEach
        void setup() {
            button = spy(new BoutonCarte());
            setPrivateField(button, "minuteur", mockTimer); // Inject the mock timer
        }

        @Test
        @DisplayName("demarrerAnimation starts the timer and sets animationActive to true")
        void testDemarrerAnimation() {
            button.demarrerAnimation();
            assertTrue(getPrivateField(button, "animationActive"));
            verify(mockTimer).start();
        }

        @Test
        @DisplayName("demarrerAnimation does nothing if button is disabled")
        void testDemarrerAnimationWhenDisabled() {
            button.setEnabled(false);
            button.demarrerAnimation();
            assertFalse(getPrivateField(button, "animationActive")); // Should remain false
            verify(mockTimer, never()).start();
        }

        @Test
        @DisplayName("arreterAnimation stops the timer and sets animationActive to false")
        void testArreterAnimation() {
            button.demarrerAnimation(); // Start first
            button.arreterAnimation();
            assertFalse(getPrivateField(button, "animationActive"));
            verify(mockTimer).stop();
        }

        @Test
        @DisplayName("ActionListener toggles animation on click when enabled")
        void testActionListenerTogglesAnimation() {
            // Need to retrieve the actual ActionListener from the button
            // This is a bit tricky as it's added in the constructor
            // A common pattern is to inject an ActionListener or make it accessible for testing.
            // For now, we'll simulate a click.

            // Ensure button is enabled for action
            button.setEnabled(true);
            setPrivateField(button, "animationActive", false); // Start with animation off

            button.doClick(); // Simulate a click
            assertTrue(getPrivateField(button, "animationActive"));
            verify(mockTimer).start();

            button.doClick(); // Simulate another click
            assertFalse(getPrivateField(button, "animationActive"));
            verify(mockTimer).stop();
        }

        @Test
        @DisplayName("ActionListener does not toggle animation when disabled")
        void testActionListenerDoesNotToggleWhenDisabled() {
            button.setEnabled(false);
            setPrivateField(button, "animationActive", false); // Start with animation off

            button.doClick(); // Simulate a click
            assertFalse(getPrivateField(button, "animationActive")); // Should remain false
            verify(mockTimer, never()).start();
            verify(mockTimer, never()).stop();
        }

        @Test
        @DisplayName("Timer's ActionListener updates phase and repaints")
        void testTimerActionListener() {
            // Get the ActionListener instance that was added to the mockTimer
            ArgumentCaptor<ActionListener> listenerCaptor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockTimer).addActionListener(listenerCaptor.capture());
            ActionListener timerActionListener = listenerCaptor.getValue();

            // Spy on the button to verify repaint
            BoutonCarte spyButton = spy(new BoutonCarte());
            setPrivateField(spyButton, "minuteur", mockTimer); // Ensure it's using the mocked timer
            // Set animation active manually to ensure timer action is relevant
            setPrivateField(spyButton, "animationActive", true);

            // Simulate the timer firing
            float initialPhase = (float) getPrivateField(spyButton, "phase", float[].class)[0];
            timerActionListener.actionPerformed(null); // The ActionEvent is usually ignored

            // Verify phase updated and repaint called
            float newPhase = (float) getPrivateField(spyButton, "phase", float[].class)[0];
            assertTrue(newPhase > initialPhase);
            verify(spyButton, atLeastOnce()).repaint();
        }
    }

    @Nested
    @DisplayName("Painting Tests")
    class PaintingTests {
        private BoutonCarte button;
        private Graphics2D mockG2d;

        @BeforeEach
        void setup() {
            // Create a spy for the button to observe internal calls
            button = spy(new BoutonCarte());
            // Set dummy size for paintComponent to work correctly
            button.setSize(100, 50);

            // Mock Graphics2D
            mockG2d = mock(Graphics2D.class);
            // Mock `create()` method of Graphics
            when(mockG2d.create()).thenReturn(mockG2d);
        }

        @Test
        @DisplayName("paintComponent sets rendering hints")
        void testPaintComponentRenderingHints() {
            button.paintComponent(mockG2d);
            verify(mockG2d).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            verify(mockG2d).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        @Test
        @DisplayName("paintComponent clips to rounded rectangle")
        void testPaintComponentClipping() {
            button.paintComponent(mockG2d);
            verify(mockG2d).setClip(any(Shape.class));
            // Specifically, verify it's a RoundRectangle2D.Float
            ArgumentCaptor<Shape> shapeCaptor = ArgumentCaptor.forClass(Shape.class);
            verify(mockG2d).setClip(shapeCaptor.capture());
            assertTrue(shapeCaptor.getValue() instanceof java.awt.geom.RoundRectangle2D.Float);
        }


        @Test
        @DisplayName("paintComponent draws background image when present")
        void testPaintComponentDrawsImage() throws NoSuchFieldException, IllegalAccessException {
            BufferedImage testImage = createDummyImage();
            setPrivateField(button, "imageFond", testImage); // Set image using reflection

            button.paintComponent(mockG2d);
            verify(mockG2d).drawImage(eq(testImage), eq(0), eq(0), eq(button.getWidth()), eq(button.getHeight()), eq(null));
            verify(mockG2d, never()).fillRoundRect(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()); // Should not fill with color if image is present
        }

        @Test
        @DisplayName("paintComponent draws background color when no image")
        void testPaintComponentDrawsColor() throws NoSuchFieldException, IllegalAccessException {
            setPrivateField(button, "imageFond", null); // Ensure no image
            setPrivateField(button, "couleurFond", Color.BLUE); // Set a background color

            button.paintComponent(mockG2d);
            verify(mockG2d).setColor(Color.BLUE);
            verify(mockG2d).fillRoundRect(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());
            verify(mockG2d, never()).drawImage(any(), anyInt(), anyInt(), anyInt(), anyInt(), any());
        }

        @Test
        @DisplayName("paintComponent applies hover color over image when not animating")
        void testPaintComponentHoverOverImage() throws NoSuchFieldException, IllegalAccessException {
            BufferedImage testImage = createDummyImage();
            setPrivateField(button, "imageFond", testImage);
            setPrivateField(button, "animationActive", false); // Not animating
            when(button.getModel().isRollover()).thenReturn(true); // Mouse is hovering

            button.paintComponent(mockG2d);
            // Verify image is drawn first
            verify(mockG2d).drawImage(eq(testImage), anyInt(), anyInt(), anyInt(), anyInt(), any());
            // Then verify hover color fill
            verify(mockG2d).setColor((Color) getPrivateField(button, "couleurFondSurvol"));
            verify(mockG2d).fillRoundRect(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());
        }

        @Test
        @DisplayName("paintComponent applies hover color over color background when not animating")
        void testPaintComponentHoverOverColor() throws NoSuchFieldException, IllegalAccessException {
            setPrivateField(button, "imageFond", null);
            setPrivateField(button, "couleurFond", Color.GREEN);
            setPrivateField(button, "animationActive", false);
            when(button.getModel().isRollover()).thenReturn(true);

            button.paintComponent(mockG2d);
            // Verify base color fill first
            verify(mockG2d).setColor(Color.GREEN);
            verify(mockG2d).fillRoundRect(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());
            // Then verify hover color fill (should override base color within the fillRoundRect call)
            verify(mockG2d).setColor((Color) getPrivateField(button, "couleurFondSurvol"));
            verify(mockG2d, times(2)).fillRoundRect(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());
        }


        @Test
        @DisplayName("paintComponent draws animated border when animation is active")
        void testPaintComponentAnimatedBorder() throws NoSuchFieldException, IllegalAccessException {
            setPrivateField(button, "animationActive", true);
            setPrivateField(button, "couleurBordureAnimation", Color.BLUE);

            button.paintComponent(mockG2d);
            // Verify setStroke is called multiple times with different BasicStroke for animation
            verify(mockG2d, atLeast(1)).setStroke(any(BasicStroke.class));
            // Verify setColor is called with the animation color (potentially with alpha changes)
            verify(mockG2d, atLeast(1)).setColor(argThat(color -> color.getRed() == Color.BLUE.getRed() && color.getGreen() == Color.BLUE.getGreen() && color.getBlue() == Color.BLUE.getBlue()));
            // Verify drawRoundRect for the border
            verify(mockG2d, atLeast(1)).drawRoundRect(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());
        }

        @Test
        @DisplayName("paintComponent draws normal border when animation is inactive")
        void testPaintComponentNormalBorder() throws NoSuchFieldException, IllegalAccessException {
            setPrivateField(button, "animationActive", false); // Animation inactive
            setPrivateField(button, "couleurBordureNormale", Color.RED);

            button.paintComponent(mockG2d);
            verify(mockG2d).setColor(Color.RED);
            verify(mockG2d).setStroke(any(BasicStroke.class)); // Should be a single call for normal stroke
            verify(mockG2d).drawRoundRect(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());
        }

        @Test
        @DisplayName("paintComponent applies disabled overlay when button is disabled")
        void testPaintComponentDisabledOverlay() {
            button.setEnabled(false);
            button.paintComponent(mockG2d);
            verify(mockG2d).setColor(new Color(176, 174, 174, 63)); // Verify disabled overlay color
            verify(mockG2d).fillRoundRect(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());
        }

        @Test
        @DisplayName("paintComponent disposes Graphics2D")
        void testPaintComponentDisposesGraphics() {
            button.paintComponent(mockG2d);
            verify(mockG2d).dispose();
        }
    }

    @Nested
    @DisplayName("Size and Layout Tests")
    class SizeLayoutTests {
        private BoutonCarte button;

        @BeforeEach
        void setUp() {
            button = new BoutonCarte();
        }

        @Test
        @DisplayName("getPreferredSize returns default size for empty button")
        void testGetPreferredSizeEmpty() {
            Dimension size = button.getPreferredSize();
            assertEquals(100, size.width);
            assertEquals(50, size.height);
        }

        @Test
        @DisplayName("getPreferredSize returns size based on text with padding")
        void testGetPreferredSizeWithText() {
            button.setText("Hello");
            // Superclass's preferred size calculation is hard to mock precisely without a real UI environment.
            // We'll just verify it's larger than the default and has some padding.
            Dimension superSize = new JButton("Hello").getPreferredSize(); // Get actual JButton preferred size
            Dimension customSize = button.getPreferredSize();
            assertTrue(customSize.width > superSize.width);
            assertTrue(customSize.height > superSize.height);
        }

        @Test
        @DisplayName("getPreferredSize returns default size for image-only button")
        void testGetPreferredSizeWithImageOnly() {
            button.setText(""); // Ensure no text
            button.setImageFond(createDummyImage());
            Dimension size = button.getPreferredSize();
            assertEquals(120, size.width); // Based on the example in BoutonCarte
            assertEquals(50, size.height);
        }

        @Test
        @DisplayName("getMinimumSize returns appropriate minimums")
        void testGetMinimumSize() {
            // Text-based minimum (with padding)
            button.setText("A");
            Dimension textMin = button.getMinimumSize();
            assertTrue(textMin.width > 0);
            assertTrue(textMin.height > 0);

            // Image-only minimum
            button.setText("");
            button.setImageFond(createDummyImage());
            Dimension imageMin = button.getMinimumSize();
            assertEquals(50, imageMin.width);
            assertEquals(50, imageMin.height);

            // Empty minimum
            button.setImageFond((Path) null);
            Dimension emptyMin = button.getMinimumSize();
            assertEquals(40, emptyMin.width);
            assertEquals(20, emptyMin.height);
        }

        @Test
        @DisplayName("getMaximumSize returns Integer.MAX_VALUE for width and height")
        void testGetMaximumSize() {
            Dimension maxSize = button.getMaximumSize();
            assertEquals(Integer.MAX_VALUE, maxSize.width);
            assertEquals(Integer.MAX_VALUE, maxSize.height);
        }
    }

    // --- Helper for accessing private fields ---
    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access private field " + fieldName + ": " + e.getMessage());
            return null; // Should not reach here
        }
    }

    private <T> T getPrivateField(Object obj, String fieldName, Class<T> type) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return type.cast(field.get(obj));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access private field " + fieldName + " of type " + type.getName() + ": " + e.getMessage());
            return null;
        }
    }

    private void setPrivateField(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set private field " + fieldName + ": " + e.getMessage());
        }
    }
}
