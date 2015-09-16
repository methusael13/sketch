package core.shape_interface.editor;

/**
 * Shape Status
 * Used to store the shape properties
 * during shape edit for proper repaint.
 * 
 * @author Mithusayel Murmu
 */
public class SStatus {
    
    public static int DRAGX = 0;
    public static int DRAGY = 0;
    public static boolean usable = false;
    
    public static void setDragPos(int dx, int dy) {
        DRAGX = dx;
        DRAGY = dy;
        usable = true;
    }
    
    public static void reset() {
        DRAGX = 0;
        DRAGY = 0;
        usable = false;
    }
}
