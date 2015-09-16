package core.geom;

import core.Model;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

/**
 *
 * @author Mithusayel Murmu
 */
public class Line extends Shapes {
    private Line2D line;
    
    private Rectangle prevRect;
    
    public Line(Point start, Point end) {
        type = Model.TOOLS.LINE;
        
        line = new Line2D.Float();
        
        resize(start.x, start.y, end.x, end.y);
    }
    
    private Line() {
        type = Model.TOOLS.LINE;
    }
    
    @Override
    public void resetInverse() {
        prevRect = null;
    }
    
    public int getX1() { return (int)line.getX1(); }
    public int getY1() { return (int)line.getY1(); }
    public int getX2() { return (int)line.getX2(); }
    public int getY2() { return (int)line.getY2(); }
    public int getCX() { return (getX1() + getX2()) / 2; }
    public int getCY() { return (getY1() + getY2()) / 2; }
    
    @Override
    public Rectangle getBounds() {
        return line.getBounds();
    }

    @Override
    public Line2D getShape() {
        return line;
    }

    @Override
    public void modify(Point start, Point end) {
        resize(start.x, start.y, end.x, end.y);
    }

    @Override
    public void translate(int dx, int dy) {
        line.setLine(getX1() + dx, getY1() + dy, getX2() + dx, getY2() + dy);
    }
    
    @Override
    public void paint(Graphics2D g2) {
        g2.draw(line);
    }

    @Override
    public final void resize(int x1, int y1, int x2, int y2) {
        if(x2 < x1 || y2 < y1)
            line.setLine(x2, y2, x1, y1);
        else
            line.setLine(x1, y1, x2, y2);
    }

    @Override
    public void rotate(int x, int y, int cx, int cy) {}
    
    @Override
    public Line clone() {
        Line ln = new Line();
        ln.setAntialias(antialias);
        ln.setFill(fill);
        ln.setColor(color);
        ln.setStroke(stroke);
        
        ln.line = (Line2D)line.clone();
        
        return ln;
    }

    @Override
    public void resize(int dx, int dy, int cx, int cy, Rectangle r) {}

    @Override
    public void hflip() {
        if(prevRect == null) {
            prevRect = getBounds();
        }
        
        int cx = (int)prevRect.getCenterX();
        
        int p1x = 2 * cx - getX1();
        int p2x = 2 * cx - getX2();
        resize(p1x, getY1(), p2x, getY2());
    }

    @Override
    public void vflip() {
        if(prevRect == null) {
            prevRect = getBounds();
        }
        
        int cy = (int)prevRect.getCenterY();
        
        int p1y = 2 * cy - getY1();
        int p2y = 2 * cy - getY2();
        resize(getX1(), p1y, getX2(), p2y);
    }

    @Override
    public void rot_left() {}

    @Override
    public void rot_right() {}
}
