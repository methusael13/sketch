package core.geom.tools;

import core.Model;
import core.shape_interface.PadInterface;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 *
 * @author Mithusayel Murmu
 */
public class EraserInterface extends PadInterface {

    private Eraser eraser;
    
    public EraserInterface(Model model) {
        this.model = model;
    }
    
    @Override
    public void clearShape() {
        eraser = null;
    }
    
    @Override
    public void paint(Graphics2D g2) {}
    
    @Override
    public void mouseDown(MouseEvent e) {
        if (Model.getMode() == Model.IDLE_MODE) {
            model.setMode(Model.DRAW_MODE);

            model.createNewElement(e.getPoint(), e.getPoint());
            eraser = (Eraser) model.getElement();
        }
    }
    
    @Override
    public void mouseDrag(MouseEvent e) {
        if(eraser == null)
            return;
        
        pad.paintTemp();
        
        if(Model.getMode() == Model.DRAW_MODE)
            eraser.modify(null, e.getPoint());
        
        pad.paintTemp();
    }
    
    @Override
    public void mouseUp(MouseEvent e) {       
        if(Model.getMode() == Model.DRAW_MODE) {
            eraser.done();
            model.registerShape(eraser);
            model.setMode(Model.IDLE_MODE);
        }
        pad.repaint();
    }
}
