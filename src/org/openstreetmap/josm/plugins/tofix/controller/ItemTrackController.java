package org.openstreetmap.josm.plugins.tofix.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.openstreetmap.josm.plugins.tofix.bean.FixedBean;
import org.openstreetmap.josm.plugins.tofix.bean.TrackBean;
import org.openstreetmap.josm.plugins.tofix.util.Request;

public class ItemTrackController {

    public void send_track_edit(String url, TrackBean trackBean) {
        JsonObjectBuilder attributesBuilder = Json.createObjectBuilder();
        JsonObjectBuilder trackBeanBuilder = Json.createObjectBuilder();
        trackBeanBuilder.add("user", trackBean.getAttributes().getUser())
                .add("action", trackBean.getAttributes().getAction())
                .add("key", trackBean.getAttributes().getKey())
                .add("editor", trackBean.getAttributes().getEditor());
        attributesBuilder.add("attributes", trackBeanBuilder);
        JsonObject track_edit = attributesBuilder.build();
        try {
            System.out.println("en itemtraccontroller esto es la lista de atributtos "+track_edit.toString());
            Request.sendPOST_Json(url, track_edit.toString());
        } catch (IOException ex) {
            Logger.getLogger(ItemTrackController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send_track_skip(String url, TrackBean trackBean) {
        JsonObjectBuilder attributesBuilder = Json.createObjectBuilder();
        JsonObjectBuilder trackBeanBuilder = Json.createObjectBuilder();
        trackBeanBuilder.add("user", trackBean.getAttributes().getUser())
                .add("action", trackBean.getAttributes().getAction())
                .add("key", trackBean.getAttributes().getKey())
                .add("editor", trackBean.getAttributes().getEditor());
        attributesBuilder.add("attributes", trackBeanBuilder);
        JsonObject track_skip = attributesBuilder.build();
        try {
            Request.sendPOST_Json(url, track_skip.toString());
        } catch (IOException ex) {
            Logger.getLogger(ItemTrackController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send_track_fix(String url, FixedBean fixedBean) {
        JsonObjectBuilder fixedBeanBeanBuilder = Json.createObjectBuilder();
        fixedBeanBeanBuilder.add("user", fixedBean.getUser())
                .add("key", fixedBean.getKey());
        JsonObject track_fixed = fixedBeanBeanBuilder.build();
        try {
            Request.sendPOST_Json(url, track_fixed.toString());
        } catch (IOException ex) {
            Logger.getLogger(ItemTrackController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send_track_noterror(String url, FixedBean noterrorBean) { //noterrorBean has the same structure to  FixedBean
        JsonObjectBuilder noterrorBeanBeanBuilder = Json.createObjectBuilder();
        noterrorBeanBeanBuilder.add("user", noterrorBean.getUser())
                .add("key", noterrorBean.getKey());
        JsonObject track_noterror = noterrorBeanBeanBuilder.build();
        try {
            Request.sendPOST_Json(url, track_noterror.toString());
        } catch (IOException ex) {
            Logger.getLogger(ItemTrackController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
