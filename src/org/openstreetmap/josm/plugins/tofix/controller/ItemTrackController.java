package org.openstreetmap.josm.plugins.tofix.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.openstreetmap.josm.plugins.tofix.bean.ActionBean;
import org.openstreetmap.josm.plugins.tofix.bean.TrackBean;
import org.openstreetmap.josm.plugins.tofix.util.Request;

public class ItemTrackController {

    public void send_track_edit(String url, TrackBean trackBean) {
        JsonObjectBuilder attributesBuilder = Json.createObjectBuilder();
        JsonObjectBuilder trackBeanBuilder = Json.createObjectBuilder();
        trackBeanBuilder.add("user", trackBean.getAttributes().getUser())
                .add("editor", trackBean.getAttributes().getEditor());
        attributesBuilder.add("attributes", trackBeanBuilder);
        JsonObject track_edit = attributesBuilder.build();
        try {
            Request.sendPOST_Json(url, track_edit.toString());
        } catch (IOException ex) {
            Logger.getLogger(ItemTrackController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send_track_action(String url, ActionBean trackBean) { //skip, fixed and noterror
        JsonObjectBuilder trackBeanBuilder = Json.createObjectBuilder();
        trackBeanBuilder.add("user", trackBean.getUser())
                .add("action", trackBean.getAction())
                .add("key", trackBean.getKey())
                .add("editor", trackBean.getEditor());
        JsonObject track_skip = trackBeanBuilder.build();
        try {
            Request.sendPUT_Json(url, track_skip.toString());
        } catch (IOException ex) {
            Logger.getLogger(ItemTrackController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
