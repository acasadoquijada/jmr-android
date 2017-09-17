package com.jmr_android.fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.jmr_android.activity.MainActivity;
import com.jmr_android.adapter.GalleryAdapter;
import com.jmr_android.helper.DBHelper;
import com.jmr_android.helper.GalleryHelper;
import com.jmr_android.helper.RealPathUtil;
import com.jmr_android.helper.SquareLayout;
import com.jmr_android.helper.Utility;
import com.jmr_android.jmr.JMRImage;
import com.jmr_android.jmr.MPEG7ColorStructure;
import com.jmr_android.jmr.MediaDescriptor;
import com.jmr_android.jmr.ResultList;
import com.jmr_android.jmr.ResultMetadata;
import com.jmr_android.jmr.SingleColorDescription;
import com.example.alejandro.jmr_android.R;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ConsultFragment extends Fragment {

    private static final int CAMERA_REQUEST = 1888;
    private static final int FILE_REQUEST = 2888;
    public static final int SINGLE_COLOR_DESCRIPTOR = 0;
    public static final int MPEG7_COLOR_STRUCTURE = 1;
    private int activeDescriptor;
    private int imageNumberConsult;
    private GalleryHelper galleryImages;
    private JMRImage consultImage;
    private ResultList<ResultMetadata> resultMetadatas;
    private ArrayList<JMRImage> resultImages, consultImages;
    private ArrayList<MediaDescriptor> descriptors;
    private SingleColorDescription singleColorDescription;
    private MPEG7ColorStructure mpeg7ColorStructure;
    private GalleryAdapter resultAdapter, consultAdapter;
    private RecyclerView recyclerViewResult, recyclerViewConsult;
    private FloatingActionButton fabCamera, fabGallery;
    private FloatingActionMenu floatingActionMenu;
    private Uri mImageUri;
    private DBHelper descriptorBD;
    private ProgressDialog progress;
    private View previousView;
    private JMRImage previousConsultImage;
    private RecyclerView.LayoutManager jLayoutManager;



    public static ConsultFragment newInstance() {
        ConsultFragment fragment = new ConsultFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean result = Utility.checkPermission(getContext());
        if (result) {
            galleryImages = new GalleryHelper(getActivity());
        }

        imageNumberConsult = 200;
        activeDescriptor = SINGLE_COLOR_DESCRIPTOR;
        descriptorBD = new DBHelper(getContext());
        //deleteDataBase();
        descriptors = new ArrayList<>();
        SingleColorDescription singleColorDescription = null;
        MPEG7ColorStructure mpeg7ColorStructure = null;

        descriptors.add((MediaDescriptor) singleColorDescription);
        descriptors.add((MediaDescriptor) mpeg7ColorStructure);

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

        floatingActionMenu = (FloatingActionMenu) getView().findViewById(R.id.fab);
        fabCamera = (FloatingActionButton) getView().findViewById(R.id.menu_item_camera);
        fabGallery = (FloatingActionButton) getView().findViewById(R.id.menu_item_gallery);

        floatingCameraButtonBehaviour();
        floatingGalleryButtonBehaviour();

        super.onViewCreated(view, savedInstanceState);
    }

    private void floatingCameraButtonBehaviour() {
        if (fabCamera != null) {
            fabCamera.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    boolean result = Utility.checkPermission(getContext());
                    if (result) {
                        cameraIntent();
                    } else {
                        Log.d("PERMISOS", "DENEGADO");
                    }
                    floatingActionMenu.close(false);
                }
            });
        }
    }

    private void floatingGalleryButtonBehaviour() {
        if (fabGallery != null) {
            fabGallery.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean result = Utility.checkPermission(getContext());
                    if (result) {
                        galleryIntent();
                    }
                    floatingActionMenu.close(false);
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
        } catch (Exception e) {
            Log.w("cameraIntent exception ", e.toString());
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
            else if (requestCode == FILE_REQUEST)
                onSelectFromGalleryResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            String realPath;

            if (Build.VERSION.SDK_INT < 11) {
                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(getContext(), data.getData());
            } else if (Build.VERSION.SDK_INT < 19) {
                realPath = RealPathUtil.getRealPathFromURI_API11to18(getContext(), data.getData());
            } else {
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
        String path = null;
        File destination = null;
        try {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            destination = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).getPath() + "/Camera",
                    System.currentTimeMillis() + ".jpg");

            path = destination.getAbsolutePath();

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

        galleryImages.addImage(path);

        addConsultImage(path);
    }

    private void colocarImagenesResultado() {

        Log.d("COLOCANDO", "RESULTADOS");

        if (resultImages.size() > 0) {
            resultImages.clear();
        }

        for (int i = 0; i < resultMetadatas.size(); i++) {
            JMRImage JMRImage = new JMRImage();

            JMRImage.setName("Imagen resultado " + Integer.toString(i));
            JMRImage.setPath((String) (resultMetadatas.get(i).getMetadata()));
            Double distance = (Double) (resultMetadatas.get(i).getResult());
            JMRImage.setDistance(Double.toString(distance));

            resultImages.add(JMRImage);
        }

        resultAdapter.notifyDataSetChanged();

        Log.d("RESULT IMAGE SIZE", Integer.toString(resultImages.size()));
        Log.d("CONSULT IMAGE SIZE", Integer.toString(consultImages.size()));
    }

    private void addConsultImage(String path) {
        JMRImage jmrImage = new JMRImage();
        jmrImage.setName("Imagen consulta " + Integer.toString(consultImages.size() + 1));
        jmrImage.setPath(path);
        consultImages.add(jmrImage);

        consultImage = jmrImage;

        previousConsultImage = jmrImage;

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

        recyclerViewConsult = (RecyclerView) (getActivity()).findViewById(R.id.recycler_view2);

        consultAdapter = new GalleryAdapter(( getActivity()).getApplicationContext(), consultImages);

        jLayoutManager = new LinearLayoutManager(
                (getActivity()), LinearLayoutManager.HORIZONTAL, false);

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
                                //    Log.d("Soy la imagen",Integer.toString(position));

                                ImageView imageIconSelected = (ImageView)
                                        squareLayout.findViewById(R.id.selectedIcon);

                                int visibility = imageIconSelected.getVisibility();

                                if (visibility == ImageView.INVISIBLE) {
                                    imageIconSelected.setVisibility(ImageView.VISIBLE);
                                } else if (visibility == ImageView.VISIBLE) {
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

    public void consult() {
        if (consultImage != null) {
            progress = ProgressDialog.show(getContext(), "Consulta",
                    "Realizando consulta", true);
            CountDownLatch latch = new CountDownLatch(1);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    int tamanioGaleria = galleryImages.size();

                    long startTime = System.currentTimeMillis();

                    double distance = 0.00;

                    Bitmap image = null;
                    if (descriptorBD.getData(
                            consultImage.getPath(), activeDescriptor) != null) {

                        int[] value = descriptorBD.getData(consultImage.getPath(), activeDescriptor);

                        switch (activeDescriptor) {
                            case SINGLE_COLOR_DESCRIPTOR:
                                singleColorDescription = new SingleColorDescription(value);
                                break;
                            case MPEG7_COLOR_STRUCTURE:
                                mpeg7ColorStructure = new MPEG7ColorStructure(value);
                                break;
                        }
                    } else {
                        image = galleryImages.getImagen(consultImage.getPath());

                        int[] values = null;
                        switch (activeDescriptor) {
                            case SINGLE_COLOR_DESCRIPTOR:
                                singleColorDescription = new SingleColorDescription(image);
                                values = singleColorDescription.getColor();
                                break;
                            case MPEG7_COLOR_STRUCTURE:
                                mpeg7ColorStructure = new MPEG7ColorStructure(image);
                                values = mpeg7ColorStructure.getHisto();
                                break;
                        }

                        descriptorBD.setData(consultImage.getPath(), values, activeDescriptor);
                    }

                    if (resultMetadatas.size() > 0) {
                        resultMetadatas.clear();
                    }
                    for (int i = 0; i < imageNumberConsult; i++) {

                        Log.d("Descriptor: ", "imagen " + Integer.toString(i));
                        // Log.d("Path", galleryImages.getImageURI(i));

                        Bitmap img = galleryImages.getImagen(i);

                        ResultMetadata<Double, String> resultMetadaGaleria;

                        SingleColorDescription singleColorDescriptionConsult = null;
                        MPEG7ColorStructure mpeg7ColorStructureConsult = null;

                        if (img != null) {
                            String imagePath = galleryImages.getImageURI(i);

                            if (descriptorBD.getData(
                                    imagePath, activeDescriptor) != null) {
                                Log.d("AQUI", "AQUI");

                                int[] value = descriptorBD.getData(imagePath, activeDescriptor);

                                switch (activeDescriptor) {
                                    case SINGLE_COLOR_DESCRIPTOR:
                                        singleColorDescriptionConsult = new SingleColorDescription(value);
                                        break;
                                    case MPEG7_COLOR_STRUCTURE:
                                        mpeg7ColorStructureConsult = new MPEG7ColorStructure(value);
                                        break;
                                }

                            } else {
                                Log.d("NORL", "AQUI");
                                image = galleryImages.getImagen(imagePath);

                                int[] values = null;
                                switch (activeDescriptor) {
                                    case SINGLE_COLOR_DESCRIPTOR:
                                        singleColorDescriptionConsult = new SingleColorDescription(image);
                                        values = singleColorDescriptionConsult.getColor();
                                        break;
                                    case MPEG7_COLOR_STRUCTURE:
                                        mpeg7ColorStructureConsult = new MPEG7ColorStructure(image);
                                        values = mpeg7ColorStructureConsult.getHisto();
                                        break;
                                }

                                descriptorBD.setData(imagePath, values, activeDescriptor);
                            }

                            switch (activeDescriptor) {
                                case SINGLE_COLOR_DESCRIPTOR:
                                    distance = singleColorDescriptionConsult.compare
                                            (singleColorDescription);
                                    break;
                                case MPEG7_COLOR_STRUCTURE:
                                    distance = mpeg7ColorStructureConsult.compare
                                            (mpeg7ColorStructure);
                                    break;
                            }

                            resultMetadaGaleria
                                    = new ResultMetadata(distance, imagePath);

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

                    Log.d("Tiempo descriptor", formatTime);
                    normalizeResult();

                    for (int i = 1; i < resultMetadatas.size(); i++) {

                        double xi = (Double) resultMetadatas.get(i).getResult();

                        Log.d("Distancia imagen " + Integer.toString(i) + " ", Double.toString(xi));

                    }
                    progress.dismiss();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            colocarImagenesResultado();
                        }
                    });

                }
            });

            t.start();

        }

    }

    private File createTemporaryFile(String part, String ext) throws Exception {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        if (!tempDir.exists()) {
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

    public void setActiveDescriptor(int descriptor) {
        if (descriptor == ConsultFragment.MPEG7_COLOR_STRUCTURE ||
                descriptor == ConsultFragment.SINGLE_COLOR_DESCRIPTOR) {

            activeDescriptor = descriptor;
        } else {
            Log.e("Descriptor", "descriptor erroneo");
        }
    }

    public void calculateDataBase(){

        SingleColorDescription descriptorSingleColor;
        MPEG7ColorStructure descriptorMPEG7;

        int i = 0;

        Log.d("BD", "Calculando BD");
        for(i = 0; i < imageNumberConsult; i++){

            Bitmap img = galleryImages.getImagen(i);
            String path = galleryImages.getImageURI(i);
            int values[];
            if(img != null){
                if(descriptorBD.getData(path,SINGLE_COLOR_DESCRIPTOR) == null){
                    descriptorSingleColor = new SingleColorDescription(img);
                    values = descriptorSingleColor.getColor();
                    descriptorBD.setData(path,values,SINGLE_COLOR_DESCRIPTOR);
                }

                if(descriptorBD.getData(path, MPEG7_COLOR_STRUCTURE) == null){
                    descriptorMPEG7 = new MPEG7ColorStructure(img);
                    values = descriptorMPEG7.getHisto();
                    descriptorBD.setData(path,values,MPEG7_COLOR_STRUCTURE);
                }
            }
        }
    }

    public void setImageConsultNumber(int number){
        if( (number > 0) && (number < galleryImages.size()) ){
            imageNumberConsult = number;
        } else {
            Log.e("Numero imagenes", "numero erroneo");
        }
    }

    public void setAllImageConsult(){
        imageNumberConsult = galleryImages.size();
    }

    public void deleteDataBase(){
        descriptorBD.clearDatabase();
    }

}

