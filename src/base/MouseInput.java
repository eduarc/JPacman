package base;

/*
 * @(#) MouseInput.java
 */

/**
 *
 * @author Eduar Castrillo
 */
public class MouseInput extends Input {

    public static final int T_DRAGGED   = 0x220;
    public static final int T_MOVED     = 0x221;
    public static final int T_ENTERED   = 0x222;
    public static final int T_EXITED    = 0x223;
    public static final int T_WHEEL     = 0x225;
    public static final int B_NOBUTTON  = 0;

    /* Coordenadas del Puntero del Mouse */
    private int x;
    private int y;
    /* Direccion demovimiento del Wheel */
    //private int wheelDir;

    /* Crea una nueva instancia de <code>MouseInput</code> */
    public MouseInput(int button, int type, int times, int X, int Y) {
        super(button, type, times, C_MOUSE);
        this.x = X;
        this.y = Y;
    }

    /* Retorna la coordenada X donde ocurrio la entrada */
    public int getX() {
        return x;
    }
    /* Retorna la coordenada Y donde ocurrio la entrada */
    public int getY() {
        return y;
    }
}
