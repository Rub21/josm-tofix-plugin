/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openstreetmap.josm.plugins.tofix.bean.items;

import java.util.LinkedList;
import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;

/**
 *
 * @author samely
 */
public class ItemOsmlintMultipoint extends ItemTask {

    private Long way;
    private String geom;

    public Long getWay() {
        return way;
    }

    public void setWay(Long way) {
        this.way = way;
    }

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
