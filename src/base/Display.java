package base;

/*
 * @(#) Display.java
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Display renderizador de escenas.
 * 
 * @author Eduar Castrillo
 */
public class Display extends JPanel {
    
    /* Dimension de este <code>Display</code> */
    private Dimension dimension;

    /* Buffer que contiene la escena a dibujar en pantalla. Utilizado para implementar
     * double-buffer */
    private BufferedImage dBuffer;
    /*
     * Contexto grafico de este <code>Display</code>
     */
    private Graphics2D gdc;

    /* Crea una nueva instancia de <code>Display</code> */
    public Display(int w, int h) {

        dimension = new Dimension(w, h);
        
        dBuffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        gdc = dBuffer.createGraphics();

        setBackground(Color.BLACK);

        setMinimumSize(dimension);
        setMaximumSize(dimension);
        setSize(dimension);

        setVisible(true);
    }

    /* Retorna la dimension preferida para este <code>Display</code> */
    public Dimension getPreferredSize() {
        return dimension;
    }

    /* Retorna el contexto grafico de este <code>Display</code> con el cual se
     * dibujaran los contenidos de las escenas a mostrar. */
    public Graphics2D getGDC() {
        return gdc;
    }

    /* Sobreescribir para evitar el borrado */
    public void update(Graphics g) {
        paint(g);
    }

    /* Dibujar escena */
    public void paint(Graphics g) {
        g.drawImage(dBuffer, 0, 0, this);
    }
}
