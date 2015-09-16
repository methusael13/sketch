package core.geom;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

/**
 *
 * @author Mithusayel Murmu
 */
public class Star {
    
    private int cx;
    private int cy;
    private int arms;
    
    private float outRad;
    private float inRad;
    
    private GeneralPath path;
    
    public Star(int x, int y, int arms, float or, float inr) {
        cx = x;
        cy = y;
        this.arms = arms;
        
        outRad = or;
        inRad = inr;
        
        path = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        createStar();
    }
    
    private void createStar() {
        double angle = Math.PI / arms;

        for (int i = 0; i < 2 * arms; i++) {
            double r = (i & 1) == 0 ? outRad : inRad;
            
            double x = cx + Math.cos(i * angle) * r;
            double y = cy + Math.sin(i * angle) * r;
            
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        
        path.closePath();
    }
    
    public Shape getShape() {
        return path;
    }
}
