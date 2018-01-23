package org.openstreetmap.josm.plugins.tofix.controller;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.plugins.tofix.bean.AccessToProject;
import org.openstreetmap.josm.plugins.tofix.bean.ResponseBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.Item;
import org.openstreetmap.josm.plugins.tofix.util.Request;
import org.openstreetmap.josm.plugins.tofix.util.Util;
import static org.openstreetmap.josm.tools.I18n.tr;

/**
 *
 * @author ruben
 */
public class ItemController {

    Item item = new Item();
    ResponseBean responseBean = new ResponseBean();
    JsonArray relation;
    AccessToProject accessToProject;

    public AccessToProject getAccessToProject() {
        return accessToProject;
    }

    public void setAccessToProject(AccessToProject accessToProject) {
        this.accessToProject = accessToProject;
    }

    public JsonArray getRelation() {
        return relation;
    }

    public void setRelation(JsonArray relation) {
        this.relation = relation;
    }

    public Item getItem() {
        try {
            //Get item as String
            String itemString = Request.sendGET(accessToProject.getProject_url());

            Util.print(itemString);
            JsonReader reader = Json.createReader(new StringReader(itemString));
            JsonArray arrayItems = reader.readArray();
            //Check if the array hays items
            if (arrayItems.size() > 0) {
                JsonObject itemObject = arrayItems.getJsonObject(0);
                Util.print(itemObject);
                Util.print(itemObject.getString("id"));
                Util.print(itemObject.getString("project_id"));
                Util.print(itemObject.getJsonObject("featureCollection"));
                item.setId(itemObject.getString("id"));
                item.setProject_id(itemObject.getString("project_id"));
                item.setFeatureCollection(itemObject.getJsonObject("featureCollection"));
                Util.print(item.getFeatureCollection());
                item.setStatusServer(200);

            } else {
                new Notification(tr("There are not items in this project")).show();
                item.setStatusServer(410);
            }
            reader.close();
        } catch (Exception ex) {
            item.setStatusServer(502);
            Util.alert(ex);
            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return item;
    }
}
