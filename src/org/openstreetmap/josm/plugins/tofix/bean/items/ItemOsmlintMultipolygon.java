package org.openstreetmap.josm.plugins.tofix.bean.items;

import java.util.LinkedList;
import java.util.List;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;

/**
 *
 * @author samely
 */
public class ItemOsmlintMultipolygon extends AbstractItemOsmlint {

    List<Node> bound = new LinkedList<>();

    public List<List<List<List<Node>>>> get_nodes() {
        String geostring = getCoordinates();
        List<List<List<List<Node>>>> list = new LinkedList<>();
        List<List<List<Node>>> lll = new LinkedList<>();
        List<List<Node>> ll = new LinkedList<>();
        List<Node> l = new LinkedList<>();
        geostring = geostring.replace("[[[[", "").replace("]]]]", "");
        String[] multiarray = geostring.replace("[[[", "").split("]]]");

        for (int k = 0; k < multiarray.length; k++) {
            String[] array = null;
            if (multiarray[k].contains("[[")) {
                array = multiarray[k].replace("[[", "").split("]],");
            } else {
                array[0] = multiarray[k];
            }
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
            lll.add(ll);
        }
        boundSize(bound);
        list.add(lll);
        return list;
    }
}
