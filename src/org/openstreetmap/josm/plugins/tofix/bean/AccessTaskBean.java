package org.openstreetmap.josm.plugins.tofix.bean;

/**
 *
 * @author ruben
 */
public class AccessTaskBean {

    private String host = "http://54.147.184.23:8000";
    // private String task_url; 
    private String task;
    private String task_source;
    private boolean access;
    // private String track_url;
    //private String fixed_url;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTask_url() {
        return getHost() + "/task/" + getTask();
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTask_source() {
        return task_source;
    }

    public void setTask_source(String task_source) {
        this.task_source = task_source;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public String getTrack_url() {
        return getHost() + "/track/" + getTask();
    }

    public String getFixed_url() {
        return getHost() + "/fixed/" + getTask();
    }

}
