package core.shape_interface.editor.geom;

import core.Model;
import core.geom.Shapes;
import core.shape_interface.editor.Control;
import core.shape_interface.editor.ShapeEditor;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 *
 * @author Mithusayel Murmu
 */
public class BoxShapeEditor extends ShapeEditor {
    protected Shapes boundShape;
    
    private Color rotLineColor;
    
    /*
     * we strore the center location of the
     * shape during rotation of the shape to keep it steady
     * in its place. These values are initialized on every mouse press.
     */
    protected int rot_cx;
    protected int rot_cy;
    protected Point anchor;
    protected Rectangle prevState;
    
    public BoxShapeEditor(Shapes shape) {
        super();
        
        boundShape = shape;
        rotLineColor = new Color(~Model.getPadColor().getRGB());
        createControlPoints();
    }
    
    @Override
    public void pressControl(Point p) {
        idx = determineSelectedControl(p);

        lastPoint = p;
        curPoint = p;
        
        prevState = boundShape.getBounds();
        rot_cx = (int)prevState.getCenterX();
        rot_cy = (int)prevState.getCenterY();
        
        boundShape.resetInverse();
        
        switch(ctrl[idx].type) {
            case NE:
                anchor = ctrl[5].getLoc();
                break;
            case SE:
                anchor = ctrl[0].getLoc();
                break;
            case NW:
                anchor = ctrl[7].getLoc();
                break;
            case SW:
                anchor = ctrl[2].getLoc();
                break;
            case NO:
                anchor = ctrl[6].getLoc();
                break;
            case SO:
                anchor = ctrl[1].getLoc();
                break;
            case EA:
                anchor = ctrl[3].getLoc();
                break;
            case WE:
                anchor = ctrl[4].getLoc();                
        }
    }
    
    @Override
    public void dragControl(MouseEvent e) {
        curPoint = e.getPoint();
        drag_x = curPoint.x;
        drag_y = curPoint.y;
        
        int ddx = curPoint.x - lastPoint.x;
        int ddy = curPoint.y - lastPoint.y;
        
        int rx = 0;
        int ry = 0;
        if (anchor != null) {
            rx = curPoint.x - anchor.x;
            ry = curPoint.y - anchor.y;
        }
        
        Rectangle r = boundShape.getBounds();
        
        switch(ctrl[idx].type) {
            case NO:
                boundShape.resize(r.width, -ry, anchor.x, anchor.y, prevState);
                break;
            case SO:
                boundShape.resize(r.width, ry, anchor.x, anchor.y, prevState);
                break;
            case WE:
                boundShape.resize(-rx, r.height, anchor.x, anchor.y, prevState);
                break;
            case EA:
                boundShape.resize(rx, r.height, anchor.x, anchor.y, prevState);
                break;
            case NE:
                boundShape.resize(rx, -ry, anchor.x, anchor.y, prevState);
                break;
            case NW:
                boundShape.resize(-rx, -ry, anchor.x, anchor.y, prevState);
                break;
            case SE:
                boundShape.resize(rx, ry, anchor.x, anchor.y, prevState);
                break;
            case SW:
                boundShape.resize(-rx, ry, anchor.x, anchor.y, prevState);
                break;
            case CEN:
                boundShape.translate(ddx, ddy);
                break;
            case ROT:
                boundShape.rotate(drag_x, drag_y, rot_cx, rot_cy);
        }
        
        updateControlPositions();
        
        lastPoint = curPoint;
    }

