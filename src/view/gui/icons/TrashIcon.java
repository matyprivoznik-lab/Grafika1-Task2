package view.gui.icons;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

public final class TrashIcon implements Icon {

    private static final int SIZE = 18;

    @Override
    public void paintIcon(Component component, Graphics graphics, int x, int y) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(component.getForeground());

        g2.drawRoundRect(x + 5, y + 6, 8, 10, 2, 2);
        g2.drawLine(x + 10, y + 8, x + 10, y + 14);
        g2.drawLine(x + 3, y + 5, x + 15, y + 5);
        g2.drawLine(x + 7, y + 3, x + 11, y + 3);
        g2.drawLine(x + 8, y + 8, x + 8, y + 14);
        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return SIZE;
    }

    @Override
    public int getIconHeight() {
        return SIZE;
    }

}
