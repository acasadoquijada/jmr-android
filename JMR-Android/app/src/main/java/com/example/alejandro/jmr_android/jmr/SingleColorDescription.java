package com.example.alejandro.jmr_android.jmr;


import android.graphics.Bitmap;
import android.graphics.Color;


/**
 * Created by alejandro on 15/08/2017.
 */

public class SingleColorDescription {

    private JMRColor color;

    public SingleColorDescription(JMRColor color){
        this.color = color;
    }

    public SingleColorDescription(Bitmap image){
        this.color = mean(image);
    }

    private JMRColor mean(Bitmap image) {
        JMRColor pixelColor;
        float mean[] = {0.0f, 0.0f, 0.0f}; //RGB
        double imageSize = image.getWidth() * image.getHeight();
        int c = 0;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < (image.getHeight()/8); y += 8) {
                // Color conversion takes place in getRGB method, if necessary
                c = image.getPixel(x, y*4);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel(x, (y*4)+1);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel(x, (y*4)+2);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel(x, (y*4)+3);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel(x, (y*4)+4);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel(x, (y*4)+5);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel(x, (y*4)+6);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel(x, (y*4)+7);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);
            }
        }
        mean[0] /= imageSize;
        mean[1] /= imageSize;
        mean[2] /= imageSize;

        return new JMRColor((int) mean[0], (int) mean[1], (int) mean[2]);
    }

    @Override
    public String toString(){
        return "SingleColorDescriptor: [" + color.getRojo()
                + "," + color.getVerde() + "," + color.getAzul()+"]";
    }
    public JMRColor getColor(){
        return color;
    }

    static public double DefaultComparator(SingleColorDescription t, SingleColorDescription u){
        JMRColor c1 = t.color, c2 = u.color;
        double rDif = Math.pow(c1.getRojo()-c2.getRojo(),2);
        double gDif = Math.pow(c1.getVerde()-c2.getVerde(),2);
        double bDif = Math.pow(c1.getAzul()-c2.getAzul(),2);
        return Math.sqrt(rDif+gDif+bDif);
        }
    }

