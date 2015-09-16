package core.shape_interface.editor;

import core.shape_interface.editor.Control.CONTROL_LOCS;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

/**
 *
 * @author Mithusayel Murmu
 */
public abstract class ShapeEditor {
    
    protected Control[] ctrl;
    
    // For drawing the guidelines
    protected Color COLOR;
    protected BasicStroke stroke;
    
    // Selected control index
    protected int idx;
    
    // the drag_x and drag_y loc during mouse drag
    protected int drag_x;
    protected int drag_y;
    
    protected Point lastPoint;
    protected Point curPoint;
    
    protected boolean dragMode;
    
    public ShapeEditor() {     
        COLOR = new Color(0, 153, 204);
        stroke = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 
                1f, new float[] {4f}, 10f);
    }
    
    public void setDragMode(boolean dm) {
        dragMode = dm;
    }
    
    public boolean dragMode() {
        return dragMode;
    }
    
    public CONTROL_LOCS getSelectedControlType() {
        if(idx > -1 && idx < ctrl.length)
            return ctrl[idx].type;
        
        return null;
    }
    
    public void paint(Graphics2D g) {
        if(g.getRenderingHint(RenderingHints.KEY_ANTIALIASING)
                != RenderingHints.VALUE_ANTIALIAS_ON) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        
        paintEditLines(g);
    }
    
    public abstract void createControlPoints();
    public abstract void updateControlPositions();
    public abstract void dragControl(MouseEvent e);   
    public abstract void paintEditLines(Graphics2D g); 
    public abstract void pressControl(Point p);
   
    public boolean hit(Point p) {
        return (determineSelectedControl(p) != -1);
    }
    
    protected abstract int determineSelectedControl(Point p);
    public abstract Cursor getCursor(Point p);
}
