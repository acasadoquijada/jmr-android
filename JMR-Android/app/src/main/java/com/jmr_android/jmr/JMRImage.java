package com.jmr_android.jmr;

import java.io.Serializable;

/**
 * Created by Lincoln on 04/04/16.
 */
public class JMRImage implements Serializable {
    private String name;
    private String path;
    private String distance;

    public JMRImage() {

    }

    public JMRImage(String name, String path, String distance) {
        this.name = name;
        this.path = path;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setName(String name) {
        this.name = name;
    }

}
