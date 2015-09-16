package core;

import core.geom.Curve;
import core.geom.FreeShape;
import core.geom.Line;
import core.geom.Oval;
import core.geom.Rect;
import core.geom.Shapes;
import core.geom.Star;
import core.geom.brush.ShapeBrush;
import core.geom.brush.TextBrush;
import core.geom.brush.WobbleBrush;
import core.geom.brush.ZigZagBrush;
import core.geom.tools.Eraser;
import core.shape_interface.editor.Control;
import core.shape_interface.editor.SStatus;
import core.undo.FillAction;
import core.undo.ShapeAction;
import core.undo.UndoManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import static resources.ModelConstants.*;
import resources.ViewConstants;
import ui.View;
import ui.filesystem.DocumentModel;

/**
 *
 * @author Mithusayel Murmu
 */
public final class Model {
    
    protected View view;
    protected Shapes shape;
    protected Shapes copyShape;
    protected DocumentModel docModel;
    private UndoManager undoManager;
    
    private BufferedImage image;
    
    protected static Color primaryColor;
    protected static Color secondaryColor;
    protected static Color padColor;
    
    protected Stroke brush;
    
    private float strokeSize;
    
    protected boolean fill;
    protected boolean antialias;
    
    protected static int mode;
    
    protected int alpha;
    protected int CANVAS_W;
    protected int CANVAS_H;
    
    protected TOOLS type;
    protected ViewConstants.STROKE_PRESET_MODEL brushType;
    
    public static final int IDLE_MODE = 400;
    public static final int DRAW_MODE = 402;
    public static final int EDIT_MODE = 404;
    
    public static enum TOOLS {
        FREESHAPE, LINE, RECT, OVAL, CURVE, 
        ERASER, FILL
    };
    
    public Model(View view) {
        this.view = view;
        type = TOOLS.FREESHAPE;
        brushType = ViewConstants.STROKE_PRESET_MODEL.PLAIN;
        
        strokeSize = DEF_STROKE_SIZE;
        alpha = 0xff;
        
        mode = IDLE_MODE;
        
        CANVAS_W = DEF_IMAGE_W;
        CANVAS_H = DEF_IMAGE_H;
              
        primaryColor = DEF_PRIMARY_COLOR;
        secondaryColor = DEF_SECONDARY_COLOR;
        padColor = DEF_PAD_COLOR;
        
        fill = false;
        antialias = true;
        updateStroke();
        
        undoManager = new UndoManager();       
        docModel = new DocumentModel(view);
    }
    
