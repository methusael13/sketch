package core.undo;

import core.Model;
import java.awt.Graphics2D;

/**
 *
 * @author Mithusayel Murmu
 */
public abstract class UndoableAction {
    public static final int SHAPE_ACTION = 1;
    public static final int FILL_ACTION = 2;
    public static final int ERASE_ACTION = 3;
    public static final int UNDEFINED_ACTION = 4;
    
    protected int type = UNDEFINED_ACTION;
    protected String description;
    
    public void setType(int type) {
        this.type = type;
    }
    
    public int getType() {
        return type;
    }
    
    public String getActionDescription() {
        return description;
    }
    
    public abstract void execute(Graphics2D g, Model model);
}
