package org.openstreetmap.josm.plugins.tofix.bean.items;

import java.util.LinkedList;
import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.plugins.tofix.util.Util;

/**
 *
 * @author ruben
 */
public class ItemTigerdeltaBean {

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

        private String name;
        private Long way;
        private String geom;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getWay() {
            return way;
        }

        public void setWay(Long way) {
            this.way = way;
        }

        public String getSt_astext() {
            return geom;
        }

        public void setSt_astext(String st_astext) {
            this.geom = st_astext;
        }

        public List<List<Node>> get_nodes() {
            String geostring = getSt_astext();
            geostring = geostring.replace("MULTILINESTRING (", "").replace("))", ")").replace(", ", ",");
            geostring = geostring.replace("LINESTRING (", "(");
            Util.print(geostring);
            Double[][] cordinates;

            List<List<Node>> list = new LinkedList<List<Node>>();
            String[] array;
            if (geostring.contains("), (")) {
                geostring = geostring.replace(")", "").replace("(", "");
                array = geostring.split(",\\(");
                for (int i = 0; i < array.length; i++) {
                    List<Node> l = new LinkedList<Node>();
                    String[] a = array[i].split(",");
                    Util.print(a);
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
                List<Node> l = new LinkedList<Node>();
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

}
