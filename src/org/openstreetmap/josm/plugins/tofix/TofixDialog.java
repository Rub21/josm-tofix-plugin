package org.openstreetmap.josm.plugins.tofix;

import java.awt.Button;

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

import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
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
public class TofixDialog extends ToggleDialog implements ActionListener {

    boolean validator;
    private final SideButton skipButton;
    private final SideButton fixedButton;
    private final SideButton noterrorButton;
    private final Button bboxButton;
    private final JTextField bboxJtextField;
    private Shortcut skipShortcut = null;
    private Shortcut fixedShortcut = null;
    private Shortcut noterrorButtonShortcut = null;

    //size to download
    double zise = 0.0006; //per default
    AccessToProject mainAccessToProject = null;
    //Project
    ProjectBean project = new ProjectBean();
    // Projects list
    ListProjectBean projectsList = null;
    ListProjectsController listProjectController = new ListProjectsController();
    //Item
    ItemBean item = new ItemBean();
    ItemController itemController = new ItemController();
    // To-Fix layer
    MapView mv = MainApplication.getMap().mapView;
    ItemTrackController itemTrackController = new ItemTrackController();
    JTabbedPane TabbedPanel = new javax.swing.JTabbedPane();

    JPanel jcontenTasks = new JPanel(new GridLayout(2, 1));
    JPanel valuePanel = new JPanel(new GridLayout(1, 1));

    JPanel jcontenConfig = new JPanel(new GridLayout(2, 1));
    JPanel panelQuery = new JPanel(new GridLayout(2, 1));

    JPanel jcontenActivation = new JPanel(new GridLayout(3, 1));
    JPanel panelactivationPlugin = new JPanel(new GridLayout(1, 1));
    JPanel panelactivationLayer = new JPanel(new GridLayout(1, 1));
    JPanel panelactivationUrl = new JPanel(new GridLayout(2, 1));

    UserIdentityManager josmUserIdentityManager = UserIdentityManager.getInstance();

    TofixProject tofixProject = new TofixProject();
    private boolean needDeleteLayer;

    public TofixDialog() {

        super(tr("To-fix"), "icontofix", tr("Open to-fix window."),
                Shortcut.registerShortcut("Tool:To-fix", tr("Toggle: {0}", tr("Tool:To-fix")),
                        KeyEvent.VK_T, Shortcut.ALT_CTRL_SHIFT), 170);

        ArrayList<String> projectsList = new ArrayList<>();

//==============================================================================AUTO DELETE LAYER
        JCheckBox checkLayer = new JCheckBox(tr("Auto delete layer"));
        checkLayer.setSelected(true);
        needDeleteLayer = checkLayer.isSelected();

        checkLayer.addItemListener((ItemEvent e) -> {
            needDeleteLayer = e.getStateChange() == ItemEvent.SELECTED;
        });

//==============================================================================CONFIG API URL
        JCheckBox checkSetNewAPIUrl = new JCheckBox(tr("Set default url"));
        checkSetNewAPIUrl.setSelected(true);

        checkSetNewAPIUrl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (checkSetNewAPIUrl.isSelected()) {
                    Config.setHOST(Config.DEFAULT_HOST);
                    JOptionPane.showMessageDialog(Main.parent, tr("Setting default URL"));
                } else {
                    try {
                        String newHost = JOptionPane.showInputDialog(tr("Enter the new URL"));
                        if (newHost.isEmpty()) {
                            Config.setHOST(Config.DEFAULT_HOST);
                            JOptionPane.showMessageDialog(Main.parent, tr("Setting default URL"));
                        } else {
                            Config.setHOST(newHost);
                            JOptionPane.showMessageDialog(Main.parent, tr("Setting new URL: " + newHost));
                        }
                    } catch (Exception exc) {
                    }
                }

                if (Status.server()) {
//                              listTaskController = new ListTaskController();
//                    projectsList.clear();
//                    projectsList.add(tr("Select a task ..."));
//                    pr  = listProjectController.getListProjects();
//                    for (int i = 0; i < listTaskBean.getTasks().size(); i++) {
//                        projectsList.add(listTaskBean.getTasks().get(i).getName());
//                    }
//                    jcomboBox.setModel(new DefaultComboBoxModel<>());
//                    jcomboBox.setModel(new DefaultComboBoxModel<>(projectsList.toArray(new String[]{})));

                }

            }
        }
        );

        jcontenActivation.add(new Label(tr("Select the checkbox to:")));
        panelactivationLayer.add(checkLayer);
        panelactivationUrl.add(checkSetNewAPIUrl);

//        jcontenActivation.add(panelactivationPlugin);
        jcontenActivation.add(panelactivationLayer);
        jcontenActivation.add(panelactivationUrl);

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

        //add tittle for To-fix task
        JLabel title_tasks = new javax.swing.JLabel();
        title_tasks.setText(tr("<html><a href=\"\">List of projects</a></html>"));
        title_tasks.setCursor(new Cursor(Cursor.HAND_CURSOR));
        title_tasks.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e
            ) {
                OpenBrowser.displayUrl(Config.URL_TOFIX);
            }
        });
        jcontenTasks.add(title_tasks);

        // JComboBox for each task
        projectsList.add(tr("Select a project ..."));
        //checkout  internet connection
        if (Status.isInternetReachable()) {
            if (Status.server()) {
                //Shortcuts
                skipShortcut = Shortcut.registerShortcut("tofix:skip", tr("tofix:Skip item"), KeyEvent.VK_S, Shortcut.ALT_SHIFT);
                MainApplication.registerActionShortcut(new skipKeyAction(), skipShortcut);
                fixedShortcut = Shortcut.registerShortcut("tofix:fixed", tr("tofix:Fixed item"), KeyEvent.VK_F, Shortcut.ALT_SHIFT);
                MainApplication.registerActionShortcut(new fixedKeyAction(), fixedShortcut);
                noterrorButtonShortcut = Shortcut.registerShortcut("tofix:noterror", tr("tofix:Not a Error item"), KeyEvent.VK_N, Shortcut.ALT_SHIFT);
                MainApplication.registerActionShortcut(new NotError_key_Action(), noterrorButtonShortcut);
                //List projects
                this.projectsList = listProjectController.getListProjects();
                for (int i = 0; i < this.projectsList.getProjects().size(); i++) {
                    projectsList.add(this.projectsList.getProjects().get(i).getName());
                }

                JComboBox<String> jcomboBox = new JComboBox<>(projectsList.toArray(new String[]{}));
                valuePanel.add(jcomboBox);
                jcomboBox.addActionListener(this);
                jcontenTasks.add(valuePanel);

                //PANEL TASKS
                valuePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                panelQuery.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                panelactivationPlugin.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                panelactivationLayer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                panelactivationUrl.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                TabbedPanel.addTab(tr("Projects"), jcontenTasks);
                TabbedPanel.addTab(tr("Activation"), jcontenActivation);
                panelQuery.add(bboxButton);
                panelQuery.add(bboxJtextField);
                TabbedPanel.addTab(tr("Querying"), panelQuery);

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

//==============================================================================OBJECT EVENTS==============================================================================
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
