/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openstreetmap.josm.plugins.tofix.bean.items;

import org.openstreetmap.josm.plugins.tofix.bean.TaskCompleteBean;

/**
 *
 * @author ruben
 */
public class Item {

    private int status;
    private ItemKeeprightBean itemKeeprightBean;
    private ItemKrakatoaBean itemKrakatoaBean;
    private ItemNycbuildingsBean itemNycbuildingsBean;
    private ItemTigerdeltaBean itemTigerdeltaBean;
    private ItemUnconnectedBean itemUnconnectedBean;
    private TaskCompleteBean taskCompleteBean;
    

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public ItemNycbuildingsBean getItemNycbuildingsBean() {
        return itemNycbuildingsBean;
    }

    public void setItemNycbuildingsBean(ItemNycbuildingsBean itemNycbuildingsBean) {
        this.itemNycbuildingsBean = itemNycbuildingsBean;
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

    public TaskCompleteBean getTaskCompleteBean() {
        return taskCompleteBean;
    }

    public void setTaskCompleteBean(TaskCompleteBean taskCompleteBean) {
        this.taskCompleteBean = taskCompleteBean;
    }
}
