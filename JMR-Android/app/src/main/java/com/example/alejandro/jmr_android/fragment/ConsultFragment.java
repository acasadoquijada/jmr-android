package com.example.alejandro.jmr_android.fragment;

/**
 * Created by alejandro on 03/09/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TimingLogger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.alejandro.jmr_android.Gallery;
import com.example.alejandro.jmr_android.HMMDImage;
import com.example.alejandro.jmr_android.R;
import com.example.alejandro.jmr_android.RealPathUtil;
import com.example.alejandro.jmr_android.Utility;
import com.example.alejandro.jmr_android.activity.MainActivity;
import com.example.alejandro.jmr_android.adapter.GalleryAdapter;
import com.example.alejandro.jmr_android.helper.SquareLayout;
import com.example.alejandro.jmr_android.jmr.JMRImage;
import com.example.alejandro.jmr_android.jmr.MPEG7ColorStructure;
import com.example.alejandro.jmr_android.jmr.ResultList;
import com.example.alejandro.jmr_android.jmr.ResultMetadata;
import com.example.alejandro.jmr_android.jmr.SingleColorDescription;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

public class ConsultFragment extends Fragment {

    private static final int CAMERA_REQUEST = 1888;
    private static final int FILE_REQUEST = 2888;
    private Gallery galleryImages;
    private JMRImage imagenConsultaBuena;
    private ResultList<ResultMetadata> resultMetadatas;
    private ArrayList<JMRImage> resultImages, consultImages;
    private ProgressDialog pDialog;
    private GalleryAdapter resultAdapter, consultAdapter;
    private RecyclerView recyclerViewResult, recyclerViewConsult;
    private FloatingActionButton fab_camera, fab_gallery, fab_consult;
    private FloatingActionMenu floatingActionMenu;
    private Uri mImageUri;
    private ProgressDialog dialog;

    public static ConsultFragment newInstance() {
        ConsultFragment fragment = new ConsultFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        galleryImages = new Gallery(getActivity());
        pDialog = new ProgressDialog(getContext());
        /*
        imagenConsultaBuena = new JMRImage();
        imagenConsultaBuena.setPath(galleryImages.getImageURI(4));
        */

        resultImages = new ArrayList<>();
        consultImages = new ArrayList<>();
        resultMetadatas = new ResultList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consult, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initConsultImageView();
        initResultImagesView();

        floatingActionMenu = (FloatingActionMenu)getView().findViewById(R.id.fab);
        fab_camera = (FloatingActionButton) getView().findViewById(R.id.menu_item_camera);
        fab_gallery = (FloatingActionButton) getView().findViewById(R.id.menu_item_gallery);
        fab_consult = (FloatingActionButton) getView().findViewById(R.id.menu_item_make_consult);

        floatingCameraButtonBehaviour();
        floatingGalleryButtonBehaviour();
        floatingConsultButtonBehaviour();

        super.onViewCreated(view, savedInstanceState);
    }

    private void floatingCameraButtonBehaviour(){
        if(fab_camera != null){
            fab_camera.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean result= Utility.checkPermission(getContext());
                    if(result){
                        cameraIntent();
                    }
                    floatingActionMenu.close(false);
                }
            });
        }
    }

    private void floatingGalleryButtonBehaviour(){
        if(fab_gallery != null){
            fab_gallery.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean result = Utility.checkPermission(getContext());
                    if(result){
                        galleryIntent();
                    }
                    floatingActionMenu.close(false);
                }
            });
        }
    }

    private void floatingConsultButtonBehaviour(){
        if(fab_consult != null){
            fab_consult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calcularDescriptor();
                    resultAdapter = new GalleryAdapter(getContext(), resultImages);
                    recyclerViewResult.setAdapter(resultAdapter);
                }
            });
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), FILE_REQUEST);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photo = null;
        try {
            photo = this.createTemporaryFile("picture", ".jpg");
            photo.delete();
        } catch(Exception e) {
            Log.w("cameraIntent exception ",e.toString());
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
            else if(requestCode == FILE_REQUEST)
                onSelectFromGalleryResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data){
        if (data != null) {
            String realPath;

            if (Build.VERSION.SDK_INT < 11) {
                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(getContext(), data.getData());
            } else if (Build.VERSION.SDK_INT < 19) {
                realPath = RealPathUtil.getRealPathFromURI_API11to18(getContext(), data.getData());
            }
            else {
                realPath = RealPathUtil.getRealPathFromURI_API19(getContext(), data.getData());
            }
            Log.d("PATH galeria", realPath);
            addConsultImage(realPath);
        }
    }

    private void onCaptureImageResult(Intent data) {
        getContext().getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = getContext().getContentResolver();
        Bitmap bitmap = null;
        File destination = null;
        try {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            destination = new File(Environment.getExternalStoragePublicDirectory(
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

        galleryImages.addImage(destination.getAbsolutePath());

        addConsultImage(destination.getAbsolutePath());
    }

    public void colocarImagenesResultado(){

        Log.d("COLOCANDO","RESULTADOS");

        if(resultImages.size() > 0){
            resultImages.clear();
        }

        for(int i = 1; i < resultMetadatas.size(); i++) {
            JMRImage JMRImage = new JMRImage();

            JMRImage.setName("Imagen resultado " + Integer.toString(i));
            JMRImage.setPath((String)(resultMetadatas.get(i).getMetadata()));
            Double distance = (Double)(resultMetadatas.get(i).getResult());
            JMRImage.setDistance(Double.toString(distance));

            resultImages.add(JMRImage);
        }

        Log.d("RESULT IMAGE SIZE", Integer.toString(resultImages.size()));
        Log.d("CONSULT IMAGE SIZE", Integer.toString(consultImages.size()));
    }

    private void addConsultImage(String path){
        JMRImage jmrImage = new JMRImage();
        jmrImage.setName("Imagen consulta " + Integer.toString(consultImages.size()+1));
        jmrImage.setPath(path);
        consultImages.add(jmrImage);

        imagenConsultaBuena = jmrImage;
        /*
            Ponerle la estrellita
         */

        consultAdapter.notifyDataSetChanged();
    }

    private void initResultImagesView() {

        recyclerViewResult = (RecyclerView) getView().
                findViewById(R.id.recycler_view);

        resultAdapter = new GalleryAdapter(getContext(), resultImages);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(
                ((MainActivity) getActivity()).getApplicationContext(), 4);

        recyclerViewResult.setLayoutManager(mLayoutManager);
        recyclerViewResult.setItemAnimator(new DefaultItemAnimator());
        recyclerViewResult.setAdapter(resultAdapter);

        recyclerViewResult.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener
                (((MainActivity) getActivity()).getApplicationContext(),
                        recyclerViewResult,
                        new GalleryAdapter.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("images", resultImages);
                                bundle.putInt("position", position);

                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                SlideshowDialogFragment newFragment =
                                        SlideshowDialogFragment.newInstance();
                                newFragment.setArguments(bundle);
                                newFragment.show(ft, "slideshow");
                            }

                            @Override
                            public void onLongClick(View view, int position) {

                            }
                        }));

    }

    private void initConsultImageView() {

        recyclerViewConsult = (RecyclerView) ((MainActivity) getActivity()).findViewById(R.id.recycler_view2);

        consultAdapter = new GalleryAdapter(((MainActivity) getActivity()).getApplicationContext(), consultImages);

        RecyclerView.LayoutManager jLayoutManager = new LinearLayoutManager(
                ((MainActivity) getActivity()), LinearLayoutManager.HORIZONTAL, false);

        recyclerViewConsult.setLayoutManager(jLayoutManager);
        recyclerViewConsult.setItemAnimator(new DefaultItemAnimator());
        recyclerViewConsult.setAdapter(consultAdapter);

        recyclerViewConsult.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener
                (((MainActivity) getActivity()).getApplicationContext(),
                        recyclerViewConsult,
                        new GalleryAdapter.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("images", consultImages);
                                bundle.putInt("position", position);

                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                SlideshowDialogFragment newFragment =
                                        SlideshowDialogFragment.newInstance();
                                newFragment.setArguments(bundle);
                                newFragment.show(ft, "slideshow");
                            }

                            @Override
                            public void onLongClick(View view, int position) {
                                SquareLayout squareLayout = (SquareLayout) view;
                                Log.d("Soy la imagen",Integer.toString(position));

                                ImageView imageIconSelected = (ImageView)
                                        squareLayout.findViewById(R.id.selectedIcon);

                                int visibility = imageIconSelected.getVisibility();

                                if(visibility == ImageView.INVISIBLE){
                                    imageIconSelected.setVisibility(ImageView.VISIBLE);
                                }
                                else if(visibility == ImageView.VISIBLE){
                                    imageIconSelected.setVisibility(ImageView.INVISIBLE);
                                }
                            }
                        }));
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

    public void calcularDescriptor(){

        if(imagenConsultaBuena != null){
            int tamanioGaleria = galleryImages.size();

            /* Cojo la imagen consulta y la meto en el array
             de resultados, para poder compararla con las de
             la galeria
            */
            long startTime = System.currentTimeMillis();

            Log.d("Estoy en: ", "caca calcular galeria");

            double distancia = 0.00;

            ResultMetadata<Double, String> resultMetada =
                    new ResultMetadata(0.0,imagenConsultaBuena.getPath());

            Bitmap bitmapConsultImage = galleryImages.getImagen(imagenConsultaBuena.getPath());

            SingleColorDescription descriptorImagenConsulta =
                    new SingleColorDescription(bitmapConsultImage);

            if(resultMetadatas.size() > 0){
                resultMetadatas.clear();
            }

            resultMetadatas.add(resultMetada);

            int ini = 0;
            int fin = 800;

            Log.d("Descriptor", "comienzo a calcular");
            for(int i = ini; i < fin; i++){
                Log.d("Descriptor: ", "imagen " + Integer.toString(i));
                Log.d("Path", galleryImages.getImageURI(i));
                Bitmap img = galleryImages.getImagen(i);

                if(img != null){
                    // SingleColorDescription descriptor = new SingleColorDescription(img);
                    SingleColorDescription descriptor = new SingleColorDescription(img);

                    distancia = descriptor.compare(descriptorImagenConsulta);

                    ResultMetadata<Double, String> resultMetadaGaleria
                            = new ResultMetadata(distancia, galleryImages.getImageURI(i));

                    resultMetadatas.add(resultMetadaGaleria);
                }

            }

            long endTime = System.currentTimeMillis();

            long MethodeDuration = (endTime - startTime);

            String formatTime = String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(MethodeDuration),
                    TimeUnit.MILLISECONDS.toSeconds(MethodeDuration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(MethodeDuration))
            );

            Log.d("Tiempo descriptor", formatTime );
            normalizeResult();

            for (int i = 1; i < resultMetadatas.size(); i++) {
                double newResult;
                double xi = (Double) resultMetadatas.get(i).getResult();

                Log.d("Distancia imagen " + Integer.toString(i) + " ", Double.toString(xi));

            }
            colocarImagenesResultado();

        }

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

    private void normalizeResult() {

        Collections.sort(resultMetadatas, new Comparator<ResultMetadata>() {
            public int compare(ResultMetadata r1, ResultMetadata r2) {
                return r1.compareTo(r2);
            }
        });

        double min = (Double) resultMetadatas.get(0).getResult();
        double max = (Double) resultMetadatas.getLast().getResult();

        for (int i = 1; i < resultMetadatas.size(); i++) {
            double newResult;
            double xi = (Double) resultMetadatas.get(i).getResult();

            newResult = (xi - min) / (max - min);

            resultMetadatas.get(i).setResult(newResult);

        }
    }
}

