package org.openstreetmap.josm.plugins.tofix.bean;

import org.openstreetmap.josm.plugins.tofix.util.Config;

/**
 *
 * @author ruben
 */
public class AccessToProject {

    private String host = Config.getHOST();

    private String project_id;
//    private boolean task_isCompleted;
//    private boolean task_isAllItemsLoad;
//    private String task_iduser;
    private String project_name;
//    private String task_description;
//    private String task_updated;
    private String project_changesetComment;
//    private String task_date;
//    private int task_edit;
//    private int task_fixed;
//    private int task_skip;
//    private String task_type;
//    private int task_items;
//    private int task_noterror;

    private boolean access;
    private Long osm_obj_id;
    private String key;

    public AccessToProject(String task_id, boolean access) {
        this.project_id = task_id;
        this.access = access;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProject_url() {
        String url = this.getHost() + "/" + Config.API_VERSION + "/projects/" + this.getProject_id() + "/items" + Config.QUERY;
        return url;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
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

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getProject_changesetComment() {
        return project_changesetComment;
    }

    public void setProject_changesetComment(String project_changesetComment) {
        this.project_changesetComment = project_changesetComment;
    }

}
