package ui.filesystem.filters;

import java.io.File;

/**
 *
 * @author Mithusayel Murmu
 */
public class JPEGFilter extends ImageFilter {
    
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String ext = getExtension(f.getPath());
        switch(ext) {
            case "jpg":
            case "jpeg":
                return true;
        } 
        
        return false;
    }
    
    @Override
    public String getSupportedFormat() {
        return "jpeg";
    }

    @Override
    public String getDescription() {
        return "JPG, JPEG Files";
    }
    
}
