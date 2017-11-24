
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import base.Stage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author arch
 */
public class PacmanStage extends Stage {

    Color fgColor;
    Color bgColor;

    int liveTime;
    int difficultInterval;
    int levelToLive;
    int scoreNeedToLive;
    
    Cookie live;
    ArrayList<Cookie> cookies;
    ArrayList<Cookie> pcookies;
    ArrayList<Door> doors;
    ArrayList<Wall> walls;
    ArrayList<Ghost> ghosts;
    ArrayList<Tunnel> tunnels;
    Pacman pacman;

    public PacmanStage() {}

    public PacmanStage(String name, int w, int h) {
        super(name, w, h);

        live = null;
        cookies = new ArrayList();
        pcookies = new ArrayList();
        doors   = new ArrayList();
        walls   = new ArrayList();
        ghosts  = new ArrayList();
        tunnels = new ArrayList();
    }

    /* Crear y adicionar todos los objetos al mapa */
    public boolean parse(String objData) {
        return true;
    }

    public void render(Graphics2D g) {

        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        super.render(g);
    }
}
