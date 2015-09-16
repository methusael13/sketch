package resources;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Mithusayel Murmu
 */
public class ResourceLoader {
    
    public static Image loadImage(Class<?> cls, String path) {
        final ClassLoader loader = cls.getClassLoader();
        final InputStream in = loader.getResourceAsStream(path);
        
        if(in == null)
            return null;
        
        Image img = null;
        try {
            img = ImageIO.read(in);
        } catch(IOException e) {}
        
        return img;
    }
    
    public static ImageIcon loadIcon(Class<?> cls, String path) {
        final Image img = loadImage(cls, path);
        
        if(img == null)
            return null;
        
        return new ImageIcon(img);
    }
    
    public static Properties loadProperties(Class<?> cls, String path) {
        final ClassLoader loader = cls.getClassLoader();
        final InputStream in = loader.getResourceAsStream(path);
        
        Properties props = new Properties();
        try {
            props.load(in);
        } catch(IOException e) { 
            System.err.println("Missing properties file - " + path);
            return null;
        }
        
        return props;
    }
}
