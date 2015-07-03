package org.openstreetmap.josm.plugins.tofix.bean.items;

/**
 *
 * @author ruben
 */
public class ItemUnconnectedBean {
    private String key;
    private ItemUnconnectedValueBean value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ItemUnconnectedValueBean getValue() {
        return value;
    }

    public void setValue(ItemUnconnectedValueBean value) {
        this.value = value;
    }
}
