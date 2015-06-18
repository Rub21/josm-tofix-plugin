package org.openstreetmap.josm.plugins.tofix.util;

import java.util.Arrays;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.data.coor.LatLon;
//import java.awt.geom;

/**
 *
 * @author ruben
 */
public class Util {

    public static void alert(Object object) {
        JOptionPane.showMessageDialog(null, object);
    }

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

    public static void error_request_data() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
