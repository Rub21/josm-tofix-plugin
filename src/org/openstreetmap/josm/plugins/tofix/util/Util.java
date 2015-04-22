package org.openstreetmap.josm.plugins.tofix.util;

import java.util.Arrays;
import org.openstreetmap.josm.data.coor.LatLon;

/**
 *
 * @author ruben
 */
public class Util {

    public static void print(Object object) {
        System.err.println(object);
    }

    public static LatLon format_St_astext_Keepright(String St_astext) {
        String str = St_astext.replaceAll("[^-?0-9.]+", " ");
        Double lat = Double.parseDouble(Arrays.asList(str.trim().split(" ")).get(1));
        Double lon = Double.parseDouble(Arrays.asList(str.trim().split(" ")).get(0));
        LatLon coor = new LatLon(lat, lon);
        Util.print(coor);
        return coor;
    }

}
