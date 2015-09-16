package ui.filesystem;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

/**
 *
 * @author Mithusayel Murmu
 */
public class ImageFilePreview extends JComponent implements PropertyChangeListener {
    
    private File file;
    private ImageIcon thumbnail;
    
    public ImageFilePreview(final JFileChooser chooser) {
        setPreferredSize(new Dimension(200, 150));
        chooser.addPropertyChangeListener(ImageFilePreview.this);
    }
    
    public void loadImage() {
        if(file == null) {
            thumbnail = null;
            return;
        }
        
        ImageIcon icon = new ImageIcon(file.getPath());
        if(icon != null) {
            if(icon.getIconWidth() > 190) {
                thumbnail = new ImageIcon(icon.getImage().
                        getScaledInstance(190, -1, Image.SCALE_DEFAULT));
            } else {
                thumbnail = icon;
            }
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        boolean valid = false;
        String prop = e.getPropertyName();
        
        switch (prop) {
            case JFileChooser.DIRECTORY_CHANGED_PROPERTY:
                file = null;
                valid = true;
                break;
            case JFileChooser.SELECTED_FILE_CHANGED_PROPERTY:
                file = (File)e.getNewValue();
                valid = true;
                break;
        }
        
        if(valid) {
            thumbnail = null;
            if(isShowing()) {
                loadImage();
                repaint();
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        if(thumbnail == null)
            loadImage();
        
        if (thumbnail != null) {
            int x = (getWidth() - thumbnail.getIconWidth())/2;
            int y = (getHeight() - thumbnail.getIconHeight())/2;

            if (y < 0) {
                y = 0;
            }

            if (x < 5) {
                x = 5;
            }
            thumbnail.paintIcon(this, g, x, y);
        }
    }
}

