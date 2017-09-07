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
    public SingleColorDescription(JMRColor color){
        this.color = color;
    }

    public SingleColorDescription(Bitmap image){
        this.color = mean(image);
    }

    private JMRColor mean(Bitmap image) {
        JMRColor pixelColor;
        media = new float[3];
        media[0] = 0.0f;
        media[1] = 0.0f;
        media[2] = 0.0f;

        float mean[] = {0.0f, 0.0f, 0.0f}; //RGB
        double imageSize = image.getWidth() * image.getHeight();
        int c = 0;
        for (int x = 0; x < (image.getWidth()/8); x +=8) {
            for (int y = 0; y < (image.getHeight()/8); y += 8) {
                c = image.getPixel(x*8, y*8);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel(x*8, (y*8)+1);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel(x*8, (y*8)+2);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel(x*8, (y*8)+3);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel(x*8, (y*8)+4);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel(x*8, (y*8)+5);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel(x*8, (y*8)+6);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel(x*8, (y*8)+7);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+1, y*8);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+1, (y*8)+1);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+1, (y*8)+2);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+1, (y*8)+3);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+1, (y*8)+4);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+1, (y*8)+5);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+1, (y*8)+6);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+1, (y*8)+7);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+2, y*8);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+2, (y*8)+1);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+2, (y*8)+2);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+2, (y*8)+3);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+2, (y*8)+4);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+2, (y*8)+5);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+2, (y*8)+6);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+2, (y*8)+7);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+3, y*8);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+3, (y*8)+1);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+3, (y*8)+2);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+3, (y*8)+3);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+3, (y*8)+4);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+3, (y*8)+5);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+3, (y*8)+6);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+3, (y*8)+7);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+4, y*8);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+4, (y*8)+1);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+4, (y*8)+2);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+4, (y*8)+3);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+4, (y*8)+4);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+4, (y*8)+5);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+4, (y*8)+6);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+4, (y*8)+7);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+5, y*8);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+5, (y*8)+1);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+5, (y*8)+2);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+5, (y*8)+3);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+5, (y*8)+4);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+5, (y*8)+5);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+5, (y*8)+6);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+5, (y*8)+7);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+6, y*8);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+6, (y*8)+1);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+6, (y*8)+2);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+6, (y*8)+3);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+6, (y*8)+4);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+6, (y*8)+5);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+6, (y*8)+6);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+6, (y*8)+7);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+7, y*8);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+7, (y*8)+1);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+7, (y*8)+2);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+7, (y*8)+3);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+7, (y*8)+4);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+7, (y*8)+5);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+7, (y*8)+6);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);

                c = image.getPixel((x*8)+7, (y*8)+7);
                mean[0] += Color.red(c);
                mean[1] += Color.green(c);
                mean[2] += Color.blue(c);
            }
        }

        Log.d("MEDIA", Float.toString(mean[0]) + " " + Float.toString(mean[1])
                + " " + Float.toString(mean[2]));

        mean[0] /= imageSize;
        mean[1] /= imageSize;
        mean[2] /= imageSize;

        return new JMRColor((int) mean[0], (int) mean[1], (int) mean[2]);
    }

    private void addMean(Bitmap image, int x, int y){
        int c = image.getPixel(x,y);
        media[0] += Color.red(c);
        media[1] += Color.green(c);
        media[2] += Color.blue(c);
    }

    @Override
    public String toString(){
        return "SingleColorDescriptor: [" + color.getRojo()
                + "," + color.getVerde() + "," + color.getAzul()+"]";
    }
    public JMRColor getColor(){
        return color;
    }

    public Double compare(SingleColorDescription desc) {
        JMRColor c1 = this.color, c2 = desc.color;
        double rDif = Math.pow(c1.getRojo()-c2.getRojo(),2);
        double gDif = Math.pow(c1.getVerde()-c2.getVerde(),2);
        double bDif = Math.pow(c1.getAzul()-c2.getAzul(),2);
        return Math.sqrt(rDif+gDif+bDif);
    }

    static public double DefaultComparator(SingleColorDescription t, SingleColorDescription u){
        JMRColor c1 = t.color, c2 = u.color;
        double rDif = Math.pow(c1.getRojo()-c2.getRojo(),2);
        double gDif = Math.pow(c1.getVerde()-c2.getVerde(),2);
        double bDif = Math.pow(c1.getAzul()-c2.getAzul(),2);
        return Math.sqrt(rDif+gDif+bDif);
        }
    }

