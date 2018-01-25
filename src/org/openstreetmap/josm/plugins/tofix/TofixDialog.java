package org.openstreetmap.josm.plugins.tofix;

import java.awt.Button;

import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import org.openstreetmap.josm.Main;

import org.openstreetmap.josm.actions.UploadAction;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.gui.io.UploadDialog;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.plugins.tofix.bean.AccessToProject;
import org.openstreetmap.josm.plugins.tofix.bean.ListProjectBean;
import org.openstreetmap.josm.plugins.tofix.bean.ProjectBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemBean;
import org.openstreetmap.josm.plugins.tofix.controller.ItemController;
import org.openstreetmap.josm.plugins.tofix.controller.ItemTrackController;
import org.openstreetmap.josm.plugins.tofix.controller.ListProjectsController;
import org.openstreetmap.josm.plugins.tofix.util.Config;
import org.openstreetmap.josm.plugins.tofix.util.Status;
import org.openstreetmap.josm.plugins.tofix.util.Util;
import static org.openstreetmap.josm.tools.I18n.tr;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.OpenBrowser;
import org.openstreetmap.josm.tools.Shortcut;

/**
 *
 * @author ruben
 */
public final class TofixDialog extends ToggleDialog implements ActionListener {

    MapView mv = MainApplication.getMap().mapView;
    //PANELS
    ItemTrackController itemTrackController = new ItemTrackController();
    JTabbedPane TabbedPanel = new javax.swing.JTabbedPane();
    JPanel jContentPanelProjects = new JPanel(new GridLayout(2, 1));
    JPanel jContenActivation = new JPanel(new GridLayout(3, 1));
    JPanel jPanelProjects = new JPanel(new GridLayout(1, 1));
    JPanel jPanelQuery = new JPanel(new GridLayout(2, 1));
    JPanel jPanelDeleteLayer = new JPanel(new GridLayout(1, 1));
    JPanel jPanelSetNewAPI = new JPanel(new GridLayout(2, 1));

    //OBJECTS FOR EVNETS
    private final JLabel JlabelTitleProject;
    private final SideButton skipButton;
    private final SideButton fixedButton;
    private final SideButton noterrorButton;
    private final Button bboxButton;
    private final JTextField bboxJtextField;
    private final Shortcut skipShortcut;
    private final Shortcut fixedShortcut;
    private final Shortcut noterrorButtonShortcut;
    private final JComboBox<String> jcomboBox;
    private final JCheckBox jCheckBoxDeleteLayer;
    private JCheckBox jCheckBoxSetNewAPI;

    //VARS
    double zise = 0.0006; //size to download,per default
    boolean validator;
    private boolean needDeleteLayer;
    ArrayList<String> listStringsForCombo = new ArrayList<>();

    // LOCAL
    AccessToProject mainAccessToProject = null;
    ProjectBean project = new ProjectBean();
    ListProjectsController listProjectController = new ListProjectsController();
    ListProjectBean projectsList = null;
    ItemBean item = new ItemBean();
    ItemController itemController = new ItemController();
    TofixProject tofixProject = new TofixProject();

    public TofixDialog() {
        super(tr("To-fix"), "icontofix", tr("Open to-fix window."),
                Shortcut.registerShortcut("Tool:To-fix", tr("Toggle: {0}", tr("Tool:To-fix")),
                        KeyEvent.VK_T, Shortcut.ALT_CTRL_SHIFT), 170);

//==============================================================================SETUP LINK TO THE PROJECT        
        JlabelTitleProject = new javax.swing.JLabel();
        JlabelTitleProject.setText(tr("<html><a href=\"\">List of projects</a></html>"));
        JlabelTitleProject.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JlabelTitleProject.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e
            ) {
                OpenBrowser.displayUrl(Config.URL_TOFIX);
            }
        });
        jContentPanelProjects.add(JlabelTitleProject);

//==============================================================================FILL COMBO
        listStringsForCombo.add(tr("Select a project ..."));
        jcomboBox = new JComboBox<>(listStringsForCombo.toArray(new String[]{}));
        jcomboBox.addActionListener(this);
        jPanelProjects.add(jcomboBox);
        jContentPanelProjects.add(jPanelProjects);

        fillCombo();
//==============================================================================AUTO DELETE LAYER
        jContenActivation.add(new Label(tr("Select the checkbox to:")));

        jCheckBoxDeleteLayer = new JCheckBox(tr("Auto delete layer"));
        jCheckBoxDeleteLayer.setSelected(true);
        needDeleteLayer = jCheckBoxDeleteLayer.isSelected();
        jCheckBoxDeleteLayer.addItemListener((ItemEvent e) -> {
            needDeleteLayer = e.getStateChange() == ItemEvent.SELECTED;
        });
        jPanelDeleteLayer.add(jCheckBoxDeleteLayer);
        jContenActivation.add(jPanelDeleteLayer);
