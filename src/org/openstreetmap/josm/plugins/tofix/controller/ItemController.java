package org.openstreetmap.josm.plugins.tofix.controller;

import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.josm.plugins.tofix.bean.AccessToTask;
import org.openstreetmap.josm.plugins.tofix.bean.ResponseBean;
import org.openstreetmap.josm.plugins.tofix.bean.TaskCompleteBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.Item;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemKeeprightBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemKrakatoaBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemNycbuildingsBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemSmallcomponents;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemStrava;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemTigerdeltaBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemUnconnectedBean;
import org.openstreetmap.josm.plugins.tofix.util.Request;

/**
 *
 * @author ruben
 */
public class ItemController {

    Gson gson = new Gson();
    Item item = new Item();
    ResponseBean responseBean = new ResponseBean();

    AccessToTask accessToTask;

    public AccessToTask getAccessToTask() {
        return accessToTask;
    }

    public void setAccessToTask(AccessToTask accessToTask) {
        this.accessToTask = accessToTask;
    }

    public Item getItem() {

        try {
            responseBean = Request.sendPOST(accessToTask.getTask_url());
            item.setStatus(responseBean.getStatus());

            switch (responseBean.getStatus()) {
                case 200:
                    if (accessToTask.getTask_source().equals("unconnected")) {
                        item.setItemUnconnectedBean(gson.fromJson(responseBean.getValue(), ItemUnconnectedBean.class));
                    }
                    if (accessToTask.getTask_source().equals("keepright")) {
                        item.setItemKeeprightBean(gson.fromJson(responseBean.getValue(), ItemKeeprightBean.class));
                    }
                    if (accessToTask.getTask_source().equals("tigerdelta")) {
                        item.setItemTigerdeltaBean(gson.fromJson(responseBean.getValue(), ItemTigerdeltaBean.class));
                    }
                    if (accessToTask.getTask_source().equals("nycbuildings")) {
                        item.setItemNycbuildingsBean(gson.fromJson(responseBean.getValue(), ItemNycbuildingsBean.class));
                    }
                    if (accessToTask.getTask_source().equals("krakatoa")) {
                        item.setItemKrakatoaBean(gson.fromJson(responseBean.getValue(), ItemKrakatoaBean.class));
                    }
                    if (accessToTask.getTask_source().equals("strava")) {
                        item.setItemStrava(gson.fromJson(responseBean.getValue(), ItemStrava.class));
                    }
                    if (accessToTask.getTask_source().equals("components")) {
                        item.setItemSmallcomponents(gson.fromJson(responseBean.getValue(), ItemSmallcomponents.class));
                    }
                    break;
                case 410:
                    item.setTaskCompleteBean(gson.fromJson(responseBean.getValue().replace("\\", "").replace("\"{", "{").replace("}\"", "}"), TaskCompleteBean.class));
                    break;
                case 503:
                    //Servidor en mantenimiento
                    break;
            }

        } catch (Exception ex) {
            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return item;
    }
}
