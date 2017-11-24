package base;


/*
 * @(#) Stage.java
 */

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Stage del juego.
 *
 * @author Eduar Castrillo
 */
public abstract class Stage {

    private String name;
    /* Dimension de este Stage */
    private int width, height;
    /* Arreglo que contiene todos los objetos en el Stage */
    private ArrayList<Object2D> objects;

    /* Constructor sin parametros */
    public Stage() {
        this("", 1, 1);
    }

    public Stage(int w, int h) {
        this("", w, h);
    }

    public Stage(String name, int w, int h) {

        this.name = name;
        objects = new ArrayList();
        width  = (w > 0) ? w : 1;
        height = (h > 0) ? h : 1;
    }

    public Object2D intersects(int x, int y, int w, int h) {

        for(Object2D o : objects) {
            if(o.intersects(x, y, w, h)) {
                return o;
            }
        }
        return null;
    }
    
    public Object2D intersects(Object2D target) {
        return intersects(target.x, target.y, target.width, target.height);
    }

    public boolean add(Object2D o) {
        return objects.add(o);
    }

    public boolean remove(Object2D o) {
        return objects.remove(o);
    }

    public boolean contains(Object2D o) {
        return objects.contains(o);
    }

    public String getName() {
        return name;
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    /* Retorna una enumeracion con los objetos de este Stage */
    public Iterator<Object2D> getObjects() {
        return objects.iterator();
    }

    public void removeAllObjects() {
        objects.clear();
    }

    public abstract boolean parse(String objData);

    /* Carga un Stage serializado en el archivo F */
    public boolean load(File f) {

        BufferedReader fr = null;
        StringTokenizer token = null;
        String data;
        
        try {
            fr = new BufferedReader(new FileReader(f));
            
                // nombre del Stage
            name = fr.readLine();
                // Leer dimension del Stage
            data = fr.readLine();
            token = new StringTokenizer(data, ",");
            
            if(token.countTokens() != 2) {
                return false;   // Error al leer el encabezado
            }

            width  = Integer.parseInt(token.nextToken());
            height = Integer.parseInt(token.nextToken());
            if(width <= 0 || height <= 0) {
                return false;
            }

            data = fr.readLine();
            while(data != null) {
                if(!parse(data)) {
                    return false;
                }
            }
        } catch(IOException ex) {
            System.out.println("Error when loading Stage");
            return false;
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {}
        }
        return true;   // error en la carga
    }

    /* Serializa este Stage en el archivo F */
    public boolean serialize(File f) {
        
        FileWriter out = null;
        int nObjs = objects.size();
        StringBuffer buff = new StringBuffer(33 * nObjs);
        
        try {
            out = new FileWriter(f);
                // Formatear la informacion a escribir en el archivo
            buff.append(name + "\n");
            buff.append(width + "," + height + "\n");

            for(Object2D o : objects) {
                buff.append(o);
            }
            out.write(buff.toString());
        } catch (IOException ex) {
            System.out.println("Error when writing Stage");
            return false;
        } finally {
            try {
                out.close();
            } catch (IOException ex) {}
        }
        return true;
    }

    /* Renderiza todos los objetos en el contexto grafico pasado como parametro */
    public void render(Graphics2D g) {
        int i = 0;
        for(; i < objects.size(); ++i) {
            objects.get(i).render(g);
        }
    }
}
