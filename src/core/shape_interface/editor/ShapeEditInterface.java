package core.shape_interface.editor;

import core.Model;
import core.geom.Curve;
import core.geom.Line;
import core.geom.Shapes;
import core.shape_interface.editor.Control.CONTROL_LOCS;
import core.shape_interface.editor.geom.BoxShapeEditor;
import core.shape_interface.editor.geom.CurveShapeEditor;
import core.shape_interface.editor.geom.LineShapeEditor;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Mithusayel Murmu
 */
public class ShapeEditInterface extends MouseAdapter {
    
    private Model model;
    private ShapeEditor se;
    private Shapes shape;
    
    public ShapeEditInterface(Model model) {
        this.model = model;
        shape = model.getElement();
        
        initializeShapeEditor(shape);
    }
    
    public void exitEditMode() {
        assert Model.getMode() == Model.EDIT_MODE;

        model.setMode(Model.IDLE_MODE);
        model.validateTemporaryShape();
        model.updateCursor(Cursor.getDefaultCursor());
    }
    
    public void updateControls() {
        se.updateControlPositions();
    }
    
    public void paintControls(Graphics2D g2) {
        se.paint(g2);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if(!se.hit(e.getPoint())) {
            exitEditMode();
            return;
        }
        
        se.pressControl(e.getPoint());
        se.setDragMode(true);
        shape.lockShape();
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        model.updateCursor(se.getCursor(e.getPoint()));
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        se.dragControl(e);

        switch (model.getToolType()) {
            case RECT:
            case OVAL:
            case FREESHAPE: {
                if(se.getSelectedControlType() == CONTROL_LOCS.ROT)
                    SStatus.setDragPos(se.drag_x, se.drag_y);
            }
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        se.setDragMode(false);
        se.updateControlPositions();
        shape.releaseShape();
        SStatus.reset();
    }

    private void initializeShapeEditor(Shapes shape) {
        switch (shape.getType()) {
            case FREESHAPE:
            case RECT:
            case OVAL: {
                se = new BoxShapeEditor(shape);
            }
            break;

            case LINE: {
                se = new LineShapeEditor((Line) shape);
            }
                break;
                
            case CURVE: {
                se = new CurveShapeEditor((Curve) shape);
            }
        }
    }
}
