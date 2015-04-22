package org.openstreetmap.josm.plugins.tofix;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static javax.swing.Action.SMALL_ICON;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.downloadtasks.DownloadOsmTask;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.JosmUserIdentityManager;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.plugins.tofix.bean.AccessTaskBean;
import org.openstreetmap.josm.plugins.tofix.bean.AtributesBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemFixedBean;
import org.openstreetmap.josm.plugins.tofix.bean.ListTaskBean;
import org.openstreetmap.josm.plugins.tofix.bean.TrackBean;
import org.openstreetmap.josm.plugins.tofix.controller.ItemController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemEditController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemFixedController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemSkipController;
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
    AccessTaskBean accessTaskBean = new AccessTaskBean();

    // Lista de tasks
    ListTaskBean listTaskBean = null;
    ListTaskController listTaskController = new ListTaskController();

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

    JCheckBox autoedit_skip = new JCheckBox("Automatic Download after Skip");
    JCheckBox autoedit_fixed = new JCheckBox("Automatic Download after Fixed");

    public TofixDialog() {
        super(tr("To-fix"), "icontofix", tr("Open to-fix window."),
                Shortcut.registerShortcut("tool:to-fix", tr("Toggle: {0}", tr("To-fix")),
                        KeyEvent.VK_F, Shortcut.CTRL_SHIFT), 75);

        //Geting start request the data
        accessTaskBean.setTask("mixedlayer");//by default
        accessTaskBean.setTask_source("mixedlayer");// by default
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
                skipButton.setEnabled(true);
                edit();

            }
        });
        // Fixed Skip
        skipButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Skip"));
                putValue(SMALL_ICON, ImageProvider.get("mapmode", "skip.png"));
                putValue(SHORT_DESCRIPTION, tr("Skip Error"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                skip();
                get_new_item();
                if (autoedit_skip.isSelected()) {
                    editButton.doClick();
                } else {
                    skipButton.setEnabled(false);
                    fixedButton.setEnabled(false);
                }
            }
        });
        skipButton.setEnabled(false);
        // Fixed Button
        fixedButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Fixed"));
                putValue(SMALL_ICON, ImageProvider.get("mapmode", "fixed.png"));
                putValue(SHORT_DESCRIPTION, tr("Fixed Error"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                fixed();
                get_new_item();
                if (autoedit_fixed.isSelected()) {
                    editButton.doClick();
                } else {
                    skipButton.setEnabled(false);
                    fixedButton.setEnabled(false);
                }
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
        jcontenpanel.add(autoedit_skip);
        jcontenpanel.add(autoedit_fixed);

        //  jcontenpanel.add(autoskip);
        this.setPreferredSize(new Dimension(0, 40));
        createLayout(jcontenpanel, false, Arrays.asList(new SideButton[]{
            editButton, skipButton, fixedButton
        }));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        accessTaskBean.setTask(listTaskBean.getTasks().get(cb.getSelectedIndex()).getId());
    }

    public void skip() {
        if (accessTaskBean.isAccess()) {
            TrackBean trackBean = new TrackBean();
            AtributesBean atributesBean = new AtributesBean();
            atributesBean.setAction("skip");
            atributesBean.setEditor("josm");
            atributesBean.setUser(josmUserIdentityManager.getUserName());
            atributesBean.setKey(itemBean.getKey());
            trackBean.setAttributes(atributesBean);
            ItemSkipController skipController = new ItemSkipController(accessTaskBean.getTask_url(), trackBean);
            skipController.sendTrackBean();
        }

    }

    public void edit() {
        if (accessTaskBean.isAccess()) {
            Download.Download(downloadOsmTask, bounds, itemBean);
            TrackBean trackBean = new TrackBean();
            AtributesBean atributesBean = new AtributesBean();
            atributesBean.setAction("edit");
            atributesBean.setEditor("josm");
            atributesBean.setUser(josmUserIdentityManager.getUserName());
            atributesBean.setKey(itemBean.getKey());
            trackBean.setAttributes(atributesBean);
            ItemEditController itemEditController = new ItemEditController(accessTaskBean.getTrack_url(), trackBean);
            itemEditController.sendTrackBean();
        }

    }

    public void fixed() {
        if (accessTaskBean.isAccess()) {
            ItemFixedBean itemFixedBean = new ItemFixedBean();
            itemFixedBean.setUser(josmUserIdentityManager.getUserName());
            itemFixedBean.setKey(itemBean.getKey());
            ItemFixedController itemFixedController = new ItemFixedController(accessTaskBean.getFixed_url(), itemFixedBean);
            itemFixedController.sendTrackBean();
        }

    }

    private void get_new_item() {
        itemController = new ItemController(accessTaskBean.getTask_url());
        itemBean = itemController.getItemBean();
        if (itemBean != null) {
            accessTaskBean.setAccess(true);
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
        } else {
            accessTaskBean.setAccess(false);
            JOptionPane.showMessageDialog(Main.parent, "Something went wrong on Server!, Please change the Task or try to again");
        }

    }
}
