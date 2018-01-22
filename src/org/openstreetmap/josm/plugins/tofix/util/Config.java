package org.openstreetmap.josm.plugins.tofix.util;

public class Config {

    private static String HOST;
    public static final String DEFAULT_HOST = "http://localhost:3000";
    public static final String URL_TOFIX = "http://osmlab.github.io/to-fix/";
    public static final String URL_OSM = "http://www.openstreetmap.org";
    public static final String URL_TOFIX_ISSUES = "https://github.com/JOSM/tofix/issues";
    public static final String TOKEN ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjUxMDgzNiIsInVzZXJuYW1lIjoiUnViMjEiLCJpbWFnZSI6Imh0dHBzOi8vd3d3Lm9wZW5zdHJlZXRtYXAub3JnL2F0dGFjaG1lbnRzL3VzZXJzL2ltYWdlcy8wMDAvNTEwLzgzNi9vcmlnaW5hbC9hNjI3NzkxZTFiZDJmZjk0ZWM1YjdjNDA4NjBmNTdiMy5qcGcifQ.aSBlGvOEvg1Ru_Kb0UXP6wR1MUjTYWd5zD-dXPY4lsQ";

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
