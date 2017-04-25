package org.openstreetmap.josm.plugins.tofix.bean.items;

import java.util.LinkedList;
import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;

/**
 *
 * @author samely
 */
public class ItemOsmlintPoint extends AbstractItemOsmlint {

    List<Node> bound = new LinkedList<>();

    public Node get_node() {
        String geoString = getCoordinates();
        geoString = geoString.replace("[", "").replace("]", "");
        String[] array = geoString.split(",");
        LatLon latLon = new LatLon(Double.parseDouble(array[1]), Double.parseDouble(array[0]));
        bound.add(new Node(latLon));
        boundSize(bound);
        return new Node(latLon);
    }
}
