package org.openstreetmap.josm.plugins.tofix;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.net.ssl.SSLEngineResult;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static javax.swing.Action.SMALL_ICON;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.plugins.tofix.bean.StatusBean;
import org.openstreetmap.josm.plugins.tofix.controller.StatusController;
import org.openstreetmap.josm.plugins.tofix.controller.TaskController;
import static org.openstreetmap.josm.tools.I18n.tr;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.Shortcut;

/**
 *
 * @author ruben
 */
public class TofixDialog extends ToggleDialog implements ActionListener {

    private final SideButton editButton;
    private final SideButton skipButton;
    private final SideButton fixedButton;

    public TofixDialog() {
        super(tr("To-fix"), "icontofix", tr("Open to-fix window."),
                Shortcut.registerShortcut("tool:to-fix", tr("Toggle: {0}", tr("To-fix")),
                        KeyEvent.VK_F, Shortcut.CTRL_SHIFT), 150);

        editButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Edit"));
                putValue(SMALL_ICON, ImageProvider.get("mapmode", "edit.png"));
                putValue(SHORT_DESCRIPTION, tr("Dowload data"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showConfirmDialog(Main.parent, "Edit");
                JOptionPane.showMessageDialog(Main.parent, tr("Edit."));
                StatusBean statusBean = null;
                StatusController statusController = new StatusController(statusBean, "test");
                statusController.getStatusBean();
            }
        });
        skipButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Skip"));
                putValue(SMALL_ICON, ImageProvider.get("mapmode", "skip.png"));
                putValue(SHORT_DESCRIPTION, tr("Skip Error"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(Main.parent, tr("Skip."));
                TaskController taskController = new TaskController("http://54.147.184.23:8000/task/unconnectedmajor");
                taskController.getTaskBean();

            }
        });

        fixedButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Fixed"));
                putValue(SMALL_ICON, ImageProvider.get("mapmode", "fixed.png"));
                putValue(SHORT_DESCRIPTION, tr("Fixed Error"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Main.parent, tr("Fixed."));
            }
        });

        JPanel valuePanel = new JPanel();
        valuePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        JPanel jPanel2 = new JPanel(new GridLayout(0, 2));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        // RadioButtons
        JRadioButton unconnected_majorButton = new JRadioButton("Unconnected major");
        JRadioButton unconnected_minorButton = new JRadioButton("Unconnected minor");
        JRadioButton impossible_one_waysButton = new JRadioButton("Impossible one-ways");
        JRadioButton kinksButton = new JRadioButton("Kinks");
        JRadioButton mixed_layersButton = new JRadioButton("Mixed layers");
        JRadioButton broken_polygonsButton = new JRadioButton("Broken polygons");
        JRadioButton LoopingsButton = new JRadioButton("Loopings");
        JRadioButton strange_layerButton = new JRadioButton("Strange layer");
        JRadioButton highway_intersects_highwayButton = new JRadioButton("Highway intersects highway");

        // Initialalizer in Unconnected major
        unconnected_majorButton.setSelected(true);

        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(unconnected_majorButton);
        group.add(unconnected_minorButton);
        group.add(impossible_one_waysButton);
        group.add(kinksButton);
        group.add(mixed_layersButton);
        group.add(broken_polygonsButton);
        group.add(LoopingsButton);
        group.add(strange_layerButton);
        group.add(highway_intersects_highwayButton);

        //Setup Action Command
        unconnected_majorButton.setActionCommand("Unconnected major");
        unconnected_minorButton.setActionCommand("Unconnected minor");
        impossible_one_waysButton.setActionCommand("Impossible one-ways");
        kinksButton.setActionCommand("Kinks");
        mixed_layersButton.setActionCommand("Mixed layers");
        broken_polygonsButton.setActionCommand("Broken polygons");
        LoopingsButton.setActionCommand("Loopings");
        strange_layerButton.setActionCommand("Strange layer");
        highway_intersects_highwayButton.setActionCommand("Highway intersects highway");

        // Add on Panel              
        jPanel2.add(unconnected_majorButton);
        jPanel2.add(unconnected_minorButton);
        jPanel2.add(impossible_one_waysButton);
        jPanel2.add(kinksButton);
        jPanel2.add(mixed_layersButton);
        jPanel2.add(broken_polygonsButton);
        jPanel2.add(LoopingsButton);
        jPanel2.add(strange_layerButton);
        jPanel2.add(highway_intersects_highwayButton);

        // Add Action Listener for each one
        unconnected_majorButton.addActionListener(this);
        unconnected_minorButton.addActionListener(this);
        impossible_one_waysButton.addActionListener(this);
        kinksButton.addActionListener(this);
        mixed_layersButton.addActionListener(this);
        broken_polygonsButton.addActionListener(this);
        LoopingsButton.addActionListener(this);
        strange_layerButton.addActionListener(this);
        highway_intersects_highwayButton.addActionListener(this);

        this.setPreferredSize(new Dimension(0, 92));
        createLayout(jPanel2, false, Arrays.asList(new SideButton[]{
            editButton, skipButton, fixedButton
        }));

//Highway intersects footpath
//Highway intersects water
//Misspelled tags
//Tiger delta
//NYC overlapping buildings
//USA overlapping buildings
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(Main.parent, tr(e.getActionCommand()));

    }

    //http://54.147.184.23:8000/count/unconnectedmajor
}
