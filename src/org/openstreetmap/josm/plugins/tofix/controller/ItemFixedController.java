package org.openstreetmap.josm.plugins.tofix.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.josm.plugins.tofix.bean.ItemFixedBean;
import org.openstreetmap.josm.plugins.tofix.bean.TrackBean;
import org.openstreetmap.josm.plugins.tofix.util.Request;
import org.openstreetmap.josm.plugins.tofix.util.Util;

/**
 *
 * @author ruben
 */
public class ItemFixedController {

    private String url;
    private ItemFixedBean itemFixedBean;
    public ItemFixedController(String url, ItemFixedBean itemFixedBean) {
        this.url = url;
        this.itemFixedBean = itemFixedBean;
    }

    public void sendTrackBean() {
        Gson gson = new Gson();
        String string_obj = gson.toJson(itemFixedBean).toString();
        try {
            Request.sendPOST_Json(url, string_obj);

        } catch (IOException ex) {
            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
