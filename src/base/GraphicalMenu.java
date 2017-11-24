/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Eduar Castrillo
 */
public class GraphicalMenu implements KeyListener {

    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;
    public static final int TOP = 3;
    public static final int BOTTOM = 4;
    public static final int HORIZONTAL = 5;
    public static final int VERTICAL = 6;
    public static final int BACKGROUND_COLOR = 7;
    public static final int FOREGROUND_COLOR = 8;
    public static final int FOCUS_COLOR = 9;
    private int x, y;
    private Font font, selFont;
    private ArrayList<Item> items;
    private int index;
    private int interSpace;
    private int alignment;
    private int orientation;
    private Color bgColor;
    private Color fgColor;
    private Color fcColor;

    private ArrayList<GraphicalMenuListener> listeners;
    
    public GraphicalMenu(int ori, int alig, Font f, Font sf) {

        bgColor = Color.BLACK;
        fgColor = Color.GRAY;
        fcColor = Color.WHITE;

        setOrientation(ori);
        setAlignment(alig);
        setFont(f);
        setSelectedFont(sf);

        items = new ArrayList(0);
        listeners = new ArrayList(0);
    }

    public void addGraphicalMenuListener(GraphicalMenuListener pl) {
        listeners.add(pl);
    }

    public void removeGraphicalMenuListener(GraphicalMenuListener pl) {
        listeners.remove(pl);
    }

    public void addItem(String text) {

        Item i = new Item(text);
        if (!items.contains(i)) {
            items.add(i);
        }
    }

    public void removeItem(String text) {

        Item i = new Item(text);
        items.remove(i);
    }

    public void setSelectedIndex(int i) {

        if (i >= 0 && i < items.size()) {
            index = i;
        }
    }

    public void setSelectedItem(String text) {

        Item i = new Item(text);
        if (items.contains(i)) {
            setSelectedIndex(items.indexOf(i));
        }
    }

