package core.shape_interface;

import core.Model;
import core.geom.Rect;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 *
 * @author Mithusayel Murmu
 */
public class RectInterface extends PadInterface {
    private Rect shape; 
    
    private Point anchor;
    private Point ext;
    
    public RectInterface(Model model) {
        this.model = model;
    }
    
    @Override
    public void clearShape() {
        shape = null;
        sei = null;
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if (Model.getMode() == Model.IDLE_MODE) {
            model.setMode(Model.DRAW_MODE);
            
            anchor = e.getPoint();
            ext = e.getPoint();

            model.createNewElement(e.getPoint(), e.getPoint());
            shape = (Rect) model.getElement();
        } 
        
        else if(Model.getMode() == Model.EDIT_MODE) {
            if(shape == null)
                shape = (Rect) model.getElement();
            
            sei.mousePressed(e);
        }
    }
    
    @Override
    public void mouseDrag(MouseEvent e) {
        if(shape == null)
            return;
        
        pad.paintTemp();
        ext = e.getPoint();
        
        if(Model.getMode() == Model.DRAW_MODE)
            shape.modify(anchor, ext);
        else if(Model.getMode() == Model.EDIT_MODE)
            sei.mouseDragged(e);
        
        pad.paintTemp();
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
        if (Model.getMode() == Model.DRAW_MODE) {
            model.setMode(Model.EDIT_MODE);
            createEditor();

        } else {
            if(shape != null || sei != null) {
                shape.resetAngles();
                sei.mouseReleased(e);
            }
        }

        pad.repaint();
    }
}
