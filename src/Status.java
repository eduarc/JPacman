/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author arch
 */
interface Status {
    /* Valores para status  de PlayingMode*/
    public static final int ST_MENU        = 0x1;
    public static final int ST_READY       = 0x2;
    public static final int ST_PLAYING     = 0x4;
    public static final int ST_PAUSED      = 0x8;
    public static final int ST_GAMEOVER    = 0x10;
    public static final int ST_ABOUT       = 0x20;
    public static final int ST_HIGH_SCORES = 0x30;
    public static final int ST_WIN         = 0x40;
    public static final int ST_QUIT        = 0x50;
}
