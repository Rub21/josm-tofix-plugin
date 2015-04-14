/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openstreetmap.josm.plugins.tofix.controller;

import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.josm.plugins.tofix.bean.ListTasksBean;
import org.openstreetmap.josm.plugins.tofix.util.Request;

/**
 *
 * @author ruben
 */
public class ListTasksController {

    Gson gson = new Gson();

    ListTasksBean listTasksBean = null;
    private final String url;

    public ListTasksController(String url) {
        this.url = url;
    }

    public ListTasksBean getListTasksBean() {
        String stringListTasksBean = null;
        try {
            stringListTasksBean = Request.sendGET(url);
        } catch (Exception ex) {
            Logger.getLogger(StatusController.class.getName()).log(Level.SEVERE, null, ex);
        }
        listTasksBean = gson.fromJson(stringListTasksBean, ListTasksBean.class);
   
        return listTasksBean;

    }

}
