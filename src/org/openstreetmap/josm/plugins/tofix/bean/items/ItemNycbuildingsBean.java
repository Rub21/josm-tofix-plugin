/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openstreetmap.josm.plugins.tofix.bean.items;

/**
 *
 * @author ruben
 */
public class ItemNycbuildingsBean {
   private  String key;
   private  ItemNycbuildingsValueBean value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ItemNycbuildingsValueBean getValue() {
        return value;
    }

    public void setValue(ItemNycbuildingsValueBean value) {
        this.value = value;
    }
   
}

