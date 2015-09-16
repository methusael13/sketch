package core.shape_interface.editor.geom;

import core.Model;
import core.geom.Curve;
import core.shape_interface.editor.Control;
import core.shape_interface.editor.ShapeEditor;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 *
 * @author Mithusayel Murmu
 */
public class CurveShapeEditor extends ShapeEditor {
    private Curve shape;

    private Color boundColor;;
    
    public CurveShapeEditor(Curve shape) {
        super();
        
        this.shape = shape;
        boundColor = new Color(~Model.getPadColor().getRGB());
        
        createControlPoints();
    }

    @Override
    public final void createControlPoints() {
        ctrl = new Control[5];
        
        ctrl[0] = new Control(shape.getX1(), shape.getY1(), Control.CONTROL_LOCS.P1);
        ctrl[1] = new Control(shape.getX2(), shape.getY2(), Control.CONTROL_LOCS.P2);
        ctrl[2] = new Control(shape.getCtrlX1(), shape.getCtrlY1(), Control.CONTROL_LOCS.C1);
        ctrl[3] = new Control(shape.getCtrlX2(), shape.getCtrlY2(), Control.CONTROL_LOCS.C2);
        ctrl[4] = new Control(shape.getCX(), shape.getCY(), Control.CONTROL_LOCS.CEN);
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
                shape.setPoint1(e.getX(), e.getY());
                break;
            }
                
            case P2: {
                shape.setPoint2(e.getX(), e.getY());
                break;
            }
                
            case C1: {
                shape.setCtrlPoint1(e.getX(), e.getY());
                break;
            }
                
            case C2: {
                shape.setCtrlPoint2(e.getX(), e.getY());
            }
        }
        
        updateControlPositions();
        
        lastPoint = curPoint;
    }

    @Override
    public void paintEditLines(Graphics2D g) {
        g.setColor(boundColor);
        g.setStroke(stroke);
        g.drawLine(shape.getX1(), shape.getY1(), shape.getCtrlX1(), shape.getCtrlY1());
        g.drawLine(shape.getCtrlX1(), shape.getCtrlY1(), shape.getCtrlX2(), shape.getCtrlY2());
        g.drawLine(shape.getCtrlX2(), shape.getCtrlY2(), shape.getX2(), shape.getY2());
        
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
        ctrl[2].updateBounds(shape.getCtrlX1(), shape.getCtrlY1());
        ctrl[3].updateBounds(shape.getCtrlX2(), shape.getCtrlY2());
        ctrl[4].updateBounds(shape.getCX(), shape.getCY());
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
