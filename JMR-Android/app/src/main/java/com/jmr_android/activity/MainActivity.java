package com.jmr_android.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jmr_android.fragment.ConsultFragment;
import com.example.alejandro.jmr_android.R;
import com.jmr_android.fragment.SettingsFragment;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class MainActivity extends AppCompatActivity {

    private BottomNavigation bottomNavigation;
    private ConsultFragment consultFragment;
    private SettingsFragment settingsFragment;

    private boolean isConsultFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        consultFragment = ConsultFragment.newInstance();

        isConsultFragment = true;

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
                        Log.d("ESTOY", "1");

                        transaction.replace(R.id.main_fragment, consultFragment, "a");
                        transaction.addToBackStack("a");
                        transaction.commit();

                        if (isConsultFragment) {
                            Log.d("ESTOY", "CAL");
                        }
                        isConsultFragment = true;
                        break;

                    case 1:
                        Log.d("ESTOY", "2");

                        transaction.replace(R.id.main_fragment, settingsFragment, "A");
                        transaction.addToBackStack("b");
                        transaction.commit();
                        isConsultFragment = false;
                        break;
                      /*  transaction.replace(R.id.main_fragment, settingFragment, "b");

                        break;*/

                }

            }

            @Override
            public void onMenuItemReselect(@IdRes int i, int i1, boolean b) {
                // Do something

                if (i1 == 0) {
                    consultFragment.calculateDescriptor();
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

    public void setActiveDescriptor(int descriptor) {

        if (descriptor == ConsultFragment.MPEG7_COLOR_STRUCTURE ||
                descriptor == ConsultFragment.SINGLE_COLOR_DESCRIPTOR) {

            consultFragment.setActiveDescriptor(descriptor);
        } else {
            Log.e("Descriptor", "descriptor erroneo");
        }
    }


}