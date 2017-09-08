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

    public native float[] meanC(MyBitMap obj);

    private JMRColor mean(Bitmap image) {

        MyBitMap myBitMap = new MyBitMap(image);
/*
        float[] mean = new float[3];

        mean = meanC(myBitMap);*/

        int [] image1D = new int[image.getHeight()*image.getWidth()];
        int k = 0;
        for(int x = 0; x < image.getHeight(); x++){
            for(int y = 0; y < image.getWidth(); y++){
                image1D[k] = image.getPixel(x,y);
                k++;
            }
        }

        float mean[] = {0.0f, 0.0f, 0.0f}; //RGB
        int c = 0;
        for(int i = 0; i < image1D.length/8; i++){
            c = image1D[i*2];
            mean[0] += Color.red(c);
            mean[1] += Color.green(c);
            mean[2] += Color.blue(c);

            c = image1D[i*2+1];
            mean[0] += Color.red(c);
            mean[1] += Color.green(c);
            mean[2] += Color.blue(c);

            c = image1D[i*2+2];
            mean[0] += Color.red(c);
            mean[1] += Color.green(c);
            mean[2] += Color.blue(c);

            c = image1D[i*2+3];
            mean[0] += Color.red(c);
            mean[1] += Color.green(c);
            mean[2] += Color.blue(c);

            c = image1D[i*2+4];
            mean[0] += Color.red(c);
            mean[1] += Color.green(c);
            mean[2] += Color.blue(c);

            c = image1D[i*2+5];
            mean[0] += Color.red(c);
            mean[1] += Color.green(c);
            mean[2] += Color.blue(c);

            c = image1D[i*2+6];
            mean[0] += Color.red(c);
            mean[1] += Color.green(c);
            mean[2] += Color.blue(c);

            c = image1D[i*2+7];
            mean[0] += Color.red(c);
            mean[1] += Color.green(c);
            mean[2] += Color.blue(c);
        }

        double imageSize = image.getWidth() * image.getHeight();

        mean[0] /= imageSize;
        mean[1] /= imageSize;
        mean[2] /= imageSize;

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
        double rDif = Math.pow(c1.getRojo() - c2.getRojo(), 2);
        double gDif = Math.pow(c1.getVerde() - c2.getVerde(), 2);
        double bDif = Math.pow(c1.getAzul() - c2.getAzul(), 2);
        return Math.sqrt(rDif + gDif + bDif);
    }


}
