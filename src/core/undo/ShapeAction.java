package core.undo;

import core.Model;
import core.geom.Shapes;
import core.geom.tools.Eraser;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 *
 * @author Mithusayel Murmu
 */
public class ShapeAction extends UndoableAction {

    private Shapes shape;
    
    public ShapeAction(Shapes shape) {
        this.shape = shape;
        type = SHAPE_ACTION;
        
        description = shape instanceof Eraser ? 
                "Erase" : "Add Shape";
    }

    @Override
    public void execute(Graphics2D g2, Model model) {
        if (shape.getAntialias()) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        g2.setPaint(shape.getColor());
        g2.setStroke(shape.getStroke());

        shape.paint(g2);
    }   
}