    private void updateStroke() {
        switch(brushType) {
            case PLAIN:
                brush = new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, 
                        BasicStroke.JOIN_ROUND);
                break;
            case WOBBLE:
                brush = new WobbleBrush(strokeSize);
                break;
            case ZIGZAG:
                brush = new ZigZagBrush(new BasicStroke(strokeSize, 
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                break;
            case SHAPE:
                brush = new ShapeBrush(new Shape[] {
                    new Star(0, 0, 5, strokeSize, .4f * strokeSize).getShape(),
                    new Ellipse2D.Float(0, 0, strokeSize, strokeSize)
                }, strokeSize * ShapeBrush.APPARENT_GAP);
                break;
                
            case TXT:
                brush = new TextBrush(DEF_BRUSH_FONT.deriveFont(strokeSize + 8), 
                        (strokeSize + 8) * TextBrush.APPARENT_GAP);
        }
        
        if(shape != null && mode != IDLE_MODE) {
            shape.setStroke(brush);
            view.notifyRepaint();
        }
    }
    
    public int getWidth() { return CANVAS_W; }
    public int getHeight() { return CANVAS_H; }
    
    public static int getMode() {
        return mode;
    }
    
    public static Color getPrimaryColor() {
        return primaryColor;
    }
    
    public static Color getSecondaryColor() {
        return secondaryColor;
    }
    
    public static Color getPadColor() {
        return padColor;
    }
    
    public DocumentModel getDocumentModel() {
        return docModel;
    }
    
    public UndoManager getUndoManager() {
        return undoManager;
    }
    
    public boolean getFill() {
        return fill;
    }
    
    public int getAlpha() {
        return alpha;
    }
    
    public ViewConstants.STROKE_PRESET_MODEL getBrushType() {
        return brushType;
    }
    
    public Stroke getStroke() {
        return brush;
    }
    
    public float getStrokeSize() {
        return strokeSize;
    }
    
    public Shapes getElement() {
        return shape;
    }
    
    public TOOLS getToolType() {
        return type;
    }
    
    public boolean getAntialias() {
        return antialias;
    }
    
    public Rectangle getExtendedBounds() {
        Rectangle r = shape.getBounds();

        int minx = r.x < 0 ? 0 : r.x;
        int miny = r.y < 0 ? 0 : r.y;
                
        if (mode == DRAW_MODE) {
            r.setBounds((int) (minx - strokeSize / 2 - Control.SIZE),
                    (int) (miny - strokeSize / 2 - Control.SIZE),
                    (int) (r.getMaxX() + strokeSize + Control.SIZE * 2),
                    (int) (r.getMaxY() + strokeSize + Control.SIZE * 2));
        } else if (mode == EDIT_MODE) {

            if (!SStatus.usable) {
                r.setBounds((int) (minx - strokeSize / 2 - Control.SIZE),
                        (int) (miny - 25 - strokeSize / 2 - Control.SIZE),
                        (int) (r.getMaxX() + strokeSize + Control.SIZE * 2),
                        (int) (r.getMaxY() + strokeSize + Control.SIZE * 2 + 25));
            } else {
                minx = SStatus.DRAGX < 0 ? 0 : SStatus.DRAGX < minx ? SStatus.DRAGX : minx; 
                miny = SStatus.DRAGY < 0 ? 0 : SStatus.DRAGX < miny ? SStatus.DRAGY : miny;
                
                r.setBounds((int) (minx - strokeSize / 2 - Control.SIZE),
                        (int) (miny - 25 - strokeSize / 2 - Control.SIZE),
                        (int) (Math.max(r.getMaxX(), SStatus.DRAGX) + strokeSize + Control.SIZE * 2),
                        (int) (Math.max(r.getMaxY(), SStatus.DRAGY) + strokeSize + Control.SIZE * 2 + 25));
            }
        }

        return r;
    }
    
    public BufferedImage getImage() {
        if(image == null || CANVAS_W != image.getWidth() || CANVAS_H != image.getHeight()) {
            image = new BufferedImage(CANVAS_W, CANVAS_H, BufferedImage.TYPE_INT_ARGB_PRE);
            
            Graphics g = image.getGraphics();
            g.setColor(padColor);
            g.fillRect(0, 0, CANVAS_W, CANVAS_H);
            
            g.dispose();
        }
        
        return image;
    }
    
    public void setDimensions(int w, int h) {
        if (CANVAS_W != w || CANVAS_H != h) {
            CANVAS_W = w;
            CANVAS_H = h;
            
            view.notifyDimensionUpdate();
            view.notifyRepaint();
        }
    }
    
    public void setPrimaryColor(Color c) {
        if(primaryColor != c) {
            primaryColor = c;
            
            if (shape != null && mode != IDLE_MODE) {
                shape.setColor(primaryColor);
                view.notifyRepaint();
            }
        }
    }
    
    public void setAlpha(int alpha) {
        if(this.alpha != alpha) {
            this.alpha = alpha;
            
            int r = primaryColor.getRed();
            int g = primaryColor.getGreen();
            int b = primaryColor.getBlue();
            setPrimaryColor(new Color(r, g, b, alpha));
        }
    }
    
    public void setSecondaryColor(Color c) {
        secondaryColor = c;
    }
    
    public void setPadColor(Color c) {
        if(padColor != c) {
            padColor = c;
            view.notifyRepaint();
        }
    }

    public void setStrokeSize(float f) {
        if(strokeSize != f) {
            strokeSize = f;
            
            updateStroke();           
        }
    }
    
    public void setFill(boolean fill) {
        if(this.fill != fill) {
            this.fill = fill;
            
            if (shape != null && mode != IDLE_MODE) {
                shape.setFill(fill);
                view.notifyRepaint();
            }
        }
    }
    
    public void setMode(int mode) {
        Model.mode = mode;
        
        switch(mode) {
            case IDLE_MODE:
                view.setFlips(false);
                view.setRotates(false);
                break;
            case EDIT_MODE:
                view.setFlips(true);
                view.setRotates(true);
        }
    }
    
    public void setBrushType(ViewConstants.STROKE_PRESET_MODEL type) {
        if(brushType != type) {
            brushType = type;
            
            updateStroke();
        }
    }
    
    public void setAntialias(boolean alias) {
        if(antialias != alias) {
            antialias = alias;
            
            if (shape != null) {
                shape.setAntialias(alias);
                view.notifyRepaint();
            }
            view.notifyRepaint();
        }
    }
    
    public void setToolType(TOOLS elm) {
        if(type != elm) {
            if(mode != IDLE_MODE)
                validateTemporaryShape();
            
            type = elm;
            view.updateShapeInterface();
        }
    }
    
    public void copy() {
        if(mode != EDIT_MODE || shape.locked())
            return;
        
        if(shape != null) {
            copyShape = shape;
        }
    }
    
    public void paste() {
        if(copyShape == null || mode == DRAW_MODE)
            return;
        
        if(shape != null && mode == EDIT_MODE) {
            validateTemporaryShape();
        }
        
        setMode(EDIT_MODE);
        shape = copyShape.clone();
        shape.setCopy(true);
        
        view.updateShapeInterface(shape.getType());
        view.forceEditMode();
        
        view.notifyRepaint();
    }
    
    public void flipShape(int type) {
        if (mode == EDIT_MODE && shape != null) {
            switch (type) {
                case View.FlipAction.HFLIP:
                    shape.hflip();
                    break;
                case View.FlipAction.VFLIP:
                    shape.vflip();
                    break;
            }
            
            view.forceControlUpdate();
            view.notifyRepaint();
        }
    }
    
    public void rotateShape(int type) {
        if (mode == EDIT_MODE && shape != null) {
            switch (type) {
                case View.RotateAction.ROTL:
                    shape.rot_left();
                    break;
                case View.RotateAction.ROTR:
                    shape.rot_right();
                    break;
            }
            
            view.forceControlUpdate();
            view.notifyRepaint();
        }
    }
    
    public void clearPad() {
        if(mode == IDLE_MODE) {
            shape = new Rect(new Point(-1, -1), new Point(CANVAS_W + 1, CANVAS_H + 1));
            shape.setColor(padColor);
            shape.setFill(true);
            shape.setStroke(brush);
            
            validateTemporaryShape();
            shape = null;
        }
    }
    
    public void validateTemporaryShape() {
        if(mode != IDLE_MODE) {
            resetModel();
        }
        
        registerShape(shape);
        view.notifyRepaint();
    }
    
    public void resetModel() {
        setMode(IDLE_MODE);
        updateCursor(Cursor.getDefaultCursor());
    }
    
    public void registerShape(Shapes s) {
        if (s != null) {
            if(s.isCopy()) {
                view.updateShapeInterface();
                s.setCopy(false);
            }
            
            undoManager.add(new ShapeAction(s));

            Graphics2D g2 = getImage().createGraphics();
            if (s.getAntialias()) {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            } else {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }

            g2.setPaint(s.getColor());
            g2.setStroke(s.getStroke());

            s.paint(g2);
            g2.dispose();
            
            docModel.setChanged();
            view.updateURState(undoManager.getAccessibleUndoModel());
        }
    }
    
    public void performUndo() {
        // Disable undo when a shape is being drawn
        if(mode != IDLE_MODE) {
            return;
        }
        
        if(undoManager.canUndo()) {
            undoManager.undo();
            redrawImage();
         
            view.notifyRepaint();
            docModel.setChanged();
            
            view.updateURState(undoManager.getAccessibleUndoModel());
        }
    }
    
    public void performRedo() {
        // Disable redo when a shape is being drawn
        if(mode != IDLE_MODE) {
            return;
        }
        
        if(undoManager.canRedo()) {
            undoManager.redo();
            redrawImage();
         
            view.notifyRepaint();
            docModel.setChanged();
            
            view.updateURState(undoManager.getAccessibleUndoModel());
        }
    }
    
    public void performFill(Point p) {
        FillAction fa = new FillAction(primaryColor, p);
        fa.execute(null, this);
        
        undoManager.add(fa);
        view.updateURState(undoManager.getAccessibleUndoModel());
        
        view.notifyRepaint();
    }
    
    public void clearAllElements() {
        // Disable undo when a shape is being drawn
        if(mode != IDLE_MODE) {
            return;
        }
        
        if(!undoManager.isEmpty()) {
            undoManager.clear();
            
            view.notifyRepaint();
            view.updateURState(undoManager.getAccessibleUndoModel());
        }
    }
    
    public void removeTemporaryElement() {
        if(mode == EDIT_MODE && !shape.locked()) {
            if(shape.isCopy()) {
                view.updateShapeInterface();
            }
            
            shape = null;
            resetModel();
            
            view.notifyTemporaryShapeRemove();
            view.notifyRepaint();
        }
    }
    
    public void updateCursor(Cursor cursor) {
        view.notifyCursorUpdate(cursor);
    }
    
    public void createNewDocument(Dimension dim) {
        if(mode == DRAW_MODE)
            return;
        
        if(mode == EDIT_MODE) 
            removeTemporaryElement();
            
        clearAllElements();
        docModel.resetModel();
        
        setDimensions(dim.width, dim.height);
    }
    
    public void createNewElement(Point s, Point e) {
        switch(type) {
            case FREESHAPE:
                shape = new FreeShape(s);
                break;
            case LINE:
                shape = new Line(s, e);
                break;
            case RECT:
                shape = new Rect(s, e);
                break;
            case OVAL:
                shape = new Oval(s, e);
                break;
            case CURVE:
                shape = new Curve(s, e);
                break;
            case ERASER:
                shape = new Eraser(s);
                break;
        }
        
        updateElementProperties();
    }
    
    public void updateElementProperties() {
        shape.setColor(primaryColor);
        shape.setStroke(brush);
        shape.setFill(fill);
        shape.setAntialias(antialias);
    }
    
    public void paint(Graphics g) {
        g.drawImage(getImage(), 0, 0, null);
        
        if(mode != IDLE_MODE)
            paintTemp(g);
    }
    
    public void repaint() {
        view.notifyRepaint();
    }
    
    private void paintTemp(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        if (shape.getAntialias()) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        g2.setPaint(shape.getColor());
        g2.setStroke(shape.getStroke());

        shape.paint(g2);
        
        if(mode == EDIT_MODE)
            view.notifyControlsPaint(g2);
    }
    
    /**
     * Used mainly with undo command.
     * Redraws all the shapes in the history list
     * to the BufferedImage.
     */
    private void redrawImage() {
        Graphics2D g2 = getImage().createGraphics();
        g2.setColor(padColor);
        g2.fillRect(0, 0, getImage().getWidth(), getImage().getHeight());
        
        undoManager.traverseList(g2, this);
        
        g2.dispose();
    }
}
