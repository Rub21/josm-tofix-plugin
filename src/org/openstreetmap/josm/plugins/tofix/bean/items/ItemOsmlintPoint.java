package org.openstreetmap.josm.plugins.tofix.bean.items;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;

/**
 *
 * @author samely
 */
public class ItemOsmlintPoint extends AbstractItemOsmlint {

    public Node get_node(){        
        System.out.println("Esoty entando a get_node en itemosmlintpoint");
        System.out.println("Estas son las coordenadas en itemosmlintpoint: "+getCoordinates().toString());
        String geoString = getCoordinates();
        geoString = geoString.replace("[", "").replace("]", "");
        String[] array = geoString.split(",");
        System.out.println("Esto es el size del array "+ array.length);
        System.out.println("Esto e el arrary en itemoslpoint: "+array[1].toString() + " "+array[0].toString());
        LatLon latLon = new LatLon(Double.parseDouble(array[1]), Double.parseDouble(array[0]));
        System.out.println("En itemosmlintpoint esto es el latlon: "+latLon.getX()+latLon.getY());
        return new Node(latLon);
    }
}
