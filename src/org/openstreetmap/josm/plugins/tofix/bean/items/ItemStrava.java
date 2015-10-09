package org.openstreetmap.josm.plugins.tofix.bean.items;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;

public class ItemStrava {
    private String key;
       private Value value  = new Value();

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

        public Node get_node() {
            String geoString = getGeom();
            geoString = geoString.replace("POINT(", "").replace(")", "");
            String[] array = geoString.split(" ");
            LatLon latLon = new LatLon(Double.parseDouble(array[1]), Double.parseDouble(array[0]));
            Node node = new Node(latLon);
            return node;
        }

    }
}
