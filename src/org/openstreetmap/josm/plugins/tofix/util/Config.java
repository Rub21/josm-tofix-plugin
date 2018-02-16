package org.openstreetmap.josm.plugins.tofix.util;

public class Config {

    private static String HOST;
    public static final String DEFAULT_HOST = "http://192.168.1.49:3000";
    public static final String API_VERSION = "v1";
    public static final String URL_TOFIX = "http://osmlab.github.io/to-fix/";
    public static final String URL_OSM = "http://www.openstreetmap.org";
    public static final String URL_TOFIX_ISSUES = "https://github.com/JOSM/tofix/issues";
    public static final String DEFAULT_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjUxMDgzNiIsInVzZXJuYW1lIjoiUnViMjEiLCJpbWFnZSI6Imh0dHBzOi8vd3d3Lm9wZW5zdHJlZXRtYXAub3JnL2F0dGFjaG1lbnRzL3VzZXJzL2ltYWdlcy8wMDAvNTEwLzgzNi9vcmlnaW5hbC9hNjI3NzkxZTFiZDJmZjk0ZWM1YjdjNDA4NjBmNTdiMy5qcGcifQ.OS3O191_WqGGTGo6w2GUEUTWoCaRncqcpEO6lk3t_kM";
    public static String TOKEN = "nome";
    public static String QUERY;
    public static final String DEFAULT_QUERY = "?status=open&lock=unlocked&page_size=1&fc=true&random=true";
    public static String BBOX = "none";

    public static String getQUERY() {
        if (BBOX.equals("none")) {
            QUERY = DEFAULT_QUERY;
        } else {
            QUERY = DEFAULT_QUERY + "&bbox=" + BBOX;
        }
        return QUERY;
    }

    public static String getHOST() {
        if (HOST == null || HOST.isEmpty()) {
            HOST = DEFAULT_HOST;
        }
        return HOST;
    }

    public static void setHOST(String aHOST) {
        HOST = aHOST;
    }

    public static void setBBOX(String bbox) {
        BBOX = bbox;
    }

    public static String getAPILogin() {
        return getHOST() + "/" + API_VERSION + "/" + "auth/openstreetmap";
    }

    public static String getTOKEN() {
        if (!TOKEN.equals("none")) {
            TOKEN = DEFAULT_TOKEN;
        }
        Util.print(TOKEN);
        return TOKEN;
    }

    public static void setTOKEN(String token) {
        TOKEN = token;
    }

}
