import controller.Controller;
import view.Window;

import javax.swing.SwingUtilities;

import static javax.swing.SwingUtilities.invokeLater;

/**
 * Application entry point.
 * Initializes and displays the raster canvas using {@link SwingUtilities#invokeLater(Runnable)}.
 *
 * @author PGRF FIM UHK
 * @version 2026
 */
public class Main {

    public static void main(String[] args) {
        invokeLater(() -> {
            Window window = new Window(800, 600);
            new Controller(window.getCanvas(), window.getToolbar()).init();
        });
    }

}
