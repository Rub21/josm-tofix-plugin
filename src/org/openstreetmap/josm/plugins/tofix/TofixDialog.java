package org.openstreetmap.josm.plugins.tofix;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.UploadAction;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.UserIdentityManager;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.gui.io.UploadDialog;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.plugins.tofix.bean.AccessToProject;
import org.openstreetmap.josm.plugins.tofix.bean.ActionBean;
import org.openstreetmap.josm.plugins.tofix.bean.ListProjectBean;
import org.openstreetmap.josm.plugins.tofix.bean.TrackBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.Item;
import org.openstreetmap.josm.plugins.tofix.controller.ItemController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemTrackController;
import org.openstreetmap.josm.plugins.tofix.controller.ListProjectsController;
import org.openstreetmap.josm.plugins.tofix.util.Config;
import org.openstreetmap.josm.plugins.tofix.util.Status;
import org.openstreetmap.josm.plugins.tofix.util.Util;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.OpenBrowser;
import org.openstreetmap.josm.tools.Shortcut;

/**
 *
 * @author ruben
 */
public class TofixDialog extends ToggleDialog implements ActionListener {
    
    boolean validator;
    private final SideButton skipButton;
    private final SideButton fixedButton;
    private final SideButton noterrorButton;
    private Shortcut skipShortcut = null;
    private Shortcut fixedShortcut = null;
    private Shortcut noterrorButtonShortcut = null;
    JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);

    //size to download
    double zise = 0.0006; //per default

    AccessToProject mainAccessToProject = null;
    // Project list
    ListProjectBean listProjectBean = null;
    ListProjectsController listProjectController = new ListProjectsController();

    //Item
    Item item = new Item();
    ItemController itemController = new ItemController();

    // To-Fix layer
    MapView mv = MainApplication.getMap().mapView;
    
    ItemTrackController itemTrackController = new ItemTrackController();
    
    JTabbedPane TabbedPanel = new javax.swing.JTabbedPane();
    
    JPanel jcontenTasks = new JPanel(new GridLayout(2, 1));
    JPanel valuePanel = new JPanel(new GridLayout(1, 1));
    
    JPanel jcontenConfig = new JPanel(new GridLayout(2, 1));
    JPanel panelslide = new JPanel(new GridLayout(1, 1));
    
    JPanel jcontenActivation = new JPanel(new GridLayout(4, 1));
    JPanel panelactivationPlugin = new JPanel(new GridLayout(1, 1));
    JPanel panelactivationLayer = new JPanel(new GridLayout(1, 1));
    JPanel panelactivationUrl = new JPanel(new GridLayout(2, 1));
    
    UserIdentityManager josmUserIdentityManager = UserIdentityManager.getInstance();
    
    TofixTask tofixTask = new TofixTask();
    boolean checkboxStatus;
    boolean checkboxStatusLayer;
    JCheckBox checkPlugin;
    
    public TofixDialog() {
        
        super(tr("To-fix"), "icontofix", tr("Open to-fix window."),
                Shortcut.registerShortcut("Tool:To-fix", tr("Toggle: {0}", tr("Tool:To-fix")),
                        KeyEvent.VK_T, Shortcut.ALT_CTRL_SHIFT), 170);

        //ENABLE-DISABLE CHECKBOX
        checkPlugin = new JCheckBox(tr("Enable Tofix plugin"));
        checkPlugin.setSelected(true);
        checkboxStatus = checkPlugin.isSelected();
        
        checkPlugin.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    checkboxStatus = true;
                } else {
                    checkboxStatus = false;
                }
                return;
            }
        });

        //AUTO DELETE LAYER
        JCheckBox checkLayer = new JCheckBox(tr("Auto delete layer"));
        checkLayer.setSelected(true);
        checkboxStatusLayer = checkLayer.isSelected();
        
        checkLayer.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                checkboxStatusLayer = e.getStateChange() == ItemEvent.SELECTED;
            }
        });

        //CONFIG URL
        JCheckBox checkUrl = new JCheckBox(tr("Set default url"));
        checkUrl.setSelected(true);
        
        jcontenActivation.add(new Label(tr("Select the checkbox to:")));
        panelactivationPlugin.add(checkPlugin);
        
        panelactivationLayer.add(checkLayer);
        
        panelactivationUrl.add(checkUrl);
        
        jcontenActivation.add(panelactivationPlugin);
        
        jcontenActivation.add(panelactivationLayer);
        
        jcontenActivation.add(panelactivationUrl);

        //BUTTONS
        // "Skip" button
        skipButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Skip"));
                new ImageProvider("mapmode", "skip").getResource().attachImageIcon(this, true);
                putValue(SHORT_DESCRIPTION, tr("Skip Error"));
            }
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkboxStatus) {
                    action("skip");
                    deleteLayer();
                    
                } else {
                    msg();
                }
            }
        });
        
        skipButton.setEnabled(
                false);

        // "Fixed" button
        fixedButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Fixed"));
                new ImageProvider("mapmode", "fixed").getResource().attachImageIcon(this, true);
                putValue(SHORT_DESCRIPTION, tr("Fixed Error"));
            }
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkboxStatus) {
                    eventFixed(e);
                } else {
                    msg();
                }
            }
        });
        
        fixedButton.setEnabled(false);

        // "Not a error" button
        noterrorButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Not an error"));
                new ImageProvider("mapmode", "noterror").getResource().attachImageIcon(this, true);
                putValue(SHORT_DESCRIPTION, tr("Not an error"));
            }
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkboxStatus) {
                    action("noterror");
                    deleteLayer();
                } else {
                    msg();
                }
            }
        });
        
        noterrorButton.setEnabled(false);

        //add tittle for To-fix task
        JLabel title_tasks = new javax.swing.JLabel();
        
        title_tasks.setText(tr("<html><a href=\"\">List of tasks</a></html>"));
        title_tasks.setCursor(
                new Cursor(Cursor.HAND_CURSOR));
        title_tasks.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e
            ) {
                OpenBrowser.displayUrl(Config.URL_TOFIX);
            }
        }
        );
        jcontenTasks.add(title_tasks);

        // JComboBox for each task
        ArrayList<String> projectsList = new ArrayList<>();
        projectsList.add(tr("Select a task ..."));
        
        if (Status.isInternetReachable()) { //checkout  internet connection
            if (Status.server()) {
                //Shortcuts
//                skipShortcut = Shortcut.registerShortcut("tofix:skip", tr("tofix:Skip item"), KeyEvent.VK_S, Shortcut.ALT_SHIFT);
//                MainApplication.registerActionShortcut(new Skip_key_Action(), skipShortcut);
//                fixedShortcut = Shortcut.registerShortcut("tofix:fixed", tr("tofix:Fixed item"), KeyEvent.VK_F, Shortcut.ALT_SHIFT);
//                MainApplication.registerActionShortcut(new Fixed_key_Action(), fixedShortcut);
//                noterrorButtonShortcut = Shortcut.registerShortcut("tofix:noterror", tr("tofix:Not a Error item"), KeyEvent.VK_N, Shortcut.ALT_SHIFT);
//                MainApplication.registerActionShortcut(new NotError_key_Action(), noterrorButtonShortcut);

                //List projects
                listProjectBean = listProjectController.getListProjects();
                for (int i = 0; i < listProjectBean.getProjects().size(); i++) {
                    projectsList.add(listProjectBean.getProjects().get(i).getName());
                }
                
                JComboBox<String> jcomboBox = new JComboBox<>(projectsList.toArray(new String[]{}));
                valuePanel.add(jcomboBox);
                jcomboBox.addActionListener(this);
                jcontenTasks.add(valuePanel);

                //add title to download
                jcontenConfig.add(new Label(tr("Set download area (m²)")));

                //Add Slider to download
                slider.setMinorTickSpacing(2);
                slider.setMajorTickSpacing(5);
                slider.setPaintTicks(true);
                slider.setPaintLabels(true);
                
                Hashtable<Integer, JLabel> table = new Hashtable<>();
                table.put(1, new JLabel(tr("~.02")));
                table.put(3, new JLabel("~.20"));
                table.put(5, new JLabel("~.40"));
                slider.setLabelTable(table);
                
                slider.addChangeListener(new javax.swing.event.ChangeListener() {
                    @Override
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        zise = slider.getValue() * 0.001;
                    }
                });
                panelslide.add(slider);
                jcontenConfig.add(panelslide);

                //PANEL TASKS
                valuePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                panelslide.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                panelactivationPlugin.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                panelactivationLayer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                panelactivationUrl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                
                TabbedPanel.addTab(tr("Tasks"), jcontenTasks);
                TabbedPanel.addTab(tr("Config"), jcontenConfig);
                TabbedPanel.addTab(tr("Activation"), jcontenActivation);

                //add panel in JOSM
                createLayout(TabbedPanel, false, Arrays.asList(new SideButton[]{
                    skipButton, noterrorButton, fixedButton
                }));
                
            } else {
                skipButton.setEnabled(false);
                fixedButton.setEnabled(false);
                noterrorButton.setEnabled(false);
            }
        }
    }
    
    public final void start() {
        mainAccessToProject = new AccessToProject("mixedlayer", false);//start mixedlayer task by default
    }
    
    public void msg() {
        JOptionPane.showMessageDialog(
                Main.parent,
                tr("Activate to-fix plugin."),
                tr("Warning"),
                JOptionPane.WARNING_MESSAGE
        );
    }

