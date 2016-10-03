package org.openstreetmap.josm.plugins.tofix.bean.items;

/**
 *
 * @author samely
 */
public abstract class AbstractItemGeom extends ItemTask {

    private String geom;

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }
}
