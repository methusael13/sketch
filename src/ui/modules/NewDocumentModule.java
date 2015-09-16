package ui.modules;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import ui.View;

public class NewDocumentModule extends JDialog {
    
    private JLabel templateLabel;
    private JLabel sizeLabel;
    private JLabel widthLabel;
    private JLabel heightLabel;

    private JButton okButton;
    private JButton cancelButton;

    private JComboBox templates;
    private JSpinner widthSpinner;
    private JSpinner heightSpinner;
    private SpinnerModel spmW;
    private SpinnerModel spmH;

    private JPanel topPanel;
    private JPanel bottomPanel;

    private Object[] sizePresets = new Object[]
    {
        "640 X 480",
        "800 X 600",
        "1024 X 768",
        "1280 X 1024",
        "1600 X 1200",
        "A3 (300 ppi)",
        "A4 (300 ppi)",
        "A5 (300 ppi)",
        "A6 (150 ppi)",
        "Web banner small (468 X 60)",
        "Web banner huge (728 X 90)",
        "CD Cover",
        "PAL - 720 X 576",
        "NTSC - 720 X 486"
    };

    // The resolution representation of the above mentioned presets
    private int[][] sizePresetsRes = new int[][]
    {
        {640, 480},
        {800, 600},
        {1024, 768},
        {1280, 1024},
        {1600, 1200},
        {3508, 4960},
        {2480, 3508},
        {1754, 2480},
        {1240, 1754},
        {468, 60},
        {728, 90},
        {1429, 1417},
        {720, 576},
        {720, 486}
    };

    public NewDocumentModule(final View view) {
        
        super(view);
        setTitle("New Pad");
        setSize(400, 270);
        setResizable(false);
        setLocationRelativeTo(view);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        
        createGUI(view);
        setVisible(true);
    }
    
    private void createGUI(final View view) {
        topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Set Image size"));

        templateLabel = new JLabel("Templates");
        GridBagConstraints con = new GridBagConstraints();
        con.fill = GridBagConstraints.HORIZONTAL;
        con.insets = new Insets(15, 15, 0, 2);
        con.gridwidth = 2;
        con.weightx = 1;
        topPanel.add(templateLabel, con);

        templates = new JComboBox(new DefaultComboBoxModel(sizePresets));
        GridBagConstraints con1 = new GridBagConstraints();
        con1.fill = GridBagConstraints.BOTH;
        con1.insets = new Insets(15, 0, 0, 15);
        con1.gridx = 2;
        con1.gridy = 0;
        con1.ipadx = 50;
        con1.weightx = 0.5;
        con1.gridwidth = 2;

        templates.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int index = templates.getSelectedIndex();
                widthSpinner.setValue(sizePresetsRes[index][0]);
                heightSpinner.setValue(sizePresetsRes[index][1]);
            }
        });
        topPanel.add(templates, con1);

        sizeLabel = new JLabel("Custom Size");
        GridBagConstraints con2 = new GridBagConstraints();
        con2.fill = GridBagConstraints.HORIZONTAL;
        con2.gridx = 0;
        con2.gridy = 1;
        con2.gridwidth = 2;
        con2.insets = new Insets(30, 15, 0, 2);
        con2.weightx = 1;
        topPanel.add(sizeLabel, con2);

        widthLabel = new JLabel("Width:");
        GridBagConstraints con3 = new GridBagConstraints();
        con3.fill = GridBagConstraints.HORIZONTAL;
        con3.gridx = 0;
        con3.gridy = 2;
        con3.insets = new Insets(10, 40, 0, 2);
        con3.gridwidth = 2;
        topPanel.add(widthLabel, con3);

        spmW = new SpinnerNumberModel(640, 16, 3000, 1);
        widthSpinner = new JSpinner(spmW);
        GridBagConstraints con4 = new GridBagConstraints();
        con4.fill = GridBagConstraints.HORIZONTAL;
        con4.gridx = 2;
        con4.gridy = 2;
        con4.ipadx = 30;
        con4.insets = new Insets(10, 0, 0, 0);
        con4.gridwidth = 1;
        topPanel.add(widthSpinner, con4);

        heightLabel = new JLabel("Height:");
        GridBagConstraints con5 = new GridBagConstraints();
        con5.fill = GridBagConstraints.HORIZONTAL;
        con5.gridx = 0;
        con5.gridy = 3;
        con5.insets = new Insets(5, 37, 0, 2);
        con5.gridwidth = 2;
        topPanel.add(heightLabel, con5);

        spmH = new SpinnerNumberModel(480, 16, 3000, 1);
        heightSpinner = new JSpinner(spmH);
        GridBagConstraints con6 = new GridBagConstraints();
        con6.fill = GridBagConstraints.HORIZONTAL;
        con6.gridx = 2;
        con6.gridy = 3;
        con6.insets = new Insets(5, 0, 0, 0);
        con6.gridwidth = 1;
        con6.ipadx = 30;
        topPanel.add(heightSpinner, con6);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewCanvasSize(
                        (Integer)widthSpinner.getValue(),
                        (Integer)heightSpinner.getValue(),
                        view);
                dispose();
            }
        });
        bottomPanel.add(okButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        bottomPanel.add(cancelButton);

        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setNewCanvasSize(int w, int h, View view) {
        // The size limit is already maintained by the JSpinner itself
        // yet it'd be better to leave no backdoors!
        if(w < 16 || w > 6000)
            w = 640;
        if(h < 16 || h > 5000)
            h = 480;

        view.getModel().createNewDocument(new Dimension(w, h));
    }
    
    public static void newDocument(View view) {
        if (view.getModel().getDocumentModel().isChanged()) {
            int ans = JOptionPane.showConfirmDialog(
                    view,
                    "Do you want to save the current document?",
                    "Confirm Save",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            switch (ans) {
                case JOptionPane.YES_OPTION:
                    view.getApplication().save(View.SaveAction.SAVE);
                    break;
                case JOptionPane.CANCEL_OPTION:
                    return;
            }
        }

        new NewDocumentModule(view);
    }
}