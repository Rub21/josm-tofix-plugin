package org.openstreetmap.josm.plugins.tofix.controller;

import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.josm.plugins.tofix.bean.StatusBean;
import org.openstreetmap.josm.plugins.tofix.util.Util;
/**
 *
 * @author ruben
 */
public class StatusController {

    private StatusBean statusBean;
    private String url;
   // private Util util;

    public StatusController(StatusBean statusBean, String url) {
        this.statusBean = statusBean;
        //this.url = url;
        this.url = "http://54.147.184.23:8000/status";

    }

    public void setStatusBean(StatusBean statusBean) {
        this.statusBean = statusBean;

    }

    public StatusBean getStatusBean() {
        String json = null;
        try {
            json = Util.readUrl(this.getUrl());
        } catch (Exception ex) {
            Logger.getLogger(StatusController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Gson gson = new Gson();
        statusBean = gson.fromJson(json, StatusBean.class);
        System.out.println(statusBean.getStatus());
        return statusBean;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
