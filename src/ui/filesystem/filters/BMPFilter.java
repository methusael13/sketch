package ui.filesystem.filters;

/**
 *
 * @author Mithusayel Murmu
 */
public class BMPFilter extends ImageFilter {
    
    @Override
    public String getSupportedFormat() {
        return "bmp";
    }

    @Override
    public String getDescription() {
        return "BMP Files";
    }
}
