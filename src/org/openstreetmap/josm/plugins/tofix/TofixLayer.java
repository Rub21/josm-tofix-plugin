package org.openstreetmap.josm.plugins.tofix;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.data.osm.visitor.paint.MapRendererFactory;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.LayerListPopup;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.tools.ImageProvider;

public class TofixLayer extends Layer implements ActionListener {

    LatLon latLon;
    List<Node> list_nodes;
    List<List<Node>> list_list_nodes;
    List<List<List<Node>>> list_list_list_nodes;
    List<List<List<List<Node>>>> list_list_list_list_nodes;

    String type = "";
    float width;

    final Collection<OsmPrimitive> points = Main.main.getInProgressSelection();

    /**
     * Constructs a new {@code TofixLayer}.
     *
     * @param name layer name
     */
    public TofixLayer(String name) {
        super(name);
    }

    @Override
    public Icon getIcon() {
        return ImageProvider.get("icontofix_layer");
    }

    @Override
    public String getToolTipText() {
        return tr("Layer to draw OSM error");
    }

    @Override
    public boolean isMergable(Layer other) {
        return false;
    }

    public void add_Node(LatLon latLon) {
        type = "draw_node";
        this.latLon = latLon;
        Main.map.mapView.repaint();
    }

    public void add_Nodes(List<Node> list_nodes) {
        type = "draw_nodes";
        this.list_nodes = list_nodes;
        Main.map.mapView.repaint();
    }

    public void add_Line(List<List<Node>> list_nodes) {
        type = "draw_line";
        this.list_list_nodes = list_nodes;
        Main.map.mapView.repaint();
    }

    public void add_lines(List<List<List<Node>>> list_nodes) {
        type = "draw_lines";
        this.list_list_list_nodes = list_nodes;
        Main.map.mapView.repaint();
    }

    public void add_Lines(List<List<List<List<Node>>>> list_nodes) {
        type = "draw_Lines";
        this.list_list_list_list_nodes = list_nodes;
        Main.map.mapView.repaint();
    }

    @Override
    public void paint(Graphics2D g, final MapView mv, Bounds bounds) {
        Stroke ss = g.getStroke();
        if (MapRendererFactory.getInstance().isWireframeMapRendererActive()) {
            width = 1f;
        } else {
            width = 5f;
        }

        g.setColor(new Color(254, 30, 123));
        g.setStroke(new BasicStroke((float) width));
        if ("draw_node".equals(type)) {
            Point pnt = mv.getPoint(latLon);
            g.drawOval(pnt.x - 25, pnt.y - 25, 50, 50);
        } else if ("draw_line".equals(type)) {
            for (List<Node> l_nodes : list_list_nodes) {
                for (int i = 0; i < l_nodes.size() - 1; i++) {
                    Point pnt1 = mv.getPoint(l_nodes.get(i).getCoor());
                    Point pnt2 = mv.getPoint(l_nodes.get(i + 1).getCoor());
                    g.drawLine(pnt1.x, pnt1.y, pnt2.x, pnt2.y);
                }
            }
        } else if ("draw_nodes".equals(type)) {
            for (Node node : list_nodes) {
                Point pnt = mv.getPoint(node.getCoor());
                g.drawOval(pnt.x - 10, pnt.y - 10, 20, 20);
            }
        } else if ("draw_lines".equals(type)) {
            for (List<List<Node>> ll_nodes : list_list_list_nodes) {
                for (List<Node> l_nodes : ll_nodes) {
                    for (int i = 0; i < l_nodes.size() - 1; i++) {
                        Point pnt1 = mv.getPoint(l_nodes.get(i).getCoor());
                        Point pnt2 = mv.getPoint(l_nodes.get(i + 1).getCoor());
                        g.drawLine(pnt1.x, pnt1.y, pnt2.x, pnt2.y);
                    }
                }
            }
        } else if ("draw_Lines".equals(type)) {
            for (List<List<List<Node>>> lll_nodes : list_list_list_list_nodes) {
                for (List<List<Node>> ll_nodes : lll_nodes) {
                    for (List<Node> l_nodes : ll_nodes) {
                        for (int i = 0; i < l_nodes.size() - 1; i++) {
                            Point pnt1 = mv.getPoint(l_nodes.get(i).getCoor());
                            Point pnt2 = mv.getPoint(l_nodes.get(i + 1).getCoor());
                            g.drawLine(pnt1.x, pnt1.y, pnt2.x, pnt2.y);
                        }
                    }
                }
            }
        }
        g.setStroke(ss);
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
