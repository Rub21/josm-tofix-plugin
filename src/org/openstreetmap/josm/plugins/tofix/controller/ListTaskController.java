package org.openstreetmap.josm.plugins.tofix.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import org.openstreetmap.josm.plugins.tofix.bean.ListTaskBean;
import org.openstreetmap.josm.plugins.tofix.bean.TaskBean;
import org.openstreetmap.josm.plugins.tofix.util.Config;
import org.openstreetmap.josm.plugins.tofix.util.Request;

/**
 *
 * @author ruben
 */
public class ListTaskController {

    private ListTaskBean listTasksBean = new ListTaskBean();
    private String url;

    public ListTaskController() {
        this.url = Config.HOST + "tasks";
    }

    public ListTaskBean getListTasksBean() {
        List<TaskBean> tasks = new LinkedList<>();
        String stringListTaskBean = null;
        try {
            stringListTaskBean = Request.sendGET(url);
            JsonReader jsonReader = Json.createReader(new StringReader(stringListTaskBean));
            JsonObject jsonObject = jsonReader.readObject();
            JsonArray jsonArray = jsonObject.getJsonArray("tasks");
            for (JsonValue value : jsonArray) {
                TaskBean taskBean = new TaskBean();
                JsonObject jsontask = Json.createReader(new StringReader(value.toString())).readObject();
                taskBean.setId(jsontask.getString("id"));
                taskBean.setTitle(jsontask.getString("title"));
                taskBean.setSource(jsontask.getString("source"));
                taskBean.setStatus(jsontask.getBoolean("status"));
                tasks.add(taskBean);
            }
            jsonReader.close();
        } catch (IOException ex) {
            Logger.getLogger(ListTaskController.class.getName()).log(Level.SEVERE, null, ex);
        }
        listTasksBean.setTasks(tasks);
        return listTasksBean;
    }
}
