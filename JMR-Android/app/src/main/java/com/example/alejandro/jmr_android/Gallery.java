package com.example.alejandro.jmr_android;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.alejandro.jmr_android.jmr.JMRImage;

import java.io.IOException;
import java.util.ArrayList;

public class Gallery {
    
    private ArrayList<JMRImage> images;
    private Activity activity;
    private int size;

    public Gallery(Activity activity){

        this.activity = activity;
        images = new ArrayList<>();
        getGalleryImages();
        this.size = this.images.size();
    }

    public ArrayList<JMRImage> getAllImagesURI(){ return images;}

    public String getImageURI(int index){
        return images.get(index).getName();
    }

    public int size(){
        return size;
    }

    public Bitmap getImagen(int index){
        Bitmap imagen=null;
        try {
            imagen = MediaStore.Images.Media.getBitmap(
                    this.activity.getApplicationContext().getContentResolver(),
                    Uri.parse("file:///" + images.get(index).getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap imagenReescalada = Bitmap.createScaledBitmap(imagen,200,200,true);

        return imagenReescalada;
    }

    public Bitmap getImagen(int index, int ancho, int largo){
        Bitmap imagen=null;
        try {
            imagen = MediaStore.Images.Media.getBitmap(
                    this.activity.getApplicationContext().getContentResolver(),
                    Uri.parse("file:///" + images.get(index)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap imagenReescalada = Bitmap.createScaledBitmap(imagen,ancho,largo,true);

        return imagenReescalada;
    }

    private void getGalleryImages(){
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        int index = 0;
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.d("Nombre", absolutePathOfImage);
            JMRImage jmrImage = new JMRImage();
            jmrImage.setName(absolutePathOfImage);
            jmrImage.setIndex(index);
            index++;

            images.add(jmrImage);
        }
    }
}
