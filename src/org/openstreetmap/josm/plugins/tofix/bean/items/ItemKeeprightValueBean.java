package org.openstreetmap.josm.plugins.tofix.bean;

/**
 *
 * @author ruben
 */
public class ItemKeeprightValueBean {
    String object_type;
    Long object_id;
    String st_astext;

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

    public Long getObject_id() {
        return object_id;
    }

    public void setObject_id(Long object_id) {
        this.object_id = object_id;
    }


    public String getSt_astext() {
        return st_astext;
    }

    public void setSt_astext(String st_astext) {
        this.st_astext = st_astext;
    }
    
    
}
