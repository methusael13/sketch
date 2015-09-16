package resources;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

/**
 *
 * @author Mithusayel Murmu
 */
public class ViewConstants {
    
    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 768;
    
    public static final int WINDOW_LOCX;
    public static final int WINDOW_LOCY;
    
    public static final String WINDOW_TITLE = "Sketch";
    
    public static final Font PROPS_TITLE_FONT = new Font("Arial", Font.BOLD, 12);
    public static final Font STATUS_MEMORY_FONT = PROPS_TITLE_FONT;
    
    static {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        WINDOW_LOCX = (dim.width - WINDOW_WIDTH) / 2;
        WINDOW_LOCY = (dim.height - WINDOW_HEIGHT) / 2;
    }
    
    public static final String[] STROKE_PRESET_VAL = 
    {
        "Plain",
        "Wobble",
        "ZigZag",
        "Text Outline",
        "Shape Outline",
        /*"Dash - 2f",
        "Dash - 3f",
        "Dash - 4f",
        "Dash - 5f",
        "Dash - 6f",*/
    };
    
    public static enum STROKE_PRESET_MODEL {
        PLAIN, WOBBLE, ZIGZAG, TXT, SHAPE,
        D2, D3, D4, D5, D6
    }
}
