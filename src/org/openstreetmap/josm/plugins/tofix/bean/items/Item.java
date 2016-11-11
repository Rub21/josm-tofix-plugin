package org.openstreetmap.josm.plugins.tofix.bean.items;

import org.openstreetmap.josm.plugins.tofix.bean.TaskCompleteBean;

/**
 *
 * @author ruben
 */
public class Item {

    private int status;
    private String type;
    private ItemKeeprightBean itemKeeprightBean;
    private ItemKrakatoaBean itemKrakatoaBean;
    private ItemUsaBuildingsBean itemUsabuildingsBean;
    private ItemTigerdeltaBean itemTigerdeltaBean;
    private ItemUnconnectedBean itemUnconnectedBean;
    private TaskCompleteBean taskCompleteBean;
    private ItemStrava itemStrava;
    private ItemSmallcomponents itemSmallcomponents;
    private ItemOsmlintPoint itemOsmlintPoint;
    private ItemOsmlintMultipoint itemOsmlintMultipoint;
    private ItemOsmlintLinestring itemOsmlintLinestring;

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

    public ItemKeeprightBean getItemKeeprightBean() {
        return itemKeeprightBean;
    }

    public void setItemKeeprightBean(ItemKeeprightBean itemKeeprightBean) {
        this.itemKeeprightBean = itemKeeprightBean;
    }

    public ItemKrakatoaBean getItemKrakatoaBean() {
        return itemKrakatoaBean;
    }

    public void setItemKrakatoaBean(ItemKrakatoaBean itemKrakatoaBean) {
        this.itemKrakatoaBean = itemKrakatoaBean;
    }

    public ItemUsaBuildingsBean getItemUsabuildingsBean() {
        return itemUsabuildingsBean;
    }

    public void setItemUsabuildingsBean(ItemUsaBuildingsBean itemNycbuildingsBean) {
        this.itemUsabuildingsBean = itemNycbuildingsBean;
    }

    public ItemTigerdeltaBean getItemTigerdeltaBean() {
        return itemTigerdeltaBean;
    }

    public void setItemTigerdeltaBean(ItemTigerdeltaBean itemTigerdeltaBean) {
        this.itemTigerdeltaBean = itemTigerdeltaBean;
    }

    public ItemUnconnectedBean getItemUnconnectedBean() {
        return itemUnconnectedBean;
    }

    public void setItemUnconnectedBean(ItemUnconnectedBean itemUnconnectedBean) {
        this.itemUnconnectedBean = itemUnconnectedBean;
    }

    public ItemStrava getItemStrava() {
        return itemStrava;
    }

    public void setItemStrava(ItemStrava itemStrava) {
        this.itemStrava = itemStrava;
    }

    public ItemSmallcomponents getItemSmallcomponents() {
        return itemSmallcomponents;
    }

    public void setItemSmallcomponents(ItemSmallcomponents itemSmallcomponents) {
        this.itemSmallcomponents = itemSmallcomponents;
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

}
