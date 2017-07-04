package org.openstreetmap.josm.plugins.tofix.util;

import java.io.IOException;
import java.net.URL;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.tofix.controller.StatusController;
import org.openstreetmap.josm.tools.HttpClient;

/**
 *
 * @author ruben
 */
public class Status {

    static String host = Config.getHOST();

    public static boolean server() {
      
        System.out.println("Este es el host en status: "+host);
        return "a ok".equals(new StatusController(host).getStatusBean().getStatus());
    }

    public static boolean isInternetReachable() {
        try {
            HttpClient.create(new URL(Config.URL_OSM)).connect().disconnect();
            return true;
        } catch (IOException e) {
            Main.error(e, "Couldn't connect to the osm server. Please check your internet connection.");
            return false;
        }
    }
}