    public void setInterItemSpace(int i) {
        interSpace = (i < 0) ? 0 : i;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setColor(int field, Color c) {

        switch (field) {
            case BACKGROUND_COLOR:
                bgColor = c;
                break;
            case FOREGROUND_COLOR:
                fgColor = c;
                break;
            case FOCUS_COLOR:
                fcColor = c;
                break;
            default:
                throw new IllegalArgumentException("Invalid color field: " + field);
        }
    }

    public final void setOrientation(int o) {

        orientation = HORIZONTAL;
        if (o == VERTICAL) {
            orientation = o;
        }
    }

    public final void setAlignment(int alig) {

        alignment = CENTER; // Default

        if (orientation == HORIZONTAL) {
            switch (alig) {
                case TOP:
                case BOTTOM:
                    alignment = alig;
            }
        } else if (orientation == VERTICAL) {
            switch (alig) {
                case LEFT:
                case RIGHT:
                    alignment = alig;
            }
        }
    }

    public final void setFont(Font f) {
        font = f;
    }

    public final void setSelectedFont(Font f) {
        selFont = f;
    }

    public int getSelectedIndex() {
        return index;
    }
    
    public int getInterItemSpace() {
        return interSpace;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {

        if (orientation == VERTICAL) {
            return maxWidth();
        } else {
            int w = 0;
            for (int i = items.size() - 1; i >= 0; --i) {
                w += items.get(i).width;
            }
            return w += ((items.size() - 1) * interSpace);
        }
    }

    public int getHeight() {

        if (orientation == HORIZONTAL) {
            return maxHeight();
        } else {
            int h = 0;
            for (int i = items.size() - 1; i >= 0; --i) {
                h += items.get(i).height;
            }
            return h += ((items.size() - 1) * interSpace);
        }
    }

    public Color getColor(int field) {

        switch (field) {
            case BACKGROUND_COLOR:
                return bgColor;
            case FOREGROUND_COLOR:
                return fgColor;
            case FOCUS_COLOR:
                return fcColor;
        }
        throw new IllegalArgumentException("Invalid color field: " + field);
    }

    public int getOrientation() {
        return orientation;
    }

    public int getAlignment() {
        return alignment;
    }

    public void selectNext() {
        
        if(index == items.size() - 1) {
            index = 0;
            return;
        }
        ++index;
    }

    public void selectPrevious() {

        if(index == 0) {
            index = items.size() - 1;
            return;
        }
        --index;
    }

    public void keyTyped(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            for (int i = listeners.size() - 1; i > 0; --i) {
                listeners.get(i).actionPerformed(new GraphicalMenuEvent(this));
            }
        }
    }

    public void keyPressed(KeyEvent e) {

        boolean throwEvent = false;
        int key = e.getKeyCode();

        if(orientation == VERTICAL) {
            if(key == KeyEvent.VK_UP) {
                selectPrevious();
            }
            else if(key == KeyEvent.VK_DOWN) {
                selectNext();
            }
            throwEvent = true;
        } else {    // HORIZONTAL
            if(key == KeyEvent.VK_LEFT) {
                selectPrevious();
            }
            else if(key == KeyEvent.VK_RIGHT) {
                selectNext();
            }
            throwEvent = true;
        }
        // Lanzar evento
        if (throwEvent) {
            for (int i = listeners.size() - 1; i > 0; --i) {
                listeners.get(i).itemSelectedChanged(new GraphicalMenuEvent(this));
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        // Vacio!!
    }

    public void paint(Graphics2D g) {

        Item item;
        int acumulateSpace = 0;
        int iy, ix;

        if (orientation == VERTICAL) {
            int itemW, rw;
            int maxW = maxWidth();

            if (alignment == CENTER) {
                for (int i = 0; i < items.size(); ++i) {

                    item = items.get(i);
                    itemW = item.width;
                    rw = maxW - itemW;
                    iy = y + acumulateSpace;

                    ix = x + rw / 2;
                    paintItem(g, item, ix, iy);
                    acumulateSpace += item.height + interSpace;
                }
            } else if (alignment == LEFT) {
                for (int i = 0; i < items.size(); ++i) {

                    item = items.get(i);
                    itemW = item.width;
                    rw = maxW - itemW;
                    iy = y + acumulateSpace;

                    paintItem(g, item, x, iy);
                    acumulateSpace += item.height + interSpace;
                }
            } else if (alignment == RIGHT) {
                for (int i = 0; i < items.size(); ++i) {

                    item = items.get(i);
                    itemW = item.width;
                    rw = maxW - itemW;
                    iy = y + acumulateSpace;

                    paintItem(g, item, x + rw, iy);
                    acumulateSpace += item.height + interSpace;
                }
            }
        } else {    // HORIZONTAL
            int itemH, rh;
            int maxH = maxHeight();

            if (alignment == CENTER) {
                for (int i = 0; i < items.size(); ++i) {

                    item = items.get(i);
                    itemH = item.height;
                    rh = maxH - itemH;
                    ix = x + acumulateSpace;

                    iy = y + rh / 2;
                    paintItem(g, item, ix, iy);
                    acumulateSpace += item.width + interSpace;
                }
            } else if (alignment == TOP) {
                for (int i = 0; i < items.size(); ++i) {

                    item = items.get(i);
                    itemH = item.height;
                    rh = maxH - itemH;
                    ix = x + acumulateSpace;

                    paintItem(g, item, ix, y);
                    acumulateSpace += item.width + interSpace;
                }
            } else if (alignment == BOTTOM) {
                for (int i = 0; i < items.size(); ++i) {

                    item = items.get(i);
                    itemH = item.height;
                    rh = maxH - itemH;
                    ix = x + acumulateSpace;

                    paintItem(g, item, ix, y + rh);
                    acumulateSpace += item.width + interSpace;
                }
            }
        }
    }

    private void paintItem(Graphics2D g, Item item, int x, int y) {

        int w = item.width;
        int h = item.height;

        g.setColor(bgColor);
        g.fillRect(x, y, w, h);

        if (items.get(index) != item) {
            g.setColor(fgColor);
            g.setFont(font);
        } else {
            g.setColor(fcColor);
            g.setFont(selFont);
        }
        g.drawString(item.text, x, y + ((3*h)/4));
    }

    private int maxWidth() {

        Item it = items.get(items.size() - 1);
        getDimension(it);
        int width = it.width;
        int w;
        for (int i = items.size() - 2; i >= 0; --i) {
            it = items.get(i);
            getDimension(it);
            w = it.width;
            if (width < w) {
                width = w;
            }
        }
        return width;
    }

    private int maxHeight() {

        Item it = items.get(items.size() - 1);
        getDimension(it);
        int height = it.height;
        int h;
        for (int i = items.size() - 2; i >= 0; --i) {
            it = items.get(i);
            getDimension(it);
            h = it.height;
            if (height < h) {
                height = h;
            }
        }
        return height;
    }

    private void getDimension(Item it) {

        Rectangle2D r = null;
        if(items.get(index) == it) {
            r = selFont.getStringBounds(it.text, new FontRenderContext(null, true, true));
        } else {
            r = font.getStringBounds(it.text, new FontRenderContext(null, true, true));
        }
        it.width = (int) r.getWidth();
        it.height = (int) r.getHeight();
    }

    private class Item {

        String text;
        int width;
        int height;

        public Item(String t) {
            text = t;
        }

        public boolean equals(Object o) {
            
            if(o instanceof Item) {
                Item it = (Item)o;
                return text.equals(it.text);
            }
            return false;
        }
    }
}
