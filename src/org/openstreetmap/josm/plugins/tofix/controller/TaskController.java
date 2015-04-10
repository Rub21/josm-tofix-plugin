package org.openstreetmap.josm.plugins.tofix.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.josm.plugins.tofix.bean.StatusBean;
import org.openstreetmap.josm.plugins.tofix.bean.TaskBean;
import org.openstreetmap.josm.plugins.tofix.util.PostRequest;

/**
 *
 * @author ruben
 */
public class TaskController {

    String url;
    TaskBean taskBean;

    public TaskController(String url) {
        this.url = url;
    }

    public TaskBean getTaskBean() {
        String stringtaskBean = null;
        try {
            stringtaskBean = PostRequest.sendPOST(url);
        } catch (IOException ex) {
            Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.err.println(stringtaskBean);

        Gson gson = new Gson();
        taskBean = gson.fromJson(stringtaskBean, TaskBean.class);
        taskBean.sumary();
        return taskBean;
    }

    public void setTaskBean(TaskBean taskBean) {
        this.taskBean = taskBean;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
