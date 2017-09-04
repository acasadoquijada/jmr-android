package com.example.alejandro.jmr_android.fragment;

/**
 * Created by alejandro on 03/09/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alejandro.jmr_android.Gallery;
import com.example.alejandro.jmr_android.R;
import com.example.alejandro.jmr_android.Utility;
import com.example.alejandro.jmr_android.activity.MainActivity;
import com.example.alejandro.jmr_android.adapter.GalleryAdapter;
import com.example.alejandro.jmr_android.jmr.ResultList;
import com.example.alejandro.jmr_android.jmr.ResultMetadata;
import com.example.alejandro.jmr_android.model.Image;
import com.github.clans.fab.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class ConsultFragment extends Fragment {

    private static final int CAMERA_REQUEST = 1888;
    final private int REQUEST_MEDIA_ACCESS = 2;
    private String userChoosenTask;
    private Gallery imagenesGaleria;
    private Bitmap imagenConsulta;
    private Color colorImagenConsulta;
    private ResultList<ResultMetadata> resultMetadatas;
    private ArrayList<Image> images, imageConsulta;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter, mAdapter2;
    private RecyclerView recyclerView, recyclerView2;
    private FloatingActionButton fab_camera;
    private Uri mImageUri;


    public static ConsultFragment newInstance() {
        ConsultFragment fragment = new ConsultFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagenesGaleria = ((MainActivity) getActivity()).getGallery();
        images = new ArrayList<>();
        imageConsulta = new ArrayList<>();
        colocarImagenesResultado();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consult, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setConsultImage();
        setResultImage();

        fab_camera = (FloatingActionButton) getView().
                findViewById(R.id.menu_item_camera);

        fab_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean result= Utility.checkPermission(getContext());

                if(result){
                    cameraIntent();
                }

            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private File createTemporaryFile(String part, String ext) throws Exception {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photo = null;
        try
        {
            // place where to store camera taken picture
            photo = this.createTemporaryFile("picture", ".jpg");
            photo.delete();
        }
        catch(Exception e) {

        }

        mImageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

        startActivityForResult(intent, CAMERA_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        getContext().getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = getContext().getContentResolver();
        Bitmap bitmap;
        try
        {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


            File destination = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).getPath() + "/Camera",

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

        } catch (Exception e) {
            Log.d("Failed to load", e.toString());
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent();

                } else {
                    //code for deny
                }
                break;
        }
    }

    private void setConsultImage() {

        /*
            RecyclerView imagenes resultado
         */

        recyclerView = (RecyclerView) getView().
                findViewById(R.id.recycler_view);

        mAdapter = new GalleryAdapter(((MainActivity) getActivity()).getApplicationContext(), images);

        pDialog = new ProgressDialog(((MainActivity) getActivity()).getApplicationContext());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(
                ((MainActivity) getActivity()).getApplicationContext(), 4);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener
                (((MainActivity) getActivity()).getApplicationContext(),
                        recyclerView,
                        new GalleryAdapter.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Log.d("HE PULSADO", "HE PULSADO");
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("images", images);
                                bundle.putInt("position", position);

                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                                newFragment.setArguments(bundle);
                                newFragment.show(ft, "slideshow");
                            }

                            @Override
                            public void onLongClick(View view, int position) {

                            }
                        }));

    }

    private void setResultImage() {

        /*
            RecyclerView imagenes resultado
         */
        recyclerView2 = (RecyclerView) ((MainActivity) getActivity()).findViewById(R.id.recycler_view2);

        mAdapter2 = new GalleryAdapter(((MainActivity) getActivity()).getApplicationContext(), images);

        pDialog = new ProgressDialog(((MainActivity) getActivity()).getApplicationContext());

        RecyclerView.LayoutManager jLayoutManager = new LinearLayoutManager(
                ((MainActivity) getActivity()), LinearLayoutManager.HORIZONTAL, false);

        recyclerView2.setLayoutManager(jLayoutManager);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(mAdapter);

        recyclerView2.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener
                (((MainActivity) getActivity()).getApplicationContext(),
                        recyclerView2,
                        new GalleryAdapter.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Log.d("HE PULSADO", "HE PULSADO");
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("images", images);
                                bundle.putInt("position", position);

                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                                newFragment.setArguments(bundle);
                                newFragment.show(ft, "slideshow");
                            }

                            @Override
                            public void onLongClick(View view, int position) {

                            }
                        }));
    }

    public void colocarImagenesResultado(){


        for(int i = 1; i < 100; i++) {
            Image image = new Image();

            image.setName("Imagen " + Integer.toString(i));
            image.setMedium(imagenesGaleria.getImageURI(i));
            image.setLarge(imagenesGaleria.getImageURI(i));
            image.setTimestamp("distancia");

            images.add(image);
        }
    }

}