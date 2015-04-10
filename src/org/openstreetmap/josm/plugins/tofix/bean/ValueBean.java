
package org.openstreetmap.josm.plugins.tofix.bean;

/**
 *
 * @author ruben
 */
public class ValueBean {

     Double X;
     Double Y;
     Float weight;
     Integer problem_id;
     String way_id;
     String node_id;
     Double highwaykey;
     Double distance;
     String iso_a2;

    public Double getX() {
        return X;
    }

    public void setX(Double X) {
        this.X = X;
    }

    public Double getY() {
        return Y;
    }

    public void setY(Double Y) {
        this.Y = Y;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(Integer problem_id) {
        this.problem_id = problem_id;
    }

    public String getWay_id() {
        return way_id;
    }

    public void setWay_id(String way_id) {
        this.way_id = way_id;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public Double getHighwaykey() {
        return highwaykey;
    }

    public void setHighwaykey(Double highwaykey) {
        this.highwaykey = highwaykey;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getIso_a2() {
        return iso_a2;
    }

    public void setIso_a2(String iso_a2) {
        this.iso_a2 = iso_a2;
    }

}
