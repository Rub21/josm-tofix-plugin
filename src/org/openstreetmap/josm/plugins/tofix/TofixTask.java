package org.openstreetmap.josm.plugins.tofix;

import javax.swing.JOptionPane;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.downloadtasks.DownloadOsmTask;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.plugins.tofix.bean.AccessToTask;
import org.openstreetmap.josm.plugins.tofix.bean.items.Item;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemKeeprightBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemUnconnectedBean;
import org.openstreetmap.josm.plugins.tofix.controller.ItemController;
import org.openstreetmap.josm.plugins.tofix.layer.TofixLayer;
import org.openstreetmap.josm.plugins.tofix.util.Config;
import org.openstreetmap.josm.plugins.tofix.util.Download;
import static org.openstreetmap.josm.tools.I18n.tr;

/**
 *
 * @author ruben
 */
public class TofixTask {

    ItemController itemController = new ItemController();

    Bounds bounds = null;
    DownloadOsmTask downloadOsmTask = new DownloadOsmTask();
    Node node = null;
    TofixLayer tofixLayer = new TofixLayer("Tofix-layer");

    public AccessToTask work(Item item, AccessToTask accessToTask) {

        if (accessToTask.getTask_source().equals("unconnected")) {
            accessToTask = work_unconnected(item.getItemUnconnectedBean(), accessToTask);
        }
        if (accessToTask.getTask_source().equals("keepright")) {
            accessToTask = work_keepright(item.getItemKeeprightBean(), accessToTask);
        }
        if (accessToTask.getTask_source().equals("tigerdelta")) {

        }
        if (accessToTask.getTask_source().equals("nycbuildings")) {

        }
        if (accessToTask.getTask_source().equals("krakatoa")) {

        }
        return accessToTask;
    }

    private AccessToTask work_unconnected(ItemUnconnectedBean itemUnconnectedBean, AccessToTask accessToTask) {
        accessToTask.setKey(itemUnconnectedBean.getKey());
        node = itemUnconnectedBean.getValue().get_node();
        bounds = new Bounds(node.getCoor().toBBox(Config.bounds).toRectangle());
        TofixDraw.draw_Node(tofixLayer, node.getCoor());
        Download.Download(downloadOsmTask, bounds, itemUnconnectedBean.getValue().getNode_id());
        return accessToTask;
    }

    private AccessToTask work_keepright(ItemKeeprightBean itemKeeprightBean, AccessToTask accessToTask) {
        accessToTask.setKey(itemKeeprightBean.getKey());
        node = itemKeeprightBean.getValue().get_node();
        bounds = new Bounds(node.getCoor().toBBox(Config.bounds).toRectangle());
        TofixDraw.draw_Node(tofixLayer, node.getCoor());
        Download.Download(downloadOsmTask, bounds, itemKeeprightBean.getValue().getObject_id());
        return accessToTask;

    }

    public void task_complete(Item item, AccessToTask accessToTask) {
        String message = "Task : " + accessToTask.getTask_name() + " was completed\n"
                + "Total items : " + item.getTaskCompleteBean().getMessage().getValue().getTotal();
        //  + "Fixed : " + item.getTaskCompleteBean().getMessage().getValue().getFix() + "\n"
        //  + "Not Error : " + item.getTaskCompleteBean().getMessage().getValue().getNoterror() + "\n"
        //  + "Skip : " + item.getTaskCompleteBean().getMessage().getValue().getSkip() + "\n";
        JOptionPane.showMessageDialog(Main.panel, tr(message));
    }

}
