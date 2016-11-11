package org.openstreetmap.josm.plugins.tofix.bean.items;

import java.util.LinkedList;
import java.util.List;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;

/**
 *
 * @author samely
 */
public class ItemOsmlintMultipoint extends AbstractItemOsmlint {

    public List<Node> get_nodes() {
        String geoString = getCoordinates();
        List<Node> list = new LinkedList<>();
        String multipoint = geoString.substring(0, 10);
        if (multipoint.equalsIgnoreCase("MULTIPOINT")) {
            geoString = geoString.replace("[", "").replace("]", "");
            String[] arr = geoString.split(",");
            for (int i = 0; i < arr.length; i++) {
                LatLon latLon = new LatLon(Double.parseDouble(arr[i].split(",")[1]), Double.parseDouble(arr[i].split(",")[0]));
                Node node = new Node(latLon);
                list.add(node);
            }
        }
        multipoint = geoString.substring(0, 5);
        if (multipoint.equalsIgnoreCase("POINT")) {
            geoString = geoString.replace("[", "").replace("]", "");
            String[] arr = geoString.split(",");
            for (int i = 0; i < arr.length; i++) {
                LatLon latLon = new LatLon(Double.parseDouble(arr[i].split(",")[1]), Double.parseDouble(arr[i].split(",")[0]));
                Node node = new Node(latLon);
                list.add(node);
            }
        }
        return list;
    }
}
