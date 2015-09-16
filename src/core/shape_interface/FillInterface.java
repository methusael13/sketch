package core.shape_interface;

import core.Model;
import java.awt.event.MouseEvent;

/**
 *
 * @author Mithusayel Murmu
 */
public class FillInterface extends ActionInterface {    

    public FillInterface(Model model) {
        this.model = model;
    }
    
    @Override
    protected void mouseDown(MouseEvent e) {
        model.performFill(e.getPoint());
    }
}
