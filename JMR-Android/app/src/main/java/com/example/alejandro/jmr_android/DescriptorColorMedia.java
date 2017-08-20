package com.example.alejandro.jmr_android;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.example.alejandro.jmr_android.jmr.JMRColor;

/**
 * Created by alejandro on 07/08/2017.
 */

public class DescriptorColorMedia {

    private Bitmap imagen;
    private JMRColor color;

    private double distancia;

    public DescriptorColorMedia(Bitmap imagen){
        this.imagen = imagen;
        calcularColor();
    }

    private void calcularColor(){

        long redColors = 0;
        long greenColors = 0;
        long blueColors = 0;
        long pixelCount = 0;
        Log.d("Ancho y alto:",
                Integer.toString(imagen.getHeight()) + " - " +
                Integer.toString(imagen.getWidth()));

        for (int y = 0; y < imagen.getHeight(); y++)
        {
            for (int x = 0; x < imagen.getWidth(); x++)
            {
                int c = imagen.getPixel(x, y);
                pixelCount++;
                redColors += Color.red(c);
                greenColors += Color.green(c);
                blueColors += Color.blue(c);
            }
        }
        // calculate average of bitmap r,g,b values
        long red = (redColors/pixelCount);
        long green = (greenColors/pixelCount);
        long blue = (blueColors/pixelCount);

        color = new JMRColor((int)red,(int)green,(int)blue);

    }

    static public double calcularDistancia(DescriptorColorMedia d1, DescriptorColorMedia d2){
        JMRColor c1 = d1.color, c2 = d2.color;
        double rDif = Math.pow(c1.getRojo()-c2.getRojo(),2);
        double gDif = Math.pow(c1.getVerde()-c2.getVerde(),2);
        double bDif = Math.pow(c1.getAzul()-c2.getAzul(),2);
        return Math.sqrt(rDif+gDif+bDif);
    }
}
