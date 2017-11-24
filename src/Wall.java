/*
 * @(#) Wall.java    1.0
 */

import base.Object2D;
import java.awt.Graphics2D;
import java.awt.Color;

/**
 * @author Castrillo Velilla
 * @version 1.0
 */
public class Wall extends Object2D {

    private boolean filled = false;
    private boolean rounded = false;
    int round = 5;
    Color c;

    public Wall(int x, int y, int w, int h, Color c) {
        super(Component_ID.WALL, x, y, w, h);
        this.c = c;
    }

    public void setFilled(boolean f) {
        filled = f;
    }

    public void setRounded(boolean r, int nr) {
        rounded = r;
        round = ( round < 0 ) ? 0 : nr;
    }

    public boolean isFilled() {
        return filled;
    }

    public boolean isRounded() {
        return rounded;
    }

    public int getVRound() {
        return round;
    }

    public String toString() {
        return super.toString() + "," +
               filled + "," + rounded + "," + round + "," + c.getRGB();
    }

    public void animate(long time) {}

    public void render(Graphics2D g) {

        g.setColor(c);

        if (filled) {
            if (rounded) {
                g.fillRoundRect(x, y, width, height, round, round);
            } else {
                g.fillRect(x, y, width, height);
            }
        } else {
            if (rounded) {
                g.drawRoundRect(x, y, width, height, round, round);
            } else {
                g.drawRect(x, y, width, height);
            }
        }
    }
}