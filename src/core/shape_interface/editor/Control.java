package core.shape_interface.editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 *
 * @author Mithusayel Murmu
 */
public class Control extends Rectangle {
    
    public static final int SIZE = 8;
    
    public static enum CONTROL_LOCS
    {
        NE, NO, NW, EA, CEN, WE, SW, 
        SO, SE, P1, P2, C1, C2, C3, ROT
    }
    
    protected Color controlLocColor;
    protected Color controlRotColor;
    protected BasicStroke stroke;
    
    public CONTROL_LOCS type;
    public int cx;
    public int cy;
    
    // The x and y positions must be the center of the 
    // control point.
    public Control(int x, int y, CONTROL_LOCS type) {
        super();
        
        this.type = type;
        controlLocColor = new Color(0xA3F800);
        controlRotColor = new Color(0xBA00FF);
        stroke = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
        
        updateBounds(x, y);
    }
    
    public final void updateBounds(int x, int y) {
        cx = x;
        cy = y;
        setBounds(cx - SIZE/2, cy - SIZE/2, SIZE, SIZE);
    }
    
    public Point getLoc() {
        return new Point(cx, cy);
    }
    
    public void paint(Graphics2D g) {
        g.setStroke(stroke);
        if(g.getRenderingHint(RenderingHints.KEY_ANTIALIASING)
                != RenderingHints.VALUE_ANTIALIAS_ON) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        
        switch(type) {
            case ROT:
            case C1:
            case C2:
            case C3:
                g.setColor(controlRotColor);
                break;
                
            default:
                g.setColor(controlLocColor);
        }
        
        g.fillOval(x, y, SIZE, SIZE);  
        g.setColor(Color.BLACK);
        g.drawOval(x, y, SIZE, SIZE);
    }
    
    public Cursor getControlCursor() {
        Cursor cur = Cursor.getDefaultCursor();
        
        switch (type) {
            case NE:
                cur = Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
                break;
            case NO:
            case SO:
                cur = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
                break;
            case NW:
                cur = Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
                break;
            case WE:
            case EA:
                cur = Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
                break;
            case SE:
                cur = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
                break;
            case SW:
                cur = Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
                break;
            case CEN:
                cur = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
                break;
            case P1:
            case P2:
                cur = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
                break;
            case C1:
            case C2:
            case C3:
            case ROT:
                cur = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        }
        
        return cur;
    }
}