//    public class Skip_key_Action extends AbstractAction {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            if (checkboxStatus) {
//                action("skip");
//                deleteLayer();
//            } else {
//                msg();
//            }
//        }
//    }
//    public class Fixed_key_Action extends AbstractAction {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            if (checkboxStatus) {
//                eventFixed(e);
//            } else {
//                msg();
//            }
//        }
//    }
//    public class NotError_key_Action extends AbstractAction {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            if (checkboxStatus) {
//                action("noterror");
//                deleteLayer();
//            } else {
//                msg();
//            }
//        }
//    }
    // Event when seelct a projects
    @Override
    public void actionPerformed(ActionEvent e) {
        start();
        JComboBox<?> cb = (JComboBox<?>) e.getSource();
        if (cb.getSelectedIndex() != 0) {
            mainAccessToProject.setProject_id(listProjectBean.getProjects().get(cb.getSelectedIndex() - 1).getId());
            mainAccessToProject.setProject_name(listProjectBean.getProjects().get(cb.getSelectedIndex() - 1).getName());

//            deleteLayer();
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
        if (mainAccessToProject.isAccess()) {
            TrackBean trackBean = new TrackBean();
            trackBean.getAttributes().setEditor("josm");
            trackBean.getAttributes().setUser(josmUserIdentityManager.getUserName());
            itemTrackController.send_track_edit(mainAccessToProject.getProject_url(), trackBean);
        }
    }
    
    public void action(String action) { //fixed, noterror or skip
        if (mainAccessToProject.isAccess()) {
            ActionBean trackBean = new ActionBean();
            trackBean.setAction(action);
            trackBean.setEditor("josm");
            trackBean.setUser(josmUserIdentityManager.getUserName());
            trackBean.setKey(mainAccessToProject.getKey());
            itemTrackController.send_track_action(mainAccessToProject.getProject_url(), trackBean);
        }
        get_new_item();
        
    }
    
    private void get_new_item() {
//        item.setStatus(0);
        itemController.setAccessToProject(mainAccessToProject);
        item = itemController.getItem();
        
        switch (200) {
            case 200:
                        Util.print("================== Num 200 access");

                mainAccessToProject.setAccess(true);
                mainAccessToProject = tofixTask.work(item, mainAccessToProject, zise);
                edit();
                break;
            case 410:
                mainAccessToProject.setAccess(false);
//                tofixTask.task_complete(item, mainAccessToProject);
                break;
            case 503:
                mainAccessToProject.setAccess(false);
                new Notification(tr("Maintenance server")).show();
                break;
            case 520:
                mainAccessToProject.setAccess(false);
                JLabel text = new javax.swing.JLabel();
                text.setText(tr("<html>Something went wrong, please update the plugin or report an issue at <a href=\"\">josm-tofix-plugin/issues</a></html>"));
                text.setCursor(new Cursor(Cursor.HAND_CURSOR));
                text.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        OpenBrowser.displayUrl(Config.URL_TOFIX_ISSUES);
                    }
                });
                JOptionPane.showMessageDialog(Main.parent, text, tr("Warning"), JOptionPane.WARNING_MESSAGE);
                break;
            default:
                mainAccessToProject.setAccess(false);
                new Notification(tr("Something went wrong, try again")).show();
        }
    }
    
    private void eventFixed(ActionEvent e) {
        if (!MainApplication.getLayerManager().getEditDataSet().isModified()) {
            new Notification(tr("No change to upload!")).show();
            action("skipeventFixed");
        } else if (new Bounds(MainApplication.getLayerManager().getEditDataSet().getDataSourceArea().getBounds()).getArea() < 30) {
            validator = false;
            UploadDialog.getUploadDialog().addComponentListener(new ComponentAdapter() {
                @Override
                public void componentShown(ComponentEvent e) {
                    validator = true;
                }
            });
            DataSet data = MainApplication.getLayerManager().getEditLayer().data;
            data.getChangeSetTags().put("comment", mainAccessToProject.getProject_changesetComment());
            data.getChangeSetTags().put("source", MainApplication.getMap().mapView.getLayerInformationForSourceTag());
            
            new UploadAction().actionPerformed(e);
            
            if (validator && !UploadDialog.getUploadDialog().isCanceled() && UploadDialog.getUploadDialog().getChangeset().isNew()) {
                action("fixed");
                deleteLayer();
            }
        } else {
            new Notification(tr("The bounding box is too big.")).show();
        }
    }
    
    public void deleteLayer() {
        if (checkboxStatusLayer) {
            OsmDataLayer editLayer = MainApplication.getLayerManager().getEditLayer();
            if (editLayer != null) {
                editLayer.data.clear();
                MainApplication.getLayerManager().removeLayer(editLayer);
            }
        }
    }
    
    public void downloadCancelled() {
        action("skip");
        deleteLayer();
    }
}
