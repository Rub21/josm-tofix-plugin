package org.openstreetmap.josm.plugins.tofix;

import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

/**
 * main class for the Tofix plugin.
 *
 */
public class Tofix extends Plugin{

    public Tofix(PluginInformation info) {
        super(info);
    }

    /**
     * Called when the JOSM map frame is created or destroyed.
     */
    @Override
    public void mapFrameInitialized(MapFrame oldFrame, MapFrame newFrame) {
        if (oldFrame == null && newFrame != null) { // map frame added

        }
    }
}
