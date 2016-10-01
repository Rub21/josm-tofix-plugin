package org.openstreetmap.josm.plugins.tofix.bean.items;

/**
 *
 * @author samely
 */
public abstract class AbstractItemOsmlint extends ItemTask {

    private Long way;
    private String geom;

    public Long getWay() {
        return way;
    }

    public void setWay(Long way) {
        this.way = way;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }
}
