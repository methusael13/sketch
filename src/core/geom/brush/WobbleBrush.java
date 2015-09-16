package core.geom.brush;

import java.awt.*;
import java.awt.geom.*;

public class WobbleBrush implements Stroke {

    private float detail;
    private float amplitude;
    private static final float FLATNESS = 1;
    
    private BasicStroke stroke;
    
    public WobbleBrush(float size) {
        this(size, 2, 2);
    }

    public WobbleBrush(float size, float detail, float amplitude) {
        this.detail = detail;
        this.amplitude = amplitude;
        
        stroke = new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    @Override
    public Shape createStrokedShape(Shape shape) {
        GeneralPath result = new GeneralPath();
        shape = stroke.createStrokedShape(shape);
        
        PathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), FLATNESS);
        
        float points[] = new float[6];
        
        float moveX = 0, moveY = 0;
        float lastX = 0, lastY = 0;
        float thisX = 0, thisY = 0;
        
        int type = 0;
        
        float next = 0;

        while (!it.isDone()) {
            type = it.currentSegment(points);
            
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    moveX = lastX = randomize(points[0]);
                    moveY = lastY = randomize(points[1]);
                    result.moveTo(moveX, moveY);
                    
                    next = 0;
                    break;

                case PathIterator.SEG_CLOSE:
                    points[0] = moveX;
                    points[1] = moveY;
                // Fall into....

                case PathIterator.SEG_LINETO:
                    thisX = randomize(points[0]);
                    thisY = randomize(points[1]);
                    
                    float dx = thisX - lastX;
                    float dy = thisY - lastY;
                    
                    float distance = (float) Math.sqrt(dx * dx + dy * dy);
                    
                    if (distance >= next) {
                        float r = 1.0f / distance;
                        
                        while (distance >= next) {
                            float x = lastX + next * dx * r;
                            float y = lastY + next * dy * r;
                            result.lineTo(randomize(x), randomize(y));
                            next += detail;
                        }
                    }
                    
                    next -= distance;
                    lastX = thisX;
                    lastY = thisY;
                    break;
            }
            it.next();
        }

        return result;
    }

    private float randomize(float x) {
        return x + (float) Math.random() * amplitude * 2 - 1;
    }
}
