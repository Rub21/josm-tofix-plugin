package org.openstreetmap.josm.plugins.tofix;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.text.DecimalFormat;
import java.util.List;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.gui.io.UploadDialog;
import org.openstreetmap.josm.plugins.tofix.bean.AccessToTask;
import org.openstreetmap.josm.plugins.tofix.bean.items.Item;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemOsmlintLinestring;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemOsmlintMultilinestring;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemOsmlintMultipoint;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemOsmlintMultipolygon;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemOsmlintPoint;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemOsmlintPolygon;
import org.openstreetmap.josm.plugins.tofix.controller.ItemController;
import org.openstreetmap.josm.plugins.tofix.util.Download;

/**
 *
 * @author ruben
 */
public class TofixTask {

    ItemController itemController = new ItemController();
    Bounds bounds = null;

    Node node = null;
    MapView mv = null;
    TofixLayer tofixLayer = new TofixLayer("Tofix-layer");

    public AccessToTask work(Item item, AccessToTask accessToTask, double size, JsonArray relation) { //size to download    
        if ("Point".equals(item.getType())) {
            accessToTask = work_osmlintpoint(item.getItemOsmlintPoint(), accessToTask, size);
        }
        if ("LineString".equals(item.getType())) {
            accessToTask = work_osmlintlinestring(item.getItemOsmlintLinestring(), accessToTask, size);
        }
        if ("MultiPoint".equals(item.getType())) {
            accessToTask = work_osmlintmultipoint(item.getItemOsmlintMultipoint(), accessToTask, size);
        }
        if ("MultiLineString".equals(item.getType())) {
            accessToTask = work_osmlintmultilinestring(item.getItemOsmlintMultilinestring(), accessToTask, size);
        }
        if ("Polygon".equals(item.getType())) {
            accessToTask = work_osmlintpolygon(item.getItemOsmlintPolygon(), accessToTask, size, relation);

        }
        if ("MultiPolygon".equals(item.getType())) {
            accessToTask = work_osmlintmultipolygon(item.getItemOsmlintMultipolygon(), accessToTask, size);
        }
        UploadDialog.getUploadDialog().getChangeset().getCommentsCount();
        return accessToTask;
    }

