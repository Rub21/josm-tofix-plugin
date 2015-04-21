package org.openstreetmap.josm.plugins.tofix;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static javax.swing.Action.SMALL_ICON;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.downloadtasks.DownloadOsmTask;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.JosmUserIdentityManager;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.plugins.tofix.bean.AtributesBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemFixedBean;
import org.openstreetmap.josm.plugins.tofix.bean.ListTaskBean;
import org.openstreetmap.josm.plugins.tofix.bean.TrackBean;
import org.openstreetmap.josm.plugins.tofix.controller.ItemController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemEditController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemFixedController;
import org.openstreetmap.josm.plugins.tofix.controller.ListTaskController;
import org.openstreetmap.josm.plugins.tofix.layer.TofixLayer;
import org.openstreetmap.josm.plugins.tofix.util.*;
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

    //Tofix host 
    String host = "http://54.147.184.23:8000";
    String url_task = url_task = host + "/task/unconnectedmajor";
    String task = "unconnectedmajor";

    // Lista de tasks
    ListTaskBean listTaskBean = null;
    ListTaskController listTaskController = new ListTaskController("http://osmlab.github.io/to-fix/src/data/tasks.json");

    ItemController itemController = null;
    ItemBean itemBean = null;

    Bounds bounds = null;
    DownloadOsmTask downloadOsmTask = new DownloadOsmTask();

    //Tofix Layer
    MapView mv = Main.map.mapView;
    TofixLayer tofixLayer = new TofixLayer("Tofix-layer");

    JPanel valuePanel = new JPanel();
    JPanel jcontenpanel = new JPanel(new GridLayout(4, 0));

    JosmUserIdentityManager josmUserIdentityManager = JosmUserIdentityManager.getInstance();

    JCheckBox autoedit = new JCheckBox("Automatic Download after Skip");

    public TofixDialog() {
        super(tr("To-fix"), "icontofix", tr("Open to-fix window."),
                Shortcut.registerShortcut("tool:to-fix", tr("Toggle: {0}", tr("To-fix")),
                        KeyEvent.VK_F, Shortcut.CTRL_SHIFT), 75);

        //Geting start request the data
        get_new_item();
        // Fixed Button
        editButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Edit"));
                putValue(SMALL_ICON, ImageProvider.get("mapmode", "edit.png"));
                putValue(SHORT_DESCRIPTION, tr("Dowload data"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                fixedButton.setEnabled(true);
                edit();

            }
        });
        //editButton.setEnabled(false);
        // Fixed Skip
        skipButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Skip"));
                putValue(SMALL_ICON, ImageProvider.get("mapmode", "skip.png"));
                putValue(SHORT_DESCRIPTION, tr("Skip Error"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                editButton.setEnabled(true);
                fixedButton.setEnabled(false);
                skip();
                if (autoedit.isSelected()) {
                    editButton.doClick();
                    fixedButton.setEnabled(true);
                }

            }
        });
        // Fixed Button
        fixedButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Fixed"));
                putValue(SMALL_ICON, ImageProvider.get("mapmode", "fixed.png"));
                putValue(SHORT_DESCRIPTION, tr("Fixed Error"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {

//                if (autoskip.isSelected()) {
//                    
//                }
                // fixedButton.setEnabled(false);
                fixed();
                skipButton.doClick();

            }
        });
        fixedButton.setEnabled(false);
        // Panels
        valuePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jcontenpanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        // JComboBox for each task
        ArrayList<String> tasksList = new ArrayList<String>();
        if (Status.isInternetReachable()) { //checkout  internet connection
            listTaskBean = listTaskController.getListTasksBean();
            for (int i = 0; i < listTaskBean.getTasks().size(); i++) {
                tasksList.add(listTaskBean.getTasks().get(i).getTitle());
            }
            if (!Status.server()) {
                editButton.setEnabled(false);
                skipButton.setEnabled(false);
                fixedButton.setEnabled(false);
            }
        } else {
            editButton.setEnabled(false);
            skipButton.setEnabled(false);
        }
        JComboBox comboBox = new JComboBox(tasksList.toArray());
        jcontenpanel.add(comboBox);
        comboBox.addActionListener(this);
        jcontenpanel.add(autoedit);

        //  jcontenpanel.add(autoskip);
        this.setPreferredSize(new Dimension(0, 40));
        createLayout(jcontenpanel, false, Arrays.asList(new SideButton[]{
            editButton, skipButton, fixedButton
        }));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        task = listTaskBean.getTasks().get(cb.getSelectedIndex()).getId();
        url_task = host + "/task/" + task;
    }

    public void skip() {

    }

    public void edit() {
        Download.Download(downloadOsmTask, bounds, itemBean);
        TrackBean trackBean = new TrackBean();
        AtributesBean atributesBean = new AtributesBean();
        atributesBean.setAction("edit");
        atributesBean.setEditor("josm");
        atributesBean.setUser(josmUserIdentityManager.getUserName());
        atributesBean.setKey(itemBean.getKey());
        trackBean.setAttributes(atributesBean);
        ItemEditController itemEditController = new ItemEditController(host + "/track/" + task, trackBean);
        itemEditController.sendTrackBean();
    }

    public void fixed() {
        ItemFixedBean itemFixedBean = new ItemFixedBean();
        itemFixedBean.setUser(josmUserIdentityManager.getUserName());
        itemFixedBean.setKey(itemBean.getKey());
        ItemFixedController ItemFixedController = new ItemFixedController(host + "/fixed/" + task);
        ItemFixedController.fixed(itemFixedBean);
    }

    private void get_new_item() {
        itemController = new ItemController(url_task);
        itemBean = itemController.getItemBean();
        Util.print(itemBean.getKey());
        LatLon coor = new LatLon(itemBean.getValue().getY(), itemBean.getValue().getX());
        if (coor.isOutSideWorld()) {
            JOptionPane.showMessageDialog(Main.parent, tr("Can not find outside of the world."));
            return;
        }
        BoundingXYVisitor v = new BoundingXYVisitor();
        //double ex = 0.0001; = 2.34 m
        double ex = 0.0007;// 16.7 m
        bounds = new Bounds(itemBean.getValue().getY() - ex, itemBean.getValue().getX() - ex, itemBean.getValue().getY() + ex, itemBean.getValue().getX() + ex);
        v.visit(bounds);
        Main.map.mapView.zoomTo(v);
        if (!Main.map.mapView.hasLayer(tofixLayer)) {
            mv.addLayer(tofixLayer);
            tofixLayer.add_coordinate(coor);
        } else {
            tofixLayer.add_coordinate(coor);
        }
    }
}
