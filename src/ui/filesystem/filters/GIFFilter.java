package ui.filesystem.filters;

/**
 *
 * @author Mithusayel Murmu
 */
public class GIFFilter extends ImageFilter {
    
    @Override
    public String getSupportedFormat() {
        return "gif";
    }

    @Override
    public String getDescription() {
        return "GIF Files";
    }
}
