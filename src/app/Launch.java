package app;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author Mithusayel Murmu
 */
public class Launch {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Executor exe = Executors.newCachedThreadPool();
                Launcher launch = new Launcher(Launch.class.getClassLoader());
                exe.execute(launch);
            }
        });
    }
    
    private static String getRootPackage(Class cl) {
        Package pack = cl.getPackage();
        if(pack == null)
            return null;
        
        String name = pack.getName();
        if(name.contains("."))
            name = name.substring(0, name.indexOf("."));
        
        return name;
    }
    
    private static class Launcher extends SwingWorker<Void, ProgressInfo> {
        private ClassLoader loader;
        private Class[] cl;
        
        public Launcher(ClassLoader loader) {
            this.loader = loader;
            cl = new Class[60];
        }
        
        @Override
        protected Void doInBackground() {
            float factor = 100f / cl.length;

            for (int i = 0; i < cl.length; i++) {
                try {
                    cl[i] = loader.loadClass(classNames[i]);
                } catch(ClassNotFoundException e) {
                    Logger.getLogger(Launch.class.getName()).log(
                            Level.SEVERE, "Start up failed!", e);
                }

                publish(new ProgressInfo((int) ((i + 1) * factor), getRootPackage(cl[i])));
            }
            
            return null;
        }
        
        @Override
        protected void process(List<ProgressInfo> data) {
            ProgressInfo pi = data.get(data.size() - 1);
            int prog = pi.progress;
            
            System.out.format("Loading... %s (%d%c)", pi.status, prog, '%');
        }
        
        @Override
        protected void done() {
            try {
                Application app = (Application) cl[0].newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(Launch.class.getName()).log(Level.SEVERE, null, ex);
            }
        }      
    }
    
    private static class ProgressInfo {
        protected String status;
        protected int progress;
        
        public ProgressInfo(int progress, String status) {
            this.progress = progress;
            this.status = status;
        }
    }
    
    private static String[] classNames = 
    {
        "app.Application",
        "core.Model",
        "core.geom.Curve",
        "core.geom.FreeShape",
        "core.geom.Line",
        "core.geom.Oval",
        "core.geom.Rect",
        "core.geom.Shapes",
        "core.geom.Star",
        "core.geom.brush.ShapeBrush",
        "core.geom.brush.TextBrush",
        "core.geom.brush.WobbleBrush",
        "core.geom.brush.ZigZagBrush",
        "core.geom.tools.Eraser",
        "core.geom.tools.EraserInterface",
        "core.modules.FloodFill",
        "core.shape_interface.ActionInterface",
        "core.shape_interface.CurveInterface",
        "core.shape_interface.FillInterface",
        "core.shape_interface.FreeShapeInterface",
        "core.shape_interface.LineInterface",
        "core.shape_interface.OvalInterface",
        "core.shape_interface.PadInterface",
        "core.shape_interface.RectInterface",
        "core.shape_interface.editor.Control",
        "core.shape_interface.editor.SStatus",
        "core.shape_interface.editor.ShapeEditInterface",
        "core.shape_interface.editor.ShapeEditor",
        "core.shape_interface.editor.geom.BoxShapeEditor",
        "core.shape_interface.editor.geom.CurveShapeEditor",
        "core.shape_interface.editor.geom.LineShapeEditor",
        "core.undo.FillAction",
        "core.undo.ShapeAction",
        "core.undo.UndoManager",
        "core.undo.UndoableAction",
        "resources.ImageResource",
        "resources.ModelConstants",
        "resources.PadConstants",
        "resources.ResourceLoader",
        "resources.ViewConstants",
        "ui.Pad",
        "ui.PropertiesPanel",
        "ui.StatusPanel",
        "ui.View",
        "ui.filesystem.DocumentModel",
        "ui.filesystem.IOUtilities",
        "ui.filesystem.ImageFilePreview",
        "ui.filesystem.filters.BMPFilter",
        "ui.filesystem.filters.GIFFilter",
        "ui.filesystem.filters.ImageFilter",
        "ui.filesystem.filters.ImageFilterRegister",
        "ui.filesystem.filters.JPEGFilter",
        "ui.filesystem.filters.PNGFilter",
        "ui.modules.BrushPanel",
        "ui.modules.ColorSetPanel",
        "ui.modules.ColorWheelPanel",
        "ui.modules.LeftToolBar",
        "ui.modules.MemoryInfo",
        "ui.modules.NewDocumentModule",
        "ui.modules.TopToolBar"
    };
}
