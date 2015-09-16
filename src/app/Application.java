package app;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import javax.swing.SwingUtilities;
import resources.ViewConstants;
import ui.View;
import ui.filesystem.IOUtilities;

/**
 *
 * @author Mithusayel Murmu
 */
public class Application {
    private View view;
    private IOUtilities io;
    
    public Application() {   
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view = new View(Application.this, ViewConstants.WINDOW_TITLE);
                io = new IOUtilities(view);
                
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {}
                view.setVisible(true);
            }
        });
    }
    
    /**
     * Desc:
     * <pre>
     * Returns the handle to the file last saved and
     * is currently being worked on. Returns null, if
     * no file has yet been saved.
     * </pre>
     * 
     * @return 
     */
    public File getCurrentFileHandle() {
        return io.getFileHandle();
    }
    
    public String getFileFormat() {
        return io.getFileFormat();
    }
    
    public String getName() {
        return ViewConstants.WINDOW_TITLE;
    }
    
    public View getView() {
        return view;
    }
    
    public void resetIO() {
        io.resetData();
    }
    
    public void exit(int code) {
        System.exit(code);
    }
    
    public void save(int type) {
        io.setSaveType(type);

        switch (type) {
            case View.SaveAction.SAVE:
                File f = getCurrentFileHandle();

                if (f == null) {
                    showSave(view);
                } else {
                    if (view.getModel().getDocumentModel().isChanged()) {
                        save(f, getFileFormat());
                    }
                }
                break;

            case View.SaveAction.SAVE_AS:
                showSave(view);
        }
    }
    
    public void save(File f, String format) {
        io.save(f, format);
    }
    
    public void showSave(Frame frame) {
        try {
            io.showSaveDialog(frame);
        } catch (IOException e) {}
    }
}
