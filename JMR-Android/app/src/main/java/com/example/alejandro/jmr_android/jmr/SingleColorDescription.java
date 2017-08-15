package com.example.alejandro.jmr_android.jmr;


import android.graphics.Bitmap;
import android.graphics.Color;


/**
 * Created by alejandro on 15/08/2017.
 */

public class SingleColorDescription {

    private MiColor color;

    public SingleColorDescription(MiColor color){
        this.color = color;
    }

    public SingleColorDescription(Bitmap image){
        this.color = mean(image);
    }

    private MiColor mean(Bitmap image) {
        MiColor pixelColor;
        float mean[] = {0.0f, 0.0f, 0.0f}; //RGB
        double imageSize = image.getWidth() * image.getHeight();

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                // Color conversion takes place in getRGB method, if necessary
                int c = image.getPixel(x, y);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);
            }
        }
        mean[0] /= imageSize;
        mean[1] /= imageSize;
        mean[2] /= imageSize;

        return new MiColor((int) mean[0], (int) mean[1], (int) mean[2]);
    }

    @Override
    public String toString(){
        return "SingleColorDescriptor: [" + color.getRojo()
                + "," + color.getVerde() + "," + color.getAzul()+"]";
    }
    public MiColor getColor(){
        return color;
    }

}
