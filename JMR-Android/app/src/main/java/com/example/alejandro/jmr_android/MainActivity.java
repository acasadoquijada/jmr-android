package com.example.alejandro.jmr_android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect, btnGaleria;
    private ImageButton botonAñadirImagenConsulta;
    private ImageView imageViewConsulta;
    private String userChoosenTask;
    private LinearLayout consultLayout;
    private LinearLayout imagenesConsultaScrollLayout;
    private HorizontalScrollView horizontalScrollView;
    private GridLayout gridLayoutResultado;
    private Galeria galeria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        galeria = new Galeria(this);

        botonAñadirImagenConsulta = (ImageButton)findViewById
                (R.id.botonAñadirImagenConsulta);

        consultLayout = (LinearLayout)findViewById
                (R.id.ConsultLayout);

        horizontalScrollView = (HorizontalScrollView)findViewById
                (R.id.ScrollHorizontalConsulta);

        imagenesConsultaScrollLayout = (LinearLayout)findViewById
                (R.id.ImagenesConsultaScrollLayout);

        gridLayoutResultado = (GridLayout) findViewById
                (R.id.gridLayoutResultado);

        botonAñadirImagenConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                añadirImagenConsulta();
            }
        });

        ArrayList<String> imagenes = galeria.getImagenes();
        for(int i = 0; i < galeria.getTamanioGaleria(); i++){
            Log.i("Imagen " + i + " : ",imagenes.get(i));
        }


    }
/*
    public void añadirImagenResultado(){

        imageViewConsulta = new ImageView(this);

        Bitmap bm = Bitmap.createScaledBitmap(b, 300,300, true);
        //  parms.gravity = Gravity.CENTER;
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
                444,
                444);
        parms.setMargins(20, 20, 20, 20);
        imageViewConsulta.setLayoutParams(parms);
        imageViewConsulta.getLayoutParams().height = 500;
        imageViewConsulta.getLayoutParams().width = 500;
        imageViewConsulta.setImageBitmap(bm);

        imagenesConsultaScrollLayout.addView(imageViewConsulta);
    }
*/
    public void añadirImagenConsulta(){

        // Obtenemos la imagen y la añadimos.
        obtenerImagen();

        // Recorremos la galeria y calculamos.

        // Colocamos las imagenes acorde a dicho calculo.
        colocarImagenesResultado();
    }

    public void colocarImagenesResultado(){

        for(int i = 0; i < 10; i++) {

            imageViewConsulta = new ImageView(this);

            Bitmap bm = Bitmap.createScaledBitmap(galeria.getImagen(0), 200, 200, true);

            imageViewConsulta.setImageBitmap(bm);

            gridLayoutResultado.addView(imageViewConsulta);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void obtenerImagen() {
        final CharSequence[] items = {
                "Obtener desde la cámara",
                "Obtener desde la galería",
                "Cancelar" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Obtener imagen consulta");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(MainActivity.this);

                if (items[item].equals("Obtener desde la cámara")) {
                    userChoosenTask ="Obtener desde la cámara";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Obtener desde la galería")) {
                    userChoosenTask = "Obtener desde la galería";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        addImagenScrollConsulta(thumbnail);
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.
                        getBitmap(getApplicationContext()
                                .getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        addImagenScrollConsulta(bm);
    }

    public void addImagenScrollConsulta(Bitmap b){
        imageViewConsulta = new ImageView(this);

        RelativeLayout rl = new RelativeLayout(this);
        Bitmap bm = Bitmap.createScaledBitmap(b, 300,300, true);
        //  parms.gravity = Gravity.CENTER;
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
                444,
                444);
        parms.setMargins(20, 20, 20, 20);
        imageViewConsulta.setLayoutParams(parms);
        imageViewConsulta.getLayoutParams().height = 500;
        imageViewConsulta.getLayoutParams().width = 500;
        imageViewConsulta.setImageBitmap(bm);
        rl.addView(imageViewConsulta);

        imagenesConsultaScrollLayout.addView(rl);
    }
}