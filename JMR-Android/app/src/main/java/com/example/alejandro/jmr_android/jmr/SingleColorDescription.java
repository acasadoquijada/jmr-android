package com.example.alejandro.jmr_android.jmr;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;


/**
 * Created by alejandro on 15/08/2017.
 */

public class SingleColorDescription {

    private JMRColor color;
    private float[] media;

    public SingleColorDescription(JMRColor color) {
        this.color = color;
    }

    public SingleColorDescription(Bitmap image) {
        this.color = mean(image);
    }

    static {
        System.loadLibrary("descriptor");
    }

    public native float[] meanC(int[] image);

    public native double compare(int red1, int green1, int blue1, int red2, int green2, int blue2);

    private JMRColor mean(Bitmap image) {

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

        return new JMRColor((int) mean[0], (int) mean[1], (int) mean[2]);
    }

    @Override
    public String toString() {
        return "SingleColorDescriptor: [" + color.getRojo()
                + "," + color.getVerde() + "," + color.getAzul() + "]";
    }

    public JMRColor getColor() {
        return color;
    }

    public Double compare(SingleColorDescription desc) {
        JMRColor c1 = this.color, c2 = desc.color;

        double value = compare(c1.getRojo(),c1.getVerde(),c1.getAzul(),
                c2.getRojo(),c2.getVerde(),c2.getAzul());

        return value;
    }


}
