
import java.awt.Color;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author arch
 */
public class DefaultStage extends PacmanStage {

    Cookie live;
    ArrayList<Cookie> or_cookies;
    ArrayList<Cookie> or_pcookies;
    Ghost pink;
    Ghost cyan;
    Ghost orange;
    Ghost red;

    public DefaultStage() {
        super("The Beginning",375, 450);

        or_cookies = new ArrayList();
        or_pcookies = new ArrayList();
        
        bgColor = Color.BLACK;
        fgColor = Color.YELLOW;
        
        liveTime = 10;
        difficultInterval = 3;
        levelToLive = 3;
        scoreNeedToLive = 850;
        live = new Cookie(Cookie.LIVE, 180, 260, 20, 20, Color.YELLOW, Color.YELLOW);

        // -----------------------
        // AGREGAR OBJETOS AL MAPA
        // -----------------------

        Color tunnel = Color.BLACK;
        Color wall = Color.BLUE;

        Color nPacman = Color.YELLOW;
        Color sPacman = Color.BLUE;

        Color sGhost = Color.BLUE;

        Color nCookie = Color.WHITE;
        Color sCookie = Color.MAGENTA;
        
        addWall(new Wall(0, 0, 375, 15, wall));
        addWall(new Wall(180, 15, 15, 60, wall));
        addWall(new Wall(150, 105, 75, 15, wall));
        addWall(new Wall(180, 120, 15, 45, wall));
        addWall(new Wall(150, 240, 75, 15, wall));  // 5
        addWall(new Wall(150, 285, 75, 15, wall));
        addWall(new Wall(180, 300, 15, 45, wall));
        addWall(new Wall(150, 375, 75, 30, wall));
        addWall(new Wall(0, 435, 375, 15, wall));
        addWall(new Wall(0, 15, 15, 150, wall)); // 10
        addWall(new Wall(45, 45, 30, 30, wall));
        addWall(new Wall(105, 45, 45, 30, wall));
        addWall(new Wall(225, 45, 45, 30, wall));
        addWall(new Wall(300, 45, 30, 30, wall));
        addWall(new Wall(360, 15, 15, 150, wall)); // 15
        addWall(new Wall(45, 105, 30, 15, wall));
        addWall(new Wall(300, 105, 30, 15, wall));
        addWall(new Wall(15, 150, 45, 15, wall));
        addWall(new Wall(60, 150, 15, 45, wall));
        addWall(new Wall(105, 105, 15, 90, wall)); // 20
        addWall(new Wall(120, 150, 30, 15, wall));
        addWall(new Wall(225, 150, 30, 15, wall));
        addWall(new Wall(255, 105, 15, 90, wall));
        addWall(new Wall(300, 150, 15, 45, wall));
        addWall(new Wall(315, 150, 45, 15, wall)); // 25
        addWall(new Wall(0, 180, 60, 15, wall));
        addWall(new Wall(315, 180, 60, 15, wall));
        addWall(new Wall(0, 225, 60, 15, wall));
        addWall(new Wall(60, 225, 15, 45, wall));
        addWall(new Wall(105, 225, 15, 75, wall)); // 30
        addWall(new Wall(150, 195, 15, 45, wall));
        addWall(new Wall(60, 315, 15, 45, wall));
        addWall(new Wall(210, 195, 15, 45, wall)); // 33
        addWall(new Wall(255, 225, 15, 75, wall));
        addWall(new Wall(300, 225, 15, 45, wall)); // 35
        addWall(new Wall(315, 225, 60, 15, wall));
        addWall(new Wall(0, 255, 60, 15, wall));
        addWall(new Wall(315, 255, 60, 15, wall));
        addWall(new Wall(0, 270, 15, 165, wall));
        addWall(new Wall(360, 270, 15, 165, wall)); // 40
        addWall(new Wall(45, 300, 30, 15, wall));
        addWall(new Wall(300, 300, 30, 15, wall));
        addWall(new Wall(15, 345, 15, 15, wall));
        addWall(new Wall(105, 330, 45, 15, wall));
        addWall(new Wall(225, 330, 45, 15, wall)); // 45
        addWall(new Wall(300, 315, 15, 45, wall));
        addWall(new Wall(345, 345, 15, 15, wall));
        addWall(new Wall(45, 390, 75, 15, wall));
        addWall(new Wall(255, 390, 75, 15, wall));
        addWall(new Wall(105, 375, 15, 15, wall)); // 50
        addWall(new Wall(255, 375, 15, 15, wall));

        
        Tunnel t = new Tunnel(new Door(0, 195, Door.VERTICAL, 30, tunnel),
                                  new Door(374, 195, Door.VERTICAL, 30, tunnel),
                                        Direction.WEST,
                                            Direction.EAST);

        Door d1 = t.getDoor1();
        Door d2 = t.getDoor2();
        add(d1);
        add(d2);
        doors.add(d1);
        doors.add(d2);
        tunnels.add(t);

        Pacman p = new Pacman(174, 345, 30, 30, Direction.WEST, nPacman, sPacman);
        p.setInitDelay(8);
        add(p);
        pacman = p;

        pink = new Ghost(174, 210, 6, Direction.NORTH, Color.PINK, sGhost);
        //pink.setTypeAI(AIGhost.CRAZY);
        pink.setInitDelay(15);

        cyan = new Ghost(175, 210, 6, Direction.NORTH, Color.CYAN, sGhost);
        //cyan.setTypeAI(AIGhost.CRAZY);
        cyan.setInitDelay(15);

        orange = new Ghost(172, 210, 6, Direction.NORTH, Color.ORANGE, sGhost);
        orange.setInitDelay(14);

        red = new Ghost(173, 210, 6, Direction.NORTH, Color.RED, sGhost);
        red.setInitDelay(13);

        add(pink);
        add(cyan);
        add(orange);
        add(red);

        ghosts.add(pink);
        ghosts.add(cyan);
        ghosts.add(orange);
        ghosts.add(red);
        
        // --------
        // GALLETAS
        // --------

        int j = 0;
        int i = 0;

        // Powerfull cookies
        Cookie c1 = new Cookie(Cookie.POWERFULL, 25, 55, 10, 10, nCookie, sCookie);
        //c1.setAnimateColor(true);
        Cookie c2 = new Cookie(Cookie.POWERFULL, 25, 325, 10, 10, nCookie, sCookie);
        //c2.setAnimateColor(true);
        Cookie c3 = new Cookie(Cookie.POWERFULL, 340, 55, 10, 10, nCookie, sCookie);
        //c3.setAnimateColor(true);
        Cookie c4 = new Cookie(Cookie.POWERFULL, 340, 325, 10, 10, nCookie, sCookie);
        //c4.setAnimateColor(true);

        or_pcookies.add(c1);
        or_pcookies.add(c2);
        or_pcookies.add(c3);
        or_pcookies.add(c4);
        
                        // -------------- //
                        // Cadenas Largas //
                        // -------------- //

        // -------------- //
        // VARIANTES EN Y //
        // -------------- //
        for ( j = 29, i = 0; i < 24; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 89, j, 3, 3, nCookie, sCookie));
        }

        for ( j = 29, i = 0; i < 24; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 284, j, 3, 3, nCookie, sCookie));
        }

        // -------------- //
        // VARIANTES EN X //
        // -------------- //
        for ( j = 29, i = 0; i < 22; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 419, 3, 3, nCookie, sCookie));
        }
        for ( j = 104, i = 0; i < 12; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 89, 3, 3, nCookie, sCookie));
        }

                        // -------------- //
                        // Cadenas cortas //
                        // -------------- //

        // -------------- //
        // VARIANTES EN X //
        // -------------- //

        for ( j = 44, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 29, 3, 3, nCookie, sCookie));
        }
        for ( j = 44, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 89, 3, 3, nCookie, sCookie));
        }
        for ( j = 44, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 134, 3, 3, nCookie, sCookie));
        }
        for ( j = 44, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 284, 3, 3, nCookie, sCookie));
        }
        for ( j = 44, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 374, 3, 3, nCookie, sCookie));
        }
        for ( j = 104, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 359, 3, 3, nCookie, sCookie));
        }
        for ( j = 104, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 359, 3, 3, nCookie, sCookie));
        }
        for ( j = 104, i = 0; i < 5; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 29, 3, 3, nCookie, sCookie));
        }
        for ( j = 149, i = 0; i < 2; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 134, 3, 3, nCookie, sCookie));
        }
        for ( j = 209, i = 0; i < 2; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 134, 3, 3, nCookie, sCookie));
        }
        for ( j = 209, i = 0; i < 5; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 29, 3, 3, nCookie, sCookie));
        }
        for ( j = 299, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 29, 3, 3, nCookie, sCookie));
        }
        for ( j = 299, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 89, 3, 3, nCookie, sCookie));
        }
        for ( j = 299, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 134, 3, 3, nCookie, sCookie));
        }
        for ( j = 299, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 284, 3, 3, nCookie, sCookie));
        }
        for ( j = 299, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 374, 3, 3, nCookie, sCookie));
        }
        for ( j = 239, i = 0; i < 4; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 359, 3, 3, nCookie, sCookie));
        }
        for ( j = 104, i = 0; i < 4; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 314, 3, 3, nCookie, sCookie));
        }
        for ( j = 224, i = 0; i < 4; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, j, 314, 3, 3, nCookie, sCookie));
        }
        // -------------- //
        // VARIANTES EN Y //
        // -------------- //
        for ( j = 29, i = 0; i < 2; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 29, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 29, i = 0; i < 2; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 344, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 44, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 164, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 44, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 209, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 74, i = 0; i < 5; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 29, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 74, i = 0; i < 5; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 344, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 104, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 134, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 104, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 239, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 284, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 29, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 284, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 344, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 314, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 209, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 314, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 164, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 329, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 44, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 329, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 329, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 374, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 29, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 374, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 344, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 374, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 134, j, 3, 3, nCookie, sCookie));
        }
        for ( j = 374, i = 0; i < 3; i++, j+=15 ) {
            or_cookies.add(new Cookie(Cookie.NORMAL, 239, j, 3, 3, nCookie, sCookie));
        }

        restore();
    }

    public void restore() {
        
        pacman.restore();
        pink.restore();
        cyan.restore();
        orange.restore();
        red.restore();
        
        for(Cookie c : or_cookies) {
            if(!cookies.contains(c)) {
                add(c);
                cookies.add(c);
            }
        }
        for(Cookie c : or_pcookies) {
            if(!cookies.contains(c)) {
                add(c);
                pcookies.add(c);
                cookies.add(c);
            }
        }
    }

    public final void addWall(Wall w) {
        add(w);
        walls.add(w);
        w.setRounded(true, 10);
    }
}
