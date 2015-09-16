package ui;

import app.Application;
import core.Model;
import core.undo.UndoManager;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import resources.ImageResource;
import resources.PadConstants;
import resources.ResourceLoader;
import resources.ViewConstants;
import ui.modules.LeftToolBar;
import ui.modules.NewDocumentModule;
import ui.modules.TopToolBar;

/**
 * <pre>
 * Menu Structure (For reference only):
 *
 * File (Alt + F)
 *      Save (Ctrl + S)
 *      Exit (Ctrl + Q)
 *
 * Edit (Alt + E) 
 *      Undo (Ctrl + Z)
 *      Copy (Ctrl + C)
 *      Paste (Ctrl + P)
 * 
 * </pre>
 * @author Mithusayel Murmu
 */
public final class View extends JFrame {

    private Application app;
    private Model model;
    protected Pad pad;

    public View(final Application app, String name) {
        super(name);
        
        this.app = app;
        model = new Model(View.this);
        
        setIconImage(ResourceLoader.loadImage(getClass(), ImageResource.TITLE_ICON));
        setSize(ViewConstants.WINDOW_WIDTH, ViewConstants.WINDOW_HEIGHT);
        setLocation(ViewConstants.WINDOW_LOCX, ViewConstants.WINDOW_LOCY);

        try {
            boolean nimbus = false;
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ((info.getName()).equals("GTK+")) {
                    UIManager.setLookAndFeel(info.getClassName());
                    nimbus = true;
                    break;
                }
            }

            if (!nimbus) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Logger.getLogger(View.class.getName()).
                        log(Level.INFO, "Using System Look & Feel.", nimbus);
            }
        } catch (Exception e) {}

        initListeners();
        initActions();
        initMenubar();
        initGUIComponents();
    }

    private void initMenubar() {
        menubar = new JMenuBar();

        // Create the File menu
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem newItem = new JMenuItem(newAction);
        saveItem = new JMenuItem(saveAction);
        saveAsItem = new JMenuItem(saveAsAction);
        exitItem = new JMenuItem(exitAction);
        
        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Create the Edit Menu
        editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);

        undoItem = new JMenuItem(undoAction);
        redoItem = new JMenuItem(redoAction);
        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.addSeparator();
        editMenu.add(new JMenuItem(cAction));
        editMenu.add(new JMenuItem(pAction));

        menubar.add(fileMenu);
        menubar.add(editMenu);
        setJMenuBar(menubar);
    }

    private void initGUIComponents() {
        initTopToolbar();
        initLeftToolbar();

        ParentPanel parentPanel = new ParentPanel(new FlowLayout());

        pad = new Pad(View.this, parentPanel);
        parentPanel.add(pad, BorderLayout.CENTER);

        JScrollPane pane = new JScrollPane();
        pane.getViewport().setView(parentPanel);
        pane.getHorizontalScrollBar().setUnitIncrement(18);
        pane.getVerticalScrollBar().setUnitIncrement(18);

        propPanel = new PropertiesPanel(model);
        propPanel.setTitle("Properties - Tools");
        
        sPanel = new StatusPanel(View.this);
        
        getContentPane().add(pane, BorderLayout.CENTER);
        getContentPane().add(propPanel, BorderLayout.EAST);
        getContentPane().add(sPanel, BorderLayout.SOUTH);
    }

    private void initTopToolbar() {
        saveButton = new JButton(saveAction);
        saveButton.setOpaque(false);
        saveButton.setText(null);
        
        undoButton = new JButton(undoAction);
        undoButton.setOpaque(false);
        undoButton.setText(null);
        
        redoButton = new JButton(redoAction);
        redoButton.setOpaque(false);
        redoButton.setText(null);

        JButton hflipBut = new JButton(hflipAction);
        hflipBut.setOpaque(false);
        hflipAction.setEnabled(false);
        JButton vflipBut = new JButton(vflipAction);
        vflipBut.setOpaque(false);
        vflipAction.setEnabled(false);
        
        JButton rotlBut = new JButton(rotlAction);
        rotlBut.setOpaque(false);
        rotlAction.setEnabled(false);
        JButton rotrBut = new JButton(rotrAction);
        rotrBut.setOpaque(false);
        rotrAction.setEnabled(false);
        
        TopToolBar topToolbar = new TopToolBar();
        topToolbar.setFloatable(false);
        topToolbar.setRollover(true);
        topToolbar.setOrientation(JToolBar.HORIZONTAL);

        topToolbar.add(saveButton);
        topToolbar.addSeparator();
        topToolbar.add(undoButton);
        topToolbar.add(redoButton);
        topToolbar.addSeparator();
        topToolbar.add(hflipBut);
        topToolbar.add(vflipBut);
        topToolbar.add(rotlBut);
        topToolbar.add(rotrBut);

        getContentPane().add(topToolbar, BorderLayout.NORTH);
    }

    private void initLeftToolbar() {
        LeftToolBar leftToolbar = new LeftToolBar();
        leftToolbar.setFloatable(false);
        leftToolbar.setRollover(true);
        leftToolbar.setOrientation(JToolBar.VERTICAL);

        shapeButton = new JToggleButton[buttonToolsAction.length];
        ButtonGroup shapeBGroup = new ButtonGroup();

        for (int i = 0; i < shapeButton.length; i++) {
            shapeButton[i] = new JToggleButton(buttonToolsAction[i]);
            shapeBGroup.add(shapeButton[i]);
            shapeButton[i].setText(null);

            if (i == shapeButton.length - 2) {
                leftToolbar.addSeparator();
            }
            leftToolbar.add(shapeButton[i]);
        }
        shapeButton[0].setSelected(true);

        getContentPane().add(leftToolbar, BorderLayout.WEST);
    }

    private void initListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                app.exit(0);
            }
        });

        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                pad.requestFocusInWindow();
            }
        });
    }

    private void initActions() {
        // Initialize the actions
        buttonToolsAction = new ToolsAction[7];
        buttonToolsAction[0] = new ToolsAction("Free Shape", ImageResource.FS_ICON,
                Model.TOOLS.FREESHAPE, "Create a free hand shape");
        buttonToolsAction[1] = new ToolsAction("Rectangle", ImageResource.RECT_ICON,
                Model.TOOLS.RECT, "Create a Rectangle shape");
        buttonToolsAction[2] = new ToolsAction("Ellipse", ImageResource.ELLIPSE_ICON,
                Model.TOOLS.OVAL, "Create an Ellipse shape");
        buttonToolsAction[3] = new ToolsAction("Line", ImageResource.LINE_ICON,
                Model.TOOLS.LINE, "Create a Line");
        buttonToolsAction[4] = new ToolsAction("Curve", ImageResource.CURVE_ICON,
                Model.TOOLS.CURVE, "Create a Cubic Curve");
        buttonToolsAction[5] = new ToolsAction("Eraser", ImageResource.ERASER_ICON,
                Model.TOOLS.ERASER, "Erases the drawings");
        buttonToolsAction[6] = new ToolsAction("Flood Fill", ImageResource.FILL_ICON,
                Model.TOOLS.FILL, "Flood Fill");

        newAction = new NewFileAction("New", ImageResource.NEW_ICON, "Create New Document", 
                KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        
        exitAction = new ExitAction("Exit", ImageResource.EXIT_ICON,
                "Quit Application", KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        
        undoAction = new URAction("Undo", ImageResource.UNDO_ICON, "Undo the last step", 
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), URAction.UNDO);
        
        redoAction = new URAction("Redo", ImageResource.REDO_ICON, "Redo the undoed step", 
                KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), URAction.REDO);
        
        saveAction = new SaveAction("Save", ImageResource.SAVE_ICON, "Save Image", 
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), SaveAction.SAVE);
        
        saveAsAction = new SaveAction("Save As", ImageResource.SAVE_ICON, "Save Image As", 
                KeyStroke.getKeyStroke(KeyEvent.VK_S, 
                InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), SaveAction.SAVE_AS);
        
        cAction = new CPAction("Copy", null, KeyStroke.getKeyStroke(KeyEvent.VK_C, 
                InputEvent.CTRL_DOWN_MASK), CPAction.COPY);
        
        pAction = new CPAction("Paste", null, KeyStroke.getKeyStroke(KeyEvent.VK_V, 
                InputEvent.CTRL_DOWN_MASK), CPAction.PASTE);
        
        hflipAction = new FlipAction(null, ResourceLoader.loadIcon(getClass(), ImageResource.HFLIP_ICON), KeyStroke.
                getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK), "Horizontal Flip", FlipAction.HFLIP);
        vflipAction = new FlipAction(null, ResourceLoader.loadIcon(getClass(), ImageResource.VFLIP_ICON), KeyStroke.
                getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK), "Vertical Flip", FlipAction.VFLIP);
        
        rotlAction = new RotateAction(null, ResourceLoader.loadIcon(getClass(), ImageResource.ROTL_ICON), KeyStroke.
                getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK), "Rotate Left", RotateAction.ROTL);
        rotrAction = new RotateAction(null, ResourceLoader.loadIcon(getClass(), ImageResource.ROTR_ICON), KeyStroke.
                getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK), "Rotate Right", RotateAction.ROTR);
    }
    
    public Application getApplication() {
        return app;
    }

    public Model getModel() {
        return model;
    }

    public Pad getPad() {
        return pad;
    }

    public void updateShapeInterface() {
        pad.updateShapeInterface();
    }
    
    public void updateShapeInterface(Model.TOOLS type) {
        pad.updateShapeInterface(type);
    }

    public void notifyRepaint() {
        pad.repaint();
    }
    
    public void notifyDimensionUpdate() {
        pad.updateDimensions();
        pad.revalidate();
    }

    public void notifyTemporaryShapeRemove() {
        pad.removeTemporaryShape();
    }

    public void notifyCursorUpdate(Cursor cursor) {
        pad.setCursor(cursor);
    }

    public void notifyControlsPaint(Graphics2D g2) {
        pad.notifyControlsPaint(g2);
    }
    
    public void notifyIOReset() {
        app.resetIO();
    }
    
    public void forceEditMode() {
        pad.forceEditMode();
    }
    
    /**
     * Forcefully updates the HotSpots
     */
    public void forceControlUpdate() {
        pad.forceControlUpdate();
    }
    
    public void updateURState(UndoManager.AccessibleUndoModel unModel) {
        undoAction.setEnabled(unModel.isUndoEnabled());
        redoAction.setEnabled(unModel.isRedoEnabled());
        
        undoAction.setToolTip(unModel.getUndoTip());
        redoAction.setToolTip(unModel.getRedoTip());
    }
    
    public void setFlips(boolean set) {
        hflipAction.enableButton(set);
        vflipAction.enableButton(set);
    }
    
    public void setRotates(boolean set) {
        rotlAction.enableButton(set);
        rotrAction.enableButton(set);
    }
            
    // The Menubar items
    private JMenuBar menubar;
    private JMenu fileMenu;
    private JMenuItem saveItem;
    private JMenuItem saveAsItem;
    private JMenuItem exitItem;
    private JMenu editMenu;
    private JMenuItem undoItem;
    private JMenuItem redoItem;
    
    // Top Toolbar items
    private JButton saveButton;
    private JButton undoButton;
    private JButton redoButton;
    
    // The Left toolbar items
    private JToggleButton[] shapeButton;
    private JButton clearButton;
    
    // The PropertiesPanel
    private PropertiesPanel propPanel;
    
    // The Status panel
    private StatusPanel sPanel;
    
    // Action Objects
    private ToolsAction[] buttonToolsAction;
    private NewFileAction newAction;
    private SaveAction saveAction;
    private SaveAction saveAsAction;
    private ExitAction exitAction;
    private URAction undoAction;
    private URAction redoAction;
    private CPAction cAction;
    private CPAction pAction;
    private FlipAction hflipAction;
    private FlipAction vflipAction;
    private RotateAction rotlAction;
    private RotateAction rotrAction;

    // The ParentPanel class
    private class ParentPanel extends JPanel {
        private GradientPaint gp;
        private int h;
       
        public ParentPanel(java.awt.LayoutManager layout) {
            super(layout);
            
            gp = null;
            h = 0;
        }
        
        public GradientPaint getGradient() {
            if(gp == null || getHeight() != h) {
                h = getHeight();
                gp = new GradientPaint(0, 0, PadConstants.PARENT_TOP_COLOR,
                        0, h, PadConstants.PARENT_BOT_COLOR);
            }
            
            return gp;
        }
        
        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setPaint(getGradient());
            
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    
    // Action classes
    private class ToolsAction extends AbstractAction {

        public ToolsAction(String txt, String icon, Model.TOOLS elem, String desc) {
            super(txt, ResourceLoader.loadIcon(app.getClass(), icon));
            putValue("ELEMENT_TYPE", elem);
            putValue(SHORT_DESCRIPTION, desc);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            model.setToolType((Model.TOOLS) getValue("ELEMENT_TYPE"));
            pad.requestFocusInWindow();
        }
    };

    public final class SaveAction extends AbstractAction {

        public static final int SAVE = 0x67;
        public static final int SAVE_AS = 0xd2;
        
        public SaveAction(String txt, String icon, String desc, KeyStroke stroke, int type) {
            super(txt, ResourceLoader.loadIcon(app.getClass(), icon));
            putValue(ACCELERATOR_KEY, stroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue("SAVE_TYPE", type);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            app.save((Integer)getValue("SAVE_TYPE"));
        }
    }

    public final class URAction extends AbstractAction {

        public static final int UNDO = 0x3e;
        public static final int REDO = 0x4e;
        
        public static final String UNDO_DESC = "Undo last action";
        public static final String REDO_DESC = "Redo last action";
        
        public URAction(String txt, String icon, String desc, KeyStroke stroke, int type) {
            super(txt, ResourceLoader.loadIcon(app.getClass(), icon));
            putValue(ACCELERATOR_KEY, stroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue("TYPE", type);
            
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch((Integer)getValue("TYPE")) {
                case UNDO:
                    model.performUndo();
                    break;
                case REDO:
                    model.performRedo();
            }
        }
        
        public void setToolTip(String str) {
            putValue(SHORT_DESCRIPTION, str);
        }
    }
    
    private class CPAction extends AbstractAction {
        public static final int COPY = 2;
        public static final int PASTE = 4;
        
        public int type;
        
        public CPAction(String txt, ImageIcon icon, KeyStroke stroke, int type) {
            super(txt, icon);
            putValue(ACCELERATOR_KEY, stroke);
            
            this.type = type;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            switch(type) {
                case COPY:
                    model.copy();
                    break;
                case PASTE:
                    model.paste();
            }
        }
    }
    
    public final class FlipAction extends AbstractAction {
        public static final int HFLIP = 101;
        public static final int VFLIP = 289;
        
        public int type;
        
        public FlipAction(String txt, ImageIcon icon, KeyStroke stroke, 
                String desc,int type) {
            super(txt, icon);
            putValue(ACCELERATOR_KEY, stroke);
            putValue(SHORT_DESCRIPTION, desc);
            
            this.type = type;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            model.flipShape(type);
        }
        
        public void enableButton(boolean en) {
            setEnabled(en);
        }
    }
    
    public final class RotateAction extends AbstractAction {
        public static final int ROTL = 0x53ff64;
        public static final int ROTR = 0x04dea4;
        
        public int type;
        
        public RotateAction(String txt, ImageIcon icon, KeyStroke stroke, 
                String desc,int type) {
            super(txt, icon);
            putValue(ACCELERATOR_KEY, stroke);
            putValue(SHORT_DESCRIPTION, desc);
            
            this.type = type;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            model.rotateShape(type);
        }
        
        public void enableButton(boolean en) {
            setEnabled(en);
        }
    }
    
    private class NewFileAction extends AbstractAction {
        
        public NewFileAction(String txt, String icon, String desc, KeyStroke stroke) {
            super(txt, ResourceLoader.loadIcon(app.getClass(), icon));
            putValue(ACCELERATOR_KEY, stroke);
            putValue(SHORT_DESCRIPTION, desc);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            NewDocumentModule.newDocument(View.this);
        }
    }

    private class ExitAction extends AbstractAction {

        public ExitAction(String txt, String icon, String desc, KeyStroke stroke) {
            super(txt, ResourceLoader.loadIcon(app.getClass(), icon));
            putValue(ACCELERATOR_KEY, stroke);
            putValue(SHORT_DESCRIPTION, desc);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            app.exit(0);
        }
    }
}
