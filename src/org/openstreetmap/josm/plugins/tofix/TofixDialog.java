package org.openstreetmap.josm.plugins.tofix;

import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static javax.swing.Action.SMALL_ICON;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.downloadtasks.DownloadOsmTask;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.gui.JosmUserIdentityManager;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.plugins.tofix.bean.AccessTaskBean;
import org.openstreetmap.josm.plugins.tofix.bean.AttributesBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemFixedBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemKeeprightBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemKrakatoaBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemNycbuildingsBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemTigerdeltaBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemUnconnectedBean;
import org.openstreetmap.josm.plugins.tofix.bean.ListTaskBean;
import org.openstreetmap.josm.plugins.tofix.bean.TrackBean;
import org.openstreetmap.josm.plugins.tofix.controller.ItemController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemEditController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemFixedController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemTrackController;
import org.openstreetmap.josm.plugins.tofix.controller.ListTaskController;
import org.openstreetmap.josm.plugins.tofix.layer.TofixLayer;
import org.openstreetmap.josm.plugins.tofix.util.*;
import static org.openstreetmap.josm.tools.I18n.tr;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.Shortcut;
import org.openstreetmap.josm.tools.OpenBrowser;

/**
 *
 * @author ruben
 */
public class TofixDialog extends ToggleDialog implements ActionListener {

    // private final SideButton editButton;
    private final SideButton skipButton;
    private final SideButton fixedButton;
    private final SideButton noterrorButton;
    private Shortcut skipShortcut = null;
    private Shortcut fixedShortcut = null;
    private Shortcut noterrorButtonShortcut = null;
    // To-Fix host
    AccessTaskBean accessTaskBean = null;
    // Task list
    ListTaskBean listTaskBean = null;
    ListTaskController listTaskController = new ListTaskController();

    ItemController itemController = new ItemController();

    Bounds bounds = null;
    DownloadOsmTask downloadOsmTask = new DownloadOsmTask();

    // To-Fix layer
    MapView mv = Main.map.mapView;
    TofixLayer tofixLayer = new TofixLayer("Tofix-layer");

    JPanel valuePanel = new JPanel(new GridLayout(1, 1));
    JPanel jcontenpanel = new JPanel(new GridLayout(1, 2));

    JosmUserIdentityManager josmUserIdentityManager = JosmUserIdentityManager.getInstance();

    private final double size_bounds = 0.001;

    public TofixDialog() {

        super(tr("To-fix"), "icontofix", tr("Open to-fix window."),
                Shortcut.registerShortcut("tool:to-fix", tr("Toggle: {0}", tr("To-fix")),
                        KeyEvent.VK_T, Shortcut.CTRL_SHIFT), 75);

        // Request data
        accessTaskBean = new AccessTaskBean("mixedlayer", "keepright", false);

        skipButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Skip"));
                putValue(SMALL_ICON, ImageProvider.get("mapmode", "skip.png"));
                putValue(SHORT_DESCRIPTION, tr("Skip Error"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                skip();
            }
        });
        skipButton.setEnabled(false);

        // "Fixed" button
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

