package com.example.alejandro.jmr_android.fragment;

/**
 * Created by alejandro on 03/09/2017.
 */

import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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

import com.example.alejandro.jmr_android.Gallery;
import com.example.alejandro.jmr_android.R;
import com.example.alejandro.jmr_android.activity.MainActivity;
import com.example.alejandro.jmr_android.adapter.GalleryAdapter;
import com.example.alejandro.jmr_android.jmr.ResultList;
import com.example.alejandro.jmr_android.jmr.ResultMetadata;
import com.example.alejandro.jmr_android.model.Image;

import java.util.ArrayList;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class ConsultFragment extends Fragment {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    final private int REQUEST_MEDIA_ACCESS = 2;
    private String userChoosenTask;
    private Gallery imagenesGaleria;
    private Bitmap imagenConsulta;
    private Color colorImagenConsulta;
    private ResultList<ResultMetadata> resultMetadatas;
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter, mAdapter2;
    private RecyclerView recyclerView, recyclerView2;
    private BottomNavigation bottomNavigation;
    private ConsultFragment consultFragment;
    private SettingFragment settingFragment;
    private StatisticsFragment statisticsFragment;

    public static ConsultFragment newInstance() {
        ConsultFragment fragment = new ConsultFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagenesGaleria = ((MainActivity) getActivity()).getGallery();
        images = new ArrayList<>();
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
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        Log.d("SOY", "VISIBLE");

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