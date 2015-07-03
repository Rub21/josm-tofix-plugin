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
import org.openstreetmap.josm.plugins.tofix.bean.AccessToTask;
import org.openstreetmap.josm.plugins.tofix.bean.AttributesBean;
import org.openstreetmap.josm.plugins.tofix.bean.FixedBean;
import org.openstreetmap.josm.plugins.tofix.bean.ListTaskBean;
import org.openstreetmap.josm.plugins.tofix.bean.TaskCompleteBean;
import org.openstreetmap.josm.plugins.tofix.bean.TrackBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.Item;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemKeeprightBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemKrakatoaBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemNycbuildingsBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemTigerdeltaBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemUnconnectedBean;
import org.openstreetmap.josm.plugins.tofix.controller.ItemController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemEditController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemFixedController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemTrackController;
import org.openstreetmap.josm.plugins.tofix.controller.ListTaskController;
import org.openstreetmap.josm.plugins.tofix.layer.TofixLayer;
import org.openstreetmap.josm.plugins.tofix.util.*;
import org.openstreetmap.josm.plugins.tofix.util.Config;
import static org.openstreetmap.josm.tools.I18n.tr;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.OpenBrowser;
import org.openstreetmap.josm.tools.Shortcut;

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
    private final double size_bounds = 0.003;//extent to download
    AccessToTask mainAccessToTask = null;
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

    TofixTask tofixTask = new TofixTask();

    public TofixDialog() {

        super(tr("To-fix"), "icontofix", tr("Open to-fix window."),
                Shortcut.registerShortcut("Tool:To-fix", tr("Toggle: {0}", tr("Tool:To-fix")),
                        KeyEvent.VK_T, Shortcut.ALT_CTRL_SHIFT), 70);

        // "Skip" button
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
                OpenBrowser.displayUrl(Config.url_tofix);
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
            } else {
                // Request data
                mainAccessToTask = new AccessToTask("mixedlayer", "keepright", false);//start mixedlayer task by default
                //Shortcuts
                skipShortcut = Shortcut.registerShortcut("tofix:skip", tr("tofix:Skip item"), KeyEvent.VK_S, Shortcut.ALT_SHIFT);
                Main.registerActionShortcut(new Skip_key_Action(), skipShortcut);

                fixedShortcut = Shortcut.registerShortcut("tofix:fixed", tr("tofix:Fixed item"), KeyEvent.VK_F, Shortcut.ALT_SHIFT);
                Main.registerActionShortcut(new Fixed_key_Action(), fixedShortcut);

                noterrorButtonShortcut = Shortcut.registerShortcut("tofix:noterror", tr("tofix:Not a Error item"), KeyEvent.VK_N, Shortcut.ALT_SHIFT);
                Main.registerActionShortcut(new NotError_key_Action(), noterrorButtonShortcut);
            }
        }
    }

    private void TofixTask(Item item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            mainAccessToTask.setTask_name(listTaskBean.getTasks().get(cb.getSelectedIndex() - 1).getTitle());
            mainAccessToTask.setTask_id(listTaskBean.getTasks().get(cb.getSelectedIndex() - 1).getId());
            mainAccessToTask.setTask_source(listTaskBean.getTasks().get(cb.getSelectedIndex() - 1).getSource());
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
        if (mainAccessToTask.isAccess()) {
            Util.print("Mandado a Edition");
            Util.print(mainAccessToTask.getTask_url());
            Util.print(mainAccessToTask.getKey());
            TrackBean trackBean = new TrackBean();
            AttributesBean attributesBean = new AttributesBean();
            attributesBean.setAction("edit");
            attributesBean.setEditor("josm");
            attributesBean.setUser(josmUserIdentityManager.getUserName());
            attributesBean.setKey(mainAccessToTask.getKey());
            trackBean.setAttributes(attributesBean);
            ItemEditController itemEditController = new ItemEditController(mainAccessToTask.getTrack_url(), trackBean);
            itemEditController.sendTrackBean();
        }
    }

    public void skip() {
        if (mainAccessToTask.isAccess()) {
            Util.print("Skipt");
            Util.print(mainAccessToTask.getTask_url());
            Util.print(mainAccessToTask.getKey());
            TrackBean trackBean = new TrackBean();
            AttributesBean attributesBean = new AttributesBean();
            attributesBean.setAction("skip");
            attributesBean.setEditor("josm");
            attributesBean.setUser(josmUserIdentityManager.getUserName());
            attributesBean.setKey(mainAccessToTask.getKey());
            trackBean.setAttributes(attributesBean);
            ItemTrackController skipController = new ItemTrackController(mainAccessToTask.getTrack_url(), trackBean);
            skipController.sendTrackBean();
        }
        get_new_item();
    }

    public void fixed() {
        if (mainAccessToTask.isAccess()) {
            Util.print("Arreglado");
            Util.print(mainAccessToTask.getTask_url());
            Util.print(mainAccessToTask.getKey());
            FixedBean itemFixedBean = new FixedBean();
            itemFixedBean.setUser(josmUserIdentityManager.getUserName());
            itemFixedBean.setKey(mainAccessToTask.getKey());
            //itemFixedBean.setEditor("josm");
            ItemFixedController itemFixedController = new ItemFixedController(mainAccessToTask.getFixed_url(), itemFixedBean);
            itemFixedController.sendTrackBean();
        }
        get_new_item();
    }

    public void noterror() {
        if (mainAccessToTask.isAccess()) {
            Util.print("No es un Error");
            Util.print(mainAccessToTask.getTask_url());
            Util.print(mainAccessToTask.getKey());
            TrackBean trackBean = new TrackBean();
            AttributesBean attributesBean = new AttributesBean();
            attributesBean.setAction("noterror");
            attributesBean.setEditor("josm");
            attributesBean.setUser(josmUserIdentityManager.getUserName());
            attributesBean.setKey(mainAccessToTask.getKey());
            trackBean.setAttributes(attributesBean);
            ItemTrackController notaerrorController = new ItemTrackController(mainAccessToTask.getTrack_url(), trackBean);
            notaerrorController.sendTrackBean();
        }
        get_new_item();
    }

    private void get_new_item() {
        itemController.setAccessToTask(mainAccessToTask);
        Item item = itemController.getItem();
        switch (item.getStatus()) {
            case 200:
                mainAccessToTask.setAccess(true);
                mainAccessToTask = tofixTask.work(item, mainAccessToTask);
                Util.print("ya sido descargado");
                Util.print(mainAccessToTask.getTask_url());
                Util.print(mainAccessToTask.getKey());
                edit();
                break;
            case 410:
                mainAccessToTask.setAccess(false);
                tofixTask.task_complete(item, mainAccessToTask);
                break;

            case 404:
                mainAccessToTask.setAccess(false);
                JOptionPane.showMessageDialog(Main.panel, "Maitenace server");
                break;

            default:
                mainAccessToTask.setAccess(false);
                JOptionPane.showMessageDialog(Main.panel, "Somethig when wrong in server");
        }
    }

}
