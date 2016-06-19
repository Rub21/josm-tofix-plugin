package org.openstreetmap.josm.plugins.tofix.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonReader;

import org.openstreetmap.josm.plugins.tofix.bean.StatusBean;
import org.openstreetmap.josm.plugins.tofix.util.Request;

/**
 *
 * @author ruben
 */
public class StatusController {

    private final String url;

    /**
     * Constructs a new {@code StatusController}.
     */
    public StatusController(String url) {
        this.url = url;
    }

    public StatusBean getStatusBean() {
        StatusBean statusBean = new StatusBean();
        try (JsonReader jsonReader = Json.createReader(new StringReader(Request.sendGET(url)))) {
            statusBean.setStatus(jsonReader.readObject().getString("status"));
        } catch (IOException ex) {
            Logger.getLogger(StatusController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return statusBean;
    }
}
