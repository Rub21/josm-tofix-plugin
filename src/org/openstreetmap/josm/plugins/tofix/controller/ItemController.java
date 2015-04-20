package org.openstreetmap.josm.plugins.tofix.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.josm.plugins.tofix.bean.ItemBean;
import org.openstreetmap.josm.plugins.tofix.util.Request;

/**
 *
 * @author ruben
 */
public class ItemController {

    private String url;
    private ItemBean itemBean;

    public ItemController(String url) {
        this.url = url;
    }

    public ItemBean getItemBean() {
        Gson gson = new Gson();
        String stringItemBean = null;
        try {
            stringItemBean = Request.sendPOST(url);
            itemBean = gson.fromJson(stringItemBean, ItemBean.class);
            itemBean.sumary();
        } catch (IOException ex) {
            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itemBean;
    }
}
