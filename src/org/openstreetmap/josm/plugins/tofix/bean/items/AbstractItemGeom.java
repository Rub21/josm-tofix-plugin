package org.openstreetmap.josm.plugins.tofix.bean.items;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.openstreetmap.josm.data.osm.Node;

/**
 *
 * @author samely
 */
public abstract class AbstractItemGeom extends ItemTask {

    private String geometry;
    private String coordinates;
    private Rectangle2D rectangle2D;

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

    public Rectangle2D getRectangle2D() {
        return rectangle2D;
    }

    public void setRectangle2D(Rectangle2D rectangle2D) {
        this.rectangle2D = rectangle2D;
    }

    public void boundSize(List<Node> nodeList) {
        try {
            List<Double> xList = new LinkedList<>();
            List<Double> yList = new LinkedList<>();

            for (Node node : nodeList) {
                xList.add(node.getCoor().getX());
                yList.add(node.getCoor().getY());
            }

            Double xMax = Collections.max(xList);
            Double xMin = Collections.min(xList);
            Double yMax = Collections.max(yList);
            Double yMin = Collections.min(yList);

            setRectangle2D(new Rectangle.Double(xMin, yMin, xMax-xMin, yMax-yMin));
        } catch (Exception e) {
        }
    }
}
