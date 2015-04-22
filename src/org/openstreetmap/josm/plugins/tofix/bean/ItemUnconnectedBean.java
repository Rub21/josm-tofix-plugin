package org.openstreetmap.josm.plugins.tofix.bean;

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

    public void sumary() {
        System.err.println("Key :" + getKey());
        System.err.println("X :" + getValue().getX());
        System.err.println("Y :" + getValue().getY());
        System.err.println("weight :" + getValue().getWeight());
        System.err.println("problem_id :" + getValue().getProblem_id());
        System.err.println("node_id :" + getValue().getNode_id());
        System.err.println("way_id :" + getValue().getWay_id());
        System.err.println("highwaykey :" + getValue().getHighwaykey());
        System.err.println("distance :" + getValue().getDistance());
        System.err.println("iso_a2 :" + getValue().getIso_a2());
    }
}
