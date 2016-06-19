package org.openstreetmap.josm.plugins.tofix;

import java.awt.GraphicsEnvironment;

import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

public class TofixPlugin extends Plugin {

    /**
     * Constructs a new {@code TofixPlugin}.
     * @param info plugin information
     */
    public TofixPlugin(PluginInformation info) {
        super(info);
    }

    @Override
    public void mapFrameInitialized(MapFrame oldFrame, MapFrame newFrame) {
        if (newFrame != null && !GraphicsEnvironment.isHeadless()) {
            newFrame.addToggleDialog(new TofixDialog());
        }
    }
}
