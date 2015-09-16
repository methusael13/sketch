package ui.filesystem;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import ui.View;
import ui.filesystem.filters.BMPFilter;
import ui.filesystem.filters.GIFFilter;
import ui.filesystem.filters.ImageFilter;
import ui.filesystem.filters.ImageFilterRegister;
import ui.filesystem.filters.JPEGFilter;
import ui.filesystem.filters.PNGFilter;

public class IOUtilities {

    private JFileChooser saveChooser;
    private JFileChooser openChooser;
    
    private ImageFilter[] filters;
    
    private View view;
    private File fileHandle;
    private String fileFormat;
    
    private int type;

    public IOUtilities(View view) {
        this.view = view;
        type = View.SaveAction.SAVE;
        
        saveChooser = new JFileChooser();
        openChooser = new JFileChooser();
        
        filters = new ImageFilter[4];
        filters[0] = new PNGFilter();
        filters[1] = new JPEGFilter();
        filters[2] = new GIFFilter();
        filters[3] = new BMPFilter();

        openChooser.setAccessory(new ImageFilePreview(openChooser));
        saveChooser.setAccessory(new ImageFilePreview(saveChooser));
        
        for(int i = 0; i < filters.length; ++i) {
            saveChooser.addChoosableFileFilter(filters[i]);
            openChooser.addChoosableFileFilter(filters[i]);
        }
    }
    
    public void resetData() {
        fileHandle = null;
        fileFormat = null;
    }
    
    public File getFileHandle() {
        return fileHandle;
    }
    
    public String getFileFormat() {
        return fileFormat;
    }
    
    public void setSaveType(int type) {
        this.type = type;
    } 

    public void save(File file, String format) {
        
        try {
            ImageIO.write(view.getModel().getImage(), format, file);
        } catch (IOException e) {}
        
        if (type == View.SaveAction.SAVE) {
            fileHandle = null;
            fileFormat = null;
            
            String name = file.getName();
            view.getModel().getDocumentModel().setSaved(name);

            fileHandle = file;
            fileFormat = format;
        }
    }

    public void showSaveDialog(Frame frame) throws IOException {
        if (saveChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION
                && saveChooser.getSelectedFile() != null) {

            File f = saveChooser.getSelectedFile();
            String format = inferFileFormat(f);
            
            if (format == null || format.isEmpty() || !ImageFilterRegister.isSupportedFormat(format)) {
                if (saveChooser.getFileFilter() != saveChooser.getAcceptAllFileFilter()) {
                    format = ((ImageFilter) saveChooser.getFileFilter()).getSupportedFormat();
                } else {
                    format = "png";
                }
            }
            
            if(!format.equalsIgnoreCase(inferFileFormat(f))) {
                f = new File(getVisualPath(f).concat(".").concat(format));
            }

            if (f.exists()) {
                String overWriteMSG = "\"" + f.getPath() + "\" already exists! Overwrite it?";
                if (JOptionPane.showConfirmDialog(frame, overWriteMSG) != JOptionPane.OK_OPTION) {
                    return;
                }
            } else {
                if (!f.createNewFile()) {
                    JOptionPane.showMessageDialog(frame, "Failed to create file!");
                    return;
                }
            }

            save(f, format);
        }
    }
    
    private String inferFileFormat(File file) {
        String name = file.getName();
        int idx;
        
        if((idx = name.lastIndexOf(".")) != -1)
            return name.substring(idx + 1);
        
        return null;
    }
    
    private String getVisualPath(File file) {
        String path = file.getPath();
        int idx;
        
        if((idx = path.lastIndexOf(".")) != -1)
            return path.substring(0, idx);
        
        return path;
    }
}
