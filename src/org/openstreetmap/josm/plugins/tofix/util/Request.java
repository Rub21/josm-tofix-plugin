package org.openstreetmap.josm.plugins.tofix.util;

import java.nio.charset.StandardCharsets;
import java.net.*;
import java.io.IOException;

import org.openstreetmap.josm.plugins.tofix.bean.ResponseBean;
import org.openstreetmap.josm.tools.HttpClient;
import org.openstreetmap.josm.tools.HttpClient.Response;

/**
 *
 * @author ruben
 */
public class Request {

    public static ResponseBean sendPOST(String url) throws IOException {
        Response resp = HttpClient.create(new URL(url), "POST").setRequestBody(url.getBytes()).connect();
        //Crear un ResponseBean para que regrese el String y el status de la peticion.
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus(resp.getResponseCode());//agregar el estatus
        responseBean.setValue(resp.fetchContent());//agrega el valor de la respuesta
        resp.disconnect();
        return responseBean;
    }

    public static void sendPOST_Json(String url, String object) throws IOException {
        HttpClient.create(new URL(url), "POST")
                .setHeader("Content-Type", "application/json")
                .setAccept("application/json")
                .setRequestBody(object.getBytes(StandardCharsets.UTF_8))
                .connect().disconnect();
    }

    public static String sendGET(String url) throws IOException {
        Response response = HttpClient.create(new URL(url)).connect();
        String result = response.fetchContent();
        response.disconnect();

        return result;
    }
}
