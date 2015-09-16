package core.geom.tools;

import core.Model;
import core.geom.Shapes;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;

/**
 *
 * @author Mithusayel Murmu
 */
public class Eraser extends Shapes {
    
    private Color boundaryColor;
    private BasicStroke eraserStroke;
    
    private GeneralPath path;
    
    /**
     * state = 0 (Mouse Dragged)
     * state = 1 (Mouse Released)
     */
    private int state;
    
    public Eraser(Point start) {
        path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        path.moveTo(start.x, start.y);
        
        boundaryColor = new Color(~Model.getPadColor().getRGB());
        eraserStroke = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                2f, new float[]{3f}, 10f);
        
        state = 0;
    }

    private Eraser() {}
    
    @Override
    public void resetInverse() {}
    
    public void done() {
        state = 1;
        path.closePath();
        setFill(true);
    }
    
    @Override
    public void setColor(Color c) {
        color = Model.getPadColor();
    }

    @Override
    public GeneralPath getShape() {
        return path;
    }

    @Override
    public void modify(Point start, Point end) {
        path.lineTo(end.x, end.y);
    }

    @Override
    public void translate(int x, int y) {}

    @Override
    public void rotate(int x, int y, int cx, int cy) {}

    @Override
    public void resize(int dx, int dy, int cx, int cy) {}

    @Override
    public Rectangle getBounds() {
        return path.getBounds();
    }

    @Override
    public Eraser clone() {
        Eraser er = new Eraser();
        er.path = (GeneralPath)path.clone();
        er.color = Model.getPadColor();
        er.state = state;
        er.eraserStroke = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                2f, new float[]{3f}, 10f);
        er.boundaryColor = boundaryColor;
        
        return er;
    }
    
    @Override
    public void paint(Graphics2D g2) {
        if(state == 0) {
            g2.setStroke(eraserStroke);
            g2.setColor(boundaryColor);
            g2.draw(path);
        } else {
            g2.setColor(color);
            g2.fill(path);
        }
    }

    @Override
    public void resize(int dx, int dy, int cx, int cy, Rectangle r) {}

    @Override
    public void hflip() {}

    @Override
    public void vflip() {}

    @Override
    public void rot_left() {}

    @Override
    public void rot_right() {}
}
