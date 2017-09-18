package com.jmr_android.helper;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class GalleryHelper {

    private ArrayList<String> images;
    private Activity activity;

    public GalleryHelper(Activity activity) {

        this.activity = activity;
        this.images = new ArrayList<>();
        getGalleryImages();

        Collections.reverse(this.images);
    }

    public GalleryHelper(Activity activity, boolean debug) {
        if (debug) {
            this.activity = activity;
            this.images = new ArrayList<>();
            images.add("/storage/emulated/0/DCIM/Camera/Bark.0005.3.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Bark.0005.4.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Bark.0006.1.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Bark.0006.2.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Fabric.0005.3.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Fabric.0005.4.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Fabric.0006.1.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Fabric.0006.2.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Flowers.0002.2.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Flowers.0002.3.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Flowers.0002.4.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Flowers.0003.1.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Fabric.0006.2.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Water.0002.1.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Water.0002.2.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Water.0002.3.jpg");
            images.add("/storage/emulated/0/DCIM/Camera/Water.0002.4.jpg");
        }
    }

    public Bitmap getImagen(String path) {

        Bitmap image = null;
        if (images.contains(path)) {
            int index = images.indexOf(path);
            image = this.getImagen(index);

            return image;
        } else {
            return image;
        }

    }

    public Bitmap getImagen(int index) {
        Bitmap imagen = null;
        try {
            imagen = MediaStore.Images.Media.getBitmap(
                    this.activity.getApplicationContext().getContentResolver(),
                    Uri.parse("file:///" + images.get(index)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imagen != null) {
            Bitmap imagenReescalada = Bitmap.createScaledBitmap(imagen, 200, 200, true);
            return imagenReescalada;
        }
        return null;
    }

    public String getImageURI(int index) {
        return images.get(index);
    }

    public void addImage(String path) {
        if (path != null) {
            images.add(path);
        }
    }

    private void getGalleryImages() {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        int index = 0;
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.d("Nombre", absolutePathOfImage);
            index++;
            images.add(absolutePathOfImage);

        }
    }

    public int size() {
        return images.size();
    }
}
