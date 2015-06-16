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
        return coor;
    }

    public static Long format_Elems_Nycbuildings(String elems) {
        String[] arr = elems.replace("way", "").split("_");
        return Long.valueOf(arr[0]);
    }

}
