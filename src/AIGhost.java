
import base.Object2D;
import base.Stage;
import java.util.Iterator;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author arch
 */
public class AIGhost implements Direction {

    /**
     * Tipos de movimientos
     */
    public static final int HUNTER     = 0;
    public static final int GUARD      = 1;
    public static final int CRAZY      = 2;
    public static final int RUN_OFF    = 3;
    public static final int GO_TO_HOME = 4;

    // AIGhost predeterminado
    private static final AIGhost defaultAIGhost = new AIGhost();

    private static Stage stage;
    private static Ghost ghost;
    private static Object2D target;
    private static boolean rOff;

    private AIGhost() {
        // Vacio
    }

    private AIGhost getInstance() {
        return defaultAIGhost;
    }

    public static void exec(boolean runningOff, Ghost g, Object2D t, Stage st) {

        ghost = g;
        stage = st;
        target = t;
        rOff = runningOff;

        int ai = g.getTypeAI();

        if (ai == HUNTER) {
            hunter();
        }
        else if (ai == GUARD) {
            guard();
        }
        else if (ai == CRAZY) {
            crazy();
        }
    }

    private static void hunter() {

        Ghost g = ghost;
        Object2D t = target;

        int ts = getSector(t);
        int gs = getSector(g);

        // Decidir Cambiar de tarea antes de terminarala
        int noTask = 1 + (int)(Math.random() * 500);
        if (noTask == 1) {
            g.inTask = false;
        }

        if (g.inTask == false) {
            if (ts == gs) {
                sameSector();
            } else {
                opposedSector();
            }
            g.inTask = true;
        }

        if (g.alternativeRoad == 1) { // ALTERNATIVE ROADS
            chooseAlternativeRoad();
        }
        update();
    }

    private static void crazy() {

    }

    private static void guard() {

    }

    private static void update() {

         // Si el objeto llegó a un limite (Puerta o muro) concluyó tarea
        if (ghost.blockedWall(ghost.getMovementDirection())
                || ghost.blockedDoor(ghost.getMovementDirection()))
        {
            ghost.inTask = false;
        } else { // mover el fantasma hasta que concluya la tarea
            ghost.move();
        }
    }

    private static int getSector(Object2D obj) {

        int w = stage.getWidth();
        int h = stage.getHeight();
        int s = NORTH | WEST;  // sector

        if(obj.y < h / 2) {
            if(obj.x < w / 2) {
                s = NORTH | EAST;
            }
        } else {
            s = SOUTH | WEST;
            if(obj.x < w / 2) {
                s = SOUTH | EAST;
            }
        }
        return s;
    }

    private static void sameSector() {

        Object2D t = target;
        Ghost g = ghost;

        int fd = 0;
        int sd = 0;
        boolean tpMgp = false;
        int ar = UNKNOWN;  // alternative road
        boolean alternativeRoad = false;

        int dx = Math.abs(t.x - g.x);
        int dy = Math.abs(t.y - g.y);

        boolean txMgx  = t.x > g.x;
        boolean tyMgy  = t.y > g.y;

        if (dx > dy) {
            fd = WEST;
            sd = EAST;
            tpMgp = txMgx;
            ar = NORTH | SOUTH;
        } else {
            fd = SOUTH;
            sd = NORTH;
            tpMgp = tyMgy;
            ar = EAST | WEST;
        }

        if (rOff) {
            fd ^= sd;
            sd ^= fd;
            fd ^= sd;
        }

        if (tpMgp) {
            alternativeRoad = g.blockedWall(fd) || g.blockedDoor(fd);

            if (!alternativeRoad) {
                g.setMovementDirection(fd);
                return;
            }
        } else {
            alternativeRoad = g.blockedWall(sd) || g.blockedDoor(sd);

            if (!alternativeRoad) {
                g.setMovementDirection(sd);
                return;
            }
        }
        if (alternativeRoad)
            changeMovDirection(ar);
    }

