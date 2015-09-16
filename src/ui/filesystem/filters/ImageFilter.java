package ui.filesystem.filters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Mithusayel Murmu
 */
public abstract class ImageFilter extends FileFilter {
    
    public ImageFilter() {
        registerFilter();
    }
    
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        if(getExtension(f.getPath()).equals(getSupportedFormat()))
            return true;
        
        return false;
    }
    
    private void registerFilter() {
        ImageFilterRegister.
                registerImageFilter(getClass(), getSupportedFormat());
    }
    
    public abstract String getSupportedFormat();

    public final String getExtension(String path) {
        int idx = path.lastIndexOf(".");

        return path.substring(idx + 1).toLowerCase();
    }
}
