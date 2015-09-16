package ui.modules;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ui.PropertiesPanel;

/**
 *
 * @author Mithusayel Murmu
 */
public class ColorWheelPanel extends JPanel {
    private ColorWheel wheel;
    private BrightnessSlider slider;
    
    private PropertiesPanel prop;
    
    public ColorWheelPanel(PropertiesPanel prop) {
        this.prop = prop;
        
        setOpaque(false);
        
        wheel = new ColorWheel(75);
        slider = new BrightnessSlider(SwingConstants.VERTICAL, 0, 100, (int)(wheel.getBrightness()*100));
        slider.setToolTipText("Adjust Brightness");
        slider.setOpaque(false);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                wheel.setBrightness((float)slider.getValue()/slider.getMaximum());
            }
        });
        slider.setPreferredSize(new Dimension(15, wheel.getRadius() * 2));
        
        add(wheel, BorderLayout.CENTER);
        add(slider, BorderLayout.EAST);
    }
    
    private class ColorWheel extends JComponent {
        private BufferedImage image;
        
        private float brightness = 1f;
        private Ellipse2D bounds;
        
        private Color backFill;
        private boolean fTime;
        
        private MouseAdapter adapter;
        
        private int radius;
        
        public ColorWheel(int rad) {
            radius = rad;
            backFill = new Color(255, 255, 255, 0);
            fTime = true;
            
            Dimension dim = new Dimension(radius * 2 + 2, radius * 2 + 2);
            setMinimumSize(dim);
            setMaximumSize(dim);
            setPreferredSize(dim);
            
            adapter = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(bounds.contains(e.getPoint())) {
                        int col = getImage().getRGB(e.getX(), e.getY());
                        int r = (col >> 16) & 0xff;
                        int g = (col >> 8) & 0xff;
                        int b = col & 0xff;
                        int a = prop.getModel().getAlpha();
                        
                        prop.setColor(new Color(r, g, b, a));
                    }
                }
                
                @Override
                public void mouseDragged(MouseEvent e) {
                    mousePressed(e);
                }
            };
            
            addMouseListener(adapter);
            addMouseMotionListener(adapter);
            
            setToolTipText("<html><center>Color Wheel</center><&nbsp>"
                    + "<I><strong>Click or drag to select color</strong></I></html>");
        }
        
        public int getRadius() {
            return radius;
        }
        
        public BufferedImage getImage() {
            if(image == null)
                image = new BufferedImage(radius*2 + 2, radius*2 + 2, BufferedImage.TYPE_INT_ARGB_PRE);
            
            return image;
        }
        
        public void setBrightness(float b) {
            if(brightness != b) {
                brightness = b;

                drawWheel();
                repaint();
            }
        }
        
        public float getBrightness() { 
            return brightness; 
        }
        
        public Ellipse2D getWheelBounds(int cx, int cy) {
            if(bounds == null)
                bounds = new Ellipse2D.Float(cx - radius, cy - radius, radius * 2, radius * 2);
            
            return bounds;
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(fTime) {
                drawWheel();
                fTime = false;
            }
            
            g.drawImage(getImage(), 0, 0, null);
        }
 
        private void drawWheel() {
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            
            Graphics2D g2 = getImage().createGraphics();
            g2.setColor(backFill);
            g2.fillRect(0, 0, getWidth(), getHeight());

            for (float i = radius; i > 0; i--) {
                int size = (int) (i * 2);
                for (float ang = 0; ang <= 360; ang++) {
                    Color c = Color.getHSBColor(ang / 360, i / radius, brightness);

                    g2.setColor(c);
                    g2.fillArc((int) (cx - i), (int) (cy - i), size, size, (int) (ang - 90), 5);
                }
            }

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLACK);
            g2.draw(getWheelBounds(cx, cy));

            g2.dispose();
        }
    }
    
    private class BrightnessSlider extends JSlider {

        private RoundRectangle2D clipRect;
        private GradientPaint gp;
        private static final int arcSize = 15;
        private int controlRad = 4;

        public BrightnessSlider(int or, int mn, int mx, int val) {
            super(or, mn, mx, val);
        }

        public RoundRectangle2D getRoundClip() {
            if (clipRect == null) {
                clipRect = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arcSize, arcSize);
            }

            return clipRect;
        }

        public GradientPaint getGradient() {
            if (gp == null) {
                gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), Color.BLACK);
            }

            return gp;
        }

        @Override
        public void setValue(int val) {
            sliderModel.setValue(val);
            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Shape clip = g2.getClip();

            g2.setClip(getRoundClip());

            g2.setPaint(getGradient());
            g2.fillRect(0, 0, getWidth(), getHeight());

            int dv = getMaximum() - getMinimum();
            int py = (getValue() - getMinimum()) * getHeight() / dv;
            py = getHeight() - py;

            g2.setPaint(Color.GRAY);
            g2.setClip(clip);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcSize, arcSize);

            g2.setPaint(Color.WHITE);
            g2.fillOval(getWidth() / 2 - controlRad, Math.min(getHeight() - controlRad * 2,
                    Math.max(py - controlRad, 0)), controlRad * 2, controlRad * 2);
            g2.setPaint(Color.BLACK);
            g2.drawOval(getWidth() / 2 - controlRad, Math.min(getHeight() - controlRad * 2,
                    Math.max(py - controlRad, 0)), controlRad * 2, controlRad * 2);
        }
    }
}
