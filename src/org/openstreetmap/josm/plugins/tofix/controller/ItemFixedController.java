package org.openstreetmap.josm.plugins.tofix.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.josm.plugins.tofix.bean.FixedBean;
import org.openstreetmap.josm.plugins.tofix.util.Request;

/**
 *
 * @author ruben
 */
//public class ItemFixedController {
//
//    private String url;
//    private FixedBean itemFixedBean;
//    public ItemFixedController(String url, FixedBean itemFixedBean) {
//        this.url = url;
//        this.itemFixedBean = itemFixedBean;
//    }
//
//    public void sendTrackBean() {
//        Gson gson = new Gson();
//        String string_obj = gson.toJson(itemFixedBean).toString();
//        try {
//            Request.sendPOST_Json(url, string_obj);
//
//        } catch (IOException ex) {
//            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//}
