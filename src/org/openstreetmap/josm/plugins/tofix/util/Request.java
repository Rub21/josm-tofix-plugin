package org.openstreetmap.josm.plugins.tofix.util;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.openstreetmap.josm.tools.HttpClient;
import org.openstreetmap.josm.tools.HttpClient.Response;

/**
 *
 * @author ruben
 */
public class Request {
    
    public static void sendPOST_Json(String url, String object) throws IOException {
        Util.print("sendPOST_Json => :" + url + "->" + object);
        HttpClient.create(new URL(url), "POST")
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", Config.TOKEN)
                .setAccept("application/json")
                .setRequestBody(object.getBytes(StandardCharsets.UTF_8))
                .connect().disconnect();
    }

    public static void sendPUT_Json(String url, String object) throws IOException {
        Util.print("sendPUT_Json => :" + url + "->" + object);
        HttpClient.create(new URL(url), "PUT")
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", Config.TOKEN)
                .setAccept("application/json")
                .setRequestBody(object.getBytes(StandardCharsets.UTF_8))
                .connect().disconnect();
    }

    public static String sendGET(String url) throws IOException {
        Util.print("sendGET => :" + url);
        Response response = HttpClient.create(new URL(url))
                .setHeader("Authorization", Config.TOKEN)
                .connect();
        String result = response.fetchContent();
        response.disconnect();
        return result;
    }
}
