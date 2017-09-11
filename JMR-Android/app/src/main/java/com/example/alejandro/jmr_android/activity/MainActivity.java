package com.example.alejandro.jmr_android.activity;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.alejandro.jmr_android.Gallery;
import com.example.alejandro.jmr_android.fragment.ConsultFragment;
import com.example.alejandro.jmr_android.R;
import com.example.alejandro.jmr_android.adapter.GalleryAdapter;
import com.example.alejandro.jmr_android.fragment.SettingsFragment;
import com.example.alejandro.jmr_android.fragment.StatisticsFragment;
import com.example.alejandro.jmr_android.jmr.JMRImage;
import com.example.alejandro.jmr_android.jmr.ResultList;
import com.example.alejandro.jmr_android.jmr.ResultMetadata;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class MainActivity extends AppCompatActivity {

    private BottomNavigation bottomNavigation;
    private ConsultFragment consultFragment;
    private SettingsFragment settingsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        consultFragment = ConsultFragment.newInstance();

        settingsFragment = new SettingsFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigation = (BottomNavigation)findViewById(R.id.BottomNavigation);

        bottomNavigation.setOnMenuItemClickListener(new BottomNavigation.OnMenuItemSelectionListener() {
            @Override
            public void onMenuItemSelect(@IdRes int i, int i1, boolean b) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                clearBackStack();
                switch (i1){
                    case 0:
                        transaction.replace(R.id.main_fragment, consultFragment, "a");
                        transaction.addToBackStack("a");
                        transaction.commit();
                        break;

                    case 1:
                        transaction.replace(R.id.main_fragment,settingsFragment,"A");
                        transaction.addToBackStack("b");
                        transaction.commit();
                        break;
                      /*  transaction.replace(R.id.main_fragment, settingFragment, "b");

                        break;*/

                }

            }

            @Override
            public void onMenuItemReselect(@IdRes int i, int i1, boolean b) {
                // Do something
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, consultFragment);
        transaction.commit();
    }

    private void clearBackStack() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.getBackStackEntryCount() != 0) {
            Log.d("LIMMPIO","LIMPIO");
            fragmentManager.popBackStackImmediate();
        }
    }

    public void setActiveDescriptor(int descriptor){

        if(descriptor == ConsultFragment.MPEG7_COLOR_STRUCTURE ||
                descriptor == ConsultFragment.SINGLE_COLOR_DESCRIPTOR) {

            consultFragment.setActiveDescriptor(descriptor);
        }

        else{
            Log.e("Descriptor", "descriptor erroneo");
        }
    }


}