package org.openstreetmap.josm.plugins.tofix.controller;

import com.google.gson.Gson;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.stream.JsonParser;
import org.openstreetmap.josm.plugins.tofix.bean.ListTaskBean;
import org.openstreetmap.josm.plugins.tofix.util.Request;
import org.openstreetmap.josm.plugins.tofix.util.Util;

/**
 *
 * @author ruben
 */
public class ListTaskController {

    private ListTaskBean listTasksBean = null;
    private String url;

    public ListTaskController() {
        this.url = "http://osmlab.github.io/to-fix/src/data/tasks.json";
    }

    public ListTaskBean getListTasksBean() {
        Gson gson = new Gson();
        String stringListTaskBean = null;
        try {
            stringListTaskBean = Request.sendGET(url);
//
//            StringReader reader = new StringReader(stringListTaskBean);
//            Util.print("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//            Util.print(reader);
//            JsonParser parser = Json.createParser(reader);
//            Util.print(parser);
//            Util.print("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        } catch (Exception ex) {
            Logger.getLogger(StatusController.class.getName()).log(Level.SEVERE, null, ex);
        }
        listTasksBean = gson.fromJson(stringListTaskBean, ListTaskBean.class);

        return listTasksBean;

    }

}
