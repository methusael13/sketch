package core.undo;

import core.Model;
import java.awt.Graphics2D;
import java.util.ArrayList;
import ui.View;

/**
 *
 * @author: Mithusayel Murmu
 */
public class UndoManager {
    
    private static final int MAX_LIMIT = 25; 
            
    private UndoableAction[] undoList;
    private ArrayList<UndoableAction> discarded;
    
    private int idx;
    private int max_idx;
    
    private AccessibleUndoModel model;
    
    public UndoManager() {
        model = new AccessibleUndoModel();
        
        undoList = new UndoableAction[MAX_LIMIT];
        discarded = new ArrayList<>();
        
        idx = MAX_LIMIT - 1;
        max_idx = MAX_LIMIT;
    }
    
    public AccessibleUndoModel getAccessibleUndoModel() {
        return model;
    }
    
    public void add(UndoableAction action) {
        if (idx >= max_idx) {
            removeUnreachableElements();
            
            model.setRedoTip(View.URAction.REDO_DESC);
            model.setRedoEnabled(false);
        }
        
        if (idx < 0) {
            idx = 0;
            shiftList(1);
        }
        
        undoList[max_idx = idx--] = action;
        
        model.setUndoTip("Undo " + action.getActionDescription());
        model.setUndoEnabled(true);
    }
    
    public boolean isEmpty() {
        return max_idx == MAX_LIMIT;
    }
    
    public void clear() {
        idx = MAX_LIMIT - 1;
        max_idx = MAX_LIMIT;
        
        for (int i = 0; i < undoList.length; i++) {
            undoList[i] = null;
        }
        
        model.setRedoEnabled(false);
        model.setUndoEnabled(false);
        
        model.setUndoTip(View.URAction.UNDO_DESC);
        model.setRedoTip(View.URAction.REDO_DESC);
    }
    
    public boolean canUndo() {
        return idx < MAX_LIMIT - 1;
    }
    
    public boolean canRedo() {
        return idx > max_idx - 1;
    }
    
    public boolean undo() {
        if (idx < MAX_LIMIT-1) {
            ++idx;
            
            if (canUndo()) {
                model.setUndoTip("Undo " + undoList[idx + 1].getActionDescription());
            } else {
                model.setUndoTip(View.URAction.UNDO_DESC);
                model.setUndoEnabled(false);
            }
            
            model.setRedoEnabled(true);
            model.setRedoTip("Redo " + undoList[idx].getActionDescription());
            return true;
        }
        
        return false;
    }
    
    public boolean redo() {
        if (idx > max_idx - 1) {
            --idx;
            
            if (canRedo()) {
                model.setRedoTip("Redo " + undoList[idx].getActionDescription());
            } else {
                model.setRedoTip(View.URAction.REDO_DESC);
                model.setRedoEnabled(false);
            }
            
            model.setUndoEnabled(true);
            model.setUndoTip("Undo " + undoList[idx + 1].getActionDescription());
            return true;
        }
        
        return false;
    }
    
    public void traverseList(Graphics2D g2, Model model) {
        if (!discarded.isEmpty()) {
            for (UndoableAction act : discarded) {
                act.execute(g2, model);
            }
        }
        
        for (int i = MAX_LIMIT - 1; i > idx; i--) {
            undoList[i].execute(g2, model);
        }
    }
    
    private void shiftList(int dx) {
        if (dx > -1) {
            for (int i = 1; i <= dx; i++) {
                discarded.add(undoList[undoList.length - i]);
            }
            
            for (int i = undoList.length - 1; i >= 0; i--) {
                undoList[i] = null;
                undoList[i] = (i - dx) > -1 ? undoList[i - dx] : null;
            }
        }
    }
    
    public void removeUnreachableElements() {
        for (int i = idx; i >= max_idx; --i) {
            undoList[i] = null;
        }
    }
    
    public class AccessibleUndoModel {
        protected String undoTip;
        protected String redoTip;
        protected boolean undoEnabled;
        protected boolean redoEnabled;

        public String getUndoTip() {
            return undoTip;
        }

        public String getRedoTip() {
            return redoTip;
        }

        public boolean isUndoEnabled() {
            return undoEnabled;
        }

        public boolean isRedoEnabled() {
            return redoEnabled;
        }

        public void setUndoTip(String undoTip) {
            this.undoTip = undoTip;
        }

        public void setRedoTip(String redoTip) {
            this.redoTip = redoTip;
        }

        public void setUndoEnabled(boolean undoEnabled) {
            this.undoEnabled = undoEnabled;
        }

        public void setRedoEnabled(boolean redoEnabled) {
            this.redoEnabled = redoEnabled;
        }
    }
}
