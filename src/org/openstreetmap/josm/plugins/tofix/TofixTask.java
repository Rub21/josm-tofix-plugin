package org.openstreetmap.josm.plugins.tofix;

import java.text.DecimalFormat;
import java.util.List;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.downloadtasks.DownloadOsmTask;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.gui.io.UploadDialog;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.plugins.tofix.bean.AccessToTask;
import org.openstreetmap.josm.plugins.tofix.bean.items.Item;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemKeeprightBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemKrakatoaBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemOsmlintLinestring;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemOsmlintMultipoint;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemOsmlintPoint;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemSmallcomponents;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemStrava;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemTigerdeltaBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemUnconnectedBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemUsaBuildingsBean;
import org.openstreetmap.josm.plugins.tofix.controller.ItemController;
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
    MapView mv = null;
    TofixLayer tofixLayer = new TofixLayer("Tofix-layer");

    public AccessToTask work(Item item, AccessToTask accessToTask, double size) { //size to download       
        if (accessToTask.getTask_source().equals("unconnected")) {
            accessToTask = work_unconnected(item.getItemUnconnectedBean(), accessToTask, size);
        }
        if (accessToTask.getTask_source().equals("keepright")) {
            accessToTask = work_keepright(item.getItemKeeprightBean(), accessToTask, size);
        }
        if (accessToTask.getTask_source().equals("tigerdelta")) {
            accessToTask = work_tigerdelta(item.getItemTigerdeltaBean(), accessToTask, size);
        }
        if (accessToTask.getTask_source().equals("nycbuildings")) {
            accessToTask = work_nycbuildings(item.getItemUsabuildingsBean(), accessToTask, size);
        }
        if (accessToTask.getTask_source().equals("krakatoa")) {
            accessToTask = work_krakatoa(item.getItemKrakatoaBean(), accessToTask, size);
        }
        if (accessToTask.getTask_source().equals("strava")) {
            accessToTask = work_strava(item.getItemStrava(), accessToTask, size);
        }
        if (accessToTask.getTask_source().equals("components")) {
            accessToTask = work_smallcomponents(item.getItemSmallcomponents(), accessToTask, size);
        }
        if (accessToTask.getTask_source().equals("osmlint-point")) {
            accessToTask = work_osmlintpoint(item.getItemOsmlintPoint(), accessToTask, size);
        }
        if (accessToTask.getTask_source().equals("osmlint-linestring")) {
            accessToTask = work_osmlintlinestring(item.getItemOsmlintLinestring(), accessToTask, size);
        }
        if (accessToTask.getTask_source().equals("osmlint-multipoint")) {
            accessToTask = work_osmlintmultipoint(item.getItemOsmlintMultipoint(), accessToTask, size);
        }

        UploadDialog.getUploadDialog().getChangeset().getCommentsCount();
        return accessToTask;
    }

    public void deleteLayer() {
        while (Main.main.hasEditLayer()) {
                Main.main.removeLayer(Main.main.getEditLayer());
        }
//         for(OsmDataLayer la : Main.map.mapView.getLayersOfType(OsmDataLayer.class)){           
//            Main.map.mapView.removeLayer(la);
//        }
    }

    private AccessToTask work_unconnected(ItemUnconnectedBean itemUnconnectedBean, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemUnconnectedBean.getKey());
        node = itemUnconnectedBean.get_node();
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        checkTofixLayer();
        TofixDraw.draw_Node(tofixLayer, node.getCoor());
        Download.Download(downloadOsmTask, bounds, itemUnconnectedBean.getWay_id());
        return accessToTask;
    }

    private AccessToTask work_keepright(ItemKeeprightBean itemKeeprightBean, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemKeeprightBean.getKey());
        node = itemKeeprightBean.get_node();
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        checkTofixLayer();
        TofixDraw.draw_Node(tofixLayer, node.getCoor());
        Download.Download(downloadOsmTask, bounds, itemKeeprightBean.getObject_id());
        return accessToTask;

    }

    private AccessToTask work_nycbuildings(ItemUsaBuildingsBean itemNycbuildingsBean, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemNycbuildingsBean.getKey());
        node = itemNycbuildingsBean.get_node();
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        checkTofixLayer();
        TofixDraw.draw_Node(tofixLayer, node.getCoor());
        Download.Download(downloadOsmTask, bounds, itemNycbuildingsBean.osm_obj_id());
        return accessToTask;
    }

    private AccessToTask work_tigerdelta(ItemTigerdeltaBean itemTigerdeltaBean, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemTigerdeltaBean.getKey());
        List<List<Node>> list = itemTigerdeltaBean.get_nodes();
        node = new Node(new LatLon(list.get(0).get(0).getCoor().lat(), list.get(0).get(0).getCoor().lon()));
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        checkTofixLayer();
        TofixDraw.draw_line(tofixLayer, node.getCoor(), list);
        Download.Download(downloadOsmTask, bounds, 0x0L);//0x0L = null porque no exixte el id del objeto
        return accessToTask;
    }

    private AccessToTask work_krakatoa(ItemKrakatoaBean itemkrakatoaBean, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemkrakatoaBean.getKey());
        List<Node> list = itemkrakatoaBean.get_nodes();
        node = new Node(new LatLon(list.get(0).getCoor().lat(), list.get(0).getCoor().lon()));
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        checkTofixLayer();
        TofixDraw.draw_nodes(tofixLayer, node.getCoor(), list);
        Download.Download(downloadOsmTask, bounds, 0x0L);//0x0L = null porque no exixte el id del objeto
        return accessToTask;
    }

    private AccessToTask work_strava(ItemStrava itemStrava, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemStrava.getKey());
        node = itemStrava.get_node();
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        checkTofixLayer();
        TofixDraw.draw_Node(tofixLayer, node.getCoor());
        Download.Download(downloadOsmTask, bounds, 0x0L);
        return accessToTask;
    }

    private AccessToTask work_smallcomponents(ItemSmallcomponents itemSmallcomponents, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemSmallcomponents.getKey());
        List<List<Node>> list = itemSmallcomponents.get_nodes();
        node = new Node(new LatLon(list.get(0).get(0).getCoor().lat(), list.get(0).get(0).getCoor().lon()));
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        checkTofixLayer();
        TofixDraw.draw_line(tofixLayer, node.getCoor(), list);
        Download.Download(downloadOsmTask, bounds, 0x0L);//0x0L = null porque no exixte el id del objeto
        return accessToTask;
    }

    private AccessToTask work_osmlintpoint(ItemOsmlintPoint itemOsmlintPoint, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemOsmlintPoint.getKey());
        node = itemOsmlintPoint.get_node();
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        checkTofixLayer();
        TofixDraw.draw_Node(tofixLayer, node.getCoor());
        Download.Download(downloadOsmTask, bounds, itemOsmlintPoint.getWay());
        return accessToTask;

    }

    private AccessToTask work_osmlintlinestring(ItemOsmlintLinestring itemOsmlintLinestring, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemOsmlintLinestring.getKey());
        List<List<Node>> list = itemOsmlintLinestring.get_nodes();
        node = new Node(new LatLon(list.get(0).get(0).getCoor().lat(), list.get(0).get(0).getCoor().lon()));
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        checkTofixLayer();
        TofixDraw.draw_line(tofixLayer, node.getCoor(), list);
        Download.Download(downloadOsmTask, bounds, itemOsmlintLinestring.getWay());
        return accessToTask;
    }

    private AccessToTask work_osmlintmultipoint(ItemOsmlintMultipoint itemOsmlintMultipoint, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemOsmlintMultipoint.getKey());
        List<Node> list = itemOsmlintMultipoint.get_nodes();
        node = new Node(new LatLon(list.get(0).getCoor().lat(), list.get(0).getCoor().lon()));
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        checkTofixLayer();
        TofixDraw.draw_nodes(tofixLayer, node.getCoor(), list);
        Download.Download(downloadOsmTask, bounds, itemOsmlintMultipoint.getWay());
        return accessToTask;
    }

    public void task_complete(Item item, AccessToTask accessToTask) {
        DecimalFormat myFormatter = new DecimalFormat("#,###");
        String num = myFormatter.format(item.getTaskCompleteBean().getTotal());
        String message = "Task " + accessToTask.getTask_name() + " is complete\n"
                + num + " issues fixed";
        new Notification(tr(message)).show();
    }

    public final void checkTofixLayer() {
        if (!Main.map.mapView.hasLayer(tofixLayer)) {
            Main.main.addLayer(tofixLayer);
        }
    }   

}
