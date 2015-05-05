package org.openstreetmap.josm.plugins.tofix.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.tofix.controller.StatusController;

/**
 *
 * @author ruben
 */
public class Status {

    final static String host = "http://54.147.184.23:8000/status";

    public static boolean server() {
        StatusController statusController = new StatusController(host);
        Util.print("=============================================");
        Util.print(statusController.getStatusBean().getStatus());
        
        if (statusController.getStatusBean().getStatus().equals("a ok")) {
            return true;
        } else {
            JOptionPane.showConfirmDialog(Main.parent, "The server is on maintenance!");
            return false;
        }

    }

    public static boolean isInternetReachable() {
        try {
            URL url = new URL("http://www.openstreetmap.org");
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
            //is no connection, this line will fail
            Object objData = urlConnect.getContent();

        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
