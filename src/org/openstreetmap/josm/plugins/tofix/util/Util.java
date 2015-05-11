package org.openstreetmap.josm.plugins.tofix.util;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.openstreetmap.josm.data.coor.CachedLatLon;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.tools.Geometry;

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

//        for (List<Double[]> l : list) {
//            for (Double[] s : l) {
//                Util.print(s[0] + " , " + s[1]);
//            }
//        }

//:"MULTILINESTRING((-95.434784 30.066343,-95.434784 30.066349),(-95.434814 30.066545,-95.434845 30.066711),(-95.43486 30.06678,-95.434906 30.067045,-95.434952 30.067226))"}}
//"MULTILINESTRING((-83.312073 35.471603,-83.3125 35.471741,-83.312843 35.471855))"}}
//        Double lat = Double.parseDouble(list[2]);
//        Double lon = Double.parseDouble(list[1]);
        // LatLon coor = new LatLon(4, 6);
        // Util.print(coor);
        return list;

    }
}
