package org.openstreetmap.josm.plugins.tofix;

import java.awt.Cursor;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.mapmode.MapMode;
import org.openstreetmap.josm.gui.MapFrame;

/**
 *
 * @author ruben
 */
public class TofixMode extends MapMode {

    public TofixMode(MapFrame mapFrame, String name, String desc) {
        super(name, "icontofix.png", desc, mapFrame, Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    @Override
    public void enterMode() {
        super.enterMode();
        Main.map.mapView.addMouseListener(this);
    }

    @Override
    public void exitMode() {
        super.exitMode();
        Main.map.mapView.removeMouseListener(this);
    }
}
