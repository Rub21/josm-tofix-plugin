package org.openstreetmap.josm.plugins.tofix;

import javax.swing.JOptionPane;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.downloadtasks.DownloadOsmTask;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.plugins.tofix.bean.AccessToTask;
import org.openstreetmap.josm.plugins.tofix.bean.items.Item;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemUnconnectedBean;
import org.openstreetmap.josm.plugins.tofix.controller.ItemController;
import org.openstreetmap.josm.plugins.tofix.layer.TofixLayer;
import org.openstreetmap.josm.plugins.tofix.util.Config;
import org.openstreetmap.josm.plugins.tofix.util.Download;

/**
 *
 * @author ruben
 */
public class TofixTask {

    ItemController itemController = new ItemController();

    Bounds bounds = null;
    DownloadOsmTask downloadOsmTask = new DownloadOsmTask();

    TofixLayer tofixLayer = new TofixLayer("Tofix-layer");

    public AccessToTask work(Item item, AccessToTask accessToTask) {

        if (accessToTask.getTask_source().equals("unconnected")) {
            accessToTask = work_unconnected(item.getItemUnconnectedBean(), accessToTask);
        }
        if (accessToTask.getTask_source().equals("keepright")) {

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
        Node node = itemUnconnectedBean.getValue().get_coordinates();
        LatLon latLon = new LatLon(node.getCoor().lat(), node.getCoor().lon());
        bounds = new Bounds(latLon.toBBox(Config.bounds).toRectangle());
        TofixDraw.draw_Node(tofixLayer, latLon);
        Download.Download(downloadOsmTask, bounds, itemUnconnectedBean.getValue().getNode_id());
        return accessToTask;
    }

}
