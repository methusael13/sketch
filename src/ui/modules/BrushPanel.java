package ui.modules;

import core.geom.brush.TextBrush;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import resources.ViewConstants;
import ui.PropertiesPanel;

/**
 *
 * @author Mithusayel Murmu
 */
public class BrushPanel extends JPanel {
    
    private PropertiesPanel props;
    
    // Components
    private JSlider sizeSlider;
    private JComboBox strokeCombo;
    private JCheckBox alias;
    private JCheckBox fill;
    
    private JComponent[] extraProps;
    
    public BrushPanel(final PropertiesPanel panel, int width) {
        super();
        setOpaque(false);
        
        props = panel;
        Dimension dim = new Dimension(width - 10, 150);
        setPreferredSize(dim);
        
        initGUI(width - 10);
    }
    
    private void initGUI(int w) {
        final GridBagLayout gb = new GridBagLayout();
        final GridBagConstraints gc = new GridBagConstraints();
        setLayout(gb);
        
        JLabel strokeLab = new JLabel("Stroke Type:");
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(4, 0, 0, 0);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.gridwidth = 2;
        gb.addLayoutComponent(strokeLab, gc);
        
        strokeCombo = new JComboBox(ViewConstants.STROKE_PRESET_VAL);
        strokeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = strokeCombo.getSelectedIndex();
                ViewConstants.STROKE_PRESET_MODEL[] brushes = ViewConstants.STROKE_PRESET_MODEL.values();
                props.getModel().setBrushType(brushes[idx]);
                
                createExtraProps(gb, gc);
            }
        });
        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.gridwidth = 2;
        gb.addLayoutComponent(strokeCombo, gc);
        
        JLabel sizeLab = new JLabel("Stroke Size:");
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.insets = new Insets(10, 0, 0, 0);
        gb.addLayoutComponent(sizeLab, gc);
        
        final JLabel sizeInfo = new JLabel(String.valueOf((int)props.getModel().getStrokeSize()));
        gc.gridx = 1;
        gc.gridy = 2;
        gc.insets = new Insets(4, w/2 - 15, 0, 0);
        gb.addLayoutComponent(sizeInfo, gc);
        
        sizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 30, (int)props.getModel().getStrokeSize());
        sizeSlider.setOpaque(false);
        sizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int val = sizeSlider.getValue();
                sizeInfo.setText(String.valueOf(val));
                sizeSlider.setToolTipText(String.valueOf(val));
                props.getModel().setStrokeSize(val);
            }
        });
        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 2;
        gc.insets = new Insets(0, 0, 0, 0);
        gb.addLayoutComponent(sizeSlider, gc);            
        
        alias = new JCheckBox("Smooth Strokes");
        alias.setOpaque(false);
        alias.setSelected(props.getModel().getAntialias());
        alias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                props.getModel().setAntialias(alias.isSelected());
            }
        });
        gc.gridx = 0;
        gc.gridy = 4;
        gc.insets = new Insets(10, 0, 0, 0);
        gb.addLayoutComponent(alias, gc);
        
        fill = new JCheckBox("Fill");
        fill.setOpaque(false);
        fill.setSelected(props.getModel().getFill());
        fill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                props.getModel().setFill(fill.isSelected());
            }
        });
        gc.gridx = 0;
        gc.gridy = 5;
        gc.insets = new Insets(0, 0, 0, 0);
        gb.addLayoutComponent(fill, gc);
        
        add(sizeLab);
        add(sizeInfo);
        add(sizeSlider);
        add(strokeLab);
        add(strokeCombo);
        add(alias);
        add(fill);
    }
    
    public void createExtraProps(GridBagLayout gb, GridBagConstraints gc) {
        ViewConstants.STROKE_PRESET_MODEL type = props.getModel().getBrushType();
        
        // Clear and remove previously set components
        if(extraProps != null) {
            for(int i = 0; i < extraProps.length; i++) {
                remove(extraProps[i]);
                extraProps[i] = null;
            }
            
            extraProps = null;
        }
        
        switch (type) {
            case TXT: {
                extraProps = new JComponent[4];
                
                extraProps[0] = new JLabel("Text:");
                gc.gridx = 0;
                gc.gridy = 6;
                gc.insets = new Insets(10, 0, 0, 0);
                gc.fill = GridBagConstraints.HORIZONTAL;
                gc.gridwidth = 2;
                gc.weightx = 1;
                gb.addLayoutComponent(extraProps[0], gc);
                
                extraProps[1] = new JTextField();
                ((JTextField)extraProps[1]).setHorizontalAlignment(JTextField.RIGHT);
                ((JTextField)extraProps[1]).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String txt = ((JTextField)extraProps[1]).getText();
                        ((TextBrush)props.getModel().getStroke()).setText(txt);
                        props.getModel().repaint();
                    }
                });
                gc.gridy = 7;
                gc.insets = new Insets(0, 0, 0, 0);
                gb.addLayoutComponent(extraProps[1], gc);
                
                extraProps[2] = new JCheckBox("Repeat");
                extraProps[2].setOpaque(false);
                ((TextBrush)props.getModel().getStroke()).setRepeat(true);
                ((JCheckBox)extraProps[2]).setSelected(TextBrush.isRepeatEnabled());
                ((JCheckBox)extraProps[2]).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ((TextBrush)props.getModel().getStroke()).
                                setRepeat(((JCheckBox)extraProps[2]).isSelected());
                        props.getModel().repaint();
                    }
                });
                gc.gridy = 8;
                gc.insets = new Insets(5, 0, 0, 0);
                gb.addLayoutComponent(extraProps[2], gc);
                
                extraProps[3] = new JCheckBox("Stretch to fit");
                extraProps[3].setOpaque(false);
                ((JCheckBox)extraProps[3]).setSelected(TextBrush.stretchToFit());
                ((JCheckBox)extraProps[3]).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ((TextBrush)props.getModel().getStroke()).
                                stretchToFit(((JCheckBox)extraProps[3]).isSelected());
                        props.getModel().repaint();
                    }
                });
                gc.gridy = 9;
                gc.insets = new Insets(0, 0, 0, 0);
                gb.addLayoutComponent(extraProps[3], gc);
                
                for(int i = 0; i < extraProps.length; i++) {
                    add(extraProps[i]);
                }
            }
        }
        
        Dimension dim = getPreferredSize();
        dim.height = 30 * (extraProps == null ? 5 : (gc.gridy + 1));
        setPreferredSize(dim);
        
        revalidate();
    }
}
