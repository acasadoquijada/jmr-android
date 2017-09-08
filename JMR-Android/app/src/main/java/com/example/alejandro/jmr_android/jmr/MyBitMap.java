package com.example.alejandro.jmr_android.jmr;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by alejandro on 07/09/2017.
 */


public class MyBitMap  {

    private Bitmap img;

    public MyBitMap(Bitmap img){
        this.img = img;
    }

    /** Returns the bitmap's width */
    public final int getWidth() {

        return img.getWidth();
    }

    /** Returns the bitmap's height */
    public final int getHeight() {

        return img.getHeight();
    }

    public float[] getPixel(int x, int y){
        float[] pixel = new float[3];

        int c = img.getPixel(x,y);

        pixel[0] += Color.red(c);
        pixel[1] += Color.green(c);
        pixel[2] += Color.blue(c);

        return pixel;
    }

    public int getRed(int x, int y){
        int c = img.getPixel(x,y);

        return Color.red(c);
    }

    public int getGreen(int x, int y){
        int c = img.getPixel(x,y);

        return Color.green(c);
    }

    public int getBlue(int x, int y){
        int c = img.getPixel(x,y);

        return Color.blue(c);
    }

}
