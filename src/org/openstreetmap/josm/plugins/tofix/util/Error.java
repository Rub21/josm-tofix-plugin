package org.openstreetmap.josm.plugins.tofix.util;

import javax.swing.JOptionPane;
import org.openstreetmap.josm.Main;

/**
 *
 * @author ruben
 */
public class Error {

// can not request data - Something went wrong on Server
    public static void error_request_data() {
        JOptionPane.showMessageDialog(Main.parent, "Something went wrong on Server!, Please change the Task or try to again");
    }
}
