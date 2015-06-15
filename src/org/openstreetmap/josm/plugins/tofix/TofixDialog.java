package org.openstreetmap.josm.plugins.tofix;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static javax.swing.Action.SMALL_ICON;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.openstreetmap.josm.Main;

import org.openstreetmap.josm.actions.downloadtasks.DownloadOsmTask;
import org.openstreetmap.josm.data.APIDataSet;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.gui.JosmUserIdentityManager;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.io.OnlineResource;
import org.openstreetmap.josm.plugins.tofix.bean.AccessTaskBean;
import org.openstreetmap.josm.plugins.tofix.bean.AtributesBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemFixedBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemKeeprightBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemNycbuildingsBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemTigerdeltaBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemUnconnectedBean;
import org.openstreetmap.josm.plugins.tofix.bean.ListTaskBean;
import org.openstreetmap.josm.plugins.tofix.bean.TrackBean;
import org.openstreetmap.josm.plugins.tofix.controller.ItemController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemEditController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemFixedController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemSkipController;
import org.openstreetmap.josm.plugins.tofix.controller.ListTaskController;
import org.openstreetmap.josm.plugins.tofix.layer.TofixLayer;
import org.openstreetmap.josm.plugins.tofix.util.*;
import org.openstreetmap.josm.tools.Geometry;
import static org.openstreetmap.josm.tools.I18n.tr;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.Shortcut;

/**
 *
 * @author ruben
 */
public class TofixDialog extends ToggleDialog implements ActionListener {

    // private final SideButton editButton;
    private final SideButton skipButton;
    private final SideButton fixedButton;
    private final SideButton uploadButton;

    //Tofix host 
    AccessTaskBean accessTaskBean = null;
    // Lista de tasks
    ListTaskBean listTaskBean = null;
    ListTaskController listTaskController = new ListTaskController();

    ItemController itemController = new ItemController();

    Bounds bounds = null;
    DownloadOsmTask downloadOsmTask = new DownloadOsmTask();

    //Tofix Layer
    MapView mv = Main.map.mapView;
    TofixLayer tofixLayer = new TofixLayer("Tofix-layer");

    JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JPanel jcontenpanel = new JPanel(new GridLayout(1, 0));

    JosmUserIdentityManager josmUserIdentityManager = JosmUserIdentityManager.getInstance();

