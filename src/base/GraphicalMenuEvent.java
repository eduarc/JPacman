/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base;
/**
 *
 * @author Castrillo Velilla
 */
public class GraphicalMenuEvent {

    private Object source;

    public GraphicalMenuEvent(Object source) {

        if(source == null)
            throw new IllegalArgumentException("null source");
        this.source  = source;
    }

    public Object getSource() {
        return source;
    }
}