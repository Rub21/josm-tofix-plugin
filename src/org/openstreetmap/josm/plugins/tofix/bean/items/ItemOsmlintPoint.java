package org.openstreetmap.josm.plugins.tofix.bean.items;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;

/**
 *
 * @author samely
 */
public class ItemOsmlintPoint extends AbstractItemOsmlint {

    public Node get_node(){        
        String geoString = getGeom();
        geoString = geoString.replace("POINT(", "").replace(")", "");
        String[] array = geoString.split(" ");
        LatLon latLon = new LatLon(Double.parseDouble(array[1]), Double.parseDouble(array[0]));
        return new Node(latLon);
    }
}
