package core.shape_interface.editor.geom;

import core.geom.Line;
import core.shape_interface.editor.Control;
import core.shape_interface.editor.ShapeEditor;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 *
 * @author Mithusayel Murmu
 */
public class LineShapeEditor extends ShapeEditor {
    private Line shape;
    
    public LineShapeEditor(Line shape) {
        super();
        
        this.shape = shape;
        createControlPoints();
    }

    @Override
    public final void createControlPoints() {
        ctrl = new Control[3];
        
        ctrl[0] = new Control(shape.getX1(), shape.getY1(), Control.CONTROL_LOCS.P1);
        ctrl[1] = new Control(shape.getX2(), shape.getY2(), Control.CONTROL_LOCS.P2);
        ctrl[2] = new Control(shape.getCX(), shape.getCY(), Control.CONTROL_LOCS.CEN);
    }

    @Override
    public void dragControl(MouseEvent e) {
        curPoint = e.getPoint();
        
        int ddx = curPoint.x - lastPoint.x;
        int ddy = curPoint.y - lastPoint.y;
        
        switch (ctrl[idx].type) {
            case CEN: {
                shape.translate(ddx, ddy);
                break;
            }
                
            case P1: {
                shape.resize(curPoint.x, curPoint.y, shape.getX2(), shape.getY2());
                break;
            }
                
            case P2: {
                shape.resize(curPoint.x, curPoint.y, shape.getX1(), shape.getY1());
            }
        }
        
        updateControlPositions();
        lastPoint = curPoint;
        
        /*
         * As the shape is being dragged, it keeps
         * changing its respective P1 and P2 points.
         * To keep track of which point is being is pressed,
         * we need to continuously update the index of the 
         * selected control, after every drag.
         */
        idx = determineSelectedControl(e.getPoint());
    }

    @Override
    public void paintEditLines(Graphics2D g) {
        for (int i = ctrl.length - 1; i >= 0; i--) {
            ctrl[i].paint(g);
        }
    }

    @Override
    public void pressControl(Point p) {
        idx = determineSelectedControl(p);

        shape.resetInverse();
        lastPoint = p;
        curPoint = p;
    }

    @Override
    protected int determineSelectedControl(Point p) {
        int ind = -1;

        for (int i = 0; i < ctrl.length; i++) {
            if (ctrl[i].contains(p)) {
                ind = i;
                break;
            }
        }
        
        return ind;
    }

    @Override
    public void updateControlPositions() {
        ctrl[0].updateBounds(shape.getX1(), shape.getY1());
        ctrl[1].updateBounds(shape.getX2(), shape.getY2());
        ctrl[2].updateBounds(shape.getCX(), shape.getCY());

    }

    @Override
    public Cursor getCursor(Point p) {
        for (int i = 0; i < ctrl.length; i++) {
            if (ctrl[i].contains(p)) {
                return ctrl[i].getControlCursor();
            }
        }
        
        return Cursor.getDefaultCursor();
    }
}
