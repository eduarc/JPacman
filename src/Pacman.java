
import base.Object2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author debian
 */
public class Pacman extends Object2D implements Direction {

    private static final int CLOSING = 0;   // estados
    private static final int OPENING = 1;   // de la boca
    private int ix, iy;             // posicion incial
    private int initialMovDir;      // direccion de movimiento incial
    private int nextMovDir;         // siguiente direccion de movimiento
    private int movDir;
    private int pClosedMouth = 100; // porcentaje de cerrado en la boca del pacman
    private int startMouth = 45;    // angulo de incio de la boca
    private int endMouth = 270;     // angulo de fin de la boca
    private int mouthStatus = CLOSING;      // estado actual de la boca
    private boolean death = false;
    private Color nColor;       // color normal
    private Color sColor;       // color anormal
    private Color currColor;    // color actual
    private int hunterDelay = 0;    
    private int mouthDelay = 20;
    private int deadDelay = 70;
    private int updateDelay = 15;
    private int initUpdateDelay;
    private boolean hunter;
    
    private PlayingMode pmode;

    /**
     *
     * @param x posicion en eje x
     * @param y posicion en ejer y
     * @param w ancho
     * @param h alto
     * @param iMovDir direccion de movimiento inicial
     * @param nc color normal
     * @param sc color anormal
     */
    public Pacman(int x, int y, int w, int h, int iMovDir, Color nc, Color sc) {
        super(Component_ID.PACMAN, x, y, w, h);

        ix = x;
        iy = y;

        setMovementDirection(iMovDir);
        initialMovDir = getMovementDirection();
        nextMovDir = initialMovDir;

        nColor = nc;
        sColor = sc;
        currColor = nColor;

        animDelay = 1;
    }

    public void setPlayingMode(PlayingMode pm) {
        pmode = pm;
    }

    public void updateMouth() {

        --mouthDelay;
        if (mouthDelay >= 0) {
            return;
        }
        mouthDelay = 20;

        int pcm = getPercentageClosedMouth();

        if (mouthStatus == CLOSING) {
            if (pcm > 0) {
                setPercentageClosedMouth(pcm - 10);
            } else {
                mouthStatus = OPENING;
            }
        } else {
            if (pcm < 90) {
                setPercentageClosedMouth(pcm + 10);
            } else {
                mouthStatus = CLOSING;
            }
        }
    }

    public void updateDead() {

        --deadDelay;
        if (deadDelay >= 0) {
            return;
        }
        deadDelay = 50;

        // Paso de animacion
        startMouth += 10;
        endMouth = 360 - (2 * (startMouth - 90));
        if (startMouth == 270) {
            restore();
            pmode.lives--;
        }
    }

    public void update() {

        --updateDelay;
        if(updateDelay >= 0) {
            return;
        }
        updateDelay = initUpdateDelay;

        if(!blockedWall(nextMovDir)) {
            setMovementDirection(nextMovDir);
        }

        Door d = blockedDoor(nextMovDir);
        if(d != null) {
            for(Tunnel t : pmode.tunnels) {
                if(t.contains(d)) {
                    toCrossTunnel(t);
                    break;
                }
            }
        }
        else {
            for(Ghost g : pmode.ghosts) {
                if (!g.isCaptured()) {
                    if(collided(g)) {
                        if(hunter) {
                            g.setCaptured(true);
                            pmode.score += 500;
                        } else {
                            death = true;
                            startMouth = 90;
                            endMouth = 360;
                            currColor = sColor;
                            break;
                        }
                    }
                }
            }

            if(death) {
                for(Ghost g : pmode.ghosts) {
                    g.restore();
                }
                return;
            }
            
            ArrayList<Cookie> cookies = pmode.cookies;
            Cookie c;
            for(int i = pmode.cookies.size() - 1; i >= 0; --i) {
                c = cookies.get(i);
                if(eat(c)) {
                    setPercentageClosedMouth(90);
                    
                    pmode.cookies.remove(c);
                    pmode.pcookies.remove(c);
                    pmode.stage.remove(c);
                    pmode.score += 10;
                    
                    if(c.getType() == Cookie.POWERFULL) {
                        hunter = true;
                        hunterDelay += 5000;
                        pmode.score += 90;
                        //pmode.timeHunter = hunterDelay;
                        
                        for(Ghost g : pmode.ghosts) {
                            g.setScared(true);
                        }
                    }
                    else if(c.getType() == Cookie.LIVE) {
                        pmode.lives++;
                    }
                    // complementar
                }
            }
            if(!blockedWall(movDir)) {/*&& !blockedFrontBy(Components_IDs.GDOOR))*/
                move();
            }
        }
    }

    public void updateHunter() {

        --hunterDelay;
        if(hunterDelay >= 0) {
            return;
        }
        hunter = false;
        
        for(Ghost g : pmode.ghosts) {
            g.setScared(false);
        }
    }

    public void setInitDelay(int delay) {
        initUpdateDelay = delay;
    }

