package com.example.alejandro.jmr_android;

import android.graphics.Bitmap;

/**
 * Created by alejandro on 12/08/2017.
 */

public class Resultado {

    private Bitmap imagen;
    private double distancia;

    public Resultado(){
    }

    public Resultado(Bitmap imagen, double distancia){
        this.imagen = imagen;
        this.distancia = distancia;
    }

    public double getDistancia(){
        return distancia;
    }

    public Bitmap getImagen(){
        return imagen;
    }

    public void setDistancia(double distancia){
        this.distancia = distancia;
    }

    public int compareTo(Resultado r){
        return Double.compare(distancia,r.distancia);
    }


}
