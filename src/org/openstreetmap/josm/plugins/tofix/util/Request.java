package org.openstreetmap.josm.plugins.tofix.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openstreetmap.josm.data.UserIdentityManager;
import org.openstreetmap.josm.plugins.tofix.bean.ResponseBean;
import org.openstreetmap.josm.tools.HttpClient;
import org.openstreetmap.josm.tools.HttpClient.Response;

/**
 *
 * @author ruben
 */
public class Request {

    public static ResponseBean sendPOST(String url) throws IOException {
        Util.print("sendPOST => :" + url);
        Map<String, String> params = new LinkedHashMap<>();
        params.put("user", UserIdentityManager.getInstance().getUserName());
        params.put("editor", "josm");

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        String urlParameters = postData.toString();

        Response resp = HttpClient.create(new URL(url), "POST")
                .setRequestBody(url.getBytes(StandardCharsets.UTF_8))
                .setRequestBody(urlParameters.getBytes(StandardCharsets.UTF_8))
                .setHeader("Authorization", Config.TOKEN)
                .connect();

        //Crear un ResponseBean para que regrese el String y el status de la peticion.
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus(resp.getResponseCode());//agregar el estatus
        responseBean.setValue(resp.fetchContent());//agrega el valor de la respuesta
        resp.disconnect();
        return responseBean;
    }

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

//    public static void sendPUT_Json(String url, String object) throws IOException {
//        HttpClient.create(new URL(url), "PUT")
//                .setHeader("Content-Type", "application/json")
//                .setAccept("application/json")
//                .setRequestBody(object.getBytes(StandardCharsets.UTF_8))
//                .connect().disconnect();
//    }
}