//==============================================================================CONFIG API URL
        jCheckBoxSetNewAPI = new JCheckBox(tr("Set default API"));
        jCheckBoxSetNewAPI.setSelected(true);
        jCheckBoxSetNewAPI.addActionListener((ActionEvent e) -> {
            if (jCheckBoxSetNewAPI.isSelected()) {
                Config.setHOST(Config.DEFAULT_HOST);
                JOptionPane.showMessageDialog(Main.parent, tr("Setting default URL"));
                fillCombo();
            } else {

                try {
                    String newHost = JOptionPane.showInputDialog(tr("Enter the new URL"));
                    if (newHost == null || (newHost != null && ("".equals(newHost)))) {
                        Config.setHOST(Config.DEFAULT_HOST);
                        jCheckBoxSetNewAPI.setSelected(true);
                    } else {
                        Config.setHOST(newHost);
                        fillCombo();
                    }
                } catch (HeadlessException exc) {
                }
            }
        });
        jPanelSetNewAPI.add(jCheckBoxSetNewAPI);
        jContenActivation.add(jPanelSetNewAPI);
//BUTTONS
//=============================================================================="Skip" button
        skipButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Skip"));
                new ImageProvider("mapmode", "skip").getResource().attachImageIcon(this, true);
                putValue(SHORT_DESCRIPTION, tr("Skip Error"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                skip();
                deleteLayer();

            }
        });
        skipButton.setEnabled(false);
        skipShortcut = Shortcut.registerShortcut("tofix:skip", tr("tofix:Skip item"),
                KeyEvent.VK_S, Shortcut.ALT_SHIFT);
        MainApplication.registerActionShortcut(new skipKeyAction(), skipShortcut);
//=============================================================================="Fixed" button
        fixedButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Fixed"));
                new ImageProvider("mapmode", "fixed").getResource().attachImageIcon(this, true);
                putValue(SHORT_DESCRIPTION, tr("Fixed Error"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                eventFixed(e);

            }
        });
        fixedButton.setEnabled(false);
        fixedShortcut = Shortcut.registerShortcut("tofix:fixed", tr("tofix:Fixed item"),
                KeyEvent.VK_F, Shortcut.ALT_SHIFT);
        MainApplication.registerActionShortcut(new fixedKeyAction(), fixedShortcut);
//=============================================================================="Not a error" button
        noterrorButton = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Not an error"));
                new ImageProvider("mapmode", "noterror").getResource().attachImageIcon(this, true);
                putValue(SHORT_DESCRIPTION, tr("Not an error"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                notError();
                deleteLayer();

            }
        });
        noterrorButton.setEnabled(false);
        noterrorButtonShortcut = Shortcut.registerShortcut("tofix:noterror", tr("tofix:Not a Error item"),
                KeyEvent.VK_N, Shortcut.ALT_SHIFT);
        MainApplication.registerActionShortcut(new NotError_key_Action(), noterrorButtonShortcut);
//============================================================================== Select bbox button+jtextfield
        bboxJtextField = new JTextField();
        bboxButton = new Button(tr("Bbox"));
        bboxButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //BBox - bbox extent in minX, minY, maxX, maxY order
                Bounds bounds = mv.getRealBounds();
                String bbox = String.valueOf(bounds.getMinLat()) + ',' + String.valueOf(bounds.getMinLon()) + ',' + String.valueOf(bounds.getMaxLat()) + ',' + String.valueOf(bounds.getMaxLon());
                bboxJtextField.setText(bbox);
            }
        });
        jPanelQuery.add(bboxButton);
        jPanelQuery.add(bboxJtextField);
//============================================================================== FILL PANELS
        //PANEL TASKS
        jPanelProjects.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanelQuery.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanelDeleteLayer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanelSetNewAPI.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        TabbedPanel.addTab(tr("Projects"), jContentPanelProjects);
        TabbedPanel.addTab(tr("Activation"), jContenActivation);
        TabbedPanel.addTab(tr("Querying"), jPanelQuery);
        //add panel in JOSM
        createLayout(TabbedPanel, false, Arrays.asList(new SideButton[]{
            skipButton, noterrorButton, fixedButton
        }));

    }

//==============================================================================OBJECT EVENTS==============================================================================
    public void fillCombo() {
        listStringsForCombo.clear();
        listStringsForCombo.add(tr("Select a project ..."));
        if (Status.isInternetReachable()) {
            if (Status.serverStatus()) {
                projectsList = listProjectController.getListProjects();
                for (int i = 0; i < projectsList.getProjects().size(); i++) {
                    listStringsForCombo.add(projectsList.getProjects().get(i).getName());
                }
                jcomboBox.setModel(new DefaultComboBoxModel<>());
                jcomboBox.setModel(new DefaultComboBoxModel<>(listStringsForCombo.toArray(new String[]{})));

            } else {
                jcomboBox.setModel(new DefaultComboBoxModel<>());
                jcomboBox.setModel(new DefaultComboBoxModel<>(listStringsForCombo.toArray(new String[]{})));
                Util.alert("Check your url:" + Config.getHOST());
            }
        } else {
            skipButton.setEnabled(false);
            fixedButton.setEnabled(false);
            noterrorButton.setEnabled(false);
        }
    }

