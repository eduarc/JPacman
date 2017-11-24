package base;

/*
 * @(#) GameEngine.java
 */
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Iterator;
//import javax.swing.SwingUtilities;

/**
 * Controla la animacion, el dibujado y la entrada desde el teclado.
 *
 * @author Eduar Castrillo
 */
public abstract class GameEngine {

    /* Pantalla donde se realiza el dibujado */
    private Display display;
    /* Administrador de dispositivos de mando */
    private InputManager inputman;
    /* Thread de ejecucion */
    private Thread loop;
    /* */
    private Thread painter;
    /* */
    private Thread inputs;
    /* */
    private HashMap<Object2D, Thread> anims;
    /* */
    int animsCounter;
    /* Ejecutando el motor */
    // private boolean running;
    /* Numero de saltos graficos */
    private int gSkips;

    public GameEngine(Dimension displaySize) {

        // running = false;
        gSkips = 20;        // 50 FPS
        
        display = new Display(displaySize.width, displaySize.height);
        display.setFocusable(true);

        inputman = InputManager.getManager();
        anims = new HashMap();
        animsCounter = 0;
    }

    public void setFPS(int fps) {

        if(fps < 1 || fps > 1000) {
            throw new IllegalArgumentException("Illegal FPS value: " + fps);
        }
        gSkips = 1000 / fps;
    }
    
    public void installMouse(boolean ins) {

        if(ins) {
            display.addMouseListener(inputman);
            display.addMouseMotionListener(inputman);
            display.addMouseWheelListener(inputman);
        } else {
            display.removeMouseListener(inputman);
            display.removeMouseMotionListener(inputman);
            display.removeMouseWheelListener(inputman);
        }
    }

    public void installKeyboard(boolean ins) {
        
        if(ins) {
            display.addKeyListener(inputman);
        } else {
            display.removeKeyListener(inputman);
        }
    }

    public void startPainter() {
        
        painter = new Thread(new Runnable() {
            public void run() {
                int skipsCount = gSkips;
                Graphics2D gdc = display.getGDC();
                while(true) {
                    --skipsCount;
                    if(skipsCount == 0) {
                        skipsCount = gSkips;
                        render((Graphics2D)gdc.create());
                        display.repaint();
                    }
                    try {
                        Thread.sleep(1);
                    } catch(InterruptedException ex) {
                        break;
                    }
                }
            }
        });
        painter.setPriority(Thread.MAX_PRIORITY);
        painter.start();
    }

    public void stopPainter() {

        if(painter != null) {
            painter.interrupt();
            painter = null;
        }
    }
    
    public void startInputs() {
        
        inputs = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    if(inputman.getNumInputs() > 0) {
                        command(inputman.pop());     // Procesar comando de juego
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        });
        inputs.setPriority(Thread.MAX_PRIORITY);
        inputs.start();
    }

    public void stopInputs() {

        if(inputs != null) {
            inputs.interrupt();
            inputs = null;
        }
    }

    public void addAnim(Object2D obj) {
        anims.put(obj, null);
    }

    public void removeAnim(Object2D obj) {
        anims.remove(obj);
    }

    public void removeAllAnims() {
        anims.clear();
    }
    
    public void startAnim(final Object2D obj) {
        
        if(!anims.containsKey(obj)) {
            throw new NullPointerException("Animation Thread not found");
        }
        Thread t = new Thread(new Runnable() {
            public void run() {
                long elapsed;
                while(true) {
                    elapsed = System.currentTimeMillis();
                    obj.animate(elapsed);
                    try {
                        Thread.sleep(obj.animDelay);
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        });
        anims.put(obj, t);
        t.start();
    }

    public void stopAnim(Object2D obj) {
        
        Thread t = anims.get(obj);
        if(t != null) {
            t.interrupt();
            t = null;
            anims.put(obj, t);
        }
    }
    
    /* Inicia la ejecucion del motor */
    public void startAllAnims() {

        Object2D obj;
        Iterator<Object2D> it = anims.keySet().iterator();
        while(it.hasNext()) {
            obj = it.next();
            startAnim(obj);
        }
    }

    /* Detiene la ejecucion del motor */
    public void stopAllAnims() {

        Object2D obj;
        Iterator<Object2D> it = anims.keySet().iterator();
        while(it.hasNext()) {
            obj = it.next();
            stopAnim(obj);
        }
    }

    public void startUpdate() {

        loop = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    update();
                    try {
                        Thread.sleep(1);
                    } catch(InterruptedException ex) {
                        break;
                    }
                }
            }
        });
        loop.setPriority(Thread.MAX_PRIORITY);
        loop.start();
    }

    public void stopUpdate() {

        if(loop != null) {
            loop.interrupt();
            loop = null;
        }
    }

    public Display getDisplay() {
        return display;
    }
    
    public Graphics2D getGDC() {
        return display.getGDC();
    }

    /* Loop principal del motor de juego */
    public abstract void update();
    /* Procesa las entradas de los controladores de mando */
    public abstract void command(Input in);
    /* Crea la escena de juego que ser'a mostrada en pantalla */
    public abstract void render(Graphics2D gdc);
}