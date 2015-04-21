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
    private String user;
    private ItemBean itemBean;

    public ItemController(String url,String user) {
        this.url = url;
        this.user=user;
    }

    public ItemBean getItemBean() {
        Gson gson = new Gson();
        String stringItemBean = null;
        try {
            stringItemBean = Request.sendPOST_skip(url, user);
            itemBean = gson.fromJson(stringItemBean, ItemBean.class);
            //itemBean.sumary();
        } catch (IOException ex) {
            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itemBean;
    }
}
