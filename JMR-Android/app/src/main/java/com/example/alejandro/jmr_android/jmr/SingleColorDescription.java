package com.example.alejandro.jmr_android.jmr;


import android.graphics.Bitmap;


/**
 * Created by alejandro on 15/08/2017.
 */

public class SingleColorDescription {

    private int[] color;

    public SingleColorDescription(){
        color = new int[3];
    }

    public SingleColorDescription(Bitmap image) {
        color = new int[3];
        mean(image);
    }

    public SingleColorDescription(int[] rgb){
        setColor(rgb);
    }

    static {
        System.loadLibrary("descriptor");
    }

    public native float[] meanC(int[] image);

    public native double compare(int red1, int green1, int blue1, int red2, int green2, int blue2);

    private void mean(Bitmap image) {

        float[] mean = {0.0f,0.0f,0.0f};

        int [] image1D = new int[image.getHeight()*image.getWidth()];
        int k = 0;
        for(int x = 0; x < image.getHeight(); x++){
            for(int y = 0; y < image.getWidth(); y++){
                image1D[k] = image.getPixel(x,y);
                k++;
            }
        }

        mean = meanC(image1D);

        color[0] = (int)mean[0];
        color[1] = (int)mean[1];
        color[2] = (int)mean[2];
    }

    public void setColor(int[] rgb){
        color = rgb;
    }

    public int[] getColor(){
        return color;
    }

    public Double compare(SingleColorDescription desc) {
        int[] c1 = this.color;
        int[] c2 = desc.color;

        double value = compare(c1[0],c1[1],c1[2],
                c2[0],c2[1],c2[2]);

        return value;
    }


}
