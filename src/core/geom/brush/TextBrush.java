package core.geom.brush;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;

public class TextBrush implements Stroke {

    private GlyphVector glyphVector;
    
    private static boolean stretchToFit = false;
    private static boolean repeat = true;
    
    private float advance;
    
    private static String text = "Metal";
    private AffineTransform t = new AffineTransform();
    
    private static final float FLATNESS = 1;
    public static final float APPARENT_GAP = 0.91f;

    public TextBrush(Font font) {
        this(font, 20f);
    }

    public TextBrush(Font font, float advance) {
        this.advance = advance;
        
        FontRenderContext frc = new FontRenderContext(null, true, true);
        glyphVector = font.createGlyphVector(frc, text);
    }
    
    public static String getText() {
        return text;
    }
    
    public static boolean isRepeatEnabled() {
        return repeat;
    }
    
    public static boolean stretchToFit() {
        return stretchToFit;
    }
    
    public void setText(String str) {
        if(str == null || str.isEmpty() || str.equals(text))
            return;
        
        text = str;
        FontRenderContext frc = new FontRenderContext(null, true, true);
        Font f = glyphVector.getFont();
        
        glyphVector = f.createGlyphVector(frc, text);
    }
    
    public void setFont(Font f) {
        if(f == null || f.equals(glyphVector.getFont()))
            return;
        
        FontRenderContext frc = new FontRenderContext(null, true, true);
        glyphVector = f.createGlyphVector(frc, text);
    }
    
    public void setRepeat(boolean reap) {
        repeat = reap;
    }
    
    public void stretchToFit(boolean s) {
        stretchToFit = s;
    }

    @Override
    public Shape createStrokedShape(Shape shape) {       
        GeneralPath result = new GeneralPath();
        PathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), FLATNESS);
        
        float points[] = new float[6];
        float moveX = 0, moveY = 0;
        float lastX = 0, lastY = 0;
        float thisX = 0, thisY = 0;
        
        int type = 0;
        float next = 0;
        int currentChar = 0;
        int length = glyphVector.getNumGlyphs();

        if (length == 0) {
            return result;
        }

        float factor = stretchToFit ? 
                measurePathLength(shape) / (float) glyphVector.getLogicalBounds().getWidth() : 1.0f;

        while (currentChar < length && !it.isDone()) {
            type = it.currentSegment(points);
            
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    moveX = lastX = points[0];
                    moveY = lastY = points[1];
                    result.moveTo(moveX, moveY);
                    
                    next = advance;
                    break;

                case PathIterator.SEG_CLOSE:
                    points[0] = moveX;
                    points[1] = moveY;
                // Fall into....

                case PathIterator.SEG_LINETO:
                    thisX = points[0];
                    thisY = points[1];
                    
                    float dx = thisX - lastX;
                    float dy = thisY - lastY;
                    
                    float distance = (float) Math.sqrt(dx * dx + dy * dy);
                    
                    if (distance >= next) {
                        float r = 1.0f / distance;
                        float angle = (float) Math.atan2(dy, dx);
                        
                        while (currentChar < length && distance >= next) {
                            Shape glyph = glyphVector.getGlyphOutline(currentChar);
                            Point2D p = glyphVector.getGlyphPosition(currentChar);
                            
                            float px = (float) p.getX();
                            float py = (float) p.getY();
                            float x = lastX + next * dx * r;
                            float y = lastY + next * dy * r;
                            
                            t.setToTranslation(x, y);
                            t.rotate(angle);
                            t.translate(-px - advance, -py);
                            
                            result.append(t.createTransformedShape(glyph), false);
                            
                            next += advance * factor;
                            currentChar++;
                            
                            if (repeat) {
                                currentChar %= length;
                            }
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

    public float measurePathLength(Shape shape) {
        PathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), FLATNESS);
        
        float points[] = new float[6];
        float moveX = 0, moveY = 0;
        float lastX = 0, lastY = 0;
        float thisX = 0, thisY = 0;
        
        int type = 0;
        float total = 0;

        while (!it.isDone()) {
            type = it.currentSegment(points);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    moveX = lastX = points[0];
                    moveY = lastY = points[1];
                    break;

                case PathIterator.SEG_CLOSE:
                    points[0] = moveX;
                    points[1] = moveY;
                // Fall into....

                case PathIterator.SEG_LINETO:
                    thisX = points[0];
                    thisY = points[1];
                    
                    float dx = thisX - lastX;
                    float dy = thisY - lastY;
                    
                    total += (float) Math.sqrt(dx * dx + dy * dy);
                    
                    lastX = thisX;
                    lastY = thisY;
                    break;
            }
            it.next();
        }

        return total;
    }
}
