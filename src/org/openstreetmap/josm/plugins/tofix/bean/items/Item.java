package org.openstreetmap.josm.plugins.tofix.bean.items;

import org.openstreetmap.josm.plugins.tofix.bean.TaskCompleteBean;

/**
 *
 * @author ruben
 */
public class Item {

    private int status;
    private String type;    
    private TaskCompleteBean taskCompleteBean;   
    private ItemOsmlintPoint itemOsmlintPoint;
    private ItemOsmlintMultipoint itemOsmlintMultipoint;
    private ItemOsmlintLinestring itemOsmlintLinestring;
    private ItemOsmlintMultilinestring itemOsmlintMultilinestring;
    private ItemOsmlintPolygon itemOsmlintPolygon;
    private ItemOsmlintMultipolygon itemOsmlintMultipolygon;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TaskCompleteBean getTaskCompleteBean() {
        return taskCompleteBean;
    }

    public void setTaskCompleteBean(TaskCompleteBean taskCompleteBean) {
        this.taskCompleteBean = taskCompleteBean;
    }
    
    public ItemOsmlintPoint getItemOsmlintPoint() {
        return itemOsmlintPoint;
    }

    public void setItemOsmlintPoint(ItemOsmlintPoint itemOsmlintPoint) {
        this.itemOsmlintPoint = itemOsmlintPoint;
    }

    public ItemOsmlintMultipoint getItemOsmlintMultipoint() {
        return itemOsmlintMultipoint;
    }

    public void setItemOsmlintMultipoint(ItemOsmlintMultipoint itemOsmlintMultipoint) {
        this.itemOsmlintMultipoint = itemOsmlintMultipoint;
    }

    public ItemOsmlintLinestring getItemOsmlintLinestring() {
        return itemOsmlintLinestring;
    }

    public void setItemOsmlintLinestring(ItemOsmlintLinestring itemOsmlintLinestring) {
        this.itemOsmlintLinestring = itemOsmlintLinestring;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ItemOsmlintMultilinestring getItemOsmlintMultilinestring() {
        return itemOsmlintMultilinestring;
    }

    public void setItemOsmlintMultilinestring(ItemOsmlintMultilinestring itemOsmlintMultilinestring) {
        this.itemOsmlintMultilinestring = itemOsmlintMultilinestring;
    }

    public ItemOsmlintPolygon getItemOsmlintPolygon() {
        return itemOsmlintPolygon;
    }

    public void setItemOsmlintPolygon(ItemOsmlintPolygon itemOsmlintPolygon) {
        this.itemOsmlintPolygon = itemOsmlintPolygon;
    }

    public ItemOsmlintMultipolygon getItemOsmlintMultipolygon() {
        return itemOsmlintMultipolygon;
    }

    public void setItemOsmlintMultipolygon(ItemOsmlintMultipolygon itemOsmlintMultipolygon) {
        this.itemOsmlintMultipolygon = itemOsmlintMultipolygon;
    }

}
