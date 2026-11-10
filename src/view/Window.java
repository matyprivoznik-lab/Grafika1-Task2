package view;

import view.gui.Toolbar;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private final Canvas canvas;
    private final Toolbar toolbar;

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- Constructors -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Window(int width, int height) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("FIM UHK | PGRF1 | 2026 | PŘÍVOZNÍK MATĚJ");
        setLayout(new BorderLayout());
        setResizable(false);

        toolbar = new Toolbar();
        canvas = new Canvas(width, height);
        add(toolbar, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        pack();

        setLocationRelativeTo(null);
        canvas.setFocusable(true);
        canvas.grabFocus();
        setVisible(true);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Getters -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Canvas getCanvas() {
        return canvas;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

}
