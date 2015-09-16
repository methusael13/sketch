package ui.modules;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JToolBar;
import resources.PadConstants;

/**
 *
 * @author Mithusayel Murmu
 */
public class LeftToolBar extends JToolBar {
    
    // Paint properties;
    private GradientPaint panelGradient;
    private int h;
    
    public LeftToolBar() {
        super();
        
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.GRAY));
    }
    
    public GradientPaint getPanelGradient() {
        if (panelGradient == null || getHeight() != h) {
            h = getHeight();
            panelGradient = new GradientPaint(0, 0, PadConstants.PARENT_TOP_COLOR, 
                    0, h, PadConstants.PARENT_BOT_COLOR);
        }

        return panelGradient;
    }
    
    @Override
    public void paintComponent(Graphics g) {        
        Graphics2D g2 = (Graphics2D)g;
        
        g2.setPaint(getPanelGradient());
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
