package org.openstreetmap.josm.plugins.tofix.bean.items;

import java.util.LinkedList;
import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;

/**
 *
 * @author ruben
 */
public class ItemKrakatoaBean {

    private String key;
    private Value value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public class Value {

        private String geom;

        public String getGeom() {
            return geom;
        }

        public void setGeom(String geom) {
            this.geom = geom;
        }

        public List<Node> get_nodes() {
            String geoString = getGeom();
            List<Node> list = new LinkedList<Node>();
            String multipoint = geoString.substring(0, 10);
            if (multipoint.equalsIgnoreCase("MULTIPOINT")) {
                geoString = geoString.replace("MULTIPOINT(", "").replace(")", "");
                String[] arr = geoString.split(",");
                for (int i = 0; i < arr.length; i++) {
                    String[] latlon = arr[i].split(" ");
                    LatLon latLon = new LatLon(Double.parseDouble(arr[i].split(" ")[1]), Double.parseDouble(arr[i].split(" ")[0]));
                    Node node = new Node(latLon);
                    list.add(node);
                }
            }
            multipoint = geoString.substring(0, 5);
            if (multipoint.equalsIgnoreCase("POINT")) {
                geoString = geoString.replace("POINT(", "").replace(")", "");
                String[] arr = geoString.split(",");
                for (int i = 0; i < arr.length; i++) {
                    String[] latlon = arr[i].split(" ");
                    LatLon latLon = new LatLon(Double.parseDouble(arr[i].split(" ")[1]), Double.parseDouble(arr[i].split(" ")[0]));
                    Node node = new Node(latLon);
                    list.add(node);
                }
            }
            return list;
        }
    }

}
