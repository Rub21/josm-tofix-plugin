package org.openstreetmap.josm.plugins.tofix.bean.items;

/**
 *
 * @author samely
 */
public abstract class AbstractItemOsmlint extends AbstractItemGeom {

    private Long way;

    public Long getWay() {
        return way;
    }

    public void setWay(Long way) {
        this.way = way;
    }
}
