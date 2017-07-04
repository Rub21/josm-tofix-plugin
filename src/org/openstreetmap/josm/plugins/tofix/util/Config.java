package org.openstreetmap.josm.plugins.tofix.util;

public class Config {

    private static String HOST;
    public static final String DEFAULT_HOST = "https://build-to-fix-production.mapbox.com";
    public static final String URL_TOFIX = "http://osmlab.github.io/to-fix/";
    public static final String URL_OSM = "http://www.openstreetmap.org";
    public static final String URL_TOFIX_ISSUES = "https://github.com/JOSM/tofix/issues";

    public static String getHOST() {
        if (HOST == null || HOST.isEmpty()) {
            HOST = DEFAULT_HOST;
        }
        return HOST;
    }

    public static void setHOST(String aHOST) {
        HOST = aHOST;
    }

}
