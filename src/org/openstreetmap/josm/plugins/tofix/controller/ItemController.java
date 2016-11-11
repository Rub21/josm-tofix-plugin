package org.openstreetmap.josm.plugins.tofix.controller;

import java.io.StringReader;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.openstreetmap.josm.plugins.tofix.bean.AccessToTask;
import org.openstreetmap.josm.plugins.tofix.bean.ResponseBean;
import org.openstreetmap.josm.plugins.tofix.bean.TaskCompleteBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.Item;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemKeeprightBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemKrakatoaBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemOsmlintLinestring;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemOsmlintMultipoint;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemOsmlintPoint;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemUsaBuildingsBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemSmallcomponents;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemStrava;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemTigerdeltaBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemUnconnectedBean;

import org.openstreetmap.josm.plugins.tofix.util.Request;
import org.openstreetmap.josm.plugins.tofix.util.Util;

/**
 *
 * @author ruben
 */
public class ItemController {

    Item item = new Item();
    ResponseBean responseBean = new ResponseBean();

    AccessToTask accessToTask;

    public AccessToTask getAccessToTask() {
        return accessToTask;
    }

    public void setAccessToTask(AccessToTask accessToTask) {
        this.accessToTask = accessToTask;
    }

    public Item getItem() {

        try {
            System.out.println("This is the url that enter in itemcontroller" + accessToTask.getTask_url());
            responseBean = Request.sendPOST(accessToTask.getTask_url());
            
            item.setStatus(responseBean.getStatus());
            Util.print(responseBean.getValue());
            JsonReader reader = Json.createReader(new StringReader(responseBean.getValue()));
            JsonObject object = reader.readObject();
            JsonObject geometry = (JsonObject) object.get("geometry");
            System.out.println("En itemcontroller esto es geometry type " + geometry.getString("type"));
            JsonObject properties = (JsonObject) object.get("properties");
            System.out.println("geometry type in item controller: "+geometry.getString("type"));
            item.setType(geometry.getString("type"));

            //if the structure change , we need to customize this site, to easy resolve , but we need to standardize the source in each task.
            switch (responseBean.getStatus()) {
                case HttpURLConnection.HTTP_OK:
                    if (geometry.getString("type").equals("Point")) {    
                        ItemOsmlintPoint iop = new ItemOsmlintPoint();
                        iop.setKey(properties.getString("_key"));
                        if (properties.containsKey("_osmId") && geometry.containsKey("type")) {
                            iop.setWay(Long.parseLong(properties.getJsonNumber("_osmId").toString()));
                            iop.setGeometry(geometry.getJsonString("type").toString());
                            iop.setCoordinates(geometry.get("coordinates").toString());
                            item.setItemOsmlintPoint(iop);
                        } else {
                            item.setStatus(520);
                        }
                    }
                    if (geometry.getString("type").equals("MultiPoint")) {
                        System.out.println("Estoy leyendo MultiPoint intemcontorller");

                        ItemOsmlintMultipoint iom = new ItemOsmlintMultipoint();
                        iom.setKey(properties.getString("_key"));
                        if (properties.containsKey("_osmId") && geometry.containsKey("type")) {
                            iom.setWay(Long.parseLong(properties.getJsonNumber("_osmId").toString()));
                            iom.setGeometry(geometry.getJsonString("type").toString());
                            iom.setCoordinates(geometry.get("coordinates").toString());
                            item.setItemOsmlintMultipoint(iom);
                        } else {
                            item.setStatus(520);
                        }

                    }
                    if (geometry.getString("type").equals("LineString")) {
                        System.out.println("Estoy leyendo LineString intemcontorller");

                        ItemOsmlintLinestring iol = new ItemOsmlintLinestring();
                        iol.setKey(properties.getString("_key"));
                        if (object.containsKey("geometry")) {
                            iol.setGeometry(geometry.getJsonString("type").toString());
                            iol.setCoordinates(geometry.get("coordinates").toString());
                            item.setItemOsmlintLinestring(iol);
                        } else {
                            item.setStatus(520);
                        }

                    }
                    if (geometry.getString("type").equals("MultiLineString")) {
                        System.out.println("Estoy leyendo MultiLineString intemcontorller");

                    }

//                    if (accessToTask.getTask_source().equals("unconnected")) {
//                        //https://github.com/osmlab/to-fix/wiki/Task%20sources#unconnected-minor
//                        ItemUnconnectedBean iub = new ItemUnconnectedBean();
//                        iub.setKey(object.getString("key"));
//                        JsonObject value = object.getJsonObject("value");
//                        if (value.containsKey("way_id") && value.containsKey("st_astext")) {
//                            //iub.setNode_id(Long.parseLong(value.getString("node_id")));
//                            iub.setWay_id(Long.parseLong(value.getString("way_id")));
//                            iub.setSt_astext(value.getString("st_astext"));
//                            item.setItemUnconnectedBean(iub);
//                        } else if (value.containsKey("X") && value.containsKey("Y") && value.containsKey("way_id") && value.containsKey("node_id")) {
//                            //Format from Arun https://github.com/osmlab/to-fix/wiki/Task%20sources#unconnected-major                           
//                            String st_astext = "POINT(" + value.getString("X") + " " + value.getString("Y") + ")";
//                            // iub.setNode_id(Long.parseLong(value.getString("node_id")));
//                            iub.setWay_id(Long.parseLong(value.getString("way_id")));
//                            iub.setSt_astext(st_astext);
//                            item.setItemUnconnectedBean(iub);
//                        } else {
//                            item.setStatus(520);// response 520 Unknown Error                            
//                        }
//                    }
//                    if (accessToTask.getTask_source().equals("keepright")) {
//                        //https://github.com/osmlab/to-fix/wiki/Task%20sources#broken-polygons
//                        ItemKeeprightBean ikb = new ItemKeeprightBean();
//                        ikb.setKey(object.getString("key"));
//                        JsonObject value = object.getJsonObject("value");
//                        if (value.containsKey("object_type") && value.containsKey("object_id") && value.containsKey("st_astext")) {
//                            ikb.setObject_id(Long.parseLong(value.getString("object_id")));
//                            ikb.setObject_type(value.getString("object_type"));
//                            ikb.setSt_astext(value.getString("st_astext"));
//                            item.setItemKeeprightBean(ikb);
//                        } else {
//                            item.setStatus(520);
//                        }
//                    }
//                    if (accessToTask.getTask_source().equals("tigerdelta")) {
//                        //https://github.com/osmlab/to-fix/wiki/Task%20sources#tiger-delta
//                        ItemTigerdeltaBean itb = new ItemTigerdeltaBean();
//                        itb.setKey(object.getString("key"));
//                        JsonObject value = object.getJsonObject("value");
//                        if (value.containsKey("geom")) {
//                            itb.setGeom(value.getString("geom"));
//                            item.setItemTigerdeltaBean(itb);
//                        } else {
//                            item.setStatus(520);
//                        }
//                    }
//                    if (accessToTask.getTask_source().equals("nycbuildings")) {
//                        //https://github.com/osmlab/to-fix/wiki/Task%20sources#usa-overlapping-buildings
//                        ItemUsaBuildingsBean inb = new ItemUsaBuildingsBean();
//                        inb.setKey(object.getString("key"));
//                        JsonObject value = object.getJsonObject("value");
//                        if (value.containsKey("lat") && value.containsKey("lon") && value.containsKey("elems")) {
//                            inb.setLat(Double.parseDouble(value.getString("lat")));
//                            inb.setLon(Double.parseDouble(value.getString("lon")));
//                            inb.setElems(value.getString("elems"));
//                            item.setItemUsabuildingsBean(inb);
//                        } else {
//                            item.setStatus(520);
//                        }
//                    }
//                    if (accessToTask.getTask_source().equals("krakatoa")) {
//                        //https://github.com/osmlab/to-fix/wiki/Task%20sources#krakatoa
//                        ItemKrakatoaBean ikb = new ItemKrakatoaBean();
//                        ikb.setKey(object.getString("key"));
//                        JsonObject value = object.getJsonObject("value");
//                        if (value.containsKey("geom")) {
//                            ikb.setGeom(value.getString("geom"));
//                            item.setItemKrakatoaBean(ikb);
//                        } else {
//                            item.setStatus(520);
//                        }
//                    }
//                    if (accessToTask.getTask_source().equals("strava")) {
//                        //https://github.com/osmlab/to-fix/wiki/Task%20sources#strava
//                        ItemStrava istrava = new ItemStrava();
//                        istrava.setKey(object.getString("key"));
//                        JsonObject value = object.getJsonObject("value");
//                        if (value.containsKey("geom")) {
//                            istrava.setGeom(value.getString("geom"));
//                            item.setItemStrava(istrava);
//                        } else {
//                            item.setStatus(520);
//                        }
//                    }
//                    if (accessToTask.getTask_source().equals("components")) {
//                        //https://github.com/osmlab/to-fix/wiki/Task%20sources#small-components
//                        ItemSmallcomponents isc = new ItemSmallcomponents();
//                        isc.setKey(object.getString("key"));
//                        JsonObject value = object.getJsonObject("value");
//                        if (value.containsKey("nothing") && value.containsKey("geom")) {
//                            isc.setGeom(value.getString("geom"));
//                            isc.setNothing(value.getString("nothing"));
//                            item.setItemSmallcomponents(isc);
//                        } else {
//                            item.setStatus(520);
//                        }
//                    }
//                    if (accessToTask.getTask_source().equals("osmlint-point")) {
//                        //https://github.com/osmlab/to-fix/wiki/Output-formats-osmlint-----osmlint2csv---tofix
//                        ItemOsmlintPoint iop = new ItemOsmlintPoint();
//                        iop.setKey(object.getString("key"));
//                        JsonObject value = object.getJsonObject("value");
//                        if (value.containsKey("way") && value.containsKey("geom")) {
//                            iop.setWay(Long.parseLong(value.getString("way")));
//                            iop.setGeom(value.getString("geom"));
//                            item.setItemOsmlintPoint(iop);
//                        } else {
//                            item.setStatus(520);
//                        }
//                    }
//                    if (accessToTask.getTask_source().equals("osmlint-linestring")) {
//                        //https://github.com/osmlab/to-fix/wiki/Output-formats-osmlint-----osmlint2csv---tofix
//                        ItemOsmlintLinestring iol = new ItemOsmlintLinestring();
//                        iol.setKey(object.getString("key"));
//                        JsonObject value = object.getJsonObject("value");
//                        if (value.containsKey("geom")) {
//                            iol.setGeom(value.getString("geom"));
//                            item.setItemOsmlintLinestring(iol);
//                        } else {
//                            item.setStatus(520);
//                        }
//                    }
//                    if (accessToTask.getTask_source().equals("osmlint-multipoint")) {
//                        //https://github.com/osmlab/to-fix/wiki/Output-formats-osmlint-----osmlint2csv---tofix
//                        ItemOsmlintMultipoint iom = new ItemOsmlintMultipoint();
//                        iom.setKey(object.getString("key"));
//                        JsonObject value = object.getJsonObject("value");
//                        if (value.containsKey("way") && value.containsKey("geom")) {
//                            iom.setWay(Long.parseLong(value.getString("way")));
//                            iom.setGeom(value.getString("geom"));
//                            item.setItemOsmlintMultipoint(iom);
//                        } else {
//                            item.setStatus(520);
//                        }
//                    }
                    break;
                case 410:
                    TaskCompleteBean taskCompleteBean = new TaskCompleteBean();
                    taskCompleteBean.setTotal(0);
                    String total = responseBean.getValue().replaceAll("[^0-9]+", " ");
                    if (total.trim().split(" ")[1] != null) {
                        taskCompleteBean.setTotal(Integer.parseInt(total.trim().split(" ")[1]));
                    }
                    item.setTaskCompleteBean(taskCompleteBean);
                    break;
                case 503:
                    //Servidor en mantenimiento
                    break;
            }

            reader.close();

        } catch (Exception ex) {
            Util.alert(ex);
            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return item;
    }
}
