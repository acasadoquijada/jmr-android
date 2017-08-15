package com.example.alejandro.jmr_android.jmr;

/**
 * Created by alejandro on 07/08/2017.
 */

public class MiColor {

    private int rojo;
    private int verde;
    private int azul;

    public MiColor(){
        rojo = 0;
        verde = 0;
        azul = 0;
    }

    public MiColor(int rojo, int verde, int azul){
        this.rojo = rojo;
        this.verde = verde;
        this.azul = azul;
    }

    public void setRojo(int rojo){
        this.rojo = rojo;
    }

    public void setVerde(int verde){
        this.verde = verde;
    }

    public void setAzul(int azul){
        this.azul = azul;
    }

    public long getRojo(){
        return rojo;
    }

    public long getAzul(){
        return azul;
    }

    public long getVerde(){
        return verde;
    }
}
