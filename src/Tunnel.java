
import java.awt.Graphics2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author debian
 */
public class Tunnel {

    private int outdir1;
    private int outdir2;

    private Door door1;
    private Door door2;

    public Tunnel(Door d1, Door d2, int odir1, int odir2) {

        door1 = d1;
        door2 = d2;
        outdir1 = odir1;
        outdir2 = odir2;
    }

    public Door getDoor1() {
        return door1;
    }

    public Door getDoor2() {
        return door2;
    }

    public boolean contains(Door d) {
        return door1.equals(d) || door2.equals(d);
    }
    
    public int getOutdir1() {
        return outdir1;
    }

    public int getOutdir2() {
        return outdir2;
    }

    public String toString() {
        return door1.toString() + "," + outdir1 + "," +
               door2.toString() + "," + outdir2;
    }

    public void render(Graphics2D g) {
        door1.render(g);
        door2.render(g);
    }
}
