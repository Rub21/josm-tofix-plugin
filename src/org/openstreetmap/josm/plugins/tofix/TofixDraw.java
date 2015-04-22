package org.openstreetmap.josm.plugins.tofix;

import javax.swing.JOptionPane;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.tofix.bean.ItemUnconnectedBean;
import org.openstreetmap.josm.plugins.tofix.layer.TofixLayer;
import static org.openstreetmap.josm.tools.I18n.tr;

/**
 *
 * @author ruben
 */
public class TofixDraw {

    ItemUnconnectedBean itemBean = null;

    public static void draw(final TofixLayer tofixLayer, LatLon latLon) {
        MapView mv = Main.map.mapView;
        Bounds bounds = null;
        // LatLon coor = new LatLon(lon, lat);
        if (latLon.isOutSideWorld()) {
            JOptionPane.showMessageDialog(Main.parent, tr("Can not find outside of the world."));
            return;
        }
        BoundingXYVisitor v = new BoundingXYVisitor();
        //double ex = 0.0001; = 2.34 m
        //double ex = 0.0007;// 16.7 m       
        v.visit(new Bounds(latLon.toBBox(0.0007).toRectangle()));
        Main.map.mapView.zoomTo(v);
        if (!Main.map.mapView.hasLayer(tofixLayer)) {
            mv.addLayer(tofixLayer);
            tofixLayer.add_coordinate(latLon);
        } else {
            tofixLayer.add_coordinate(latLon);
        }
    }

}
