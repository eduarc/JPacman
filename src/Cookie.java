/*
 * @(#) Cookie.java    1.0, 14/07/2007
 */
import base.Object2D;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @author Castrillo Velilla
 * @version 1.0
 */
public class Cookie extends Object2D {

    public static final int NORMAL    = 0;
    public static final int POWERFULL = 1;
    public static final int LIVE      = 2;

    private int type;
    private Color nColor;
    private Color sColor;
    private Color currColor;

    public Cookie(int type, int x, int y, int w, int h, Color nc, Color sc) {
        super(Component_ID.COOKIE, x, y, w, h);

        setType(type);
        nColor = nc;
        sColor = sc;
        currColor = nColor;
        
        animDelay = 100;
    }

    public final void setType(int t) {
        type = t;
    }

    public int getType() {
        return type;
    }

    public void animate(long time) {

        if(currColor.equals(nColor)) {
            currColor = sColor;
        } else {
            currColor = nColor;
        }
    }
    
    public String toString() {
        return super.toString() + "," + type + "," +
               nColor.getRGB() + "," + sColor.getRGB();
    }

    public void render(Graphics2D g) {

        g.setColor(currColor);

        if(type != LIVE) {
            g.fillOval(x, y, width, height);
        } else {
            g.fillArc(x, y, width, height, 45, 270);
        }
    }
}