    private AccessToTask work_osmlintpoint(ItemOsmlintPoint itemOsmlintPoint, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemOsmlintPoint.getKey());
        node = itemOsmlintPoint.get_node();
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        checkTofixLayer();
        TofixDraw.draw_Node(tofixLayer, node.getCoor());
        Download.download(bounds, itemOsmlintPoint.getWay());
        return accessToTask;
    }

    private AccessToTask work_osmlintlinestring(ItemOsmlintLinestring itemOsmlintLinestring, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemOsmlintLinestring.getKey());
        List<List<Node>> list = itemOsmlintLinestring.get_nodes();
        node = new Node(new LatLon(list.get(0).get(0).getCoor().lat(), list.get(0).get(0).getCoor().lon()));
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        checkTofixLayer();
        TofixDraw.draw_line(tofixLayer, node.getCoor(), list);

        Download.download(bounds, itemOsmlintLinestring.getWay());
        return accessToTask;
    }

    private AccessToTask work_osmlintmultipoint(ItemOsmlintMultipoint itemOsmlintMultipoint, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemOsmlintMultipoint.getKey());
        List<Node> list = itemOsmlintMultipoint.get_nodes();
        node = new Node(new LatLon(list.get(0).getCoor().lat(), list.get(0).getCoor().lon()));
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        checkTofixLayer();
        TofixDraw.draw_nodes(tofixLayer, node.getCoor(), list);
        Download.download(bounds, itemOsmlintMultipoint.getWay());
        return accessToTask;
    }

    private AccessToTask work_osmlintmultilinestring(ItemOsmlintMultilinestring itemOsmlintMultilinestring, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemOsmlintMultilinestring.getKey());
        List<List<List<Node>>> list = itemOsmlintMultilinestring.get_nodes();
        node = new Node(new LatLon(list.get(0).get(0).get(0).getCoor().lat(), list.get(0).get(0).get(0).getCoor().lon()));
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        checkTofixLayer();
        TofixDraw.draw_lines(tofixLayer, node.getCoor(), list);
        Download.download(bounds, itemOsmlintMultilinestring.getWay());
        return accessToTask;
    }

    private AccessToTask work_osmlintpolygon(ItemOsmlintPolygon itemOsmlintPolygon, AccessToTask accessToTask, double size, JsonArray relation) {
        accessToTask.setKey(itemOsmlintPolygon.getKey());
        List<List<List<Node>>> list = itemOsmlintPolygon.get_nodes();
        node = new Node(new LatLon(list.get(0).get(0).get(0).getCoor().lat(), list.get(0).get(0).get(0).getCoor().lon()));

        if (relation != null && relation.size() > 0) {
            for (int i = 0; i < relation.size(); i++) {
                String type = relation.getJsonObject(i).getJsonObject("geometry").get("type").toString();
                JsonObject jo = relation.getJsonObject(i);
                Node node_rel = new Node();
                if (type.contains("Point")) {
                    ItemOsmlintPoint point = new ItemOsmlintPoint();
                    point.setGeometry(type);
                    point.setCoordinates(jo.getJsonObject("geometry").get("coordinates").toString());
                    node_rel = point.get_node();
                    tofixLayer.add_Node(node_rel.getCoor());
                    bounds = new Bounds(node_rel.getCoor().toBBox(size).toRectangle());
                }
                if (type.contains("LineString")) {
                    ItemOsmlintLinestring linestring = new ItemOsmlintLinestring();
                    linestring.setGeometry(type);
                    linestring.setCoordinates(jo.getJsonObject("geometry").get("coordinates").toString());
                    List<List<Node>> list_rel = linestring.get_nodes();
                    node_rel = new Node(new LatLon(list_rel.get(0).get(0).getCoor().lat(), list_rel.get(0).get(0).getCoor().lon()));
                    tofixLayer.add_Line(list_rel);
                    bounds = new Bounds(node_rel.getCoor().toBBox(size).toRectangle());
                }
                if (type.contains("MultiPoint")) {
                    ItemOsmlintMultipoint multipoint = new ItemOsmlintMultipoint();
                    multipoint.setGeometry(type);
                    multipoint.setCoordinates(jo.getJsonObject("geometry").get("coordinates").toString());
                    List<Node> list_rel = multipoint.get_nodes();
                    node_rel = new Node(new LatLon(list_rel.get(0).getCoor().lat(), list_rel.get(0).getCoor().lon()));
                    tofixLayer.add_Nodes(list_rel);
                }
                if (type.contains("MultiLineString")) {
                    ItemOsmlintMultilinestring multilinestring = new ItemOsmlintMultilinestring();
                    multilinestring.setGeometry(type);
                    multilinestring.setCoordinates(jo.getJsonObject("geometry").get("coordinates").toString());
                    List<List<List<Node>>> list_rel = multilinestring.get_nodes();
                    node_rel = new Node(new LatLon(list_rel.get(0).get(0).get(0).getCoor().lat(), list_rel.get(0).get(0).get(0).getCoor().lon()));
                    tofixLayer.add_lines(list_rel);
                }
                if (type.contains("Polygon")) {
                    ItemOsmlintPolygon polygon = new ItemOsmlintPolygon();
                    polygon.setGeometry(type);
                    polygon.setCoordinates(jo.getJsonObject("geometry").get("coordinates").toString());
                    List<List<List<Node>>> list_rel = polygon.get_nodes();
                    node_rel = new Node(new LatLon(list_rel.get(0).get(0).get(0).getCoor().lat(), list_rel.get(0).get(0).get(0).getCoor().lon()));
                    tofixLayer.add_lines(list_rel);
                }
                if (type.contains("MultiPolygon")) {
                    ItemOsmlintMultipolygon multipolygon = new ItemOsmlintMultipolygon();
                    multipolygon.setGeometry(type);
                    multipolygon.setCoordinates(jo.getJsonObject("geometry").get("coordinates").toString());
                    List<List<List<List<Node>>>> list_rel = multipolygon.get_nodes();
                    node_rel = new Node(new LatLon(list_rel.get(0).get(0).get(0).get(0).getCoor().lat(), list_rel.get(0).get(0).get(0).get(0).getCoor().lon()));
                    tofixLayer.add_Lines(list_rel);
                }
            }
        }
        if (bounds == null) {
            bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        }
        checkTofixLayer();
        TofixDraw.draw_lines(tofixLayer, node.getCoor(), list);
        Download.download(bounds, itemOsmlintPolygon.getWay());
        return accessToTask;
    }

    private AccessToTask work_osmlintmultipolygon(ItemOsmlintMultipolygon itemOsmlintMultipolygon, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemOsmlintMultipolygon.getKey());
        List<List<List<List<Node>>>> list = itemOsmlintMultipolygon.get_nodes();
        node = new Node(new LatLon(list.get(0).get(0).get(0).get(0).getCoor().lat(), list.get(0).get(0).get(0).get(0).getCoor().lon()));
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        checkTofixLayer();
        TofixDraw.draw_Lines(tofixLayer, node.getCoor(), list);
        Download.download(bounds, itemOsmlintMultipolygon.getWay());
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
        if (!Main.getLayerManager().containsLayer(tofixLayer)) {
            Main.getLayerManager().addLayer(tofixLayer);
        }
    }
}
