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
    Long way_id;
    Long node_id;
    String highwaykey;
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

    public Long getWay_id() {
        return way_id;
    }

    public void setWay_id(Long way_id) {
        this.way_id = way_id;
    }

    public Long getNode_id() {
        return node_id;
    }

    public void setNode_id(Long node_id) {
        this.node_id = node_id;
    }

    public String getHighwaykey() {
        return highwaykey;
    }

    public void setHighwaykey(String highwaykey) {
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
