package view.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JButton;

public final class Button extends JButton {

    private final boolean accent;

    private static final Color HOVER = new Color(0xB5464F);
    private static final Color NORMAL = new Color(0xC94F58);
    private static final Color PRESSED = new Color(0x9E3942);
    private static final Color OUTLINE = new Color(0xDB6B72);
    private static final Color NEUTRAL = new Color(0x34393F);
    private static final Color NEUTRAL_HOVER = new Color(0x41474E);
    private static final Color HOVER_OUTLINE = new Color(0xC04A53);
    private static final Color PRESSED_OUTLINE = new Color(0x87313A);
    private static final Color NEUTRAL_PRESSED = new Color(0x2D3237);
    private static final Color NEUTRAL_OUTLINE = new Color(0x4B535B);
    private static final Color NEUTRAL_HOVER_OUTLINE = new Color(0x626B75);
    private static final Color NEUTRAL_PRESSED_OUTLINE = new Color(0x3B4249);

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- Constructors -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Button(String text) {
        this(text, null, true);
    }

    public Button(String text, Icon icon) {
        this(text, icon, true);
    }

    public Button(String text, boolean accent) {
        this(text, null, accent);
    }

    private Button(String text, Icon icon, boolean accent) {
        super(text, icon);
        this.accent = accent;
        setIconTextGap(8);
        setToolTipText(text);
        setForeground(Color.WHITE);
        setMargin(new Insets(6, 8, 6, 8));
        setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setRolloverEnabled(true);
        setContentAreaFilled(false);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @Override
    protected void paintBorder(Graphics graphics) {
        Graphics2D g2 = createAntialiasedGraphics(graphics);
        g2.setColor(getOutlineColor());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 9, 9);
        g2.dispose();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = createAntialiasedGraphics(graphics);
        g2.setColor(getFillColor());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 9, 9);
        g2.dispose();
        super.paintComponent(graphics);
    }

    private Graphics2D createAntialiasedGraphics(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return g2;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    private Color getFillColor() {
        if (accent) {
            return getModel().isPressed()
                ? PRESSED
                : getModel().isRollover()
                    ? HOVER
                    : NORMAL;
        }

        return getModel().isPressed()
            ? NEUTRAL_PRESSED
            : getModel().isRollover()
                ? NEUTRAL_HOVER
                : NEUTRAL;
    }

    private Color getOutlineColor() {
        if (accent) {
            return getModel().isPressed()
                ? PRESSED_OUTLINE
                : getModel().isRollover()
                    ? HOVER_OUTLINE
                    : OUTLINE;
        }

        return getModel().isPressed()
            ? NEUTRAL_PRESSED_OUTLINE
            : getModel().isRollover()
                ? NEUTRAL_HOVER_OUTLINE
                : NEUTRAL_OUTLINE;
    }

}
