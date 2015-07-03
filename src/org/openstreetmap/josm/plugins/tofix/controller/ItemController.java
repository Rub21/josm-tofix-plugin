package org.openstreetmap.josm.plugins.tofix.controller;

import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.josm.plugins.tofix.bean.ResponseBean;
import org.openstreetmap.josm.plugins.tofix.bean.TaskCompleteBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.Item;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemUnconnectedBean;
import org.openstreetmap.josm.plugins.tofix.util.Request;

/**
 *
 * @author ruben
 */
public class ItemController {

    private String url;

    Gson gson = new Gson();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Item getItemUnconnectedBean() {
        ResponseBean responseBean = new ResponseBean();
        Item item = new Item();
        try {
            responseBean = Request.sendPOST(getUrl());
            item.setStatus(responseBean.getStatus());
            switch (responseBean.getStatus()) {
                case 200:
                    item.setItemUnconnectedBean(gson.fromJson(responseBean.getValue(), ItemUnconnectedBean.class));
                    break;
                case 410:
                    item.setTaskCompleteBean(gson.fromJson(responseBean.getValue(), TaskCompleteBean.class));
                    break;
            }

        } catch (Exception ex) {
            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return item;
    }

//    public ItemKeeprightBean getItemKeeprightBean() {
//        ItemKeeprightBean itemKeeprightBean = new ItemKeeprightBean();
//        String stringItem = null;
//        try {
//            stringItem = Request.sendPOST(getUrl());
//            itemKeeprightBean = gson.fromJson(stringItem, ItemKeeprightBean.class);
//            return itemKeeprightBean;
//        } catch (Exception ex) {
//
//            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public ItemNycbuildingsBean getItemNycbuildingsBean() {
//        ItemNycbuildingsBean itemNycbuildingsBean = new ItemNycbuildingsBean();
//        String stringItem = null;
//        try {
//            stringItem = Request.sendPOST(getUrl());
//            itemNycbuildingsBean = gson.fromJson(stringItem, ItemNycbuildingsBean.class);
//
//            return itemNycbuildingsBean;
//        } catch (Exception ex) {
//
//            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public ItemTigerdeltaBean getItemTigerdeltaBean() {
//        ItemTigerdeltaBean itemTigerdeltaBean = new ItemTigerdeltaBean();
//        String stringItem = null;
//        try {
//            stringItem = Request.sendPOST(getUrl());
//            itemTigerdeltaBean = gson.fromJson(stringItem, ItemTigerdeltaBean.class);
//
//            return itemTigerdeltaBean;
//        } catch (Exception ex) {
//
//            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public ResponseBean getItemKrakatoBean() {
//        ResponseBean item = new ResponseBean();
//
//        ItemKrakatoaBean itemKrakatoaBean = new ItemKrakatoaBean();
//
//        ResponseBean responseBean = null;
//
//        try {
//            responseBean = Request.sendPOST(getUrl());
//            gson.fromJson(stringItem, ItemKrakatoaBean.class);
//
//            return itemKrakatoaBean;
//        } catch (Exception ex) {
//
//            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
}
