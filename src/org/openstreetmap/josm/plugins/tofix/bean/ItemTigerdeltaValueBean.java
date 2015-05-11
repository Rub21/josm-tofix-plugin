package org.openstreetmap.josm.plugins.tofix.bean;

/**
 *
 * @author ruben
 */
public class ItemTigerdeltaValueBean {

    private String name;
    private Long way;
    private String st_astext;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getWay() {
        return way;
    }

    public void setWay(Long way) {
        this.way = way;
    }

    public String getSt_astext() {
        return st_astext;
    }

    public void setSt_astext(String st_astext) {
        this.st_astext = st_astext;
    }
    
}
