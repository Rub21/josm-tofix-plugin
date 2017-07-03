package org.openstreetmap.josm.plugins.tofix.bean;

import org.openstreetmap.josm.plugins.tofix.util.Config;

/**
 *
 * @author ruben
 */
public class AccessToTask {

    private String host = Config.getHOST();

    private String task_idtask;
    private boolean task_isCompleted;
    private boolean task_isAllItemsLoad;
    private String task_iduser;
    private String task_name;
    private String task_description;
    private String task_updated;
    private String task_changesetComment;
    private String task_date;
    private int task_edit;
    private int task_fixed;
    private int task_skip;
    private String task_type;
    private int task_items;
    private int task_noterror;

    private boolean access;
    private Long osm_obj_id;
    private String key;

    public AccessToTask(String task_id, boolean access) {
        this.task_idtask = task_id;
        this.access = access;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTask_url() {
        String url = this.getHost() + "/tasks/" + this.getTask_idtask()+"/"+this.getTask_type()+"/items";
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

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_idtask() {
        return task_idtask;
    }

    public void setTask_idtask(String task_idtask) {
        this.task_idtask = task_idtask;
    }

    public boolean getTask_isCompleted() {
        return task_isCompleted;
    }

    public void setTask_isCompleted(boolean task_isCompleted) {
        this.task_isCompleted = task_isCompleted;
    }

    public String getTask_description() {
        return task_description;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public String getTask_updated() {
        return task_updated;
    }

    public void setTask_updated(String task_updated) {
        this.task_updated = task_updated;
    }

    public String getTask_changesetComment() {
        return task_changesetComment;
    }

    public void setTask_changesetComment(String task_changesetComment) {
        this.task_changesetComment = task_changesetComment;
    }

    public String getTask_date() {
        return task_date;
    }

    public void setTask_date(String task_date) {
        this.task_date = task_date;
    }

    public int getTask_edit() {
        return task_edit;
    }

    public void setTask_edit(int task_edit) {
        this.task_edit = task_edit;
    }

    public int getTask_fixed() {
        return task_fixed;
    }

    public void setTask_fixed(int task_fixed) {
        this.task_fixed = task_fixed;
    }

    public int getTask_skip() {
        return task_skip;
    }

    public void setTask_skip(int task_skip) {
        this.task_skip = task_skip;
    }

    public int getTask_items() {
        return task_items;
    }

    public void setTask_items(int task_items) {
        this.task_items = task_items;
    }

    public int getTask_noterror() {
        return task_noterror;
    }

    public void setTask_noterror(int task_noterror) {
        this.task_noterror = task_noterror;
    }

    public boolean isTask_isAllItemsLoad() {
        return task_isAllItemsLoad;
    }

    public void setTask_isAllItemsLoad(boolean task_isAllItemsLoad) {
        this.task_isAllItemsLoad = task_isAllItemsLoad;
    }

    public String getTask_iduser() {
        return task_iduser;
    }

    public void setTask_iduser(String task_iduser) {
        this.task_iduser = task_iduser;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }
}
