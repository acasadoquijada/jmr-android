package com.example.alejandro.jmr_android.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.alejandro.jmr_android.Gallery;
import com.example.alejandro.jmr_android.R;
import com.example.alejandro.jmr_android.adapter.GalleryAdapter;
import com.example.alejandro.jmr_android.jmr.ResultList;
import com.example.alejandro.jmr_android.jmr.ResultMetadata;
import com.example.alejandro.jmr_android.jmr.SingleColorDescription;
import com.example.alejandro.jmr_android.model.Image;

import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    final private int REQUEST_MEDIA_ACCESS = 2;
    private String userChoosenTask;
    private Gallery imagenesGaleria;
    private Bitmap imagenConsulta;
    private Color colorImagenConsulta;
    private ResultList <ResultMetadata> resultMetadatas;
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter, mAdapter2;
    private RecyclerView recyclerView, recyclerView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_MEDIA_ACCESS);

        imagenesGaleria = new Gallery(this);

        /*
            RecyclerView imagenes resultado
         */
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        images = new ArrayList<>();

        mAdapter = new GalleryAdapter(getApplicationContext(), images);

        pDialog = new ProgressDialog(this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener
                (getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Log.d("HE PULSADO","HE PULSADO");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("images", images);
                        bundle.putInt("position", position);

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "slideshow");
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        /*
            RecyclerView imagenes resultado
         */
        recyclerView2 = (RecyclerView) findViewById(R.id.recycler_view2);

        mAdapter2 = new GalleryAdapter(getApplicationContext(), images);

        pDialog = new ProgressDialog(this);
        RecyclerView.LayoutManager jLayoutManager = new LinearLayoutManager
                (this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView2.setLayoutManager(jLayoutManager);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(mAdapter);

        recyclerView2.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener
                (getApplicationContext(), recyclerView2, new GalleryAdapter.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Log.d("HE PULSADO","HE PULSADO");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("images", images);
                        bundle.putInt("position", position);

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "slideshow");
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        colocarImagenesResultado();
    }

    public void colocarImagenesResultado(){

        pDialog.setMessage("Cargando imágenes...");
        pDialog.show();

        for(int i = 1; i < 100; i++) {
            Image image = new Image();

            image.setName("Imagen " + Integer.toString(i));
            image.setMedium(imagenesGaleria.getImageURI(i));
            image.setLarge(imagenesGaleria.getImageURI(i));
            image.setTimestamp("distancia");

            images.add(image);
        }

        pDialog.hide();
    }

    public void añadirImagenConsulta(){

        // Obtenemos la imagen y la añadimos.
        Log.d("Estoy en: ", "aniadir imagen");

        obtenerImagen();


        // Recorremos la imagenesGaleria y calculamos.
        // calcularDescriptor();

        // Colocamos las imagenes acorde a dicho calculo.
        //colocarImagenesResultado();

    }

    public void calcularDescriptor(){

        int tamanioGaleria = imagenesGaleria.size();

        /* Cojo la imagen consulta y la meto en el array
         de resultados, para poder compararla con las de
         la imagenesGaleria
        */

        double distancia = 0.00;

        ResultMetadata<Double, Bitmap> resultMetada =
                new ResultMetadata(0.0,imagenConsulta);

        SingleColorDescription descriptorImagenConsulta =
                new SingleColorDescription(imagenConsulta);

        resultMetadatas.add(resultMetada);

        Log.d("Estoy en: ", "principio calcular imagenesGaleria");
        for(int i = 1; i < 11; i++){
            Log.d("Estoy en: ", "Cojo imagen imagenesGaleria");
            Bitmap img = imagenesGaleria.getImagen(i);

            Log.d("Estoy en: ", "Calculo descriptor");
            SingleColorDescription descriptor = new SingleColorDescription(img);

            Log.d("Estoy en: ", "Calculo distancia");

            distancia = SingleColorDescription.DefaultComparator
                    (descriptorImagenConsulta, descriptor);

            ResultMetadata<Double, Bitmap> resultMetadaGaleria
                    = new ResultMetadata(distancia, img);

            resultMetadatas.add(resultMetadaGaleria);
       }

        for(int i = 0; i < 11; i++){
            double aux = (Double)resultMetadatas.get(i).getResult();
            Log.d("Imagen " + i," " + Double.toString(aux));
        }

        normalize();

        for(int i = 0; i < 11; i++){
            double aux = (Double)resultMetadatas.get(i).getResult();
            Log.d("Imagen " + i," " + Double.toString(aux));        }

      //  colocarImagenesResultado();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_MEDIA_ACCESS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("HOLA","HOLA");
                  //  colocarImagenesResultado();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Permission denied to read your External storage"
                            , Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void obtenerImagen() {
        /*final CharSequence[] items = {
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
        builder.show();*/
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
        Bitmap imagenConsulta = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imagenConsulta.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

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

    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        imagenConsulta=null;
        if (data != null) {
            try {
                imagenConsulta = MediaStore.Images.Media.
                        getBitmap(getApplicationContext()
                                .getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    public void normalize(){

        Collections.sort(resultMetadatas, new Comparator<ResultMetadata>(){
            public int compare(ResultMetadata r1, ResultMetadata r2) {
                return r1.compareTo(r2);
            }
        });

        double min = (Double)resultMetadatas.get(0).getResult();
        double max = (Double)resultMetadatas.getLast().getResult();

        for(int i = 1; i < 11; i++){
            double newResult;
            double xi = (Double)resultMetadatas.get(i).getResult();

            newResult  = (xi - min)/(max-min);

            resultMetadatas.get(i).setResult(newResult);

        }
    }

}