// Event select a project, it automatic will get a item and display
    @Override
    public void actionPerformed(ActionEvent e) {
        start();
        JComboBox<?> cb = (JComboBox<?>) e.getSource();
        if (cb.getSelectedIndex() != 0) {
            mainAccessToProject.setProject_id(projectsList.getProjects().get(cb.getSelectedIndex() - 1).getId());
            mainAccessToProject.setProject_name(projectsList.getProjects().get(cb.getSelectedIndex() - 1).getName());
            project = projectsList.getProjects().get(cb.getSelectedIndex() - 1);

            deleteLayer();
            getNewItem();
            skipButton.setEnabled(true);
            fixedButton.setEnabled(true);
            noterrorButton.setEnabled(true);
        } else {
            skipButton.setEnabled(false);
            fixedButton.setEnabled(false);
            noterrorButton.setEnabled(false);
        }
    }

    public class skipKeyAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            skip();
            deleteLayer();

        }
    }

    public class fixedKeyAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            eventFixed(e);

        }
    }

    public class NotError_key_Action extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            notError();
            deleteLayer();

        }
    }

    private void eventFixed(ActionEvent e) {
        if (!MainApplication.getLayerManager().getEditDataSet().isModified()) {
            new Notification(tr("No change to upload!")).show();
            //Be sure you mark as fixed
            fixed();
        } else if (new Bounds(MainApplication.getLayerManager().getEditDataSet().getDataSourceArea().getBounds()).getArea() < 30) {
            validator = false;
            UploadDialog.getUploadDialog().addComponentListener(new ComponentAdapter() {
                @Override
                public void componentShown(ComponentEvent e) {
                    validator = true;
                }
            });
            DataSet data = MainApplication.getLayerManager().getEditLayer().data;
            project.getChangesetComment();
            //Set up the changeset comment and the source
            if (!project.getChangesetComment().equals("none")) {
                data.getChangeSetTags().put("comment", project.getChangesetComment());
            }
            data.getChangeSetTags().put("source", MainApplication.getMap().mapView.getLayerInformationForSourceTag());
            new UploadAction().actionPerformed(e);

            if (validator && !UploadDialog.getUploadDialog().isCanceled() && UploadDialog.getUploadDialog().getChangeset().isNew()) {
                fixed();
                deleteLayer();
            }
        } else {
            new Notification(tr("The bounding box of the edited layer is too big.")).show();
        }
    }

    //==============================================================================FUNCTIONS==============================================================================
    private void getNewItem() {
        itemController.setAccessToProject(mainAccessToProject);
        item = itemController.getItem();
        switch (item.getStatusServer()) {
            case 200:
                mainAccessToProject.setAccess(true); //This atribute to access to  the actions
                mainAccessToProject = tofixProject.work(item, mainAccessToProject, zise);
                edit();
                break;
            case 410:
                mainAccessToProject.setAccess(false);
                break;
            case 503:
                mainAccessToProject.setAccess(false);
                new Notification(tr("Maintenance server")).show();
                break;
            default:
                mainAccessToProject.setAccess(false);
                new Notification(tr("Something went wrong, try again")).show();
        }
    }

//Actions
    public void edit() {
        if (mainAccessToProject.isAccess()) {
            itemTrackController.lockItem(item, "locked");
        }
    }

    public void skip() {
        if (mainAccessToProject.isAccess()) {
            itemTrackController.lockItem(item, "unlocked");
            getNewItem();
        }
    }

    public void fixed() {
        if (mainAccessToProject.isAccess()) {
            itemTrackController.updateStatusItem(item, "fixed");
            getNewItem();
        }
    }

    public void notError() {
        if (mainAccessToProject.isAccess()) {
            itemTrackController.updateStatusItem(item, "noterror");
            getNewItem();
        }
    }

    public final void start() {
        mainAccessToProject = new AccessToProject("mixedlayer", false);//start mixedlayer task by default
    }

    public void downloadCancelled() {
        skip();
        deleteLayer();
    }

    public void deleteLayer() {
        if (needDeleteLayer) {
            OsmDataLayer editLayer = MainApplication.getLayerManager().getEditLayer();
            if (editLayer != null) {
                editLayer.data.clear();
                MainApplication.getLayerManager().removeLayer(editLayer);
            }
        }
    }
}
