package core.shape_interface;

import core.Model;
import core.geom.FreeShape;
import java.awt.event.MouseEvent;

/**
 *
 * @author Mithusayel Murmu
 */
public class FreeShapeInterface extends PadInterface {

    private FreeShape shape;
    
    public FreeShapeInterface(Model model) {
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

            model.createNewElement(e.getPoint(), e.getPoint());
            shape = (FreeShape) model.getElement();
        } 
        
        else if(Model.getMode() == Model.EDIT_MODE) {
            if(shape == null)
                shape = (FreeShape) model.getElement();
            
            sei.mousePressed(e);
        }
    }
    
    @Override
    public void mouseDrag(MouseEvent e) {
        if(shape == null)
            return;
        
        pad.paintTemp();
        
        if(Model.getMode() == Model.DRAW_MODE)
            shape.modify(null, e.getPoint());
        else if(Model.getMode() == Model.EDIT_MODE)
            sei.mouseDragged(e);
        
        pad.paintTemp();
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
        if(Model.getMode() == Model.DRAW_MODE) {
            if(model.getFill()) {
                shape.getShape().closePath();
            }
            model.setMode(Model.EDIT_MODE);
            createEditor();
        } else {
            if(shape != null && sei != null) {
                shape.resetAngles();
                sei.mouseReleased(e);
            }
        }
        pad.repaint();
    }
}
