package com.example.alejandro.jmr_android.jmr;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Lincoln on 04/04/16.
 */
public class JMRImage implements Serializable{
    private String name;
    private String path;
    private String distance;
    private Bitmap image;
    private int index;
    private boolean pressed;

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

    public Bitmap getImage(){
        return image;
    }

    public int getIndex(){
        return index;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public boolean getPressed(){
        return pressed;
    }

    public void setPressed(boolean pressed){
        this.pressed = pressed;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }

    public void setName(String name){
        this.name = name;
    }

}
