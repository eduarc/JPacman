/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import base.GameEngine;
import base.GraphicalMenu;
import base.Input;
import base.Stage;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import javax.swing.JFrame;

/**
 *
 * @author arch
 */
public class PacmanGame extends GameEngine implements Direction, Status {

    public static final int SUPER_WIDTH = 420;
    public static final int SUPER_HEIGHT = 540;

    /* Estado del juego */
    int readyDelay = 3000;
    int winDelay = 3000;

    GraphicalMenu menu;
    /* Elementos de juego */
    PlayingMode pmode;
    Pacman pacman;
    DefaultStage defStage;

    JFrame frame;

    public PacmanGame() {
        super(new Dimension(SUPER_WIDTH, SUPER_HEIGHT));

        menu = new GraphicalMenu(GraphicalMenu.VERTICAL,
                                     GraphicalMenu.CENTER,
                                     new Font("auto", Font.BOLD, 32),
                                     new Font("auto", Font.BOLD, 50));
        menu.setColor(menu.FOCUS_COLOR, Color.YELLOW);
        menu.setInterItemSpace(10);
        menu.addItem("New Game");
        menu.addItem("High Scores");
        menu.addItem("About");
        menu.addItem("Exit");

        frame = new JFrame("PACMAN Game");
        frame.setSize(SUPER_WIDTH, SUPER_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(SUPER_WIDTH, SUPER_HEIGHT));
        frame.setMaximumSize(new Dimension(SUPER_WIDTH, SUPER_HEIGHT));
        frame.setLocationRelativeTo(frame);
        frame.add(getDisplay());
        frame.setVisible(true);

        menu.setX((frame.getWidth() - menu.getWidth()) / 2);
        menu.setY((frame.getHeight() - menu.getHeight()) / 2);

        setFPS(60);
        installKeyboard(true);

        /* Configurar GDC para dibujado */
        getGDC().setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
        getGDC().setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        defStage = new DefaultStage();
        pacman = defStage.pacman;
        addAnim(pacman);
        
        for(Ghost g : defStage.ghosts) {
            addAnim(g);
        }
        for(Cookie c : defStage.pcookies) {
            addAnim(c);
        }

        pmode = new PlayingMode(defStage);
        pacman = pmode.pacman;
        pmode.status = ST_MENU;
    }

    /* Procesa los comandos de Juego */
    public void command(Input in) {

        int type = in.getType();
        int b = in.getButton();
        int status = pmode.status;
        
        if(type == Input.T_PRESSED) {

            // teclas de juego
            if(status == ST_PLAYING) {
                
                if(b == KeyEvent.VK_UP) {
                    pacman.setNextMovementDirection(NORTH);
                }
                else if(b == KeyEvent.VK_DOWN) {
                    pacman.setNextMovementDirection(SOUTH);
                }
                if(b == KeyEvent.VK_LEFT) {
                    pacman.setNextMovementDirection(EAST);
                }
                else if(b == KeyEvent.VK_RIGHT) {
                    pacman.setNextMovementDirection(WEST);
                }
                if(b == KeyEvent.VK_P) {
                    status = ST_PAUSED;
                }
                else if(b == KeyEvent.VK_Q) {
                    status = ST_QUIT;
                }
            }
            else if(status == ST_PAUSED) {
                if(b == KeyEvent.VK_P) {
                    status = ST_PLAYING;
                }
            }
            else if(status == ST_READY) {
                if(b == KeyEvent.VK_P) {
                    status = ST_PAUSED;
                }
                else if(b == KeyEvent.VK_Q) {
                    status = ST_QUIT;
                }
            }
            else if(status == ST_QUIT) {
                if(b == KeyEvent.VK_N) {
                    status = ST_PLAYING;
                }
                if(b == KeyEvent.VK_Y) {
                    status = ST_MENU;
                }
            }
            // teclas de menu
            else if(status == ST_MENU) {
                if(b == KeyEvent.VK_UP) {
                    menu.selectPrevious();
                }
                else if(b == KeyEvent.VK_DOWN) {
                    menu.selectNext();
                }
                else if(b == KeyEvent.VK_ENTER) {
                    int index = menu.getSelectedIndex();

                    if(index == 0) {
                            // Decidir que Mapa se elige...
                            // Por ahora el por Defecto
                        defStage.restore();
                        pmode.restore();
                        status = ST_READY;
                    }
                    else if(index == 1) {
                        status = ST_HIGH_SCORES;
                        // HIGH SCORES
                    }
                    else if(index == 2) {
                        status = ST_ABOUT;
                        // ABOUT
                    }
                    else if(index == 3) {
                        System.exit(0);
                    }
                }
            }
            else if(status == ST_HIGH_SCORES) {
                status = ST_MENU;
            }
            else if(status == ST_ABOUT) {
                status = ST_MENU;
            }
        }
        else if(type == Input.T_TYPED) {
            if(status == ST_MENU) {
                //...
            }
        }
        pmode.status = status;
    }

