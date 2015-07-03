package org.openstreetmap.josm.plugins.tofix.bean;

/**
 *
 * @author ruben
 */
public class ItemKeeprightBean {

    private String key;
    private ItemKeeprightValueBean value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ItemKeeprightValueBean getValue() {
        return value;
    }

    public void setValue(ItemKeeprightValueBean value) {
        this.value = value;
    }
    
}
