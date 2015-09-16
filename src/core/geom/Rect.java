package core.geom;

import core.Model;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Mithusayel Murmu
 */
public class Rect extends Shapes {
    private Rectangle2D rect;
    private GeneralPath path;
    
    private double prevAngle;
    private double curAngle;
    
    // First time rotate
    private boolean fRot;
    
    private AffineTransform trans;
    private AffineTransform inverse;
    
    private Rectangle prevRect;
    
    public Rect(Point start, Point end) {
        type = Model.TOOLS.RECT;
        
        rect = new Rectangle2D.Float();
        rect.setFrame(Math.min(start.x, end.x), 
                Math.min(start.y, end.y), 
                Math.abs(start.x - end.x),
                Math.abs(start.y - end.y));
        
        path = new GeneralPath();
        path.append(rect, false);
        
        fRot = true;
        prevAngle = curAngle = 0;
        
        trans = AffineTransform.getRotateInstance(0);
    }
    
    private Rect() {
        type = Model.TOOLS.RECT;
        
        fRot = true;
        prevAngle = curAngle = 0;
        
        trans = AffineTransform.getRotateInstance(0);
    }
    
    @Override
    public void resetInverse() {
        inverse = null;
        prevRect = null;
    }
    
    @Override
    public Rectangle getBounds() {
        return path.getBounds();
    }

    @Override
    public GeneralPath getShape() {
        return path;
    }

    @Override
    public void modify(Point start, Point end) {
        rect.setFrame(Math.min(start.x, end.x), 
                Math.min(start.y, end.y), 
                Math.abs(start.x - end.x),
                Math.abs(start.y - end.y));
        
        path.reset();
        path.append(rect, false);
    }
    
    @Override
    public void hflip() {
        if(prevRect == null)
            prevRect = getBounds();
        
        int cx = (int)prevRect.getCenterX();
        int cy = (int)prevRect.getCenterY();
        
        translate(-cx, -cy);
        trans.setToScale(-1, 1);
        path.transform(trans);
        translate(cx, cy);
    }
    
    @Override
    public void vflip() {
        if(prevRect == null)
            prevRect = getBounds();
        
        int cx = (int)prevRect.getCenterX();
        int cy = (int)prevRect.getCenterY();
        
        translate(-cx, -cy);
        trans.setToScale(1, -1);
        path.transform(trans);
        translate(cx, cy);
    }
    
    @Override
    public void rot_left() {
        if(prevRect == null)
            prevRect = getBounds();
        
        int cx = (int)prevRect.getCenterX();
        int cy = (int)prevRect.getCenterY();
        
        translate(-cx, -cy);
        trans.setToQuadrantRotation(-1);
        path.transform(trans);
        translate(cx, cy);
    }
    
    @Override
    public void rot_right() {
        if(prevRect == null)
            prevRect = getBounds();
        
        int cx = (int)prevRect.getCenterX();
        int cy = (int)prevRect.getCenterY();
        
        translate(-cx, -cy);
        trans.setToQuadrantRotation(1);
        path.transform(trans);
        translate(cx, cy);
    }

    @Override
    public void translate(int dx, int dy) {
        trans.setToTranslation(dx, dy);
        path.transform(trans);
    }
    
    @Override
    public void resize(int dx, int dy, int cx, int cy) {}
    
    @Override
    public void resize(int dx, int dy, int cx, int cy, Rectangle r) {
        if(dx == 0 || dy == 0)
            return;
        
        double nw = dx / Math.max(r.getWidth(), 1);
        double nh = dy / Math.max(r.getHeight(), 1);
        
        translate(-cx, -cy);
        
        if(inverse != null)
            path.transform(inverse);
        
        trans.setToScale(nw, nh);
        path.transform(trans);
        
        try {
            inverse = trans.createInverse();
        } catch(NoninvertibleTransformException e) {
            inverse = AffineTransform.getTranslateInstance(0, 0);
        }
        
        translate(cx, cy);
    }
    
    @Override
    public void paint(Graphics2D g2) {
        if(fill)
            g2.fill(path);
        else
            g2.draw(path);
    }

    @Override
    public void rotate(int x, int y, int cx, int cy) {
        curAngle = Math.PI/2 - Math.atan2(cy - y, x - cx);
        
        if(fRot) {
            prevAngle = curAngle;
            fRot = false;
        }
        
        trans.setToRotation(curAngle - prevAngle, cx, cy);
        path.transform(trans);
        
        prevAngle = curAngle;
    }
    
    public void resetAngles() {
        prevAngle = curAngle = 0;
        fRot = true;
    }
    
    @Override
    public Rect clone() {
        Rect r = new Rect();
        r.setAntialias(antialias);
        r.setFill(fill);
        r.setColor(color);
        r.setStroke(stroke);
        
        r.path = (GeneralPath)path.clone();
        r.rect = (Rectangle2D)rect.clone();
        
        return r;
    }
}