        // "Not a error" button
        noterrorButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Not a error"));
                putValue(SMALL_ICON, ImageProvider.get("mapmode", "noterror.png"));
                putValue(SHORT_DESCRIPTION, tr("Not a error"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                noterror();
            }
        });

        noterrorButton.setEnabled(false);

        //add tittle for To-fix task
        JLabel jLabel = new javax.swing.JLabel();
        jLabel.setText("<html><a href=\"\">List of To-fix tasks:</a></html>");
        jLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OpenBrowser.displayUrl("http://osmlab.github.io/to-fix/");
            }
        });
        jcontenpanel.add(jLabel);
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
            skipButton, noterrorButton, fixedButton
        }));
        jcontenpanel.add(valuePanel);

        if (!Status.server()) {
            jcomboBox.setEnabled(false);
            skipButton.setEnabled(false);
            fixedButton.setEnabled(false);
            noterrorButton.setEnabled(false);
        }
        //Shortcuts
        skipShortcut = Shortcut.registerShortcut("tofix:skip", tr("tofix:Skip item"), KeyEvent.VK_S, Shortcut.ALT_SHIFT);
        Main.registerActionShortcut(new Skip_key_Action(), skipShortcut);
        
        fixedShortcut = Shortcut.registerShortcut("tofix:fixed", tr("tofix:Fixed item"), KeyEvent.VK_F, Shortcut.ALT_SHIFT);
        Main.registerActionShortcut(new Fixed_key_Action(), fixedShortcut);
        
        noterrorButtonShortcut = Shortcut.registerShortcut("tofix:noterror", tr("tofix:Not a Error item"), KeyEvent.VK_N, Shortcut.ALT_SHIFT);
        Main.registerActionShortcut(new NotError_key_Action(), noterrorButtonShortcut);
    }

    public class Skip_key_Action extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            skip();
        }
    }

    public class Fixed_key_Action extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            fixed();
        }
    }

    public class NotError_key_Action extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            noterror();
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
            noterrorButton.setEnabled(true);
        } else {
            skipButton.setEnabled(false);
            fixedButton.setEnabled(false);
            noterrorButton.setEnabled(false);
        }
    }

    public void edit() {
        if (accessTaskBean.isAccess()) {
            Download.Download(downloadOsmTask, bounds, accessTaskBean.getOsm_obj_id());
            TrackBean trackBean = new TrackBean();
            AttributesBean attributesBean = new AttributesBean();
            attributesBean.setAction("edit");
            attributesBean.setEditor("josm");
            attributesBean.setUser(josmUserIdentityManager.getUserName());
            attributesBean.setKey(accessTaskBean.getKey());
            trackBean.setAttributes(attributesBean);
            ItemEditController itemEditController = new ItemEditController(accessTaskBean.getTrack_url(), trackBean);
            itemEditController.sendTrackBean();
        }
    }

    public void skip() {
        if (accessTaskBean.isAccess()) {
            TrackBean trackBean = new TrackBean();
            AttributesBean attributesBean = new AttributesBean();
            attributesBean.setAction("skip");
            attributesBean.setEditor("josm");
            attributesBean.setUser(josmUserIdentityManager.getUserName());
            attributesBean.setKey(accessTaskBean.getKey());
            trackBean.setAttributes(attributesBean);
            ItemTrackController skipController = new ItemTrackController(accessTaskBean.getTrack_url(), trackBean);
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

    public void noterror() {
        if (accessTaskBean.isAccess()) {
            TrackBean trackBean = new TrackBean();
            AttributesBean attributesBean = new AttributesBean();
            attributesBean.setAction("noterror");
            attributesBean.setEditor("josm");
            attributesBean.setUser(josmUserIdentityManager.getUserName());
            attributesBean.setKey(accessTaskBean.getKey());
            trackBean.setAttributes(attributesBean);
            ItemTrackController notaerrorController = new ItemTrackController(accessTaskBean.getTrack_url(), trackBean);
            notaerrorController.sendTrackBean();
        }
        get_new_item();
    }

    private void get_new_item() {
        if (accessTaskBean.getTask_source().equals("keepright")) {
            get_item_keepright();
            edit();
        }
        if (accessTaskBean.getTask_source().equals("unconnected")) {
            //if (accessTaskBean.getTask().equals("unconnected_minor1")) {
            JOptionPane.showConfirmDialog(Main.panel, "Not implemented yet");
            // } else {
            // get_item_unconnected();
            // edit();
            // }
        }
        if (accessTaskBean.getTask_source().equals("tigerdelta")) {
            get_item_tigerdelta();
            edit();
        }
        if (accessTaskBean.getTask_source().equals("nycbuildings")) {
            get_item_nycbuildings();
            edit();
        }
        if (accessTaskBean.getTask_source().equals("krakatoa")) {
            get_item_krakatoa();
            edit();
        }
    }

    private void get_item_keepright() {
        ItemKeeprightBean itemKeeprightBean = null;
        itemController.setUrl(accessTaskBean.getTask_url());
        itemKeeprightBean = itemController.getItemKeeprightBean();
        if (itemKeeprightBean != null) {
            accessTaskBean.setAccess(true);
            accessTaskBean.setOsm_obj_id(itemKeeprightBean.getValue().getObject_id());
            accessTaskBean.setKey(itemKeeprightBean.getKey());
            LatLon latLon = Util.format_St_astext_Keepright(itemKeeprightBean.getValue().getSt_astext());
            bounds = new Bounds(latLon.toBBox(size_bounds).toRectangle());
            TofixDraw.draw_Node(tofixLayer, latLon);
        } else {
            accessTaskBean.setAccess(false);
            Util.error_request_data();
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
            LatLon latLon = new LatLon(itemUnconnectedBean.getValue().getY(), itemUnconnectedBean.getValue().getX());
            bounds = new Bounds(latLon.toBBox(size_bounds).toRectangle());
            TofixDraw.draw_Node(tofixLayer, latLon);
        } else {
            accessTaskBean.setAccess(false);
            Util.error_request_data();
        }
    }

    private void get_item_nycbuildings() {
        ItemNycbuildingsBean itemNycbuildingsBean = null;
        itemController.setUrl(accessTaskBean.getTask_url());
        itemNycbuildingsBean = itemController.getItemNycbuildingsBean();
        if (itemNycbuildingsBean != null) {
            accessTaskBean.setAccess(true);
            accessTaskBean.setOsm_obj_id(Util.format_Elems_Nycbuildings(itemNycbuildingsBean.getValue().getElems()));
            accessTaskBean.setKey(itemNycbuildingsBean.getKey());
            LatLon latLon = new LatLon(itemNycbuildingsBean.getValue().getLat(), itemNycbuildingsBean.getValue().getLon());
            bounds = new Bounds(latLon.toBBox(size_bounds).toRectangle());
            TofixDraw.draw_Node(tofixLayer, latLon);
        } else {
            accessTaskBean.setAccess(false);
            Util.error_request_data();
        }

    }

    private void get_item_tigerdelta() {
        ItemTigerdeltaBean itemTigerdeltaBean = null;
        itemController.setUrl(accessTaskBean.getTask_url());
        itemTigerdeltaBean = itemController.getItemTigerdeltaBean();
        if (itemTigerdeltaBean != null) {
            accessTaskBean.setAccess(true);
            //accessTaskBean.setOsm_obj_id(itemTigerdeltaBean.getValue().getWay());
            accessTaskBean.setOsm_obj_id(0x0L);//null porque no exixte el id del objeto
            accessTaskBean.setKey(itemTigerdeltaBean.getKey());
            List<List<Node>> list = itemTigerdeltaBean.getValue().get_coordinates();
            LatLon latLon = new LatLon(list.get(0).get(0).getCoor().lat(), list.get(0).get(0).getCoor().lon());//  Util.print(latLon);
            bounds = new Bounds(latLon.toBBox(size_bounds).toRectangle());
            TofixDraw.draw_line(tofixLayer, latLon, list);
        } else {
            accessTaskBean.setAccess(false);
            Util.error_request_data();
        }

    }

    private void get_item_krakatoa() {
        ItemKrakatoaBean itemKrakatoaBean = null;
        itemController.setUrl(accessTaskBean.getTask_url());
        itemKrakatoaBean = itemController.getItemKrakatoBean();
        if (itemKrakatoaBean != null) {
            accessTaskBean.setAccess(true);
            accessTaskBean.setOsm_obj_id(0x0L);//porque no existe el id de objetos e este task
            accessTaskBean.setKey(itemKrakatoaBean.getKey());

            Util.print(itemKrakatoaBean.getValue().getGeom());
            List<Node> list = itemKrakatoaBean.getValue().get_coordinates();
            LatLon latLon = new LatLon(list.get(0).getCoor().lat(), list.get(0).getCoor().lon());
            bounds = new Bounds(latLon.toBBox(size_bounds).toRectangle());
            TofixDraw.draw_nodes(tofixLayer, latLon, list);
        } else {
            accessTaskBean.setAccess(false);
            Util.error_request_data();
        }
    }

}
