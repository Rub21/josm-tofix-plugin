package org.openstreetmap.josm.plugins.tofix.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.openstreetmap.josm.plugins.tofix.bean.StatusBean;
import org.openstreetmap.josm.plugins.tofix.util.Request;

/**
 *
 * @author ruben
 */
public class StatusController {

    private final String url;

    public StatusController(String url) {
        this.url = url;
    }

    public StatusBean getStatusBean() {
        StatusBean statusBean = new StatusBean();
        String stringStatusBean = null;
        try {
            stringStatusBean = Request.sendGET(url);
        } catch (IOException ex) {
            Logger.getLogger(StatusController.class.getName()).log(Level.SEVERE, null, ex);
        }
        JsonReader jsonReader = Json.createReader(new StringReader(stringStatusBean));
        JsonObject jsonObject = jsonReader.readObject();
        statusBean.setStatus(jsonObject.getString("status"));
        jsonReader.close();
        return statusBean;
    }
}
