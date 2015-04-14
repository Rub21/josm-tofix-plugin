package org.openstreetmap.josm.plugins.tofix;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static javax.swing.Action.SMALL_ICON;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.downloadtasks.DownloadOsmTask;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.plugins.tofix.bean.ListTasksBean;
import org.openstreetmap.josm.plugins.tofix.bean.TaskBean;
import org.openstreetmap.josm.plugins.tofix.controller.ListTasksController;
import org.openstreetmap.josm.plugins.tofix.controller.TaskController;
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

    ListTasksBean listTasksBean = null;
    ListTasksController listTasksController = new ListTasksController("http://osmlab.github.io/to-fix/src/data/tasks.json");

    DownloadOsmTask task = null;
    TaskController taskController = new TaskController("http://54.147.184.23:8000/task/unconnectedmajor");
    TaskBean taskBean = null;

    Bounds bounds = null;

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
                //StatusController statusController = new StatusController("http://54.147.184.23:8000/status");
                // JOptionPane.showMessageDialog(Main.parent, statusController.getStatusBean().getStatus());

                //Dowloan rub21
                task = new DownloadOsmTask();
                Download.Download(task, bounds, taskBean);

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

                taskBean = taskController.getTaskBean();

                LatLon coor = new LatLon(taskBean.getValue().getY(), taskBean.getValue().getX());

                if (coor.isOutSideWorld()) {
                    JOptionPane.showMessageDialog(Main.parent, tr("Can not draw outside of the world."));
                    return;
                }
                BoundingXYVisitor v = new BoundingXYVisitor();

                //double ex = 0.0001; = 2.34 m
                double ex = 0.0007;// 16.7 m
                bounds = new Bounds(taskBean.getValue().getY() - ex, taskBean.getValue().getX() - ex, taskBean.getValue().getY() + ex, taskBean.getValue().getX() + ex);
                v.visit(bounds);
                Main.map.mapView.zoomTo(v);

                // skipButton.setEnabled(!Main.isOffline(OnlineResource.OSM_API)); // agregr para despues
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

        JPanel jcontenpanel = new JPanel(new GridLayout(0, 2));
        jcontenpanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        //Call the tasks
        ButtonGroup group = new ButtonGroup();

        listTasksBean = listTasksController.getListTasksBean();
        for (int i = 0; i < listTasksBean.getTasks().size(); i++) {
            System.out.println(listTasksBean.getTasks().get(i).getId());
            JRadioButton jRadioButton = new JRadioButton(listTasksBean.getTasks().get(i).getTitle());
            group.add(jRadioButton);
            jRadioButton.setActionCommand(listTasksBean.getTasks().get(i).getId());
            jcontenpanel.add(jRadioButton);
            jRadioButton.addActionListener(this);
        }
        this.setPreferredSize(new Dimension(0, 92));
        createLayout(jcontenpanel, false, Arrays.asList(new SideButton[]{
            editButton, skipButton, fixedButton
        }));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(Main.parent, tr(e.getActionCommand()));

    }

    //http://54.147.184.23:8000/count/unconnectedmajor
}
