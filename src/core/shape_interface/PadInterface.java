package core.shape_interface;

import core.Model;
import core.geom.tools.EraserInterface;
import core.shape_interface.editor.ShapeEditInterface;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import ui.Pad;

/**
 *
 * @author Mithusayel Murmu
 */
public abstract class PadInterface extends MouseAdapter {
    
    protected Pad pad;
    protected Model model;
    protected ShapeEditInterface sei;
    
    public void setPad(Pad pad) {
        this.pad = pad;
    }
    
    public void paint(Graphics2D g2) {
        sei.paintControls(g2);
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        pad.requestFocusInWindow();
        if(Model.getMode() == Model.EDIT_MODE)
            sei.mouseMoved(e);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e))
            mouseDown(e);
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e))
            mouseDrag(e);
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e))
            mouseUp(e);
    }
    
    public void createEditor() {
        sei = new ShapeEditInterface(model);
    }
    
    public void updateControls() {
        if(sei != null) {
            sei.updateControls();
        }
    }
    
    public abstract void clearShape();
    protected abstract void mouseDown(MouseEvent e);
    protected abstract void mouseDrag(MouseEvent e);
    protected abstract void mouseUp(MouseEvent e);
    
    public static PadInterface getFreeShapeControlInstance(Model model) {
        return new FreeShapeInterface(model);
    }

    public static PadInterface getLineControlInstance(Model model) {
        return new LineInterface(model);
    }

    public static PadInterface getRectControlInstance(Model model) {
        return new RectInterface(model);
    }

    public static PadInterface getOvalControlInstance(Model model) {
        return new OvalInterface(model);
    }

    public static PadInterface getCurveControlInstance(Model model) {
        return new CurveInterface(model);
    }
    
    public static PadInterface getEraserControlInstance(Model model) {
        return new EraserInterface(model);
    }
    
    public static PadInterface getFillControlInstance(Model model) {
        return new FillInterface(model);
    }
}
