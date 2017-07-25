package com.example.alejandro.jmr_android;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;


public class Galeria extends AppCompatActivity {

    /** The images. */
    private ArrayList<String> images;
    private ImageView imageView;
    String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        imageView = (ImageView) findViewById(R.id.imageView);

        images = getAllShownImagesPath(this);
        Log.i("PepitoPerez", images.get(0));

        Bitmap bm=null;
        try {
            bm = MediaStore.Images.Media.getBitmap(
                    getApplicationContext().getContentResolver(),
                    Uri.parse("file:///" + images.get(0)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(bm);



    }

    private ArrayList<String> getAllShownImagesPath(Activity activity) {
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
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }
}
