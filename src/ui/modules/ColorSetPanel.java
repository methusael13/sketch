package ui.modules;

import core.Model;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ui.PropertiesPanel;

/**
 *
 * @author Mithusayel Murmu
 */
public class ColorSetPanel extends JPanel { 
    private PropertiesPanel panel;
    
    // Components
    private ColorSet[] set;
    private AlphaSet alpha;
    
    private int selectedID;
    
    public ColorSetPanel(final PropertiesPanel panel) {
        this.panel = panel;
        
        setOpaque(false);
        selectedID = 0;
        
        set = new ColorSet[2];
        set[0] = new ColorSet(Model.getPrimaryColor(), 0);
        set[1] = new ColorSet(Model.getSecondaryColor(), 1);
        set[selectedID].setSelected(true);
        
        alpha = new AlphaSet(170);
        
        addComponents();
    }
    
    private void addComponents() {
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        setLayout(gb);
        
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(15, 0, 0, 0);
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gb.setConstraints(alpha, gc);
        
        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(5, 0, 0, 0);
        gb.setConstraints(set[0], gc);
        
        gc.gridx = 0;
        gc.gridy = 2;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 0, 0, 0);
        gb.setConstraints(set[1], gc);
        
        add(alpha);
        add(set[0]);
        add(set[1]);
    }
    
    public int getSelectedID() {
        return selectedID;
    }
    
    public void setSelectedID(int id) {
        if(id == 0) {
            selectedID = id;
            set[0].setSelected(true);
            set[1].setSelected(false);
        } else if(id == 1) {
            selectedID = id;
            set[1].setSelected(true);
            set[0].setSelected(false);
        }
    }
    
    public void updateColorSet(Color c) {
        set[selectedID].setColor(c);
    }
    
    private class ColorSet extends JLabel {
        private final int ID;
        
        private Color col;
        private Color activeBColor;
        private Color idleBColor;
        
        private boolean selected;
        
        private static final int arcSize = 15;
        
        private Dimension dim;
        
        public ColorSet(Color c, int id) {
            this.col = c;
            this.ID = id;
            
            activeBColor = Color.GRAY;
            idleBColor = Color.DARK_GRAY;

            setOpaque(false);
            
            dim = new Dimension(170, 25);
            setMaximumSize(dim);
            setMinimumSize(dim);
            setPreferredSize(dim);
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    setSelectedID(ID);
                    panel.getModel().setPrimaryColor(col);
                }
            });
            
            if(ID == 0)
                setToolTipText("Primary Color");
            else
                setToolTipText("Secondary Color");
        }
        
        public void setSelected(boolean s) {
            selected = s;
            
            repaint();
        }
        
        public void setColor(Color c) {
            this.col = c;
            repaint();
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(col);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcSize, arcSize);
            
            if(selected)
                g2.setColor(activeBColor);
            else
                g2.setColor(idleBColor);
            
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, arcSize, arcSize);
        }
    }
    
    private class AlphaSet extends JPanel {
        
        private JSlider slider;
        
        public AlphaSet(int w) {
            setOpaque(false);
            
            Dimension dim = new Dimension(w, 50);
            setMaximumSize(dim);
            setMinimumSize(dim);
            setPreferredSize(dim);
            
            initGUI();
        }
        
        private void initGUI() {
            GridBagLayout gb = new GridBagLayout();
            GridBagConstraints gc = new GridBagConstraints();
            setLayout(gb);

            JLabel alphaLabel = new JLabel("Alpha:");
            gc.gridx = 0;
            gc.gridy = 0;
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.weightx = 1;
            gc.insets = new Insets(10, 0, 0, 0);
            gc.gridwidth = 1;
            gc.gridheight = 1;
            gb.setConstraints(alphaLabel, gc);

            slider = new JSlider(JSlider.HORIZONTAL, 0, 255, 255);
            slider.setOpaque(false);
            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    panel.getModel().setAlpha(slider.getValue());
                }
            });
            alphaLabel.setLabelFor(slider);
            gc.gridx = 0;
            gc.gridy = 1;
            gc.insets = new Insets(0, 0, 0, 0);
            gb.setConstraints(slider, gc);
            
            add(alphaLabel);
            add(slider);
        }
    } 
}
