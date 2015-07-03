package org.openstreetmap.josm.plugins.tofix.bean;

import org.openstreetmap.josm.plugins.tofix.util.Config;

/**
 * 
 * @author ruben
 */
public class AccessToTask {

    private String host = Config.host;
    private String task;
    private String task_source;
    private boolean access;
    private Long osm_obj_id;
    private String key;

    public AccessToTask(String task, String task_source, boolean access) {
        this.task = task;
        this.task_source = task_source;
        this.access = access;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTask_url() {
        String url = this.getHost() + "task/" + this.getTask();
        return url;
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
        return getHost() + "track/" + getTask();
    }

    public String getFixed_url() {
        return getHost() + "fixed/" + getTask();
    }

    public Long getOsm_obj_id() {
        return osm_obj_id;
    }

    public void setOsm_obj_id(Long osm_obj_id) {
        this.osm_obj_id = osm_obj_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    

}
