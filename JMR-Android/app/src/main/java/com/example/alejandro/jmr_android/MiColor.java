package com.example.alejandro.jmr_android;

/**
 * Created by alejandro on 07/08/2017.
 */

public class MiColor {

    private long rojo;
    private long verde;
    private long azul;

    public MiColor(){
        rojo = 0;
        verde = 0;
        azul = 0;
    }

    public MiColor(long rojo, long verde, long azul){
        this.rojo = rojo;
        this.verde = verde;
        this.azul = azul;
    }

    public void setRojo(long rojo){
        this.rojo = rojo;
    }

    public void setVerde(long verde){
        this.verde = verde;
    }

    public void setAzul(long azul){
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
