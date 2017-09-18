package com.jmr_android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jmr_android.fragment.AditionalFragment;
import com.jmr_android.fragment.ConsultFragment;
import com.example.alejandro.jmr_android.R;
import com.jmr_android.fragment.SettingsFragment;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class MainActivity extends AppCompatActivity {

    private BottomNavigation bottomNavigation;
    private ConsultFragment consultFragment;
    private SettingsFragment settingsFragment;
    private AditionalFragment aditionalFragment;
    public static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        act = this;

        consultFragment = ConsultFragment.newInstance();

        settingsFragment = new SettingsFragment();

        aditionalFragment = new AditionalFragment();

        bottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);

        bottomNavigation.setOnMenuItemClickListener(new BottomNavigation.OnMenuItemSelectionListener() {
            @Override
            public void onMenuItemSelect(@IdRes int i, int i1, boolean b) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i1) {
                    case 0:
                        transaction.replace(R.id.main_fragment, consultFragment, "a");
                        transaction.addToBackStack("a");
                        transaction.commit();

                        break;

                    case 1:

                        transaction.replace(R.id.main_fragment, settingsFragment, "A");
                        transaction.addToBackStack("b");
                        transaction.commit();
                        break;

                    case 2:
                        transaction.replace(R.id.main_fragment, aditionalFragment, "C");
                        transaction.addToBackStack("c");
                        transaction.commit();
                        break;
                }

            }

            @Override
            public void onMenuItemReselect(@IdRes int i, int i1, boolean b) {
                if (i1 == 0) {
                    consultFragment.consult();
                }
            }
        });


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, consultFragment);
        transaction.commit();

    }

    public void setActiveDescriptor(int descriptor){
        consultFragment.setActiveDescriptor(descriptor);
    }

    public void setImageConsultNumber(int number){
        consultFragment.setImageConsultNumber(number);
    }

    public void setAllImageConsult(){
        consultFragment.setAllImageConsult();
    }

    public void deleteDataBase(){
        consultFragment.deleteDataBase();
    }

    public void calculateDataBase(){
        consultFragment.calculateDataBase();
    }

}