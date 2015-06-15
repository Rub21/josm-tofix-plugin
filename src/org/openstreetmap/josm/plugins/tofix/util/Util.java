package org.openstreetmap.josm.plugins.tofix.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
//import java.awt.geom;

/**
 *
 * @author ruben
 */
public class Util {

    public static void print(Object object) {
        System.err.println(object);
    }

    public static LatLon format_St_astext_Keepright(String st_astext) {
        String str = st_astext.replaceAll("[^-?0-9.]+", " ");
        Double lat = Double.parseDouble(Arrays.asList(str.trim().split(" ")).get(1));
        Double lon = Double.parseDouble(Arrays.asList(str.trim().split(" ")).get(0));
        LatLon coor = new LatLon(lat, lon);
        Util.print(coor);
        return coor;
    }

    public static Long format_Elems_Nycbuildings(String elems) {
        String[] arr = elems.replace("way", "").split("_");

        Util.print(arr);
        return Long.valueOf(arr[0]);
    }

    public static List format_st_astext_Tigerdelta(String st_astext) {
        // String[] list = st_astext.replaceAll("[^-?0-9.]+", " ").split(" ");
        st_astext = st_astext.replace("MULTILINESTRING(", "").replace("))", ")");
        Double[][] cordinates;

        List<List<Node>> list = new LinkedList<List<Node>>();
        String[] array;
        if (st_astext.contains("),(")) {
            st_astext = st_astext.replace(")", "").replace("(", "");
            array = st_astext.split(",\\(");
            for (int i = 0; i < array.length; i++) {
                List<Node> l = new LinkedList<Node>();
                String[] a = array[i].split(",");
                for (int j = 0; j < a.length; j++) {
                    LatLon latLon = new LatLon(Double.parseDouble(a[j].split(" ")[1]), Double.parseDouble(a[j].split(" ")[0]));
                    Node node = new Node(latLon);
                    l.add(node);
                }
                list.add(l);
            }
        } else {
            st_astext = st_astext.replace(")", "").replace("(", "");
            array = st_astext.split(",");
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
    
//      public static List geom_Krakatoa(String geom) {
//      };
}
