package ui.modules;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JToolBar;
import resources.PadConstants;

/**
 *
 * @author Mithusayel Murmu
 */
public class TopToolBar extends JToolBar {
    
    // Paint properties;
    private GradientPaint panelGradient;
    
    public GradientPaint getPanelGradient() {
        if (panelGradient == null) {
            panelGradient = new GradientPaint(0, 0, PadConstants.PARENT_TOP_COLOR, 
                    0, getHeight(), PadConstants.PARENT_BOT_COLOR);
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