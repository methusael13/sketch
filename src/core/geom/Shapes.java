package core.geom;

import core.Model;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;

/**
 *
 * @author Mithusayel Murmu
 */
public abstract class Shapes {
    protected Color color;
    protected Stroke stroke;
    
    protected boolean fill;
    protected boolean antialias;
    
    /* This lock prevents the 
     * shape from being affected by key presses
     * when it is being edited(drag in EDIT mode) or drawn.
     */
    private boolean shapeLocked = false;
    private boolean isCopy = false;
    protected Model.TOOLS type;
    
    public abstract Shape getShape();
    public abstract Rectangle getBounds();
    
    public abstract void hflip();
    public abstract void vflip();
    public abstract void rot_left();
    public abstract void rot_right();
    public abstract void modify(Point start, Point end);
    public abstract void translate(int x, int y);
    public abstract void rotate(int x, int y, int cx, int cy);
    public abstract void resize(int dx, int dy, int cx, int cy);
    
    /**
     * 
     * @param dx The width to resize to.
     * @param dy The height to resize to
     * @param cx The anchor point(x) that should remain constant.
     * @param cy The anchor point(y) that should remain constant.
     * @param r The Rectangle object that defines the state of 
     * the shape prior to resize.
     */
    public abstract void resize(int dx, int dy, int cx, int cy, Rectangle r);
    public abstract void resetInverse();
    public abstract void paint(Graphics2D g2);
    
    @Override
    public abstract Shapes clone();
    
    public Color getColor() {
        return color;
    }
    
    public Model.TOOLS getType() {
        return type;
    }
    
    public boolean getFill() {
        return fill;
    }
    
    public boolean getAntialias() {
        return antialias;
    }
    
    public void setCopy(boolean c) {
        isCopy = c;
    }
    
    public boolean isCopy() {
        return isCopy;
    }
    
    public void lockShape() {
        shapeLocked = true;
    }
    
    public void releaseShape() {
        shapeLocked = false;
    }
    
    public boolean locked() {
        return shapeLocked;
    }
    
    public void setFill(boolean f) {
        fill = f;
    }
    
    public void setAntialias(boolean a) {
        antialias = a;
    }
    
    public Stroke getStroke() {
        return stroke;
    }
    
    public void setColor(Color c) {
        color = c;
    }
    
    public void setStroke(Stroke bs) {
        stroke = bs;
    }
}