    public void setNextMovementDirection(int dir) {

        switch (dir) {
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
                nextMovDir = dir;
                break;
            default:
                nextMovDir = UNKNOWN;
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

    /**
     * Establece el porcentaje de cerrado de la boca
     *
     * @param percentage porcentaje de cerrado 
     */
    public void setPercentageClosedMouth(int percentage) {

        pClosedMouth = percentage;

        int pHalfMouth = (45 * pClosedMouth) / 100;
        int tmpV1 = 0;

        if (movDir == NORTH) {
            startMouth = 135 - pHalfMouth;
            tmpV1 = 90;
        } else if (movDir == SOUTH) {
            startMouth = 315 - pHalfMouth;
            tmpV1 = 270;
        } else if (movDir == WEST) {
            startMouth = 45 - pHalfMouth;
        } else if (movDir == EAST) {
            startMouth = 225 - pHalfMouth;
            tmpV1 = 180;
        }
        endMouth = 360 - ((startMouth - tmpV1) * 2);
    }

    /**
     * Establece al pacman todos sus valores iniciales
     * posicion, direccion inicial de movimiento, etc...
     */
    public void restore() {

        setPosition(ix, iy);
        setMovementDirection(initialMovDir);
        nextMovDir = initialMovDir;
        setPercentageClosedMouth(0);
        currColor = nColor;
        death = false;
        hunterDelay = 0;
        hunter = false;
    }

    public int getPercentageClosedMouth() {
        return pClosedMouth;
    }

    /**
     * @return color actual
     */
    public Color getCurrentColor() {
        return currColor;
    }

    /**
     * @return color normal
     */
    public Color getNColor() {
        return nColor;
    }

    /**
     * @return color anormal
     */
    public Color getSColor() {
        return sColor;
    }

    public int getHunterDelay() {
        return hunterDelay;
    }

    /* Aqui presumimos un valor de velocidad valido */
    public int getMovementDirection() {
        return movDir;
    }

    public boolean isDeath() {
        return death;
    }

    public boolean isHunter() {
        return hunter;
    }

    public void animate(long time) {
        
        int st = pmode.status;
        if(st == Status.ST_PLAYING) {
            if(death) {
                updateDead();
                return;
            }
            updateMouth();
            update();
            if(hunter) {
                updateHunter();
            }
        }
        else if(st == Status.ST_READY) {
            updateMouth();
        }
    }
    
    public void render(Graphics2D g) {

        g.setColor(currColor);
        g.fillArc(x, y, width, height, startMouth, endMouth);
    }

    public String toString() {
        return super.toString() + "," + initialMovDir + "," + nColor.getRGB() + ","
                + sColor.getRGB();
    }

    //----------------- FUNCIONES DE UTILERIA -----------------//
    private boolean eat(Cookie c) {

        if (x > c.x || y > c.y) {
            return false;
        }
        return (x + width > c.x + c.width)
                && (y + width > c.y + c.height);
    }

    private boolean enableAccess(Door d) {

        if (d.getOrientation() == Door.HORIZONTAL) {
            return width <= d.getLength();
        } else {
            return height <= d.getLength();
        }
    }

    private boolean frontAndInsideFrameOf(Door d) {

        if (enableAccess(d)) {

            if (d.getOrientation() == Door.HORIZONTAL) {

                if (d.x > x) {
                    return false;
                }
                if (d.x + d.getLength() < x + width) {
                    return false;
                }
                return y == d.y + 1 || y + height == d.y;

            } else {     // VERTICAL

                if (d.y > y) {
                    return false;
                }
                if (d.y + d.getLength() < y + height) {
                    return false;
                }
                return x == d.x + 1 || x + width == d.x;
            }
        }
        return false;
    }

    private boolean toCrossTunnel(Tunnel t) {

        Door outDoor = null;
        int outDir = 0;

        if (frontAndInsideFrameOf(t.getDoor1())) {
            outDoor = t.getDoor2();
            outDir = t.getOutdir2();
        } else if (frontAndInsideFrameOf(t.getDoor2())) {
            outDoor = t.getDoor1();
            outDir = t.getOutdir1();
        }

        if (outDoor == null) {
            return false;
        }

        if (enableAccess(outDoor)) {

            int w = width;
            int h = height;
            int newX = 0;
            int newY = 0;
            // en caso que la puesta de salida sea horizontal
            if (outDoor.getOrientation() == Door.HORIZONTAL) {

                newX = outDoor.x + ((outDoor.getLength() - w) / 2);

                if (outDir == NORTH) {
                    newY = outDoor.y - h - 1;
                } else {
                    newY = outDoor.y + 1;
                }
            } // en caso que la puesta de salida sea vertical
            else if (outDoor.getOrientation() == Door.VERTICAL) {

                newY = outDoor.y + ((outDoor.getLength() - h) / 2);

                if (outDir == WEST) {
                    newX = outDoor.x + 1;
                } else {
                    newX = outDoor.x - w;
                }
            }
            setMovementDirection(outDir);
            setPosition(newX, newY);
            return true;
        }
        return false;
    }

    /* ** */
    public boolean collided(Ghost g) {
        int gy = g.y;
        int gx = g.x;
        int gw = g.width;
        int gh = g.height;
        return contains(gx + gw/2, gy + gh/2);
    }

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

    public Door blockedDoor(int side) {

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
                return d;
            }
        }
        return null;
    }
}