package org.openstreetmap.josm.plugins.tofix.bean;

/**
 *
 * @author ruben
 */
public class TaskBean {

    private String idtask;
    private boolean isCompleted;
    private boolean isAllItemsLoad;
    private String name;
    private String description;
    private String updated;
    private String changesetComment;
    private String date;
    private int edit;
    private int fixed;
    private int skip;
    private int items;
    private int noterror;

    private String geometry;

    public String getIdtask() {
        return idtask;
    }

    public void setIdtask(String idtask) {
        this.idtask = idtask;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getChangesetComment() {
        return changesetComment;
    }

    public void setChangesetComment(String changesetComment) {
        this.changesetComment = changesetComment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getEdit() {
        return edit;
    }

    public void setEdit(int edit) {
        this.edit = edit;
    }

    public int getFixed() {
        return fixed;
    }

    public void setFixed(int fixed) {
        this.fixed = fixed;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public int getNoterror() {
        return noterror;
    }

    public void setNoterror(int noterror) {
        this.noterror = noterror;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public boolean isIsAllItemsLoad() {
        return isAllItemsLoad;
    }

    public void setIsAllItemsLoad(boolean isAllItemsLoad) {
        this.isAllItemsLoad = isAllItemsLoad;
    }
}
