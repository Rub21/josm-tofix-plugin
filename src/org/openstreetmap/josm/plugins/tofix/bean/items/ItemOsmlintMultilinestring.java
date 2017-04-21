package org.openstreetmap.josm.plugins.tofix.bean.items;

import java.util.LinkedList;
import java.util.List;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;

public class ItemOsmlintMultilinestring extends AbstractItemOsmlint {

    List<Node> bound = new LinkedList<>();

    public List<List<List<Node>>> get_nodes() {
        String geostring = getCoordinates();
        List<List<List<Node>>> list = new LinkedList<>();
        geostring = geostring.replace("[[[", "").replace("]]]", "");
        String[] array = geostring.replace("[[", "").split("]],");
        List<List<Node>> ll = new LinkedList<>();
        List<Node> l = new LinkedList<>();

        for (int i = 0; i < array.length; i++) {
            String[] subarray = array[i].replace("[", "").split("],");
            for (int j = 0; j < subarray.length; j++) {
                LatLon latLon = new LatLon(Double.parseDouble(subarray[j].split(",")[1]), Double.parseDouble(subarray[j].split(",")[0]));
                Node node = new Node(latLon);
                l.add(node);
            }
            bound.addAll(l);
            ll.add(l);
        }
        boundSize(bound);
        list.add(ll);

        return list;
    }
}
