package com.example.alejandro.jmr_android;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by alejandro on 06/09/2017.
 */

public class HMMDImage {

    private float[][][] image;
    private int height;
    private int width;

    public HMMDImage() {

    }

    public HMMDImage(Bitmap bitmap) {
        height = bitmap.getHeight();
        width = bitmap.getWidth();
        image = new float[height][width][4];

        int[] auxRgbVec = new int[3];
        float[] rgbVec = new float[3];

        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int c = bitmap.getPixel(x, y);
                auxRgbVec[0] = Color.red(c);
                auxRgbVec[1] = Color.green(c);
                auxRgbVec[2] = Color.blue(c);

                rgbVec[0] = (auxRgbVec[0]*1.0f)/255;
                rgbVec[1] = (auxRgbVec[1]*1.0f)/255;
                rgbVec[2] = (auxRgbVec[2]*1.0f)/255;

                image[x][y] = fromRGB(rgbVec);
            }
        }
    }

    static {
        System.loadLibrary("descriptor");
    }

    public void getHeight(int heigth){
        this.height = heigth;
    }

    public int getHeight(){
        return height;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public int getWidth(){
        return width;
    }

    public float[] getPixel(int x, int y) {
        return image[x][y];
    }

    public void setPixel(int x, int y, float[] pixel) {
        if (pixel.length == 4) {
            image[x][y] = pixel;
        }
    }

    public native float[] fromRGBC(float[] rgbVec);

    private float[] fromRGB(float[] rgbVec) {

        float[] hmmdVec = new float[4];
        float r = rgbVec[0];
        float g = rgbVec[1];
        float b = rgbVec[2];

        r = (r < 0.0f) ? 0.0f : ((r > 1.0f) ? 1.0f : r);
        g = (g < 0.0f) ? 0.0f : ((g > 1.0f) ? 1.0f : g);
        b = (b < 0.0f) ? 0.0f : ((b > 1.0f) ? 1.0f : b);

        float max = Math.max(Math.max(r, g), Math.max(g, b));
        float min = Math.min(Math.min(r, g), Math.min(g, b));
        float diff = (max - min);
        //	float sum = (float) ((max + min)/2.);

        float hue = 0;
        if (diff == 0)
            hue = 0;
        else if (r == max && (g - b) > 0)
            hue = 60 * (g - b) / (max - min);
        else if (r == max && (g - b) <= 0)
            hue = 60 * (g - b) / (max - min) + 360;
        else if (g == max)
            hue = (float) (60 * (2. + (b - r) / (max - min)));
        else if (b == max)
            hue = (float) (60 * (4. + (r - g) / (max - min)));

        // set hue

        hmmdVec[0] = hue;
        hmmdVec[1] = max;
        hmmdVec[2] = min;
        hmmdVec[3] = diff;

        //my.Debug.printCount(" - HMMD output: hmmd=["+hmmdVec[0]+","+hmmdVec[1]+","+hmmdVec[2]+","+hmmdVec[3]+"];");

        return hmmdVec;
    }

    public String toString() {
        String pixelString;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelString = Float.toString(getPixel(x, y)[0]) + " "
                        + Float.toString(getPixel(x, y)[1]) + " "
                        + Float.toString(getPixel(x, y)[2]) + " "
                        + Float.toString(getPixel(x, y)[3]);

              //  Log.d("Pixel " + Integer.toString(x) + "," + Integer.toString(y), pixelString);
            }

        }
        return null;
    }
}
