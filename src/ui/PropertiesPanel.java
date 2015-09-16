package ui;

import core.Model;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import resources.PadConstants;
import resources.ViewConstants;
import ui.modules.BrushPanel;
import ui.modules.ColorSetPanel;
import ui.modules.ColorWheelPanel;

/**
 *
 * @author Mithusayel Murmu
 */
public class PropertiesPanel extends JPanel {

    private Model model;
    
    // Components
    private EmptyBar emptyBar1;
    private ColorWheelPanel cwPanel;
    private ColorSetPanel csPanel;
    private EmptyBar emptyBar2;
    private BrushPanel bPanel;
    
    // Paint properties;
    private GradientPaint panelGradient;
    private GradientPaint titleGradient;
    private int h;
    private int titleHeight;
    
    // Panel props
    private String title;

    public PropertiesPanel(Model model) {
        this.model = model;
        panelGradient = null;
        title = null;
        
        h = 0;
        titleHeight = 25;

        Dimension dim = new Dimension(190, getHeight());
        setPreferredSize(dim);
        
        emptyBar1 = new EmptyBar(dim.width, 20);
        cwPanel = new ColorWheelPanel(this);
        csPanel = new ColorSetPanel(this);
        emptyBar2 = new EmptyBar(dim.width, 20);
        bPanel = new BrushPanel(this, dim.width);

        setLayout(new FlowLayout());
        add(emptyBar1);
        add(cwPanel);
        add(csPanel);
        add(emptyBar2);
        add(bPanel);
        
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
    }
    
    public Model getModel() {
        return model;
    }
    
    public void setTitle(String title) {
        this.title = title;
        repaint();
    }
    
    public ColorWheelPanel getCWPanel() {
        return cwPanel;
    }

    public GradientPaint getPanelGradient() {
        if (panelGradient == null || getHeight() != h) {
            h = getHeight();
            panelGradient = new GradientPaint(0, 0, PadConstants.PARENT_TOP_COLOR, 
                    0, h, PadConstants.PARENT_BOT_COLOR);
        }

        return panelGradient;
    }
    
    public GradientPaint getTitleGradient() {
        if(titleGradient == null) {
            titleGradient = new GradientPaint(0, 0, PadConstants.PARENT_TOP_COLOR, 
                    0, titleHeight, PadConstants.PARENT_BOT_COLOR);
        }
        
        return titleGradient;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setPaint(getPanelGradient());
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        paintTitleBar(g2);
    }
    
    public void setColor(Color c) {
        model.setPrimaryColor(c);
        
        csPanel.updateColorSet(c);
    }
    
    private void paintTitleBar(Graphics2D g2) {
        g2.setPaint(getTitleGradient());
        g2.fillRect(0, 0, getWidth(), titleHeight);
        
        g2.setPaint(Color.GRAY);
        g2.drawLine(0, titleHeight, getWidth(), titleHeight);
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.setFont(ViewConstants.PROPS_TITLE_FONT);
        
        if(title == null)
            return;
        
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(title)) / 2;
        int y = (titleHeight + g2.getFont().getSize() - 2) / 2;
        g2.drawString(title, x, y);
    }
    
    private class EmptyBar extends JPanel {
        
        public EmptyBar(int w, int h) {
            setPreferredSize(new Dimension(w, titleHeight - 5));
            setOpaque(false);
        }
    }
}
