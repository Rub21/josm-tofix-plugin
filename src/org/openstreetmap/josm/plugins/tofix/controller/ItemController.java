package org.openstreetmap.josm.plugins.tofix.controller;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.kitfox.svg.A;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.tofix.bean.AccessToTask;
import org.openstreetmap.josm.plugins.tofix.bean.ResponseBean;
import org.openstreetmap.josm.plugins.tofix.bean.TaskCompleteBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.Item;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemKeeprightBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemKrakatoaBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemUsaBuildingsBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemSmallcomponents;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemStrava;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemTigerdeltaBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemUnconnectedBean;

import org.openstreetmap.josm.plugins.tofix.util.Request;
import org.openstreetmap.josm.plugins.tofix.util.Util;

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
                        //https://github.com/osmlab/to-fix/wiki/Task%20sources#unconnected-minor
                        JsonReader reader = Json.createReader(new StringReader(responseBean.getValue()));
                        JsonObject object = reader.readObject();
                        ItemUnconnectedBean iub = new ItemUnconnectedBean();
                        iub.setKey(object.getString("key"));
                        JsonObject value = object.getJsonObject("value");
                        if (value.containsKey("way_id") && value.containsKey("node_id") && value.containsKey("st_astext")) {
                            iub.setNode_id(Long.parseLong(value.getString("node_id")));
                            iub.setWay_id(Long.parseLong(value.getString("way_id")));
                            iub.setSt_astext(value.getString("st_astext"));
                            item.setItemUnconnectedBean(iub);
                        } else if (value.containsKey("X") && value.containsKey("Y") && value.containsKey("way_id") && value.containsKey("node_id")) {
                            //Format from Arun https://github.com/osmlab/to-fix/wiki/Task%20sources#unconnected-major                           
                            String st_astext = "POINT(" + value.getString("X") + " " + value.getString("Y") + ")";
                            iub.setNode_id(Long.parseLong(value.getString("node_id")));
                            iub.setWay_id(Long.parseLong(value.getString("way_id")));
                            iub.setSt_astext(st_astext);
                            item.setItemUnconnectedBean(iub);
                        } else {
                            item.setStatus(520);// response 520 Unknown Error                            
                        }
                        //TODO
                        //if the structure change , we need to customize this site, to easy resolve , but we need to standardize the source in each task.
                    }
                    if (accessToTask.getTask_source().equals("keepright")) {

                    }
                    if (accessToTask.getTask_source().equals("tigerdelta")) {
                        //https://github.com/osmlab/to-fix/wiki/Task%20sources#tiger-delta
                        JsonReader reader = Json.createReader(new StringReader(responseBean.getValue()));
                        JsonObject object = reader.readObject();
                        ItemTigerdeltaBean itb = new ItemTigerdeltaBean();
                        itb.setKey(object.getString("key"));
                        JsonObject value = object.getJsonObject("value");
                        if (value.containsKey("geom")) {
                            itb.setGeom(value.getString("geom"));
                            item.setItemTigerdeltaBean(itb);
                        } else {
                            item.setStatus(520);
                        }

                    }
                    if (accessToTask.getTask_source().equals("nycbuildings")) {
                        //https://github.com/osmlab/to-fix/wiki/Task%20sources#usa-overlapping-buildings
                        JsonReader reader = Json.createReader(new StringReader(responseBean.getValue()));
                        JsonObject object = reader.readObject();
                        ItemUsaBuildingsBean inb = new ItemUsaBuildingsBean();
                        inb.setKey(object.getString("key"));
                        JsonObject value = object.getJsonObject("value");
                        if (value.containsKey("lat") && value.containsKey("lon") && value.containsKey("elems")) {
                            inb.setLat(Double.parseDouble(value.getString("lat")));
                            inb.setLon(Double.parseDouble(value.getString("lon")));
                            inb.setElems(value.getString("elems"));
                            item.setItemUsabuildingsBean(inb);
                        } else {
                            item.setStatus(520);
                        }
                    }
                    if (accessToTask.getTask_source().equals("krakatoa")) {
                        item.setItemKrakatoaBean(gson.fromJson(responseBean.getValue(), ItemKrakatoaBean.class));
                    }
                    if (accessToTask.getTask_source().equals("strava")) {
                        item.setItemStrava(gson.fromJson(responseBean.getValue(), ItemStrava.class));
                    }
                    if (accessToTask.getTask_source().equals("components")) {
                        //https://github.com/osmlab/to-fix/wiki/Task%20sources#small-components
                        
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
            Util.alert(ex);
            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return item;
    }
}
