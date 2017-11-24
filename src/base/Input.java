package base;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Contiene la informacion de una entrada desde un controlador de mando
 *
 * @author Eduar Castrillo
 */
public class Input {

    /* Controladores predefinidos */
    public static final int C_KEYBOARD = 0x0;
    public static final int C_MOUSE    = 0x1;
    /* Tipo de entrada */
    public static final int T_RELEASED = 0x110;
    public static final int T_PRESSED  = 0x111;
    public static final int T_TYPED    = 0x112;
    public static final int T_CLICKED  = T_TYPED;
    /* Boton de entrada */
    private int button;
    /* tipo de entrada. el tipo puede ser:
     * B_PRESSED, B_RELEASED o B_TYPED */
    private int type;
    /* Cantidad de repeticiones de la entrada (consecutivas).
     * NOTA: Utilizado en eventos del mouse */
    private int times;
    /* Controlador que envia la entrada */
    private int controller;

    /* Crea una nueva instancia de <code>Input</code> */
    public Input(int button, int type, int times, int cont) {
        
        this.button     = button;
        this.type       = type;
        this.times      = times;
        this.controller = cont;
    }

    /* Retorna el boton de entrada */
    public int getButton() {
        return button;
    }

    /* Retorna el tipo de entrada */
    public int getType() {
        return type;
    }

    /* Cantidad de repeticiones de la entrada.
     * NOTA: Utilizado en eventos del mouse */
    public int getTimes() {
        return times;
    }

    /* Retorna el mando de entrada */
    public int getController() {
        return controller;
    }
}