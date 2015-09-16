package ui.modules;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import resources.ViewConstants;

/**
 *
 * @author Mithusayel Murmu
 */
public class MemoryInfo extends JPanel {
    private Timer timer;
    
    private long usage;
    private long total;
    private long lastUsage;
    
    private Color c1;
    private Color c2;
    private Color borderColorIdle;
    private Color borderColorActive;
    private GradientPaint gp;
    
    private int sHeight;
    private int totWidth;
    
    private boolean mousein;
    
    private RoundRectangle2D clipRect;

    public MemoryInfo(int height, final JLabel lab) {
        lab.setFont(ViewConstants.STATUS_MEMORY_FONT);

        setToolTipText("Click to force garbage collection");
        
        lastUsage = usage = total = 1;

        c1 = Color.getHSBColor((float)usage / total, 1f, 1f);
        c2 = c1.darker();
        borderColorIdle = Color.GRAY;
        borderColorActive = Color.WHITE;

        sHeight = height / 2;
        totWidth = 150;
        setPreferredSize(new Dimension(totWidth, sHeight));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e))
                    System.gc();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                mousein = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                mousein = false;
                repaint();
            }
        });

        final Runtime rt = Runtime.getRuntime();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                total = rt.totalMemory();
                usage = total - rt.freeMemory();

                lab.setText(initDisplays());
                repaint();
            }
        };

        timer = new Timer();
        timer.schedule(task, 50, 2000);
    }

    public String initDisplays() {
        float usage_mb = usage / 1000000f;
        float total_mb = total / 1000000f;
        
        c1 = Color.getHSBColor(usage_mb / total_mb, 1f, 1f);
        c2 = c1.darker();

        return String.format("%.1f/%.1f MB", usage_mb, total_mb);
    }

    public GradientPaint getGradient() {
        if (gp == null || lastUsage != usage) {
            lastUsage = usage;
            gp = new GradientPaint(0, 0, c1, 0, sHeight, c2);
        }
        return gp;
    }
    
    public RoundRectangle2D getClipRect() {
        if(clipRect == null)
            clipRect = new RoundRectangle2D.Float(0, 0, getWidth(), sHeight, 10, 10);
        
        return clipRect;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Shape s = g2.getClip();
        g2.setClip(getClipRect());

        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), sHeight);

        g2.setPaint(getGradient());
        int w = (int) (usage * getWidth() / total);
        g2.fillRect(0, 0, w, sHeight);
        
        g2.setClip(s);
        g2.setPaint(mousein ? borderColorActive : borderColorIdle);
        g2.drawRoundRect(0, 0, getWidth()-1, sHeight-1, 9, 9);
    }
}
