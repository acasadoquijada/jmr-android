package com.example.alejandro.jmr_android.jmr;

import android.graphics.Bitmap;

/**
 * Created by alejandro on 21/08/2017.
 */

public class JMRImage {

    private Bitmap image;
    private String name;
    private int index;
    private boolean pressed;

    public void JMRImage(){
        this.image = null;
        this.name = null;
        index = -1;
        pressed = false;
    }

    public void JMRImage(Bitmap image, String name){
        this.image = image;
        this.name = name;
        index = -1;
        pressed = false;
    }

    public Bitmap getImage(){
        return image;
    }

    public String getName(){
        return name;
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
