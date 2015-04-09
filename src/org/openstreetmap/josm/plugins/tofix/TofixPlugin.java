package org.openstreetmap.josm.plugins.tofix;

import org.openstreetmap.josm.gui.IconToggleButton;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import static org.openstreetmap.josm.tools.I18n.tr;

public class TofixPlugin extends Plugin {

    private IconToggleButton btn;
    protected static TofixDialog tofixDialog;
    private TofixMode mode;

    public TofixPlugin(PluginInformation info) {
        super(info);
    }

    @Override
    public void mapFrameInitialized(MapFrame oldFrame, MapFrame newFrame) {
        if (newFrame != null) {
            newFrame.addToggleDialog(tofixDialog = new TofixDialog());
            mode = new TofixMode(newFrame, "To-Fix", tr("To-fix mode"));
            btn = new IconToggleButton(mode);
            btn.setVisible(true);
            newFrame.addMapMode(btn);
        } else {
            btn = null;
            mode = null;
            tofixDialog = null;
        }

    }
}
