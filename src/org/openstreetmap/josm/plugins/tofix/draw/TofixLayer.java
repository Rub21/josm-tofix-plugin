package org.openstreetmap.josm.plugins.tofix.draw;

import java.awt.BasicStroke;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.LayerListPopup;
import org.openstreetmap.josm.gui.layer.Layer;

public class TofixLayer extends Layer implements ActionListener {

    LatLon coordinate;
    List<LatLon> listcoordinates = new LinkedList<LatLon>();

    public TofixLayer(String name) {
        super(name);

    }

    private static final Icon icon = new ImageIcon("icontofix.png");

    final Collection<OsmPrimitive> points = Main.main.getInProgressSelection();

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public String getToolTipText() {
        return tr("Layer to draw OSM error");
    }

    @Override
    public boolean isMergable(Layer other) {
        return false;
    }

    public void add_coordinate(LatLon coordinate) {
        listcoordinates.add(coordinate);
        Main.map.mapView.repaint();

    }

    @Override
    public void paint(Graphics2D g, final MapView mv, Bounds bounds) {
        g.setColor(Color.red);
        g.setStroke(new BasicStroke((float) 5));
        Point l = null;
//        for (OsmPrimitive p : points) {
//            Point pnt = mv.getPoint(p.getBBox().getCenter());
//            if (l != null) {
//                g.drawLine(l.x, l.y, pnt.x, pnt.y);
//            }
//            g.drawOval(pnt.x - 2, pnt.y - 2, 20, 20);
//            l = pnt;
//        }
        for (LatLon coor : listcoordinates) {
            Point pnt = mv.getPoint(coor);
            g.drawOval(pnt.x - 25, pnt.y - 25, 50, 50);

        }

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

    @Override
    public void actionPerformed(ActionEvent e) {

        JOptionPane.showConfirmDialog(null, e.getSource());
    }

    @Override
    public void mergeFrom(Layer layer) {

    }

}