    @Override
    public final void createControlPoints() {
        if (boundShape == null) {
            throw new IllegalStateException("Unable to create control points for null shape!");
        }

        Rectangle rect = boundShape.getBounds();
        int minx = rect.x;
        int miny = rect.y;
        int maxx = (int) rect.getMaxX();
        int maxy = (int) rect.getMaxY();
        int cx = (int) rect.getCenterX();
        int cy = (int) rect.getCenterY();

        ctrl = new Control[10];
        
        ctrl[0] = new Control(minx, miny, Control.CONTROL_LOCS.NW);
        ctrl[1] = new Control(cx, miny, Control.CONTROL_LOCS.NO);
        ctrl[2] = new Control(maxx, miny, Control.CONTROL_LOCS.NE);
        ctrl[3] = new Control(minx, cy, Control.CONTROL_LOCS.WE);
        ctrl[4] = new Control(maxx, cy, Control.CONTROL_LOCS.EA);
        ctrl[5] = new Control(minx, maxy, Control.CONTROL_LOCS.SW);
        ctrl[6] = new Control(cx, maxy, Control.CONTROL_LOCS.SO);
        ctrl[7] = new Control(maxx, maxy, Control.CONTROL_LOCS.SE);
        ctrl[8] = new Control(cx, cy, Control.CONTROL_LOCS.CEN);
        ctrl[9] = new Control(cx, miny - 20, Control.CONTROL_LOCS.ROT);
    }

    @Override
    public void paintEditLines(Graphics2D g2) {
        Rectangle rect = boundShape.getBounds();
        
        // If Rot control is being dragged
        if(dragMode && idx == 9)
            paintExtras(g2);
        else
            paintNormal(rect, g2);
    }
    
    /**
     * Paint the editor as it would appear normally
     * @param g2 
     */
    private void paintNormal(Rectangle rect, Graphics2D g2) {
        g2.setColor(COLOR);
        g2.setStroke(stroke);

        g2.drawRect(rect.x, rect.y, rect.width, rect.height);
        g2.setColor(rotLineColor);
        g2.drawLine((int)rect.getCenterX(), (int)rect.getMinY(), ctrl[9].cx, ctrl[9].cy);
        
        for (int i = ctrl.length - 1; i >= 0; i--) {
            ctrl[i].paint(g2);
        }
    }
    
    /**
     * Paint the editor when the Rot control is being dragged
     * @param rect
     * @param g2 
     */
    private void paintExtras(Graphics2D g2) {
        g2.setStroke(stroke);
        g2.setColor(rotLineColor);
        g2.drawLine(rot_cx, rot_cy, drag_x, drag_y);
        
        ctrl[8].updateBounds(rot_cx, rot_cy);
        ctrl[8].paint(g2);
        ctrl[9].updateBounds(drag_x, drag_y);
        ctrl[9].paint(g2);
    }

    @Override
    protected int determineSelectedControl(Point p) {
        int ind = -1;
        
        boolean found = false;
        for (int i = 0; i < ctrl.length; i++) {
            if (ctrl[i].contains(p)) {
                ind = i;
                found = true;
                break;
            }
        }

        if (!found && boundShape.getBounds().contains(p)) {
            ind = 8; // The index for center control
        }
        
        return ind;
    }

    @Override
    public void updateControlPositions() {
        Rectangle rect = boundShape.getBounds();
        int minx = rect.x;
        int miny = rect.y;
        int maxx = (int) rect.getMaxX();
        int maxy = (int) rect.getMaxY();
        int cx = (int) rect.getCenterX();
        int cy = (int) rect.getCenterY();

        ctrl[0].updateBounds(minx, miny);
        ctrl[1].updateBounds(cx, miny);
        ctrl[2].updateBounds(maxx, miny);
        ctrl[3].updateBounds(minx, cy);
        ctrl[4].updateBounds(maxx, cy);
        ctrl[5].updateBounds(minx, maxy);
        ctrl[6].updateBounds(cx, maxy);
        ctrl[7].updateBounds(maxx, maxy);
        ctrl[8].updateBounds(cx, cy);
        ctrl[9].updateBounds(cx, miny - 20);
    }

    @Override
    public Cursor getCursor(Point p) {
        for (int i = 0; i < ctrl.length; i++) {
            if (ctrl[i].contains(p)) {
                return ctrl[i].getControlCursor();
            }
        }

        /* If the above loop had found something
         * we wouldn't have been here. But since the loop
         * terminated without returning, it mustn't have found
         * anything. So, we set the center cursor to be returned instead.
         */

        if (boundShape.getBounds().contains(p)) {
            return ctrl[8].getControlCursor();
        }
        
        return Cursor.getDefaultCursor();
    }
}
