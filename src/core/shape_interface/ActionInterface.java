package core.shape_interface;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 *
 * @author Mithusayel Murmu
 */
public abstract class ActionInterface extends PadInterface {

    @Override
    public void clearShape() {}
    
    @Override
    protected void mouseDrag(MouseEvent e) {}

    @Override
    protected void mouseUp(MouseEvent e) {}
    
    @Override
    public void paint(Graphics2D g) {}
    
    @Override
    public void createEditor() {}
    
    @Override
    public void updateControls() {}
    
}
