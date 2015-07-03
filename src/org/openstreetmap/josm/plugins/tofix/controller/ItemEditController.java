package org.openstreetmap.josm.plugins.tofix.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.josm.plugins.tofix.bean.TrackBean;
import org.openstreetmap.josm.plugins.tofix.util.Request;

/**
 *
 * @author ruben
 */
//public class ItemEditController {
//
//    private String url;
//    private TrackBean trackBean;
//
//    public ItemEditController(String url, TrackBean trackBean) {
//        this.url = url;
//        this.trackBean = trackBean;
//    }
//
//    public void sendTrackBean() {
//        Gson gson = new Gson();
//        String string_obj = gson.toJson(trackBean).toString();
//        try {
//            Request.sendPOST_Json(url, string_obj);
//        } catch (IOException ex) {
//            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
//}
