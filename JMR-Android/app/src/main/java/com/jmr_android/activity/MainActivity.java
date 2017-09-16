package com.jmr_android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.jmr_android.DialogPreference.CalculateBDDialogPreference;
import com.jmr_android.DialogPreference.DeleteBDDialogPreference;
import com.jmr_android.fragment.ConsultFragment;
import com.example.alejandro.jmr_android.R;
import com.jmr_android.fragment.SettingsFragment;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class MainActivity extends AppCompatActivity {

    private BottomNavigation bottomNavigation;
    private ConsultFragment consultFragment;
    private SettingsFragment settingsFragment;
    public static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        act = this;


        consultFragment = ConsultFragment.newInstance();

        settingsFragment = new SettingsFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);

        bottomNavigation.setOnMenuItemClickListener(new BottomNavigation.OnMenuItemSelectionListener() {
            @Override
            public void onMenuItemSelect(@IdRes int i, int i1, boolean b) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                clearBackStack();
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

                }

            }

            @Override
            public void onMenuItemReselect(@IdRes int i, int i1, boolean b) {
                // Do something

                if (i1 == 0) {
                    consultFragment.consult();
                }
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, consultFragment);
        transaction.commit();
    }

    private void clearBackStack() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.getBackStackEntryCount() != 0) {
            Log.d("LIMMPIO", "LIMPIO");
            fragmentManager.popBackStackImmediate();
        }
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