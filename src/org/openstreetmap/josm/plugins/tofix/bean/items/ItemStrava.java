package org.openstreetmap.josm.plugins.tofix.bean.items;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;

public class ItemStrava extends AbstractItemGeom {

    public Node get_node() {
        String[] array = getGeom().replace("POINT(", "").replace(")", "").split(" ");
        return new Node(new LatLon(Double.parseDouble(array[1]), Double.parseDouble(array[0])));
    }
}
