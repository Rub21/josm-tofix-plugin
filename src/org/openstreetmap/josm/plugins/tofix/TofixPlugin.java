package org.openstreetmap.josm.plugins.tofix;

import javax.swing.JMenu;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

public class TofixPlugin extends Plugin {
    static boolean reverterUsed = false;
    public TofixPlugin(PluginInformation info) {
        super(info);
        JMenu windowMenu = Main.main.menu.windowMenu;
        //MainMenu.add(historyMenu, new ObjectsHistoryAction());
        MainMenu.add(windowMenu, new TofixAcction());

    }
}