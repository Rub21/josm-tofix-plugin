package org.openstreetmap.josm.plugins.tofix.bean.items;

/**
 *
 * @author samely
 */
public abstract class AbstractItemGeom extends ItemTask {

    private String geometry;
    private String coordinates;

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geom) {
        this.geometry = geom;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
