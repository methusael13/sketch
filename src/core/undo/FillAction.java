package core.undo;

import core.Model;
import core.modules.FloodFill;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Mithusayel Murmu
 */
public class FillAction extends UndoableAction {
    
    private Color fillColor;
    private Point point;
    
    public FillAction(Color col, Point p) {
        fillColor = col;
        point = p;
        
        description = "Flood Fill";
    }

    @Override
    public void execute(Graphics2D g, Model model) {
        FloodFill fill = new FloodFill(model.getImage(), fillColor);
        fill.floodFill(point.x, point.y);
    }
    
}
