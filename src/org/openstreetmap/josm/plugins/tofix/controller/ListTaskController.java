package org.openstreetmap.josm.plugins.tofix.controller;

import java.io.IOException;
import java.io.StringReader;
import java.text.Collator;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonParsingException;

import org.openstreetmap.josm.plugins.tofix.bean.ListProjectBean;
import org.openstreetmap.josm.plugins.tofix.bean.ProjectBean;
import org.openstreetmap.josm.plugins.tofix.util.Config;
import org.openstreetmap.josm.plugins.tofix.util.Request;

/**
 *
 * @author ruben
 */
public class ListTaskController {

    private final ListProjectBean listTasksBean = new ListProjectBean();
    private final String url;

    /**
     * Constructs a new {@code ListTaskController}.
     */
    public ListTaskController() {
        this.url = Config.getHOST() + "/tasks";
    }

    public ListProjectBean getListTasksBean() {
        List<ProjectBean> tasks = new LinkedList<>();
        try (JsonReader jsonReader = Json.createReader(new StringReader(Request.sendGET(url)))) {
            JsonObject jsonObject = jsonReader.readObject();
            JsonArray jsonArray = jsonObject.getJsonArray("tasks");
//            for (JsonValue value : jsonArray) {
//                ProjectBean taskBean = new ProjectBean();
//                try (JsonReader jsonReader2 = Json.createReader(new StringReader(value.toString()))) {
//                    JsonObject jsontask = jsonReader2.readObject();
//                    JsonObject value_jsontask = (JsonObject) jsontask.get("value");
//                    JsonObject stats_jsontask = (JsonObject) value_jsontask.get("stats");
//                    if (value_jsontask != null) {
//                        taskBean.setIdtask(jsontask.getString("idtask"));
//                        taskBean.setIsCompleted(jsontask.getBoolean("isCompleted"));
//                        taskBean.setIsAllItemsLoad(jsontask.getBoolean("isAllItemsLoad"));
//                        taskBean.setIduser(jsontask.getString("iduser"));
//                        taskBean.setName(value_jsontask.getString("name"));
//                        taskBean.setDescription(value_jsontask.getString("description"));
//                        taskBean.setUpdated(value_jsontask.getJsonNumber("updated").toString());
//                        taskBean.setChangesetComment(value_jsontask.getString("changesetComment"));
//
//                        if (stats_jsontask != null) {
//                            taskBean.setDate(stats_jsontask.getJsonNumber("date").toString());
//                            taskBean.setEdit(Integer.parseInt(stats_jsontask.getJsonNumber("edit").toString()));
//                            taskBean.setFixed(Integer.parseInt(stats_jsontask.getJsonNumber("fixed").toString()));
//                            taskBean.setSkip(Integer.parseInt(stats_jsontask.getJsonNumber("skip").toString()));
//                            taskBean.setType(stats_jsontask.getString("type"));
//                            taskBean.setItems(Integer.parseInt(stats_jsontask.getJsonNumber("items").toString()));
//                            taskBean.setNoterror(Integer.parseInt(stats_jsontask.getJsonNumber("noterror").toString()));
//                        }
//
//                    }
//                }
//                tasks.add(taskBean);
//            }
        } catch (IOException | NullPointerException | JsonParsingException ex) {
            Logger.getLogger(ListTaskController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Collections.sort(tasks, (o1, o2) -> Collator.getInstance().compare(o1.getName(), o2.getName()));
        listTasksBean.setProject(tasks);
        return listTasksBean;
    }
}
