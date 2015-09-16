package core.geom;

import core.Model;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;

/**
 *
 * @author Mithusayel Murmu
 */
public class FreeShape extends Shapes {
    private GeneralPath path;
    
    private double prevAngle;
    private double curAngle;
    
    // First time rotate
    private boolean fRot;
    
    private AffineTransform trans;
    private AffineTransform inverse;
    
    private Rectangle prevRect;
    
    public FreeShape(Point start) {
        type = Model.TOOLS.FREESHAPE;
        
        path = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        path.moveTo(start.x, start.y);
        
        fRot = true;
        prevAngle = curAngle = 0;
        
        trans = AffineTransform.getRotateInstance(0);
    }
    
    private FreeShape() {
        type = Model.TOOLS.FREESHAPE;
        
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
    public GeneralPath getShape() {
        return path;
    }

    @Override
    public void modify(Point p1, Point p2) {
        path.lineTo(p2.x, p2.y);
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
    public void translate(int x, int y) {
        trans.setToTranslation(x, y);
        path.transform(trans);
        trans.setToIdentity();
    }
    
    /**
     * 
     * @param dx : change in width
     * @param dy : change in height
     * @param cx : x point which remains constant
     * @param cy : y point which remains constant
     */
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

    @Override
    public Rectangle getBounds() {
        return path.getBounds();
    }
    
    @Override
    public void paint(Graphics2D g2) {
        if(Model.getMode() == Model.DRAW_MODE)
            g2.draw(path);
        else {
            if(fill)
                g2.fill(path);
            else
                g2.draw(path);
        }
    }
    
    public void resetAngles() {
        prevAngle = curAngle = 0;
        fRot = true;
    }
    
    @Override
    public FreeShape clone() {
        FreeShape fs = new FreeShape();
        fs.setAntialias(antialias);
        fs.setFill(fill);
        fs.setColor(color);
        fs.setStroke(stroke);
        
        fs.path = (GeneralPath)path.clone();
        
        return fs;
    }
}
