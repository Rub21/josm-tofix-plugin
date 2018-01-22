package org.openstreetmap.josm.plugins.tofix.util;

import java.io.IOException;
import java.net.URL;

import org.openstreetmap.josm.plugins.tofix.controller.StatusController;
import org.openstreetmap.josm.tools.HttpClient;
import org.openstreetmap.josm.tools.Logging;

/**
 *
 * @author ruben
 */
public class Status {

    static String host = Config.getHOST();

    public static boolean server() {
        return "OK".equals(new StatusController(host).getStatusBean().getStatus());
    }

    public static boolean isInternetReachable() {
        try {
            HttpClient.create(new URL(Config.URL_OSM)).connect().disconnect();
            return true;
        } catch (IOException e) {
            Logging.log(Logging.LEVEL_ERROR, "Couldn't connect to the osm server. Please check your internet connection.", e);
            return false;
        }
    }
}