    private static void opposedSector() {

        Object2D t = target;
        Ghost g = ghost;
        int ts = getSector(t);
        int gs = getSector(g);

        int md = 0; // movement direction
        int ar = NORTH | SOUTH;  // alternative road
        int dir = WEST;
        boolean totallyOpposed = false;
        boolean alternativeRoad = false;

        if((ts & NORTH) != 0) {
            totallyOpposed = (gs & SOUTH) != 0;
            md = NORTH;
            if((ts & EAST) != 0) {
                dir = EAST;
            }
        }
        else { // SOUTH
            totallyOpposed = (gs & NORTH) != 0;
            md = SOUTH;
            if((ts & EAST) != 0) {
                dir = EAST;
            }
        }

        if (rOff) {
            md = (md == NORTH) ? SOUTH : NORTH;
            dir = (dir == WEST) ? EAST : WEST;
        }

        if (totallyOpposed) {
            alternativeRoad = g.blockedWall(md) || g.blockedDoor(md);

            if (!alternativeRoad) {
                g.setMovementDirection(md);
                return;
            }
            ar = EAST | WEST;
        }
        else {
            alternativeRoad = g.blockedWall(dir) || g.blockedDoor(dir);

            if (!alternativeRoad) {
                g.setMovementDirection(dir);
                return;
            }
        }
            // alternativeRoad
        changeMovDirection(ar);
    }

    private static void changeMovDirection(int opts) {

        Object2D t = target;
        Ghost g = ghost;

        int fd = 0;  // first direction
        int sd = 0;  //  second direction
        boolean txMgx  = t.x >  g.x;
        boolean tyMgy  = t.y >  g.y;

        if(opts == (NORTH | SOUTH)) {
            fd = NORTH;
            sd = SOUTH;
            if(tyMgy) {
                fd = SOUTH;
                sd = NORTH;
            }
        } else {    // EAST | WEST
            fd = EAST;
            sd = WEST;
            if(txMgx) {
                fd = WEST;
                sd = EAST;
            }
        }

        if (rOff) {
            fd ^= sd;
            sd ^= fd;
            fd ^= sd;
        }

        if(!g.blockedWall(fd) && !g.blockedDoor(fd)) {
            g.setMovementDirection(fd);
        }
        else if(!g.blockedWall(sd) && !g.blockedDoor(sd)) {
            g.setMovementDirection(sd);
        }
    }

    private static void chooseAlternativeRoad() {

        Object2D t = target;
        Ghost g = ghost;
        
        int fd = 0;
        int sd = 0;
        boolean noSameAxy = false;
        boolean tpMgp = false;
        int dir = 0;

        boolean txMgx  = t.x > g.x;
        boolean txEQgx = t.x == g.x;
        boolean tyMgy  = t.y >  g.y;
        boolean tyEQgy = t.y == g.y;

        int movDir = g.getMovementDirection();
        
        if (movDir == NORTH || movDir == SOUTH) {
            fd = WEST;
            sd = EAST;
            noSameAxy = !txEQgx;
            tpMgp = txMgx;
            dir = NORTH;
            if(movDir == NORTH) {
                dir = SOUTH;
            }
        } else {    // EAST || WEST
            fd = SOUTH;
            sd = NORTH;
            noSameAxy = !tyEQgy;
            tpMgp = tyMgy;
            dir = EAST;
            if(movDir == EAST) {
                dir = WEST;
            }
        }

        if (rOff) {
            fd ^= sd;
            sd ^= fd;
            fd ^= sd;
        }

        int i = 1 + (int)(Math.random() * 2);
        i = (i == 1) ? 1 : 0;   // 1 or 0

        if (noSameAxy) {
            if (tpMgp) {

                if (!g.blockedWall(fd)) {
                    g.setMovementDirection(fd);
                }
                else if (g.blockedWall(dir) ) {
                    int newDir = sd;
                    if(i == 1) {
                        newDir = dir;
                    }
                    g.setMovementDirection(newDir);
                }
            } else {
                if (!g.blockedWall(sd)) {
                    g.setMovementDirection(sd);
                }
                else if (g.blockedWall(dir)) {
                    int newDir = fd;
                    if(i == 1) {
                        newDir = dir;
                    }
                    g.setMovementDirection(newDir);
                }
            }
        }
    }
}
