
import java.util.ArrayList;
import java.util.Iterator;

/*
 * @(#) PlayingMode.java
 */

/**
 *
 * @author Eduar Castrillo
 */
public class PlayingMode {

    int lives;
    int level;
    int score;
    //int timeHunter;

    int status;
    
    PacmanStage stage;
    Cookie live;
    ArrayList<Cookie> cookies;
    ArrayList<Cookie> pcookies;
    ArrayList<Door> doors;
    ArrayList<Wall> walls;
    ArrayList<Ghost> ghosts;
    ArrayList<Tunnel> tunnels;
    Pacman pacman;

    public PlayingMode() {
        restore();
    }

    public PlayingMode(PacmanStage st) {
        restore();
        setStage(st);
    }

    public final void restore() {
        lives  = 3;
        level  = 0;
        score = 0;
        //timeHunter = 0;
    }

    public final void setStage(PacmanStage st) {

        stage = st;
        cookies = stage.cookies;
        pcookies = stage.pcookies;
        doors   = stage.doors;
        walls   = stage.walls;
        ghosts  = stage.ghosts;
        tunnels = stage.tunnels;
        pacman = stage.pacman;

        pacman.setPlayingMode(this);

        Iterator<Ghost> gs = ghosts.iterator();
        Ghost g;
        while(gs.hasNext()) {
            g = gs.next();
            g.setPlayingMode(this);
        }
    }
}