    public TofixDialog() {

        super(tr("To-fix"), "icontofix", tr("Open to-fix window."),
                Shortcut.registerShortcut("tool:to-fix", tr("Toggle: {0}", tr("To-fix")),
                        KeyEvent.VK_F, Shortcut.CTRL_SHIFT), 75);

        //Geting start request the data
        accessTaskBean = new AccessTaskBean("mixedlayer", "keepright", false);
        //get_new_item();

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
            }
        });
        fixedButton.setEnabled(false);

        uploadButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Upload"));
                putValue(SMALL_ICON, ImageProvider.get("mapmode", "fixed.png"));
                putValue(SHORT_DESCRIPTION, tr("upload data"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                UploadAction uploadAction = new UploadAction();
                uploadAction.uploadData((OsmDataLayer) Main.main.getActiveLayer(), new APIDataSet(Main.main.getCurrentDataSet().allModifiedPrimitives()));
               // uploadAction.click();

            }
        });

        // Panels
        valuePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jcontenpanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        // JComboBox for each task
        ArrayList<String> tasksList = new ArrayList<String>();
        tasksList.add("Select a task ...");
        if (Status.isInternetReachable()) { //checkout  internet connection
            listTaskBean = listTaskController.getListTasksBean();
            for (int i = 0; i < listTaskBean.getTasks().size(); i++) {
                tasksList.add(listTaskBean.getTasks().get(i).getTitle());
            }

        }

        JComboBox jcomboBox = new JComboBox(tasksList.toArray());

        valuePanel.add(jcomboBox);
        jcomboBox.addActionListener(this);
        createLayout(jcontenpanel, false, Arrays.asList(new SideButton[]{
            skipButton, fixedButton, uploadButton
        }));
        jcontenpanel.add(valuePanel);

        if (!Status.server()) {
            jcomboBox.setEnabled(false);
            skipButton.setEnabled(false);
            fixedButton.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        if (cb.getSelectedIndex() != 0) {
            accessTaskBean.setTask(listTaskBean.getTasks().get(cb.getSelectedIndex() - 1).getId());
            accessTaskBean.setTask_source(listTaskBean.getTasks().get(cb.getSelectedIndex() - 1).getSource());
            get_new_item();
            skipButton.setEnabled(true);
            fixedButton.setEnabled(true);
        } else {
            skipButton.setEnabled(false);
            fixedButton.setEnabled(false);
        }
    }

    public void edit() {
        if (accessTaskBean.isAccess()) {
            Download.Download(downloadOsmTask, bounds, accessTaskBean.getOsm_obj_id());
            TrackBean trackBean = new TrackBean();
            AtributesBean atributesBean = new AtributesBean();
            atributesBean.setAction("edit");
            atributesBean.setEditor("josm");
            atributesBean.setUser(josmUserIdentityManager.getUserName());
            atributesBean.setKey(accessTaskBean.getKey());
            trackBean.setAttributes(atributesBean);
            ItemEditController itemEditController = new ItemEditController(accessTaskBean.getTrack_url(), trackBean);
            itemEditController.sendTrackBean();
        }
    }

    public void skip() {
        if (accessTaskBean.isAccess()) {
            TrackBean trackBean = new TrackBean();
            AtributesBean atributesBean = new AtributesBean();
            atributesBean.setAction("skip");
            atributesBean.setEditor("josm");
            atributesBean.setUser(josmUserIdentityManager.getUserName());
            atributesBean.setKey(accessTaskBean.getKey());
            trackBean.setAttributes(atributesBean);
            ItemSkipController skipController = new ItemSkipController(accessTaskBean.getTrack_url(), trackBean);
            skipController.sendTrackBean();
        }
        get_new_item();

    }

    public void fixed() {
        if (accessTaskBean.isAccess()) {
            ItemFixedBean itemFixedBean = new ItemFixedBean();
            itemFixedBean.setUser(josmUserIdentityManager.getUserName());
            itemFixedBean.setKey(accessTaskBean.getKey());
            //itemFixedBean.setEditor("josm");
            ItemFixedController itemFixedController = new ItemFixedController(accessTaskBean.getFixed_url(), itemFixedBean);
            itemFixedController.sendTrackBean();
        }
        get_new_item();
    }

    private void get_new_item() {
        if (accessTaskBean.getTask_source().equals("keepright")) {
            get_item_keepright();
            edit();
        }

        if (accessTaskBean.getTask_source().equals("unconnected")) {
            if (accessTaskBean.getTask().equals("unconnected_minor1")) {
                JOptionPane.showConfirmDialog(Main.panel, "Not implemented yet");
            } else {
                get_item_unconnected();
                edit();
            }
        }
        if (accessTaskBean.getTask_source().equals("tigerdelta")) {
            get_item_tigerdelta();
            edit();
        }

        if (accessTaskBean.getTask_source().equals("nycbuildings")) {
            get_item_nycbuildings();
            edit();
        }
    }

    private void get_item_keepright() {
        ItemKeeprightBean itemKeeprightBean = null;
        itemController.setUrl(accessTaskBean.getTask_url());
        itemKeeprightBean = itemController.getItemKeeprightBean();
        Util.print(itemKeeprightBean.getKey());
        if (itemKeeprightBean != null) {
            accessTaskBean.setAccess(true);
            accessTaskBean.setOsm_obj_id(itemKeeprightBean.getValue().getObject_id());
            accessTaskBean.setKey(itemKeeprightBean.getKey());
            LatLon latLon = Util.format_St_astext_Keepright(itemKeeprightBean.getValue().getSt_astext());
            bounds = new Bounds(latLon.toBBox(0.0007).toRectangle());

            TofixDraw.draw_Node(tofixLayer, latLon);
        } else {
            accessTaskBean.setAccess(false);
            JOptionPane.showMessageDialog(Main.parent, "Something went wrong on Server!, Please change the Task or try to again");
        }
    }

    private void get_item_unconnected() {
        ItemUnconnectedBean itemUnconnectedBean = null;
        itemController.setUrl(accessTaskBean.getTask_url());
        itemUnconnectedBean = itemController.getItemBean();
        if (itemUnconnectedBean != null) {
            accessTaskBean.setAccess(true);
            accessTaskBean.setOsm_obj_id(itemUnconnectedBean.getValue().getNode_id());
            accessTaskBean.setKey(itemUnconnectedBean.getKey());
            // itemUnconnectedBean.
            //Util.print(itemUnconnectedBean.getValue().getY()+"   o "+itemUnconnectedBean.getValue().getX());
            LatLon latLon = new LatLon(itemUnconnectedBean.getValue().getY(), itemUnconnectedBean.getValue().getX());
            bounds = new Bounds(latLon.toBBox(0.0007).toRectangle());
            TofixDraw.draw_Node(tofixLayer, latLon);
        } else {
            accessTaskBean.setAccess(false);
            JOptionPane.showMessageDialog(Main.parent, "Something went wrong on Server!, Please change the Task or try to again");
        }
    }

    private void get_item_nycbuildings() {
        ItemNycbuildingsBean itemNycbuildingsBean = null;
        itemController.setUrl(accessTaskBean.getTask_url());
        itemNycbuildingsBean = itemController.getItemNycbuildingsBean();
        Util.print(accessTaskBean.getTask_url());
        if (itemNycbuildingsBean != null) {
            accessTaskBean.setAccess(true);
            accessTaskBean.setOsm_obj_id(Util.format_Elems_Nycbuildings(itemNycbuildingsBean.getValue().getElems()));
            accessTaskBean.setKey(itemNycbuildingsBean.getKey());
            LatLon latLon = new LatLon(itemNycbuildingsBean.getValue().getLat(), itemNycbuildingsBean.getValue().getLon());
            Util.print(latLon);
            bounds = new Bounds(latLon.toBBox(0.0007).toRectangle());
            TofixDraw.draw_Node(tofixLayer, latLon);
        } else {
            accessTaskBean.setAccess(false);
            JOptionPane.showMessageDialog(Main.parent, "Something went wrong on Server!, Please change the Task or try to again");
        }

    }

    private void get_item_tigerdelta() {
        ItemTigerdeltaBean itemTigerdeltaBean = null;
        itemController.setUrl(accessTaskBean.getTask_url());
        itemTigerdeltaBean = itemController.getItemTigerdeltaBean();
        Util.print(accessTaskBean.getTask_url());
        if (itemTigerdeltaBean != null) {
            accessTaskBean.setAccess(true);
            accessTaskBean.setOsm_obj_id(itemTigerdeltaBean.getValue().getWay());
            accessTaskBean.setKey(itemTigerdeltaBean.getKey());
            List<List<Node>> list = Util.format_st_astext_Tigerdelta(itemTigerdeltaBean.getValue().getSt_astext());
            LatLon latLon = new LatLon(list.get(0).get(0).getCoor().lat(), list.get(0).get(0).getCoor().lon());//  Util.print(latLon);
            bounds = new Bounds(latLon.toBBox(0.001).toRectangle());
            TofixDraw.draw_line(tofixLayer, latLon, list);

        } else {
            accessTaskBean.setAccess(false);
            JOptionPane.showMessageDialog(Main.parent, "Something went wrong on Server!, Please change the Task or try to again");
        }

    }
}
