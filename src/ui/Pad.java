package ui;

import core.Model;
import core.shape_interface.PadInterface;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import resources.PadConstants;

/**
 *
 * @author Mithusayel Murmu
 */
public final class Pad extends JPanel {
    
    private Model model;
    private JPanel parent;
    private PadInterface control;
    
    // All the interactive actions
    private Action deleteAction;
            
    protected PadBounds can;
    
    public static final int OFFSET = 1;
    
    public Pad(final View view, final JPanel parent) {
        this.parent = parent;
        
        model = view.getModel();
        can = new PadBounds();
        
        updateShapeInterface();
        mapInputActions();
        
        setBackground(Model.getPadColor());
        setBorder(BorderFactory.createLineBorder(PadConstants.PAD_BORDER_COLOR, 2, true));
        
        // Don't ever delete this
        updateDimensions();
    }
    
    private void mapInputActions() {
        deleteAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.removeTemporaryElement();
            }
        };
        
        final InputMap in = getInputMap();
        final ActionMap act = getActionMap();
        
        in.put(KeyStroke.getKeyStroke("DELETE"), "delete");       
        act.put("delete", deleteAction);
    }
    
    public final void updateDimensions() {
        final Dimension dim = new Dimension(model.getWidth(), model.getHeight());
        
        setMinimumSize(dim);
        setPreferredSize(dim);
        setMaximumSize(dim);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        model.paint(g);
    }
    
    @Override
    public int getY() {
        setLocation(can.x(), can.y());
        return can.y();
    }
    
    public void paintTemp() {
        //repaint(model.getExtendedBounds());
        repaint();
    }
    
    public void notifyControlsPaint(Graphics2D g2) {
        control.paint(g2);
    }
    
    public void forceEditMode() {
        control.createEditor();
    }
    
    public void forceControlUpdate() {
        control.updateControls();
    }
    
    public void updateShapeInterface(Model.TOOLS type) {
        removeControls();
        
        switch(type) {
            case FREESHAPE:
                control = PadInterface.getFreeShapeControlInstance(model);
                break;
            case LINE:
                control = PadInterface.getLineControlInstance(model);
                break;
            case RECT:
                control = PadInterface.getRectControlInstance(model);
                break;
            case OVAL:
                control = PadInterface.getOvalControlInstance(model);
                break;
            case CURVE:
                control = PadInterface.getCurveControlInstance(model);
                break;
            case ERASER:
                control = PadInterface.getEraserControlInstance(model);
                break;
            case FILL:
                control = PadInterface.getFillControlInstance(model);
        }
        
        control.setPad(this);
        updateControls();
    }
    
    public void updateShapeInterface() {
        updateShapeInterface(model.getToolType());
    }
    
    private void updateControls() {
        if(control != null) {
            addMouseListener(control);
            addMouseMotionListener(control);
        }
    }
    
    private void removeControls() {
        if(control != null) {
            removeMouseListener(control);
            removeMouseMotionListener(control);
        }
    }
    
    public void removeTemporaryShape() {
        control.clearShape();
    }
    
    protected class PadBounds {
        
        public final int x() {
            return (parent.getWidth() - model.getWidth()) / 2;
        }
        
        public final int y() {
            return (parent.getHeight() - model.getHeight()) / 2;
        }
        
        public final int w() {
            return model.getWidth();
        }
        
        public final int h() {
            return model.getHeight();
        }
    }
}
