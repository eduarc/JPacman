
import base.Input;
import base.GameEngine;
import base.GraphicalMenu;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author debian
 */
public class TestGameEngine extends GameEngine {

    static int T_WIDTH = 640;
    static int T_HEIGHT = 480;

    int MAX_SKIPS = 5;
    int skips = MAX_SKIPS;

    Image dragon;
    Image ball;
    Image bg;
    int ballX = 10, ballY = 10, bx = 1, by = 1;
    int dragonX, dragonY, dx, dy;

    GraphicalMenu mainMenu;

    public TestGameEngine() {
        super(new Dimension(T_WIDTH, T_HEIGHT));

        dragon = new ImageIcon(getClass().getResource("dragon.png")).getImage();
        ball = new ImageIcon(getClass().getResource("ball.png")).getImage();
        bg = new ImageIcon(getClass().getResource("bg.png")).getImage();

        /*mainMenu = new GraphicalMenu(GraphicalMenu.VERTICAL,
                                     GraphicalMenu.CENTER,
                                     new Font("anorexia", Font.BOLD, 32));

        mainMenu.setInterItemSpace(10);
        mainMenu.addItem("New Game");
        mainMenu.addItem("High Scores");
        mainMenu.addItem("About");
        mainMenu.addItem("Exit");*/
    }

    public void command(Input in) {

        int type = in.getType();
        int b = in.getButton();

        if (type == Input.T_PRESSED) {

            if (b == KeyEvent.VK_UP) {
                dy = -1;
            }
            else if (b == KeyEvent.VK_DOWN) {
                dy = 1;
            }
            else if (b == KeyEvent.VK_LEFT) {
                dx = -1;
            }
            else if (b == KeyEvent.VK_RIGHT) {
                dx = 1;
            }
        } 
        else if (type == Input.T_RELEASED) {

            if (b == KeyEvent.VK_UP
                    || b == KeyEvent.VK_DOWN)
            {
                dy = 0;
            } else if (b == KeyEvent.VK_LEFT
                    || b == KeyEvent.VK_RIGHT)
            {
                dx = 0;
            }
        }
    }

    public void update() {

        --skips;
        if(skips != 0) {
            return;
        }
        skips = MAX_SKIPS;

        if (ballY == 0 || ballY == T_WIDTH - 260) {
            by = -by;
        }
        if (ballX == 0 || ballX == T_HEIGHT + 40) {
            bx = -bx;
        }
        ballY += by;
        ballX += bx;

        dragonY += dy;
        dragonX += dx;
    }

    public void render(Graphics2D gdc) {

        gdc.drawImage(bg, 0, 0, 640, 480, null);
        gdc.drawImage(ball, ballX, ballY, null);
        gdc.drawImage(dragon, dragonX, dragonY, null);
    }

    /* METODO MAIN */
    public static void main(String args[]) {

        TestGameEngine engine = new TestGameEngine();
        engine.setFPS(60);
        engine.installKeyboard(true);

        JFrame frame = new JFrame("Ball Game");
        frame.setSize(T_WIDTH, T_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(engine.getDisplay());
        frame.setVisible(true);
        engine.start();
    }
}