    /* Realizar paso de animacion de juego */
    public void update() {

        int status = pmode.status;

        if(status == ST_PLAYING) {
            if(pmode.lives < 0) {
                readyDelay = 3000;
                status = ST_GAMEOVER;
                // ... mostrar Cuadro de dialogo Puntaje
                status = ST_MENU;
            }
            else if(pmode.cookies.isEmpty()) {
                status = ST_WIN;
                pmode.score += 100;
            }
        }
        else if(status == ST_READY) {
            --readyDelay;
            if(readyDelay >= 0) {
                return;
            }
            status = ST_PLAYING;
        }
        else if(status == ST_WIN) {
            --winDelay;
            if(winDelay >= 0) {
                return;
            }
            winDelay = 3000;
            readyDelay = 3000;

            defStage.restore();
            pmode.level++;
            status = ST_READY;
        }
        pmode.status = status;
    }

    /*
     * Renderiza la escena de juego
     * Esta Ajustado para el unico Stage que existe...
     */
    public void render(Graphics2D g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, SUPER_WIDTH, SUPER_HEIGHT);

        int status = pmode.status;
        
        if(status == ST_PLAYING
                || status == ST_PAUSED
                || status == ST_READY
                || status == ST_WIN
                || status == ST_QUIT) {

            // ---------------------
            // ESTADISTICAS DE JUEGO
            // ---------------------
            Stage st = pmode.stage;
            int dx, dy;
            String msg;
            Rectangle2D r;

            g.setColor(Color.YELLOW);

            dx = 385;
            for(int i = pmode.lives; i > 0; --i) {
                g.fillArc(dx, 10, 15, 15, 45, 270);
                dx -= 16;
            }

            g.setFont(new Font("auto", Font.BOLD, 20));

            msg = "Score: " + pmode.score;
            g.drawString(msg, 20, 500);

            msg = "Level: " + pmode.level;
            g.drawString(msg, 20, 25);
            
                // ESCENARIO
            g.translate(22, 30);
            st.render(g);

            if(status == ST_PAUSED
                    || status == ST_READY
                    || status == ST_QUIT) {

                int stWidth  = st.getWidth();
                int stHeight = st.getHeight();

                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g.setColor(Color.BLACK);
                g.fillRect(0, -2, frame.getWidth(), stHeight + 4);

                if(status == ST_PAUSED) {
                    g.setFont(new Font("auto", Font.BOLD, 30));
                    r = g.getFontMetrics().getStringBounds("PAUSED", g);

                    g.setColor(Color.YELLOW);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                    dx = (stWidth - (int)r.getWidth()) / 2;
                    //dy = ((stHeight - (int)r.getHeight()) / 2) + 2*(int)r.getHeight();
                    g.drawString("PAUSED", dx, 282);
                } else if(status == ST_READY){    // READY
                    g.setFont(new Font("auto", Font.BOLD, 20));
                    r = g.getFontMetrics().getStringBounds("GET READY!", g);

                    g.setColor(Color.YELLOW);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

                    dx = (stWidth - (int)r.getWidth()) / 2;
                    //dy = ((stHeight - (int)r.getHeight()) / 2) + (int)r.getHeight();
                    g.drawString("GET READY!", dx, 278);
                } else {    // QUIT
                    g.setFont(new Font("auto", Font.BOLD, 20));
                    r = g.getFontMetrics().getStringBounds("QUIT? Y/N", g);

                    g.setColor(Color.YELLOW);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

                    dx = (stWidth - (int)r.getWidth()) / 2;
                    //dy = ((stHeight - (int)r.getHeight()) / 2) + (int)r.getHeight();
                    g.drawString("QUIT? Y/N", dx, 278);
                }
            }
        }
        /* Dibujar estadistica de juego */
        else if(status == ST_GAMEOVER) {
            
        }
        /* Dibujar menu de juego */
        else if(status == ST_MENU) {
            menu.setX((frame.getWidth() - menu.getWidth()) / 2);
            menu.setY((frame.getHeight() - menu.getHeight()) / 2);
            menu.paint(g);
        }
        else if(status == ST_ABOUT) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("auto", Font.BOLD, 40));
            g.drawString("About...", 10, 45);

            g.setFont(new Font("auto", Font.BOLD, 25));
            g.drawString("JPacman Game in JAVA", 10, 85);
            g.setFont(new Font("auto", Font.BOLD, 18));
            g.drawString("email: eduarcastrillo@gmail.com", 10, 120);
            g.drawString("Ingenieria de Sistemas", 10, 145);
            g.drawString("Universidad Nacional de Colombia", 10, 170);
            g.drawString("Colombia - 2010", 10, 195);

            g.drawString("Press any key to continue...", 10, 240);
        }
        else if(status == ST_HIGH_SCORES) {
            
        }
    }

    public static void main(String args[]) {
        PacmanGame engine = new PacmanGame();
        engine.startUpdate();
        engine.startPainter();
        engine.startInputs();
        engine.startAllAnims();
    }
}
