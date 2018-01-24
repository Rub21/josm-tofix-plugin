package org.openstreetmap.josm.plugins.tofix.controller;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.Main;
import static org.openstreetmap.josm.gui.mappaint.mapcss.ExpressionFactory.Functions.tr;
import org.openstreetmap.josm.plugins.tofix.bean.AccessToProject;
import org.openstreetmap.josm.plugins.tofix.bean.ItemBean;
import org.openstreetmap.josm.plugins.tofix.util.Request;
import org.openstreetmap.josm.plugins.tofix.util.Util;

/**
 *
 * @author ruben
 */
public class ItemController {

    ItemBean item = new ItemBean();
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

    public ItemBean getItem() {
        try {
            //Get item as String
            String itemString = Request.sendGET(accessToProject.getProject_url());
            Util.print(itemString);
            JsonReader reader = Json.createReader(new StringReader(itemString));
            JsonArray arrayItems = reader.readArray();
            //Check if the array hays items
            if (arrayItems.size() > 0) {
                JsonObject itemObject = arrayItems.getJsonObject(0);
                item.setId(itemObject.getString("id"));
                item.setProject_id(itemObject.getString("project_id"));
                item.setFeatureCollection(itemObject.getJsonObject("featureCollection"));
                item.setStatusServer(200);

            } else {
                JOptionPane.showMessageDialog(Main.parent, tr("There are no more items to work on this project!"), tr("Warning"), JOptionPane.WARNING_MESSAGE);
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
