
import base.Object2D;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author arch
 */
public class Ghost extends Object2D implements Direction {

    public static final int MINIMUN_SIZE = 5;

    protected int ix, iy;           // posicion inicial
    private int initialMovDir;      // direccion de movimiento inicial
    private int movDir;
    private int initUpdateDelay;
    private int scale;              // escala de dibujado
    private Color nColor;           // color normal
    private Color sColor;           // color anormal
    private Color currColor;        // color actual
    private int skirtDelay = 500;
    private int alternativeRoadDelay = 100;
    private int colorDelay = 70;
    private int updateDelay = 15;
    private int currUpdateDelay;
    private Object2D home;
    private boolean captured = false;
    private boolean scared = false;
    
    private int typeAI;                 // tipo de "IA"
    private int sectorOnMap;            // sector a proteger en el mapa

    /** variables para la IA */
    boolean inTask = false;
    int alternativeRoad;
    boolean skirtOne = false;

    private PlayingMode pmode;
    private Pacman pacman;              // pacman..objetivo final

    public Ghost(int x, int y, int scale, int iMovDir, Color nc, Color sc) {
        super(Component_ID.GHOST, x, y, scale * MINIMUN_SIZE, scale * MINIMUN_SIZE);

        ix = x;
        iy = y;

        this.scale = scale;
        setMovementDirection(iMovDir);
        initialMovDir = getMovementDirection();

        nColor = nc;
        sColor = sc;
        currColor = nColor;
        
        home = new Object2D() {
            public void render(Graphics2D g) {}
            public void animate(long time) {}
        };
        home.x = ix;
        home.y = iy;

        typeAI = AIGhost.HUNTER;
        animDelay = 1;
    }

    public void setPlayingMode(PlayingMode pm) {
        
        pmode = pm;
        pacman = pmode.pacman;
    }

    public void updateSkirt() {
        
        --skirtDelay;
        if(skirtDelay >= 0) {
            return;
        }
        skirtDelay = 500;

        if(skirtOne) {
            skirtOne = false;
        } else {
            skirtOne = true;
        }
    }

    public void updateAlternativeRoad() {
        
        --alternativeRoadDelay;
        if(alternativeRoadDelay >= 0) {
            return;
        }
        alternativeRoadDelay = 100;
        
        alternativeRoad = 1 + (int)(Math.random() * 2);
        alternativeRoad = (alternativeRoad == 1) ? 1 : 0;   // 1 o 0
    }

    public void updateColor() {

        --colorDelay;
        if(colorDelay >= 0) {
            return;
        }
        if(pacman.getHunterDelay() > 1500) {
            colorDelay = 120;
        } else {
           colorDelay = 60;
        }

        if(currColor == sColor) {
            currColor = nColor;
        } else {
            currColor = sColor;
        }
    }

    public void update() {

        --updateDelay;
        if(updateDelay >= 0) {
            return;
        }
        updateDelay = currUpdateDelay;

        if(captured) {
            updateDelay /= 4;
            AIGhost.exec(false, this, home, pmode.stage);
            if (x == ix && y == iy) {
                captured = false;
                currUpdateDelay = initUpdateDelay;
            }
        } else {
            if (!scared) {
                AIGhost.exec(false, this, pacman, pmode.stage);
            } else {
                AIGhost.exec(true, this, pacman, pmode.stage);
            }
        }
    }

    public void setInitDelay(int delay) {
        initUpdateDelay = delay;
        currUpdateDelay = delay;
    }

    public void setTypeAI(int t) {
        typeAI = t;
    }

    public void setSectorOnMap(int s) {
        sectorOnMap = s;
    }

    public void setCaptured(boolean c) {
        captured = c;
    }

    public void setScared(boolean s) {
        scared = s;
        if(s == false) {
            currColor = nColor;
        }
    }

    public void setMovementDirection(int dir) {

        vx = vy = 0;
        switch (dir) {
            case NORTH:
                vy = -1;
                break;
            case SOUTH:
                vy = 1;
                break;
            case EAST:
                vx = -1;
                break;
            case WEST:
                vx = 1;
                break;
            default:
                dir = UNKNOWN;
        }
        movDir = dir;
    }

