package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import resources.PadConstants;
import ui.modules.MemoryInfo;

/**
 *
 * @author Mithusayel Murmu
 */
public class StatusPanel extends JPanel {
    private GradientPaint panelGradient;
    private int height;
    
    private MemoryInfo mem;
    private JLabel memLab;
    
    public StatusPanel(View view) {
        height = 24;
        
        Dimension dim = new Dimension(view.getWidth(), height);
        setPreferredSize(dim);
        
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        
        memLab = new JLabel();
        mem = new MemoryInfo(height, memLab);
        setLayout(new FlowLayout());
        add(mem, BorderLayout.CENTER);
        add(memLab, BorderLayout.EAST);
    }
    
    public GradientPaint getPanelGradient() {
        if (panelGradient == null) {
            panelGradient = new GradientPaint(0, 0, PadConstants.PARENT_TOP_COLOR, 
                    0, height, PadConstants.PARENT_BOT_COLOR);
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
