package ui.filesystem.filters;

import java.util.ArrayList;

/**
 *
 * @author Mithusayel Murmu
 */
public class ImageFilterRegister {
    
    private static ArrayList<FilterInfo> installed = new ArrayList<>();
    
    public static void registerImageFilter(
            Class<? extends ImageFilter> filter, String format) {
        installed.add(new FilterInfo(filter, format));
    }
    
    public static ArrayList<FilterInfo> getInstalledFilters() {
        return installed;
    }
    
    public static boolean isSupportedFormat(String f) {
        if(installed == null)
            return false;
        
        boolean supported = false;
        f = f.toLowerCase();
        
        for(FilterInfo info : installed) {
            if(info.getFormat().equals(f)) {
                supported = true;
                break;
            }
        }
        
        return supported;
    }
    
    public static class FilterInfo {
        private String format;
        private Class<? extends ImageFilter> filter;
        
        public FilterInfo(Class<? extends ImageFilter> filter, String format) {
            this.filter = filter;
            this.format = format;
        }
        
        public String getFormat() {
            return format;
        }
        
        public Class<? extends ImageFilter> getFilterClass() {
            return filter;
        }
    }
}
