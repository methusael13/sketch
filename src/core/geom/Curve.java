package core.geom;

import core.Model;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.CubicCurve2D;

/**
 * @TO BE MODIFIED
 * @author Mithusayel Murmu
 */
public final class Curve extends Shapes {
    private CubicCurve2D curve;
    
    private Rectangle prevRect;
    
    public Curve(Point start, Point end) {
        type = Model.TOOLS.CURVE;
        
        curve = new CubicCurve2D.Float();
        modify(start, end);
    }
    
    private Curve() {
        type = Model.TOOLS.CURVE;
    }
    
    @Override
    public void resetInverse() {
        prevRect = null;
    }

    @Override
    public CubicCurve2D getShape() {
        return curve;
    }
    
    public int getX1() { return (int)curve.getX1(); }
    public int getY1() { return (int)curve.getY1(); }
    public int getX2() { return (int)curve.getX2(); }
    public int getY2() { return (int)curve.getY2(); }
    public int getCtrlX1() { return (int)curve.getCtrlX1(); }
    public int getCtrlY1() { return (int)curve.getCtrlY1(); }
    public int getCtrlX2() { return (int)curve.getCtrlX2(); }
    public int getCtrlY2() { return (int)curve.getCtrlY2(); }
    public int getCX() { return (int)getBounds().getCenterX(); }
    public int getCY() { return (int)getBounds().getCenterY(); }

    @Override
    public void modify(Point p1, Point p2) {
        resize(p1.x, p1.y, p2.x, p2.y);
    }
    
    public void setCtrlPoint1(int x1, int y1) {
        curve.setCurve(getX1(), getY1(), x1, y1, getCtrlX2(), getCtrlY2(), getX2(), getY2());
    }
    
    public void setCtrlPoint2(int x2, int y2) {
        curve.setCurve(getX1(), getY1(), getCtrlX1(), getCtrlY1(), x2, y2, getX2(), getY2());
    }
    
    public void setPoint1(int x1, int y1) {
        curve.setCurve(x1, y1, getCtrlX1(), getCtrlY1(), getCtrlX2(), getCtrlY2(), getX2(), getY2());
    }
    
    public void setPoint2(int x2, int y2) {
        curve.setCurve(getX1(), getY1(), getCtrlX1(), getCtrlY1(), getCtrlX2(), getCtrlY2(), x2, y2);
    }

    @Override
    public void translate(int dx, int dy) {
        curve.setCurve(getX1() + dx, getY1() + dy, getCtrlX1() + dx, getCtrlY1() + dy, 
                getCtrlX2() + dx, getCtrlY2() + dy, getX2() + dx, getY2() + dy);
    }

    @Override
    public Rectangle getBounds() {
        return curve.getBounds();
    }
    
    @Override
    public void paint(Graphics2D g2) {
        g2.draw(curve);
    }

    @Override
    public void rotate(int x1, int y1, int cx, int cy) {}

    /**
     * Should be called only in DRAW_MODE
     * @param x1
     * @param y1
     * @param x2
     * @param y2 
     */
    @Override
    public void resize(int x1, int y1, int x2, int y2) {
        int cx1 = (3 * x1 + x2) / 4;
        int cy1 = (3 * y1 + y2) / 4;
        int cx2 = (x1 + 3 * x2) / 4;
        int cy2 = (y1 + 3 * y2) / 4;

        curve.setCurve(x1, y1, cx1, cy1, cx2, cy2, x2, y2);
    }
    
    @Override
    public Curve clone() {
        Curve c = new Curve();
        c.setAntialias(antialias);
        c.setFill(fill);
        c.setColor(color);
        c.setStroke(stroke);
        
        c.curve = (CubicCurve2D)curve.clone();
        return c;
    }

    @Override
    public void resize(int dx, int dy, int cx, int cy, Rectangle r) {}

    @Override
    public void hflip() {
        if(prevRect == null)
            prevRect = getBounds();
        
        int cx = (int)prevRect.getCenterX();
        
        int p1x = 2 * cx - getX1();
        int p2x = 2 * cx - getX2();
        int ctx1 = 2 * cx - getCtrlX1();
        int ctx2 = 2 * cx - getCtrlX2();
        
        curve.setCurve(p1x, getY1(), ctx1, getCtrlY1(), ctx2, getCtrlY2(), p2x, getY2());
    }

    @Override
    public void vflip() {
        if(prevRect == null)
            prevRect = getBounds();
        
        int cy = (int)prevRect.getCenterY();
        
        int p1y = 2 * cy - getY1();
        int p2y = 2 * cy - getY2();
        int cty1 = 2 * cy - getCtrlY1();
        int cty2 = 2 * cy - getCtrlY2();
        
        curve.setCurve(getX1(), p1y, getCtrlX1(), cty1, getCtrlX2(), cty2, getX2(), p2y);
    }

    @Override
    public void rot_left() {}

    @Override
    public void rot_right() {}
}
