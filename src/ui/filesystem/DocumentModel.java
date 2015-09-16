package ui.filesystem;

import ui.View;

/**
 *
 * @author Mithusayel Murmu
 */
public class DocumentModel {
    private String name;
    private boolean changed;
    
    private View parent;
    
    public DocumentModel(View parent) {
        this.parent = parent;
        name = "Untitled";
        changed = true;
        
        update();
    }
    
    public void resetModel() {
        name = "Untitled";
        changed = true;
        
        parent.notifyIOReset();
        update();
    }
    
    public void setChanged() {
        if (!changed) {
            changed = true;
            update();
        }
    }
    
    public void setSaved(String fileName) {
        if (changed) {
            changed = false;
            name = fileName;
            update();
        }
    }
    
    public boolean isChanged() {
        return changed;
    }
    
    public final void update() {
        if(changed) {
            parent.setTitle(parent.getApplication().
                    getName() + " - " + name + "*");
        } else {
            parent.setTitle(parent.getApplication().
                    getName() + " - " + name);
        }
    }
}
