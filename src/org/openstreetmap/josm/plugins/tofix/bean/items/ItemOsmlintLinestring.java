package org.openstreetmap.josm.plugins.tofix.bean.items;

import java.util.LinkedList;
import java.util.List;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;

/**
 *
 * @author samely
 */
public class ItemOsmlintLinestring extends AbstractItemOsmlint {

    public List<List<Node>> get_nodes() {
        String geostring = getGeom();
        geostring = geostring.replace("MULTILINESTRING(", "").replace("))", ")").replace(", ", ",");
        geostring = geostring.replace("LINESTRING(", "(");

        List<List<Node>> list = new LinkedList<>();
        String[] array;
        if (geostring.contains("), (")) {
            geostring = geostring.replace(")", "").replace("(", "");
            array = geostring.split(",\\(");
            for (int i = 0; i < array.length; i++) {
                List<Node> l = new LinkedList<>();
                String[] a = array[i].split(",");
                for (int j = 0; j < a.length; j++) {
                    LatLon latLon = new LatLon(Double.parseDouble(a[j].split(" ")[1]), Double.parseDouble(a[j].split(" ")[0]));
                    Node node = new Node(latLon);
                    l.add(node);
                }
                list.add(l);
            }
        } else {
            geostring = geostring.replace(")", "").replace("(", "");
            array = geostring.split(",");
            List<Node> l = new LinkedList<>();
            for (int i = 0; i < array.length; i++) {
                LatLon latLon = new LatLon(Double.parseDouble(array[i].split(" ")[1]), Double.parseDouble(array[i].split(" ")[0]));
                Node node = new Node(latLon);
                l.add(node);
            }
            list.add(l);
        }
        return list;
    }
}
