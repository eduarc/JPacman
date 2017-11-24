/*
 * @(#) Door.java    1.0 14/07/2007
 */
import base.Object2D;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @author Castrillo Velilla
 * @version 1.0
 */
public class Door extends Object2D {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL   = 1;

    private Color color;
    private int length;
    private int orientation = HORIZONTAL;

    public Door(int x, int y, int or, int length, Color c) {
        super(Component_ID.DOOR, x, y, 1, 1);

        orientation = or;
        this.length = (length > 0) ? length : 1;

        if (orientation == HORIZONTAL) {
            width = length;
            height = 1;
        } else {
            width = 1;
            height = length;
        }
        color = c;
    }

    public int getLength() {
        return length;
    }

    public int getOrientation() {
        return orientation;
    }

    public String toString() {
        return super.toString() + "," + length + "," + orientation + "," + color.getRGB();
    }

    public void animate(long time) {}
    
    public void render(Graphics2D g) {

        g.setColor(color);

        if (orientation == VERTICAL) {
            g.drawLine(x, y, x, y + length);
        } else {
             g.drawLine(x, y, x + length, y);
        }
    }
}