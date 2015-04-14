package org.openstreetmap.josm.plugins.tofix.bean;

import java.util.List;

/**
 *
 * @author ruben
 */
public class ListTasksBean {

    private List<TasksBean> tasks;

    public List<TasksBean> getTasks() {
        return tasks;
    }

    public void setTasks(List<TasksBean> tasks) {
        this.tasks = tasks;
    }

}