    public int getTypeAI() {
        return typeAI;
    }

    public int getSectorOnMap() {
        return sectorOnMap;
    }

    public boolean isCaptured() {
        return captured;
    }

    public boolean isScared() {
        return scared;
    }

    public int getMovementDirection() {
        return movDir;
    }

    public int getScale() {
        return scale;
    }

    public void restore() {

        setPosition(ix, iy);
        setMovementDirection(initialMovDir);
        captured = false;
        currColor = nColor;
        scared = false;
    }
    
    public void render(Graphics2D g) {

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));

        if(captured) {
            paintCapturedGhost(g);
        } else {
            paintGhost(g);
        }
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public String toString() {
        return super.toString() + "," + initialMovDir + "," + nColor.getRGB() + ","
                + sColor.getRGB() + "," + scale;
    }

    public void animate(long elapsedTime) {
        int st = pmode.status;

        if(st == Status.ST_PLAYING) {
            if(pacman.isDeath()) {
                return;
            }
            updateAlternativeRoad();
            updateSkirt();
            if(pacman.isHunter()) {
                updateColor();
            }
            update();
        }
        else if(st == Status.ST_READY) {
            updateSkirt();
        }
    }

    // -----------
    // UTILITARIOS
    // -----------

    private void paintGhost(Graphics2D g) {

        int w = width;
        int f = scale;
        int nPoints = 0;
        int pX[];
        int pY[];

        // cuerpo
        g.setColor(currColor);
        g.fillArc(x, y, w, (f*2), 0, 180);
        g.fillRect(x, (y + f), w, f*3);

        if(skirtOne == false) {
            // Parte uno
            nPoints = 11;
            pX = new int[nPoints];
            pY = new int[nPoints];

            pX[0] = x;
            pY[0] = y+(f*4);

            pX[1] = x+(f/2);
            pY[1] = y+(f*5);

            pX[2] = x+f;
            pY[2] = y+(f*4);

            pX[3] = x+f+(f/2);
            pY[3] = y+(f*5);

            pX[4] = x+(f*2);
            pY[4] = y+(f*4);

            pX[5] = x+(f*2)+(f/2);
            pY[5] = y+(f*5);

            pX[6] = x+(f*3);
            pY[6] = y+(f*4);

            pX[7] = x+(f*3)+(f/2);
            pY[7] = y+(f*5);

            pX[8] = x+(f*4);
            pY[8] = y+(f*4);

            pX[9] = x+(f*4)+(f/2);
            pY[9] = y+(f*5);

            pX[10] = x+(f*5);
            pY[10] = y+(f*4);

        } else {
            // Parte dos
            nPoints = 13;
            pX = new int[nPoints];
            pY = new int[nPoints];

            pX[0] = x;
            pY[0] = y+(f*4);

            pX[1] = x;
            pY[1] = y+(f*5);

            pX[2] = x+(f/2);
            pY[2] = y+(f*4)+(f/2);

            pX[3] = x+f;
            pY[3] = y+(f*5);

            pX[4] = x+f+(f/2);
            pY[4] = y+(f*4)+(f/2);

            pX[5] = x+(f*2);
            pY[5] = y+(f*5);

            pX[6] = x+(f*2)+(f/2);
            pY[6] = y+(f*4)+(f/2);

            pX[7] = x+(f*3);
            pY[7] = y+(f*5);

            pX[8] = x+(f*3)+(f/2);
            pY[8] = y+(f*4)+(f/2);

            pX[9] = x+(f*4);
            pY[9] = y+(f*5);

            pX[10] = x+(f*4)+(f/2);
            pY[10] = y+(f*4)+ (f/2);

            pX[11] = x+(f*5);
            pY[11] = y+(f*5);

            pX[12] = x+(f*5);
            pY[12] = y+(f*4);
        }
        g.fillPolygon(pX, pY, nPoints);

        // ojos
        g.setColor(Color.BLACK);
        g.fillOval(x+f+(f/4), y+f+(f/4), f,f);
        g.fillOval(x+(3*f)-(f/4), y+f+(f/4), f,f);

        if(scared) {
            // cejas
            g.drawLine(x+(f/2), y+f+(f/2), x+f+(f/2), y+(3*(f/4)));
            g.drawLine(x+(3*f)+(3*(f/4)), y+(3*(f/4)), x+(4*f)+(f/4), y+f+(f/2));

            //boca
            int[] pX1 = { x+f, x+f+(f/2), x+(2*f), x+(2*f)+(f/2), x+(3*f), x+(3*f)+(f/2), x+(4*f)};
            int[] pY1 = { y+(3*f)-(f/4), y+(2*f)+(3*(f/4)) };

            g.drawLine(pX1[0], pY1[0], pX1[1], pY1[1]);
            g.drawLine(pX1[1], pY1[1], pX1[2], pY1[0]);
            g.drawLine(pX1[2], pY1[0], pX1[3], pY1[1]);
            g.drawLine(pX1[3], pY1[1], pX1[4], pY1[0]);
            g.drawLine(pX1[4], pY1[0], pX1[5], pY1[1]);
            g.drawLine(pX1[5], pY1[1], pX1[6], pY1[0]);
        } else {
            // cejas
            g.drawLine(x+f+(3*(f/4)), y+(3*(f/4)), x+(2*f)+(f/4), y+f+(f/4));
            g.drawLine(x+(2*f)+(3*(f/4))+(f/4), y+f+(f/4), x+(3*f)+(f/4)+(f/4), y+(3*(f/4)));

            //boca
            g.drawLine(x+f+(f/2), y+(3*f)+(f/4), x+(2*f)+(f/3), y+(2*f)+(3*(f/4)));
            g.drawLine(x+(2*f)+(f/2), y+(2*f)+(3*(f/4)), x+(3*f)+(f/3), y+(3*f)+(f/4));
        }
    }

    private void paintCapturedGhost(Graphics2D g) {

        int f = scale;
        int[] pX = { x+f, x+f+(f/2), x+(2*f), x+(2*f)+(f/2), x+(3*f), x+(3*f)+(f/2), x+(4*f)};
        int[] pY = { y+(4*f), y+(3*f)+(f/2) };

        g.setColor(Color.WHITE);
        g.fillOval(x+f, y+(3*(f/4)), f+(3*(f/4)), 2*f+(f/4));
        g.fillOval(x+2*f+(3*(f/4)), y+f+(f/4), 3*(f/2), 3*(f/2));

        g.setColor(Color.BLACK);
        g.fillOval(x+f+(f/2), y+f+(f/2), f/2, f/2);
        g.fillOval(x+(3*f), y+f+f-(f/4), f/2, f/2);

        g.setColor(Color.WHITE);
        g.drawLine(pX[0], pY[0], pX[1], pY[1]);
        g.drawLine(pX[1], pY[1], pX[2], pY[0]);
        g.drawLine(pX[2], pY[0], pX[3], pY[1]);
        g.drawLine(pX[3], pY[1], pX[4], pY[0]);
        g.drawLine(pX[4], pY[0], pX[5], pY[1]);
        g.drawLine(pX[5], pY[1], pX[6], pY[0]);
    }

    // ---------------------
    // FUNCIONES DE UTILERIA
    // ---------------------
    public boolean blockedWall(int side) {

        int px = x;
        int py = y;

        switch(side) {
            case NORTH: --py;   break;
            case SOUTH: ++py;   break;
            case EAST:  --px;   break;
            case WEST:  ++px;   break;
        }

        for(Wall w : pmode.walls) {
            if(w.intersects(px, py, width, height)) {
                return true;
            }
        }
        return false;
    }

    public boolean blockedDoor(int side) {

        int px = x;
        int py = y;

        switch(side) {
            case NORTH: --py;   break;
            case SOUTH: ++py;   break;
            case EAST:  --px;   break;
            case WEST:  ++px;   break;
        }
        for(Door d : pmode.doors) {
            if(d.intersects(px, py, width, height)) {
                return true;
            }
        }
        return false;
    }
}
