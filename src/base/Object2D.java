package base;

/*
 * @(#) Object2D.java
 */

import java.awt.Graphics2D;

/**
 * Objecto Bi-Dimensional con capacidad de movimiento.
 * 
 * @author Eduar Castrillo
 */
public abstract class Object2D {

    /* identificador del tipo de objeto */
    public int ID;
    /* Coodernadas posicionales del objeto */
    public int x, y;
    /* Dimension del objeto. Asuminos cada objeto como un
     * rectangulo */
    public int width, height;
    /* Vector velocidad del objeto, este vector determina el movimiento */
    public int vx, vy;
    /* Retardo de la animacion en milisegundos */
    public int animDelay;

    /* Constructor sin parametros */
    public Object2D() {
        
        ID = 0;
        x  = 0;
        y  = 0;
        width  = 0;
        height = 0;
        vx = 0;
        vy = 0;
    }

    public Object2D(int id) {
        this.ID = id;
    }

    public Object2D(int X, int Y, int W, int H) {

        this.x = X;
        this.y = Y;
        width  = W;
        height = H;
    }

    public Object2D(int id, int X, int Y, int W, int H) {
        
        this.ID = id;
        this.x  = X;
        this.y  = Y;
        width   = W;
        height  = H;
    }

    /* Establece la posicion del objeto */
    public void setPosition(int X, int Y) {
        this.x = X;
        this.y = Y;
    }

    /* establece la dimension del objeto */
    public void setDimension(int W, int H) {
        width  = W;
        height = H;
    }

    /* Establece el vector velocidad del objeto */
    public void setVelocity(int VX, int VY) {
        this.vx = VX;
        this.vy = VY;
    }

    /* Desplaza el objeto. El vector velocidad determina el movimiento */
    public void move() {
        x += vx;
        y += vy;
    }
    
    /* Determina si este objeto contiene el punto especificado */
    public boolean contains(int X, int Y) {

        int w = width;
        int h = height;
        if ((w | h) < 0) {
            return false;
        }
        int tx = this.x;
        int ty = this.y;
        if (X < tx || Y < ty) {
            return false;
        }
        w += tx;
        h += ty;
            // Sin overflow...
        return (w > X) && (h > Y);
    }

    /* Determina si este objeto intercepta el retangulo especificado */
    public boolean intersects(int X, int Y, int W, int H) {
        
        int tw = this.width;
        int th = this.height;
        int rw = W;
        int rh = H;

        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        int tx = this.x;
        int ty = this.y;
        int rx = X;
        int ry = Y;
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        // Sin overflow...
        return ((rw > tx) &&
                (rh > ty) &&
                (tw > rx) &&
                (th > ry));
    }

    public abstract void render(Graphics2D g);
    //public abstract void parse(String data);
    public abstract void animate(long elapsedTime);
    
    /* Utilizado en la serializacion de los Stage */
    public String toString() {
        return  ID + "," +
                x + "," + y + "," +
                width + "," + height + "," +
                vx + "," + vy + "\n";
    }
}
