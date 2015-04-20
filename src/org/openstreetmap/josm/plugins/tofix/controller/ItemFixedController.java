package org.openstreetmap.josm.plugins.tofix.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.josm.plugins.tofix.bean.ItemBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemFixedBean;
import org.openstreetmap.josm.plugins.tofix.util.Request;

/**
 *
 * @author ruben
 */
public class ItemFixedController {
        private String url;


    public ItemFixedController(String url) {
        this.url = url;
    }

    public void fixed(ItemFixedBean itemFixedBean) {
        Gson gson = new Gson();
        String stringItemBean = null;
        try {
             Request.sendPOSTA(url, itemFixedBean);

        } catch (IOException ex) {
            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
}
