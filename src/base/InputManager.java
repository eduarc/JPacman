package base;

/*
 * @(#) InputManager.java
 */
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Iterator;
import java.util.LinkedList;
/**
 *
 * @author Eduar Castrillo
 */
public class InputManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener /* ControllerListener*/ {

    /* Cola de entradas desde los controladores */
    private LinkedList<Input> inputs;
    /* Administrador de entradas por defecto */
    private static InputManager manager = new InputManager();

    /* Constructor privado */
    private InputManager() {
        inputs = new LinkedList();
    }

    /* Retorna el Administrador de Entradas por defector */
    public static InputManager getManager() {
        return manager;
    }

    /* Retorna la entrada que est'a a la espera de ser procesada */
    public synchronized Input pop() {
        
        if(inputs.isEmpty()) {
            return null;
        }
        return inputs.pop();
    }
    
    /* NO Hay entradas en la cola de espera */
    public synchronized boolean noInputs() {
        return inputs.isEmpty();
    }

    /* retorna la cola de entradas */
    public synchronized Iterator<Input> getInputs() {
        return inputs.iterator();
    }

    /* retorna el numero de entradas en cola */
    public synchronized int getNumInputs() {
        return inputs.size();
    }

    // -----------------------
    // - CONTROLADOR DEL MOUSE
    // -----------------------
    
    public void keyTyped(KeyEvent e) {

        int vk = e.getKeyCode();
        Input in = new Input(vk, Input.T_TYPED, 0, Input.C_KEYBOARD);
        inputs.add(in);
    }

    public void keyPressed(KeyEvent e) {
       
       int vk = e.getKeyCode();
       Input in = new Input(vk, Input.T_PRESSED, 0, Input.C_KEYBOARD);
       inputs.add(in);
    }

    public void keyReleased(KeyEvent e) {
        
        int vk = e.getKeyCode();
        Input in = new Input(vk, Input.T_RELEASED, 0, Input.C_KEYBOARD);
        inputs.add(in);
    }

    // -----------------------
    // - CONTROLADOR DEL MOUSE
    // -----------------------

    public void mouseClicked(MouseEvent e) {
        
        int bt = e.getButton();  // boton presionado
        int times = e.getClickCount();
        int x = e.getX();
        int y = e.getY();
        Input in = new MouseInput(bt, Input.T_CLICKED, times, x, y);
        inputs.add(in);
    }

    public void mousePressed(MouseEvent e) {

        int bt = e.getButton();  // boton presionado
        int times = e.getClickCount();
        int x = e.getX();
        int y = e.getY();
        Input in = new MouseInput(bt, Input.T_PRESSED, times, x, y);
        inputs.add(in);
    }

    public void mouseReleased(MouseEvent e) {
        
        int bt = e.getButton();  // boton presionado
        int times = e.getClickCount();
        int x = e.getX();
        int y = e.getY();
        Input in = new MouseInput(bt, Input.T_RELEASED, times, x, y);
        inputs.add(in);
    }

    public void mouseEntered(MouseEvent e) {

        int bt = e.getButton();
        int times = e.getClickCount();
        int x = e.getX();
        int y = e.getY();
        Input in = new MouseInput(bt, MouseInput.T_ENTERED, times, x, y);
        inputs.add(in);
    }

    public void mouseExited(MouseEvent e) {

        int bt = e.getButton();
        int times = e.getClickCount();
        int x = e.getX();
        int y = e.getY();
        Input in = new MouseInput(bt, MouseInput.T_EXITED, times, x, y);
        inputs.add(in);
    }

    public void mouseDragged(MouseEvent e) {

        int bt = e.getButton();  // boton presionado
        int times = e.getClickCount();
        int x = e.getX();
        int y = e.getY();
        Input in = new MouseInput(bt, MouseInput.T_DRAGGED, times, x, y);
        inputs.add(in);
    }

    public void mouseMoved(MouseEvent e) {
        
        int bt = e.getButton();
        int times = e.getClickCount();
        int x = e.getX();
        int y = e.getY();
        Input in = new MouseInput(bt, MouseInput.T_MOVED, times, x, y);
        inputs.add(in);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        // Pendiente
    }

    // ----------------------------------
    // - CONTROLADORES DE MANDO, JOYSTICKS
    // ----------------------------------

    /*
    public void buttonPressed(ControllerEvent e) {

        int b  = e.getButton();
        int id = e.getController();
        Input in = new Input(b, Input.T_PRESSED, 0, id);
        inputs.add(in);
    }

    public void buttonReleased(ControllerEvent e) {

        int b  = e.getButton();
        int id = e.getController();
        Input in = new Input(b, Input.T_RELEASED, 0, id);
        inputs.add(in);
    }

    public void buttonClicked(ControllerEvent e) {

        int b  = e.getButton();
        int id = e.getController();
        Input in = new Input(b, Input.T_CLICKED, 0, id);
        inputs.add(in);
    }
     */
}
