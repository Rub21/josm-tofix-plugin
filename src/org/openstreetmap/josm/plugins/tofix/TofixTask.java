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

    //Main clases 
    Item item = new Item();
    ItemController itemController = new ItemController();

    private final double size_bounds = Config.bounds;
    Bounds bounds = null;
    DownloadOsmTask downloadOsmTask = new DownloadOsmTask();

    TofixLayer tofixLayer = new TofixLayer("Tofix-layer");

    public void fetch_item_unconnected(AccessToTask accessTaskBean) {
        itemController.setUrl(accessTaskBean.getTask_url());
        Item item = itemController.getItemUnconnectedBean();
        ItemUnconnectedBean itemUnconnectedBean = item.getItemUnconnectedBean();

        switch (item.getStatus()) {
            case 200:
                accessTaskBean.setAccess(true);
                accessTaskBean.setKey(itemUnconnectedBean.getKey());
                accessTaskBean.setOsm_obj_id(itemUnconnectedBean.getValue().getNode_id());
                Node node = itemUnconnectedBean.getValue().get_coordinates();
                // itemUnconnectedBean.
                LatLon latLon = new LatLon(node.getCoor().lat(), node.getCoor().lon());
                bounds = new Bounds(latLon.toBBox(size_bounds).toRectangle());
                TofixDraw.draw_Node(tofixLayer, latLon);//draw in layer
                Download.Download(downloadOsmTask, bounds, accessTaskBean.getOsm_obj_id());//Download data
                break;

            case 410:
                accessTaskBean.setAccess(false);
                JOptionPane.showMessageDialog(Main.panel, "Task  completo");
                break;
            default:
                accessTaskBean.setAccess(false);
                JOptionPane.showMessageDialog(Main.panel, "Somethig when wrong in server");
        }

    }

}
