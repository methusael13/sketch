package ui.filesystem.filters;

/**
 *
 * @author Mithusayel Murmu
 */
public class PNGFilter extends ImageFilter {

    @Override
    public String getSupportedFormat() {
        return "png";
    }

    @Override
    public String getDescription() {
        return "PNG Files";
    }
}
