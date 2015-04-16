package org.openstreetmap.josm.plugins.tofix.draw;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collection;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.LayerListPopup;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.plugins.tofix.util.Util;

/**
 * This is a layer that draws a grid
 */
public class TofixLayer extends Layer {

    LatLon coordinate;

    public TofixLayer(String name, LatLon coordinate) {
        super(name);
        this.coordinate = coordinate;
    }

    private static final Icon icon = new ImageIcon("icontofix.png");

    private Collection<OsmPrimitive> points = Main.main.getInProgressSelection();

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public String getToolTipText() {
        return tr("Layer to make measurements");
    }

    @Override
    public boolean isMergable(Layer other) {
        //return other instanceof TofixLayer;
        return false;
    }

    @Override
    public void mergeFrom(Layer from) {
        // TODO: 
    }

    @Override
    public void paint(Graphics2D g, final MapView mv, Bounds bounds) {

        g.setColor(Color.green);
        Point l = null;
        for (OsmPrimitive p : points) {
            Point pnt = mv.getPoint(p.getBBox().getCenter());
            if (l != null) {
                g.drawLine(l.x, l.y, pnt.x, pnt.y);
            }
            g.drawOval(pnt.x - 2, pnt.y - 2, 20, 20);
            l = pnt;
        }
        Util.print(coordinate);
        Point pnt = mv.getPoint(coordinate);
        g.drawOval(pnt.x, pnt.y-2, 100, 100);
    }

    @Override
    public void visitBoundingBox(BoundingXYVisitor v) {
        // nothing to do here
    }

    @Override
    public Object getInfoComponent() {
        return getToolTipText();
    }

    @Override
    public Action[] getMenuEntries() {
        return new Action[]{
            LayerListDialog.getInstance().createShowHideLayerAction(),
            // TODO: implement new JMenuItem(new LayerListDialog.DeleteLayerAction(this)),
            SeparatorLayerAction.INSTANCE,
            SeparatorLayerAction.INSTANCE,
            new LayerListPopup.InfoAction(this)};
    }

    private int round(double d) {
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if (result < 0.5) {
            return d < 0 ? -i : i;
        } else {
            return d < 0 ? -(i + 1) : i + 1;
        }
    }

}
